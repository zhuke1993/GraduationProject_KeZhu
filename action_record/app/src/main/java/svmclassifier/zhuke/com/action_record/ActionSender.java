package svmclassifier.zhuke.com.action_record;


import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.StringTokenizer;

/**
 * 动作数据发送线程
 * Created by ZHUKE on 2016/4/17.
 */
public class ActionSender implements Runnable {

    private static Socket socket;
    private static PrintStream printStream;

    @Override
    public void run() {
        try {
            //发送数据
            ActionSender.updateToSendArray();
            ActionSender.sendAction(ActionSender.actionStrBuiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化socket
     */
    public static void initSocket() {
        try {
            socket = new Socket(SVMConfig.serverIP, SVMConfig.serverPort);
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
    public static void sendMessage(String message) {
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
    public static void sendAction(String message) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(SVMConfig.serverActionURL + "?userId=" + SVMConfig.loginUserId + "&action=" + URLEncoder.encode(message, "UTF-8")).openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(200);
        conn.setConnectTimeout(200);
        conn.setUseCaches(false);

        if (conn.getResponseCode() == 200) {
            System.out.println("Success send action data = " + message);
        }
        conn.disconnect();
    }

    /**
     * 更新缓冲区数组
     */
    public static void updateBuffer() throws InterruptedException {
        // 将新接收到的数据存入到temp数组的第temp_state行
        String s = ActionRecorder.getCurrentAction();
        double[] d = actionNormalize(s);
        System.out.println("得到状态值" + s);
        if (d != null) {
            System.arraycopy(d, 0, SVMConfig.ACTION_TEMP[SVMConfig.TEMP_STATE], 0, SVMConfig.FEATURE_NUM);
            SVMConfig.TEMP_STATE = (SVMConfig.TEMP_STATE + 1) % SVMConfig.ACTION_TO_RECORD;
        }
    }

    private static double[] actionNormalize(String action) {
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


    /**
     * 更新待发送数组
     */
    public static void updateToSendArray() {
        if (SVMConfig.TEMP_STATE < SVMConfig.R - SVMConfig.L + SVMConfig.NOISE) {
            //此时需要取历史数据
            int count = 0;
            for (int i = 0, j = SVMConfig.TEMP_STATE; j >= SVMConfig.NOISE; i++, j--) {
                SVMConfig.TO_SEND[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
                count++;
            }
            for (int i = SVMConfig.ACTION_TO_RECORD, j = count; count < SVMConfig.R - SVMConfig.L; i--, j++) {
                SVMConfig.TO_SEND[count] = SVMConfig.ACTION_TEMP[i - 1];
                count++;
            }
        } else {
            //直接存取
            for (int i = 0, j = SVMConfig.TEMP_STATE; i < SVMConfig.R - SVMConfig.L; i++, j--) {
                SVMConfig.TO_SEND[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
            }
        }
    }

    public static String actionStrBuiler() {
        // 将待预测数组进行格式化处理，将各属性值进行调整
        double[] t = new double[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];
        for (int i = 0; i < SVMConfig.FEATURE_NUM; i++) {
            for (int j = 0; j < SVMConfig.R - SVMConfig.L; j++) {
                t[i * (SVMConfig.R - SVMConfig.L) + j] = SVMConfig.TO_SEND[j][i];
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < t.length; i++) {
            sb.append(t[i] + ",");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

}
