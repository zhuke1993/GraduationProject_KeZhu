package com.zhuke.smart_home.central;

import com.zhuke.smart_home.beans.Device;
import com.zhuke.smart_home.beans.DeviceStatus;
import com.zhuke.smart_home.beans.SHMessage;

import java.util.Vector;

/**
 * Created by ZHUKE on 2016/4/2.
 */
public class SHConfig {
    /**
     * 设备列表
     */
    public static Vector<Device> deviceVector = new Vector<Device>(16);


    /**
     * 设备状态列表
     */
    public static Vector<DeviceStatus> deviceStatusVector = new Vector<DeviceStatus>(16);

    /**
     * 消息列表
     */
    public static Vector<SHMessage> messageVector = new Vector<SHMessage>(512);
}
