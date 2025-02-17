package com.usci.norovirus.service;

import com.lims.core.orm.Page;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.dao.TuantiDao;
import com.usci.norovirus.entity.Tuanti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class TuantiService {

    @Autowired
    private TuantiDao tuantiDao;


    @Transactional(transactionManager = "sqltransactionManager")
    public void save(Tuanti entity){
        entity.setInputTime(Struts2Utils.getStringDate(new Date()));
        entity.setInputName(Struts2Utils.getSessionUser().getName());
        tuantiDao.save(entity);
    }

    @Transactional(transactionManager = "sqltransactionManager")
    public void update(Tuanti entity){
        entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
        entity.setUpdateName(Struts2Utils.getSessionUser().getName());
        tuantiDao.update(entity);
    }

    public void findPage(Page<Tuanti> page, String sql, String string, int start, int limit) {
        String oj=tuantiDao.queryBySql("select count(1) from ("+sql+") as total").get(0).toString();
        //分页sql
        sql+=" LIMIT "+start+" , "+limit;
        List<Tuanti> lcs=tuantiDao.entityBySql(sql, Tuanti.class);
        page.setResult(lcs);
        page.setTotalCount(Long.parseLong(oj.toString()));
    }

    public List<Tuanti > entitybysql(String sql){
        return tuantiDao.entityBySql(sql,Tuanti.class);
    }
    public Tuanti findUniqueBy(Integer  id) {
        return tuantiDao.findUniqueBy("id", id);
    }

    public  Tuanti findUniqueBytuanname(String tuanname){
        return tuantiDao.findUniqueBy("tuanname", tuanname);
    }

    public String findBySqlSelectCount(String sql){
        String oj=tuantiDao.queryBySql(sql).get(0).toString();
        return oj;
    }

    @Transactional(transactionManager = "sqltransactionManager")
    public void update(String sql) throws SQLException {
        tuantiDao.updateBySql(sql);
    }

}
