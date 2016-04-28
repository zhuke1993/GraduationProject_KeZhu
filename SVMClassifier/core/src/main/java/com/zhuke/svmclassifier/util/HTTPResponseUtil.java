package com.zhuke.svmclassifier.util;

import com.google.gson.Gson;
import com.zhuke.svmclassifier.enums.ResponseCodeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HTTPResponseUtil {
    private String code;
    private Object msg;

    public static HTTPResponseUtil _401Error = new HTTPResponseUtil(ResponseCodeEnum.UNAUTHORIZED.getCode(), ResponseCodeEnum.UNAUTHORIZED.getValue());
    public static HTTPResponseUtil _403Error = new HTTPResponseUtil(ResponseCodeEnum.FORBIDDEN.getCode(), ResponseCodeEnum.FORBIDDEN.getValue());
    public static HTTPResponseUtil _ServerError = new HTTPResponseUtil(ResponseCodeEnum.SERVER_ERROR.getCode(), ResponseCodeEnum.SERVER_ERROR.getValue());
    public static HTTPResponseUtil _OK = new HTTPResponseUtil(ResponseCodeEnum.OK.getCode(), ResponseCodeEnum.OK.getValue());

    public HTTPResponseUtil(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public HTTPResponseUtil() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void write(HttpServletResponse response) throws IOException {
        response.getWriter().print(this.toString());
        response.getWriter().flush();
    }

    public HTTPResponseUtil(Object object) {
        if (object instanceof Exception) {
            this.setCode(ResponseCodeEnum.SERVER_ERROR.getCode());
            this.setMsg(ResponseCodeEnum.SERVER_ERROR.getValue());
        } else {
            this.setCode(ResponseCodeEnum.OK.getCode());
            this.setMsg(object);
        }
    }
}
