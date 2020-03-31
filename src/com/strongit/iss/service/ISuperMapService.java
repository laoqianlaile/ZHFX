package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

import com.strongit.iss.entity.TCityCustomCoordinates;
import com.strongit.iss.exception.BusinessServiceException;


/**
 * <pre>
 *     ISuperMapService 接口
 *     空间政府精细化管理功能的接口
 * @author zhoupeng
 * @E-mai：zhoupeng@strongit.com.cn
 *  @Date 2016年10月10日下午3:36:18
 *  @see com.strongit.iss.service.ISuperMapService
 * </pre>
 */
public interface ISuperMapService {
	
	/**
	 *  <pre>
	 *   获取全国数据信息
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  filters
	 * 			-- 查询条件
	 * @param searchSql
	 * 			-- 查询条件
	 * @param orderbySql
	 * 			-- 排序
	 * 			
	 * @return
	 *     --  filters==null或者filters== ""  返回全国数据信息
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年11月3日上午10:36:17
	 * </pre>
	 */
	public List<Map<String,Object>> getQueryByGeometry(Map<String, String> filters,String searchSql,String orderbySql) throws BusinessServiceException ;
	
	
	
	/**
	 *  <pre>
	 *   获取空间政府精细化管理项目信息
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  params
	 * 			-- 查询条件
	 * 			
	 * @return
	 *     --  params==null  获取空间政府精细化管理所有的项目信息
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月19日上午10:36:17
	 * </pre>
	 */
	public List<Map<String,Object>> getSuperMapGovReportByMap(Map<String,String> params) throws BusinessServiceException ;
	
	
	
	/**
	 *  <pre>
	 *   保存空间政府精细化管理项目信息
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  params
	 * 			-- 查询条件
	 * @param employeeGuid
	 * 			-- 当前用户ID
	 * 			
	 * @return
	 *     --  filters==null或者filters== ""  返回保存事件成功
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月27日上午10:36:17
	 * </pre>
	 */
	public boolean saveCusCoordinates(Map<String,String> params,String employeeGuid);
	
	
	/**
	 *  <pre>
	 *   保存空间政府精细化管理项目信息
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  markInfo
	 * 			-- 自定义实体对象
	 * 			
	 * @return
	 *     --  markInfo==null  返回保存事件成功
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月27日上午10:36:17
	 * </pre>
	 */
	public void saveEntity(TCityCustomCoordinates markInfo);
	
	
	/**
	 *  <pre>
	 *   查询自定义区域的数据集
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  filters
	 * 			-- 查询条件
	 * @param employeeGuid
	 * 			-- 当前用户ID
	 * 			
	 * @return
	 *     --  filters==null或者filters== ""  返回当前用户所有数据
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月27日上午10:36:17
	 * </pre>
	 */
	public List<Map<String, Object>> searchCoordinates(Map<String,String> filters,String employeeGuid);
	
	
	
	/**
	 *  <pre>
	 *   删除空间政府精细化管理自定义区域的数据
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  params
	 * 			-- 查询条件
	 * 			
	 * @return
	 *     --  params==null或者params== ""  不做删除处理
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月27日上午10:36:17
	 * </pre>
	 */
	public boolean deleteCusCoordinates(Map<String,String> params);
	
	
	
}
