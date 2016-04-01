package com.zhuke.svmclassifier.service;

/**
 * 预测线程，不断预测传来的数据
 *
 * @author ZHUKE
 */
public interface PredictService extends Runnable{

    public void predict();
}
