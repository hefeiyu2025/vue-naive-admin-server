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
     *
     * @param name 部门名称
     * @param status 部门状态
     * @return 部门树形列表
     */
    List<SysDept> listDeptTree(String name, Boolean status);

    /**
     * 获取部门列表
     *
     * @param dept 查询条件
     * @return 部门列表
     */
    List<SysDept> listDepts(SysDept dept);

    /**
     * 添加部门
     *
     * @param dept 部门信息
     * @return 是否成功
     */
    boolean addDept(SysDept dept);

    /**
     * 更新部门
     *
     * @param dept 部门信息
     * @return 是否成功
     */
    boolean updateDept(SysDept dept);

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 是否成功
     */
    boolean deleteDept(Long id);

    /**
     * 获取部门及其所有子部门ID列表
     *
     * @param deptId 部门ID
     * @return 部门ID列表
     */
    List<Long> getSubDeptIds(Long deptId);

    /**
     * 构建部门树形结构
     *
     * @param depts 部门列表
     * @return 树形结构的部门列表
     */
    List<SysDept> buildDeptTree(List<SysDept> depts);

    /**
     * 构建部门层级路径
     *
     * @param parentId 父部门ID
     * @return 部门层级路径
     */
    String buildDeptPath(Long parentId);

    /**
     * 检查部门编码是否唯一
     *
     * @param code 部门编码
     * @param deptId 部门ID
     * @return 是否唯一
     */
    boolean checkDeptCodeUnique(String code, Long deptId);

    /**
     * 检查部门名称是否唯一
     *
     * @param name 部门名称
     * @param parentId 父部门ID
     * @param deptId 部门ID
     * @return 是否唯一
     */
    boolean checkDeptNameUnique(String name, Long parentId, Long deptId);

    /**
     * 获取部门详情
     *
     * @param id 部门ID
     * @return 部门信息
     */
    SysDept getDeptById(Long id);

    /**
     * 获取用户的数据权限范围（部门ID列表）
     *
     * @param userId 用户ID
     * @return 部门ID列表
     */
    List<Long> getUserDataScope(Long userId);
} 