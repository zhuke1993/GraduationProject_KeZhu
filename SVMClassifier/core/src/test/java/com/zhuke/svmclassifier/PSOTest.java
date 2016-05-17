package com.zhuke.svmclassifier;

import com.zhuke.svmclassifier.service.DataSource2SvmProblemService;
import com.zhuke.svmclassifier.service.PSOService;
import libsvm.svm_problem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ZHUKE on 2016/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PSOTest {
    @Autowired
    private PSOService psoService;

    @Autowired
    private DataSource2SvmProblemService dataSource2SvmProblemService;

    @Test
    public void testPSO() throws Exception {
        psoService.pso();

    }

}
