package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.entity.SysNotification;
import com.naiveadmin.server.mapper.SysNotificationMapper;
import com.naiveadmin.server.service.ISysNotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 消息通知Service实现类
 */
@Service
public class SysNotificationServiceImpl extends ServiceImpl<SysNotificationMapper, SysNotification> implements ISysNotificationService {

    @Override
    public IPage<SysNotification> getNotificationPage(IPage<SysNotification> page, Long userId, Boolean isRead,
                                                    String category, String type, String priority,
                                                    LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.selectNotificationPage(page, userId, isRead, category, type, priority, startTime, endTime);
    }

    @Override
    public Integer getUnreadCount(Long userId) {
        return baseMapper.selectUnreadCount(userId);
    }

    @Override
    public SysNotification getNotificationById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotification(SysNotification notification) {
        notification.setCreateTime(LocalDateTime.now());
        notification.setIsRead(false);
        save(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long id, Long userId) {
        baseMapper.markAsRead(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(Long userId) {
        baseMapper.markAllAsRead(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotification(Long id) {
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteExpiredNotifications() {
        baseMapper.deleteExpiredNotifications();
    }
} 