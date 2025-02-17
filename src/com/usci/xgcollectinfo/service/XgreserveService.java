package com.usci.xgcollectinfo.service;

import com.lims.core.orm.Page;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.xgcollectinfo.dao.XgreserveDao;
import com.usci.xgcollectinfo.entity.Xgreserve;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class XgreserveService {
    @Autowired
    private XgreserveDao xgreserveDao;


    /**
     * 执行sql语句
     */
    public List<Xgreserve> queryBySql(String sql){
        return xgreserveDao.queryBySql(sql);
    }


    public void findpage(Page<Xgreserve> page, String sql, int start, int limit){
        String oj=xgreserveDao.queryBySql("select count(1) from ("+sql+") as total").get(0).toString();
        sql+=" LIMIT "+start+" , "+limit;
        List<Xgreserve> lcs=xgreserveDao.entityBySql(sql, Xgreserve.class);
        page.setResult(lcs);
        page.setTotalCount(Long.parseLong(oj.toString()));
    }

    /**
     * 录入模板
     */
    public void excelmuban(){
        HSSFWorkbook hssfworkbook = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet hssfsheet = hssfworkbook.createSheet("luru");

        //sheet名称乱码处理
        hssfworkbook.setSheetName(0,"luru");
        //取得第 i 行
        HSSFRow hssfrow1 = hssfsheet.createRow(0);
        //创建第一个单元格

        HSSFCellStyle style77 = hssfworkbook.createCellStyle();
        HSSFFont font77 = hssfworkbook.createFont();
        font77.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        style77.setFont(font77);
        style77.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style77.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style77.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style77.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style77.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style77.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        HSSFCell hssfcell_00 = hssfrow1.createCell((short)0);
        hssfcell_00.setCellStyle(style77);
        hssfcell_00.setCellValue("姓名");
        hssfsheet.setColumnWidth(0, 10*256);

        HSSFCell hssfcell_01 = hssfrow1.createCell((short)1);
        hssfcell_01.setCellValue("身份证号");
        hssfsheet.setColumnWidth(1, 20*256);
        hssfcell_01.setCellStyle(style77);

        HSSFCell hssfcell_02 = hssfrow1.createCell((short)2);
        hssfcell_02.setCellValue("手机号");
        hssfsheet.setColumnWidth(2, 20*256);
        hssfcell_02.setCellStyle(style77);

        for(int i = 0;i<29;i++){
            //取得第 i 行
            HSSFRow hssfrow = hssfsheet.createRow(i+1);
            HSSFCell hssfcell_0 = hssfrow.createCell((short)0);
            hssfcell_0.setCellValue("");
            HSSFCellStyle style = hssfworkbook.createCellStyle();  //样式
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            hssfcell_0.setCellStyle(style);

            HSSFCell hssfcell_1 = hssfrow.createCell((short)1);
            hssfcell_1.setCellValue("");
            hssfcell_1.setCellStyle(style);

            HSSFCell hssfcell_2 = hssfrow.createCell((short)2);
            hssfcell_2.setCellValue("");
            hssfcell_2.setCellStyle(style);
        }

        FileOutputStream file=null;
        try {
            String uploadFileSavePath= ServletActionContext.getServletContext().getRealPath("/documents");
            String path=uploadFileSavePath+"/luru.xls";
            file=new FileOutputStream(path);
            hssfworkbook.write(file);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(file!=null){
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据身份证号获取对象
     * @param certNumber
     * @return
     */
    public Xgreserve findbycertNumber(String certNumber){
        return xgreserveDao.findUniqueBy("certNumber",certNumber);
    }

    /**
     * 根据id获取对象
     * @param id
     * @return
     */
    public Xgreserve findbyid(Integer id){
        return xgreserveDao.findUniqueBy("id",id);
    }

    @Transactional
    public void save(Xgreserve xgreserve){
        xgreserve.setInputtime(Struts2Utils.getStringDate(new Date()));
        xgreserve.setInputname(Struts2Utils.getSessionUser().getName());
        xgreserveDao.save(xgreserve);
    }

    @Transactional
    public void delete(Xgreserve xgreserve){
        xgreserveDao.delete(xgreserve);
    }

    @Transactional
    public void update(Xgreserve xgreserve){
        xgreserve.setUpdatetime(Struts2Utils.getStringDate(new Date()));
        xgreserve.setUpdatename(Struts2Utils.getSessionUser().getName());
        xgreserveDao.update(xgreserve);
    }

}
