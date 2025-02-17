package com.lims.core.utils.web;


import java.io.UnsupportedEncodingException;

import org.apache.struts2.convention.annotation.AllowedMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * Struts2中典型CRUD Action的抽象基类
 * 
 * 主要定义了对Preparable,ModelDriven接口的使�?以及CRUD函数和返回�?的命�?
 *
 * @param <T> CRUDAction类代理的对象类型.
 * 
 * @author 聂梦肖
 */
/*@AllowedMethods*/
@AllowedMethods("regex:.*")
public abstract class CrudActionSupport<T> extends ActionSupport implements ModelDriven<T>, Preparable {

	private static final long serialVersionUID = 962964563005873912L;

	/** 进行增删改操作后,以redirect方式重新打开action默认页的result�?*/
	public static final String RELOAD = "reload";

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected Msg msg = new Msg();
	protected int start;
	protected int limit;
	//跳转返回当前页
	protected int returnStart;
	protected String returnEntity;
	
	protected String backstate;
	//添加/修改
	protected String saveorupdate;
	//Ext数组转对象
	protected String iteminfo;
	
	

	

	public String getIteminfo() {
		return iteminfo;
	}

	public void setIteminfo(String iteminfo) {
		this.iteminfo = iteminfo;
	}

	public String getSaveorupdate() {
		return saveorupdate;
	}

	public void setSaveorupdate(String saveorupdate) {
		this.saveorupdate = saveorupdate;
	}

	public String getBackstate() {
		return backstate;
	}

	public void setBackstate(String backstate) {
		this.backstate = backstate;
	}



	public int getReturnStart() {
		return returnStart;
	}

	public void setReturnStart(int returnStart) {
		this.returnStart = returnStart;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * Action函数, 默认的action函数, 默认调用list()函数.
	 */
	@Override
	public String execute() throws Exception {
		return list();
	}

	//-- CRUD Action函数 --//
	/**
	 * Action函数,显示Entity列表界面.
	 * 建议return SUCCESS.
	 */
	public abstract String list() throws Exception;

	
	
	/**
	 *模块的主界面（list） 
	 * @return
	 * @throws Exception
	 */
	public abstract String modulepage() throws Exception;
	/**
	 * Action函数,新增或修改Entity. 
	 * 建议return RELOAD.
	 */
	public abstract String save() throws Exception;

	/**
	 * Action函数,删除Entity.
	 * 建议return RELOAD.
	 */
	//public abstract String delete() throws Exception;

	//-- Preparable函数 --//
	/**
	 * prepare()函数
	 */
	public void prepare() throws Exception {
		prepareModel();
	}


	/**
	 * 等同于prepare()的内部函数提供prepardMethodName()函数调用. 
	 */
	protected abstract void prepareModel() throws Exception;
	
	/**
	 * 跳转web-inf中的页面
	 */
	protected String dispatcher(String file)
	   {
	     String location = "/";
	     if ((file != null) && (!file.equalsIgnoreCase(""))) {
	       location = file;
	     }
	     putObjToContext("location", location);
	     return "jspDiapatcher";
	   }
	
	protected void putObjToContext(String key, Object value)
	   {
	     ActionContext.getContext().put(key, value);
	   }
	/**
	 * 
	 * @param rolename角色名称
	 * @return 返回操作权限
	 */
	protected  String role_right(String rolename){
		return null;
	}

	public String getReturnEntity() {
		try {
			if(returnEntity!=null){
				returnEntity   = java.net.URLDecoder.decode(returnEntity, "UTF-8");
			}			 
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} 	
		return returnEntity;
	}

	public void setReturnEntity(String returnEntity) {
		try {
			if(returnEntity!=null){
				returnEntity   = java.net.URLDecoder.decode(returnEntity, "UTF-8");
			}			 
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} 	
		this.returnEntity = returnEntity;
	}

	
	
}
