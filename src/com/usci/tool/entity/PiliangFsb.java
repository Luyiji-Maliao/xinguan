package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Transient;

/**
 * 批量  产前  分析结果反馈   审核   发放
 * **/
public class PiliangFsb {
		//分析号
		private String analysisResultNo;
		//分析状态
		private String anaResultState;
		//样本编号
		private String anaSampleNo;
		//21风险指数
		private Double anaT21Value;
		//21风险程度
		private String anaT21Result;
		//18风险指数
		private Double anaT18Value;
		//18风险程度
		private String anaT18Result;
		//13风险指数
		private Double anaT13Value;
		//13风险程度
		private String anaT13Result;
		//结果描述
		private String anaResultDes;
		//其他提示
		private String anaOtherSuggest;
		//特别提醒
		private String anaSpecialResult;
		//结果反馈更新人
		private String updateName;
		//结果反馈更新时间
		private String updateTime;
//生成报告状态(set   已生成)
		//结果反馈录入人
		private String inputPerson;
		//结果反馈录入时间
		private String inputDate;
/**----------------审核--------------**/
//样本编号         (获取anaSampleNo)
		//审核状态
		private String reportState;//（未审核，通过，不通过）
		//审核备注
		private String reportOpinion;
		//审核人
		private String reportReviewName;
		//审核时间
		private String reportReviewTime;
		//报告路径
		private String reportPdf;
		//审核更新人
		private String shenheupdateName;
		//审核更新时间
		private String shenheupdateTime;
		//姓名
		private String reportName;
		//手机号
		private String reportMobile;
		//证件号
		private String reportIdCard;
		//送检单路径
		private String img;
		//审核录入人
		private String inputName;
		//审核录入时间
		private String inputTime;
		//送检单信息更正
		private String sjdgz;
		//送检单位
		private String checkHospital;
		//孕周
		private String gestationalWeeks;
		//如果重抽血			原样本编号
		private String againSampleNo;
		//如果重抽血			原入库日期
		private String osReceiveDateTime;
		//如果重抽血			原孕周
		private String againGestationalWeeks;
//21风险指数                  
//21风险程度
//18风险指数						(获取分析反馈的 数据)
//18风险程度
//13风险指数
//13风险程度
		//阳性的结论
		private String conclusion;
		//如有二次生成的报告，调取第一份报告的 审核意见
		private String againReportOpinion;
		//中山医院审核 默认 NULL（通过，不通过，未审核）
		private String zsstate;
/**----------------------发放----------------------**/
//样本编号         (获取anaSampleNo)
//客户姓名
//客户手机
//证件号
		//检测结果
		private String anaDetectionResult;
		//短信状态
		private String smsState;
		//打印状态
		private String reportIssueState;
		//快递单号
		private String expressNo;
		//快递公司
		private String expressCompany;
//报告路径         (获取审核的路径)
		//报告发放更新人
		private String fafangupdateName;
		//报告发放更新时间
		private String fafangupdateTime;
		//报告编号（YXA时间 序列号）
		private String reportIssueNo;
		//打印日期
		private String reportDate;
		//发短信时间
		private String smsDate;
		//报告发放录入人
		private String fafanginputPerson;
		//报告发放录入时间
		private String fafanginputDate;
		//区域联系人
		private String cityContacts;
		//代理商
		private String agent;
//送检医院					(获取审核的)
		//报告接收人类型
		private String reportRecipient;
		//报告接收人
		private String reportPerson;
		//报告模板
		private String reportTemplate;
		//短信状态更新人
		private String ssupdateName;
		//短信状态更新时间
		private String ssupdateTime;
		//打印状态更新时间
		private String prupdateName;
		//打印状态更新人
		private String prupdateTime;
		
		
		
