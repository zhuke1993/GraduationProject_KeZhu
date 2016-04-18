package com.zhuke.svmclassifier.controller;

import com.zhuke.svmclassifier.service.DataRecordService;
import com.zhuke.svmclassifier.service.LearningService;
import com.zhuke.svmclassifier.service.PredictService;
import com.zhuke.svmclassifier.service.SVMConfig;
import com.zhuke.svmclassifier.service.impl.LearningServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;

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

    @RequestMapping("/predict/start.do")
    public void startPredict(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (SVMConfig.IS_PREDICTING == 0) {
            predictService.predict();
            SVMConfig.IS_PREDICTING = 1;
            response.getWriter().print("预测服务开启成功。");
        } else {
            response.getWriter().print("预测服务正在运行中，无需重复启动。");
        }

    }

    @RequestMapping("/predict/stop.do")
    public void stopPredict(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SVMConfig.IS_PREDICTING = 0;
        response.getWriter().print("终止预测成功");
    }


    @RequestMapping("/learning.do")
    public void learning(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        String lable = ServletRequestUtils.getStringParameter(request, "lable");
        learningService.learning(lable);
        response.getWriter().print("OK");
    }

    @RequestMapping("/action_record.do")
    public void actionRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dataRecordService.dataRecieve(URLDecoder.decode(request.getParameter("action"), "UTF-8"));
        response.getWriter().print("OK");
    }

}
