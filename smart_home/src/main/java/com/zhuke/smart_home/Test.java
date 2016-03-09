package com.zhuke.smart_home;

import com.zhuke.smart_home.beans.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ZHUKE on 2016/3/8.
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
        Message users = context.getBean(Message.class);
        users.setName("朱轲");
        System.out.println(users.getName());
    }
}
