package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.service.DataRecordService;
import com.zhuke.svmclassifier.service.SVMConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.StringTokenizer;

/**
 * 数据接收中心
 * Created by ZHUKE on 2016/3/31.
 */
@Service
public class DataRecordServiceImpl implements DataRecordService {

    Logger logger = LogManager.getLogger(DataRecordServiceImpl.class);

    public void dataRecieve(String acc) {

        logger.info("得到客户端发送的请求：action = " + acc);
        StringTokenizer st = new StringTokenizer(acc, ",");
        int count = st.countTokens();
        double[] d = new double[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];
        for (int i = 0; i < count; i++) {
            d[i] = Double.parseDouble(st.nextToken());
        }
        SVMConfig.setToLearn(d);
        SVMConfig.setToPerdict(d);
    }

    private double[] actionNormalize(String action) {
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

}
