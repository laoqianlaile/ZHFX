package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

import com.strongit.iss.exception.BusinessServiceException;

public interface IBudgetService {
	
	/**
	 * 中央预算内申报地图报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月21日下午2:53:37
	 */
	public List<Map<String,Object>> getBudgetReportByMap(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报发改委行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月22日下午1:15:54
	 */
	public List<Map<String,Object>> getBudgetReportByIndustry(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报国标行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午9:55:34
	 */
	public List<Map<String,Object>> getBudgetReportByGBIndustry(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报重大战略报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午10:42:08
	 */
	public List<Map<String,Object>> getBudgetReportByMajor(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报政府投资方向报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午10:46:46
	 */
	public List<Map<String,Object>> getBudgetReportByGovernment(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报审核备类型报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午10:51:16
	 */
	public List<Map<String,Object>> getBudgetReportByProjectType(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报申报部门报表数据（地方发改委，中央部门，央企）
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午11:00:07
	 */
	public List<Map<String,Object>> getBudgetReportByDepartmentTypeLv1(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报申报部门报表数据（发改委）
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午11:09:55
	 */
	public List<Map<String,Object>> getBudgetReportByDepartmentTypeLv2(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报委内司局报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午11:18:58
	 */
	public List<Map<String,Object>> getBudgetReportByWNSJ(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报申报时间报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午11:24:51
	 */
	public List<Map<String,Object>> getBudgetReportBySubmitTime(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内申报投资规模
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月23日下午6:59:51
	 */
	public List<Map<String,Object>> getBudgetReportByTZGM(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内下达地图报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:08:25
	 */
	public List<Map<String,Object>> getBudgetIssuedByMap(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内下达发改委行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:16:51
	 */
	public List<Map<String,Object>> getBudgetIssuedByIndustry(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内下达国标行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:19:49
	 */
	public List<Map<String,Object>> getBudgetIssuedByGBIndustry(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内下达重大战略报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:21:34
	 */
	public List<Map<String,Object>> getBudgetIssuedByMajor(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内下达政府投资方向报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:24:01
	 */
	public List<Map<String,Object>> getBudgetIssuedByGovernment(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内下达委内司局报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:25:56
	 */
	public List<Map<String,Object>> getBudgetIssuedByWNSJ(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内下达下达时间报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:27:26
	 */
	public List<Map<String,Object>> getBudgetIssuedByIssuedTime(Map<String,String> filters,String sql) throws BusinessServiceException ;
	
	/**
	 * 中央预算内下达投资规模
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月23日下午6:59:51
	 */
	public List<Map<String,Object>> getBudgetIssuedByTZGM(Map<String,String> filters,String sql) throws BusinessServiceException ;

	/**
	 * 中央预算内项目调度分地区报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:26
	 */
	public List<Map<String,Object>> getBudgetPlaceDispatch(Map<String,String> filters,String sql,String searchSql,String orderbySql) throws BusinessServiceException ;


	
	/**
	 * 中央预算内项目调度分拟建成时间报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:26
	 */
	public List<Map<String,Object>> getBudgetEndtimeDispatch(Map<String,String> filters,String sql,String searchSql,String orderbySql) throws BusinessServiceException ;

	
	/**
	 * 中央预算内项目调度分形象进度报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:26
	 */
	public List<Map<String,Object>> getBudgetImageprogressDispatch(Map<String,String> filters,String sql,String searchSql,String orderbySql) throws BusinessServiceException ;

	
	/**
	 * 中央预算内项目调度分国标行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:26
	 */
	public List<Map<String,Object>> getBudgetGbindustrydispatch(Map<String,String> filters,String sql,String searchSql,String orderbySql) throws BusinessServiceException ;

	
	/**
	 * 中央预算内项目调度分所属行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:26
	 */
	public List<Map<String,Object>> getBudgetIndustryDispatch(Map<String,String> filters,String sql,String searchSql,String orderbySql) throws BusinessServiceException ;

	
	/**
	 * 中央预算内项目调度分政府投资方向报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:26
	 */
	public List<Map<String,Object>> getBudgetGovinvestdirectiondispatch(Map<String,String> filters,String sql,String searchSql,String orderbySql) throws BusinessServiceException ;

}
