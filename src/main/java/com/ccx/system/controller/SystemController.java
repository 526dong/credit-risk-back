package com.ccx.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zhaotm on 2017/7/1.
 * 系统管理controller
 */
@Controller
@RequestMapping("/index")
public class SystemController {
    /**
     * 进人adminindex页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public String index(){
        return "system/index";
    }
}
