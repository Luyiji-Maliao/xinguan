package com.usci.xgcollectinfo.action;

import com.lims.core.orm.Page;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.xgcollectinfo.entity.xgcollectinfo;
import com.usci.xgcollectinfo.service.Xgcollectinfo2Service;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;


@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
        @Result(name = "modulepage", location = "/WEB-INF/content/xgcollectinfo/xgblcrud.jsp")
})
public class Xgcollectinfo2Action extends CrudActionSupport<xgcollectinfo> {

    private xgcollectinfo entity;
    @Autowired
    private Xgcollectinfo2Service xgcollectinfoService2;

    private String searchSampleNo;
    private String searchCertNumber;
    private String searchpartyName;
    private String searchinputaddress;
    private String searchphone;
    private String startDate;
    private String endDate;
    private String saveorupdate;
    private String ids;
    private String arr;
    private String jsonArray;
    private String searchisruku;
    private String searchfiveone;
    private String itmessc;

    public String getItmessc() {
        return itmessc;
    }

    public void setItmessc(String itmessc) {
        this.itmessc = itmessc;
    }

    public String getSearchisruku() {
        return searchisruku;
    }

    public void setSearchisruku(String searchisruku) {
        this.searchisruku = searchisruku;
    }

    public String getSearchfiveone() {
        return searchfiveone;
    }

    public void setSearchfiveone(String searchfiveone) {
        this.searchfiveone = searchfiveone;
    }

