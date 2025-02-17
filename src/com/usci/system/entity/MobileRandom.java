package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 手机（根据随机序列号）直接登录
 * @author 聂梦肖
 *
 */
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_mobilerandom")
public class MobileRandom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 30, nullable = false)
	private String userName;  //用户名
	@Column(length = 50)
	private String mobileRandom; //序列
	@Column(length = 30)
	private String moduleName; //所属模块(表 列： Leave)
	@Column(length = 20)
	private String moduleState; //所属模块(表 列： Leave)
	@Column(length = 11)
	private Integer moduleId;//表id  （leaveId）
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobileRandom() {
		return mobileRandom;
	}
	public void setMobileRandom(String mobileRandom) {
		this.mobileRandom = mobileRandom;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleState() {
		return moduleState;
	}
	public void setModuleState(String moduleState) {
		this.moduleState = moduleState;
	}
	
	
}
