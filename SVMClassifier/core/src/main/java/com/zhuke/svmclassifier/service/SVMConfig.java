package com.zhuke.svmclassifier.service;

import com.zhuke.svmclassifier.entity.SVMParam;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_problem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 设置中心
 * Created by ZHUKE on 2016/3/31.
 */
@Service
@Scope("prototype")
public class SVMConfig {

    @Autowired
    private DataSource2SvmProblemService dataSource2SvmProblemService;


    private Logger logger = LogManager.getLogger(SVMConfig.class);

    private Long userId = 0L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public SVMConfig(Long userId) {
        this.userId = userId;
    }

    public SVMConfig() {
    }

    /**
     * 预测线程是否运行中，0-否，1-是
     */
    public int IS_PREDICTING = 0;

    /**
     * 远程服务器的url地址
     */
    public String REMOUT_URL;

    /**
     * 常量model
     */
    public svm_model MODEL;

    /**
     * 手机需要每隔多长得时间向手环发送一个字符“r”，手环返回回该时刻的状态值,单位为ms（1s = 1000ms）
     */
    public int ACTION_TIME;

    /**
     * 预测线程执行的时间片
     */
    public int PREDICT_TIME;

    /**
     * 应用的model文件存放路径
     */
    public String MODELFILE_PATH;

    /**
     * 特征数
     */
    public int FEATURE_NUM;


    /**
     * 记录前2s的动作
     */
    public int ACTION_TO_RECORD;

    /**
     * 从预测数组中取出数据行数的左边界，以当前时刻为时间截点
     */
    public int L;

    /**
     * 从预测数组中取出数据行数的右边界，以当前时刻为时间截点
     */
    public int R;

    /**
     * 从预测数组中取出数据的有边界的延迟
     */
    public int NOISE;

    /**
     * svm_parameter的c的值
     */
    public double C;

    /**
     * svm_parameter的gamma的值
     */
    public double G;


    /**
     * 设置一个temp_state来监视temp数组的情况，即最新的数据到达了哪一行
     */
    public int TEMP_STATE;

    /**
     * 用来记录待预测的数据的数组
     */
    public double[] TO_PERDICT;

    /**
     * 用来记录待学习数据的数组
     */
    public double[] TO_LEARN;

    /**
     * 用来记录前2s的时间内，手环的动作信息状态，当数组满时，会覆盖最旧的数据，维护一个20*6的数组
     * 数组中的每一行的数据都已经进行了归一化处理，且数据格式为ax ay az bx by bz
     */
    public double[][] ACTION_TEMP;

    public synchronized double[] getToPerdict() {
        return TO_PERDICT;
    }

    public synchronized void setToPerdict(double[] toPerdict) {
        System.arraycopy(toPerdict, 0, TO_PERDICT, 0, toPerdict.length);
    }

    public synchronized double[] getToLearn() {
        return TO_LEARN;
    }

    public synchronized void setToLearn(double[] toLearn) {
        System.arraycopy(toLearn, 0, TO_LEARN, 0, toLearn.length);
    }

    public void initConfig() {
        Properties properties = new Properties();
        try {
            if (userId != 0) {
                properties.load(new FileInputStream(SVMConfig.class.getResource("/").getFile() + "svm_classifier.properties"));
                //properties.load(new FileInputStream("D:\\Users\\Administrator\\Documents\\GraduationProject_KeZhu\\SVMClassifier\\web\\target\\web\\WEB-INF\\classes\\svm_classifier.properties"));

                REMOUT_URL = properties.getProperty("conf.REMOUT_URL");
                ACTION_TIME = Integer.parseInt(properties.getProperty("conf.ACTION_TIME"));
                PREDICT_TIME = Integer.parseInt(properties.getProperty("conf.PREDICT_TIME"));
                MODELFILE_PATH = properties.getProperty("conf.MODELFILE_PATH");
                FEATURE_NUM = Integer.parseInt(properties.getProperty("conf.FEATURE_NUM"));
                ACTION_TO_RECORD = Integer.parseInt(properties.getProperty("conf.ACTION_TO_RECORD"));
                L = Integer.parseInt(properties.getProperty("conf.L"));
                R = Integer.parseInt(properties.getProperty("conf.R"));
                NOISE = Integer.parseInt(properties.getProperty("conf.NOISE"));
                C = Double.parseDouble(properties.getProperty("conf.C"));
                G = Double.parseDouble(properties.getProperty("conf.G"));

                TEMP_STATE = 0;
                ACTION_TEMP = new double[ACTION_TO_RECORD][FEATURE_NUM];
                TO_LEARN = new double[(R - L) * FEATURE_NUM];
                TO_PERDICT = new double[(R - L) * FEATURE_NUM];

                //初始化文件夹以及文件
                File modelFile = new File(SVMConfig.class.getResource("/").getFile() + "/" + MODELFILE_PATH);

                try {
                    // 如果这是一个文件夹且不存在的话，则进行新建文件夹
                    if (!modelFile.exists()) {
                        modelFile.createNewFile();
                    }
                } catch (IOException e) {
                    logger.error("Field to create system file.", e);
                }
                svm_problem prob = dataSource2SvmProblemService.readFromDB(userId);
                if (prob.l != 0) {
                    MODEL = svm.svm_train(prob, SVMParam.customize(C, G));
                    svm.svm_save_model(this.getClass().getResource("/").getFile() + MODELFILE_PATH + "_" + userId, MODEL);
                }
                logger.info("Successed to initiation system config for userId = " + userId);
            }
        } catch (IOException e) {
            logger.error("Field to load configuration file.", e);
        }
    }

    public void main(String[] args) throws FileNotFoundException {
        System.out.println(SVMConfig.class.getResource("/").getFile());
        FileInputStream fi = new FileInputStream(SVMConfig.class.getResource("/").getFile() + "/svm_classifier.properties");
    }

}
