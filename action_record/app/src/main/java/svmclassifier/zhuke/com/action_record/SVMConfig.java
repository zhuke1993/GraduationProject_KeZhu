package svmclassifier.zhuke.com.action_record;

/**
 * Created by ZHUKE on 2016/4/17.
 */
public class SVMConfig {

    static String serverIP = "zhuke1993.vicp.cc";

    static int serverPort = 8999;

    static String serverActionURL = "http://zhuke1993.vicp.cc:8081/svmclassifier/action_record.do";

    public static String serverLoginURL = "http://zhuke1993.vicp.cc:8081/svmclassifier/login.do";

    public static String serverRegisterURL = "http://zhuke1993.vicp.cc:8081/svmclassifier/register.do";

    static long threadTime = 100;

    public static boolean isUpdateBuffer = false;

    public static int loginUserId = 0;

    public static String loginUserName;

    /**
     * 特征数
     */
    public static int FEATURE_NUM = 8;


    /**
     * 记录前2s的动作
     */
    public static int ACTION_TO_RECORD = 20;

    /**
     * 从预测数组中取出数据行数的左边界，以当前时刻为时间截点
     */
    public static int L = 10;

    /**
     * 从预测数组中取出数据行数的右边界，以当前时刻为时间截点
     */
    public static int R = 20;

    /**
     * 从预测数组中取出数据的有边界的延迟
     */
    public static int NOISE = 5;


    /**
     * 设置一个temp_state来监视temp数组的情况，即最新的数据到达了哪一行
     */
    public static int TEMP_STATE = 0;

    /**
     * 用来记录待学习数据的数组
     */
    public static double[][] TO_SEND = new double[SVMConfig.R - SVMConfig.L][SVMConfig.FEATURE_NUM];

    /**
     * 用来记录前2s的时间内，手环的动作信息状态，当数组满时，会覆盖最旧的数据，维护一个20*6的数组
     * 数组中的每一行的数据都已经进行了归一化处理，且数据格式为ax ay az bx by bz
     */
    public static double[][] ACTION_TEMP = new double[SVMConfig.ACTION_TO_RECORD][SVMConfig.FEATURE_NUM];

    public static String action_f = "/mnt/sdcard/action_record/action.txt";
    public static String appDir = "/mnt/sdcard/action_record";

    public static void init() {
        FileUtil.iniFile();
    }
}
