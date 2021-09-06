package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench")
public class ActivityController {

    @Resource
    private ActivityService activityService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/activity/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        List<User> userList = userService.getUserList();
        return userList;
    }

    @RequestMapping(value = "/activity/save.do")
    @ResponseBody
    public boolean save(HttpServletRequest request,Activity activity){
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setId(id);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        boolean flag = activityService.save(activity);
        return flag;
    }

    @RequestMapping(value = "/activity/pageList.do")
    @ResponseBody
    public PaginationVO<Activity> pageList(String name, String owner, String startDate, String endDate, Integer pageNo, Integer pageSize){
        Integer skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        PaginationVO<Activity> vo = activityService.pageList(map);
        return vo;
    }

    @RequestMapping(value = "/activity/delete.do")
    @ResponseBody
    public boolean delete(String[] ids){
        boolean flag = activityService.delete(ids);
        return flag;
    }

    @RequestMapping(value = "/activity/getUserListAndActivity.do")
    @ResponseBody
    public Map<String,Object> getUserListAndActivity(String id){
        System.out.println(id);
        Map<String,Object> map = activityService.getUserListAndActivity(id);
        return map;
    }

    @RequestMapping(value = "/activity/update.do")
    @ResponseBody
    public boolean update(HttpServletRequest request ,Activity activity){
        activity.setEditTime(DateTimeUtil.getSysTime());
        activity.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        boolean flag = activityService.update(activity);
        return flag;
    }

    @RequestMapping(value = "/activity/detail.do")
    public ModelAndView detail(HttpServletRequest request, String id){
        ModelAndView mv = new ModelAndView();
        Activity activity = activityService.detail(id);
        mv.addObject("activity",activity);
        mv.setViewName("/workbench/activity/detail.jsp");
        return mv;
    }

    @RequestMapping(value = "/activity/getRemarkListById.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkListById(String activityId){
        List<ActivityRemark> activityRemarkList = activityService.getRemarkListById(activityId);
        return activityRemarkList;
    }

    @RequestMapping(value = "/activity/deleteRemark.do")
    @ResponseBody
    public boolean deleteRemark(String id){
        boolean flag = activityService.deleteRemark(id);
        return flag;
    }

    @RequestMapping(value = "/activity/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(HttpServletRequest request,ActivityRemark activityRemark){
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("0");
        boolean flag = activityService.saveRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("activityRemark",activityRemark);

        return map;
    }

    @RequestMapping(value = "/activity/updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(HttpServletRequest request,ActivityRemark activityRemark){
        activityRemark.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("1");
        boolean flag = activityService.updateRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("activityRemark",activityRemark);

        return map;
    }
}
