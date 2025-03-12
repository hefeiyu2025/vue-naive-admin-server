package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naiveadmin.server.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 认证相关数据访问接口
 */
public interface SysAuthMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户认证信息
     */
    SysUser selectUserAuthByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询权限列表
     */
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 更新用户最后登录时间
     */
    void updateUserLoginTime(@Param("userId") Long userId);

    /**
     * 更新用户密码
     */
    int updateUserPassword(@Param("userId") Long userId, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

    /**
     * 重置用户密码
     */
    int resetUserPassword(@Param("userId") Long userId, @Param("password") String password);
} 