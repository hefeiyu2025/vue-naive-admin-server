package com.naiveadmin.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naiveadmin.server.entity.SysNotice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告Service接口
 */
public interface ISysNoticeService extends IService<SysNotice> {

    /**
     * 分页查询公告列表
     */
    IPage<SysNotice> getNoticePage(IPage<SysNotice> page, String keyword, String type, String status, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询有效的公告列表
     */
    List<SysNotice> getValidNoticeList();

    /**
     * 获取公告详情
     */
    SysNotice getNoticeById(Long id);

    /**
     * 创建公告
     */
    void createNotice(SysNotice notice);

    /**
     * 更新公告
     */
    void updateNotice(SysNotice notice);

    /**
     * 删除公告
     */
    void deleteNotice(Long id);

    /**
     * 发布公告
     */
    void publishNotice(Long id);

    /**
     * 撤回公告
     */
    void revokeNotice(Long id);

    /**
     * 阅读公告
     */
    void readNotice(Long id);
} 