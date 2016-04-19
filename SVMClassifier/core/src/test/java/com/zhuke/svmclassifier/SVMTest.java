package com.zhuke.svmclassifier;

import com.zhuke.svmclassifier.service.DataSource2SvmProblemService;
import com.zhuke.svmclassifier.entity.ActionRecord;
import libsvm.svm_problem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

/**
 * Created by ZHUKE on 2016/4/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SVMTest {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private DataSource2SvmProblemService dataSource2SvmProblemService;

    @Test
    @Transactional
    @Rollback(false)
    public void addAction() throws IOException {
        RandomAccessFile file = new RandomAccessFile(SVMTest.class.getResource("/").getFile() + "brease_cancer.txt", "r");
        String s = file.readLine();
        while (!StringUtils.isEmpty(s)) {
            ActionRecord actionRecord = new ActionRecord();
            actionRecord.setCreatedOn(new Date());
            actionRecord.setAction(s);
            hibernateTemplate.save(actionRecord);

            s = file.readLine();
        }
    }


    @Test
    public void testDatasource2svmproblem() throws IOException {
        svm_problem svm_problem = dataSource2SvmProblemService.readFromDB();
        System.out.println(svm_problem.l);
        for (int i = 0; i < svm_problem.l; i++) {
            System.out.println(svm_problem.x[i]);
            System.out.println(svm_problem.y[i]);
        }
        System.out.println(svm_problem.x);
        System.out.println(svm_problem.y);
    }

}
