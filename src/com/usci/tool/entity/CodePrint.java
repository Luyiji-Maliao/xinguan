package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 条码打印
 * @author 周晓锋
 *
 */
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "tl_codeprint")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class CodePrint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codePrintId;
	//姓名
	@Column(length = 30)
	private String name;
	//样本编号
	@Column(length = 30)
	private String sampleNo;
	//送检单位
	@Column(length = 30)
	private String inspectionUnit;
	//下载状态
	@Column(length = 30)
	private String downState;
	//录入人
	@Column(length = 30,nullable = false)
	private String inputName;
	//录入时间
	@Column(length = 30,nullable = false)
	private String inputTime;
	//更新人
	@Column(length = 30,nullable = false)
	private String updateName;
	//更新时间
	@Column(length = 30,nullable = false)
	private String updateTime;
	@Transient
	private String inputTime2;
	public Integer getCodePrintId() {
		return codePrintId;
	}
	public void setCodePrintId(Integer codePrintId) {
		this.codePrintId = codePrintId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSampleNo() {
		return sampleNo;
	}
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getInspectionUnit() {
		return inspectionUnit;
	}
	public void setInspectionUnit(String inspectionUnit) {
		this.inspectionUnit = inspectionUnit;
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
	public String getDownState() {
		return downState;
	}
	public void setDownState(String downState) {
		this.downState = downState;
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
	public String getInputTime2() {
		return inputTime2;
	}
	public void setInputTime2(String inputTime2) {
		this.inputTime2 = inputTime2;
	}
	
	
	
}
