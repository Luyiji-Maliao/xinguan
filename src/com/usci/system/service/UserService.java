package com.usci.system.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.usci.system.dao.*;
import com.usci.system.entity.*;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.google.common.collect.Lists;
import com.lims.core.dto.CheckNode;
import com.lims.core.dto.Node;
import com.lims.core.dto.Node1;
import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.Struts2Utils;
import com.ndktools.javamd5.Mademd5;

@Component
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private CopyRoleRightDao copyRoleRightDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ControlUserDao  controlUserDao;
    @Autowired
	private WxUserDao wxUserDao;
    
    @Transactional
    public void save(User entity) {
    	
    		entity.setLoginStatus(1);
    		userDao.merge(entity);
    }


	@Transactional
	public void savewxuser(WxLoginUser entity) {
		wxUserDao.merge(entity);
	}

    @Transactional
    public void update(User entity,List<Integer> roleIds){
    	userDao.merge(entity);
    	User user=userDao.get(entity.getId());
    	if(entity.getLoginStatus()==1){   //生效
			if(roleIds.size()>0){
				List<Role> roles = roleDao.get(roleIds);
				user.setRoles(roles);
			}
		}else{				//失效
			List<Integer> nl=new ArrayList<Integer>();
			Role role=roleDao.findUnique(Restrictions.eq("roleName", "失效用户"));
			if(role!=null){
				nl.add(role.getId());
				List<Role> roles = roleDao.get(nl);
				user.setRoles(roles);
			}
		}

    }

	public List<WxLoginUser> wxuser(String sql){
		List<WxLoginUser> lcs=userDao.entityBySql(sql,WxLoginUser.class);
		return lcs;
	}
		@SuppressWarnings("unchecked")
	public List<Node> initUserRights(Integer userId, Integer parentId) {
		List<Node> nodes = Lists.newArrayList();
		List<Node> userRights = null;
		String hql = "select ri.id as ids,ri.moduleName as text,ri.isLeaf as leaf,ri.moduleUrl as url,ri.jspPage as jspPage from sys_user u join sys_user_sys_role us on (u.id=us.sys_user_id) join sys_role r on (r.id = us.roles_id) join sys_role_sys_right rr on (rr.sys_role_id = r.id) join sys_right ri on (rr.rights_id = ri.id) where u.id="+userId;
		if (parentId == null) {
			hql += " and ri.parent_id is null order by ri.rightSort ASC,ri.id ASC";
			userRights = userDao.entityBySql(hql,Node.class);
		} else {
			hql += " and ri.parent_id = "+parentId+" order by ri.rightSort ASC,ri.id ASC";
			userRights = userDao.entityBySql(hql,Node.class);
		}
		for (Node objects : userRights) {
			//if("管理驾驶舱".equals(objects[1]))
			//	continue;
			objects.setId(""+objects.getIds());
			if(!nodes.contains(objects)){  //去重
				nodes.add(objects);
			}
			
		}
		return nodes;
	}
		
		//权限分配
		@SuppressWarnings("unchecked")
		public List<CheckNode> initUserRoleRights(Integer userId,Integer roleId, Integer parentId) {
			List<Node> rights = null; //查询所有
			String hql = "select ri.id as ids,ri.moduleName as text,ri.isLeaf as leaf,ri.moduleUrl as url,ri.rights as right_web_num from sys_user u join sys_user_sys_role us on (u.id=us.sys_user_id) join sys_role r on (r.id = us.roles_id) join sys_role_sys_right rr on (rr.sys_role_id = r.id) join sys_right ri on (rr.rights_id = ri.id) where u.id="+userId;
			if (parentId == null) {
				hql += " and ri.parent_id is null order by ri.rightSort ASC,ri.id ASC";
				rights = userDao.entityBySql(hql,Node.class);
			} else {
				hql += " and ri.parent_id = "+parentId+" order by ri.rightSort ASC,ri.id ASC";
				rights = userDao.entityBySql(hql,Node.class);
			}
			List<CheckNode> nodes = Lists.newArrayList();
			List<CheckNode> userNodes = roleService.initRoleRights(roleId, parentId); //当前角色所有
			//标记是否已选择
			for (Node objects : rights) {

				CheckNode node = new CheckNode();
				node.setId("" + objects.getIds());
				node.setText(objects.getText());
				node.setLeaf(objects.isLeaf());
				node.setUrl(objects.getUrl());
				node.setRight_web_num(objects.getRight_web_num());
				// node.setIconCls("menuIcon");
				if (userNodes.contains(node)) {
					node.setChecked(true);
				}
				
				
				//
					List<CopyRoleRight> lcrrs=copyRoleRightDao.find(Restrictions.eq("roleid", roleId),Restrictions.eq("rightid", objects.getIds()));
					if(lcrrs.size()>0){
						node.setRoleright(lcrrs.get(0).getRoleHaveRight());
					}else{
						node.setRoleright("0"); //数据库中不存在这条权限的子权限
					}
				
					for (Node object : rights) {
						
						if(!nodes.contains(node)){  //去重
							nodes.add(node);
						}
						
					}

				
			}

			return nodes;
		}
		/**
		 * 用户是否存在
		 * @param username
		 * @return
		 */
		public User checkAccount(String username) {
			return userDao.findUniqueBy("username", username);
		}
		/**
		 * 分页
		 * @param page
		 * @param filters
		 */
		public void findPage(Page<User> page,List<PropertyFilter> filters){
			userDao.findPage(page, filters);
			List<User> lu=page.getResult();
			if(lu.size()>0){
				for (User user : lu) {
					if(user.getDepartmentId()!=null&&!"".equals(user.getDepartmentId().toString())){
						Department d=departmentDao.get(user.getDepartmentId());
						user.setDepartment(d);
					}
					if(user.getPositionId()!=null&&!"".equals(user.getPositionId().toString())){
						Position p=positionDao.get(user.getPositionId());
						user.setPosition(p);
					}
					String sql = "select r.id,r.roleName,r.updateTime,r.description  from sys_role r join sys_user_sys_role us on(r.id=us.roles_id) where sys_user_id="+user.getId();
					List<Role> roles = roleDao.entityBySql(sql,Role.class);
					user.setRoles(roles);
				}
			}
		}
		/**
		 * 检查身份证是否重复
		 * @param IDnumber
		 * @return
		 */
		public User checkIDnumber(String IDnumber){
			return userDao.findUniqueBy("idNumber", IDnumber);
		}

		public User get(Integer id){
			return userDao.get(id);
		}
		/**
		 * 分配角色
		 * @param userId
		 * @param roleIds
		 */
		@Transactional
		public void userRole(Integer userId,List<Integer> roleIds){
			User user = userDao.get(userId);
			try {
				for (Integer roleid: roleIds) {
					String sql = "insert into sys_user_sys_role values ("+userId+","+roleid+")";
					userDao.updateBySql(sql);
				}
			}catch (Exception e){

			}
		}
		/**
		 * 根据账号查询
		 * @param name
		 * @return
		 */
		public List<User> findByUserName(String name){
			return userDao.findBy("username", name);
		}
		/**
		 * 修改个人信息
		 * @param entity
		 * @param un
		 */
		@Transactional
		public void updateUser(User entity){ 
			
			User u=userDao.get(entity.getId());
			u.setEmail(entity.getEmail());
			u.setMobile(entity.getMobile());
			
			if(entity.getPassword().trim().length()!=0){
				Mademd5 md=new Mademd5();
				u.setPassword(md.toMd5(entity.getPassword()));
			}
			
			u.setIdNumber(entity.getIdNumber());
			//从身份证提取生日
			if(entity.getIdNumber() != null&&entity.getIdNumber().length() == 18){
				u.setBirthday(entity.getIdNumber().substring(6,10)+"-"+entity.getIdNumber().substring(10,12)+"-"+entity.getIdNumber().substring(12,14));
			}
			u.setName(entity.getName());
			u.setRemark(entity.getRemark());
			u.setEducation(entity.getEducation());
			u.setAddress(entity.getAddress());
			u.setSex(entity.getSex());
			u.setEmePerson(entity.getEmePerson());
			u.setEmePhone(entity.getEmePhone());
			u.setQq(entity.getQq());
			u.setSchool(entity.getSchool());
			u.setPhone(entity.getPhone());
			u.setUpdateTime(Struts2Utils.getStringDate(new Date()));
		}
		@Transactional
		public void updatePwd(User entity){
			userDao.batchExecute("update User set password=? where username=?", entity.getPassword(),entity.getUsername());
		}
		/**
		 * combobox
		 * @param limit
		 * @return
		 */
		public Page<User> list(int limit){
			
			return userDao.findPage(new Page<User>(limit));
		}
		/**
		 * 
		 * @param name用户姓名 
		 * @return
		 */
		public User findByName(String name){
			return userDao.findUnique(Restrictions.eq("name", name));
		}
		/**
		 * 
		 * @param name 用户姓名
		 * @return
		 */
		public List<User> findListName(String name) {
			return userDao.findBy("name", name);
		}
		/**
		 * 
		 * @param email 邮箱
		 * @return
		 */
		public User checkEmail(String email) {
			// TODO Auto-generated method stub
			return userDao.findUniqueBy("email", email);
		}
		/**
		 * 
		 * @param mobile 手机号
		 * @return
		 */
		public User findByMobile(String mobile) {
			return userDao.findUniqueBy("mobile", mobile);
		}
		/**
		 * 根据部门id查找用户
		 * @param id
		 * @return
		 */
		public List<User> findBydeptid(Integer id){
			return  userDao.findBy("departmentId", id); 
		}
		/**
		 * 部门id和职位id
		 * @param deptId
		 * @param posId
		 * @return
		 */
		public List<User> findByDeptidAndPosid(Integer deptId, Integer posId) {
			// TODO Auto-generated method stub
			return userDao.find(Restrictions.eq("departmentId", deptId),Restrictions.eq("positionId", posId));
		}
		
		
		@SuppressWarnings("unchecked")
		public List<Node1> initUserRights1(Integer userId, Integer parentId) {
			List<Node1> nodes = Lists.newArrayList();
			List<Node> userRights = null;
			String hql = "select ri.id as ids,ri.moduleName as text,ri.isLeaf as leaf,ri.moduleUrl as url,ri.rights as right_web_num from sys_user u join sys_user_sys_role us on (u.id=us.sys_user_id) join sys_role r on (r.id = us.roles_id) join sys_role_sys_right rr on (rr.sys_role_id = r.id) join sys_right ri on (rr.rights_id = ri.id) where u.id="+userId;
			if (parentId == null) {
				hql += " and ri.parent_id is null order by ri.id";
				userRights = userDao.entityBySql(hql,Node.class);
			} else {
				hql += " and rights.parent_id.id="+parentId+" order by ri.id";
				userRights =  userDao.entityBySql(hql,Node.class);
			}
			for (Node objects : userRights) {
				
				Node1 node = new Node1();
				node.setId("" + objects.getIds());
				//node.setIconCls("menuIcon");
				if(!nodes.contains(node)){  //去重
					nodes.add(node);
				}
				
			}
			return nodes;
		}
	
		@Transactional(propagation=Propagation.REQUIRED)
		public void delete(int id){
				try {
					roleDao.deleteBySql("DELETE FROM sys_user_sys_role WHERE sys_user_id="+id);
					roleDao.deleteBySql("delete FROM sys_user WHERE id="+id);
				} catch (HibernateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
		
		public ControlUser findControlUser(String username){
			return controlUserDao.findUnique(Restrictions.eq("username", username));
		}
		/**
		 * 查询邮箱
		 * @param o
		 * @return
		 */
		public List<User> findByName(String [] userName){
	    	List<User> u=userDao.createCriteria(Restrictions.in("name", userName),Restrictions.eq("loginStatus", 1)).list();
	    	return u;
	    }
		
}
