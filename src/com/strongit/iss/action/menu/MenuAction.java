package com.strongit.iss.action.menu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.Datagrid;
import com.strongit.iss.common.ETypeName;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.entity.TFgwStatisMenu;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.IMenuService;

/**
 * <pre>
 *     继承于自定义Action BaseActionSupport
 *      系统菜单管理Action,用于跳转前端页面请求
 * @author tannc
 * @E-mai：tannc@strongit.com.cn
 *  @Date 2016年10月15日下午3:36:18
 *  @see com.strongit.iss.action.BaseActionSupport
 * </pre>
 */
@SuppressWarnings("serial")
public class MenuAction extends BaseActionSupport<TFgwStatisMenu> {
	
	private Page<TFgwStatisMenu> menuPage = new Page<TFgwStatisMenu>(20, true);
	//第一页
	protected int page = 1;
	//页容量
	protected int rows = 20;
	//主键ID
	private String guid;
	
	@Autowired
	private IMenuService menuService;
	
	/**
	 *  <pre>
	 *  前端页面 menu-manageList.jsp跳转页面
	 *
	 * @return
	 *      返回跳转页面
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午7:46:43
	 * </pre>
	 */
	public String manageList() {
		return "manageList";
	}
	
	/**
	 * <pre>
	 *  获取前端页面menu-manageList.jsp表格展示的列表数
	 *  默认每页20列数据
	 *  默认排序是按照 sort 升序
	 * @see com.strongit.iss.entity.TFgwStatisMenu#sort
	 * @return
	 *        null --跳转到源页面
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午3:39:46
	 * @Exception 抛出获取列表的异常
	 * </pre>
	 */
	public String getMenuList() throws Exception{
		// 构建分页对象
		bulidPage();
		String name = Struts2Utils.getRequest().getParameter("name");
		long start =System.currentTimeMillis();
		menuPage = menuService.getMenuList(menuPage, name);
		long end =System.currentTimeMillis();
		// 计算当前页面耗时情况
		logger.debug("execute MenuServiceImpl.getMenuList method cost time : "
				+ (end - start) + " mills.");
		List<TFgwStatisMenu> list = menuPage.getResult();
		formatList(list);
		// 封装到datagrid中让easyui控件渲染
		Datagrid<TFgwStatisMenu> dg = new Datagrid<TFgwStatisMenu>(menuPage.getTotalCount(), list);
		// 返回分页对象的JSON字符串的形式到前端
		Struts2Utils.renderJson(dg);
		return null;
	}
	
	/**
	 * <pre>
	 * 设置分页对象的分页大小、页号
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午8:47:55
	 * </pre>
	 */
	private void bulidPage() {
		menuPage.setPageNo(page);
		menuPage.setPageSize(rows);
	}
	
	/**
	 * <pre>
	 *  设置菜单的父级名称
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午8:48:13
	 * </pre>
	 */
	private void formatList(List<TFgwStatisMenu> list) {
		List<TFgwStatisMenu> all = menuService.getAll();
		if (null != list && list.size() > 0 && null != all && all.size() > 0) {
			for (TFgwStatisMenu aMenu : all) {
				for (TFgwStatisMenu menu : list) {
					if (aMenu.getGuid().equals(menu.getParentGuid())) {
						menu.setParentName(aMenu.getName());
					}
				}
			}
		}
	}
	
	/**
	 * <pre>
	 *   新增/编辑页面
	 * @return
	 *     返回新增/编辑页面 menu-input.jsp
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午5:46:52
	 * </pre>
	 */
	public String input() {
		return "input";
	}
	
	/**
	 * <pre>
	 * 获取上级菜单列表
	 * @return
	 *       null --  返回调用页面
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午5:47:12
	 * </pre>
	 */
	public String getParentList() {
		//获取上级菜单
		List<TFgwStatisMenu> list = menuService.getParentList();
		//拼接json
		StringBuffer json = new StringBuffer("[");
		//判断不为空
		if (null != list && list.size() > 0) {
			//遍历集合
			for (TFgwStatisMenu menu : list) {
				json.append("{\"name\":\"").append(menu.getName()).append("\",\"value\":\"").append(menu.getGuid()).append("\"},");
			}
			//去除多余，
			json.deleteCharAt(json.length() - 1);
		}
		json.append("]");
		//返回json
		Struts2Utils.renderText(json.toString());
		return null;
	}
	/**
	 * <pre>
	 *   得到菜单的信息
	 * @return
	 *       null --  返回调用页面
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午5:47:12
	 * </pre>
	 */
	public String getBaseInfo() {
		//定义一个map
		Map<String, Object> map = new HashMap<String, Object>();
		//主键ID
		String guid = Struts2Utils.getRequest().getParameter("guid");
		//主键不为空
		if (StringUtils.isNotBlank(guid)) {
			TFgwStatisMenu menu = menuService.getByGuid(guid);
			if (null != menu) {
				//主键ID
				map.put("guid", menu.getGuid());
				//业务模式
				map.put("business", menu.getMenuFlag());
				//上级菜单
				map.put("parent", menu.getParentGuid());
				//菜单名称
				map.put("menuName", menu.getName());
				//url
				map.put("url", menu.getUrl());
				//类型
				map.put("typeFlag", menu.getTypeFlag());
				//是否启用
				map.put("enable", menu.getEnable());
				//url
				//map.put("url", menu.getUrl());
				//模块名称
				map.put("menuTitle", menu.getMenuTitle());
				//页面高度
				map.put("pageHeight", menu.getPageHeight());
				//页面宽度
				map.put("pageWidth", menu.getPageWidth());
				//排序
				map.put("sort", menu.getSort());
				//传值到前台
				Struts2Utils.renderJson(map);
			}
		}
		return null;
	}

