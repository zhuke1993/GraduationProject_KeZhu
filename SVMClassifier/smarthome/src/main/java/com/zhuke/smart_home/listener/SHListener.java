package com.zhuke.smart_home.listener;

import com.zhuke.smart_home.central.CentralControl;
import com.zhuke.smart_home.service.DeviceService;
import com.zhuke.smart_home.service.ServerConnService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ZHUKE on 2016/3/8.
 */
public class SHListener extends ContextLoaderListener {
    private static Logger logger = LogManager.getLogger(SHListener.class);

    @Override
    public void contextInitialized(javax.servlet.ServletContextEvent event) {
        try {
            super.contextInitialized(event);
            ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());

            ServerConnService serverConnService = applicationContext.getBean(ServerConnService.class);

            CentralControl centralControl = applicationContext.getBean(CentralControl.class);

            ThreadPoolExecutor threadPoolExecutor = applicationContext.getBean(ThreadPoolExecutor.class);
            threadPoolExecutor.execute(serverConnService);
            threadPoolExecutor.execute(centralControl);

            DeviceService deviceService = applicationContext.getBean(DeviceService.class);
            deviceService.initDeviceStatus();
            deviceService.initBindsDevice();

        } catch (Exception e) {
            logger.error("Start server occured exception", e);
        }
    }
}
