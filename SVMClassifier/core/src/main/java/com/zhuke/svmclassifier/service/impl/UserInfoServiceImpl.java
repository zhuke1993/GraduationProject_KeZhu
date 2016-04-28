package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.config.SystemConfig;
import com.zhuke.svmclassifier.entity.UserInfo;
import com.zhuke.svmclassifier.service.SVMConfig;
import com.zhuke.svmclassifier.service.UserInfoService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZHUKE on 2016/4/21.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService, ApplicationContextAware {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    private ApplicationContext appContext;

    public UserInfo login(String name, String password) {
        List<UserInfo> userInfoList = (List<UserInfo>) hibernateTemplate.find("from UserInfo u where u.userName = ? and password = ?", name, password);
        if (userInfoList.size() != 0) {
            if (SystemConfig.getSVMConfig(userInfoList.get(0).getId()) == null) {
                Long userId = userInfoList.get(0).getId();

                SVMConfig svmConfig = appContext.getBean(SVMConfig.class);

                svmConfig.setUserId(userId);
                svmConfig.initConfig();
                SystemConfig.runningUserMap.put(userId, svmConfig);
            }
            return userInfoList.get(0);
        }
        return null;
    }

    @Transactional
    public void register(UserInfo userInfo) {
        hibernateTemplate.save(userInfo);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    public UserInfo getUserInfo(String userName) {
        List<UserInfo> userInfo = (List<UserInfo>) hibernateTemplate.find("from UserInfo u where u.email = ?", userName);
        if (userInfo.size() > 0) {
            return (UserInfo) userInfo.get(0);
        }
        return null;
    }
}
