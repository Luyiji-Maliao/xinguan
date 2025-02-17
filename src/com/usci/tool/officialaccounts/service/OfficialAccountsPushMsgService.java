package com.usci.tool.officialaccounts.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.tool.officialaccounts.dao.OfficialAccountsPushMsgDao;
import com.usci.tool.officialaccounts.entity.OfficialAccountsPushMsg;


@Service
@Transactional(readOnly=true)
public class OfficialAccountsPushMsgService {
    @Autowired
    private OfficialAccountsPushMsgDao officialAccountsPushMsgDao;
    private static final Logger log = LoggerFactory.getLogger(OfficialAccountsPushMsgService.class);
  
    @Transactional(propagation=Propagation.REQUIRED)
    public void save(OfficialAccountsPushMsg entity){
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		entity.setInputName(Struts2Utils.getSessionUser().getName());
    	entity.setInputTime(Struts2Utils.getStringDate(new Date()));
    	officialAccountsPushMsgDao.save(entity);
    }
    @Transactional(propagation=Propagation.REQUIRED)
    public void update(OfficialAccountsPushMsg entity){
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		officialAccountsPushMsgDao.update(entity);
    }
   
    
    public void findPage(Page<OfficialAccountsPushMsg> page,	List<PropertyFilter> buildFromHttpRequest) {
    	officialAccountsPushMsgDao.findPage(page, buildFromHttpRequest);	
	}
    /**
     * 通用精确查询
     * @param entity
     * @return
     */
    public List<OfficialAccountsPushMsg> selectEntity(OfficialAccountsPushMsg entity){
    	if(entity==null){
    		return new ArrayList<OfficialAccountsPushMsg>();
    	}
    	Criterion [] criterion=new Criterion[3];
    	int i=0;
    	if(entity.getOapmProperty()!=null){
    		criterion[i]= Restrictions.eq("oapmProperty",entity.getOapmProperty());
        	i++;
    	}
    	if(entity.getDataState()!=-1){
    		criterion[i]= Restrictions.eq("dataState",entity.getDataState());
    		i++;
    	}
    	if(entity.getOapmNames()!=null){
    		criterion[i]= Restrictions.eq("oapmNames",entity.getOapmNames());
    		i++;
    	}
    	if(entity.getOapmSign()!=null){
    		criterion[i]= Restrictions.eq("oapmSign",entity.getOapmSign());
    		i++;
    	}
    	return officialAccountsPushMsgDao.createCriteria(criterion).list();
    }
   
    

}
