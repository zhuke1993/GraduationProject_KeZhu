package com.zhuke.smart_home.dao.impl;

import com.zhuke.smart_home.beans.UserInfo;
import com.zhuke.smart_home.dao.UserInfoDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by ZHUKE on 2016/3/15.
 */
@Component
public class UserInfoDaoImpl implements UserInfoDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void add(UserInfo userInfo) {
        hibernateTemplate.save(userInfo);
        hibernateTemplate.flush();
    }
}
