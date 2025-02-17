package com.lims.core.utils.excel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
//  poi-bin-2.5.1-final-20040804.zip
import com.usci.norovirus.entity.Appointmentinfo;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Norovirus;
import com.usci.norovirus.entity.XgBuKaiFaPiao;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.struts2.ServletActionContext;
import com.lims.core.utils.web.Struts2Utils;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 写入Excel
 * @author lenovo
 *2015-12-21
 */
public class WriteExcel {

	private static final Logger log=LoggerFactory.getLogger(WriteExcel.class);
 

 
 
 





	/***
	 * 生成Excel方法
	 * @param  excelName    Excel名称
	 * @param  sheetName    sheet页名称
	 * @param  selectIndex  下拉框下标
	 * @param  selectArray  下拉框的值
	 * @param  titleArray   第一行单元格的列  二维数组 内层数组为  实体字段(String),显示的内容(String),宽度{int}
	 * @param  valueList    单元格的值
	 * @param  path    目标路径
	 * @throws Exception 
	 * */
	public static String creatExcel(String excelName, String sheetName, int[] selectIndex, List<String[]> selectArray, Object[][]titleArray, List valueList,String path)  {
		int count = 0 ;//是否可以生成Excel
		for (int i = 0; i < titleArray.length; i++) {
			if(!(titleArray[i].length>=2&&titleArray[i].length<=3)){
				count++;
			}
		}
		if(count==0){
			FileOutputStream fileoutputstream=null;
			//工作簿
			try {
				HSSFWorkbook hssfworkbook = new HSSFWorkbook();
				//创建sheet页
				hssfworkbook = WriteExcel.createSheet(sheetName,hssfworkbook,selectIndex,selectArray, titleArray, valueList);
				if(path==null||path.equals("")){
					path=ServletActionContext.getServletContext().getRealPath("/documents")+"/"+excelName+".xls";
				}
				
				fileoutputstream = new FileOutputStream(path);
				hssfworkbook.write(fileoutputstream);
				
			} catch (Exception e) {
				log.error("Excel导出异常",e);
				
			}finally{
				if(fileoutputstream!=null){
					try{
						fileoutputstream.close();
					}catch(IOException io){
						log.error("IO关闭异常",io);
					}
				}
				
			}
			return "EXCEL已导出";
		}else{
			return "导出出错,请联系管理员";
		}
	}
	  
	public  static  HSSFWorkbook createSheet(String sheetName,HSSFWorkbook hssfworkbook,int[] selectIndex, List<String[]> selectArray, Object[][]titleArray, List valueList) throws Exception, Exception{
		HSSFSheet hssfsheet = hssfworkbook.createSheet();
		//sheet名称乱码处理
		hssfworkbook.setSheetName(0, sheetName);

		//创建第一行
		HSSFRow hssfrow = hssfsheet.createRow(0);
		for (int k = 0; k < titleArray.length; k++) {
			//第一行表头
			HSSFCell hssfcell = hssfrow.createCell( k);
			hssfcell.setCellValue(titleArray[k][1].toString());
			//定义宽度
			if(titleArray[k].length==3){
				hssfsheet.setColumnWidth(k, Integer.parseInt(titleArray[k][2].toString()) * 256);
			}
			//是否有下拉框
			if (selectIndex != null && selectIndex.length > 0 && selectArray != null && selectArray.size() > 0 && selectIndex.length == selectArray.size()) {
				for (int j = 0; j < selectIndex.length; j++) {
					CellRangeAddressList regions = new CellRangeAddressList(1, 1000, selectIndex[j], selectIndex[j]);
					DVConstraint constraint = DVConstraint.createExplicitListConstraint(selectArray.get(j));
					//绑定下拉框和作用区域
					HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
					//对sheet页生效
					hssfsheet.addValidationData(data_validation);
				}
			}
		}

		//遍历数据行
		if(valueList!=null&&valueList.size()>0){
			for (int i = 1; i <= valueList.size(); i++) {
				HSSFRow hssfrownew = hssfsheet.createRow(i);
				//遍历列
				for (int k = 0; k < titleArray.length; k++) {
					String value = "";   //单元格的值
					Field fields = valueList.get(i - 1).getClass().getDeclaredField(titleArray[k][0].toString());
					if (fields != null) {
						fields.setAccessible(true);							
						if(fields.get(valueList.get(i - 1))!=null &&!"".equals(fields.get(valueList.get(i - 1)))){
							value = fields.get(valueList.get(i - 1)).toString();
						}else{
							value = "";
						}							
					}
					HSSFCell hssfcellnew = hssfrownew.createCell( k);
					hssfcellnew.setCellValue(value);
				}
			}
		}
		return hssfworkbook;
	}


