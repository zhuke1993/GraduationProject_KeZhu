package com.zhuke.smart_home.service.impl;

import com.zhuke.smart_home.beans.Device;
import com.zhuke.smart_home.beans.DeviceStatus;
import com.zhuke.smart_home.central.SHConfig;
import com.zhuke.smart_home.service.DeviceService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by ZHUKE on 2016/3/15.
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    private Logger logger = LogManager.getLogger(DeviceService.class);

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void initDeviceStatus() {
        ArrayList<DeviceStatus> deviceStatusList = (ArrayList<DeviceStatus>) hibernateTemplate.find("from DeviceStatus", null);
        SHConfig.deviceStatusVector = new Vector<DeviceStatus>(deviceStatusList);
        logger.info("初始化设备状态列表成功。");
    }

    public void initBindsDevice() {
        ArrayList<Device> deviceList = (ArrayList<Device>) hibernateTemplate.find("from Device", null);
        SHConfig.deviceVector = new Vector<Device>(deviceList);
        logger.info("初始化设备列表成功。");
    }

    public void updateDeviceStatus(Device device, DeviceStatus deviceStatus) {
        device.setDeviceStatus(deviceStatus.getCode());
    }


    @Transactional
    public void registerDevice(Device device) {
        SHConfig.deviceVector.add(device);
        hibernateTemplate.save(device);
    }

    @Transactional
    public void deleteDevice(Long deviceId) {
        for (int i = 0; i < SHConfig.deviceVector.size(); i++) {
            if (SHConfig.deviceVector.get(i).getDeviceId() == deviceId) {
                SHConfig.deviceVector.remove(i);
                hibernateTemplate.delete(SHConfig.deviceVector.remove(i));
            }
        }
    }
}
