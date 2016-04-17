package svmclassifier.zhuke.com.action_record;


import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

/**
 * 动作数据发送线程
 * Created by ZHUKE on 2016/4/17.
 */
public class ActionSender implements Runnable {

    private static Socket socket;
    private static PrintStream printStream;

    @Override
    public void run() {
        while (true) {
            try {
                sendMessage(ActionRecorder.getCurrentAction());
                Thread.sleep(Config.threadTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化socket
     */
    private void initSocket() {
        try {
            socket = new Socket(Config.serverIP, Config.serverPort);
            printStream = new PrintStream(socket.getOutputStream(), true, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 采用socket连接的方式发送数据，保证了数据到达的时效性
     *
     * @param message
     */
    private void sendMessage(String message) {
        if (socket == null) {
            initSocket();
        }
        printStream.print(message);
    }

    /**
     * 采用http请求的方式发送数据，时效性保证的精确性不高，服务器收到请求的时间不一
     *
     * @throws IOException
     */
    private void sendAction() throws IOException {
        String currentAction = ActionRecorder.getCurrentAction();

        HttpURLConnection conn = (HttpURLConnection) new URL(Config.serverURL + "?action=" + currentAction).openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(200);
        conn.setConnectTimeout(200);
        conn.setUseCaches(false);

        if (conn.getResponseCode() == 200) {
            System.out.println("Success send action data = " + currentAction);
        }
        conn.disconnect();
    }

}
