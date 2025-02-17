package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;

/**
 * 预处理批量添加json数组转实体用
 * **/
public class PiliangYcl {
	//任务单号
	private String workOrderNo;
	//描述
	private String remark;
	//下单人
	private String underSingle;
	//下单日期
	private String underSingleDate;
	//预处理类型
	private String pretreatmentType;
	//执行人
	private String executor;
	//接收时间
	private String executorDate;
	//状态
	private String pretreatmentState;
	//更新人
	private String updateName;
	//更新时间
	private String updateTime;
	//完成日期
	private String endDate;
	
	//样本编号
	private String orderSampleNo;
	//下单备注
	private String orderRemark;
	//(反馈)使用量
	private String feedbackUsageAmount;
	//预处理后数量
	private String pretreatmentamount;
	//反馈备注
	private String feedbackRemark;
	
	
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getPretreatmentType() {
		return pretreatmentType;
	}
	public void setPretreatmentType(String pretreatmentType) {
		this.pretreatmentType = pretreatmentType;
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
	public String getPretreatmentState() {
		return pretreatmentState;
	}
	public void setPretreatmentState(String pretreatmentState) {
		this.pretreatmentState = pretreatmentState;
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getOrderSampleNo() {
		return orderSampleNo;
	}
	public void setOrderSampleNo(String orderSampleNo) {
		this.orderSampleNo = orderSampleNo;
	}
	public String getOrderRemark() {
		return orderRemark;
	}
	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}
	public String getFeedbackUsageAmount() {
		return feedbackUsageAmount;
	}
	public void setFeedbackUsageAmount(String feedbackUsageAmount) {
		this.feedbackUsageAmount = feedbackUsageAmount;
	}
	public String getPretreatmentamount() {
		return pretreatmentamount;
	}
	public void setPretreatmentamount(String pretreatmentamount) {
		this.pretreatmentamount = pretreatmentamount;
	}
	public String getFeedbackRemark() {
		return feedbackRemark;
	}
	public void setFeedbackRemark(String feedbackRemark) {
		this.feedbackRemark = feedbackRemark;
	}
	
	
}
