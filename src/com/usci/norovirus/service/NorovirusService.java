package com.usci.norovirus.service;


import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.Struts2Utils;
import com.lims.core.utils.web.WebPathUtil;
import com.lowagie.text.pdf.PdfWriter;
import com.usci.norovirus.dao.NorovirusDao;
import com.usci.norovirus.entity.Appointmentinfoxgwx;
import com.usci.norovirus.entity.Norovirus;
import com.usci.system.entity.Position;
import com.usci.system.entity.Role;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@Transactional(readOnly=true)
public class NorovirusService {

	Logger log = LoggerFactory.getLogger(NorovirusService.class);
    @Autowired
    private NorovirusDao norovirusDao;
    //获取当前环境操作系统
    private static final String operSystem=System.getProperties().getProperty("os.name");
    @Autowired
    private AppointmentinfoxgwxService appointmentinfoxgwxService;

    public void findPage(Page<Norovirus> page, String sql, String string, int start, int limit) {
        String oj=norovirusDao.queryBySql("select count(1) from ("+sql+") as total").get(0).toString();
        //分页sql
        sql+=" LIMIT "+start+" , "+limit;
        List<Norovirus> lcs=norovirusDao.entityBySql(sql, Norovirus.class);
        page.setResult(lcs);
        page.setTotalCount(Long.parseLong(oj.toString()));
    }

    public void findBySampleNoAll(Page<Norovirus> page, String sql) {
        List<Norovirus> lcs=norovirusDao.entityBySql(sql, Norovirus.class);
        page.setResult(lcs);
    }

    public  List<Norovirus> findQudaoAll(String sql) {
        List<Norovirus> lcs=norovirusDao.entityBySql(sql, Norovirus.class);
       return lcs;
    }



    /**
     * 根据样本编号查找
     * @param sampleNo
     * @return
     */
    public List<Norovirus> findBySampleNo(String sampleNo) {
        return norovirusDao.findBy("sampleNo", sampleNo);
    }

    /**
     * 根据渠道查找
     * @param qudao
     * @return
     */
    public List<Norovirus> findByqudao(String qudao) {
        return norovirusDao.findBy("qudao", qudao);
    }

    /**
     * 根据渠道查找
     * @param id
     * @return
     */
    public Norovirus findById(Integer id) {
        return norovirusDao.findUniqueBy("id", id);
    }

    /**
     * 根据样本编号查找
     * @param sampleNo
     * @return
     */
    public Norovirus findUniqueBy(String sampleNo) {
        return norovirusDao.findUniqueBy("sampleNo", sampleNo);
    }

    /**
     * 根据样本编号和送检单位和身份证号查询，尽量保证唯一性
     * @param
     */
    public Norovirus findUniqueByLimits(String sampleNo,String sendingPerson,String idcard){
    	return norovirusDao.findUnique(Restrictions.eq("sampleNo", sampleNo),Restrictions.eq("sendingPerson", sendingPerson),Restrictions.eq("idcard", idcard));
    }

    @Transactional
    public void save(Norovirus entity){
        entity.setInputTime(Struts2Utils.getStringDate(new Date()));
        entity.setInputName(Struts2Utils.getSessionUser().getName());
        norovirusDao.save(entity);
    }

    @Transactional
    public void update(Norovirus entity){
        entity.setUpdateTime(Struts2Utils.getStringDate(new Date()));
        entity.setUpdateName(Struts2Utils.getSessionUser().getName());
        norovirusDao.update(entity);
    }

    public String createPdf(Norovirus norovirus, String  modelsvalue) {
    	log.info(norovirus+"");
        PdfReader  reader=null;
        String pdfnames="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy HH:mm:ss", Locale.ENGLISH);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMM d,yyyy HH:mm", Locale.ENGLISH);

