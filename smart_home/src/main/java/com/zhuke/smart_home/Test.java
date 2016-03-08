package com.zhuke.smart_home;

import com.zhuke.smart_home.beans.Users;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ZHUKE on 2016/3/8.
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
        Users users = context.getBean(Users.class);
        users.setName("朱轲");
        System.out.println(users.getName());
    }
}
