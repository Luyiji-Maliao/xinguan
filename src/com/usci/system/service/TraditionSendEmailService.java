package com.usci.system.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.utils.mail.JavaMailQunFa;
import com.lims.core.utils.web.Struts2Utils;

@Component
@Transactional(readOnly=true)
public class TraditionSendEmailService {
	@Autowired
	private SendEmailService sendEmailService;
	/**
	 * 财审点击本次财审结束，筛选出通过的体检样本
	 * @param title
	 * @param content
	 */
	public void transferPass2(String title, String content) {
		/*#StringBuilder email=new StringBuilder();
		email.append("jietian@scisoon.cn");
		email.append(",linding@scisoon.cn");
		email.append(",weixiaozhao@scisoon.cn");
		email.append(",nanasuo@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(","+Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
	}
	
	/**
	 * 财审点击本次财审结束，筛选出通过的肿瘤样本
	 * @param title
	 * @param content
	 */
	public void transferPass(String title, String content) {
		/*#StringBuilder email=new StringBuilder();
		email.append("linding@scisoon.cn");
		email.append(",jietian@scisoon.cn");
		email.append(",nanasuo@scisoon.cn");
		email.append(",shuangliu@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(","+Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
	}
	public void transferPass21(String title, String content) {
		/*#StringBuilder email=new StringBuilder();
		email.append("linding@scisoon.cn");
		email.append(",jietian@scisoon.cn");
		email.append(",fulijia@scisoon.cn");
		email.append(",nanasuo@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(","+Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
	}
	public void transferPassBlood(String title, String content) {
		/*#StringBuilder email=new StringBuilder();
		email.append("linding@scisoon.cn");
		email.append(",jietian@scisoon.cn");
		
		email.append(",nanasuo@scisoon.cn");
		email.append(",shuangliu@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(","+Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
	}
	/**
	 * 财务，部门确认退款，项目管理确认时，发邮件通知实验
	 * */
	public void financeRefundSendEmail1(String title,String content){
		/*#StringBuilder email=new StringBuilder();
		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
	}
	


	/**
	 * 财务，点击本次财审结束，统计生育线的不通过数量
	 * */
	public void businessRefundSendEmail1(String title,String content){
		/*#StringBuilder email=new StringBuilder();
		email.append("schedule@scisoon.cn");
		email.append(",nanasuo@scisoon.cn");
		email.append(",weixiaozhao@scisoon.cn");
		email.append(",jingjingwu@scisoon.cn");
		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(","+Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
	}
	
	/**
	 * 财务，点击本次财审结束，统计肿瘤线的不通过数量
	 * */
	public void businessRefundSendEmail2(String title,String content){
		/*#StringBuilder email=new StringBuilder();
		email.append("shuangliu@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		email.append(",nanasuo@scisoon.cn");

		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(","+Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
	}

	/**
	 * 财务，点击本次财审结束，统计肿瘤线的不通过数量
	 * 新产线（优替--乳腺癌21基因）
	 * */
	public void businessRefundSendEmail4(String title,String content){
		/*#StringBuilder email=new StringBuilder();
		email.append("fulijia@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		email.append(",nanasuo@scisoon.cn");

		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(","+Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
		
	}
	/**
	 * 财务，点击本次财审结束，统计肿瘤线的不通过数量
	 * 血液病线（优睿）
	 * */
	public void businessRefundSendEmail5(String title,String content){
		/*#StringBuilder email=new StringBuilder();
		email.append("shuangliu@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		email.append(",nanasuo@scisoon.cn");
		email.append(",nanmu@scisoon.cn");

		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(","+Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
		
	}
	/**
	 * 财务，点击本次财审结束，统计体检线的不通过数量
	 * */
	public void businessRefundSendEmail3(String title,String content){
		/*#StringBuilder email=new StringBuilder();
		email.append("jietian@scisoon.cn");
		email.append(",weixiaozhao@scisoon.cn");
		email.append(",nanasuo@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		email.append(",schedule@scisoon.cn");
		//抄送给操作人
		if(Struts2Utils.getSessionUser().getEmail()!=null&&!"".equals(Struts2Utils.getSessionUser().getEmail())){
			email.append(","+Struts2Utils.getSessionUser().getEmail().trim());
		}
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,content,email.toString(),null);#*/
	}
}
