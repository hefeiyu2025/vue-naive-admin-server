package com.naiveadmin.server.service;

import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysMenu;

public interface SysMenuService {
    Result getMenuTree(String keyword, Boolean status, String type);
    
    Result getMenuList(String keyword, Boolean status, String type);
    
    Result getDetail(Long id);
    
    Result create(SysMenu menu);
    
    Result update(SysMenu menu);
    
    Result delete(Long id);
    
    Result getIconList();
} 