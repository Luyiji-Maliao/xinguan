package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.*;

@Table
@Entity
public class AutoCol {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer autocolid;
	private Integer autobeanid;
	private String colName;	//列明
	private String colHeader;//ext 列显示
	private String colsearch;//ext 高级搜索 id
	private String colType;	//列类型
	private String extcolType; //ext类型
	private String colUPName;  //首字母大写列名
	private String colUPERName;  //全大写列名
	private String colLong;	//列长度
	private String colAllType;	//列全限定类型
	private String colNull;		//可否为空
	private String colJspName;		//jsp所用列描述名
	private String colhidden;//ext form列是否显示
	public AutoCol() {
	}
	

	
	public AutoCol(String colName, String colType,String collong,String colHeader,String colhidden) {
		
		setColType(colType);
		setColLong(collong);
		setColHeader(colHeader);
		setExtcolType(colType);
		setColName(colName);
		setColUPName();
		setColsearch(colName);
		setColhidden(colhidden);
	}
	
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName.toLowerCase();
		if(colName.contains("date")||colName.contains("time")){
				this.extcolType="datefield";
		}
	}
	
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
		/**
		 * 原用于数据库
		if(colType.equals("nvarchar")||colType.equals("varchar")||colType.equals("nchar")||colType.equals("char"))
			this.colType="String";
		if(colType.equals("datetime")){
			this.colType="Date";
		}	
		*/	
	}
	
	public String getColUPName() {
		return colUPName;
	}
	public void setColUPName() {
		this.colUPName=this.colName.substring(0,1).toUpperCase()+this.colName.substring(1);
	}
	
	public String getColLong() {
		return colLong;
	}
	
	public void setColLong(String colLong) {
		this.colLong = colLong;
	}
	
	
	public String getColUPERName() {
		return colUPERName;
	}
	
	public void setColUPERName() {
		this.colUPERName=this.colName.toUpperCase();
	}
	
	public String getColAllType() {
		return colAllType;
	}
	
	public void setColAllType() {
		this.colAllType = "java.lang."+colType;
		if(colType.equals("Date")){
			this.colAllType="java.util.Date";
		}
	}
	
	public String getColNull() {
		return colNull;
	}
	
	public void setColNull(String colNull) {
		this.colNull = colNull;
	}
	public void setColNull() {
		if(this.colNull==null||this.colNull.equals("")){
			this.colNull="false";
		}
	}
	
	public String getColJspName() {
		return colJspName;
	}
	
	public void setColJspName(String colJspName) {
		this.colJspName = colJspName;
	}

	public String getColHeader() {
		return colHeader;
	}

	public void setColHeader(String colHeader) {
		this.colHeader = colHeader;
	}

	public void setColUPName(String colUPName) {
		this.colUPName = colUPName;
	}

	public void setColUPERName(String colUPERName) {
		this.colUPERName = colUPERName;
	}

	public void setColAllType(String colAllType) {
		this.colAllType = colAllType;
	}

	public String getExtcolType() {
		return extcolType;
	}

	public void setExtcolType(String extcolType) {
		if(extcolType.equals("String")){
			this.extcolType="textfield";
		}
		if(extcolType.equals("Double")||extcolType.equals("Integer")){
			this.extcolType="numberfield";
		}
		
	}

	public String getColsearch() {
		return colsearch;
	}

	public void setColsearch(String colsearch) {
		this.colsearch = "s_"+colsearch;
	}

	public String getColhidden() {
		return colhidden;
	}

	public void setColhidden(String colhidden) {
		this.colhidden = colhidden;
	}



	public Integer getAutocolid() {
		return autocolid;
	}



	public void setAutocolid(Integer autocolid) {
		this.autocolid = autocolid;
	}



	public Integer getAutobeanid() {
		return autobeanid;
	}



	public void setAutobeanid(Integer autobeanid) {
		this.autobeanid = autobeanid;
	}
	
	
}

