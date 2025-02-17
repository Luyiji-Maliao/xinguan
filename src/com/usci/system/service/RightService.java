package com.usci.system.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.dao.RightDao;
import com.usci.system.entity.Right;

@Component
@Transactional(readOnly=true)
public class RightService {
	@Autowired
	private RightDao rightDao;
	@Transactional
	public void save(Right entity){
		entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		rightDao.save(entity);
	}
	
	public List<Right> listAll(){
		return rightDao.getAll();
	}
	
	public Right findByname(String rightname){
		return  rightDao.findUnique(Restrictions.eq("moduleName", rightname));
	}
	
	public Right findByJspPage(String jsppage){
		return  rightDao.findUniqueBy("jspPage", jsppage);
	}
	
}