    public String getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(String jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getArr() {
        return arr;
    }

    public void setArr(String arr) {
        this.arr = arr;
    }

    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }
    public String getSaveorupdate() {
        return saveorupdate;
    }
    public void setSaveorupdate(String saveorupdate) {
        this.saveorupdate = saveorupdate;
    }
    public String getSearchphone() {
        return searchphone;
    }
    public void setSearchphone(String searchphone) {
        this.searchphone = searchphone;
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
    public String getSearchinputaddress() {
        return searchinputaddress;
    }
    public void setSearchinputaddress(String searchinputaddress) {
        this.searchinputaddress = searchinputaddress;
    }
    public String getSearchCertNumber() {
        return searchCertNumber;
    }
    public void setSearchCertNumber(String searchCertNumber) {
        this.searchCertNumber = searchCertNumber;
    }
    public String getSearchSampleNo() {
        return searchSampleNo;
    }
    public void setSearchSampleNo(String searchSampleNo) {
        this.searchSampleNo = searchSampleNo;
    }
    public String getSearchpartyName() {
        return searchpartyName;
    }
    public void setSearchpartyName(String searchpartyName) {
        this.searchpartyName = searchpartyName;
    }

    @Override
    public String list() throws Exception {
        return "modulepage";
    }
    public String save(){
        xgcollectinfo x = xgcollectinfoService2.findbySamplenum(entity.getSamplenum());
        if(x != null && x.getIsshow() == 0){
            msg.setSuccess(false);
            msg.setMsg("样本编号重复!!!");
            Struts2Utils.renderJson(msg);
        }else{
            entity.setInputname(Struts2Utils.getSessionUser().getName());
            entity.setInputtime(Struts2Utils.getStringDate(new Date()));
            entity.setIsruku("否");
            xgcollectinfoService2.save(entity);
            msg.setMsg("恭喜你，录入成功");
            Struts2Utils.renderJson(msg);
        }
        return null;
    }

    public void upd(){
        String ms = "修改成功";
        xgcollectinfo  x = xgcollectinfoService2.findUniqueBy(entity.getId());
        x.setSamplenum(entity.getSamplenum());
        x.setPartyName(entity.getPartyName());
        x.setPhone(entity.getPhone());
        x.setCertNumber(entity.getCertNumber());
        x.setInputAddress(entity.getInputAddress());
        x.setIsruku("否");
        x.setFiveone(entity.getFiveone());
        x.setUpdatename(Struts2Utils.getSessionUser().getName());
        x.setUpdatetime(Struts2Utils.getStringDate(new Date()));
        xgcollectinfoService2.update(x);
        msg.setMsg(ms);
        Struts2Utils.renderJson(msg);
    }

    /**
     * 录入模板
     */
    public void downloadlrfile() throws IOException{
        xgcollectinfoService2.excelmuban();
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

    /**
     * 批量添加
     */
    public void saves(){
        List<xgcollectinfo> ln=Struts2Utils.conver(itmessc,xgcollectinfo.class);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<ln.size();i++){
            xgcollectinfo xg = xgcollectinfoService2.findbySamplenum(ln.get(i).getSamplenum());
            if(null == xg){
                xgcollectinfo xgcollectinfo = ln.get(i);
                xgcollectinfo.setIsruku("否");
                xgcollectinfo.setIsshow(0);
                xgcollectinfo.setInputname(Struts2Utils.getSessionUser().getName());
                xgcollectinfoService2.save(ln.get(i));
            }else{
                sb.append(ln.get(i).getSamplenum()+",");
            }
        }
        String ms = "录入成功";
        if(sb!=null && !sb.toString().equals("")){
            ms+=sb+"已存在";
        }
        msg.setMsg(ms);
        Struts2Utils.renderJson(msg);
    }

    /**
     * 循环删除
     */
    public void delete(){
        String[] id = ids.split(",");
        for(String i : id){
            xgcollectinfo xgcollectinfo = xgcollectinfoService2.findUniqueBy(Integer.parseInt(i));
            xgcollectinfo.setIsshow(1);
            xgcollectinfo.setUpdatetime(Struts2Utils.getStringDate(new Date()));
            xgcollectinfo.setUpdatename(Struts2Utils.getSessionUser().getName());
            xgcollectinfoService2.update(xgcollectinfo);
        }
    }

    public List<xgcollectinfo> selectFunction(){
        String sql = this.getsql(Struts2Utils.getSessionUser().getName(),0);
        List<xgcollectinfo> list = xgcollectinfoService2.queryBySql(sql);
        return list;
    }

    public String Excel(){
        List<xgcollectinfo> xgcollectinfolist=Struts2Utils.conver(jsonArray,xgcollectinfo.class);
        if(xgcollectinfolist.size()==0){
            xgcollectinfolist = this.selectFunction();
        }
        if(xgcollectinfolist.size()>0){
            xgcollectinfoService2.excel(xgcollectinfolist,"核算检测信息记录");
        }
        Struts2Utils.renderJson("123");
        return null;
    }

    public void daochuExcel() throws IOException {
        String name=new String("核算检测信息记录".getBytes("UTF-8"), "ISO-8859-1");
        Struts2Utils.getResponse().addHeader("Content-Disposition", "attachment;filename=\""+name+Struts2Utils.getymd(new Date())+".xls"+"\"");
        OutputStream os = Struts2Utils.getResponse().getOutputStream();
        String uploadFileSavePath= ServletActionContext.getServletContext().getRealPath("/documents");
        String path=uploadFileSavePath+"/核算检测信息记录.xls";
        FileInputStream is=new FileInputStream(path);

        byte[] buffer = new byte[400];
        int length = 0;
        while (-1 != (length = is.read(buffer))) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }

    public String list2() throws Exception {
        String sql = this.getsql(Struts2Utils.getSessionUser().getName(),0);
        Page<xgcollectinfo> page = new Page<xgcollectinfo>(limit);
        page.setPageNo((start / limit) + 1);
        xgcollectinfoService2.findpage(page,sql,start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }

    public String list3() throws Exception{
        String sql = this.getsql(Struts2Utils.getSessionUser().getName(),0);
        Page<xgcollectinfo> page = new Page<xgcollectinfo>(limit);
        page.setPageNo((start / limit) + 1);
        xgcollectinfoService2.findpage(page,sql,start,limit);
        Struts2Utils.renderJson(page);
        return null;
    }

    public String getsql(String LoginName,int isshow){
        String sql = "select * from xg_collectinfo where 1=1 ";
        if(arr == null || arr.equals("") || arr.equals("非全部")){
            sql +=" and inputName ='"+LoginName+"' ";
        }
        if(searchSampleNo != null && !searchSampleNo.equals("")){
            sql+=" and sampleNum like '%"+searchSampleNo+"%' ";
        }
        if(searchCertNumber != null && !searchCertNumber.equals("")){
            sql+=" and certNumber like '%"+searchCertNumber+"%' ";
        }
        if(searchpartyName != null && !searchpartyName.equals("")){
            sql+=" and partyName like '%"+searchpartyName+"%' ";
        }
        if(searchinputaddress != null &&!searchinputaddress.equals("")){
            sql+=" and inputAddress like '%"+searchinputaddress+"%' ";
        }
        if(searchphone != null && !searchphone.equals("")){
            sql+=" and phone like '%"+searchphone+"%' ";
        }
        if(searchisruku != null && !searchisruku.equals("")){
            sql+=" and isruku = '"+searchisruku+"' ";
        }
        if(searchfiveone != null && !searchfiveone.equals("")){
            sql+=" and fiveone like '%"+searchfiveone+"%' ";
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
        sql+=" and isshow = "+isshow+" ";
        sql+=" order by inputtime desc";
        return sql;
    }


    @Override
    public String modulepage() throws Exception {
        return "modulepage";
    }

    @Override
    protected void prepareModel() throws Exception {
        entity = new xgcollectinfo();

    }

    @Override
    public xgcollectinfo getModel()  {
        return entity;
    }

}
