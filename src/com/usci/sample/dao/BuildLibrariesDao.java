package com.usci.sample.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.sample.entity.BuildLibraries;

@Component
public class BuildLibrariesDao extends HibernateDao<BuildLibraries, Integer>{
    
}
