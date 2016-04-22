package com.zhuke.svmclassifier.config;

import com.zhuke.svmclassifier.entity.Message;
import com.zhuke.svmclassifier.service.SVMConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ZHUKE on 2016/4/21.
 */
public class SystemConfig {
    public static ConcurrentHashMap<Long, SVMConfig> runningUserMap = new ConcurrentHashMap<Long, SVMConfig>();

    public static Vector<Message> messageVector = new Vector<Message>();

    /**
     * 得到某一userId的所有控制报文消息，并从队列中移除
     * 如果没有消息，则返回null
     *
     * @param userId
     * @return
     */
    public static List<Message> getMessage(Long userId) {
        List<Message> messageList = new ArrayList<Message>();

        for (int i = 0; i < messageVector.size(); i++) {
            if (messageVector.get(i).getUserId() == userId) {
                Message message = messageVector.get(i);
                messageVector.remove(i);
                i--;
                messageList.add(message);
            }
        }
        return messageList.size() == 0 ? null : messageList;
    }

    public static SVMConfig getSVMConfig(Long userId) {
        return runningUserMap.get(userId);
    }

    public static int IS_PREDICTING = 0;
}
