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
import com.usci.system.entity.Department;
import com.usci.system.entity.Position;
import com.usci.system.service.DepartmentService;
import com.usci.system.service.PositionService;


@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
/**
 * 职位
 * @author 聂梦肖
 *
 */
@Results({
	@Result(name = "logins", type = "redirect", location = "session.jsp"),
	@Result(name = "modulepage", location = "/WEB-INF/content/systemmanage/positionmanage.jsp")
	})
	/**
	 * 职位
	 */
public class PositionAction extends CrudActionSupport<Position> {
	
	private static final long serialVersionUID = -3602199213267115774L;

	private Position entity;

	@Autowired
	private PositionService positionService;
	@Autowired
	private DepartmentService departmentService;
	private Integer departmentId;
	

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	@Override
	protected void prepareModel() throws Exception {

		entity = new Position();

	}

	@Override
	public Position getModel() {
		return entity;
	}
/**
 * 保存
 */
	@Override
	public String save() throws Exception {
		
		Position p =positionService.findByPosnameAnddeptid(entity.getPosName(),departmentId);
		Department d=departmentService.get(departmentId);
		if("添加".equals(saveorupdate)){
			if(p!=null){
				msg.setSuccess(false);
				msg.setMsg("添加的职位已存在，请重新添加");
				Struts2Utils.renderJson(msg);
			}else{
				entity.setDepartment(d);
				positionService.save(entity);
				msg.setMsg("添加成功");
				Struts2Utils.renderJson(msg);
			}
		}
		if("修改".equals(saveorupdate)){
			int i=0;
			if(p!=null){//此部门有此职位
				if(p.getId().equals(entity.getId())){//判断此职位id与前台传过来的id是否相同
					i=0;
				}else{
					i=1;
				}
			}else{
				i=0;
			}
			if(i==0){
				entity.setDepartment(d);
				positionService.save(entity);
				msg.setMsg("修改成功");
				Struts2Utils.renderJson(msg);
			}else{
				msg.setSuccess(false);
				msg.setMsg("修改的职位已存在，请重新修改");
				Struts2Utils.renderJson(msg);
			}
		}
		return null;
	}


	@Override
	public String list() throws Exception {		
		Page<Position> page = new Page<Position>(limit);
		page.setPageNo((start / limit) + 1);
		positionService.findPage(page, PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
		Struts2Utils.renderJson(page);
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
	

}
