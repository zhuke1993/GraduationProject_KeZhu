package com.zhuke.smart_home.service.impl;

import com.zhuke.smart_home.beans.SHMessage;
import com.zhuke.smart_home.central.SHConfig;
import com.zhuke.smart_home.service.MessageService;
import com.zhuke.smart_home.service.ServerConnService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ServerConnServiceImpl implements ServerConnService {
    Logger logger = LogManager.getLogger(ServerConnService.class);

    @Autowired
    private MessageService messageService;

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
            logger.info("......服务器启动成功，等待客户端连接......");
            while (true) {
                if (selector.select() > 0) {
                    Set<SelectionKey> set = selector.selectedKeys();
                    Iterator<SelectionKey> it = set.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();

                        if (key.isAcceptable()) {
                            SocketChannel serverChanel = server.accept();
                            logger.info("收到一个新连接:" + serverChanel.socket().getRemoteSocketAddress());
                            serverChanel.configureBlocking(false);
                            serverChanel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        }

                        try {
                            if (key.isReadable()) {
                                SocketChannel serverChanel = (SocketChannel) key.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                serverChanel.read(byteBuffer);
                                byteBuffer.flip();
                                logger.info("收到客户端：" + ((SocketChannel) key.channel()).socket().getRemoteSocketAddress() +
                                        "的消息：" + new String(byteBuffer.array()));
                                SHMessage message = messageService.parseMessage(new String(byteBuffer.array(), 0, byteBuffer.limit()));
                                SHConfig.messageVector.add(message);
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
