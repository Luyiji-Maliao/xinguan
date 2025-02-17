package com.lims.core.interceptor;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.lims.core.utils.web.Struts2Utils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.usci.system.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 过滤Ext session
 * @author 聂梦肖
 *
 */
@SuppressWarnings("serial")
public class SessionInterceptor extends MethodFilterInterceptor  {
	private static final Logger log= LoggerFactory.getLogger(SessionInterceptor.class);
	@Override
	protected String doIntercept(ActionInvocation ai) throws Exception {

			User user=(User)ai.getInvocationContext().getSession().get("loginUser");
			if(user!=null){
				ServletContext ap = Struts2Utils.getSession().getServletContext();
			    Map<String ,String> s = (Map) ap.getAttribute("loginMaps");
				if(s!=null&&s.containsKey(user.getUsername())&&!s.get(user.getUsername()).equals(Struts2Utils.getSession().getId()) ){
					HttpServletRequest req=ServletActionContext.getRequest();
					HttpServletResponse res=ServletActionContext.getResponse();
					PrintWriter pw=res.getWriter();
					
					String flag="";
					if(req.getHeader("X-Requested-With")!=null&&req.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")){
						log.info("用户没登录或登录过期，不能访问");
						
						res.setCharacterEncoding("text/html;charset=utf-8");
						res.setContentType("text/html;charset=utf-8");
						flag="9999";
						pw.write(flag);
						return null;
					}else{
						return "logins";
					}
				}
				return ai.invoke();
			}else{
				HttpServletRequest req=ServletActionContext.getRequest();
				HttpServletResponse res=ServletActionContext.getResponse();
				PrintWriter pw=res.getWriter();
				
				String flag="";
				if(req.getHeader("X-Requested-With")!=null&&req.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")){
					log.info("用户没登录或登录过期，不能访问");
					
					res.setCharacterEncoding("text/html;charset=utf-8");
					res.setContentType("text/html;charset=utf-8");
					flag="9999";
					pw.write(flag);
					return null;
				}else{
					return "logins";
				}
				
			}
	}

}
