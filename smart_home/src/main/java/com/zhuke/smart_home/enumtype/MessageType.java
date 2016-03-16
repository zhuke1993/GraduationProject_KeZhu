package com.zhuke.smart_home.enumtype;

/**
 * Created by ZHUKE on 2016/3/15.
 */
public enum MessageType {
    LEARNING("learning"),
    PREDICT("predict");

    private String messageType;

    MessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
