package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naiveadmin.server.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param page    分页参数
     * @param keyword 搜索关键词
     * @return 角色分页列表
     */
    IPage<SysRole> selectRolePage(Page<SysRole> page, @Param("keyword") String keyword);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询角色信息（包含菜单）
     *
     * @param roleId 角色ID
     * @return 角色信息
     */
    SysRole selectRoleById(@Param("roleId") Long roleId);
} 