	/**
	 *    新冠导出Excel
	 * **/
	public static void writxinguanExcel(List<Norovirus> ln, String path) {
		//工作簿
		HSSFWorkbook hssfworkbook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet hssfsheet = hssfworkbook.createSheet();
		//sheet名称乱码处理
		hssfworkbook.setSheetName(0,"新型冠状报告自动化");
		//取得第 i 行
		HSSFRow hssfrow1 = hssfsheet.createRow(0);


		HSSFCell hssfcell_00 = hssfrow1.createCell(0);
		hssfcell_00.setCellValue("ID");
		hssfsheet.setColumnWidth(0, 12*256);
		HSSFCell hssfcell_001 = hssfrow1.createCell(1);
		hssfcell_001.setCellValue("样本编号");
		hssfsheet.setColumnWidth(1, 20*256);
		HSSFCell hssfcell_002 = hssfrow1.createCell(2);
		hssfcell_002.setCellValue("姓名");
		hssfsheet.setColumnWidth(2, 12*256);
		HSSFCell hssfcell_003 = hssfrow1.createCell(3);
		hssfcell_003.setCellValue("预约号");
		hssfsheet.setColumnWidth(3, 15*256);
		HSSFCell hssfcell_004 = hssfrow1.createCell(4);
		hssfcell_004.setCellValue("身份证号");
		hssfsheet.setColumnWidth(4, 18*256);
		HSSFCell hssfcell_005 = hssfrow1.createCell(5);
		hssfcell_005.setCellValue("送检人单位");
		hssfsheet.setColumnWidth(5, 16*256);
		HSSFCell hssfcell_006 = hssfrow1.createCell(6);
		hssfcell_006.setCellValue("采样时间");
		hssfsheet.setColumnWidth(6, 12*256);
		HSSFCell hssfcell_007 = hssfrow1.createCell(7);
		hssfcell_007.setCellValue("样本类型");
		hssfsheet.setColumnWidth(7, 25*256);
		HSSFCell hssfcell_008 = hssfrow1.createCell(8);
		hssfcell_008.setCellValue("接收日期");
		hssfsheet.setColumnWidth(8, 16*256);
		HSSFCell hssfcell_009 = hssfrow1.createCell(9);
		hssfcell_009.setCellValue("检测项目");
		hssfsheet.setColumnWidth(9, 30*256);
		HSSFCell hssfcell_0010 = hssfrow1.createCell(10);
		hssfcell_0010.setCellValue("采样单位");
		hssfsheet.setColumnWidth(10, 16*256);
		HSSFCell hssfcell_0011 = hssfrow1.createCell(11);
		hssfcell_0011.setCellValue("检测结果");
		hssfsheet.setColumnWidth(11, 16*256);
		HSSFCell hssfcell_0012 = hssfrow1.createCell(12);
		hssfcell_0012.setCellValue("报告日期");
		hssfsheet.setColumnWidth(12, 16*256);


		for (int i = 0; i < ln.size(); i++) {
			//取得第 i 行
			HSSFRow hssfrow = hssfsheet.createRow(i+1);
			HSSFCell hssfcell_0 = hssfrow.createCell(0);
			hssfcell_0.setCellValue(ln.get(i).getId());
			HSSFCell hssfcell_1 = hssfrow.createCell(1);
			hssfcell_1.setCellValue(ln.get(i).getSampleNo());
			HSSFCell hssfcell_2 = hssfrow.createCell(2);
			hssfcell_2.setCellValue(ln.get(i).getName());
			HSSFCell hssfcell_3 = hssfrow.createCell(3);
			hssfcell_3.setCellValue(ln.get(i).getReservation());
			HSSFCell hssfcell_4 = hssfrow.createCell(4);
			hssfcell_4.setCellValue(ln.get(i).getIdcard());
			HSSFCell hssfcell_5 = hssfrow.createCell(5);
			hssfcell_5.setCellValue(ln.get(i).getSendingPerson());
			HSSFCell hssfcell_6 = hssfrow.createCell(6);
			hssfcell_6.setCellValue(ln.get(i).getSamplingDate());
			HSSFCell hssfcell_7 = hssfrow.createCell(7);
			hssfcell_7.setCellValue(ln.get(i).getSampleType());
			HSSFCell hssfcell_8 = hssfrow.createCell(8);
			hssfcell_8.setCellValue(ln.get(i).getReceptionDate());
			HSSFCell hssfcell_9 = hssfrow.createCell(9);
			hssfcell_9.setCellValue(ln.get(i).getCheckProject());

			HSSFCell hssfcell_10 = hssfrow.createCell(10);
			hssfcell_10.setCellValue(ln.get(i).getInspection());

			HSSFCell hssfcell_11 = hssfrow.createCell(11);
			hssfcell_11.setCellValue(ln.get(i).getDetectionResult());
			HSSFCell hssfcell_12 = hssfrow.createCell(12);
			hssfcell_12.setCellValue(ln.get(i).getReportdate());
		}
		FileOutputStream fileoutputstream;
		try {
			fileoutputstream = new FileOutputStream(path);
			hssfworkbook.write(fileoutputstream);
			fileoutputstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void writnuoruExcel(List<Norovirus> ln, String path) {
		//工作簿
		HSSFWorkbook hssfworkbook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet hssfsheet = hssfworkbook.createSheet();
		//sheet名称乱码处理
		hssfworkbook.setSheetName(0,"诺如报告自动化");
		//取得第 i 行
		HSSFRow hssfrow1 = hssfsheet.createRow(0);


		HSSFCell hssfcell_00 = hssfrow1.createCell(0);
		hssfcell_00.setCellValue("ID");
		hssfsheet.setColumnWidth(0, 12*256);
		HSSFCell hssfcell_001 = hssfrow1.createCell(1);
		hssfcell_001.setCellValue("样本编号");
		hssfsheet.setColumnWidth(1, 20*256);
		HSSFCell hssfcell_002 = hssfrow1.createCell(2);
		hssfcell_002.setCellValue("姓名");
		hssfsheet.setColumnWidth(2, 12*256);
//		HSSFCell hssfcell_003 = hssfrow1.createCell(3);
//		hssfcell_003.setCellValue("预约号");
//		hssfsheet.setColumnWidth(3, 15*256);
		HSSFCell hssfcell_004 = hssfrow1.createCell(4);
		hssfcell_004.setCellValue("身份证号");
		hssfsheet.setColumnWidth(4, 18*256);
		HSSFCell hssfcell_005 = hssfrow1.createCell(5);
		hssfcell_005.setCellValue("送检人单位");
		hssfsheet.setColumnWidth(5, 16*256);
		HSSFCell hssfcell_006 = hssfrow1.createCell(6);
		hssfcell_006.setCellValue("采样时间");
		hssfsheet.setColumnWidth(6, 12*256);
		HSSFCell hssfcell_007 = hssfrow1.createCell(7);
		hssfcell_007.setCellValue("样本类型");
		hssfsheet.setColumnWidth(7, 25*256);
		HSSFCell hssfcell_008 = hssfrow1.createCell(8);
		hssfcell_008.setCellValue("接收日期");
		hssfsheet.setColumnWidth(8, 16*256);
		HSSFCell hssfcell_009 = hssfrow1.createCell(9);
		hssfcell_009.setCellValue("检测项目");
		hssfsheet.setColumnWidth(9, 30*256);
		HSSFCell hssfcell_0010 = hssfrow1.createCell(10);
		hssfcell_0010.setCellValue("采样单位");
		hssfsheet.setColumnWidth(10, 16*256);
		HSSFCell hssfcell_0011 = hssfrow1.createCell(11);
		hssfcell_0011.setCellValue("检测结果");
		hssfsheet.setColumnWidth(11, 16*256);
		HSSFCell hssfcell_0012 = hssfrow1.createCell(12);
		hssfcell_0012.setCellValue("报告日期");
		hssfsheet.setColumnWidth(12, 16*256);


		for (int i = 0; i < ln.size(); i++) {
			//取得第 i 行
			HSSFRow hssfrow = hssfsheet.createRow(i+1);
			HSSFCell hssfcell_0 = hssfrow.createCell(0);
			hssfcell_0.setCellValue(ln.get(i).getId());
			HSSFCell hssfcell_1 = hssfrow.createCell(1);
			hssfcell_1.setCellValue(ln.get(i).getSampleNo());
			HSSFCell hssfcell_2 = hssfrow.createCell(2);
			hssfcell_2.setCellValue(ln.get(i).getName());
//			HSSFCell hssfcell_3 = hssfrow.createCell(3);
//			hssfcell_3.setCellValue(ln.get(i).getReservation());
			HSSFCell hssfcell_4 = hssfrow.createCell(4);
			hssfcell_4.setCellValue(ln.get(i).getIdcard());
			HSSFCell hssfcell_5 = hssfrow.createCell(5);
			hssfcell_5.setCellValue(ln.get(i).getSendingPerson());
			HSSFCell hssfcell_6 = hssfrow.createCell(6);
			hssfcell_6.setCellValue(ln.get(i).getSamplingDate());
			HSSFCell hssfcell_7 = hssfrow.createCell(7);
			hssfcell_7.setCellValue(ln.get(i).getSampleType());
			HSSFCell hssfcell_8 = hssfrow.createCell(8);
			hssfcell_8.setCellValue(ln.get(i).getReceptionDate());
			HSSFCell hssfcell_9 = hssfrow.createCell(9);
			hssfcell_9.setCellValue(ln.get(i).getCheckProject());

			HSSFCell hssfcell_10 = hssfrow.createCell(10);
			hssfcell_10.setCellValue(ln.get(i).getInspection());

			HSSFCell hssfcell_11 = hssfrow.createCell(11);
			hssfcell_11.setCellValue(ln.get(i).getDetectionResult());
			HSSFCell hssfcell_12 = hssfrow.createCell(12);
			hssfcell_12.setCellValue(ln.get(i).getReportdate());
		}
		FileOutputStream fileoutputstream;
		try {
			fileoutputstream = new FileOutputStream(path);
			hssfworkbook.write(fileoutputstream);
			fileoutputstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 *    新冠财务导出Excel
	 * **/
	public static void writxinguancaiwuExcel(List<Norovirus> ln, String path) {
		//工作簿
		HSSFWorkbook hssfworkbook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet hssfsheet = hssfworkbook.createSheet();
		//sheet名称乱码处理
		hssfworkbook.setSheetName(0,"新型冠状财务信息");
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
		hssfcell_006.setCellValue("联系方式");
		hssfsheet.setColumnWidth(6, 12*256);
		HSSFCell hssfcell_007 = hssfrow1.createCell(7);
		hssfcell_007.setCellValue("预约号");
		hssfsheet.setColumnWidth(7, 15*256);
		HSSFCell hssfcell_008 = hssfrow1.createCell(8);
		hssfcell_008.setCellValue("采样时间");
		hssfsheet.setColumnWidth(8, 18*256);
		HSSFCell hssfcell_009 = hssfrow1.createCell(9);
		hssfcell_009.setCellValue("接收日期");
		hssfsheet.setColumnWidth(9, 14*256);
		HSSFCell hssfcell_0010 = hssfrow1.createCell(10);
		hssfcell_0010.setCellValue("检测项目");
		hssfsheet.setColumnWidth(10, 14*256);
		HSSFCell hssfcell_0011 = hssfrow1.createCell(11);
		hssfcell_0011.setCellValue("采样单位");
		hssfsheet.setColumnWidth(11, 16*256);
		HSSFCell hssfcell_0012 = hssfrow1.createCell(12);
		hssfcell_0012.setCellValue("报告日期");
		hssfsheet.setColumnWidth(12, 16*256);
		HSSFCell hssfcell_0013 = hssfrow1.createCell(13);
		hssfcell_0013.setCellValue("商户单号");
		hssfsheet.setColumnWidth(13, 30*256);
		HSSFCell hssfcell_0014 = hssfrow1.createCell(14);
		hssfcell_0014.setCellValue("付款金额");
		hssfsheet.setColumnWidth(14, 25*256);
		HSSFCell hssfcell_0015 = hssfrow1.createCell(15);
		hssfcell_0015.setCellValue("是否要发票");
		hssfsheet.setColumnWidth(15, 16*256);
		HSSFCell hssfcell_0016 = hssfrow1.createCell(16);
		hssfcell_0016.setCellValue("发票类型");
		hssfsheet.setColumnWidth(16, 16*256);
		HSSFCell hssfcell_0017 = hssfrow1.createCell(17);
		hssfcell_0017.setCellValue("发票邮箱");
		hssfsheet.setColumnWidth(17, 16*256);
		HSSFCell hssfcell_0018 = hssfrow1.createCell(18);
		hssfcell_0018.setCellValue("发票抬头");
		hssfsheet.setColumnWidth(18, 16*256);
		HSSFCell hssfcell_0019 = hssfrow1.createCell(19);
		hssfcell_0019.setCellValue("税号");
		hssfsheet.setColumnWidth(19, 12*256);
		HSSFCell hssfcell_0020 = hssfrow1.createCell(20);
		hssfcell_0020.setCellValue("预约采样点");
		hssfsheet.setColumnWidth(20, 16*256);
		HSSFCell hssfcell_0021 = hssfrow1.createCell(21);
		hssfcell_0021.setCellValue("合作机构");
		hssfsheet.setColumnWidth(21, 16*256);
		HSSFCell hssfcell_0022 = hssfrow1.createCell(22);
		hssfcell_0022.setCellValue("分支机构");
		hssfsheet.setColumnWidth(22, 16*256);
		HSSFCell hssfcell_0023 = hssfrow1.createCell(23);
		hssfcell_0023.setCellValue("业务员");
		hssfsheet.setColumnWidth(23, 16*256);
		HSSFCell hssfcell_0024 = hssfrow1.createCell(24);
		hssfcell_0024.setCellValue("销售姓名");
		hssfsheet.setColumnWidth(24, 16*256);

		HSSFCell hssfcell_0025 = hssfrow1.createCell(25);
		hssfcell_0025.setCellValue("来源渠道");
		hssfsheet.setColumnWidth(25, 16*256);

		HSSFCell hssfcell_0026 = hssfrow1.createCell(26);
		hssfcell_0026.setCellValue("实际采样地点");
		hssfsheet.setColumnWidth(26, 16*256);

		HSSFCell hssfcell_0027 = hssfrow1.createCell(27);
		hssfcell_0027.setCellValue("兑换码");
		hssfsheet.setColumnWidth(27, 16*256);

		HSSFCell hssfcell_0028 = hssfrow1.createCell(28);
		hssfcell_0028.setCellValue("受检者类型");
		hssfsheet.setColumnWidth(28, 16*256);
		for (int i = 0; i < ln.size(); i++) {
			//取得第 i 行
			HSSFRow hssfrow = hssfsheet.createRow(i+1);
			HSSFCell hssfcell_0 = hssfrow.createCell(0);
			hssfcell_0.setCellValue(ln.get(i).getId());
			HSSFCell hssfcell_1 = hssfrow.createCell(1);
			hssfcell_1.setCellValue(ln.get(i).getSampleNo());

			HSSFCell hssfcell_2 = hssfrow.createCell(2);
			hssfcell_2.setCellValue(ln.get(i).getInspection());
			HSSFCell hssfcell_03 = hssfrow.createCell(3);
			hssfcell_03.setCellValue(ln.get(i).getIdcard());
			HSSFCell hssfcell_4 = hssfrow.createCell(4);
			hssfcell_4.setCellValue(ln.get(i).getSendingPerson());
			HSSFCell hssfcell_5 = hssfrow.createCell(5);
			hssfcell_5.setCellValue(ln.get(i).getName());

			HSSFCell hssfcell_7 = hssfrow.createCell(7);
			hssfcell_7.setCellValue(ln.get(i).getReservation());
			HSSFCell hssfcell_8 = hssfrow.createCell(8);
			hssfcell_8.setCellValue(ln.get(i).getSamplingDate());
			HSSFCell hssfcell_9 = hssfrow.createCell(9);
			hssfcell_9.setCellValue(ln.get(i).getReceptionDate());
			HSSFCell hssfcell_10 = hssfrow.createCell(10);
			hssfcell_10.setCellValue(ln.get(i).getCheckProject());
			HSSFCell hssfcell_11 = hssfrow.createCell(11);
			hssfcell_11.setCellValue(ln.get(i).getInspection());
			HSSFCell hssfcell_12 = hssfrow.createCell(12);
			hssfcell_12.setCellValue(ln.get(i).getReportdate());

			HSSFCell hssfcell_25 = hssfrow.createCell(25);
			hssfcell_25.setCellValue(ln.get(i).getQudao());
			if(ln.get(i).getAppointmentinfo()!=null){

				Appointmentinfo a =ln.get(i).getAppointmentinfo();

				HSSFCell hssfcell_6 = hssfrow.createCell(6);
				hssfcell_6.setCellValue(a.getPhone());

				HSSFCell hssfcell_13 = hssfrow.createCell(13);
				hssfcell_13.setCellValue(a.getOuttradeno());
				HSSFCell hssfcell_14 = hssfrow.createCell(14);
				hssfcell_14.setCellValue(a.getTotalfee());
				HSSFCell hssfcell_15 = hssfrow.createCell(15);
				hssfcell_15.setCellValue(a.getIsfapiao());
				HSSFCell hssfcell_16 = hssfrow.createCell(16);
				hssfcell_16.setCellValue(a.getFapiaotype());
				HSSFCell hssfcell_17 = hssfrow.createCell(17);
				hssfcell_17.setCellValue(a.getAccessemail());
				HSSFCell hssfcell_18 = hssfrow.createCell(18);
				hssfcell_18.setCellValue(a.getFapiaotaitou());
				HSSFCell hssfcell_19 = hssfrow.createCell(19);
				hssfcell_19.setCellValue(a.getShuihao());
				HSSFCell hssfcell_20 = hssfrow.createCell(20);
				hssfcell_20.setCellValue(a.getCaiyangdidian());
				HSSFCell hssfcell_21 = hssfrow.createCell(21);
				hssfcell_21.setCellValue(a.getCooperationcompanyname());
				HSSFCell hssfcell_22 = hssfrow.createCell(22);
				hssfcell_22.setCellValue(a.getCooperationsubcompanyname());
				HSSFCell hssfcell_23 = hssfrow.createCell(23);
				hssfcell_23.setCellValue(a.getBusinessmanager());
				HSSFCell hssfcell_24 = hssfrow.createCell(24);
				hssfcell_24.setCellValue(a.getSalename());

				HSSFCell hssfcell_28 = hssfrow.createCell(28);
				hssfcell_28.setCellValue(a.getSubjecttype());

				if(ln.get(i).getXgBuKaiFaPiao()!=null){
					XgBuKaiFaPiao b = ln.get(i).getXgBuKaiFaPiao();
					hssfcell_15.setCellValue(b.getIsfapiao());
					hssfcell_16.setCellValue(b.getFapiaotype());
					hssfcell_17.setCellValue(b.getAccessemail());
					hssfcell_18.setCellValue(b.getFapiaotaitou());
					hssfcell_19.setCellValue(b.getShuihao());
				}

				hssfcell_25.setCellValue(a.getQudao());

				HSSFCell hssfcell_26 = hssfrow.createCell(26);
				hssfcell_26.setCellValue(a.getShicaiyangdian());


				if(a.getDuihuanma()!=null){
					HSSFCell hssfcell_27 = hssfrow.createCell(27);
					hssfcell_27.setCellValue(a.getDuihuanma().replace("CODE_128,c:;p:","").replace("c:;p:","").replace(";",""));
				}
			}
		}
		FileOutputStream fileoutputstream;
		try {
			fileoutputstream = new FileOutputStream(path);
			hssfworkbook.write(fileoutputstream);
			fileoutputstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 *    导出采样点客户信息
	 * **/
	public static void caiyangdianExcel(List<Appointmentinfoxgwx> ln, String path) {
		//工作簿
		HSSFWorkbook hssfworkbook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet hssfsheet = hssfworkbook.createSheet();
		//sheet名称乱码处理
		hssfworkbook.setSheetName(0,"采样点客户信息");
		//取得第 i 行
		HSSFRow hssfrow1 = hssfsheet.createRow(0);
		HSSFCell hssfcell_00 = hssfrow1.createCell(0);
		hssfcell_00.setCellValue("ID");
		hssfsheet.setColumnWidth(0, 12*256);
		HSSFCell hssfcell_001 = hssfrow1.createCell(1);
		hssfcell_001.setCellValue("样本编号");
		hssfsheet.setColumnWidth(1, 16*256);
		HSSFCell hssfcell_002 = hssfrow1.createCell(2);
		hssfcell_002.setCellValue("身份证号");
		hssfsheet.setColumnWidth(2, 20*256);
		HSSFCell hssfcell_003 = hssfrow1.createCell(3);
		hssfcell_003.setCellValue("送检人单位");
		hssfsheet.setColumnWidth(3, 20*256);
		HSSFCell hssfcell_004 = hssfrow1.createCell(4);
		hssfcell_004.setCellValue("姓名");
		hssfsheet.setColumnWidth(4, 12*256);
		HSSFCell hssfcell_005 = hssfrow1.createCell(5);
		hssfcell_005.setCellValue("预约号");
		hssfsheet.setColumnWidth(5, 12*256);
		HSSFCell hssfcell_006 = hssfrow1.createCell(6);
		hssfcell_006.setCellValue("采样时间");
		hssfsheet.setColumnWidth(6, 22*256);
		HSSFCell hssfcell_007 = hssfrow1.createCell(7);
		hssfcell_007.setCellValue("报告日期");
		hssfsheet.setColumnWidth(7, 18*256);
		HSSFCell hssfcell_008 = hssfrow1.createCell(8);
		hssfcell_008.setCellValue("商户订单号");
		hssfsheet.setColumnWidth(8, 20*256);
		HSSFCell hssfcell_009 = hssfrow1.createCell(9);
		hssfcell_009.setCellValue("付款金额");
		hssfsheet.setColumnWidth(9, 14*256);
		HSSFCell hssfcell_0010 = hssfrow1.createCell(10);
		hssfcell_0010.setCellValue("实际采样点");
		hssfsheet.setColumnWidth(10, 30*256);
		HSSFCell hssfcell_0011 = hssfrow1.createCell(11);
		hssfcell_0011.setCellValue("合作机构");
		hssfsheet.setColumnWidth(11, 30*256);
		HSSFCell hssfcell_0012 = hssfrow1.createCell(12);
		hssfcell_0012.setCellValue("分支机构");
		hssfsheet.setColumnWidth(12, 30*256);
		HSSFCell hssfcell_0013 = hssfrow1.createCell(13);
		hssfcell_0013.setCellValue("业务员");
		hssfsheet.setColumnWidth(13, 16*256);
		HSSFCell hssfcell_0014 = hssfrow1.createCell(14);
		hssfcell_0014.setCellValue("销售姓名");
		hssfsheet.setColumnWidth(14, 16*256);
		HSSFCell hssfcell_0015 = hssfrow1.createCell(15);
		hssfcell_0015.setCellValue("销售渠道");
		hssfsheet.setColumnWidth(15, 16*256);

		for (int i = 0; i < ln.size(); i++) {
			//取得第 i 行
			HSSFRow hssfrow = hssfsheet.createRow(i+1);
			HSSFCell hssfcell_0 = hssfrow.createCell(0);
			hssfcell_0.setCellValue(ln.get(i).getId());
			HSSFCell hssfcell_1 = hssfrow.createCell(1);
			hssfcell_1.setCellValue(ln.get(i).getSampleNo());
			HSSFCell hssfcell_2 = hssfrow.createCell(2);
			hssfcell_2.setCellValue(ln.get(i).getSfz());
			HSSFCell hssfcell_3 = hssfrow.createCell(3);
			hssfcell_3.setCellValue(ln.get(i).getSending());
			HSSFCell hssfcell_4 = hssfrow.createCell(4);
			hssfcell_4.setCellValue(ln.get(i).getName());
			HSSFCell hssfcell_5 = hssfrow.createCell(5);
			hssfcell_5.setCellValue(ln.get(i).getYuyuenum());
			HSSFCell hssfcell_6 = hssfrow.createCell(6);
			if(ln.get(i).getSamplebindtime()!=null && !ln.get(i).getSamplebindtime().equals("")){
				hssfcell_6.setCellValue(ln.get(i).getSamplebindtime());
			}else{
				hssfcell_6.setCellValue(ln.get(i).getYuyuedate()+" "+ln.get(i).getYuyuetime());
			}
			HSSFCell hssfcell_7 = hssfrow.createCell(7);
			if(ln.get(i).getNorovirus()!=null){
				Norovirus n=ln.get(i).getNorovirus();
				hssfcell_7.setCellValue(n.getReceptionDate());
			}
			HSSFCell hssfcell_8 = hssfrow.createCell(8);
			hssfcell_8.setCellValue(ln.get(i).getOuttradeno());
			HSSFCell hssfcell_9 = hssfrow.createCell(9);
			String money=ln.get(i).getTotalfee();
			if(money!=null && !money.equals("") && money.length()>3){
				money=money.substring(0,money.length()-2);
			}
			hssfcell_9.setCellValue(money);
			HSSFCell hssfcell_10 = hssfrow.createCell(10);
			hssfcell_10.setCellValue(ln.get(i).getShicaiyangdian());


			HSSFCell hssfcell_11 = hssfrow.createCell(11);
			hssfcell_11.setCellValue(ln.get(i).getCooperationcompanyname());
			HSSFCell hssfcell_12 = hssfrow.createCell(12);
			hssfcell_12.setCellValue(ln.get(i).getCooperationsubcompanyname());
			HSSFCell hssfcell_13 = hssfrow.createCell(13);
			hssfcell_13.setCellValue(ln.get(i).getBusinessmanager());
			HSSFCell hssfcell_14 = hssfrow.createCell(14);
			hssfcell_14.setCellValue(ln.get(i).getSalename());
			HSSFCell hssfcell_15 = hssfrow.createCell(15);
			hssfcell_15.setCellValue(ln.get(i).getQudao());
		}
		FileOutputStream fileoutputstream;
		try {
			fileoutputstream = new FileOutputStream(path);
			hssfworkbook.write(fileoutputstream);
			fileoutputstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /**
     *    财务兑换码
     * **/
    public static void caiwuExcel(List<Appointmentinfoxgwx> ln, String path) {
        //工作簿
        HSSFWorkbook hssfworkbook = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet hssfsheet = hssfworkbook.createSheet();
        //sheet名称乱码处理
        hssfworkbook.setSheetName(0,"新冠财务信息");
        //取得第 i 行
        HSSFRow hssfrow1 = hssfsheet.createRow(0);

        HSSFCell hssfcell_00 = hssfrow1.createCell(0);
        hssfcell_00.setCellValue("样本编号");
        hssfsheet.setColumnWidth(0, 12*256);
        HSSFCell hssfcell_001 = hssfrow1.createCell(1);
        hssfcell_001.setCellValue("姓名");
        hssfsheet.setColumnWidth(1, 16*256);
        HSSFCell hssfcell_002 = hssfrow1.createCell(2);
        hssfcell_002.setCellValue("预约号");
        hssfsheet.setColumnWidth(2, 20*256);
        HSSFCell hssfcell_003 = hssfrow1.createCell(3);
        hssfcell_003.setCellValue("身份证号");
        hssfsheet.setColumnWidth(3, 20*256);
        HSSFCell hssfcell_004 = hssfrow1.createCell(4);
        hssfcell_004.setCellValue("采样时间");
        hssfsheet.setColumnWidth(4, 12*256);
        HSSFCell hssfcell_005 = hssfrow1.createCell(5);
        hssfcell_005.setCellValue("联系电话");
        hssfsheet.setColumnWidth(5, 12*256);
        HSSFCell hssfcell_006 = hssfrow1.createCell(6);
        hssfcell_006.setCellValue("是否需要发票");
        hssfsheet.setColumnWidth(6, 22*256);
        HSSFCell hssfcell_007 = hssfrow1.createCell(7);
        hssfcell_007.setCellValue("税号");
        hssfsheet.setColumnWidth(7, 18*256);
        HSSFCell hssfcell_008 = hssfrow1.createCell(8);
        hssfcell_008.setCellValue("发票抬头");
        hssfsheet.setColumnWidth(8, 20*256);
        HSSFCell hssfcell_009 = hssfrow1.createCell(9);
        hssfcell_009.setCellValue("发票类型");
        hssfsheet.setColumnWidth(9, 14*256);
		HSSFCell hssfcell_0010 = hssfrow1.createCell(10);
		hssfcell_0010.setCellValue("电子发票接收邮箱");
		hssfsheet.setColumnWidth(10, 14*256);
		HSSFCell hssfcell_0011 = hssfrow1.createCell(11);
		hssfcell_0011.setCellValue("商户单号");
		hssfsheet.setColumnWidth(11, 14*256);
		HSSFCell hssfcell_0012 = hssfrow1.createCell(12);
		hssfcell_0012.setCellValue("总费用");
		hssfsheet.setColumnWidth(12, 14*256);
		HSSFCell hssfcell_0013 = hssfrow1.createCell(13);
		hssfcell_0013.setCellValue("检查是否付款到账");
		hssfsheet.setColumnWidth(13, 14*256);
        HSSFCell hssfcell_0014 = hssfrow1.createCell(14);
        hssfcell_0014.setCellValue("实际采样点");
        hssfsheet.setColumnWidth(14, 30*256);
        HSSFCell hssfcell_0015 = hssfrow1.createCell(15);
        hssfcell_0015.setCellValue("合作机构");
        hssfsheet.setColumnWidth(15, 30*256);
        HSSFCell hssfcell_0016 = hssfrow1.createCell(16);
        hssfcell_0016.setCellValue("分支机构");
        hssfsheet.setColumnWidth(16, 30*256);
        HSSFCell hssfcell_0017 = hssfrow1.createCell(17);
        hssfcell_0017.setCellValue("业务员");
        hssfsheet.setColumnWidth(17, 16*256);
        HSSFCell hssfcell_0018 = hssfrow1.createCell(18);
        hssfcell_0018.setCellValue("销售姓名");
        hssfsheet.setColumnWidth(18, 16*256);
		HSSFCell hssfcell_0019 = hssfrow1.createCell(19);
		hssfcell_0019.setCellValue("渠道");
		hssfsheet.setColumnWidth(19, 16*256);
		HSSFCell hssfcell_0020 = hssfrow1.createCell(20);
		hssfcell_0020.setCellValue("兑换码");
		hssfsheet.setColumnWidth(20, 16*256);
		HSSFCell hssfcell_0021 = hssfrow1.createCell(21);
		hssfcell_0021.setCellValue("退款状态");
		hssfsheet.setColumnWidth(21, 14*256);
		HSSFCell hssfcell_0022 = hssfrow1.createCell(22);
		hssfcell_0022.setCellValue("实际退款金额");
		hssfsheet.setColumnWidth(22, 14*256);
		HSSFCell hssfcell_0023 = hssfrow1.createCell(23);
		hssfcell_0023.setCellValue("报告日期");
		hssfsheet.setColumnWidth(23, 14*256);
		HSSFCell hssfcell_0024 = hssfrow1.createCell(24);
		hssfcell_0024.setCellValue("手续费");
		hssfsheet.setColumnWidth(24, 14*256);
		HSSFCell hssfcell_0025 = hssfrow1.createCell(25);
		hssfcell_0025.setCellValue("进账金额");
		hssfsheet.setColumnWidth(25, 14*256);
		HSSFCell hssfcell_0026 = hssfrow1.createCell(26);
		hssfcell_0026.setCellValue("回款方式");
		hssfsheet.setColumnWidth(26, 14*256);
		HSSFCell hssfcell_0027 = hssfrow1.createCell(27);
		hssfcell_0027.setCellValue("是否已回款");
		hssfsheet.setColumnWidth(27, 14*256);
		HSSFCell hssfcell_0028 = hssfrow1.createCell(28);
		hssfcell_0028.setCellValue("开票金额");
		hssfsheet.setColumnWidth(28, 14*256);
		HSSFCell hssfcell_0029 = hssfrow1.createCell(29);
		hssfcell_0029.setCellValue("发票号");
		hssfsheet.setColumnWidth(29, 14*256);
		HSSFCell hssfcell_0030 = hssfrow1.createCell(30);
		hssfcell_0030.setCellValue("开票时间");
		hssfsheet.setColumnWidth(30, 14*256);
		HSSFCell hssfcell_0031 = hssfrow1.createCell(31);
		hssfcell_0031.setCellValue("受检者类型");
		hssfsheet.setColumnWidth(31, 14*256);

        for (int i = 0; i < ln.size(); i++) {
            //取得第 i 行
            HSSFRow hssfrow = hssfsheet.createRow(i+1);
            HSSFCell hssfcell_0 = hssfrow.createCell(0);
            hssfcell_0.setCellValue(ln.get(i).getSampleNo());
			HSSFCell hssfcell_1 = hssfrow.createCell(1);
			hssfcell_1.setCellValue(ln.get(i).getName());
			HSSFCell hssfcell_2 = hssfrow.createCell(2);
			hssfcell_2.setCellValue(ln.get(i).getYuyuenum());
			HSSFCell hssfcell_3 = hssfrow.createCell(3);
			hssfcell_3.setCellValue(ln.get(i).getSfz());
			HSSFCell hssfcell_4 = hssfrow.createCell(4);
			hssfcell_4.setCellValue(ln.get(i).getSamplebindtime());
			HSSFCell hssfcell_5 = hssfrow.createCell(5);
			hssfcell_5.setCellValue(ln.get(i).getPhone());
			HSSFCell hssfcell_6 = hssfrow.createCell(6);
			hssfcell_6.setCellValue(ln.get(i).getIsfapiao());
			HSSFCell hssfcell_7 = hssfrow.createCell(7);
			hssfcell_7.setCellValue(ln.get(i).getShuihao());
			HSSFCell hssfcell_8 = hssfrow.createCell(8);
			hssfcell_8.setCellValue(ln.get(i).getFapiaotaitou());
			HSSFCell hssfcell_9 = hssfrow.createCell(9);
			hssfcell_9.setCellValue(ln.get(i).getFapiaotype());
			HSSFCell hssfcell_10 = hssfrow.createCell(10);
			hssfcell_10.setCellValue(ln.get(i).getAccessemail());
			HSSFCell hssfcell_11 = hssfrow.createCell(11);
			hssfcell_11.setCellValue(ln.get(i).getOuttradeno());
			HSSFCell hssfcell_12 = hssfrow.createCell(12);
			hssfcell_12.setCellValue(ln.get(i).getTotalfee());
			HSSFCell hssfcell_13 = hssfrow.createCell(13);
			hssfcell_13.setCellValue(ln.get(i).getIssuccessed());
			HSSFCell hssfcell_14 = hssfrow.createCell(14);
			hssfcell_14.setCellValue(ln.get(i).getShicaiyangdian());
            HSSFCell hssfcell_15 = hssfrow.createCell(15);
            hssfcell_15.setCellValue(ln.get(i).getCooperationcompanyname());
            HSSFCell hssfcell_16 = hssfrow.createCell(16);
            hssfcell_16.setCellValue(ln.get(i).getCooperationsubcompanyname());
            HSSFCell hssfcell_17 = hssfrow.createCell(17);
            hssfcell_17.setCellValue(ln.get(i).getBusinessmanager());
            HSSFCell hssfcell_18 = hssfrow.createCell(18);
            hssfcell_18.setCellValue(ln.get(i).getSalename());
			HSSFCell hssfcell_19 = hssfrow.createCell(19);
			hssfcell_19.setCellValue(ln.get(i).getQudao());
			if(ln.get(i).getDuihuanma()!=null){
				HSSFCell hssfcell_20 = hssfrow.createCell(20);
				hssfcell_20.setCellValue(ln.get(i).getDuihuanma().replace("CODE_128,c:;p:","").replace("c:;p:","").replace(";",""));
			}
			HSSFCell hssfcell_21 = hssfrow.createCell(21);
			hssfcell_21.setCellValue(ln.get(i).getTuikuan());
			HSSFCell hssfcell_22 = hssfrow.createCell(22);
			hssfcell_22.setCellValue(ln.get(i).getShijijine());
			HSSFCell hssfcell_23 = hssfrow.createCell(23);
			hssfcell_23.setCellValue(ln.get(i).getReportdate());
			HSSFCell hssfcell_24 = hssfrow.createCell(24);
			hssfcell_24.setCellValue(ln.get(i).getShouxufei());
			HSSFCell hssfcell_25 = hssfrow.createCell(25);
			hssfcell_25.setCellValue(ln.get(i).getJinzhangmoney());
			HSSFCell hssfcell_26 = hssfrow.createCell(26);
			hssfcell_26.setCellValue(ln.get(i).getCollectionmethod());
			HSSFCell hssfcell_27 = hssfrow.createCell(27);
			hssfcell_27.setCellValue(ln.get(i).getHuikuanstate());
			HSSFCell hssfcell_28 = hssfrow.createCell(28);
			hssfcell_28.setCellValue(ln.get(i).getKaipiaomoney());
			HSSFCell hssfcell_29 = hssfrow.createCell(29);
			hssfcell_29.setCellValue(ln.get(i).getKaipiaono());
			HSSFCell hssfcell_30 = hssfrow.createCell(30);
			hssfcell_30.setCellValue(ln.get(i).getKaipiaodate());
			HSSFCell hssfcell_31 = hssfrow.createCell(31);
			hssfcell_31.setCellValue(ln.get(i).getSubjecttype());
        }
        FileOutputStream fileoutputstream;
        try {
            fileoutputstream = new FileOutputStream(path);
            hssfworkbook.write(fileoutputstream);
            fileoutputstream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	/**
	 *    预约表信息
	 * **/
	public static void yuyueExcel(List<Appointmentinfoxgwx> ln, String path) {
		//工作簿
		HSSFWorkbook hssfworkbook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet hssfsheet = hssfworkbook.createSheet();
		//sheet名称乱码处理
		hssfworkbook.setSheetName(0,"新冠预约信息");
		//取得第 i 行
		HSSFRow hssfrow1 = hssfsheet.createRow(0);

		HSSFCell hssfcell_00 = hssfrow1.createCell(0);
		hssfcell_00.setCellValue("样本编号");
		hssfsheet.setColumnWidth(0, 12*256);
		HSSFCell hssfcell_001 = hssfrow1.createCell(1);
		hssfcell_001.setCellValue("姓名");
		hssfsheet.setColumnWidth(1, 16*256);

		HSSFCell hssfcell_002 = hssfrow1.createCell(2);
		hssfcell_002.setCellValue("身份证");
		hssfsheet.setColumnWidth(2, 16*256);

		HSSFCell hssfcell_003 = hssfrow1.createCell(3);
		hssfcell_003.setCellValue("团体名称/商户单号");
		hssfsheet.setColumnWidth(3, 16*256);
		HSSFCell hssfcell_004 = hssfrow1.createCell(4);
		hssfcell_004.setCellValue("渠道");
		hssfsheet.setColumnWidth(4, 16*256);
		HSSFCell hssfcell_005 = hssfrow1.createCell(5);
		hssfcell_005.setCellValue("实际采样点");
		hssfsheet.setColumnWidth(5, 16*256);
		HSSFCell hssfcell_006 = hssfrow1.createCell(6);
		hssfcell_006.setCellValue("兑换码");
		hssfsheet.setColumnWidth(6, 16*256);
		HSSFCell hssfcell_007 = hssfrow1.createCell(7);
		hssfcell_007.setCellValue("样本绑定时间");
		hssfsheet.setColumnWidth(7, 16*256);

		for (int i = 0; i < ln.size(); i++) {
			//取得第 i 行
			HSSFRow hssfrow = hssfsheet.createRow(i+1);
			HSSFCell hssfcell_0 = hssfrow.createCell(0);
			hssfcell_0.setCellValue(ln.get(i).getSampleNo());
			HSSFCell hssfcell_1 = hssfrow.createCell(1);
			hssfcell_1.setCellValue(ln.get(i).getName());
			HSSFCell hssfcell_2 = hssfrow.createCell(2);
			hssfcell_2.setCellValue(ln.get(i).getSfz());
			HSSFCell hssfcell_3 = hssfrow.createCell(3);
			hssfcell_3.setCellValue(ln.get(i).getOuttradeno());
			HSSFCell hssfcell_4 = hssfrow.createCell(4);
			hssfcell_4.setCellValue(ln.get(i).getQudao());
			HSSFCell hssfcell_5 = hssfrow.createCell(5);
			hssfcell_5.setCellValue(ln.get(i).getShicaiyangdian());
			HSSFCell hssfcell_7 = hssfrow.createCell(7);
			hssfcell_7.setCellValue(ln.get(i).getSamplebindtime());
			if(ln.get(i).getDuihuanma()!=null){
				HSSFCell hssfcell_6 = hssfrow.createCell(6);
				hssfcell_6.setCellValue(ln.get(i).getDuihuanma().replace("CODE_128,c:;p:","").replace("c:;p:","").replace(";",""));
			}

		}
		FileOutputStream fileoutputstream;
		try {
			fileoutputstream = new FileOutputStream(path);
			hssfworkbook.write(fileoutputstream);
			fileoutputstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}