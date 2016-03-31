package com.zhuke.svmclassifier.core;

import com.zhuke.svmclassifier.entity.ActionRecord;
import com.zhuke.svmclassifier.entity.SVMParam;
import com.zhuke.svmclassifier.util.FileToSvmProblem;
import com.zhuke.svmclassifier.util.FileUtil;
import libsvm.svm;
import libsvm.svm_problem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;


/**
 * 学习线程，在被触发时对触发前的动作数据进行学习
 *
 * @author ZHUKE
 */
@Service
public class LearningService implements Runnable {

    private Logger logger = LogManager.getLogger(LearningService.class);

    private String lable;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public synchronized void setLable(String lable) {
        this.lable = lable;
    }

    public void learning() {

        Assert.notNull(lable);
        //接收到家电发来的数据
        //String lable = WifiCenter.accept();
        if (!StringUtils.isEmpty(lable)) {
            logger.info("收到学习指令:" + lable);

            double[] t = new double[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];
            for (int k = 0; k < SVMConfig.FEATURE_NUM; k++) {
                for (int j = 0; j < SVMConfig.R - SVMConfig.L; j++) {
                    t[j * SVMConfig.FEATURE_NUM + k] = SVMConfig.ACTION_TEMP[j][k];
                }
            }

            //将数据拼接成字符串，存入action.txt文件中，数据格式为 <lable>空格<attr1>:<value1>空格<attr2>:<value2>
            String addStr = lable + "";
            for (int i = 0; i < t.length; i++) {
                addStr = addStr + " " + (i + 1) + ":" + t[i];
            }
            //System.out.println("下列数据等待被学习：\n"+addStr);
            FileUtil.addStr2File(SVMConfig.class.getResource("/").getFile() + "/" + SVMConfig.ACTION_TO_PREDICT_F, addStr + "\n");
            ActionRecord actionRecord = new ActionRecord();
            actionRecord.setAction(addStr);

            hibernateTemplate.save(actionRecord);

            try {
                //对model进行再次训练
                svm_problem prob = FileToSvmProblem.read_problem(SVMConfig.class.getResource("/").getFile() + "/" + SVMConfig.ACTION_TO_PREDICT_F, SVMParam.setparameter(SVMConfig.C, SVMConfig.G));
                SVMConfig.MODEL = svm.svm_train(prob, SVMParam.setparameter(SVMConfig.C, SVMConfig.G));
                System.out.println(SVMConfig.MODEL);
                //将最新的model保存进model_f.txt文件中
                svm.svm_save_model(SVMConfig.class.getResource("/").getFile() + "/" + SVMConfig.MODELFILE_PATH, SVMConfig.MODEL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        learning();
    }
}