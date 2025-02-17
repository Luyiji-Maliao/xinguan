package com.lims.core.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.usci.system.entity.User;

public class SessionListener  implements HttpSessionListener ,HttpSessionAttributeListener{

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		ServletContext application = arg0.getSession().getServletContext();
		User u=(User)arg0.getSession().getAttribute("loginUser");
		 Map<String,String> ss = (Map) application.getAttribute("loginMaps");
		   if(ss!=null){
			   for (Map.Entry<String, String> entry : ss.entrySet()) {
		            if (arg0.getSession().getId().equals(entry.getValue())) {
		                ss.remove(entry.getKey());
		 			   application.setAttribute("loginMaps", ss);

		                break;
		            }
		        } 
		   }
		   

	
	            
	        


		  
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		//arg0.getSession().getAttribute("loginUser");
	/*	ServletContext application = arg0.getSession().getServletContext();
		   Map s = (Map) application.getAttribute("loginMap");
		   User u=(User)arg0.getSession().getAttribute("loginUser");
		   if(s==null){
			   s=new HashMap();
			   s.put(u.getUsername(), u.getUsername());  
		   }else{
			   if(s.get(u.getUsername())==null){//不存在此账号
					  s.put(u.getUsername(), u.getUsername());  
			   }
		   }
		  
		 
		   
		//arg0.getSession().getServletContext().
		application.setAttribute("loginMap", s);*/
		  
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		/*ServletContext application = arg0.getSession().getServletContext();
		   Map s = (Map) application.getAttribute("loginMap");
		   User u=(User)arg0.getSession().getAttribute("loginUser");
		   if(s==null){
			   s=new HashMap();
			   s.put(u.getUsername(), u.getUsername());  
		   }else{
			   if(s.get(u.getUsername())==null){//不存在此账号
					  s.put(u.getUsername(), u.getUsername());  
			   }
		   }
		  
		 
		   
		//arg0.getSession().getServletContext().
		application.setAttribute("loginMap", s);*/
	}

}
