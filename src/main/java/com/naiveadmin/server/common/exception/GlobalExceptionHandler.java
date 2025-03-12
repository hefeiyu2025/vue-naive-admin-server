package com.naiveadmin.server.common.exception;

import com.naiveadmin.server.common.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        return R.failed(e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("参数校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        log.error(msg, e);
        return R.validateFailed(msg);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("参数绑定失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        log.error(msg, e);
        return R.validateFailed(msg);
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder("参数校验失败:");
        for (ConstraintViolation<?> violation : violations) {
            sb.append(violation.getPropertyPath()).append("：").append(violation.getMessage()).append(", ");
        }
        String msg = sb.toString();
        log.error(msg, e);
        return R.validateFailed(msg);
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public R<Void> handleAuthenticationException(AuthenticationException e) {
        log.error("认证异常: {}", e.getMessage(), e);
        if (e instanceof BadCredentialsException) {
            return R.failed("用户名或密码错误");
        }
        return R.unauthorized();
    }

    /**
     * 处理授权异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.error("授权异常: {}", e.getMessage(), e);
        return R.forbidden();
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return R.failed("系统异常，请联系管理员");
    }
} 