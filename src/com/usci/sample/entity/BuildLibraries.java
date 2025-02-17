package com.usci.sample.entity;
import javax.persistence.*;
import javax.persistence.GeneratedValue;

import javax.persistence.*;

/**
 * 样本中心/建库/ 下单公共信息表 bl_
 * @author 聂梦肖
 *2015-12-22
 */
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "bl_buildlibraries")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class BuildLibraries {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int buildLibrariesId;
	// 任务单（核酸提取）编号
	@Column(length = 50,  nullable = false)
	private String workOrderNo;
	//建库方法
	@Column(length = 50,  nullable = false)
	private String buildLibrariesMethod;
	//下单人（发送者）
	@Column(length = 50,  nullable = false)
	private String underSingle;
	//下单时间（发送者）
	@Column(length = 30,  nullable = false)
	private String underSingleDate;
	//执行人（接收者）
	@Column(length = 50,  nullable = false)
	private String executor;
	//接收时间（接收者）
	@Column(length = 50,  nullable = false)
	private String executorDate;
	
	//状态（未下单，已下单，已反馈）
	@Column(length = 50,  nullable = false)
	private String buildLibrariesState;
	//更新人
	@Column(length = 30,nullable = false)
	private String updateName;
	//更新时间
	@Column(length = 30,nullable = false)
	private String updateTime;
	//备注
	@Column(length = 300,nullable = false)
	private String remark;
	//完成日期
	@Column(length = 30)
	private String endDate;
	//板号
	@Transient
	private String boardNo;

	public String getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(String boardNo) {
		this.boardNo = boardNo;
	}

	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getBuildLibrariesId() {
		return buildLibrariesId;
	}
	public void setBuildLibrariesId(int buildLibrariesId) {
		this.buildLibrariesId = buildLibrariesId;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getBuildLibrariesMethod() {
		return buildLibrariesMethod;
	}
	public void setBuildLibrariesMethod(String buildLibrariesMethod) {
		this.buildLibrariesMethod = buildLibrariesMethod;
	}
	public String getUnderSingle() {
		return underSingle;
	}
	public void setUnderSingle(String underSingle) {
		this.underSingle = underSingle;
	}
	public String getUnderSingleDate() {
		return underSingleDate;
	}
	public void setUnderSingleDate(String underSingleDate) {
		this.underSingleDate = underSingleDate;
	}
	public String getExecutor() {
		return executor;
	}
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	public String getExecutorDate() {
		return executorDate;
	}
	public void setExecutorDate(String executorDate) {
		this.executorDate = executorDate;
	}
	public String getBuildLibrariesState() {
		return buildLibrariesState;
	}
	public void setBuildLibrariesState(String buildLibrariesState) {
		this.buildLibrariesState = buildLibrariesState;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
