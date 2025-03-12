package com.naiveadmin.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naiveadmin.server.entity.SysNotification;

import java.time.LocalDateTime;

/**
 * 消息通知Service接口
 */
public interface ISysNotificationService extends IService<SysNotification> {

    /**
     * 分页查询通知列表
     */
    IPage<SysNotification> getNotificationPage(IPage<SysNotification> page, Long userId, Boolean isRead,
                                             String category, String type, String priority,
                                             LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取未读通知数量
     */
    Integer getUnreadCount(Long userId);

    /**
     * 获取通知详情
     */
    SysNotification getNotificationById(Long id);

    /**
     * 创建通知
     */
    void createNotification(SysNotification notification);

    /**
     * 标记通知为已读
     */
    void markAsRead(Long id, Long userId);

    /**
     * 标记所有通知为已读
     */
    void markAllAsRead(Long userId);

    /**
     * 删除通知
     */
    void deleteNotification(Long id);

    /**
     * 删除过期通知
     */
    void deleteExpiredNotifications();
} 