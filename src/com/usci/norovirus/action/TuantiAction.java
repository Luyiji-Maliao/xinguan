package com.usci.norovirus.action;

import com.lims.core.orm.Page;
import com.lims.core.utils.excel.WriteExcel;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.QrCodeUtils;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.entity.*;
import com.usci.norovirus.entity.Tuanti;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.norovirus.service.NorovirusService;
import com.usci.norovirus.service.TuantiService;
import com.usci.norovirus.service.TuantiService;
import com.usci.system.service.SendUrlService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })

@Results({
        @Result(name = "logins", type = "redirect", location = "session.jsp"),
        @Result(name = "modulepage", location = "/WEB-INF/content/norovirus/tuanti.jsp"),
})
public class TuantiAction extends CrudActionSupport<Tuanti> {

    private Tuanti entity;

    private String itemsxjyb;

    private String searchName;

    private String ininputTime;

    private String osoinputTime;
    private String startDate;
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIninputTime() {
        return ininputTime;
    }

    public void setIninputTime(String ininputTime) {
        this.ininputTime = ininputTime;
    }

    public String getOsoinputTime() {
        return osoinputTime;
    }

    public void setOsoinputTime(String osoinputTime) {
        this.osoinputTime = osoinputTime;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }


    public String getItemsxjyb() {
        return itemsxjyb;
    }

    public void setItemsxjyb(String itemsxjyb) {
        this.itemsxjyb = itemsxjyb;
    }

    @Autowired
    private TuantiService tuantiService;

    @Autowired
    private AppointmentinfoxgwxService appointmentinfoxgwxService;

    @Autowired
    private NorovirusService norovirusService;

