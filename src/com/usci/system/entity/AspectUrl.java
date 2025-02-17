package com.usci.system.entity;
import javax.persistence.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.servlet.http.HttpServletRequest;


import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.system.service.InterceptRequestService;
@Service
@Aspect//声明这是一个切面
public class AspectUrl {
	//审核
	@Pointcut(value="execution(* com..*.YxyReportReviewService.save(..))")
	  private void pointCut1(){}
	/**
	 * 去除action set方法，getModel方法，SalarybillAction.readexcel方法
	 */
	  @Pointcut(value="execution(* com..*.*Action.*(..))&&!execution(* com..*.*Action.getModel(..))&&!execution(* com..*.*Action.set*(..))&&!execution(* com..*.SalarybillAction.readexcel(..))")
	  private void pointCut2(){//定义一个切入点 后面的通知直接引入切入点方法pointCut即可           
	  }
	      
	  
	
	    
	    
	  // @Autowired
	  // private UserService us;
	    //前置通知（进入环绕后执行，下一步执行方法）
	    @Before(value="pointCut2()")
	    public void doAccessCheck(JoinPoint point) throws IOException{
	    	/*System.out.println("@Before执行");
	    	String class_name = point.getTarget().getClass().getName();
	        String method_name = point.getSignature().getName();
	        System.out.println("classname:"+class_name+",,,,,method_name:"+method_name);
	    	*/
	        //System.out.println(joinPoint+"@Before前置通知:"+Arrays.toString(joinPoint.getArgs()));
	    }
	    @Autowired
	    private InterceptRequestService irs;
	   // @Around(value="pointCut2()")
	    public Object ceshi(ProceedingJoinPoint pjp) throws Throwable{
	    	Object obj = null;
	    	Object[] args = pjp.getArgs();
	    	System.out.println("cuowu1");
	    	  obj = pjp.proceed(args);
	    	  System.out.println("cuowu2");
	    	  return obj;
	    }
	    //环绕通知（连接到切入点开始执行，下一步进入前置通知，在下一步才是执行操作方法）
	    /**
	     * 拦截所有URL访问信息
	     */
		  //  @Around(value="pointCut2()")
		    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable{
		    	System.out.println("around");
		    	long startTime = System.currentTimeMillis();
		    	Object obj = null;
		    	Object[] args = pjp.getArgs();
		    	HttpServletRequest request = ServletActionContext.getRequest();
		    	
		    	//获取参数
		    	Map<String, String> param = new HashMap<String, String>();
		    	Map<String, String[]> params = request.getParameterMap();
		    	//入库
		    	if(request.getRequestURL().toString().contains("outsample!save.action")){
		    		StringBuilder sb1=new StringBuilder();
		    		
		    	/*	sb1.append("INSERT INTO `uscilims`.`sample_outsample` (");
		    		sb2.append("VALUES(");
		    		for (String key : params.keySet()) {
			            String[] values = params.get(key);
			            
			               // System.out.println(key+":"+values[0]);
			            sb1.append(key+",");
			            
			            sb2.append("'"+values[0]+"',");
			            
			        }*/
		    		sb1.append("BEGIN\n");
		    		sb1.append("/*!*/;\n");
    				sb1.append("# at 22529442\n");
    				sb1.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529552 CRC32 0x71952f8d 	Table_map: `uscilims`.`sample_outsample` mapped to number 315\n");
    				sb1.append("# at 22529552\n");
					sb1.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529764 CRC32 0xb100da4f 	Write_rows: table id 315 flags: STMT_END_F\n");
		    		sb1.append("### INSERT INTO `uscilims`.`sample_outsample`\n");
		    		sb1.append("### SET\n");
		    		int i=1;
		    		for (String key : params.keySet()) {
			            String[] values = params.get(key);
			            
			               // System.out.println(key+":"+values[0]);
			            sb1.append("###   @"+i+"='"+values[0]+"'\n");
			            i++;
			            //sb2.append("'"+values[0]+"',");
			            
			        }
		    		 sb1.append("# at 22529764\n");
		    		 sb1.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529795 CRC32 0xf8241792 	Xid = 18432038\n");
		    		 sb1.append("COMMIT/*!*/;\n");
		    		
		    		// sb2.append(")");
		    		System.out.println(sb1);
		    		//System.out.println(sb2);
		    		BufferedWriter out = null;
		    		try {
		    		out = new BufferedWriter(new OutputStreamWriter(
		    		new FileOutputStream(new File("E:\\yxy.txt"), true)));
		    		out.write(sb1+"\r\n");
		    		} catch (Exception e) {
		    		e.printStackTrace();
		    		} finally {
			    		if(out!=null){
			    			out.close();
			    		}
		    		
		    		
		    		}
		    	}
		    	//下单
		    	if(request.getRequestURL().toString().contains("sypmorder!save.action")){
		    		StringBuilder sb2=new StringBuilder();
		    		

		    		for (String key : params.keySet()) {
			            String[] values = params.get(key);
			            
			                //System.out.println(key+":"+values[0]);
			                List<Map<Object,Object>>	lp=Struts2Utils.conver(values[0],Map.class);
			                for (Map<Object,Object> sy : lp) {
			                	sb2.append("BEGIN\n");
					    		sb2.append("/*!*/;\n");
			    				sb2.append("# at 22529442\n");
			    				sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529552 CRC32 0x71952f8d 	Table_map: `uscilims`.`pm_sypmorder` mapped to number 316\n");
			    				sb2.append("# at 22529552\n");
								sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529764 CRC32 0xb100da4f 	Write_rows: table id 316 flags: STMT_END_F\n");
					    		sb2.append("### INSERT INTO `uscilims`.`pm_sypmorder`\n");
					    		sb2.append("### SET\n");
					    		int i=1;
			                	for (Object key2 : sy.keySet()) {
			                		Object values2 = sy.get(key2);
						               // System.out.println(key+":"+values2);
						                sb2.append("###   @"+i+"='"+values2+"'\n");
							            i++;
			                	}
			                	sb2.append("# at 22529764\n");
					     		sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529795 CRC32 0xf8241792 	Xid = 18432038\n");
					    		sb2.append("COMMIT/*!*/;\n");
					    		//不记录其他检测项目
					    		if(!sb2.toString().contains("优馨益--地中海贫血基因检测")){
			                		sb2=new StringBuilder();
			                	}
							}
			        }
		    		
		    		BufferedWriter out = null;
		    		try {
		    		out = new BufferedWriter(new OutputStreamWriter(
		    		new FileOutputStream(new File("E:\\yxy.txt"), true)));
		    		out.write(sb2+"\r\n");
		    		} catch (Exception e) {
		    		e.printStackTrace();
		    		} finally {
			    		if(out!=null){
			    			out.close();
			    		}
		    		
		    		
		    		}
		    	}
		    	//结果反馈
		    	if(request.getRequestURL().toString().contains("thamaresult!reExport.action")){
		    		StringBuilder sb2=new StringBuilder();
		    		

		    		for (String key : params.keySet()) {
			            String[] values = params.get(key);
			            	if(!"itemsxjyb".equals(key)){
			            		continue;
			            	}
			                //System.out.println(key+":"+values[0]);
			                List<Map<Object,Object>>	lp=Struts2Utils.conver(values[0],Map.class);
			                for (Map<Object,Object> sy : lp) {
			                	sb2.append("BEGIN\n");
					    		sb2.append("/*!*/;\n");
			    				sb2.append("# at 22529442\n");
			    				sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529552 CRC32 0x71952f8d 	Table_map: `uscilims`.`inc_thamaresult` mapped to number 316\n");
			    				sb2.append("# at 22529552\n");
								sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529764 CRC32 0xb100da4f 	Write_rows: table id 316 flags: STMT_END_F\n");
					    		sb2.append("### INSERT INTO `uscilims`.`inc_thamaresult`\n");
					    		sb2.append("### SET\n");
					    		int i=1;
			                	for (Object key2 : sy.keySet()) {
			                		Object values2 = sy.get(key2);
						               // System.out.println(key+":"+values2);
						                sb2.append("###   @"+i+"='"+values2+"'\n");
							            i++;
			                	}
			                	sb2.append("# at 22529764\n");
					     		sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529795 CRC32 0xf8241792 	Xid = 18432038\n");
					    		sb2.append("COMMIT/*!*/;\n");
					    		
							}
			        }
		    		
		    		BufferedWriter out = null;
		    		try {
		    		out = new BufferedWriter(new OutputStreamWriter(
		    		new FileOutputStream(new File("E:\\yxy.txt"), true)));
		    		out.write(sb2+"\r\n");
		    		} catch (Exception e) {
		    		e.printStackTrace();
		    		} finally {
			    		if(out!=null){
			    			out.close();
			    		}
		    		
		    		
		    		}
		    	}
		    	
		    	
		    	
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
		        String json=  gson.toJson(param);
		    	
		    		InterceptRequest ir=new InterceptRequest();
		    	    Timestamp timeStamep = new Timestamp(new Date().getTime());
		    	    ir.setAccessDate(timeStamep);
			    	ir.setAccessIP(Struts2Utils.getIpAddr(request));
			    	ir.setAccessURL(request.getRequestURL().toString());
			    	ir.setAccessName(Struts2Utils.getSessionUser()!=null?Struts2Utils.getSessionUser().getUsername():null);
			    	
			    	boolean wm=irs.WarnMessage(ir,Struts2Utils.getIpAddr(request), request.getRequestURL().toString(), 2, 2, 120);
			    	ir.setAccessParameter(json.toString());
			    	if(wm){
			    		ir.setPushState("是");
			    	}
			    	long endTime = System.currentTimeMillis();
			    	ir.setAccessTimeDifference((endTime-startTime));
			    	irs.save(ir);
			    	
			        obj = pjp.proceed(args);
			    	
		    	  return obj;
		    }
		    private static String[] types = { "java.lang.Integer", "java.lang.Double",
				"java.lang.Float", "java.lang.Long", "java.lang.Short",
				"java.lang.Byte", "java.lang.Boolean", "java.lang.Char",
				"java.lang.String", "int", "double", "long", "short", "byte",
				"boolean", "char", "float" };
		    /**
		     * 获取地贫审核数据
		     * @param pjp
		     * @return
		     * @throws Throwable
		     */
		    @Around(value="pointCut1()")
		    public Object reportreview(ProceedingJoinPoint joinPoint) throws Throwable{
		    	StringBuilder sb2=new StringBuilder();
		    	Object[] args = joinPoint.getArgs();
		    	/*for (Object object : args) {
					System.out.println("object:::"+object);
				}*/	
		    	Map<String, Object> s=getFieldsValue(args[0]);
		    	
		    	sb2.append("BEGIN\n");
	    		sb2.append("/*!*/;\n");
				sb2.append("# at 22529442\n");
				sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529552 CRC32 0x71952f8d 	Table_map: `uscilims`.`report_yxy_reportreview` mapped to number 316\n");
				sb2.append("# at 22529552\n");
				sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529764 CRC32 0xb100da4f 	Write_rows: table id 316 flags: STMT_END_F\n");
	    		sb2.append("### INSERT INTO `uscilims`.`report_yxy_reportreview`\n");
	    		sb2.append("### SET\n");
	    		int i=1;
		    	for (String key:s.keySet()) {
					//System.out.println(key+",,,,,,"+s.get(key));
					 sb2.append("###   @"+i+"='"+s.get(key)+"'\n");
					 i++;
				}
		    	sb2.append("# at 22529764\n");
	     		sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529795 CRC32 0xf8241792 	Xid = 18432038\n");
	    		sb2.append("COMMIT/*!*/;\n");
	    		
	    		BufferedWriter out = null;
	    		try {
	    		out = new BufferedWriter(new OutputStreamWriter(
	    		new FileOutputStream(new File("E:\\yxy.txt"), true)));
	    		out.write(sb2+"\r\n");
	    		} catch (Exception e) {
	    		e.printStackTrace();
	    		} finally {
		    		if(out!=null){
		    			out.close();
		    		}
	    		
	    		
	    		}
	    		
		    	
		    	
		        return joinPoint.proceed(args);
		    }
		    
