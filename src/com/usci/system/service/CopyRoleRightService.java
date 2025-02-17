package com.usci.system.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.dao.CopyRoleRightDao;
import com.usci.system.entity.CopyRoleRight;


@Component
@Transactional(readOnly=true)
public class CopyRoleRightService {
	@Autowired
	private CopyRoleRightDao copyrolerightDao;
	@Transactional
	public void save(CopyRoleRight entity){
		entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		copyrolerightDao.save(entity);
	}
	
	public List<CopyRoleRight> listAll(){
		return copyrolerightDao.getAll();
	}
	//批量删除
	@Transactional
	public void delete(Integer roleid ){
		copyrolerightDao.batchExecute("delete from CopyRoleRight where roleid=?", roleid);
	}
	//单个删除
	@Transactional
	public void delete(CopyRoleRight entity){
		copyrolerightDao.delete(entity);
	}
	
	
	public List<CopyRoleRight> findByRoleid(Integer roleid){
		return copyrolerightDao.find("from CopyRoleRight where roleid=?", roleid);
	}
	
	public List<CopyRoleRight> lroleidAndrightid(Integer roleid, Integer rightid){
		return copyrolerightDao.find(Restrictions.eq("roleid", roleid),Restrictions.eq("rightid", rightid));
	}
	
	
	public CopyRoleRight roleidAndrightid(Integer roleid, Integer rightid){
		return copyrolerightDao.findUnique(Restrictions.eq("roleid", roleid),Restrictions.eq("rightid", rightid));
	}
	
	public List<CopyRoleRight> findByRightid(Integer rightid){
		return copyrolerightDao.find("from CopyRoleRight where rightid=?" ,rightid);
	}
	
}
