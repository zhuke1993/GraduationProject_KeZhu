package com.zhuke.svmclassifier.core;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by ZHUKE on 2016/3/31.
 */
@Service
public class MessageSendService {

    private Logger logger = LogManager.getLogger(MessageSendService.class);

    public static Selector selector;
    public static SocketChannel socketChannel;

    public void sendMessage(String message) {

        try {
            Assert.notNull(selector);
            Assert.notNull(socketChannel);
            Assert.notNull(message);
        } catch (Exception e) {
            logger.error("向服务器发送消息时异常，未连接到服务器", e);
            ServerConnService.getServerConn();
        }

        try {
            if (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isWritable()) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.put(message.getBytes());
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        logger.info("已发送一条消息至服务器，内容 = " + message);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Occured a error." + e);
        }
    }
}
