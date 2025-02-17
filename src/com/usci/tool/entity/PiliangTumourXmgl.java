package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;

/**
 * 肿瘤项目管理批量添加
 * **/
public class PiliangTumourXmgl {
	//任务单号
	private String pmorderNo;
	//预出库日期
	private String stockoutDate;
	//出库负责人
	private String stockouter;
	//预上机日期
	private String biologicalcomputerDate;
	//上机负责人
	private String biologicalcomputer;
	//分析上传日期
	private String analysisresultDate;
	//信息分析负责人
	private String analysisresulter;
	//报告邮寄最晚日期
	private String reportissueDate;
	//客服负责人
	private String reportissueer;
	//审核状态
	private String reviewState;
	//更新时间
	private String updateTime;
	//更新人
	private String updateName;
	
	//样本编号
	private String sampleNo;
	//入库日期
	private String osReceiveDateTime;
	//备注
	private String remark;
	//待办id ，修改待办状态
	//检测项目
	private String checkProjectName;
	//样本类型
	private String sampleTypeName;
	
	public String getPmorderNo() {
		return pmorderNo;
	}
	public void setPmorderNo(String pmorderNo) {
		this.pmorderNo = pmorderNo;
	}
	public String getStockoutDate() {
		return stockoutDate;
	}
	public void setStockoutDate(String stockoutDate) {
		this.stockoutDate = stockoutDate;
	}
	public String getStockouter() {
		return stockouter;
	}
	public void setStockouter(String stockouter) {
		this.stockouter = stockouter;
	}
	public String getBiologicalcomputerDate() {
		return biologicalcomputerDate;
	}
	public void setBiologicalcomputerDate(String biologicalcomputerDate) {
		this.biologicalcomputerDate = biologicalcomputerDate;
	}
	public String getBiologicalcomputer() {
		return biologicalcomputer;
	}
	public void setBiologicalcomputer(String biologicalcomputer) {
		this.biologicalcomputer = biologicalcomputer;
	}
	public String getAnalysisresultDate() {
		return analysisresultDate;
	}
	public void setAnalysisresultDate(String analysisresultDate) {
		this.analysisresultDate = analysisresultDate;
	}
	public String getAnalysisresulter() {
		return analysisresulter;
	}
	public void setAnalysisresulter(String analysisresulter) {
		this.analysisresulter = analysisresulter;
	}
	public String getReportissueDate() {
		return reportissueDate;
	}
	public void setReportissueDate(String reportissueDate) {
		this.reportissueDate = reportissueDate;
	}
	public String getReportissueer() {
		return reportissueer;
	}
	public void setReportissueer(String reportissueer) {
		this.reportissueer = reportissueer;
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
	public String getSampleNo() {
		return sampleNo;
	}
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getOsReceiveDateTime() {
		return osReceiveDateTime;
	}
	public void setOsReceiveDateTime(String osReceiveDateTime) {
		this.osReceiveDateTime = osReceiveDateTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
