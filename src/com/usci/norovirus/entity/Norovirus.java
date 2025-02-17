package com.usci.norovirus.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * SampleJd entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "norovirus")
public class Norovirus implements java.io.Serializable {
	private Integer id;
	private String name;
	private String idcard;
	private String sampleNo;
	private String sendingPerson;
	private String checkProject;
	private String sampleType;
	private String inspection;
	private String samplingDate;
	private String receptionDate;
	private String ct;
	private String iss;
	private String detectionResult;
	private String pathName;
	private String inputTime;
	private String inputName;
	private String reservation;
	private String englishName;
	private String passport;
	private String reportdate;
	private String englishreport;


	private String updateTime;
	private String updateName;

	private String qudao;
	private String fiveone;
	private String sreportdate;
	private String sinputTime;
	private String ssamplingDate;
	private String sreceptionDate;

	private Appointmentinfo appointmentinfo;

	private XgBuKaiFaPiao xgBuKaiFaPiao;
	@Transient
	public String getFiveone() {
		return fiveone;
	}

	public void setFiveone(String fiveone) {
		this.fiveone = fiveone;
	}
	@Transient
	public String getSreportdate() {
		return sreportdate;
	}

	public void setSreportdate(String sreportdate) {
		this.sreportdate = sreportdate;
	}
	@Transient
	public String getSinputTime() {
		return sinputTime;
	}

	public void setSinputTime(String sinputTime) {
		this.sinputTime = sinputTime;
	}
	@Transient
	public String getSsamplingDate() {
		return ssamplingDate;
	}

	public void setSsamplingDate(String ssamplingDate) {
		this.ssamplingDate = ssamplingDate;
	}
	@Transient
	public String getSreceptionDate() {
		return sreceptionDate;
	}

	public void setSreceptionDate(String sreceptionDate) {
		this.sreceptionDate = sreceptionDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 55)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "sampleNo", length = 55)
	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	@Column(name = "idcard",length = 55)
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}


	@Column(name = "sendingPerson", length = 55)
	public String getSendingPerson() {
		return sendingPerson;
	}

	public void setSendingPerson(String sendingPerson) {
		this.sendingPerson = sendingPerson;
	}

	@Column(name = "sampleType", length = 55)
	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	@Column(name = "inspection", length = 55)
	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}
	@Column(name = "samplingDate", length = 55)
	public String getSamplingDate() {
		return samplingDate;
	}

	public void setSamplingDate(String samplingDate) {
		this.samplingDate = samplingDate;
	}
	@Column(name = "receptionDate", length = 55)
	public String getReceptionDate() {
		return receptionDate;
	}

	public void setReceptionDate(String receptionDate) {
		this.receptionDate = receptionDate;
	}
	@Column(name = "ct", length = 255)
	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}
	@Column(name = "iss", length = 255)
	public String getIss() {
		return iss;
	}

	public void setIss(String iss) {
		this.iss = iss;
	}
	@Column(name = "detectionResult", length = 255)
	public String getDetectionResult() {
		return detectionResult;
	}

	public void setDetectionResult(String detectionResult) {
		this.detectionResult = detectionResult;
	}

	@Column(name = "inputTime", length = 50)
	public String getInputTime() {
		return inputTime;
	}

	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}

	@Column(name = "inputName", length = 50)
	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	@Column(name = "pathName", length = 500)
	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	@Column(name = "checkProject", length = 55)
	public String getCheckProject() {
		return checkProject;
	}

	public void setCheckProject(String checkProject) {
		this.checkProject = checkProject;
	}
	@Column(name = "reservation", length = 255)
	public String getReservation() {
		return reservation;
	}

	public void setReservation(String reservation) {
		this.reservation = reservation;
	}
	@Transient
	public Appointmentinfo getAppointmentinfo() {
		return appointmentinfo;
	}

	public void setAppointmentinfo(Appointmentinfo appointmentinfo) {
		this.appointmentinfo = appointmentinfo;
	}
	@Column(name = "updateTime", length = 55)
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "updateName", length = 55)
	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	@Column(name = "qudao", length = 55)
	public String getQudao() {
		return qudao;
	}

	public void setQudao(String qudao) {
		this.qudao = qudao;
	}
	@Column(name = "englishName", length = 100)
	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	@Column(name = "passport", length = 55)
	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}
	@Column(name = "reportdate", length = 55)
	public String getReportdate() {
		return reportdate;
	}

	public void setReportdate(String reportdate) {
		this.reportdate = reportdate;
	}

	@Transient
	public XgBuKaiFaPiao getXgBuKaiFaPiao() {
		return xgBuKaiFaPiao;
	}

	public void setXgBuKaiFaPiao(XgBuKaiFaPiao xgBuKaiFaPiao) {
		this.xgBuKaiFaPiao = xgBuKaiFaPiao;
	}

	public String getEnglishreport() {
		return englishreport;
	}

	public void setEnglishreport(String englishreport) {
		this.englishreport = englishreport;
	}


	@Override
	public String toString() {
		return "Norovirus{" +
				"id=" + id +
				", name='" + name + '\'' +
				", idcard='" + idcard + '\'' +
				", sampleNo='" + sampleNo + '\'' +
				", sendingPerson='" + sendingPerson + '\'' +
				", checkProject='" + checkProject + '\'' +
				", sampleType='" + sampleType + '\'' +
				", inspection='" + inspection + '\'' +
				", samplingDate='" + samplingDate + '\'' +
				", receptionDate='" + receptionDate + '\'' +
				", ct='" + ct + '\'' +
				", iss='" + iss + '\'' +
				", detectionResult='" + detectionResult + '\'' +
				", pathName='" + pathName + '\'' +
				", inputTime='" + inputTime + '\'' +
				", inputName='" + inputName + '\'' +
				", reservation='" + reservation + '\'' +
				", englishName='" + englishName + '\'' +
				", passport='" + passport + '\'' +
				", reportdate='" + reportdate + '\'' +
				", englishreport='" + englishreport + '\'' +
				", updateTime='" + updateTime + '\'' +
				", updateName='" + updateName + '\'' +
				", qudao='" + qudao + '\'' +
				", fiveone='" + fiveone + '\'' +
				", appointmentinfo=" + appointmentinfo +
				", xgBuKaiFaPiao=" + xgBuKaiFaPiao +
				'}';
	}
}