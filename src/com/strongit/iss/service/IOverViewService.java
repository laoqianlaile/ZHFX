package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

import com.strongit.iss.exception.BusinessServiceException;

public interface IOverViewService {
	
	/**
	 * 审核备地图
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月3日上午10:36:17
	 */
	public List<Map<String,Object>> getAuditPreparationByPlace(Map<String,String> filters) throws BusinessServiceException ;
	
	/**
	 * 审核备国标行业
	 * @orderBy 
	 * @param params
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月3日上午10:36:40
	 */
	public List<Map<String, Object>> getAuditPreparationByGBIndustry(Map<String, String> params) throws BusinessServiceException ;
	
	/**
	 * 审核备趋势
	 * @orderBy 
	 * @param params
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月3日上午10:37:03
	 */
	public List<Map<String, Object>> getAuditPreparationTrends(Map<String, String> params) throws BusinessServiceException ;

}
