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
 * 部门实体类
 */
@Data
@TableName("sys_dept")
public class SysDept implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门编码
     */
    private String code;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 部门层级路径
     */
    private String path;

    /**
     * 部门负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态（0-禁用，1-正常）
     */
    private Boolean status;

    /**
     * 排序号
     */
    private Integer orderNum;

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
     * 子部门列表
     */
    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<>();
} 