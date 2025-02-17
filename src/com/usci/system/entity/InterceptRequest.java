package com.usci.system.entity;
import javax.persistence.*;


import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_interceptrequest")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class InterceptRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer interceptRequestID;
	//账号
	private String accessName;
	//IP
	private String accessIP;
	//访问URL
	private String accessURL;
	//参数
	private String accessParameter;
	//访问时间
	private Timestamp  accessDate;
	//推送状态               
	private String pushState;
	//访问标记（0,1）（记录1秒内访问十次 的IP，每隔一个小时推送一次此ip信息）
	private int accessSign;
	//访问时差(拦截此访问所需时间，未记录save方法时间)
	private long accessTimeDifference;
	public Integer getInterceptRequestID() {
		return interceptRequestID;
	}
	public void setInterceptRequestID(Integer interceptRequestID) {
		this.interceptRequestID = interceptRequestID;
	}
	
	public String getAccessName() {
		return accessName;
	}
	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}
	public String getAccessIP() {
		return accessIP;
	}
	public void setAccessIP(String accessIP) {
		this.accessIP = accessIP;
	}
	public String getAccessURL() {
		return accessURL;
	}
	public void setAccessURL(String accessURL) {
		this.accessURL = accessURL;
	}
	public String getAccessParameter() {
		return accessParameter;
	}
	public void setAccessParameter(String accessParameter) {
		this.accessParameter = accessParameter;
	}
	public Timestamp getAccessDate() {
		return accessDate;
	}
	public void setAccessDate(Timestamp accessDate) {
		this.accessDate = accessDate;
	}
	
	public String getPushState() {
		return pushState;
	}
	public void setPushState(String pushState) {
		this.pushState = pushState;
	}
	public int getAccessSign() {
		return accessSign;
	}
	public void setAccessSign(int i) {
		this.accessSign = i;
	}
	public long getAccessTimeDifference() {
		return accessTimeDifference;
	}
	public void setAccessTimeDifference(long l) {
		this.accessTimeDifference = l;
	}
	
}
