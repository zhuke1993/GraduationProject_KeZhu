package com.zhuke.svmclassifier.controller;

import com.google.gson.Gson;
import com.zhuke.smart_home.central.SHConfig;
import com.zhuke.svmclassifier.config.SystemConfig;
import com.zhuke.svmclassifier.entity.Message;
import com.zhuke.svmclassifier.entity.UserInfo;
import com.zhuke.svmclassifier.exceptions.UsernameExistedException;
import com.zhuke.svmclassifier.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
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
    private UserInfoService userInfoService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private RegisterLoginService registerLoginService;

    @RequestMapping("/predict/start.do")
    public void startPredict(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long userId = Long.parseLong(request.getParameter("userId"));
        SVMConfig svmConfig = SystemConfig.getSVMConfig(userId);
        if (SystemConfig.IS_PREDICTING == 0) {
            SystemConfig.IS_PREDICTING = 1;
            threadPoolExecutor.execute(predictService);
            response.getWriter().print("预测服务开启成功。");
        } else {
            response.getWriter().print("预测服务正在运行中，无需重复启动。");
        }

    }

    @RequestMapping("/predict/stop.do")
    public void stopPredict(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SystemConfig.IS_PREDICTING = 0;
        response.getWriter().print("终止预测成功");
    }


    @RequestMapping("/learning.do")
    public void learning(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {

        Long userId = Long.parseLong(request.getParameter("userId"));

        String lable = ServletRequestUtils.getStringParameter(request, "lable");
        learningService.learning(userId, lable);
        response.getWriter().print("OK");
    }

    @RequestMapping("/action_record.do")
    public void actionRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long userId = Long.parseLong(request.getParameter("userId"));
        dataRecordService.dataRecieve(userId, URLDecoder.decode(request.getParameter("action"), "UTF-8"));
        response.getWriter().print("OK");
    }

    @RequestMapping("/login.do")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("username");
        String password = request.getParameter("password");
        UserInfo loginUser = userInfoService.login(name, password);
        if (loginUser != null) {
            response.getWriter().print(loginUser.getId());
        } else {
            response.getWriter().print("FAILED");
        }
    }

    @RequestMapping("/register.do")
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String name = URLDecoder.decode(request.getParameter("email"), "UTF-8");
            String password = URLDecoder.decode(request.getParameter("password"), "UTF-8");

            UserInfo userInfo = new UserInfo();
            userInfo.setCreatedOn(new Date());
            userInfo.setUserName(name);
            userInfo.setPassword(password);
            userInfo.setEmail(name);

            registerLoginService.register(userInfo);
            response.getWriter().print("OK");
        } catch (UsernameExistedException e) {
            e.printStackTrace();
            response.getWriter().print(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("服务器错误");
        }
    }

    @RequestMapping(value = "/smarthome/device_list.do", method = RequestMethod.GET)
    public void getDeviceVector(HttpServletRequest request, HttpServletResponse response) {
        try {
            Gson gson = new Gson();
            String deviceListJson = gson.toJson(SHConfig.deviceVector);
            response.getWriter().print(deviceListJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/smarthome/messages.do")
    public void getMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long userId = Long.parseLong(request.getParameter("userId"));
            List<Message> messages = SystemConfig.getMessage(userId);
            response.getWriter().write(new Gson().toJson(messages));
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("服务器繁忙,errMsg=" + e.getMessage() + "\n请退出重新登陆。");
        }
    }
}
