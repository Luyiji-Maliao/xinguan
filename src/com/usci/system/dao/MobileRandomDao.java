package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.MobileRandom;

@Component
public class MobileRandomDao extends HibernateDao<MobileRandom, Integer> {
    
}
