package com.usci.xgcollectinfo.service;


import com.lims.core.orm.Page;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.xgcollectinfo.dao.FiveoneinfoDao;
import com.usci.xgcollectinfo.entity.Fiveoneinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class FiveoneinfoService {
    @Autowired
    private FiveoneinfoDao fiveoneinfoDao;

    public void findpage(Page<Fiveoneinfo> page, String sql, int start, int limit){
        String oj=fiveoneinfoDao.queryBySql("select count(1) from ("+sql+") as total").get(0).toString();
        sql+=" LIMIT "+start+" , "+limit;
        List<Fiveoneinfo> lcs=fiveoneinfoDao.entityBySql(sql, Fiveoneinfo.class);
        page.setResult(lcs);
        page.setTotalCount(Long.parseLong(oj.toString()));
    }


    public Fiveoneinfo findUniqueBy(String fiveonenum){
        return  fiveoneinfoDao.findUniqueBy("fiveonenum",fiveonenum);
    }

    /**
     * 添加
     * @param fiveoneinfo
     */
    @Transactional
    public void add(Fiveoneinfo fiveoneinfo){
        fiveoneinfo.setInputtime(Struts2Utils.getStringDate(new Date()));
        fiveoneinfo.setRukutime(Struts2Utils.getStringDate(new Date()));
        fiveoneinfo.setInputname(Struts2Utils.getSessionUser().getName());
        fiveoneinfo.setRukuname(Struts2Utils.getSessionUser().getName());
        fiveoneinfoDao.save(fiveoneinfo);
    }

    /**
     * 修改
     * @param fiveoneinfo
     */
    @Transactional
    public void update(Fiveoneinfo fiveoneinfo){
        fiveoneinfo.setRukutime(Struts2Utils.getStringDate(new Date()));
        fiveoneinfo.setRukuname(Struts2Utils.getSessionUser().getName());
        fiveoneinfoDao.update(fiveoneinfo);
    }
}
