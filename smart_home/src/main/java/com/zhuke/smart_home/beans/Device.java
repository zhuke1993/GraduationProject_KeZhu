package com.zhuke.smart_home.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by ZHUKE on 2016/3/14.
 */
@Entity
public class Device {
    @Column
    @Id
    private Long id;
    @Column
    private Long deviceId;
    @Column
    private String name;
    @Column
    private Long ownerId;
    @Column
    private Date joinDate;
    @Column
    private String deviceType;
    /**
     * 设备UUID号，作为唯一标识
     */
    @Column
    private String uuid;

    @Column
    private Long deviceStatusId;

    public String getDeviceType() {
        return deviceType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Long getDeviceStatusId() {
        return deviceStatusId;
    }

    public void setDeviceStatusId(Long deviceStatusId) {
        this.deviceStatusId = deviceStatusId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

}
