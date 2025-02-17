package com.lims.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.entity.User;
/**
 * session过滤访问链接（登陆）
 * @author 聂梦肖
 *
 */
public class SessionFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;
		@SuppressWarnings("unused")
		HttpSession session=req.getSession();
		User loginuser=(User) Struts2Utils.getSession().getAttribute("loginUser");
		if(loginuser!=null){
			chain.doFilter(request, response);
		}else{
			System.out.println("未登录");
			//返回登陆页面 
			res.sendRedirect("http://"+req.getHeader("Host")+"/welcome.jsp");
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
