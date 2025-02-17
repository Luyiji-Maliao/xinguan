package com.usci.norovirus.dao;

import com.lims.core.orm.hibernate.SqlHibernateDao;
import com.usci.norovirus.entity.Fapiao;
import com.usci.norovirus.entity.XgBuKaiFaPiao;
import org.springframework.stereotype.Component;

@Component
public class FapiaoDao extends SqlHibernateDao<Fapiao, Integer> {

}
