package com.naiveadmin.server.controller;

import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysRole;
import com.naiveadmin.server.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;

    @GetMapping("/list")
    public Result getList(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Boolean status,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer pageSize) {
        return roleService.getList(keyword, status, page, pageSize);
    }

    @GetMapping("/all")
    public Result getAllRoles() {
        return Result.success(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public Result getDetail(@PathVariable Long id) {
        return roleService.getDetail(id);
    }

    @PostMapping
    public Result create(@RequestBody SysRole role) {
        return roleService.create(role);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        return roleService.update(role);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return roleService.delete(id);
    }

    @PostMapping("/{roleId}/permissions")
    public Result assignPermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        return roleService.assignPermissions(roleId, permissionIds);
    }

    @GetMapping("/{roleId}/permissions")
    public Result getRolePermissions(@PathVariable Long roleId) {
        return roleService.getRolePermissions(roleId);
    }
} 