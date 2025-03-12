package com.naiveadmin.server.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naiveadmin.server.common.api.R;
import com.naiveadmin.server.entity.SysNotification;
import com.naiveadmin.server.service.ISysNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 消息通知Controller
 */
@Tag(name = "消息通知管理")
@RestController
@RequestMapping("/system/notification")
@RequiredArgsConstructor
public class SysNotificationController {

    private final ISysNotificationService notificationService;

    @Operation(summary = "获取通知列表")
    @GetMapping("/list")
    public R<IPage<SysNotification>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Long userId,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime) {
        IPage<SysNotification> page = notificationService.getNotificationPage(new Page<>(pageNum, pageSize),
                userId, isRead, category, type, priority, startTime, endTime);
        return R.success(page);
    }

    @Operation(summary = "获取未读通知数量")
    @GetMapping("/unread/count")
    public R<Integer> unreadCount(@RequestParam Long userId) {
        return R.success(notificationService.getUnreadCount(userId));
    }

    @Operation(summary = "获取通知详情")
    @GetMapping("/{id}")
    public R<SysNotification> getInfo(@PathVariable Long id) {
        return R.success(notificationService.getNotificationById(id));
    }

    @Operation(summary = "新增通知")
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysNotification notification) {
        notificationService.createNotification(notification);
        return R.success();
    }

    @Operation(summary = "标记通知为已读")
    @PutMapping("/{id}/read")
    public R<Void> markAsRead(@PathVariable Long id, @RequestParam Long userId) {
        notificationService.markAsRead(id, userId);
        return R.success();
    }

    @Operation(summary = "标记所有通知为已读")
    @PutMapping("/read/all")
    public R<Void> markAllAsRead(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return R.success();
    }

    @Operation(summary = "删除通知")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return R.success();
    }

    @Operation(summary = "删除过期通知")
    @DeleteMapping("/expired")
    public R<Void> removeExpired() {
        notificationService.deleteExpiredNotifications();
        return R.success();
    }
} 