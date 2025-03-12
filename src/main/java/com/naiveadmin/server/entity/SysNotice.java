package com.naiveadmin.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知公告实体类
 */
@Data
@TableName("sys_notice")
public class SysNotice implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告类型（1-通知，2-公告，3-警告）
     */
    private String type;

    /**
     * 公告状态（0-草稿，1-已发布，2-已撤回）
     */
    private String status;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 是否置顶（0-否，1-是）
     */
    private Boolean isTop;

    /**
     * 阅读次数
     */
    private Integer readCount;

    /**
     * 创建者
     */
    private String createBy;

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
} 