package com.usci.norovirus.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.usci.norovirus.entity.Appointmentinfo;
import com.usci.norovirus.entity.Norovirus;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.orm.Page;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.norovirus.dao.XgBuKaiFaPiaoDao;
import com.usci.norovirus.entity.XgBuKaiFaPiao;

@Component
@Transactional(readOnly=true)
public class XgBuKaiFaPiaoService {

	@Autowired
	XgBuKaiFaPiaoDao xgBuKaiFaPiaoDao;
	@Autowired
	private AppointmentinfoService appointmentinfoService;
	private static Logger log = LoggerFactory.getLogger(XgBuKaiFaPiaoService.class);
	
	public void findPage(String sql, Page<XgBuKaiFaPiao> xgBuKaiFaPiaoPage,
			int start, int limit) {
		// TODO Auto-generated method stub
		List<XgBuKaiFaPiao> xgBukaiFaPiaoAllList = xgBuKaiFaPiaoDao.entityBySql(sql, XgBuKaiFaPiao.class);
		sql+=" limit "+start+","+limit;
		List<XgBuKaiFaPiao> xgBuKaiFaPiaoLimitList = xgBuKaiFaPiaoDao.entityBySql(sql, XgBuKaiFaPiao.class);
		xgBuKaiFaPiaoPage.setTotalCount(Long.parseLong(xgBukaiFaPiaoAllList.size()+""));
		xgBuKaiFaPiaoPage.setResult(xgBuKaiFaPiaoLimitList);
		
		
		
	}

	public XgBuKaiFaPiao findByYuyuenum(String sql) {
		List<XgBuKaiFaPiao> lcs=xgBuKaiFaPiaoDao.entityBySql(sql, XgBuKaiFaPiao.class);
		if(lcs!=null && lcs.size()>0){
			return lcs.get(0);
		}
		return null;
	}

