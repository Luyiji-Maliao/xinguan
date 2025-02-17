package com.usci.tool.entity;
import javax.persistence.*;


/**
 * 肿瘤建库批量添加
 * **/
public class PiliangTumourJian {
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
	//核酸下单编号
	private String orderSampleNo;
	//index编号
	private String orderIndexNo;
	//探针
	private String orderProbe;
	//范围
	private String orderScope;
	//预计出库日期
	private String estimatedOutTime;
	//下单备注
	private String orderRemark;
	// 检测项目
	private String checkProjectName;
	// 样本类型
	private String sampleTypeName;
	//文库编号
	private String feedbackSampleNo;
	//PCRcycle数
	private String pcRcycleNum;
	//PCR后浓度（ng/μl）
	private String pcRcycleConcentration;
	//纯化倍数
	private String purificationFactorone;
	//中间出库浓度(ng/ul)
	private String intermediateConcentration;
	//体积（μl）
	private String feedbackVolume;
	//混合杂交量（ng）
	private String hybridCross;
	//取样体积ul
	private String sampleVolume;
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
	public String getWorkOrderState() {
		return workOrderState;
	}
	public void setWorkOrderState(String workOrderState) {
		this.workOrderState = workOrderState;
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
	public String getPcRcycleNum() {
		return pcRcycleNum;
	}
	public void setPcRcycleNum(String pcRcycleNum) {
		this.pcRcycleNum = pcRcycleNum;
	}
	public String getPcRcycleConcentration() {
		return pcRcycleConcentration;
	}
	public void setPcRcycleConcentration(String pcRcycleConcentration) {
		this.pcRcycleConcentration = pcRcycleConcentration;
	}
	public String getPurificationFactorone() {
		return purificationFactorone;
	}
	public void setPurificationFactorone(String purificationFactorone) {
		this.purificationFactorone = purificationFactorone;
	}
	public String getIntermediateConcentration() {
		return intermediateConcentration;
	}
	public void setIntermediateConcentration(String intermediateConcentration) {
		this.intermediateConcentration = intermediateConcentration;
	}
	public String getFeedbackVolume() {
		return feedbackVolume;
	}
	public void setFeedbackVolume(String feedbackVolume) {
		this.feedbackVolume = feedbackVolume;
	}
	public String getHybridCross() {
		return hybridCross;
	}
	public void setHybridCross(String hybridCross) {
		this.hybridCross = hybridCross;
	}
	public String getSampleVolume() {
		return sampleVolume;
	}
	public void setSampleVolume(String sampleVolume) {
		this.sampleVolume = sampleVolume;
	}
	public String getOrderProjectNo() {
		return orderProjectNo;
	}
	public void setOrderProjectNo(String orderProjectNo) {
		this.orderProjectNo = orderProjectNo;
	}
	
}
