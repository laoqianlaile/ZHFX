package com.strongit.iss.common;

import java.awt.Color;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StrongCellStyle {

	/**
	 * 正常的单元格样式
	 * @author			肖红飞
	 * @createDate		2014-10-22上午11:15:37
	 * @arithMetic                                                                           
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle normalStyle03(HSSFWorkbook workbook){
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置边框样式
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		return cellStyle;
	}
	
	/**
	 * 红色字体带粉色背景提示的的单元格样式
	 * @author			xhf
	 * @createDate		2015年4月10日下午1:52:52
	 * @arithMetic                                                                           
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle redFontremindStyle03(HSSFWorkbook workbook){
		HSSFCellStyle cellStyle = StrongCellStyle.remindStyle03(workbook);
		//获得字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.RED.index);
		cellStyle.setFont(font);//选择需要用到的字体格式
		return cellStyle;
	}
	
	/**
	 * 正常带背景提示的的单元格样式
	 * @author			xhf
	 * @createDate		2015年4月10日下午1:52:52
	 * @arithMetic                                                                           
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle remindStyle03(HSSFWorkbook workbook){
		HSSFCellStyle cellStyle = StrongCellStyle.normalStyle03(workbook);
		HSSFPalette palette = workbook.getCustomPalette();  //wb HSSFWorkbook对象
		palette.setColorAtIndex((short)9, (byte) (198), (byte) (224), (byte) (180));
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor((short)9);
		return cellStyle;
	}
	
	/**
	 * 白色字体的样式
	 * @author			xhf
	 * @createDate		2015年4月10日下午1:52:44
	 * @arithMetic                                                                           
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle whiteFontStyle03(HSSFWorkbook workbook){
		//得到正常的样式
		HSSFCellStyle cellStyle = StrongCellStyle.normalStyle03(workbook);
		//获得字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.WHITE.index);
		cellStyle.setFont(font);//选择需要用到的字体格式
		return cellStyle;
	}
	
	public static XSSFCellStyle normalStyle07(XSSFWorkbook workbook){
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 设置边框样式
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		
		return cellStyle;
	}
	
	/**
	 * 错误的样式
	 * @author			肖红飞
	 * @createDate		2014-10-22上午11:15:21
	 * @arithMetic                                                                           
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle errorStyle03(HSSFWorkbook workbook){
		HSSFCellStyle cellStyle = StrongCellStyle.normalStyle03(workbook);
		HSSFPalette palette = workbook.getCustomPalette();  //wb HSSFWorkbook对象
		palette.setColorAtIndex((short)10, (byte) (255), (byte) (151), (byte) (151));
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor((short)10);
		
		return cellStyle;
	}
	
	public static XSSFCellStyle errorStyle07(XSSFWorkbook workbook){
		XSSFCellStyle cellStyle = StrongCellStyle.normalStyle07(workbook);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(new XSSFColor( new Color(255,151,151)));
		return cellStyle;
	}
	
	/**
	 * 加粗字体的样式
	 * @author			肖红飞
	 * @createDate		2014-10-22下午01:31:33
	 * @arithMetic                                                                           
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle blodStyle(HSSFWorkbook workbook){
		//得到正常的样式
		HSSFCellStyle cellStyle = StrongCellStyle.normalStyle03(workbook);
		//获得字体
		HSSFFont font = workbook.createFont();

		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		font.setFontHeightInPoints((short) 11);//设置字体大小
		cellStyle.setFont(font);//选择需要用到的字体格式
		
		return cellStyle;
	}

	/**
	 * 红色加粗字体
	 * @author			xhf
	 * @createDate		2015年6月19日下午2:54:05
	 * @arithMetic                                                                           
	 * @param hwb
	 * @return
	 */
	public static HSSFCellStyle redblodStyle(HSSFWorkbook workbook) {
		//得到正常的样式
		HSSFCellStyle cellStyle = StrongCellStyle.normalStyle03(workbook);
		//获得字体
		HSSFFont font = workbook.createFont();

		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		font.setFontHeightInPoints((short) 11);//设置字体大小
		font.setColor(HSSFColor.RED.index);
		cellStyle.setFont(font);//选择需要用到的字体格式
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平方向的对齐方式
		return cellStyle;
	}
	
	
}
