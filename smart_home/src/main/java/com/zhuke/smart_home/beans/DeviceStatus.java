package com.zhuke.smart_home.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 设备状态
 * Created by ZHUKE on 2016/3/14.
 */
@Entity
@Table(name = "device_status")
public class DeviceStatus {

    @Column
    @Id
    private Long id;

    @Column
    private String code;

    @Column
    private String value;

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

    @Override
    public String toString() {
        return "DeviceStatus{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
