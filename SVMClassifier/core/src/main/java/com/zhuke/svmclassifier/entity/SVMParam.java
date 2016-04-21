package com.zhuke.svmclassifier.entity;

import libsvm.svm_parameter;

/**
 * 此类是svm的各类参数设置
 *
 * @author ZHUKE
 */
public class SVMParam {

    /**
     * svm参数设置
     * 8.0 0.0078125 87.5
     *
     * @param c
     * @param g
     * @return
     */
    public static svm_parameter customize(double c, double g) {
        svm_parameter param = new svm_parameter();
        param.gamma = g;
        param.C = c;
        return param;
    }
}
