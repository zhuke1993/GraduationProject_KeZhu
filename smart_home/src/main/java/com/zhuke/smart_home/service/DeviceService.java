package com.zhuke.smart_home.service;

import com.zhuke.smart_home.beans.Device;
import com.zhuke.smart_home.beans.DeviceStatus;

/**
 * Created by ZHUKE on 2016/3/15.
 */
public interface DeviceService {
    /**
     * 初始化设备状态列表
     */
    void initDeviceStatus();

    /**
     * 初始化绑定设备
     */
    void initBindsDevice();

    /**
     * 更新设备状态
     *
     * @param device       设备对象
     * @param deviceStatus 设备状态
     */
    void updateDeviceStatus(Device device, DeviceStatus deviceStatus);


}
