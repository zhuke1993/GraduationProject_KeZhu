package com.zhuke.smart_home.beans;

import org.springframework.stereotype.Component;

/**
 * 控制信息报文
 * Created by ZHUKE on 2016/3/8.
 */
@Component
public class Message {
    private String code;

    private String info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
