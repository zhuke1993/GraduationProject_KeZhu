package com.zhuke.smart_home.enumtype;

/**
 * 设备类型
 * Created by ZHUKE on 2016/3/15.
 */
public enum DeviceType {
    SMARTDEVICE(1L, "智能家电"),
    WRISTBANDS(2L, "动作采集装置");
    private Long id;
    private String value;

    DeviceType(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
