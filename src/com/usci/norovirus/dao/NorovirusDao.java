package com.usci.norovirus.dao;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.norovirus.entity.Norovirus;
import com.usci.system.entity.Position;
import org.springframework.stereotype.Component;

@Component
public class NorovirusDao extends HibernateDao<Norovirus, Integer> {

}
