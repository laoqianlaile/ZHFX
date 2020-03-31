package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

import com.strongit.iss.exception.BusinessServiceException;

public interface IFivePlanService {
	
	/**
	 * 五年项目储备分建设地点
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getFivePlanPlaceReportByMap(Map<String,String> filters,String querySql,String searchSql,String orderbySql) throws BusinessServiceException ; 

	/**
	 * 五年项目储备分国标行业
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getFivePlanGbindustryReportByMap(Map<String,String> filters,String querySql,String searchSql,String orderbySql) throws BusinessServiceException ; 

	
	/**
	 * 五年项目储备分所属行业
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getFivePlanIndustryReportByMap(Map<String,String> filters,String searchSql,String querySql,String orderbySql) throws BusinessServiceException ; 

	
	/**
	 * 五年项目储备分重大战略
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getFivePlanMajorstrategyReportByMap(Map<String,String> filters,String searchSql,String querySql,String orderbySql) throws BusinessServiceException ; 

	/**
	 * 五年项目储备分申报单位
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getFivePlanCreatedepartmentguidReportByMap(Map<String,String> filters,String searchSql,String querySql,String orderbySql) throws BusinessServiceException ; 


	/**
	 * 五年项目储备分储备级别
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getFivePlanStorelevelReportByMap(Map<String,String> filters,String searchSql,String querySql,String orderbySql) throws BusinessServiceException ; 


	/**
	 * 五年项目储备分入库时间
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getFivePlanStoretimeReportByMap(Map<String,String> filters,String searchSql,String querySql,String orderbySql) throws BusinessServiceException ; 


	/**
	 * 五年项目储备分项目类型
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:53:37
	 */
	public List<Map<String,Object>> getFivePlanProtypeReportByMap(Map<String,String> filters,String searchSql,String querySql,String orderbySql) throws BusinessServiceException ; 





}
