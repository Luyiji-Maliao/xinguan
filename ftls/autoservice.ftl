package ${ab.packageserviceurl};

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import ${ab.packagedaourl}.${ab.javadao};
import ${ab.packagebeanurl}.${ab.javabean};
import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.Struts2Utils;


@Component
@Transactional
public class ${ab.javaservice} {
    
    @Autowired
    private ${ab.javadao}   autodao;
    
    @Transactional
    public void save(${ab.javabean} entity) {
    		entity.setInputtime(Struts2Utils.getStringDate(new Date()));
    		entity.setInputname(Struts2Utils.getSessionUser().getName());
    		entity.setUpdatetime(Struts2Utils.getStringDate(new Date()));
    		entity.setUpdatename(Struts2Utils.getSessionUser().getName());
    		autodao.merge(entity);
    }
    @Transactional
   public void update(${ab.javabean} entity){
    	entity.setUpdatetime(Struts2Utils.getStringDate(new Date()));
		entity.setUpdatename(Struts2Utils.getSessionUser().getName());
		autodao.merge(entity);
   }
    @Transactional
	public void delete(Integer id){
    	autodao.delete(id);
	}
		
		
		
	public void findPage(Page<${ab.javabean}> page,List<PropertyFilter> filters){
			
		autodao.findPage(page, filters);
	}
		
	public ${ab.javabean} get(Integer id){
		return autodao.get(id);
	}
		
	public List<${ab.javabean}> searchList(${ab.javabean} entity){
		String sql="from ${ab.javabean} where 1=1 ";
		if(entity!=null){
			<#list lac as item>
			
			if(entity.get${item.colUPName}()!=null&&!"".equals(entity.get${item.colUPName}())){
				sql+=" and ${item.colName} like '%"+entity.get${item.colUPName}()+"%' ";
			}
			</#list>
			
			
		}
		List<${ab.javabean}> sl=autodao.createQuery(sql).list();
		return sl;
	}
	
	public  void createExcel(List<${ab.javabean}> li) {
		  //工作簿
		  HSSFWorkbook hssfworkbook = new HSSFWorkbook();
		  //创建sheet页
		  HSSFSheet hssfsheet = hssfworkbook.createSheet();
		  //sheet名称乱码处理
		 // hssfworkbook.setSheetName(0,"${ab.javabean}");
		  //取得第 i 行
		  HSSFRow hssfrow1 = hssfsheet.createRow(0);
		  <#list lac as item>
		  	 HSSFCell hssfcell_00${item_index} = hssfrow1.createCell((short)${item_index});
		 	 hssfcell_00${item_index}.setCellValue("${item.colHeader}");
 			 hssfsheet.setColumnWidth(${item_index}, 12*256);
		  </#list>
		 
		
	

		  for (int i = 0; i < li.size(); i++) {
			  
			  //取得第 i 行
			  HSSFRow hssfrow = hssfsheet.createRow(i+1);
			  <#list lac as item>
			  HSSFCell hssfcell_${item_index} = hssfrow.createCell((short)${item_index});
			  hssfcell_${item_index}.setCellValue(li.get(i).get${item.colUPName}());
 			  </#list>
			  
		  }
		  
		  try {
		    	String uploadFileSavePath=ServletActionContext.getRequest().getRealPath("/documents");
		    	String path=uploadFileSavePath+"/${ab.beannametoLower}.xls";
		    	FileOutputStream file=new FileOutputStream(path);
		    	hssfworkbook.write(file);
		    	file.close();	
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  
		  
		 
	} 	
		
	
}
