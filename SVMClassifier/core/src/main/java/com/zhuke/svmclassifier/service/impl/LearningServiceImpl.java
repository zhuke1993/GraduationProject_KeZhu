package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.entity.ActionRecord;
import com.zhuke.svmclassifier.entity.SVMParam;
import com.zhuke.svmclassifier.service.DataSource2SvmProblemService;
import com.zhuke.svmclassifier.service.LearningService;
import com.zhuke.svmclassifier.service.SVMConfig;
import libsvm.svm;
import libsvm.svm_problem;
import org.apache.ibatis.type.DoubleTypeHandler;
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

    public static void main(String[] args) {
        double[] t = new double[(20 - 10) * 8];
        double[][] d = new double[20][8];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 8; j++) {
                d[i][j] = i * j + j;
                System.out.print(d[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 20 - 5; j > 20 - 5 - (20 - 10); j--) {
                //将数组按列聚合
                t[i * 10 + 15 - j] = d[j][i];
            }
        }
        System.out.println("-------------");
        for (int i = 0; i < t.length; i++) {
            System.out.print(t[i] + " ");
        }
    }

    @Transactional(readOnly = false)
    public void learning() {

        Assert.notNull(lable);
        if (!StringUtils.isEmpty(lable)) {
            logger.info("收到学习指令:" + lable);

            if (SVMConfig.TEMP_STATE < SVMConfig.R - SVMConfig.L + SVMConfig.NOISE) {
                //此时需要取历史数据
                int count = 0;
                for (int i = 0, j = SVMConfig.TEMP_STATE; j >= SVMConfig.NOISE; i++, j--) {
                    SVMConfig.TO_PERDICT[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
                    count++;
                }
                for (int i = SVMConfig.ACTION_TO_RECORD, j = count; count < SVMConfig.R - SVMConfig.L; i--, j++) {
                    SVMConfig.TO_PERDICT[count] = SVMConfig.ACTION_TEMP[i - 1];
                    count++;
                }
            } else {
                //直接存取
                for (int i = 0, j = SVMConfig.TEMP_STATE; i < SVMConfig.R - SVMConfig.L; i++, j--) {
                    SVMConfig.TO_PERDICT[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
                }
            }

            // 将待预测数组进行格式化处理，将各属性值进行调整
            double[] t = new double[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];

            for (int i = 0; i < SVMConfig.FEATURE_NUM; i++) {
                for (int j = 0; j < SVMConfig.R - SVMConfig.L; j++) {
                    t[i * SVMConfig.FEATURE_NUM + j] = SVMConfig.TO_PERDICT[j][i];
                }
            }
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
                svm.svm_save_model(this.getClass().getResource("/") + SVMConfig.MODELFILE_PATH, SVMConfig.MODEL);
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