		    /**
		     * 获取地贫报告数据
		     * @param pjp
		     * @return
		     * @throws Throwable
		     */
		    @Around(value="execution(* com..*.YxyReportIssueService.save(..))")
		    public Object reportissue(ProceedingJoinPoint joinPoint) throws Throwable{
		    	StringBuilder sb2=new StringBuilder();
		    	Object[] args = joinPoint.getArgs();
		    	/*for (Object object : args) {
					System.out.println("object:::"+object);
				}*/	
		    	Map<String, Object> s=getFieldsValue(args[0]);
		    	
		    	sb2.append("BEGIN\n");
	    		sb2.append("/*!*/;\n");
				sb2.append("# at 22529442\n");
				sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529552 CRC32 0x71952f8d 	Table_map: `uscilims`.`report_yxy_reportissue` mapped to number 316\n");
				sb2.append("# at 22529552\n");
				sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529764 CRC32 0xb100da4f 	Write_rows: table id 316 flags: STMT_END_F\n");
	    		sb2.append("### INSERT INTO `uscilims`.`report_yxy_reportissue`\n");
	    		sb2.append("### SET\n");
	    		int i=1;
		    	for (String key:s.keySet()) {
					//System.out.println(key+",,,,,,"+s.get(key));
					 sb2.append("###   @"+i+"='"+s.get(key)+"'\n");
					 i++;
				}
		    	sb2.append("# at 22529764\n");
	     		sb2.append("#"+Struts2Utils.getStringDate(new Date())+" server id 1  end_log_pos 22529795 CRC32 0xf8241792 	Xid = 18432038\n");
	    		sb2.append("COMMIT/*!*/;\n");
	    		
	    		BufferedWriter out = null;
	    		try {
	    		out = new BufferedWriter(new OutputStreamWriter(
	    		new FileOutputStream(new File("E:\\yxy.txt"), true)));
	    		out.write(sb2+"\r\n");
	    		} catch (Exception e) {
	    		e.printStackTrace();
	    		} finally {
		    		if(out!=null){
		    			out.close();
		    		}
	    		
	    		
	    		}
	    		
		    	
		    	
		        return joinPoint.proceed(args);
		    }
		    
			/**
			 * 得到实体参数的值
			 * @param obj
			 */
			public static Map<String, Object> getFieldsValue(Object obj) {
				Field[] fields = obj.getClass().getDeclaredFields();
				String typeName = obj.getClass().getName();
				for (String t : types) {
					if(t.equals(typeName))
						return null;
				}
				Map<String, Object> hm=new HashMap<String, Object>();
				for (Field f : fields) {
					f.setAccessible(true);//private修饰的属性，需要设置true才能允许反射
					try {
						for (String str : types) {
							if (f.getType().getName().equals(str)){
								hm.put(f.getName(), f.get(obj));
							}
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				
				return hm;
			}
			
}

