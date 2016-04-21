package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.entity.UserInfo;
import com.zhuke.svmclassifier.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZHUKE on 2016/4/21.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public UserInfo login(String name, String password) {
        List<UserInfo> userInfoList = (List<UserInfo>) hibernateTemplate.find("from UserInfo u where u.userName = ? and password = ?", name, password);
        if (userInfoList.size() != 0) {
            return userInfoList.get(0);
        }
        return null;
    }

    @Transactional
    public void register(UserInfo userInfo) {
        hibernateTemplate.save(userInfo);
    }
}
