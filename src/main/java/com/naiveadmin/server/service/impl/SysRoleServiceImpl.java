package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.common.exception.ServiceException;
import com.naiveadmin.server.entity.SysRole;
import com.naiveadmin.server.entity.SysRoleMenu;
import com.naiveadmin.server.mapper.SysRoleMapper;
import com.naiveadmin.server.mapper.SysRoleMenuMapper;
import com.naiveadmin.server.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色Service实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;

    public SysRoleServiceImpl(SysRoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }

    @Override
    public IPage<SysRole> getRolePage(IPage<SysRole> page, String keyword, Boolean status) {
        Page<SysRole> rolePage = new Page<>(page.getCurrent(), page.getSize());
        return baseMapper.selectRolePage(rolePage, keyword);
    }

    @Override
    public List<SysRole> getAllRoles() {
        return list(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getDeleted, false));
    }

    @Override
    public SysRole getRoleById(Long id) {
        return baseMapper.selectRoleById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(SysRole role) {
        // 校验角色编码唯一性
        if (!checkRoleCodeUnique(role.getCode(), null)) {
            throw new ServiceException("角色编码已存在");
        }

        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        save(role);

        // 保存角色菜单关联
        if (role.getMenuIds() != null && !role.getMenuIds().isEmpty()) {
            List<SysRoleMenu> roleMenuList = role.getMenuIds().stream()
                    .map(menuId -> {
                        SysRoleMenu roleMenu = new SysRoleMenu();
                        roleMenu.setRoleId(role.getId());
                        roleMenu.setMenuId(menuId);
                        return roleMenu;
                    })
                    .collect(Collectors.toList());
            roleMenuMapper.batchRoleMenu(roleMenuList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRole role) {
        // 校验角色编码唯一性
        if (!checkRoleCodeUnique(role.getCode(), role.getId())) {
            throw new ServiceException("角色编码已存在");
        }

        role.setUpdateTime(LocalDateTime.now());
        updateById(role);

        // 更新角色菜单关联
        if (role.getMenuIds() != null) {
            // 删除原有菜单关联
            roleMenuMapper.deleteRoleMenuByRoleId(role.getId());
            // 添加新的菜单关联
            if (!role.getMenuIds().isEmpty()) {
                List<SysRoleMenu> roleMenuList = role.getMenuIds().stream()
                        .map(menuId -> {
                            SysRoleMenu roleMenu = new SysRoleMenu();
                            roleMenu.setRoleId(role.getId());
                            roleMenu.setMenuId(menuId);
                            return roleMenu;
                        })
                        .collect(Collectors.toList());
                roleMenuMapper.batchRoleMenu(roleMenuList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        // 删除角色菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(id);
        // 删除角色
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoles(List<Long> ids) {
        // 删除角色菜单关联
        roleMenuMapper.deleteRoleMenu(ids);
        // 删除角色
        removeByIds(ids);
    }

    @Override
    public void updateRoleStatus(Long id, Boolean status) {
        SysRole role = new SysRole();
        role.setId(id);
        role.setStatus(status);
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleMenus(Long roleId, List<Long> menuIds) {
        // 删除原有菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // 添加新的菜单关联
        if (!menuIds.isEmpty()) {
            List<SysRoleMenu> roleMenuList = menuIds.stream()
                    .map(menuId -> {
                        SysRoleMenu roleMenu = new SysRoleMenu();
                        roleMenu.setRoleId(roleId);
                        roleMenu.setMenuId(menuId);
                        return roleMenu;
                    })
                    .collect(Collectors.toList());
            roleMenuMapper.batchRoleMenu(roleMenuList);
        }
    }

    @Override
    public boolean checkRoleCodeUnique(String code, Long roleId) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getCode, code)
                .eq(SysRole::getDeleted, false);
        if (roleId != null) {
            queryWrapper.ne(SysRole::getId, roleId);
        }
        return baseMapper.selectCount(queryWrapper) == 0;
    }
} 