package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naiveadmin.server.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserRoleByUserId(Long userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteUserRole(List<Long> ids);

    /**
     * 批量新增用户角色信息
     *
     * @param userRoleList 用户角色列表
     * @return 结果
     */
    int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * 删除用户和角色关联信息
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") List<Long> userIds);
} 