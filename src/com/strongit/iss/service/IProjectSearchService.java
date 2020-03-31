package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

public interface IProjectSearchService {
	/**
	 *  得到行业的数据字典
	 * @return
	 */
	List<Map<String, Object>> dicIndustry(String groupNo);
	/**
	 * 获取项目申报单位
	 * @orderBy 
	 * @return
	 * @author xiangyong
	 * @Date 2016年8月2日下午2:36:16
	 */
	List<Map<String, Object>> getProjectUnit();
}
