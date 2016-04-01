package com.zhuke.svmclassifier.listener;

import com.zhuke.svmclassifier.service.SVMConfig;
import com.zhuke.svmclassifier.service.ServerConnService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Created by ZHUKE on 2016/3/8.
 */
public class SVMClassifierListener extends ContextLoaderListener {
    private static Logger logger = LogManager.getLogger(SVMClassifierListener.class);

    @Override
    public void contextInitialized(javax.servlet.ServletContextEvent event) {
        try {
            super.contextInitialized(event);
            ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());

            ServerConnService serverConnService = applicationContext.getBean(ServerConnService.class);
            serverConnService.getServerConn();

            SVMConfig.initConfig();
            logger.info("系统设置初始化成功。");

        } catch (Exception e) {
            logger.error("初始化启动tomcat容器异常", e);
        }
    }
}
