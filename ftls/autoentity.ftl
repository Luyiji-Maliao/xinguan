package ${ab.packagebeanurl};

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "${ab.tabname}")
public class ${ab.beanname} {
	@Id
	@GeneratedValue
	private Integer ${ab.beannametoLower}id;
	
	<#list lac as item>
	//${item.colHeader}
	<#if  (item.colLong)??&&item.colLong!="">
	@Column(length=${item.colLong})
	</#if>
	private ${item.colType} ${item.colName};
	</#list>
	
	
	
	
	<#list lac as item>
	public ${item.colType} get${item.colUPName}() {
		return ${item.colName};
	}
	public void set${item.colUPName}(${item.colType} ${item.colName}) {
		this.${item.colName} = ${item.colName};
	}
	</#list>
	
	
	public Integer get${ab.beannametoUpper}id() {
		return ${ab.beannametoLower}id;
	}


	public void set${ab.beannametoUpper}id(Integer ${ab.beannametoLower}id) {
		this.${ab.beannametoLower}id = ${ab.beannametoLower}id;
	}
	


			
}
