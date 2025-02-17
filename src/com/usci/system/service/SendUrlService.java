package com.usci.system.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.usci.tool.url.service.InterfaceUrlUtilService;

@Component
@Transactional(readOnly = true)
public class SendUrlService {

	@Autowired
	private InterfaceUrlUtilService interfaceUrlUtilService;

	/**
	 * 产前报告url
	 * 
	 * @return
	 */
	public String cqurl() {
		return interfaceUrlUtilService.cqurl();
	}

	/**
	 * 产前客服补录的同步数据接口url
	 * 
	 * @return
	 */
	public String cqrgiurl() {
		return interfaceUrlUtilService.cqrgiurl();
	}

	/**
	 * 产前客服补录的同步数据接口url 重庆医院接口更新调整
	 * 
	 * @return
	 */
	public String cqrgiurl2() {
		return interfaceUrlUtilService.cqrgiurl2();
	}

	/**
	 * 产前报告-手机短信的方法
	 * */
	public int reportIssueSendSms(String mobil, String template) {
		return interfaceUrlUtilService.reportIssueSendSms(mobil, template);
	}

	/**
	 * 客服回访url
	 * 
	 * @return
	 */
	public URL kfurl(String number) {
		return interfaceUrlUtilService.kfurl(number);
	}

	/**
	 * 肿瘤-优替报告url
	 * 
	 * @return
	 */
	public String yturl() {
		return interfaceUrlUtilService.yturl();
	}

	/**
	 * 上机拆分URL
	 * **/
	public String biologicalComputerURL() {
		return interfaceUrlUtilService.biologicalComputerURL();
	}

	/**
	 * 上机拆分URL--BGI
	 * **/
	public String biologicalComputerURLBGI() {
		return interfaceUrlUtilService.biologicalComputerURLBGI();
	}

	/**
	 * 阿尔兹海默报告URL
	 * **/
	public String adReport() {
		return interfaceUrlUtilService.adReport();
	}

	/**
	 * 优馨益报告图片URL
	 * **/
	public String yxyImgURL() {
		return interfaceUrlUtilService.yxyImgURL();
	}

	/**
	 * 体检礼品卡上传报告时推送微信
	 * **/
	public String giftcardURL() {

		return interfaceUrlUtilService.giftcardURL();
	}

	/**
	 * 优逸小癌种上传报告时推送微信
	 * **/
	public String giftYYCancerURL() {

		return interfaceUrlUtilService.giftYYCancerURL();
	}

	/**
	 * 心源性猝死批量添加时推送微信
	 * **/
	public String elephantURL() {
		return interfaceUrlUtilService.elephantURL();
	}

	/**
	 * 小象基因上传报告时推送微信
	 * **/
	public String elephantURL1() {
		return interfaceUrlUtilService.elephantURL1();
	}

	/**
	 * 优逸分析结果反馈（解读完毕）推送微信
	 */
	public String analyseURL() {
		return interfaceUrlUtilService.analyseURL();
	}

	/**
	 * nipt，niptplus,优馨益结果反馈页面查看图片的路径
	 * 
	 * @return
	 */
	public String getPath() {
		return interfaceUrlUtilService.getPath();
	}
	
	/**
	 * 重庆市妇幼报告模板 报告内部审核完成后推送医院系统
	 */
	public String cqfyshenheURL(){
		return interfaceUrlUtilService.getCqfyReviewReportPath();
	}
}
