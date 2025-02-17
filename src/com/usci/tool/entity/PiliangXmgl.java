package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;

/**
 * 项目管理 批量添加json数组转实体用
 * **/
public class PiliangXmgl {
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
	//信息分析日期
	private String analysisresultDate;
	//信息分析负责人
	private String analysisresulter;
	//最晚报告日期
	private String reportissueDate;
	//报告负责人
	private String reportissueer;
	//更新时间
	private String updateName;
	//更新人
	private String updateTime;
	
	//样本编号
	private String sampleNo;
	//入库日期
	private String osReceiveDateTime;
	//备注
	private String remark;
	//区分优讯还是BGI
	private String distinguish;
	//BGI样本编号
	private String bgiSampleNo;
	
	
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
	public String getDistinguish() {
		return distinguish;
	}
	public void setDistinguish(String distinguish) {
		this.distinguish = distinguish;
	}
	public String getBgiSampleNo() {
		return bgiSampleNo;
	}
	public void setBgiSampleNo(String bgiSampleNo) {
		this.bgiSampleNo = bgiSampleNo;
	}
	
}
