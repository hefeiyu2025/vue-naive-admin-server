package com.naiveadmin.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 用户角色关联实体类
 */
@Data
@TableName("sys_user_role")
public class SysUserRole {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
} 