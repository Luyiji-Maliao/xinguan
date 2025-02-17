package com.lims.core.utils.web;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**   
 * CSV操作(导出和导入)
 *
 * @author 林计钦
 * @version 1.0 Jan 27, 2014 4:30:58 PM   
 */
public class CSVUtils {
    
    /**
     * 导出
     * 
     * @param file csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据
     * @return
     */
    public static boolean exportCsv(File file, List<String> dataList){
        boolean isSucess=false;
        
        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.append(data).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }
        
        return isSucess;
    }
    
    /**
     * 导入
     * 
     * @param file csv文件(路径+文件)
     * @return
     */
    public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();
        
        BufferedReader br=null;
        try { 
            br = new BufferedReader(new FileReader(file));
            String line = ""; 
            while ((line = br.readLine()) != null) { 
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        return dataList;
    }
    /**
     * 下载文件
     * @param response
     * @param csvFilePath
     *       文件路径
     * @param fileName
     *       文件名称
     * @throws IOException
     */
    public static void exportFile(HttpServletResponse response, String csvFilePath, String fileName)
                                                    throws IOException {
      response.setContentType("application/csv;charset=UTF-8");
      response.setHeader("Content-Disposition",
        "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
   
      InputStream in = null;
      try {
        in = new FileInputStream(csvFilePath);
        int len = 0;
        byte[] buffer = new byte[1024];
        response.setCharacterEncoding("UTF-8");
        OutputStream out = response.getOutputStream();
        while ((len = in.read(buffer)) > 0) {
          out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
          out.write(buffer, 0, len);
        }
      } catch (FileNotFoundException e) {
       
      } finally {
        if (in != null) {
          try {
            in.close();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      }
    }

}