package com.zhuke.smart_home.service.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ClientTest {
    private static Logger logger = LogManager.getLogger(ClientTest.class);

    public static void main(String[] args) throws Exception {
        ClientTest.client();
    }

    public static void client() {
        SocketChannel socketChannel = null;
        try {
            Selector selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (true) {
                if (selector.select() > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isConnectable()) {
                            logger.info("Connected to server");
                            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            socketChannel.finishConnect();
                        }

                        if (key.isWritable()) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            byteBuffer.clear();
                            byteBuffer.put("hello".getBytes());
                            Thread.sleep(1000);
                            System.out.println("Send a new message to server:" + "hello");
                            byteBuffer.flip();
                            socketChannel.write(byteBuffer);
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}