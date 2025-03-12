package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.entity.SysNotice;
import com.naiveadmin.server.mapper.SysNoticeMapper;
import com.naiveadmin.server.service.ISysNoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告Service实现类
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    @Override
    public IPage<SysNotice> getNoticePage(IPage<SysNotice> page, String keyword, String type, String status, LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.selectNoticePage(page, keyword, type, status, startTime, endTime);
    }

    @Override
    public List<SysNotice> getValidNoticeList() {
        return baseMapper.selectValidNoticeList();
    }

    @Override
    public SysNotice getNoticeById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotice(SysNotice notice) {
        notice.setCreateTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        notice.setReadCount(0);
        save(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotice(SysNotice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        updateById(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotice(Long id) {
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishNotice(Long id) {
        baseMapper.publishNotice(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeNotice(Long id) {
        baseMapper.revokeNotice(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readNotice(Long id) {
        baseMapper.updateNoticeReadCount(id);
    }
} 