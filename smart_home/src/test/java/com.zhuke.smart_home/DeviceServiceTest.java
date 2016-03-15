package com.zhuke.smart_home;

import com.zhuke.smart_home.service.DeviceService;
import com.zhuke.smart_home.service.impl.DeviceServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ZHUKE on 2016/3/15.
 */
public class DeviceServiceTest extends DefaultSpringTestCase {
    @Autowired
    private DeviceService deviceService;

    @Test
    public void initDeviceStatus() {
        deviceService.initDeviceStatus();
        System.out.println("开始初始化设备状态列表");
        for (int i = 0; i < DeviceServiceImpl.deviceStatuses.size(); i++) {
            System.out.println(DeviceServiceImpl.deviceStatuses.get(i));
        }
    }

}
