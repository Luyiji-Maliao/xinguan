package com.usci.aop;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lims.core.utils.web.Struts2Utils;

@Service
@Aspect//声明这是一个切面
public class ExceptionAspect {
	/**
	 * 去除action set方法，getModel方法，SalarybillAction.readexcel方法
	 */
	  @Pointcut(value="execution(* com..*.*Action.*(..))&&!execution(* com..*.*Action.getModel(..))&&!execution(* com..*.*Action.set*(..))&&!execution(* com..*.SalarybillAction.readexcel(..))")
	  private void exception(){//定义一个切入点 后面的通知直接引入切入点方法pointCut即可           
	  }
	      
	  

	
	    
		private static final Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);
		
		
		
	    //@AfterThrowing(value="exception()", throwing="e")
	    public void exception(JoinPoint point,Throwable e) throws Throwable{
	    	//System.out.println("异常");
	    	try{
	    	HttpServletRequest request = ServletActionContext.getRequest();
	    	String username=null;
	    	if(Struts2Utils.getSessionUser()!=null){
	    		username=Struts2Utils.getSessionUser().getName();
	    	}
	    	//System.out.println("Struts2Utils.getSessionUser():"+Struts2Utils.getSessionUser()==null?null:Struts2Utils.getSessionUser().getName());
	    	//logger.error("账号："+username);
	    	//logger.error("URL:"+request.getRequestURL().toString());
	    	//获取参数
	    	Map<String, String> param = new HashMap<String, String>();
	    	Map<String, String[]> params = request.getParameterMap();
	        for (String key : params.keySet()) {
	            String[] values = params.get(key);
	            for (int i = 0; i < values.length; i++) {
	            	//过滤密码
	            	if(!"password".equals(key)){
	            		  param.put(key, values[i]);
	            	}
	               // System.out.println(key+":"+values[i]);
	            }
	        }
	        Gson gson = new Gson();
	    	logger.error("账号："+username+"\nURL:"+request.getRequestURL().toString()+
	    			"\n参数："+gson.toJson(param)+"\n异常信息：",e);
	    	//logger.error("异常信息：",e);
	    	}catch(Throwable ex){
	    		//ex.printStackTrace();
	    		logger.error("异常错误：",ex);
	    	}
	    }
	  
	  
}

