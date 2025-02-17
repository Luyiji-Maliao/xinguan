package com.usci.tool.entity;
import javax.persistence.*;
import java.util.List;

import javax.persistence.*;

@Table
@Entity
public class AutoBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer autobeanid;
	private String packageurl;
	@Transient
	private String packagebeanurl;
	@Transient
	private String packagedaourl;
	@Transient
	private String packageserviceurl;
	@Transient
	private String packageactionurl;
	private String beanname;
	@Transient
	private String beannametoLower;
	@Transient
	private String beannametoUpper;
	private String tabname;
	@Transient
	private String javabean;
	@Transient
	private String javadao;
	@Transient
	private String javaservice;
	@Transient
	private String javaaction;
	
	private String actionresult;
	private String searchfield;
	private int extcolumn; //列布局（）
	private String extcolumnWidth;
	private String projectworkspace;
	@Transient
	private List<AutoCol> extcol;
	
	
	public AutoBean() {
		super();
	}
	public AutoBean(String beanname,String packageurl) {
		super();
		this.beanname = beanname;
		this.beannametoLower=beanname.toLowerCase();
		this.beannametoUpper=beanname.substring(0,1).toUpperCase()+beanname.substring(1);
		this.javabean=beanname;
		this.javadao=beanname+"Dao";
		this.javaservice=beanname+"Service";
		this.javaaction=beanname+"Action";
		
		this.packageurl=packageurl;
		this.packagebeanurl=packageurl+".entity";
		this.packagedaourl=packageurl+".dao";
		this.packageserviceurl=packageurl+".service";
		this.packageactionurl=packageurl+".action";
	}
	
	
	public Integer getAutobeanid() {
		return autobeanid;
	}
	public void setAutobeanid(Integer autobeanid) {
		this.autobeanid = autobeanid;
	}
	public String getBeanname() {
		return beanname;
	}
	public void setBeanname(String beanname) {
		this.beanname = beanname;
	}
	public String getTabname() {
		return tabname;
	}
	public void setTabname(String tabname) {
		this.tabname = tabname;
	}
	public String getPackagebeanurl() {
		return packagebeanurl;
	}
	public void setPackagebeanurl(String packagebeanurl) {
		this.packagebeanurl = packagebeanurl;
	}
	public String getPackagedaourl() {
		return packagedaourl;
	}
	public void setPackagedaourl(String packagedaourl) {
		this.packagedaourl = packagedaourl;
	}
	public String getPackageserviceurl() {
		return packageserviceurl;
	}
	public void setPackageserviceurl(String packageserviceurl) {
		this.packageserviceurl = packageserviceurl;
	}
	public String getPackageactionurl() {
		return packageactionurl;
	}
	public void setPackageactionurl(String packageactionurl) {
		this.packageactionurl = packageactionurl;
	}
	public String getJavabean() {
		return javabean;
	}
	public void setJavabean(String javabean) {
		this.javabean = javabean;
	}
	public String getJavadao() {
		return javadao;
	}
	public void setJavadao(String javadao) {
		this.javadao = javadao;
	}
	public String getJavaservice() {
		return javaservice;
	}
	public void setJavaservice(String javaservice) {
		this.javaservice = javaservice;
	}
	public String getJavaaction() {
		return javaaction;
	}
	public void setJavaaction(String javaaction) {
		this.javaaction = javaaction;
	}
	public String getPackageurl() {
		return packageurl;
	}
	public void setPackageurl(String packageurl) {
		this.packageurl = packageurl;
	}
	public String getBeannametoLower() {
		return beannametoLower;
	}
	public void setBeannametoLower(String beannametoLower) {
		this.beannametoLower = beannametoLower;
	}
	public String getBeannametoUpper() {
		return beannametoUpper;
	}
	public void setBeannametoUpper(String beannametoUpper) {
		this.beannametoUpper = beannametoUpper;
	}
	public String getActionresult() {
		return actionresult;
	}
	public void setActionresult(String actionresult) {
		this.actionresult = actionresult;
	}
	public String getSearchfield() {
		return searchfield;
	}
	public void setSearchfield(String searchfield) {
		this.searchfield = searchfield;
	}
	public int getExtcolumn() {
		return extcolumn;
	}
	public void setExtcolumn(int extcolumn) {
		this.extcolumn = extcolumn;
	}
	public List<AutoCol> getExtcol() {
		return extcol;
	}
	public void setExtcol(List<AutoCol> extcol) {
		this.extcol = extcol;
	}
	public String getExtcolumnWidth() {
		return extcolumnWidth;
	}
	public void setExtcolumnWidth(String extcolumnWidth) {
		this.extcolumnWidth = extcolumnWidth;
	}
	public String getProjectworkspace() {
		return projectworkspace;
	}
	public void setProjectworkspace(String projectworkspace) {
		this.projectworkspace = projectworkspace;
	}
	
	
	
}
