package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.InterceptRequest;

@Component
public class InterceptRequestDao extends HibernateDao<InterceptRequest, Integer>{
    
}
