package com.naiveadmin.server.service;

import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysRole;

import java.util.List;

public interface SysRoleService {
    Result getList(String keyword, Boolean status, Integer page, Integer pageSize);
    
    Result getAllRoles();
    
    Result getDetail(Long id);
    
    Result create(SysRole role);
    
    Result update(SysRole role);
    
    Result delete(Long id);
    
    Result assignPermissions(Long roleId, List<Long> permissionIds);
    
    Result getRolePermissions(Long roleId);
} 