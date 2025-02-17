package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.Role;

@Component
public class RoleDao extends HibernateDao<Role, Integer> {
    
}
