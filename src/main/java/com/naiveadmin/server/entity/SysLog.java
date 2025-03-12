package com.naiveadmin.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_log")
public class SysLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private Integer businessType;
    
    private String method;
    
    private String requestMethod;
    
    private Integer operatorType;
    
    private String operatorName;
    
    private String deptName;
    
    private String url;
    
    private String ip;
    
    private String location;
    
    private String param;
    
    private String result;
    
    private Integer status;
    
    private String errorMsg;
    
    private LocalDateTime operationTime;
    
    private LocalDateTime createTime;
} 