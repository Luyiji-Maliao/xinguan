package com.usci.xgcollectinfo.service;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.xgcollectinfo.dao.xgcollectinfoDao;
import com.usci.xgcollectinfo.entity.xgcollectinfo;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class xgcollectinfoService {
    @Autowired
    private xgcollectinfoDao xgcollectinfoDao;


    /**
     * 查询isshow为0状态的信息
     * @return
     */
    public List<xgcollectinfo> find(String LoginName) {
        String hql = "from xgcollectinfo where 1=1 ";
        if(null != LoginName && !LoginName.equals("")){
            hql+=" and inputname = '"+LoginName+"' ";
        }
        hql+=" and isshow = 0 order by inputTime desc ";
        List<xgcollectinfo> lcs =(List<xgcollectinfo>) xgcollectinfoDao.createQuery(hql)
                .list();
        HttpServletRequest request= ServletActionContext.getRequest();
        request.setAttribute("xgList",lcs);
        return lcs;
    }

    public List<xgcollectinfo> findBy(String samplenum){
        String hql = "from xgcollectinfo where 1=1 ";
        if(samplenum != null && !samplenum.equals("")){
            hql +=" and samplenum = '"+samplenum+"' ";
        }
        hql+=" and isshow = 0 ";
        List<xgcollectinfo> lcs = new ArrayList<xgcollectinfo>();
        if(xgcollectinfoDao.find(hql) != null){
            lcs = xgcollectinfoDao.find(hql);
        }
        return lcs;
    }

    /**
     * 获取当天已采集数量
     * @return
     */
    public int NowDateCount(String LoginName){
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        String date = s.format(d).toString();
        String hql = "from xgcollectinfo where 1=1 ";
        if(null != LoginName && !LoginName.equals("")){
            hql+=" and inputname = '"+LoginName+"' ";
        }
        hql+=" and inputtime like '"+date+"%'";
        hql+=" and isshow = 0";
        Query query = xgcollectinfoDao.createQuery(hql);
        List<xgcollectinfo> lcs = new ArrayList<xgcollectinfo>();
        if(query != null){
             lcs = query.list();
        }
        return lcs.size();
    }

    /**
     * 获取当天该身份证号是否录入过
     * @param certNumber
     * @return
     */
    public List<xgcollectinfo> getNowAll(String certNumber){
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        String date = s.format(d).toString();
        String hql = "from xgcollectinfo where inputtime like '"+date+"%' and certNumber = '"+certNumber+"' order by inputtime desc";

        Query query = xgcollectinfoDao.createQuery(hql);
        List<xgcollectinfo> lcs =query.list();
        return lcs;
    }

    /**
     * 添加
     * @param xgcollectinfo
     * @return
     */
    @Transactional
    public int save (xgcollectinfo xgcollectinfo){
        xgcollectinfo.setInputtime(Struts2Utils.getStringDate(new Date()));
        xgcollectinfo.setInputname(Struts2Utils.getSessionUser().getName());
        int isOk = 1;
        try{
            xgcollectinfoDao.save(xgcollectinfo);
        }catch (Exception e){
            isOk = 0;
        }
        HttpServletRequest request= ServletActionContext.getRequest();
//        request.setAttribute("xgList",this.find());
        return isOk;
    }

    /**
     * 获取最新的样本编号   （暂时没用到）
     * @return
     */
    public int getNewSamplenum(){
        HttpServletRequest request= ServletActionContext.getRequest();
        List<xgcollectinfo> lcs =(List<xgcollectinfo>) request.getAttribute("xgList");
        String samplenumstr = lcs.get(0).getSamplenum();
        int samplenum1 = 0;
        if(samplenumstr != null && !samplenumstr.equals("")){
            String s = samplenumstr.substring(2);
            samplenum1 = Integer.parseInt(s);
        }
        return samplenum1;
    }

    /**
     * 修改
     * @param xgcollectinfo
     */
    @Transactional
    public void update(xgcollectinfo xgcollectinfo){
        xgcollectinfoDao.update(xgcollectinfo);
    }

    /**
     * 根据id获取 一个xgcollectinfo对象
     * @param id
     * @return
     */
    public xgcollectinfo findUniqueBy(Integer id){
        return  xgcollectinfoDao.findUniqueBy("id",id);
    }

    public xgcollectinfo findbySamplenum(String samplenum){
        return xgcollectinfoDao.findUniqueBy("samplenum",samplenum);
    }


}
