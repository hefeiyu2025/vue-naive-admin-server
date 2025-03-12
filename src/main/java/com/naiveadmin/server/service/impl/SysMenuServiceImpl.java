package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.common.exception.ServiceException;
import com.naiveadmin.server.entity.SysMenu;
import com.naiveadmin.server.entity.SysRoleMenu;
import com.naiveadmin.server.mapper.SysMenuMapper;
import com.naiveadmin.server.mapper.SysRoleMenuMapper;
import com.naiveadmin.server.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单Service实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final SysRoleMenuMapper roleMenuMapper;

    public SysMenuServiceImpl(SysRoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }

    @Override
    public List<SysMenu> getMenuTree(String keyword, Boolean status, String type) {
        List<SysMenu> menuList = baseMapper.selectMenuList(keyword, type, status);
        return buildMenuTree(menuList);
    }

    @Override
    public List<SysMenu> getMenuList(String keyword, Boolean status, String type) {
        return baseMapper.selectMenuList(keyword, type, status);
    }

    @Override
    public SysMenu getMenuById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMenu(SysMenu menu) {
        // 校验菜单名称唯一性
        if (!checkMenuNameUnique(menu.getName(), menu.getParentId(), null)) {
            throw new ServiceException("菜单名称已存在");
        }
        // 校验路由路径唯一性
        if (!checkPathUnique(menu.getPath(), null)) {
            throw new ServiceException("路由路径已存在");
        }
        // 校验权限标识唯一性
        if (menu.getPermission() != null && !checkPermissionUnique(menu.getPermission(), null)) {
            throw new ServiceException("权限标识已存在");
        }

        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(SysMenu menu) {
        // 校验菜单名称唯一性
        if (!checkMenuNameUnique(menu.getName(), menu.getParentId(), menu.getId())) {
            throw new ServiceException("菜单名称已存在");
        }
        // 校验路由路径唯一性
        if (!checkPathUnique(menu.getPath(), menu.getId())) {
            throw new ServiceException("路由路径已存在");
        }
        // 校验权限标识唯一性
        if (menu.getPermission() != null && !checkPermissionUnique(menu.getPermission(), menu.getId())) {
            throw new ServiceException("权限标识已存在");
        }

        menu.setUpdateTime(LocalDateTime.now());
        updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        // 检查是否存在子菜单
        if (baseMapper.selectCount(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, id)
                .eq(SysMenu::getDeleted, false)) > 0) {
            throw new ServiceException("存在子菜单，不允许删除");
        }

        // 检查菜单是否已分配给角色
        if (roleMenuMapper.checkMenuExistRole(id) > 0) {
            throw new ServiceException("菜单已分配给角色，不允许删除");
        }

        // 删除菜单
        removeById(id);
    }

    @Override
    public List<String> getIconList() {
        // TODO: 实现图标列表获取逻辑
        return new ArrayList<>();
    }

    @Override
    public boolean checkMenuNameUnique(String name, Long parentId, Long menuId) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getName, name)
                .eq(SysMenu::getParentId, parentId)
                .eq(SysMenu::getDeleted, false);
        if (menuId != null) {
            queryWrapper.ne(SysMenu::getId, menuId);
        }
        return baseMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public boolean checkPathUnique(String path, Long menuId) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getPath, path)
                .eq(SysMenu::getDeleted, false);
        if (menuId != null) {
            queryWrapper.ne(SysMenu::getId, menuId);
        }
        return baseMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public boolean checkPermissionUnique(String permission, Long menuId) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getPermission, permission)
                .eq(SysMenu::getDeleted, false);
        if (menuId != null) {
            queryWrapper.ne(SysMenu::getId, menuId);
        }
        return baseMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public List<SysMenu> getUserMenuList(Long userId) {
        List<SysMenu> menuList = baseMapper.selectMenusByUserId(userId);
        return buildMenuTree(menuList);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        return baseMapper.selectPermissionsByUserId(userId);
    }

    /**
     * 构建菜单树形结构
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menuList) {
        List<SysMenu> tree = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (menu.getParentId() == 0L) {
                tree.add(findChildren(menu, menuList));
            }
        }
        return tree;
    }

    /**
     * 递归查找子菜单
     */
    private SysMenu findChildren(SysMenu parent, List<SysMenu> menuList) {
        parent.setChildren(new ArrayList<>());
        for (SysMenu menu : menuList) {
            if (menu.getParentId().equals(parent.getId())) {
                parent.getChildren().add(findChildren(menu, menuList));
            }
        }
        return parent;
    }
} 