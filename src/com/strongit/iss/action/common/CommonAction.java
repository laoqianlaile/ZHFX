/**
 * @author XiaXiang
 * @Date 2016年10月17日上午10:33:56
 */
package com.strongit.iss.action.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.entity.TFgwStatisMenu;
import com.strongit.iss.service.IMenuService;

/**
 * @author XiaXiang
 *
 */
@SuppressWarnings("serial")
public class CommonAction extends BaseActionSupport<Object> {
	
	private String type;
	@Autowired
	private IMenuService menuService;
	/**
	 * 跳转共通总览页面
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月17日上午10:34:49
	 */
	public String commonView() {
		return "commonView";
	}
	
	/**
	 * 基础业务
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月17日下午7:41:33
	 */
	public String overview() {
		return "overview";
	}
	
	/**
	 * 重点业务
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月17日下午7:41:33
	 */
	public String majorView() {
		return "majorView";
	}
	/**
	 * 获取用户菜单
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2017年3月24日下午12:10:23
	 */
	public String getMenuList() {
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
		}
		//业务菜单
		List<TFgwStatisMenu> List = menuService.getListByMenuFlag(type, userId);
		//拼接所有菜单Types_flag
		String menuTypesFlag ="";
		for(TFgwStatisMenu fgwStatisMenu:List){
			menuTypesFlag = menuTypesFlag + fgwStatisMenu.getTypeFlag();
		}
		// 把菜单列表返回前端
		Struts2Utils.renderText(menuTypesFlag);
		return null;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
