package com.usci.norovirus.action;

import com.lims.core.orm.Page;
import com.lims.core.utils.excel.WriteExcel;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.lims.core.utils.web.WebPathUtil;
import com.usci.norovirus.entity.*;
import com.usci.norovirus.service.*;

import com.usci.report.service.QrCodeKeyService;
import com.usci.system.service.SendEmailService;
import com.usci.tool.service.PmoderjobService;

import com.usci.xgcollectinfo.entity.Fiveoneinfo;
import com.usci.xgcollectinfo.entity.xgcollectinfo;
import com.usci.xgcollectinfo.service.FiveoneinfoService;
import com.usci.xgcollectinfo.service.Xgcollectinfo2Service;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })

@Results({
        @Result(name = "logins", type = "redirect", location = "session.jsp"),
        @Result(name = "modulepage", location = "/WEB-INF/content/norovirus/norovirus.jsp"),
        @Result(name = "modulepageone", location = "/WEB-INF/content/norovirus/coronal.jsp"),
        @Result(name = "modulepagetwo", location = "/WEB-INF/content/norovirus/coronalyin.jsp"),
})
public class NorovirusAction extends CrudActionSupport<Norovirus> {

    private Norovirus entity;

    private String itemsxjyb;

    private String itmespi;

    private String itmessc;

    private String reportPath;

    private String ininputTime;


    private String searchSampleNo;
    private String startdate;
    private String enddate;
    private String isShowSampleNo;
    private String dcTime;
    private Logger log = LoggerFactory.getLogger(NorovirusAction.class);
    @Autowired
    private PmoderjobService pmoderjobService;


    @Autowired
    private FiveoneinfoService fiveoneinfoService;
    private static final String separator=File.separator;
    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }


    public String getSearchSampleNo() {
        return searchSampleNo;
    }

    public void setSearchSampleNo(String searchSampleNo) {
        this.searchSampleNo = searchSampleNo;
    }

    public String getIninputTime() {
        return ininputTime;
    }

    public void setIninputTime(String ininputTime) {
        this.ininputTime = ininputTime;
    }



    @Autowired
    private QrCodeKeyService qrCodeKeyService;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private AppointmentinfoService appointmentinfoService;

    @Autowired
    private AppointmentinfoxgwxService appointmentinfoxgwxService;

    @Autowired
    private NorovirusreportstateService norovirusreportstateService;
