package com.zhuke.svmclassifier.service;

import org.springframework.stereotype.Service;


/**
 * 在被触发时对触发前的动作数据进行学习
 *
 * @author ZHUKE
 */
@Service
public interface LearningService{

    public void learning();
}