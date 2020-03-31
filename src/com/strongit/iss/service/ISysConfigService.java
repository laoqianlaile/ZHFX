package com.strongit.iss.service;

import java.util.List;

import com.strongit.iss.entity.TSysConfig;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.IBaseService;

/**
 * 
 * @author li
 * @Date 2016年12月7日上午11:17:29
 */
public interface ISysConfigService extends IBaseService{
	
	/**
	 * 根据菜单名称查询菜单列表
	 * @orderBy 
	 * @param page
	 * @param name
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:36:10
	 */
	public Page<TSysConfig> getConfigList(Page<TSysConfig> page, String configName, String configType);

	/**
	 * 根据ID获取对象
	 * @orderBy 
	 * @param guid
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:36:28
	 */
	public TSysConfig getBySysid(String sysid);

	
	
	/**
	 * 根据ID获取对象
	 * @orderBy 
	 * @param guid
	 * @return
	 * @author zhoupeng
	 * @Date 2016年01月13日下午3:36:28
	 */
	public List<TSysConfig> getBySysids(String sysid);
	
	
	
	/**
	 * 保存
	 * @orderBy 
	 * @param menu
	 * @author li
	 * @Date 2016年12月7日下午3:36:39
	 */
	public void saveEntity(TSysConfig configure);

	/**
	 * 更新
	 * @orderBy 
	 * @param menu
	 * @author li
	 * @Date 2016年12月7日下午3:36:50
	 */
	public void updateEntity(TSysConfig configure);

	
	
	/**
	 * 更新
	 * @orderBy 
	 * @param menu
	 * @author zhoupeng
	 * @Date 2016年12月7日下午3:36:50
	 */
	public void updateEntityList(String idStr,String enable);
	
	
	/**
	 * 删除
	 * @orderBy 
	 * @param menu
	 * @author li
	 * @Date 2016年12月7日下午3:38:46
	 */
	public void deleteEntity(TSysConfig configure);
	
	
	/**
	 * 批量删除
	 * @orderBy 
	 * @param menu
	 * @author zhoupeng
	 * @Date 2016年12月7日下午3:38:46
	 */
	public void deleteEntityList(String sysid);
	

	/**
	 * 获取所有对象
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:36:58
	 */
	//public List<TSysConfig> getAll();

	/**
	 * 根据类型获取菜单列表
	 * @orderBy 
	 * @param type
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:37:16
	 */
//	public List<TSysConfig> getListByMenuFlag(String type);

}
