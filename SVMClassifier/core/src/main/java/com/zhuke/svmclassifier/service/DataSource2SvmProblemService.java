package com.zhuke.svmclassifier.service;

import libsvm.svm_parameter;
import libsvm.svm_problem;

import java.io.IOException;

/**
 * 数据转换为svm_problem
 *
 * @author ZHUKE
 */
public interface DataSource2SvmProblemService {

    double atof(String s);


    /**
     * 获取问题描述，从数据库进行获取，数据的存放格式为<lable>空格<attr1>:<value1>空格<attr2>:<value2>
     *
     * @return 得到的svm_problem
     * @throws IOException
     */
    public svm_problem readFromDB(Long userId, int limit, int size) throws IOException;

    public svm_problem readFromDB(Long userId) throws IOException;


    /**
     * 获取问题描述，从文件中进行获取，文件中数据的存放格式为<lable>空格<attr1>:<value1>空格<attr2>:<value2>
     *
     * @param input_file_name 文件名
     * @param param           参数值
     * @return 得到的svm_problem
     * @throws IOException
     */
    public svm_problem readFromFile(String input_file_name, svm_parameter param) throws IOException;

}