//    @Autowired
//    private reportautoService reportautoService;

    @Autowired
    private XgBuKaiFaPiaoService xgBuKaiFaPiaoService;
    @Autowired
    private Xgcollectinfo2Service xgcollectinfo2Service;

    public String getItmespi() {
        return itmespi;
    }

    public void setItmespi(String itmespi) {
        this.itmespi = itmespi;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getItemsxjyb() {
        return itemsxjyb;
    }

    public void setItemsxjyb(String itemsxjyb) {
        this.itemsxjyb = itemsxjyb;
    }

    public String getItmessc() {
        return itmessc;
    }

    public void setItmessc(String itmessc) {
        this.itmessc = itmessc;
    }


    @Autowired
    private NorovirusService norovirusService;
    //查诺如报告信息
    @Override
    public String list() throws Exception {
        String sql= this.getSql("DESC","诺如",null);
        Page<Norovirus> page = new Page<Norovirus>(limit);
        page.setPageNo((start / limit) + 1);
        norovirusService.findPage(page,sql,"",start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }
    //查中文新冠报告信息
    public String list1() throws Exception {
        String sql= this.getSql("DESC","新型冠状",null);
        Page<Norovirus> page = new Page<Norovirus>(limit);
        page.setPageNo((start / limit) + 1);
        norovirusService.findPage(page,sql,"",start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }

    /**
     * 新冠英文报告
     * @return
     * @throws Exception
     */
    public String list2() throws Exception {
        String sql= this.getSql("DESC","新型冠状","英文");
        Page<Norovirus> page = new Page<Norovirus>(limit);
        page.setPageNo((start / limit) + 1);
        norovirusService.findPage(page,sql,"",start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }

    public String getSql(String orderby,String chekcProject,String nameType){

        String sql = "select id,`name`,englishName,idcard,reportdate,passport,reservation,sampleNo,englishreport,sendingPerson,qudao,sampleType,checkProject,pathName,inspection,samplingDate,receptionDate,ct,inputTime,inputName,updateTime,updateName,iss,detectionResult " +
                " from norovirus where 1=1";
        if(chekcProject!=null && !chekcProject.equals("")){
            sql += " AND checkProject like "+"'%"+chekcProject+"%'";
        }

        if(nameType!=null && nameType.equals("英文")){
            sql += " AND englishName is not null";
        }
//        else if(nameType == null){
//            sql += " AND englishName is null";
//        }
        if(entity.getInputTime()!=null&&!entity.getInputTime().equals("")){
            sql += " AND STR_TO_DATE(inputTime,'%Y-%m-%d') >= "+"'"+entity.getInputTime()+"'";
        }
        if(entity.getSinputTime()!=null&&!entity.getSinputTime().equals("")){
            sql += " AND STR_TO_DATE(inputTime,'%Y-%m-%d') <= "+"'"+entity.getSinputTime()+"'";
        }
        if(entity.getSamplingDate()!=null&&!entity.getSamplingDate().equals("")){
            sql += " AND STR_TO_DATE(samplingDate,'%Y-%m-%d') >= "+"'"+entity.getInputTime()+"'";
        }
        if(entity.getSsamplingDate()!=null&&!entity.getSsamplingDate().equals("")){
            sql += " AND STR_TO_DATE(samplingDate,'%Y-%m-%d') <= "+"'"+entity.getSsamplingDate()+"'";
        }
        if(entity.getReceptionDate()!=null&&!entity.getReceptionDate().equals("")){
            sql += " AND STR_TO_DATE(receptionDate,'%Y-%m-%d') >= "+"'"+entity.getReceptionDate()+"'";
        }
        if(entity.getSreceptionDate()!=null&&!entity.getSreceptionDate().equals("")){
            sql += " AND STR_TO_DATE(receptionDate,'%Y-%m-%d') <= "+"'"+entity.getSreceptionDate()+"'";
        }
        if(entity.getReportdate()!=null&&!entity.getReportdate().equals("")){
            sql += " AND STR_TO_DATE(reportdate,'%Y-%m-%d') >= "+"'"+entity.getReportdate()+"'";
        }
        if(entity.getSreportdate()!=null&&!entity.getSreportdate().equals("")){
            sql += " AND STR_TO_DATE(reportdate,'%Y-%m-%d') <= "+"'"+entity.getSreportdate()+"'";
        }
        if(entity.getName()!=null && !entity.getName().equals("")){
            sql += " AND (name like "+"'%"+entity.getName()+"%')";
        }
        if(entity.getSampleNo()!=null && !entity.getSampleNo().equals("")){
            sql += " AND (sampleNo like "+"'%"+entity.getSampleNo()+"%')";
        }
        if(entity.getEnglishName()!=null && !entity.getEnglishName().equals("")){
            sql += " AND (englishName like "+"'%"+entity.getEnglishName()+"%')";
        }
        if(entity.getIdcard()!=null && !entity.getIdcard().equals("")){
            sql += " AND (idcard like "+"'%"+entity.getIdcard()+"%')";
        }
        if(entity.getPassport()!=null && !entity.getPassport().equals("")){
            sql += " AND (passport like "+"'%"+entity.getPassport()+"%')";
        }
        if(entity.getReservation()!=null && !entity.getReservation().equals("")){
            sql += " AND (reservation like "+"'%"+entity.getReservation()+"%')";
        }
        if(entity.getInputName()!=null && !entity.getInputName().equals("")){
            sql += " AND (inputName like "+"'%"+entity.getInputName()+"%')";
        }
        if(entity.getInspection()!=null && !entity.getInspection().equals("")){
            sql += " AND (inspection like "+"'%"+entity.getInspection()+"%')";
        }
        if(entity.getCheckProject()!=null && !entity.getCheckProject().equals("")){
            sql += " AND (checkProject like "+"'%"+entity.getCheckProject()+"%')";
        }
        if(entity.getDetectionResult()!=null && !entity.getDetectionResult().equals("")){
            sql += " AND (detectionResult like "+"'%"+entity.getDetectionResult()+"%')";
        }
        if(entity.getSendingPerson()!=null && !entity.getSendingPerson().equals("")){
            sql += " AND (sendingPerson like "+"'%"+entity.getSendingPerson()+"%')";
        }
        if(entity.getEnglishreport()!=null && !entity.getEnglishreport().equals("")){
            sql += " AND (englishreport like "+"'%"+entity.getEnglishreport()+"%')";
        }
        if(entity.getQudao()!=null && !entity.getQudao().equals("")){
            sql += " AND (qudao like "+"'%"+entity.getQudao()+"%')";
        }
        if(entity.getCt()!=null && !entity.getCt().equals("")){
            sql += " AND (ct like "+"'%"+entity.getCt()+"%')";
        }
        if(entity.getIss()!=null && !entity.getIss().equals("")){
            sql += " AND (iss like "+"'%"+entity.getIss()+"%')";
        }
        
        if(searchSampleNo!=null && !searchSampleNo.equals("")){
            sql += " AND (sampleNo like "+"'%"+searchSampleNo+"%' or name like "+"'%"+searchSampleNo+"%')";
        }
        sql+=" order by id " +orderby;
        return sql;
    }
    //只是返回导出财务信息的sql 具体操作看谁调用
    public String getCaiwuSql(Norovirus noroviri){

        String sql = "SELECT a.id,concat(a.yuyuenum,'') as yuyuenum,concat(a.subjecttype,'') as subjecttype,concat(a.phone,'') as phone,concat(a.qudao,'') as qudao,concat(a.shuihao,'') as shuihao," +
                "concat(a.fapiaotype,'') as fapiaotype,concat(a.shicaiyangdian,'') as shicaiyangdian," +
                "concat(a.NAME,'') sname,a.duihuanma,concat(a.outtradeno,'') outtradeno,concat(a.accessemail,'') accessemail,concat(a.totalfee,'') totalfee,concat(a.isfapiao,'') isfapiao,concat(a.fapiaotaitou,'') fapiaotaitou,concat(a.caiyangdidian,'') caiyangdidian," +
                " concat(b.cooperationcompanyname,'') cooperationcompanyname,concat(b.cooperationsubcompanyname,'') cooperationsubcompanyname,concat(b.businessmanager,'')businessmanager,concat(b.salename,'') salename FROM xg_appointmentinfo " +
                " a LEFT JOIN xg_cooperationinformation b ON a.cooperationstr=b.cooperationstr WHERE issuccessed = '是' AND 1=0";

        if(noroviri.getReservation()!=null && !noroviri.getReservation().equals("")){
            sql +=" OR a.sampleNo ='"+noroviri.getSampleNo()+"'";
            /*if(noroviri.getName()!=null && !noroviri.getName().equals("")){
                sql +=" AND a.name ='"+noroviri.getName()+"'";
            }*/
        }
        return sql;
    }
    //补开发票的sql
    public String getBukaifapiaoSql(Appointmentinfo appointmentinfo){

        String sql = "select id,isfapiao,shuihao,fapiaotaitou,zhucedizhi,zhucedianhua,kaihuyinhang,yinhangzhanghao,fapiaotype,accessname,accessphone,accessaddress,accessemail,yuyueid,bukaistate,bukainame,bukaitime" +
                " from  xg_bukaifapiao b WHERE 1=1 ";

        if(appointmentinfo!=null){
            sql +=" AND yuyueid = "+ appointmentinfo.getId();
        }
        return sql;
    }

    /**
     * 诺如和新冠生成报告,现在没有新冠
     * @return
     * @throws Exception
     */
    public String saveAll() throws Exception{
        List<Norovirus> ln=Struts2Utils.conver(itemsxjyb,Norovirus.class);
        StringBuffer reportpath = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<ln.size();i++){
            String pathName = "";
            String osno=ln.get(i).getSampleNo();
            if(!"".equals(osno.trim())&&osno.trim()!=null) {
                if(ln.get(i).getDetectionResult().equals("阴性")){
                    Norovirus norovirus =norovirusService.findUniqueBy(osno);
                    if(ln.get(i).getCheckProject().indexOf("诺如")!=-1&&"否".equals(isShowSampleNo)){
                    	pathName =  norovirusService.createPdf(ln.get(i),"nr1.pdf");
                    }else if(ln.get(i).getCheckProject().indexOf("诺如")!=-1){
                        pathName =  norovirusService.createPdf(ln.get(i),"nr.pdf");
                    }else{
                        pathName =  norovirusService.createPdf(ln.get(i),"xg.pdf");
                    }

                    String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                    //调用方法插入图片
                    qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);

                    ln.get(i).setPathName(pathName);
                    if(norovirus!=null){    //已经存在
                        norovirus.setPathName(pathName);
                        norovirus.setIdcard(ln.get(i).getIdcard());
                        norovirus.setCt(ln.get(i).getCt());
                        norovirus.setCheckProject(ln.get(i).getCheckProject());
                        norovirus.setInspection(ln.get(i).getInspection());
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirus.setReceptionDate(ln.get(i).getReceptionDate());
                        norovirus.setSamplingDate(ln.get(i).getSamplingDate());
                        norovirus.setIss(ln.get(i).getIss());
                        norovirus.setSampleType(ln.get(i).getSampleType());
                        norovirus.setName(ln.get(i).getName());
                        norovirus.setSendingPerson(ln.get(i).getSendingPerson());
                        norovirus.setReservation(ln.get(i).getReservation());
                        norovirus.setQudao(ln.get(i).getQudao());
                        norovirusService.update(norovirus);
                    }else{
                        norovirusService.save(ln.get(i));
                    }

                    reportpath.append(pathName+",");
                }else{
                    sb.append(ln.get(i).getSampleNo()+",");
                }
            }
        }
        String re="";
        //
        if("".equals(re)){
            re="添加成功";
            if(sb!=null && !sb.toString().equals("")){
                re+=",其中样本"+sb.toString()+"为阳性,不出报告";
            }
        }
        msg.setMsg(re);
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }


    /**
     * 新冠 和 诺如客服补录
     * @return
     * @throws Exception
     */

    public String kefusaveAll() throws Exception{
        List<Norovirus> ln=Struts2Utils.conver(itemsxjyb,Norovirus.class);
        for(int i=0;i<ln.size();i++){
            String osno=ln.get(i).getSampleNo();
            if(!"".equals(osno.trim())&&osno.trim()!=null) {
                    Norovirus norovirus =norovirusService.findUniqueBy(osno);
                    if(norovirus!=null){    //已经存在
                        norovirus.setIdcard(ln.get(i).getIdcard());
                        norovirus.setCt(ln.get(i).getCt());
                        norovirus.setCheckProject(ln.get(i).getCheckProject());
                        norovirus.setInspection(ln.get(i).getInspection());
                        norovirus.setReceptionDate(ln.get(i).getReceptionDate());
                        norovirus.setSamplingDate(ln.get(i).getSamplingDate());
                        norovirus.setIss(ln.get(i).getIss());
                        norovirus.setSampleType(ln.get(i).getSampleType());
                        norovirus.setName(ln.get(i).getName());
                        norovirus.setSendingPerson(ln.get(i).getSendingPerson());
                        norovirus.setReservation(ln.get(i).getReservation());
                        norovirus.setEnglishName(ln.get(i).getEnglishName());
                        norovirus.setPassport(ln.get(i).getPassport());
                        norovirusService.update(norovirus);
                    }else{
                        norovirusService.save(ln.get(i));
                    }
            }
        }
        String re="";
        //
        if("".equals(re)){
            re="补录成功";
        }
        msg.setMsg(re);
        Struts2Utils.renderJson(msg);
        return null;
    }
    //是否预约
    public String isyuyue() throws Exception{
        StringBuffer kefu = new StringBuffer();
        List<Norovirus> ln=Struts2Utils.conver(itemsxjyb,Norovirus.class);
        for(int i=0;i<ln.size();i++){
            String osno=ln.get(i).getSampleNo();
            Appointmentinfoxgwx appont =appointmentinfoxgwxService.findUniqueBySampleNo(osno);
            Norovirus norovirus = norovirusService.findUniqueBy(osno);
            if(norovirus!=null || appont!=null){
                System.out.println(osno+",");
                kefu.append(osno+",");
            }
        }

        msg.setMsg(kefu.toString());
        Struts2Utils.renderJson(msg);
        return null;
    }

    //查询是否出报告
//    public String isbaogao() throws Exception{
//        StringBuffer kefu = new StringBuffer();
//        List<Norovirus> ln=Struts2Utils.conver(itemsxjyb,Norovirus.class);
//        for(int i=0;i<ln.size();i++){
//            String osno=ln.get(i).getSampleNo();
//            Norovirus norovirus = norovirusService.findUniqueBy(osno);
//            if(norovirus!=null &&norovirus.getPathName()==null){
//                this.nrscReport();
//            }else if (norovirus!=null &&norovirus.getPathName() !=null){
//                System.out.println(osno+",");
//                kefu.append(osno+",");
//            }
//        }
//
//        msg.setMsg(kefu.toString());
//        Struts2Utils.renderJson(msg);
//        return null;
//    }

    public String update(){
        Norovirus norovirus = norovirusService.findById(entity.getId());
        norovirus.setReceptionDate(entity.getReceptionDate());
        norovirus.setSamplingDate(entity.getSamplingDate());
        norovirus.setIdcard(entity.getIdcard());
        norovirus.setInspection(entity.getInspection());
        norovirus.setCheckProject(entity.getCheckProject());
        norovirus.setSampleType(entity.getSampleType());
        norovirus.setSendingPerson(entity.getSendingPerson());
        norovirus.setName(entity.getName());
        norovirus.setReportdate(entity.getReportdate());
        if(norovirus.getCheckProject().contains("诺如")){
            norovirus.setCt(entity.getCt());
            norovirus.setIss(entity.getIss());
        }else{
            norovirus.setReservation(entity.getReservation());
            norovirus.setQudao(entity.getQudao());
            norovirus.setEnglishName(entity.getEnglishName());
            norovirus.setEnglishreport(entity.getEnglishreport());
            norovirus.setPassport(entity.getPassport());
            //修改预约表信息
            Appointmentinfoxgwx appointmentinfoxgwx = appointmentinfoxgwxService.findUniqueBySampleNo(norovirus.getSampleNo());
            appointmentinfoxgwx.setSampleNo(entity.getSampleNo());
            appointmentinfoxgwx.setSfz(entity.getIdcard());
            appointmentinfoxgwx.setName(entity.getName());
            appointmentinfoxgwx.setEnglishName(entity.getEnglishName());
            appointmentinfoxgwx.setEnglishreport(entity.getEnglishreport());
            appointmentinfoxgwxService.update(appointmentinfoxgwx);
        }
        norovirus.setSampleNo(entity.getSampleNo());
        norovirusService.update(norovirus);
        msg.setMsg("已修改");
        Struts2Utils.renderJson(msg);
        return null;
    }

    /**
     * 合管生成报告
     * @return
     */
    public String fiveoneReport(){
        List<Norovirus> ln=Struts2Utils.conver(itmessc,Norovirus.class);
        StringBuffer reportpath = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        StringBuffer yang = new StringBuffer();
        StringBuffer nofund = new StringBuffer();
        StringBuffer isrk = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Norovirus n : ln){
            Fiveoneinfo fone = fiveoneinfoService.findUniqueBy(n.getFiveone());
            if(null != fone){  //是否存在合管编码
                if(fone.getIsruku() != "是"){  //合管是否已经入库
                    //查询属于该合管编号的所有样本
                    List<xgcollectinfo> xlist = xgcollectinfo2Service.getByfiveone(n.getFiveone());
                    for(xgcollectinfo xgcollectinfo : xlist){
                        String pathName = "";
                        String sampleno = xgcollectinfo.getSamplenum();
                        Norovirus ns = norovirusService.findUniqueBy(sampleno);
                        if(null != ns){  //合管中的这个样本是否已入库   只生成该合管中已入库的样本
                            if(!"".equals(ns.getSampleNo().trim())&&ns.getSampleNo().trim()!=null){
                                //阴性出报告
                                if(n.getDetectionResult().equals("阴性")){
                                    if(ns.getDetectionResult()!=null && ns.getDetectionResult().contains("阳性")){
                                        yang.append(ns.getSampleNo()+",");
                                        continue;
                                    }
                                    ns.setDetectionResult(n.getDetectionResult());
                                    if(ns.getCheckProject().indexOf("诺如")!=-1){
                                        pathName =  norovirusService.createPdf(ns,"nr.pdf");
                                    }else{
                                        pathName =  norovirusService.createPdf(ns,"xg.pdf");
                                    }
                                    String qrimgUrl=qrCodeKeyService.createQrCode(ns.getSampleNo(),ns.getName(),false);
                                    //调用方法插入图片
                                    qrCodeKeyService.insertQrcodeImg(qrimgUrl,"/samplereport/"+pathName,1,413,780,null);
                                    ns.setPathName(pathName);
                                    //已经存在
                                    ns.setPathName(pathName);
                                    ns.setDetectionResult(ns.getDetectionResult());
                                    norovirusService.update(ns);
                                    reportpath.append(pathName+",");
                                }else{
                                    sb.append(ns.getSampleNo()+",");
                                }
                            }
                        }
                    }
                }else{
                    isrk.append(n.getFiveone()+"，") ;
                }
            }else{
                nofund.append(n.getFiveone()+"，") ;
            }
        }
        String re="";
        //
        if("".equals(re)){
            re="报告生成成功";
            if(sb!=null && !sb.toString().equals("")){
                re+=",其中样本"+sb.toString()+"为阳性,不出报告,";
            }
            if(yang!=null && !yang.toString().equals("")){
                re+="样本为"+yang.toString()+"已为阳性不能更改,";
            }
            if(nofund!=null && !nofund.toString().equals("")){
                re+="合管编号为"+nofund.toString()+"不存在";
            }
            if(isrk!=null && !isrk.toString().equals("")){
                re+="合管编号为"+isrk.toString()+"已入库";
            }
        }
        msg.setMsg(re);
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }

    /**
     * 下载合管生成报告模板
     * @throws IOException
     */
    public void heguanExcel() throws IOException{
        norovirusService.excelmuban();
        String name=new String("heguan".getBytes("UTF-8"), "ISO-8859-1");
        Struts2Utils.getResponse().addHeader("Content-Disposition", "attachment;filename=\""+name+Struts2Utils.getymd(new Date())+".xls"+"\"");
        OutputStream os = Struts2Utils.getResponse().getOutputStream();
        String uploadFileSavePath= ServletActionContext.getServletContext().getRealPath(separator+"documents");
        String path=uploadFileSavePath+separator+"heguan.xls";
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
     * 新冠预约生成报告 (客服补录)
     * @return
     * @throws Exception
     */
    public String scReport() throws Exception{



        List<Norovirus> ln=Struts2Utils.conver(itmessc,Norovirus.class);
        StringBuffer reportpath = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        StringBuffer kefu = new StringBuffer();
        StringBuffer yang = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<ln.size();i++){
            String pathName = "";
            String osno=ln.get(i).getSampleNo();
            if(!"".equals(osno.trim())&&osno.trim()!=null) {
                //在报告表查样本编号
                Norovirus norovirus = norovirusService.findUniqueBy(osno);

                //在预约表里查样本编号
                Appointmentinfoxgwx appont =appointmentinfoxgwxService.findUniqueBySampleNo(osno);
                //是否为阴性 阳性报告不出 如需出阳性报告注释if判断即可
                if(ln.get(i).getDetectionResult().equals("阴性")){
                    //在预约表里查样本编号

                    //如果报告表不为空
                    if(norovirus!=null){
                        if(norovirus.getDetectionResult()!=null && norovirus.getDetectionResult().contains("阳性")){
                            yang.append(norovirus.getSampleNo()+",");
                            continue;
                        }
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        //如果为诺如报告
                        if(ln.get(i).getCheckProject().indexOf("诺如")!=-1){
                            pathName =  norovirusService.createPdf(norovirus,"nr.pdf");
                            String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                            //调用方法插入图片 防伪二维码 X,y坐标
                            qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);
                        }else{ //不为诺如为新冠报告时
                            //当报告表里不需要英文报告时
                            if (norovirus.getEnglishreport()==null||norovirus.getEnglishreport().equals("")){
                                norovirus.setEnglishreport("否");
                            }
                            if (norovirus.getEnglishreport().equals("否")){
                                pathName =  norovirusService.createPdf(norovirus,"xg.pdf");
                                String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                                //调用方法插入图片
                                qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);
                            }else {//报告表里需要英文报告时
                                pathName =  norovirusService.createPdf(norovirus,"xgzyw.pdf");
                                String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                                //调用方法插入图片
                                qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);
                                qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,2,413,780,null);
                            }

                        }


                        ln.get(i).setPathName(pathName);
                        //已经存在
                        norovirus.setPathName(pathName);
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirusService.update(norovirus);
//                        String num = norovirus.getSampleNo();
//                        reportauto reportauto = reportautoService.findBynum(num);
//                        if(reportauto != null){
//                            reportauto.setType(norovirus.getDetectionResult());
//                            reportautoService.update(reportauto); //修改reportauto表的样本状态
//                        }
                        reportpath.append(pathName+",");
                    }else if(appont!=null){//如果预约表不为空
                        if(norovirus == null){//报告表里为空时
                            norovirus = new Norovirus();
                        }
                        if (appont.getEnglishreport()==null||appont.getEnglishreport().equals("")){
                            appont.setEnglishreport("否");
                        }

                        norovirus.setIdcard(appont.getSfz());
                        norovirus.setCheckProject(appont.getCheckProject());
                        if(appont.getOuttradeno().equals("河北项目")){
                            norovirus.setReceptionDate(appont.getDaoyangdate());
                            norovirus.setSamplingDate(appont.getDaoyangdate());
                        }else{
                            norovirus.setReceptionDate(appont.getSamplebindtime());
                            norovirus.setSamplingDate(appont.getSamplebindtime());
                            norovirus.setInspection(appont.getInspection());
                        }
                        norovirus.setSampleType(appont.getSampleType());
                        norovirus.setName(appont.getName());
                        norovirus.setSendingPerson(appont.getSending());
                        norovirus.setReservation(appont.getYuyuenum());
                        norovirus.setSampleNo(appont.getSampleNo());
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirus.setQudao(appont.getQudao());
                        norovirus.setEnglishName(appont.getEnglishName());
                        norovirus.setPassport(appont.getPassport());
                        norovirus.setEnglishreport(appont.getEnglishreport());
                        //如果报告表里不出英文报告就直接生成中文报告
                        if (norovirus.getEnglishreport().equals("否")||norovirus.getEnglishreport()==null||norovirus.getEnglishreport().equals("")){
                            pathName =  norovirusService.createPdf(norovirus,"xg.pdf");
                            String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                            //调用方法插入图片 插入防伪二维码图片
                            qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);
                        }else {//如果报告表里需要英文报告
                            //生成英文报告
                            pathName =  norovirusService.createPdf(norovirus,"xgzyw.pdf");
                            String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                            //调用方法插入图片 插入防伪二维码图片
                            qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);
                            qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,2,413,780,null);
                        }
                        norovirus.setPathName(pathName);
                        norovirus.setPathName(pathName);
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        appont.setReportdate(sdf.format(new Date()));
                        appointmentinfoxgwxService.update(appont);
                        norovirusService.save(norovirus);
                        reportpath.append(pathName+",");
                    }else{
                        kefu.append(osno+",");
                        Norovirusreportstate norovirusreportstate = norovirusreportstateService.findUniqueBy(osno);
                        if(norovirusreportstate == null){
                            norovirusreportstate = new Norovirusreportstate();
                            norovirusreportstate.setDetectionResult(ln.get(i).getDetectionResult());
                            norovirusreportstate.setSampleNo(osno);
                            norovirusreportstate.setReportState("未生成");
                            norovirusreportstateService.save(norovirusreportstate);
                        }
                    }
                }else{
                    if(norovirus!=null){
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirus.setCt(ln.get(i).getCt());
                        norovirus.setIss(ln.get(i).getIss());
                        norovirusService.update(norovirus);
                    }else if(appont!=null){
                        if(norovirus == null){//报告表里为空时
                            norovirus = new Norovirus();
                        }
                        if (appont.getEnglishreport()==null||appont.getEnglishreport().equals("")){
                            appont.setEnglishreport("否");
                        }
                        norovirus.setIdcard(appont.getSfz());
                        norovirus.setCheckProject(appont.getCheckProject());
                        if(appont.getOuttradeno().equals("河北项目")){
                            norovirus.setReceptionDate(appont.getDaoyangdate());
                            norovirus.setSamplingDate(appont.getDaoyangdate());
                        }else{
                            norovirus.setReceptionDate(appont.getSamplebindtime());
                            norovirus.setSamplingDate(appont.getSamplebindtime());
                            norovirus.setInspection(appont.getInspection());
                        }
                        norovirus.setSampleType(appont.getSampleType());
                        norovirus.setName(appont.getName());
                        norovirus.setSendingPerson(appont.getSending());
                        norovirus.setReservation(appont.getYuyuenum());
                        norovirus.setSampleNo(appont.getSampleNo());
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirus.setQudao(appont.getQudao());
                        norovirus.setEnglishName(appont.getEnglishName());
                        norovirus.setPassport(appont.getPassport());
                        norovirus.setEnglishreport(appont.getEnglishreport());
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        appont.setReportdate(sdf.format(new Date()));
                        appointmentinfoxgwxService.update(appont);
                        norovirusService.save(norovirus);
                        reportpath.append(pathName+",");
                    }
                    sb.append(ln.get(i).getSampleNo()+",");
                }
            }
        }
        String re="";
        //
        if("".equals(re)){
            re="报告生成成功";
            if(sb!=null && !sb.toString().equals("")){
                re+=",其中样本"+sb.toString()+"为阳性,不出报告,";
            }
            if(yang!=null && !yang.toString().equals("")){
                re+="样本为"+yang.toString()+"已为阳性不能更改,";
            }
            if(kefu!=null && !kefu.toString().equals("")){
                re+="样本为"+kefu.toString()+"客服未补录";
//                sendEmailService.xinguan("新型冠状病毒客服未补录","样本编号为"+kefu.toString()+"客服未补录信息,请及时补录信息",null);
            }
        }
        msg.setMsg(re);
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }
    /**
     * 诺如生成报告
     * @return
     * @throws Exception
     */
    public String nrscReport() throws Exception{
        List<Norovirus> ln=Struts2Utils.conver(itmessc,Norovirus.class);
        StringBuffer reportpath = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        StringBuffer kefu = new StringBuffer();
        StringBuffer yang = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<ln.size();i++){
            String pathName = "";
            String osno=ln.get(i).getSampleNo();
            if(!"".equals(osno.trim())&&osno.trim()!=null) {
                Norovirus norovirus = norovirusService.findUniqueBy(osno);
                if(ln.get(i).getDetectionResult().equals("阴性")){
                    if(norovirus!=null){
                        if(norovirus.getDetectionResult()!=null && norovirus.getDetectionResult().contains("阳性")){
                            yang.append(norovirus.getSampleNo()+",");
                            continue;
                        }
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirus.setCt(ln.get(i).getCt());
                        norovirus.setIss(ln.get(i).getIss());
                        if("否".equals(isShowSampleNo)){
                            pathName =  norovirusService.createPdf(norovirus,"nr1.pdf");
                        }else{
                            pathName =  norovirusService.createPdf(norovirus,"nr.pdf");
                        }

                        String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                        //调用方法插入图片
                        qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);
                        ln.get(i).setPathName(pathName);
                        //已经存在
                        norovirus.setPathName(pathName);
                        norovirusService.update(norovirus);
                        reportpath.append(pathName+",");
                    }else{
                        kefu.append(osno+",");
                    }
                }else{
                    norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                    norovirus.setCt(ln.get(i).getCt());
                    norovirus.setIss(ln.get(i).getIss());
                    norovirusService.update(norovirus);
                    sb.append(ln.get(i).getSampleNo()+",");
                }
            }
        }
        String re="";
        //
        if("".equals(re)){
            re="报告生成成功";
            if(sb!=null && !sb.toString().equals("")){
                re+=",其中样本"+sb.toString()+"为阳性,不出报告,";
            }
            if(yang!=null && !yang.toString().equals("")){
                re+="样本为"+yang.toString()+"已为阳性不能更改,";
            }
            if(kefu!=null && !kefu.toString().equals("")){
                re+="样本为"+kefu.toString()+"客服未补录";
                sendEmailService.xinguan("诺如病毒核酸检测客服未补录","样本编号为"+kefu.toString()+"客服未补录信息,请及时补录信息",null);
            }
        }
        msg.setMsg(re);
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }
    
    //补录生成报告，仅用于报告中不展示样本编号的情况
    public String blscReport(){
    	StringBuffer reportPaths = new StringBuffer();
    	List<Norovirus> ln=Struts2Utils.conver(itemsxjyb,Norovirus.class);
    	
        for(int i=0;i<ln.size();i++){
        	String pathName = "";
        	if("阴性".equals(ln.get(i).getDetectionResult())){
        		String osno=ln.get(i).getSampleNo();
                Norovirus norovirus = null;
                //判断样本编号+送检单位相同+身份证号，就进行更新，否则添加一条新数据(仅仅样本编号很难保证唯一性)
                if(!"".equals(osno.trim())&&osno.trim()!=null) {
                    norovirus =norovirusService.findUniqueByLimits(osno,ln.get(i).getSendingPerson(),ln.get(i).getIdcard());
                }
                if(norovirus!=null){    //已经存在
                    norovirus.setIdcard(ln.get(i).getIdcard());
                    norovirus.setCt(ln.get(i).getCt());
                    norovirus.setCheckProject(ln.get(i).getCheckProject());
                    norovirus.setInspection(ln.get(i).getInspection());
                    norovirus.setReceptionDate(ln.get(i).getReceptionDate());
                    norovirus.setSamplingDate(ln.get(i).getSamplingDate());
                    norovirus.setIss(ln.get(i).getIss());
                    norovirus.setSampleType(ln.get(i).getSampleType());
                    norovirus.setName(ln.get(i).getName());
                    norovirus.setSendingPerson(ln.get(i).getSendingPerson());
                    norovirus.setReservation(ln.get(i).getReservation());
                    norovirus.setEnglishName(ln.get(i).getEnglishName());
                    norovirus.setPassport(ln.get(i).getPassport());
                    
                    if(ln.get(i).getCheckProject().indexOf("诺如")!=-1){
                        pathName =  norovirusService.createPdf(norovirus,"nr1.pdf");
                    }else{
                        pathName =  norovirusService.createPdf(norovirus,"xg1.pdf");
                    }

                    String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                    //调用方法插入图片
                    qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);
                    norovirus.setPathName(pathName);
                    reportPaths.append(pathName+",");
                    
                    norovirusService.update(norovirus);
                }else{
                	norovirus = ln.get(i);
                	if(ln.get(i).getCheckProject().indexOf("诺如")!=-1){
                        pathName =  norovirusService.createPdf(norovirus,"nr1.pdf");
                    }else{
                        pathName =  norovirusService.createPdf(norovirus,"xg1.pdf");
                    }

                    String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                    //调用方法插入图片
                    qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);
                    norovirus.setPathName(pathName);
                    reportPaths.append(pathName+",");
                	
                    norovirusService.save(ln.get(i));
                }
        	}
            
        }
        msg.setMsg("补录成功");
        msg.setO(reportPaths);
        Struts2Utils.renderJson(msg);
        
        return null;
    	
    }



    /**
     * 新冠预约生成报告
     * @return
     * @throws Exception
     */
    public String scyinReport() throws Exception{
        List<Norovirus> ln=Struts2Utils.conver(itmessc,Norovirus.class);
        StringBuffer reportpath = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        StringBuffer kefu = new StringBuffer();

        for(int i=0;i<ln.size();i++){
            String pathName = "";
            String pathName1 = "";
            String osno=ln.get(i).getSampleNo();
            if(!"".equals(osno.trim())&&osno.trim()!=null) {
                if(ln.get(i).getDetectionResult().equals("阴性")){
                    Norovirus norovirus = norovirusService.findUniqueBy(osno);
                    if(norovirus!=null){

                        norovirus.setDetectionResult("阴性");
                        pathName =  norovirusService.createPdf(norovirus,"xgzw.pdf");
                        String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                        //调用方法插入图片
                        qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);

                        pathName1 =  norovirusService.createPdf(norovirus,"xgyw.pdf");
                        String qrimgUrl1=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getEnglishName(),false);
                        //调用方法插入图片
                        qrCodeKeyService.insertQrcodeImg(qrimgUrl1,separator+"samplereport"+separator+pathName1,1,413,780,null);

                        pathName+=","+pathName1;

                        ln.get(i).setPathName(pathName);
                        //已经存在
                        norovirus.setPathName(pathName);
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirusService.update(norovirus);
                        reportpath.append(pathName+",");
                    }else{
                        kefu.append(osno+",");
                        Norovirusreportstate norovirusreportstate = norovirusreportstateService.findUniqueBy(osno);
                        if(norovirusreportstate == null){
                            norovirusreportstate = new Norovirusreportstate();
                            norovirusreportstate.setDetectionResult(ln.get(i).getDetectionResult());
                            norovirusreportstate.setSampleNo(osno);
                            norovirusreportstate.setReportState("未生成");
                            norovirusreportstateService.save(norovirusreportstate);
                        }
                    }
                }else{
                    sb.append(ln.get(i).getSampleNo()+",");
                }
            }
        }
        String re="";
        //
        if("".equals(re)){
            re="报告生成成功";
            if(sb!=null && !sb.toString().equals("")){
                re+=",其中样本"+sb.toString()+"为阳性,不出报告,";
            }
            if(kefu!=null && !kefu.toString().equals("")){
                re+="样本为"+kefu.toString()+"客服未补录";
                sendEmailService.xinguan("新型冠状病毒客服未补录","样本编号为"+kefu.toString()+"客服未补录信息,请及时补录信息",null);
            }
        }
        msg.setMsg(re);
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }
    
    /**
     * 生成个人英文报告
     *	与团体不同的是生成盖章的报告
     * @return
     */
    public String scgryinReport(){
    	List<Norovirus> ln=Struts2Utils.conver(itmessc,Norovirus.class);
        StringBuffer reportpath = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        StringBuffer kefu = new StringBuffer();
        StringBuffer kefuname = new StringBuffer();
        StringBuffer noyuyue = new StringBuffer();
        log.info("ln.size"+ln.size());
        for(int i=0;i<ln.size();i++){
            String pathName = "";
            String pathName1 = "";
            String name=ln.get(i).getName();
            String reservation  = ln.get(i).getReservation();
            log.info(name+"\t"+reservation);
            if(reservation!=null&&!"".equals(reservation.trim())&&name!=null&&!"".equals(name.trim())) {
                Norovirus norovirus = norovirusService.findByNameAndReservation(name,reservation);
                log.info(norovirus==null?"isnull":"notnull");
                if(norovirus!=null){
                    if(norovirus.getDetectionResult().equals("阴性")){
                        norovirus.setEnglishName(ln.get(i).getEnglishName());
                        norovirus.setPassport(ln.get(i).getPassport());
                        pathName =  norovirusService.createPdf(norovirus,"xgzwgz.pdf");
                        String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                        //调用方法插入图片
                        qrCodeKeyService.insertQrcodeImg(qrimgUrl,separator+"samplereport"+separator+pathName,1,413,780,null);

                        pathName1 =  norovirusService.createPdf(norovirus,"xgywgz.pdf");
                        String qrimgUrl1=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getEnglishName(),false);
                        //调用方法插入图片
                        qrCodeKeyService.insertQrcodeImg(qrimgUrl1,separator+"samplereport"+separator+pathName1,1,413,780,null);

                        pathName+=","+pathName1;

                        ln.get(i).setPathName(pathName);
                        //已经存在
                        norovirus.setPathName(pathName);
                        norovirusService.update(norovirus);
                        reportpath.append(pathName+",");
                    }else{
                    	sb.append(name+",");
                    }
                    
                }else{
                	/**
                	 * 不存在的根据姓名和预约码去补录里查询样本编号
                	 */
                	log.info("name"+name);
                	String sql = "select sampleNo from xg_appointmentinfo where name='"+name+"' and yuyuenum='"+reservation+"' and ! ISNULL(sampleNo)";
                	List<Appointmentinfo> appointmentInfoList = appointmentinfoService.findBySql(sql);
                	if(appointmentInfoList.size()>0){
                		kefu.append(appointmentInfoList.get(0).getSampleNo());
                		kefuname.append(name+",");
                        Norovirusreportstate norovirusreportstate = norovirusreportstateService.findUniqueBy(appointmentInfoList.get(0).getSampleNo());
                        if(norovirusreportstate == null){
                            norovirusreportstate = new Norovirusreportstate();
                            norovirusreportstate.setSampleNo(appointmentInfoList.get(0).getSampleNo());
                            norovirusreportstate.setReportState("未生成");
                            norovirusreportstateService.save(norovirusreportstate);
                        }
                	}else{
                		noyuyue.append(name+",");
                	}
                }
            }
        }
        String re="";
        
        if("".equals(re)){
            re="报告生成成功,";
            if(sb!=null && !sb.toString().equals("")){
                re+="其中样本姓名"+sb.toString().substring(0,sb.toString().length()-1)+"不为阴性,不出报告,";
            }
            if(kefu!=null && !kefu.toString().equals("")){
                re+="样本姓名为"+kefuname.toString()+"客服未补录,";
                sendEmailService.xinguan("新型冠状病毒客服未补录","样本编号为"+kefu.toString()+"客服未补录信息,请及时补录信息",null);
            }
            if(!noyuyue.toString().equals("")){
            	re+="样本姓名为"+noyuyue.toString()+"无样本绑定信息,请留意";
            }
        }
        msg.setMsg(re.substring(0,re.length()-1));
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }

    /**
     * 一键生成客服未补录的所有样本报告
     * @return
     * @throws Exception
     */
    /*public String buscReport() throws Exception{
        String sql = "select id,sampleNo,detectionResult,reportState,inputName,inputTime,updateName,updateTime from norovirus_reportstate where reportState = '未处理'";
        List<Norovirusreportstate> ln=norovirusreportstateService.findBySampleNoAll(sql);
        StringBuffer reportpath = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        StringBuffer kefu = new StringBuffer();
        StringBuffer sc = new StringBuffer();

        for(int i=0;i<ln.size();i++){
            String pathName = "";
            String osno=ln.get(i).getSampleNo();
            if(!"".equals(osno.trim())&&osno.trim()!=null) {
                if(ln.get(i).getDetectionResult().equals("阴性")){
                    Appointmentinfoxgwx appont =appointmentinfoxgwxService.findUniqueBySampleNo(osno);
                    Norovirus norovirus = norovirusService.findUniqueBy(osno);

                    if(norovirus!=null){
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());

                        pathName =  norovirusService.createPdf(norovirus,"xg.pdf");

                        String qrimgUrl=qrCodeKeyService.createQrCode(norovirus.getSampleNo(),norovirus.getName(),false);
                        //调用方法插入图片
                        qrCodeKeyService.insertQrcodeImg(qrimgUrl,"/samplereport/"+pathName,1,413,780,null);
                        //已经存在
                        norovirus.setPathName(pathName);
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirusService.update(norovirus);
                        reportpath.append(pathName+",");
                        sc.append(osno+",");
                        ln.get(i).setReportState("已处理");
                        norovirusreportstateService.update(ln.get(i));
                    }else if(appont!=null){
                        if(norovirus == null){
                            norovirus = new Norovirus();
                        }
                        norovirus.setIdcard(appont.getSfz());
                        norovirus.setCheckProject(appont.getCheckProject());
                        norovirus.setInspection(appont.getInspection());
                        norovirus.setReceptionDate(appont.getDaoyangdate());
                        norovirus.setSamplingDate(appont.getDaoyangdate());
                        norovirus.setSampleType(appont.getSampleType());
                        norovirus.setName(appont.getName());
                        norovirus.setSendingPerson(appont.getSending());
                        norovirus.setReservation(appont.getYuyuenum());
                        norovirus.setSampleNo(appont.getSampleNo());
                        norovirus.setDetectionResult("阴性");
                        pathName =  norovirusService.createPdf(norovirus,"xg.pdf");
                        String qrimgUrl=qrCodeKeyService.createQrCode(appont.getSampleNo(),appont.getName(),false);
                        //调用方法插入图片
                        qrCodeKeyService.insertQrcodeImg(qrimgUrl,"/samplereport/"+pathName,1,413,780,null);
                        norovirus.setPathName(pathName);
                        norovirus.setPathName(pathName);
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirusService.save(norovirus);
                        reportpath.append(pathName+",");
                        sc.append(osno+",");
                        ln.get(i).setReportState("已处理");
                        norovirusreportstateService.update(ln.get(i));
                    }else{
                        kefu.append(osno+",");
                    }
                }else{
                    sb.append(ln.get(i).getSampleNo()+",");
                }
            }
        }
        String re="";
        //
        if(!sc.toString().equals("")){
            re="报告生成成功";
        }else if(ln.size()==0){
            re = "暂无未补录要生成报告的样本";
        }else{
            re = "其余样本均为添加补录信息,不能添加生成报告,已发邮件提醒";
        }
        if(kefu!=null && !kefu.toString().equals("")){
            //re+="样本为"+kefu.toString()+"客服未补录";
            //sendEmailService.xinguan("新型冠状病毒客服未补录","样本编号为"+kefu.toString()+"客服未补录信息,请及时补录信息",null);
        }
        msg.setMsg(re);
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }*/


    /**
     * 新冠客服生成报告
     * @return
     * @throws Exception
     */
   /* public String kescReport() throws Exception{
        List<Norovirus> ln=Struts2Utils.conver(itmessc,Norovirus.class);
        StringBuffer reportpath = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        StringBuffer kefu = new StringBuffer();
        for(int i=0;i<ln.size();i++){
            String pathName = "";
            String osno=ln.get(i).getSampleNo();
            if(!"".equals(osno.trim())&&osno.trim()!=null) {
                if(ln.get(i).getDetectionResult().equals("阴性")){
                    Norovirus norovirus =norovirusService.findUniqueBy(osno);
                    if(norovirus!=null){
                        norovirus.setDetectionResult("阴性");

                        if(ln.get(i).getCheckProject().indexOf("诺如")!=-1){
                            pathName =  norovirusService.createPdf(norovirus,"nr.pdf");
                        }else{
                            pathName =  norovirusService.createPdf(norovirus,"xg.pdf");
                        }

                        String qrimgUrl=qrCodeKeyService.createQrCode(ln.get(i).getSampleNo(),ln.get(i).getName(),false);
                        //调用方法插入图片
                        qrCodeKeyService.insertQrcodeImg(qrimgUrl,"/samplereport/"+pathName,1,413,780,null);
                        ln.get(i).setPathName(pathName);
                        //已经存在
                        norovirus.setPathName(pathName);
                        norovirus.setDetectionResult(ln.get(i).getDetectionResult());
                        norovirusService.update(norovirus);
                        reportpath.append(pathName+",");
                    }else{
                        kefu.append(ln.get(i).getSampleNo()+",");
                    }
                }else{
                    sb.append(ln.get(i).getSampleNo()+",");
                }
            }
        }
        String re="";
        //
        if("".equals(re)){
            re="报告生成成功";
            if(sb!=null && !sb.toString().equals("")){
                re+=",其中样本"+sb.toString()+"为阳性,不出报告,";
            }
            if(kefu!=null && !kefu.toString().equals("")){
                re+="样本为"+kefu.toString()+"客服未补录";
                sendEmailService.xinguan("新型冠状病毒客服未补录","样本编号为"+kefu.toString()+"客服未补录信息,请及时补录信息",null);
            }
        }
        msg.setMsg(re);
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }*/


    public String downloadsample() throws Exception{
        List<Norovirus> ln=Struts2Utils.conver(itmespi,Norovirus.class);
        StringBuffer reportpath = new StringBuffer();
        StringBuffer s = new StringBuffer();
        String re = "样本编号为";

        String sql = "select id,`name`,idcard,reservation,sampleNo,sendingPerson,sampleType,checkProject,pathName,inspection,samplingDate,receptionDate,ct,inputTime,inputName,iss,detectionResult " +
                " from norovirus where 1=1 ";
        sql += " AND sampleNo in (''";
        for (Norovirus n: ln) {
            sql+=",'"+n.getSampleNo()+"'";
        }
        sql += ")";

        Page<Norovirus> page = new Page<Norovirus>();
        norovirusService.findBySampleNoAll(page,sql);
        List<Norovirus> noroviriList = page.getResult();
        for (Norovirus n:noroviriList) {
            if(n.getDetectionResult() == null || n.getDetectionResult().equals("")){
                s.append(n.getSampleNo()+",");
                continue;
            }
            reportpath.append(n.getPathName()+",");
        }
        if(!s.toString().equals("")){
            re+=s.toString()+"未生成报告,不能下载";
            msg.setMsg(re);
        }
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }

    public String downloadExcel(){
        List<Norovirus> noroviriList=Struts2Utils.conver(itemsxjyb,Norovirus.class);
        if(noroviriList.size() == 0){
            String sql= this.getSql("DESC","新型冠状","中文和英文");
            Page<Norovirus> page = new Page<Norovirus>();
            norovirusService.findBySampleNoAll(page,sql);
            noroviriList = page.getResult();
        }
        if(noroviriList.size()>0) {
            if(noroviriList.size()<=30000){
                for (Norovirus norovirus:noroviriList) {
                    String sql =  this.getCaiwuSql(norovirus);
                    Appointmentinfo appointmentinfo = appointmentinfoService.findByYuyuenum(sql);
                }
                String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath(separator+"documents");
                WriteExcel.writxinguanExcel(noroviriList, uploadFileSavePath + separator+"xinguaninfo.xls");
                Struts2Utils.renderJson("EXCEL已导出");
            }else{
                Struts2Utils.renderJson("否");
            }

        }else{
            Struts2Utils.renderJson("无可导出数据");
        }
        return null;
    }
    public String nrdownloadExcel(){
        List<Norovirus> noroviriList=Struts2Utils.conver(itemsxjyb,Norovirus.class);
        if(noroviriList.size() == 0){
            String sql= this.getSql("DESC","诺如","");
            Page<Norovirus> page = new Page<Norovirus>();
            norovirusService.findBySampleNoAll(page,sql);
            noroviriList = page.getResult();
        }
        if(noroviriList.size()>0) {
            if(noroviriList.size()<=30000){
                String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath(separator+"documents");
                WriteExcel.writnuoruExcel(noroviriList, uploadFileSavePath + separator+"nuoruinfo.xls");
                Struts2Utils.renderJson("EXCEL已导出");
            }else{
                Struts2Utils.renderJson("否");
            }

        }else{
            Struts2Utils.renderJson("无可导出数据");
        }
        return null;
    }

    public String caiwuExcel(){
        List<Norovirus> noroviriList=Struts2Utils.conver(itemsxjyb,Norovirus.class);
        if(noroviriList.size() == 0){
            String sql= this.getSql("DESC","新型冠状","中文和英文");
            Page<Norovirus> page = new Page<Norovirus>();
            norovirusService.findBySampleNoAll(page,sql);
            noroviriList = page.getResult();
        }
        if(noroviriList.size()>0) {
            /*if(noroviriList.size()<=30000){*/
                for (Norovirus norovirus:noroviriList) {

                    String sql =  this.getCaiwuSql(norovirus);
                    Appointmentinfo appointmentinfo = appointmentinfoService.findByYuyuenum(sql);
                    if(appointmentinfo!=null){
                        String fapiaopsql =  this.getBukaifapiaoSql(appointmentinfo);
                        XgBuKaiFaPiao xgBuKaiFaPiao = xgBuKaiFaPiaoService.findByYuyuenum(fapiaopsql);
                        norovirus.setAppointmentinfo(appointmentinfo);
                        norovirus.setXgBuKaiFaPiao(xgBuKaiFaPiao);
                    }

                }
                String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath(separator+"documents");
                WriteExcel.writxinguancaiwuExcel(noroviriList, uploadFileSavePath + separator+"xinguancaiwu.xls");
                Struts2Utils.renderJson("EXCEL已导出");
            /*}else{
                Struts2Utils.renderJson("最多导出30000条数据！");
            }*/
        }else{
            Struts2Utils.renderJson("无可导出数据");
        }
        return null;
    }


    /**
     * 下载报告
     */
    @SuppressWarnings({ "unchecked", "deprecation",  "unused" })
    public String downloadreport(){
        List<File> lf=new ArrayList<File>();
        //生成电子版客户信息
        SimpleDateFormat time=new SimpleDateFormat("yyyyMM");
        String cqrq = time.format(new Date());
        String uploadFileSavePath= ServletActionContext.getServletContext().getRealPath(separator+"samplereport"+separator+"cqzip"+separator+cqrq);
        String root = ServletActionContext.getServletContext().getRealPath(separator+"samplereport"+separator);
        if (!new File(uploadFileSavePath).exists()){//判断目录是否存在
            boolean flafile = new File(uploadFileSavePath).mkdirs();
        }

        if (!new File(root).exists()){//判断目录是否存在
            boolean flafile = new File(uploadFileSavePath).mkdirs();
        }

        // zip存放路径
        String zipfilePath = uploadFileSavePath +separator+"报告.zip";
        File zipfile = new File(zipfilePath);

        String [] reportabPath = reportPath.split(",");
        if(reportabPath!=null && reportabPath.length!=0){
            for (String oneReprotPaht: reportabPath) {
                File oneReportfile = new File(root+oneReprotPaht);
                lf.add(oneReportfile);
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
                out.putNextEntry(new ZipEntry(file.getName()));
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
            String name=new String("报告".getBytes("UTF-8"), "ISO-8859-1");
            Struts2Utils.getResponse().addHeader("Content-Disposition", "attachment;filename=\""+name+Struts2Utils.getymd(new Date())+".zip"+"\"");
            OutputStream os = Struts2Utils.getResponse().getOutputStream();
            String path=uploadFileSavePath+separator+"报告.zip";
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


    public void getyichubaogao(){
        String sql="select id from norovirus where pathName is not null and pathName!='' ";

        if(startdate!=null && !startdate.equals("")){
            startdate=startdate.replace("T"," ");
            sql+=" and inputTime >= '"+startdate+"'";
        }
        if(enddate!=null && !enddate.equals("")){
            enddate=enddate.replace("T"," ");
            sql+=" and inputTime <= '"+enddate+"'";
        }
        int count=norovirusreportstateService.count(sql);
        String c=count+"";
        Struts2Utils.renderJson(c);
    }

    public String reportdataexcel() throws IllegalAccessException, ParseException {
            String sql1 = "select name,idcard,detectionResult,sampleNo,reportdate from norovirus where 1=1 ";
            //导出当前时间凌晨4点到第二条凌晨四点的数据
            if (startdate!=null&&startdate.length()>1){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date sDate = sdf.parse(startdate);
                Calendar c = Calendar.getInstance();
                c.setTime(sDate);
                c.add(Calendar.DAY_OF_MONTH, 1);
                sDate = c.getTime();
                enddate = sdf.format(sDate);
                sql1+=" and reportdate >= '"+startdate +" 04:00:00' and reportdate<='"+enddate +" 04:00:00'";
            }

            sql1+=" and checkProject like '%新型冠状病毒%' and qudao !='张家口宣化平安医院'";
            List<Norovirus> norovirusList = norovirusService.findQudaoAll(sql1);
            WebPathUtil webPathUtil = new WebPathUtil();
            String uploadFileSavePath = webPathUtil.getRealPath(separator+"documents");
            String res = norovirusService.reportDateExcel(norovirusList, uploadFileSavePath + separator+"核酸检测信息-北京优迅医学检验实验室-孟岳峰-13718137762.xls");
            msg.setSuccess(true);
            msg.setMsg(res);
            Struts2Utils.renderJson(msg);
            return null;

    }
    
    public String updateSendingPerson() throws HibernateException, SQLException{
    	List<Norovirus> ln=Struts2Utils.conver(itmessc,Norovirus.class);
    	String res = norovirusService.updateSendingPersons(ln);
    	msg.setSuccess(true);
    	msg.setMsg(res);
    	Struts2Utils.renderJson(msg);
    	return null;
    }

    /***
     * 样本重复验证
     */
    public String checkSampleNo() throws Exception{

        if(!"".equals(entity.getSampleNo().trim())){
            List<Norovirus> lc=norovirusService.findBySampleNo(entity.getSampleNo());

                if(lc.size()==0){//
                    Struts2Utils.renderJson("修改成功");
                }else{
                    if(lc.get(0).getId().equals(entity.getId())){
                        Struts2Utils.renderJson("没修改");//
                    }else{
                        Struts2Utils.renderJson("存在");
                    }
                }

        }

        return null;
    }

    @Override
    public String modulepage() throws Exception {
        return "modulepage";
    }

    public String modulepageone() throws Exception {
        return "modulepageone";
    }

    public String modulepagetwo() throws Exception {
        return "modulepagetwo";
    }

    @Override
    public String save() throws Exception {
        return null;
    }

    @Override
    protected void prepareModel() throws Exception {
        entity = new Norovirus();
    }

    @Override
    public Norovirus getModel() {
        return entity;
    }

    public Norovirus getEntity() {
        return entity;
    }

    public void setEntity(Norovirus entity) {
        this.entity = entity;
    }

    public String getIsShowSampleNo() {
		return isShowSampleNo;
	}

	public void setIsShowSampleNo(String isShowSampleNo) {
		this.isShowSampleNo = isShowSampleNo;
	}

	public String getDcTime() {
		return dcTime;
	}

	public void setDcTime(String dcTime) {
		this.dcTime = dcTime;
	}
}
