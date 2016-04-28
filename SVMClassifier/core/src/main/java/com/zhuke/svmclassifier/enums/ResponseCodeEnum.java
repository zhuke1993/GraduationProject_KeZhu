package com.zhuke.svmclassifier.enums;

public enum ResponseCodeEnum {
    OK("OK", "处理成功"),
    FAILED("FAILED", "处理失败"),
    UNAUTHORIZED("401", "请登陆"),
    FORBIDDEN("403", "权限不足"),
    SERVER_ERROR("500", "服务器错误");

    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ResponseCodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
