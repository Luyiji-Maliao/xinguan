package com.lims.core.utils.web;

import com.google.gson.Gson;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static final int timeout = 10000;


	/**
	 * 获取一个HttpClient对象
	 * 
	 * @return
	 */
	public static HttpClient getHttpClient() {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		return client;
	}








	/**
	 * 发送post请求
	 * 
	 * @param url
	 *            地址
	 * @return
	 * @throws Exception 
	 * @throws BizException 
	 */
	public static String post(String url) throws Exception{
		return post(url, getHttpClient());
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 *            地址
	 * @param client
	 * @return
	 * @throws Exception 
	 * @throws BizException 
	 */
	public static String post(String url, HttpClient client) throws Exception {
		// 设置代理服务器地址和端口
		// client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
		// 使用POST方法
		HttpMethod method = new PostMethod(url);
		method.setRequestHeader("Content-type", "text/html; charset=" + "utf-8");
		String response = null;
		try {
			client.executeMethod(method);
			method = dealRedirect(method);
			response = method.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error("发送post请求失败", e);
			throw new Exception("发送post请求失败");
		} finally {
			// 释放连接
			method.releaseConnection();
		}
		return response;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数
	 * @return
	 * @throws BizException 
	 */
	public static String post(String url, Map<String, String> params) {
		return post(url, params, getHttpClient());
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws BizException 
	 */
	public static String postJosn(String url, Map<String, Object> params) throws Exception {
		return postJson(url, params, getHttpClient());
	}

	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}

	public static String getUrlParamsByMap(Map<String, String> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = s.substring(0, s.length()-1);
		}
		return s;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数
	 * @param client
	 * @return
	 * @throws BizException 
	 */
	public static String post(String url, Map<String, String> params, HttpClient client) {
		String response = "";
		PostMethod method = new PostMethod(url);
		try {
			method.setRequestBody(convertParams(params));
			client.executeMethod(method);
			method = dealPostRedirect(method);
			InputStream inputStream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			StringBuffer stringBuffer = new StringBuffer();
			String str= "";
			while((str = br.readLine()) != null){
				stringBuffer.append(str);
			}
			response = new String(stringBuffer);
		} catch (IOException e) {
			logger.info(e.toString());
//			logger.error("发送post请求失败", e);
			throw new RuntimeException("发送post请求失败");
		} finally {
			method.releaseConnection();
		}
		return response;
	}

	/**
	 * 发送post json数据请求
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数
	 * @param client
	 * @return
	 * @throws BizException 
	 */
	public static String postJson(String url, Map<String, Object> params, HttpClient client) throws Exception {
		String response = null;
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Content-type", "application/json; charset=" + "utf-8");
		try {
			String jsonStr = new Gson().toJson(params);
			RequestEntity requestEntity = new StringRequestEntity(jsonStr, "application/json", "utf-8");
			method.setRequestEntity(requestEntity);
			client.executeMethod(method);
			method = dealPostRedirect(method);
			response = method.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error("发送postJson请求失败", e);
			throw new Exception("发送post请求失败");
		} finally {
			method.releaseConnection();
		}
		return response;
	}

	/**
	 * 登陆系统,返回的HttpClient对象已经包含登陆cookie，可以直接访问其他请求
	 * 
	 * @param url
	 *            登陆url地址
	 * @param params
	 *            登陆时所需的所有参数
	 * @return
	 * @throws BizException 
	 */
	public static HttpClient login(String url, Map<String, String> params) throws Exception {
		HttpClient client = getHttpClient();
		PostMethod post = new PostMethod(url);
		try {
			post.setRequestBody(convertParams(params));
			client.executeMethod(post);
			String resp = post.getResponseBodyAsString();
			logger.info("登陆返回:" + post.getStatusCode() + "," + resp);
		} catch (Exception e) {
			logger.error("登陆系统失败", e);
			throw new Exception("登陆系统失败");
		} finally {
			post.releaseConnection();
		}
		return client;
	}





	/**
	 * 提交json数据
	 * 
	 * @param url
	 *            地址
	 * @param json
	 *            json内容
	 * @return
	 * @throws BizException 
	 */
	public static String postJson(String url, String json) throws Exception {
		return postJson(url, json, getHttpClient());
	}

	/**
	 * 提交json数据
	 * 
	 * @param url
	 *            地址
	 * @param json
	 *            json内容
	 * @param client
	 * @return
	 * @throws BizException 
	 */
	public static String postJson(String url, String json, HttpClient client) throws Exception {
		String resp = null;
		PostMethod post = new PostMethod(url);
		try {
			RequestEntity requestEntity = new StringRequestEntity(json, "application/json", "utf-8");
			post.setRequestEntity(requestEntity);
			// 指定请求内容的类型
			post.setRequestHeader("Content-type", "application/json; charset=" + "utf-8");
			client.executeMethod(post);
			resp = post.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error("提交xml失败", e);
			throw new Exception("提交xml失败");
		} finally {
			post.releaseConnection();
		}
		return resp;
	}

	/**
	 * 参数转化
	 * 
	 * @param params
	 *            参数
	 * @return
	 */
	private static NameValuePair[] convertParams(Map<String, String> params) {
		NameValuePair[] paramPair = new NameValuePair[params.size()];
		int index = 0;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			NameValuePair simcard = new NameValuePair(entry.getKey(), entry.getValue());
			paramPair[index] = simcard;
			index++;
		}
		return paramPair;
	}
	
	/**
	 * 处理重定向
	 * 
	 * @param method
	 * @return
	 */
	private static HttpMethod dealRedirect(HttpMethod method) throws HttpException, IOException {
		// 检查是否重定向
		int statuscode = method.getStatusCode();
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER)
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// 读取新的 URL 地址
			Header header = method.getResponseHeader("location");
			if (header != null) {
				method.releaseConnection();
				HttpClient client = getHttpClient();
				String newuri = header.getValue();
				if ((newuri == null) || (newuri.equals(""))) {
					newuri = "/";
				}
				HttpMethod redirect = new GetMethod(newuri);
				client.executeMethod(redirect);
				return dealRedirect(redirect);
			} else {
				return method;
			}
		} else {
			return method;
		}
	}
	
	/**
	 * 处理重定向
	 * 
	 * @param method
	 * @return
	 */
	private static PostMethod dealPostRedirect(PostMethod method) throws HttpException, IOException {
		// 检查是否重定向
		int statuscode = method.getStatusCode();
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER)
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// 读取新的 URL 地址
			Header header = method.getResponseHeader("location");
			if (header != null) {
				method.releaseConnection();
				HttpClient client = getHttpClient();
				String newuri = header.getValue();
				if ((newuri == null) || (newuri.equals(""))) {
					newuri = "/";
				}
				PostMethod redirect = new PostMethod(newuri);
				client.executeMethod(redirect);
				return dealPostRedirect(redirect);
			} else {
				return method;
			}
		} else {
			return method;
		}
	}

	/**
	 * 发送get请求 请求返回字节
	 *
	 * @param url
	 * @return
	 */
	public static byte[] getBytes(String url) {
		return getBytes(url, getHttpClient());
	}

	/**
	 * 发送get请求 请求返回字节
	 *
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, getHttpClient());
	}

	/**
	 * 发送get请求
	 *
	 * @param url
	 *            地址
	 * @param client
	 * @return
	 */
	private static byte[] getBytes(String url, HttpClient client) {
		// 设置代理服务器地址和端口
		// client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		byte[] response = null;
		HttpMethod method = new GetMethod(url);
		try {
			client.executeMethod(method);
			method = dealRedirect(method);
			response = method.getResponseBody();
		} catch (Exception e) {
			logger.error("发送get请求失败", e);
			throw new RuntimeException("发送get请求失败");
		} finally {
			// 释放连接
			method.releaseConnection();
		}
		return response;
	}

	/**
	 * 发送get请求
	 *
	 * @param url
	 *            地址
	 * @param client
	 * @return
	 */
	private static String doGet(String url, HttpClient client) {
		// 设置代理服务器地址和端口
		// client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		String response = null;
		HttpMethod method = new GetMethod(url);
		method.setRequestHeader("Authorization", "uc0d98e79de93d6e49521deab9b0d9eb7a");
		try {
			client.executeMethod(method);
			method = dealRedirect(method);
			InputStream inputStream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			StringBuffer stringBuffer = new StringBuffer();
			String str= "";
			while((str = br.readLine()) != null){
				stringBuffer.append(str);
			}
			response = new String(stringBuffer);
		} catch (Exception e) {
			logger.error("发送get请求失败", e);
			throw new RuntimeException("发送get请求失败");
		} finally {
			// 释放连接
			method.releaseConnection();
		}
		return response;
	}
}
