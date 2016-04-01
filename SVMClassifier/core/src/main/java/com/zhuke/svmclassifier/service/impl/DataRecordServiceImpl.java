package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.service.DataRecordService;
import com.zhuke.svmclassifier.service.SVMConfig;
import com.zhuke.svmclassifier.util.DataProcessing;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 数据接收中心
 * Created by ZHUKE on 2016/3/31.
 */
@Service
public class DataRecordServiceImpl implements DataRecordService {
    Logger logger = LogManager.getLogger(DataRecordServiceImpl.class);

    public void dataRecieve() {

        logger.info(".........数据记录线程已启动........");

        String acc = null;
        // 工作主要状态，需要通过蓝牙定时向蓝牙发送数据r，得到蓝牙返回回来的该状态的状态属性
        // TODO: 2016/3/31  
        //System.out.println("向手环发送了一个动作值r");
        // 接收蓝牙返回回的一个状态信息，包含6个double值的string，中间以空格隔开了
        // TODO: 2016/3/31

        logger.info("从手环得到了其此时的状态值:" + acc);

        // 对数据进行归一化处理，并将其存入二维数组中
        double[] d = DataProcessing.normalization(acc);

        // Conf.temp_state自增
        SVMConfig.TEMP_STATE++;
        // temp数组为一个循环队列，当temp_state的值超过最大值的时候，需要从0开始
        if (SVMConfig.TEMP_STATE == 20) {
            SVMConfig.TEMP_STATE = SVMConfig.TEMP_STATE
                    % SVMConfig.ACTION_TO_RECORD;
        }
        // 将新接收到的数据存入到temp数组的第temp_state行
        SVMConfig.ACTION_TEMP[SVMConfig.TEMP_STATE] = d;

        // 线程休眠指定时间后再次发送数据r，接收手环数据，休眠值从配置类中读取
        try {
            Thread.sleep(SVMConfig.ACTION_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        dataRecieve();
    }
}
