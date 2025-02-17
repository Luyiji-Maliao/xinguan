package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;

/**
 * 上机批量添加
 * **/
public class PiliangShangji {
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
	//状态（未下单，已下单，已反馈）
	private String orderState;
	//完成日期
	private String endDate;
	//描述
	private String remark;
	//更新人
	private String updateName;
	//更新时间
	private String updateTime;
	// 项目类型
	private String checkProjectName;
	// 样本类型
	private String sampleTypeName;
	//原始样本编号
	private String originalSampleNo;
	//下单样本编号
	private String orderSampleNo;
	//上机时间
	private String orderDate;
	//测序仪编号
	private String orderSequencerNo;
	//FC_ID
	private String orderfcId;
	//测序策略
	private String orderSequencingStrategy;
	//Lane
	private String orderLane;
	//捕获后文库号
	private String orderCaptureLibraryNo;
	//项目代码
	private String orderProjectNo;
	//文库名称
	private String orderLibraryName;
	//index
	private String orderIndex;
	//下单reads
	private String orderReads;
	//下单DATA
	private String orderData;
	//出库浓度
	private String orderOutConcentration;
	//出库体积
	private String orderOutVolume;
	//出库总量
	private String orderOutAmount;
	//稀释液浓度
	private String orderDiluentConcentration;
	//稀释液体积
	private String orderDiluentvolume;
	//2100
	private String orderBiologicalAnalyzer;
	//qpcr
	private String orderQpcr;
	//下单备注
	private String orderRemark;
	//Density
	private String feedbackDensity;
	//PF%
	private String feedbackpf;
	//Q30
	private String feedbackq30;
	//Yield
	private String feedbackYield;
	//反馈样本编号
	private String feedbackSampleNo;
	//反馈备注
	private String feedbackRemark;
	//QC策略
	private String buildFeedbackStrategy;
	//测序策略(建库方法)
	private String buildBy;
	//BUFFER批号
	private String bufferNumber;
	//REAGENT批号
	private String reagentNumber;
	//校正reads（Mb）
	private String correctingReads;
	
	
	
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
	public String getOrderSampleNo() {
		return orderSampleNo;
	}
	public void setOrderSampleNo(String orderSampleNo) {
		this.orderSampleNo = orderSampleNo;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderSequencerNo() {
		return orderSequencerNo;
	}
	public void setOrderSequencerNo(String orderSequencerNo) {
		this.orderSequencerNo = orderSequencerNo;
	}
	public String getOrderfcId() {
		return orderfcId;
	}
	public void setOrderfcId(String orderfcId) {
		this.orderfcId = orderfcId;
	}
	public String getOrderSequencingStrategy() {
		return orderSequencingStrategy;
	}
	public void setOrderSequencingStrategy(String orderSequencingStrategy) {
		this.orderSequencingStrategy = orderSequencingStrategy;
	}
	public String getOrderLane() {
		return orderLane;
	}
	public void setOrderLane(String orderLane) {
		this.orderLane = orderLane;
	}
	public String getOrderCaptureLibraryNo() {
		return orderCaptureLibraryNo;
	}
	public void setOrderCaptureLibraryNo(String orderCaptureLibraryNo) {
		this.orderCaptureLibraryNo = orderCaptureLibraryNo;
	}
	public String getOrderProjectNo() {
		return orderProjectNo;
	}
	public void setOrderProjectNo(String orderProjectNo) {
		this.orderProjectNo = orderProjectNo;
	}
	public String getOrderLibraryName() {
		return orderLibraryName;
	}
	public void setOrderLibraryName(String orderLibraryName) {
		this.orderLibraryName = orderLibraryName;
	}
	public String getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}
	public String getOrderReads() {
		return orderReads;
	}
	public void setOrderReads(String orderReads) {
		this.orderReads = orderReads;
	}
	public String getOrderData() {
		return orderData;
	}
	public void setOrderData(String orderData) {
		this.orderData = orderData;
	}
	public String getOrderOutConcentration() {
		return orderOutConcentration;
	}
	public void setOrderOutConcentration(String orderOutConcentration) {
		this.orderOutConcentration = orderOutConcentration;
	}
	public String getOrderOutVolume() {
		return orderOutVolume;
	}
	public void setOrderOutVolume(String orderOutVolume) {
		this.orderOutVolume = orderOutVolume;
	}
	public String getOrderOutAmount() {
		return orderOutAmount;
	}
	public void setOrderOutAmount(String orderOutAmount) {
		this.orderOutAmount = orderOutAmount;
	}
	public String getOrderDiluentConcentration() {
		return orderDiluentConcentration;
	}
	public void setOrderDiluentConcentration(String orderDiluentConcentration) {
		this.orderDiluentConcentration = orderDiluentConcentration;
	}
	public String getOrderDiluentvolume() {
		return orderDiluentvolume;
	}
	public void setOrderDiluentvolume(String orderDiluentvolume) {
		this.orderDiluentvolume = orderDiluentvolume;
	}
	public String getOrderBiologicalAnalyzer() {
		return orderBiologicalAnalyzer;
	}
	public void setOrderBiologicalAnalyzer(String orderBiologicalAnalyzer) {
		this.orderBiologicalAnalyzer = orderBiologicalAnalyzer;
	}
	public String getOrderQpcr() {
		return orderQpcr;
	}
	public void setOrderQpcr(String orderQpcr) {
		this.orderQpcr = orderQpcr;
	}
	public String getOrderRemark() {
		return orderRemark;
	}
	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}
	public String getOriginalSampleNo() {
		return originalSampleNo;
	}
	public void setOriginalSampleNo(String originalSampleNo) {
		this.originalSampleNo = originalSampleNo;
	}
	public String getFeedbackDensity() {
		return feedbackDensity;
	}
	public void setFeedbackDensity(String feedbackDensity) {
		this.feedbackDensity = feedbackDensity;
	}
	public String getFeedbackpf() {
		return feedbackpf;
	}
	public void setFeedbackpf(String feedbackpf) {
		this.feedbackpf = feedbackpf;
	}
	public String getFeedbackq30() {
		return feedbackq30;
	}
	public void setFeedbackq30(String feedbackq30) {
		this.feedbackq30 = feedbackq30;
	}
	public String getFeedbackYield() {
		return feedbackYield;
	}
	public void setFeedbackYield(String feedbackYield) {
		this.feedbackYield = feedbackYield;
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
	public String getBufferNumber() {
		return bufferNumber;
	}
	public void setBufferNumber(String bufferNumber) {
		this.bufferNumber = bufferNumber;
	}
	public String getReagentNumber() {
		return reagentNumber;
	}
	public void setReagentNumber(String reagentNumber) {
		this.reagentNumber = reagentNumber;
	}
	public String getCorrectingReads() {
		return correctingReads;
	}
	public void setCorrectingReads(String correctingReads) {
		this.correctingReads = correctingReads;
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
