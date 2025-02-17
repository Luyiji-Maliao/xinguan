package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;

/**
 * 优逸(全套,男15,女16)审核,发放批量添加json数组转实体用
 * **/
public class piliangBao {
	//审核
	
	//样本编号
	private String sampleNo;
	//客户姓名
	private String tumourName;
	//pdf路径
	private String pdfPath;
	//送检单
	private String img;
	//审核状态 			 ****reportState
	private String reportRewiewState;
	//客服部审核意见
	private String custReportOpinion;
	//客服部审核人
	private String custReportReviewName;
	//客服部审核时间
	private String custReportReviewTime;
	//质量部审核意见
	private String reportOpinion;
	//质量部审核人
	private String reportReviewName;
	//质量部审核时间
	private String reportReviewTime;
	//更新人			 ****updateName
	private String reviewUpdateName;
	//更新时间		 ****updateTime
	private String reviewUpdateTime;
	//所属产品
	private String tumProduct;
	//所属套餐
	private String tumPackage;
	//录入人		 	 ****inputName
	private String reviewInputName;
	//录入时间		 ****inputTime
	private String reviewInputTime;
	
	//发放
	//销售
	private String saleName;
	//区域联系人
	private String cityContacts;
	//送检医院
	private String hospital;
	//客户手机
	private String cusMobile;
	//报告打印状态
	private String reportState;
	//是否需要副本
	private String haveCopies;
	//接收人类型
	private String reportRecipient;
	//报告接收人
	private String reportPerson;
	//快递单号
	private String expressNo;
	//快递公司
	private String expressCompany;
	//报告模板
	private String reportTemplate;
	//打印日期
	private String reportDate;
	//更新人
	private String updateName;
	//更新时间
	private String updateTime;
	//录入人
	private String inputName;
	//录入时间
	private String inputTime;
	//标签打印状态
	private String codeState;
	//标签打印时间
	private String codePrintDate;
	//标签打印人
	private String codePerson;
	
	
	public String getSampleNo() {
		return sampleNo;
	}
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getTumourName() {
		return tumourName;
	}
	public void setTumourName(String tumourName) {
		this.tumourName = tumourName;
	}
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getReportRewiewState() {
		return reportRewiewState;
	}
	public void setReportRewiewState(String reportRewiewState) {
		this.reportRewiewState = reportRewiewState;
	}
	public String getCustReportOpinion() {
		return custReportOpinion;
	}
	public void setCustReportOpinion(String custReportOpinion) {
		this.custReportOpinion = custReportOpinion;
	}
	public String getCustReportReviewName() {
		return custReportReviewName;
	}
	public void setCustReportReviewName(String custReportReviewName) {
		this.custReportReviewName = custReportReviewName;
	}
	public String getCustReportReviewTime() {
		return custReportReviewTime;
	}
	public void setCustReportReviewTime(String custReportReviewTime) {
		this.custReportReviewTime = custReportReviewTime;
	}
	public String getReportOpinion() {
		return reportOpinion;
	}
	public void setReportOpinion(String reportOpinion) {
		this.reportOpinion = reportOpinion;
	}
	public String getReportReviewName() {
		return reportReviewName;
	}
	public void setReportReviewName(String reportReviewName) {
		this.reportReviewName = reportReviewName;
	}
	public String getReportReviewTime() {
		return reportReviewTime;
	}
	public void setReportReviewTime(String reportReviewTime) {
		this.reportReviewTime = reportReviewTime;
	}
	public String getTumProduct() {
		return tumProduct;
	}
	public void setTumProduct(String tumProduct) {
		this.tumProduct = tumProduct;
	}
	public String getTumPackage() {
		return tumPackage;
	}
	public void setTumPackage(String tumPackage) {
		this.tumPackage = tumPackage;
	}
	public String getSaleName() {
		return saleName;
	}
	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}
	public String getCityContacts() {
		return cityContacts;
	}
	public void setCityContacts(String cityContacts) {
		this.cityContacts = cityContacts;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getCusMobile() {
		return cusMobile;
	}
	public void setCusMobile(String cusMobile) {
		this.cusMobile = cusMobile;
	}
	public String getReportState() {
		return reportState;
	}
	public void setReportState(String reportState) {
		this.reportState = reportState;
	}
	public String getHaveCopies() {
		return haveCopies;
	}
	public void setHaveCopies(String haveCopies) {
		this.haveCopies = haveCopies;
	}
	public String getReportRecipient() {
		return reportRecipient;
	}
	public void setReportRecipient(String reportRecipient) {
		this.reportRecipient = reportRecipient;
	}
	public String getReportPerson() {
		return reportPerson;
	}
	public void setReportPerson(String reportPerson) {
		this.reportPerson = reportPerson;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getExpressCompany() {
		return expressCompany;
	}
	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}
	public String getReportTemplate() {
		return reportTemplate;
	}
	public void setReportTemplate(String reportTemplate) {
		this.reportTemplate = reportTemplate;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
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
	public String getInputName() {
		return inputName;
	}
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}
	public String getInputTime() {
		return inputTime;
	}
	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
	public String getCodeState() {
		return codeState;
	}
	public void setCodeState(String codeState) {
		this.codeState = codeState;
	}
	public String getCodePrintDate() {
		return codePrintDate;
	}
	public void setCodePrintDate(String codePrintDate) {
		this.codePrintDate = codePrintDate;
	}
	public String getCodePerson() {
		return codePerson;
	}
	public void setCodePerson(String codePerson) {
		this.codePerson = codePerson;
	}
	public String getReviewUpdateName() {
		return reviewUpdateName;
	}
	public void setReviewUpdateName(String reviewUpdateName) {
		this.reviewUpdateName = reviewUpdateName;
	}
	public String getReviewUpdateTime() {
		return reviewUpdateTime;
	}
	public void setReviewUpdateTime(String reviewUpdateTime) {
		this.reviewUpdateTime = reviewUpdateTime;
	}
	public String getReviewInputName() {
		return reviewInputName;
	}
	public void setReviewInputName(String reviewInputName) {
		this.reviewInputName = reviewInputName;
	}
	public String getReviewInputTime() {
		return reviewInputTime;
	}
	public void setReviewInputTime(String reviewInputTime) {
		this.reviewInputTime = reviewInputTime;
	}
	
}
