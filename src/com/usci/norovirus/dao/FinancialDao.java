package com.usci.norovirus.dao;

import com.lims.core.orm.hibernate.SqlHibernateDao;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Financial;
import org.springframework.stereotype.Component;

@Component
public class FinancialDao extends SqlHibernateDao<Financial, Integer> {

}
