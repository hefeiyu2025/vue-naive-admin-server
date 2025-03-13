package com.naiveadmin.server.service;

import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysPermission;

public interface SysPermissionService {
    Result getList(String keyword, Boolean status, Integer page, Integer pageSize);
    
    Result getAllPermissions();
    
    Result getDetail(Long id);
    
    Result create(SysPermission permission);
    
    Result update(SysPermission permission);
    
    Result delete(Long id);
} 