package com.usci.system.service;


import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.usci.system.dao.EverCookiesDao;
import com.usci.system.entity.EverCookies;

@Component
@Transactional(readOnly=true)
public class EverCookiesService {
	@Autowired
	private EverCookiesDao everCookiesDao;
	
	@Transactional
	public void save(EverCookies entity) {
		everCookiesDao.merge(entity);
	}

	public EverCookies findByUseridAndcookiesName(Integer userid,String cookiesname){
		return everCookiesDao.findUnique(Restrictions.eq("userId", userid),Restrictions.eq("cookieName", cookiesname));
	}
	
	
}
