package com.usci.tool.officialaccounts.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "tool_wcoa_pushmsg")
public class OfficialAccountsPushMsg {
	/** 
	 * 所属方法（类-方法）
	 */
	@Column( length=100,nullable = false)
	private String oapmProperty;
	/**
	 * 其他个性化标识（同一个方法中有多条线可其他标识区分）
	 */
	@Column( length=100,nullable = false)
	private String oapmSign;
	/**
	 * 推送人
	 */
	@Column( length=100,nullable = false)
	private String oapmNames;
	@Column(length=100,nullable = false)
	private String oapmRemake;
	
	@Id  
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer oapmId;
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
	public String getOapmProperty() {
		return oapmProperty;
	}
	public void setOapmProperty(String oapmProperty) {
		this.oapmProperty = oapmProperty;
	}
	public String getOapmSign() {
		return oapmSign;
	}
	public void setOapmSign(String oapmSign) {
		this.oapmSign = oapmSign;
	}
	public String getOapmNames() {
		return oapmNames;
	}
	public void setOapmNames(String oapmNames) {
		this.oapmNames = oapmNames;
	}
	public String getOapmRemake() {
		return oapmRemake;
	}
	public void setOapmRemake(String oapmRemake) {
		this.oapmRemake = oapmRemake;
	}
	public Integer getOapmId() {
		return oapmId;
	}
	public void setOapmId(Integer oapmId) {
		this.oapmId = oapmId;
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
