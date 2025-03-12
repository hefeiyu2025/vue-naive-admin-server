package com.naiveadmin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naiveadmin.server.entity.SysDept;

import java.util.List;

/**
 * 部门Service接口
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * 获取部门树形列表
     */
    List<SysDept> getDeptTree(String keyword, Boolean status);

    /**
     * 获取部门列表
     */
    List<SysDept> getDeptList(String keyword, Boolean status);

    /**
     * 获取部门详情
     */
    SysDept getDeptById(Long id);

    /**
     * 创建部门
     */
    void createDept(SysDept dept);

    /**
     * 更新部门
     */
    void updateDept(SysDept dept);

    /**
     * 删除部门
     */
    void deleteDept(Long id);

    /**
     * 检查部门名称是否唯一
     */
    boolean checkDeptNameUnique(String name, Long parentId, Long deptId);

    /**
     * 检查部门编码是否唯一
     */
    boolean checkDeptCodeUnique(String code, Long deptId);

    /**
     * 获取部门及其所有子部门ID列表
     */
    List<Long> getChildDeptIds(Long deptId);

    /**
     * 获取用户的数据权限范围（部门ID列表）
     */
    List<Long> getUserDataScope(Long userId);
} 