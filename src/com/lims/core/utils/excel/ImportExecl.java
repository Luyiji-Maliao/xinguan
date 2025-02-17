
package com.lims.core.utils.excel;

import java.io.BufferedInputStream;
import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;

import java.io.InputStream;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @描述：excel读取
 * 
 *               导入的jar包
 * 
 *               poi-3.8-beta3-20110606.jar
 * 
 *               poi-ooxml-3.8-beta3-20110606.jar
 * 
 *               poi-examples-3.8-beta3-20110606.jar
 * 
 *               poi-excelant-3.8-beta3-20110606.jar
 * 
 *               poi-ooxml-schemas-3.8-beta3-20110606.jar
 * 
 *               poi-scratchpad-3.8-beta3-20110606.jar
 * 
 *               xmlbeans-2.3.0.jar
 * 
 *               dom4j-1.6.1.jar
 * 
 *               jar包官网下载地址：http://poi.apache.org/download.html
 * 
 *               下载poi-bin-3.8-beta3-20110606.zipp

 */

public class ImportExecl
{
	Logger log = LoggerFactory.getLogger(ImportExecl.class);

	/** 总行数 */

	private int totalRows = 0;

	/** 总列数 */

	private int totalCells = 0;

	/** 错误信息 */

	private String errorInfo;

	/** 构造方法 */

	public ImportExecl()
	{

	}

	/**
	 * 
	 * @描述：得到总行数

	 */

	public int getTotalRows()
	{

		return totalRows;

	}

	/**
	 * 
	 * @描述：得到总列数
	 * 
	
	 */

	public int getTotalCells()
	{

		return totalCells;

	}

	/**
	 * 
	 * @描述：得到错误信息
	
	 */

	public String getErrorInfo()
	{

		return errorInfo;

	}

	/**
	 * 
	 * @描述：验证excel文件
	
	 */

	public boolean validateExcel(String filePath)
	{

		/** 检查文件名是否为空或者是否是Excel格式的文件 */

		if (filePath == null || !(WDWUtil.isExcel2003(filePath) || WDWUtil.isExcel2007(filePath)))
		{

			errorInfo = "文件名不是excel格式";

			return false;

		}

		/** 检查文件是否存在 */

		File file = new File(filePath);

		if (file == null || !file.exists())
		{

			errorInfo = "文件不存在";

			return false;

		}

		return true;

	}

	/**
	 * 
	 * @描述：根据文件名读取excel文件
	 * 
	
	 */

	public List<List<String>> read(String filePath)
	{

		List<List<String>> dataLst = new ArrayList<List<String>>();

		InputStream is = null;

		try
		{

			/** 验证文件是否合法 */

			if (!validateExcel(filePath))
			{


				return null;

			}

			/** 判断文件的类型，是2003还是2007 */

			boolean isExcel2003 = true;

//			if (WDWUtil.isExcel2007(filePath))
//			{
//
//				isExcel2003 = false;
//
//			}

			/** 调用本类提供的根据流读取的方法 */

			File file = new File(filePath);

			is = new FileInputStream(file);
			
			BufferedInputStream bis = new BufferedInputStream(is);
			
			//根据数据流判断Excel版本
			if(POIFSFileSystem.hasPOIFSHeader(bis)){
				isExcel2003=true;
			}
			if(POIXMLDocument.hasOOXMLHeader(bis)){
				isExcel2003=false;
			}

			dataLst = read(bis, isExcel2003);

			is.close();

		}
		catch (Exception ex)
		{

			ex.printStackTrace();

		}
		finally
		{

			if (is != null)
			{

				try
				{

					is.close();

				}
				catch (IOException e)
				{

					is = null;

					e.printStackTrace();

				}

			}

		}

		/** 返回最后读取的结果 */

		return dataLst;

	}

	/**
	 * 
	 * @描述：根据流读取Excel文件
	 * 
	
	 */

	public List<List<String>> read(InputStream inputStream, boolean isExcel2003)
	{

		List<List<String>> dataLst = null;

		try
		{

			/** 根据版本选择创建Workbook的方式 */

			Workbook wb = null;

			if (isExcel2003)
			{
				wb = new HSSFWorkbook(inputStream);
			}
			else
			{
				wb = new XSSFWorkbook(inputStream);
			}
			dataLst = read(wb);

		}
		catch (IOException e)
		{

			e.printStackTrace();

		}

		return dataLst;

	}

