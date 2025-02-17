package com.usci.norovirus.action;

import com.lims.core.orm.Page;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Cooperationinformation;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.norovirus.service.CooperationinformationService;
import com.usci.norovirus.service.NorovirusreportstateService;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Scope("prototype")

@Results({
        @Result(name = "logins", type = "redirect", location = "session.jsp"),
        @Result(name = "modulepage", location = "/WEB-INF/content/norovirus/signlist.jsp"),
        @Result(name = "signed", location = "/WEB-INF/content/norovirus/signedlist.jsp"),
        @Result(name = "saleperformance", location = "/WEB-INF/content/norovirus/saleperformance.jsp"),
        @Result(name = "customer", location = "/WEB-INF/content/norovirus/customerinfo.jsp"),
        @Result(name = "tongji", location = "/WEB-INF/content/norovirus/tongji.jsp"),
        @Result(name = "dangtian", location = "/WEB-INF/content/norovirus/dangtian.jsp")
})
public class SignAction extends CrudActionSupport<Appointmentinfoxgwx> {

    private Appointmentinfoxgwx entity;
    private String name;
    private String samplebindtime;
    private String leadername;

    @Autowired
    private AppointmentinfoxgwxService appointmentinfoService;
    @Autowired
    private CooperationinformationService cooperationinformationService;

    @Autowired
    private NorovirusreportstateService norovirusreportstateService;
    public String getLeadername() {
        return leadername;
    }

    public void setLeadername(String leadername) {
        this.leadername = leadername;
    }

    public String getSamplebindtime() {
        return samplebindtime;
    }

