package com.usci.tool.url.service;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.utils.web.Struts2Utils;
import com.ndktools.javamd5.Mademd5;
import com.usci.tool.url.entity.InterfaceUrl;




@Service
@Transactional(readOnly=true)
public class InterfaceUrlUtilService {
    
    private static final Logger log = LoggerFactory.getLogger(InterfaceUrlUtilService.class);
  
    @Autowired
    private InterfaceUrlService interfaceUrlService;
    /**
     * 获取接口，数据库控制正式与测试接口的正常使用
     * @return
     */
    private String getUrl(String iuProperty){
    	String opertorName=Struts2Utils.getUser()!=null?Struts2Utils.getUser().getName():"";
    	InterfaceUrl entity=new InterfaceUrl();
    	//控制接口的使用状态
    	entity.setDataState(0);
    	entity.setIuProperty(iuProperty);
		 
		 String url="";
    	List<InterfaceUrl> urls=interfaceUrlService.selectEntity(entity);
    	if(1==urls.size()){
			log.info("接口工具,获取接口成功：操作人：{},iuProperty：{},",opertorName,iuProperty);
			url=urls.get(0).getInterfaceUrl();
    	}else{
			log.warn("接口工具，组合不唯一返回空：操作人：{},iuProperty：{}",opertorName,iuProperty);
    	}
    	return url;
    }
    /**
	 * 产前报告url
	 * @return
	 */
	public String cqurl(){
		
		return getUrl("InterfaceUrlUtilService_cqurl");
	}

	/**
	 * 产前客服补录的同步数据接口url
	 * @return
	 */
	public String cqrgiurl(){
		return getUrl("InterfaceUrlUtilService_cqrgiurl");
	}

	/**
	 * 产前客服补录的同步数据接口url 重庆医院接口更新调整
	 * @return
	 */
	public String cqrgiurl2(){
		return getUrl("InterfaceUrlUtilService_cqrgiurl2");
	}
	
	/**
	 * 产前报告-手机短信的方法
	 * */
	public int reportIssueSendSms(String mobil,String template){
		return Struts2Utils.sendSMS(getUrl("InterfaceUrlUtilService_reportIssueSendSms"),mobil,template, Struts2Utils.getStringDatehmss(new Date()));
	}/**
	 * 客服回访url
	 * @return
	 */
	public URL kfurl(String number){
		URL url = null;
		Mademd5 md=new Mademd5();
		try {
			//url = new URL("http://400.ccic2.com/interface/PreviewOutcall?enterpriseId=3003252&hotline=&cno=2000&pwd="+md.toMd5("usci@8137").toLowerCase()+"&customerNumber="+number);
			url = new URL(getUrl("InterfaceUrlUtilService_kfurl")+"?enterpriseId=3003252&hotline=&cno=2000&pwd="+md.toMd5("usci@8137").toLowerCase()+"&customerNumber="+"18600900461");
		} catch (MalformedURLException e) {
			log.error("客服回访url：\n异常信息：", e);
		}
		return url;
	}
	
	/**
	 * 肿瘤-优替报告url
	 * @return
	 */
	public String yturl(){
		return getUrl("InterfaceUrlUtilService_yturl");
	}
	/**
	 * 上机拆分URL
	 * **/
	public String biologicalComputerURL(){
		return getUrl("InterfaceUrlUtilService_biologicalComputerURL");
	}
	
	/**
	 * 上机拆分URL--BGI
	 * **/
	public String biologicalComputerURLBGI(){
		return getUrl("InterfaceUrlUtilService_biologicalComputerURLBGI");
	}

	/**
	 * 阿尔兹海默报告URL
	 * **/
	public String adReport(){
		return getUrl("InterfaceUrlUtilService_adReport");
		
	}
	
	/**
	 * 优馨益报告图片URL
	 * **/
	public String yxyImgURL(){
		return getUrl("InterfaceUrlUtilService_yxyImgURL");
	}
	
	/**
	 * 体检礼品卡上传报告时推送微信
	 * **/
	public String giftcardURL(){
		
		return getUrl("InterfaceUrlUtilService_giftcardURL");
	}
	
	/**
	 * 优逸小癌种上传报告时推送微信
	 * **/
	public String giftYYCancerURL(){
		
		return getUrl("InterfaceUrlUtilService_giftYYCancerURL");
	}
	/**
	 * 心源性猝死批量添加时推送微信
	 * **/
	public String elephantURL(){		
		return getUrl("InterfaceUrlUtilService_elephantURL");
	}
	/**
	 * 小象基因上传报告时推送微信
	 * **/
	public String elephantURL1(){		
		return getUrl("InterfaceUrlUtilService_elephantURL1");
		}
	/**
	 * 优逸分析结果反馈（解读完毕）推送微信
	 */
	public String analyseURL(){
		return getUrl("InterfaceUrlUtilService_analyseURL");
	}
	/**
	 * nipt，niptplus,优馨益结果反馈页面查看图片的路径
	 * @return
	 */
	public String getPath(){
		return getUrl("InterfaceUrlUtilService_getPath");
	}
    /**
     * PHP推送消息
     */
	public String pushPhpMessage(){
		return getUrl("InterfaceUrlUtilService_pushPhpMessage");
	}
	 /**
     * PHP推送消息--主要用于向 客户 推送消息
     */
	public String pushPhpMessage2(){
		return getUrl("InterfaceUrlUtilService_pushPhpMessage2");
	}
	 /**
     * PHP推送消息-- 向销售推送无详情的消息
     */
	public String pushPhpMessage3(){
		return getUrl("InterfaceUrlUtilService_pushPhpMessage3");
	}
	/**
	 * 重庆市妇幼报告模板报告内部审核完成后推送重庆市妇幼医院系统url
	 * 正式：http://bk.cqrgi.com/external/receiveReport 
	 * 测试：http://uatbk.cqrgi.com/external/receiveReport 
	 */
	public String getCqfyReviewReportPath() {
		// TODO Auto-generated method stub
		return getUrl("InterfaceUrlUtilService_getCqfyReviewReportPath");
	}
}
