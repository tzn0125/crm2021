package com.bjpowernode.crm.handler;

import com.bjpowernode.crm.Exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public Map<String,Object> UserLoginHandler(Exception ex){
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        map.put("msg",ex.getMessage());
        return map;
    }
}
