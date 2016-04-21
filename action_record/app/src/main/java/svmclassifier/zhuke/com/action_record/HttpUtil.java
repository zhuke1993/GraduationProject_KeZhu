package svmclassifier.zhuke.com.action_record;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ZHUKE on 2016/4/21.
 */
public class HttpUtil implements Runnable {

    private static String url;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        HttpUtil.url = url;
    }

    public static void login(final String name, final String password) throws IOException {
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.setUrl(SVMConfig.serverLoginURL + "?username=" + URLEncoder.encode(name, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
        Thread thread = new Thread(httpUtil);
        thread.start();
    }

    @Override
    public void run() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(200);
            conn.setConnectTimeout(200);
            conn.setUseCaches(false);

            if (conn.getResponseCode() == 200) {
                System.out.println("Success send action  " + url);
                BufferedReader bufr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String result = bufr.readLine();
                if (result == null || result.equals("") || result.equals("FAILED")) {
                } else {
                    SVMConfig.loginUserId = Integer.parseInt(result);
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
