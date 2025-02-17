package com.usci.norovirus.dao;

import com.lims.core.orm.hibernate.SqlHibernateDao;
import com.usci.norovirus.entity.Fapiao;
import com.usci.norovirus.entity.Huikuan;
import org.springframework.stereotype.Component;

@Component
public class HuikuanDao extends SqlHibernateDao<Huikuan, Integer> {

}