    public void setSamplebindtime(String samplebindtime) {
        this.samplebindtime = samplebindtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 待签到列表
     * @return
     * @throws Exception
     */
    @Override
    public String list() throws Exception {
        String sql= this.getSql();
        Page<Appointmentinfoxgwx> page = new Page<Appointmentinfoxgwx>(limit);
        page.setPageNo((start / limit) + 1);
        appointmentinfoService.findPage(page,sql,"",start,limit);
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("rows",page.getResult());
        hashMap.put("total",page.getTotalCount());
        hashMap.put("pageno",page.getPageNo());
        hashMap.put("pagesize",page.getTotalPages());
        hashMap.put("start",page.getPageSize());
        hashMap.put("dilist",this.getcaiyangdian(sql));
        Struts2Utils.renderJson(hashMap);
        return null;
    }

    public String getSql(){
        String sql="SELECT id,`name`,sex,age,phone,yuyuedate,yuyuetime,caiyangdidian,caiyangdidian as caiyangdian,yuyuenum " +
                "  FROM xg_appointmentinfo " +
                "  where  (sampleNo is null  or sampleNo='') and issuccessed='是' ";
        if(entity.getSamplebindtime()!=null && !"".equals(entity.getSamplebindtime())){
            sql+=" and to_days(yuyuedate) = to_days('"+entity.getSamplebindtime()+"') ";
        }else{
            sql+=" and to_days(yuyuedate) = to_days(now()) ";
        }
        if(entity!=null && entity.getCaiyangdidian()!=null && !entity.getCaiyangdidian().equals("") ){
            sql+=" and caiyangdidian='"+entity.getCaiyangdidian()+"'";
        }
        if(entity!=null && entity.getName()!=null && !entity.getName().equals("") ){
            sql+=" and (name like '%"+entity.getName()+"%' or phone like '%"+entity.getName()+"%')";
        }
        sql+="  group by id  order by yuyuetime ";
        return sql;
    }


    /**
     * 已签到列表
     * @return
     * @throws Exception
     */
    public String signedlist() throws Exception {
        String sql= this.getsignedSql();
        Page<Appointmentinfoxgwx> page = new Page<Appointmentinfoxgwx>(limit);
        page.setPageNo((start / limit) + 1);
        appointmentinfoService.findPage(page,sql,"",start,limit);
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("rows",page.getResult());
        hashMap.put("total",page.getTotalCount());
        hashMap.put("pageno",page.getPageNo());
        hashMap.put("pagesize",page.getTotalPages());
        hashMap.put("start",page.getPageSize());
        hashMap.put("dilist",this.getcaiyangdian(sql));
        Struts2Utils.renderJson(hashMap);
        return null;
    }



    public String getsignedSql(){
        String sql="SELECT id,`name`,sex,age,phone,yuyuedate,yuyuetime,shicaiyangdian,shicaiyangdian as caiyangdian,yuyuenum,samplebindtime " +
                "  FROM xg_appointmentinfo " +
                "  where  (sampleNo is not null  and sampleNo!='') and issuccessed='是' ";
        if(entity.getSamplebindtime()!=null && !"".equals(entity.getSamplebindtime())){
            sql+=" and to_days(samplebindtime) = to_days('"+entity.getSamplebindtime()+"')  ";
        }
        if(entity!=null && entity.getShicaiyangdian()!=null && !entity.getShicaiyangdian().equals("") ){
            sql+=" and shicaiyangdian='"+entity.getShicaiyangdian()+"'";
        }
        if(entity!=null && entity.getName()!=null && !entity.getName().equals("") ){
            sql+=" and (name like '%"+entity.getName()+"%' or phone like '%"+entity.getName()+"%')";
        }
        sql+="  group by id  order by samplebindtime ";
        return sql;
    }

    /**
     * 销售与合作结构列表
     * @return
     * @throws Exception
     */
    public String salelist() throws Exception {
        String sql= this.getBysalenameSql();
        Page<Cooperationinformation> page = new Page<Cooperationinformation>(limit);
        page.setPageNo((start / limit) + 1);
        cooperationinformationService.findPage(page,sql,"",start,limit);

        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("rows",page.getResult());
        hashMap.put("total",page.getTotalCount());
        hashMap.put("pageno",page.getPageNo());
        hashMap.put("pagesize",page.getTotalPages());
        hashMap.put("start",page.getPageSize());
        Struts2Utils.renderJson(hashMap);
        return null;
    }

    /**
     * 销售与合作机构sql
     * @return
     */
    public String getBysalenameSql(){
        String sql="select coo.*,count(app.issuccessed) as renshu from xg_cooperationinformation as coo LEFT JOIN xg_appointmentinfo as app on app.cooperationstr=coo.cooperationstr " +
                " where issuccessed='是'  and (coo.permissionperson like '%"+Struts2Utils.getUser().getName()+";%' or salename='"+Struts2Utils.getUser().getName()+"') ";
//        if(entity.getSamplebindtime()!=null && !"".equals(entity.getSamplebindtime())){
//            sql+=" and to_days(samplebindtime) = to_days('"+entity.getSamplebindtime()+"')  ";
//        }
//        if(entity!=null && entity.getShicaiyangdian()!=null && !entity.getShicaiyangdian().equals("") ){
//            sql+=" and shicaiyangdian='"+entity.getShicaiyangdian()+"'";
//        }
        if(entity!=null && entity.getName()!=null && !entity.getName().equals("") ){
            sql+=" and (salename like '%"+entity.getName()+"%' or cooperationcompanyname like '%"+entity.getName()+"%' or cooperationsubcompanyname like '%\"+entity.getName()+\"%')";
        }
        sql+="  group by cooperationcompanyname,cooperationsubcompanyname,businessmanager  order by count(cooperationsubcompanyname) desc ";
        return sql;
    }


    /**
     * 根据分销码获取对应的客户信息
     * @return
     * @throws Exception
     */
    public String getbyma() throws Exception {
        String sql= this.getBymaSql();
        String maxmindate="select min(if(samplebindtime is null,yuyuedate,samplebindtime)) as min," +
                "max(if(samplebindtime is null,yuyuedate,samplebindtime)) as max from xg_appointmentinfo " +
                "where cooperationstr = '"+entity.getCooperationstr()+"' and  issuccessed='是' ";
        Page<Appointmentinfoxgwx> page = new Page<Appointmentinfoxgwx>(limit);
        page.setPageNo((start / limit) + 1);
        appointmentinfoService.findPage(page,sql,"",start,limit);
        List<Object[]> li=appointmentinfoService.queryBySql(maxmindate);
        String mindate="";
        String maxdate="";
        if(li.size()>0){
            mindate=li.get(0)[0].toString();
            maxdate=li.get(0)[1].toString();
        }
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("rows",page.getResult());
        hashMap.put("total",page.getTotalCount());
        hashMap.put("pageno",page.getPageNo());
        hashMap.put("pagesize",page.getTotalPages());
        hashMap.put("start",page.getPageSize());
        hashMap.put("dilist",this.getcaiyangdian(sql));
        hashMap.put("mindate",mindate);
        hashMap.put("maxdate",maxdate);
        Struts2Utils.renderJson(hashMap);
        return null;
    }

    public String getBymaSql(){
        String sql="select id,openid,name,sex,age,sending,sfz,phone,isgeli,comebjtime,comebjreason,yuyuedate,yuyuetime,caiyangdidian," +
                "isfapiao,shuihao,fapiaotaitou,zhucedizhi,zhucedianhua,kaihuyinhang,yinhangzhanghao,fapiaotype,accessname," +
                "accessphone,accessaddress,accessemail,isagree,outtradeno,totalfee,inspection,issuccessed,yuyuenum,xgreason,subjecttype," +
                "cooperationstr,sampleNo,qudao,shicaiyangdian,if(shicaiyangdian is null,caiyangdidian,shicaiyangdian) as caiyangdian,inputTime,inputName,updateTime,updateName,samplebindtime,customerbindtime from xg_appointmentinfo where cooperationstr = '"+entity.getCooperationstr()+"' and  issuccessed='是'  ";
        if(entity.getSamplebindtime()!=null && !"".equals(entity.getSamplebindtime())){
            sql+=" and to_days(samplebindtime) = to_days('"+entity.getSamplebindtime()+"')  ";
        }
        if(entity!=null && entity.getShicaiyangdian()!=null && !entity.getShicaiyangdian().equals("") && !entity.getShicaiyangdian().equals("全部采样点")){
            sql+=" and (shicaiyangdian='"+entity.getShicaiyangdian()+"' or caiyangdidian='"+entity.getShicaiyangdian()+"')";
        }
        if(entity!=null && entity.getName()!=null && !entity.getName().equals("") ){
            sql+=" and (name like '%"+entity.getName()+"%' or phone like '%"+entity.getName()+"%')";
        }
        sql+=" order by samplebindtime desc";
        return sql;
    }

    /**
     * 获取统计信息每个采样点每天的人数
     * @return
     * @throws Exception
     */
    public String gettongji() throws Exception {
        String sql= this.gettongjiSql();
        List<Appointmentinfoxgwx> applist=appointmentinfoService.entitybysql(sql);
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("data",applist);
        hashMap.put("dilist",this.getcaiyangdian(sql));
        hashMap.put("datelist",this.getalldate(sql));
        Struts2Utils.renderJson(hashMap);
        return null;
    }



    /**
     * 获取统计信息每个采样点每天的人数
     * @return
     */
    public String gettongjiSql(){
        String sql="select caiyangdidian as caiyangdian,yuyuedate,count(caiyangdidian) as renshu " +
                " from xg_appointmentinfo where TO_DAYS(now()) - TO_DAYS(yuyuedate) >= 1  and issuccessed='是' and " +
                " caiyangdidian not in(select caiyangdidian from xg_tuanti) ";

        sql+=" group by caiyangdidian,yuyuedate  order by caiyangdidian,yuyuedate ";
        return sql;
    }


    /**
     * 获取每天的人数，所有采样点都算进去
     * @return
     * @throws Exception
     */
    public String tongjiall() throws Exception {
        String sql= this.tongjiallSql();
        List<Appointmentinfoxgwx> applist=appointmentinfoService.entitybysql(sql);
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("data",applist);
        hashMap.put("dilist",this.getcaiyangdian(sql));
        hashMap.put("datelist",this.getalldate(sql));
        Struts2Utils.renderJson(hashMap);
        return null;
    }
    /**
     * 查询每天总人数
     * @return
     */
    public String tongjiallSql(){
        String sql="select caiyangdidian as caiyangdian,yuyuedate,count(caiyangdidian) as renshu " +
                " from xg_appointmentinfo where TO_DAYS(now()) - TO_DAYS(yuyuedate) >= 1  and issuccessed='是' and outtradeno!='河北项目'  ";

        sql+=" group by yuyuedate  order by yuyuedate";
        return sql;
    }
    /**
     * 根据渠道分组 获取每个渠道每天的总预约人数
     * @return
     * @throws Exception
     */
    public String yuyuenumbyqudao() throws Exception {
        String sql= this.yuyuenumbyqudaoSql();
        List<Appointmentinfoxgwx> applist=appointmentinfoService.entitybysql(sql);
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("data",applist);
        Struts2Utils.renderJson(hashMap);
        return null;
    }
    /**
     * 根据渠道分组 获取每个渠道每天的总预约人数
     * @return
     */
    public String yuyuenumbyqudaoSql(){
        String sql="SELECT yuyuedate,count(*) as heji,sum( case when qudao like '%线上预约%' or qudao like '%兑换码%'then  1 else 0 end ) as xianshang," +
                " sum(  case when qudao like '%团体%' then  1 else 0 end  ) as tuanti, sum(  case when qudao like '%京东%' then  1 else 0 end  ) as jingdong, " +
                " sum(  case when qudao not in ('京东','线上预约','团体') and qudao not like '%兑换码%' then  1 else 0 end  ) as huada " +
                " FROM xg_appointmentinfo   WHERE issuccessed = '是' " ;
                sql=yuyuedate(sql);
         sql+=" GROUP BY yuyuedate  order by yuyuedate ";
        return sql;
    }
    /**
     * 根据渠道分组 获取每个渠道每天的实际采样人数
     * @return
     * @throws Exception
     */
    public String shijinumby() throws Exception {
        String sql= this.shijinumbyqudaoSql();
        List<Appointmentinfoxgwx> applist=appointmentinfoService.entitybysql(sql);
        Struts2Utils.renderJson(applist);
        return null;
    }
    /**
     * 根据渠道分组 获取每个渠道每天的实际采样人数
     * @return
     */
    public String shijinumbyqudaoSql(){
        String sql="SELECT samplebindtime,count(*) as heji,sum( case when qudao like '%线上预约%' or qudao like '%兑换码%'then  1 else 0 end ) as xianshang," +
                " sum(  case when qudao like '%团体%' then  1 else 0 end  ) as tuanti, sum(  case when qudao like '%京东%' then  1 else 0 end  ) as jingdong, " +
                " sum(  case when qudao not in ('京东','线上预约','团体') and qudao not like '%兑换码%' then  1 else 0 end  ) as huada " +
                " FROM xg_appointmentinfo   WHERE issuccessed = '是' " ;
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and date(samplebindtime) >= date('"+entity.getStartdate()+"')";
        }
        if(entity.getEnddate()!=null && !entity.getEnddate().equals("")){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and date(samplebindtime) <= date('"+entity.getEnddate()+"')";
        }
        sql+=" GROUP BY date(samplebindtime)  order by samplebindtime ";
        return sql;
    }
    /**
     * 获取采样点到昨天为止所有人数
     * @return
     */
    public String caiyangall() throws Exception {
        String sql= this.caiyangallSql();
        List<Appointmentinfoxgwx> applist=appointmentinfoService.entitybysql(sql);
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("data",applist);
        hashMap.put("dilist",this.getcaiyangdian(sql));
        hashMap.put("datelist",this.getalldate(sql));
        Struts2Utils.renderJson(hashMap);
        return null;
    }
    /**
     * 查询各个采样点到昨天总人数
     * @return
     */
    public String caiyangallSql(){
        String sql="select caiyangdidian as caiyangdian,yuyuedate,count(caiyangdidian) as renshu,qudao " +
                " from xg_appointmentinfo where TO_DAYS(now()) - TO_DAYS(yuyuedate) >= 1  and issuccessed='是'  ";

        sql+=" group by caiyangdidian  order by count(caiyangdidian) desc ";
        return sql;
    }


