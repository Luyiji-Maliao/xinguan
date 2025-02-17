package com.usci.norovirus.service;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.lims.core.orm.Page;
import com.lims.core.utils.web.Struts2Utils;
import com.lowagie.text.pdf.PdfWriter;
import com.usci.norovirus.dao.NorovirusDao;
import com.usci.norovirus.dao.NorovirusreportstateDao;
import com.usci.norovirus.entity.Norovirus;
import com.usci.norovirus.entity.Norovirusreportstate;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class NorovirusreportstateService {

    @Autowired
    private NorovirusreportstateDao  norovirusreportstateDao;


    public void findPage(Page<Norovirusreportstate> page, String sql, String string, int start, int limit) {
        String oj=norovirusreportstateDao.queryBySql("select count(1) from ("+sql+") as total").get(0).toString();
        //分页sql
        sql+=" LIMIT "+start+" , "+limit;
        List<Norovirusreportstate> lcs=norovirusreportstateDao.entityBySql(sql, Norovirusreportstate.class);
        page.setResult(lcs);
        page.setTotalCount(Long.parseLong(oj.toString()));
    }

    public List<Norovirusreportstate> findBySampleNoAll(String sql) {
        List<Norovirusreportstate> lcs=norovirusreportstateDao.entityBySql(sql, Norovirusreportstate.class);
       return lcs;
    }

    /**
     * 获取条数
     * @param sql
     * @return
     */
    public int count(String sql){
        String oj=norovirusreportstateDao.queryBySql("select count(1) from ("+sql+") as total").get(0).toString();
        return Integer.parseInt(oj);
    }

    /**
     * 根据样本编号查找
     * @param sampleNo
     * @return
     */
    public Norovirusreportstate findUniqueBy(String sampleNo) {
        return norovirusreportstateDao.findUniqueBy("sampleNo", sampleNo);
    }


    @Transactional
    public void save(Norovirusreportstate entity){
        entity.setInputTime(Struts2Utils.getStringDate(new Date()));
        entity.setInputName(Struts2Utils.getSessionUser().getName());
        norovirusreportstateDao.save(entity);
    }

    @Transactional
    public void update(Norovirusreportstate entity){
        entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
        entity.setUpdateName(Struts2Utils.getSessionUser().getName());
        norovirusreportstateDao.update(entity);
    }

}
