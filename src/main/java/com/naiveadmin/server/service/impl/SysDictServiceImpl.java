package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.entity.SysDictData;
import com.naiveadmin.server.entity.SysDictType;
import com.naiveadmin.server.mapper.SysDictDataMapper;
import com.naiveadmin.server.mapper.SysDictMapper;
import com.naiveadmin.server.service.ISysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDictType> implements ISysDictService {

    private final SysDictDataMapper dictDataMapper;

    @Override
    public IPage<SysDictType> getDictTypePage(IPage<SysDictType> page, String keyword, Integer status) {
        return baseMapper.selectDictTypePage(page, keyword, status);
    }

    @Override
    public SysDictType getDictTypeById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDictType(SysDictType dictType) {
        dictType.setCreateTime(LocalDateTime.now());
        dictType.setUpdateTime(LocalDateTime.now());
        save(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictType(SysDictType dictType) {
        dictType.setUpdateTime(LocalDateTime.now());
        updateById(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictType(Long id) {
        removeById(id);
    }

    @Override
    public IPage<SysDictData> getDictDataPage(IPage<SysDictData> page, String dictType, String keyword, Integer status) {
        return baseMapper.selectDictDataPage(page, dictType, keyword, status);
    }

    @Override
    public List<SysDictData> getDictDataByType(String dictType) {
        return baseMapper.selectDictDataByType(dictType);
    }

    @Override
    public SysDictData getDictDataById(Long id) {
        return dictDataMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDictData(SysDictData dictData) {
        dictData.setCreateTime(LocalDateTime.now());
        dictData.setUpdateTime(LocalDateTime.now());
        dictDataMapper.insert(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictData(SysDictData dictData) {
        dictData.setUpdateTime(LocalDateTime.now());
        dictDataMapper.updateById(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictData(Long id) {
        dictDataMapper.deleteById(id);
    }

    @Override
    public boolean checkDictTypeUnique(String type, Long id) {
        Integer count = baseMapper.checkDictTypeUnique(type, id);
        return count == 0;
    }
} 