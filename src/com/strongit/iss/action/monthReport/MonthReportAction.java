package com.strongit.iss.action.monthReport;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.service.IMonthReportService;

/**
 * 
 * @author li
 *
 */
@SuppressWarnings("serial")
public class MonthReportAction extends BaseActionSupport<Object> {
	
	private String replaymonth1;
	private String replaymonth2;
	private String downloadPath;
	
	String filePath=ServletActionContext.getServletContext().getRealPath("/");
	String zipPath = filePath+"files/download/";
	
	
	@Autowired
	private IMonthReportService monthReportService;
	
	/**
	 * 综合分析报告
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日下午4:12:08
	 */
	public String details() {
		//replaymonth1 = "201601";
		//replaymonth2 = "201606";
		return "details";
	}
	
  /**
	 * 获取简要情况
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:24:34
	 */
/*	public String getBriefingInfo(){
		//月份ID
		replaymonth = Struts2Utils.getRequest().getParameter("replaymonth");
		//简要信息
		List<Map<String, Object>> list = monthReportService.getBriefingInfoByMonth(replaymonth);
		//返回数据
		//if (null != list && list.size() > 0) {
		//	Struts2Utils.renderJson(list);
		//}
		Struts2Utils.renderJson(list);
		return null;
	}*/
	
