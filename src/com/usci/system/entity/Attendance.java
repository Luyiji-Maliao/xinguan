package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_attendance")
public class Attendance {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	//部门
	@Column(length = 20)
	private String attDeptName;
	//员工
	@Column(length = 20)
	private String attUserName;
	//卡号
	@Column(length = 20)
	private String attJobNumber;
	//日期
	@Column(length = 20)
	private String attDate;
	//周
	@Column(length = 20)
	private String attWeek;
	//班次
	@Column(length = 20)
	private String attClasses;
	//考勤
	@Column(length = 20)
	private String attState;
	//签到
	@Column(length = 20)
	private String attSignin;
	//签退
	@Column(length = 20)
	private String attSignout;
	@Transient //统计考勤
	private String attCount;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAttDeptName() {
		return attDeptName;
	}
	public void setAttDeptName(String attDeptName) {
		this.attDeptName = attDeptName;
	}
	public String getAttUserName() {
		return attUserName;
	}
	public void setAttUserName(String attUserName) {
		this.attUserName = attUserName;
	}
	public String getAttJobNumber() {
		return attJobNumber;
	}
	public void setAttJobNumber(String attJobNumber) {
		this.attJobNumber = attJobNumber;
	}
	public String getAttDate() {
		return attDate;
	}
	public void setAttDate(String attDate) {
		this.attDate = attDate;
	}
	public String getAttWeek() {
		return attWeek;
	}
	public void setAttWeek(String attWeek) {
		this.attWeek = attWeek;
	}
	public String getAttClasses() {
		return attClasses;
	}
	public void setAttClasses(String attClasses) {
		this.attClasses = attClasses;
	}
	public String getAttState() {
		return attState;
	}
	public void setAttState(String attState) {
		this.attState = attState;
	}
	public String getAttSignin() {
		return attSignin;
	}
	public void setAttSignin(String attSignin) {
		this.attSignin = attSignin;
	}
	public String getAttSignout() {
		return attSignout;
	}
	public void setAttSignout(String attSignout) {
		this.attSignout = attSignout;
	}
	public String getAttCount() {
		return attCount;
	}
	public void setAttCount(String attCount) {
		this.attCount = attCount;
	}
	
}
