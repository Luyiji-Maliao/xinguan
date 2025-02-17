package com.usci.sample.action;



import java.util.List;

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
import com.usci.sample.entity.BuildLibraries;
import com.usci.sample.service.BuildLibrariesService;



@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
/**
 * 样本中心/建库/下单
 * @author 聂梦肖
 *
 */
@Results({
	@Result(name = "logins", type = "redirect", location = "session.jsp"),
	@Result(name = "modulepage", location = "/WEB-INF/content/sample/buildlibraries/buildlibraries.jsp"),
	@Result(name = "buildlibrariesnew", location = "/WEB-INF/content/sample/buildlibraries/buildlibraries_new.jsp"),
	@Result(name = "feedbacking", location = "/WEB-INF/content/sample/buildlibraries/buildlibraries_feedbacking.jsp"),
	@Result(name = "feedlook", location = "/WEB-INF/content/sample/buildlibraries/buildlibraries_feedlook.jsp"),
	@Result(name = "newlook", location = "/WEB-INF/content/sample/buildlibraries/buildlibraries_newlook.jsp")
	})
	
public class BuildlibrariesAction extends CrudActionSupport<BuildLibraries> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5842226129886749480L;
	private BuildLibraries entity;
	private String itemsinfo;
	@Autowired
	private BuildLibrariesService buildlibrariesService;

	
	public String getItemsinfo() {
		return itemsinfo;
	}

	public void setItemsinfo(String itemsinfo) {
		this.itemsinfo = itemsinfo;
	}

	@Override
	protected void prepareModel() throws Exception {

		entity = new BuildLibraries();

	}

	@Override
	public BuildLibraries getModel() {
		return entity;
	}


	public String updatexiadanstate(){
		Struts2Utils.getSession().getServletContext().setAttribute("isxiadan",0);
		return null;
	}

	public String getUserId(){
		Struts2Utils.renderJson(Struts2Utils.getSessionUser().getName()+","+Struts2Utils.getSession().getServletContext().getAttribute("isxiadan")+","+Struts2Utils.getSession().getServletContext().getAttribute("hsxiadan"));
		return null;
	}


	@Override
	public String list() throws Exception {

		return null;
	}




	/**
	 * 列表页
	 * @return
	 */
	@Override
	public String modulepage() throws Exception {
		// TODO Auto-generated method stub
		return "modulepage";
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	/**
	 * 下单（新建）页
	 * @return
	 */
	public String buildLibrariesNew()  {
		// TODO Auto-generated method stub
		return "buildlibrariesnew";
	}
	

	
	private int orderfeedback;
	
	public int getOrderfeedback() {
		return orderfeedback;
	}

	public void setOrderfeedback(int orderfeedback) {
		this.orderfeedback = orderfeedback;
	}

	




}
