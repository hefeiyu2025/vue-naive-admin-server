package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naiveadmin.server.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {
    
    int insertBatch(@Param("list") List<SysRolePermission> list);
    
    List<Long> selectPermissionsByRoleId(@Param("roleId") Long roleId);
} 