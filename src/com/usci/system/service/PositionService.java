package com.usci.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.orm.hibernate.HibernateDao;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.dao.PositionDao;
import com.usci.system.entity.Position;

@Component
@Transactional(readOnly=true)
public class PositionService {
    @Autowired
    private PositionDao positionDao;
    @Transactional
    public void save(Position entity) {
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
        positionDao.merge(entity);
    }
    
   
	public void findPage(Page<Position> page,List<PropertyFilter> filters){
		positionDao.findPage(page, filters);
		
	}
	/**
	 * 
	 * @param posname 职位
	 * @param deptid  部门id
	 * @return
	 */
	public Position findByPosnameAnddeptid(String posname,Integer deptid){
	return positionDao.findUnique(Restrictions.eq("posName", posname),Restrictions.eq("department.id", deptid));	
	}


	public Position get(Integer id) {
		// TODO Auto-generated method stub
		return positionDao.get(id);
	}


	public List<Position> listall() {
		//HibernateDao  h=new HibernateDao();
		//return h.getSession().createCriteria(Position.class).createCriteria("department").add(Restrictions.eq("deptName", "IT")).list();
		Map m=new HashMap();
		m.put("department.deptName", "it");
		m.put("department.description","it");
		List l=new ArrayList();
		l.add(Restrictions.eq("id", 1));
		return positionDao.createCriteria(l,m).list();
		
	}
}
