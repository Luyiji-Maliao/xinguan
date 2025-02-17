package com.usci.system.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.dao.InformsDao;
import com.usci.system.entity.Informs;
import com.usci.system.entity.Schedule;
import com.usci.system.entity.ScheduleSort;

@Component
@Transactional
public class InformsService {
	@Autowired
	private InformsDao informsDao;
	
	@Transactional
	public void save(Informs entity) {
		informsDao.merge(entity);
	}
	@Transactional(readOnly=true)
	public void findPage(Page<Informs> page,List<PropertyFilter> filters){
		informsDao.findPage(page, filters);
	}

	/**
	 * 修改状态
	 * @param id
	 */
	@Transactional
		public void updateStatus(Integer id) {
			informsDao.batchExecute("update Informs set  informsStatus=? where id=? ","0",id);

		}
		/**
		 * 必备参数
		 * @param informsTitle
		 * @param informsSender
		 * @param informsReceiver
		 * @param informsContent
		 * @param informsLink
		 */
	@Transactional
		public void saveInforms(String informsTitle,String informsSender,String informsReceiver,String informsContent,String informsLink ){
			Informs i=new Informs();
			i.setInformsTitle(informsTitle);
			i.setInformsSender(informsSender);
			i.setInformsReceiver(informsReceiver);
			i.setInformsContent(informsContent);
			i.setInformsLink(informsLink);
			i.setInformsStatus("1");
			i.setInformsSenderDate(Struts2Utils.getStringDate(new Date()));
			informsDao.save(i);
		}
	@Transactional
		public void saves(Informs entity){
			String [] receivers=entity.getInformsReceiver().split(",");
			for (String re : receivers) {
					entity.setInformsReceiver(re);
					entity.setInformsSender(Struts2Utils.getSessionUser().getName());
					entity.setInformsSenderDate(Struts2Utils.getStringDate(new Date()));
					informsDao.merge(entity);
			}
		}
}
