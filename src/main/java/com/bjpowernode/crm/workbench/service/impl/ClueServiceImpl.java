package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {

    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;

    @Resource
    private ActivityDao activityDao;

    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;

    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;

    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    @Override
    public boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> activities = clueDao.getActivityListByClueId(clueId);
        return activities;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByNameAndByNotByClueId(String aname, String clueId) {
        List<Activity> activities = clueDao.getActivityListByNameAndByNotByClueId(aname,clueId);
        return activities;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean flag = true;
        for(String aid : aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);
            int count = clueActivityRelationDao.bund(car);
            if(count != 1){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityByName(String aname) {
        List<Activity> activities = activityDao.getActivityByName(aname);
        return activities;
    }

    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        //1.
        Clue clue = clueDao.getById(clueId);

        //2.
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setDescription(clue.getDescription());
            customer.setName(clue.getCompany());

            int count1 = customerDao.save(customer);
            if(count1 != 1){
                flag = false;
            }
        }

        //3.
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());

        int count2 = contactsDao.save(contacts);
        if(count2 != 1){
            flag = false;
        }

        //4.查寻出与线索关联的备注信息,转换
        List<ClueRemark> clueRemarks = clueRemarkDao.getListByClueId(clueId);
        for(ClueRemark clueRemark:clueRemarks){
            String noteContent = clueRemark.getNoteContent();

            CustomerRemark cr = new CustomerRemark();
            cr.setNoteContent(noteContent);
            cr.setCustomerId(customer.getId());
            cr.setCreateTime(createTime);
            cr.setCreateBy(createTime);
            cr.setId(UUIDUtil.getUUID());
            cr.setEditFlag("0");
            int count3 = customerRemarkDao.save(cr);
            if(count3 != 1){
                flag = false;
            }


            ContactsRemark csr = new ContactsRemark();
            csr.setNoteContent(noteContent);
            csr.setContactsId(contacts.getId());
            csr.setCreateTime(createTime);
            csr.setCreateBy(createBy);
            csr.setId(UUIDUtil.getUUID());
            csr.setEditFlag("0");
            int count4 = contactsRemarkDao.save(csr);
            if(count4 != 1){
                flag = false;
            }
        }

        //5.
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationDao.getListByClueId(clueId);
        for(ClueActivityRelation clueActivityRelation:clueActivityRelations){
            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation csar = new ContactsActivityRelation();
            csar.setId(UUIDUtil.getUUID());
            csar.setContactsId(contacts.getId());
            csar.setActivityId(activityId);
            int count5 = contactsActivityRelationDao.save(csar);
            if(count5 != 1){
                flag = false;
            }
        }

        //6.
        if(tran.getId() != null){
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());
            int count6 = tranDao.save(tran);
            if(count6 != 1){
                flag = false;
            }

            //7.
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateTime(createTime);
            th.setCreateBy(createBy);
            th.setExpectedDate(tran.getExpectedDate());
            th.setMoney(tran.getMoney());
            th.setStage(tran.getStage());
            th.setTranId(tran.getId());
            int count7 = tranHistoryDao.save(th);
            if(count7 != 1){
                flag = false;
            }
        }

        //8.
        for(ClueRemark clueRemark:clueRemarks){
            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8 != 1){
                flag = false;
            }
        }

        //9.
        for(ClueActivityRelation clueActivityRelation:clueActivityRelations){
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if(count9 != 1){
                flag = false;
            }
        }

        //10.
        int count10 = clueDao.delete(clueId);
        if(count10 != 1){
            flag = false;
        }

        return flag;
    }
}
