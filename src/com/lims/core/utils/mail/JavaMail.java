package com.lims.core.utils.mail;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lims.core.utils.web.Struts2Utils;

/**
 * 单个邮件发送
 * 邮件发送（不带附件）
 * @author Administrator
 *
 */
public class JavaMail {
    private final static Logger log=LoggerFactory.getLogger(JavaMail.class);

    /**
     * Message对象将存储我们实际发送的电子邮件信息，
     * Message对象被作为一个MimeMessage对象来创建并且需要知道应当选择哪一个JavaMail session。
     */
    private MimeMessage message;
    
    /**
     * Session类代表JavaMail中的一个邮件会话。
     * 每一个基于JavaMail的应用程序至少有一个Session（可以有任意多的Session）。
     * 
     * JavaMail需要Properties来创建一个session对象。
     * 寻找"mail.smtp.host"    属性值就是发送邮件的主机
     * 寻找"mail.smtp.auth"    身份验证，目前免费邮件服务器都需要这一项
     */
    private Session session;
    
    /***
     * 邮件是既可以被发送也可以被受到。JavaMail使用了两个不同的类来完成这两个功能：Transport 和 Store。 
     * Transport 是用来发送信息的，而Store用来收信。对于这的教程我们只需要用到Transport对象。
     */
    private Transport transport;
    
    private String mailHost="";
    private String sender_username="";
    private String sender_password="";

    
    private Properties properties = new Properties();
    /*
     * 初始化方法
     */
    public JavaMail(boolean debug) {
        InputStream in = JavaMail.class.getResourceAsStream("MailServer.properties");
        try {
            properties.load(in);
            this.mailHost = properties.getProperty("mail.smtp.host");
            this.sender_username = properties.getProperty("mail.sender.username");
            this.sender_password = properties.getProperty("mail.sender.password");
        } catch (IOException e) {
           log.error("初始化：",e);
        }
        
        session = Session.getInstance(properties);
        session.setDebug(debug);//开启后有调试信息
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
     */
    public void doSendHtmlEmail(String subject, String sendHtml,
            String receiveUser) {
    	long startTime=System.currentTimeMillis();
    	receiveUser=Struts2Utils.trimStartEnd(receiveUser, ",", ",");
        try {
            // 发件人
            //InternetAddress from = new InternetAddress(sender_username);
            // 下面这个是设置发送人的Nick name
            InternetAddress from = new InternetAddress(MimeUtility.encodeWord("优迅医学LIMS")+" <"+sender_username+">");
            message.setFrom(from);
            
            // 收件人
            // 收件人
            String[]  strs=receiveUser.split(",");
            InternetAddress[] to = new InternetAddress[strs.length];
            for(int i=0;i<strs.length;i++){
            	to[i]=new InternetAddress(strs[i]);
            }
           
            message.setRecipients(Message.RecipientType.TO, to);//还可以有CC、BCC
            
            // 邮件主题
            message.setSubject(subject);
            
            String content = sendHtml.toString();
            // 邮件内容,也可以使纯文本"text/plain"
            message.setContent(content, "text/html;charset=UTF-8");
            
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
        	log.error("群发邮件异常："+"耗时："+methodTime+"，操作人："+opertorName+",主题："+subject+",收件人："+receiveUser,e);

        }finally {
            if(transport!=null){
                try {
                    transport.close();
                } catch (MessagingException e) {
                    log.error("transport关闭异常",e);
                }
            }
        }
    }

    public static void main(String[] args) {/*
    		 JavaMail se = new JavaMail(false);
    	        se.doSendHtmlEmail("测试邮件", "系统测试邮件无需回复1", "2745554997@qq.com");*/
    	
    	/*String result="报告发送失败!请稍后再试!<br/>";
        
        String names="yanxiazhou@scisoon.cn,mengxiaonie@scisoon.cn";
        JavaMail jm=new JavaMail(false);
        jm.doSendHtmlEmail("报告发送失败-测试", result, names);*/
    		
    }
}