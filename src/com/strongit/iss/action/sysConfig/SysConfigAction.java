package com.strongit.iss.action.sysConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Datagrid;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.entity.TSysConfig;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.ISysConfigService;

/**
 * @author li
 * @Date 2016年12月7日上午11:04:58
 */
@SuppressWarnings("serial")
public class SysConfigAction extends BaseActionSupport<TSysConfig>{
	
	private Page<TSysConfig> configPage = new Page<TSysConfig>(20, true);
	//第一页
	protected int page = 1;
	//页容量
	protected int rows = 20;
	//主键ID
	private String sysid;
	
	@Autowired
	private ISysConfigService sysConfigService;

	/**
	 * 获取列表
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:18:36
	 */
	public String list() {
		return "list";
	}
	
	/**
	 * 获取菜单列表
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:18:54
	 */
	public String getConfigList() {
		bulidPage();
		//名称
		String configName = Struts2Utils.getRequest().getParameter("name");
		//类型
		String configType = Struts2Utils.getRequest().getParameter("type");
		long start =System.currentTimeMillis();
		configPage = sysConfigService.getConfigList(configPage, configName, configType);
		long end =System.currentTimeMillis();
		logger.debug("execute sysConfigServiceImpl.getMenuList method cost time : "
				+ (end - start) + " mills.");
		List<TSysConfig> list = configPage.getResult();
		for(TSysConfig tSysConfig:list){
			if(tSysConfig.getEnbale() == 1){
				tSysConfig.setEnbaleName("启用");
			}else{
				tSysConfig.setEnbaleName("禁用");
			}
			
		}
		
		//formatList(list);
		// 封装到datagrid中让easyui控件渲染
		Datagrid<TSysConfig> dg = new Datagrid<TSysConfig>(configPage.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;
	}
	
	/**
	 * 初始化Page
	 * @orderBy 
	 * @author li
	 * @Date 2016年12月7日下午3:19:12
	 */
	private void bulidPage() {
		configPage.setPageNo(page);
		configPage.setPageSize(rows);
	}
	
	/**
	 * 新增/编辑
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:19:45
	 */
	public String input() {
		return "input";
	}

	/**
	 * 获取基本信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:20:11
	 */
	public String getBaseInfo() {
		//定义一个map
		Map<String, Object> map = new HashMap<String, Object>();
		//主键ID
		String guid = Struts2Utils.getRequest().getParameter("sysid");
		//主键不为空
		if (StringUtils.isNotBlank(guid)) {
			TSysConfig configure = sysConfigService.getBySysid(sysid);
			if (null != configure) {
				//主键ID
				map.put("sysid", configure.getSysid());
				//配置编码
				map.put("configCode", configure.getConfigCode());
				//配置名称
				map.put("configName", configure.getConfigName());
				//配置变量
				map.put("configInfo", configure.getConfigInfo());
				//数据来源
				map.put("source", configure.getSource());
				//类型
				map.put("type", configure.getType());
				//排序
				map.put("sort", configure.getSort());
				//备注
				map.put("remark", configure.getRemark());
				//是否启用
				map.put("enbale", configure.getEnbale());
				
				//传值到前台
				Struts2Utils.renderJson(map);
			}
		}
		return null;
	}
	
	/**
	 * 保存
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:20:27
	 */
	public String save() {
		//获取页面上传递过来的参数
		String entity = Struts2Utils.getRequest().getParameter("entity");
		//定义字典项对象
		TSysConfig configure = null;
		//若json字符串不为空
		if (StringUtils.isNotBlank(entity)) {
			try {
				//解码，格式为UTF-8
				entity = URLDecoder.decode(entity, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//将json字符串转换为java实体对象
			configure = (TSysConfig) JSONObject.toBean(
					JSONObject.fromObject(entity), TSysConfig.class);
		}
		//若对象不为空
		if (null != configure) {
			if (StringUtils.isNotBlank(configure.getSysid())) {
				//更新字典信息
				sysConfigService.updateEntity(configure);
			} else {
				//保存字典
				sysConfigService.saveEntity(configure);
			}
		}
		return renderText("true");
	}
	
	/**
	 * 删除
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:20:42
	 */
	public String delete() {
		String sysid = Struts2Utils.getRequest().getParameter("sysid");
		//主键ID不为空
		if (StringUtils.isNotBlank(sysid)) {
			//根据主键ID获取对象信息
			TSysConfig configure = sysConfigService.getBySysid(sysid);
			//若对象不为空
			if (null != configure) {
				//删除
				sysConfigService.deleteEntity(configure);
			}
		}
		return renderText("true");
	}
	
	
	/**
	 * 删除
	 * @orderBy 
	 * @return
	 * @author zhoupeng
	 * @Date 2016年01月13日下午3:20:42
	 */
	public String deleteList() {
		String sysid = Struts2Utils.getRequest().getParameter("sysid");
		//主键ID不为空
		if (StringUtils.isNotBlank(sysid)) {
			//若对象不为空
			if (null != sysid) {
				//删除
				sysConfigService.deleteEntityList(sysid);
			}
		}
		return renderText("true");
	}
	
	
	
	/**
	 * 启用
	 * @orderBy 
	 * @return
	 * @author zhoupeng
	 * @Date 2016年01月13日下午3:20:42
	 */
	public String enableBtn() {
		String sysid = Struts2Utils.getRequest().getParameter("sysid");
		//主键ID不为空
		if (StringUtils.isNotBlank(sysid)) {
			//若对象不为空
			//if (null != configure) {
				//启用
				sysConfigService.updateEntityList(sysid,"1");
			//}
		}
		return renderText("true");
	}
	
	
	
	
	/**
	 * 禁用
	 * @orderBy 
	 * @return
	 * @author zhoupeng
	 * @Date 2016年01月13日下午3:20:42
	 */
	public String disableBtn() {
		String sysid = Struts2Utils.getRequest().getParameter("sysid");
		//主键ID不为空
		if (StringUtils.isNotBlank(sysid)) {
		
			//若对象不为空
			//if (null != configure) {
				//禁用
				sysConfigService.updateEntityList(sysid,"0");
			//}
		}
		return renderText("true");
	}
	
	
	

	/**
	 * 根据类型获取菜单列表
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日下午3:21:12
	 */
//	public String initMenuList() {
//		Map<String, String> map = new HashMap<String, String>();
//		//基础业务菜单
//		List<TSysConfig> baseList = sysConfigService.getListByMenuFlag("0");
//		//重点业务菜单
//		List<TSysConfig> impList = sysConfigService.getListByMenuFlag("2");
//		//拼接基础业务html
//		String baseHtml = "";
//		//拼接重点业务html
//		String impHtml = "";
//		//存储基础业务父级菜单列表
//		List<TSysConfig> baseParentList = new ArrayList<TSysConfig>();
//		//存储基础业务子级菜单列表
//		List<TSysConfig> baseChildList = new ArrayList<TSysConfig>();
//		//存储重点业务父级菜单列表
//		List<TSysConfig> impParentList = new ArrayList<TSysConfig>();
//		//存储重点业务子级菜单列表
//		List<TSysConfig> impChildList = new ArrayList<TSysConfig>();
//		//基础菜单不为空
//		if (null != baseList && baseList.size() > 0) {
//			for (TSysConfig menu : baseList) {
//				//为父级
//				if (StringUtils.isBlank(menu.getParentGuid())) {
//					//放入父级list
//					baseParentList.add(menu);
//				} else {
//					//放入子级list
//					baseChildList.add(menu);
//				}
//			}
//		}
//		//拼接基础业务html
//		baseHtml = getHtml(baseParentList, baseChildList, baseHtml);
//		//重点菜单不为空
//		if (null != impList && impList.size() > 0) {
//			for (TSysConfig menu : impList) {
//				//为父级
//				if (StringUtils.isBlank(menu.getParentGuid())) {
//					//放入父级list
//					impParentList.add(menu);
//				} else {
//					//放入子级list
//					impChildList.add(menu);
//				}
//			}
//		}
//		impHtml = getHtml(impParentList, impChildList, impHtml);
//		map.put("baseInfo", baseHtml);
//		map.put("impInfo", impHtml);
//		Struts2Utils.renderJson(map);
//		return null;
//	}
	
	public Page<TSysConfig> getConfigPage() {
		return configPage;
	}

	public void setConfigPage(Page<TSysConfig> configPage) {
		this.configPage = configPage;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}
	
}