package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.service.MessageSendService;
import com.zhuke.svmclassifier.service.PredictService;
import com.zhuke.svmclassifier.service.SVMConfig;
import libsvm.svm_node;
import libsvm.svm;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预测线程，不断预测传来的数据
 *
 * @author ZHUKE
 */
@Service
public class PredictServiceImpl implements PredictService {
    private Logger logger = LogManager.getLogger(PredictServiceImpl.class);

    @Autowired
    private MessageSendService messageSendService;

    /*public static void main(String[] args) {
        double[][] d = new double[20][8];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 8; j++) {
                d[i][j] = i * j + j;
                System.out.print(d[i][j] + " ");
            }
            System.out.println();
        }

        SVMConfig.TEMP_STATE = 10;
        SVMConfig.ACTION_TO_RECORD = 20;
        SVMConfig.R = 20;
        SVMConfig.L = 10;
        SVMConfig.NOISE = 5;
        SVMConfig.ACTION_TEMP = d;
        SVMConfig.TO_PERDICT = new double[10][8];

        if (SVMConfig.TEMP_STATE < SVMConfig.R - SVMConfig.L + SVMConfig.NOISE) {
            //此时需要取历史数据
            int count = 0;
            for (int i = 0, j = SVMConfig.TEMP_STATE; j >= SVMConfig.NOISE; i++, j--) {
                SVMConfig.TO_PERDICT[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
                count++;
            }
            for (int i = SVMConfig.ACTION_TO_RECORD, j = count; count < SVMConfig.R - SVMConfig.L; i--, j++) {
                SVMConfig.TO_PERDICT[count] = SVMConfig.ACTION_TEMP[i - 1];
                count++;
            }
        } else {
            //直接存取
            for (int i = 0, j = SVMConfig.TEMP_STATE; i < SVMConfig.R - SVMConfig.L; i++, j--) {
                SVMConfig.TO_PERDICT[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
            }
        }

        System.out.println("------------------------------");

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(SVMConfig.TO_PERDICT[i][j] + " ");
            }
            System.out.println();
        }

    }*/

    public void predict() {
        logger.info("..........预测线程已启动..........");

        while (SVMConfig.IS_PREDICTING == 1) {

            if (SVMConfig.TEMP_STATE < SVMConfig.R - SVMConfig.L + SVMConfig.NOISE) {
                //此时需要取历史数据
                int count = 0;
                for (int i = 0, j = SVMConfig.TEMP_STATE; j >= SVMConfig.NOISE; i++, j--) {
                    SVMConfig.TO_PERDICT[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
                    count++;
                }
                for (int i = SVMConfig.ACTION_TO_RECORD, j = count; count < SVMConfig.R - SVMConfig.L; i--, j++) {
                    SVMConfig.TO_PERDICT[count] = SVMConfig.ACTION_TEMP[i - 1];
                    count++;
                }
            } else {
                //直接存取
                for (int i = 0, j = SVMConfig.TEMP_STATE; i < SVMConfig.R - SVMConfig.L; i++, j--) {
                    SVMConfig.TO_PERDICT[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
                }
            }

            // 将待预测数组进行格式化处理，将各属性值进行调整
            double[] t = new double[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];

            for (int i = 0; i < SVMConfig.FEATURE_NUM; i++) {
                for (int j = 0; j < SVMConfig.R - SVMConfig.L; j++) {
                    t[i * SVMConfig.FEATURE_NUM + j] = SVMConfig.TO_PERDICT[j][i];
                }
            }
            // 将该数组填充进node数组，进行预测
            svm_node[] nodes = new svm_node[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new svm_node();
                nodes[i].index = i + 1;
                nodes[i].value = t[i];
            }
            // 进行预测,得出预测值
            double result = svm.svm_predict(SVMConfig.MODEL, nodes);

            // 向局域网内的所有的家电广播得到的结果值
            //messageSendService.sendMessage(String.valueOf(result));

            System.out.println("----------------------------");
            System.out.println("|" +
                    "|得到预测值" + result +
                    "|" +
                    "|");
            try {
                Thread.sleep(SVMConfig.PREDICT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        predict();
    }
}
