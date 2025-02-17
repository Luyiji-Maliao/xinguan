package com.usci.tool.url.service;

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
import com.usci.tool.url.dao.InterfaceUrlDao;
import com.usci.tool.url.entity.InterfaceUrl;


@Service
@Transactional(readOnly=true)
public class InterfaceUrlService {
    @Autowired
    private InterfaceUrlDao interfaceUrlDao;
    private static final Logger log = LoggerFactory.getLogger(InterfaceUrlService.class);
  
    @Transactional(propagation=Propagation.REQUIRED)
    public void save(InterfaceUrl entity){
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		entity.setInputName(Struts2Utils.getSessionUser().getName());
    	entity.setInputTime(Struts2Utils.getStringDate(new Date()));
    	interfaceUrlDao.save(entity);
    }
    @Transactional(propagation=Propagation.REQUIRED)
    public void update(InterfaceUrl entity){
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		interfaceUrlDao.update(entity);
    }
   
    
    public void findPage(Page<InterfaceUrl> page,	List<PropertyFilter> buildFromHttpRequest) {
    	interfaceUrlDao.findPage(page, buildFromHttpRequest);	
	}
    /**
     * 通用精确查询
     * @param entity
     * @return
     */
    public List<InterfaceUrl> selectEntity(InterfaceUrl entity){
    	if(entity==null){
    		return new ArrayList<InterfaceUrl>();
    	}
    	Criterion [] criterion=new Criterion[3];
    	int i=0;
    	if(entity.getIuProperty()!=null){
    		criterion[i]= Restrictions.eq("iuProperty",entity.getIuProperty());
        	i++;
    	}
    	if(entity.getDataState()!=-1){
    		criterion[i]= Restrictions.eq("dataState",entity.getDataState());
    		i++;
    	}
    	if(entity.getInterfaceUrl()!=null){
    		criterion[i]= Restrictions.eq("interfaceUrl",entity.getInterfaceUrl());
    		i++;
    	}
    	if(entity.getIuSign()!=null){
    		criterion[i]= Restrictions.eq("iuSign",entity.getIuSign());
    		i++;
    	}
    	return interfaceUrlDao.createCriteria(criterion).list();
    }
   
    

}
