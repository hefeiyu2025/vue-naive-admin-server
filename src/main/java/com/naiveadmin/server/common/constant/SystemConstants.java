package com.naiveadmin.server.common.constant;

/**
 * 系统常量
 */
public interface SystemConstants {

    /**
     * 通知公告类型
     */
    interface NoticeType {
        String NOTICE = "1";    // 通知
        String ANNOUNCEMENT = "2";    // 公告
        String WARNING = "3";    // 警告
    }

    /**
     * 通知公告状态
     */
    interface NoticeStatus {
        String DRAFT = "0";    // 草稿
        String PUBLISHED = "1";    // 已发布
        String REVOKED = "2";    // 已撤回
    }

    /**
     * 通知类型
     */
    interface NotificationType {
        String INFO = "info";       // 信息
        String SUCCESS = "success";  // 成功
        String WARNING = "warning";  // 警告
        String ERROR = "error";      // 错误
    }

    /**
     * 通知优先级
     */
    interface NotificationPriority {
        String LOW = "low";         // 低
        String MEDIUM = "medium";    // 中
        String HIGH = "high";        // 高
    }

    /**
     * 通用状态
     */
    interface Status {
        Integer DISABLE = 0;    // 停用
        Integer ENABLE = 1;     // 正常
    }

    /**
     * 是否标识
     */
    interface YesNo {
        Integer NO = 0;    // 否
        Integer YES = 1;   // 是
    }
} 