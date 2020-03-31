package com.strongit.iss.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.entity.RoleMenu;
import com.strongit.iss.entity.TFgwStatisMenu;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IRoleService;
/**
 * @author tanghewen
 *
 */
@Service
@Transactional
public class RoleServiceImpl extends BaseService implements IRoleService{
	/**
	 * 获取所有角色信息
	 * @orderBy 
	 * @param pageBo
	 * @param condition
	 * @return
	 * @author tanghw
	 * @Date 2017年3月23日下午4:23:04
	 */
	@Override
	public Page<Map<String, Object>> getRolesPage(Page<Map<String, Object>> pageBo, Map<String, Object> condition) {
		// 定义查询结果返回接收变量
		Page<Map<String, Object>> resultPage = null;
		List<Object> paramList = new ArrayList<Object>();
		String strRoleName = (String) condition.get("roleName");
		Map<String,Object> values = new HashMap<String,Object>();
		String sql = "select role_guid as roleGuid ,role_name as roleName ,role_desc as roleDesc ,role_type as roleType from Role where enabled =1 ";
		if(StringUtils.isNotBlank(strRoleName)){
			sql += " and role_Name like '%" + strRoleName.trim() + "%'";
			values.put("roleName",strRoleName);
		}
		sql +=" order by UPDATE_TIME desc";
		
		//获取参数
		resultPage = this.dao.findBySql(pageBo, sql,paramList.toArray());
		return resultPage;	
	}
	/**
	 * 根据条件更新授权角色资源
	 * @orderBy 
	 * @param userId
	 * @param projectId
	 * @param labelIds
	 * @author tanghw
	 * @Date 2017年3月23日下午4:29:33
	 */
	@Override
	public void updateRoleMenuByContition(String userId, String roleGuid, List<String> menuGuids) throws Exception{
		//先判断参数是否为空
		if(userId==null||userId==""||"" == roleGuid || null== roleGuid){
			throw new Exception("传入参数有误");
		}
		try {
			//根据项目id获取项目的修改前的授权资源（授权菜单菜单）
			List<TFgwStatisMenu> oldMenus =this.getRoleMenuByRoleGuid(roleGuid);
			//循环角色旧授权菜单 匹配角色新授权菜单  删除新授权资源中没有的原授权资源
			for(TFgwStatisMenu menu:oldMenus){
				boolean delflag = true;
				for(int i=0;i<menuGuids.size();i++){
					String menuGuid=menuGuids.get(i);
					//判断原有菜单是否存在新菜单中
					if(menuGuid.equals(menu.getGuid())){
						delflag = false;
						break;
					}
				}
				if(delflag){
					//删除新菜单中没有的原菜单
					this.deleteRolMenuByContiton(menu.getGuid(), roleGuid);
					this.dao.flush();
				}
			}
			//判断项目是否添加了新的已有标签 有的话保存添加的标签
			//newMenuGuids可能为空存的是[]所以进行格式转换后判断
			String menuGuidss = menuGuids.toString();
			if(!menuGuidss.equals("[]")){
				 for(String menuGuid : menuGuids){
					 boolean saveflag = true;
					 for(TFgwStatisMenu menu:oldMenus){
						 if(menuGuid.equals(menu.getGuid())){
							 saveflag = false;
								break;
							}
					 }
					 if(saveflag){
					 	System.out.println("menuGuid==>"+menuGuid);
						 //保存旧菜单中没有的新菜单
						 this.saveRoleMuneByProjectId(roleGuid,menuGuid,userId);
					 }
				 }
			 }
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("更新授权信息失败");
		}

	}
	/**
	 * 根据角色id获取授权资源
	 * @orderBy 
	 * @param roleGuid
	 * @return
	 * @throws Exception
	 * @author tanghw
	 * @Date 2017年3月23日下午4:35:13
	 */
	public List<TFgwStatisMenu> getRoleMenuByRoleGuid(String roleGuid){
		StringBuffer hql =new StringBuffer(" select T.* from T_FGW_STATIS_MENU t, ROLE_MENU r where t.guid=r.menu_guid and t.enable='1' and r.role_guid=? ");
		return dao.findBySql(TFgwStatisMenu.class, hql.toString(),roleGuid);
	}
	/**
	 * 根据角色id,菜单id删除授权资源
	 * @orderBy 
	 * @param labelId
	 * @param projectId
	 * @throws Exception
	 * @author tanghw
	 * @Date 2017年3月23日下午4:48:08
	 */
	private void deleteRolMenuByContiton(String menuGuid, String roleGuid)
			throws Exception {
		String delSql="delete from ROLE_MENU rm where rm.menu_Guid=? and rm.role_Guid=? ";
		this.dao.executeSql(delSql, menuGuid,roleGuid);
		this.dao.flush();
	}
	/**
	 * 根据角色Id，授权资源id（菜单id），用户id保存角色授权信息。
	 * @orderBy 
	 * @param roleGuid
	 * @param menuGuid
	 * @param userId
	 * @throws Exception
	 * @author tanghw
	 * @Date 2017年3月23日下午5:12:42
	 */
	private void saveRoleMuneByProjectId(String roleGuid, String menuGuid,
			String userId) throws Exception {
		Date currentTime=new Timestamp(new Date().getTime());
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setRoleMenuGuid(UUID.randomUUID().toString());
		roleMenu.setRoleGuid(roleGuid);
		roleMenu.setMenuGuid(menuGuid);
		roleMenu.setCreateUserId(userId);
		roleMenu.setCreatTime(currentTime);
		roleMenu.setUpdateTime(currentTime);
		roleMenu.setUpdateUserId(userId);
		this.dao.save(roleMenu);	
	}
}
