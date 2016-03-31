package com.zhuke.svmclassifier.controller;

import com.zhuke.svmclassifier.core.DataRecordService;
import com.zhuke.svmclassifier.core.LearningService;
import com.zhuke.svmclassifier.core.PredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ZHUKE on 2016/3/31.
 */
@Controller
public class SVMController {

    @Autowired
    private DataRecordService dataRecordService;

    @Autowired
    private LearningService learningService;

    @Autowired
    private PredictService predictService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @RequestMapping("/core/predict/start.do")
    public void startPredict(HttpServletRequest request, HttpServletResponse response) throws IOException {
        threadPoolExecutor.execute(dataRecordService);
        threadPoolExecutor.execute(learningService);
        threadPoolExecutor.execute(predictService);
        response.getWriter().print("开始预测成功");
    }

    @RequestMapping("/core/predict/stop.do")
    public void stopPredict(HttpServletRequest request, HttpServletResponse response) throws IOException {
        threadPoolExecutor.shutdownNow();
        response.getWriter().print("终止预测成功");
    }


    @RequestMapping("/core/learning.do")
    public void learning(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        String lable = ServletRequestUtils.getStringParameter(request, "lable");
        learningService.setLable(lable);
        learningService.learning();

        response.getWriter().print("OK");
    }

}
