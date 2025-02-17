package com.usci.tool.entity;
import javax.persistence.*;


/**
 * 肿瘤杂交批量添加
 * **/
public class PiliangTumourZajiao {
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
	//任务单状态
	private String workOrderState;
	//审核状态
	private String reviewState;
	//更新人
	private String updateName;
	//更新时间
	private String updateTime;
	//描述
	private String remark;
	//完成日期
	private String endDate;
	
	
	//原始样本编号
	private String originalSampleNo;
	//下单样本编号
	private String orderSampleNo;
	//下单备注
	private String orderRemark;
	// 检测项目
	private String checkProjectName;
	// 样本类型
	private String sampleTypeName;
	//杂交捕获后文库编号
	private String feedbackSampleNo;
	//post-PCR浓度(ng/ul)
	private String postpcRcycleConcentration;
	//纯化倍数
	private String purificationFactortwo;
	//纯化后浓度(ng/ul)
	private String purificationConcentration;
	//纯化后体积
	private String purificationVolume;
	//出库总量（ng）
	private String feedbackTotalAmount;
	//稀释液浓度（ng/ul）
	private String diluentConcentration;
	//稀释液体积（ul）
	private String diluentVolume;
	//项目代码
	private String orderProjectNo;
	//审核人
	private String reviewPerson;
	
	
	
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
	public String getWorkOrderState() {
		return workOrderState;
	}
	public void setWorkOrderState(String workOrderState) {
		this.workOrderState = workOrderState;
	}
	public String getReviewState() {
		return reviewState;
	}
	public void setReviewState(String reviewState) {
		this.reviewState = reviewState;
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
	public String getFeedbackSampleNo() {
		return feedbackSampleNo;
	}
	public void setFeedbackSampleNo(String feedbackSampleNo) {
		this.feedbackSampleNo = feedbackSampleNo;
	}
	public String getPostpcRcycleConcentration() {
		return postpcRcycleConcentration;
	}
	public void setPostpcRcycleConcentration(String postpcRcycleConcentration) {
		this.postpcRcycleConcentration = postpcRcycleConcentration;
	}
	public String getPurificationFactortwo() {
		return purificationFactortwo;
	}
	public void setPurificationFactortwo(String purificationFactortwo) {
		this.purificationFactortwo = purificationFactortwo;
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
	public String getFeedbackTotalAmount() {
		return feedbackTotalAmount;
	}
	public void setFeedbackTotalAmount(String feedbackTotalAmount) {
		this.feedbackTotalAmount = feedbackTotalAmount;
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
	public String getReviewPerson() {
		return reviewPerson;
	}
	public void setReviewPerson(String reviewPerson) {
		this.reviewPerson = reviewPerson;
	}
	
}
