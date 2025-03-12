package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.common.exception.ServiceException;
import com.naiveadmin.server.entity.SysUser;
import com.naiveadmin.server.entity.SysUserRole;
import com.naiveadmin.server.mapper.SysUserMapper;
import com.naiveadmin.server.mapper.SysUserRoleMapper;
import com.naiveadmin.server.service.IFileService;
import com.naiveadmin.server.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 用户Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final IFileService fileService;

    @Override
    public Page<SysUser> listUserPage(IPage<SysUser> page, SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(user.getUsername())) {
            wrapper.like(SysUser::getUsername, user.getUsername())
                    .or()
                    .like(SysUser::getNickname, user.getUsername());
        }
        wrapper.orderByDesc(SysUser::getCreateTime);
        return this.page((Page<SysUser>) page, wrapper);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(SysUser user) {
        // 检查用户名是否已存在
        if (this.getUserByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 设置默认值
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(true); // 默认启用

        return this.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        user.setUpdateTime(LocalDateTime.now());
        // 如果密码不为空，则加密
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserByIds(List<Long> ids) {
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
} 