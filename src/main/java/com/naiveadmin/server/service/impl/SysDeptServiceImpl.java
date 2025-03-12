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

import java.time.LocalDateTime;
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
    public List<SysDept> getDeptTree() {
        // 获取所有部门列表
        List<SysDept> deptList = this.list(new LambdaQueryWrapper<SysDept>()
                .orderByAsc(SysDept::getOrderNum));
        
        // 构建树形结构
        return buildDeptTree(deptList);
    }

    @Override
    public List<SysDept> listDepts(SysDept dept) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        
        // 部门名称模糊查询
        if (StringUtils.hasText(dept.getName())) {
            wrapper.like(SysDept::getName, dept.getName());
        }
        
        // 部门编码模糊查询
        if (StringUtils.hasText(dept.getCode())) {
            wrapper.like(SysDept::getCode, dept.getCode());
        }
        
        // 状态查询
        if (dept.getStatus() != null) {
            wrapper.eq(SysDept::getStatus, dept.getStatus());
        }
        
        // 排序
        wrapper.orderByAsc(SysDept::getOrderNum);
        
        return this.list(wrapper);
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
        
        // 设置默认值
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        dept.setStatus(true);

        return this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(SysDept dept) {
        // 检查部门名称是否唯一
        if (!checkDeptNameUnique(dept.getName(), dept.getParentId(), dept.getId())) {
            throw new ServiceException("部门名称已存在");
        }
        
        // 检查部门编码是否唯一
        if (!checkDeptCodeUnique(dept.getCode(), dept.getId())) {
            throw new ServiceException("部门编码已存在");
        }

        // 如果父部门发生变化，更新部门路径
        SysDept oldDept = this.getById(dept.getId());
        if (!Objects.equals(oldDept.getParentId(), dept.getParentId())) {
            dept.setPath(buildDeptPath(dept.getParentId()));
            // 更新子部门路径
            updateChildrenDeptPath(dept);
        }

        dept.setUpdateTime(LocalDateTime.now());
        return this.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDept(Long id) {
        // 检查是否存在子部门
        if (this.count(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, id)) > 0) {
            throw new ServiceException("存在子部门，不能删除");
        }

        return this.removeById(id);
    }

    @Override
    public List<Long> getSubDeptIds(Long deptId) {
        List<Long> deptIds = new ArrayList<>();
        deptIds.add(deptId);
        
        // 递归获取子部门ID
        List<SysDept> children = this.list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, deptId));
        
        if (!children.isEmpty()) {
            children.forEach(child -> deptIds.addAll(getSubDeptIds(child.getId())));
        }
        
        return deptIds;
    }

    @Override
    public String buildDeptPath(Long parentId) {
        if (parentId == null || parentId == 0) {
            return "/0";
        }
        
        SysDept parent = this.getById(parentId);
        if (parent == null) {
            throw new ServiceException("父部门不存在");
        }
        
        return parent.getPath() + "/" + parent.getId();
    }

    @Override
    public boolean checkDeptCodeUnique(String code, Long deptId) {
        return this.count(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getCode, code)
                .ne(deptId != null, SysDept::getId, deptId)) == 0;
    }

    @Override
    public boolean checkDeptNameUnique(String name, Long parentId, Long deptId) {
        return this.count(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getName, name)
                .eq(SysDept::getParentId, parentId)
                .ne(deptId != null, SysDept::getId, deptId)) == 0;
    }

    /**
     * 构建部门树形结构
     */
    private List<SysDept> buildDeptTree(List<SysDept> deptList) {
        List<SysDept> tree = new ArrayList<>();
        
        // 获取根部门
        deptList.stream()
                .filter(dept -> dept.getParentId() == null || dept.getParentId() == 0)
                .forEach(dept -> {
                    dept.setChildren(getChildren(dept.getId(), deptList));
                    tree.add(dept);
                });
        
        return tree;
    }

    /**
     * 递归获取子部门
     */
    private List<SysDept> getChildren(Long parentId, List<SysDept> deptList) {
        List<SysDept> children = new ArrayList<>();
        
        deptList.stream()
                .filter(dept -> Objects.equals(dept.getParentId(), parentId))
                .forEach(dept -> {
                    dept.setChildren(getChildren(dept.getId(), deptList));
                    children.add(dept);
                });
        
        return children;
    }

    /**
     * 更新子部门路径
     */
    private void updateChildrenDeptPath(SysDept dept) {
        List<SysDept> children = this.list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, dept.getId()));
        
        if (!children.isEmpty()) {
            children.forEach(child -> {
                child.setPath(dept.getPath() + "/" + dept.getId());
                this.updateById(child);
                updateChildrenDeptPath(child);
            });
        }
    }

    @Override
    public SysDept getDeptById(Long id) {
        return this.getById(id);
    }

    @Override
    public List<Long> getUserDataScope(Long userId) {
        // TODO: 实现用户数据权限范围查询
        // 这里简单返回空列表，实际应根据用户角色查询其可访问的部门ID
        return new ArrayList<>();
    }
}