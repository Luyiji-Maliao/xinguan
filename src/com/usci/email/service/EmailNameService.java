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
import com.usci.email.dao.EmailNameDao;
import com.usci.email.dao.EmailOutDao;
import com.usci.email.entity.EmailName;
import com.usci.email.entity.EmailOut;


@Service
@Transactional(readOnly=true)
public class EmailNameService {
    @Autowired
    private EmailNameDao emailNameDao;
    private static final Logger log = LoggerFactory.getLogger(EmailNameService.class);
  
    @Transactional(propagation=Propagation.REQUIRED)
    public void save(EmailName entity){
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		entity.setInputName(Struts2Utils.getSessionUser().getName());
    	entity.setInputTime(Struts2Utils.getStringDate(new Date()));
    	emailNameDao.save(entity);
    }
    @Transactional(propagation=Propagation.REQUIRED)
    public void update(EmailName entity){
    	 entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		emailNameDao.update(entity);
    }
   
    
    public void findPage(Page<EmailName> page,	List<PropertyFilter> buildFromHttpRequest) {
    	emailNameDao.findPage(page, buildFromHttpRequest);	
	}
    /**
     * 通用精确查询
     * @param entity
     * @return
     */
    public List<EmailName> selectEntity(EmailName entity){
    	if(entity==null){
    		return new ArrayList<EmailName>();
    	}
    	Criterion [] criterion=new Criterion[3];
    	int i=0;
    	
    	if(entity.getDataState()!=-1){
    		criterion[i]= Restrictions.eq("dataState",entity.getDataState());
    		i++;
    	}
    	if(entity.getEmailName()!=null){
    		criterion[i]= Restrictions.eq("emailName",entity.getEmailName());
    		i++;
    	}
    	if(entity.getEmailAccount()!=null){
    		criterion[i]= Restrictions.eq("emailAccount",entity.getEmailAccount());
    		i++;
    	}
    	return emailNameDao.createCriteria(criterion).list();
    }
   
    public List<EmailName> findByName(String [] o){
    	List<EmailName> len=emailNameDao.createCriteria(Restrictions.in("emailName", o),Restrictions.eq("dataState", 0)).list();
    	return len;
    }
    
    public EmailName getEmailName(String name){
    	return emailNameDao.findUnique(Restrictions.eq("emailName", name));
    }

}
