package com.usci.norovirus.action;

import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Fapiao;
import com.usci.norovirus.entity.Fapiao;
import com.usci.norovirus.entity.Huikuan;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.norovirus.service.FapiaoService;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })

@Results({
        @Result(name = "logins", type = "redirect", location = "session.jsp"),
       
})
public class FapiaoAction extends CrudActionSupport<Fapiao> {

    private Fapiao entity;

    private String itemsxjyb;





    @Autowired
    private FapiaoService fapiaoService;

    @Autowired
    private AppointmentinfoxgwxService appointmentinfoxgwxService;

    public String getItemsxjyb() {
        return itemsxjyb;
    }

    public void setItemsxjyb(String itemsxjyb) {
        this.itemsxjyb = itemsxjyb;
    }


    @Override
    public String list() throws Exception {
        return null;
    }

    public void onekaipiao(){
        List<Appointmentinfoxgwx> ln=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        StringBuffer sb = new StringBuffer("");
        String s = "开票成功";
        for (Appointmentinfoxgwx a:ln) {
            Appointmentinfoxgwx xg =  appointmentinfoxgwxService.findUniqueBy(a.getId());
                Fapiao fapiao =  fapiaoService.findUniqueByYuyueid(xg.getId());
                if (fapiao!=null){
                    fapiao.setKaipiaodate(entity.getKaipiaodate().substring(0,10));
                    fapiao.setKaipiaono(entity.getKaipiaono());
                    fapiao.setKaipiaostate("已开发票");
                    fapiaoService.update(fapiao);
                }else{
                    fapiao = new Fapiao();
                    fapiao.setYuyueid(xg.getId());
                    fapiao.setKaipiaodate(entity.getKaipiaodate().substring(0,10));
                    fapiao.setKaipiaono(entity.getKaipiaono());
                    fapiao.setKaipiaostate("已开发票");
                    fapiaoService.save(fapiao);
                }
        }
        msg.setMsg(s);
        Struts2Utils.renderJson(msg);
    }

    public void kaipiao(){
        List<Appointmentinfoxgwx> ln=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        StringBuffer sb = new StringBuffer("");
        StringBuffer ishui = new StringBuffer("");
        String s = "开票成功";
        for (Appointmentinfoxgwx a:ln) {
            Appointmentinfoxgwx xg =  appointmentinfoxgwxService.findBySampleNo(a.getSampleNo());
            if(xg!=null){
                Fapiao fapiao =  fapiaoService.findUniqueByYuyueid(xg.getId());
                if (fapiao!=null){
                    fapiao.setKaipiaodate(a.getKaipiaodate());
                    fapiao.setKaipiaono(a.getKaipiaono());
                    fapiao.setKaipiaostate("已开发票");
                    fapiaoService.update(fapiao);
                }else{
                    fapiao = new Fapiao();
                    fapiao.setYuyueid(xg.getId());
                    fapiao.setKaipiaodate(a.getKaipiaodate());
                    fapiao.setKaipiaono(a.getKaipiaono());
                    fapiao.setKaipiaostate("已开发票");
                    fapiaoService.save(fapiao);
                }

            }else{
                sb.append(a.getSampleNo()+",");
            }
        }
        if(!sb.toString().equals("")){
            s="样本编号为"+sb.toString()+"的样本信息不存在或未回款不能开票,其他样本开票成功";
        }
        msg.setMsg(s);
        Struts2Utils.renderJson(msg);
    }

    public void iskaipiao(){
        List<Appointmentinfoxgwx> ln=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        StringBuffer sb = new StringBuffer("");

        for (Appointmentinfoxgwx a:ln) {
            Appointmentinfoxgwx xg =  appointmentinfoxgwxService.findUniqueBySampleNo(a.getSampleNo());
            if(xg == null){
                xg = new Appointmentinfoxgwx();
                xg.setId(0);
            }
            Fapiao fapiao = fapiaoService.findUniqueByYuyueid(xg.getId());
            if(fapiao!=null){
                sb.append(a.getSampleNo()+",");
            }
        }
        msg.setMsg(sb.toString());
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
        entity = new Fapiao();
    }

    @Override
    public Fapiao getModel() {
        return entity;
    }
}
