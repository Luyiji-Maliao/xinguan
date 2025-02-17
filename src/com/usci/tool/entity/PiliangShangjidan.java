package com.usci.tool.entity;
import javax.persistence.*;


/**
 * 上机单批量更新用
 * **/
public class PiliangShangjidan {
	//FC_ID
	private String orderfcId;
	//更新人
	private String updateName;
	//更新时间
	private String updateTime;
	
	
	
	public String getOrderfcId() {
		return orderfcId;
	}
	public void setOrderfcId(String orderfcId) {
		this.orderfcId = orderfcId;
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
	
	
	
}