    @Override
    public String list() throws Exception {
        String sql= this.getSql("DESC");
        Page<Tuanti> page = new Page<Tuanti>(limit);
        page.setPageNo((start / limit) + 1);
        tuantiService.findPage(page,sql,"",start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }

    public String getSql(String orderby){

        String sql = "select a.id,a.tuanname,a.code,a.subjecttype,a.yuyuedate,a.yuyuetime,a.caiyangdidian,a.isfapiao,a.fapiaotaitou,a.zhucedizhi," +
                "a.zhucedianhua,a.kaihuyinhang,a.yinhangzhanghao,a.fapiaotype,a.accessname,a.accessaddress,a.xgreason,a.qudao," +
                "a.sampleType,a.sending,a.checkProject,a.inspection,a.inputTime,a.imgpath,a.shuihao,a.inputName,a.updateTime,a.updateName,a.isjiesuan from " +
                "xg_tuanti a where 1=1 ";
        if(entity.getTuanname()!=null && !entity.getTuanname().equals("")){
            sql+=" AND a.tuanname like '%"+entity.getTuanname()+"%' ";
        }
        if(entity.getCaiyangdidian()!=null && !entity.getCaiyangdidian().equals("")){
            sql+=" AND a.caiyangdidian like '%"+entity.getCaiyangdidian()+"%'";
        }
        if(entity.getSending()!=null && !entity.getSending().equals("")){
            sql+=" AND a.sending like '%"+entity.getSending()+"%'";
        }
        if(entity.getXgreason()!=null && !entity.getXgreason().equals("")){
            sql+=" AND a.xgreason like '%"+entity.getXgreason()+"%'";
        }
        if(entity.getFapiaotaitou()!=null && !entity.getFapiaotaitou().equals("")){
            sql+=" AND a.fapiaotaitou like '%"+entity.getFapiaotaitou()+"%'";
        }
        if(entity.getAccessname()!=null && !entity.getAccessname().equals("")){
            sql+=" AND a.accessname like '%"+entity.getAccessname()+"%'";
        }
        if(entity.getCode()!=null && !entity.getCode().equals("")){
            sql+=" AND a.code like '%"+entity.getCode()+"%'";
        }
        if(entity.getShuihao()!=null && !entity.getShuihao().equals("")){
            sql+=" AND a.shuihao like '%"+entity.getShuihao()+"%'";
        }
        if(startDate!=null && !startDate.equals("")){
            sql+=" AND date(yuyuedate)>= date('"+startDate+"') ";
        }
        if(endDate!=null && !endDate.equals("")){
            sql+=" AND date(yuyuedate) <= date('"+endDate+"') ";
        }
        sql+=" order by a.id " +orderby;

        return sql;
    }

    public void istuanname(){
        List<Tuanti> ln=Struts2Utils.conver(itemsxjyb,Tuanti.class);
        StringBuffer sb = new StringBuffer("");
        for (Tuanti a:ln) {
            Tuanti tuanti =  tuantiService.findUniqueBytuanname(a.getTuanname());
            if(tuanti!=null){
                sb.append(tuanti.getTuanname()+",");
            }
        }
        msg.setMsg(sb.toString());
        Struts2Utils.renderJson(msg);
    }




    public String saveAll() throws Exception{
        List<Tuanti> ln=Struts2Utils.conver(itemsxjyb,Tuanti.class);
        for(int i=0;i<ln.size();i++){
            Tuanti tuanti = tuantiService.findUniqueBytuanname(ln.get(i).getTuanname());
            String uploadFileSavePath= ServletActionContext.getServletContext().getRealPath("/upload/xgtuanti/");
            String code = (int) (Math.random() * 9000 + 1000) + "";
            if(tuanti==null){
                ln.get(i).setSampleType("咽拭子");
                ln.get(i).setCheckProject("新型冠状病毒 2019-nCoV 核酸检测");
                ln.get(i).setInspection("优迅医学");
                ln.get(i).setIsjiesuan("否");
                ln.get(i).setCode(code);
                tuantiService.save(ln.get(i));
                Tuanti tuantiid = tuantiService.findUniqueBytuanname(ln.get(i).getTuanname());
                String filename = "/xgtuanti"+tuantiid.getId()+(int)(Math.random()*9999)+".jpg";
                String imgpath = uploadFileSavePath+filename;
                QrCodeUtils.createQRCode("http://xinguan.scisoon.cn/xinguan/index.php/Appointment/tuantiluru?id="+tuantiid.getId(),imgpath,500,500,false);
                tuantiid.setImgpath("/upload/xgtuanti/"+filename);
                tuantiService.update(tuantiid);
            }else{
                tuanti.setTuanname(ln.get(i).getTuanname());
                tuanti.setYuyuedate(ln.get(i).getYuyuedate());
                tuanti.setYuyuetime(ln.get(i).getYuyuetime());
                tuanti.setCaiyangdidian(ln.get(i).getCaiyangdidian());
                tuanti.setIsfapiao(ln.get(i).getIsfapiao());
                tuanti.setFapiaotaitou(ln.get(i).getFapiaotaitou());
                tuanti.setZhucedizhi(ln.get(i).getZhucedizhi());
                tuanti.setZhucedianhua(ln.get(i).getZhucedianhua());
                tuanti.setKaihuyinhang(ln.get(i).getKaihuyinhang());
                tuanti.setYinhangzhanghao(ln.get(i).getYinhangzhanghao());
                tuanti.setFapiaotype(ln.get(i).getFapiaotype());
                tuanti.setAccessname(ln.get(i).getAccessname());
                tuanti.setAccessphone(ln.get(i).getAccessphone());
                tuanti.setAccessaddress(ln.get(i).getAccessaddress());
                tuanti.setAccessemail(ln.get(i).getAccessemail());
                tuanti.setXgreason(ln.get(i).getXgreason());
                tuanti.setCooperationstr(ln.get(i).getCooperationstr());
                tuanti.setQudao(ln.get(i).getQudao());
                tuanti.setShuihao(ln.get(i).getShuihao());
                tuanti.setSending(ln.get(i).getSending());
                tuanti.setSubjecttype(ln.get(i).getSubjecttype());
                tuanti.setCode(code);
                String filename = "/xgtuanti"+tuanti.getId()+(int)(Math.random()*9999)+".jpg";
                String imgpath = uploadFileSavePath+filename;
                QrCodeUtils.createQRCode("http://xinguan.scisoon.cn/xinguan/index.php/Appointment/tuantiluru?id="+tuanti.getId(),imgpath,500,500,false);
                tuanti.setImgpath("/upload/xgtuanti/"+filename);
                tuantiService.update(tuanti);
            }
        }
        String re="添加成功";

        msg.setMsg(re);
        Struts2Utils.renderJson(msg);
        return null;
    }

    public void updateJiesuan() throws SQLException {
        List<Tuanti> tuanti = Struts2Utils.conver(itemsxjyb, Tuanti.class);
        if(tuanti.size()>0){
            Tuanti tuantientity =  tuantiService.findUniqueBy(tuanti.get(0).getId());
            tuantientity.setIsjiesuan("是");
            tuantiService.update(tuantientity);
            //appointmentinfoxgwxService.deleteByOuttradeno(tuantientity.getTuanname());
        }
        msg.setMsg("结算成功");
        Struts2Utils.renderJson(msg);
    }

    public String tuantiinfo() throws SQLException {
        List<Appointmentinfoxgwx> ln = Struts2Utils.conver(itemsxjyb, Appointmentinfoxgwx.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Tuanti tuanti = tuantiService.findUniqueBytuanname(entity.getTuanname());
        for (int i = 0; i < ln.size(); i++) {
            /*String total = appointmentinfoxgwxService.findBySqlSelectCount(appointmentinfoxgwxService.getSqlTotal(tuanti.getYuyuedate()));
            String yunum = "";
            if (total != null) {
                int tot = Integer.parseInt(total);
                String s = (tot + 1) + "";
                if (s.length() == 1 && tot != 9) {
                    yunum += "000" + (tot + 1);
                } else if (s.length() == 2) {
                    yunum += "00" + (tot + 1);
                } else if (s.length() == 3) {
                    yunum += "0" + (tot + 1);
                } else {
                        yunum = "" + (tot + 1);
                }
            }
            String date = tuanti.getYuyuedate();*/
            /*String yuyuenum = "YX" + date.substring(5, 7) + date.substring(8, 10) + yunum;*/
            String yuyuenum ="YX"+(int)(Math.random() * 100000000+10000000);
            ln.get(i).setYuyuenum(yuyuenum);
            ln.get(i).setIsfapiao("团体统一开");
            ln.get(i).setQudao("团体");
            ln.get(i).setYuyuedate(tuanti.getYuyuedate());
            ln.get(i).setYuyuetime(tuanti.getYuyuetime());
            ln.get(i).setSending(tuanti.getSending());
            ln.get(i).setSubjecttype(tuanti.getSubjecttype());
            ln.get(i).setXgreason(tuanti.getXgreason());
            ln.get(i).setIsagree("是");
            ln.get(i).setTotalfee("0");
            ln.get(i).setOuttradeno(tuanti.getTuanname());
            ln.get(i).setCaiyangdidian(tuanti.getCaiyangdidian());
            ln.get(i).setIssuccessed("是");
            ln.get(i).setSampleType("咽拭子");
            ln.get(i).setCheckProject("新型冠状病毒 2019-nCoV 核酸检测");
            ln.get(i).setInspection("优迅医学");

            appointmentinfoxgwxService.save(ln.get(i));
        }
        String re = "";
        //
        if ("".equals(re)) {
            re = "添加成功";
        }
        msg.setMsg(re);
        Struts2Utils.renderJson(msg);
        return null;
    }

    public String caiSql(String outNo,String shicaiyangdian){
        String sql ="select name,sex,age,sfz,phone,samplebindtime,sampleNo,shicaiyangdian from xg_appointmentinfo where 1=1 and (sampleNo is not null or sampleNo = '') ";
        if(outNo!=null && !outNo.equals("")){
            sql += " AND outtradeno = '"+outNo+"'";
        }

        if(shicaiyangdian!=null && !shicaiyangdian.equals("")){
            sql += " AND shicaiyangdian = '"+shicaiyangdian+"'";
        }

        if(ininputTime!=null && !ininputTime.equals("")){
            sql += " AND STR_TO_DATE(samplebindtime,'%Y-%m-%d') >='" +ininputTime+"'";
        }

        if(osoinputTime!=null && !osoinputTime.equals("")){
            sql += " AND STR_TO_DATE(samplebindtime,'%Y-%m-%d') <='" +osoinputTime+"'";
        }

        return sql;
     }

    public String  groupByShicaiyangSql(String outNo){
        String sql ="select shicaiyangdian from xg_appointmentinfo WHERE outtradeno = '"+outNo+"'  and (sampleNo is not null or sampleNo = '')";
        if(ininputTime!=null && !ininputTime.equals("")){
            sql += " AND STR_TO_DATE(samplebindtime,'%Y-%m-%d') >='" +ininputTime+"'";
        }

        if(osoinputTime!=null && !osoinputTime.equals("")){
            sql += " AND STR_TO_DATE(samplebindtime,'%Y-%m-%d') <='" +osoinputTime+"'";
        }
        sql +="  GROUP BY shicaiyangdian";
        //sql = "select shicaiyangdian,totalfee from ("+sql+") as xg_appointmentinfo";
        return sql;
    }

    @SuppressWarnings({ "unchecked", "deprecation",  "unused" })
    public String tuantijiesuan(){
        List<Tuanti> applist=Struts2Utils.conver(itemsxjyb,Tuanti.class);
        Tuanti tuanti = applist.get(0);
        String uploadFileSavePathdocuments = ServletActionContext.getServletContext().getRealPath("/documents");
        Map<String,Map<String,List<String>>> one = this.tuantiExcel(applist.get(0), uploadFileSavePathdocuments + "/"+tuanti.getTuanname()+"采样明细表.xls");
        //存放报告路径和execl表格的
        List<File> lf=new ArrayList<File>();
        File excel = new File(uploadFileSavePathdocuments + "/"+tuanti.getTuanname()+"采样明细表.xls");
        lf.add(excel);

        SimpleDateFormat time=new SimpleDateFormat("yyyyMM");
        String cqrq = time.format(new Date());
        String uploadFileSavePath= ServletActionContext.getServletContext().getRealPath("/samplereport/xgzip/"+cqrq);
        String root = ServletActionContext.getServletContext().getRealPath("/samplereport/");
        if (!new File(uploadFileSavePath).exists()){//判断目录是否存在
            boolean flafile = new File(uploadFileSavePath).mkdirs();
        }

        if (!new File(root).exists()){//判断目录是否存在
            boolean flafile = new File(uploadFileSavePath).mkdirs();
        }

        // zip存放路径
        String zipfilePath = uploadFileSavePath +"/"+tuanti.getTuanname()+"报告.zip";
        File zipfile = new File(zipfilePath);

        // 报告存放路径
        String reportfilePath = uploadFileSavePath +"/"+tuanti.getTuanname()+"报告";
        File reportfilePathFile = new File(reportfilePath);

        for (String s :one.keySet()) {
            Map<String,List<String>> two = one.get(s);
            for  (String t :two.keySet()){

                List<String> reportPath = two.get(t);

                //存放报告路径
                List<File> relf=new ArrayList<File>();
                File reportfile = new File(reportfilePath+"/"+s+"/"+(s+t));

                if (!reportfile.exists()){//判断目录是否存在
                    boolean flafile = reportfile.mkdirs();
                }

                for (String path : reportPath ) {
                    File oneReportfile = new File(root+path);
                    relf.add(oneReportfile);
                }


                byte[] buf = new byte[1024];
                FileInputStream in = null;
                // 输出文件
                try {
                    for (int i = 0; i < relf.size(); i++) {
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(reportfile+"/"+relf.get(i).getName()));
                        File reportPathTuanti = new File(reportfile+"/"+relf.get(i).getName());
                        lf.add(reportPathTuanti);
                        File file = relf.get(i);
                        in = new FileInputStream(file);
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.close();
                    }

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }finally{
                    if(in!=null){
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }


        byte[] buf = new byte[1024];
        FileInputStream in = null;
        // 打包压缩
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < lf.size(); i++) {
                File file = lf.get(i);
                in = new FileInputStream(file);
                if(i == 0){
                    out.putNextEntry(new ZipEntry(file.getName()));
                }else{
                    String apReportPath = file.getAbsolutePath();
                    String zipName = apReportPath.substring(apReportPath.indexOf(tuanti.getTuanname()+"报告"),apReportPath.length());
                    out.putNextEntry(new ZipEntry(zipName));
                }

                out.setEncoding("GBK");
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
            }
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            String name=new String((tuanti.getTuanname()+"报告").getBytes("UTF-8"), "ISO-8859-1");
            Struts2Utils.getResponse().addHeader("Content-Disposition", "attachment;filename=\""+name+Struts2Utils.getymd(new Date())+".zip"+"\"");
            OutputStream os = Struts2Utils.getResponse().getOutputStream();
            String path=zipfilePath;
            FileInputStream is=new FileInputStream(path);

            byte[] buffer = new byte[400];
            int length = 0;
            while (-1 != (length = is.read(buffer))) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Map<String,Map<String,List<String>>> tuantiExcel(Tuanti tuanti,String path){


        Map<String,Map<String,List<String>>> one = new HashMap<String, Map<String, List<String>>>();

        /**总表start*/
        String shicaiyangsql = this.groupByShicaiyangSql(tuanti.getTuanname());
        List<Appointmentinfoxgwx> shicaiyangdian = appointmentinfoxgwxService.entitybysql(shicaiyangsql);
        //工作簿
        HSSFWorkbook hssfworkbook = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet hssfsheet = hssfworkbook.createSheet();
        //sheet名称乱码处理
        hssfworkbook.setSheetName(0,"总表");
        //取得第 i 行
        HSSFRow hssfrow1 = hssfsheet.createRow(0);

        Integer sampleCount = 0;

        HSSFCell hssfcell_00 = hssfrow1.createCell(0);
        hssfcell_00.setCellValue("采样地点");
        hssfsheet.setColumnWidth(0, 12*256);
        HSSFCell hssfcell_001 = hssfrow1.createCell(1);
        hssfcell_001.setCellValue("样本数");
        hssfsheet.setColumnWidth(1, 16*256);
        for (int i = 0; i < shicaiyangdian.size(); i++) {
            String countsql =  this.caiSql(tuanti.getTuanname(),shicaiyangdian.get(i).getShicaiyangdian());
            List<Appointmentinfoxgwx> count = appointmentinfoxgwxService.entitybysql(countsql);
            if(count == null){
                count = new ArrayList<Appointmentinfoxgwx>();
            }
            HSSFRow hssfrow = hssfsheet.createRow(i+1);
            HSSFCell hssfcell_0 = hssfrow.createCell(0);
            hssfcell_0.setCellValue(shicaiyangdian.get(i).getShicaiyangdian());
            HSSFCell hssfcell_1 = hssfrow.createCell(1);
            hssfcell_1.setCellValue(count.size());
            sampleCount += count.size();
        }
        int zongji = shicaiyangdian.size();
        HSSFRow hssfrow = hssfsheet.createRow(zongji+1);
        HSSFCell hssfcell_0 = hssfrow.createCell(0);
        hssfcell_0.setCellValue("总计");
        HSSFCell hssfcell_1 = hssfrow.createCell(1);
        hssfcell_1.setCellValue(sampleCount);


        /**总表stop*/

        /**总表明细start*/
        String sql =  this.caiSql(tuanti.getTuanname(),null);

        List<Appointmentinfoxgwx> mingxi = appointmentinfoxgwxService.entitybysql(sql);
        //创建sheet页
        hssfsheet = hssfworkbook.createSheet();
        //sheet名称乱码处理
        hssfworkbook.setSheetName(1,"总表明细");
        //取得第 i 行
        hssfrow1 = hssfsheet.createRow(0);

        hssfcell_00 = hssfrow1.createCell(0);
        hssfcell_00.setCellValue("序号");
        hssfsheet.setColumnWidth(0, 12*256);
        hssfcell_001 = hssfrow1.createCell(1);
        hssfcell_001.setCellValue("姓名");
        hssfsheet.setColumnWidth(1, 16*256);
        HSSFCell hssfcell_002 = hssfrow1.createCell(2);
        hssfcell_002.setCellValue("性别");
        hssfsheet.setColumnWidth(2, 20*256);
        HSSFCell hssfcell_003 = hssfrow1.createCell(3);
        hssfcell_003.setCellValue("年龄");
        hssfsheet.setColumnWidth(3, 20*256);
        HSSFCell hssfcell_004 = hssfrow1.createCell(4);
        hssfcell_004.setCellValue("身份证号码");
        hssfsheet.setColumnWidth(4, 12*256);
        HSSFCell hssfcell_005 = hssfrow1.createCell(5);
        hssfcell_005.setCellValue("手机号");
        hssfsheet.setColumnWidth(5, 12*256);
        HSSFCell hssfcell_006 = hssfrow1.createCell(6);
        hssfcell_006.setCellValue("采样时间");
        hssfsheet.setColumnWidth(6, 22*256);
        HSSFCell hssfcell_007 = hssfrow1.createCell(7);
        hssfcell_007.setCellValue("样本编号");
        hssfsheet.setColumnWidth(7, 18*256);
        HSSFCell hssfcell_008 = hssfrow1.createCell(8);
        hssfcell_008.setCellValue("采样地点");
        hssfsheet.setColumnWidth(8, 18*256);
        for (int i = 0; i < mingxi.size(); i++) {
            hssfrow = hssfsheet.createRow(i+1);
            hssfcell_0 = hssfrow.createCell(0);
            hssfcell_0.setCellValue(i+1);
            hssfcell_1 = hssfrow.createCell(1);
            hssfcell_1.setCellValue(mingxi.get(i).getName());
            HSSFCell hssfcell_2 = hssfrow.createCell(2);
            hssfcell_2.setCellValue(mingxi.get(i).getSex());
            HSSFCell hssfcell_3 = hssfrow.createCell(3);
            hssfcell_3.setCellValue(mingxi.get(i).getAge());
            HSSFCell hssfcell_4 = hssfrow.createCell(4);
            hssfcell_4.setCellValue(mingxi.get(i).getSfz());
            HSSFCell hssfcell_5 = hssfrow.createCell(5);
            hssfcell_5.setCellValue(mingxi.get(i).getPhone());
            HSSFCell hssfcell_6 = hssfrow.createCell(6);
            hssfcell_6.setCellValue(mingxi.get(i).getSamplebindtime());
            HSSFCell hssfcell_7 = hssfrow.createCell(7);
            hssfcell_7.setCellValue(mingxi.get(i).getSampleNo());
            HSSFCell hssfcell_8 = hssfrow.createCell(8);
            hssfcell_8.setCellValue(mingxi.get(i).getShicaiyangdian());

            /**获取报告路径 start*/
            if(one.containsKey(mingxi.get(i).getShicaiyangdian())){
                Map<String,List<String>> two = one.get(mingxi.get(i).getShicaiyangdian());
                if(two.containsKey(mingxi.get(i).getSamplebindtime().substring(0,10))){
                    List<String> reportPaht = two.get(mingxi.get(i).getSamplebindtime().substring(0,10));
                    Norovirus norovirus = norovirusService.findUniqueBy(mingxi.get(i).getSampleNo());
                    if(norovirus!=null){
                        reportPaht.add(norovirus.getPathName());
                    }
                }else{
                    List<String> reportPaht = new ArrayList<String>();
                    Norovirus norovirus = norovirusService.findUniqueBy(mingxi.get(i).getSampleNo());
                    if(norovirus!=null){
                        reportPaht.add(norovirus.getPathName());
                    }
                    two.put(mingxi.get(i).getSamplebindtime().substring(0,10),reportPaht);
                }
            }else{
                Map<String,List<String>> two = new HashMap<String, List<String>>();
                List<String> reportPaht = new ArrayList<String>();
                Norovirus norovirus = norovirusService.findUniqueBy(mingxi.get(i).getSampleNo());
                if(norovirus!=null){
                    reportPaht.add(norovirus.getPathName());
                }
                two.put(mingxi.get(i).getSamplebindtime().substring(0,10),reportPaht);
                one.put(mingxi.get(i).getShicaiyangdian(),two);
            }
            /**获取报告路径 end*/
        }

        /**总表stop*/


        /**各个采样点明细start*/
        for(int i = 0 ;i<shicaiyangdian.size();i++){
            sql =  this.caiSql(tuanti.getTuanname(),shicaiyangdian.get(i).getShicaiyangdian());

            mingxi = appointmentinfoxgwxService.entitybysql(sql);
            //创建sheet页
            hssfsheet = hssfworkbook.createSheet();
            //sheet名称乱码处理
            hssfworkbook.setSheetName(i+2,shicaiyangdian.get(i).getShicaiyangdian());
            //取得第 i 行
            hssfrow1 = hssfsheet.createRow(0);

            hssfcell_00 = hssfrow1.createCell(0);
            hssfcell_00.setCellValue("序号");
            hssfsheet.setColumnWidth(0, 12*256);
            hssfcell_001 = hssfrow1.createCell(1);
            hssfcell_001.setCellValue("姓名");
            hssfsheet.setColumnWidth(1, 16*256);
            hssfcell_002 = hssfrow1.createCell(2);
            hssfcell_002.setCellValue("性别");
            hssfsheet.setColumnWidth(2, 20*256);
            hssfcell_003 = hssfrow1.createCell(3);
            hssfcell_003.setCellValue("年龄");
            hssfsheet.setColumnWidth(3, 20*256);
            hssfcell_004 = hssfrow1.createCell(4);
            hssfcell_004.setCellValue("身份证号码");
            hssfsheet.setColumnWidth(4, 12*256);
            hssfcell_005 = hssfrow1.createCell(5);
            hssfcell_005.setCellValue("手机号");
            hssfsheet.setColumnWidth(5, 12*256);
            hssfcell_006 = hssfrow1.createCell(6);
            hssfcell_006.setCellValue("采样时间");
            hssfsheet.setColumnWidth(6, 22*256);
            hssfcell_007 = hssfrow1.createCell(7);
            hssfcell_007.setCellValue("样本编号");
            hssfsheet.setColumnWidth(7, 18*256);
            hssfcell_008 = hssfrow1.createCell(8);
            hssfcell_008.setCellValue("采样地点");
            hssfsheet.setColumnWidth(8, 18*256);
            for (int j = 0; j < mingxi.size(); j++) {
                hssfrow = hssfsheet.createRow(j+1);
                hssfcell_0 = hssfrow.createCell(0);
                hssfcell_0.setCellValue(j+1);
                hssfcell_1 = hssfrow.createCell(1);
                hssfcell_1.setCellValue(mingxi.get(j).getName());
                HSSFCell hssfcell_2 = hssfrow.createCell(2);
                hssfcell_2.setCellValue(mingxi.get(j).getSex());
                HSSFCell hssfcell_3 = hssfrow.createCell(3);
                hssfcell_3.setCellValue(mingxi.get(j).getAge());
                HSSFCell hssfcell_4 = hssfrow.createCell(4);
                hssfcell_4.setCellValue(mingxi.get(j).getSfz());
                HSSFCell hssfcell_5 = hssfrow.createCell(5);
                hssfcell_5.setCellValue(mingxi.get(j).getPhone());
                HSSFCell hssfcell_6 = hssfrow.createCell(6);
                hssfcell_6.setCellValue(mingxi.get(j).getSamplebindtime());
                HSSFCell hssfcell_7 = hssfrow.createCell(7);
                hssfcell_7.setCellValue(mingxi.get(j).getSampleNo());
                HSSFCell hssfcell_8 = hssfrow.createCell(8);
                hssfcell_8.setCellValue(mingxi.get(j).getShicaiyangdian());
            }
        }
        /***/

        
        FileOutputStream fileoutputstream;
        try {
            fileoutputstream = new FileOutputStream(path);
            hssfworkbook.write(fileoutputstream);
            fileoutputstream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return one;

      /*  }else{
            Struts2Utils.renderJson("否");
        }*/
    }


    @Override
    public String modulepage() throws Exception {
        return "modulepage";
    }


    @Override
    public String save() throws Exception {
        return null;
    }

    @Override
    protected void prepareModel() throws Exception {
        entity = new Tuanti();
    }

    @Override
    public Tuanti getModel() {
        return entity;
    }
}