        String date = "";
        if(norovirus.getReportdate() == null || norovirus.getReportdate().equals("")){
            date = sdf.format(new Date());
        }else{
            date = norovirus.getReportdate();
        }
        norovirus.setReportdate(date);
        if(norovirus!=null&&modelsvalue!=null){
            try {
                //获取模板路径
                String uploadFileSavePath=ServletActionContext.getServletContext().getRealPath(File.separator+"reportmould");
                //得到模板
                List<String> files=recursion(uploadFileSavePath);
                for (String fileName : files) {
                    if (fileName.equals(modelsvalue)) {
                        reader = new PdfReader(uploadFileSavePath+File.separator+fileName);
                    }
                }
                //拼接一个生成名称
                String deskFileName= norovirus.getSampleNo()+"_"+norovirus.getName();
                if(norovirus.getReservation()!=null){
                    deskFileName+= "_"+norovirus.getReservation();
                }
                if(norovirus.getInspection()!=null){
                    deskFileName+= "_"+norovirus.getInspection();
                }
                deskFileName += "_"+Struts2Utils.getStringDatehmss(new Date());
                if(modelsvalue.equals("xgyw.pdf")||modelsvalue.equals("xgywgz.pdf")){
                    deskFileName+="_英文报告";
                }
                deskFileName = deskFileName.replace(" ","_");
                String reportfile=Struts2Utils.getymd(new Date());
                pdfnames=File.separator+reportfile+File.separator+deskFileName + ".pdf";
                String root = ServletActionContext.getServletContext().getRealPath(File.separator+"samplereport"+File.separator+reportfile) + File.separator;
                //判断报告目录是否存在
                if (!new File(root).exists()) {
                    new File(root).mkdirs();
                }
                File deskFile = new File(root,deskFileName + ".pdf");
                PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(deskFile));
                //加密（密码，可打印，可复制）									//"world1".getBytes()
                //stamp.setEncryption("Hello".getBytes(), null, PdfWriter.AllowPrinting | PdfWriter.AllowCopy, PdfWriter.STRENGTH40BITS);
                //只能打印
                stamp.setEncryption(null, null, PdfWriter.AllowPrinting, PdfWriter.STRENGTH40BITS);
                // 取出报表模板中的所有字段
                AcroFields form = stamp.getAcroFields();
                //LINUX
                String fontPath="/usr/share/fonts/winfonts/simsun.ttc,1";
                if(operSystem!=null&&operSystem.toLowerCase().contains("windows")){
                    //WINDOW
                     fontPath="C:/WINDOWS/Fonts/simsun.ttc,1";
                }
                BaseFont bf = BaseFont.createFont(fontPath,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                form.addSubstitutionFont(bf);
                // 为字段赋值,注意字段名称是区分大小写的
                form.setField("sampleNo",norovirus.getSampleNo());
                form.setField("name",norovirus.getName());
                form.setField("englishName",norovirus.getEnglishName());
                form.setField("passport",norovirus.getPassport());
                form.setField("sendingPerson", norovirus.getSendingPerson());
                form.setField("idcard",norovirus.getIdcard());
                form.setField("inspection",norovirus.getInspection());
                form.setField("sampleType",norovirus.getSampleType());
                form.setField("samplingDate",norovirus.getSamplingDate());
                form.setField("receptionDate",norovirus.getReceptionDate());//样本检测时间： 样本合格的入库时间
                form.setField("samplingDate1",norovirus.getSamplingDate());
                form.setField("receptionDate1",norovirus.getReceptionDate());
                form.setField("detectionResult", norovirus.getDetectionResult());
                form.setField("detectionResult1", norovirus.getDetectionResult());
                if(modelsvalue.equals("xgzyw.pdf")){
                    form.setField("detectionResult1", "NEGATIVE");
                    if(norovirus.getSamplingDate()!=null && !norovirus.getSamplingDate().equals("")){
                        form.setField("samplingDate",dateFormat.format(sdf.parse(norovirus.getSamplingDate())));
                    }
                    if(norovirus.getReceptionDate()!=null && !norovirus.getReceptionDate().equals("")){
                        form.setField("receptionDate",dateFormat.format(sdf.parse(norovirus.getReceptionDate())));
                    }

                }else if(modelsvalue.equals("xgywgz.pdf")){
                	form.setField("detectionResult", "NEGATIVE");
                }
                form.setField("ct",norovirus.getCt());
                form.setField("iss",norovirus.getIss());
                form.setField("year", date.substring(0,4));
                form.setField("month",date.substring(5,7));
                form.setField("day",date.substring(8,10));
                form.setField("shi",date.substring(11,13));
                form.setField("fen",date.substring(14,16));
                form.setField("yinyear",dateFormat1.format(sdf.parse(date)));
                //必须要调用这个，否则文档不会生成的
                stamp.setFormFlattening(true);

                stamp.close();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                log.error(e.toString());
            }

        }
        return pdfnames;
    }

    public static List<String> recursion(String root) {
        File file = new File(root);
        List<String> lists = new ArrayList<String>();
        File[] subFile = file.listFiles();
        for (int i = 0; i < subFile.length; i++) {
            if (subFile[i].isDirectory()) {
                recursion(subFile[i].getAbsolutePath());
            } else {
                String filename = subFile[i].getName();

                lists.add(filename);
            }
        }
        return lists;
    }

    /**
     * 合管生成报告模板
     */
    public void excelmuban(){
        HSSFWorkbook hssfworkbook = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet hssfsheet = hssfworkbook.createSheet("heguan");

        //sheet名称乱码处理
        hssfworkbook.setSheetName(0,"heguan");
        //取得第 i 行
        HSSFRow hssfrow1 = hssfsheet.createRow(0);
        //创建第一个单元格

        HSSFCellStyle style77 = hssfworkbook.createCellStyle();
        HSSFFont font77 = hssfworkbook.createFont();
        font77.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        style77.setFont(font77);
        style77.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style77.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style77.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style77.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style77.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style77.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        HSSFCell hssfcell_00 = hssfrow1.createCell((short)0);
        hssfcell_00.setCellStyle(style77);
        hssfcell_00.setCellValue("五合一管编号");
        hssfsheet.setColumnWidth(0, 20*256);

        HSSFCell hssfcell_01 = hssfrow1.createCell((short)1);
        hssfcell_01.setCellValue("检测结果");
        hssfsheet.setColumnWidth(1, 10*256);
        hssfcell_01.setCellStyle(style77);

        for(int i = 0;i<29;i++){
            //取得第 i 行
            HSSFRow hssfrow = hssfsheet.createRow(i+1);
            HSSFCell hssfcell_0 = hssfrow.createCell((short)0);
            hssfcell_0.setCellValue("");
            HSSFCellStyle style = hssfworkbook.createCellStyle();  //样式
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            hssfcell_0.setCellStyle(style);

            HSSFCell hssfcell_1 = hssfrow.createCell((short)1);
            hssfcell_1.setCellValue("");
            hssfcell_1.setCellStyle(style);
        }
        FileOutputStream file=null;
        try {
            String uploadFileSavePath= ServletActionContext.getServletContext().getRealPath(File.separator+"documents");
            String path=uploadFileSavePath+File.separator+"heguan.xls";
            file=new FileOutputStream(path);
            hssfworkbook.write(file);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(file!=null){
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String reportDateExcel(List<Norovirus> norovirusList,String path) throws IllegalAccessException {
    	WebPathUtil webPathUtil = new WebPathUtil();
    	String templatePath = webPathUtil.getRealPath(File.separator+"excelTemplate"+File.separator+"新冠肺炎核酸检测信息模版.xls");
    	Map<Integer,Norovirus> errorMap = new HashMap<Integer,Norovirus>();
        Map<Integer,Norovirus> erroridMap = new HashMap<Integer,Norovirus>();
    	try {
			InputStream in = new FileInputStream(new File(templatePath));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
			String nameRegEx = "[\\u4e00-\\u9fa5]{2,6}";
			Pattern namePattern = Pattern.compile(nameRegEx);
			HSSFWorkbook hssfworkbook = new HSSFWorkbook(in);
			//得到excel的第0张表
			HSSFSheet hssfSheet = hssfworkbook.getSheetAt(0);

			/**
			//得到第一行的单元格格式
			//格式不好用，很多不兼容的问题，这是一个方法，但是建议不要用了
			HSSFRow hssfRow = hssfSheet.getRow(1);
			HSSFCellStyle hssfCellStyle0 = hssfRow.getCell(0).getCellStyle();
			HSSFCellStyle hssfCellStyle1 = hssfRow.getCell(1).getCellStyle();
			HSSFCellStyle hssfCellStyle2 = hssfRow.getCell(2).getCellStyle();
			HSSFCellStyle hssfCellStyle3 = hssfRow.getCell(3).getCellStyle();
			HSSFCellStyle hssfCellStyle4 = hssfRow.getCell(4).getCellStyle();
			*/
			for (int i = 0; i < norovirusList.size(); i++) {
				Norovirus norovirus = norovirusList.get(i);
				String date = norovirus.getReportdate();
                norovirus.setName(norovirus.getName().replaceAll(" ", ""));
                norovirus.setIdcard(norovirus.getIdcard().replaceAll(" ", "").replace('✘', 'X'));

				if(norovirus.getName()==null || !NorovirusService.isNameWrod(norovirus.getName())){
                    System.out.println("姓名"+norovirus.toString());
					errorMap.put(i,norovirus);
				}

                if(norovirus.getIdcard()==null || !this.isIDNumber(norovirus.getIdcard())){
                    System.out.println("身份证"+norovirus.toString());
                    erroridMap.put(i,norovirus);
                }

				String dateRes = "";
				if(norovirus.getReportdate().length()>=19){
					dateRes = norovirus.getReportdate().substring(0,10).replace("-", "/")+" "+norovirus.getReportdate().substring(10,norovirus.getReportdate().length());
				}else{
					continue;
				}

				//取得第 i+1 行
				HSSFRow hssfrow = hssfSheet.createRow(i+1);
				HSSFCell hssfcell_0 = hssfrow.createCell(0);
				hssfcell_0.setCellValue(norovirus.getName());
				//hssfcell_0.setCellStyle(hssfCellStyle0);
				HSSFCell hssfcell_1 = hssfrow.createCell(1);
				hssfcell_1.setCellValue(norovirus.getIdcard());
				//hssfcell_1.setCellStyle(hssfCellStyle1);
				HSSFCell hssfcell_2 = hssfrow.createCell(2);
				hssfcell_2.setCellValue(norovirus.getDetectionResult());
				//hssfcell_2.setCellStyle(hssfCellStyle2);
				HSSFCell hssfcell_3 = hssfrow.createCell(3);
				hssfcell_3.setCellValue(dateRes);
				//hssfcell_3.setCellStyle(hssfCellStyle3);
				HSSFCell hssfcell_4 = hssfrow.createCell(4);
				hssfcell_4.setCellValue("北京优迅医学检验实验室");
				//hssfcell_4.setCellStyle(hssfCellStyle4);
			}
			FileOutputStream fileoutputstream1 = new FileOutputStream(path);
			hssfworkbook.write(fileoutputstream1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.toString());
		}
		String resStr = "Excel已导出";
    	if(errorMap.size()>0 || erroridMap.size()>0){
    		resStr+=",在";
    		for(Integer key:errorMap.keySet()){
    			resStr+=((key+2)+",");
    		}
            resStr = resStr.substring(0, resStr.length()-1)+"行数据名字存在特殊字符或长度不符合条件;\n";
            for(Integer key:erroridMap.keySet()){
                resStr+=((key+2)+",");
            }
            resStr = resStr.substring(0, resStr.length()-1)+"行数据身份证存在特殊字符或长度不符合条件,请留意";
    	}
        System.out.println(resStr);
    	return resStr;

    }

    @Transactional
	public String updateSendingPersons(List<Norovirus> ln) throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
    	for(Norovirus norovirus:ln){
    		log.info("sampleNo:"+norovirus.getSampleNo()+"\tsendingPerson:"+norovirus.getSendingPerson());
    		String sql1 = "select * from norovirus where sampleNo='"+norovirus.getSampleNo()+"'";
    		List<Norovirus> norovirusList = norovirusDao.entityBySql(sql1, Norovirus.class);
    		if(norovirusList.size()>0){
    			String sql2 = "update norovirus set sendingPerson ='"+norovirus.getSendingPerson()+"' where sampleNo='"+norovirus.getSampleNo()+"'";
    			norovirusDao.updateBySql(sql2);
    		}else{
    			sb.append(norovirus.getSampleNo()+",");
    		}
    	}
    	String res = "修改成功";
    	if(sb.toString().trim().length()>0){
    		res+=",其中样本编号是："+sb.toString().substring(0,sb.toString().length()-1)+"的样本无信息";
    	}
    	return res;
	}

	public Norovirus findByNameAndReservation(String name, String reservation) {
		// TODO Auto-generated method stub
		List<Norovirus> norovirusList = norovirusDao.find(Restrictions.eq("name", name),Restrictions.eq("reservation", reservation));
		if(norovirusList.size()>0){
			return norovirusList.get(0);
		}else{
			return null;
		}
	}

	public String getSql(String name,String idcard){
        String sql = "select * from norovirus where name = '"+name+"' AND idcard = '"+idcard+"' AND checkProject = '诺如病毒核酸检测' order by reportdate desc";
        return sql;
    }

    public Norovirus findByNameAndCard(String name, String idcard) {
        String sql = this.getSql(name,idcard);
        List<Norovirus> norovirusList = norovirusDao.entityBySql(sql,Norovirus.class);
        if(norovirusList.size()>0){
            return norovirusList.get(0);
        }else{
            return null;
        }
    }

	public List<Norovirus> getInSamples(List<String> sampleNos) {
		// TODO Auto-generated method stub
		return norovirusDao.createCriteria(Restrictions.in("sampleNo", sampleNos)).list();
	}


    public  boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾


        boolean matches = IDNumber.matches(regularExpression);

        //判断第18位校验值
        if (matches) {

            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

        }
        return matches;
    }

    public static boolean isNameWrod(String str){

        String pattern = "[\u4e00-\u9fa5]{2,6}";

        boolean isMatch = Pattern.matches(pattern, str);

        return isMatch;
    }

}
