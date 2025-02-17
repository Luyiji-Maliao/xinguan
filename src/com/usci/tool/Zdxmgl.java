package com.usci.tool;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.service.UserService;
import com.usci.system.service.ZdxmglSendEmailService;
/***
 * 重大项目管理  定时发邮件
 * ***/
@Component("zdxmglalertTask")
public class Zdxmgl {

}
