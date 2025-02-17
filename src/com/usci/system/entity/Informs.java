package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_informs")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Informs {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	//消息标题
	@Column(length = 40, nullable = false)
	private String informsTitle;
	//发起人
	@Column(length = 20, nullable = false)
	private String informsSender;
	//接收人
	@Column(length = 20, nullable = false)
	private String informsReceiver;
	//发起时间
	@Column(length = 20, nullable = false)
	private String informsSenderDate;
	//事件链接（待办跳转）
	@Column(length = 45)
	private String informsLink;
	//消息内容
	@Column(length = 5000)
	private String informsContent;
	//事件状态(消息       1显示0隐藏)
	@Column(length = 1, nullable = false)
	private String informsStatus;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getInformsTitle() {
		return informsTitle;
	}
	public void setInformsTitle(String informsTitle) {
		this.informsTitle = informsTitle;
	}
	public String getInformsSender() {
		return informsSender;
	}
	public void setInformsSender(String informsSender) {
		this.informsSender = informsSender;
	}
	
	public String getInformsSenderDate() {
		return informsSenderDate;
	}
	public void setInformsSenderDate(String informsSenderDate) {
		this.informsSenderDate = informsSenderDate;
	}
	public String getInformsLink() {
		return informsLink;
	}
	public void setInformsLink(String informsLink) {
		this.informsLink = informsLink;
	}
	public String getInformsContent() {
		return informsContent;
	}
	public void setInformsContent(String informsContent) {
		this.informsContent = informsContent;
	}
	public String getInformsStatus() {
		return informsStatus;
	}
	public void setInformsStatus(String informsStatus) {
		this.informsStatus = informsStatus;
	}
	public String getInformsReceiver() {
		return informsReceiver;
	}
	public void setInformsReceiver(String informsReceiver) {
		this.informsReceiver = informsReceiver;
	}
	
	
	
}