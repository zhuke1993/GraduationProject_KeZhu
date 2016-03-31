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
public class MessageSendService implements Runnable {

    private Logger logger = LogManager.getLogger(MessageSendService.class);

    public static Selector selector;
    public static SocketChannel socketChannel;
    private String message;

    public void sendMessage() {
        Assert.notNull(selector);
        Assert.notNull(socketChannel);
        Assert.notNull(message);

        try {
            while (true) {
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
                            logger.info("Send a message to server, message = " + message);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Occured a error." + e);
        }
    }

    public void run() {
        sendMessage();
    }


    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
