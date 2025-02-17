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
import com.usci.system.entity.Informs;
import com.usci.system.service.InformsService;

/**
 * 
 * 消息中心
 *
 */
@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
@Result(name = "logins", type = "redirect", location = "session.jsp")
})
public class InformsAction extends CrudActionSupport<Informs> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 225326543028900566L;
	
	@Autowired
	private InformsService informsService;
	
	private Informs entity;
	



	@Override
	public String list() throws Exception {
		Page<Informs> page = new Page<Informs>(limit);
		page.setPageNo((start / limit) + 1);
		informsService.findPage(page, PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
		Struts2Utils.renderJson(page);
		return null;
	}

	@Override
	public String modulepage() throws Exception {
		return "modulepage";
	}

	@Override
	protected void prepareModel() throws Exception {
		entity=new Informs();
	}

	@Override
	public String save() throws Exception {
		
		informsService.saves(entity);
		msg.setMsg("成功添加消息");
		Struts2Utils.renderJson(msg);
		return null;
	}

	@Override
	public Informs getModel() {
		return entity;
	}

	/**
	 * 状态修改
	 * @return
	 */
	public String updateStatus(){
		informsService.updateStatus(entity.getId());
		Struts2Utils.renderJson("操作完成");
		return null;
	}
}