    /**
     * 获取现场预约、线上预约、合计、实际检测人数
     * @return
     * @throws Exception
     */
    public String getcaiyangren() throws Exception {
        String xianchangsql=this.xianchangsql();
        List<Appointmentinfoxgwx> xianchanglist=appointmentinfoService.entitybysql(xianchangsql);
        String xianshangsql=this.xianshangsql();
        List<Appointmentinfoxgwx> xianshanglist=appointmentinfoService.entitybysql(xianshangsql);
        String hejisql=this.hejisql();
        List<Appointmentinfoxgwx> hejilist=appointmentinfoService.entitybysql(hejisql);
        String shijisql=this.shijisql();
        List<Appointmentinfoxgwx> shijilist=appointmentinfoService.entitybysql(shijisql);
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("xianchanglist",xianchanglist);
        hashMap.put("xianshanglist",xianshanglist);
        hashMap.put("hejilist",hejilist);
        hashMap.put("shijilist",shijilist);
        Struts2Utils.renderJson(hashMap);
        return null;
    }

    public String yichubaogaocyd() throws Exception {
        String shijisql=this.yichusql();
        List<Appointmentinfoxgwx> shijilist=appointmentinfoService.entitybysql(shijisql);
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("shijilist",shijilist);
        Struts2Utils.renderJson(hashMap);
        return null;
    }

