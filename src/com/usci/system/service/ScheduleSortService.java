package com.usci.system.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.dao.ScheduleSortDao;
import com.usci.system.entity.EverCookies;
import com.usci.system.entity.ScheduleSort;

@Component
@Transactional(readOnly=true)
public class ScheduleSortService {
	@Autowired
	private ScheduleSortDao scheduleSortDao;
	@Autowired
	private EverCookiesService ecs;
	@Transactional
	public void save(ScheduleSort entity) {
	   Integer ii=	scheduleSortDao.returnsave(entity);
		EverCookies e=ecs.findByUseridAndcookiesName(Struts2Utils.getSessionUser().getId(), "schedulesortcookie");
		 JSONArray jsonArray = JSONArray.fromObject(e.getCookieContent());
		 List<Map<String,Object>> mapListJson = (List)jsonArray;
		 int c1=0;
		 int c2=0;
		 int c3=0;
		 for (int i = 0; i < mapListJson.size(); i++) {  
	            Map<String,Object> obj=mapListJson.get(i);  
	            
	                if(obj.get("col").equals(0)){
	                	c1++;
	                }
					if(obj.get("col").equals(1)){
						c2++; 
					}
					if(obj.get("col").equals(2)){
						c3++;
					}
	        }  
		int c= Math.min(Math.min(c1, c2), c3);
		String s=e.getCookieContent().substring(0,e.getCookieContent().length()-1)+",{'id':"+ii+",'col':0}"+"]";
		if(c1==c){
			s=e.getCookieContent().substring(0,e.getCookieContent().length()-1)+",{'id':"+ii+",'col':0}"+"]";
		}else if(c2==c){
			s=e.getCookieContent().substring(0,e.getCookieContent().length()-1)+",{'id':"+ii+",'col':1}"+"]";
		}else if(c3==c){
			s=e.getCookieContent().substring(0,e.getCookieContent().length()-1)+",{'id':"+ii+",'col':2}"+"]";
		}
		e.setCookieContent(s);
	}
	
	@Transactional
	public Integer returnsave(ScheduleSort entity){
		return scheduleSortDao.returnsave(entity);
	}

	public Page<ScheduleSort> list(int limit) {
		return scheduleSortDao.findPage(new Page<ScheduleSort>(limit));
	}
	
	@SuppressWarnings("unchecked")
	public List<ScheduleSort> findList(String userName){
		return scheduleSortDao.createCriteria(Restrictions.eq("userName", userName),Restrictions.eq("ssStatus", 1)).list();
	}
	/**
	 * 根据用户id和标题
	 * @param userid
	 * @param title
	 * @return
	 */
	public ScheduleSort findByUseridAndTitle(String userName,String title){
		return scheduleSortDao.findUnique(Restrictions.eq("userName",userName ),Restrictions.eq("title", title));
	}
	/**
	 * 下拉列表
	 * @param page
	 * @param filters
	 */
	public void findPage(Page<ScheduleSort> page,List<PropertyFilter> filters) {
		scheduleSortDao.findPage(page, filters);		
	}
	@Transactional
	public  void deleteSort(Integer id) {
		ScheduleSort ss=findByUseridAndTitle(Struts2Utils.getSessionUser().getName(),"其他");
		scheduleSortDao.batchExecute("update Schedule set scheduleSortId=?  where scheduleSortId=? and receiverStatus=? ",ss.getId(),id,"1");
		scheduleSortDao.delete(id);
	}
	@Transactional
	public void updateTitle(ScheduleSort entity){
		scheduleSortDao.batchExecute("update ScheduleSort set title=?  where id=? and userName=? ",entity.getTitle(),entity.getId(),Struts2Utils.getSessionUser().getName());

	}
	
}
