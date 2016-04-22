package com.zhuke.smart_home.beans;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ZHUKE on 2016/3/14.
 */
@Entity
public class Device {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
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

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "modified_on")
    private Date modifiedOn;

    private String deviceStatus;

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

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
