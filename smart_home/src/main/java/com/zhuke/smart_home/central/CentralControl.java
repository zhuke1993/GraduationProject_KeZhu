package com.zhuke.smart_home.central;

import com.zhuke.smart_home.beans.Device;
import com.zhuke.smart_home.beans.Message;
import com.zhuke.smart_home.enumtype.MessageType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ZHUKE on 2016/3/14.
 */
@Controller
public class CentralControl {

    private Logger logger = LogManager.getLogger(CentralControl.class);

    private CopyOnWriteArrayList<Device> smartDeviceList = new CopyOnWriteArrayList<Device>();

    public void broadcastMessage(Message message) {
        for (int i = 0; i < smartDeviceList.size(); i++) {
            if (smartDeviceList.get(i).getDeviceId() == message.getToSdId()
                    && message.getType().equals(MessageType.PREDICT.getMessageType())) {
               //todo smartDeviceList.get(i).update(message);
            }
        }
    }

    public void learning() {
        //// TODO: 2016/3/15

    }

    public void registerDevice(Device device) {
        smartDeviceList.add(device);
    }

    public void deleteDevice(Device device) {
        for (int i = 0; i < smartDeviceList.size(); i++) {
            if (smartDeviceList.get(i).getDeviceId() == device.getDeviceId()) {
                smartDeviceList.remove(i);
            }
        }
    }

}
