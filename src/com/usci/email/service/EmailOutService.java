package com.usci.email.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
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
import com.usci.email.dao.EmailOutDao;
import com.usci.email.entity.EmailOut;

@Service
@Transactional(readOnly=true)
public class EmailOutService {
    @Autowired
    private EmailOutDao emailOutDao;
    private static final Logger log = LoggerFactory.getLogger(EmailOutService.class);
  
    @Transactional(propagation=Propagation.REQUIRED)
    public void save(EmailOut entity){
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		entity.setInputName(Struts2Utils.getSessionUser().getName());
    	entity.setInputTime(Struts2Utils.getStringDate(new Date()));
    	emailOutDao.save(entity);
    }
    @Transactional(propagation=Propagation.REQUIRED)
    public void update(EmailOut entity){
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		emailOutDao.update(entity);
    }
   
    
    public void findPage(Page<EmailOut> page,	List<PropertyFilter> buildFromHttpRequest) {
    	emailOutDao.findPage(page, buildFromHttpRequest);	
	}
    /**
     * 通用精确查询
     * @param entity
     * @return
     */
    public List<EmailOut> selectEntity(EmailOut entity){
    	if(entity==null){
    		return new ArrayList<EmailOut>();
    	}
    	Criterion [] criterion=new Criterion[3];
    	int i=0;
    	if(entity.getEmailProperty()!=null){
    		criterion[i]= Restrictions.eq("emailProperty",entity.getEmailProperty());
        	i++;
    	}
    	if(entity.getDataState()!=-1){
    		criterion[i]= Restrictions.eq("dataState",entity.getDataState());
    		i++;
    	}
    	if(entity.getEmailNames()!=null){
    		criterion[i]= Restrictions.eq("emailNames",entity.getEmailNames());
    		i++;
    	}
    	if(entity.getEmailSign()!=null){
    		criterion[i]= Restrictions.eq("emailSign",entity.getEmailSign());
    		i++;
    	}
    	return emailOutDao.createCriteria(criterion).list();
    }
   
    

}
