package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

import com.strongit.iss.exception.BusinessServiceException;

public interface IRollPlanService {
	
	/**
	 * 三年滚动计划地区
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getRollPlanPlaceReportByMap(Map<String,String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException ; 
	
	
	/**
	 * 三年滚动计划政府投资方向
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getRollPlanGovernmentReportByMap(Map<String,String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException ; 
	
	
	
	/**
	 * 三年滚动计划项目类型
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getRollPlanProtypeReportByMap(Map<String,String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException ; 
	
	
	
	/**
	 * 三年滚动计划阶段
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getRollPlanStageReportByMap(Map<String,String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException ; 
	
	
	/**
	 * 三年滚动计划国标行业
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getRollPlanGbindustryreportByMap(Map<String,String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException ; 
	
	
	/**
	 * 三年滚动计划所属行业
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getRollPlanIndustryReportByMap(Map<String,String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException ; 
	
	
	
	/**
	 * 三年滚动计划重大战略
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getRollPlanMajorstrategyReportByMap(Map<String,String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException ; 
	
	
	
	/**
	 * 三年滚动计划预计开工时间
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getRollPlanExpectstartyearReportByMap(Map<String,String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException ; 
	
	
	
	/**
	 * 三年滚动计划编入三年滚动计划时间
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getRollPlanCreatetimeReportByMap(Map<String,String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException ; 
	
	
}
