package com.usci.tool.dao;


import org.springframework.stereotype.Component;



import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.tool.entity.Operate;

@Component
public class OperateDao extends HibernateDao<Operate, Integer>{

}
