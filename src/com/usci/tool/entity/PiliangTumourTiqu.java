package com.usci.tool.entity;
import javax.persistence.*;



/**
 * 肿瘤提取批量添加
 * **/
public class PiliangTumourTiqu {
	//任务单号
	private String workOrderNo;
	//下单人
	private String underSingle;
	//下单时间
	private String underSingleDate;
	//接受人
	private String executor;
	//接收时间（接收者）
	private String executorDate;
	//状态
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
	
	//样本编号
	private String orderSampleNo;
	//文库编号(建库反馈样本编号)
	private String buildFeedbackSampleNo;
	//index编号
	private String orderIndexNo;
	//探针
	private String orderProbe;
	//范围
	private String orderScope;
	//项目核对人
	private String checkProjectPerson;
	//样本数量
	private String sampleNumber;
	//预计出库日期
	private String estimatedOutTime;
	//下单备注
	private String orderRemark;
	//检测项目
	private String checkProjectName;
	//样本类型
	private String sampleTypeName;
	//提取反馈样本编号
	private String feedbackSampleNo;
	//样本使用量
	private String sampleUsageAmount;
	//实验剩余次数
	private String feedbackSurplus;
	//剩余组织量
	private String residualTissue;
	//提取后浓度（ng/ul)
	private String extractionConcentration;
	//总量
	private String feedbackAmount;
	//260/280
	private String numResult;
	//打断取样量（ng）
	private String interruptedSampling;
	//打断时间
	private String interruptedTime;
	//取样体积/水
	private String samplingVolume;
	//纯化后浓度（ng/μl）
	private String purifiedConcentration;
	//建库起始量（ng）
	private String buildStartAmount;
	//建库取样量（ng）
	private String builduseAmount;
	//备注   remark
	private String feedbackRemark;
	//执行人
	private String executorName;
	//实验核对人
	private String checkerName;
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
	public String getOrderSampleNo() {
		return orderSampleNo;
	}
	public void setOrderSampleNo(String orderSampleNo) {
		this.orderSampleNo = orderSampleNo;
	}
	public String getBuildFeedbackSampleNo() {
		return buildFeedbackSampleNo;
	}
	public void setBuildFeedbackSampleNo(String buildFeedbackSampleNo) {
		this.buildFeedbackSampleNo = buildFeedbackSampleNo;
	}
	public String getOrderIndexNo() {
		return orderIndexNo;
	}
	public void setOrderIndexNo(String orderIndexNo) {
		this.orderIndexNo = orderIndexNo;
	}
	public String getOrderProbe() {
		return orderProbe;
	}
	public void setOrderProbe(String orderProbe) {
		this.orderProbe = orderProbe;
	}
	public String getOrderScope() {
		return orderScope;
	}
	public void setOrderScope(String orderScope) {
		this.orderScope = orderScope;
	}
	public String getCheckProjectPerson() {
		return checkProjectPerson;
	}
	public void setCheckProjectPerson(String checkProjectPerson) {
		this.checkProjectPerson = checkProjectPerson;
	}
	public String getSampleNumber() {
		return sampleNumber;
	}
	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
	public String getEstimatedOutTime() {
		return estimatedOutTime;
	}
	public void setEstimatedOutTime(String estimatedOutTime) {
		this.estimatedOutTime = estimatedOutTime;
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
	public String getSampleUsageAmount() {
		return sampleUsageAmount;
	}
	public void setSampleUsageAmount(String sampleUsageAmount) {
		this.sampleUsageAmount = sampleUsageAmount;
	}
	public String getFeedbackSurplus() {
		return feedbackSurplus;
	}
	public void setFeedbackSurplus(String feedbackSurplus) {
		this.feedbackSurplus = feedbackSurplus;
	}
	public String getResidualTissue() {
		return residualTissue;
	}
	public void setResidualTissue(String residualTissue) {
		this.residualTissue = residualTissue;
	}
	public String getExtractionConcentration() {
		return extractionConcentration;
	}
	public void setExtractionConcentration(String extractionConcentration) {
		this.extractionConcentration = extractionConcentration;
	}
	public String getFeedbackAmount() {
		return feedbackAmount;
	}
	public void setFeedbackAmount(String feedbackAmount) {
		this.feedbackAmount = feedbackAmount;
	}
	public String getNumResult() {
		return numResult;
	}
	public void setNumResult(String numResult) {
		this.numResult = numResult;
	}
	public String getInterruptedSampling() {
		return interruptedSampling;
	}
	public void setInterruptedSampling(String interruptedSampling) {
		this.interruptedSampling = interruptedSampling;
	}
	public String getInterruptedTime() {
		return interruptedTime;
	}
	public void setInterruptedTime(String interruptedTime) {
		this.interruptedTime = interruptedTime;
	}
	public String getSamplingVolume() {
		return samplingVolume;
	}
	public void setSamplingVolume(String samplingVolume) {
		this.samplingVolume = samplingVolume;
	}
	public String getPurifiedConcentration() {
		return purifiedConcentration;
	}
	public void setPurifiedConcentration(String purifiedConcentration) {
		this.purifiedConcentration = purifiedConcentration;
	}
	public String getBuildStartAmount() {
		return buildStartAmount;
	}
	public void setBuildStartAmount(String buildStartAmount) {
		this.buildStartAmount = buildStartAmount;
	}
	public String getBuilduseAmount() {
		return builduseAmount;
	}
	public void setBuilduseAmount(String builduseAmount) {
		this.builduseAmount = builduseAmount;
	}
	public String getFeedbackRemark() {
		return feedbackRemark;
	}
	public void setFeedbackRemark(String feedbackRemark) {
		this.feedbackRemark = feedbackRemark;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
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
	public String getExecutorName() {
		return executorName;
	}
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
	
}
