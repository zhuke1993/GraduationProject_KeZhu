package com.zhuke.svmclassifier.service;

import com.zhuke.svmclassifier.entity.UserInfo;

public interface RegisterLoginService {
    void register(UserInfo userInfo);

    boolean login(String name, String password);
}
