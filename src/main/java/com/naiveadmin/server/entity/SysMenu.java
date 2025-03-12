package com.naiveadmin.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单实体类
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 是否隐藏（0-显示，1-隐藏）
     */
    private Boolean hidden;

    /**
     * 是否缓存（0-不缓存，1-缓存）
     */
    private Boolean keepAlive;

    /**
     * 菜单类型（M-目录，C-菜单，F-按钮）
     */
    private String type;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 状态（0-禁用，1-正常）
     */
    private Boolean status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 子菜单列表
     */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
} 