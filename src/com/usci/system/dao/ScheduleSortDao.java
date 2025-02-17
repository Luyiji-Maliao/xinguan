package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.ScheduleSort;

@Component
public class ScheduleSortDao extends HibernateDao<ScheduleSort, Integer> {
    
}
