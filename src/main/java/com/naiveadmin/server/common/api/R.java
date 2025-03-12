package com.naiveadmin.server.common.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 */
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 是否成功
     */
    private boolean success;

    protected R() {
    }

    protected R(int code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    /**
     * 成功返回结果
     */
    public static <T> R<T> success() {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null, true);
    }

    /**
     * 成功返回结果
     *
     * @param data 返回数据
     */
    public static <T> R<T> success(T data) {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, true);
    }

    /**
     * 成功返回结果
     *
     * @param data    返回数据
     * @param message 返回消息
     */
    public static <T> R<T> success(T data, String message) {
        return new R<>(ResultCode.SUCCESS.getCode(), message, data, true);
    }

    /**
     * 失败返回结果
     */
    public static <T> R<T> failed() {
        return new R<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), null, false);
    }

    /**
     * 失败返回结果
     *
     * @param message 错误消息
     */
    public static <T> R<T> failed(String message) {
        return new R<>(ResultCode.FAILED.getCode(), message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> R<T> failed(IErrorCode errorCode) {
        return new R<>(errorCode.getCode(), errorCode.getMessage(), null, false);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> R<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 错误消息
     */
    public static <T> R<T> validateFailed(String message) {
        return new R<>(ResultCode.VALIDATE_FAILED.getCode(), message, null, false);
    }

    /**
     * 未登录返回结果
     */
    public static <T> R<T> unauthorized() {
        return failed(ResultCode.UNAUTHORIZED);
    }

    /**
     * 未授权返回结果
     */
    public static <T> R<T> forbidden() {
        return failed(ResultCode.FORBIDDEN);
    }
} 