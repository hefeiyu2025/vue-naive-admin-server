package com.naiveadmin.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naiveadmin.server.entity.SysDictData;
import com.naiveadmin.server.entity.SysDictType;

import java.util.List;

/**
 * 字典Service接口
 */
public interface ISysDictService extends IService<SysDictType> {

    /**
     * 分页查询字典类型列表
     */
    IPage<SysDictType> getDictTypePage(IPage<SysDictType> page, String keyword, Integer status);

    /**
     * 获取字典类型详情
     */
    SysDictType getDictTypeById(Long id);

    /**
     * 创建字典类型
     */
    void createDictType(SysDictType dictType);

    /**
     * 更新字典类型
     */
    void updateDictType(SysDictType dictType);

    /**
     * 删除字典类型
     */
    void deleteDictType(Long id);

    /**
     * 分页查询字典数据列表
     */
    IPage<SysDictData> getDictDataPage(IPage<SysDictData> page, String dictType, String keyword, Integer status);

    /**
     * 根据字典类型查询字典数据列表
     */
    List<SysDictData> getDictDataByType(String dictType);

    /**
     * 获取字典数据详情
     */
    SysDictData getDictDataById(Long id);

    /**
     * 创建字典数据
     */
    void createDictData(SysDictData dictData);

    /**
     * 更新字典数据
     */
    void updateDictData(SysDictData dictData);

    /**
     * 删除字典数据
     */
    void deleteDictData(Long id);

    /**
     * 检查字典类型是否唯一
     */
    boolean checkDictTypeUnique(String type, Long id);
} 