	/**
	 * 
	 * @描述：读取数据
	 * 
	 * 
	 */

	private List<List<String>> read(Workbook wb)
	{

		List<List<String>> dataLst = new ArrayList<List<String>>();

		/** 得到第一个shell */

		Sheet sheet = wb.getSheetAt(0);
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator(); 
		/** 得到Excel的行数 */

		this.totalRows = sheet.getPhysicalNumberOfRows();

		/** 得到Excel的列数 */

		if (this.totalRows >= 1 && sheet.getRow(0) != null)
		{

			this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();

		}

		/** 循环Excel的行 */

		for (int r = 0; r < this.totalRows; r++)
		{

			Row row = sheet.getRow(r);

			if (row == null)
			{

				continue;

			}

			List<String> rowLst = new ArrayList<String>();

			/** 循环Excel的列 */

			for (int c = 0; c < this.getTotalCells(); c++)
			{

				Cell cell = row.getCell(c);

				String cellValue = "";
				
				if (null != cell)
				{
					
					// 以下是判断数据的类型
					switch (cell.getCellType())
					{
					case HSSFCell.CELL_TYPE_NUMERIC: // 数字
						
						
						if(HSSFDateUtil.isCellDateFormatted(cell)){
							
			                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			                
			                

			                cellValue=  sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
			           
						}else{
							
							Object inputValue = null;// 单元格值
							Long longVal = Math.round(cell.getNumericCellValue());
							Double doubleVal = cell.getNumericCellValue();
							 if(Double.parseDouble(longVal + ".0") == doubleVal){   //判断是否含有小数位.0
					              inputValue = longVal;
					          }else{
					              inputValue = doubleVal;
					          }
							 
							 DecimalFormat df = new DecimalFormat("#.####");    //格式化为四位小数
							 cellValue=String.valueOf(df.format(inputValue));      //返回String类型
						
						}

						
						break;

					case HSSFCell.CELL_TYPE_STRING: // 字符串
						
						cellValue = cell.getStringCellValue();
						break;

					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						
						cellValue = cell.getBooleanCellValue() + "";
						break;

					case HSSFCell.CELL_TYPE_FORMULA: // 公式
						evaluator.evaluateFormulaCell(cell);  
						cellValue = cell.getNumericCellValue() + "";
						break;

					case HSSFCell.CELL_TYPE_BLANK: // 空值
						cellValue = "";
						break;

					case HSSFCell.CELL_TYPE_ERROR: // 故障
						cellValue = "非法字符";
						break;

					default:
						cellValue = "未知类型";
						break;
					}
				}

				rowLst.add(cellValue);

			}

			/** 保存第r行的第c列 */

			dataLst.add(rowLst);

		}

		return dataLst;

	}

	/**
	 * 
	 * @描述：main测试方法
	 * 
	 * 
	 */

	public static void main(String[] args) throws Exception
	{

		ImportExecl poi = new ImportExecl();

		// List<List<String>> list = poi.read("d:/aaa.xls");

		List<List<String>> list = poi.read("e:/ceshi/1.xlsx");

		if (list != null)
		{

			for (int i = 0; i < list.size(); i++)
			{

				System.out.print("第" + (i) + "行");

				List<String> cellList = list.get(i);

				for (int j = 0; j < cellList.size(); j++)
				{

					// System.out.print("    第" + (j + 1) + "列值：");

					System.out.print("    " + cellList.get(j));

				}
				System.out.println();

			}

		}

	}

}

/**
 * 
 * @描述：工具类
 * 
 
 */

class WDWUtil
{

	/**
	 * 
	 * @描述：是否是2003的excel，返回true是2003
	 * 
	
	 */

	public static boolean isExcel2003(String filePath)
	{

		return filePath.matches("^.+\\.(?i)(xls)$");

	}

	/**
	 * 
	 * @描述：是否是2007的excel，返回true是2007
	 * 
	
	 */

	public static boolean isExcel2007(String filePath)
	{

		return filePath.matches("^.+\\.(?i)(xlsx)$");

	}

}

