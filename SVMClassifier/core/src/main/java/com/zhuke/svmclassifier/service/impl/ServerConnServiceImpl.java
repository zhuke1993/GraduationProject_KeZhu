package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.service.SVMConfig;
import com.zhuke.svmclassifier.service.ServerConnService;
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

@Service
public class ServerConnServiceImpl implements ServerConnService {

    private static Logger logger = LogManager.getLogger(ServerConnServiceImpl.class);


    public void getServerConn() {
        SocketChannel socketChannel = null;
        Selector selector = null;
        Selector clientSelector = null;

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(SVMConfig.class.getResource("/").getFile() + "/" + "svm_classifier.properties"));
            //properties.load(new FileInputStream("D:\\Users\\Administrator\\Documents\\GraduationProject_KeZhu\\SVMClassifier\\web\\src\\main\\resources\\svm_classifier.properties"));
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(properties.getProperty("server.host"),
                    Integer.parseInt(properties.getProperty("server.port"))));

            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            clientSelector = Selector.open();

            int count = 1;
            while (selector.isOpen() && count <= 3) {
                if (selector.isOpen() && selector.select() > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isConnectable()) {
                            socketChannel.register(clientSelector, SelectionKey.OP_WRITE);
                            socketChannel.finishConnect();
                            selector.close();//完成连接，关闭selector

                            logger.info("已连接到服务器：" + socketChannel.getRemoteAddress().toString());
                            MessageSendServiceImpl.selector = clientSelector;
                            MessageSendServiceImpl.socketChannel = socketChannel;
                            break;
                        }
                    }
                    Thread.sleep(1000);//线程暂停1s等待重连服务器
                    logger.info("连接服务器失败，正在重试第" + count + "次");
                    count++;
                }
            }
        } catch (IOException e) {
            logger.error("连接服务器发生异常", e);
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