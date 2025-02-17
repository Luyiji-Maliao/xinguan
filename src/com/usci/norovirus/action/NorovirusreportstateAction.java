package com.usci.norovirus.action;

import com.lims.core.orm.Page;
import com.lims.core.utils.excel.WriteExcel;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.entity.Appointmentinfo;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Norovirus;
import com.usci.norovirus.entity.Norovirusreportstate;
import com.usci.norovirus.service.AppointmentinfoService;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.norovirus.service.NorovirusService;
import com.usci.norovirus.service.NorovirusreportstateService;
import com.usci.report.service.QrCodeKeyService;
import com.usci.system.service.SendEmailService;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })

@Results({
        @Result(name = "logins", type = "redirect", location = "session.jsp"),
        @Result(name = "modulepage", location = "/WEB-INF/content/norovirus/reportstate.jsp"),
})
public class NorovirusreportstateAction extends CrudActionSupport<Norovirus> {

    private Norovirus entity;

    private String itemsxjyb;

    private String itmespi;

    private String itmessc;

    private String reportPath;

    private String ininputTime;

    private String osoinputTime;

    private String searchReservation;
    private String searchSampleNo;
    private String searchName;

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchReservation() {
        return searchReservation;
    }

    public void setSearchReservation(String searchReservation) {
        this.searchReservation = searchReservation;
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

    public String getOsoinputTime() {
        return osoinputTime;
    }

    public void setOsoinputTime(String osoinputTime) {
        this.osoinputTime = osoinputTime;
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

    @Override
    public String list() throws Exception {
        String sql= this.getSql("DESC");
        Page<Norovirusreportstate> page = new Page<Norovirusreportstate>(limit);
        page.setPageNo((start / limit) + 1);
        norovirusreportstateService.findPage(page,sql,"",start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }


    public String getSql(String orderby){

        String sql = "select s.id,s.sampleNo,s.detectionResult,s.inputName,s.inputTime,s.updateName,s.updateTime,s.reportState from norovirus_reportstate s " +
                " where 1=1";

        if(searchSampleNo!=null && !searchSampleNo.equals("")){
            sql += " AND sampleNo like "+"'%"+searchSampleNo+"%'";
        }

        sql+=" order by s.reportState " +orderby;
        return sql;
    }

    /**
     * 一键生成客服未补录的所有样本报告
     * @return
     * @throws Exception
     */
    public String buscReport() throws Exception{
        List<Norovirusreportstate> ln=Struts2Utils.conver(itmessc,Norovirusreportstate.class);
        String quanbu = "";
        if(ln == null || ln.size() == 0){
            String sql = "select id,sampleNo,detectionResult,reportState,inputName,inputTime,updateName,updateTime from norovirus_reportstate where reportState = '未生成'";
            ln=norovirusreportstateService.findBySampleNoAll(sql);
            quanbu = "quanbu";
        }
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
                    Norovirusreportstate ns = norovirusreportstateService.findUniqueBy(osno);

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
                        ns.setReportState("已生成");
                        norovirusreportstateService.update(ns);
                    }else if(appont!=null){
                        if(norovirus == null){
                            norovirus = new Norovirus();
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
                        norovirus.setIdcard(appont.getSfz());
                        norovirus.setCheckProject(appont.getCheckProject());
                        norovirus.setInspection(appont.getInspection());
                        norovirus.setSampleType(appont.getSampleType());
                        norovirus.setName(appont.getName());
                        norovirus.setSendingPerson(appont.getSending());
                        norovirus.setReservation(appont.getYuyuenum());
                        norovirus.setSampleNo(appont.getSampleNo());
                        norovirus.setQudao(appont.getQudao());
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
                        ns.setReportState("已生成");
                        norovirusreportstateService.update(ns);
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
            re = "其余样本均未添加补录信息,不能添加生成报告";
        }
        if(kefu!=null && !kefu.toString().equals("") && quanbu.equals("quanbu")){
            re +=",客服未补录样本已发邮件提醒";
            //sendEmailService.xinguan("新型冠状病毒客服未补录","样本编号为"+kefu.toString()+"客服未补录信息,请及时补录信息",null);
        }
        msg.setMsg(re);
        msg.setO(reportpath);
        Struts2Utils.renderJson(msg);
        return null;
    }


    @Override
    public String modulepage() throws Exception {
        return "modulepage";
    }

    public String modulepageone() throws Exception {
        return "modulepageone";
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
}
