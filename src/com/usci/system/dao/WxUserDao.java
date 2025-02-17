package com.usci.system.dao;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.system.entity.WxLoginUser;
import org.springframework.stereotype.Component;

@Component
public class WxUserDao extends HibernateDao<WxLoginUser, Integer>{
    
}
