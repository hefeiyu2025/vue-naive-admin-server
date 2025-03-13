package com.naiveadmin.server.model;

import com.naiveadmin.server.entity.SysUser;
import lombok.Data;

/**
 * 登录结果
 */
@Data
public class LoginResult {
    /**
     * 令牌
     */
    private String token;
    
    /**
     * 用户信息
     */
    private UserInfo userInfo;
    
    /**
     * 用户信息
     */
    @Data
    public static class UserInfo {
        /**
         * 用户ID
         */
        private Long id;
        
        /**
         * 用户名
         */
        private String username;
        
        /**
         * 昵称
         */
        private String nickname;
        
        /**
         * 头像
         */
        private String avatar;
        
        /**
         * 角色列表
         */
        private java.util.List<String> roles;
        
        /**
         * 从SysUser构建UserInfo
         */
        public static UserInfo fromSysUser(SysUser user, java.util.List<String> roles) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setNickname(user.getNickname());
            userInfo.setAvatar(user.getAvatar());
            userInfo.setRoles(roles);
            return userInfo;
        }
    }
} 