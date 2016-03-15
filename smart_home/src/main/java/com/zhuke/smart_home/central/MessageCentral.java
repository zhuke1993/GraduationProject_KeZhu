package com.zhuke.smart_home.central;

import com.zhuke.smart_home.beans.Message;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 维护一个队列，存储接受到的控制信息
 * Created by ZHUKE on 2016/3/14.
 */
public class MessageCentral {
    public static CopyOnWriteArrayList<Message> messages = new CopyOnWriteArrayList<Message>();
}
