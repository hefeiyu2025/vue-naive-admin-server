package com.naiveadmin.server.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.naiveadmin.server.entity.SysMenu;
import com.naiveadmin.server.entity.SysRole;
import com.naiveadmin.server.entity.SysUser;
import com.naiveadmin.server.mapper.SysMenuMapper;
import com.naiveadmin.server.mapper.SysRoleMapper;
import com.naiveadmin.server.mapper.SysUserMapper;
import com.naiveadmin.server.security.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户详情服务实现类
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;

    public UserDetailsServiceImpl(SysUserMapper userMapper, SysRoleMapper roleMapper, SysMenuMapper menuMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .eq(SysUser::getDeleted, false)
        );

        if (Objects.isNull(user)) {
            log.error("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在");
        }

        if (!user.getStatus()) {
            log.error("用户已禁用: {}", username);
            throw new UsernameNotFoundException("用户已禁用");
        }

        // 查询用户角色
        List<SysRole> roles = roleMapper.selectRolesByUserId(user.getId());
        user.setRoles(roles);

        // 获取用户权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // 添加角色权限
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
            
            // 查询角色菜单权限
            List<SysMenu> menus = menuMapper.selectMenusByRoleId(role.getId());
            List<SimpleGrantedAuthority> menuAuthorities = menus.stream()
                    .filter(menu -> menu.getPermission() != null && !menu.getPermission().isEmpty())
                    .map(menu -> new SimpleGrantedAuthority(menu.getPermission()))
                    .collect(Collectors.toList());
            
            authorities.addAll(menuAuthorities);
        }

        // 创建并返回LoginUser
        return new LoginUser(user, authorities);
    }
} 