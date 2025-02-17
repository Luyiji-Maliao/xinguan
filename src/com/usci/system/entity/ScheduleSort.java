package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_schedulesort")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class ScheduleSort {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	//用户姓名
	@Column(length=30,nullable = false)
	private String userName;
	//待办分类名
	@Column(length = 30, nullable = false)
	private String  title;
	//@Transient
	//private String layout="fit" ;
	@Transient
	private String tools="[{ type : 'gear', handler : function(e, target, panel) {if(panel.initialConfig.title=='其他'){Ext.example.msg('消息','不可修改'); return false;} Ext.getCmp('saveorupdates').setValue('修改');Ext.getCmp('id').setValue(panel.id.substring(0,(panel.id.length-7)));Ext.getCmp('title').setValue(panel.initialConfig.title);schedulesortWin.show(); } }]";
	@Column(length = 1, nullable = false)
	private Integer ssStatus;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public Integer getSsStatus() {
		return ssStatus;
	}
	public void setSsStatus(Integer ssStatus) {
		this.ssStatus = ssStatus;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	/*public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}*/
	public String getTools() {
		return tools;
	}
	public void setTools(String tools) {
		this.tools = tools;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}