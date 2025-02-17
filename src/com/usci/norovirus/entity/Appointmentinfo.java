package com.usci.norovirus.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * SampleJd entity. @author MyEclipse Persistence Tools
 */

public class Appointmentinfo implements java.io.Serializable {

	private String yuyuenum; //'预约号'
	private String sname; //'姓名'
	private String outtradeno; // '商户单号'
	private String totalfee; //'付款金额'
	private String isfapiao; //'是否要发票'
	private String fapiaotaitou; //'发票抬头'
	private String caiyangdidian; //'采样点'
	private String cooperationcompanyname; //'合作机构'
	private String cooperationsubcompanyname; //'分支机构'
	private String businessmanager; //'业务员'
	private String salename; //'销售姓名'
	private String shuihao; //税号
	private String fapiaotype; //发票类型
	private String accessemail;
	private String qudao; //来源渠道
	private String shicaiyangdian; //实际采样点
	private Integer id; //预约id
	private String phone;
	private String duihuanma;
	private String sampleNo;
	private String samplebindtime;
	private String subjecttype;
	private String englishName;
	private String passport;
	private String englishreport;


	public String getSubjecttype() {
		return subjecttype;
	}

	public void setSubjecttype(String subjecttype) {
		this.subjecttype = subjecttype;
	}

	public String getDuihuanma() {
		return duihuanma;
	}

	public void setDuihuanma(String duihuanma) {
		this.duihuanma = duihuanma;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQudao() {
		return qudao;
	}

	public void setQudao(String qudao) {
		this.qudao = qudao;
	}

	public String getAccessemail() {
		return accessemail;
	}

	public void setAccessemail(String accessemail) {
		this.accessemail = accessemail;
	}

	public String getShuihao() {
		return shuihao;
	}

	public void setShuihao(String shuihao) {
		this.shuihao = shuihao;
	}

	public String getFapiaotype() {
		return fapiaotype;
	}

	public void setFapiaotype(String fapiaotype) {
		this.fapiaotype = fapiaotype;
	}

	public String getYuyuenum() {
		return yuyuenum;
	}

	public void setYuyuenum(String yuyuenum) {
		this.yuyuenum = yuyuenum;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String name) {
		this.sname = name;
	}

	public String getOuttradeno() {
		return outtradeno;
	}

	public void setOuttradeno(String outtradeno) {
		this.outtradeno = outtradeno;
	}

	public String getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
	}

	public String getIsfapiao() {
		return isfapiao;
	}

	public void setIsfapiao(String isfapiao) {
		this.isfapiao = isfapiao;
	}

	public String getFapiaotaitou() {
		return fapiaotaitou;
	}

	public void setFapiaotaitou(String fapiaotaitou) {
		this.fapiaotaitou = fapiaotaitou;
	}

	public String getCaiyangdidian() {
		return caiyangdidian;
	}

	public void setCaiyangdidian(String caiyangdidian) {
		this.caiyangdidian = caiyangdidian;
	}

	public String getCooperationcompanyname() {
		return cooperationcompanyname;
	}

	public void setCooperationcompanyname(String cooperationcompanyname) {
		this.cooperationcompanyname = cooperationcompanyname;
	}

	public String getCooperationsubcompanyname() {
		return cooperationsubcompanyname;
	}

	public void setCooperationsubcompanyname(String cooperationsubcompanyname) {
		this.cooperationsubcompanyname = cooperationsubcompanyname;
	}

	public String getBusinessmanager() {
		return businessmanager;
	}

	public void setBusinessmanager(String businessmanager) {
		this.businessmanager = businessmanager;
	}

	public String getSalename() {
		return salename;
	}

	public void setSalename(String salename) {
		this.salename = salename;
	}

	public String getShicaiyangdian() {
		return shicaiyangdian;
	}

	public void setShicaiyangdian(String shicaiyangdian) {
		this.shicaiyangdian = shicaiyangdian;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getSamplebindtime() {
		return samplebindtime;
	}

	public void setSamplebindtime(String samplebindtime) {
		this.samplebindtime = samplebindtime;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getEnglishreport() {
		return englishreport;
	}

	public void setEnglishreport(String englishreport) {
		this.englishreport = englishreport;
	}
}