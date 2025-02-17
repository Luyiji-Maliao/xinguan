package com.usci.norovirus.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.lims.core.orm.Page;
import com.lims.core.utils.excel.WriteExcel;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.entity.Norovirus;
import com.usci.norovirus.entity.XgBuKaiFaPiao;
import com.usci.norovirus.service.AppointmentinfoService;
import com.usci.norovirus.service.NorovirusService;
import com.usci.norovirus.service.XgBuKaiFaPiaoService;

@Component
@Scope("prototype")
@Results({
        @Result(name = "modulepage", location = "/WEB-INF/content/norovirus/bukaifapiao.jsp")
})
public class XgbukaifapiaoAction extends CrudActionSupport<XgBuKaiFaPiao> {
	

	@Autowired
	private XgBuKaiFaPiaoService xgBuKaiFaPiaoService;
	@Autowired
	private NorovirusService norovirusService;
	@Autowired
	private AppointmentinfoService appointmentinfoService;
	
	private Logger log = LoggerFactory.getLogger(XgbukaifapiaoAction.class);
	
	private String searchYuyuenum;
	private String searchName;
	private String searchSfz;
	private String itemsxjyb;
	private String ininputTime;
	private String osoinputTime;
	private String searchSampleNo;
	private String searchBukaistate;
	private XgBuKaiFaPiao entity;
	
	public String getSql(){
		String sql = "select bkfp.*,xgap.sampleNo,xgap.yuyuenum,xgap.outtradeno,xgap.name,xgap.sfz from xg_bukaifapiao bkfp LEFT JOIN xg_appointmentinfo xgap on bkfp.yuyueid = xgap.id where 1=1 ";
		if(ininputTime!=null&&ininputTime.length()>=10){
			ininputTime = ininputTime.substring(0, 10)+" 00:00:00";
			sql+=" and bkfp.inputtime>'"+ininputTime+"' ";
		}
		if(osoinputTime!=null&&osoinputTime.length()>=10){
			osoinputTime = osoinputTime.substring(0,10)+" 24:00:00";
			sql+=" and bkfp.inputtime<'"+osoinputTime+"' ";
		}
		if(searchBukaistate!=null&&!"".equals(searchBukaistate)){
			if("已补开".equals(searchBukaistate)){
				sql+=" and bkfp.bukaistate='已补开' ";
			}else if("未补开".equals(searchBukaistate)){
				sql+=" and (bkfp.bukaistate!='已补开' or bkfp.bukaistate is null) ";
			}
		}
		if(searchYuyuenum!=null&&!"".equals(searchYuyuenum)){
			sql+=" and xgap.yuyuenum like '%"+searchYuyuenum+"%' ";
		}
		if(searchName!=null&&!"".equals(searchName)){
			sql+=" and xgap.name like  '%"+searchName+"%' ";
		}
		if(searchSfz!=null&&!"".equals(searchSfz)){
			sql+=" and xgap.sfz like  '%"+searchSfz+"%' ";
		}
		if(searchSampleNo!=null&&!"".equals(searchSampleNo)){
			sql+=" and xgap.sampleNo like  '%"+searchSampleNo+"%' ";
		}
		
		sql+=" ORDER BY CASE WHEN bkfp.bukaistate like '%已补开%' then 1 WHEN bkfp.bukaistate like '%未补开%' THEN 2 ELSE 3 END DESC";
		return sql;
	}
	
