package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.entity.ActionRecord;
import com.zhuke.svmclassifier.entity.SVMParam;
import com.zhuke.svmclassifier.service.DataSource2SvmProblemService;
import com.zhuke.svmclassifier.service.LearningService;
import com.zhuke.svmclassifier.service.SVMConfig;
import libsvm.svm;
import libsvm.svm_problem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;


/**
 * 在被触发时对触发前的动作数据进行学习
 *
 * @author ZHUKE
 */
@Service
public class LearningServiceImpl implements LearningService {

    private Logger logger = LogManager.getLogger(LearningServiceImpl.class);

    private String lable;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private DataSource2SvmProblemService dataSource2SvmProblemService;


    public synchronized void setLable(String lable) {
        this.lable = lable;
    }

    @Transactional
    public void learning() {

        Assert.notNull(lable);
        if (!StringUtils.isEmpty(lable)) {
            logger.info("收到学习指令:" + lable);

            double[] t = new double[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];
            for (int k = 0; k < SVMConfig.FEATURE_NUM; k++) {
                for (int j = 0; j < SVMConfig.R - SVMConfig.L; j++) {
                    t[j * SVMConfig.FEATURE_NUM + k] = SVMConfig.ACTION_TEMP[j][k];
                }
            }

            //将数据拼接成字符串，数据格式为 <lable>空格<attr1>:<value1>空格<attr2>:<value2>
            String actionStr = lable + "";
            for (int i = 0; i < t.length; i++) {
                actionStr = actionStr + " " + (i + 1) + ":" + t[i];
            }
            ActionRecord actionRecord = new ActionRecord();
            actionRecord.setAction(actionStr);
            hibernateTemplate.save(actionRecord);
            logger.info("数据已保存：" + actionStr);

            try {
                //对model进行再次训练
                svm_problem prob = dataSource2SvmProblemService.readFromDB(SVMParam.setparameter(SVMConfig.C, SVMConfig.G));
                SVMConfig.MODEL = svm.svm_train(prob, SVMParam.setparameter(SVMConfig.C, SVMConfig.G));
                logger.info("SVM学习结束，new model = " + SVMConfig.MODEL.toString());
            } catch (IOException e) {
                logger.error("学习线程发生异常", e);
            }
        }
    }

    public void run() {
        learning();
    }
}