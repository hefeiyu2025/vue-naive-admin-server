package com.naiveadmin.server.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naiveadmin.server.common.api.R;
import com.naiveadmin.server.entity.SysUser;
import com.naiveadmin.server.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {

    private final ISysUserService userService;

    @Operation(summary = "获取用户列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public R<Page<SysUser>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        Page<SysUser> pageParam = new Page<>(page, pageSize);
        SysUser query = new SysUser();
        query.setUsername(keyword);
        return R.success(userService.listUserPage(pageParam, query));
    }

    @Operation(summary = "创建用户")
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('sys:user:create')")
    public R<?> create(@RequestBody SysUser user) {
        if (userService.addUser(user)) {
            return R.success();
        }
        return R.failed();
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public R<?> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        if (userService.updateUser(user)) {
            return R.success();
        }
        return R.failed();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public R<?> delete(@PathVariable Long id) {
        if (userService.deleteUserByIds(Collections.singletonList(id))) {
            return R.success();
        }
        return R.failed();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public R<SysUser> info(@RequestParam String username) {
        return R.success(userService.getUserByUsername(username));
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public R<?> updatePassword(@RequestBody UpdatePasswordParam param) {
        SysUser user = new SysUser();
        user.setPassword(param.getNewPassword());
        if (userService.updatePassword(user)) {
            return R.success();
        }
        return R.failed();
    }

    @Operation(summary = "上传头像")
    @PostMapping("/avatar")
    public R<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(file);
        return R.success(avatarUrl);
    }
}

record UpdatePasswordParam(String oldPassword, String newPassword) {
    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
} 