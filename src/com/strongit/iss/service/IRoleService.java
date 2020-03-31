package com.strongit.iss.service;

import java.util.List;
import java.util.Map;

import com.strongit.iss.entity.TFgwStatisMenu;
import com.strongit.iss.orm.hibernate.Page;

/**
 * 
 * @author tanghw
 *
 */
public interface IRoleService extends IBaseService {
	/**
	 * 获取所有角色信息
	 * @orderBy 
	 * @param pageBo
	 * @param condition
	 * @return
	 * @author tanghw
	 * @Date 2017年3月23日下午4:23:04
	 */
	public Page<Map<String, Object>> getRolesPage(Page<Map<String, Object>>pageBo,Map<String, Object> condition);
	
	/**
	 * 根据条件更新授权角色资源
	 * @orderBy 
	 * @param userId
	 * @param projectId
	 * @param labelIds
	 * @author tanghw
	 * @Date 2017年3月23日下午4:29:33
	 */
	public void updateRoleMenuByContition(String userId,String roleGuid,List<String> menuGuids)throws Exception;
	/**
	 * 根据角色id获取授权资源
	 * @orderBy 
	 * @param roleGuid
	 * @return
	 * @author tanghw
	 * @Date 2017年3月24日上午9:33:58
	 */
	public List<TFgwStatisMenu> getRoleMenuByRoleGuid(String roleGuid);
}
