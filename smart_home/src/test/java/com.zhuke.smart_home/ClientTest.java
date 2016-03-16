package com.zhuke.smart_home;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ClientTest {

    public static void main(String[] args) throws Exception {
        client();
    }

    public static void client() {
        SocketChannel channel = null;
        try {

            Selector selector = Selector.open();
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(8888));
            channel.register(selector, SelectionKey.OP_CONNECT);

            while (true) {
                if (selector.select() > 0) {

                    Iterator<SelectionKey> set = selector.selectedKeys().iterator();
                    while (set.hasNext()) {
                        SelectionKey key = set.next();
                        set.remove();

                        SocketChannel ch = (SocketChannel) key.channel();
                        if (key.isConnectable()) {
                            ch.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            ch.finishConnect();
                        }

                        if (key.isReadable()) {
                            ByteArrayOutputStream output = new ByteArrayOutputStream();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int len = 0;
                            while ((len = ch.read(buffer)) != 0) {
                                buffer.flip();
                                byte by[] = new byte[buffer.remaining()];
                                buffer.get(by);
                                output.write(by);
                                buffer.clear();
                            }
                            System.out.println(new String(output.toByteArray()));
                            output.close();
                        }

                        if (key.isWritable()) {
                            ch.write(ByteBuffer.wrap((("client say:hi")).getBytes()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class ClientRunnable implements Runnable {

        private SocketChannel ch;

        private ClientRunnable(SocketChannel ch) {
            this.ch = ch;
        }

        public void run() {
            try {
                while (true) {
                    ch.write(ByteBuffer.wrap((("client say:hi")).getBytes()));
                    Thread.sleep(100000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    ch.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


}
