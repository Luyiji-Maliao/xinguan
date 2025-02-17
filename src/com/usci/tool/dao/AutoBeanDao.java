package com.usci.tool.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.tool.entity.AutoBean;

@Component
public class AutoBeanDao extends HibernateDao<AutoBean, Integer>{
    
}
