package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.Right;

@Component
public class RightDao extends HibernateDao<Right, Integer> {

}
