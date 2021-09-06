package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/transaction")
public class TranController {

    @Resource
    private TranService tranService;
    @Resource
    private UserService userService;
    @Resource
    private CustomerService customerService;

    @RequestMapping(value = "/add.do")
    public ModelAndView add(){
        ModelAndView mv = new ModelAndView();
        List<User> userList = userService.getUserList();
        mv.addObject("userList",userList);
        mv.setViewName("/workbench/transaction/save.jsp");
        return mv;
    }

    @RequestMapping(value = "/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name){
        List<String> customerNameList = customerService.getCustomerName(name);
        return customerNameList;
    }

    @RequestMapping(value = "/save.do")
    public ModelAndView save(HttpServletRequest request, Tran tran, String customerName){
        ModelAndView mv = new ModelAndView();
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(((User)(request.getSession().getAttribute("user"))).getName());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        boolean flag = tranService.save(tran,customerName);
        if(flag){
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }
        return mv;
    }

    @RequestMapping(value = "/detail.do")
    public ModelAndView save(HttpServletRequest request, String id){
        ModelAndView mv = new ModelAndView();
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String,String>)application.getAttribute("pMap");
        Tran tran = tranService.detail(id);
        String stage = tran.getStage();
        String possibility = pMap.get(stage);
        mv. addObject("possibility",possibility);
        mv.addObject("tran",tran);
        mv.setViewName("/workbench/transaction/detail.jsp");
        return mv;
    }

    @RequestMapping(value = "/getHistoryListByTranId.do")
    @ResponseBody
    public List<TranHistory> getHistoryListByTranId(HttpServletRequest request, String tranId){
        Map<String,String> pMap = (Map<String,String>)request.getServletContext().getAttribute("pMap");
        List<TranHistory> tranHistories = tranService.getHistoryListByTranId(tranId);
        for(TranHistory tranHistory:tranHistories){
            String stage = tranHistory.getStage();
            String possibility = pMap.get(stage);
            tranHistory.setPossibility(possibility);
        }
        return tranHistories;
    }

    @RequestMapping(value = "/changeStage.do")
    @ResponseBody
    public Map<String,Object> changeStage(HttpServletRequest request, Tran tran){
        tran.setEditBy(((User)(request.getSession().getAttribute("user"))).getName());
        tran.setEditTime(DateTimeUtil.getSysTime());
        boolean flag = tranService.changeStage(tran);
        Map<String,String> pMap = (Map<String,String>)request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("tran",tran);
        return map;
    }

    @RequestMapping(value = "/getCharts.do")
    @ResponseBody
    public Map<String,Object> getCharts(){
        Map<String,Object> map = tranService.getCharts();
        return map;
    }
}
