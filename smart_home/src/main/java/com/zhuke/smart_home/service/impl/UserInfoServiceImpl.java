package com.zhuke.smart_home.service.impl;

import com.zhuke.smart_home.beans.UserInfo;
import com.zhuke.smart_home.dao.UserInfoDao;
import com.zhuke.smart_home.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ZHUKE on 2016/3/15.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao dao;

    @Transactional(readOnly = false)
    public void addUserInfo(UserInfo userInfo) {
        dao.add(userInfo);
    }
}