	public String getSql2(){

		String sql = "SELECT xb.isfapiao, xb.shuihao, xb.fapiaotaitou, xb.zhucedizhi, xb.zhucedianhua, xb.kaihuyinhang, xb.yinhangzhanghao, xb.id, xb.fapiaotype, xb.accessname, xb.accessphone, xb.accessaddress, xb.accessemail, xb.yuyueid, xb.bukaistate, xb.bukainame, xb.bukaitime, xb.kaipiaodate, xb.kaipiaono, xb.updatename, xb.updatetime, xb.inputtime, xb.inputname, xap.sampleNo, xap.yuyuenum, xap.outtradeno, xap.totalfee, xc.cooperationcompanyname, xc.cooperationsubcompanyname, xc.businessmanager, xc.salename, IF(noro.inspection!='',noro.inspection,'优迅医学') inspection, IF(noro.idcard!='',noro.idcard,xap.sfz) sfz, IF(noro.sendingPerson!='',noro.sendingPerson,xap.sending) sendingPerson, IF(noro.`name`!='',noro.name,xap.name) name, if(noro.samplingDate!='',noro.samplingDate,xap.samplebindtime) samplebindtime, if(noro.receptionDate!='',noro.receptionDate,xap.daoyangdate) receptionDate, if(noro.checkProject!='',noro.checkProject,xap.checkProject)checkProject, if(xap.shicaiyangdian!='',xap.shicaiyangdian,xap.caiyangdidian) caiyangdidian, noro.reportdate reportDate FROM xinguan.xg_bukaifapiao xb LEFT JOIN xinguan.xg_appointmentinfo xap ON xb.yuyueid = xap.id LEFT JOIN xinguan.xg_cooperationinformation xc ON xap.cooperationstr = xc.cooperationstr LEFT JOIN uscilims.norovirus noro on noro.sampleNo = xap.sampleNo WHERE 1 = 1 ";
		List<XgBuKaiFaPiao> xgBuKaiFaPiaoList = Struts2Utils.conver(itemsxjyb, XgBuKaiFaPiao.class);
		if(xgBuKaiFaPiaoList.size()>0){
			sql+=" and xb.id in (";
			for(XgBuKaiFaPiao xgBuKaiFaPiao:xgBuKaiFaPiaoList){
				sql+="'"+xgBuKaiFaPiao.getId()+"',";
			}
			sql = sql.substring(0, sql.length()-1)+") ";
		}else{
			if(ininputTime!=null&&ininputTime.length()>=10){
				ininputTime = ininputTime.substring(0, 10)+" 00:00:00";
				sql+=" and xb.inputtime>'"+ininputTime+"' ";
			}
			if(osoinputTime!=null&&osoinputTime.length()>=10){
				osoinputTime = osoinputTime.substring(0,10)+" 24:00:00";
				sql+=" and xb.inputtime<'"+osoinputTime+"' ";
			}
			if(searchBukaistate!=null&&!"".equals(searchBukaistate)){
				if("已补开".equals(searchBukaistate)){
					sql+=" and xb.bukaistate='已补开' ";
				}else if("未补开".equals(searchBukaistate)){
					sql+=" and (xb.bukaistate!='已补开' or xb.bukaistate is null) ";
				}
			}
			if(searchYuyuenum!=null&&!"".equals(searchYuyuenum)){
				sql+=" and xap.yuyuenum like '%"+searchYuyuenum+"%' ";
			}
			if(searchName!=null&&!"".equals(searchName)){
				sql+=" and xap.name like  '%"+searchName+"%' ";
			}
			if(searchSfz!=null&&!"".equals(searchSfz)){
				sql+=" and xap.sfz like  '%"+searchSfz+"%' ";
			}
			if(searchSampleNo!=null&&!"".equals(searchSampleNo)){
				sql+=" and xap.sampleNo like  '%"+searchSampleNo+"%' ";
			}
		}
		
		return sql;
	}
	
	
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		String sql = getSql();
		Page<XgBuKaiFaPiao> xgBuKaiFaPiaoPage = new Page<XgBuKaiFaPiao>(limit);
		xgBuKaiFaPiaoPage.setPageNo((start / limit) + 1);
		xgBuKaiFaPiaoService.findPage(sql,xgBuKaiFaPiaoPage,start,limit);
		Struts2Utils.renderJson(xgBuKaiFaPiaoPage);
		
		
		return null;
	}
	
	public String querenbukai() throws HibernateException, SQLException{
		xgBuKaiFaPiaoService.querenbukai(entity);
		msg.setMsg("确认补开成功");
		Struts2Utils.renderJson(msg);
		return null;
	}
	
	public String caiwuExcel(){
		
		String sql = this.getSql2();
		List<XgBuKaiFaPiao> xgBuKaiFaPiaoList = xgBuKaiFaPiaoService.entityBySql(sql);
		
		//查询norovirus中的数据
		if(xgBuKaiFaPiaoList.size()>0){
			String uploadFileSavePath = ServletActionContext.getServletContext().getRealPath("/documents");
            xgBuKaiFaPiaoService.writeBuKaiCaiWuExcel(xgBuKaiFaPiaoList, uploadFileSavePath + "/xinguanbukaicaiwu.xls");
            msg.setMsg("EXCEL已导出");
            Struts2Utils.renderJson(msg);
		}else{
			msg.setMsg("无可导出数据");
			Struts2Utils.renderJson(msg);
		}
		
		return null;
	}
	
	public String checkBatchUpdate(){
		StringBuffer resultBuffer = new StringBuffer();
		StringBuffer noSampleNo = new StringBuffer();
		StringBuffer isBuKaiSampleNo = new StringBuffer();
		List<XgBuKaiFaPiao> xgBuKaiFaPiaoList = Struts2Utils.conver(itemsxjyb, XgBuKaiFaPiao.class);
		if(xgBuKaiFaPiaoList.size()>0){
			for(XgBuKaiFaPiao xgBuKaiFaPiao:xgBuKaiFaPiaoList){
				List<XgBuKaiFaPiao> xgBuKaiFaPiaoList1 = xgBuKaiFaPiaoService.findBySampleNo(xgBuKaiFaPiao.getSampleNo());
				if(xgBuKaiFaPiaoList1.size()>0){
					if("已补开".equals(xgBuKaiFaPiaoList1.get(0).getBukaistate())){
						isBuKaiSampleNo.append(xgBuKaiFaPiao.getSampleNo()+",");
					}
				}else{
					noSampleNo.append(xgBuKaiFaPiao.getSampleNo()+",");
				}
			}
			if(!"".equals(noSampleNo.toString())){
				resultBuffer.append("样本编号："+noSampleNo.toString().substring(0,noSampleNo.toString().length()-1)+"无补开发票信息，继续执行将跳过;");
			}
			if(!isBuKaiSampleNo.toString().equals("")){
				resultBuffer.append("样本编号："+isBuKaiSampleNo.toString().substring(0,isBuKaiSampleNo.toString().length()-1)+"已补开,确定覆盖原有数据吗;");
			}
		}
		if(resultBuffer.toString().equals("")){
			msg.setMsg("无异常信息");
		}else{
			msg.setMsg(resultBuffer.toString().substring(0, resultBuffer.toString().length()-1));
		}
		Struts2Utils.renderJson(msg);
		
		
		return null;
	}
	
	public String batchUpdate() throws HibernateException, SQLException{
		List<XgBuKaiFaPiao> xgBuKaiFaPiaoList = Struts2Utils.conver(itemsxjyb, XgBuKaiFaPiao.class);
		xgBuKaiFaPiaoService.batchQuerenbukai(xgBuKaiFaPiaoList);
		
		msg.setMsg("确认补开成功");
		Struts2Utils.renderJson(msg);
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		entity = new XgBuKaiFaPiao();
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XgBuKaiFaPiao getModel() {
		// TODO Auto-generated method stub
		
		return entity;
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
	
}
