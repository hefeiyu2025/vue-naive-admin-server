package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.common.exception.ServiceException;
import com.naiveadmin.server.entity.SysRole;
import com.naiveadmin.server.entity.SysRoleMenu;
import com.naiveadmin.server.entity.SysRolePermission;
import com.naiveadmin.server.mapper.SysRoleMapper;
import com.naiveadmin.server.mapper.SysRoleMenuMapper;
import com.naiveadmin.server.mapper.SysRolePermissionMapper;
import com.naiveadmin.server.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色Service实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysRolePermissionMapper rolePermissionMapper;

    public SysRoleServiceImpl(SysRoleMenuMapper roleMenuMapper, SysRolePermissionMapper rolePermissionMapper) {
        this.roleMenuMapper = roleMenuMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public IPage<SysRole> getRolePage(IPage<SysRole> page, String keyword, Boolean status) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(keyword), SysRole::getName, keyword)
                .or()
                .like(StringUtils.hasText(keyword), SysRole::getCode, keyword)
                .eq(Objects.nonNull(status), SysRole::getStatus, status)
                .orderByDesc(SysRole::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public Result getList(String keyword, Boolean status, Integer page, Integer pageSize) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysRole::getName, keyword)
                    .or()
                    .like(SysRole::getCode, keyword);
        }
        if (status != null) {
            wrapper.eq(SysRole::getStatus, status);
        }
        wrapper.orderByDesc(SysRole::getCreateTime);

        Page<SysRole> pageResult = page(new Page<>(page, pageSize), wrapper);
        return Result.success(pageResult);
    }

    @Override
    public List<SysRole> getAllRoles() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getStatus, true)
                .orderByDesc(SysRole::getCreateTime);
        return list(wrapper);
    }

    @Override
    public Result getDetail(Long id) {
        SysRole role = getById(id);
        if (role == null) {
            return Result.error("角色不存在");
        }
        return Result.success(role);
    }

    @Override
    public Result create(SysRole role) {
        if (checkRoleExists(role)) {
            return Result.error("角色编码已存在");
        }
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        save(role);
        return Result.success();
    }

    @Override
    public Result update(SysRole role) {
        SysRole existingRole = getById(role.getId());
        if (existingRole == null) {
            return Result.error("角色不存在");
        }
        if (!existingRole.getCode().equals(role.getCode()) && checkRoleExists(role)) {
            return Result.error("角色编码已存在");
        }
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
        return Result.success();
    }

    @Override
    public Result delete(Long id) {
        if (!removeById(id)) {
            return Result.error("删除失败");
        }
        // 删除角色权限关联
        rolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, id));
        return Result.success();
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
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        removeById(id);
        // 删除角色权限关联
        rolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, id));
        // 删除角色菜单关联
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoles(List<Long> ids) {
        removeBatchByIds(ids);
        // 删除角色权限关联
        rolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>()
                .in(SysRolePermission::getRoleId, ids));
        // 删除角色菜单关联
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                .in(SysRoleMenu::getRoleId, ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleStatus(Long id, Boolean status) {
        SysRole role = new SysRole();
        role.setId(id);
        role.setStatus(status);
        updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleMenus(Long roleId, List<Long> menuIds) {
        // 删除原有的角色菜单关联
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId));

        // 批量插入新的角色菜单关联
        if (!menuIds.isEmpty()) {
            List<SysRoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> {
                        SysRoleMenu roleMenu = new SysRoleMenu();
                        roleMenu.setRoleId(roleId);
                        roleMenu.setMenuId(menuId);
                        return roleMenu;
                    })
                    .collect(Collectors.toList());
            for (SysRoleMenu roleMenu : roleMenus) {
                roleMenuMapper.insert(roleMenu);
            }
        }
    }

    @Override
    public boolean checkRoleCodeUnique(String code, Long roleId) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getCode, code)
                .ne(Objects.nonNull(roleId), SysRole::getId, roleId);
        return count(wrapper) == 0;
    }

    @Override
    @Transactional
    public Result assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除原有权限
        rolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId));
        
        // 添加新权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<SysRolePermission> rolePermissions = permissionIds.stream()
                    .map(permissionId -> {
                        SysRolePermission rp = new SysRolePermission();
                        rp.setRoleId(roleId);
                        rp.setPermissionId(permissionId);
                        return rp;
                    })
                    .collect(Collectors.toList());
            for (SysRolePermission rolePermission : rolePermissions) {
                rolePermissionMapper.insert(rolePermission);
            }
        }
        return Result.success();
    }

    @Override
    public Result getRolePermissions(Long roleId) {
        List<Long> permissionIds = rolePermissionMapper.selectPermissionsByRoleId(roleId);
        return Result.success(permissionIds);
    }

    @Override
    public SysRole getRoleById(Long id) {
        return getById(id);
    }

    private boolean checkRoleExists(SysRole role) {
        return count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getCode, role.getCode())
                .ne(role.getId() != null, SysRole::getId, role.getId())) > 0;
    }
} 