package com.strongit.iss.service;

import java.util.List;

import com.strongit.iss.entity.TFgwStatisMenu;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.IBaseService;

/**
 * <pre>
 *     继承于IBaseService 接口
 *     菜单管理的接口
 * @author tannc
 * @E-mai：tannc@strongit.com.cn
 *  @Date 2016年10月15日下午3:36:18
 *  @see com.strongit.iss.service.IBaseService
 * </pre>
 */
public interface IMenuService extends IBaseService {

	/**
	 *  <pre>
	 *   获取菜单列表
	 *   默认排序是sort升序，参考
	 *   @see com.strongit.iss.entity.TFgwStatisMenu#sort
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
	public Page<TFgwStatisMenu> getMenuList(Page<TFgwStatisMenu> page, String name);

	/**
	 *  <pre>
	 *    获取父级菜单列表
	 *   默认排序是sort升序，参考
	 *   @see com.strongit.iss.entity.TFgwStatisMenu#sort
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	public List<TFgwStatisMenu> getParentList();

	/**
	 *  <pre>
	 *   通过主键获取对象
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
	public TFgwStatisMenu getByGuid(String guid);

	/**
	 *  <pre>
	 *   保存页面编辑的菜单
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	public void saveEntity(TFgwStatisMenu menu);

	/**
	 *  <pre>
	 *   更新页面编辑的菜单
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	public void updateEntity(TFgwStatisMenu menu);

	/**
	 *  <pre>
	 *   删除菜单对象
	 * @param menu
	 *          -- 待删除的菜单对象
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	public void deleteEntity(TFgwStatisMenu menu);

	/**
	 *  <pre>
	 *   获取所有未删除的菜单列表
	 * @return
	 *      -- 菜单列表  数据库没有数据返回空对象
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	public List<TFgwStatisMenu> getAll();


	/**
	 *  <pre>
	 *     根据名称获取对象信息
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
	public TFgwStatisMenu getByName(String name);

	/**
	 *  <pre>
	 *     根据业务类型获取菜单列表
	 * @param type ("0" : 基础业务， "1" ： 重点业务)
	 * @return
	 *      -- 返回类别对应的列表
	 *      -- type==null 或者  type=="" 返回空对象
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	public List<TFgwStatisMenu> getListByMenuFlag(String type);
	
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
	public List<TFgwStatisMenu> getListByMenuFlag(String type,String userId);
	
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
	public String getTime();

}
