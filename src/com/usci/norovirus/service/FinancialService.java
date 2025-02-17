package com.usci.norovirus.service;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.dao.AppointmentinfoDao;
import com.usci.norovirus.dao.FinancialDao;
import com.usci.norovirus.entity.Appointmentinfo;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Financial;
import com.usci.norovirus.entity.Norovirus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class FinancialService {

    @Autowired
    private FinancialDao financialDao;


    @Transactional(transactionManager = "sqltransactionManager")
    public void save(Financial entity){
        entity.setShenqingdate(Struts2Utils.getStringDate(new Date()));
        entity.setShenqingname(Struts2Utils.getSessionUser().getName());
        financialDao.save(entity);
    }

    @Transactional(transactionManager = "sqltransactionManager")
    public void update(Financial entity){
        if(entity.getTuikuandate()!=null && !entity.getTuikuandate().equals("")){

        }else{
            entity.setTuikuandate(Struts2Utils.getStringDate(new Date()));
        }
        entity.setTuikuanname(Struts2Utils.getSessionUser().getName());
        financialDao.update(entity);
    }

    /**
     * 根据xgappid查询信息
     * @param outtradeno
     * @return
     */
    public List<Financial> findByOuttradeno(String outtradeno) {
        return financialDao.findBy("outtradeno", outtradeno);
    }


}
