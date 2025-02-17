package com.usci.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.types.selectors.DepthSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.dto.DepartUserNode;
import com.lims.core.orm.Page;
import com.lims.core.utils.web.JsonUtils;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.dao.DepartmentDao;
import com.usci.system.dao.PositionDao;
import com.usci.system.entity.Department;
import com.usci.system.entity.Position;
import com.usci.system.entity.User;

@Component
@Transactional(readOnly=true)
public class DepartmentService {
    @Autowired
    private DepartmentDao departMentDao;
    @Autowired
    private UserService userService;
    @Autowired
    private PositionDao positionDao; 
    @Transactional
    public void save(Department entity) { 
    	entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
        departMentDao.save(entity);
    } 
    
    public Page<Department> list(int limit){
		return departMentDao.findPage(new Page<Department>(limit));
	}
	
	public Department get(Integer id){
		return departMentDao.get(id);
	}
	StringBuffer json = new StringBuffer();

	@SuppressWarnings("rawtypes")
	public List findDepartment() throws Exception {

		List list = departMentDao.find("from Department");
		return list;
	}

	// 封装成json
	@SuppressWarnings("rawtypes")
	public void constructorJson(List<Department> list, Department treeNode) {
		// list 所有数据，treenode:当前（一级）节点
		if (hasChild(list, treeNode)) {// 有子节点（下一级）
			this.json.append("{\"id\":'");
			this.json.append(treeNode.getId() + "'");
			this.json.append(",\"deptName\":'");
			this.json.append(JsonUtils.formatStr(treeNode.getDeptName()) + "'");
			this.json.append(",\"leaf\":false");
			this.json.append(",\"expanded\":true");
			this.json.append(",\"description\":'");
			this.json.append(JsonUtils.formatStr(treeNode.getDescription() == null ? ""
					: treeNode.getDescription())
					+ "'");
			this.json.append(",\"pid\":'");
			if(treeNode.getParent()!=null){
				this.json.append(treeNode.getParent().getId() + "'");
			}else{
				this.json.append("'");
			}
			
			// 遍历循环字节点
			this.json.append(",\"children\":[");
			List childList = getChildList(list, treeNode);// 获得当前节点的字节点（下一级）
			Iterator iterator = childList.iterator();
			while (iterator.hasNext()) {
				Department node = (Department) iterator.next();
				constructorJson(list, node);
			}
			this.json.append("]},");
		} else {// 叶子
			this.json.append("{\"id\":'");
			this.json.append(treeNode.getId() + "'");
			this.json.append(",\"deptName\":'");
			this.json.append(JsonUtils.formatStr(treeNode.getDeptName()) + "'");
			this.json.append(",\"leaf\":true");

			this.json.append(",\"description\":'");
			this.json.append(JsonUtils.formatStr(treeNode.getDescription() == null ? ""
					: treeNode.getDescription())
					+ "'");
			
			this.json.append(",\"pid\":'");
			if(treeNode.getParent()!=null){
				this.json.append(treeNode.getParent().getId() + "'");
			}else{
				this.json.append("'");
			}
			
			this.json.append("},");
		}
	}

	// 获取当前节点的下一级节点
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Department> getChildList(List<Department> list, Department node) {
		List li = new ArrayList();
		Iterator it = list.iterator();// 所有数据迭代
		while (it.hasNext()) {
			Department n = (Department) it.next();
			// 父节点不为null，并且父节点与传过来的（一级）节点相等
			if ((n.getParent() != null)
					&& (n.getParent().getId().equals(node.getId()))) {
				li.add(n);
			}
		}
		return li;
	}

	public boolean hasChild(List<Department> list, Department node) {
		return getChildList(list, node).size() > 0;
	}

