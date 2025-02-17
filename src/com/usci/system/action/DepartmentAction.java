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
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.entity.Department;
import com.usci.system.service.DepartmentService;


@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
/**
 * 部门
 * @author 聂梦肖
 *
 */
@Results({
	@Result(name = "logins", type = "redirect", location = "session.jsp"),
	@Result(name = "modulepage", location = "/WEB-INF/content/systemmanage/department.jsp"),
	@Result(name = "deptuser", location = "/WEB-INF/content/systemmanage/departmentuser.jsp")
	})
public class DepartmentAction extends CrudActionSupport<Department> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7141593270070990673L;

	private Department entity;

	@Autowired
	private DepartmentService departmentService;
	private Integer deptid;
	
	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	public Integer getDeptid() {
		return deptid;
	}

	@Override
	protected void prepareModel() throws Exception {

		entity = new Department();

	}

	@Override
	public Department getModel() {
		return entity;
	}
/**
 * 保存
 */
	@Override
	public String save() throws Exception {
		StringBuffer sb=new StringBuffer();
		String deptNames=entity.getDeptName();
		if(!"".equals(deptNames.trim())){
			List<Department> dn=departmentService.findBydeptName(deptNames);
				if(dn.size()>0){
					sb.append(deptNames);
				}else{
					if(entity.getParentid()!=null&&!"".equals(entity.getParentid().toString())){//存在父节点
						Department d=departmentService.get(entity.getParentid());
						entity.setParent(d);
					}
					departmentService.save(entity);
				}
			
		}
		if(sb.length()>0){
			msg.setSuccess(false);
			msg.setMsg("重复部门："+sb.toString());
		}else{
			msg.setMsg("部门添加完成");
		}
		Struts2Utils.renderJson(msg);
		return null;
	}

	/**
	 * 修改
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception{
		String deptNames=entity.getDeptName();
		List<Department> lo=departmentService.findBydeptName(deptNames);
		int i=0;
		if(lo.size()>0){
			if(lo.size()==1){
				if(lo.get(0).getId().equals(entity.getId())){
					i=0;
				}else{
					i=1;
				}
			}else{
				i=1;
			}
		}else{
			i=0;
		}
		if(i==0){
			//获取父节点
			if(entity.getParentid()!=null&&!"".equals(entity.getParentid().toString())){//存在父节点
				Department p=departmentService.get(entity.getParentid());
				entity.setParent(p);
			}
			entity.setId(deptid);
			departmentService.update(entity);
			msg.setMsg("完成部门修改");
		}else{
			msg.setSuccess(false);
			msg.setMsg("部门已存在，请重新修改");
		}
		Struts2Utils.renderJson(msg);
		return null;
	}

	@Override
	public String list() throws Exception {		
		Struts2Utils.renderJson(departmentService.list(limit));
		return null;
	}

/**
 * grid显示
 * @throws Exception
 */
	 @SuppressWarnings("unchecked")
	@Action("departmentShowJson")
	   public void departmentShowJson() throws Exception {
	    
	     List<Department> aList = this.departmentService.findDepartment();
	     String a = this.departmentService.getJson(aList);	 
	     Struts2Utils.sendDataJson(a, ServletActionContext.getResponse());
	   }
	 
	 /**
	  * grid显示s
	  * @throws Exception
	  */
	 	 @SuppressWarnings("unchecked")
	 	@Action("departmentShowJsons")
	 	   public void departmentShowJsons() throws Exception {
	 	     List<DepartUserNode> aList = this.departmentService.findDeptuser();
	 	     String a = this.departmentService.getJsons(aList);	 
	 	     Struts2Utils.sendDataJson(a, ServletActionContext.getResponse());
	 	   }

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 部门用户显示
	 * @return
	 */
	public String deptuser(){
		return "deptuser";
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
