package com.usci.xgcollectinfo.service;

import com.lims.core.orm.Page;
import com.usci.xgcollectinfo.dao.xgcollectinfoDao;
import com.usci.xgcollectinfo.entity.xgcollectinfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class Xgcollectinfo2Service {
    @Autowired
    private xgcollectinfoDao xgcollectinfoDao;

    /**
     * 添加
     * @param xgcollectinfo
     * @return
     */
    @Transactional
    public int save (xgcollectinfo xgcollectinfo){
        int isOk = 1;
        try{
            xgcollectinfoDao.save(xgcollectinfo);
        }catch (Exception e){
            isOk = 0;
        }
        HttpServletRequest request= ServletActionContext.getRequest();
//        request.setAttribute("xgList",this.find());
        return isOk;
    }

    public void findpage(Page<xgcollectinfo> page, String sql, int start, int limit) {
        String oj=xgcollectinfoDao.queryBySql("select count(1) from ("+sql+") as total").get(0).toString();
        sql+=" LIMIT "+start+" , "+limit;
        List<xgcollectinfo> lcs=xgcollectinfoDao.entityBySql(sql, xgcollectinfo.class);
        page.setResult(lcs);
        page.setTotalCount(Long.parseLong(oj.toString()));
    }

    /**
     * 获取当天该身份证号是否录入过
     * @param certNumber
     * @return
     */
    public List<xgcollectinfo> getNowAll(String certNumber){
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        String date = s.format(d).toString();
        String hql = "from xgcollectinfo where inputtime like '"+date+"%' and certNumber = '"+certNumber+"'";

        Query query = xgcollectinfoDao.createQuery(hql);
        List<xgcollectinfo> lcs =query.list();
        return lcs;
    }

//    public List<xgcollectinfo> entityBySql(String sql){
//        List<xgcollectinfo> list = xgcollectinfoDao.entityBySql(sql, xgcollectinfo.class);
//        return list;
//    }

    /**
     * 根据sql查询
     * @param sql
     * @return  List
     */
    public List<xgcollectinfo> queryBySql(String sql){
        return xgcollectinfoDao.entityBySql(sql,xgcollectinfo.class);
    }



    public void excel(List<xgcollectinfo> lc,String str){
        HSSFWorkbook hssfworkbook = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet hssfsheet = hssfworkbook.createSheet(str);

        //sheet名称乱码处理
        hssfworkbook.setSheetName(0,str);
        //取得第 i 行
        HSSFRow hssfrow1 = hssfsheet.createRow(0);
        //创建第一个单元格
        HSSFCellStyle style77 = hssfworkbook.createCellStyle();
        HSSFFont font77 = hssfworkbook.createFont();
        font77.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        style77.setFont(font77);

        HSSFCell hssfcell_01 = hssfrow1.createCell((short)0);
        hssfcell_01.setCellValue("样本编号");
        hssfsheet.setColumnWidth(0, 20*256);
        hssfcell_01.setCellStyle(style77);

        HSSFCell hssfcell_02 = hssfrow1.createCell((short)1);
        hssfcell_02.setCellValue("姓名");
        hssfsheet.setColumnWidth(1, 20*256);
        hssfcell_02.setCellStyle(style77);
        HSSFCell hssfcell_03 = hssfrow1.createCell((short)2);
        hssfcell_03.setCellValue("手机号");
        hssfsheet.setColumnWidth(2, 20*256);
        hssfcell_03.setCellStyle(style77);
        HSSFCell hssfcell_04 = hssfrow1.createCell((short)3);
        hssfcell_04.setCellValue("身份证号");
        hssfsheet.setColumnWidth(3, 20*256);
        hssfcell_04.setCellStyle(style77);
        HSSFCell hssfcell_05 = hssfrow1.createCell((short)4);
        hssfcell_05.setCellValue("采样地点");
        hssfsheet.setColumnWidth(4, 20*256);
        hssfcell_05.setCellStyle(style77);
        HSSFCell hssfcell_06 = hssfrow1.createCell((short)5);
        hssfcell_06.setCellValue("五合一管编号");
        hssfsheet.setColumnWidth(5, 20*256);
        hssfcell_06.setCellStyle(style77);

        HSSFCell hssfcell_09 = hssfrow1.createCell((short)6);
        hssfcell_09.setCellValue("录入时间");
        hssfsheet.setColumnWidth(6, 20*256);
        hssfcell_09.setCellStyle(style77);

        HSSFCell hssfcell_11 = hssfrow1.createCell((short)7);
        hssfcell_11.setCellValue("录入人");
        hssfsheet.setColumnWidth(7, 20*256);
        hssfcell_11.setCellStyle(style77);


        for(int i=0;i<lc.size();i++){

            HSSFRow hssfrow2 = hssfsheet.createRow(i+1);
            HSSFCell nhssfcell_01 = hssfrow2.createCell((short)0);
            nhssfcell_01.setCellValue(lc.get(i).getSamplenum());
            HSSFCell nhssfcell_02 = hssfrow2.createCell((short)1);
            nhssfcell_02.setCellValue(lc.get(i).getPartyName());
            HSSFCell nhssfcell_03 = hssfrow2.createCell((short)2);
            nhssfcell_03.setCellValue(lc.get(i).getPhone());
            HSSFCell nhssfcell_04 = hssfrow2.createCell((short)3);
            nhssfcell_04.setCellValue(lc.get(i).getCertNumber());
            HSSFCell nhssfcell_05 = hssfrow2.createCell((short)4);
            nhssfcell_05.setCellValue(lc.get(i).getInputAddress());
            HSSFCell nhssfcell_06 = hssfrow2.createCell((short)5);
            nhssfcell_06.setCellValue(lc.get(i).getFiveone());
            HSSFCell nhssfcell_07 = hssfrow2.createCell((short)6);
            nhssfcell_07.setCellValue(lc.get(i).getInputtime());
            HSSFCell nhssfcell_08 = hssfrow2.createCell((short)7);
            nhssfcell_08.setCellValue(lc.get(i).getInputname());
        }
        FileOutputStream file=null;
        try {
            String uploadFileSavePath=ServletActionContext.getServletContext().getRealPath("/documents");
            String path=uploadFileSavePath+"/核算检测信息记录.xls";
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
     * 批量录入模板
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
        hssfcell_00.setCellValue("样本编号");
        hssfsheet.setColumnWidth(0, 10*256);

        HSSFCell hssfcell_01 = hssfrow1.createCell((short)1);
        hssfcell_01.setCellValue("姓名");
        hssfsheet.setColumnWidth(1, 10*256);
        hssfcell_01.setCellStyle(style77);

        HSSFCell hssfcell_02 = hssfrow1.createCell((short)2);
        hssfcell_02.setCellValue("手机号");
        hssfsheet.setColumnWidth(2, 10*256);
        hssfcell_02.setCellStyle(style77);

        HSSFCell hssfcell_03 = hssfrow1.createCell((short)3);
        hssfcell_03.setCellValue("身份证号");
        hssfsheet.setColumnWidth(3, 20*256);
        hssfcell_03.setCellStyle(style77);
        HSSFCell hssfcell_04 = hssfrow1.createCell((short)4);
        hssfcell_04.setCellValue("采样地点");
        hssfsheet.setColumnWidth(4, 10*256);
        hssfcell_04.setCellStyle(style77);

        HSSFCell hssfcell_05 = hssfrow1.createCell((short)5);
        hssfcell_05.setCellValue("五合一管编号");
        hssfsheet.setColumnWidth(5, 20*256);
        hssfcell_05.setCellStyle(style77);


        HSSFCell hssfcell_06 = hssfrow1.createCell((short)6);
        hssfcell_06.setCellValue("录入时间");
        hssfsheet.setColumnWidth(6, 20*256);
        hssfcell_06.setCellStyle(style77);

        HSSFCell hssfcell_07 = hssfrow1.createCell((short)7);
        hssfcell_07.setCellValue("录入人");
        hssfsheet.setColumnWidth(7, 20*256);
        hssfcell_07.setCellStyle(style77);




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

            HSSFCell hssfcell_3 = hssfrow.createCell((short)3);
            hssfcell_3.setCellValue("");
            hssfcell_3.setCellStyle(style);

            HSSFCell hssfcell_4 = hssfrow.createCell((short)4);
            hssfcell_4.setCellValue("");
            hssfcell_4.setCellStyle(style);

            HSSFCell hssfcell_6 = hssfrow.createCell((short)5);
            hssfcell_6.setCellValue("");
            hssfcell_6.setCellStyle(style);

            HSSFCell hssfcell_7 = hssfrow.createCell((short)6);
            hssfcell_7.setCellValue("");
            hssfcell_7.setCellStyle(style);

            HSSFCell hssfcell_8 = hssfrow.createCell((short)7);
            hssfcell_8.setCellValue("");
            hssfcell_8.setCellStyle(style);
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
     * 根据五合一编号查询所有样本  并且样本isshow状态为0
     * @param fiveone
     * @return
     */
    public List<xgcollectinfo> getByfiveone(String fiveone){
        String sql = "select * from xg_collectinfo where fiveone = '"+fiveone+"' and isshow = 0";
        return xgcollectinfoDao.entityBySql(sql,xgcollectinfo.class);
    }

    public xgcollectinfo findbySamplenum(String samplenum){
        String hql = "from xgcollectinfo where samplenum = '"+samplenum+"' and isshow = 0";
        Query query = xgcollectinfoDao.createQuery(hql);
        List<xgcollectinfo> lcs =query.list();
        xgcollectinfo xgcollectinfo = null;
        if(lcs.size()!=0){
            xgcollectinfo = lcs.get(0);
        }
        return xgcollectinfo;
    }

    public xgcollectinfo findUniqueBy(Integer id){
        return  xgcollectinfoDao.findUniqueBy("id",id);
    }

    @Transactional
    public void update(xgcollectinfo xgcollectinfo){
        xgcollectinfoDao.update(xgcollectinfo);
    }

}
