package com.zhuke.smart_home.central;

import com.zhuke.smart_home.service.DeviceService;
import com.zhuke.smart_home.service.MessageService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by ZHUKE on 2016/3/14.
 */
@Service
public class CentralControl implements Runnable {

    private Logger logger = LogManager.getLogger(CentralControl.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MessageService messageService;


    public void run() {
        //不断解析收到的控制报文
        while (SHConfig.messageVector.size() > 0) {
            messageService.broadcastMessage(SHConfig.messageVector.get(0));
            SHConfig.messageVector.remove(0);
        }
    }
}
