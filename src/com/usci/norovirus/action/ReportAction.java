package com.usci.norovirus.action;

import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Norovirus;
import com.usci.norovirus.service.AppointmentinfoxgwxService;
import com.usci.norovirus.service.NorovirusService;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("exceptionstack") })
public class ReportAction extends CrudActionSupport<Norovirus> {
    private Norovirus entity;
    @Autowired
    private NorovirusService norovirusService;
    @Autowired
    private AppointmentinfoxgwxService appointmentinfoxgwxService;
    public String screport2() throws Exception{
        Appointmentinfoxgwx appointmentinfoxgwx  = appointmentinfoxgwxService.findUniqueBy(10538);
        appointmentinfoxgwx.setName("123222");
        appointmentinfoxgwxService.update(appointmentinfoxgwx);
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
        entity=new Norovirus();
    }

    @Override
    public Norovirus getModel() {
        return entity;
    }

    public void  findReport(){
        Norovirus n=norovirusService.findByNameAndCard(entity.getName(),entity.getIdcard());
        if(n!=null&&n.getPathName()!=null&&!"".equals(n.getPathName())){
            msg.setSuccess(true);
            msg.setMsg(n.getPathName());
        }else{
            msg.setSuccess(false);
            msg.setMsg("报告未出，请稍后查询");
        }

        Struts2Utils.renderJson(msg);
    }

}
