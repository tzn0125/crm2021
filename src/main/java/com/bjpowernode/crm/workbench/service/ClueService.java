package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    boolean unbund(String id);

    List<Activity> getActivityListByNameAndByNotByClueId(String aname, String clueId);

    boolean bund(String cid, String[] aids);

    List<Activity> getActivityByName(String aname);

    boolean convert(String clueId, Tran tran, String createBy);
}