    /**
     * 查询现场预约的用户 预约时间和绑定时间是当天的，下单时间大于早上八点的
     * @return
     */
    public String xianchangsql(){
        String sql= "select caiyangdidian as caiyangdian,yuyuedate,yuyuetime,samplebindtime,inputTime,count(caiyangdidian) as renshu,qudao from xg_appointmentinfo " +
                " where issuccessed='是'  and date(yuyuedate) = date(IF(inputTime is null,updatetime,inputTime))  and IF(inputTime is null,updatetime,inputTime) >  concat(yuyuedate,' 08:00') and outtradeno!='河北项目' ";
        sql+=istiaojian();
        sql+=" group by caiyangdidian order by caiyangdidian";
        return sql;
    }

    /**
     * 查询线上预约的用户
     * @return
     */
    public String xianshangsql(){
        String sql= "select caiyangdidian as caiyangdian,yuyuedate,yuyuetime,inputTime,count(caiyangdidian) as renshu,qudao from xg_appointmentinfo " +
                " where  IF(inputTime is null,updatetime,inputTime) < concat(yuyuedate,' 08:00') and issuccessed='是' and outtradeno!='河北项目'  ";
        sql+=istiaojian();
        sql+=" group by caiyangdidian order by caiyangdidian";
        return sql;
    }

