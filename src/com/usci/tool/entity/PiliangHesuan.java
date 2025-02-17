package com.usci.tool.entity;
import javax.persistence.*;

/**
 * 优馨安核酸批量添加json数组转实体用
 * **/
public class PiliangHesuan {
	//任务单号
	private String workOrderNo;
	//核酸提取方法
	private String dnaExtractionMethod;
	//下单人
	private String underSingle;
	//下单时间
	private String underSingleDate;
	//接收人
	private String executor;
	//接收时间
	private String executorDate;
	//任务单状态
	private String nucleicacidsState;
	//完成日期
	private String endDate;
	//描述
	private String remark;
	//更新人
	private String updateName;
	//更新时间
	private String updateTime;
	
	

/**---------------------反馈数据-----------------------**/
	//核酸下单样本编号
	private String orderSampleNo;
	//样本量
	private String sampleCount;
	//规定使用量
	private String provisionsCount;
	//下单备注
	private String orderRemark;
	//原始样本编号
	private String originalSampleNo;
	//核酸反馈样本编号
	private String feedbackSampleNo;
	//总量
	private String feedbackTotalAmount;
	//浓度
	private String feedbackConcentration;
	//体积
	private String feedbackVolume;
	//Qubit检测仪器编号
	private String qubitNumber;
	//试剂批号
	private String reagentNumber;
	//反馈备注
	private String feedbackRemark;

	
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getDnaExtractionMethod() {
		return dnaExtractionMethod;
	}
	public void setDnaExtractionMethod(String dnaExtractionMethod) {
		this.dnaExtractionMethod = dnaExtractionMethod;
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
	public String getNucleicacidsState() {
		return nucleicacidsState;
	}
	public void setNucleicacidsState(String nucleicacidsState) {
		this.nucleicacidsState = nucleicacidsState;
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
	public String getSampleCount() {
		return sampleCount;
	}
	public void setSampleCount(String sampleCount) {
		this.sampleCount = sampleCount;
	}
	public String getProvisionsCount() {
		return provisionsCount;
	}
	public void setProvisionsCount(String provisionsCount) {
		this.provisionsCount = provisionsCount;
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
	public String getFeedbackSampleNo() {
		return feedbackSampleNo;
	}
	public void setFeedbackSampleNo(String feedbackSampleNo) {
		this.feedbackSampleNo = feedbackSampleNo;
	}
	public String getFeedbackTotalAmount() {
		return feedbackTotalAmount;
	}
	public void setFeedbackTotalAmount(String feedbackTotalAmount) {
		this.feedbackTotalAmount = feedbackTotalAmount;
	}
	public String getFeedbackConcentration() {
		return feedbackConcentration;
	}
	public void setFeedbackConcentration(String feedbackConcentration) {
		this.feedbackConcentration = feedbackConcentration;
	}
	public String getFeedbackVolume() {
		return feedbackVolume;
	}
	public void setFeedbackVolume(String feedbackVolume) {
		this.feedbackVolume = feedbackVolume;
	}
	public String getFeedbackRemark() {
		return feedbackRemark;
	}
	public void setFeedbackRemark(String feedbackRemark) {
		this.feedbackRemark = feedbackRemark;
	}
	public String getQubitNumber() {
		return qubitNumber;
	}
	public void setQubitNumber(String qubitNumber) {
		this.qubitNumber = qubitNumber;
	}
	public String getReagentNumber() {
		return reagentNumber;
	}
	public void setReagentNumber(String reagentNumber) {
		this.reagentNumber = reagentNumber;
	}
}
