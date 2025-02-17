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
public class JavaMailReturnedValue {
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
    public JavaMailReturnedValue(boolean debug) {
        InputStream in = JavaMailQunFa.class.getResourceAsStream("MailServer.properties");
        try {
            properties.load(in);
            this.mailHost = properties.getProperty("mail.smtp.host");
            this.sender_username = properties.getProperty("mail.sender.username");
            this.sender_password = properties.getProperty("mail.sender.password");
        } catch (IOException e) {
            e.printStackTrace();
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
     * @throws UnsupportedEncodingException 
     * @throws MessagingException 
     */
    public void doSendHtmlEmail(String subject, String sendHtml, String receiveUser, File attachment) throws UnsupportedEncodingException, MessagingException {
     
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
        
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        JavaMailQunFa se = new JavaMailQunFa(true);
        File file = new File("f:\\bg\\1.png");
        se.doSendHtmlEmail("活动", "发件人： 聂梦肖<br/>失败环节： 预处理<br/>失败原因：系统邮件无需回复<br/>处理方式：xxxxxx<br/>", "2745554997@qq.com",null);
    }
}