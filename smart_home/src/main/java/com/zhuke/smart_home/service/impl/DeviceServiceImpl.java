package com.zhuke.smart_home.service.impl;

import com.zhuke.smart_home.beans.Device;
import com.zhuke.smart_home.beans.DeviceStatus;
import com.zhuke.smart_home.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by ZHUKE on 2016/3/15.
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    public static ArrayList<DeviceStatus> deviceStatuses = new ArrayList<DeviceStatus>();

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void initDeviceStatus() {
        ArrayList<DeviceStatus> deviceStatusList = (ArrayList<DeviceStatus>) hibernateTemplate.find("from DeviceStatus", null);
        DeviceServiceImpl.deviceStatuses = deviceStatusList;
    }

    public void initBindsDevice() {

    }

    public void updateDeviceStatus(Device device, DeviceStatus deviceStatus) {

    }
}
