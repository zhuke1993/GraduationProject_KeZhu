package com.zhuke.smart_home.listener;

import org.springframework.web.context.ContextLoaderListener;

/**
 * Created by ZHUKE on 2016/3/8.
 */
public class SHListener extends ContextLoaderListener{
    @Override
    public void contextInitialized(javax.servlet.ServletContextEvent event) {
        super.contextInitialized(event);
    }
}
