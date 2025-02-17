package com.usci.system.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.usci.tool.officialaccounts.service.OfficialAccountsPushMsgUtilService;
import com.usci.system.dao.InterceptRequestDao;
import com.usci.system.entity.InterceptRequest;

@Component
@Transactional(readOnly = true)
public class InterceptRequestService {
	@Autowired
	private InterceptRequestDao interceptRequestDao;
	
	@Autowired
	private OfficialAccountsPushMsgUtilService accountsPushMsgUtilService;
	@Transactional
	public void save(InterceptRequest entity) {
		interceptRequestDao.save(entity);
	}

	/**
	 * 记录second秒内访问limit次 的IP，每隔timeInterval秒推送一次此ip信息
	 * 
	 * @param accessIP
	 *            访问IP
	 * @param accessURL
	 *            访问url
	 * @param second
	 *            推送标准（单位秒）
	 * @param limit
	 *            获取几秒内的访问次数
	 * @param timeInterval
	 *            推送间隔时间（单位秒）
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	public boolean WarnMessage(InterceptRequest entity,String accessIP, String accessURL, int second,
			int limit, int timeInterval) throws UnsupportedEncodingException {
		boolean flag=false;
		List<Object[]> oi = interceptRequestDao.queryBySql("SELECT (TIMESTAMPDIFF(SECOND,MAX(accessDate),NOW(3))-"
						+ timeInterval
						+ ") FROM sys_interceptrequest "
						+ "WHERE accessIP = '"
						+ accessIP
						+ "' AND accessURL = '"
						+ accessURL
						+ "' AND pushstate = '是'");

		Object o=oi.get(0);

		// 判断是否满足时间间隔推送条件  timeInterval
		if (oi.get(0) == null|| (oi.get(0) != null && Integer.parseInt(o.toString())>0)) {
			String sql2 = "";

			if (oi.get(0)!= null) {
				sql2 = "AND accessDate > (SELECT MAX(accessDate) FROM sys_interceptrequest WHERE accessIP='"
						+ accessIP
						+ "' AND accessURL = '"
						+ accessURL
						+ "' AND pushstate = '是' ) ";

			}

			// 获取最新limit条数据
			List<Object> li = interceptRequestDao
					.queryBySql("SELECT	(UNIX_TIMESTAMP(accessDate)*1000) FROM	sys_interceptrequest WHERE  accessIP='"
							+ accessIP
							+ "' AND accessURL = '"
							+ accessURL
							+ "' "
							+ sql2
							+ "ORDER BY accessDate DESC LIMIT "
							+ limit);

			if (li.size() >= (limit - 1)) {

				String unix=li.get((li.size() - 1)).toString();

				long l=Long.parseLong(unix.substring(0, unix.length()-4));
				//用当前时间(entity.getAccessDate())和最后一条比较，如果小于设定的second，则发推送信息
				if ((entity.getAccessDate().getTime() - l) <= (second * 1000)) {
					flag=true;
					SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
					StringBuilder table=new StringBuilder();
					table.append("<br/>IP:"+entity.getAccessIP());
					table.append("<br/>账号："+entity.getAccessName());
					table.append("<br/>URL:"+entity.getAccessURL());

				}
			}
		}

		return flag;
	}

}
