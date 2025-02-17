package com.usci.email.entity;

import javax.persistence.*;

import javax.persistence.GeneratedValue;
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
        @Table(name = "email_entityfield")
public class EmailEntityField {
	 @Id  
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer entityFieldId;
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
	public Integer getEntityFieldId() {
		return entityFieldId;
	}
	public void setEntityFieldId(Integer entityFieldId) {
		this.entityFieldId = entityFieldId;
	}
	
}
