package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.common.exception.ServiceException;
import com.naiveadmin.server.entity.SysRole;
import com.naiveadmin.server.entity.SysUser;
import com.naiveadmin.server.entity.SysUserRole;
import com.naiveadmin.server.entity.SysDept;
import com.naiveadmin.server.entity.SysPermission;
import com.naiveadmin.server.mapper.SysRoleMapper;
import com.naiveadmin.server.mapper.SysUserMapper;
import com.naiveadmin.server.mapper.SysUserRoleMapper;
import com.naiveadmin.server.service.IFileService;
import com.naiveadmin.server.service.ISysUserService;
import com.naiveadmin.server.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final IFileService fileService;
    private final ISysDeptService deptService;

    @Override
    public Page<SysUser> listUserPage(IPage<SysUser> page, SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        
        // 用户名或昵称模糊查询
        if (StringUtils.hasText(user.getUsername())) {
            wrapper.like(SysUser::getUsername, user.getUsername())
                    .or()
                    .like(SysUser::getNickname, user.getUsername());
        }

        // 部门查询
        if (user.getDeptId() != null) {
            List<Long> deptIds = deptService.getSubDeptIds(user.getDeptId());
            wrapper.in(SysUser::getDeptId, deptIds);
        }

        wrapper.orderByDesc(SysUser::getCreateTime);
        Page<SysUser> userPage = this.page((Page<SysUser>) page, wrapper);

        // 设置部门信息
        userPage.getRecords().forEach(this::setUserDept);

        return userPage;
    }

    @Override
    public SysUser getUserByUsername(String username) {
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));
        
        if (user != null) {
            setUserDept(user);
        }
        
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignUserRoles(Long userId, List<Long> roleIds) {
        // 删除用户原有角色
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId));

        // 分配新角色
        if (roleIds != null && !roleIds.isEmpty()) {
            List<SysUserRole> userRoles = roleIds.stream()
                    .map(roleId -> {
                        SysUserRole userRole = new SysUserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        return userRole;
                    })
                    .collect(Collectors.toList());
            
            // 批量插入用户角色关联
            for (SysUserRole userRole : userRoles) {
                userRoleMapper.insert(userRole);
            }
        }
        
        return true;
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        List<Long> roleIds = getUserRoleIds(userId);
        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        return roleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getId, roleIds)
                .eq(SysRole::getStatus, true))
                .stream()
                .map(SysRole::getCode)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        List<Long> roleIds = getUserRoleIds(userId);
        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        return roleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getId, roleIds)
                .eq(SysRole::getStatus, true))
                .stream()
                .filter(role -> role.getPermissions() != null)
                .flatMap(role -> role.getPermissions().stream())
                .map(SysPermission::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(SysUser user) {
        // 检查用户名是否已存在
        if (this.getUserByUsername(user.getUsername()) != null) {
            throw new ServiceException("用户名已存在");
        }

        // 检查部门是否存在
        if (user.getDeptId() != null) {
            SysDept dept = deptService.getDeptById(user.getDeptId());
            if (dept == null) {
                throw new ServiceException("部门不存在");
            }
        }

        // 设置默认值
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(true); // 默认启用

        // 保存用户
        boolean success = this.save(user);

        // 分配角色
        if (success && user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<Long> roleIds = user.getRoles().stream()
                    .map(SysRole::getId)
                    .collect(Collectors.toList());
            this.assignUserRoles(user.getId(), roleIds);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        // 检查部门是否存在
        if (user.getDeptId() != null) {
            SysDept dept = deptService.getDeptById(user.getDeptId());
            if (dept == null) {
                throw new ServiceException("部门不存在");
            }
        }

        user.setUpdateTime(LocalDateTime.now());
        
        // 如果密码不为空，则加密
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 更新用户
        boolean success = this.updateById(user);

        // 更新角色
        if (success && user.getRoles() != null) {
            List<Long> roleIds = user.getRoles().stream()
                    .map(SysRole::getId)
                    .collect(Collectors.toList());
            this.assignUserRoles(user.getId(), roleIds);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserByIds(List<Long> ids) {
        // 删除用户角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .in(SysUserRole::getUserId, ids));

        // 删除用户
        return this.removeByIds(ids);
    }

    @Override
    public boolean resetPassword(SysUser user) {
        user.setPassword(passwordEncoder.encode("123456")); // 默认密码
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    public boolean updatePassword(SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new ServiceException("文件名不能为空");
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!isValidImageExtension(extension)) {
            throw new ServiceException("只支持jpg、jpeg、png、gif格式的图片");
        }

        // 上传文件
        String avatarUrl = fileService.uploadFile(file, "avatar");

        // 更新当前用户的头像URL
        SysUser currentUser = getCurrentUser();
        if (currentUser != null) {
            // 删除旧头像
            if (StringUtils.hasText(currentUser.getAvatar())) {
                fileService.deleteFile(currentUser.getAvatar());
            }
            
            // 更新新头像
            currentUser.setAvatar(avatarUrl);
            currentUser.setUpdateTime(LocalDateTime.now());
            this.updateById(currentUser);
        }

        return avatarUrl;
    }

    /**
     * 检查文件扩展名是否为有效的图片格式
     */
    private boolean isValidImageExtension(String extension) {
        return "jpg".equals(extension) || 
               "jpeg".equals(extension) || 
               "png".equals(extension) || 
               "gif".equals(extension);
    }

    /**
     * 获取当前登录用户
     * TODO: 实现获取当前登录用户的逻辑
     */
    private SysUser getCurrentUser() {
        return null;
    }

    /**
     * 检查用户名是否唯一
     */
    private boolean checkUsernameUnique(String username, Long userId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .ne(userId != null, SysUser::getId, userId)
                .eq(SysUser::getDeleted, false)) == 0;
    }

    /**
     * 检查手机号是否唯一
     */
    private boolean checkPhoneUnique(String phone, Long userId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, phone)
                .ne(userId != null, SysUser::getId, userId)
                .eq(SysUser::getDeleted, false)) == 0;
    }

    /**
     * 检查邮箱是否唯一
     */
    private boolean checkEmailUnique(String email, Long userId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, email)
                .ne(userId != null, SysUser::getId, userId)
                .eq(SysUser::getDeleted, false)) == 0;
    }

    /**
     * 设置用户部门信息
     */
    private void setUserDept(SysUser user) {
        if (user.getDeptId() != null) {
            user.setDept(deptService.getDeptById(user.getDeptId()));
        }
    }
} 