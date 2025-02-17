package com.usci.xgcollectinfo.action;

import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.xgcollectinfo.entity.Xgreserve;
import com.usci.xgcollectinfo.entity.xgcollectinfo;
import com.usci.xgcollectinfo.service.XgreserveService;
import com.usci.xgcollectinfo.service.xgcollectinfoService;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
@Results({
        @Result(name = "modulepage", location = "/WEB-INF/content/xgcollectinfo/xgindex.jsp")
})
public class xgcollectinfoAction extends CrudActionSupport<xgcollectinfo> {

    @Autowired
    private xgcollectinfoService xgcollectinfoService;
    @Autowired
    private XgreserveService xgreserveService;

    private String codemethod;

    private String ids;
    private String samplenums;
    private String idcards;
    private String itemsinfo;

    public String getItemsinfo() {
        return itemsinfo;
    }
    public void setItemsinfo(String itemsinfo) {
        this.itemsinfo = itemsinfo;
    }
    public String getSamplenums() {
        return samplenums;
    }
    public void setSamplenums(String samplenums) {
        this.samplenums = samplenums;
    }
    public String getIdcards() {
        return idcards;
    }
    public void setIdcards(String idcards) {
        this.idcards = idcards;
    }
    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }
    public String getCodemethod() {
        return codemethod;
    }
    public void setCodemethod(String codemethod) {
        this.codemethod = codemethod;
    }
    private List<xgcollectinfo> xlist;
    private xgcollectinfo entity;


    @Override
    public String list() throws Exception {

//        Struts2Utils.renderJson();
        return null;
    }
    /**
     * 通过手机号查询储备表里的手机号
     * @throws Exception
     */
    public void getPhoneByCardnum() throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        Xgreserve xgreserve = xgreserveService.findbycertNumber(entity.getCertNumber());
        if(null != xgreserve){
            response.getWriter().print(xgreserve.getPhone());
        }
    }

    public String checked() throws Exception{
        HttpServletRequest request= ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        List<xgcollectinfo> list = Struts2Utils.conver(iteminfo, xgcollectinfo.class);
        StringBuffer returnsample = new StringBuffer();
        StringBuffer msg = new StringBuffer();
        StringBuffer cfsample = new StringBuffer();
        StringBuffer sfcfmsg = new StringBuffer();
        for(xgcollectinfo xg : list){
            List<xgcollectinfo> x = xgcollectinfoService.findBy(xg.getSamplenum());
            if(x.size()== 0){  //判断样本状态为0的是否重复
                List<xgcollectinfo> c = xgcollectinfoService.getNowAll(xg.getCertNumber());
                if(c.size() == 0){

                }else{
                    sfcfmsg.append("\n该身份证号今日已录入，上次录入信息为：\n姓名："+c.get(0).getPartyName()+"\n身份证号："+c.get(0).getCertNumber()+"\n录入时间："+c.get(0).getInputtime());
                }
            }else{
                cfsample.append(xg.getSamplenum()+",");
            }
        }
        if(cfsample!=null && !cfsample.toString().equals("")){
            msg.append("|"+cfsample+"|");
        }
        if(sfcfmsg!=null && !sfcfmsg.toString().equals("")){
            msg.append(sfcfmsg);
        }
        response.getWriter().print(msg);
        return null;
    }

    /**
     * 确认添加
     * @return
     */
    public String againsave(){
        List<xgcollectinfo> list = Struts2Utils.conver(iteminfo, xgcollectinfo.class);
        for(xgcollectinfo x : list){

                x.setIsruku("否");
                xgcollectinfoService.save(x);

        }
        return null;
    }

    @Override
    public String save() throws Exception {
        HttpServletRequest request= ServletActionContext.getRequest();
        List<xgcollectinfo> x = xgcollectinfoService.findBy(entity.getSamplenum());
        if(x.size()!= 0){
            request.setAttribute("isSamplenum","样本编号重复!!!");
        }else{
            String Samplenums = entity.getSamplenum();
            String qz = "";  //单独截取前缀
            for(int i=0;i < Samplenums.length();i++) {
                if(Character.isDigit(Samplenums.charAt(i)) == false){
                    qz+=Samplenums.charAt(i);
                }
            }
            int qzlenth = 0;
            if(qz.length() != 0){  //获取前缀的长度
                qzlenth = qz.length();
            }
            String strsamplenum = entity.getSamplenum().substring(qzlenth); //截去前缀 为：0001
            int samplenum = Integer.parseInt(strsamplenum);   // 转换序号 为：1
            if(codemethod.equals("减码") && samplenum-1 == 0){
                request.setAttribute("isSamplenum","不能再减了!!!");
            } else
                {
                List<xgcollectinfo> list = xgcollectinfoService.getNowAll(entity.getCertNumber());
                String zeros = "";  // 0
                for(int i=0;i < strsamplenum.length();i++) {  //循环截去前缀之后的字符串
                    if(String.valueOf(strsamplenum.charAt(i)).equals("0")){   //判断该字符是否为0
                        zeros+=strsamplenum.charAt(i);
                    }else{
                        break;
                    }
                }
                String abc = "";  //增码或减码String.format之后的字符
                if(codemethod.equals("增码")){
                    if(zeros.equals("")){
                        samplenum+=1;
                    }else if(!zeros.equals("")){
                        samplenum+=1;
                        abc = String.format("%0"+strsamplenum.length()+"d",samplenum);
                    }
                }else{
                    if(zeros.equals("")){
                        samplenum-=1;
                    }else if(!zeros.equals("")){
                        samplenum-=1;
                        abc = String.format("%0"+strsamplenum.length()+"d",samplenum);
                    }
                }
                entity.setInputname(Struts2Utils.getSessionUser().getName());
                xgcollectinfoService.save(entity);
                request.setAttribute("inputmsg","");
                if(zeros.equals("")){
                    request.setAttribute("ZengNum",qz+samplenum);
                }else if(!zeros.equals("")){
                    request.setAttribute("ZengNum",qz+abc);
                }
            }
        }

        xlist = xgcollectinfoService.find(Struts2Utils.getSessionUser().getName());
        request.setAttribute("xlist",xlist);
        request.setAttribute("ZengJiantext",codemethod);
        int count = xgcollectinfoService.NowDateCount(Struts2Utils.getSessionUser().getName());
        request.setAttribute("NowCount",count);

        return "modulepage";
    }

    /**
     * 修改状态
     * @return
     * @throws Exception
     */
    public String updIsShow() throws  Exception{
        Integer id = Integer.parseInt(ids.trim());
        xgcollectinfo xgcollectinfo = xgcollectinfoService.findUniqueBy(id);
        xgcollectinfo.setIsshow(1);
        xgcollectinfo.setUpdatetime(Struts2Utils.getStringDate(new Date()));
        xgcollectinfo.setUpdatename(Struts2Utils.getSessionUser().getName());
        xgcollectinfoService.update(xgcollectinfo);
        return null;
    }

    @Override
    protected void prepareModel() throws Exception {
        entity = new xgcollectinfo();

    }

    @Override
    public xgcollectinfo getModel()  {
        return entity;
    }

    @Override
    public String modulepage() throws Exception {
        HttpServletRequest request= ServletActionContext.getRequest();
        xlist = xgcollectinfoService.find(Struts2Utils.getSessionUser().getName());
        request.setAttribute("xlist",xlist);
        int count = xgcollectinfoService.NowDateCount(Struts2Utils.getSessionUser().getName());
        request.setAttribute("NowCount",count);
        request.setAttribute("inputmsg","");  //录入时间
        request.setAttribute("inputAddress",entity.getInputAddress());
        request.setAttribute("ZengJiantext",codemethod);
        String fiveone = "";
        if(entity.getFiveone()!=null){
            fiveone = entity.getFiveone();
        }
        if(fiveone != ""){
//            String Samplenums = entity.getSamplenum();
            String qz = "";  //单独截取前缀
            for(int i=0;i < fiveone.length();i++) {
                if(Character.isDigit(fiveone.charAt(i)) == false){
                    qz+=fiveone.charAt(i);
                }
            }
            int qzlenth = 0;
            if(qz.length() != 0){  //获取前缀的长度
                qzlenth = qz.length();
            }
            String strsamplenum = fiveone.substring(qzlenth); //截去前缀 为：0001
            int samplenum = Integer.parseInt(strsamplenum);   // 转换序号 为：1
            if(codemethod.equals("减码") && samplenum-1 == 0){
                request.setAttribute("isSamplenum","合管编码已经不能再减了！");
            } else
            {
                String zeros = "";  // 0
                for(int i=0;i < strsamplenum.length();i++) {  //循环截去前缀之后的字符串
                    if(String.valueOf(strsamplenum.charAt(i)).equals("0")){   //判断该字符是否为0
                        zeros+=strsamplenum.charAt(i);
                    }else{
                        break;
                    }
                }
                String abc = "";  //增码或减码String.format之后的字符
                if(codemethod.equals("增码")){
                    if(zeros.equals("")){
                        samplenum+=1;
                    }else if(!zeros.equals("")){
                        samplenum+=1;
                        abc = String.format("%0"+strsamplenum.length()+"d",samplenum);
                    }
                }else{
                    if(zeros.equals("")){
                        samplenum-=1;
                    }else if(!zeros.equals("")){
                        samplenum-=1;
                        abc = String.format("%0"+strsamplenum.length()+"d",samplenum);
                    }
                }
                request.setAttribute("inputmsg","");
                if(zeros.equals("")){
                    request.setAttribute("ZengNum",qz+samplenum);
                }else if(!zeros.equals("")){
                    request.setAttribute("ZengNum",qz+abc);
                }
            }

        }
        return "modulepage";
    }
}
