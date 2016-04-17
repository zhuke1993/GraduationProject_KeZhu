package com.zhuke.svmclassifier.service;

/**
 * 数据接收中心
 * Created by ZHUKE on 2016/3/31.
 */
public interface DataRecordService extends Runnable{

    public void dataRecieve(String acc);
}
