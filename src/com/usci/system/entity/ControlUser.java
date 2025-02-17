package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_controluser")
public class ControlUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cid;
	
	private String username;
	
	private String loginInfo;

	private Integer loginstate;
	
	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(String loginInfo) {
		this.loginInfo = loginInfo;
	}

	public Integer getLoginstate() {
		return loginstate;
	}

	public void setLoginstate(Integer loginstate) {
		this.loginstate = loginstate;
	}
	
}
