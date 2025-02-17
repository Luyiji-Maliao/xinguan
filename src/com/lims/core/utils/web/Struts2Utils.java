package com.lims.core.utils.web;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.lims.core.utils.mail.JavaMailQunFa;
import com.usci.system.entity.User;

/**
 * Struts2工具类.
 * 
 * 实现获取Request/Response/Session与绕过jsp/freemaker直接输出文本的简化函数.
 * 
 * @author YCCN
 */
public class Struts2Utils {

	// -- header 常量定义 --//
	private static final String HEADER_ENCODING = "encoding";
	private static final String HEADER_NOCACHE = "no-cache";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final boolean DEFAULT_NOCACHE = true;
	private static final Logger log = LoggerFactory
			.getLogger(Struts2Utils.class);
	/**
	 * 整数正则
	 */
	private static final Pattern PATTERN_INTEGER = Pattern
			.compile("^[-\\+]?[\\d]*$");
	/**
	 * 正整数正则
	 */
	private static final Pattern PATTERN_POSITIVE_INTEGER = Pattern
			.compile("[0-9]*");
	private static ObjectMapper mapper = new ObjectMapper();

	// -- 取得Request/Response/Session的简化函数 --//
	/**
	 * 取得HttpSession的简化函数.
	 */
	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	/**
	 * 取得HttpSession的简化函数.
	 */
	public static HttpSession getSession(boolean isNew) {
		return ServletActionContext.getRequest().getSession(isNew);
	}

	/**
	 * 取得HttpSession中Attribute的简化函数.
	 */
	public static Object getSessionAttribute(String name) {
		HttpSession session = getSession(false);
		return (session != null ? session.getAttribute(name) : null);
	}

	/**
	 * 取得HttpRequest的简化函数.
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 获取用户信息信息
	 */
	public static User getUser() {
		try {
			User u = (User) ServletActionContext.getRequest().getSession()
					.getAttribute("loginUser");
			return u;
		} catch (NullPointerException n) {
			return null;
		} catch (Exception e) {
			log.error("获取用户信息信息异常", e);
		}
		return null;
	}

	/**
	 * 取得HttpRequest中Parameter的简化方法.
	 */
	public static String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	/**
	 * 取得HttpResponse的简化函数.
	 */
	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	// -- 绕过jsp/freemaker直接输出文本的函数 --//
	/**
	 * 直接输出内容的简便函数.
	 * 
	 * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain",
	 * "hello", "no-cache:false"); render("text/plain", "hello", "encoding:GBK",
	 * "no-cache:false");
	 * 
	 * @param headers
	 *            可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
	 */
	public static void render(final String contentType, final String content,
			final String... headers) {
		HttpServletResponse response = initResponseHeader(contentType, headers);
		try {
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 直接输出文本.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderText(final String text, final String... headers) {
		render(ServletUtils.TEXT_TYPE, text, headers);
	}

	/**
	 * 直接输出HTML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml(final String html, final String... headers) {
		render(ServletUtils.HTML_TYPE, html, headers);
	}

	/**
	 * 直接输出XML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderXml(final String xml, final String... headers) {
		render(ServletUtils.XML_TYPE, xml, headers);
	}

	/**
	 * 直接输出JSON.
	 * 
	 * @param jsonString
	 *            json字符串.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(final String jsonString,
			final String... headers) {
		render(ServletUtils.JSON_TYPE, jsonString, headers);
	}

	/**
	 * 直接输出JSON,使用Json转换Java对象.
	 * 
	 * @param data
	 *            可以是List<POJO>, POJO[], POJO, 也可以Map名值对.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(final Object data, final String... headers) {
		HttpServletResponse response = initResponseHeader(
				ServletUtils.JSON_TYPE, headers);
		try {

			mapper.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 直接输出支持跨域Mashup的JSONP.
	 * 
	 * @param callbackName
	 *            callback函数名.
	 * @param object
	 *            Java对象,可以是List<POJO>, POJO[], POJO ,也可以Map名值对, 将被转化为json字符串.
	 */
	public static void renderJsonp(final String callbackName,
			final Object object, final String... headers) {
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}

		String result = new StringBuilder().append(callbackName).append("(")
				.append(jsonString).append(");").toString();

		// 渲染Content-Type为javascript的返回内容,输出结果为javascript语句,
		// 如callback197("{html:'Hello World!!!'}");
		render(ServletUtils.JS_TYPE, result, headers);
	}

	/**
	 * 分析并设置contentType与headers.
	 */
	private static HttpServletResponse initResponseHeader(
			final String contentType, final String... headers) {
		// 分析headers参数
		String encoding = DEFAULT_ENCODING;
		boolean noCache = DEFAULT_NOCACHE;
		for (String header : headers) {
			String headerName = StringUtils.substringBefore(header, ":");
			String headerValue = StringUtils.substringAfter(header, ":");

			if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
				encoding = headerValue;
			} else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
				noCache = Boolean.parseBoolean(headerValue);
			} else {
				throw new IllegalArgumentException(headerName
						+ "不是一个合法的header类型");
			}
		}

