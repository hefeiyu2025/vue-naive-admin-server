package com.naiveadmin.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_permission")
public class SysPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String code;
    
    private Long menuId;
    
    private String urlPerm;
    
    private String btnPerm;
    
    private Boolean status;
    
    @TableLogic
    private Boolean deleted;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 