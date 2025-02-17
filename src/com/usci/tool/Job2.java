package com.usci.tool;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.service.SendEmailService;

import com.usci.system.service.WorkdayCountService;

import com.usci.tool.entity.BgiXxfx;
/***
 * 外来入库  定时发邮件
 * ***/
@Component("alertTask")
public class Job2{
	

}
