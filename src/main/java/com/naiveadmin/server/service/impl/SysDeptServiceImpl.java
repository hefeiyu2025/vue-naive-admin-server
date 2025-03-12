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

import java.util.ArrayList;
import java.util.List;

/**
 * 部门Service实现类
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public List<SysDept> getDeptTree(String keyword, Boolean status) {
        List<SysDept> deptList = baseMapper.selectDeptList(keyword, status);
        return buildDeptTree(deptList);
    }

    @Override
    public List<SysDept> getDeptList(String keyword, Boolean status) {
        return baseMapper.selectDeptList(keyword, status);
    }

    @Override
    public SysDept getDeptById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDept(SysDept dept) {
        if (!checkDeptNameUnique(dept.getName(), dept.getParentId(), null)) {
            throw new ServiceException("部门名称已存在");
        }
        if (!checkDeptCodeUnique(dept.getCode(), null)) {
            throw new ServiceException("部门编码已存在");
        }
        baseMapper.insert(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(SysDept dept) {
        SysDept oldDept = baseMapper.selectById(dept.getId());
        if (oldDept == null) {
            throw new ServiceException("部门不存在");
        }
        if (!oldDept.getName().equals(dept.getName()) && !checkDeptNameUnique(dept.getName(), dept.getParentId(), dept.getId())) {
            throw new ServiceException("部门名称已存在");
        }
        if (!oldDept.getCode().equals(dept.getCode()) && !checkDeptCodeUnique(dept.getCode(), dept.getId())) {
            throw new ServiceException("部门编码已存在");
        }
        baseMapper.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Long deptId) {
        List<Long> childrenDeptIds = baseMapper.selectChildrenDeptIds(deptId);
        if (!childrenDeptIds.isEmpty()) {
            throw new ServiceException("存在下级部门，不允许删除");
        }
        baseMapper.deleteById(deptId);
    }

    @Override
    public boolean checkDeptNameUnique(String name, Long parentId, Long deptId) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getName, name)
                .eq(SysDept::getParentId, parentId)
                .ne(deptId != null, SysDept::getId, deptId);
        return baseMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public boolean checkDeptCodeUnique(String code, Long deptId) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getCode, code)
                .ne(deptId != null, SysDept::getId, deptId);
        return baseMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public List<Long> getChildDeptIds(Long deptId) {
        return baseMapper.selectChildrenDeptIds(deptId);
    }

    @Override
    public List<Long> getUserDataScope(Long userId) {
        // 实现用户数据权限范围查询
        // 这里简单返回空列表，实际应根据用户角色查询其可访问的部门ID
        return new ArrayList<>();
    }

    /**
     * 构建部门树形结构
     */
    private List<SysDept> buildDeptTree(List<SysDept> deptList) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        for (SysDept dept : deptList) {
            tempList.add(dept.getId());
        }
        for (SysDept dept : deptList) {
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(deptList, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = deptList;
        }
        return returnList;
    }

    private void recursionFn(List<SysDept> list, SysDept t) {
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        for (SysDept n : list) {
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }
}