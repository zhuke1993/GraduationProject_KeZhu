package com.zhuke.svmclassifier;

import com.google.gson.Gson;
import com.zhuke.svmclassifier.config.SystemConfig;
import com.zhuke.svmclassifier.entity.Message;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by ZHUKE on 2016/4/22.
 */

public class MessageTest {

    @Test
    public void testGetMessageTest() {
        Message message = new Message(1l, 12l, new Date());
        SystemConfig.messageVector.add(message);
        message = new Message(1l, 13l, new Date());
        SystemConfig.messageVector.add(message);

        message = new Message(2l, 13l, new Date());
        SystemConfig.messageVector.add(message);

        System.out.println("before:" + new Gson().toJson(SystemConfig.messageVector));

        List<Message> message1 = SystemConfig.getMessage(1l);

        System.out.println("get:" + new Gson().toJson(message1));

        System.out.println("after:" + new Gson().toJson(SystemConfig.messageVector));
    }

}
