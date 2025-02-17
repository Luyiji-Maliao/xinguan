package com.usci.email.dao;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.email.entity.EmailName;
import org.springframework.stereotype.Component;

@Component
public class EmailNameDao extends HibernateDao<EmailName, Integer>{

}
