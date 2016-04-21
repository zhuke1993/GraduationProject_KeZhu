package com.zhuke.svmclassifier.listener;

import com.zhuke.svmclassifier.service.SVMConfig;
import com.zhuke.svmclassifier.service.impl.DataRecordServiceImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.concurrent.ThreadPoolExecutor;

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

           /* ServerConnService serverConnService = applicationContext.getBean(ServerConnService.class);
            serverConnService.getServerConn();*/
            DataRecordServiceImpl serverConnService = applicationContext.getBean(DataRecordServiceImpl.class);
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) applicationContext.getBean("threadPoolExecutor");
            threadPoolExecutor.execute(serverConnService);
            SVMConfig svmConfig = applicationContext.getBean(SVMConfig.class);
            svmConfig.initConfig();
        } catch (Exception e) {
            logger.error("初始化启动tomcat容器异常", e);
        }
    }
}
