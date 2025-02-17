package com.usci.tool.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;


import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lims.core.utils.excel.ImportExecl;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;

import com.usci.system.entity.User;


/**
 * 肿瘤实验批量
 * **/
@SuppressWarnings("serial")
@Component
@Scope("prototype")
@Results({
	@Result(name = "modulepage", location = "/WEB-INF/content/tools/zlpiliang.jsp")
})
public class zlpiliangAction extends CrudActionSupport<User>  {
	
	
	private String itemsxjyb;
	private String FiledataFileName;
	private File   Filedata;
	private String type;

	private int kaishi;
	private int jieshu;
	
	
	
	public int getKaishi() {
		return kaishi;
	}

	public void setKaishi(int kaishi) {
		this.kaishi = kaishi;
	}

	public int getJieshu() {
		return jieshu;
	}

	public void setJieshu(int jieshu) {
		this.jieshu = jieshu;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItemsxjyb() {
		return itemsxjyb;
	}

	public void setItemsxjyb(String itemsxjyb) {
		this.itemsxjyb = itemsxjyb;
	}
	
	
	public String getFiledataFileName() {
		return FiledataFileName;
	}

	public void setFiledataFileName(String filedataFileName) {
		FiledataFileName = filedataFileName;
	}

	public File getFiledata() {
		return Filedata;
	}

	public void setFiledata(File filedata) {
		Filedata = filedata;
	}
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modulepage() throws Exception {
		// TODO Auto-generated method stub
		return "modulepage";
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	 FileWriter fw=null;
	 BufferedWriter bw=null;



	


	

	
}
