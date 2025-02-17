package com.usci.email.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;




/**
 * 外部（特殊）邮件管理
 * @author 聂梦肖
 *selectBeforeUpdate=true 更新前先查询确保是同一个session，负责dynamicUpdate不起效
 */
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "email_emailname")
public class EmailName extends EmailEntityField{
	
	/**
	 * 收件人
	 */
	@Column( length=30,nullable = false)
	private String emailName;
	/**
	 * 邮箱
	 */
	@Column( length=50,nullable = false, unique=true)
	private String emailAccount;
	
	
	
	
	public String getEmailName() {
		return emailName;
	}
	public void setEmailName(String emailName) {
		this.emailName = emailName;
	}
	public String getEmailAccount() {
		return emailAccount;
	}
	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}
	@Override
	public String toString() {
		return "EmailName [getEmailAccount()=" + getEmailAccount()
				+ ", getEmailName()=" + getEmailName() + ", getDataState()="
				+ getDataState() + ", getEntityFieldId()=" + getEntityFieldId()
				+ ", getInputName()=" + getInputName() + ", getInputTime()="
				+ getInputTime() + ", getUpdateName()=" + getUpdateName()
				+ ", getUpdateTime()=" + getUpdateTime() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
	
	
}
