package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.service.DataRecordService;
import com.zhuke.svmclassifier.service.SVMConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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

    /*public static void main(String[] args) {
        int[][] d = new int[10][8];
        int[] e = new int[8];
        Arrays.fill(e, new Integer(1));
        Arrays.fill(d, e);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(d[i][j] + " ");
            }
            System.out.println();
        }
        int[] f = new int[80];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                f[i * 10 + j] = d[j][i];
            }
        }
        for (int i = 0; i < 80; i++) {
            System.out.print(f[i]+" ");
        }
    }*/

    public void dataRecieve(String acc) {

        // 将新接收到的数据存入到temp数组的第temp_state行
       /* double[] d = actionNormalize(acc);
        if (d != null && d.length % SVMConfig.FEATURE_NUM == 0) {
            for (int i = 0; i < d.length / SVMConfig.FEATURE_NUM; i++) {
                try {
                    System.arraycopy(d, i * SVMConfig.FEATURE_NUM, SVMConfig.ACTION_TEMP[SVMConfig.TEMP_STATE], 0, SVMConfig.FEATURE_NUM);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SVMConfig.TEMP_STATE = (SVMConfig.TEMP_STATE + 1) % SVMConfig.ACTION_TO_RECORD;
            }
        }*/
        logger.info("得到客户端发送的请求：action = " + acc);
        StringTokenizer st = new StringTokenizer(acc, ",");
        int count = st.countTokens();
        double[] d = new double[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];
        for (int i = 0; i < count; i++) {
            d[i] = Double.parseDouble(st.nextToken());
        }
        SVMConfig.setActionArray(d);
    }

    private double[] actionNormalize(String action) {
        try {
            StringTokenizer st = new StringTokenizer(action, ",~");
            int count = st.countTokens();
            double[] d = new double[count];
            for (int i = 0; i < count; i++) {
                d[i] = Double.parseDouble(st.nextToken());
                //对方向传感器的值进行归一化处理
                if (i == 3) {
                    d[i] = new BigDecimal(d[i] / 360).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                } else if (i == 4) {
                    d[i] = new BigDecimal(d[i] / 180).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
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
                                    dataRecieve(new String(byteBuffer.array(), 0, byteBuffer.limit()));
                                } else {
                                    key.cancel();
                                }
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
