package com.naiveadmin.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * 角色状态（0：禁用，1：启用）
     */
    private Boolean status;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 权限列表
     */
    @TableField(exist = false)
    private List<String> permissions;
} 