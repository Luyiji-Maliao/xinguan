package com.usci.norovirus.dao;

import com.lims.core.orm.hibernate.SqlHibernateDao;
import com.usci.norovirus.entity.Appointmentinfo;
import com.usci.norovirus.entity.Tuanti;
import org.springframework.stereotype.Component;

@Component
public class TuantiDao extends SqlHibernateDao<Tuanti, Integer> {

}
