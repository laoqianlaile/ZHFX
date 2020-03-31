package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

import com.strongit.iss.service.IBaseService;

public interface IScreenSaverService extends IBaseService {

	/**
	 * 获取总用户数
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:27:16
	 */
	public String getAllUserCounts();

	/**
	 * 获取活跃用户数
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:27:16
	 */
	public String getActiveUserCounts(String date);

	/**
	 * 五年规划项目储备相关信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:27:16
	 */
	public Object[] getFivePlanInfo();

	/**
	 * 三年滚动计划相关信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:27:16
	 */
	public Object[] getThreePlanInfo();

	/**
	 * 项目审核备办理信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:27:16
	 */
	public Object[] getProjectAuditInfo();

	/**
	 * 中央预算内投资信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:27:16
	 */
	public Object[] getCentralBudgetInfo();

	/**
	 * 专项建设项目信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:27:16
	 */
	public Object[] getSpecialConstructionInfo();
	
	/**
	 * 获取活跃用户地区信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:27:16
	 */
	public List<Map<String, Object>> getPositionInfo(String date);

	/***
	 *   登录认证
	 * @orderBy 
	 * @param  
	 * @return 
	 * @author tannc
	 * @Date 2016/10/29 13:18
	 **/
	public Map<String,Object> login(String username, String encode);
}

