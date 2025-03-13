package com.naiveadmin.server.controller;

import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.model.LoginParam;
import com.naiveadmin.server.model.LoginResult;
import com.naiveadmin.server.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    /**
     * 用户登录
     */
    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<LoginResult> login(@Valid @RequestBody LoginParam loginParam) {
        LoginResult loginResult = authService.login(loginParam);
        return Result.success(loginResult);
    }

    /**
     * 用户登出
     */
    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }
} 