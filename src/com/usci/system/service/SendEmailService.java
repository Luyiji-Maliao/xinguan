package com.usci.system.service;

import java.io.File;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.utils.mail.JavaMailQunFa;
import com.lims.core.utils.mail.JavaMailSalaryBill;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.email.entity.EmailName;
import com.usci.email.entity.EmailOut;
import com.usci.email.service.EmailNameService;
import com.usci.email.service.EmailOutService;
import com.usci.tool.officialaccounts.service.OfficialAccountsPushMsgUtilService;
import com.usci.system.entity.User;

@Component
@Transactional(readOnly=true)
public class SendEmailService {
	@Autowired
	private UserService userService;
	@Autowired
	private OfficialAccountsPushMsgUtilService accountsPushMsgUtilService;
	@Autowired
	private EmailOutService emailOutService;
	@Autowired
	private EmailNameService emailNameService;
	@Autowired
	private SendUrlService sendUrlService;

	private static final Logger log= LoggerFactory.getLogger(SendEmailService.class);





	/**
	 *
	 * @param emailProperty 配置属性（当前类-方法）
	 * @param emailSign 特殊标识（默认： 当前方法-1）
	 * @return
	 */
	private String findEmail(String emailProperty,String emailSign){
		String opertorName=Struts2Utils.getUser()!=null?Struts2Utils.getUser().getName():"";
		String emailAccount="";
		 EmailOut eo=new EmailOut();
		 eo.setDataState(-1);
		 eo.setEmailProperty(emailProperty);
		 //特殊标识，默认 当前方法名-1
		 eo.setEmailSign(emailSign);
		 List<EmailOut> leo=emailOutService.selectEntity(eo);
			//EmailProperty和EmailSign组合唯一
			if(leo.size()==1){
				String [] o=leo.get(0).getEmailNames().split(";");
				emailAccount=this.findByName(o);
				log.info("邮件系统：操作人：{},emailProperty：{},emailSign:{},email:{}",opertorName,emailProperty,emailSign,emailAccount);
				if(emailAccount.trim().length()==0){
					emailAccount="";
				}
			}else{
				log.warn("邮件系统：操作人：{},不存在或存在 EmailProperty和EmailSign的多个组合，不发送",opertorName);
			}
		 return emailAccount;
	}
	/**
	 * 根据邮箱姓名查找邮箱
	 * @param emailName
	 * @return
	 */
	private String findByName(String [] emailName){
		StringBuilder email=new StringBuilder();
		//先查询系统中人员邮箱
		List<User> lu=userService.findByName(emailName);
		//判断是否有外部(特殊)邮箱
		if(lu.size()!=emailName.length){
			List<EmailName> len=emailNameService.findByName(emailName);
			for (EmailName en : len) {
	    		if(email.length()>0){
	    			email.append(","+en.getEmailAccount());
	    		}else{
	    			email.append(en.getEmailAccount());
	    		}
			}
		}

		for (User user : lu) {
			if(email.length()>0){
    			email.append(","+user.getEmail());
    		}else{
    			email.append(user.getEmail());
    		}
		}

		//所有邮件都抄送操作人
		String opertorEmail=Struts2Utils.getUser()!=null?Struts2Utils.getUser().getEmail():"";
		if(opertorEmail!=null&&!"".equals(opertorEmail)){
			if(email.length()>0){
				email.append(","+opertorEmail);
			}else{
				email.append(opertorEmail);
			}
		}

		return email.toString();
	}



	/**
	 * 新冠预约或客服未补录样本信息时邮件提醒
	 * @param title
	 * @param content
	 * @param attachment
	 */
	public void xinguan(String title,String content, File attachment) {
		//寇晓燕;郝庆凯;王耀萱;王伟伟;高司航;张静波;王冬
		String email=this.findEmail("SendEmailService-xinguan","xinguan-1");
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmails1(title, content, email,null);

	}
	
	/**
	 * 发送邮件给孟岳峰，昨日   新冠肺炎检测信息  用于上传政府接口  临时使用
	 * 
	 */
	public void sendxgzfExcel(String title,String content,File attachment){
		JavaMailQunFa se = new JavaMailQunFa(false);
		String jsr = "";
		jsr+=this.findEmail("SendEmailService-sendxgzfExcel","sendxgzfExcel-jsr");
		//jsr+=",guixuchen@Scisoon.cn";
		se.doSendHtmlEmail(title, content, jsr, attachment);
	}

}