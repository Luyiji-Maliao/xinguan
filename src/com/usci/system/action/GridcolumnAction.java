package com.usci.system.action;

import java.util.List;


import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.entity.GridColumn;
import com.usci.system.service.GridColumnService;

/**
 * 
 * grid列的显示与隐藏
 *
 */
@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Result(name = "logins", type = "redirect", location = "session.jsp")
public class GridcolumnAction extends CrudActionSupport<GridColumn> {
	private static final long serialVersionUID = -7907751217823048503L;
	
	@Autowired
	private GridColumnService gridColumnService;
	
	private String userId;
	private String pageName;
	private String msgString;
	
	private String userIdCopy;
	private String pageNameCopy;
	private Integer columnCount;
	
	private GridColumn column;
	private GridColumn temp;
	private List<GridColumn> list;
	private List<GridColumn> returnList;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
		
	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getMsgString() {
		return msgString;
	}

	public void setMsgString(String msgString) {
		this.msgString = msgString;
	}
	
	public String getUserIdCopy() {
		return userIdCopy;
	}

	public void setUserIdCopy(String userIdCopy) {
		this.userIdCopy = userIdCopy;
	}

	public String getPageNameCopy() {
		return pageNameCopy;
	}

	public void setPageNameCopy(String pageNameCopy) {
		this.pageNameCopy = pageNameCopy;
	}

	public Integer getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(Integer columnCount) {
		this.columnCount = columnCount;
	}
	
	public String change() {
		String chs[] = msgString.split(":");
		
		column = new GridColumn();
		column.setUserId(Integer.parseInt(userId));
		column.setPageName(pageName);
		column.setColumnId(chs[0]);
		column.setColumnState(chs[1]);
		if ((list = gridColumnService.findByCondition(column)).size() != 0) {
			temp = gridColumnService.get(list.get(0).getId());
			temp.setColumnState(chs[1]);
			gridColumnService.update(temp);
		} else {
			gridColumnService.save(column);
		}
		return null;
	}
	
	public String find() {
		GridColumn column = new GridColumn();
		column.setUserId(Integer.parseInt(userIdCopy));
		column.setPageName(pageNameCopy);
		returnList =  gridColumnService.findByCondition(column, 0);
		
		Struts2Utils.renderJson(returnList);
		return null;
	}
	
	public String returnList() {
		Struts2Utils.renderJson(returnList);
		
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GridColumn getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modulepage() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
