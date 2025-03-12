package com.naiveadmin.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息通知实体类
 */
@Data
@TableName("sys_notification")
public class SysNotification implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 通知ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知类型（info-信息，success-成功，warning-警告，error-错误）
     */
    private String type;

    /**
     * 是否已读（0-未读，1-已读）
     */
    private Boolean isRead;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 关联链接
     */
    private String link;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 发送者头像
     */
    private String senderAvatar;

    /**
     * 优先级（low-低，medium-中，high-高）
     */
    private String priority;

    /**
     * 通知分类
     */
    private String category;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 接收用户ID
     */
    private Long userId;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    private Boolean deleted;
} 