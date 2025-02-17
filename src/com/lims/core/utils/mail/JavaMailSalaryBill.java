package com.lims.core.utils.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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


/**
 * 邮件群发--产前审核邮件通知
 */
public class JavaMailSalaryBill {
    private MimeMessage message;
    private Session session;
    private Transport transport;

    private String mailHost = "";
    private String sender_username = "";
    private String sender_password = "";

    private Properties properties = new Properties();

    /**
     * 初始化方法
     */
    public JavaMailSalaryBill(boolean debug,String username,String password) {
    	properties.setProperty("mail.smtp.host", "smtp.exmail.qq.com");
		properties.setProperty("mail.sender.username", username);
		properties.setProperty("mail.sender.password", password);
		properties.setProperty("mail.smtp.auth","true");
		
        this.mailHost = properties.getProperty("mail.smtp.host");
		this.sender_username = properties.getProperty("mail.sender.username");
		this.sender_password = properties.getProperty("mail.sender.password");

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
    public String doSendHtmlEmail(String subject, String sendHtml, String receiveUser, File attachment,String fsr) {
    	String error="";
        try {
        	
            // 发件人
            InternetAddress from = new InternetAddress(MimeUtility.encodeWord(fsr)+" <"+sender_username+">");
            message.setFrom(from);

            // 收件人
            String[]  strs=receiveUser.split(",");
            InternetAddress[] to = new InternetAddress[strs.length];
            for(int i=0;i<strs.length;i++){
            	to[i]=new InternetAddress(strs[i]);
            }
            message.setRecipients(Message.RecipientType.TO, to);
           //设置抄送收件人
            //message.addRecipients(Message.RecipientType.CC,"2628633688@qq.com");
            //设置暗抄送人
            //message.addRecipients(Message.RecipientType.BCC,"weiyouyuan@luolai.com.cn");

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
        	error=receiveUser;
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return error;
    }


   /* @SuppressWarnings("unused")
	public static void main(String[] args) {
        JavaMailQunFa se = new JavaMailQunFa(true);
        File file = new File("f:\\bg\\1.png");
        se.doSendHtmlEmail("活动", "发件人： 聂梦肖<br/>失败环节： 预处理<br/>失败原因：系统邮件无需回复<br/>处理方式：xxxxxx<br/>", "2745554997@qq.com",null);
    }*/
    }
