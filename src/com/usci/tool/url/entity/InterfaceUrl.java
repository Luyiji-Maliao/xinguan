package com.usci.tool.url.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "tool_url_interfaceurl")
public class InterfaceUrl {
	
	/** 
	 * 所属方法（类-方法）
	 */
	@Column( length=100,nullable = false)
	private String iuProperty;
	/**
	 * 其他个性化标识（同一个方法中有多条线可其他标识区分）
	 */
	@Column( length=100,nullable = false)
	private String iuSign;
	/**
	 * URL
	 */
	@Column( length=100,nullable = false)
	private String interfaceUrl;
	@Column(length=100,nullable = false)
	private String iuRemake;
	 @Id  
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer iuid;
	/**
	 * -1：全部，0:显示，1：不显示
	 */
	@Column( length=2,nullable = false)
	private int dataState;
	@Column( length=20,nullable = false)
	private String inputName;
	@Column( length=20,nullable = false)
	private String inputTime;
	@Column( length=20,nullable = false)
	private String updateName;
	@Column( length=20,nullable = false)
	private String updateTime;
	public String getIuProperty() {
		return iuProperty;
	}
	public void setIuProperty(String iuProperty) {
		this.iuProperty = iuProperty;
	}
	public String getIuSign() {
		return iuSign;
	}
	public void setIuSign(String iuSign) {
		this.iuSign = iuSign;
	}
	public String getInterfaceUrl() {
		return interfaceUrl;
	}
	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}
	public String getIuRemake() {
		return iuRemake;
	}
	public void setIuRemake(String iuRemake) {
		this.iuRemake = iuRemake;
	}
	public Integer getIuid() {
		return iuid;
	}
	public void setIuid(Integer iuid) {
		this.iuid = iuid;
	}
	public int getDataState() {
		return dataState;
	}
	public void setDataState(int dataState) {
		this.dataState = dataState;
	}
	public String getInputName() {
		return inputName;
	}
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}
	public String getInputTime() {
		return inputTime;
	}
	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
