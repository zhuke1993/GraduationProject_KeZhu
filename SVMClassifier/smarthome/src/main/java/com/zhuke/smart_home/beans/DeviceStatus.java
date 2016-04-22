package com.zhuke.smart_home.beans;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 设备状态
 * Created by ZHUKE on 2016/3/14.
 */
@Entity
@Table(name = "device_status")
public class DeviceStatus {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column
    private String code;

    @Column
    private String value;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "modified_on")
    private Date modifiedOn;

    public DeviceStatus(Long id, String code, String value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    public DeviceStatus() {
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
