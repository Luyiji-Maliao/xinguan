package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.Attendance;

@Component
public class WorkdayCountDao extends HibernateDao<Attendance, Integer>{
    
}
