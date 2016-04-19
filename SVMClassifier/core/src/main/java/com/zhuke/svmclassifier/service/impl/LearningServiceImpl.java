package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.entity.ActionRecord;
import com.zhuke.svmclassifier.entity.SVMParam;
import com.zhuke.svmclassifier.service.DataSource2SvmProblemService;
import com.zhuke.svmclassifier.service.LearningService;
import com.zhuke.svmclassifier.service.SVMConfig;
import com.zhuke.svmclassifier.util.ArrayUtil;
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

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private DataSource2SvmProblemService dataSource2SvmProblemService;

    @Transactional
    public void learning(String lable) {
        Assert.notNull(lable);
        if (!StringUtils.isEmpty(lable)) {
            logger.info("收到学习指令:" + lable);

           /* ArrayUtil.updateToLearnArray();

            // 将待预测数组进行格式化处理，将各属性值进行调整
            double[] t = new double[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];
            for (int i = 0; i < SVMConfig.FEATURE_NUM; i++) {
                for (int j = 0; j < SVMConfig.R - SVMConfig.L; j++) {
                    t[i * (SVMConfig.R - SVMConfig.L) + j] = SVMConfig.TO_LEARN[j][i];
                }
            }*/
            double[] t = SVMConfig.getActionArray();
            //将数据拼接成字符串，数据格式为 <lable>,<attr1>:<value1>,<attr2>:<value2>
            String actionStr = lable;
            for (int i = 0; i < t.length; i++) {
                actionStr = actionStr + "," + (i + 1) + ":" + t[i];
            }
            ActionRecord actionRecord = new ActionRecord();
            actionRecord.setAction(actionStr);
            hibernateTemplate.save(actionRecord);
            logger.info("数据已保存：" + actionStr);
            try {
                //对model进行再次训练
                svm_problem prob = dataSource2SvmProblemService.readFromDB(SVMParam.setparameter(SVMConfig.C, SVMConfig.G));
                SVMConfig.MODEL = svm.svm_train(prob, SVMParam.setparameter(SVMConfig.C, SVMConfig.G));
                svm.svm_save_model(this.getClass().getResource("/").getFile() + SVMConfig.MODELFILE_PATH, SVMConfig.MODEL);
                logger.info("SVM学习结束，new model = " + SVMConfig.MODEL.toString());
            } catch (IOException e) {
                logger.error("学习线程发生异常", e);
            }
        }
    }
}