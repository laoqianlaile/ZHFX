package com.strongit.iss.service.neuinterface;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.entity.ProjectFileSearchVo;
import com.strongit.iss.orm.hibernate.Page;

/**
 * 项目档案实现层接口
 * @author xiangyong
 * @Date 2016年7月23日 下午4:20:22
 */
public interface IProjectFileService2 {
  
	/**
	 * 根据条件查询项目
	 * @param pageBo
	 * @param startYear
	 * @param endYear
	 * @param projectName
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<Map<String, Object>> searchProject(Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo, Map<String, String> filters);
	
	@Transactional(readOnly=true)
	public Page<Map<String, Object>> searchMapProject(Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo, Map<String, String> filters);
	
	@Transactional(readOnly=true)
	public List getNameByCode(String code, String kind);

	@Transactional(readOnly=true)
	Page<Map<String, Object>> searchItem(Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo, Map<String, String> filters);

	

}
