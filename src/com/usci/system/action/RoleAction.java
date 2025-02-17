package com.usci.system.action;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lims.core.dto.CheckNode;
import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.entity.Role;
import com.usci.system.entity.User;
import com.usci.system.service.RoleService;
import com.usci.system.service.UserService;
/**
 * 角色管理，与权限设置
 * @author 聂梦肖
 *
 */
@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
@Result(name = "logins", type = "redirect", location = "session.jsp"),
	@Result(name = "modulepage", location = "/WEB-INF/content/systemmanage/rolemanage.jsp")
})
public class RoleAction extends CrudActionSupport<Role> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2071849371623152978L;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	private Role entity;
	private String jspPage;
	
	public String getJspPage() {
		return jspPage;
	}

	public void setJspPage(String jspPage) {
		this.jspPage = jspPage;
	}

	

	private Integer node;

	private String rightsIds;
	
	private String [] roleright;
	
	public String[] getRoleright() {
		return roleright;
	}

	public void setRoleright(String[] roleright) {
		this.roleright = roleright;
	}

	public String getRightsIds() {
		return rightsIds;
	}

	public void setRightsIds(String rightsIds) {
		this.rightsIds = rightsIds;
	}

	public Integer getNode() {
		return node;
	}

	public void setNode(Integer node) {
		this.node = node;
	}

	@Override
	protected void prepareModel() throws Exception {
		entity = new Role();
	}

	@Override
	public Role getModel() {
		return entity;
	}

	@Override
	public String save() throws Exception {
		List<Role> lr=roleService.findByRoleName(entity.getRoleName().trim());
			if("添加".equals(saveorupdate)){
				if(entity.getRoleName().trim().equals("")){ 
					msg.setSuccess(false);
					msg.setMsg("角色不能为空");
					Struts2Utils.renderJson(msg);
				}else{
					if(lr.size()>0){
						msg.setSuccess(false);
						msg.setMsg("角色已存在，请重新添加");
						Struts2Utils.renderJson(msg);
					}else{
						roleService.save(entity);
						msg.setMsg("恭喜你，添加角色成功");
						Struts2Utils.renderJson(msg);
					}
				}
			}
			
			if("修改".equals(saveorupdate)){
				int i=0;
				if(lr.size()>0){
					if(lr.size()==1){
						if(lr.get(0).getId().equals(entity.getId())){
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
					roleService.updaterole(entity);
					msg.setMsg("恭喜你，修改角色成功");
					Struts2Utils.renderJson(msg);
				}else{
					msg.setSuccess(false);
					msg.setMsg("角色已存在，请重新修改");
					Struts2Utils.renderJson(msg);
				}
			}
			
		return null;
	}

	public String update() throws Exception {
		
		
		if (rightsIds.length() < 1) {
			msg.setMsg("至少选择一个权限，请选择要设置的权限");
		} else {
			roleService.manageRoleRights(entity.getId(), rightsIds,roleright);
			msg.setMsg("恭喜你，角色权限设置成功");
		}

		Struts2Utils.renderJson(msg);
		return null;
	}

	@Override
	public String list() throws Exception {
		
		Page<Role> page = new Page<Role>(limit);
		page.setPageNo((start/limit)+1);
		roleService.findRoles(page,PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
		Struts2Utils.renderJson(page);
		return null;
	}
	public String listAll() throws Exception {
		Struts2Utils.renderJson(roleService.findAll());
		return null;
	}
	public String listRoleRights() {
		
		List<CheckNode> nodes = roleService.initAllRights(entity.getId(), node);
		
		Struts2Utils.renderJson(nodes);
		return null;
	}
	//权限分配
	public String listRoleRightsByUser(){
		User loginUser = (User) Struts2Utils.getSessionAttribute("loginUser");
		List<CheckNode> nodes = userService.initUserRoleRights(loginUser.getId(),entity.getId(), node);
		Struts2Utils.renderJson(nodes);
		return null;
	}
	//页面操作权限
	public String roleright(){

		Struts2Utils.renderJson(roleService.roleright(jspPage));
		
		return null;
	}
	
	public String updaterole(){
		List<Role> lr=roleService.findByRoleName(entity.getRoleName().trim());
		int i=0;
		if(lr.size()>0){
			if(lr.size()==1){
				
				if(lr.get(0).getId().equals(entity.getId())){
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
			roleService.updaterole(entity);
			msg.setMsg("恭喜你，修改角色成功");
			Struts2Utils.renderJson(msg);
		}else{
			msg.setSuccess(false);
			msg.setMsg("角色已存在，请重新修改");
			Struts2Utils.renderJson(msg);
		}
		return null;
	}


	@Override
	public String modulepage() throws Exception {
		
		if(entity.getRoleName()!=null&&!"".equals(entity.getRoleName())){
			Struts2Utils.getRequest().setAttribute("rolename", entity.getRoleName());
		}else{
			Struts2Utils.getRequest().setAttribute("rolename", "");
		}
		return "modulepage";
	}
	
	public void roleuser(){
		JSONObject json=new JSONObject();
		List<String> list = roleService.roleuser(entity.getId());
		json.put("names", list.get(0));
		json.put("moduleNames", list.get(1));
		json.put("rights", list.get(2));
		json.put("jspPages", list.get(3));
		Struts2Utils.renderJson(json);
	}
	public void delete(){
		Struts2Utils.renderJson(roleService.delete(entity.getId()));
	}
	public void pageRight(){
		Page<Role> page = new Page<Role>(limit);
		page.setPageNo((start/limit)+1);
		roleService.pageRight(page,jspPage);
		Struts2Utils.renderJson(page);
	}
}
