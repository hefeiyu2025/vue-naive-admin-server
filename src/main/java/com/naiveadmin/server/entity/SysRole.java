package com.naiveadmin.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体类
 */
@Data
@TableName("sys_role")
public class SysRole {
    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 角色状态（0：禁用，1：启用）
     */
    private Boolean status;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 权限列表
     */
    @TableField(exist = false)
    private List<SysPermission> permissions;
} 