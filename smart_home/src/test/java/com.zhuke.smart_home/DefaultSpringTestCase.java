package com.zhuke.smart_home;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ZHUKE on 2016/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:ApplicationContext.xml")
public class DefaultSpringTestCase {
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Test
    public void test1() {
        System.out.println("--------------------");
        System.out.println(threadPoolExecutor.getActiveCount());

    }
}