    /**
     * 查询采样点合计预约人数
     * @return
     */
    public String hejisql(){
        String sql= "select caiyangdidian as caiyangdian,yuyuedate,yuyuetime,inputTime,count(caiyangdidian) as renshu,qudao from xg_appointmentinfo where issuccessed='是'  and  outtradeno!='河北项目'  and caiyangdidian is not null and caiyangdidian !=''  ";
        sql+=istiaojian();
        sql+=" group by caiyangdidian order by caiyangdidian";
        return sql;
    }
    /**
     * 查询实际采样的用户数
     * @return
     */
    public String shijisql(){
        String sql= "select shicaiyangdian as caiyangdian,yuyuedate,yuyuetime,inputTime,count(shicaiyangdian) as renshu,qudao from xg_appointmentinfo where issuccessed='是' and shicaiyangdian is not null and shicaiyangdian !=''  and  outtradeno!='河北项目'  ";
        if(entity.getStartdate()!=null && !"".equals(entity.getStartdate())){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and date(samplebindtime) >= date('"+entity.getStartdate()+"')";
        }
        if(entity.getEnddate()!=null && !"".equals(entity.getEnddate())){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and date(samplebindtime)  <= date('"+entity.getEnddate()+"')";
        }
        sql+=" group by shicaiyangdian order by count(shicaiyangdian) desc ";
        return sql;
    }

    public String istiaojian(){
        String tj="";
        if(entity.getStartdate()!=null && !"".equals(entity.getStartdate())){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            tj+=" and date(yuyuedate) >= date('"+entity.getStartdate()+"')";
        }
        if(entity.getEnddate()!=null && !"".equals(entity.getEnddate())){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            tj+=" and date(yuyuedate)  <= date('"+entity.getEnddate()+"')";
        }
        return tj;
    }
    /**
     * 查询实际采样的用户数
     * @return
     */
    public String yichusql(){
        String sql= "select shicaiyangdian as caiyangdian,yuyuedate,yuyuetime,inputTime,count(shicaiyangdian) as renshu,qudao from xg_appointmentinfo where issuccessed='是'  and  outtradeno!='河北项目'  ";
        if(entity.getStartdate()!=null && !"".equals(entity.getStartdate())){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and date(reportdate) >= date('"+entity.getStartdate()+"')";
        }
        if(entity.getEnddate()!=null && !"".equals(entity.getEnddate())){

            sql+=" and date(reportdate)  <= date('"+entity.getEnddate()+"')";
        }
        sql+=" group by shicaiyangdian order by count(shicaiyangdian) desc ";
        return sql;
    }
    /**
     * 获取数据库可用的采样点
     */
    public void getdidianAll(){
        String sql="select caiyangdiname as caiyangdidian,isused as name from xg_caiyangdidianinfo where isused='是'";
        List<Appointmentinfoxgwx> lcs=appointmentinfoService.entitybysql(sql);
        List<String> ls=new ArrayList<String>();
        for (Appointmentinfoxgwx o : lcs) {
            ls.add(o.getCaiyangdidian());
        }
        Struts2Utils.renderJson(ls);
    }

    /**
     * 根据sql语句获取其中包含的所有采样点
     * @param sql
     * @return
     */
    public List<String> getcaiyangdian(String sql){
        String sqll="select caiyangdian from ("+sql+") as a group by caiyangdian ";
        List<Appointmentinfoxgwx> lcs=appointmentinfoService.entitybysql(sqll);
        List<String> ls=new ArrayList<String>();
        for (Appointmentinfoxgwx o : lcs) {
            ls.add(o.getCaiyangdian());
        }
        return ls;
    }
    /**
     * 根据sql语句获取其中包含的所有日期
     * @param sql
     * @return
     */
    public List<String> getalldate(String sql){
        String sqll="select yuyuedate from ("+sql+") as a group by yuyuedate order by yuyuedate asc";
        List<Appointmentinfoxgwx> lcs=appointmentinfoService.entitybysql(sqll);
        List<String> ls=new ArrayList<String>();
        for (Appointmentinfoxgwx o : lcs) {
            ls.add(o.getYuyuedate());
        }
        return ls;
    }

