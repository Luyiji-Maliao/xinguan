package com.usci.wxbukaifapiao.dao;

import com.lims.core.orm.hibernate.HibernateDao;

import com.lims.core.orm.hibernate.SqlHibernateDao;
import com.usci.wxbukaifapiao.entity.Bkfp;
import org.springframework.stereotype.Component;
import org.springframework.test.context.jdbc.Sql;

@Component
public class BkfpDao extends SqlHibernateDao<Bkfp,Integer> {
}
