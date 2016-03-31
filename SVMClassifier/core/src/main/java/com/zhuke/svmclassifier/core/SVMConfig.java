package com.zhuke.svmclassifier.core;

import libsvm.svm_model;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 设置中心
 * Created by ZHUKE on 2016/3/31.
 */
public class SVMConfig {

    private static Logger logger = LogManager.getLogger(SVMConfig.class);

    /**
     * 远程服务器的url地址
     */
    public static String REMOUT_URL;

    /**
     * 常量model
     */
    public static svm_model MODEL;

    /**
     * 手机需要每隔多长得时间向手环发送一个字符“r”，手环返回回该时刻的状态值,单位为ms（1s = 1000ms）
     */
    public static int ACTION_TIME;

    /**
     * 预测线程执行的时间片
     */
    public static int PREDICT_TIME;


    /**
     * 应用的model文件存放路径
     */
    public static String MODELFILE_PATH;


    /**
     * 特征数
     */
    public static int FEATURE_NUM;


    /**
     * 记录前2s的动作
     */
    public static int ACTION_TO_RECORD;

    /**
     * 从预测数组中取出数据行数的左边界，以当前时刻为时间截点
     */
    public static int L;

    /**
     * 从预测数组中取出数据行数的右边界，以当前时刻为时间截点
     */
    public static int R;

    /**
     * svm_parameter的c的值
     */
    public static double C;

    /**
     * svm_parameter的gamma的值
     */
    public static double G;


    /**
     * 设置一个temp_state来监视temp数组的情况，即最新的数据到达了哪一行
     */
    public static int TEMP_STATE;

    /**
     * 用来记录待预测的数据的数组
     */
    public static double[][] TO_PERDICT;

    /**
     * 用来记录前2s的时间内，手环的动作信息状态，当数组满时，会覆盖最旧的数据，维护一个20*6的数组
     * 数组中的每一行的数据都已经进行了归一化处理，且数据格式为ax ay az bx by bz
     */
    public static double[][] ACTION_TEMP;

    /**
     * 即将被训练的数据存放文件地址
     */
    public static String ACTION_TO_PREDICT_F;


    public static void initConfig() {
        Properties properties = new Properties();
        try {
            properties.load(SVMConfig.class.getResourceAsStream("/svm_classifier.properties"));

            REMOUT_URL = properties.getProperty("conf.REMOUT_URL");
            ACTION_TIME = Integer.parseInt(properties.getProperty("conf.ACTION_TIME"));
            PREDICT_TIME = Integer.parseInt(properties.getProperty("conf.PREDICT_TIME"));
            MODELFILE_PATH = properties.getProperty("conf.MODELFILE_PATH");
            FEATURE_NUM = Integer.parseInt(properties.getProperty("conf.FEATURE_NUM"));
            ACTION_TO_RECORD = Integer.parseInt(properties.getProperty("conf.ACTION_TO_RECORD"));
            L = Integer.parseInt(properties.getProperty("conf.L"));
            R = Integer.parseInt(properties.getProperty("conf.R"));
            C = Integer.parseInt(properties.getProperty("conf.C"));
            G = Double.parseDouble(properties.getProperty("conf.G"));
            ACTION_TO_PREDICT_F = properties.getProperty("conf.ACTION_TO_PREDICT_F");

            TEMP_STATE = 0;
            TO_PERDICT = new double[R - L][FEATURE_NUM];
            ACTION_TEMP = new double[ACTION_TO_RECORD][FEATURE_NUM];


            //初始化文件夹以及文件
            File tempFile = new File(SVMConfig.class.getResource("/").getFile() + "/" + SVMConfig.ACTION_TO_PREDICT_F);
            File modelFile = new File(SVMConfig.class.getResource("/").getFile() + "/" + SVMConfig.MODELFILE_PATH);
            try {
                // 如果这是一个文件夹且不存在的话，则进行新建文件夹
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                }
                if (!modelFile.exists()) {
                    modelFile.createNewFile();
                }
            } catch (IOException e) {
                logger.error("Field to create system file.", e);
            }

        } catch (IOException e) {
            logger.error("Field to load configuration file.", e);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(SVMConfig.class.getResource("/").getFile());
        FileInputStream fi = new FileInputStream(SVMConfig.class.getResource("/").getFile() + "/svm_classifier.properties");
    }

}
