package com.usci.system.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lims.core.dto.Node;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lims.core.dto.CheckNode;
import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.Struts2Utils;

import com.usci.system.dao.CopyRoleRightDao;
import com.usci.system.dao.RightDao;
import com.usci.system.dao.RoleDao;
import com.usci.system.entity.CopyRoleRight;
import com.usci.system.entity.Right;
import com.usci.system.entity.Role;
import com.usci.system.entity.User;

@Component
@Transactional(readOnly=true)
public class RoleService {
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RightDao rightDao;
	@Autowired
    private CopyRoleRightService copyrolerightService;	
	//private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RightService rightService;
	@Autowired
	private UserService userService;
	@Autowired
	private CopyRoleRightDao copyrolerightDao;
	@Transactional
	public void save(Role entity) {
		if(entity.getIsSysNeeded()==null){
			entity.setIsSysNeeded(1);
		}
		entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		roleDao.save(entity);
	}

	public List<Role> listRoles(List<Integer> ids) {
		return roleDao.get(ids);
	}

	public Role findRole(Integer id) {
		return roleDao.get(id);
	}
	@Transactional
	public void deleteRole(int id){
		roleDao.delete(id);
	}
	public void findRoles(Page<Role> page,List<PropertyFilter> filters) {
		
		page.setOrderBy("roleName");
		page.setOrder("ASC");
		roleDao.findPage(page,filters);
	}
	
	/*public List<Role> findRoles(List<Integer> ids){
		return roleDao.get(ids);
	}*/
	
	public List<Role> findAll(){
		return roleDao.getAll();
	}

	@SuppressWarnings("unchecked") //当前角色拥有的权限
	public List<CheckNode> initRoleRights(Integer roleId, Integer parentId) {

		List<CheckNode> nodes = Lists.newArrayList();
		List<Node> userRights = null;
		
		String hql = "select rights.id as ids,rights.moduleName as text,rights.isLeaf as leaf,rights.moduleUrl as jspPage from sys_role role " +
				" join sys_role_sys_right srsr on(srsr.sys_role_id = role.id) " +
				" join sys_right as rights on (rights.id = srsr.rights_id) where role.id="+roleId;
		if (parentId == null) {
			hql += " and rights.parent_id is null";
			userRights = roleDao.entityBySql(hql,Node.class);
		} else {
			hql += " and rights.parent_id="+parentId;
			userRights = roleDao.entityBySql(hql,Node.class);
		}

		for (Node objects : userRights) {
			CheckNode node = new CheckNode();
			node.setId("" + objects.getIds());
			// node.setIconCls("menuIcon");
			nodes.add(node);
		}

		return nodes;
	}

