package com.zhuke.smart_home.beans;

import java.util.Date;

/**
 * 控制信息报文
 * Created by ZHUKE on 2016/3/8.
 */
public class SHMessage {
    private Long id;
    private Long fromSdId;
    private Long toSdId;
    private Long statusId;
    private Date sendDate;
    /**
     * 消息类型，learning & predict
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromSdId() {
        return fromSdId;
    }

    public void setFromSdId(Long fromSdId) {
        this.fromSdId = fromSdId;
    }

    public Long getToSdId() {
        return toSdId;
    }

    public void setToSdId(Long toSdId) {
        this.toSdId = toSdId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @Override
    public String toString() {
        return "SHMessage{" +
                "id=" + id +
                ", fromSdId=" + fromSdId +
                ", toSdId=" + toSdId +
                ", statusId=" + statusId +
                ", sendDate=" + sendDate +
                ", type='" + type + '\'' +
                '}';
    }
}
