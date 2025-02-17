package com.usci.system.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.dao.GridColumnDao;
import com.usci.system.entity.GridColumn;

@Component
@Transactional(readOnly=true)
public class GridColumnService {
	@Autowired
	private GridColumnDao columnDao;
	
	@Transactional
	public void save(GridColumn entity) {
		entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		columnDao.save(entity);
	}
	@Transactional
	public void update(GridColumn entity) {
		entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		columnDao.update(entity);
	}
	
	@Transactional
	public GridColumn get(Integer id) {
		return columnDao.get(id);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<GridColumn> findByCondition(GridColumn entity) {
		List<GridColumn> list = columnDao.createQuery("from GridColumn where userId=? and pageName=? and columnId=?", entity.getUserId(), entity.getPageName(), entity.getColumnId()).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<GridColumn> findByCondition(GridColumn entity, int columnCount) {
		List<GridColumn> list = columnDao.createQuery("from GridColumn where userId=? and pageName=?", entity.getUserId(), entity.getPageName()).list();
		return list;
	}
	
}
