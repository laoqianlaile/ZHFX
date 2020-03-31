package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author li
 *
 */
public interface IMonthReportService extends IBaseService {
	
	/**
	 * 获取分行业情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:14:22
	 */
	List<Map<String, Object>> getIndustryInfoByMonth(String replaymonth1,String replaymonth2);

	/**
	 * 获取分地区情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:14:43
	 */
	List<Map<String, Object>> getAreaInfoByMonth(String replaymonth1,String replaymonth2);
	
	/**
	 * 获取东三省情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:14:43
	 */
	List<Map<String, Object>> getEastAreaInfoByMonth(String replaymonth1,String replaymonth2);

	/**
	 * 获取分产业情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:14:49
	 */
	List<Map<String, Object>> getPropertyInfoByMonth(String replaymonth1,String replaymonth2);

	/**
	 * 获取分项目类型情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:14:55
	 */
	List<Map<String, Object>> getProjectTypeInfoByMonth(String replaymonth1,String replaymonth2);
	
	/**
	 * 获取分投资类型情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:14:55
	 */
	List<Map<String, Object>> getInvestTypeInfoByMonth(String replaymonth1,String replaymonth2);

	/**
	 * 获取分投资规模情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:15:01
	 */
	List<Map<String, Object>> getInvestScaleInfoByMonth(String replaymonth1,String replaymonth2);

	/**
	 * 获取分开工时间情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:15:08
	 */
	List<Map<String, Object>> getStartTimeInfoByMonth(String replaymonth1,String replaymonth2);
	
	/**
	 * 获取总计信息
	 * @orderBy 
	 * @param replaymonth1
	 * @param replaymonth2
	 * @return
	 * @author li
	 * @Date 2016年10月29日下午3:49:10
	 */
	List<Map<String, Object>> getTotalInfoByMonth(String replaymonth1,String replaymonth2);
	
	/**
	 * 获取东三省总计信息
	 * @orderBy 
	 * @param replaymonth1
	 * @param replaymonth2
	 * @return
	 * @author li
	 * @Date 2016年10月29日下午3:49:10
	 */
	List<Map<String, Object>> getEastTotalInfoByMonth(String replaymonth1,String replaymonth2);
	
	/**
	 * 获取各分地区总计信息
	 * @orderBy 
	 * @param replaymonth1
	 * @param replaymonth2
	 * @return
	 * @author li
	 * @Date 2016年10月29日下午3:49:10
	 */
	List<Map<String, Object>> getAllTotalInfoByMonth(String replaymonth1,String replaymonth2);
	
	/**
	 * 生成word文档
	 * @orderBy 
	 * @param replaymonth1
	 * @param replaymonth2
	 * @return
	 * @author li
	 * @Date 2016年11月29日下午3:43:51
	 */
	void createWord(String replaymonth1,String replaymonth2,String filePath,String fileName);

}
