package com.usci.norovirus.action;

import com.lims.core.orm.Page;
import com.lims.core.utils.excel.WriteExcel;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.ndktools.javamd5.Mademd5;
import com.usci.norovirus.entity.Appointmentinfo;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Norovirus;
import com.usci.norovirus.service.AppointmentinfoService;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.norovirus.service.NorovirusService;
import com.usci.report.service.QrCodeKeyService;
import com.usci.system.service.SendEmailService;
import com.usci.system.service.SendUrlService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        @Result(name = "modulepage", location = "/WEB-INF/content/norovirus/appointmentinfo.jsp"),
})
public class AppointmentinfoxgwxAction extends CrudActionSupport<Appointmentinfoxgwx> {
	private Logger log = LoggerFactory.getLogger(AppointmentinfoxgwxAction.class);
    private Appointmentinfoxgwx entity;

    private String itemsxjyb;

    private String searchSfz;

    private String searchName;

    private String searchYuyuenum;
    private String tuikuanstate;

    private String ininputTime;

    private String osoinputTime;
    
    private String samplebindstarttime;
    private String samplebindendtime;
    
    public String getSearchSfz() {
        return searchSfz;
    }

    public void setSearchSfz(String searchSfz) {
        this.searchSfz = searchSfz;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchYuyuenum() {
        return searchYuyuenum;
    }

    public void setSearchYuyuenum(String searchYuyuenum) {
        this.searchYuyuenum = searchYuyuenum;
    }

    public String getItemsxjyb() {
        return itemsxjyb;
    }

    public void setItemsxjyb(String itemsxjyb) {
        this.itemsxjyb = itemsxjyb;
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

    public String getTuikuanstate() {
        return tuikuanstate;
    }

    public void setTuikuanstate(String tuikuanstate) {
        this.tuikuanstate = tuikuanstate;
    }

    public String getSamplebindstarttime() {
		return samplebindstarttime;
	}

	public void setSamplebindstarttime(String samplebindstarttime) {
		this.samplebindstarttime = samplebindstarttime;
	}

	public String getSamplebindendtime() {
		return samplebindendtime;
	}

	public void setSamplebindendtime(String samplebindendtime) {
		this.samplebindendtime = samplebindendtime;
	}

	@Autowired
    private AppointmentinfoxgwxService appointmentinfoxgwxService;
    @Autowired
    private NorovirusService norovirusService;


    @Autowired
    private SendUrlService sendUrlService;//手机短信

    @Override
    public String list() throws Exception {
        String sql= this.getSql("DESC",null,null);
        Page<Appointmentinfoxgwx> page = new Page<Appointmentinfoxgwx>(limit);
        page.setPageNo((start / limit) + 1);
        appointmentinfoxgwxService.findPage(page,sql,"",start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }

    public String list1() throws Exception {
        String sql= this.getSql("DESC","","已退款");
        Page<Appointmentinfoxgwx> page = new Page<Appointmentinfoxgwx>(limit);
        page.setPageNo((start / limit) + 1);
        appointmentinfoxgwxService.findPage(page,sql,"",start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }



    public String getSql(String orderby,String orderById,String tuikuan){


        String sql = "select a.id,a.reportdate,a.openid,a.name,a.englishName,a.sex,a.age,a.sending,a.sfz,a.passport,a.phone,a.isgeli,a.comebjtime,a.comebjreason, " +
                "a.yuyuedate,a.yuyuetime,a.caiyangdidian," +
                "if(b.yuyueid is null,if(fp.yuyueid is null,a.isfapiao,concat('已开',a.isfapiao)),if(b.bukaistate = '已补开',concat(b.bukaistate,b.isfapiao),concat('需补开',b.isfapiao)) ) as isfapiao, " +
                " if(b.yuyueid is null,a.shuihao,b.shuihao) as shuihao, " +
                " if(b.yuyueid is null,a.fapiaotaitou,b.fapiaotaitou) as fapiaotaitou, " +
                " if(b.yuyueid is null,a.zhucedizhi,b.zhucedizhi) as zhucedizhi, " +
                " if(b.yuyueid is null,a.zhucedianhua,b.zhucedianhua) as zhucedianhua, " +
                " if(b.yuyueid is null,a.kaihuyinhang,b.kaihuyinhang) as kaihuyinhang, " +
                " if(b.yuyueid is null,a.yinhangzhanghao,b.yinhangzhanghao) as yinhangzhanghao, " +
                " if(b.yuyueid is null,a.fapiaotype,b.fapiaotype) as fapiaotype, " +
                " if(b.yuyueid is null,a.accessname,b.accessname) as accessname, " +
                " if(b.yuyueid is null,a.accessphone,b.accessphone) as accessphone, " +
                " if(b.yuyueid is null,a.accessaddress,b.accessaddress) as accessaddress, " +
                " if(b.yuyueid is null,a.accessemail,b.accessemail) as accessemail, " +
                "a.isagree,a.outtradeno,a.totalfee,a.inspection,a.issuccessed,a.yuyuenum,a.xgreason,a.subjecttype, " +
                "a.cooperationstr,a.sampleNo,a.qudao,a.shicaiyangdian,a.inputTime,a.inputName,a.updateTime,a.updateName, " +
                "a.samplebindtime,a.customerbindtime,a.duihuanma,f.tuikuan,f.tuikuandate,f.tuikuanname,f.tuikuanyuanyin,f.tuikuanjine " +
                ",f.shenqingdate,f.shenqingname,f.shijijine," +
                "if(fp.yuyueid is null,if(b.yuyueid is null,null,b.kaipiaono),fp.kaipiaono) as kaipiaono," +
                "if(fp.yuyueid is null,if(b.yuyueid is null,null,b.kaipiaodate),fp.kaipiaodate) as kaipiaodate," +
                "if(fp.yuyueid is null,if(b.yuyueid is null,null,b.bukaistate),fp.kaipiaostate) as kaipiaostate," +
                "h.shouxufei,h.jinzhangmoney,h.collectionmethod,h.kaipiaomoney,if(h.yuyueid is null,'否',h.huikuanstate) as huikuanstate " +
                " from xg_appointmentinfo a LEFT JOIN xg_financial f on  " +
                "(f.xgappid=a.id and f.outtradeno = f.outtradeno)" +
                "LEFT JOIN xg_bukaifapiao b on(b.yuyueid = a.id)" +
                "LEFT JOIN xg_fapiao fp on (fp.yuyueid = a.id)" +
                "LEFT JOIN xg_huikuan h on (h.yuyueid = a.id)" +
                "where 1=1";

        if(searchName!=null && !searchName.equals("")){
            sql+=" AND a.name like'%"+searchName+"%'";
        }
        if(searchSfz!=null  && !searchSfz.equals("")){
            sql+=" AND (a.sfz like'%"+searchSfz+"%' or a.phone like '%"+searchSfz+"%')";
        }
        if(searchYuyuenum!=null  && !searchYuyuenum.equals("")){
            sql+=" AND (a.yuyuenum like'%"+searchYuyuenum+"%' or a.outtradeno like '%"+searchYuyuenum+"%')";
        }
        if(entity.getPhone()!=null&&!"".equals(entity.getPhone())){
            sql+=" AND a.phone like '%"+entity.getPhone()+"%' ";
        }
        if(entity.getOuttradeno()!=null&&!"".equals(entity.getOuttradeno())){
        	sql+=" AND a.outtradeno like '%"+entity.getOuttradeno()+"%' ";
        }
        if(entity.getOpenid()!=null&&!"".equals(entity.getOpenid())){
        	sql+=" AND a.openid = '"+entity.getOpenid()+"' ";
        }
        if(entity.getIsfapiao()!=null&&!"".equals(entity.getIsfapiao())){
        	sql+=" AND a.isfapiao like '%"+entity.getIsfapiao()+"%' ";
        }
        if(entity.getCaiyangdidian()!=null&&!"".equals(entity.getCaiyangdidian())){
        	sql+=" AND a.caiyangdidian like '%"+entity.getCaiyangdidian()+"%' ";
        }
        if(entity.getCooperationstr()!=null&&!"".equals(entity.getCooperationstr())){
        	sql+=" AND a.cooperationstr like '%"+entity.getCooperationstr()+"%' ";
        }
        if(samplebindstarttime!=null&&!"".equals(samplebindstarttime)&&samplebindstarttime.length()>=10){
        	sql+=" AND a.samplebindtime > '"+samplebindstarttime.substring(0, 10)+" 00:00:00"+"' ";
        }
        if(samplebindendtime!=null&&!"".equals(samplebindendtime)&&samplebindendtime.length()>=10){
        	sql+=" AND a.samplebindtime < '"+samplebindendtime.substring(0, 10)+" 24:00:00"+"' ";
        }
        if(entity.getSampleNo()!=null&&!"".equals(entity.getSampleNo())){
        	sql+=" AND a.sampleNo like '%"+entity.getSampleNo()+"%' ";
        }
        if(entity.getDuihuanma()!=null&&!"".equals(entity.getDuihuanma())){
        	sql+=" AND a.duihuanma like '%"+entity.getDuihuanma()+"%' ";
        }
        if(entity.getTuikuan()!=null&&!"".equals(entity.getTuikuan())){
            sql+=" AND f.tuikuan like '%"+entity.getTuikuan()+"%' ";
        }
        if(entity.getShicaiyangdian()!=null&&!"".equals(entity.getShicaiyangdian())){
        	sql+=" AND a.shicaiyangdian like '%"+entity.getShicaiyangdian()+"%' ";
        }
        if(entity.getHuikuanstate()!=null&&!"".equals(entity.getHuikuanstate())){
            sql+=" AND if(h.yuyueid is null,'否',h.huikuanstate) like '%"+entity.getHuikuanstate()+"%' ";
        }
        if(entity.getKaipiaostate()!=null&&!"".equals(entity.getKaipiaostate())){
            sql+=" AND if(fp.yuyueid is not null,'是',if(b.bukaistate = '已补开','是','否')) like '%"+entity.getKaipiaostate()+"%' ";
        }
        if(entity.getKaipiaono()!=null&&!"".equals(entity.getKaipiaono())){
            sql+=" AND if(fp.yuyueid is null,if(b.yuyueid is null,null,b.kaipiaono),fp.kaipiaono) like '%"+entity.getKaipiaono()+"%' ";
        }
        if(entity.getSubjecttype()!=null&&!"".equals(entity.getSubjecttype())){
            sql+=" AND a.subjecttype like '%"+entity.getSubjecttype()+"%' ";
        }
        //默认为已付款，如果entity.getIssuccessed的值是全部，那么不筛选
        if("全部".equals(entity.getIssuccessed())){
        	
        }else{
        	sql+=" AND a.issuccessed = '是' ";
        }
        if(entity.getShuihao()!=null&&!"".equals(entity.getShuihao())){
        	sql+=" AND a.shuihao LIKE '%"+entity.getShuihao()+"%' ";
        }
        if(entity.getQudao()!=null&&!"".equals(entity.getQudao())){
        	sql+=" and a.qudao LIKE '%"+entity.getQudao()+"%' ";
        }

        
        
        if(tuikuan!=null){
            sql+=" AND f.tuikuan is not null";
        }
        if(tuikuanstate!=null && !tuikuanstate.equals("")){
            sql += " AND f.tuikuan like '%"+tuikuanstate+"%'";
        }
        if(ininputTime!=null &&!"".equals(ininputTime)){
            sql += " AND STR_TO_DATE(f.shenqingdate,'%Y-%m-%d') >= "+"'"+ininputTime+"'";
        }

        if(osoinputTime!=null &&!"".equals(osoinputTime)){
            sql += " AND STR_TO_DATE(f.shenqingdate,'%Y-%m-%d') <= "+"'"+osoinputTime+"'";
        }
        if(orderById == null){
            sql+=" order by a.id " +orderby;
        }else{
            sql+=" order by CASE WHEN f.tuikuan like '%申请退款%' then 1 WHEN f.tuikuan like '%已退款%' THEN 2 ELSE 3 END,f.shenqingdate " +orderby;
        }

        log.info(sql);
        return sql;
    }


    public String getSqlTotal(String date){
        String sql = "select count(1) total from xg_appointmentinfo where 1=1 and yuyuedate ='"+date+"'";
        return sql;
    }


    public String saveAll() throws Exception{
        List<Appointmentinfoxgwx> ln=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<ln.size();i++){
             /*String total = appointmentinfoxgwxService.findBySqlSelectCount(this.getSqlTotal(ln.get(i).getYuyuedate()));
                String sql = "select count(1) from xg_appointmentinfo where name = '"+ln.get(i).getName()+"' and sfz = '"+ln.get(i).getSfz()+"' and (openid is  null or openid = '')";
                String openidCount = appointmentinfoxgwxService.findBySqlSelectCount(sql);
                if(Integer.parseInt(openidCount)==0){
                    String yunum ="";
                    if(total!=null){
                        int tot = Integer.parseInt(total);
                        String s = (tot+1)+"";
                        if(s.length()==1 && tot!=9){
                            yunum+="000"+(tot+1);
                        }else  if(s.length()==2){
                            yunum+="00"+(tot+1);
                        }else  if(s.length()==3){
                            yunum+="0"+(tot+1);
                        }else{
                            yunum = ""+(tot+1);
                        }
                    }
                    String date = ln.get(i).getYuyuedate();
                    String yuyuenum = "YX"+date.substring(5,7)+date.substring(8,10)+yunum;
                    ln.get(i).setYuyuenum(yuyuenum);*/
            ln.get(i).setIssuccessed("是");
            ln.get(i).setSampleType("咽拭子");
            ln.get(i).setCheckProject("新型冠状病毒 2019-nCoV 核酸检测");
            ln.get(i).setInspection("优迅医学");
            appointmentinfoxgwxService.save(ln.get(i));
            if(ln.get(i).getPhone()!=null && !ln.get(i).getPhone().equals("")){
                String smsTemplate = "尊敬的用户，您已成功下单优迅医学核酸检测服务，完成预约还需您前往“优迅医学”微信公众号填写个人信息，并完成采样点和日期的预约。完成预约后，我们将为您生成预约码，请您携带好身份证件及预约码及时前往采样点采样，感谢您的配合。【预约流程：微信搜索并关注优迅医学公众号——新冠检测——预约检测——填写信息】";
                //sendUrlService.reportIssueSendSms(ln.get(i).getPhone(),smsTemplate);
            }
                /*}*/
        }
        String re="";
        //
        if("".equals(re)){
            re="添加成功";
        }
        msg.setMsg(re);
        Struts2Utils.renderJson(msg);
        return null;
    }


    /**
     * 上地医院导出推荐客户的信息
     * @return
     */
    public String shangdiExcel(){
        List<Appointmentinfoxgwx> applist=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        int total=0;
        if(applist.size() <= 0){
            String sql =  this.getcaiyangSql("上地医院");
            appointmentinfoxgwxService.getTotalCount(sql);
            if(total<=30000){
                applist=appointmentinfoxgwxService.entitybysql(sql);
            }
        }

        if(applist.size()<=30000 && total <=30000){
            for (Appointmentinfoxgwx app:applist) {
                List<Norovirus> noroviruslist=norovirusService.findBySampleNo(app.getSampleNo());
                if(noroviruslist.size()>0){
                    app.setNorovirus(noroviruslist.get(0));
                }
            }
            List<Norovirus> ns=norovirusService.findQudaoAll(this.getshagdifafareSql());

            for (Norovirus n : ns) {
                Appointmentinfoxgwx appx=new Appointmentinfoxgwx();
                appx.setId(n.getId());
                appx.setSampleNo(n.getSampleNo());
                appx.setSfz(n.getIdcard());
                appx.setSending(n.getSendingPerson());
                appx.setName(n.getName());
                appx.setSamplebindtime(n.getSamplingDate());
                appx.setYuyuenum(n.getReservation());
                appx.setQudao(n.getQudao());
                appx.setNorovirus(n);
                applist.add(appx);
            }

            if(applist.size()<=0) {
                Struts2Utils.renderJson("无可导出数据");
                return null;
            }

            String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath("/documents");
            WriteExcel.caiyangdianExcel(applist, uploadFileSavePath + "/上地医院.xls");
            Struts2Utils.renderJson("EXCEL已导出");
        }else{
            Struts2Utils.renderJson("否");
        }
        return null;
    }


    /**
     * 邯钢宾馆导出推荐客户的信息
     * @return
     */
    public String hangangExcel(){
        List<Appointmentinfoxgwx> applist=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        int total=0;
        if(applist.size() <= 0){
            String sql =  this.getcaiyangSql("邯钢");
            appointmentinfoxgwxService.getTotalCount(sql);
            if(total<=30000){
                applist=appointmentinfoxgwxService.entitybysql(sql);
            }
        }
        if(applist.size()<=0) {
            Struts2Utils.renderJson("无可导出数据");
            return null;
        }
        if(applist.size()<=30000 && total <=30000){
            for (Appointmentinfoxgwx app:applist) {
                List<Norovirus> noroviruslist=norovirusService.findBySampleNo(app.getSampleNo());
                if(noroviruslist.size()>0){
                    app.setNorovirus(noroviruslist.get(0));
                }
            }
            String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath("/documents");
            WriteExcel.caiyangdianExcel(applist, uploadFileSavePath + "/邯钢.xls");
            Struts2Utils.renderJson("EXCEL已导出");
        }else{
            Struts2Utils.renderJson("否");
        }
        return null;
    }

    /**
     * 预约表财务导出信息
     * @return
     */
    public String caiwuExcel(){
        List<Appointmentinfoxgwx> applist=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        int total=0;
        if(applist.size() <= 0){
            String sql =  this.caiwuSql();
            appointmentinfoxgwxService.getTotalCount(sql);
            if(total<=30000){
                applist=appointmentinfoxgwxService.entitybysql(sql);
            }
        }

/*        if(applist.size()<=30000 && total <=30000){*/
            if(applist.size()<=0) {
                Struts2Utils.renderJson("无可导出数据");
                return null;
            }

            String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath("/documents");
            WriteExcel.caiwuExcel(applist, uploadFileSavePath + "/yuyuecaiwu.xls");
            Struts2Utils.renderJson("EXCEL已导出");
      /*  }else{
            Struts2Utils.renderJson("否");
        }*/
        return null;
    }

    /**
     * 预约表导出信息
     * @return
     */
    public String yuyueExcel(){
        List<Appointmentinfoxgwx> applist=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        int total=0;
        if(applist.size() <= 0){
            String sql =  this.caiwuSql();
            appointmentinfoxgwxService.getTotalCount(sql);
            if(total<=30000){
                applist=appointmentinfoxgwxService.entitybysql(sql);
            }
        }

        /*        if(applist.size()<=30000 && total <=30000){*/
        if(applist.size()<=0) {
            Struts2Utils.renderJson("无可导出数据");
            return null;
        }

        String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath("/documents");
        WriteExcel.yuyueExcel(applist, uploadFileSavePath + "/yuyueinfo.xls");
        Struts2Utils.renderJson("EXCEL已导出");
      /*  }else{
            Struts2Utils.renderJson("否");
        }*/
        return null;
    }

    /**
     * 预约表财务导出退款信息
     * @return
     */
    public String tuikuanExcel(){
        List<Appointmentinfoxgwx> applist=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        int total=0;
        if(applist.size() <= 0){
            String sql =   this.getSql("DESC","","已退款");
            appointmentinfoxgwxService.getTotalCount(sql);
            if(total<=30000){
                applist=appointmentinfoxgwxService.entitybysql(sql);
            }
        }

        if(applist.size()<=30000 && total <=30000){
            if(applist.size()<=0) {
                Struts2Utils.renderJson("无可导出数据");
                return null;
            }

            String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath("/documents");
            this.tuikuanExcel(applist, uploadFileSavePath + "/xinguantuikuan.xls");
            Struts2Utils.renderJson("EXCEL已导出");
        }else{
            Struts2Utils.renderJson("否");
        }
        return null;
    }


    public String getcaiyangSql(String caiyangdian){
        String sql = "select a.id as id, a.openid, a.`name`, a.sex, a.age, a.sfz, a.phone, a.isgeli, a.comebjtime, a.comebjreason, a.yuyuedate," +
                " a.yuyuetime, a.caiyangdidian, a.isfapiao, a.shuihao, a.fapiaotaitou, a.zhucedizhi, a.zhucedianhua, a.kaihuyinhang, a.yinhangzhanghao," +
                " a.fapiaotype, a.accessname, a.accessphone, a.accessaddress, a.accessemail, a.isagree, a.outtradeno, a.totalfee, a.issuccessed, a.yuyuenum," +
                " a.xgreason, a.subjecttype, a.cooperationstr, a.sampleNo, a.qudao, a.daoyangdate, a.shicaiyangdian, a.sampleType, a.duihuanma, a.sending," +
                " a.checkProject, a.inspection, a.inputTime, a.inputName, a.updateTime, a.updateName, a.samplebindtime, a.customerbindtime, " +
                " c.cooperationcompanyname, c.cooperationsubcompanyname, c.businessmanager, c.salename, c.permissionperson " +
                " from xg_appointmentinfo a left Join xg_cooperationinformation c on c.cooperationstr=a.cooperationstr where issuccessed='是' ";

        if(ininputTime!=null &&!"".equals(ininputTime)){
            sql += " AND STR_TO_DATE(samplebindtime,'%Y-%m-%d') >= "+"'"+ininputTime+"'";
        }

        if(osoinputTime!=null &&!"".equals(osoinputTime)){
            sql += " AND STR_TO_DATE(samplebindtime,'%Y-%m-%d') <= "+"'"+osoinputTime+"'";
        }
        if(caiyangdian!=null && !caiyangdian.equals("")){
            sql+=" and (shicaiyangdian like '%"+caiyangdian+"%' or cooperationcompanyname like '%"+caiyangdian+"%')";
        }

        return sql;
    }


    /**
     * 上地发热门诊的信息
     * @param
     * @return
     */
    public String getshagdifafareSql(){
        String sql = "select n.id,n.name,n.idcard,n.sampleNo,n.sendingPerson,n.checkProject,n.sampleType,n.inspection," +
                "n.samplingDate,n.receptionDate,n.ct,n.iss,n.detectionResult,n.inputTime,n.inputName,n.updateTime,n.updateName," +
                "n.pathName,n.reservation,n.qudao from norovirus n where 1=1 ";

        if(ininputTime!=null &&!"".equals(ininputTime)){
            sql += " AND STR_TO_DATE(n.samplingDate,'%Y-%m-%d') >= "+"'"+ininputTime+"'";
        }

        if(osoinputTime!=null &&!"".equals(osoinputTime)){
            sql += " AND STR_TO_DATE(n.samplingDate,'%Y-%m-%d') <= "+"'"+osoinputTime+"'";
        }
        sql += " AND n.qudao like '%上地医院发热门诊%'";

        return sql;
    }


    public String caiwuSql(){
        String sql = "select a.id as id,a.reportdate, a.openid, case a.updateName when '已退款' then '退款用户' else a.`name` end as name,a.sex, a.age, a.sfz, a.phone, a.isgeli, a.comebjtime,a.comebjreason, a.yuyuedate, a.yuyuetime, a.caiyangdidian, " +
                " if(b.yuyueid is null,if(fp.yuyueid is null,a.isfapiao,concat('已开',a.isfapiao)),if(b.bukaistate = '已补开',concat(b.bukaistate,b.isfapiao),concat('需补开',b.isfapiao)) ) as isfapiao, " +
                " if(b.yuyueid is null,a.shuihao,b.shuihao) as shuihao, " +
                " if(b.yuyueid is null,a.fapiaotaitou,b.fapiaotaitou) as fapiaotaitou, " +
                " if(b.yuyueid is null,a.zhucedizhi,b.zhucedizhi) as zhucedizhi, " +
                " if(b.yuyueid is null,a.zhucedianhua,b.zhucedianhua) as zhucedianhua, " +
                " if(b.yuyueid is null,a.kaihuyinhang,b.kaihuyinhang) as kaihuyinhang, " +
                " if(b.yuyueid is null,a.yinhangzhanghao,b.yinhangzhanghao) as yinhangzhanghao, " +
                " if(b.yuyueid is null,a.fapiaotype,b.fapiaotype) as fapiaotype, " +
                " if(b.yuyueid is null,a.accessname,b.accessname) as accessname, " +
                " if(b.yuyueid is null,a.accessphone,b.accessphone) as accessphone, " +
                " if(b.yuyueid is null,a.accessaddress,b.accessaddress) as accessaddress, " +
                " if(b.yuyueid is null,a.accessemail,b.accessemail) as accessemail" +
                " , a.isagree, a.outtradeno, a.totalfee, a.issuccessed, a.yuyuenum, " +
                " a.xgreason, a.subjecttype, a.cooperationstr, a.sampleNo, a.qudao, a.daoyangdate, a.shicaiyangdian, a.sampleType, a.duihuanma, a.sending, " +
                " a.checkProject, a.inspection, a.inputTime, a.inputName, a.updateTime, a.updateName, a.samplebindtime, a.customerbindtime,  " +
                " c.cooperationcompanyname, c.cooperationsubcompanyname, c.businessmanager, c.salename, c.permissionperson " +
                ",f.tuikuan,f.tuikuandate,f.tuikuanname,f.tuikuanyuanyin,f.tuikuanjine" +
                ",f.shenqingdate,f.shenqingname,f.shijijine,"+
                "if(fp.yuyueid is null,if(b.yuyueid is null,null,b.kaipiaono),fp.kaipiaono) as kaipiaono," +
                "if(fp.yuyueid is null,if(b.yuyueid is null,null,b.kaipiaodate),fp.kaipiaodate) as kaipiaodate," +
                "if(fp.yuyueid is null,if(b.yuyueid is null,null,b.bukaistate),fp.kaipiaostate) as kaipiaostate, "+
                "h.shouxufei,h.jinzhangmoney,h.collectionmethod,h.kaipiaomoney,if(h.yuyueid is null,'否',h.huikuanstate) as huikuanstate " +
                " from xg_appointmentinfo a " +
                " left Join xg_cooperationinformation c on c.cooperationstr=a.cooperationstr" +
                " left Join xg_financial f on f.xgappid=a.id" +
                " LEFT JOIN xg_bukaifapiao b on (b.yuyueid = a.id)" +
                " LEFT JOIN xg_fapiao fp on (fp.yuyueid = a.id)"+
                " LEFT JOIN xg_huikuan h on (h.yuyueid = a.id)" +
                " where issuccessed = '是' and if(a.qudao<>'团体' or(a.qudao = '团体' and sampleNo is not null),1=1,1=2)";

        if(ininputTime!=null &&!"".equals(ininputTime)){
            sql += " AND STR_TO_DATE(a.inputTime,'%Y-%m-%d') >= "+"'"+ininputTime+"'";
        }

        if(osoinputTime!=null &&!"".equals(osoinputTime)){
            sql += " AND STR_TO_DATE(a.inputTime,'%Y-%m-%d') <= "+"'"+osoinputTime+"'";
        }

        if(searchName!=null && !searchName.equals("")){
            sql+=" AND a.name like'%"+searchName+"%'";
        }
        if(searchSfz!=null  && !searchSfz.equals("")){
            sql+=" AND (a.sfz like'%"+searchSfz+"%' or a.phone like '%"+searchSfz+"%')";
        }
        if(searchYuyuenum!=null  && !searchYuyuenum.equals("")){
            sql+=" AND (a.yuyuenum like'%"+searchYuyuenum+"%' or a.outtradeno like '%"+searchYuyuenum+"%')";
        }

        if(entity.getOuttradeno()!=null&&!"".equals(entity.getOuttradeno())){
            sql+=" AND a.outtradeno like '%"+entity.getOuttradeno()+"%' ";
        }
        if(entity.getOpenid()!=null&&!"".equals(entity.getOpenid())){
            sql+=" AND a.openid = '"+entity.getOpenid()+"' ";
        }
        if(entity.getIsfapiao()!=null&&!"".equals(entity.getIsfapiao())){
            sql+=" AND if(b.yuyueid is null,a.isfapiao,b.isfapiao) like '%"+entity.getIsfapiao()+"%' ";
        }
        if(entity.getCaiyangdidian()!=null&&!"".equals(entity.getCaiyangdidian())){
            sql+=" AND a.caiyangdidian like '%"+entity.getCaiyangdidian()+"%' ";
        }
        if(entity.getCooperationstr()!=null&&!"".equals(entity.getCooperationstr())){
            sql+=" AND a.cooperationstr like '%"+entity.getCooperationstr()+"%' ";
        }
        if(samplebindstarttime!=null&&!"".equals(samplebindstarttime)&&samplebindstarttime.length()>=10){
            sql+=" AND a.samplebindtime > '"+samplebindstarttime.substring(0, 10)+" 00:00:00"+"' ";
        }
        if(samplebindendtime!=null&&!"".equals(samplebindendtime)&&samplebindendtime.length()>=10){
            sql+=" AND a.samplebindtime < '"+samplebindendtime.substring(0, 10)+" 24:00:00"+"' ";
        }
        if(entity.getSampleNo()!=null&&!"".equals(entity.getSampleNo())){
            sql+=" AND a.sampleNo like '%"+entity.getSampleNo()+"%' ";
        }
        if(entity.getDuihuanma()!=null&&!"".equals(entity.getDuihuanma())){
            sql+=" AND a.duihuanma like '%"+entity.getDuihuanma()+"%' ";
        }
        if(entity.getShicaiyangdian()!=null&&!"".equals(entity.getShicaiyangdian())){
            sql+=" AND a.shicaiyangdian like '%"+entity.getShicaiyangdian()+"%' ";
        }
        if(entity.getTuikuan()!=null&&!"".equals(entity.getTuikuan())){
            sql+=" AND f.tuikuan like '%"+entity.getTuikuan()+"%' ";
        }

        if(entity.getSubjecttype()!=null&&!"".equals(entity.getSubjecttype())){
            sql+=" AND a.subjecttype like '%"+entity.getSubjecttype()+"%' ";
        }
        //默认为已付款，如果entity.getIssuccessed的值是全部，那么不筛选
        if("全部".equals(entity.getIssuccessed())){

        }else{
            sql+=" AND a.issuccessed = '是' ";
        }
        if(entity.getShuihao()!=null&&!"".equals(entity.getShuihao())){
            sql+=" AND a.shuihao LIKE '%"+entity.getShuihao()+"%' ";
        }
        if(entity.getQudao()!=null&&!"".equals(entity.getQudao())){
            sql+=" and a.qudao LIKE '%"+entity.getQudao()+"%' ";
        }

        if(entity.getHuikuanstate()!=null&&!"".equals(entity.getHuikuanstate())){
            sql+=" AND if(h.yuyueid is null,'否',h.huikuanstate) like '%"+entity.getHuikuanstate()+"%' ";
        }
        if(entity.getKaipiaostate()!=null&&!"".equals(entity.getKaipiaostate())){
            sql+=" AND if(fp.yuyueid is not null,'是',if(b.bukaistate = '已补开','是','否')) like '%"+entity.getKaipiaostate()+"%' ";
        }
        if(entity.getKaipiaono()!=null&&!"".equals(entity.getKaipiaono())){
            sql+=" AND if(fp.yuyueid is null,if(b.yuyueid is null,null,b.kaipiaono),fp.kaipiaono) like '%"+entity.getKaipiaono()+"%' ";
        }

        if(tuikuanstate!=null && !tuikuanstate.equals("")){
            sql += " AND f.tuikuan like '%"+tuikuanstate+"%'";
        }
        if(ininputTime!=null &&!"".equals(ininputTime)){
            sql += " AND STR_TO_DATE(f.shenqingdate,'%Y-%m-%d') >= "+"'"+ininputTime+"'";
        }

        if(osoinputTime!=null &&!"".equals(osoinputTime)){
            sql += " AND STR_TO_DATE(f.shenqingdate,'%Y-%m-%d') <= "+"'"+osoinputTime+"'";
        }
        return sql;
    }

    /**
     *    财务兑换码
     * **/
    public void tuikuanExcel(List<Appointmentinfoxgwx> ln, String path) {
        //工作簿
        HSSFWorkbook hssfworkbook = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet hssfsheet = hssfworkbook.createSheet();
        //sheet名称乱码处理
        hssfworkbook.setSheetName(0,"新冠退款信息");
        //取得第 i 行
        HSSFRow hssfrow1 = hssfsheet.createRow(0);

        HSSFCell hssfcell_00 = hssfrow1.createCell(0);
        hssfcell_00.setCellValue("预约编号");
        hssfsheet.setColumnWidth(0, 12*256);
        HSSFCell hssfcell_001 = hssfrow1.createCell(1);
        hssfcell_001.setCellValue("姓名");
        hssfsheet.setColumnWidth(1, 16*256);
        HSSFCell hssfcell_002 = hssfrow1.createCell(2);
        hssfcell_002.setCellValue("商户单号");
        hssfsheet.setColumnWidth(2, 20*256);
        HSSFCell hssfcell_003 = hssfrow1.createCell(3);
        hssfcell_003.setCellValue("身份证号");
        hssfsheet.setColumnWidth(3, 20*256);
        HSSFCell hssfcell_004 = hssfrow1.createCell(4);
        hssfcell_004.setCellValue("预约号");
        hssfsheet.setColumnWidth(4, 12*256);
        HSSFCell hssfcell_005 = hssfrow1.createCell(5);
        hssfcell_005.setCellValue("联系电话");
        hssfsheet.setColumnWidth(5, 12*256);
        HSSFCell hssfcell_006 = hssfrow1.createCell(6);
        hssfcell_006.setCellValue("是否付款到账");
        hssfsheet.setColumnWidth(6, 22*256);
        HSSFCell hssfcell_007 = hssfrow1.createCell(7);
        hssfcell_007.setCellValue("销售渠道");
        hssfsheet.setColumnWidth(7, 18*256);
        HSSFCell hssfcell_008 = hssfrow1.createCell(8);
        hssfcell_008.setCellValue("付款总金额");
        hssfsheet.setColumnWidth(8, 20*256);
        HSSFCell hssfcell_009 = hssfrow1.createCell(9);
        hssfcell_009.setCellValue("申请退款金额");
        hssfsheet.setColumnWidth(9, 14*256);
        HSSFCell hssfcell_0010 = hssfrow1.createCell(10);
        hssfcell_0010.setCellValue("实际退款金额");
        hssfsheet.setColumnWidth(10, 14*256);
        HSSFCell hssfcell_0011 = hssfrow1.createCell(11);
        hssfcell_0011.setCellValue("申请退款原因");
        hssfsheet.setColumnWidth(11, 14*256);
        HSSFCell hssfcell_0012 = hssfrow1.createCell(12);
        hssfcell_0012.setCellValue("退款状态");
        hssfsheet.setColumnWidth(12, 14*256);
        HSSFCell hssfcell_0013 = hssfrow1.createCell(13);
        hssfcell_0013.setCellValue("申请退款时间");
        hssfsheet.setColumnWidth(13, 14*256);
        HSSFCell hssfcell_0014 = hssfrow1.createCell(14);
        hssfcell_0014.setCellValue("申请退款人");
        hssfsheet.setColumnWidth(14, 14*256);
        HSSFCell hssfcell_0015 = hssfrow1.createCell(15);
        hssfcell_0015.setCellValue("确定退款时间");
        hssfsheet.setColumnWidth(15, 14*256);
        HSSFCell hssfcell_0016 = hssfrow1.createCell(16);
        hssfcell_0016.setCellValue("确定退款人");
        hssfsheet.setColumnWidth(16, 14*256);

        for (int i = 0; i < ln.size(); i++) {
            //取得第 i 行
            HSSFRow hssfrow = hssfsheet.createRow(i+1);
            HSSFCell hssfcell_0 = hssfrow.createCell(0);
            hssfcell_0.setCellValue(ln.get(i).getId());
            HSSFCell hssfcell_1 = hssfrow.createCell(1);
            hssfcell_1.setCellValue(ln.get(i).getName());
            HSSFCell hssfcell_2 = hssfrow.createCell(2);
            hssfcell_2.setCellValue(ln.get(i).getOuttradeno());
            HSSFCell hssfcell_3 = hssfrow.createCell(3);
            hssfcell_3.setCellValue(ln.get(i).getSfz());
            HSSFCell hssfcell_4 = hssfrow.createCell(4);
            hssfcell_4.setCellValue(ln.get(i).getYuyuenum());
            HSSFCell hssfcell_5 = hssfrow.createCell(5);
            hssfcell_5.setCellValue(ln.get(i).getPhone());
            HSSFCell hssfcell_6 = hssfrow.createCell(6);
            hssfcell_6.setCellValue(ln.get(i).getIssuccessed());
            HSSFCell hssfcell_7 = hssfrow.createCell(7);
            hssfcell_7.setCellValue(ln.get(i).getQudao());
            HSSFCell hssfcell_8 = hssfrow.createCell(8);
            hssfcell_8.setCellValue(ln.get(i).getTotalfee());
            HSSFCell hssfcell_9 = hssfrow.createCell(9);
            hssfcell_9.setCellValue(ln.get(i).getTuikuanjine());
            HSSFCell hssfcell_10 = hssfrow.createCell(10);
            hssfcell_10.setCellValue(ln.get(i).getShijijine());
            HSSFCell hssfcell_11 = hssfrow.createCell(11);
            hssfcell_11.setCellValue(ln.get(i).getTuikuanyuanyin());
            HSSFCell hssfcell_12 = hssfrow.createCell(12);
            hssfcell_12.setCellValue(ln.get(i).getTuikuan());
            HSSFCell hssfcell_13 = hssfrow.createCell(13);
            hssfcell_13.setCellValue(ln.get(i).getShenqingdate());
            HSSFCell hssfcell_14 = hssfrow.createCell(14);
            hssfcell_14.setCellValue(ln.get(i).getShenqingname());
            HSSFCell hssfcell_15 = hssfrow.createCell(15);
            hssfcell_15.setCellValue(ln.get(i).getTuikuandate());
            HSSFCell hssfcell_16 = hssfrow.createCell(16);
            hssfcell_16.setCellValue(ln.get(i).getTuikuanname());
        }
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
    }
    
    public String getUpdateSql(){
    	log.info("entity.getName():"+entity.getName());
    	String sql = "update xg_appointmentinfo set outtradeno='"+entity.getOuttradeno()+"',name='"+entity.getName()+"',caiyangdidian='"+entity.getCaiyangdidian()+"'" +
    			",yuyuedate='"+entity.getYuyuedate()+"',yuyuetime='"+entity.getYuyuetime()+"',fapiaotaitou='"+entity.getFapiaotaitou()+"'" +
				",fapiaotype='"+entity.getFapiaotype()+"',yuyuenum='"+entity.getYuyuenum()+"',sfz='"+entity.getSfz()+"'" +
				",phone='"+entity.getPhone()+"',shicaiyangdian='"+entity.getShicaiyangdian()+"',samplebindtime='" +
				""+entity.getSamplebindtime()+"',sending='"+entity.getSending()+"',accessname='"+entity.getAccessname()+"'" +
				",accessphone='"+entity.getAccessphone()+"',sampleNo='"+entity.getSampleNo()+"',issuccessed='"+entity.getIssuccessed()+"'" +
				",qudao='"+entity.getQudao()+"',duihuanma='"+entity.getDuihuanma()+"',isfapiao='"+entity.getDuihuanma()+"'" +
				",shuihao='"+entity.getShuihao()+"',accessaddress='"+entity.getAccessaddress()+"',accessemail='"+entity.getAccessemail()+"'" +
				",updatename='"+Struts2Utils.getUser().getName()+"',updateTime='"+Struts2Utils.getStringDate(new Date())+"'" +
				" where id='"+entity.getId()+"'";
    	return sql;
    }
    //新冠预约补录更新数据
    public String update(){
    	String sql = this.getUpdateSql();
    	try{
    		appointmentinfoxgwxService.update(sql);
    		msg.setMsg("修改成功");
    		Struts2Utils.renderJson(msg);
    	}catch(Exception e){
    		msg.setSuccess(false);
    		msg.setMsg("修改失败，请联系管理员");
    		Struts2Utils.renderJson(msg);
    	}
    	
    	return null;
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
        entity = new Appointmentinfoxgwx();
    }

    @Override
    public Appointmentinfoxgwx getModel() {
        return entity;
    }
}
