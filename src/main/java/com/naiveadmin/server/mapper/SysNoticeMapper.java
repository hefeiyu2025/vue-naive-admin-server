package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.naiveadmin.server.entity.SysNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告Mapper接口
 */
@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    /**
     * 分页查询公告列表
     *
     * @param page      分页对象
     * @param keyword   关键字
     * @param type      公告类型
     * @param status    公告状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 分页结果
     */
    IPage<SysNotice> selectNoticePage(IPage<SysNotice> page,
                                    @Param("keyword") String keyword,
                                    @Param("type") String type,
                                    @Param("status") String status,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime);

    /**
     * 查询有效的公告列表
     *
     * @return 有效公告列表
     */
    List<SysNotice> selectValidNoticeList();

    /**
     * 更新公告阅读次数
     *
     * @param noticeId 公告ID
     * @return 影响行数
     */
    int updateNoticeReadCount(@Param("noticeId") Long noticeId);

    /**
     * 发布公告
     *
     * @param noticeId 公告ID
     * @return 影响行数
     */
    int publishNotice(@Param("noticeId") Long noticeId);

    /**
     * 撤回公告
     *
     * @param noticeId 公告ID
     * @return 影响行数
     */
    int revokeNotice(@Param("noticeId") Long noticeId);
} 