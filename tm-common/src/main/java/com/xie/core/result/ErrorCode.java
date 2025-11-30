package com.xie.core.result;

import lombok.Getter;

/**
 * 错误码枚举
 * <p>系统级别（1位）+ 模块（2位）+ 错误类型（2位）</p>
 */
@Getter
public enum ErrorCode {

    // 通用状态码
    SUCCESS(200, "成功"),
    FAILED(500, "失败"),

    // 用户模块状态码 1 + 00 + xx
    PARAM_ERROR(10001, "请求参数错误"),
    UNAUTHORIZED(10002, "用户未授权"),
    USER_NOT_EXIST(10003, "用户不存在"),
    USER_ALREADY_EXIST(10004, "用户已存在"),
    USER_LOGIN_ERROR(10005, "用户登录失败"),
    USER_LOGOUT_ERROR(10006, "用户登出失败"),
    USER_LOGOUT_SUCCESS(10007, "用户登出成功"),
    USER_NOT_LOGIN(10008, "用户未登录"),

    // 兜底错误码
    SERVER_ERROR(99999, "服务器错误");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误信息
     */
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
