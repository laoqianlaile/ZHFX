package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

import com.strongit.iss.entity.ProjectFileSearchVo;
import com.strongit.iss.orm.hibernate.Page;

/**
 * @author XiaXiang
 *
 */
public interface IProjectFileService extends IBaseService {

	/**
	 * 根据项目ID获取项目履历信息
	 * @orderBy 
	 * @param projectGuid
	 * @param type
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:20:12
	 */
	public List<Map<String, Object>> getProjectRecordByGuid(String projectGuid, String type,String moduleCode);

	/**
	 * 获取基本信息
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:52:20
	 */
	public List<Map<String, Object>> getBaseInfoByGuid(String projectGuid,String moduleCode);
	
	/**
	 * 获取量化建设规模
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日上午11:16:49
	 */
	public List<Map<String, Object>> getQuaInfoByGuid(String projectGuid,String moduleCode);

	/**
	 * 审核备办理事项
	 * @orderBy 
	 * @param projectGuid
	 *  @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:53:24
	 */
	public List<Map<String, Object>> getMatterInfoByGuid(String projectGuid,String moduleCode);

	/**
	 * 投资情况
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:54:20
	 */
	public List<Map<String, Object>> getInvestmentInfoByGuid(String projectGuid,String moduleCode);

	/**
	 * 计划下达情况
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:54:51
	 */
	public List<Map<String, Object>> getIssuedInfoByGuid(String projectGuid,String moduleCode);

	/**
	 * 资金到位完成情况
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:55:25
	 */
	public List<Map<String, Object>> getFinishInfoByGuid(String projectGuid,String moduleCode);

	/**
	 * 各期项目调度数据
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:56:08
	 */
	public List<Map<String, Object>> getDispatchInfoByGuid(String projectGuid,String moduleCode);
	
	/**
	 * 根据项目id获取项目前期工作信息
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日上午11:39:40
	 */
	public List<Map<String, Object>> getPreworkInfoByGuid(String projectGuid,String moduleCode);
	
	/**
	 * 根据项目id获取项目资金构成信息
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日下午12:13:05
	 */
	public List<Map<String, Object>> getInvestConstituteInfoByGuid(String projectGuid,String moduleCode);
	
	/**
	 * 根据项目id获取项目调度项目实施情况
	 * @orderBy 
	 * @param projectGuid
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日下午4:29:59
	 */
	public List<Map<String, Object>> getDispatchTmplInfoByGuid(String projectGuid);

	/**
	 * 根据条件查询项目
	 * @orderBy 
	 * @param userId
	 * @param pageBo
	 * @param projectVo
	 * @param reportParamsMap
	 * @return
	 * @author tanghw
	 * @Date 2016年10月29日下午7:50:21
	 */
	public Page<Map<String, Object>> searchProject(String userId ,Page<Map<String, Object>>pageBo,
			ProjectFileSearchVo projectVo,Map<String, String> filters,Map<String, String> reportParamsMap);
	
	/*public Page<Map<String, Object>> searchWarningProject1(String userId ,Page<Map<String, Object>>pageBo,
			ProjectFileSearchVo projectVo,Map<String, String> filters,Map<String, String> reportParamsMap);*/
	
	public Page<Map<String, Object>> searchWNProject(String userId ,Page<Map<String, Object>>pageBo,
			ProjectFileSearchVo projectVo,Map<String, String> filters,Map<String, String> reportParamsMap);
	
	public Page<Map<String, Object>> searchSBProject(String userId ,Page<Map<String, Object>>pageBo,
			ProjectFileSearchVo projectVo,Map<String, String> filters,Map<String, String> reportParamsMap);
	
	public Page<Map<String, Object>> searchXDProject(String userId ,Page<Map<String, Object>>pageBo,
			ProjectFileSearchVo projectVo,Map<String, String> filters,Map<String, String> reportParamsMap);
	
	public Page<Map<String, Object>> searchDDProject(String userId ,Page<Map<String, Object>>pageBo,
			ProjectFileSearchVo projectVo,Map<String, String> filters,Map<String, String> reportParamsMap);
	
	/**
	 * 根据条件查询项目
	 * @orderBy 
	 * @param pageBo
	 * @param projectVo 页面查询条件
	 * @param filter    地图参数集合
	 * @return
	 * @author tanghw
	 * @Date 2016年10月31日下午3:51:56
	 */
	public Page<Map<String, Object>> searchFileProject(Page<Map<String, Object>>pageBo,
			ProjectFileSearchVo projectVo,Map<String, String> filters);
	
	/**
	 * 获取前三个调度项目实施信息
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年11月2日下午1:35:06
	 */
	public List<Map<String, Object>> getTopThirdDispatchDetails();
	
	/**
	 * 项目档案导出
	 * @orderBy 
	 * @param projectGuid 项目名称
	 * @param moduleCode  模块名称
	 * @param fileName fileName下载word的路径文件名称
	 * @author tanghw
	 * @Date 2016年11月29日下午3:15:54
	 */
	public void exportWord(String projectGuid,String moduleCode,String filePath,String fileName);
	/**
	 * 根据条件查询项目
	 * @orderBy
	 * @param pageBo
	 * @param projectVo 页面查询条件
	 *
	 * @param filters    预警的参数
	 * @return
	 * @author tanghw
	 * @Date 2016年10月31日下午3:51:56
	 */
	public  Page<Map<String,Object>> searchNVProject(String userId, Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo, Map<String, String> filters, Map<String, String> reportParamsMap);
	
	/**
	 * 根据条件查询项目
	 * @param userId
	 * @param pageBo
	 * @param projectVo
	 * @param reportParamsMap
	 * @return
	 * @author zpd
	 * @Date 2017-11-03
	 */
	public Page<Map<String, Object>> searchSNProject(String userId ,Page<Map<String, Object>>pageBo,
			ProjectFileSearchVo projectVo,Map<String, String> filters,Map<String, String> reportParamsMap);

	Page<Map<String, Object>> searchWarningProject(String userId, Page<Map<String, Object>> pageBo,
			ProjectFileSearchVo projectVo, Map<String, String> filters, Map<String, String> reportParamsMap);
	
	
}
