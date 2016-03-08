package com.zhuke.smart_home.beans;

import org.springframework.stereotype.Component;

/**
 * Created by ZHUKE on 2016/3/8.
 */
@Component
public class Users {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
