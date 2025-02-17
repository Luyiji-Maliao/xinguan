package com.usci.tool;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.lims.core.utils.web.Struts2Utils;

import com.usci.tool.service.PmoderjobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/***
 * 项目管理下单，肿瘤，生育线，体检自动下单并发送邮件
 * ***/
@Component("pmoderTask")
public class PmoderJob {

	@Autowired
	private PmoderjobService pmoderjobService;
	private static final Logger log=LoggerFactory.getLogger(PmoderJob.class);

	//发送邮件给孟岳峰，新冠上传政府excel表（暂时使用）
	@Scheduled(cron="0 30 04 * * ?")
	public void putXgExcel(){
		log.info("发送新冠上传Excel开始");
		pmoderjobService.sendXgExcel();
		log.info("发送新冠上传Excel结束");
	}
	

}
