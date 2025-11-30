package com.xie.core.result;

import lombok.Data;

/**
 * 统一返回结果类
 * @param <T> 返回数据类型
 */
@Data
public class Result<T> {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 构造方法
     * @param success 是否成功
     * @param code 状态码
     * @param message 状态信息
     * @param data 返回数据
     */
    public Result(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 结果成功
     * @param data 返回数据
     * @return 结果
     * @param <T> 返回数据类型
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, 200, "成功", data);
    }

    /**
     * 结果失败
     * @param code 状态码
     * @param message 状态信息
     * @return 结果
     * @param <T> 返回数据类型
     */
    public static <T> Result<T> failed(Integer code, String message) {
        return new Result<>(false, code, message, null);
    }
}
