package com.zhuke.smart_home;

import com.zhuke.smart_home.beans.UserInfo;
import com.zhuke.smart_home.service.UserInfoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ZHUKE on 2016/3/15.
 */
public class UserInfoTest extends DefaultSpringTestCase {

    @Autowired
    private UserInfoService userInfoService;

    @Test
    @Transactional(readOnly = false)
    @Rollback(value = false)
    public void addUserInfo() {
        UserInfo userInfo = new UserInfo(2L, "朱轲", "zhuke", "zhuke_1993@126.com");
        userInfoService.addUserInfo(userInfo);
    }
}
