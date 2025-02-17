package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.Informs;

@Component
public class InformsDao extends HibernateDao<Informs, Integer> {
    
}
