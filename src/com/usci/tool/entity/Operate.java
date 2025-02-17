package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 更新日志
 * @author 刘楠
 *
 */
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "operate")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Operate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 //svn版本号
	 @Column(length = 30 ,nullable = false)
	 private String svnNo;
	 //更新内容
	 @Column(length = 500)
	 private String operation;
	 //页面名称
	 @Column(length = 200)
	 private String jspName;
	 //提交人
	 @Column(length = 30)
	 private String submitter;
	 //提交时间
	 @Column(length = 30)
	 private String submitTime;
	 //录入人
	 @Column(length = 30)
	 private String inputName;
	 //录入时间
	 @Column(length = 30)
	 private String inputTime;
	 //更新人
	 @Column(length = 30)
	 private String updateName;
	 //更新时间
	 @Column(length = 30)
	 private String updateTime;
	 //更新类型
	 @Column(length = 30)
	 private String updateType;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getSvnNo() {
		return svnNo;
	}
	public void setSvnNo(String svnNo) {
		this.svnNo = svnNo;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getSubmitter() {
		return submitter;
	}
	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
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
	public String getJspName() {
		return jspName;
	}
	public void setJspName(String jspName) {
		this.jspName = jspName;
	}
	public String getUpdateType() {
		return updateType;
	}
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	
	 
}
