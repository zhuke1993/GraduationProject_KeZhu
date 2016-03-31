package com.zhuke.svmclassifier.listener;

import com.google.common.base.Stopwatch;
import com.zhuke.svmclassifier.core.SVMConfig;
import com.zhuke.svmclassifier.core.ServerConnService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by ZHUKE on 2016/3/8.
 */
public class SVMClassifierListener extends ContextLoaderListener {
    private static Logger logger = LogManager.getLogger(SVMClassifierListener.class);

    @Override
    public void contextInitialized(javax.servlet.ServletContextEvent event) {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            logger.info("Initializing Spring web application context...");
            super.contextInitialized(event);
            ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
            logger.info("Initializing Spring web application context... done in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms, Bean count: " + applicationContext.getBeanDefinitionCount());

            ServerConnService serverConnService = applicationContext.getBean(ServerConnService.class);
            serverConnService.getServerConn();

            SVMConfig.initConfig();
            logger.info("System configuration initiate success.");

        } catch (Exception e) {
            logger.error("Start server occured exception", e);
        }
    }
}
