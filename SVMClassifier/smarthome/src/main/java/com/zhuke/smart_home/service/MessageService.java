package com.zhuke.smart_home.service;

import com.zhuke.smart_home.beans.SHMessage;

/**
 * Created by ZHUKE on 2016/4/2.
 */
public interface MessageService {
    public SHMessage parseMessage(String s);

    public void broadcastMessage(SHMessage message);
}
