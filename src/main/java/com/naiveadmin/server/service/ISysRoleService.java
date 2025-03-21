package com.naiveadmin.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naiveadmin.server.entity.SysRole;
import com.naiveadmin.server.common.Result;

import java.util.List;

/**
 * 角色Service接口
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     */
    IPage<SysRole> getRolePage(IPage<SysRole> page, String keyword, Boolean status);

    /**
     * 查询所有可用角色
     */
    List<SysRole> getAllRoles();

    /**
     * 获取角色详情
     */
    SysRole getRoleById(Long id);

    /**
     * 创建角色
     */
    void createRole(SysRole role);

    /**
     * 更新角色
     */
    void updateRole(SysRole role);

    /**
     * 删除角色
     */
    void deleteRole(Long id);

    /**
     * 批量删除角色
     */
    void deleteRoles(List<Long> ids);

    /**
     * 更新角色状态
     */
    void updateRoleStatus(Long id, Boolean status);

    /**
     * 分配角色菜单权限
     */
    void assignRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 检查角色编码是否唯一
     */
    boolean checkRoleCodeUnique(String code, Long roleId);

    /**
     * 获取角色列表
     */
    Result getList(String keyword, Boolean status, Integer page, Integer pageSize);

    /**
     * 获取角色详情
     */
    Result getDetail(Long id);

    /**
     * 创建角色
     */
    Result create(SysRole role);

    /**
     * 更新角色
     */
    Result update(SysRole role);

    /**
     * 删除角色
     */
    Result delete(Long id);

    /**
     * 分配角色权限
     */
    Result assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 获取角色权限
     */
    Result getRolePermissions(Long roleId);
} 