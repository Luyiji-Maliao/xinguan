package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.ControlUser;

@Component
public class ControlUserDao extends HibernateDao<ControlUser, Integer>{
    
}
