package com.usci.tool.officialaccounts.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;




/**
 * 外部（特殊）微信推送管理
 * @author 聂梦肖
 */
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "tool_wcoa_pushname")
public class OfficialAccountsPushName{
	
	@Id  
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer oapnId;
	/**
	 * 收件人
	 */
	@Column( length=30,nullable = false)
	private String oapnName;
	/**
	 * 手机号
	 */
	@Column( length=50,nullable = false, unique=true)
	private String oapnPhone;
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
	public Integer getOapnId() {
		return oapnId;
	}
	public void setOapnId(Integer oapnId) {
		this.oapnId = oapnId;
	}
	public String getOapnName() {
		return oapnName;
	}
	public void setOapnName(String oapnName) {
		this.oapnName = oapnName;
	}
	
	public String getOapnPhone() {
		return oapnPhone;
	}
	public void setOapnPhone(String oapnPhone) {
		this.oapnPhone = oapnPhone;
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
