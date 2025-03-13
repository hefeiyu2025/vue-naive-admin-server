package com.naiveadmin.server.controller;

import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysPermission;
import com.naiveadmin.server.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionService permissionService;

    @GetMapping("/list")
    public Result getList(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Boolean status,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer pageSize) {
        return permissionService.getList(keyword, status, page, pageSize);
    }

    @GetMapping("/all")
    public Result getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    public Result getDetail(@PathVariable Long id) {
        return permissionService.getDetail(id);
    }

    @PostMapping
    public Result create(@RequestBody SysPermission permission) {
        return permissionService.create(permission);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody SysPermission permission) {
        permission.setId(id);
        return permissionService.update(permission);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return permissionService.delete(id);
    }
} 