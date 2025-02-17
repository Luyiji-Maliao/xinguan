package com.usci.tool.officialaccounts.action;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.tool.officialaccounts.entity.OfficialAccountsPushMsg;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("exceptionstack") })
public class OfficialaccountsAction extends CrudActionSupport<OfficialAccountsPushMsg>{
	/**		 
	 * 
	 */
	private static final long serialVersionUID = -5542545754037851964L;
	private static final Logger log=LoggerFactory.getLogger(OfficialaccountsAction.class);
	private OfficialAccountsPushMsg entity;
	private String fileToBase;
	
	public String getFileToBase() {
		return fileToBase;
	}

	public void setFileToBase(String fileToBase) {
		this.fileToBase = fileToBase;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modulepage() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		entity = new OfficialAccountsPushMsg();
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OfficialAccountsPushMsg getModel() {
		// TODO Auto-generated method stub
		return entity;
	}
	
	public void pdfToBase64() throws IOException{
		
		if(fileToBase==null){
			Struts2Utils.renderJson("参数不能为空");
		}
		String path=ServletActionContext.getServletContext().getRealPath("/"+fileToBase);
		log.info("pdfToBase64:{}",path);
		if(path==null){
			Struts2Utils.renderJson("PDF不存在");
		}
		
		String base64=Struts2Utils.fileToBaseCode(path);
		String head = "data:application/pdf;base64,";
		base64 = base64.replaceAll("\r\n", "");
		base64 = base64.replaceAll("\\+", "%2B");
		Struts2Utils.renderJson(head+base64);
		
	}
	public void fileToBase64() throws IOException{
			
			if(fileToBase==null){
				Struts2Utils.renderJson("参数不能为空");
			}
			String path=ServletActionContext.getServletContext().getRealPath("/"+fileToBase);
			log.info("fileToBase64:{}",path);
			if(path==null){
				Struts2Utils.renderJson("文件不存在");
			}
			String base64=Struts2Utils.fileToBaseCode(path);
			Struts2Utils.renderJson(base64);
			
		}
}
