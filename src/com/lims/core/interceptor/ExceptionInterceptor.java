package com.lims.core.interceptor;



import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.lims.core.utils.web.Struts2Utils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
/**
 * 
 *
 */
@SuppressWarnings("serial")
public class ExceptionInterceptor extends MethodFilterInterceptor  {
	private static final  Logger log=LoggerFactory.getLogger(ExceptionInterceptor.class);
	private static final org.apache.logging.log4j.Logger logger=LogManager.getLogger(ExceptionInterceptor.class);
	@Override
	protected String doIntercept(ActionInvocation ai) {
		   String result="error";
		   HttpServletRequest request = ServletActionContext.getRequest();
		   
		   String username=null;
	    	if(Struts2Utils.getUser()!=null){
	    		username=Struts2Utils.getUser().getName();
	    	}

			Map<String, String[]> params = request.getParameterMap();
			StringBuilder sb=new StringBuilder();
		    for (String key : params.keySet()) {
	            String[] values = params.get(key);
	            for (int i = 0; i < values.length; i++) {
	            	sb.append(key+":"+values[i]+",");
	            }
	        }
		    String URL=request.getRequestURL().toString();
		    String action=ai.getAction().getClass().getName();
		    String method=ai.getProxy().getMethod();
		    String param=sb.toString();
		    log.info("操作记录-账号：{},\nURL:{},\n类：{},\n方法：{},\n参数：{}",username,URL,action,method,param);
		    logger.log(Level.forName("OPER", 399), "操作记录-账号：{},\nURL:{},\n类：{},\n方法：{},\n参数：{}",username,URL,action,method,param);

		    try {
				result=ai.invoke();
			} catch (Exception e) {
				logger.error("账号："+username+"\nURL:"+URL+"\n类："+action+"\n方法："+method+"\n参数："+param,e);
				//主动抛出时，会进入web.xml配置的500错误
				//throw new Exception(e);
				//异步请求的错误信息返回json，前台处理
				if (request.getHeader("X-Requested-With") != null&& request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {  
					Struts2Utils.renderJson("{\"success\":false}");
					return null;
				}
			}
			
				return result;
	}

}
