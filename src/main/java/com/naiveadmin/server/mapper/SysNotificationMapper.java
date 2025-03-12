package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.naiveadmin.server.entity.SysNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 消息通知Mapper接口
 */
@Mapper
public interface SysNotificationMapper extends BaseMapper<SysNotification> {

    /**
     * 分页查询通知列表
     *
     * @param page      分页对象
     * @param userId    用户ID
     * @param isRead    是否已读
     * @param category  通知分类
     * @param type      通知类型
     * @param priority  优先级
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 分页结果
     */
    IPage<SysNotification> selectNotificationPage(IPage<SysNotification> page,
                                                @Param("userId") Long userId,
                                                @Param("isRead") Boolean isRead,
                                                @Param("category") String category,
                                                @Param("type") String type,
                                                @Param("priority") String priority,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);

    /**
     * 查询未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    Integer selectUnreadCount(@Param("userId") Long userId);

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @param userId        用户ID
     * @return 影响行数
     */
    int markAsRead(@Param("notificationId") Long notificationId, @Param("userId") Long userId);

    /**
     * 标记所有通知为已读
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int markAllAsRead(@Param("userId") Long userId);

    /**
     * 删除过期的通知
     *
     * @return 影响行数
     */
    int deleteExpiredNotifications();
} 