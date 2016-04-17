package com.zhuke.smart_home;


import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * 动作数据发送线程
 * Created by ZHUKE on 2016/4/17.
 */
public class ActionSender implements Runnable {

    private static Socket socket;
    private static String serverIP = "zhuke1993.vicp.cc";
    private static int serverPort = 8999;
    private static PrintStream printStream;


    public static void main(String[] args) throws IOException, InterruptedException {
        ActionSender actionSender = new ActionSender();
        actionSender.initSocket();
        while (true) {
            actionSender.sendMessage("zhuke");
            Thread.sleep(200);
        }
    }

    private static String serverURL = "http://222.182.110.12:8080/action_record.do";

    /**
     * 采用http请求的方式发送数据，时效性保证的精确性不高，服务器收到请求的时间不一
     *
     * @throws IOException
     */
    private void sendAction() throws IOException {
        String currentAction = "";

        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL + "?action=" + currentAction).openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(200);
        conn.setConnectTimeout(200);
        conn.setUseCaches(false);

        if (conn.getResponseCode() == 200) {
            System.out.println("Success send action data = " + currentAction);
        }
        conn.disconnect();
    }

    public void run() {
        while (true) {
            try {
                sendAction();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void initSocket() {
        try {
            socket = new Socket(serverIP, serverPort);
            printStream = new PrintStream(socket.getOutputStream(), true, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 采用socket连接的方式发送数据
     *
     * @param message
     */
    private void sendMessage(String message) {
        printStream.print(message);
    }
}
