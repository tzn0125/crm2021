package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.Exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/settings/user/login.do")
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request,String loginAct, String loginPwd) throws LoginException {
        loginPwd = MD5Util.getMD5(loginPwd);
        String ip = request.getRemoteAddr();
        User user = userService.login(loginAct,loginPwd,ip);
        request.getSession().setAttribute("user",user);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }

}
