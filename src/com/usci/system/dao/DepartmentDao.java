package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.Department;

@Component
public class DepartmentDao extends HibernateDao<Department, Integer>{
    
}
