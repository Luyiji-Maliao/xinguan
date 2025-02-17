package com.usci.norovirus.service;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.dao.FapiaoDao;
import com.usci.norovirus.dao.HuikuanDao;
import com.usci.norovirus.entity.Fapiao;
import com.usci.norovirus.entity.Huikuan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@Transactional(readOnly=true)
public class HuikuanService {

	@Autowired
	private HuikuanDao huikuanDao;


	@Transactional(transactionManager = "sqltransactionManager")
	public void save(Huikuan entity){
		entity.setInputTime(Struts2Utils.getStringDate(new Date()));
		entity.setInputName(Struts2Utils.getSessionUser().getName());
		huikuanDao.save(entity);
	}

	@Transactional(transactionManager = "sqltransactionManager")
	public void update(Huikuan entity){
		entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		huikuanDao.update(entity);
	}


	/**
	 * 根据样本编号查找
	 * @param yuyueid
	 * @return
	 */
	public Huikuan findUniqueByYuyueid(Integer yuyueid) {
		return huikuanDao.findUniqueBy("yuyueid", yuyueid);
	}
	




	
	
	
	
	
}
