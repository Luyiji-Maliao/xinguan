package com.usci.norovirus.service;


import com.lims.core.orm.Page;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.dao.AppointmentinfoDao;
import com.usci.norovirus.dao.AppointmentinfoxgwxDao;
import com.usci.norovirus.entity.Appointmentinfo;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Norovirus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class AppointmentinfoxgwxService {

    @Autowired
    private AppointmentinfoxgwxDao appointmentinfoxgwxDao;
    @Autowired
    private NorovirusService norovirusService;
    Logger log = LoggerFactory.getLogger(AppointmentinfoxgwxService.class);

    /**
     * 获取总条数
     * @param sql
     * @return
     */
    public Integer getTotalCount(String sql){
        return Integer.parseInt(appointmentinfoxgwxDao.queryBySql("select count(1) from ("+sql+") as total").get(0).toString());
    }
    @Transactional(transactionManager = "sqltransactionManager")
    public void save(Appointmentinfoxgwx entity){
        entity.setInputTime(Struts2Utils.getStringDate(new Date()));
        entity.setInputName(Struts2Utils.getSessionUser().getName());
        appointmentinfoxgwxDao.save(entity);
    }

    @Transactional(transactionManager = "sqltransactionManager")
    public void glzsave(Appointmentinfoxgwx entity){
        entity.setInputTime(Struts2Utils.getStringDate(new Date()));
        entity.setInputName("橄榄枝");
        appointmentinfoxgwxDao.save(entity);
    }

    @Transactional(transactionManager = "sqltransactionManager")
    public void update(Appointmentinfoxgwx entity){
        entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
        entity.setUpdateName(Struts2Utils.getSessionUser().getName());
        appointmentinfoxgwxDao.update(entity);
    }

    @Transactional(transactionManager = "sqltransactionManager")
    public void update(Appointmentinfoxgwx entity,String tuikuan){
        entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
        if(tuikuan!=null){
            entity.setUpdateName(tuikuan);
        }else{
            entity.setUpdateName(Struts2Utils.getSessionUser().getName());
        }
        appointmentinfoxgwxDao.update(entity);
    }


    public String getSqlTotal(String date){
        String sql = "select count(1) total from xg_appointmentinfo where 1=1 and yuyuedate ='"+date+"'";
        return sql;
    }

    public void findPage(Page<Appointmentinfoxgwx> page, String sql, String string, int start, int limit) {
        int ix=sql.indexOf("from")>-1?sql.indexOf("from"):sql.indexOf("FROM");
    	String oj = appointmentinfoxgwxDao.queryBySql("select count(1) from ( select '' "+sql.substring(ix)+" ) as c").get(0).toString();
        //分页sql
        sql+=" LIMIT "+start+" , "+limit;
        List<Appointmentinfoxgwx> lcs=appointmentinfoxgwxDao.entityBySql(sql, Appointmentinfoxgwx.class);
       /* for(Appointmentinfoxgwx appointmentinfoxgwx:lcs){
        	if(appointmentinfoxgwx.getSampleNo()!=null&&!"".equals(appointmentinfoxgwx.getSampleNo())){
        		List<Norovirus> norovirusList = norovirusService.findBySampleNo(appointmentinfoxgwx.getSampleNo());
        		if(norovirusList.size()>0){
        			if(norovirusList.get(0).getPathName()!=null&&!"".equals(norovirusList.get(0).getPathName())){
        				appointmentinfoxgwx.setReportState("已生成");
        			}else{
        				appointmentinfoxgwx.setReportState("未生成");
        			}
        		}
        	}
        }*/
        page.setResult(lcs);
        page.setTotalCount(Long.parseLong(oj.toString()));
    }

    public List<Appointmentinfoxgwx > entitybysql(String sql){
        return appointmentinfoxgwxDao.entityBySql(sql,Appointmentinfoxgwx.class);
    }

    public List<Object[]> queryBySql(String sql){
        return appointmentinfoxgwxDao.queryBySql(sql);
    }
    public Appointmentinfoxgwx findUniqueBy(Integer  id) {
        return appointmentinfoxgwxDao.findUniqueBy("id", id);
    }

    public  Appointmentinfoxgwx findUniqueBySampleNo(String sampleNo){
        return appointmentinfoxgwxDao.findUniqueBy("sampleNo", sampleNo);
    }

    public String getSql(String sampleNo){
        String sql = "select a.id from xg_huikuan h LEFT JOIN xg_appointmentinfo a on(a.id=h.yuyueid) where a.sampleNo = '"+sampleNo+"'";
        return sql;
    }

    public  Appointmentinfoxgwx findBySampleNo(String sampleNo) {
        List<Appointmentinfoxgwx> lcs=appointmentinfoxgwxDao.entityBySql(this.getSql(sampleNo), Appointmentinfoxgwx.class);
        if(lcs == null || lcs.size() == 0){
            return  null;
        }
        return lcs.get(0);
    }

    public String findBySqlSelectCount(String sql){
        String oj=appointmentinfoxgwxDao.queryBySql(sql).get(0).toString();
        return oj;
    }

    @Transactional(transactionManager = "sqltransactionManager")
    public void update(String sql) throws SQLException {
        appointmentinfoxgwxDao.updateBySql(sql);
    }

    @Transactional(transactionManager = "sqltransactionManager")
    public void deleteByOuttradeno(String outtradeno) throws SQLException {
        String sql = "DELETE FROM xg_appointmentinfo WHERE outtradeno='"+outtradeno+"' AND (sampleNo='' OR sampleNo IS NULL);";
        appointmentinfoxgwxDao.deleteBySql(sql);
    }




}
