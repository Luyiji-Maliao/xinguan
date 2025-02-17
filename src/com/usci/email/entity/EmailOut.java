package com.usci.email.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;




/**
 * 外部邮件管理
 * @author 聂梦肖
 *
 */
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "email_emailout")
public class EmailOut extends EmailEntityField{
	
	/** 
	 * 所属方法（类-方法）
	 */
	@Column( length=100,nullable = false)
	private String emailProperty;
	/**
	 * 其他个性化标识（同一个方法中有多条线可其他标识区分）
	 */
	@Column( length=100,nullable = false)
	private String emailSign;
	/**
	 * 收件人
	 */
	@Column( length=100,nullable = false)
	private String emailNames;
	@Column(length=100,nullable = false)
	private String emailRemake;
	public String getEmailProperty() {
		return emailProperty;
	}
	public void setEmailProperty(String emailProperty) {
		this.emailProperty = emailProperty;
	}
	public String getEmailSign() {
		return emailSign;
	}
	public void setEmailSign(String emailSign) {
		this.emailSign = emailSign;
	}
	public String getEmailNames() {
		return emailNames;
	}
	public void setEmailNames(String emailNames) {
		this.emailNames = emailNames;
	}
	
	public String getEmailRemake() {
		return emailRemake;
	}
	public void setEmailRemake(String emailRemake) {
		this.emailRemake = emailRemake;
	}
	@Override
	public String toString() {
		return "EmailOut [getEmailNames()=" + getEmailNames()
				+ ", getEmailProperty()=" + getEmailProperty()
				+ ", getEmailSign()=" + getEmailSign() + ", getDataState()="
				+ getDataState() + ", getEntityFieldId()=" + getEntityFieldId()
				+ ", getInputName()=" + getInputName() + ", getInputTime()="
				+ getInputTime() + ", getUpdateName()=" + getUpdateName()
				+ ", getUpdateTime()=" + getUpdateTime() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
	
}
