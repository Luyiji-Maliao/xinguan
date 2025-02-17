package com.usci.system.action;



import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.entity.EverCookies;
import com.usci.system.service.EverCookiesService;

/**
 * 
 * cookies保存
 *
 */
@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Result(name = "logins", type = "redirect", location = "session.jsp")
public class EvercookiesAction extends CrudActionSupport<EverCookies> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3011129323394916264L;
	/**
	 * 
	 */
	@Autowired
	private EverCookiesService everCookiesService;
	private EverCookies entity;
	
	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	public String modulepage() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		entity=new EverCookies();
	}

	@Override
	public String save() throws Exception {
		
		EverCookies ec=everCookiesService.findByUseridAndcookiesName(Struts2Utils.getSessionUser().getId(), entity.getCookieName());
		if(ec!=null){
			entity.setId(ec.getId());
		}
		entity.setUserId(Struts2Utils.getSessionUser().getId());
		everCookiesService.save(entity);
		return null;
	}

	@Override
	public EverCookies getModel() {
		return entity;
	}
	
	public String readCookie(){
		EverCookies ec=everCookiesService.findByUseridAndcookiesName(Struts2Utils.getSessionUser().getId(), entity.getCookieName());
		if(ec!=null){
			Struts2Utils.renderJson(ec.getCookieContent());	
		}else{
			Struts2Utils.renderJson(0);
		}
		
		return null;
	}
	
}
