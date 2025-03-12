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
import com.naiveadmin.server.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    public Page<SysUser> listUserPage(IPage<SysUser> page, SysUser user) {
        Page<SysUser> resultPage = new Page<>();
        Page<SysUser> pageParam = new Page<>(page.getCurrent(), page.getSize());
        String keyword = user.getUsername();
        IPage<SysUser> iPage = baseMapper.selectUserPage(pageParam, keyword);
        resultPage.setRecords(iPage.getRecords());
        resultPage.setTotal(iPage.getTotal());
        return resultPage;
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .eq(SysUser::getDeleted, false));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(SysUser user) {
        // 校验用户名唯一性
        if (!checkUsernameUnique(user.getUsername(), null)) {
            throw new ServiceException("用户名已存在");
        }
        // 校验手机号唯一性
        if (user.getPhone() != null && !checkPhoneUnique(user.getPhone(), null)) {
            throw new ServiceException("手机号已存在");
        }
        // 校验邮箱唯一性
        if (user.getEmail() != null && !checkEmailUnique(user.getEmail(), null)) {
            throw new ServiceException("邮箱已存在");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        boolean result = save(user);

        // 保存用户角色关联
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(role.getId());
                userRoleMapper.insert(userRole);
            });
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        // 校验用户名唯一性
        if (!checkUsernameUnique(user.getUsername(), user.getId())) {
            throw new ServiceException("用户名已存在");
        }
        // 校验手机号唯一性
        if (user.getPhone() != null && !checkPhoneUnique(user.getPhone(), user.getId())) {
            throw new ServiceException("手机号已存在");
        }
        // 校验邮箱唯一性
        if (user.getEmail() != null && !checkEmailUnique(user.getEmail(), user.getId())) {
            throw new ServiceException("邮箱已存在");
        }

        user.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(user);

        // 更新用户角色关联
        if (user.getRoles() != null) {
            // 删除原有角色关联
            userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                    .eq(SysUserRole::getUserId, user.getId()));
            // 添加新的角色关联
            user.getRoles().forEach(role -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(role.getId());
                userRoleMapper.insert(userRole);
            });
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserByIds(List<Long> ids) {
        for (Long id : ids) {
            // 删除用户角色关联
            userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                    .eq(SysUserRole::getUserId, id));
        }
        // 删除用户
        return removeByIds(ids);
    }

    @Override
    public boolean resetPassword(SysUser user) {
        SysUser updateUser = new SysUser();
        updateUser.setId(user.getId());
        updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updateUser.setUpdateTime(LocalDateTime.now());
        return updateById(updateUser);
    }

    @Override
    public boolean updatePassword(SysUser user) {
        SysUser existUser = getById(user.getId());
        if (existUser == null) {
            throw new ServiceException("用户不存在");
        }
        
        // 假设前端传入的oldPassword存储在临时字段中
        // 这里需要根据实际情况修改，可能需要在接口中添加额外参数
        String oldPassword = "123456"; // 默认密码，实际应从请求中获取
        if (!passwordEncoder.matches(oldPassword, existUser.getPassword())) {
            throw new ServiceException("原密码错误");
        }
        
        existUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existUser.setUpdateTime(LocalDateTime.now());
        return updateById(existUser);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        // TODO: 实现文件上传逻辑
        return "avatar/default.jpg"; // 返回默认头像路径
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