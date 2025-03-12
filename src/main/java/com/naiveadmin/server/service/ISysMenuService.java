package com.naiveadmin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naiveadmin.server.entity.SysMenu;

import java.util.List;

/**
 * 菜单Service接口
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单树形列表
     */
    List<SysMenu> getMenuTree(String keyword, Boolean status, String type);

    /**
     * 获取菜单列表
     */
    List<SysMenu> getMenuList(String keyword, Boolean status, String type);

    /**
     * 获取菜单详情
     */
    SysMenu getMenuById(Long id);

    /**
     * 创建菜单
     */
    void createMenu(SysMenu menu);

    /**
     * 更新菜单
     */
    void updateMenu(SysMenu menu);

    /**
     * 删除菜单
     */
    void deleteMenu(Long id);

    /**
     * 获取图标列表
     */
    List<String> getIconList();

    /**
     * 检查菜单名称是否唯一
     */
    boolean checkMenuNameUnique(String name, Long parentId, Long menuId);

    /**
     * 检查路由路径是否唯一
     */
    boolean checkPathUnique(String path, Long menuId);

    /**
     * 检查权限标识是否唯一
     */
    boolean checkPermissionUnique(String permission, Long menuId);

    /**
     * 获取用户菜单列表
     */
    List<SysMenu> getUserMenuList(Long userId);

    /**
     * 获取用户权限列表
     */
    List<String> getUserPermissions(Long userId);
} 