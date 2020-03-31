package com.strongit.iss.action.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.Datagrid;
import com.strongit.iss.common.JsonTreeData;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.common.TreeNodeUtil;
import com.strongit.iss.entity.TFgwStatisMenu;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.IMenuService;
import com.strongit.iss.service.IRoleService;

/**
 * 
 * @author tanghw
 *
 */
@SuppressWarnings("serial")
public class RoleAction extends BaseActionSupport<Object> {
	
	
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IMenuService menuService;
	
	
	private String roleName;//角色名称
	private String roleGuid;//角色id
	private String page;    //分页查询页数
	private String rows;    //分页查询行数
	/** 自定义标签传参 */
	private Map<String, String> menuParams = new HashMap<String, String>();
	/**
	 * 角色页面
	 * @orderBy 
	 * @return
	 * @throws Exception
	 * @author tanghw
	 * @Date 2017年3月22日下午4:54:21
	 */
	public String list() throws Exception {
		return  "list";
	}
	public String getRoleData() {
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage); 
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(roleName)) {
			condition.put("roleName", roleName);
		}
		long start =System.currentTimeMillis();
		try {
			pageBo = roleService.getRolesPage(pageBo, condition);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute roleService.getRolesPage method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute roleService.getRolesPage method cost time : "
				+ (end - start) + " mills.");
		if (null == list) {
			list = new ArrayList<Map<String, Object>>();
		}
		// 封装到datagrid中让easyui控件渲染
		Datagrid<Map<String, Object>> dg = new Datagrid<Map<String, Object>>(pageBo.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;
	}
	/**
	 * 
	 * @orderBy 
	 * @return
	 * @throws Exception
	 * @author tanghw
	 * @Date 2017年3月23日下午2:50:44
	 */
	public String openRoleMenuDialog() throws Exception{
		return "menuList";
	}
	public void getMenusData() throws Exception{
		List<JsonTreeData> treeDataList = new ArrayList<JsonTreeData>();
		List<TFgwStatisMenu> list=menuService.getAll();
		JsonTreeData treeData = new JsonTreeData();
		treeData = new JsonTreeData();
		if(list.size() > 0){
			for(TFgwStatisMenu fgwStatisMenu : list){
				if(Constant.MENU_ENABLE.equals(fgwStatisMenu.getEnable())) {
					treeData = new JsonTreeData();
					treeData.setId(fgwStatisMenu.getGuid());
					treeData.setPid(fgwStatisMenu.getParentGuid());
					treeData.setText(fgwStatisMenu.getName());
					treeData.setState("open");
					treeDataList.add(treeData);
				}
			}


		}
		List<JsonTreeData> newTreeDataList = TreeNodeUtil.getFatherNode(treeDataList);
		Struts2Utils.renderJson(newTreeDataList);
		
	}
	/**
	 * 授权资源
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2017年3月23日下午4:21:57
	 */
	public void updateMenus() throws Exception{
		try {
			@SuppressWarnings("unchecked")
			Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
			String userId = null;
			if(user!=null){
				userId = user.get("employee_guid").toString();
			}
			String menuGuids = menuParams.get("menuGuids");
			String[] projectIdAry =menuGuids.split(",");
			List<String> labelIDsList = Arrays.asList(projectIdAry);
			String roleGuid = menuParams.get("roleGuid");
			roleService.updateRoleMenuByContition(userId, roleGuid, labelIDsList);
			Struts2Utils.renderText(Constant.RESULT_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Struts2Utils.renderText(Constant.RESULT_FAILURE);
		}
	}
	/**
	 * 根据角色guid获取角色的授权资源
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2017年3月24日上午9:29:21
	 */
	public void getRoleMenuByRoleGuid() throws Exception{
		List<JsonTreeData> treeDataList = new ArrayList<JsonTreeData>();
		//TFgwStatisMenu
		List<TFgwStatisMenu> list= roleService.getRoleMenuByRoleGuid(roleGuid);
		if(list.size() > 0){
			for(TFgwStatisMenu fgwStatisMenu : list){
				JsonTreeData treeData = new JsonTreeData();
				treeData = new JsonTreeData();
				treeData.setId(fgwStatisMenu.getGuid());
				treeData.setPid(fgwStatisMenu.getParentGuid());
				treeData.setText(fgwStatisMenu.getName());
				treeData.setState("open");
				treeDataList.add(treeData);
			}
		}
		Struts2Utils.renderJson(treeDataList);
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleGuid() {
		return roleGuid;
	}
	public void setRoleGuid(String roleGuid) {
		this.roleGuid = roleGuid;
	}
	public Map<String, String> getMenuParams() {
		return menuParams;
	}
	public void setMenuParams(Map<String, String> menuParams) {
		this.menuParams = menuParams;
	}
	
}
