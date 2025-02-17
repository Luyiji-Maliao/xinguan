package com.usci.system.service;



import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.usci.system.dao.MobileRandomDao;
import com.usci.system.entity.MobileRandom;


@Component
@Transactional(readOnly=true)
public class MobileRandomService {

	@Autowired
	private MobileRandomDao mobileRandomDao;
	/**
	 * 根据系列号登陆
	 * @param mr
	 * @return
	 */
	public MobileRandom findByMobileRandomAndId(String mr,Integer id){
		return mobileRandomDao.findUnique(Restrictions.eq("mobileRandom", mr),Restrictions.eq("moduleId", id));
	}
	/**
	 * 删除临时序列号（数据）
	 * @param id
	 */
	@Transactional
	public void deleteMobile(Integer id){
		mobileRandomDao.delete(id);
	}
	/**
	 *删除序列
	 * @param random
	 */
	@Transactional
	public void deleteMibileRandom(String random){
		mobileRandomDao.batchExecute("delete from MobileRandom where mobileRandom=? ", random);
	}
	@Transactional
	public void save(MobileRandom entity) {
		mobileRandomDao.save(entity);
	}
	
	
}
