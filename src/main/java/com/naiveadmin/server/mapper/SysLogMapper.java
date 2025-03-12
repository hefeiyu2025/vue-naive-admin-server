package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.naiveadmin.server.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
    
    /**
     * 分页查询操作日志列表
     */
    IPage<SysLog> selectLogPage(IPage<SysLog> page,
                               @Param("title") String title,
                               @Param("businessType") Integer businessType,
                               @Param("status") Integer status,
                               @Param("operatorName") String operatorName,
                               @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime);
    
    /**
     * 清空操作日志
     */
    void cleanLog();
    
    /**
     * 批量删除操作日志
     */
    int deleteLogByIds(Long[] ids);
} 