		HttpServletResponse response = ServletActionContext.getResponse();

		// 设置headers参数
		String fullContentType = contentType + ";charset=" + encoding;
		response.setContentType(fullContentType);
		if (noCache) {
			ServletUtils.setDisableCacheHeader(response);
		}

		return response;
	}

	/**
	 * 补充，页面操作权限
	 */
	public String operationAuthorization() {
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public static String getStringYeas(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(date);
	}

	/**
	 * 
	 * @return
	 */
	public static String getStringMonth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(date);
	}

	/**
	 * 
	 * @return
	 */
	public static String getStringDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(date);
	}

	/**
	 * yyyyMMddHHmmss 格式化日期(短信/随机数) 列yyyy-MM-dd HH：mm：ss：2015-02-01 02:01:01
	 * yyyyMMddHHmmss 20150201020101
	 */
	public static String getStringDatehmss(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	/**
	 * 格式化日期(时间)
	 */
	public static String getStringDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 格式化日期yyyy-MM-dd
	 */
	public static String getymdDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 格式化日期yyyy年MM月dd日
	 */
	public static String getymdhz(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		return sdf.format(date);
	}

	/**
	 * 格式化日期
	 */
	public static String getymdDate(Date date, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		return sdf.format(date);
	}

	/**
	 * 格式化日期yyyyMMdd
	 */
	public static String getymd(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	/**
	 * 字符串转换成时间
	 */

	public static Date getDateByString(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("字符串转换成时间异常,操作人：" + opertorName + "\n异常信息：", e);
		}
		return null;
	}

	/**
	 * 时间差计算
	 */
	public static long getTimeDifference(Date startDate, Date endDate) {
		long timesDis = Math.abs(startDate.getTime() - endDate.getTime());
		long day = timesDis / (1000 * 60 * 60 * 24);
		long hour = timesDis / (1000 * 60 * 60) - day * 24;
		long min = timesDis / (1000 * 60) - day * 24 * 60 - hour * 60;
		long sec = timesDis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min
				* 60;
		long[] dis = { day, hour, min, sec };
		long days = dis[0];
		if (dis[1] > 0 || dis[2] > 0 || dis[3] > 0) {
			days += 1;
		}
		return days;
	}

	/**
	 * 字符串转换成时间(y-m-d h:m:s)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateByStringhms(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("字符串转换成时间(y-m-d h:m:s)异常,操作人：" + opertorName + "\n异常信息：",
					e);
		}
		return null;
	}

	/**
	 * 两时间之差（收-发）
	 * 
	 * @throws ParseException
	 */
	public static long getmistime(String arrivedate, String shipdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return (sdf.parse(arrivedate).getTime() - sdf.parse(shipdate)
					.getTime())
					/ (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("两时间之差（收-发）异常,操作人：" + opertorName + "\n异常信息：", e);
		}
		return -100;
	}

	/**
	 * 两时间之差详细（收-发）
	 * 
	 * @throws ParseException
	 */
	public static String getmistimedetail(String arrivedate, String shipdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("0.000");// 格式化小数，不足的补0
		try {
			float f = (float) (sdf.parse(arrivedate).getTime() - sdf.parse(
					shipdate).getTime())
					/ (24 * 60 * 60 * 1000);
			return df.format(f);
		} catch (ParseException e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("两时间之差（收-发）异常,操作人：" + opertorName + "\n异常信息：", e);
		}
		return "0";
	}

	/**
	 * 将一个json字串转为list
	 * 
	 * @return
	 */

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public static <T> List<T> conver(String answer, Class c) {
		if (answer == null || answer.equals("")) {
			return new ArrayList<T>();
		}
		JSONArray jsonArray = JSONArray.fromObject(answer);
		List<T> list = (List) JSONArray.toCollection(jsonArray, c);

		return list;
	}

	/**
	 * 将Json对象转换成Map
	 * 
	 * json对象
	 * 
	 * @return Map对象
	 * @throws JSONException
	 */
	@SuppressWarnings( { "rawtypes", "unchecked" })
	public static Map toMap(String jsonString) throws JSONException {

		JSONObject jsonObject = new JSONObject(jsonString);

		Map result = new HashMap();
		Iterator iterator = jsonObject.keys();
		String key = null;
		String value = null;

		while (iterator.hasNext()) {

			key = (String) iterator.next();
			value = jsonObject.getString(key);
			if (value == null) {
				value = "";
			}
			result.put(key, value);

		}
		return result;

	}

	/**
	 * 登录ip
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getIpAddress() throws UnknownHostException {
		InetAddress address = InetAddress.getLocalHost();

		return address.getHostAddress();
	}

	/**
	 * 获取外网IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			if (ip.indexOf(",") != -1) {
				ip = ip.split(",")[0];
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * mac（物理地址）
	 * 
	 * @param ip
	 * @return
	 */
	public static String getMACAddress(String ip) {
		String macAddress = "";
		BufferedReader inputs = null;
		try {
			String scancmd = "nbtstat -A ";
			File file = new File("C:\\Windows\\SysWOW64");
			if (file.exists()) {
				scancmd = "c:\\Windows\\sysnative\\nbtstat.exe -A ";
			}
			Process p = Runtime.getRuntime().exec(scancmd + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream(),
					"gbk");
			inputs = new BufferedReader(ir);
			String line;
			while ((line = inputs.readLine()) != null) {
				if (line.indexOf("MAC Address") > 0) {
					macAddress = line.substring(
							line.indexOf("MAC Address") + 14, line.length());
				}
				if (line.indexOf("MAC 地址") > 0) {
					macAddress = line.substring(
							line.indexOf("MAC Address") + 14, line.length());
				}
			}
		} catch (IOException e) {
			log.error("getMACAddress：", e);
		}
		return macAddress;
	}

	/**
	 * 修改properties键值
	 * 
	 * @param p
	 * @throws IOException
	 */
	public static void changeproperty(String p) throws IOException {

		Properties prop = new Properties();// 属性集合对象
		FileInputStream fis;
		try {
			fis = new FileInputStream("WebRoot/WEB-INF/log4j.properties");
			prop.load(fis);// 将属性文件流装载到Properties对象中
			fis.close();// 关闭流

			// 获取属性值，sitename已在文件中定义

			// 获取属性值，country未在文件中定义，将在此程序中返回一个默认值，但并不修改属性文件

			// 修改sitename的属性值
			prop.setProperty("log4j.appender.r.File",
					"${webName.root}/WEB-INF/log/" + p + ".log");
			// 文件输出流
			FileOutputStream fos = new FileOutputStream(
					"WebRoot/WEB-INF/log4j.properties");
			// 将Properties集合保存到流中
			prop.store(fos, "Copyright (c) Boxcode Studio");
			fos.close();// 关闭流

		} catch (FileNotFoundException e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("修改properties键值异常,操作人：" + opertorName + "\n异常信息：", e);
		}// 属性文件输入流

	}

	public static User getSessionUser() {
		return (User) getSession().getAttribute("loginUser");
	}

	/**
	 * 获取文件根目录
	 */
	private static String path = null;

	public static void setPath(String path1) {
		path = path1;
	}

	public static String getPath() {
		return path;
	}

	/**
	 * json
	 */
	public static void sendDataJson(String jsonStr, HttpServletResponse response) {
		try {
			jsonStr = jsonStr.replace("\n", " ").replace("\r", " ").replace(
					"\t", " ");

			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jsonStr);
			response.getWriter().flush();
		} catch (Exception e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("json异常,操作人：" + opertorName + "\n异常信息：", e);
		}
	}

	/**
	 * 实体复制
	 */
	public static void Copy(Object source, Object dest) throws Exception {
		// 获取属性
		BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(),
				java.lang.Object.class);
		PropertyDescriptor[] sourceProperty = sourceBean
				.getPropertyDescriptors();

		BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(),
				java.lang.Object.class);
		PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();

		try {
			for (int i = 0; i < sourceProperty.length; i++) {

				for (int j = 0; j < destProperty.length; j++) {

					if (sourceProperty[i].getName().equals(
							destProperty[j].getName())) {
						// 调用source的getter方法和dest的setter方法
						destProperty[j].getWriteMethod().invoke(
								dest,
								sourceProperty[i].getReadMethod()
										.invoke(source));
						break;
					}
				}
			}
		} catch (Exception e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("属性复制异常,操作人：" + opertorName + "\n异常信息：", e);
		}
	}

	/**
	 * 输入参数：CorpID-帐号，Pwd-密码，Mobile-发送手机号(多个号码以逗号分隔)，Content-发送内容，Cell-扩展号(
	 * 可为空或必须是4位以下的数字
	 * ），SendTime-定时发送时间(固定14位长度字符串，比如：20060912152435代表2006年9月12日15时24)
	 * 输出参数：大于等于0的数字
	 * ，发送成功（得到大于等于0的数字、作为取报告的id）；-1、帐号未注册；-2、其他错误；-3、密码错误；-4、手机号格式不对
	 * ；-5、余额不足；-6、定时发送时间不是有效的时间格式；-7、提交信息末尾未加签名，请添加中文企业签名【 】；
	 * -8、发送内容需在1到500个字之间；-9、 发送号码为空
	 * http://inolink.com/WS/BatchSend.aspx?CorpID=
	 * *&Pwd=*&Mobile=*&Content=*&Cell=*&SendTime=*
	 * 
	 * @param Mobile
	 * @param Content
	 * @param send_time
	 * @return
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public static int sendSMS(String prefixuUrl,String Mobile, String Content, String send_time)
			 {
		URL url = null;
		String CorpID = "TCLKJ04032";// 账户名
		String Pwd = "tcyw0814@";// 密码
		int inputLine = 0;
		try {
		String send_content = URLEncoder.encode(Content
				.replaceAll("<br/>", " "), "GBK");// 发送内容
		// url = new
		// URL("http://inolink.com/WS/BatchSend.aspx?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+Mobile+"&Content="+send_content+"&Cell=&SendTime="+send_time);
		url = new URL(prefixuUrl+"?CorpID=" + CorpID
				+ "&Pwd=" + Pwd + "&Mobile=" + Mobile + "&Content="
				+ send_content);
		BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = new Integer(in.readLine()).intValue();
		} catch (Exception e) {
			log.error("短信异常,手机号：" + Mobile + "\n异常信息：", e);
			inputLine = -2;
		}
		log.info(new Date().toString() + ":发短信Mobile:" + Mobile + ",inputLine:"
				+ inputLine);
		return inputLine;
	}

	/**
	 * pdf打印
	 * 
	 * @param pdfPath
	 * @return
	 */
	public static boolean printPdf(String pdfPath) {
		try {
			log.info("未打印，打印路径：{}", pdfPath);
			Runtime.getRuntime().exec(
					"cmd.exe /C start acrord32 /P /h " + pdfPath);
			log.info("已打印，打印路径：{}", pdfPath);
			return true;
		} catch (Exception e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("报告打印异常,操作人：" + opertorName + ",路径:" + pdfPath
					+ "\n异常信息：", e);
			return false;
		}
	}

	public static boolean printKitchenPdf(String pdfPath) {
		try {
			log.info("未打印，打印路径：{}", pdfPath);
			Runtime
					.getRuntime()
					.exec(
							"cmd.exe /C \"c:\\Program Files\\Ghostgum\\gsview\\gsprint\" -printer myprinter "
									+ pdfPath);
			log.info("已打印，打印路径：{}", pdfPath);
			return true;
		} catch (Exception e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("报告打印异常,操作人：" + opertorName + ",路径:" + pdfPath
					+ "\n异常信息：", e);
			return false;
		}
	}

	/**
	 * 对象转Map
	 */
	@SuppressWarnings("rawtypes")
	public static Map ConvertObjToMap(Object obj) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		if (obj == null) {
			return null;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {

				Field f = obj.getClass().getDeclaredField(fields[i].getName());
				f.setAccessible(true);
				Object o = f.get(obj);
				reMap.put(fields[i].getName(), o);

			}
		} catch (Exception e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("对象转Map异常,操作人：" + opertorName + "\n异常信息：", e);
		}
		return reMap;
	}

	// 将文件转换成BASE64字符串
	public static String fileToBaseCode(String filename) throws IOException {
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(filename);
			data = new byte[in.available()];
			in.read(data);
		} catch (IOException e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return data != null ? encoder.encode(data) : "";
	}
	// 将图片转换成BASE64字符串(word)
	public static String getImageString(String filename) throws IOException {
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(filename);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return data != null ? encoder.encode(data) : "";
	}

	/**
	 * 替换字符串
	 * 
	 * @param from
	 *            String 原始字符串
	 * @param to
	 *            String 目标字符串
	 * @param source
	 *            String 母字符串
	 * @return String 替换后的字符串
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null) {
			return null;
		}
		StringBuilder bf = new StringBuilder("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			bf.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		bf.append(source);
		return bf.toString();
	}

	/**
	 * 通过反射根据 实体，字段名获取字段值
	 */
	@SuppressWarnings("rawtypes")
	public static Object reflectMethod(Object obj, String mothodName) {
		Object o = null;
		try {
			Class clazz = obj.getClass();
			PropertyDescriptor pd = new PropertyDescriptor(mothodName, clazz);
			Method getMethod = pd.getReadMethod();// 获得get方法

			if (pd != null) {
				o = getMethod.invoke(obj);// 执行get方法返回一个Object
			}
		} catch (Exception e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("反射实体异常,操作人：" + opertorName + "\n异常信息：", e);
		}

		return o;

	}

	// 把一个字符串的第一个字母大写、效率是最高的、
	public static String getMethodName(String fildeName) {
		byte[] items = fildeName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}

	/**
	 * 调用php接口
	 * 
	 * @param phpurl
	 * @param content
	 */
	public static String phpExec(String phpurl, String content) {
		URL url = null;
		String sampleNo = "";
		try {
			// 创建连接
			url = new URL(phpurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded");
			conn.connect();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(content);
			out.flush();
			out.close();
			// 获取响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String lines;
			StringBuilder sbs = new StringBuilder();
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sbs.append(lines);
			}
			reader.close();
			if (!"ok".equals(sbs.toString())) {// 错误
				// 将产前失败信息保存到失败样本推送的表中
				if (content.contains("youxinan")) {
					sampleNo = content.substring(content.indexOf("ybbh=") + 5,
							content.indexOf("&result="));
				}

				StringBuilder email = new StringBuilder();
				email.append("jinlonghuang@scisoon.cn");
				email.append(",mengxiaonie@scisoon.cn");
				JavaMailQunFa se = new JavaMailQunFa(false);
				String title = "【LIMS】产前接口";
				if ("youti".equals(content.substring(0, 5))) {
					title = "【LIMS】优替接口";
				}
				ServletContext ap = getRequest().getSession()
						.getServletContext();
				Integer em = (Integer) ap.getAttribute("em");
				if (em == null) {
					ap.setAttribute("em", 1);
				} else {
					ap.setAttribute("em", (em + 1));
				}
				if (em != null && em < 4) {
					// se.doSendHtmlEmail(title,
					// "参数："+content+"<br/>返回值："+sbs+"<br/>祝好。<br/>    该邮件为LIMS系统自动生成",
					// email.toString(),null);
				}
			}

			// 关闭连接
			conn.disconnect();
		} catch (Exception e) {
			String opertorName = Struts2Utils.getUser() != null ? Struts2Utils
					.getUser().getName() : "";
			log.error("PHP接口异常,操作人：" + opertorName + "phpurl:" + phpurl
					+ "content:" + content + "\n异常信息：", e);
		}
		return sampleNo;
	}

	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断是否为正整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPositiveInteger(String str) {
		return PATTERN_POSITIVE_INTEGER.matcher(str).matches();
	}

	/**
	 * 判断字符串是否是double
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Double转百分数 param1 小数 param2 小数点前保留几位 param3 小数点后保留几位
	 */
	public static String getPercentFormat(double d, int IntegerDigits,
			int FractionDigits) {
		NumberFormat nf = java.text.NumberFormat.getPercentInstance();
		// nf.setMaximumIntegerDigits(IntegerDigits);//小数点前保留几位
		nf.setMinimumFractionDigits(FractionDigits);// 小数点后保留几位
		String str = nf.format(d);
		return str;
	}

	// 获取当前日期n个月前的时间
	public static String getTwoMonthBefore(String nowTime, int num)
			throws ParseException {
		// 获取当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = dateFormat.parse(nowTime);
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(calendar.MONTH, -num); // 设置为前2月，可根据需求进行修改
		date = calendar.getTime();// 获取2个月前的时间
		return dateFormat.format(date);
	}

	/**
	 * 去除前后指定字符
	 * 
	 * @return
	 */
	public static String trimStartEnd(String dataSource, String start,
			String end) {
		String tempData = dataSource;
		if (tempData == null)
			return "";
		
		int index1 = 0;
		if (start!=null&&tempData.startsWith(start)) {
			index1 = 1;
		}
		int index2 = tempData.length();
		if (end!=null&&tempData.endsWith(end)) {
			index2 = index2 - 1;
		}

		return tempData.substring(index1, index2);
	}

}
