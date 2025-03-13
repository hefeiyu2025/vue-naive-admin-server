package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.common.exception.ServiceException;
import com.naiveadmin.server.entity.SysDept;
import com.naiveadmin.server.mapper.SysDeptMapper;
import com.naiveadmin.server.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public List<SysDept> listDeptTree(String name, Boolean status) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        // 根据部门名称模糊查询
        if (StringUtils.hasText(name)) {
            queryWrapper.like(SysDept::getName, name);
        }
        // 根据状态查询
        if (status != null) {
            queryWrapper.eq(SysDept::getStatus, status);
        }
        // 按排序号升序排列
        queryWrapper.orderByAsc(SysDept::getOrderNum);

        List<SysDept> deptList = list(queryWrapper);
        return buildDeptTree(deptList);
    }

    @Override
    public List<SysDept> listDepts(SysDept dept) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        
        // 部门名称模糊查询
        if (StringUtils.hasText(dept.getName())) {
            queryWrapper.like(SysDept::getName, dept.getName());
        }
        
        // 部门编码模糊查询
        if (StringUtils.hasText(dept.getCode())) {
            queryWrapper.like(SysDept::getCode, dept.getCode());
        }
        
        // 状态查询
        if (dept.getStatus() != null) {
            queryWrapper.eq(SysDept::getStatus, dept.getStatus());
        }
        
        // 排序
        queryWrapper.orderByAsc(SysDept::getOrderNum);
        
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDept(SysDept dept) {
        // 检查部门名称是否唯一
        if (!checkDeptNameUnique(dept.getName(), dept.getParentId(), null)) {
            throw new ServiceException("部门名称已存在");
        }
        
        // 检查部门编码是否唯一
        if (!checkDeptCodeUnique(dept.getCode(), null)) {
            throw new ServiceException("部门编码已存在");
        }

        // 设置部门路径
        dept.setPath(buildDeptPath(dept.getParentId()));
        
        return save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(SysDept dept) {
        // 检查部门是否存在
        SysDept existDept = getById(dept.getId());
        if (existDept == null) {
            throw new ServiceException("部门不存在");
        }

        // 检查是否为上级部门的子部门
        if (isChild(dept.getId(), dept.getParentId())) {
            throw new ServiceException("上级部门不能是当前部门的子部门");
        }

        // 检查部门名称是否唯一
        if (!checkDeptNameUnique(dept.getName(), dept.getParentId(), dept.getId())) {
            throw new ServiceException("部门名称已存在");
        }

        // 检查部门编码是否唯一
        if (!checkDeptCodeUnique(dept.getCode(), dept.getId())) {
            throw new ServiceException("部门编码已存在");
        }

        // 如果父部门发生变化，更新部门路径
        if (!Objects.equals(existDept.getParentId(), dept.getParentId())) {
            dept.setPath(buildDeptPath(dept.getParentId()));
            // 更新子部门路径
            updateChildrenDeptPath(dept);
        }

        return updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDept(Long id) {
        // 检查是否存在子部门
        if (count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id)) > 0) {
            throw new ServiceException("存在子部门，不能删除");
        }
        return removeById(id);
    }

    @Override
    public List<Long> getSubDeptIds(Long deptId) {
        List<Long> deptIds = new ArrayList<>();
        deptIds.add(deptId);
        getSubDeptIdsRecursive(deptId, deptIds);
        return deptIds;
    }

    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(SysDept::getId).collect(Collectors.toList());

        for (SysDept dept : depts) {
            // 如果是顶级节点，遍历该节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }

        if (returnList.isEmpty()) {
            returnList = depts;
        }

        return returnList;
    }

    @Override
    public String buildDeptPath(Long parentId) {
        if (parentId == null || parentId == 0) {
            return "0";
        }
        
        SysDept parent = getById(parentId);
        if (parent == null) {
            throw new ServiceException("父部门不存在");
        }
        
        return parent.getPath() + "," + parent.getId();
    }

    @Override
    public boolean checkDeptCodeUnique(String code, Long deptId) {
        if (!StringUtils.hasText(code)) {
            return true;
        }
        long count = count(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getCode, code)
                .ne(deptId != null, SysDept::getId, deptId));
        return count == 0;
    }

    @Override
    public boolean checkDeptNameUnique(String name, Long parentId, Long deptId) {
        if (!StringUtils.hasText(name)) {
            return true;
        }
        long count = count(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getName, name)
                .eq(SysDept::getParentId, parentId)
                .ne(deptId != null, SysDept::getId, deptId));
        return count == 0;
    }

    @Override
    public SysDept getDeptById(Long id) {
        return getById(id);
    }

    @Override
    public List<Long> getUserDataScope(Long userId) {
        // TODO: 实现用户数据权限范围查询
        // 这里简单返回空列表，实际应根据用户角色查询其可访问的部门ID
        return new ArrayList<>();
    }

    /**
     * 递归获取子部门ID
     */
    private void getSubDeptIdsRecursive(Long deptId, List<Long> deptIds) {
        List<SysDept> subDepts = list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, deptId));
        for (SysDept subDept : subDepts) {
            deptIds.add(subDept.getId());
            getSubDeptIdsRecursive(subDept.getId(), deptIds);
        }
    }

    /**
     * 递归构建部门树
     */
    private void recursionFn(List<SysDept> list, SysDept dept) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, dept);
        dept.setChildren(childList);
        for (SysDept child : childList) {
            if (hasChild(list, child)) {
                recursionFn(list, child);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept dept) {
        return list.stream()
                .filter(d -> Objects.equals(d.getParentId(), dept.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept dept) {
        return getChildList(list, dept).size() > 0;
    }

    /**
     * 判断是否为该部门的子部门
     */
    private boolean isChild(Long deptId, Long parentId) {
        if (parentId == null || parentId == 0) {
            return false;
        }
        return getSubDeptIds(deptId).contains(parentId);
    }

    /**
     * 更新子部门路径
     */
    private void updateChildrenDeptPath(SysDept dept) {
        List<SysDept> children = list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, dept.getId()));
        
        if (!children.isEmpty()) {
            children.forEach(child -> {
                child.setPath(dept.getPath() + "," + dept.getId());
                updateById(child);
                updateChildrenDeptPath(child);
            });
        }
    }
}