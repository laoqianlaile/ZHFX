package com.strongit.iss.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.entity.TFgwStatisMenu;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IMenuService;

/**
 * <pre>
 *     继承于IMenuService接口
 *     菜单管理的接口
 * @author tannc
 * @E-mai：tannc@strongit.com.cn
 *  @Date 2016年10月15日下午3:36:18
 *  @see com.strongit.iss.service.IMenuService
 * </pre>
 */
@Service
@Transactional
public class MenuServiceImpl extends BaseService implements IMenuService {

	/**
	 *  <pre>
	 *   获取菜单列表
	 *   默认排序是sort升序，参考
	 *   @see com.strongit.iss.entity.TFgwStatisMenu#sort
	 *   方法实现了
	 *   @see com.strongit.iss.service.IMenuService#getMenuList
	 * @param  name
	 * 				--菜单名称 过滤条件
	 * @param page
	 * 			-- 分页对象
	 * 			@see com.strongit.iss.orm.hibernate.Page
	 * @return
	 *     --  name==null或者name== ""  返回全部列表数据
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	@Override
	public Page<TFgwStatisMenu> getMenuList(Page<TFgwStatisMenu> page,
			String name) {
		String sql = " from TFgwStatisMenu t where 1=1 ";
		if (StringUtils.isNotBlank(name)) {
			sql += " and t.name like '%" + name + "%' ";
		}
		page = this.dao.find(page, sql+" order by t.sort asc ", new Object[]{});
		return page;
	}

	/**
	 *  <pre>
	 *    获取父级菜单列表
	 *   默认排序是sort升序，参考
	 *   @see com.strongit.iss.entity.TFgwStatisMenu#sort
     *   方法实现了
	 *   @see com.strongit.iss.service.IMenuService#getParentList
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	@Override
	public List<TFgwStatisMenu> getParentList() {
		String sql = " from TFgwStatisMenu t where 1=1 and t.enable = '1' ";
				//+ " and t.parentGuid is null ";
		List<TFgwStatisMenu> list = this.dao.find(sql, new Object[]{});
		return list;
	}
	/**
	 *  <pre>
	 *   通过主键获取对象
	 *   方法实现了
	 *   @see com.strongit.iss.service.IMenuService#getByGuid
	 * @param  guid
	 *         -- 过滤菜单的主键
	 * @return
	 *      -- 菜单对象
	 *      -- guid==null或者guid=="" 返回空对象
	 *      -- 查询不到返回空对象
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	@Override
	public TFgwStatisMenu getByGuid(String guid) {
		String sql = " from TFgwStatisMenu t where 1=1  "
				+ " and t.guid = '" + guid + "' ";
		List<TFgwStatisMenu> list = this.dao.find(sql, new Object[]{});
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 *  <pre>
	 *   保存页面编辑的菜单
	 *   方法实现了
	 *   @see com.strongit.iss.service.IMenuService#saveEntity
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	@Override
	public void saveEntity(TFgwStatisMenu menu) {
		this.dao.save(menu);
	}

	/**
	 *  <pre>
	 *   更新页面编辑的菜单
	 *   方法实现了
	 *   @see com.strongit.iss.service.IMenuService#updateEntity
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	@Override
	public void updateEntity(TFgwStatisMenu menu) {
		this.dao.merge(menu);
		this.dao.flush();
	}

	/**
	 *  <pre>
	 *   删除菜单对象
	 *   方法实现了
	 *   @see com.strongit.iss.service.IMenuService#deleteEntity
	 * @param menu
	 *          -- 待删除的菜单对象
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	@Override
	public void deleteEntity(TFgwStatisMenu menu) {
		this.dao.delete(menu);
	}

	/**
	 *  <pre>
	 *   获取所有未删除的菜单列表
	 * 方法实现了
	 *   @see com.strongit.iss.service.IMenuService#getAll
	 * @return
	 *      -- 菜单列表  数据库没有数据返回空对象
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	@Override
	public List<TFgwStatisMenu> getAll() {
		String sql = " from TFgwStatisMenu t ";
		List<TFgwStatisMenu> list = this.dao.find(sql, new Object[]{});
		return list;
	}

	/**
	 *  <pre>
	 *     根据名称获取对象信息
	 *   方法实现了
	 *   @see com.strongit.iss.service.IMenuService#getByName
	 * @param name
	 *        -- 查询的菜单名称
	 *        -- name==""或者 name== null 返回空对象
	 * @return
	 *      -- 根据名称获取对象信息
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	@Override
	public TFgwStatisMenu getByName(String name) {
		String hql = " from TFgwStatisMenu t where 1 = 1 and t.enable = '1' "
				+ " and t.name like '%" + name + "%' ";
		List<TFgwStatisMenu> list = this.dao.find(hql, new Object[]{});
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 *  <pre>
	 *     根据业务类型获取菜单列表
	 *   方法实现了
	 *   @see com.strongit.iss.service.IMenuService#saveEntity
	 * @param type ("0" : 基础业务， "1" ： 重点业务)
	 * @return
	 *      -- 返回类别对应的列表
	 *      -- type==null 或者  type=="" 返回空对象
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	@Override
	public List<TFgwStatisMenu> getListByMenuFlag(String type) {
		String hql = " from TFgwStatisMenu t where 1=1 and t.enable = '1' "
				+ " and t.menuFlag = '" + type + "' order by sort asc ";
		List<TFgwStatisMenu> list = this.dao.find(hql, new Object[]{});
		return list;
	}
	/**
	 *  <pre>
	 *     根据业务类型和用户id获取菜单列表
	 * @orderBy 
	 * @param type
	 * @param userId
	 * @return
	 *      -- 返回类别对应的列表
	 *      -- type==null 或者  type=="" 返回空对象
	 * @author tanghw
	 * @Date 2017年3月24日上午10:16:31
	 */
	@Override
	public List<TFgwStatisMenu> getListByMenuFlag(String type, String userId) {
		String sql =" select DISTINCT  Tm.* from T_FGW_STATIS_MENU tm, ROLE_MENU rm, user_role ur"
				+ " where tm.guid = rm.menu_guid  and ur.role_guid = rm.role_guid and tm.enable = '1'  "
				+ "and tm.menu_Flag = '" + type + "' and ur.employee_guid = '" + userId + "' order by tm._sort asc ";
		List<TFgwStatisMenu> list= this.dao.findBySql(TFgwStatisMenu.class, sql);
		return list;
	}
	
	/**
	 * 
	 * <pre>
	 *  获取最新的报告期
	 * @orderBy 
	 * @return
	 *  获取报告期
	 * @author li
	 * @Date 2017年3月30日上午9:22:32
	 * </pre>
	 */
	public String getTime(){
		String sql = " select max(TASK_NUMBER) as timeid FROM project_dispatch_rankings ";
		List list = null;
		if(list == null){
			Query query = this.dao.createSqlQuery(sql, new Object[]{});
			list = query.list(); 
		}
		String id = (null == list ? "" : list.get(0).toString());
		return id;
	}
	
}
