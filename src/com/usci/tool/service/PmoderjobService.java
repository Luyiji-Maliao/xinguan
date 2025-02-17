package com.usci.tool.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.usci.norovirus.entity.Norovirus;
import com.usci.norovirus.service.NorovirusService;
import com.usci.system.service.SendEmailService;
import com.usci.system.service.SendUrlService;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.HibernateException;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.lims.core.utils.web.HttpClientUtil;
import com.lims.core.utils.web.SignUtil;
import com.lims.core.utils.web.Struts2Utils;
import com.lims.core.utils.web.WebPathUtil;

import com.usci.system.service.WorkdayCountService;



@Component
@Transactional(readOnly = true)
public class PmoderjobService {
    @Autowired
    private WorkdayCountService workdayCountService;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private SendUrlService sendUrlService;

    @Autowired
    private NorovirusService norovirusService;
    private Logger log = LoggerFactory.getLogger(PmoderjobService.class);



	public void sendXgExcel() {
		// TODO Auto-generated method stub
		Date date = new Date(new Date().getTime()-1000*60*60*24);//当天的前一天
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String statrTime = dateFormat.format(date) + " 04:00:00";
		String endTime = dateFormat.format(new Date())  + " 04:00:00";
		String sql1 = "select name,idcard,detectionResult,sampleNo,reportdate from norovirus where reportdate >= '"+statrTime+"' and reportdate <= '"+endTime+"' and checkProject like '%新型冠状病毒%'";
		System.out.println(sql1);
		List<Norovirus> norovirusList = norovirusService.findQudaoAll(sql1);
    	WebPathUtil webPathUtil = new WebPathUtil();
    	try {
			String uploadFileSavePath = webPathUtil.getRealPath("/documents");
			String res = norovirusService.reportDateExcel(norovirusList,uploadFileSavePath + "/新冠肺炎核酸检测信息-北京优迅医学检验实验室-孟岳峰-13718137762.xls");
			sendEmailService.sendxgzfExcel(dateFormat.format(date)+"核酸检测信息Excel表", "您好，"+res.replace("已导出", "已生成，请注意查收"), new File(uploadFileSavePath + "/新冠肺炎核酸检测信息-北京优迅医学检验实验室-孟岳峰-13718137762.xls"));
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.toString());
		}
    	
	}
    
}
