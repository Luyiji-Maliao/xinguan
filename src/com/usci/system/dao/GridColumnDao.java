package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.GridColumn;

@Component
public class GridColumnDao extends HibernateDao<GridColumn, Integer> {
	
}
