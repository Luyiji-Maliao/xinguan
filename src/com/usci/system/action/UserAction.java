package com.usci.system.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sun.misc.BASE64Decoder;


import com.lims.core.dto.Node;
import com.lims.core.dto.Node1;
import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.ndktools.javamd5.Mademd5;
import com.usci.system.entity.User;
import com.usci.system.service.DepartmentService;
import com.usci.system.service.UserService;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
	@Result(name = "return_login", type = "redirect", location = "uscilims/welcome.jsp"),
	@Result(name = "dashthings", location = "/WEB-INF/content/systemmanage/dashthings.jsp"),
	@Result(name = "mobilelogin", location = "/WEB-INF/content/systemmanage/mobilelogin.jsp"),
	@Result(name = "modulepage", location = "/WEB-INF/content/systemmanage/usermanage.jsp"),
	@Result(name = "addressbook", location = "/WEB-INF/content/usertools/addressbook.jsp"),
	@Result(name = "initmain", location = "/WEB-INF/content/systemmanage/main.jsp"),
	@Result(name = "remain", location = "/WEB-INF/content/systemmanage/main.jsp"),
	@Result(name = "logins", type = "redirect", location = "session.jsp")
	})
	
public class UserAction extends CrudActionSupport<User> {
	private static final long serialVersionUID = -6048703791120857251L;
	@Autowired
	private UserService userService;
	@Autowired
	private DepartmentService departmentService;
	private User entity;
	private Integer node;
	private List<Integer> roleIds;
	private String uproleIds;//修改时角色（系统删除了）添加
	
