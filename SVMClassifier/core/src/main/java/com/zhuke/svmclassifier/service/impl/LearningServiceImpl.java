package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.config.SystemConfig;
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
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;


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
    public void learning(Long userId, String lable) {
        Assert.notNull(lable);
        if (!StringUtils.isEmpty(lable)) {
            SVMConfig svmConfig = SystemConfig.getSVMConfig(userId);
            logger.info("收到学习指令:" + lable);

            if (!ArrayUtil.isZero(svmConfig.getToLearn())) {
                double[] t = new double[svmConfig.TO_LEARN.length];
                System.arraycopy(svmConfig.TO_LEARN, 0, t, 0, t.length);

                Arrays.fill(svmConfig.getToLearn(), 0);

                //将数据拼接成字符串，数据格式为 <lable>,<attr1>:<value1>,<attr2>:<value2>
                String actionStr = lable;
                for (int i = 0; i < t.length; i++) {
                    actionStr = actionStr + " " + (i + 1) + ":" + t[i];
                }
                ActionRecord actionRecord = new ActionRecord();
                actionRecord.setAction(actionStr);
                actionRecord.setUserId(userId);
                actionRecord.setCreatedOn(new Date());

                hibernateTemplate.save(actionRecord);
                logger.info("数据已保存：userId = " + userId + " , action = " + actionStr);
                try {
                    //对model进行再次训练
                    svm_problem prob = dataSource2SvmProblemService.readFromDB(userId);
                    svmConfig.MODEL = svm.svm_train(prob, SVMParam.customize(svmConfig.C, svmConfig.G));
                    svm.svm_save_model(URLDecoder.decode(this.getClass().getResource("/").getFile() + svmConfig.MODELFILE_PATH + "_" + userId, "UTF-8"), svmConfig.MODEL);
                    logger.info("SVM学习结束，new model for userId : " + userId + " = " + svmConfig.MODEL.toString());
                } catch (IOException e) {
                    logger.error("学习线程发生异常", e);
                }
            }
        }
    }
}