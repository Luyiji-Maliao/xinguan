package com.usci.norovirus.action;

import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.system.service.SendUrlService;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 橄榄枝新冠预约录入接口
 */

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("exceptionstack") })

public class AppoAction extends CrudActionSupport<Appointmentinfoxgwx> {
    private String outtradeno;
    private String phone;

    public String getOuttradeno() {
        return outtradeno;
    }
    public void setOuttradeno(String outtradeno) {
        this.outtradeno = outtradeno;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Autowired
    private AppointmentinfoxgwxService appointmentinfoxgwxService;


    @Autowired
    private SendUrlService sendUrlService;//手机短信

    public String saveAll() throws Exception{
        try{
            Appointmentinfoxgwx appointmentinfoxgwx = new Appointmentinfoxgwx();
            appointmentinfoxgwx.setQudao("橄榄枝");  //渠道默认是橄榄枝
            appointmentinfoxgwx.setPhone(phone);
            appointmentinfoxgwx.setOuttradeno(outtradeno);
            appointmentinfoxgwx.setIssuccessed("是");
            appointmentinfoxgwx.setSampleType("咽拭子");
            appointmentinfoxgwx.setCheckProject("新型冠状病毒 2019-nCoV 核酸检测");
            appointmentinfoxgwx.setInspection("优迅医学");
            appointmentinfoxgwxService.glzsave(appointmentinfoxgwx);
            if(appointmentinfoxgwx.getPhone()!=null && !appointmentinfoxgwx.getPhone().equals("")){
                String smsTemplate = "尊敬的用户，您已成功下单优迅医学核酸检测服务，完成预约还需您前往“优迅医学”微信公众号填写个人信息，并完成采样点和日期的预约。完成预约后，我们将为您生成预约码，请您携带好身份证件及预约码及时前往采样点采样，感谢您的配合。【预约流程：微信搜索并关注优迅医学公众号——新冠检测——预约检测——填写信息】";
//                sendUrlService.reportIssueSendSms(appointmentinfoxgwx.getPhone(),smsTemplate);
            }
            msg.setSuccess(true);
        }catch (Exception e){
            msg.setSuccess(false);
        }
        Struts2Utils.renderJson(msg);
        return null;
    }


    @Override
    public String list() throws Exception {
        return null;
    }

    @Override
    public String modulepage() throws Exception {
        return null;
    }

    @Override
    public String save() throws Exception {
        return null;
    }

    @Override
    protected void prepareModel() throws Exception {

    }

    @Override
    public Appointmentinfoxgwx getModel() {
        return null;
    }
}
