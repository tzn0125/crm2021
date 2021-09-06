package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/workbench/clue")
public class ClueController {

    @Resource
    private ClueService clueService;

    @Resource
    private UserService userService;


    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        List<User> userList = userService.getUserList();
        return userList;
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public boolean save(HttpServletRequest request,Clue clue){
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setCreateBy(((User)(request.getSession().getAttribute("user"))).getName());
        clue.setId(UUIDUtil.getUUID());
        boolean flag = clueService.save(clue);
        return flag;
    }

    @RequestMapping(value = "/detail.do")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Clue clue = clueService.detail(id);
        mv.addObject("clue",clue);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;
    }

    @RequestMapping(value = "/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){
        List<Activity> activities = clueService.getActivityListByClueId(clueId);
        return activities;
    }

    @RequestMapping(value = "/unbund.do")
    @ResponseBody
    public boolean unbund(String id){
        boolean flag = clueService.unbund(id);
        return flag;
    }

    @RequestMapping(value = "/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByNameAndByNotByClueId(String aname,String clueId){
        List<Activity> activities = clueService.getActivityListByNameAndByNotByClueId(aname,clueId);
        return activities;
    }

    @RequestMapping(value = "/bund.do")
    @ResponseBody
    public boolean bund(String cid,String[] aids){
        boolean flag = clueService.bund(cid,aids);
        return flag ;
    }

    @RequestMapping(value = "/getActivityByName.do")
    @ResponseBody
    public List<Activity> getActivityByName(String aname){
        List<Activity> activities = clueService.getActivityByName(aname);
        return activities ;
    }

    @RequestMapping(value = "/convert.do")
    public ModelAndView convert(HttpServletRequest request, String clueId, Tran tran, String flag){
        ModelAndView mv = new ModelAndView();
        String createBy = ((User)(request.getSession().getAttribute("user"))).getName();
        if("a".equals(flag)){
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateBy(createBy);
            tran.setCreateTime(DateTimeUtil.getSysTime());
        }
        boolean flag1 = clueService.convert(clueId,tran,createBy);
        if(flag1){
            mv.setViewName("/workbench/clue/index.jsp");
        }

        return mv;
    }
}