	public String getUproleIds() {
		return uproleIds;
	}
	public void setUproleIds(String uproleIds) {
		this.uproleIds = uproleIds;
	}
	//拍照上传服务器
	private String img;
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}

	public Integer getNode() {
		return node;
	}

	public void setNode(Integer node) {
		this.node = node;
	}

	@Override
	protected void prepareModel() throws Exception {

		entity = new User();

	}

	@Override
	public User getModel() {
		return entity;
	}
	/**
	 *保存或修改
	 */
	@Override
	public String save() throws Exception {
		
		Mademd5 md=new Mademd5();
		
		if("添加".equals(saveorupdate)){
			List<User> lu=userService.findByUserName(entity.getUsername().trim());
				if(entity.getUsername().trim().equals("")){
					msg.setSuccess(false);
					msg.setMsg("账号不能为空");
					Struts2Utils.renderJson(msg);
				}else{
					if(lu.size()>0){//已存在
						msg.setSuccess(false);
						msg.setMsg("添加的账号已存在，请重新添加");
						Struts2Utils.renderJson(msg);
					}else{
						if(!"".equals(entity.getPassword())){
						
							entity.setPassword(md.toMd5(entity.getPassword())); //加密
						}else{
						
							entity.setPassword(md.toMd5("0000")); //加密
						}
						
						userService.save(entity);
						msg.setMsg("操作成功");
						Struts2Utils.renderJson(msg);
					}
				}
		}
		
		if("修改".equals(saveorupdate)){
			List<User> lu=userService.findListName(entity.getName().trim());
			int i=0;
			if(lu.size()>0){
				if(lu.size()==1){
					if(lu.get(0).getId().equals(entity.getId())){
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
				List<Integer> li=new ArrayList<Integer>();
				if(!uproleIds.trim().equals("")){
					String v=uproleIds.substring(0,uproleIds.length()-1);
					
					String [] vv=v.split(",");
					for (String s : vv) {
						li.add(Integer.parseInt(s));
					}
				}
				if(entity.getPassword().trim().equals("")){
					User temp=userService.get(entity.getId());
					entity.setPassword(temp.getPassword());
				}else{
					entity.setPassword(md.toMd5(entity.getPassword())); //加密
				}
				userService.update(entity,li);
				msg.setMsg("恭喜你，修改用户成功");
				Struts2Utils.renderJson(msg);
			}else{
				msg.setSuccess(false);
				msg.setMsg("用户已存在，请重新修改");
				Struts2Utils.renderJson(msg);
			}
		}
		
		return null;
	}


	
	/**
	 * 分配角色
	 * @return
	 */
	public void userRole() {
		if (roleIds == null) {
			msg.setMsg("至少选择一个角色，请选择要设置的角色");
			msg.setSuccess(false);
		} else {
			userService.userRole(entity.getId(), roleIds);
			msg.setMsg("恭喜你，分配角色成功！");
		}

		Struts2Utils.renderJson(msg);
	}

	@Override
	public String list() throws Exception {
		Page<User> page = new Page<User>(limit);
		page.setPageNo((start / limit) + 1);
		userService.findPage(page, PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
		Struts2Utils.renderJson(page);
		return null;
	}
	
	public String check() throws Exception {
		String r = "";
		if(entity.getName()!=null){
			String names[]= entity.getName().split(",");
			for(int i=0;i<names.length;i++){
				User u = userService.findByName(names[i]);
				if(u==null){
					r=names[i]+"不存在，请重新选择";
					break;
				}
			}
			
		}
		Struts2Utils.renderJson(r);
		return null;
	}
	
	
	/**
	 * 左侧树菜单
	 * @return
	 */
	public String initMenu() {
		User loginUser = (User) Struts2Utils.getSessionAttribute("loginUser");
		//List<Node1> nodes = userService.initUserRights1(loginUser.getId(), entity.getId());
		List<Node> nodes = userService.initUserRights(loginUser.getId(), entity.getId());

		Struts2Utils.renderJson(nodes);
		return null;
	}

	/**
	 * 返回登陆
	 * @return
	 */
	public String returnlogin(){
	
		return "return_login";
	}
	/**
	 * 初始化显示
	 * @return
	 */
	public String toDashboard(){
		return "todashboard";
	}
	/**
	 * 角色管理
	 * @return
	 */
	public String role_manage(){
		return "torole_manage";
	}
	/**
	 * 初始化
	 * @return
	 */
	public String initmain(){
		
		return "initmain";
	}
	/**
	 * 重新加载
	 * @return
	 */
	public String remain(){
		Struts2Utils.getSession().setAttribute("tiaozhuaninit", "0");
		return "remain";
	}
	//手机登陆内外网
	public String mobilelogin(){
		return "mobilelogin";
	}
	/**
	 * 登陆界面
	 * @return
	 */
	public String logins(){
		return "logins";
	}
	
	/**
	 * 身份证号验证
	 * @return
	 */
	public String checkIDnumber(){
		User u=userService.checkIDnumber(entity.getIdNumber());
		if("添加".equals(saveorupdate)){
			
			if(u!=null){
				Struts2Utils.renderJson(0);
			}else{
				Struts2Utils.renderJson(1);
			}
		}
		if("修改".equals(saveorupdate)){
			
			if(u!=null){
				
				if(u.getId().equals(entity.getId())){//同一个账号
					Struts2Utils.renderJson(1);
				}else{
					Struts2Utils.renderJson(0);
				}
				
			}else{
				Struts2Utils.renderJson(1);
			}
		}
		
		return null;
	}
	
	
	/**
	 * 邮箱验证
	 * @return
	 */
	public String checkEmail(){
		User u=userService.checkEmail(entity.getEmail());
		if("添加".equals(saveorupdate)){
			if(u!=null){
				Struts2Utils.renderJson(0);
			}else{
				Struts2Utils.renderJson(1);
			}
		}
		if("修改".equals(saveorupdate)){
			if(u!=null){
				if(u.getId().equals(entity.getId())){//同一个账号
					Struts2Utils.renderJson(1);
				}else{
					Struts2Utils.renderJson(0);
				}
				
			}else{
				Struts2Utils.renderJson(1);
			}
		}
		
		return null;
	}
	
	/**
	 * 用户姓名验证
	 * @return
	 */
	public String checkName(){
		User u=userService.findByName(entity.getName());
		if("添加".equals(saveorupdate)){
			if(u!=null){
				Struts2Utils.renderJson(0);
			}else{
				Struts2Utils.renderJson(1);
			}
		}
		if("修改".equals(saveorupdate)){
			if(u!=null){
				if(u.getId().equals(entity.getId())){//同一个账号
					Struts2Utils.renderJson(1);
				}else{
					Struts2Utils.renderJson(0);
				}
			}else{
				Struts2Utils.renderJson(1);
			}
		}
		
		return null;
	}
	
	/**
	 * 手机号验证
	 * @return
	 */
	public String checkMobile(){
		User u=userService.findByMobile(entity.getMobile());
		if("添加".equals(saveorupdate)){
			if(u!=null){
				Struts2Utils.renderJson(0);
			}else{
				Struts2Utils.renderJson(1);
			}
		}
		if("修改".equals(saveorupdate)){
			if(u!=null){
				if(u.getId().equals(entity.getId())){//同一个账号
					Struts2Utils.renderJson(1);
				}else{
					Struts2Utils.renderJson(0);
				}
			}else{
				Struts2Utils.renderJson(1);
			}
		}
		
		return null;
	}


	//登陆者信息
	public String preupdate(){
		
		User tempuser=(User)Struts2Utils.getSession().getAttribute("loginUser");
		User u=userService.get(tempuser.getId());
		if(u.getDepartmentId()!=null){
			u.setDepartment(departmentService.get(u.getDepartmentId()));
		}
		Struts2Utils.renderJson(u);
		return null;
	}
	/**
	 * 个人设置
	 * @return
	 */
	public String personalSet(){
		
		userService.updateUser(entity);
		msg.setMsg("员工信息已修改");
		Struts2Utils.renderJson(msg);
		return null;
	}
	
	/**
	 * 在线拍照上传到服务器
	 */
	public String html5photo(){
		
		if(null == img || img.length() < 100){  
		    // 数据太短，明显不合理  
		
		} else {  
		    // 去除开头不合理的数据  
		    img = img.substring(30);  
		    try {
				img = URLDecoder.decode(img,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		      
		}    
		BASE64Decoder decoder = new BASE64Decoder();  
        try {
			byte[] data = decoder.decodeBuffer(img);
			for(int i=0;i<data.length;++i)  
	        {  
	            if(data[i]<0)  
	            {  
	                //调整异常数据  
	                data[i]+=256;  
	            }  
	        }  
		
		    String imageName = System.currentTimeMillis()+".png";  
		    String photoPath = "D:/upload/";
		    if(Struts2Utils.getPath()!=null){
		    	photoPath=Struts2Utils.getPath();
		    }
		      
		    File photoPathFile = new File(photoPath);  
		 // 写入到文件  
	        FileOutputStream outputStream = new FileOutputStream(new File(photoPathFile,imageName));  
	          
	        outputStream.write(data);  
	        outputStream.flush();  
	        outputStream.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		 
		Struts2Utils.renderJson("0");
		return null;
	}
/**
 * 用户list
 */
	@Override
	public String modulepage() throws Exception {
		return "modulepage";
	}
	/**
	 * 桌面
	 * @return
	 */
	public String dashthings(){
		return "dashthings";
	}
	/**
	 * 下拉列表
	 * @return
	 */
	public String listcombo(){
		Struts2Utils.renderJson(userService.list(limit));

		return null;
	}
	/**
	 * 通讯录跳转
	 * @return
	 */
	public String addressbook(){
		return "addressbook";
	}
	
	/**
	 * 通讯录查询
	 * @return
	 */
	public String addressbooklist(){
		Page<User> page = new Page<User>(limit);
		page.setPageNo((start / limit) + 1);
		userService.findPage(page, PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
		Struts2Utils.renderJson(page);
		return null;
	}

	/**
	 * 获取用户详细信息
	 */
	public String findUserById(){
		Struts2Utils.renderJson(userService.get(entity.getId()));
		return null;
	}
	
	/**
	 * 获取服务器时间
	 * @return
	 */
	public String servertime(){
		//Struts2Utils.renderJson(Struts2Utils.getStringDatehmss(new Date()));//详细时间	
		Struts2Utils.renderJson(Struts2Utils.getStringDate(new Date()));//详细时间	
		return null;
	}
	
	public void delete(){
		userService.delete(entity.getId());
		Struts2Utils.renderJson(0);
	}
	
}