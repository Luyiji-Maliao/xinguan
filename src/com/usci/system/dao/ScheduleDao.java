package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.Schedule;

@Component
public class ScheduleDao extends HibernateDao<Schedule, Integer> {
    
}
