package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.service.DataRecordService;
import com.zhuke.svmclassifier.service.SVMConfig;
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
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 数据接收中心
 * Created by ZHUKE on 2016/3/31.
 */
@Service
public class DataRecordServiceImpl implements DataRecordService {

    Logger logger = LogManager.getLogger(DataRecordServiceImpl.class);

    public static void main(String[] args) {
        new DataRecordServiceImpl().startServer();
    }

    public void dataRecieve(String acc) {
        // Conf.temp_state自增
        SVMConfig.TEMP_STATE++;
        // temp数组为一个循环队列，当temp_state的值超过最大值的时候，需要从0开始
        if (SVMConfig.TEMP_STATE == 20) {
            SVMConfig.TEMP_STATE = SVMConfig.TEMP_STATE % SVMConfig.ACTION_TO_RECORD;
        }
        // 将新接收到的数据存入到temp数组的第temp_state行
        double[] d = actionNormalize(acc);
        if (d != null && d.length == SVMConfig.FEATURE_NUM) {
            SVMConfig.ACTION_TEMP[SVMConfig.TEMP_STATE] = d;
        }
    }

    private double[] actionNormalize(String action) {
        try {
            StringTokenizer st = new StringTokenizer(action, ",");
            double[] d = new double[st.countTokens()];
            for (int i = 0; i < st.countTokens(); i++) {
                d[i] = Double.parseDouble(st.nextToken());
            }
            return d;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startServer() {
        ServerSocketChannel server = null;
        try {
            Selector selector = Selector.open();
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(8999));
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("......动作数据接收服务器启动成功，等待客户端连接......");
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
                                if (byteBuffer.limit() != 0) {
                                    logger.info("收到客户端：" + ((SocketChannel) key.channel()).socket().getRemoteSocketAddress() + "的消息：" + new String(byteBuffer.array(), 0, byteBuffer.limit()));
                                } else {
                                    key.cancel();
                                }
                                dataRecieve(new String(byteBuffer.array(), 0, byteBuffer.limit()));
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
