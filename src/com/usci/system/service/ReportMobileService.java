package com.usci.system.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



@Component
@Transactional(readOnly=true)
public class ReportMobileService {
	/**
	 * 产前报告黎小玲电话
	 * @return
	 */
	public String reportMobile(){
		return "18701319883";//测试
		//return "17772422968"; //正式
	}
	
	
	
}
