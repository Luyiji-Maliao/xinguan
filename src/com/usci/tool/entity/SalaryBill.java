package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 工资条邮箱
 * @author 刘楠
 *
 */
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "salarybill_email")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class SalaryBill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 //姓名
	 @Column(length = 50 )
	 private String uname;
	 //邮箱
	 @Column(length = 100)
	 private String uemail;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUemail() {
		return uemail;
	}
	public void setUemail(String uemail) {
		this.uemail = uemail;
	}
	 
}
