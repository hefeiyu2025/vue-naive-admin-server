package com.naiveadmin.server.controller;

import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysDept;
import com.naiveadmin.server.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@RestController
@RequestMapping("/system/department")
@RequiredArgsConstructor
public class SysDeptController {

    private final ISysDeptService deptService;

    /**
     * 获取部门树形列表
     */
    @GetMapping("/tree")
    public Result<List<SysDept>> getDeptTree(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean status) {
        return Result.ok(deptService.listDeptTree(keyword, status));
    }

    /**
     * 获取部门列表
     */
    @GetMapping("/list")
    public Result<List<SysDept>> getDeptList(SysDept dept) {
        return Result.ok(deptService.listDepts(dept));
    }

    /**
     * 获取部门详情
     */
    @GetMapping("/{id}")
    public Result<SysDept> getDeptDetail(@PathVariable Long id) {
        return Result.ok(deptService.getDeptById(id));
    }

    /**
     * 添加部门
     */
    @PostMapping
    public Result<Boolean> addDept(@RequestBody SysDept dept) {
        return Result.ok(deptService.addDept(dept));
    }

    /**
     * 更新部门
     */
    @PutMapping("/{id}")
    public Result<Boolean> updateDept(@PathVariable Long id, @RequestBody SysDept dept) {
        dept.setId(id);
        return Result.ok(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDept(@PathVariable Long id) {
        return Result.ok(deptService.deleteDept(id));
    }
} 