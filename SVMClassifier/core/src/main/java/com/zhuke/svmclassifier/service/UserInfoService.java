package com.zhuke.svmclassifier.service;

import com.zhuke.svmclassifier.entity.UserInfo;

/**
 * Created by ZHUKE on 2016/4/21.
 */
public interface UserInfoService {

    UserInfo login(String name, String password);

    void register(UserInfo userInfo);
}
