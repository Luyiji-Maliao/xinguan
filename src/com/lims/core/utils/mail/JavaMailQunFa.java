package com.lims.core.utils.mail;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.entity.User;
/**
 * 邮件群发
 */
public class JavaMailQunFa {
    private MimeMessage message;
    private Session session;
    private Transport transport;
    private String mailHost = "";
    private String sender_username = "";
    private String sender_password = "";
    private Properties properties = new Properties();
    private final static Logger log=LoggerFactory.getLogger(JavaMailQunFa.class);
    /**
     * 初始化方法
     */
    
    public JavaMailQunFa(boolean debug) {
        InputStream in = JavaMailQunFa.class.getResourceAsStream("MailServer.properties");
        try {
            properties.load(in);
            this.mailHost = properties.getProperty("mail.smtp.host");
            this.sender_username = properties.getProperty("mail.sender.username");
            this.sender_password = properties.getProperty("mail.sender.password");
        } catch (IOException e) {
            log.error("邮件初始化异常",e);
        }
        session = Session.getInstance(properties);
        session.setDebug(debug);// 开启后有调试信息
        message = new MimeMessage(session);
    }

    /**
     * 发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param sendHtml
     *            邮件内容
     * @param receiveUser
     *            收件人地址
     * @param attachment
     *            附件
     */
    public void doSendHtmlEmail(String subject, String sendHtml, String receiveUser, File attachment) {
    	long startTime=System.currentTimeMillis();
    	//去除前后逗号
    	receiveUser=Struts2Utils.trimStartEnd(receiveUser, ",", ",").replace(",,", ",");
    	try {
        	sendHtml += "。</br>请勿回复此邮件，如有问题请发邮件至kefu@scisoon.cn，会有客服人员与您联系。";
            // 发件人
            InternetAddress from = new InternetAddress(MimeUtility.encodeWord("优迅医学LIMS")+" <"+sender_username+">");
            message.setFrom(from);
            // 收件人
            String[]  strs=receiveUser.split(",");
            InternetAddress[] to = new InternetAddress[strs.length];
            for(int i=0;i<strs.length;i++){
            	to[i]=new InternetAddress(strs[i]);
            }
            message.setRecipients(Message.RecipientType.TO, to);
            // 邮件主题
            message.setSubject(subject);
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
        	long methodTime=System.currentTimeMillis()-startTime;
        	String opertorName=Struts2Utils.getUser()!=null?Struts2Utils.getUser().getName():"";
        	String file= attachment==null?"，无附件":"，有附件";
            log.error("群发邮件异常："+"耗时："+methodTime+"，操作人："+opertorName+",主题："+subject+",收件人："+receiveUser+"是否有附件："+file,e);
            
            String result="主题："+subject+"发送失败!请稍后再试!<br/>";
            result+="sendHtml:"+sendHtml+"<br/>";
            result+="receiveUser:"+receiveUser+"<br/>";
//            String names=Struts2Utils.getSessionUser().getEmail().trim()+","+"mengxiaonie@scisoon.cn";
            String names = "schedule@scisoon.cn,yongxiaxu@scisoon.cn,hongcainie@scisoon.cn,mengxiaonie@scisoon.cn";
            JavaMail jm=new JavaMail(false);
            jm.doSendHtmlEmail("邮件发送失败", result, names);
            
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                	log.error("Transport关闭异常",e);
                }
            }
        }
    }

    /**
     * 发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param sendHtml
     *            邮件内容
     * @param receiveUser
     *            收件人地址
     * @param receiveMoreUser
     *            抄送人地址
     * @param attachment
     *            多个附件
     * @param rename
     *            附件命名
     */
    public String doSendHtmlEmails(String subject, String sendHtml, String receiveUser,String receiveMoreUser,List<File> files,List<String> rename) {
    	long startTime=System.currentTimeMillis();
    	receiveUser=Struts2Utils.trimStartEnd(receiveUser, ",", ",").replace(",,", ",");
    	receiveMoreUser=Struts2Utils.trimStartEnd(receiveMoreUser, ",", ",").replace(",,", ",");
    	String lastresult="";
        try {
        	sendHtml += "。</br>请勿回复此邮件，如有问题请发邮件至kefu@scisoon.cn，会有客服人员与您联系。";
            // 发件人
            InternetAddress from = new InternetAddress(MimeUtility.encodeWord("优迅医学LIMS")+" <"+sender_username+">");
            message.setFrom(from);
            // 收件人
            String[]  strs=receiveUser.split(",");
            InternetAddress[] to = new InternetAddress[strs.length];
            for(int i=0;i<strs.length;i++){
            	to[i]=new InternetAddress(strs[i]);
            }
            message.setRecipients(Message.RecipientType.TO , to);
           //设置抄送收件人
            message.addRecipients(Message.RecipientType.CC ,receiveMoreUser);
            // 邮件主题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            //批量添加附件
            if(files.size()>0){
            	for (int i = 0; i < files.size(); i++) {
            		BodyPart attachmentBodyPart = new MimeBodyPart();
            		File attachment = files.get(i);
            		if(attachment != null){
	                    DataSource source = new FileDataSource(attachment);
	                    attachmentBodyPart.setDataHandler(new DataHandler(source));
	                    attachmentBodyPart.setFileName(MimeUtility.encodeWord(rename.get(i)));
	                    multipart.addBodyPart(attachmentBodyPart);
            		}
				}
            }
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
        	long methodTime=System.currentTimeMillis()-startTime;
        	String opertorName=Struts2Utils.getUser()!=null?Struts2Utils.getUser().getName():"";
        	String file= (files.size()==0?"无附件":"，有附件");
            log.error("群发邮件异常："+"耗时："+methodTime+"，操作人："+opertorName+",主题："+subject+",收件人："+receiveUser+"转发人："+receiveMoreUser+"是否有附件："+file,e);

            lastresult="1";
            String error="";
            if(rename.size()==1){
            	for (int i = 0; i < rename.size(); i++) {
    				error+=rename.get(i);
    			}
            }else if(rename.size()>1){
            	for (int i = 0; i < rename.size(); i++) {
            		if(rename.get(i).contains("-")&&rename.get(i).split("-").length>2){
            			error=error+rename.get(i).split("-")[2]+"/";
            		}else{
            			error+=rename.get(i)+"/";
            		}   				
    			}           	
            }
            String result="报告"+error+"发送失败!请稍后再试!<br/>";
            result+="主题:"+subject+"<br/>";
            result+="内人:"+sendHtml+"<br/>";
            result+="接收人:"+receiveUser+"<br/>";
            result+="抄送人:"+receiveMoreUser+"<br/>";
            String names=(Struts2Utils.getUser()!=null?Struts2Utils.getUser().getEmail().trim()+",":"")+"mengxiaonie@scisoon.cn";
            JavaMail jm=new JavaMail(false);
            jm.doSendHtmlEmail("邮件发送失败", result, names);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    log.error("transport关闭异常",e);
                }
            }
        }
        return lastresult;
    }
    /**
     * 发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param sendHtml
     *            邮件内容
     * @param receiveUser
     *            收件人地址
     * @param receiveMoreUser
     *            抄送人地址

     */
    public String doSendHtmlEmails1(String subject, String sendHtml, String receiveUser,String receiveMoreUser) {
    	receiveUser=Struts2Utils.trimStartEnd(receiveUser, ",", ",").replace(",,", ",");
    	receiveMoreUser=Struts2Utils.trimStartEnd(receiveMoreUser, ",", ",").replace(",,", ",");
    	long startTime=System.currentTimeMillis();
    	String lastresult="";
        try {
        	sendHtml += "。</br>请勿回复此邮件，如有问题请发邮件至kefu@scisoon.cn，会有客服人员与您联系。";
            // 发件人
            InternetAddress from = new InternetAddress(MimeUtility.encodeWord("优迅医学LIMS")+" <"+sender_username+">");
            message.setFrom(from);

            // 收件人
            String[]  strs=receiveUser.split(",");
            InternetAddress[] to = new InternetAddress[strs.length];
            for(int i=0;i<strs.length;i++){
            	to[i]=new InternetAddress(strs[i]);
            }
            message.setRecipients(Message.RecipientType.TO , to);
           //设置抄送收件人
            message.addRecipients(Message.RecipientType.CC ,receiveMoreUser);
            // 邮件主题
            message.setSubject(subject);
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            //批量添加附件
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
        	long methodTime=System.currentTimeMillis()-startTime;
        	String opertorName=Struts2Utils.getUser()!=null?Struts2Utils.getUser().getName():"";

        	
        	log.error("群发邮件异常："+"耗时："+methodTime+"，操作人："+opertorName+",主题："+subject+",收件人："+receiveUser+",转发人："+receiveMoreUser,e);

            lastresult="1";
            String error="";
            String result="报告"+error+"发送失败!请稍后再试!<br/>";
            result+="主题:"+subject+"<br/>";
            result+="内容:"+sendHtml+"<br/>";
            result+="接收人:"+receiveUser+"<br/>";
            result+="抄送人:"+receiveMoreUser+"<br/>";
            String names="mengxiaonie@scisoon.cn";
            if(Struts2Utils.getUser()!=null){
            	names = ","+Struts2Utils.getUser().getEmail().trim();
            }
            JavaMail jm=new JavaMail(false);
            jm.doSendHtmlEmail("邮件发送失败", result, names);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    log.error("邮件资源关闭异常",e);
                }
            }
        }
        return lastresult;
    }
    /**
     * 发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param sendHtml
     *            邮件内容
     * @param receiveUser
     *            收件人地址
     * @param attachment
     *            附件
     */
    public void doSendHtmlEmail1(String subject, String sendHtml, String receiveUser, File attachment) {
    	long startTime=System.currentTimeMillis(); 
    	receiveUser=Struts2Utils.trimStartEnd(receiveUser, ",", ",").replace(",,", ",");
    	try {
            // 发件人
            InternetAddress from = new InternetAddress(MimeUtility.encodeWord("优迅医学LIMS")+" <"+sender_username+">");
            message.setFrom(from);
            // 收件人
            String[]  strs=receiveUser.split(",");
            InternetAddress[] to = new InternetAddress[strs.length];
            for(int i=0;i<strs.length;i++){
            	to[i]=new InternetAddress(strs[i]);
            }
            message.setRecipients(Message.RecipientType.TO, to);
            // 邮件主题
            message.setSubject(subject);
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
        	long methodTime=System.currentTimeMillis()-startTime;
        	User user = Struts2Utils.getUser();
        	String opertorName=user!=null?user.getName():"";
        	String file= attachment==null?"无附件":"，有附件";
            log.error("群发邮件异常："+"耗时："+methodTime+"，操作人："+opertorName+",主题："+subject+",收件人："+receiveUser+"是否有附件："+file,e);

        	       	
        	String result=subject+"发送失败!请稍后再试!<br/>";
            result+="sendHtml:"+sendHtml+"<br/>";
            result+="receiveUser:"+receiveUser+"<br/>";
            String names="mengxiaonie@scisoon.cn";
            if(Struts2Utils.getUser()!=null){
            	names = ","+Struts2Utils.getUser().getEmail().trim();
            }
            JavaMail jm=new JavaMail(false);
            jm.doSendHtmlEmail("邮件发送失败", result, names);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                	log.error("邮件资源关闭异常",e);
                }
            }
        }
    }
    public static void main(String[] args) {
        JavaMailQunFa se = new JavaMailQunFa(true);
        File file = new File("f:\\sjd\\fj.zip");
        se.doSendHtmlEmail("活动", "发件人： 聂梦肖<br/>失败环节： 预处理<br/>失败原因：系统邮件无需回复<br/>处理方式：xxxxxx<br/>", "mengxiaonie@scisoon.cn",file);
    }
}