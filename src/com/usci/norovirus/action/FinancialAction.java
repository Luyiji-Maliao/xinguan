package com.usci.norovirus.action;

import com.lims.core.orm.Page;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.dao.FinancialDao;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Financial;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.norovirus.service.FinancialService;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })

@Results({
        @Result(name = "logins", type = "redirect", location = "session.jsp"),
        @Result(name = "modulepage", location = "/WEB-INF/content/norovirus/financial.jsp"),
})
public class FinancialAction extends CrudActionSupport<Financial> {

    private Financial entity;

    private String itemsxjyb;

    private String tuikuaninfo;
    private String tuikuanjineinfo;


    @Autowired
    private FinancialService financialService;

    @Autowired
    private AppointmentinfoxgwxService appointmentinfoxgwxService;

    public String getItemsxjyb() {
        return itemsxjyb;
    }

    public void setItemsxjyb(String itemsxjyb) {
        this.itemsxjyb = itemsxjyb;
    }

    public String getTuikuaninfo() {
        return tuikuaninfo;
    }

    public void setTuikuaninfo(String tuikuaninfo) {
        this.tuikuaninfo = tuikuaninfo;
    }

    public String getTuikuanjineinfo() {
        return tuikuanjineinfo;
    }

    public void setTuikuanjineinfo(String tuikuanjineinfo) {
        this.tuikuanjineinfo = tuikuanjineinfo;
    }

    @Override
    public String list() throws Exception {
        return null;
    }


    public void shenqingtuikuan(){
        List<Appointmentinfoxgwx> ln=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        for (Appointmentinfoxgwx a:ln) {
            Appointmentinfoxgwx appointmentinfoxgwx = appointmentinfoxgwxService.findUniqueBy(a.getId());
            Financial financial = new Financial();
            if(appointmentinfoxgwx!=null && appointmentinfoxgwx.getYuyuenum()!=null && !appointmentinfoxgwx.getYuyuenum().equals("")){
                financial.setXgappid(a.getId());
                financial.setOuttradeno(a.getOuttradeno());
                financial.setTuikuan("申请退款");
                financial.setTuikuanyuanyin(tuikuaninfo);
                financial.setTuikuanjine(tuikuanjineinfo);
                financialService.save(financial);
            }else{
                financial.setXgappid(a.getId());
                financial.setOuttradeno(a.getOuttradeno());
                financial.setTuikuan("已退款");
                financial.setTuikuanyuanyin(tuikuaninfo);
                financial.setTuikuanjine(tuikuanjineinfo);
                financialService.save(financial);
                appointmentinfoxgwxService.update(appointmentinfoxgwx,"已退款");
            }
        }
        Struts2Utils.renderJson(msg);
    }

    public void querentuikuan(){
        List<Appointmentinfoxgwx> ln=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        for (Appointmentinfoxgwx a:ln) {
            List<Financial> financial = financialService.findByOuttradeno(a.getOuttradeno());
            for (int i =0;i<financial.size();i++){
                Financial onefinancial = financial.get(i);
                if(onefinancial.getTuikuan().equals("已退款")){
                    continue;
                }
                onefinancial.setTuikuan("已退款");
                onefinancial.setShijijine(a.getShijijine());
                onefinancial.setTuikuandate(a.getTuikuandate());
                financialService.update(onefinancial);
            }
        }
        msg.setMsg("确认退款成功");
        Struts2Utils.renderJson(msg);
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
        entity = new Financial();
    }

    @Override
    public Financial getModel() {
        return entity;
    }
}
