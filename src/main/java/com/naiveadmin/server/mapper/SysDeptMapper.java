package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naiveadmin.server.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper接口
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 查询部门列表
     *
     * @param keyword 搜索关键词
     * @param status  状态
     * @return 部门列表
     */
    List<SysDept> selectDeptList(@Param("keyword") String keyword, @Param("status") Boolean status);

    /**
     * 根据部门ID查询所有子部门ID
     *
     * @param deptId 部门ID
     * @return 子部门ID列表
     */
    List<Long> selectChildrenDeptIds(@Param("deptId") Long deptId);
} 