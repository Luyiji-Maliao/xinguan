package com.usci.system.action;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.entity.EverCookies;
import com.usci.system.entity.Right;
import com.usci.system.entity.Role;
import com.usci.system.entity.ScheduleSort;
import com.usci.system.entity.User;
import com.usci.system.service.EverCookiesService;
import com.usci.system.service.ScheduleSortService;
import com.usci.system.service.UserService;

/**
 * 
 * 待办事项分类
 *
 */
@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
@Result(name = "logins", type = "redirect", location = "session.jsp"),
@Result(name = "helpdoc", location = "/WEB-INF/content/systemmanage/helpdoc.jsp")
})
public class SchedulesortAction extends CrudActionSupport<ScheduleSort> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8412687369612408105L;
	@Autowired
	private ScheduleSortService scheduleSortService;
	private ScheduleSort entity;
	@Autowired
	private EverCookiesService everCookiesService;
	@Autowired
	private UserService userService;
	private String helpdocs;
	private static final String separator=File.separator;
	public String getHelpdocs() {
		return helpdocs;
	}

	public void setHelpdocs(String helpdocs) {
		this.helpdocs = helpdocs;
	}

	@Override
	public String list() throws Exception {
		Struts2Utils.renderJson(scheduleSortService.list(limit));
		return null;
	}

	@Override
	public String modulepage() throws Exception {
		return null;

	}

	@Override
	protected void prepareModel() throws Exception {
		entity=new ScheduleSort();
	}

	@Override
	public String save() throws Exception {
		ScheduleSort ss=scheduleSortService.findByUseridAndTitle(Struts2Utils.getSessionUser().getName(),entity.getTitle());
		if("添加".equals(saveorupdate)){
			if(ss!=null){
				msg.setSuccess(false);
				msg.setMsg("添加的待办分类已存在，请重新添加");
				Struts2Utils.renderJson(msg);
			}else{
				scheduleSortService.save(entity);
				msg.setMsg("添加成功");
				Struts2Utils.renderJson(msg);
			}
		}
		
		if("修改".equals(saveorupdate)){
			int i=0;
			if(ss!=null){
				if(ss.getId().equals(entity.getId())){//判断此id与前台传过来的id是否相同
					i=0;
				}else{
					i=1;
				}
			}else{
				i=0;
			}
			if(i==0){
				scheduleSortService.updateTitle(entity);
				msg.setMsg("修改成功");
				Struts2Utils.renderJson(msg);
			}else{
				msg.setSuccess(false);
				msg.setMsg("修改的分类已存在，请重新修改");
				Struts2Utils.renderJson(msg);
			}
		}
		
		return null;
	}
	
	
	@Override
	public ScheduleSort getModel() {
		return entity;
	}
	
	public String ssc(){
	Struts2Utils.renderJson(scheduleSortService.findList(Struts2Utils.getSessionUser().getName()));
		return null;
	}
	//检查是否有“其他”待办类型，没有则添加
	public String checkScheduleSort(){
		ScheduleSort ss=scheduleSortService.findByUseridAndTitle(Struts2Utils.getSessionUser().getName(),"其他");
		if(ss==null){
			
			 ScheduleSort s=new ScheduleSort();
		       s.setTitle("其他");
		       s.setSsStatus(1);
		       s.setUserName(Struts2Utils.getSessionUser().getName());
		       Integer ssid=  scheduleSortService.returnsave(s);
		     //把位置保存到数据库
		       EverCookies ec=new EverCookies();
		       ec.setCookieContent("[{'id' : "+ssid+",'col' : 0}]");
		       ec.setCookieName("schedulesortcookie");
		       ec.setUserId(Struts2Utils.getSessionUser().getId());
		       everCookiesService.save(ec);
		       Struts2Utils.renderJson(1);
		}else{
			Struts2Utils.renderJson(0);
		}
		return null;
	}
	
	public String listcombo(){
		Page<ScheduleSort> page = new Page<ScheduleSort>(limit);
		page.setPageNo((start / limit) + 1);
		scheduleSortService.findPage(page, PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
		Struts2Utils.renderJson(page);
		return null;
	}
	
	public String deleteSort(){
		scheduleSortService.deleteSort(entity.getId());
		Struts2Utils.renderJson(msg);
		return null;
	}
	/**   * 点击批量下载触发的方法   * @return   */ 
	@SuppressWarnings("unchecked")  
	public String moredownload(){  
		User u=userService.get(Struts2Utils.getSessionUser().getId());
		List<Role> lr=u.getRoles();
		List<Right> lt=lr.get(0).getRights();
		List<File> lf=new ArrayList<File>();
		String pathDir = ServletActionContext.getServletContext().getRealPath("/documents");
		for (Right right : lt) {
			String filePath =pathDir+"\\"+right.getModuleName()+".pdf";
			if("受检者信息".equals(right.getModuleName())){
				filePath=pathDir+"\\天津胸科电子送检单使用说明.pdf";
			}
			if("医院受检者信息".equals(right.getModuleName())){
				filePath=pathDir+"\\电子送检单使用说明.pdf";
			}
			File file = new File(filePath);
			if(file.exists()){
				lf.add(file);
			}
		}
		//lf.add(new File(pathDir+"\\"+"消息中心.docx"));
		
		HttpServletResponse response = ServletActionContext.getResponse();
		@SuppressWarnings("unused")  
		//List list1 = checktb;     

		//==========================多文件压缩成	zip包下载测试========================================   
		//生成的ZIP文件名为Demo.zip       
		String tmpFileName = "帮助文档.zip";      
		String FilePath = "D:\\";        
		byte[] buffer = new byte[1024];       
		String strZipPath = FilePath + tmpFileName;          
		try {                
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream( strZipPath));                 // 需要同时下载的两个文件result.txt ，source.txt   
	            // File[] file1 = { new File(FilePath+"1.txt"), new File(FilePath+"1.docx") }; 
	             
	             for (int i = 0; i < lf.size(); i++) {       
	            	 FileInputStream fis = new FileInputStream(lf.get(i));  
	            	 out.putNextEntry(new ZipEntry(lf.get(i).getName()));  
	            	 //设置压缩文件内的字符编码，不然会变成乱码                
	            	 out.setEncoding("GBK");                   
	            	 int len;                     
	            	 // 读入需要下载的文件的内容，打包到	zip文件           
	            	 while ((len = fis.read(buffer)) > 0) {           
	            		 out.write(buffer, 0, len);                 
	            		 }                    
	            	 out.closeEntry();                   
	            	 fis.close();              
	            	 }                 out.close();               
	            	 this.downFile1(response, tmpFileName);       
	            	 } catch (Exception e) {    
	            		      
	            }       
	            	 //return "mdlsuccess";     
	            	 return null;   
	}
	public void downFile1(HttpServletResponse response,String str){
		try {
			String FilePath = "D:\\"; 
			String path = FilePath + str;
			File file = new File(path);
			if (file.exists()) {
				InputStream ins = new FileInputStream(path);
				 BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面 
				 OutputStream outs = response.getOutputStream();// 获取文件输出IO流
				 BufferedOutputStream bouts = new BufferedOutputStream(outs);
				 response.setContentType("application/x-download");// 设置response内容的类型
				 response.setHeader( "Content-disposition",  "attachment;filename=" + URLEncoder.encode(str, "UTF-8"));// 设置头部信息
				 int bytesRead = 0; 
				 byte[] buffer = new byte[8192]; 
				// 开始向网络传输文件流
				 while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) { 
					 bouts.write(buffer,0,bytesRead);
				 }
				 bouts.flush();// 这里一定要调用flush()方法  
				 ins.close();
				 bins.close();
				 outs.close(); 
				 bouts.close();  
				} else {  
					
				}
		} catch (IOException e) {    
			 
			} 
	}
	
	public String downloadone(){
		
	
		if("受检者信息".equals(helpdocs)){
			helpdocs="天津胸科电子送检单使用说明";
		}
		if("医院受检者信息".equals(helpdocs)){
			helpdocs="电子送检单使用说明";
		}
		String pathDir = ServletActionContext.getServletContext().getRealPath("/documents");
		HttpServletResponse response = ServletActionContext.getResponse();
		File file = new File(pathDir+"\\"+helpdocs+".pdf");
		if(file.exists()){
			download(pathDir+"\\"+helpdocs+".pdf",response);
			return null;
		}else{
			return "helpdoc";
		}
		
	}
	
/**
 * 通用下载	
 * @return
 */
@SuppressWarnings("deprecation")
public String downloadfile(){
		
		try {
			helpdocs = java.net.URLDecoder.decode(helpdocs, "utf-8");
			//helpdocs=new String(helpdocs.getBytes("iso8859-1"),"utf-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(helpdocs!=null){
			String [] fileList=helpdocs.split("/");
			if(fileList.length==1){
				fileList=helpdocs.split("\\\\");
			}
			helpdocs="";
			for(int i=0;i<fileList.length;i++){
				if(i+1==fileList.length){
					helpdocs+=fileList[i];
				}else{
					helpdocs+=fileList[i]+separator;
				}
			}
		}

		String pathDir = ServletActionContext.getServletContext().getRealPath(separator+"documents");
		HttpServletResponse response = ServletActionContext.getResponse();
		File file = new File(pathDir+separator+helpdocs);
		//System.out.println("out:downloadfile:"+pathDir+separator+helpdocs);
		if(file.exists()){
			download(pathDir+separator+helpdocs,response);
			return null;
		}else{
			return "helpdoc";
		}
		
	}
	public void download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
           // String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //return response;
	}
	
}
