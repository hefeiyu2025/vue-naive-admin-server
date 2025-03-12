package com.naiveadmin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naiveadmin.server.entity.SysDept;

import java.util.List;

/**
 * 部门服务接口
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * 获取部门树形列表
     */
    List<SysDept> getDeptTree();

    /**
     * 获取部门列表
     */
    List<SysDept> listDepts(SysDept dept);

    /**
     * 添加部门
     */
    boolean addDept(SysDept dept);

    /**
     * 更新部门
     */
    boolean updateDept(SysDept dept);

    /**
     * 删除部门
     */
    boolean deleteDept(Long id);

    /**
     * 获取部门及其所有子部门ID
     */
    List<Long> getSubDeptIds(Long deptId);

    /**
     * 构建部门层级路径
     */
    String buildDeptPath(Long parentId);

    /**
     * 检查部门编码是否唯一
     */
    boolean checkDeptCodeUnique(String code, Long deptId);

    /**
     * 检查部门名称是否唯一
     */
    boolean checkDeptNameUnique(String name, Long parentId, Long deptId);

    /**
     * 获取部门详情
     */
    SysDept getDeptById(Long id);

    /**
     * 获取用户的数据权限范围（部门ID列表）
     */
    List<Long> getUserDataScope(Long userId);
} 