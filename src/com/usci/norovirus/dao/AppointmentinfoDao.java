package com.usci.norovirus.dao;

import com.lims.core.orm.hibernate.HibernateDao;
import com.lims.core.orm.hibernate.SqlHibernateDao;
import com.usci.norovirus.entity.Appointmentinfo;
import com.usci.norovirus.entity.Norovirus;
import org.springframework.stereotype.Component;

@Component
public class AppointmentinfoDao extends SqlHibernateDao<Appointmentinfo, Integer> {

}
