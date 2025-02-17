package com.usci.system.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lims.core.utils.web.CrudActionSupport;
import com.usci.system.entity.User;
import com.usci.system.service.WorkdayCountService;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
	@Result(name = "logins", type = "redirect", location = "session.jsp"),
	@Result(name = "modulepage", location = "/WEB-INF/content/systemmanage/attendance.jsp"),
	@Result(name = "showattendance", location = "/WEB-INF/content/systemmanage/showattendance.jsp")
	})
/**
 * 计算工作日接口
 * 
 * @author 范秋月
 * */
public class WorkdaycountAction extends CrudActionSupport<User> {

	private static final long serialVersionUID = -5913293534284733232L;

	@Autowired
	private WorkdayCountService workdayCountService;
	@Override
	public User getModel() {
		return null;
	}
	
	/**
	 * 查询当天是否工作日
	 * */
	public String isWorkDay(String calendar) {
		int rtday = workdayCountService.getWeekOfDate(calendar);
		String  resultday="";
		if(rtday==6||rtday==7){
			resultday="no";
		}else{
			resultday="yes";
		}
		return resultday;
	}
	/**  
	  *   
	  * <p>Title: addDateByWorkDay </P> 
	  * 查询某个日期后的 days个 工作日之后的日期(排除周末后的日期)
	  * @param calendar  当前的日期  
	  * @param day  相加天数  
	  * @return     
	  * return String    返回类型   返回相加day天，并且排除周末后的日期    
	  */  
	public String addDateByWorkDayWeek(String calendar,int days) {  
		String resultday= workdayCountService.addDateByWorkDayWeek(calendar, days);
		return resultday;
	}
	
	/**  
	  *   
	  * <p>Title: addDateByWorkDay </P> 
	  * 查询某个日期后的 days个 工作日之后的日期
	  * @param calendar  当前的日期  
	  * @param day  相加天数  
	  * @return     
	  * return String    返回类型   返回相加day天，并且排除节假日和周末后的日期    
	  */  
	public String addDateByWorkDay(String calendar,int days) {  
		String resultday= workdayCountService.addDateByWorkDay(calendar, days);
		return resultday;
	}
	
	/**
	 * 查询两个日期之间有几个工作日
	 * */
	public int workDayCount(String calendar,String calendar2) {
		int result = workdayCountService.workDayCount(calendar,calendar2);
		
		return result;
	}
	/**
	 * 查询两个日期之间有几个工作日(只排除周末)
	 * */
	public int workDayCountWeek(String calendar,String calendar2) {
		int result = workdayCountService.workDayCount(calendar,calendar2);
		
		return result;
	}

	@Override
	public String modulepage() throws Exception {
		return null;
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		
	}

	@Override
	public String list() throws Exception {
		return null;
	}

}
