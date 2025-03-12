package com.naiveadmin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.naiveadmin.server.entity.SysDictData;
import com.naiveadmin.server.entity.SysDictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典Mapper接口
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDictType> {

    /**
     * 分页查询字典类型列表
     *
     * @param page    分页对象
     * @param keyword 关键字
     * @param status  状态
     * @return 分页结果
     */
    IPage<SysDictType> selectDictTypePage(IPage<SysDictType> page,
                                         @Param("keyword") String keyword,
                                         @Param("status") Integer status);

    /**
     * 分页查询字典数据列表
     *
     * @param page     分页对象
     * @param dictType 字典类型
     * @param keyword  关键字
     * @param status   状态
     * @return 分页结果
     */
    IPage<SysDictData> selectDictDataPage(IPage<SysDictData> page,
                                         @Param("dictType") String dictType,
                                         @Param("keyword") String keyword,
                                         @Param("status") Integer status);

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<SysDictData> selectDictDataByType(@Param("dictType") String dictType);

    /**
     * 检查字典类型是否唯一
     *
     * @param type 字典类型
     * @param id   字典类型ID
     * @return 结果
     */
    Integer checkDictTypeUnique(@Param("type") String type, @Param("id") Long id);
} 