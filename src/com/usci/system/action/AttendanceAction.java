package com.usci.system.action;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lims.core.dto.DepartUserNode;
import com.lims.core.orm.PageOrder;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.entity.Attendance;
import com.usci.system.entity.Department;
import com.usci.system.service.AttendanceService;
import com.usci.system.service.DepartmentService;


@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
/**
 * 考勤
 * @author 聂梦肖
 *
 */
@Results({
	@Result(name = "logins", type = "redirect", location = "session.jsp"),
	@Result(name = "modulepage", location = "/WEB-INF/content/systemmanage/attendance.jsp"),
	@Result(name = "showattendance", location = "/WEB-INF/content/systemmanage/showattendance.jsp")
	})
public class AttendanceAction extends CrudActionSupport<Attendance> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7913638393582290115L;

	private Attendance entity;

	@Autowired
	private AttendanceService attendanceService; 

	@Override
	protected void prepareModel() throws Exception {

		entity = new Attendance();

	}

	@Override
	public Attendance getModel() {
		return entity;
	}
/**
 * 保存
 */
	@Override
	public String save() throws Exception {
		
		msg.setSuccess(true);
		Struts2Utils.renderJson(msg);
		return null;
	}

	/**
	 * 修改
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception{
		
		msg.setSuccess(true);
		Struts2Utils.renderJson(msg);
		return null;
	}

	@Override
	public String list() throws Exception {		
		PageOrder<Attendance> page = new PageOrder<Attendance>(limit);
		page.setPageNo((start / limit) + 1);
		attendanceService.findPage(page, PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
		Struts2Utils.renderJson(page);
		return null;
	}

	

	@Override
	public String input() throws Exception {
		return null;
	}

	
	/**
	 * 列表页
	 * @return
	 */
	@Override
	public String modulepage() throws Exception {
		return "modulepage";
	}
	
	public String showattendance(){
		return "showattendance";
	}
	
	/**
	 * 查看考勤
	 */
	public String showAttendance(){
		Struts2Utils.renderJson(attendanceService.showAttend(entity.getAttDate()));
		return null;
	}

}
