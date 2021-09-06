package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);

    List<Activity> getActivityListByClueId(String clueId);


    List<Activity> getActivityListByNameAndByNotByClueId(@Param("aname") String aname, @Param("clueId") String clueId);


    Clue getById(String clueId);

    int delete(String clueId);
}
