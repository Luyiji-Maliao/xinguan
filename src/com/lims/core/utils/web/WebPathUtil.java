package com.lims.core.utils.web;

import org.hibernate.boot.jaxb.SourceType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 非session情况下获取路径
 * @author IT-user-34
 *
 */
public class WebPathUtil {

	//取得\WebRoot\WEB-INF\classes\
	public String getWebClassesPath() {
	   String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	   //return path.substring(1).replace("\\", "/");
		return path;
	}
	//取得\WebRoot\WEB-INF\
	public String getWebInfPath() throws IllegalAccessException{
	   String path = getWebClassesPath();
	   if (path.indexOf("WEB-INF") > 0) {
	    path = path.substring(0, path.indexOf("WEB-INF")+8);
	   } else {
	    throw new IllegalAccessException("路径获取错误");
	   }
	   return path;
	}
	public String getWebRootPath() throws IllegalAccessException{
		String path = getWebClassesPath();
	   if (path.indexOf("WEB-INF") > 0) {
	    path = path.substring(0, path.indexOf("WEB-INF"));
	   } else {
	    throw new IllegalAccessException("路径获取错误");
	   }
	   return path;
	}
	
	public String getRealPath(String path) throws IllegalAccessException{
		if(path!=null&&!"".equals(path)&&(path.substring(0, 1).equals("/")||path.substring(0, 1).equals("\\"))){
			path = path.substring(1);
		}
		//return (getWebRootPath()+(path==null?"":path)).replace("\\", "/");
		return (getWebRootPath()+(path==null?"":path));
	}
	
	
}
