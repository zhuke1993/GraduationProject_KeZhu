package com.zhuke.svmclassifier;

import com.zhuke.svmclassifier.service.impl.MessageSendServiceImpl;
import com.zhuke.svmclassifier.service.impl.ServerConnServiceImpl;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ZHUKE on 2016/4/2.
 */
public class ServerConnTest {

    @Test
    public void testServerConn() throws IOException {

        new ServerConnServiceImpl().getServerConn();
        while (true) {
            System.out.println("input:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //String str = br.readLine();
            new MessageSendServiceImpl().sendMessage("001001001");

        }
    }

}