    /**
     * 获取已出报告的数量
     * @return
     */
    public String getyichubaogao(){
        String sql="select id from norovirus where checkProject!='诺如病毒核酸检测' and pathName is not null and sampleNo not like 'CB%' and pathName!='' ";

        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and reportdate >= '"+entity.getStartdate()+"'";
        }
        if(entity.getEnddate()!=null && !entity.getEnddate().equals("")){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and reportdate <= '"+entity.getEnddate()+"'";
        }
        int count=norovirusreportstateService.count(sql);
        String c=count+"";
        Struts2Utils.renderJson(c);
        return null;
    }
    public void gediannum(){
        String xscyd=this.xscyd();
        List<Appointmentinfoxgwx> xscydlist=appointmentinfoService.entitybysql(xscyd);
        String xsscyd=this.xsscyd();
        List<Appointmentinfoxgwx> xsscydlist=appointmentinfoService.entitybysql(xsscyd);
        String ttcyd=this.ttcyd();
        List<Appointmentinfoxgwx> ttcydlist=appointmentinfoService.entitybysql(ttcyd);
        String ttscyd=this.ttscyd();
        List<Appointmentinfoxgwx> ttscydlist=appointmentinfoService.entitybysql(ttscyd);
        String jdcyd=this.jdcyd();
        List<Appointmentinfoxgwx> jdcydlist=appointmentinfoService.entitybysql(jdcyd);
        String jdscyd=this.jdscyd();
        List<Appointmentinfoxgwx> jdscydlist=appointmentinfoService.entitybysql(jdscyd);
        String hdcyd=this.hdcyd();
        List<Appointmentinfoxgwx> hdcydlist=appointmentinfoService.entitybysql(hdcyd);
        String hdscyd=this.hdscyd();
        List<Appointmentinfoxgwx> hdscydlist=appointmentinfoService.entitybysql(hdscyd);
        String allcyd=this.allcyd();
        List<Appointmentinfoxgwx> allcydlist=appointmentinfoService.entitybysql(allcyd);
        String allscyd=this.allscyd();
        List<Appointmentinfoxgwx> allscydlist=appointmentinfoService.entitybysql(allscyd);
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("xscydlist",xscydlist);
        hashMap.put("xsscydlist",xsscydlist);
        hashMap.put("ttcydlist",ttcydlist);
        hashMap.put("ttscydlist",ttscydlist);
        hashMap.put("jdcydlist",jdcydlist);
        hashMap.put("jdscydlist",jdscydlist);
        hashMap.put("hdcydlist",hdcydlist);
        hashMap.put("hdscydlist",hdscydlist);
        hashMap.put("allcydlist",allcydlist);
        hashMap.put("allscydlist",allscydlist);
        Struts2Utils.renderJson(hashMap);
    }

    public String yuyuedate(String sql){
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and yuyuedate >= '"+entity.getStartdate()+"'";
        }
        if(entity.getEnddate()!=null && !entity.getEnddate().equals("")){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and yuyuedate <= '"+entity.getEnddate()+"'";
        }
        return sql;
    }
    /**
     * 线上预约的现场人数 线上人数 合计人数
     * @return
     */
    public String xscyd(){
        String sql="SELECT id,caiyangdidian as caiyangdian," +
                " sum(" +
                " case when yuyuetime > '08:00' AND IF( inputTime IS NULL, updatetime, inputTime ) > concat( yuyuedate, ' 08:00' )" +
                " then  1 else 0 end" +
                " ) as xianchang," +
                " sum(" +
                " CASE WHEN IF( inputTime IS NULL, updatetime, inputTime ) < concat( yuyuedate, ' 08:00' ) THEN 1 ELSE 0 END ) as xianshang, " +
                " count(caiyangdidian) as heji " +
                " FROM xg_appointmentinfo " +
                " WHERE issuccessed = '是'   and (qudao like '%线上预约%' or qudao like '%兑换码%')" +
                " ";
        sql=yuyuedate(sql);
        sql+="  GROUP BY caiyangdidian  ORDER BY count(caiyangdidian) desc  ";
        return sql;
    }
    /**
     * 线上预约的实际采样人数
     * @return
     */
    public String xsscyd(){
        String sql="select shicaiyangdian as caiyangdian,count(shicaiyangdian) as shiji from xg_appointmentinfo where issuccessed='是'  " +
                " and (qudao like '%线上预约%' or qudao like '%兑换码%')  ";
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and date(samplebindtime) >= '"+entity.getStartdate()+"'";
        }
        if(entity.getEnddate()!=null && !entity.getEnddate().equals("")){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and date(samplebindtime) <= '"+entity.getEnddate()+"'";
        }
        sql+="  group by shicaiyangdian order by count(caiyangdidian) desc  ";
        return sql;
    }

    /**
     * 团体预约的现场人数 线上人数 合计人数
     * @return
     */
    public String ttcyd(){
        String sql="SELECT tt.tuanname,xa.caiyangdidian as caiyangdian," +
                " sum(" +
                " case when xa.yuyuetime > '08:00' AND IF( xa.inputTime IS NULL, xa.updatetime, xa.inputTime ) > concat( xa.yuyuedate, ' 08:00' ) " +
                " then  1 else 0 end" +
                " ) as xianchang," +
                " sum(" +
                " CASE WHEN IF( xa.inputTime IS NULL, xa.updatetime, xa.inputTime ) < concat( xa.yuyuedate, ' 08:00' ) THEN 1 ELSE 0 END ) as xianshang,\n" +
                " count(xa.caiyangdidian) as heji" +
                " FROM xg_appointmentinfo as xa" +
                " LEFT JOIN xg_tuanti tt on tt.tuanname=xa.outtradeno " +
                " WHERE issuccessed = '是'   and xa.qudao='团体' " +
                "  ";
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and xa.yuyuedate >= '"+entity.getStartdate()+"'";
        }
        if(entity.getEnddate()!=null && !entity.getEnddate().equals("")){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and xa.yuyuedate <= '"+entity.getEnddate()+"'";
        }
        sql+="  GROUP BY xa.caiyangdidian  ORDER BY count(xa.caiyangdidian) desc ";
        return sql;
    }
    /**
     * 团体预约的实际采样人数
     * @return
     */
    public String ttscyd(){
        String sql="select tt.tuanname,xa.shicaiyangdian as caiyangdian,count(shicaiyangdian) as shiji from xg_appointmentinfo as xa LEFT JOIN xg_tuanti tt on tt.tuanname=xa.outtradeno  " +
                "   where issuccessed='是'   and xa.qudao='团体' ";
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and date(samplebindtime) >= '"+entity.getStartdate()+"'";
        }
        if(entity.getEnddate()!=null && !entity.getEnddate().equals("")){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and date(samplebindtime) <= '"+entity.getEnddate()+"'";
        }
        sql+="  group by shicaiyangdian order by count(xa.caiyangdidian) desc ";
        return sql;
    }

    /**
     * 京东预约的现场人数 线上人数 合计人数
     * @return
     */
    public String jdcyd(){
        String date="DATE_SUB( now( ), INTERVAL 1 DAY )";
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            date=" date('"+entity.getStartdate()+"') ";
        }
        String sql="SELECT xa.caiyangdidian as caiyangdian," +
                " sum(" +
                " case when xa.yuyuetime > '08:00' AND IF( inputTime IS NULL, updatetime, inputTime ) > concat( yuyuedate, ' 08:00' ) " +
                " then  1 else 0 end" +
                " ) as xianchang," +
                " sum(" +
                " CASE WHEN IF( xa.inputTime IS NULL, xa.updatetime, xa.inputTime ) < concat(yuyuedate, ' 08:00' ) THEN 1 ELSE 0 END ) as xianshang," +
                " count(xa.caiyangdidian) as heji" +
                " FROM xg_appointmentinfo as xa" +
                " WHERE issuccessed = '是'   and xa.qudao='京东'" +
                "  ";
        sql=yuyuedate(sql);
        sql+="  GROUP BY xa.caiyangdidian  ORDER BY count(xa.caiyangdidian) desc";
        return sql;
    }
    /**
     * 京东预约的实际采样人数
     * @return
     */
    public String jdscyd(){
        String sql="select shicaiyangdian as caiyangdian,count(shicaiyangdian) as shiji from xg_appointmentinfo as xa   where issuccessed='是'  " +
                "  and xa.qudao='京东' ";
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and date(samplebindtime) >= '"+entity.getStartdate()+"'";
        }
        if(entity.getEnddate()!=null && !entity.getEnddate().equals("")){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and date(samplebindtime) <= '"+entity.getEnddate()+"'";
        }
        sql+="  group by shicaiyangdian order by count(caiyangdidian) desc ";
        return sql;
    }

    /**
     * 华大预约的现场人数 线上人数 合计人数
     * @return
     */
    public String hdcyd(){
        String sql="SELECT xa.caiyangdidian as caiyangdian,sum(" +
                " case when xa.yuyuetime > '08:00' AND IF( inputTime IS NULL, updatetime, inputTime ) > concat( yuyuedate, ' 08:00' ) " +
                " then  1 else 0 end" +
                " ) as xianchang," +
                " sum(" +
                " CASE WHEN IF( xa.inputTime IS NULL, xa.updatetime, xa.inputTime ) < concat( yuyuedate , ' 08:00' ) THEN 1 ELSE 0 END ) as xianshang," +
                " count(xa.caiyangdidian) as heji" +
                " FROM xg_appointmentinfo as xa" +
                " WHERE issuccessed = '是'   and xa.qudao not in ('京东','线上预约','团体') and xa.qudao not like '%兑换码%'" +
                " ";
        sql=yuyuedate(sql);
        sql+=" GROUP BY xa.caiyangdidian  ORDER BY count(xa.caiyangdidian) desc   ";
        return sql;
    }
    /**
     * 华大预约的实际采样人数
     * @return
     */
    public String hdscyd(){
        String sql="select shicaiyangdian as caiyangdian,count(shicaiyangdian) as shiji from xg_appointmentinfo as xa   where issuccessed='是' " +
                " and xa.qudao not in ('京东','线上预约','团体') and xa.qudao not like '%兑换码%' ";
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and date(samplebindtime) >= '"+entity.getStartdate()+"'";
        }
        if(entity.getEnddate()!=null && !entity.getEnddate().equals("")){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and date(samplebindtime) <= '"+entity.getEnddate()+"'";
        }
        sql+=" group by shicaiyangdian order by  count(caiyangdidian) desc  ";
        return sql;
    }


    /**
     * 全部预约的现场人数 线上人数 合计人数
     * @return
     */
    public String allcyd(){
        String date="DATE_SUB( now( ), INTERVAL 1 DAY )";
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            date=" date('"+entity.getStartdate()+"') ";
        }
        String sql="SELECT caiyangdidian as caiyangdian," +
                " sum(" +
                " case when yuyuetime > '08:00' AND IF( inputTime IS NULL, updatetime, inputTime ) > concat( yuyuedate, ' 08:00' ) " +
                " then  1 else 0 end" +
                " ) as xianchang," +
                " sum(" +
                " CASE WHEN IF( inputTime IS NULL, updatetime, inputTime ) < concat( yuyuedate, ' 08:00' ) THEN 1 ELSE 0 END ) as xianshang," +
                " count(caiyangdidian) as heji" +
                " FROM xg_appointmentinfo " +
                " WHERE issuccessed = '是'   " +
                "  ";
        sql=yuyuedate(sql);
        sql+="  GROUP BY caiyangdidian  ORDER BY count(caiyangdidian) desc ";
        return sql;
    }
    /**
     * 全部预约的实际采样人数
     * @return
     */
    public String allscyd(){
        String sql="select shicaiyangdian as caiyangdian,count(shicaiyangdian) as shiji from xg_appointmentinfo where issuccessed='是'  " +
                "  ";
        if(entity.getStartdate()!=null && !entity.getStartdate().equals("")){
            entity.setStartdate(entity.getStartdate().replace("T"," "));
            sql+=" and date(samplebindtime) >= '"+entity.getStartdate()+"'";
        }
        if(entity.getEnddate()!=null && !entity.getEnddate().equals("")){
            entity.setEnddate(entity.getEnddate().replace("T"," "));
            sql+=" and date(samplebindtime) <= '"+entity.getEnddate()+"'";
        }
        sql+="  group by shicaiyangdian order by count(caiyangdidian) desc ";
        return sql;
    }

    public String signed() throws Exception {
        return "signed";
    }

    public String saleperformance() throws Exception {
        return "saleperformance";
    }

    public String customer() throws Exception {
        return "customer";
    }

    public String tongji() throws Exception{
        return "tongji";
    }

    public String dangtian() throws Exception{
        return "dangtian";
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
        entity=new Appointmentinfoxgwx();
    }

    @Override
    public Appointmentinfoxgwx getModel() {
        return entity;
    }

}
