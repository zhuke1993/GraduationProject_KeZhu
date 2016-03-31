package com.zhuke.smart_home.service.impl;

import com.zhuke.smart_home.service.ServerConnService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ZHUKE on 2016/3/12.
 */
@Service
public class ServerConnServiceImpl implements ServerConnService, Runnable {
    Logger logger = LogManager.getLogger(ServerConnService.class);

    public static void main(String[] args) {
        new ServerConnServiceImpl().startServer();
    }

    public void startServer() {
        ServerSocketChannel server = null;
        try {
            Selector selector = Selector.open();
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(8999));
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("Server started, waiting for new connection.");
            while (true) {
                if (selector.select() > 0) {
                    Set<SelectionKey> set = selector.selectedKeys();
                    Iterator<SelectionKey> it = set.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();

                        if (key.isAcceptable()) {
                            SocketChannel serverChanel = server.accept();
                            logger.info("A new client was connected , address:" + serverChanel.socket().getRemoteSocketAddress());
                            serverChanel.configureBlocking(false);
                            serverChanel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        }

                        try {
                            if (key.isReadable()) {
                                SocketChannel serverChanel = (SocketChannel) key.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                serverChanel.read(byteBuffer);
                                byteBuffer.clear();
                                logger.info("Received a new message:" + new String(byteBuffer.array()));
                            }
                        } catch (IOException e) {
                            logger.error("Sever occured a error", e);
                            key.cancel();
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Server occured a exception", e);
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    logger.error("Sever channel closed faild!", e);
                    e.printStackTrace();
                }
            }
        }
    }

    public void run() {
        startServer();
    }
}
