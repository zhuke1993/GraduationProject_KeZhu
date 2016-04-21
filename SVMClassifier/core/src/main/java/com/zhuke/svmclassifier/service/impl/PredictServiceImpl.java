package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.config.SystemConfig;
import com.zhuke.svmclassifier.service.MessageSendService;
import com.zhuke.svmclassifier.service.PredictService;
import com.zhuke.svmclassifier.service.SVMConfig;
import com.zhuke.svmclassifier.util.ArrayUtil;
import libsvm.svm;
import libsvm.svm_node;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

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

    public void predict() {
        logger.info("..........预测线程已启动..........");

        while (SystemConfig.IS_PREDICTING == 1) {
            Set<Long> keySet = SystemConfig.runningUserMap.keySet();
            Iterator<Long> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                Long userId = iterator.next();
                SVMConfig svmConfig = SystemConfig.getSVMConfig(userId);
                if (!ArrayUtil.isZero(svmConfig.getToPerdict())) {
                    double[] t = new double[svmConfig.TO_PERDICT.length];
                    System.arraycopy(svmConfig.TO_PERDICT, 0, t, 0, t.length);
                    Arrays.fill(svmConfig.getToPerdict(), 0);
                    SystemConfig.runningUserMap.put(userId, svmConfig);

                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < t.length; i++) {
                        sb.append(t[i] + ",");
                    }
                    // 将该数组填充进node数组，进行预测
                    svm_node[] nodes = new svm_node[(svmConfig.R - svmConfig.L) * svmConfig.FEATURE_NUM];
                    for (int i = 0; i < nodes.length; i++) {
                        nodes[i] = new svm_node();
                        nodes[i].index = i + 1;
                        nodes[i].value = t[i];
                    }
                    // 进行预测,得出预测值
                    double result = svm.svm_predict(svmConfig.MODEL, nodes);
                    // 向局域网内的所有的家电广播得到的结果值
                    //messageSendService.sendMessage(String.valueOf(result));
                    logger.info("待预测数据,userId = " + userId + ", action = " + sb.toString());
                    logger.info("得到预测值, userId = " + userId + ", result = " + result);
                    try {
                        Thread.sleep(svmConfig.PREDICT_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
