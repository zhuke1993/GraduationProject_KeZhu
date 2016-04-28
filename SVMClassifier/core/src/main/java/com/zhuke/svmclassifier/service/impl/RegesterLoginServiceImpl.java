package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.entity.UserInfo;
import com.zhuke.svmclassifier.exceptions.UsernameExistedException;
import com.zhuke.svmclassifier.service.RegisterLoginService;
import com.zhuke.svmclassifier.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@Service
public class RegesterLoginServiceImpl implements RegisterLoginService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;

    @Transactional
    public void register(UserInfo userInfo) {
        Assert.notNull(userInfo.getUserName());
        Assert.notNull(userInfo.getPassword());
        UserInfo userInfo1 = userInfoService.getUserInfo(userInfo.getUserName());
        if (userInfo1 != null) {
            throw new UsernameExistedException("用户名： " + userInfo.getUserName() + " 已存在，请更换用户名注册。");
        }
        userInfoService.register(userInfo);
    }

    public boolean login(String name, String password) {
        UserInfo userInfo = userInfoService.getUserInfo(name);
        if (userInfo != null) {
            if (userInfo.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

}
