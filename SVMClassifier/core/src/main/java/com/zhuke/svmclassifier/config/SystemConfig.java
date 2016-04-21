package com.zhuke.svmclassifier.config;

import com.zhuke.svmclassifier.service.SVMConfig;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ZHUKE on 2016/4/21.
 */
public class SystemConfig {
    public static ConcurrentHashMap<Long, SVMConfig> runningUserMap = new ConcurrentHashMap<Long, SVMConfig>();

    public static SVMConfig getSVMConfig(Long userId) {
        return runningUserMap.get(userId);
    }

    public static int IS_PREDICTING = 0;
}