	@Transactional(transactionManager = "sqltransactionManager")
	public void update(XgBuKaiFaPiao xgBuKaiFaPiao) {
		// TODO Auto-generated method stub
		xgBuKaiFaPiao.setBukainame(Struts2Utils.getSessionUser().getName());
		xgBuKaiFaPiao.setBukaitime(Struts2Utils.getStringDate(new Date()));
		xgBuKaiFaPiaoDao.merge(xgBuKaiFaPiao);
	}
	@Transactional(transactionManager = "sqltransactionManager")
	public void querenbukai(XgBuKaiFaPiao entity) throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		String sql = "update xg_bukaifapiao set bukaistate='已补开',kaipiaodate='"+entity.getKaipiaodate()+"',kaipiaono='"+entity.getKaipiaono()+"',bukainame='"+Struts2Utils.getUser().getName()+"',bukaitime='"+Struts2Utils.getStringDate(new Date())+"' ,updatename='"+Struts2Utils.getUser().getName()+"',updatetime='"+Struts2Utils.getStringDate(new Date())+"' where id='"+entity.getId()+"'";
		xgBuKaiFaPiaoDao.updateBySql(sql);
	}

	@Transactional(transactionManager = "sqltransactionManager")
	public void batchQuerenbukai(List<XgBuKaiFaPiao> xgBuKaiFaPiaoList) throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		for(XgBuKaiFaPiao xgBuKaiFaPiao:xgBuKaiFaPiaoList){
			String sql = "select id,sampleNo from xg_appointmentinfo where sampleNo='"+xgBuKaiFaPiao.getSampleNo()+"'";
			Appointmentinfo appointmentinfo = appointmentinfoService.findByYuyuenum(sql);
			if(appointmentinfo!=null){
				String sql1 = "update xg_bukaifapiao set bukaistate='已补开',kaipiaodate='"+xgBuKaiFaPiao.getKaipiaodate()+"',kaipiaono='"+xgBuKaiFaPiao.getKaipiaono()+"',bukainame='"+Struts2Utils.getUser().getName()+"',bukaitime='"+Struts2Utils.getStringDate(new Date())+"' ,updatename='"+Struts2Utils.getUser().getName()+"',updatetime='"+Struts2Utils.getStringDate(new Date())+"' where yuyueid='"+appointmentinfo.getId()+"'";
				xgBuKaiFaPiaoDao.updateBySql(sql1);
			}
		}
	}
	
	/**
	 *    新冠补开发票    财务导出Excel
	 * **/
	public static void writeBuKaiCaiWuExcel(List<XgBuKaiFaPiao> ln, String path) {
		//工作簿
		HSSFWorkbook hssfworkbook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet hssfsheet = hssfworkbook.createSheet();
		//sheet名称乱码处理
		hssfworkbook.setSheetName(0,"新型冠状补开发票财务信息");
		//取得第 i 行
		HSSFRow hssfrow1 = hssfsheet.createRow(0);
		HSSFCell hssfcell_00 = hssfrow1.createCell(0);
		hssfcell_00.setCellValue("ID");
		hssfsheet.setColumnWidth(0, 12*256);
		HSSFCell hssfcell_001 = hssfrow1.createCell(1);
		hssfcell_001.setCellValue("样本编号");
		hssfsheet.setColumnWidth(1, 20*256);
		HSSFCell hssfcell_002 = hssfrow1.createCell(2);
		hssfcell_002.setCellValue("送检单位");
		hssfsheet.setColumnWidth(2, 20*256);
		HSSFCell hssfcell_003 = hssfrow1.createCell(3);
		hssfcell_003.setCellValue("身份证号");
		hssfsheet.setColumnWidth(3, 30*256);
		HSSFCell hssfcell_004 = hssfrow1.createCell(4);
		hssfcell_004.setCellValue("送检人单位");
		hssfsheet.setColumnWidth(4, 30*256);
		HSSFCell hssfcell_005 = hssfrow1.createCell(5);
		hssfcell_005.setCellValue("姓名");
		hssfsheet.setColumnWidth(5, 12*256);
		HSSFCell hssfcell_006 = hssfrow1.createCell(6);
		hssfcell_006.setCellValue("预约号");
		hssfsheet.setColumnWidth(6, 15*256);
		HSSFCell hssfcell_007 = hssfrow1.createCell(7);
		hssfcell_007.setCellValue("采样时间");
		hssfsheet.setColumnWidth(7, 18*256);
		HSSFCell hssfcell_008 = hssfrow1.createCell(8);
		hssfcell_008.setCellValue("接收日期");
		hssfsheet.setColumnWidth(8, 14*256);
		HSSFCell hssfcell_009 = hssfrow1.createCell(9);
		hssfcell_009.setCellValue("检测项目");
		hssfsheet.setColumnWidth(9, 14*256);
		HSSFCell hssfcell_0010 = hssfrow1.createCell(10);
		hssfcell_0010.setCellValue("采样单位");
		hssfsheet.setColumnWidth(10, 16*256);
		HSSFCell hssfcell_0011 = hssfrow1.createCell(11);
		hssfcell_0011.setCellValue("报告日期");
		hssfsheet.setColumnWidth(11, 16*256);
		HSSFCell hssfcell_0012 = hssfrow1.createCell(12);
		hssfcell_0012.setCellValue("商户单号");
		hssfsheet.setColumnWidth(12, 30*256);
		HSSFCell hssfcell_0013 = hssfrow1.createCell(13);
		hssfcell_0013.setCellValue("付款金额");
		hssfsheet.setColumnWidth(13, 25*256);
		HSSFCell hssfcell_0014 = hssfrow1.createCell(14);
		hssfcell_0014.setCellValue("是否要发票");
		hssfsheet.setColumnWidth(14, 16*256);
		HSSFCell hssfcell_0015 = hssfrow1.createCell(15);
		hssfcell_0015.setCellValue("发票类型");
		hssfsheet.setColumnWidth(15, 16*256);
		HSSFCell hssfcell_0016 = hssfrow1.createCell(16);
		hssfcell_0016.setCellValue("发票邮箱");
		hssfsheet.setColumnWidth(16, 16*256);
		HSSFCell hssfcell_0017 = hssfrow1.createCell(17);
		hssfcell_0017.setCellValue("发票抬头");
		hssfsheet.setColumnWidth(17, 16*256);
		HSSFCell hssfcell_0018 = hssfrow1.createCell(18);
		hssfcell_0018.setCellValue("税号");
		hssfsheet.setColumnWidth(18, 12*256);
		HSSFCell hssfcell_0019 = hssfrow1.createCell(19);
		hssfcell_0019.setCellValue("预约采样点");
		hssfsheet.setColumnWidth(19, 16*256);
		HSSFCell hssfcell_0020 = hssfrow1.createCell(20);
		hssfcell_0020.setCellValue("合作机构");
		hssfsheet.setColumnWidth(20, 16*256);
		HSSFCell hssfcell_0021 = hssfrow1.createCell(21);
		hssfcell_0021.setCellValue("分支机构");
		hssfsheet.setColumnWidth(21, 16*256);
		HSSFCell hssfcell_0022 = hssfrow1.createCell(22);
		hssfcell_0022.setCellValue("业务员");
		hssfsheet.setColumnWidth(22, 16*256);
		HSSFCell hssfcell_0023 = hssfrow1.createCell(23);
		hssfcell_0023.setCellValue("销售姓名");
		hssfsheet.setColumnWidth(23, 16*256);
		HSSFCell hssfcell_0024 = hssfrow1.createCell(24);
		hssfcell_0024.setCellValue("补开状态");
		hssfsheet.setColumnWidth(24, 16*256);
		HSSFCell hssfcell_0025 = hssfrow1.createCell(25);
		hssfcell_0025.setCellValue("开票时间");
		hssfsheet.setColumnWidth(25, 16*256);
		HSSFCell hssfcell_0026 = hssfrow1.createCell(26);
		hssfcell_0026.setCellValue("开票号");
		hssfsheet.setColumnWidth(26, 16*256);

		for (int i = 0; i < ln.size(); i++) {
			//取得第 i 行
			HSSFRow hssfrow = hssfsheet.createRow(i+1);
			HSSFCell hssfcell_0 = hssfrow.createCell(0);
			hssfcell_0.setCellValue(ln.get(i).getId());
			HSSFCell hssfcell_1 = hssfrow.createCell(1);
			hssfcell_1.setCellValue(ln.get(i).getSampleNo());
			

			HSSFCell hssfcell_2 = hssfrow.createCell(2);
			hssfcell_2.setCellValue(ln.get(i).getInspection());
			HSSFCell hssfcell_3 = hssfrow.createCell(3);
			hssfcell_3.setCellValue(ln.get(i).getSfz());
			HSSFCell hssfcell_4 = hssfrow.createCell(4);
			hssfcell_4.setCellValue(ln.get(i).getSendingPerson());
			HSSFCell hssfcell_5 = hssfrow.createCell(5);
			hssfcell_5.setCellValue(ln.get(i).getName());
			HSSFCell hssfcell_6 = hssfrow.createCell(6);
			hssfcell_6.setCellValue(ln.get(i).getYuyuenum());
			HSSFCell hssfcell_7 = hssfrow.createCell(7);
			hssfcell_7.setCellValue(ln.get(i).getSamplebindtime());
			HSSFCell hssfcell_8 = hssfrow.createCell(8);
			hssfcell_8.setCellValue(ln.get(i).getReceptionDate());
			HSSFCell hssfcell_9 = hssfrow.createCell(9);
			hssfcell_9.setCellValue(ln.get(i).getCheckProject());
			HSSFCell hssfcell_10 = hssfrow.createCell(10);
			hssfcell_10.setCellValue(ln.get(i).getCaiyangdidian());
			HSSFCell hssfcell_11 = hssfrow.createCell(11);
			hssfcell_11.setCellValue(ln.get(i).getReportDate());

			HSSFCell hssfcell_12 = hssfrow.createCell(12);
			hssfcell_12.setCellValue(ln.get(i).getOuttradeno());
			HSSFCell hssfcell_13 = hssfrow.createCell(13);
			hssfcell_13.setCellValue(ln.get(i).getTotalfee());
			HSSFCell hssfcell_14 = hssfrow.createCell(14);
			hssfcell_14.setCellValue(ln.get(i).getIsfapiao());
			HSSFCell hssfcell_15 = hssfrow.createCell(15);
			hssfcell_15.setCellValue(ln.get(i).getFapiaotype());
			HSSFCell hssfcell_16 = hssfrow.createCell(16);
			hssfcell_16.setCellValue(ln.get(i).getAccessemail());
			HSSFCell hssfcell_17 = hssfrow.createCell(17);
			hssfcell_17.setCellValue(ln.get(i).getFapiaotaitou());
			HSSFCell hssfcell_18 = hssfrow.createCell(18);
			hssfcell_18.setCellValue(ln.get(i).getShuihao());
			HSSFCell hssfcell_19 = hssfrow.createCell(19);
			hssfcell_19.setCellValue(ln.get(i).getCaiyangdidian());
			HSSFCell hssfcell_20 = hssfrow.createCell(20);
			hssfcell_20.setCellValue(ln.get(i).getCooperationcompanyname());
			HSSFCell hssfcell_21 = hssfrow.createCell(21);
			hssfcell_21.setCellValue(ln.get(i).getCooperationsubcompanyname());
			HSSFCell hssfcell_22 = hssfrow.createCell(22);
			hssfcell_22.setCellValue(ln.get(i).getBusinessmanager());
			HSSFCell hssfcell_23 = hssfrow.createCell(23);
			hssfcell_23.setCellValue(ln.get(i).getSalename());
			HSSFCell hssfcell_24 = hssfrow.createCell(24);
			hssfcell_24.setCellValue(ln.get(i).getBukaistate());
			HSSFCell hssfcell_25 = hssfrow.createCell(25);
			hssfcell_25.setCellValue(ln.get(i).getKaipiaodate());
			HSSFCell hssfcell_26 = hssfrow.createCell(26);
			hssfcell_26.setCellValue(ln.get(i).getKaipiaono());
		}
		FileOutputStream fileoutputstream;
		try {
			fileoutputstream = new FileOutputStream(path);
			hssfworkbook.write(fileoutputstream);
			fileoutputstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("error:"+e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("error:"+e);
		}
	
	}

	public List<XgBuKaiFaPiao> FilterByTime(String ininputTime,
			String osoinputTime) {
		// TODO Auto-generated method stub
		String sql = "select * from xg_bukaifapiao where 1=1 ";
		if(ininputTime!=null&&!"".equals(ininputTime)){
			sql+=" and bukaitime>'"+ininputTime+"' ";
		}
		if(osoinputTime!=null&&!"".equals(osoinputTime)){
			sql+=" and bukaitime<'"+osoinputTime+"' ";
		}
		return xgBuKaiFaPiaoDao.entityBySql(sql, XgBuKaiFaPiao.class);
		
	}

	public List<XgBuKaiFaPiao> entityBySql(String sql) {
		// TODO Auto-generated method stub
		return xgBuKaiFaPiaoDao.entityBySql(sql, XgBuKaiFaPiao.class);
	}

	public List<XgBuKaiFaPiao> findBySampleNo(String sampleNo) {
		// TODO Auto-generated method stub
		String sql = "SELECT xb.id, xb.isfapiao, xb.shuihao, xb.fapiaotaitou, xb.zhucedizhi, xb.zhucedianhua, xb.kaihuyinhang, xb.yinhangzhanghao, xb.fapiaotype, xb.accessname, xb.accessphone, xb.accessaddress, xb.accessemail, xb.yuyueid, xb.bukaistate, xb.bukainame, xb.bukaitime, xb.kaipiaodate, xb.kaipiaono, xb.updatename, xb.updatetime FROM xg_bukaifapiao xb LEFT JOIN xg_appointmentinfo xa on xb.yuyueid=xa.id where xa.sampleNo='"+sampleNo+"' ";
		return entityBySql(sql);
		
	}

	
	
	
	
	
}
