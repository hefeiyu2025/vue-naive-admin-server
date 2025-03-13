package com.naiveadmin.server.service;

import com.naiveadmin.server.entity.SysUser;
import com.naiveadmin.server.model.LoginParam;
import com.naiveadmin.server.model.LoginResult;

/**
 * 认证服务接口
 */
public interface IAuthService {
    
    /**
     * 用户登录
     *
     * @param loginParam 登录参数
     * @return 登录结果
     */
    LoginResult login(LoginParam loginParam);
    
    /**
     * 用户登出
     *
     * @return 是否成功
     */
    boolean logout();
    
    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    SysUser getCurrentUser();
    
    /**
     * 获取当前用户ID
     *
     * @return 当前用户ID
     */
    Long getCurrentUserId();
    
    /**
     * 生成Token
     *
     * @param user 用户信息
     * @return Token
     */
    String generateToken(SysUser user);
    
    /**
     * 验证Token
     *
     * @param token Token
     * @return 用户ID
     */
    Long validateToken(String token);
} 