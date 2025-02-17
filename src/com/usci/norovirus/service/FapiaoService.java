package com.usci.norovirus.service;

import com.lims.core.orm.Page;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.dao.FapiaoDao;
import com.usci.norovirus.dao.XgBuKaiFaPiaoDao;
import com.usci.norovirus.entity.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class FapiaoService {

	@Autowired
	private FapiaoDao fapiaoDao;


	@Transactional(transactionManager = "sqltransactionManager")
	public void save(Fapiao entity){
		entity.setInputTime(Struts2Utils.getStringDate(new Date()));
		entity.setInputName(Struts2Utils.getSessionUser().getName());
		fapiaoDao.save(entity);
	}

	@Transactional(transactionManager = "sqltransactionManager")
	public void update(Fapiao entity){
		entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdateName(Struts2Utils.getSessionUser().getName());
		fapiaoDao.update(entity);
	}


	/**
	 * 根据样本编号查找
	 * @param yuyueid
	 * @return
	 */
	public Fapiao findUniqueByYuyueid(Integer yuyueid) {
		return fapiaoDao.findUniqueBy("yuyueid", yuyueid);
	}
	




	
	
	
	
	
}
