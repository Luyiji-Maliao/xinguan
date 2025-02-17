package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_schedule")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Schedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	//ScheduleSort外键
	private Integer scheduleSortId;
	//事件id
	private Integer scheduleId;
	//事件标题
	@Column(length = 40, nullable = false)
	private String scheduleTitle;
	//发起人
	@Column(length = 20, nullable = false)
	private String scheduleSender;
	//接收人
	@Column(length = 20, nullable = false)
	private String scheduleReceiver;
	//发起时间
	@Column(length = 20, nullable = false)
	private String scheduleSenderDate;
	//时间节点
	@Column(length = 20, nullable = false)
	private String scheduleTime;
	//完成时间
	@Column(length = 20)
	private String scheduleFinishDate;
	//事件链接（待办跳转）
	@Column(length = 45)
	private String scheduleLink;
	//待办内容
	@Column(length = 5000)
	private String scheduleContent;
	//事件状态(待办事项       1显示0隐藏)
	@Column(length = 1, nullable = false)
	private String scheduleStatus;
	//是否接受(    1接收0驳回)
	@Column(length = 1, nullable = false)
	private String receiverStatus;
	//提前提醒
	@Column(length = 7)
	private Integer earlyWarning;
	//优先级别
	private Integer priorityLevel;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getScheduleTitle() {
		return scheduleTitle;
	}
	public void setScheduleTitle(String scheduleTitle) {
		this.scheduleTitle = scheduleTitle;
	}
	public String getScheduleSender() {
		return scheduleSender;
	}
	public void setScheduleSender(String scheduleSender) {
		this.scheduleSender = scheduleSender;
	}
	public String getScheduleReceiver() {
		return scheduleReceiver;
	}
	public void setScheduleReceiver(String scheduleReceiver) {
		this.scheduleReceiver = scheduleReceiver;
	}
	public String getScheduleSenderDate() {
		return scheduleSenderDate;
	}
	public void setScheduleSenderDate(String scheduleSenderDate) {
		this.scheduleSenderDate = scheduleSenderDate;
	}
	public String getScheduleLink() {
		return scheduleLink;
	}
	public void setScheduleLink(String scheduleLink) {
		this.scheduleLink = scheduleLink;
	}
	public String getScheduleStatus() {
		return scheduleStatus;
	}
	public void setScheduleStatus(String scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}
	public Integer getScheduleSortId() {
		return scheduleSortId;
	}
	public void setScheduleSortId(Integer scheduleSortId) {
		this.scheduleSortId = scheduleSortId;
	}
	public String getScheduleContent() {
		return scheduleContent;
	}
	public void setScheduleContent(String scheduleContent) {
		this.scheduleContent = scheduleContent;
	}
	public String getScheduleTime() {
		return scheduleTime;
	}
	
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public String getScheduleFinishDate() {
		return scheduleFinishDate;
	}
	public void setScheduleFinishDate(String scheduleFinishDate) {
		this.scheduleFinishDate = scheduleFinishDate;
	}
	public String getReceiverStatus() {
		return receiverStatus;
	}
	public void setReceiverStatus(String receiverStatus) {
		this.receiverStatus = receiverStatus;
	}
	public Integer getEarlyWarning() {
		return earlyWarning;
	}
	public void setEarlyWarning(Integer earlyWarning) {
		this.earlyWarning = earlyWarning;
	}
	public Integer getPriorityLevel() {
		return priorityLevel;
	}
	public void setPriorityLevel(Integer priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	

	
}