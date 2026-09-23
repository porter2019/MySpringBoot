package com.xxx.myspringboot.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 接口统一返回值
 */
@Data
@NoArgsConstructor
public class ApiResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 数据
     */
    private Object data;

    /**
     * 执行成功
     */
    private boolean succeeded;

    /**
     * 错误信息
     */
    private Object errors;

    /**
     * 附加数据
     */
    private Object extras;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    private ApiResult(Integer statusCode, Object data, boolean succeeded, Object errors, Object extras) {
        this.statusCode = statusCode;
        this.data = data;
        this.succeeded = succeeded;
        this.errors = errors;
        this.extras = extras;
        this.timestamp = System.currentTimeMillis();
    }

    // ============ 成功 ============

    /**
     * 成功（无数据）
     */
    public static ApiResult success() {
        return new ApiResult(200, null, true, null, null);
    }

    /**
     * 成功（带数据）
     */
    public static ApiResult success(Object data) {
        return new ApiResult(200, data, true, null, null);
    }

    /**
     * 成功（自定义状态码 + 数据）
     */
    public static ApiResult success(int statusCode, Object data) {
        return new ApiResult(statusCode, data, true, null, null);
    }

    /**
     * 成功（带数据 + 附加数据）
     */
    public static ApiResult success(Object data, Object extras) {
        return new ApiResult(200, data, true, null, extras);
    }

    // ============ 失败 ============

    /**
     * 失败（默认 500）
     */
    public static ApiResult error() {
        return new ApiResult(500, null, false, "操作失败", null);
    }

    /**
     * 失败（带错误信息）
     */
    public static ApiResult error(Object errors) {
        return new ApiResult(500, null, false, errors, null);
    }

    /**
     * 失败（自定义状态码 + 错误信息）
     */
    public static ApiResult error(int statusCode, Object errors) {
        return new ApiResult(statusCode, null, false, errors, null);
    }

    /**
     * 失败（自定义状态码 + 错误信息 + 附加数据）
     */
    public static ApiResult error(int statusCode, Object errors, Object extras) {
        return new ApiResult(statusCode, null, false, errors, extras);
    }

    // ============ 常用快捷方法 ============

    /**
     * 未授权 401
     */
    public static ApiResult unauthorized() {
        return error(401, "未授权");
    }

    public static ApiResult unauthorized(Object errors) {
        return error(401, errors);
    }

    /**
     * 禁止访问 403
     */
    public static ApiResult forbidden() {
        return error(403, "禁止访问");
    }

    public static ApiResult forbidden(Object errors) {
        return error(403, errors);
    }

    /**
     * 资源不存在 404
     */
    public static ApiResult notFound() {
        return error(404, "资源不存在");
    }

    public static ApiResult notFound(Object errors) {
        return error(404, errors);
    }

    /**
     * 参数错误 400
     */
    public static ApiResult badRequest(Object errors) {
        return error(400, errors);
    }

    // ============ 链式操作 ============

    /**
     * 设置附加数据（链式）
     */
    public ApiResult withExtras(Object extras) {
        this.extras = extras;
        return this;
    }

    /**
     * 设置数据（链式）
     */
    public ApiResult withData(Object data) {
        this.data = data;
        return this;
    }
}