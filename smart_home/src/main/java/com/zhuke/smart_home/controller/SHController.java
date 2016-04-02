package com.zhuke.smart_home.controller;

import com.google.gson.Gson;
import com.zhuke.smart_home.central.SHConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ZHUKE on 2016/4/2.
 */
@Controller
public class SHController {

    @RequestMapping(value = "/device_list", method = RequestMethod.GET)
    public void getDeviceVector(HttpServletRequest request, HttpServletResponse response) {
        try {
            Gson gson = new Gson();
            String deviceListJson = gson.toJson(SHConfig.deviceVector);
            response.getWriter().print(deviceListJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
