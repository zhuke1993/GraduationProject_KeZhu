package com.zhuke.smart_home.service.impl;

import com.zhuke.smart_home.beans.DeviceStatus;
import com.zhuke.smart_home.beans.SHMessage;
import com.zhuke.smart_home.central.SHConfig;
import com.zhuke.smart_home.service.DeviceService;
import com.zhuke.smart_home.service.MessageService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.Vector;

/**
 * 维护一个队列，存储接受到的控制信息
 * Created by ZHUKE on 2016/3/14.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private Logger logger = LogManager.getLogger(MessageService.class);

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private DeviceService deviceService;


    /**
     * 报文格式：9位数字：前三位发送设备id，中间三位控制的设备id，后三位状态id
     *
     * @param s
     */
    public SHMessage parseMessage(String s) {

        SHMessage message = new SHMessage();
        message.setFromSdId(Long.parseLong(s.substring(0, 3)));
        message.setToSdId(Long.parseLong(s.substring(3, 6)));
        message.setStatusId(Long.parseLong(s.substring(6, 9)));
        return message;
    }


    /**
     * 广播控制报文
     *
     * @param message
     */
    public void broadcastMessage(SHMessage message) {
        for (int i = 0; i < SHConfig.deviceVector.size(); i++) {
            if (SHConfig.deviceVector.get(i).getId().compareTo(message.getToSdId()) == 0) {
                deviceService.updateDeviceStatus(SHConfig.deviceVector.get(i), hibernateTemplate.get(DeviceStatus.class, message.getStatusId()));
                logger.info("接收到控制消息," + message.toString());
            }
        }
    }
}
