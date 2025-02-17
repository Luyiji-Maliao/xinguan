package com.lims.core.utils.web;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;

import sun.misc.BASE64Encoder;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class WordGenerator {
	private static Configuration configuration = null;
	private static Map<String, Template> allTemplates = null;
	
	static {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		configuration.setClassForTemplateLoading(WordGenerator.class, "/com/lims/ftl");
		allTemplates = new HashMap<String, Template>();	// Java 7 钻石语法
		try {
			allTemplates.put("yt", configuration.getTemplate("3-2.ftl"));
			allTemplates.put("yy3", configuration.getTemplate("yy3.ftl"));
			allTemplates.put("549", configuration.getTemplate("549-1.ftl"));
			allTemplates.put("ycdxb", configuration.getTemplate("ycdxb.ftl"));
			allTemplates.put("thama", configuration.getTemplate("thama.ftl"));
			allTemplates.put("21", configuration.getTemplate("21.ftl"));
			allTemplates.put("msi", configuration.getTemplate("msi.ftl"));
			allTemplates.put("tumorcode", configuration.getTemplate("tumorcode.ftl"));
			allTemplates.put("yesuan", configuration.getTemplate("yesuan.ftl"));
			allTemplates.put("yxy", configuration.getTemplate("yxy.ftl"));
			allTemplates.put("yesuan1", configuration.getTemplate("yesuan1.ftl"));
			allTemplates.put("npusci", configuration.getTemplate("npusci.ftl"));
			allTemplates.put("npqhd", configuration.getTemplate("npqhd.ftl"));
			allTemplates.put("npxinan", configuration.getTemplate("npxinan.ftl"));
			allTemplates.put("allview", configuration.getTemplate("allview.ftl"));
			allTemplates.put("368", configuration.getTemplate("368.ftl"));
			allTemplates.put("zlyg", configuration.getTemplate("zlyg.ftl"));
			allTemplates.put("erlong", configuration.getTemplate("erlong.ftl"));
			allTemplates.put("yrfuse", configuration.getTemplate("yrfuse.ftl"));
			allTemplates.put("yrmutation", configuration.getTemplate("yrmutation.ftl"));
			allTemplates.put("pdlone", configuration.getTemplate("pdlone.ftl"));
			allTemplates.put("npchengde", configuration.getTemplate("npchengde.ftl"));
			allTemplates.put("ytdanxiang", configuration.getTemplate("ytdanxiang.ftl"));
			allTemplates.put("new21", configuration.getTemplate("new21.ftl"));
			allTemplates.put("mxyg", configuration.getTemplate("mxyg.ftl"));
			allTemplates.put("kmfy", configuration.getTemplate("kmfy.ftl"));
			allTemplates.put("newdx", configuration.getTemplate("newdx.ftl"));
			allTemplates.put("thal", configuration.getTemplate("thal.ftl"));//地贫分析结果反馈
			allTemplates.put("xinshai", configuration.getTemplate("xinshai.ftl"));
			allTemplates.put("newtable", configuration.getTemplate("newtable.ftl"));
			allTemplates.put("negative", configuration.getTemplate("negative.ftl"));
			allTemplates.put("positiveAll", configuration.getTemplate("positiveAll.ftl"));
			allTemplates.put("npgz", configuration.getTemplate("npgz.ftl"));
/**			allTemplates.put("npzxgz", configuration.getTemplate("npzxgz.ftl"));//nipt甄选版广州妇幼报告模板*/
			allTemplates.put("npscrmyy", configuration.getTemplate("npscrmyy.ftl"));
			allTemplates.put("npusci2", configuration.getTemplate("npusci2.ftl"));
			allTemplates.put("npqhd2", configuration.getTemplate("npqhd2.ftl"));
			allTemplates.put("npkmfy", configuration.getTemplate("npkmfy.ftl"));
			allTemplates.put("npuscizx", configuration.getTemplate("npuscizx.ftl"));
			allTemplates.put("npchengde2", configuration.getTemplate("npchengde2.ftl"));
			allTemplates.put("cqfy", configuration.getTemplate("cqfy.ftl"));//重庆妇幼报告模板
			allTemplates.put("supplyreport", configuration.getTemplate("supplyreport.ftl"));//补充报告模板
			allTemplates.put("npuscizx2", configuration.getTemplate("npuscizx2.ftl"));
			allTemplates.put("plusqhd", configuration.getTemplate("plusqhd.ftl"));//联合实验室暂时寄优迅的秦皇岛模板
			allTemplates.put("ybfyzx2positive", configuration.getTemplate("ybfyzx2positive.ftl"));//延边妇幼甄选升级2.0报告模板阳性模板
			allTemplates.put("ybfyzx2negative", configuration.getTemplate("ybfyzx2negative.ftl"));//延边妇幼甄选升级2.0报告模板阴性模板
            allTemplates.put("sxzxpositive", configuration.getTemplate("sxzxpositive.ftl"));//陕西甄选版2.0报告模板阳性模板
            allTemplates.put("sxzxsexornegative", configuration.getTemplate("sxzxsexornegative.ftl"));//陕西甄选版2.0报告模板阴性或性染色体异常模板
            allTemplates.put("zxgz", configuration.getTemplate("zxgz.ftl"));//甄选升级2.0广州妇儿报告模板阴性模板
            allTemplates.put("oldnpusci", configuration.getTemplate("oldnpusci.ftl"));//PLUS 地区代码DE样本用旧模板
            allTemplates.put("oldnpusci2", configuration.getTemplate("oldnpusci2.ftl"));//PLUS 地区代码DE样本用旧模板
        } catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private WordGenerator() {
		throw new AssertionError();
	}

	public static File createDoc(Map<?, ?> dataMap, String type) {
		String name = "temp" + (int) (Math.random() * 100000) + ".doc";
		File f = new File(name);
		Template t = allTemplates.get(type);
		try {
			// 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
			
			Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
			t.process(dataMap, w);
			w.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return f;
	}
	/**
	 * 地贫批量自动化
	 * */
	public static File createDocThama(Map<?, ?> dataMap, String type,String path,String sampleNo) {
		//String name = "temp" + (int) (Math.random() * 100000) + ".doc";
		if (!new File(path).exists()){//判断目录是否存在
			new File(path).mkdirs();
		}
		String fileName=sampleNo + ".doc";
		File f = new File(path,fileName);
		Template t = allTemplates.get(type);
		try {
			// 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
			Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
			t.process(dataMap, w);
			w.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return f;
	}
	
	/**  
	 * 传入数据 可直接本地生成word  
	 * @param dataMap  
	 */  
	public static void createDoc(Map<String, Object> dataMap) {  

	    // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，  
	    // ftl文件存放路径  
	    configuration.setClassForTemplateLoading(WordGenerator.class, "/com/lims/ftl");  

	    Template t = null;  
	    try {  
	        // test.ftl为要装载的模板  
	        t = configuration.getTemplate("yy5.ftl");  
	        t.setEncoding("utf-8");  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

	    // 输出文档路径及名称  
	    File outFile = new File("e:/'" + Math.random()*10000 + "'2.doc");  
	    Writer out = null;  
	    try {  
	        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));  
	    } catch (Exception e1) {  
	        e1.printStackTrace();  
	    }  

	    try {  
	        t.process(dataMap, out);  
	        out.close();  
	    } catch (TemplateException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

	}  
	
	 /**  
     *   
     * 注意dataMap里存放的数据Key值要与模板中的参数相对应  
     *   
     * @param dataMap  
     */  
    public static Map<String, Object> getData() {  
        Map<String, Object> dataMap = new HashMap<String, Object>();  
        Map<String, Object> map1 = new HashMap<String, Object>();  
        Map<String, Object> map2 = new HashMap<String, Object>();  
  
        //map1.put("title", "国泰君安*公司研究*广发证券：定增完成，如虎添翼*000776*投资银行业与经纪业行业*梁静");  
        //map1.put("content","报告类型=公司事件点评公司简称=广发证券公司代码=000776报告日期=Thu Aug 25 09:05:29 CST 2011研究员 =梁静报告标题=定增完成");  
        //map1.put("site", "上海证卷报");  
        /*String img1 = getImageStr("e:/3.png");  
        map1.put("image1", img1);  
        map1.put("asb", 1);//标识图片  
        String img2 = getImageStr("e:/12.png");  
        map1.put("image2", img2);  
        map1.put("asb", 2);//标识图片  
*/  
        //map2.put("title", "[申万销售夏敬慧] 基金仓位周报----开基仓位下降1.51%");  
        //map2.put("content","理财产品部分析师: 杨鹏（18930809297） 开基仓位有所下降：本周，开放式基金平均仓位继续下降。");  
        //map2.put("site", "上海证卷报");  
       // String img2 = getImageStr("e:/3.png");  
       // map2.put("imgs", img2);  
        //map2.put("i", 2);//标识图片  
  
        List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();  
        newsList.add(map1);  
       // newsList.add(map2);  
       // dataMap.put("newsList", newsList);  
        return map1;  
    }  
    /**  
     *   
     * 远程图片存在本地
     *   
     * 
     */  
    public static void copyFileFromURL(URL source, File destination) throws IOException {
    	 
        InputStream input = null;
        FileOutputStream output = null;
        byte[] buffer = new byte[1024];
         
        input = source.openStream();
 
        if (destination.exists()) {
            if (destination.isDirectory()) {
                throw new IOException("File '" + destination
                        + "' exists but is a directory");
            }
            if (destination.canWrite() == false) {
                throw new IOException("File '" + destination
                        + "' cannot be written to");
            }
        } else {
            File parent = destination.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent
                            + "' could not be created");
                }
            }
        }
 
        output = new FileOutputStream(destination, true);
 
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        output.close();
        input.close();
    }
    /**  
     *   
     * 远程调用图片
     *   
     * 
     */  
    public static String getImageStrs(String imgFile) {
    	String num[]=imgFile.split("/");
    	String names=num[num.length-1];
    	String deskFileName= Struts2Utils.getStringDatehmss(new Date())+names;
    	String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath("/upload/yxy") + File.separator;
    	if (!new File(uploadFileSavePath).exists()){//判断报告目录是否存在
    		new File(uploadFileSavePath).mkdirs();
    	}
    	File file=new File(uploadFileSavePath,deskFileName); 	
     		URL url;
			try {
				url = new URL(imgFile);
				try {
					copyFileFromURL(url,file);
				} catch (IOException e) {				
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}  
			if(getImageStr(file.getPath())==null){
				return "";
			}else{
				return getImageStr(file.getPath());
			}
     		
     		
     }  
    public static String getImageStr(String imgFile) {  
        InputStream in = null;  
        byte[] data = null;
        String s="";
        try {  
        	
            in = new FileInputStream(imgFile);  
            if(in==null){
            	s="";
            }else{
                data = new byte[in.available()];  
                in.read(data);  
                in.close();  
                BASE64Encoder encoder = new BASE64Encoder(); 
                s=encoder.encode(data);
            }
            
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          return s;
    }  
  
    public static void main(String[] args) {  
        WordGenerator word = new WordGenerator();  
        Map<String,Object> map = WordGenerator.getData();  
        word.createDoc(map);  
    } 
  
	/**
	 * ftl测试用方法:调试ftl文件不需要重启
	 * @param dataMap
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static File createDoc(Map<?, ?> dataMap, String type ,String ftl) throws IOException {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		configuration.setClassForTemplateLoading(WordGenerator.class, "/com/lims/ftl");
		HashMap<String, Template> allTemplate = new HashMap<String, Template>();	// Java 7 钻石语法
		allTemplate.put(type, configuration.getTemplate(ftl));
		String name = "temp" + (int) (Math.random() * 100000) + ".doc";
		File f = new File(name);
		Template t = allTemplate.get(type);
		try {
			// 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
			Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
			t.process(dataMap, w);
			w.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return f;
	}

}
