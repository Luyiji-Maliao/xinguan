package com.usci.tool.entity;
import javax.persistence.*;


/**
 * Plus生成报告
 * **/
public class PiliangPlusReport{
	// 送检单位
	private String sjdw;
	// 样本编号
	private String sampleNo;
	// 姓名
	private String name;
	// 年龄
	private String age;
	// 采样日期
	private String cyrq;
	// 孕周
	private String yz;
	// 末次月经
	private String mcyj;
	// 样本类型
	private String yblx;
	// 检测结果
	private String jcjg;
	// 21
	private Double t21result;
	// 18
	private Double t18result;
	// 13
	private Double t13result;
	// 报告时间
	private String bgrq;
	
	
	
	public String getSjdw() {
		return sjdw;
	}
	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}
	public String getSampleNo() {
		return sampleNo;
	}
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getCyrq() {
		return cyrq;
	}
	public void setCyrq(String cyrq) {
		this.cyrq = cyrq;
	}
	public String getYz() {
		return yz;
	}
	public void setYz(String yz) {
		this.yz = yz;
	}
	public String getMcyj() {
		return mcyj;
	}
	public void setMcyj(String mcyj) {
		this.mcyj = mcyj;
	}
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx) {
		this.yblx = yblx;
	}
	public String getJcjg() {
		return jcjg;
	}
	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}
	public Double getT21result() {
		return t21result;
	}
	public void setT21result(Double t21result) {
		this.t21result = t21result;
	}
	public Double getT18result() {
		return t18result;
	}
	public void setT18result(Double t18result) {
		this.t18result = t18result;
	}
	public Double getT13result() {
		return t13result;
	}
	public void setT13result(Double t13result) {
		this.t13result = t13result;
	}
	public String getBgrq() {
		return bgrq;
	}
	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}
	
}