package ${ab.packageurl}.action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ${ab.packagebeanurl}.${ab.javabean};
import ${ab.packageserviceurl}.${ab.javaservice};
import com.lims.core.orm.Page;

import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;



@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })

@Results({
	@Result(name = "logins", type = "redirect", location = "session.jsp"),
	@Result(name = "modulepage", location = "/WEB-INF/content/${ab.actionresult}/${ab.beannametoLower}.jsp")
	})
public class ${ab.javaaction} extends CrudActionSupport<${ab.javabean}> {

	private static final long serialVersionUID = 1L;

	private ${ab.javabean} entity;

	@Autowired
	private ${ab.javaservice} autoservice; 

	@Override
	protected void prepareModel() throws Exception {

		entity = new ${ab.javabean}();

	}

	@Override
	public ${ab.javabean} getModel() {
		return entity;
	}
/**
 * 保存
 */
	@Override
	public String save() throws Exception {
		
		autoservice.save(entity);
		msg.setSuccess(true);
		Struts2Utils.renderJson(msg);
		return null;
	}
	
	
	
	/**
	 * 修改
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception{

		autoservice.update(entity);
		msg.setSuccess(true);
		msg.setMsg("已修改");
		Struts2Utils.renderJson(msg);
		return null;
	}
	public String delete(){
		autoservice.delete(entity.get${ab.beannametoUpper}id());
		Struts2Utils.renderJson("已删除");
		return null;
	}
	@Override
	public String list() throws Exception {		
		
		Page<${ab.javabean}> page = new Page<${ab.javabean}>(limit);
		page.setPageNo((start/limit)+1);
		autoservice.findPage(page,PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
		Struts2Utils.renderJson(page);
		return null;
	}

	

	@Override
	public String input() throws Exception {
		return null;
	}
	
	public String createExcel(){
		List<${ab.javabean}> li=Struts2Utils.conver(iteminfo,${ab.javabean}.class);
		if(li.size()==0){
			li=autoservice.searchList(entity);
		}
		if(li.size()>0){
			//String uploadFileSavePath=ServletActionContext.getRequest().getRealPath("/documents");
	    //	WriteExcel.writeoutSampleExcel(lc, uploadFileSavePath+"/outsampleinfo.xls");
			autoservice.createExcel(li);
			msg.setMsg("有数据");
	    	Struts2Utils.renderJson(msg);
		}else{
			msg.setMsg("无数据");
			Struts2Utils.renderJson(msg);
		}
		return null;
	}
	
	//导出excel
	@SuppressWarnings("deprecation")
	public void exportExcel() throws IOException{
		String name=new String("${ab.javabean}".getBytes("UTF-8"), "ISO-8859-1");
		Struts2Utils.getResponse().addHeader("Content-Disposition", "attachment;filename=\""+name+Struts2Utils.getymd(new Date())+".xls"+"\"");
		OutputStream os = Struts2Utils.getResponse().getOutputStream();
		String uploadFileSavePath=ServletActionContext.getRequest().getRealPath("/documents");
    	String path=uploadFileSavePath+"/${ab.beannametoLower}.xls";
    	FileInputStream is=new FileInputStream(path);
    	
    	byte[] buffer = new byte[400];
		int length = 0;
		while (-1 != (length = is.read(buffer))) {
			os.write(buffer, 0, length); 
		}
		is.close();
		os.close();
		
	}
	
	/**
	 * 列表页
	 * @return
	 */
	@Override
	public String modulepage() throws Exception {
		return "modulepage";
	}
	
}
