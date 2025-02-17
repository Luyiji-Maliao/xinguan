package com.usci.xgcollectinfo.action;

import com.lims.core.orm.Page;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.xgcollectinfo.entity.Xgreserve;
import com.usci.xgcollectinfo.service.XgreserveService;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
        @Result(name = "modulepage", location = "/WEB-INF/content/xgcollectinfo/xgreserve.jsp")
})
public class XgreserveAction extends CrudActionSupport<Xgreserve> {
    private Xgreserve entity;
    @Autowired
    private XgreserveService xgreserveService;

    private String itmessc;
    private String searchName;
    private String searchphone;
    private String searchCertNumber;
    private String startDate;
    private String endDate;
    private String ids;

    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }
    public String getSearchName() {
        return searchName;
    }
    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
    public String getSearchphone() {
        return searchphone;
    }
    public void setSearchphone(String searchphone) {
        this.searchphone = searchphone;
    }
    public String getSearchCertNumber() {
        return searchCertNumber;
    }
    public void setSearchCertNumber(String searchCertNumber) {
        this.searchCertNumber = searchCertNumber;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getItmessc() {
        return itmessc;
    }
    public void setItmessc(String itmessc) {
        this.itmessc = itmessc;
    }

    public String jiansuo(){
        String sql = "select * from xg_reserve where 1=1 ";
        if(entity.getName() != null && !entity.getName().equals("")){
            sql+=" and name like '%"+entity.getName()+"%'";
        }
        if(entity.getCertNumber() != null && !entity.getCertNumber().equals("")){
            sql+=" and certNumber like '%"+entity.getCertNumber()+"%'";
        }
        if(entity.getPhone() != null && !entity.getPhone().equals("")){
            sql+=" and phone like '%"+entity.getPhone()+"%'";
        }
        List<Xgreserve> relist = xgreserveService.queryBySql(sql);
        Struts2Utils.renderJson(relist);
        return null;
    }

    public void upd(){
        Xgreserve xgreserve = xgreserveService.findbyid(entity.getId());
        if(null != xgreserve){
            xgreserve.setName(entity.getName());
            xgreserve.setCertNumber(entity.getCertNumber());
            xgreserve.setPhone(entity.getPhone());
            xgreserveService.update(xgreserve);
        }
        msg.setMsg("修改成功");
        Struts2Utils.renderJson(msg);
    }

    public void downloadlrfile() throws Exception{
        xgreserveService.excelmuban();
        String name=new String("luru".getBytes("UTF-8"), "ISO-8859-1");
        Struts2Utils.getResponse().addHeader("Content-Disposition", "attachment;filename=\""+name+Struts2Utils.getymd(new Date())+".xls"+"\"");
        OutputStream os = Struts2Utils.getResponse().getOutputStream();
        String uploadFileSavePath= ServletActionContext.getServletContext().getRealPath("/documents");
        String path=uploadFileSavePath+"/luru.xls";
        FileInputStream is=new FileInputStream(path);

        byte[] buffer = new byte[400];
        int length = 0;
        while (-1 != (length = is.read(buffer))) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }

    public void delete(){
        String[] id = ids.split(",");
        for(String i : id){
            Xgreserve xgreserve = xgreserveService.findbyid(Integer.parseInt(i));
            xgreserveService.delete(xgreserve);
        }
    }

    public String getsql(){
        String sql = "select * from xg_reserve where 1=1 ";
        if(searchName != null && !searchName.equals("")){
            sql+=" and name like '%"+searchName+"%' ";
        }
        if(searchphone != null && !searchphone.equals("")){
            sql+=" and phone like '%"+searchphone+"%' ";
        }
        if(searchCertNumber != null && !searchCertNumber.equals("")){
            sql+=" and certNumber like '%"+searchCertNumber+"%' ";
        }
        if(startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")){
            int a = startDate.indexOf("T");
            setStartDate(startDate.substring(0,a));
            a = endDate.indexOf("T");
            setEndDate(endDate.substring(0,a));
            sql+=" and inputtime >= '"+startDate+"' and inputtime<= '"+endDate+" 24'";
        }else{
            if(startDate != null && !startDate.equals("")){
                int a = startDate.indexOf("T");
                setStartDate(startDate.substring(0,a));
                sql+=" and inputtime like '"+startDate+"%' ";
            }
            if(endDate != null && !endDate.equals("")){
                int a = endDate.indexOf("T");
                setEndDate(endDate.substring(0,a));
                sql+=" and inputtime like '"+endDate+"%' ";
            }
        }
        sql+=" ORDER BY inputtime DESC";
        return sql;
    }

    @Override
    public String list() throws Exception {
        String sql = this.getsql();
        Page<Xgreserve> page = new Page<Xgreserve>(limit);
        page.setPageNo((start / limit) + 1);
        xgreserveService.findpage(page,sql,start,limit);
        Struts2Utils.renderJson(page);
        return "modulepage";
    }

    @Override
    public String modulepage() throws Exception {
        return "modulepage";
    }

    @Override
    public String save() throws Exception {
        List<Xgreserve> list=Struts2Utils.conver(itmessc,Xgreserve.class);
        String certnum = "";
        int count = 0;
        for(Xgreserve re : list){
            Xgreserve xgreserve = xgreserveService.findbycertNumber(re.getCertNumber());
            if(null != xgreserve){
                certnum += re.getCertNumber()+",";
                count++;
                if(count == 4){
                    certnum+="\n";
                    count = 0;
                }
            }else{
                xgreserveService.save(re);
            }
        }
        String ms = "录入成功！";
        if(certnum != ""){
            ms+="\n";
            ms+=certnum+"\n已存在";
        }
        msg.setMsg(ms);
        Struts2Utils.renderJson(msg);
        return null;
    }

    @Override
    protected void prepareModel() throws Exception {
        entity = new Xgreserve();
    }

    @Override
    public Xgreserve getModel() {
        return entity;
    }
}
