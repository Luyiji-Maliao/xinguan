package com.usci.system.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.Position;

@Component
public class PositionDao extends HibernateDao<Position, Integer> {
    
}
