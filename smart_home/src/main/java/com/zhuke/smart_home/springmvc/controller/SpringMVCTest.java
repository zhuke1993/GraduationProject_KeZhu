package com.zhuke.smart_home.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ZHUKE on 2016/3/8.
 */
@Controller
@RequestMapping("/")
public class SpringMVCTest {

    @RequestMapping("/zhuke")
    public String index(){
        return "views/indexs.jsp";
    }
}
