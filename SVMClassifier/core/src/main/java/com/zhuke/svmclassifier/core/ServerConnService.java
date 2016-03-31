package com.zhuke.svmclassifier.core;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ServerConnService implements Runnable {
    private static Logger logger = LogManager.getLogger(ServerConnService.class);

    private SocketChannel socketChannel;
    private Selector selector;
    private Selector clientSelector;

    public static void main(String[] args) throws Exception {
        ServerConnService serverConnService = new ServerConnService();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 6000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));

        serverConnService.getServerConn();

        MessageSendService messageSendService = new MessageSendService();
        messageSendService.setSelector(serverConnService.clientSelector);
        messageSendService.setSocketChannel(serverConnService.socketChannel);
        messageSendService.setMessage("服务器你好，我来了！");
        messageSendService.sendMessage();
    }

    public void getServerConn() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(SVMConfig.class.getResource("/").getFile() + "/" + "svm_classifier.properties"));

            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(properties.getProperty("server.host"),
                    Integer.parseInt(properties.getProperty("server.port"))));

            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            clientSelector = Selector.open();

            int count = 0;
            while (selector.isOpen() && count < 3) {
                if (selector.isOpen() && selector.select() > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isConnectable()) {
                            socketChannel.register(clientSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            logger.info("Connected to server," + socketChannel.getRemoteAddress().toString());
                            socketChannel.finishConnect();
                            selector.close();
                            MessageSendService.selector = clientSelector;
                            MessageSendService.socketChannel = socketChannel;
                            break;
                        }
                    }
                    Thread.sleep(3000);
                }
            }
        } catch (IOException e) {
            logger.error(e, e);
            try {
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        getServerConn();
    }
}