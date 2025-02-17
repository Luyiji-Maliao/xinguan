package com.usci.norovirus.service;

import com.lims.core.orm.Page;
import com.usci.norovirus.dao.CooperationinformationDao;
import com.usci.norovirus.entity.Cooperationinformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly=true)
public class CooperationinformationService {
    @Autowired
    CooperationinformationDao cooperationinformationDao;


    public void findPage(Page<Cooperationinformation> page, String sql, String string, int start, int limit) {
        String oj=cooperationinformationDao.queryBySql("select count(1) from ("+sql+") as total").get(0).toString();
        //分页sql
        sql+=" LIMIT "+start+" , "+limit;
        List<Cooperationinformation> lcs=cooperationinformationDao.entityBySql(sql, Cooperationinformation.class);
        page.setResult(lcs);
        page.setTotalCount(Long.parseLong(oj.toString()));
    }
}
