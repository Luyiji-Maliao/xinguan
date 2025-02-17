package com.usci.norovirus.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name="xg_huikuan")
public class Huikuan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	private Integer yuyueid;
	//手续费
	private String shouxufei;
	//进账金额
	private String jinzhangmoney;
	//回款方式
	private String collectionmethod;
	//开票金额
	private String kaipiaomoney;
	//回款状态
	private String huikuanstate;
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

	public String getShouxufei() {
		return shouxufei;
	}

	public void setShouxufei(String shouxufei) {
		this.shouxufei = shouxufei;
	}

	public String getJinzhangmoney() {
		return jinzhangmoney;
	}

	public void setJinzhangmoney(String jinzhangmoney) {
		this.jinzhangmoney = jinzhangmoney;
	}

	public String getCollectionmethod() {
		return collectionmethod;
	}

	public void setCollectionmethod(String collectionmethod) {
		this.collectionmethod = collectionmethod;
	}

	public String getKaipiaomoney() {
		return kaipiaomoney;
	}

	public void setKaipiaomoney(String kaipiaomoney) {
		this.kaipiaomoney = kaipiaomoney;
	}

	public String getHuikuanstate() {
		return huikuanstate;
	}

	public void setHuikuanstate(String huikuanstate) {
		this.huikuanstate = huikuanstate;
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


}
