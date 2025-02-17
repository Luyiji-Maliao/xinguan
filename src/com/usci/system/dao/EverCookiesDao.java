package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.EverCookies;

@Component
public class EverCookiesDao extends HibernateDao<EverCookies, Integer> {
    
}
