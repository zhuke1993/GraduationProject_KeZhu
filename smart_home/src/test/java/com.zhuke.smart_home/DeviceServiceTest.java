package com.zhuke.smart_home;

import com.google.gson.Gson;
import com.zhuke.smart_home.beans.Device;
import com.zhuke.smart_home.service.DeviceService;
import com.zhuke.smart_home.service.impl.DeviceServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ZHUKE on 2016/3/15.
 */
public class DeviceServiceTest extends DefaultSpringTestCase {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    public void initDeviceStatus() {
        ArrayList<Device> devices = (ArrayList<Device>) hibernateTemplate.find("from Device d", null);
        for (int i = 0; i < devices.size(); i++) {
            devices.get(i).setDeviceStatus("closed");
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(devices));
        ;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void addDeviceStatus() {

        Device device1 = new Device();
        device1.setCreatedOn(new Date());
        device1.setDeviceType("UNKNOW");
        device1.setJoinDate(new Date());
        device1.setName("电视机");
        device1.setUuid(UUID.randomUUID().toString());

        Device device = new Device();
        device.setCreatedOn(new Date());
        device.setDeviceType("UNKNOW");
        device.setJoinDate(new Date());
        device.setName("电灯1");
        device.setUuid(UUID.randomUUID().toString());


        Device device2 = new Device();
        device2.setCreatedOn(new Date());
        device2.setDeviceType("UNKNOW");
        device2.setJoinDate(new Date());
        device2.setName("电灯2");
        device2.setUuid(UUID.randomUUID().toString());

        hibernateTemplate.save(device);
        hibernateTemplate.save(device1);
        hibernateTemplate.save(device2);
    }

}
