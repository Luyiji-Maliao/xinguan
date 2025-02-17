package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.*;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_evercookies")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class EverCookies {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer userId;
	//cookie名称
	@Column(length=20,nullable = false)
	private String cookieName;
	//内容
	@Column(length=200,nullable = false)
	private String cookieContent;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCookieName() {
		return cookieName;
	}
	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}
	public String getCookieContent() {
		return cookieContent;
	}
	public void setCookieContent(String cookieContent) {
		this.cookieContent = cookieContent;
	}
	
	
}