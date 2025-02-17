package com.usci.norovirus.action;

import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Huikuan;
import com.usci.norovirus.entity.Huikuan;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.norovirus.service.HuikuanService;
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
public class HuikuanAction extends CrudActionSupport<Huikuan> {

    private Huikuan entity;

    private String itemsxjyb;




    @Autowired
    private HuikuanService huikuanService;

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


    public void huikuan(){
        List<Appointmentinfoxgwx> ln=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        StringBuffer sb = new StringBuffer("");
        String s = "回款成功";
        for (Appointmentinfoxgwx a:ln) {
            Appointmentinfoxgwx xg =  appointmentinfoxgwxService.findUniqueBySampleNo(a.getSampleNo());
            if(xg!=null){
                Huikuan huikuan =  huikuanService.findUniqueByYuyueid(xg.getId());
                if (huikuan!=null){
                    huikuan.setCollectionmethod(a.getCollectionmethod());
                    huikuan.setJinzhangmoney(a.getJinzhangmoney());
                    huikuan.setHuikuanstate("是");
                    huikuan.setKaipiaomoney(a.getKaipiaomoney());
                    huikuan.setShouxufei(a.getShouxufei());
                    huikuanService.update(huikuan);
                }else{
                    huikuan = new Huikuan();
                    huikuan.setYuyueid(xg.getId());
                    huikuan.setCollectionmethod(a.getCollectionmethod());
                    huikuan.setJinzhangmoney(a.getJinzhangmoney());
                    huikuan.setHuikuanstate("是");
                    huikuan.setKaipiaomoney(a.getKaipiaomoney());
                    huikuan.setShouxufei(a.getShouxufei());
                    huikuanService.save(huikuan);
                }

            }else{
                sb.append(a.getSampleNo()+",");
            }
        }
        if(!sb.toString().equals("")){
            s="样本编号为"+sb.toString()+"的样本信息不存在不能回款,其他样本回款成功";
        }
        msg.setMsg(s);
        Struts2Utils.renderJson(msg);
    }


    public void ishuikuan(){
        List<Appointmentinfoxgwx> ln=Struts2Utils.conver(itemsxjyb,Appointmentinfoxgwx.class);
        StringBuffer sb = new StringBuffer("");
        for (Appointmentinfoxgwx a:ln) {
            Appointmentinfoxgwx xg =  appointmentinfoxgwxService.findUniqueBySampleNo(a.getSampleNo());
            if(xg == null){
                xg = new Appointmentinfoxgwx();
                xg.setId(0);
            }
            Huikuan huikuan = huikuanService.findUniqueByYuyueid(xg.getId());
            if(huikuan!=null){
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
        entity = new Huikuan();
    }

    @Override
    public Huikuan getModel() {
        return entity;
    }
}
