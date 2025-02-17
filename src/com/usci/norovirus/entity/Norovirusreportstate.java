package com.usci.norovirus.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * SampleJd entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "norovirus_reportstate")
public class Norovirusreportstate implements java.io.Serializable {
	private Integer id;
	private String sampleNo;
	private String detectionResult;
	private String reportState;
	private String inputTime;
	private String inputName;
	private String updateTime;
	private String updateName;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "sampleNo", length = 55)
	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	@Column(name = "detectionResult", length = 255)
	public String getDetectionResult() {
		return detectionResult;
	}

	public void setDetectionResult(String detectionResult) {
		this.detectionResult = detectionResult;
	}
	@Column(name = "reportState", length = 55)
	public String getReportState() {
		return reportState;
	}

	public void setReportState(String reportState) {
		this.reportState = reportState;
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
}