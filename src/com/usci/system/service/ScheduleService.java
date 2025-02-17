package com.usci.system.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.dao.ScheduleDao;
import com.usci.system.entity.EverCookies;
import com.usci.system.entity.Schedule;
import com.usci.system.entity.ScheduleSort;

@Component
@Transactional
public class ScheduleService {
	@Autowired
	private ScheduleDao scheduleDao;
	@Autowired
	private ScheduleSortService scheduleSortService;
	@Autowired
	private InformsService informsService;
	@Autowired
	private EverCookiesService everCookiesService;
	@Autowired
	private UserService userService;
	
	@Transactional
	public void save(Schedule entity) {
		scheduleDao.merge(entity);
	}
	
	@Transactional
	public void saves(Schedule entity){
		String [] receivers=entity.getScheduleReceiver().split(",");
		for (String re : receivers) {
			ScheduleSort ss = scheduleSortService.findByUseridAndTitle(re, "其他");
			Integer ssid=0;
			if(!"".equals(re)){
				if(re.equals(Struts2Utils.getSessionUser().getName())){//发送人和接收人相同
					if(entity.getScheduleSortId()!=null){
						ssid=entity.getScheduleSortId();	
					}else{
						ssid=ss.getId();
					}
					
				}else{
					if(ss==null){
						   ScheduleSort sss=new ScheduleSort();
					       sss.setTitle("其他");
					       sss.setSsStatus(1);
					       sss.setUserName(re);
					       ssid=scheduleSortService.returnsave(sss);
					       //把位置保存到数据库
					       EverCookies ec=new EverCookies();
					       ec.setCookieContent("[{'id' : "+ssid+",'col' : 0}]");
					       ec.setCookieName("schedulesortcookie");
					       ec.setUserId(userService.findByName(re).getId());
					       everCookiesService.save(ec);
					}else{
						ssid=ss.getId();
					}
				}
				
					entity.setScheduleReceiver(re);
					entity.setPriorityLevel(1);
					entity.setScheduleSender(Struts2Utils.getSessionUser().getName());
					entity.setScheduleSenderDate(Struts2Utils.getStringDate(new Date()));
					entity.setScheduleSortId(ssid);
					scheduleDao.merge(entity);
					
					//scheduleSave("程序测试20", "管理员", "管理员", "2015-08-09", "asdfsafdsdfsaf", "https://www.baidu.com");
			}
			
		}
	}
	@Transactional(readOnly=true)
	public void findPage(Page<Schedule> page,List<PropertyFilter> filters){
		page.setOrder("asc,desc");
		page.setOrderBy("priorityLevel,scheduleSenderDate");
			scheduleDao.findPage(page, filters);
			
			
	}
	/**
	 * 标题,接收人和待办分类id
	 * @param title
	 * @param ssid
	 * @return
	 */
	@Transactional(readOnly=true)
	public Schedule findByTitleAndSsid(String title,String scheduleReceiver,Integer ssid,String senderdate){
		return scheduleDao.findUnique(Restrictions.eq("scheduleTitle", title),Restrictions.eq("scheduleReceiver", scheduleReceiver),Restrictions.eq("scheduleSortId", ssid),Restrictions.eq("scheduleSenderDate", senderdate));
	}
/**
 * 分类与状态
 * @param entity
 */
	public void sortAndStatus(Schedule entity) {
		if(entity.getScheduleStatus().equals("1")){//未完成
			scheduleDao.batchExecute("update Schedule set scheduleSortId=? , scheduleStatus=? where id=? ",entity.getScheduleSortId(),entity.getScheduleStatus(),entity.getId());
		}
		if(entity.getScheduleStatus().equals("0")){//完成（添加完成时间）
			scheduleDao.batchExecute("update Schedule set scheduleSortId=? , scheduleStatus=?,scheduleFinishDate=? where id=? ",entity.getScheduleSortId(),entity.getScheduleStatus(),Struts2Utils.getStringDate(new Date()),entity.getId());
		}
	} 
/**
 * 修改状态（完成）
 * @param id
 */
	@Transactional
	public void updateStatus(Integer id) {
		scheduleDao.batchExecute("update Schedule set  scheduleStatus=?,scheduleFinishDate=? where id=? ","0",Struts2Utils.getStringDate(new Date()),id);
	
		//添加到消息中心（给发送人提醒）
		Schedule s=scheduleDao.get(id);
		if(!s.getScheduleSender().equals(Struts2Utils.getSessionUser().getName())){//自己给自己下任务，不提醒到消息中心
			informsService.saveInforms("(完成)"+s.getScheduleTitle(), s.getScheduleReceiver(), s.getScheduleSender(), s.getScheduleContent(), s.getScheduleLink());
		}
		}
	/**
	 * 任务驳回
	 * @param id
	 */
	@Transactional
	public void receiveStatus(Integer id){
		scheduleDao.batchExecute("update Schedule set  receiverStatus=? where id=? ","0",id);
		
		//添加到消息中心（给发送人提醒）
		Schedule s=scheduleDao.get(id);
		if(!s.getScheduleSender().equals(Struts2Utils.getSessionUser().getName())){//自己给自己下任务，不提醒到消息中心
			informsService.saveInforms("(拒绝)"+s.getScheduleTitle(), s.getScheduleReceiver(), s.getScheduleSender(), s.getScheduleContent(), s.getScheduleLink());
		}
		
	}
	
	@Transactional
	public void scheduleSave(String scheduleTitle,String scheduleSender,String scheduleReceiver,String scheduleTime,String scheduleContent,String scheduleLink){
		Schedule s=new Schedule();
		s.setScheduleTitle(scheduleTitle);
		s.setScheduleSender(scheduleSender);
		s.setScheduleReceiver(scheduleReceiver);
		s.setScheduleTime(scheduleTime);
		s.setScheduleContent(scheduleContent);
		s.setScheduleLink(scheduleLink);
		s.setReceiverStatus("1");
		s.setScheduleStatus("1");
		s.setPriorityLevel(1);
		s.setScheduleSenderDate(Struts2Utils.getStringDate(new Date()));
		
		ScheduleSort ss = scheduleSortService.findByUseridAndTitle(scheduleReceiver, "其他");
		Integer ssid=0;
		if(ss==null){
			   ScheduleSort sss=new ScheduleSort();
		       sss.setTitle("其他");
		       sss.setSsStatus(1);
		       sss.setUserName(scheduleReceiver);
		       ssid=scheduleSortService.returnsave(sss);
		     //把位置保存到数据库
		       EverCookies ec=new EverCookies();
		       ec.setCookieContent("[{'id' : "+ssid+",'col' : 0}]");
		       ec.setCookieName("schedulesortcookie");
		       ec.setUserId(userService.findByName(scheduleReceiver).getId());
		       everCookiesService.save(ec);
		}else{
			ssid=ss.getId();
		}
		s.setScheduleSortId(ssid);
		scheduleDao.save(s);
	}
}
