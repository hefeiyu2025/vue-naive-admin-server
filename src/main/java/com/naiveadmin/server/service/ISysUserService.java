package com.naiveadmin.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naiveadmin.server.entity.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户服务接口
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     */
    Page<SysUser> listUserPage(IPage<SysUser> page, SysUser user);

    /**
     * 根据用户名查询用户
     */
    SysUser getUserByUsername(String username);

    /**
     * 新增用户
     */
    boolean addUser(SysUser user);

    /**
     * 修改用户
     */
    boolean updateUser(SysUser user);

    /**
     * 删除用户
     */
    boolean deleteUserByIds(List<Long> ids);

    /**
     * 重置密码
     */
    boolean resetPassword(SysUser user);

    /**
     * 修改密码
     */
    boolean updatePassword(SysUser user);

    /**
     * 上传头像
     */
    String uploadAvatar(MultipartFile file);
} 