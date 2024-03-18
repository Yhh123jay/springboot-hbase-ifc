package com.yhh.springboot_hbase_ifc.model.vo;

public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    LOGIN_FAIL(201, "用户名或密码错误"),
    FAIL(500, "失败"),
    UNAUTHORIZED(401, "未认证"),
    NOT_FOUND(404, "未找到"),
    FORBIDDEN(403, "禁止访问"),
    BAD_REQUEST(400, "错误请求"),
    METHOD_NOT_ALLOWED(405, "不支持的请求方式"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    ;

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
