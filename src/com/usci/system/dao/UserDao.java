package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.User;

@Component
public class UserDao extends HibernateDao<User, Integer>{
    
}