		public String getAnalysisResultNo() {
			return analysisResultNo;
		}
		public void setAnalysisResultNo(String analysisResultNo) {
			this.analysisResultNo = analysisResultNo;
		}
		public String getAnaResultState() {
			return anaResultState;
		}
		public void setAnaResultState(String anaResultState) {
			this.anaResultState = anaResultState;
		}
		public String getAnaSampleNo() {
			return anaSampleNo;
		}
		public void setAnaSampleNo(String anaSampleNo) {
			this.anaSampleNo = anaSampleNo;
		}
		public Double getAnaT21Value() {
			return anaT21Value;
		}
		public void setAnaT21Value(Double anaT21Value) {
			this.anaT21Value = anaT21Value;
		}
		public String getAnaT21Result() {
			return anaT21Result;
		}
		public void setAnaT21Result(String anaT21Result) {
			this.anaT21Result = anaT21Result;
		}
		public Double getAnaT18Value() {
			return anaT18Value;
		}
		public void setAnaT18Value(Double anaT18Value) {
			this.anaT18Value = anaT18Value;
		}
		public String getAnaT18Result() {
			return anaT18Result;
		}
		public void setAnaT18Result(String anaT18Result) {
			this.anaT18Result = anaT18Result;
		}
		public Double getAnaT13Value() {
			return anaT13Value;
		}
		public void setAnaT13Value(Double anaT13Value) {
			this.anaT13Value = anaT13Value;
		}
		public String getAnaT13Result() {
			return anaT13Result;
		}
		public void setAnaT13Result(String anaT13Result) {
			this.anaT13Result = anaT13Result;
		}
		public String getAnaResultDes() {
			return anaResultDes;
		}
		public void setAnaResultDes(String anaResultDes) {
			this.anaResultDes = anaResultDes;
		}
		public String getAnaOtherSuggest() {
			return anaOtherSuggest;
		}
		public void setAnaOtherSuggest(String anaOtherSuggest) {
			this.anaOtherSuggest = anaOtherSuggest;
		}
		public String getAnaSpecialResult() {
			return anaSpecialResult;
		}
		public void setAnaSpecialResult(String anaSpecialResult) {
			this.anaSpecialResult = anaSpecialResult;
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
		public String getInputPerson() {
			return inputPerson;
		}
		public void setInputPerson(String inputPerson) {
			this.inputPerson = inputPerson;
		}
		public String getInputDate() {
			return inputDate;
		}
		public void setInputDate(String inputDate) {
			this.inputDate = inputDate;
		}
		public String getReportState() {
			return reportState;
		}
		public void setReportState(String reportState) {
			this.reportState = reportState;
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
		public String getReportPdf() {
			return reportPdf;
		}
		public void setReportPdf(String reportPdf) {
			this.reportPdf = reportPdf;
		}
		public String getShenheupdateName() {
			return shenheupdateName;
		}
		public void setShenheupdateName(String shenheupdateName) {
			this.shenheupdateName = shenheupdateName;
		}
		public String getShenheupdateTime() {
			return shenheupdateTime;
		}
		public void setShenheupdateTime(String shenheupdateTime) {
			this.shenheupdateTime = shenheupdateTime;
		}
		public String getReportName() {
			return reportName;
		}
		public void setReportName(String reportName) {
			this.reportName = reportName;
		}
		public String getReportMobile() {
			return reportMobile;
		}
		public void setReportMobile(String reportMobile) {
			this.reportMobile = reportMobile;
		}
		public String getReportIdCard() {
			return reportIdCard;
		}
		public void setReportIdCard(String reportIdCard) {
			this.reportIdCard = reportIdCard;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
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
		public String getSjdgz() {
			return sjdgz;
		}
		public void setSjdgz(String sjdgz) {
			this.sjdgz = sjdgz;
		}
		public String getCheckHospital() {
			return checkHospital;
		}
		public void setCheckHospital(String checkHospital) {
			this.checkHospital = checkHospital;
		}
		public String getAgainSampleNo() {
			return againSampleNo;
		}
		public void setAgainSampleNo(String againSampleNo) {
			this.againSampleNo = againSampleNo;
		}
		public String getOsReceiveDateTime() {
			return osReceiveDateTime;
		}
		public void setOsReceiveDateTime(String osReceiveDateTime) {
			this.osReceiveDateTime = osReceiveDateTime;
		}
		public String getGestationalWeeks() {
			return gestationalWeeks;
		}
		public void setGestationalWeeks(String gestationalWeeks) {
			this.gestationalWeeks = gestationalWeeks;
		}
		public String getAgainGestationalWeeks() {
			return againGestationalWeeks;
		}
		public void setAgainGestationalWeeks(String againGestationalWeeks) {
			this.againGestationalWeeks = againGestationalWeeks;
		}
		public String getConclusion() {
			return conclusion;
		}
		public void setConclusion(String conclusion) {
			this.conclusion = conclusion;
		}
		public String getAgainReportOpinion() {
			return againReportOpinion;
		}
		public void setAgainReportOpinion(String againReportOpinion) {
			this.againReportOpinion = againReportOpinion;
		}
		public String getZsstate() {
			return zsstate;
		}
		public void setZsstate(String zsstate) {
			this.zsstate = zsstate;
		}
		public String getAnaDetectionResult() {
			return anaDetectionResult;
		}
		public void setAnaDetectionResult(String anaDetectionResult) {
			this.anaDetectionResult = anaDetectionResult;
		}
		public String getSmsState() {
			return smsState;
		}
		public void setSmsState(String smsState) {
			this.smsState = smsState;
		}
		public String getReportIssueState() {
			return reportIssueState;
		}
		public void setReportIssueState(String reportIssueState) {
			this.reportIssueState = reportIssueState;
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
		public String getFafangupdateName() {
			return fafangupdateName;
		}
		public void setFafangupdateName(String fafangupdateName) {
			this.fafangupdateName = fafangupdateName;
		}
		public String getFafangupdateTime() {
			return fafangupdateTime;
		}
		public void setFafangupdateTime(String fafangupdateTime) {
			this.fafangupdateTime = fafangupdateTime;
		}
		public String getReportIssueNo() {
			return reportIssueNo;
		}
		public void setReportIssueNo(String reportIssueNo) {
			this.reportIssueNo = reportIssueNo;
		}
		public String getReportDate() {
			return reportDate;
		}
		public void setReportDate(String reportDate) {
			this.reportDate = reportDate;
		}
		public String getSmsDate() {
			return smsDate;
		}
		public void setSmsDate(String smsDate) {
			this.smsDate = smsDate;
		}
		public String getFafanginputPerson() {
			return fafanginputPerson;
		}
		public void setFafanginputPerson(String fafanginputPerson) {
			this.fafanginputPerson = fafanginputPerson;
		}
		public String getFafanginputDate() {
			return fafanginputDate;
		}
		public void setFafanginputDate(String fafanginputDate) {
			this.fafanginputDate = fafanginputDate;
		}
		public String getCityContacts() {
			return cityContacts;
		}
		public void setCityContacts(String cityContacts) {
			this.cityContacts = cityContacts;
		}
		public String getAgent() {
			return agent;
		}
		public void setAgent(String agent) {
			this.agent = agent;
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
		public String getReportTemplate() {
			return reportTemplate;
		}
		public void setReportTemplate(String reportTemplate) {
			this.reportTemplate = reportTemplate;
		}
		public String getSsupdateName() {
			return ssupdateName;
		}
		public void setSsupdateName(String ssupdateName) {
			this.ssupdateName = ssupdateName;
		}
		public String getSsupdateTime() {
			return ssupdateTime;
		}
		public void setSsupdateTime(String ssupdateTime) {
			this.ssupdateTime = ssupdateTime;
		}
		public String getPrupdateName() {
			return prupdateName;
		}
		public void setPrupdateName(String prupdateName) {
			this.prupdateName = prupdateName;
		}
		public String getPrupdateTime() {
			return prupdateTime;
		}
		public void setPrupdateTime(String prupdateTime) {
			this.prupdateTime = prupdateTime;
		}
	
}
