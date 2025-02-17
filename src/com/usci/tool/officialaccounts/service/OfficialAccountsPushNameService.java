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
import com.usci.tool.officialaccounts.dao.OfficialAccountsPushNameDao;
import com.usci.tool.officialaccounts.entity.OfficialAccountsPushName;


@Service
@Transactional(readOnly=true)
public class OfficialAccountsPushNameService {
    @Autowired
    private OfficialAccountsPushNameDao officialAccountsPushNameDao;
    private static final Logger log = LoggerFactory.getLogger(OfficialAccountsPushNameService.class);
  
    @Transactional(propagation=Propagation.REQUIRED)
    public void save(OfficialAccountsPushName entity){
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		entity.setInputName(Struts2Utils.getSessionUser().getName());
    	entity.setInputTime(Struts2Utils.getStringDate(new Date()));
    	officialAccountsPushNameDao.save(entity);
    }
    @Transactional(propagation=Propagation.REQUIRED)
    public void update(OfficialAccountsPushName entity){
    	 entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		officialAccountsPushNameDao.update(entity);
    }
   
    
    public void findPage(Page<OfficialAccountsPushName> page,	List<PropertyFilter> buildFromHttpRequest) {
    	officialAccountsPushNameDao.findPage(page, buildFromHttpRequest);	
	}
    /**
     * 通用精确查询
     * @param entity
     * @return
     */
    public List<OfficialAccountsPushName> selectEntity(OfficialAccountsPushName entity){
    	if(entity==null){
    		return new ArrayList<OfficialAccountsPushName>();
    	}
    	Criterion [] criterion=new Criterion[3];
    	int i=0;
    	
    	if(entity.getDataState()!=-1){
    		criterion[i]= Restrictions.eq("dataState",entity.getDataState());
    		i++;
    	}
    	if(entity.getOapnName()!=null){
    		criterion[i]= Restrictions.eq("oapnName",entity.getOapnName());
    		i++;
    	}
    	if(entity.getOapnPhone()!=null){
    		criterion[i]= Restrictions.eq("oapnPhone",entity.getOapnPhone());
    		i++;
    	}
    	return officialAccountsPushNameDao.createCriteria(criterion).list();
    }
   
    public List<OfficialAccountsPushName> findByName(String [] o){
    	List<OfficialAccountsPushName> len=officialAccountsPushNameDao.createCriteria(Restrictions.in("oapnName", o),Restrictions.eq("dataState", 0)).list();
    	return len;
    }

}
