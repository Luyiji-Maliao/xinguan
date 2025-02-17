package com.usci.norovirus.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name="xg_fapiao")
public class Fapiao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	private Integer yuyueid;
	//发票时间
	private String kaipiaodate;
	//发票号
	private String kaipiaono;
	//开票状态
	private String kaipiaostate;
	//录入人
	private String inputName;
	//录入时间
	private String inputTime;
	//更新人
	private String updateTime;
	//更新时间
	private String updateName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYuyueid() {
		return yuyueid;
	}

	public void setYuyueid(Integer yuyueid) {
		this.yuyueid = yuyueid;
	}

	public String getKaipiaodate() {
		return kaipiaodate;
	}

	public void setKaipiaodate(String kaipiaodate) {
		this.kaipiaodate = kaipiaodate;
	}

	public String getKaipiaono() {
		return kaipiaono;
	}

	public void setKaipiaono(String kaipiaono) {
		this.kaipiaono = kaipiaono;
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public String getKaipiaostate() {
		return kaipiaostate;
	}

	public void setKaipiaostate(String kaipiaostate) {
		this.kaipiaostate = kaipiaostate;
	}
}
