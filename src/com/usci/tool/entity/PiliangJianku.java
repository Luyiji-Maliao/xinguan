package com.usci.tool.entity;
import javax.persistence.*;

/**
 * 建库批量添加
 * **/
public class PiliangJianku {
	//任务单号
	private String workOrderNo;
	//建库方法
	private String buildLibrariesMethod;
	//下单人（发送者）
	private String underSingle;
	//下单时间（发送者）
	private String underSingleDate;
	//执行人（接收者）
	private String executor;
	//接收时间（接收者）
	private String executorDate;
	//状态（未下单，已下单，已反馈）
	private String buildLibrariesState;
	//备注
	private String remark;
	//完成日期
	private String endDate;
	//更新人
	private String updateName;
	//更新时间
	private String updateTime;

	
	//原始样本编号
	private String originalSampleNo;
	//下单样本编号
	private String orderSampleNo;
	//index编号
	private String orderIndexNo;
	//入库日期
	private String osReceiveDateTime;
	//下单备注
	private String orderRemark;
	//反馈样本编号
	private String feedbackSampleNo;
	//建库使用量
	private String feedbackUsageAmount;
	//总量
	private String feedbackTotalAmount;
	//中间产物浓度
	private String feedbackIntermediate;
	//体积
	private String feedbackVolume;
	//出库浓度
	private String feedbackOutConcentration;
	//出库体积
	private String feedbackOutVolume;
	//文库总量
	private String feedbackAmount;
	//是否有稀释液
	private String feedbackisDiluent;
	//稀释液浓度
	private String feedbackDiluentConcentration;
	//测序深度
	private String feedbackSequenceDepth;
	//数据量
	private String feedbackDataSize;
	//qc策略
	private String feedbackqcStrategy;
	//PCR仪器编号
	private String pcrNumber;
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
	public String getOrderIndexNo() {
		return orderIndexNo;
	}
	public void setOrderIndexNo(String orderIndexNo) {
		this.orderIndexNo = orderIndexNo;
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
	public String getFeedbackUsageAmount() {
		return feedbackUsageAmount;
	}
	public void setFeedbackUsageAmount(String feedbackUsageAmount) {
		this.feedbackUsageAmount = feedbackUsageAmount;
	}
	public String getFeedbackTotalAmount() {
		return feedbackTotalAmount;
	}
	public void setFeedbackTotalAmount(String feedbackTotalAmount) {
		this.feedbackTotalAmount = feedbackTotalAmount;
	}
	public String getFeedbackIntermediate() {
		return feedbackIntermediate;
	}
	public void setFeedbackIntermediate(String feedbackIntermediate) {
		this.feedbackIntermediate = feedbackIntermediate;
	}
	public String getFeedbackVolume() {
		return feedbackVolume;
	}
	public void setFeedbackVolume(String feedbackVolume) {
		this.feedbackVolume = feedbackVolume;
	}
	public String getFeedbackOutConcentration() {
		return feedbackOutConcentration;
	}
	public void setFeedbackOutConcentration(String feedbackOutConcentration) {
		this.feedbackOutConcentration = feedbackOutConcentration;
	}
	public String getFeedbackOutVolume() {
		return feedbackOutVolume;
	}
	public void setFeedbackOutVolume(String feedbackOutVolume) {
		this.feedbackOutVolume = feedbackOutVolume;
	}
	public String getFeedbackAmount() {
		return feedbackAmount;
	}
	public void setFeedbackAmount(String feedbackAmount) {
		this.feedbackAmount = feedbackAmount;
	}
	public String getFeedbackisDiluent() {
		return feedbackisDiluent;
	}
	public void setFeedbackisDiluent(String feedbackisDiluent) {
		this.feedbackisDiluent = feedbackisDiluent;
	}
	public String getFeedbackDiluentConcentration() {
		return feedbackDiluentConcentration;
	}
	public void setFeedbackDiluentConcentration(String feedbackDiluentConcentration) {
		this.feedbackDiluentConcentration = feedbackDiluentConcentration;
	}
	public String getFeedbackSequenceDepth() {
		return feedbackSequenceDepth;
	}
	public void setFeedbackSequenceDepth(String feedbackSequenceDepth) {
		this.feedbackSequenceDepth = feedbackSequenceDepth;
	}
	public String getFeedbackDataSize() {
		return feedbackDataSize;
	}
	public void setFeedbackDataSize(String feedbackDataSize) {
		this.feedbackDataSize = feedbackDataSize;
	}
	public String getFeedbackqcStrategy() {
		return feedbackqcStrategy;
	}
	public void setFeedbackqcStrategy(String feedbackqcStrategy) {
		this.feedbackqcStrategy = feedbackqcStrategy;
	}
	public String getFeedbackRemark() {
		return feedbackRemark;
	}
	public void setFeedbackRemark(String feedbackRemark) {
		this.feedbackRemark = feedbackRemark;
	}
	public String getPcrNumber() {
		return pcrNumber;
	}
	public void setPcrNumber(String pcrNumber) {
		this.pcrNumber = pcrNumber;
	}
	public String getReagentNumber() {
		return reagentNumber;
	}
	public void setReagentNumber(String reagentNumber) {
		this.reagentNumber = reagentNumber;
	}
	public String getOsReceiveDateTime() {
		return osReceiveDateTime;
	}
	public void setOsReceiveDateTime(String osReceiveDateTime) {
		this.osReceiveDateTime = osReceiveDateTime;
	}
	
	
}
