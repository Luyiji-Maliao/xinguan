package com.usci.wxbukaifapiao.action;

import com.github.wxpay.sdk.WXPay;




import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.wxbukaifapiao.com.github.wxpay.sdk.MyConfig;
import com.usci.wxbukaifapiao.utils.ValidateCodeUtils;
import com.usci.norovirus.action.XgbukaifapiaoAction;
import com.usci.system.service.SendUrlService;
import com.usci.wxbukaifapiao.entity.Bkfp;
import com.usci.wxbukaifapiao.service.BkfpService;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs( { @InterceptorRef("exceptionstack") })
/**
 * 微信补开发票
 * @author 薛思军
 *
 */
@Results({
        @Result(name = "modulepage", location = "/WEB-INF/content/bkfp/bkfp.jsp")
})
public class BkfpAction extends CrudActionSupport<Bkfp> {
    @Autowired
    private BkfpService bkfpService;
    @Autowired
    private SendUrlService sendUrlService;//手机短信

    private Logger log = LoggerFactory.getLogger(XgbukaifapiaoAction.class);

    private String searchYuyuenum;
    private String searchName;
    private String searchSfz;
    private String itemsxjyb;
    private String ininputTime;
    private String osoinputTime;
    private String searchSampleNo;
    private String searchBukaistate;
    private Bkfp entity;
    private String validate;



    private String telephone;




    HttpServletRequest request = ServletActionContext.getRequest();

    HttpSession session = request.getSession();


    @Override
    protected void prepareModel() throws Exception {
        // TODO Auto-generated method stub
        entity = new Bkfp();
    }




    @Override
    public String save() throws Exception {
        msg.setO("否");
        entity.setAccessphone(telephone);
        //String phone = entity.getAccessphone();
        // TODO Auto-generated method stub
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", entity.getOuttradeno());



        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            if (resp.get("err_code_des")!=null){
                data.put("transaction_id", entity.getOuttradeno());
                resp = wxpay.orderQuery(data);
            }



            //商户单号在微信不为空
            if (entity.getOuttradeno().equals("")||entity.getOuttradeno()==null){
                msg.setMsg("商户单号不能为空");
                Struts2Utils.renderJson(msg);
                return null;
            }
            if (resp.get("err_code_des")!=null ||resp.get("transaction_id")==null){

                msg.setMsg("商户单号有误");
                Struts2Utils.renderJson(msg);
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }


//        List<Bkfp> fapiaotaitou = bkfpService.selectFapiaotaitou(entity.getFapiaotaitou());
//        if (fapiaotaitou!=null&&fapiaotaitou.size()!=0){
//            msg.setMsg("已补开过发票,不能重复补开");
//            Struts2Utils.renderJson(msg);
//            return null;
//        }
        if (entity.getOuttradeno()==null){
            msg.setMsg("商户单号不能为空");
            Struts2Utils.renderJson(msg);
            return null;
        }
        if (entity.getAccessphone()==null){
            msg.setMsg("手机号不能为空");
            Struts2Utils.renderJson(msg);
            return null;
        }

        if (entity.getFapiaotype().equals("1")){
            entity.setFapiaotype("电子发票");
        }else {
            msg.setMsg("请勾选发票类型");
            Struts2Utils.renderJson(msg);
            return null;
        }
        if (!session.getAttribute("phone").equals(validate)){
            msg.setMsg("验证码错误,请重新输入");
            Struts2Utils.renderJson(msg);
            return null;
        }
        if (entity.getSamplenum() != null) {
            entity.setSamplenum(null);
        }
        if (entity.getIsfapiao().equals("1")){
            if (entity.getFapiaotaitou()==null&&entity.getShuihao()==null && entity.getZhucedizhi()==null && entity.getZhucedianhua()==null && entity.getKaihuyinhang()==null && entity.getYinhangzhanghao()==null&&entity.getAccessemail()==null&&entity.getFapiaotaitou()==null && entity.getAccessemail()==null){
                msg.setMsg("请填写开票信息");
                Struts2Utils.renderJson(msg);
                return null;
            }else if (entity.getFapiaotaitou()==null || entity.getAccessemail()==null ||entity.getAccessemail().equals("")){

                msg.setMsg("请填写发票抬头或邮箱");
                Struts2Utils.renderJson(msg);
                return null;
            }
            else if (entity.getShuihao()==null && entity.getZhucedizhi()==null && entity.getZhucedianhua()==null && entity.getKaihuyinhang()==null && entity.getYinhangzhanghao()==null){
                entity.setIsfapiao("个人发票");
                bkfpService.save(entity);
                msg.setO("是");
                msg.setMsg("开票成功");
                Struts2Utils.renderJson(msg);

                return null;
            }else {
                entity.setIsfapiao("单位发票");
                bkfpService.save(entity);
                msg.setO("是");
                msg.setMsg("开票成功");
                Struts2Utils.renderJson(msg);
                return null;
            }


        }


        return null;
    }

    public String send4Order(){
        entity.setAccessphone(telephone);
        String phone =entity.getAccessphone();
        //为用户随机生成一个6位验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(6).toString();

        System.out.println(validateCode+phone);
        //ValidateCode yzm = new ValidateCode(validateCode);
        //调用短信服务为用户发送验证码
        try {
            String smsTemplate ="尊敬的客户,您的验证码是:  "+validateCode+" 有效时间30分钟【优迅医学】";
            //发验证码
            //sendUrlService.reportIssueSendSms(phone, smsTemplate);
            session.setAttribute("phone", validateCode);
            session.setMaxInactiveInterval(1800);
        } catch (Exception e) {
            e.printStackTrace();
            //短信发送失败
            return "发送失败";
        }

        //保存验证码到数据库

        return "发送成功";
    }

    @Override
    public Bkfp getModel() {
        // TODO Auto-generated method stub

        return entity;
    }

    @Override
    public String list() throws Exception {
        return null;
    }

    @Override
    public String modulepage() throws Exception {
        // TODO Auto-generated method stub
        return "modulepage";
    }




    public String getSearchYuyuenum() {
        return searchYuyuenum;
    }

    public void setSearchYuyuenum(String searchYuyuenum) {
        this.searchYuyuenum = searchYuyuenum;
    }


    public String getItemsxjyb() {
        return itemsxjyb;
    }


    public void setItemsxjyb(String itemsxjyb) {
        this.itemsxjyb = itemsxjyb;
    }


    public String getSearchName() {
        return searchName;
    }


    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }


    public String getSearchSfz() {
        return searchSfz;
    }


    public void setSearchSfz(String searchSfz) {
        this.searchSfz = searchSfz;
    }


    public String getIninputTime() {
        return ininputTime;
    }


    public void setIninputTime(String ininputTime) {
        this.ininputTime = ininputTime;
    }


    public String getOsoinputTime() {
        return osoinputTime;
    }


    public void setOsoinputTime(String osoinputTime) {
        this.osoinputTime = osoinputTime;
    }

    public String getSearchSampleNo() {
        return searchSampleNo;
    }

    public void setSearchSampleNo(String searchSampleNo) {
        this.searchSampleNo = searchSampleNo;
    }

    public String getSearchBukaistate() {
        return searchBukaistate;
    }

    public void setSearchBukaistate(String searchBukaistate) {
        this.searchBukaistate = searchBukaistate;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


}
