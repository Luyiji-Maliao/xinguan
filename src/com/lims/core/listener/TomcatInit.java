package com.lims.core.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.lims.core.utils.web.Struts2Utils;
/**
 * tomcat启动监听
 * @author 聂梦肖
 *
 */
public class TomcatInit implements ServletContextListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4129756594304141341L;

	

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		String photopath=arg0.getServletContext().getRealPath("/")+"photohtml5/";
		Struts2Utils.setPath(photopath);
		
	}  
	
}