	/**
	 * 获取分行业情况统计
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:25:39
	 */
	public String getIndustryInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//分行业情况信息
		List<Map<String, Object>> list = monthReportService.getIndustryInfoByMonth(replaymonth1,replaymonth2);
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getIndustryInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getIndustryInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		//if (null != list && list.size() > 0) {
		//	Struts2Utils.renderJson(list);
		//}
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取分地区情况统计
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:26:00
	 */
	public String getAreaInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//分地区情况信息
		List<Map<String, Object>> list = monthReportService.getAreaInfoByMonth(replaymonth1,replaymonth2);
		
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getAreaInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getAreaInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		//if (null != list && list.size() > 0) {
		//	Struts2Utils.renderJson(list);
		//}
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取东三省情况统计
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:26:00
	 */
	public String getEastAreaInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//分地区情况信息
		List<Map<String, Object>> list = monthReportService.getEastAreaInfoByMonth(replaymonth1,replaymonth2);
		//List<Map<String, Object>> list2 = monthReportService.getEastTotalInfoByMonth(replaymonth1,replaymonth2);
		//Map<String,Object>  alllist=new HashMap<String,Object>();
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getEastAreaInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getEastAreaInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		//if (null != list && list.size() > 0) {
		//	Struts2Utils.renderJson(list);
		//}
		//if(null!=list2&list2.size()>0){
			// 把合计放在第一位
			//list1.add(0, list2.get(0));
		//}
		//alllist.put("list1", list1);	
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取分产业情况统计
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:26:10
	 */
	public String getPropertyInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//分产业情况信息
		List<Map<String, Object>> list = monthReportService.getPropertyInfoByMonth(replaymonth1,replaymonth2);
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getPropertyInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getPropertyInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		//if (null != list && list.size() > 0) {
		//	Struts2Utils.renderJson(list);
		//}
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取分项目类型情况统计
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:26:20
	 */
	public String getProjectTypeInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//分项目类型情况信息
		List<Map<String, Object>> list = monthReportService.getProjectTypeInfoByMonth(replaymonth1,replaymonth2);
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getProjectTypeInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getProjectTypeInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		//if (null != list && list.size() > 0) {
		//	Struts2Utils.renderJson(list);
		//}
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取分项目类型情况统计
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:26:20
	 */
	public String getInvestTypeInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//分项目类型情况信息
		List<Map<String, Object>> list = monthReportService.getInvestTypeInfoByMonth(replaymonth1,replaymonth2);
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getInvestTypeInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getInvestTypeInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		//if (null != list && list.size() > 0) {
		//	Struts2Utils.renderJson(list);
		//}
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取分投资规模情况统计
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:26:30
	 */
	public String getInvestScaleInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//分投资规模情况信息
		List<Map<String, Object>> list = monthReportService.getInvestScaleInfoByMonth(replaymonth1,replaymonth2);
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getInvestScaleInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getInvestScaleInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		//if (null != list && list.size() > 0) {
		//	Struts2Utils.renderJson(list);
		//}
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取分开工时间情况统计
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:26:41
	 */
	public String getStartTimeInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//简要信息
		List<Map<String, Object>> list = monthReportService.getStartTimeInfoByMonth(replaymonth1,replaymonth2);
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getStartTimeInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getStartTimeInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		//if (null != list && list.size() > 0) {
		//	Struts2Utils.renderJson(list);
		//}
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取总计信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月29日下午3:51:50
	 */
	public String getTotalInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//分项目类型情况信息
		List<Map<String, Object>> list = monthReportService.getTotalInfoByMonth(replaymonth1,replaymonth2);
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getTotalInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getTotalInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取东三省总计信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月29日下午3:51:50
	 */
	public String getEastTotalInfo(){
		//月份ID
//		replaymonth1 = Struts2Utils.getRequest().getParameter("replaymonth1");
//		replaymonth2 = Struts2Utils.getRequest().getParameter("replaymonth2");
		long start =System.currentTimeMillis();
		//分项目类型情况信息
		List<Map<String, Object>> list = monthReportService.getEastTotalInfoByMonth(replaymonth1,replaymonth2);
		long end =System.currentTimeMillis();
		logger.debug("execute monthReportServiceImpl.getEastTotalInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		System.out.print("execute monthReportServiceImpl.getEastTotalInfoByMonth method cost time : "
				+ (end - start) + " mills.");
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 生成并下载word文档
	 * @orderBy 
	 * @return
	 * @author li
	 * @throws IOException 
	 * @Date 2016年11月29日下午3:49:59
	 */
	public void getWordInfo() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		//年份月份ID
    	String year=replaymonth2.substring(0,4);
    	String month=replaymonth2.substring(4);
    	//获取相对路径
		String filePath=ServletActionContext.getServletContext().getRealPath("/files/download/");
		String zipPath = filePath+"/";
		//获取Word文件名
		String fileName=year+"年"+month+"月审核备月报.docx";	
		String name=fileName;
        try{
			File file = new File(zipPath+fileName);
			if (file != null && file.exists()) {
				response.setContentType("text/html;charset=utf-8");
				response.setContentType("application/octet-stream;");
				response.setHeader(
						"Content-disposition",
						"attachment; filename="
								+ new String(name.getBytes("GBK"), "ISO8859-1"));
				response.setHeader("Content-Length",
						String.valueOf(file.length()));
				// 设置缓存
				inputStream = new BufferedInputStream(new FileInputStream(file));
				outputStream = new BufferedOutputStream(
						response.getOutputStream());
				byte[] buff = new byte[2048];
				int bytesRead;
				while (-1 != (bytesRead = inputStream
						.read(buff, 0, buff.length))) {
					outputStream.write(buff, 0, bytesRead);
				}
			} else {
				this.monthReportService.createWord(replaymonth1,replaymonth2,zipPath,fileName);	
				 file = new File(zipPath+fileName);
				response.setContentType("text/html;charset=utf-8");
				response.setContentType("application/octet-stream;");
				response.setHeader(
						"Content-disposition",
						"attachment; filename="
								+ new String(name.getBytes("GBK"), "ISO8859-1"));
				response.setHeader("Content-Length",
						String.valueOf(file.length()));
				// 设置缓存
				inputStream = new BufferedInputStream(new FileInputStream(file));
				outputStream = new BufferedOutputStream(
						response.getOutputStream());
				byte[] buff = new byte[2048];
				int bytesRead;
				while (-1 != (bytesRead = inputStream
						.read(buff, 0, buff.length))) {
					outputStream.write(buff, 0, bytesRead);
				}
			}
	    } 
        catch (Exception e) {
        	logger.error(e.getMessage(), e);
	    } finally {
	    	if (inputStream != null) {
	    		inputStream.close();
	    	}
	     if (outputStream != null) {
	    	 outputStream.close();
		 }
       }
		 	
	}
	
   //获取变量的get和set方法
	public String getReplaymonth1() {
		return replaymonth1;
	}

	public void setReplaymonth1(String replaymonth1) {
		this.replaymonth1 = replaymonth1;
	}

	public String getReplaymonth2() {
		return replaymonth2;
	}

	public void setReplaymonth2(String replaymonth2) {
		this.replaymonth2 = replaymonth2;
	}
	
	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
}
