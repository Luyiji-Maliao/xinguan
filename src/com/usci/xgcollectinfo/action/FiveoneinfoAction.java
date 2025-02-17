package com.usci.xgcollectinfo.action;

import com.lims.core.orm.Page;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.entity.Norovirus;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.norovirus.service.NorovirusService;
import com.usci.xgcollectinfo.entity.Fiveoneinfo;
import com.usci.xgcollectinfo.entity.xgcollectinfo;
import com.usci.xgcollectinfo.service.FiveoneinfoService;
import com.usci.xgcollectinfo.service.Xgcollectinfo2Service;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
        @Result(name = "modulepage", location = "/WEB-INF/content/xgcollectinfo/fiveoneinfo.jsp")
})
public class FiveoneinfoAction extends CrudActionSupport<Fiveoneinfo> {
    private Fiveoneinfo entity;
    @Autowired
    private FiveoneinfoService fiveoneinfoService;
    @Autowired
    private Xgcollectinfo2Service xgcollectinfo2Service;
    @Autowired
    private NorovirusService norovirusService;
    @Autowired
    private AppointmentinfoxgwxService appointmentinfoxgwxService;
    /**
     * 入库
     */
    public void ruku(){
        String ms = "入库成功";
        Fiveoneinfo info = fiveoneinfoService.findUniqueBy(entity.getFiveonenum());

        if(null == info){  //合管入库表是否存在该合管编号
            List<xgcollectinfo> xlist = xgcollectinfo2Service.getByfiveone(entity.getFiveonenum());
            if(xlist.size()!=0){ //合管里面是否有样本
                Fiveoneinfo fiveoneinfo = new Fiveoneinfo();
                fiveoneinfo.setFiveonenum(entity.getFiveonenum());
                fiveoneinfo.setIsruku("是");
                fiveoneinfoService.add(fiveoneinfo);
                Fiveoneinfo f =  fiveoneinfoService.findUniqueBy(entity.getFiveonenum());
                List<xgcollectinfo> xglist = xgcollectinfo2Service.getByfiveone(entity.getFiveonenum());
                if(null != xglist){
                    //推送到新冠病毒报告自动化
                    for(xgcollectinfo x : xglist){
                        Norovirus norovirus =norovirusService.findUniqueBy(x.getSamplenum());
                        if(null == norovirus){
                            Norovirus n = new Norovirus();
                            n.setIdcard(x.getCertNumber()); //身份证号
                            n.setName(x.getPartyName()); //姓名
                            n.setReceptionDate(f.getRukutime());//接收日期 == 五合一管入库日期!!!!
                            n.setSamplingDate(x.getInputtime()); //采样时间 == 样本录入日期
                            n.setCheckProject("新型冠状病毒 2046-nCoV 核酸检测");//检测项目
                            n.setSampleType("咽拭子"); //样本类型
                            n.setSampleNo(x.getSamplenum());
                            n.setQudao(x.getInputAddress());
                            norovirusService.save(n);
                        }
                        xgcollectinfo xgcollectinfo = xgcollectinfo2Service.findbySamplenum(x.getSamplenum());
                        xgcollectinfo.setIsruku("是");
                        xgcollectinfo.setUpdatename(Struts2Utils.getSessionUser().getName());
                        xgcollectinfo.setUpdatetime(Struts2Utils.getStringDate(new Date()));
                        xgcollectinfo2Service.update(xgcollectinfo);
                    }
                }

            }else{
                ms = "该编号不存在";
            }

        }else{
            ms = "该编号已入库";
        }
        msg.setMsg(ms);
        Struts2Utils.renderJson(msg);
    }



    public String list2(){
        String sql = this.getsql();
        Page<Fiveoneinfo> page = new Page<Fiveoneinfo>(limit);
        page.setPageNo((start / limit) + 1);
        fiveoneinfoService.findpage(page,sql,start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }

    public String getsql(){
        String sql = "select * from fiveoneinfo where 1=1 ";
        sql+=" ORDER BY inputtime desc";
        return sql;
    }



    @Override
    public String save() throws Exception {

        return null;
    }

    @Override
    public String list() throws Exception {
        return "modulepage";
    }

    @Override
    public String modulepage() throws Exception {
        return "modulepage";
    }



    @Override
    protected void prepareModel() throws Exception {
        entity = new Fiveoneinfo();
    }

    @Override
    public Fiveoneinfo getModel() {
        return entity;
    }
}