	/**
	 * <pre>
	 *  保存或者更新编辑的内容
	 * @return
	 *       null --  返回调用页面
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午5:47:12
	 * </pre>
	 */
	public String save() {
		//获取页面上传递过来的参数
		String entity = Struts2Utils.getRequest().getParameter("entity");
		//定义字典项对象
		TFgwStatisMenu menu = null;
		//若json字符串不为空
		if (StringUtils.isNotBlank(entity)) {
			try {
				//解码，格式为UTF-8
				entity = URLDecoder.decode(entity, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//将json字符串转换为java实体对象
			menu = (TFgwStatisMenu) JSONObject.toBean(
					JSONObject.fromObject(entity), TFgwStatisMenu.class);
		}
		//若对象不为空
		if (null != menu) {
			if (StringUtils.isNotBlank(menu.getGuid())) {
				//更新字典信息
				menuService.updateEntity(menu);
			} else {
				//保存字典
				menuService.saveEntity(menu);
			}
		}
		return renderText("true");
	}

	/**
	 * <pre>
	 *   删除菜单
	 * @return
	 *       null --  返回调用页面
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午5:47:12
	 *   此处调用的方法 renderText
	 * @see com.strongit.iss.action.BaseActionSupport#renderText
	 * </pre>
	 */
	public String delete() {
		String guid = Struts2Utils.getRequest().getParameter("guid");
		//主键ID不为空
		if (StringUtils.isNotBlank(guid)) {
			//根据主键ID获取对象信息
			TFgwStatisMenu menu = menuService.getByGuid(guid);
			//若对象不为空
			if (null != menu) {
				//删除
				menuService.deleteEntity(menu);
			}
		}
		return renderText("true");
	}
	
	/**
	 * 根据名称获取路径信息
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月17日上午11:12:37
	 */
	public String getSrcByName() {
		//类型
		String type = Struts2Utils.getRequest().getParameter("type");
		String name = "";
		//不为空
		if (StringUtils.isNotBlank(type)) {
			//根据类型判断
			switch (ETypeName.getByCode(type)) {
				case DEFVIEW: //项目库总览
					name = ETypeName.DEFVIEW.getDescription();
					break;
				case FIVEPLAN: //五年
					name = ETypeName.FIVEPLAN.getDescription();
					break;
				case ROLLPLAN: //三年
					name = ETypeName.ROLLPLAN.getDescription();
					break;
				case PROJECTDEAL: //审核备
					name = ETypeName.PROJECTDEAL.getDescription();
					break;
				case PROJECTVIEW: //项目办理
					name = ETypeName.PROJECTVIEW.getDescription();
					break;
				case CONTENTVIEW: //目录办理
					name = ETypeName.CONTENTVIEW.getDescription();
					break;
				case ITEMVIEW: //事项办理
					name = ETypeName.ITEMVIEW.getDescription();
					break;
				case YEARREPORT: //年度计划申报
					name = ETypeName.YEARREPORT.getDescription();
					break;
				case YEARISSUED: //年度计划下达
					name = ETypeName.YEARISSUED.getDescription();
					break;
				case YEARDISPATCH: //年度计划调度
					name = ETypeName.YEARDISPATCH.getDescription();
					break;
				case FUND: //专项建设
					name = ETypeName.FUND.getDescription();
					break;
				case PROJECTFILE: //项目档案
					name = ETypeName.PROJECTFILE.getDescription();
					break;
				case APPROVALRECORD: //审核备项目信息趋势情况
					name = ETypeName.APPROVALRECORD.getDescription();
					break;
				case DISTRIBUTIONSITUATION: //审核备项目信息分维度汇总
					name = ETypeName.DISTRIBUTIONSITUATION.getDescription();
					break;
				case RELEASE: //简政放权
					name = ETypeName.RELEASE.getDescription();
					break;
				case MANAGE: //放管结合
					name = ETypeName.MANAGE.getDescription();
					break;
				case OPTIMIZATION: //优化服务
					name = ETypeName.OPTIMIZATION.getDescription();
					break;
				case FINEMANAGEMENT: //政府投资精细化管理
					name = ETypeName.FINEMANAGEMENT.getDescription();
					break;
				case FORECAST: //预测预警分析
					name = ETypeName.FORECAST.getDescription();
					break;
				case PROJECTIMPLEMENT: //项目实施进展监测预警分析
					name = ETypeName.PROJECTIMPLEMENT.getDescription();
					break;
				case PROJECTHANDLE: //项目办理进展监测预警分析
					name = ETypeName.PROJECTHANDLE.getDescription();
					break;
				case USER: //用户分布情况
					name = ETypeName.USER.getDescription();
					break;
				case REPORT: //综合分析报告
					name = ETypeName.REPORT.getDescription();
					break;
				default:
					break;
			}
			//根据名称获取对象信息
			TFgwStatisMenu menu = menuService.getByName(name);
			if (null != menu) {
				Struts2Utils.renderText(menu.getUrl());
			}
		}
		return null;
	}

	/**
	 * <pre>
	 *    获取菜单列表
	 * @return
	 *       null --  返回调用页面
	 * @author tannc
	 * @E-mai：tannc@strongit.com.cn
	 * @Date 2016年10月15日下午5:47:12
	 *   此处调用的方法 Struts2Utils.renderJson 请参考
	 * @see com.strongit.iss.common.Struts2Utils#renderJson(String, String...)
	 * </pre>
	 */
	public String initMenuList() {
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String typeModule=this.getRequest().getParameter("typeModule");
		String userId = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
		}
		Map<String, String> map = new HashMap<String, String>();
		//基础业务菜单
//		List<TFgwStatisMenu> baseList = menuService.getListByMenuFlag("0");
		List<TFgwStatisMenu> baseList = menuService.getListByMenuFlag(typeModule, userId);
		//重点业务菜单
//		List<TFgwStatisMenu> impList = menuService.getListByMenuFlag("2");
//		List<TFgwStatisMenu> impList = menuService.getListByMenuFlag("2", userId);
		//拼接基础业务html
		String baseHtml = "";
		//拼接重点业务html
		String impHtml = "";
		//存储基础业务父级菜单列表
		List<TFgwStatisMenu> baseParentList = new ArrayList<TFgwStatisMenu>();
		//存储基础业务子级菜单列表
		List<TFgwStatisMenu> baseChildList = new ArrayList<TFgwStatisMenu>();
		//存储重点业务父级菜单列表
		List<TFgwStatisMenu> impParentList = new ArrayList<TFgwStatisMenu>();
		//存储重点业务子级菜单列表
		List<TFgwStatisMenu> impChildList = new ArrayList<TFgwStatisMenu>();
		//基础菜单不为空
		if (null != baseList && baseList.size() > 0) {
			for (TFgwStatisMenu menu : baseList) {
				//为父级
				if (StringUtils.isBlank(menu.getParentGuid())) {
					//放入父级list
					baseParentList.add(menu);
				} else {
					//放入子级list
					baseChildList.add(menu);
				}
			}
		}
		//拼接基础业务html
		baseHtml = getHtml(baseParentList, baseChildList, baseHtml);
		//重点菜单不为空
//		if (null != impList && impList.size() > 0) {
//			for (TFgwStatisMenu menu : impList) {
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
		impHtml = getHtml(impParentList, impChildList, impHtml);
		map.put("baseInfo", baseHtml);
		map.put("impInfo", impHtml);
		// 把菜单列表返回前端
		Struts2Utils.renderJson(map);
		return null;
	}
	
	/**
	 * 拼接html
	 * @orderBy 
	 * @param parentList
	 * @param childList
	 * @param html
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月17日下午5:01:37
	 */
	private String getHtml(List<TFgwStatisMenu> parentList,
			List<TFgwStatisMenu> childList, String html) {
		StringBuilder htmlBuilder=new StringBuilder(html);
		String taskNumber= menuService.getTime();
		String year= taskNumber.substring(0,4);
		String month= taskNumber.substring(4);
		//父级list不为空
		if (null != parentList && parentList.size() > 0) {
			//遍历父级
			for (TFgwStatisMenu menu : parentList) {
				htmlBuilder.append("<li class='parentCls'>");
				htmlBuilder.append(" <a onclick='loadFlash( this,\"").append(menu.getUrl()).append("\")'");
				htmlBuilder.append("  _url='"+menu.getUrl()); //隐藏URL
				htmlBuilder.append("'  _typeflag='"+menu.getTypeFlag()); //隐藏菜单类型
				htmlBuilder.append("' _pagewidth='"+menu.getPageWidth()); //菜单宽度
				htmlBuilder.append("' _pageheight='"+menu.getPageHeight()); // 菜单高度
				htmlBuilder.append("' _menutitle='"+menu.getMenuTitle());  // 菜单标题
				htmlBuilder.append("'> ");
				htmlBuilder.append(" ");
				htmlBuilder.append(" ");
				htmlBuilder.append( menu.getName());
				htmlBuilder.append("</a>");
				//子级不为空
				if (null != childList && childList.size() > 0) {
					for (TFgwStatisMenu chlidMenu : childList) {
						if (chlidMenu.getParentGuid().equals(menu.getGuid())) {
							htmlBuilder.append( "<ul>");
							break;
						}
					}
					//遍历子级
					for (TFgwStatisMenu chlidMenu : childList) {
						//子级的父级id与父级的id相同
						if (chlidMenu.getParentGuid().equals(menu.getGuid())) {
							//获取父子标题的标识
							String mark1= menu.getTypeFlag();
							String mark2= chlidMenu.getTypeFlag();
							if(mark1.equals("yearMain")&&mark2.equals("yearDispatch")){
								htmlBuilder.append("<li>");
								htmlBuilder.append(" <a onclick='loadFlash( this,\"").append(chlidMenu.getUrl()).append("\")'");
								htmlBuilder.append("  _url='"+chlidMenu.getUrl()); //隐藏URL
								htmlBuilder.append("'  _typeflag='"+chlidMenu.getTypeFlag()); //隐藏菜单类型
								htmlBuilder.append("' _pagewidth='"+chlidMenu.getPageWidth()); //菜单宽度
								htmlBuilder.append("' _pageheight='"+chlidMenu.getPageHeight()); // 菜单高度
								htmlBuilder.append("' _menutitle='"+chlidMenu.getMenuTitle()+"("+year+"年"+month+"月)");  // 菜单标题
								htmlBuilder.append("'> ");
								htmlBuilder.append( chlidMenu.getName());
								htmlBuilder.append("</a>");
							}
							else{
								htmlBuilder.append("<li>");
								htmlBuilder.append(" <a onclick='loadFlash( this,\"").append(chlidMenu.getUrl()).append("\")'");
								htmlBuilder.append("  _url='"+chlidMenu.getUrl()); //隐藏URL
								htmlBuilder.append("'  _typeflag='"+chlidMenu.getTypeFlag()); //隐藏菜单类型
								htmlBuilder.append("' _pagewidth='"+chlidMenu.getPageWidth()); //菜单宽度
								htmlBuilder.append("' _pageheight='"+chlidMenu.getPageHeight()); // 菜单高度
								htmlBuilder.append("' _menutitle='"+chlidMenu.getMenuTitle());  // 菜单标题
								htmlBuilder.append("'> ");
								htmlBuilder.append( chlidMenu.getName());
								htmlBuilder.append("</a>");
							}
						}
					}
					for (TFgwStatisMenu chlidMenu : childList) {
						if (chlidMenu.getParentGuid().equals(menu.getGuid())) {
							htmlBuilder.append("</ul>");
							break;
						}
					}
				}
				htmlBuilder.append("</li>");
			}
		}
		return htmlBuilder.toString();
	}
	
	/**
	 * 获取大屏的页面
	 * @orderBy 
	 * @return
	 * @author wuwei
	 * @Date 2017年4月26日上午10:33:18
	 */
	public String loadData(){
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
		}
		//获取大屏的属性
		List<TFgwStatisMenu> impList = menuService.getListByMenuFlag("4", userId);		
		List<Object> list = new ArrayList<Object>();
		for(int i=0;i<impList.size();i++){
			Map<String,String> resultInfo = new HashMap<String, String>();
			resultInfo.put("type",String.valueOf(i+1));
			resultInfo.put("id", impList.get(i).getTypeFlag());
			resultInfo.put("url", impList.get(i).getUrl());
			list.add(i, resultInfo);
		}
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/***
	 *   临时方法
	 *     初始化页面展现的内容
	 * @orderBy
	 * @param
	 * @return
	 * @author tannc
	 * @Date 2017/2/19 13:26
	 **/
 public String initPageContent() {
	//基础业务菜单
	 List<TFgwStatisMenu> baseList = menuService.getListByMenuFlag("0");
	//重点业务菜单
	 List<TFgwStatisMenu> impList = menuService.getListByMenuFlag("2");
	 baseList.addAll(impList);
	 // 把菜单列表返回前端
	 Struts2Utils.renderJson(baseList);
	 return null;
 }
	/**
	 * @return the menuPage
	 */
	public Page<TFgwStatisMenu> getMenuPage() {
		return menuPage;
	}

	/**
	 * @param menuPage the menuPage to set
	 */
	public void setMenuPage(Page<TFgwStatisMenu> menuPage) {
		this.menuPage = menuPage;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
}
