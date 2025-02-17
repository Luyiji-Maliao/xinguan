package com.usci.system.action;




import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.entity.Schedule;
import com.usci.system.service.ScheduleService;

/**
 * 
 * 待办事项
 *
 */
@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
@Result(name = "logins", type = "redirect", location = "session.jsp"),
@Result(name = "modulepage", location = "/WEB-INF/content/systemmanage/schedule.jsp"),
@Result(name = "scheduleread", location = "/WEB-INF/content/systemmanage/scheduleread.jsp")
})
public class ScheduleAction extends CrudActionSupport<Schedule> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2691425615270742167L;

	@Autowired
	private ScheduleService scheduleService;
	private Schedule entity;
	
	


	@Override
	public String list() throws Exception {
		Page<Schedule> page = new Page<Schedule>(limit);
		page.setPageNo((start / limit) + 1);
		scheduleService.findPage(page, PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
		Struts2Utils.renderJson(page);
		return null;
	}

	@Override
	public String modulepage() throws Exception {
		return "modulepage";
	}

	@Override
	protected void prepareModel() throws Exception {
		entity=new Schedule();
	}

	@Override
	public String save() throws Exception {
	
		if("添加".equals(saveorupdate)){
		
				scheduleService.saves(entity);
				msg.setMsg("添加待办成功");
				Struts2Utils.renderJson(msg);
		}
		
		if("修改".equals(saveorupdate)){
			Schedule s=scheduleService.findByTitleAndSsid(entity.getScheduleTitle(),entity.getScheduleReceiver(), entity.getScheduleSortId(),entity.getScheduleSenderDate());
			int i=0;
			if(s!=null){
				if(s.getId().equals(entity.getId())){//判断此id与前台传过来的id是否相同
					i=0;
				}else{
					i=1;
				}
			}else{
				i=0;
			}
			if(i==0){
				entity.setScheduleSender(Struts2Utils.getSessionUser().getName());
				scheduleService.save(entity);
				msg.setMsg("修改成功");
				Struts2Utils.renderJson(msg);
			}else{
				msg.setSuccess(false);
				msg.setMsg("修改的待办标题已存在，请重新修改");
				Struts2Utils.renderJson(msg);
			}
		}
		return null;
	}

	@Override
	public Schedule getModel() {
		return entity;
	}
	/**
	 * 分类与状态
	 * @return
	 */
	public String sortAndStatus(){
		scheduleService.sortAndStatus(entity);
		msg.setSuccess(true);
		msg.setMsg("操作完成");
		Struts2Utils.renderJson(msg);
		return null;
	}
	/**
	 * 状态修改
	 * @return
	 */
	public String updateStatus(){
		scheduleService.updateStatus(entity.getId());
		Struts2Utils.renderJson("操作完成");
		return null;
	}
	/**
	 * 任务驳回
	 * @return
	 */
	public String receiveStatus(){
		scheduleService.receiveStatus(entity.getId());
		Struts2Utils.renderJson("操作完成");
		return null;
	}
	
	public String scheduleread(){
		return "scheduleread";
	}
	
}
