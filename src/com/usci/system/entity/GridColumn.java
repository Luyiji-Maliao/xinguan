package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
@Entity
@Table(name="sys_colume")
public class GridColumn {
	private int id;
	private int userId;
	/**
	 * 当前页面名称
	 */
	@Column(length = 30)
	private String pageName;
	/**
	 * 隐藏/显示的某列
	 */
	@Column(length = 60)
	private String columnId;
	/**
	 * 是否隐藏
	 */
	@Column(length = 10)
	private String columnState;
	@Column(length = 20)
	private String updateTime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	
	public String getColumnState() {
		return columnState;
	}
	public void setColumnState(String columnState) {
		this.columnState = columnState;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