	// 转成json
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getJson(List<Department> list) {
		this.json = new StringBuffer();
		List nodeList0 = new ArrayList();// 保存一级节点

		Iterator it1 = list.iterator();
		while (it1.hasNext()) {
			Department node = (Department) it1.next();
			//根据是否有父节点【node.getParent()】
			if (node.getParent()== null) {// 判断是不是一级节点 node.getLevel() == 0
				
				nodeList0.add(node);// 一级节点
			}
		}

		Iterator it = nodeList0.iterator();// 一级节点迭代
		while (it.hasNext()) {
			Department node = (Department) it.next();
			constructorJson(list, node);
		}

		String jsonDate = this.json.toString();
		return ("[" + jsonDate + "]").replaceAll(",]", "]");
	}
	/*测试 s
	 * */
	// 获取当前节点的下一级节点
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DepartUserNode> getChildLists(List<DepartUserNode> list, DepartUserNode node) {
		List li = new ArrayList();
		Iterator it = list.iterator();// 所有数据迭代
		while (it.hasNext()) {
			DepartUserNode n = (DepartUserNode) it.next();
			// 父节点不为null，并且父节点与传过来的（一级）节点相等
			if ((n.getParentid() != null)
					&& (n.getParentid().equals(node.getId()))) {
				li.add(n);
			}
		}
		return li;
	}
	public boolean hasChilds(List<DepartUserNode> list, DepartUserNode node) {
		return getChildLists(list, node).size() > 0;
	}
	// 封装成jsons
	@SuppressWarnings("rawtypes")
	public void constructorJsons(List<DepartUserNode> list, DepartUserNode treeNode) {
		// list 所有数据，treenode:当前（一级）节点
		if (hasChilds(list, treeNode)) {// 有子节点（下一级）
			this.json.append("{\"id\":'");
			this.json.append(treeNode.getId() + "'");
			this.json.append(",\"deptName\":'");
			this.json.append(JsonUtils.formatStr(treeNode.getDeptName()) + "'");
			this.json.append(",\"leaf\":false");
			this.json.append(",\"expanded\":true");
			this.json.append(",\"icon\":'img/guanli.ico'");
			this.json.append(",\"posName\":''");
			this.json.append(",\"sex\":''");
			this.json.append(",\"idNumber\":''");
			this.json.append(",\"entryDate\":''");
			this.json.append(",\"school\":''");
			this.json.append(",\"userProfession\":''");
			this.json.append(",\"education\":''");
			//this.json.append(",\"nativePlace\":''");
			//this.json.append(",\"address\":''");
			this.json.append(",\"userEmail\":''");
			this.json.append(",\"regularDate\":''");
			this.json.append(",\"contractDate\":''");
			//this.json.append(",\"userMobile\":''");
			this.json.append(",\"description\":'");
			this.json.append(JsonUtils.formatStr(treeNode.getDescription() == null ? ""
					: treeNode.getDescription())
					+ "'");
			this.json.append(",\"pid\":'");
			if(treeNode.getParentid()!=null){
				this.json.append(treeNode.getParentid()+ "'");
			}else{
				this.json.append("'");
			}
			
			// 遍历循环字节点
			this.json.append(",\"children\":[");
			List childList = getChildLists(list, treeNode);// 获得当前节点的字节点（下一级）
			Iterator iterator = childList.iterator();
			while (iterator.hasNext()) {
				DepartUserNode node = (DepartUserNode) iterator.next();
				constructorJsons(list, node);
			}
			this.json.append("]},");
		} else {// 叶子
			this.json.append("{\"id\":'");
			this.json.append(treeNode.getId() + "'");
			this.json.append(",\"deptName\":'");
			this.json.append(JsonUtils.formatStr(treeNode.getDeptName()) + "'");
			this.json.append(",\"leaf\":true");
			if(treeNode.getId()>100){
				this.json.append(",\"posName\":'");
				this.json.append(treeNode.getPosName() + "'");
				this.json.append(",\"sex\":'");
				this.json.append(treeNode.getSex() + "'");
				this.json.append(",\"idNumber\":'");
				this.json.append(treeNode.getIdNumber() + "'");
				this.json.append(",\"entryDate\":'");
				this.json.append(treeNode.getEntryDate() + "'");
				this.json.append(",\"school\":'");
				this.json.append(treeNode.getSchool() + "'");
				this.json.append(",\"userProfession\":'");
				this.json.append(treeNode.getUserProfession() + "'");
				this.json.append(",\"education\":'");
				this.json.append(treeNode.getEducation() + "'");
				//this.json.append(",\"nativePlace\":'");
				//this.json.append(treeNode.getNativePlace() + "'");
				//this.json.append(",\"address\":'");
				//this.json.append(treeNode.getAddress()+ "'");
				this.json.append(",\"userEmail\":'");
				this.json.append(treeNode.getUserEmail() + "'");
				//this.json.append(",\"userMobile\":'");
				//this.json.append(treeNode.getUserMobile() + "'");
				this.json.append(",\"regularDate\":'");
				this.json.append(treeNode.getRegularDate() + "'");
				this.json.append(",\"contractDate\":'");
				this.json.append(treeNode.getContractDate() + "'");
				this.json.append(",\"icon\":'img/yuangong.ico'");
			}else{
				this.json.append(",\"posName\":''");
				this.json.append(",\"sex\":''");
				this.json.append(",\"idNumber\":''");
				this.json.append(",\"entryDate\":''");
				this.json.append(",\"school\":''");
				this.json.append(",\"userProfession\":''");
				this.json.append(",\"education\":''");
				//this.json.append(",\"nativePlace\":''");
				//this.json.append(",\"address\":''");
				this.json.append(",\"userEmail\":''");
				this.json.append(",\"regularDate\":''");
				this.json.append(",\"contractDate\":''");
				//this.json.append(",\"userMobile\":''");
				this.json.append(",\"icon\":'img/guanli.ico'");
			}
			
			this.json.append(",\"description\":'");
			this.json.append(JsonUtils.formatStr(treeNode.getDescription() == null ? ""
					: treeNode.getDescription())
					+ "'");
			
			this.json.append(",\"pid\":'");
			if(treeNode.getParentid()!=null){
				this.json.append(treeNode.getParentid() + "'");
			}else{
				this.json.append("'");
			}
			
			this.json.append("},");
		}
	}
	// 转成jsons
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getJsons(List<DepartUserNode> list) {
		this.json = new StringBuffer();
		List nodeList0 = new ArrayList();// 保存一级节点

		Iterator it1 = list.iterator();
		while (it1.hasNext()) {
			DepartUserNode node = (DepartUserNode) it1.next();
			//根据是否有父节点【node.getParent()】
			if (node.getParentid()== null) {// 判断是不是一级节点 node.getLevel() == 0
				
				nodeList0.add(node);// 一级节点
			}
		}

		Iterator it = nodeList0.iterator();// 一级节点迭代
		while (it.hasNext()) {
			DepartUserNode node = (DepartUserNode) it.next();
			constructorJsons(list, node);
		}

		String jsonDate = this.json.toString();
		return ("[" + jsonDate + "]").replaceAll(",]", "]");
	}
    @Transactional
	public void update(Department entity) {
		Department d=get(entity.getId());
		d.setDescription(entity.getDescription());
		d.setDeptName(entity.getDeptName());
		d.setParent(entity.getParent());
		d.setUpdateTime(Struts2Utils.getStringDate(new Date()));
	}
    
    
    
    
    /**
     * @return
     * @throws Exception
     */
    public List<DepartUserNode> findDeptuser() throws Exception {
    	List<DepartUserNode> ld=new ArrayList<DepartUserNode>();
		List<Department> list = departMentDao.find("from Department");
		for (Department d : list) {
			
			DepartUserNode dun=new DepartUserNode();
			dun.setId(d.getId());
			dun.setDeptName(d.getDeptName());
			dun.setDescription(d.getDescription());
			if(d.getParent()==null){
				dun.setParentid(null);
			}else{
				dun.setParentid(d.getParent().getId());
			}
			
			ld.add(dun);
			List<User> lu=userService.findBydeptid(d.getId());
			for (User user : lu) {
				if(user.getName().equals("王建伟")||user.getName().equals("管理员")){
				
				}else{
				DepartUserNode du=new DepartUserNode();
				du.setId(user.getId()+100);
				du.setDeptName(user.getName());
				
				du.setSex(user.getSex());
				du.setIdNumber(user.getIdNumber());
				du.setEntryDate(user.getEntryDate());
				du.setRegularDate(user.getRegularDate());
				du.setContractDate(user.getContractDate());
				du.setSchool(user.getSchool());
				du.setUserProfession(user.getUserProfession());
				du.setEducation(user.getEducation());
				//du.setNativePlace(user.getNativePlace());
				//du.setAddress(user.getAddress());
				//du.setUserMobile(user.getMobile());
				du.setUserEmail(user.getEmail());
				
				Position p=positionDao.get(user.getPositionId());
				du.setPosName(p.getPosName());
				du.setDescription(user.getRemark());
				du.setParentid(d.getId());
				ld.add(du);
				}
			}
		}
		return ld;
	}

	public List<Department> findBydeptName(String deptNames) {
		// TODO Auto-generated method stub
		return departMentDao.findBy("deptName", deptNames);
	}
}
