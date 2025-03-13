package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysMenu;
import com.naiveadmin.server.entity.SysRoleMenu;
import com.naiveadmin.server.mapper.SysMenuMapper;
import com.naiveadmin.server.mapper.SysRoleMenuMapper;
import com.naiveadmin.server.service.ISysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<SysMenu> menuList = getMenuList(keyword, status, type);
        return buildMenuTree(menuList);
    }

    @Override
    public List<SysMenu> getMenuList(String keyword, Boolean status, String type) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(keyword), SysMenu::getName, keyword)
                .eq(Objects.nonNull(status), SysMenu::getStatus, status)
                .eq(StringUtils.hasText(type), SysMenu::getType, type)
                .orderByAsc(SysMenu::getOrderNum);
        return list(wrapper);
    }

    @Override
    public Result getDetail(Long id) {
        SysMenu menu = getById(id);
        if (menu == null) {
            return Result.error("菜单不存在");
        }
        return Result.success(menu);
    }

    @Override
    public Result create(SysMenu menu) {
        validateMenu(menu);
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        save(menu);
        return Result.success();
    }

    @Override
    public Result update(SysMenu menu) {
        SysMenu existingMenu = getById(menu.getId());
        if (existingMenu == null) {
            return Result.error("菜单不存在");
        }
        validateMenu(menu);
        menu.setUpdateTime(LocalDateTime.now());
        updateById(menu);
        return Result.success();
    }

    @Override
    public Result delete(Long id) {
        // 检查是否有子菜单
        if (count(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, id)) > 0) {
            return Result.error("请先删除子菜单");
        }
        removeById(id);
        return Result.success();
    }

    @Override
    public List<String> getIconList() {
        // 这里返回系统支持的图标列表，可以根据实际需求修改
        List<String> icons = new ArrayList<>();
        icons.add("dashboard");
        icons.add("user");
        icons.add("setting");
        icons.add("menu");
        icons.add("role");
        icons.add("permission");
        icons.add("department");
        icons.add("notice");
        icons.add("message");
        icons.add("dict");
        return icons;
    }

    @Override
    public SysMenu getMenuById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMenu(SysMenu menu) {
        validateMenu(menu);
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(SysMenu menu) {
        SysMenu existingMenu = getById(menu.getId());
        if (existingMenu == null) {
            throw new IllegalArgumentException("菜单不存在");
        }
        validateMenu(menu);
        menu.setUpdateTime(LocalDateTime.now());
        updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        // 检查是否有子菜单
        if (count(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, id)) > 0) {
            throw new IllegalArgumentException("请先删除子菜单");
        }
        removeById(id);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        return baseMapper.selectPermissionsByUserId(userId);
    }

    private List<SysMenu> buildMenuTree(List<SysMenu> menuList) {
        List<SysMenu> tree = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (menu.getParentId() == 0L) {
                tree.add(findChildren(menu, menuList));
            }
        }
        return tree;
    }

    private SysMenu findChildren(SysMenu parent, List<SysMenu> menuList) {
        for (SysMenu menu : menuList) {
            if (menu.getParentId().equals(parent.getId())) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(findChildren(menu, menuList));
            }
        }
        return parent;
    }

    private void validateMenu(SysMenu menu) {
        if (menu.getParentId() != null && menu.getParentId() != 0) {
            SysMenu parentMenu = getById(menu.getParentId());
            if (parentMenu == null) {
                throw new IllegalArgumentException("上级菜单不存在");
            }
        }
        
        // 检查同级菜单下是否有相同的路由路径
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, menu.getParentId())
                .eq(SysMenu::getPath, menu.getPath())
                .ne(menu.getId() != null, SysMenu::getId, menu.getId());
        if (count(wrapper) > 0) {
            throw new IllegalArgumentException("同级菜单下已存在相同的路由路径");
        }
    }

    @Override
    public boolean checkMenuNameUnique(String name, Long parentId, Long menuId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getName, name)
                .eq(SysMenu::getParentId, parentId)
                .ne(Objects.nonNull(menuId), SysMenu::getId, menuId);
        return count(wrapper) == 0;
    }

    @Override
    public boolean checkPathUnique(String path, Long menuId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getPath, path)
                .ne(Objects.nonNull(menuId), SysMenu::getId, menuId);
        return count(wrapper) == 0;
    }

    @Override
    public boolean checkPermissionUnique(String permission, Long menuId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getPermission, permission)
                .ne(Objects.nonNull(menuId), SysMenu::getId, menuId);
        return count(wrapper) == 0;
    }

    @Override
    public List<SysMenu> getUserMenuList(Long userId) {
        List<SysMenu> menuList = baseMapper.selectMenusByUserId(userId);
        return buildMenuTree(menuList);
    }
} 