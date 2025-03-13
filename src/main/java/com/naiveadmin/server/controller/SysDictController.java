package com.naiveadmin.server.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysDictData;
import com.naiveadmin.server.entity.SysDictType;
import com.naiveadmin.server.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典Controller
 */
@Tag(name = "字典管理")
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final ISysDictService dictService;

    @Operation(summary = "获取字典类型列表")
    @GetMapping("/type/list")
    public Result<IPage<SysDictType>> typeList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        IPage<SysDictType> page = dictService.getDictTypePage(new Page<>(pageNum, pageSize), keyword, status);
        return Result.success(page);
    }

    @Operation(summary = "获取字典类型详情")
    @GetMapping("/type/{id}")
    public Result<SysDictType> getTypeInfo(@PathVariable Long id) {
        return Result.success(dictService.getDictTypeById(id));
    }

    @Operation(summary = "新增字典类型")
    @PostMapping("/type")
    public Result<Void> addType(@Validated @RequestBody SysDictType dictType) {
        if (!dictService.checkDictTypeUnique(dictType.getType(), null)) {
            return Result.failed("字典类型已存在");
        }
        dictService.createDictType(dictType);
        return Result.success();
    }

    @Operation(summary = "修改字典类型")
    @PutMapping("/type")
    public Result<Void> editType(@Validated @RequestBody SysDictType dictType) {
        if (!dictService.checkDictTypeUnique(dictType.getType(), dictType.getId())) {
            return Result.failed("字典类型已存在");
        }
        dictService.updateDictType(dictType);
        return Result.success();
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/type/{id}")
    public Result<Void> removeType(@PathVariable Long id) {
        dictService.deleteDictType(id);
        return Result.success();
    }

    @Operation(summary = "获取字典数据列表")
    @GetMapping("/data/list")
    public Result<IPage<SysDictData>> dataList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam String dictType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        IPage<SysDictData> page = dictService.getDictDataPage(new Page<>(pageNum, pageSize),
                dictType, keyword, status);
        return Result.success(page);
    }

    @Operation(summary = "根据字典类型查询字典数据")
    @GetMapping("/data/type/{dictType}")
    public Result<List<SysDictData>> getDataByType(@PathVariable String dictType) {
        List<SysDictData> data = dictService.getDictDataByType(dictType);
        return Result.success(data);
    }

    @Operation(summary = "获取字典数据详情")
    @GetMapping("/data/{id}")
    public Result<SysDictData> getDataInfo(@PathVariable Long id) {
        return Result.success(dictService.getDictDataById(id));
    }

    @Operation(summary = "新增字典数据")
    @PostMapping("/data")
    public Result<Void> addData(@Validated @RequestBody SysDictData dictData) {
        dictService.createDictData(dictData);
        return Result.success();
    }

    @Operation(summary = "修改字典数据")
    @PutMapping("/data")
    public Result<Void> editData(@Validated @RequestBody SysDictData dictData) {
        dictService.updateDictData(dictData);
        return Result.success();
    }

    @Operation(summary = "删除字典数据")
    @DeleteMapping("/data/{id}")
    public Result<Void> removeData(@PathVariable Long id) {
        dictService.deleteDictData(id);
        return Result.success();
    }
} 