	@SuppressWarnings("unchecked")
	public List<CheckNode> initAllRights(Integer roleId, Integer parentId) {

		List<Node> rights = null; //查询所有
		String hql = "select ri.id as ids,ri.moduleName as text,ri.isLeaf as leaf,ri.moduleUrl as url,ri.right as right_web_num from sys_right ri";
		if (parentId == null) {
			hql += " where ri.parent_id is null";
			rights = roleDao.entityBySql(hql, Node.class);
		} else {
			hql += " where ri.parent_id=?";
			rights = roleDao.entityBySql(hql, Node.class);
		}

		List<CheckNode> nodes = Lists.newArrayList();
		List<CheckNode> userNodes = initRoleRights(roleId, parentId); //当前角色所有
		
		
		for (Node objects : rights) {
			CheckNode node = new CheckNode();
			node.setId("" + objects.getIds());
			if (userNodes.contains(node)) {
				node.setChecked(true);
			}
			
			
				List<CopyRoleRight> lcrrs=copyrolerightDao.find(Restrictions.eq("roleid", roleId),Restrictions.eq("rightid", objects.getIds()));
				if(lcrrs.size()>0){
					node.setRoleright(lcrrs.get(0).getRoleHaveRight());
				}else{
					node.setRoleright("0"); //数据库中不存在这条权限的子权限
				}
			nodes.add(node);
		}
		return nodes;
	}
	/**
	 * 
	 * @param roleId(角色)
	 * @param rightsIds(权限数组)
	 */
	@Transactional
	public void manageRoleRights(Integer roleId,String rightsIds, String [] roleright){
		String[] sids = rightsIds.split(",");
		List<Integer> ids = Lists.newArrayList();
		List<String> rolerights=new ArrayList<String>();
		//权限
		for (String id : sids) {
			ids.add(Integer.parseInt(id));
		}
		//子权限
		if(roleright!=null&&roleright[0].indexOf(",") > -1){
			
			for (String st : roleright) {
				rolerights.add(st);
			}
		}
		List<Right> rights = rightDao.get(ids);
		//Role role = roleDao.get(roleId);
		String deletesql = "delete from sys_role_sys_right where sys_role_id="+roleId;
		try {
			roleDao.deleteBySql(deletesql);
			for (Right right: rights) {
				roleDao.updateBySql("insert into sys_role_sys_right(sys_role_id,rights_id) values ("+roleId+","+right.getId()+")");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		//role.setRights(rights);
		List<CopyRoleRight> lcrr = copyrolerightDao.entityBySql("select * from sys_copyroleright where roleid="+roleId,CopyRoleRight.class);

		//把角色权限表拷贝到copyroleright表中
	//	if(lcrr!=null&&lcrr.size()>0){ //是否有当前角色
			//copyrolerightDao.delete(roleId);//删除角色原有的数据（重新添加）(批量删除，有问题)
			/**
			 * 此嵌套for循环，只为删除数据表（copyroleright）中多余的数据（权限）
			 */
			for (int i = 0; i < lcrr.size(); i++) {//循环当前角色（数据库）的权限
				int j=0;
				
				for ( j = 0; j < ids.size(); j++) { //循环从前台得到的权限（id）
					if(lcrr.get(i).getRightid().equals( ids.get(j) ) ){
						break;
					}
				}
				
				if(j == ids.size()){ //删除数据表中多余的权限
					copyrolerightDao.delete(copyrolerightDao.get(lcrr.get(i).getId()));
				}
				
			}
			/**
			 * 反向【上面的】for循环，添加，修改，copyroleright表
			 */
			boolean ishave=false;
			for (int i = 0; i < ids.size(); i++) { //外层循环，前台的权限id
				ishave=false;
			loop:for (int j = 0; j < lcrr.size(); j++) {//内层循环，数据表中权限的id(修改/不变)
					if(ids.get(i).equals(lcrr.get(j).getRightid()) ){ //数据表中有前台的权限
						ishave=true;  
						for (int j2 = 0; j2 < rolerights.size(); j2++) {  //循环子权限
	 					 String	rolerights_right=rolerights.get(j2).substring(0,rolerights.get(j2).indexOf(",")); //截取子权限的权限id
	 					 if(rolerights_right.trim().equals(lcrr.get(j).getRightid().toString())){
	 						 CopyRoleRight crr=copyrolerightDao.get(lcrr.get(j).getId());//持久化，修改
	 						 crr.setRoleHaveRight(rolerights.get(j2).substring(rolerights.get(j2).indexOf(",")+1));
	 						 break loop;
	 					 }
					  }
						
					}
				}
			
				//添加
				if(!ishave){
					CopyRoleRight crrt=new CopyRoleRight();
					crrt.setRightid(ids.get(i));
					crrt.setRoleid(roleId);
					int j2=0;
					for ( j2 = 0; j2 < rolerights.size(); j2++) {  //循环子权限
	 					 String	rolerights_right=rolerights.get(j2).substring(0,rolerights.get(j2).indexOf(",")); //截取子权限的权限id
	 					 if(rolerights_right.trim().equals(ids.get(i).toString())){
	 						 
	 						 crrt.setRoleHaveRight(rolerights.get(j2).substring(rolerights.get(j2).indexOf(",")+1));
	 						 break ;
	 					 }
					  }
					if(j2==rolerights.size()){
						crrt.setRoleHaveRight("0");
					}
						crrt.setUpdateTime(Struts2Utils.getStringDate(new Date()));
						copyrolerightDao.save(crrt);
				}

			}

			//}
		
	}
	/**
	 * 页面权限
	 */
	public String roleright(String jsppage){
		String roleright="0";
		/**
		 * 操作权限显示
		 */
		
		//项目管理对接页面获取权限
		if(jsppage.equals("yxycount.jsp")||jsppage.equals("thcount.jsp")||jsppage.equals("precount.jsp")){
			jsppage="yxacount.jsp";
		}
		if(jsppage.equals("msicount.jsp")||jsppage.equals("twcount.jsp")||jsppage.equals("yycount.jsp")||jsppage.equals("yxcount.jsp")){
			jsppage="ytcount.jsp";
		}
		if(jsppage.equals("msicountns.jsp")||jsppage.equals("twcountns.jsp")||jsppage.equals("yycountns.jsp")||jsppage.equals("yxcountns.jsp")){
			jsppage="ytcountns.jsp";
		}
		Right right=rightService.findByJspPage(jsppage);
		User u=userService.get(((User)Struts2Utils.getSession().getAttribute("loginUser")).getId());
		List<Role> lr=this.findByUid(u.getId());
		if(lr!=null&&lr.size()>1){
			List<String> removeandadd = new ArrayList<String>();
			for (int i = 0; i <lr.size(); i++) {  //用户多个角色，遍历操作权限（取并集）

				CopyRoleRight crr=copyrolerightService.roleidAndrightid(lr.get(i).getId(), right.getId());

				if(crr!=null&&crr.getRoleHaveRight()!=null){ //当前角色中有这条权限

				 String []  temp=	crr.getRoleHaveRight().split(",");
				 if(removeandadd.size()>0){
					 for (String string : temp) {
						 if(!removeandadd.contains(string)){ //判断是否重复
							 removeandadd.add(string); //
						 }
					 	}
					
				 }else{
					 for (String string : temp) {
						removeandadd.add(string); //第一次添加
					}
				 }
				}
			}
			
			
			String splitpoint="";
			for (String string : removeandadd) {
				splitpoint+=string+",";
			}
			//logger.warn("username:"+Struts2Utils.getSessionUser().getName()+"jsppage:"+jsppage+"splitpoint:"+splitpoint);
			if(splitpoint.length()>0){
				roleright=splitpoint.substring(0, splitpoint.length()-1);
			}
			
			
		}else if(lr!=null&&lr.size()==1){
			CopyRoleRight crr= copyrolerightService.roleidAndrightid(lr.get(0).getId(), right.getId());
			if(crr!=null){
			roleright=	crr.getRoleHaveRight();
			}
			
		}
		return roleright;
	}

	public List<Role> findByRoleName(String roleName){
		return roleDao.findBy("roleName", roleName);
	}
	
	public Role get(Integer id){
		return roleDao.get(id);
	}
	@Transactional
	public void updaterole(Role entity){
		Role r=roleDao.get(entity.getId());
		r.setRoleName(entity.getRoleName());
		r.setDescription(entity.getDescription());
		r.setUpdateTime(Struts2Utils.getymdDate(new Date()));
		
		//logger.info(Struts2Utils.getSessionUser().getname()+"update role {}",entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> roleuser(int rid){
		List<String> lo=roleDao.queryBySql("SELECT name from sys_user  WHERE id IN (SELECT sys_user_id FROM sys_user_sys_role WHERE roles_id="+rid+")");
		List<Object[]> lor=roleDao.queryBySql("SELECT rightid,roleHaveRight FROM sys_copyroleright WHERE roleid="+rid);//查询角色拥有的模块和对应的权限 id
		
		StringBuilder sb=new StringBuilder();
		for ( String o : lo) {
			sb.append(o+",");
		}
		List<String> list=new ArrayList<String>();
		list.add(sb.toString());
		StringBuilder rights=new StringBuilder();
		StringBuilder moduleNames=new StringBuilder();
		StringBuilder jspPages=new StringBuilder();
		for(int i=0;i<lor.size();i++){
			List<Object[]> lrru=roleDao.queryBySql("SELECT moduleName,rights,jspPage FROM sys_right WHERE `id`="+lor.get(i)[0]);
			for ( Object[] ru : lrru) {
				moduleNames.append(ru[0].toString()+"*");
				StringBuffer ri = new StringBuffer();
				for(int j=0;j<lor.get(i)[1].toString().split(",").length;j++){
					if(ru[1]==null){
						ri.append("");
					}else if(Integer.valueOf(lor.get(i)[1].toString().split(",")[j])<ru[1].toString().split(",").length){
						ri.append(ru[1]==null?"":(ru[1].toString().split(",")[Integer.valueOf(lor.get(i)[1].toString().split(",")[j])]+"、"));
					}
				}
				jspPages.append((ru[2]==null?"":ru[2].toString())+"*");
				rights.append(ri.toString()+"*");
			}
		}
		list.add(moduleNames.toString());
		list.add(rights.toString());
		list.add(jspPages.toString());
		
		return list;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(int rid){
		List<String> lo=roleDao.queryBySql("SELECT name from sys_user  WHERE id IN (SELECT sys_user_id FROM sys_user_sys_role WHERE roles_id="+rid+")");
		if(lo.size()==0){//该角色没有人员，可以删除
			try {
				roleDao.deleteBySql("DELETE  FROM sys_role_sys_right WHERE sys_role_id="+rid);
				roleDao.deleteBySql("DELETE FROM sys_role WHERE id="+rid);
				roleDao.deleteBySql("DELETE FROM sys_copyroleright WHERE roleid="+rid);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		return lo.size();
	}
	/**
	 * 页面权限详情
	 * @param pagejsp
	 */
	public void pageRight(Page<Role> page, String jspPage){
		
		Object o=roleDao.queryBySql("SELECT rights FROM sys_right WHERE jspPage='"+jspPage+"'");
		List<Role> lr=new ArrayList<Role>();

		if(!"[]".equals(o.toString())){//不存在
			
			Map<String, String> m=new HashMap<String, String>();
			String [] s=o.toString().substring(1, o.toString().length()-1).split(",");
			for (int i=0;i<s.length;i++) {
				m.put(String.valueOf(i),s[i]);
			}
			
			/*String sql="SELECT r.id,r.roleName,c.roleHaveRight,GROUP_CONCAT(u.`name`) FROM sys_copyroleright c,sys_role_sys_right rr,sys_role r,sys_user u ,sys_user_sys_role ur" 
				  +" WHERE u.id=ur.sys_user_id AND r.id=ur.roles_id AND ur.roles_id and r.id=c.roleid and c.rightid=rr.rights_id AND c.roleid=rr.sys_role_id AND c.rightid=(SELECT id FROM sys_right WHERE jspPage='"+jspPage+"')"
				  +" GROUP BY r.id";*/
			String sql="SELECT r.id,r.roleName,c.roleHaveRight,GROUP_CONCAT(u.`name`) FROM sys_copyroleright c" 
						+" LEFT JOIN sys_role_sys_right rr ON c.rightid=rr.rights_id and c.roleid=rr.sys_role_id"
						+" LEFT JOIN sys_role r ON r.id=c.roleid" 
						+" LEFT JOIN sys_user_sys_role ur ON  r.id=ur.roles_id"
						+" LEFT JOIN sys_user u ON  u.id=ur.sys_user_id"
						+" WHERE  c.rightid=(SELECT id FROM sys_right WHERE jspPage='"+jspPage+"')"
						+" GROUP BY r.id";
			List<Object [] > lrr=roleDao.queryBySql(sql);
			
			for (Object[] ob : lrr) {
				Role r=new Role();
				String [] ob2=ob[2].toString().split(",");
				StringBuilder sb=new StringBuilder();
				for (String obs : ob2) {
					sb.append(m.get(obs)+",");
				}
				r.setRoleName(ob[1]==null?"":ob[1].toString());
				r.setRoleRights(sb.toString());
				r.setRoleUser(ob[3]==null?"":ob[3].toString());
				lr.add(r);
			}
		}
		page.setResult(lr);
	}
	
	/**
	 * 根据名字和页面判断是否有此页面的权限
	 * @param name
	 * @param pageName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkByPageName(User user,String pageName){
		Right right=rightService.findByJspPage(pageName);

		List<Integer> l1 = roleDao.queryBySql("SELECT rights_id FROM sys_user_sys_role S1 LEFT JOIN sys_role_sys_right S2 ON S1.roles_id = S2.sys_role_id WHERE S1.sys_user_id="+user.getId()+";");
		
		boolean flag = false;
		for (Integer i : l1) {
			if(right.getId().equals(i)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	/**
	 * 根据用户id和页面名称获取当前用户在此页面的角色
	 * @param u
	 * @param pageName
	 * @return
	 */
	public List<String> listRole(User u,String pageName){
		String sql="SELECT r2.roleName FROM sys_right r	LEFT JOIN sys_role_sys_right rr ON r.id=rr.rights_id"+
				   " LEFT JOIN sys_role r2 ON r2.id=rr.sys_role_id LEFT JOIN sys_user_sys_role ur ON ur.roles_id=r2.id"+
				   " WHERE r.jspPage='"+pageName+"' 	AND ur.sys_user_id="+u.getId();
		List<String> listRole = roleDao.queryBySql(sql);
		

	return listRole;	
	}

	public List<Role> findByUid(Integer id){
		String sql = "select sr.id,sr.description,roleName,sr.updateTime from sys_user u join sys_user_sys_role r on (r.sys_user_id = u.id) \n" +
				"join sys_role sr  on (sr.id = r.roles_id) where u.id ="+id;
		return roleDao.entityBySql(sql,Role.class);
	}
}
