package com.usci.system.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.utils.mail.JavaMailQunFa;
import com.usci.system.entity.User;

@Component
@Transactional(readOnly=true)
public class ZdxmglSendEmailService {
	@Autowired
	private UserService userService;
	
	/**
	 * 重大项目管理微信推送
	 * @throws UnsupportedEncodingException 
	 * */
	public void projectManager(String jsr,String title,String danhao,String jindu,String times,String inputuser,String remark,String tables) throws UnsupportedEncodingException{
		/*## this.pushPhpMessage(jsr, title, danhao, jindu, times, inputuser, remark, tables); 
		//this.pushPhpMessage("15101093727,15010791899", title, danhao, jindu, times, inputuser, remark, tables); 
		//重大项目管理这个 正式系统  打开  
		
		 ##*/
	}
	/**
	 * 重大项目管理发送邮件
	 * @throws UnsupportedEncodingException 
	 * */
	public void projectManagerEmail(String title,String remark,String recievers) throws UnsupportedEncodingException{
		/*##    StringBuilder email=new StringBuilder();
		String[]m=recievers.split(",");
		
		for(int i =0;i<m.length;i++){
			if(m[i]!=null && !m[i].equals("") ){
				String em = this.getEmailByname(m[i].trim());				
				if(em!=null && !em.equals(",")){
					if(i==0){
						email.append(em);
					}else{
						email.append(","+em);
					}
					
				}
			}
			
		}
		
		JavaMailQunFa se = new JavaMailQunFa(false);
		se.doSendHtmlEmail(title,remark,email.toString(),null);
		//se.doSendHtmlEmail(title,remark,"chunkezhang@scisoon.cn,xiaoyuzhang@scisoon.cn",null); 
		//重大项目管理这个 正式系统  打开  
		 	##*/
	}
	
	/**
	 * 根据人名查询手机号
	 * **/
	public String getEmailByname(String name){
			User  u=userService.findByName(name);
		if(u!=null&&u.getEmail()!=null&&!"".equals(u.getEmail())){
			return u.getEmail();
		}else{
			return ",";
		}
	}
	/**
	 * PHP推送消息
	 * @throws UnsupportedEncodingException
	 * @param phone   手机号(以英文逗号分隔,结尾必须有英文逗号   即 186xxxxxxxx,   或    186xxxxxxxx,155xxxxxxxx,)
	 * @param title   标题	(样本入库失败   或    寄送样无此样本    等)
	 * @param danhao  3主要单号(样本编号   批次编号  等标识信息)
	 * @param jindu   主要内容 (入库失败   等)
	 * @param times   时间
	 * @param inputuser 触发人
	 * @param remark  详细内容(肿瘤样本入库失败， 检测项目为肿瘤检测， 肿瘤寄送样无此样本，\n请知悉！     此类)
	 * @param tables  (详细内容较多  如表格等   此处放表格)
	 * **/
	public void pushPhpMessage(String phone,String title,String danhao,String jindu,String times,String inputuser,String remark,String tables) throws UnsupportedEncodingException{
		/*##		 String content="";
		 JSONArray json = new JSONArray();
		 JSONObject jo = new JSONObject();
		 jo.put("phone",phone);  			//接收人手机号    结尾有英文逗号
		 jo.put("title",title);				//标题
		 jo.put("danhao",danhao);			//主要单号(样本编号等)
		 jo.put("jindu",jindu);				//主要内容
		 jo.put("times",times);				//时间
		 jo.put("inputuser",inputuser);		//触发人
		 jo.put("remark",remark);			//详细内容
		 jo.put("tables",tables);			//表格等内容
		 
		 json.put(jo);
		 content="post_data_vals="+URLEncoder.encode(json.toString(), "UTF-8");
		URL url = null;
		try {
			// 创建连接
			//url = new URL("http://www.scisoonbbs.com/limsceshi/robot.php/Notice/emails");    		//测试
				url = new URL("http://www.scisoonbbs.com/limszhengshi/robot.php/Notice/emails");		//正式
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			conn.connect();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(content);
			out.flush();
			out.close();
			// 获取响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String lines;
			StringBuilder sbs = new StringBuilder();
			while((lines = reader.readLine()) != null){
				lines = new String(lines.getBytes(), "utf-8");
				sbs.append(lines);
			}
			reader.close();
			if(!"ok".equals(sbs.toString())){//错误
			//	JavaMailQunFa se = new JavaMailQunFa(false);
			//	se.doSendHtmlEmail("推送微信端异常"+Struts2Utils.getStringDate(new Date()),"返回值内容:"+sbs.toString(),"mengxiaonie@scisoon.cn",null);
			}
			
			// 关闭连接
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		##*/
	}
	
}
