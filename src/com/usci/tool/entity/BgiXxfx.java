package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 周期预警
 * BGI信息分析环节去华大数据库查询反馈时间
 * @author 刘楠
 *
 */

public class BgiXxfx {
	 //BGI样本编号
	 private String bgiNo;
	 //反馈时间
	 private String createTime;
	public String getBgiNo() {
		return bgiNo;
	}
	public void setBgiNo(String bgiNo) {
		this.bgiNo = bgiNo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public BgiXxfx(String bgiNo, String createTime) {
		super();
		this.bgiNo = bgiNo;
		this.createTime = createTime;
	}
	
	 
}
