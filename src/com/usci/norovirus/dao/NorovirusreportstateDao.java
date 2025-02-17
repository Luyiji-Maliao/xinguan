package com.usci.norovirus.dao;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.norovirus.entity.Norovirus;
import com.usci.norovirus.entity.Norovirusreportstate;
import org.springframework.stereotype.Component;

@Component
public class NorovirusreportstateDao extends HibernateDao<Norovirusreportstate, Integer> {

}
