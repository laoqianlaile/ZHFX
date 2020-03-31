package com.strongit.iss.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.entity.TSysConfig;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.ISysConfigService;

/**
 * @author zhaochao
 * @date 2018-09-06
 */
@Service
@Transactional
public class SysConfigServiceImpl extends BaseService implements ISysConfigService{
	
	/**
	 * 根据菜单名称查询菜单列表
	 * @orderBy 
	 * @param page
	 * @param name
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:42:22
	 */
	@Override
	public Page<TSysConfig> getConfigList(Page<TSysConfig> page,
			String configName, String configType) {
		String sql = " from TSysConfig t where 1=1 ";
		if (StringUtils.isNotBlank(configName)) {
			sql += " and t.configName like '%" + configName + "%' ";
		}
		if (StringUtils.isNotBlank(configType)) {
			//sql += " and t.configType = '" + configType + "' ";
			sql += " and t.type = '" + configType + "' ";
		}
		page = this.dao.find(page, sql+" order by t.sort asc ", new Object[]{});
		return page;
	}

	/**
	 * 根据ID获取对象
	 * @orderBy 
	 * @param sysid
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:43:56
	 */
	@Override
	public TSysConfig getBySysid(String sysid) {
		String sql = " from TSysConfig t where 1=1  "
				+ " and t.sysid = '" + sysid + "' ";
		List<TSysConfig> list = this.dao.find(sql, new Object[]{});
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	
	/**
	 * 根据ID获取对象
	 * @orderBy 
	 * @param sysid
	 * @return
	 * @author zhoupeng
	 * @Date 2016年12月7日下午3:43:56
	 */
	@Override
	public List<TSysConfig> getBySysids(String sysid) {
		String sysidStr = sysid.replace(",", "','");
		String sql = " from TSysConfig t where 1=1  "
				+ " and t.sysid in ('"+sysidStr+"') ";
		List<TSysConfig> list = this.dao.find(sql, new Object[]{});
		if (null != list && list.size() > 0) {
			return list;
		}
		return null;
	}
	
	/**
	 * 保存
	 * @orderBy 
	 * @param menu
	 * @author li
	 * @Date 2016年12月7日下午3:44:05
	 */
	@Override
	public void saveEntity(TSysConfig configure) {
		this.dao.save(configure);
	}

	/**
	 * 更新
	 * @orderBy 
	 * @param menu
	 * @author li
	 * @Date 2016年12月7日下午3:44:20
	 */
	@Override
	public void updateEntity(TSysConfig configure) {
		this.dao.merge(configure);
		this.dao.flush();
	}
	
	
	
	
	/**
	 * 更新
	 * @orderBy 
	 * @param menu
	 * @author zhoupeng
	 * @Date 2016年01月14日下午3:44:20
	 */
	
	public void updateEntityList(String sysid,String enbaleVal) {
		try{
			StringBuilder SQL = new StringBuilder();
	    	String sysidStr = sysid.replace(",", "','");
	    	int enbaleValInt = Integer.parseInt(enbaleVal);
	    	SQL.append("  update   T_SYS_CONFIG tt   set tt.ENABLE ="+enbaleValInt+"  where tt.sysid in ('"+sysidStr+"')");
	    
		    String sql = SQL.toString();
			long startMilis=System.currentTimeMillis();
			//执行查询并将结果转化为ListMap
			this.dao.executeSql(sql, new Object[]{});
			long endMilis=System.currentTimeMillis();
			logger.debug("SuperMapServiceImpl updateEntityList 方法执行查询花费毫秒数" + (endMilis-startMilis));
		
		}catch(Exception e){
				e.printStackTrace();
		}
	}

	/**
	 * 删除
	 * @orderBy 
	 * @param menu
	 * @author li
	 * @Date 2016年12月7日下午3:44:32
	 */
	@Override
	public void deleteEntity(TSysConfig configure) {
		this.dao.delete(configure);
	}
	
	/**
	 * 批量删除
	 * @orderBy 
	 * @param menu
	 * @author zhoupeng
	 * @Date 2016年12月7日下午3:44:32
	 */
	@Override
	public void deleteEntityList(String sysid) {
		try{
			StringBuilder SQL = new StringBuilder();
	    	String sysidStr = sysid.replace(",", "','");
	    	SQL.append("  delete   T_SYS_CONFIG tt    where tt.sysid in ('"+sysidStr+"')");
	    
		    String sql = SQL.toString();
			long startMilis=System.currentTimeMillis();
			//执行查询并将结果转化为ListMap
			this.dao.executeSql(sql, new Object[]{});
			long endMilis=System.currentTimeMillis();
			logger.debug("SuperMapServiceImpl deleteEntityList 方法执行查询花费毫秒数" + (endMilis-startMilis));
		
		}catch(Exception e){
				e.printStackTrace();
		}
	}
	

	/**
	 * 根据类型获取菜单列表
	 * @orderBy 
	 * @param type
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:45:29
	 */
//	@Override
//	public List<TSysConfig> getListByMenuFlag(String type) {
//		String hql = " from TSysConfig t where 1=1 and t.enable = '1' "
//				+ " and t.menuFlag = '" + type + "' order by sort asc ";
//		List<TSysConfig> list = this.dao.find(hql, new Object[]{});
//		return list;
//	}

}
