package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naiveadmin.server.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 分页查询用户列表
     *
     * @param page    分页参数
     * @param keyword 搜索关键词
     * @return 用户分页列表
     */
    IPage<SysUser> selectUserPage(Page<SysUser> page, @Param("keyword") String keyword);

    /**
     * 根据用户ID查询用户信息（包含角色和部门）
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    SysUser selectUserById(@Param("userId") Long userId);
} 