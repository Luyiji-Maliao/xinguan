package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;

/**
 * 2100批量添加
 * **/
public class PiliangEryi {
	//任务单号
	private String workOrderNo;
	//下单人
	private String underSingle;
	//下单时间
	private String underSingleDate;
	//接收人
	private String executor;
	//接收时间
	private String executorDate;
	//状态
	private String orderState;
	//灵敏度
	private String sensitivity;
	//完成日期
	private String endDate;
	//描述
	private String remark;
	//更新人
	private String updateName;
	//更新时间(反馈时间)
	private String updateTime;
	// 项目类型
	private String checkProjectName;
	// 样本类型
	private String sampleTypeName;
	//原始样本编号
	private String originalSampleNo;
	//下单样本编号
	private String orderSampleNo;
	//下单备注
	private String orderRemark;
	//片段大小
	private String feedbackFragmentSize;
	//储存位置
	private String feedbackStorage;
	//反馈样本编号
	private String feedbackSampleNo;
	//反馈备注
	private String feedbackRemark;
	//qc策略
	private String buildFeedbackStrategy;
	//测序策略(建库方法)
	private String buildBy;
	//混合比例
	private String mixingRatio;
	//2100试剂批号
	private String number2100;
	//纯化后浓度(ng/ul)
	private String purificationConcentration;
	//纯化后体积
	private String purificationVolume;
	//出库总量（ng）
	private String feedbackAmount;
	//稀释液浓度（ng/ul）
	private String diluentConcentration;
	//稀释液体积（ul）
	private String diluentVolume;
	//项目代码
	private String orderProjectNo;
	
	
	
	
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
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
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public String getSensitivity() {
		return sensitivity;
	}
	public void setSensitivity(String sensitivity) {
		this.sensitivity = sensitivity;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getOriginalSampleNo() {
		return originalSampleNo;
	}
	public void setOriginalSampleNo(String originalSampleNo) {
		this.originalSampleNo = originalSampleNo;
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
	public String getFeedbackFragmentSize() {
		return feedbackFragmentSize;
	}
	public void setFeedbackFragmentSize(String feedbackFragmentSize) {
		this.feedbackFragmentSize = feedbackFragmentSize;
	}
	public String getFeedbackStorage() {
		return feedbackStorage;
	}
	public void setFeedbackStorage(String feedbackStorage) {
		this.feedbackStorage = feedbackStorage;
	}
	public String getFeedbackSampleNo() {
		return feedbackSampleNo;
	}
	public void setFeedbackSampleNo(String feedbackSampleNo) {
		this.feedbackSampleNo = feedbackSampleNo;
	}
	public String getFeedbackRemark() {
		return feedbackRemark;
	}
	public void setFeedbackRemark(String feedbackRemark) {
		this.feedbackRemark = feedbackRemark;
	}
	public String getBuildFeedbackStrategy() {
		return buildFeedbackStrategy;
	}
	public void setBuildFeedbackStrategy(String buildFeedbackStrategy) {
		this.buildFeedbackStrategy = buildFeedbackStrategy;
	}
	public String getBuildBy() {
		return buildBy;
	}
	public void setBuildBy(String buildBy) {
		this.buildBy = buildBy;
	}
	public String getMixingRatio() {
		return mixingRatio;
	}
	public void setMixingRatio(String mixingRatio) {
		this.mixingRatio = mixingRatio;
	}
	public String getNumber2100() {
		return number2100;
	}
	public void setNumber2100(String number2100) {
		this.number2100 = number2100;
	}
	public String getPurificationConcentration() {
		return purificationConcentration;
	}
	public void setPurificationConcentration(String purificationConcentration) {
		this.purificationConcentration = purificationConcentration;
	}
	public String getPurificationVolume() {
		return purificationVolume;
	}
	public void setPurificationVolume(String purificationVolume) {
		this.purificationVolume = purificationVolume;
	}
	public String getFeedbackAmount() {
		return feedbackAmount;
	}
	public void setFeedbackAmount(String feedbackAmount) {
		this.feedbackAmount = feedbackAmount;
	}
	public String getDiluentConcentration() {
		return diluentConcentration;
	}
	public void setDiluentConcentration(String diluentConcentration) {
		this.diluentConcentration = diluentConcentration;
	}
	public String getDiluentVolume() {
		return diluentVolume;
	}
	public void setDiluentVolume(String diluentVolume) {
		this.diluentVolume = diluentVolume;
	}
	public String getOrderProjectNo() {
		return orderProjectNo;
	}
	public void setOrderProjectNo(String orderProjectNo) {
		this.orderProjectNo = orderProjectNo;
	}
	public String getCheckProjectName() {
		return checkProjectName;
	}
	public void setCheckProjectName(String checkProjectName) {
		this.checkProjectName = checkProjectName;
	}
	public String getSampleTypeName() {
		return sampleTypeName;
	}
	public void setSampleTypeName(String sampleTypeName) {
		this.sampleTypeName = sampleTypeName;
	}
	
}
