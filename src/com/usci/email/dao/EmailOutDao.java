package com.usci.email.dao;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.email.entity.EmailOut;
import org.springframework.stereotype.Component;

@Component
public class EmailOutDao extends HibernateDao<EmailOut, Integer>{

}
