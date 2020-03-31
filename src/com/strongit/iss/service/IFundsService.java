package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

import com.strongit.iss.exception.BusinessServiceException;

public interface IFundsService {
	
	/**
	 * 专项地图报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月3日下午7:00:50
	 */
	public List<Map<String,Object>> getFundByMap(Map<String,String> filters,String querySql) throws BusinessServiceException ;
	
	/**
	 * 专项专项类别报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月3日下午7:03:46
	 */
	public List<Map<String,Object>> getFundBySpecialType(Map<String,String> filters,String querySql) throws BusinessServiceException ;
}
