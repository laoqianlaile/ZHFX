package com.strongit.iss.action.neuview;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Cache;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.Datagrid;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.entity.ProjectFileSearchVo;
import com.strongit.iss.neuentity.Code2Name;
import com.strongit.iss.neuentity.IndustryInfo;
import com.strongit.iss.neuentity.PlaceInfo2;
import com.strongit.iss.neuentity.ProjectInfo2;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.neuinterface.IProjectFileService2;


public class ProjectFileAction  extends BaseActionSupport{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private IProjectFileService2  projectVoFileService;
//	@Autowired
//	private ReportCacheService reportCacheService;
	private String page;  //分页查询页数
	private String rows;  //分页查询行数	
	//定义地图传参map
	private Map<String, String> filters = new HashMap<String, String>();
    //封装项目查询vo类
	private ProjectFileSearchVo projectVo = new ProjectFileSearchVo();
    private String param;
    private String regParam;
	/**
	 * 实现模型驱动
	 * @orderBy 
	 * @return
	 * @author xiangyong
	 * @Date 2016年8月2日下午5:39:03
	 */
	@Override
	public ProjectFileSearchVo getModel() {
		return projectVo;
	}
	
	/**
	 * 跳转项目档案的详细页面
	 * @return
	 */
	public String details() {

		return "details";
	}
	
	/**
	 * 跳转项目档案的主页面
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String list() throws Exception{
		// 建设地点
		regParam = param;
		// 项目阶段
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectStage"))){
			String projectStage =filters.get("projectStage").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				projectStage = new String(projectStage.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			projectStage = projectStage.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("projectStage", projectStage);
		}
		// 行政层级
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("goverLevel"))){
			String goverLevel =filters.get("goverLevel").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				goverLevel = new String(goverLevel.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			goverLevel = goverLevel.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("goverLevel", goverLevel);
		}
		// 投资规模
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("investRange"))){
			String investRange =filters.get("investRange").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				investRange = new String(investRange.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			investRange = investRange.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("investRange", investRange);
		}
		// 月份统计
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("month"))){
			String month =filters.get("month").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				month = new String(month.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			month = month.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("month", month);
		}
		// 产业统计
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("production"))){
			String production =filters.get("production").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				production = new String(production.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			production = production.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("production", production);
		}
		// 国标行业
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("gbIndustry"))){
			String gbIndustry =filters.get("gbIndustry").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				gbIndustry = new String(gbIndustry.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			gbIndustry = gbIndustry.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("gbIndustry", gbIndustry);
		}
		// 委内行业
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("wnIndustry"))){
			String wnIndustry =filters.get("wnIndustry").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				wnIndustry = new String(wnIndustry.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			wnIndustry = wnIndustry.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("wnIndustry", wnIndustry);
		}
		// 按审核备类型统计情况：filters.shbType
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("shbType"))){
			String shbType =filters.get("shbType").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				shbType = new String(shbType.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			shbType = shbType.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("shbType", shbType);
		}
		// 预计开工时间趋势：filters.expectStartTime
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("expectStartTime"))){
			String expectStartTime =filters.get("expectStartTime").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				expectStartTime = new String(expectStartTime.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			expectStartTime = expectStartTime.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("expectStartTime", expectStartTime);
		}
		// 按部门统计事项办理情况（部门）：filters.department
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("department"))){
			String department =filters.get("department").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				department = new String(department.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			department = department.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("department", department);
		}
		// 按事项统计各地办理情况（事项）：filters.item
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("item"))){
			String item =filters.get("item").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				item = new String(item.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			item = item.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("item", item);
		}
		// 按层级统计事项办理情况：filters.itemLevle
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("itemLevle"))){
			String itemLevle =filters.get("itemLevle").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				itemLevle = new String(itemLevle.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			itemLevle = itemLevle.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("itemLevle", itemLevle);
		}
		return "list";
	}
	
	public String itemList() throws Exception{
		// 建设地点
		regParam = param;
		// 项目阶段
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectStage"))){
			String projectStage =filters.get("projectStage").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				projectStage = new String(projectStage.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			projectStage = projectStage.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("projectStage", projectStage);
		}
		// 行政层级
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("goverLevel"))){
			String goverLevel =filters.get("goverLevel").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				goverLevel = new String(goverLevel.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			goverLevel = goverLevel.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("goverLevel", goverLevel);
		}
		// 投资规模
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("investRange"))){
			String investRange =filters.get("investRange").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				investRange = new String(investRange.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			investRange = investRange.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("investRange", investRange);
		}
		// 月份统计
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("month"))){
			String month =filters.get("month").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				month = new String(month.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			month = month.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("month", month);
		}
		// 产业统计
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("production"))){
			String production =filters.get("production").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				production = new String(production.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			production = production.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("production", production);
		}
		// 国标行业
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("gbIndustry"))){
			String gbIndustry =filters.get("gbIndustry").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				gbIndustry = new String(gbIndustry.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			gbIndustry = gbIndustry.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("gbIndustry", gbIndustry);
		}
		// 委内行业
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("wnIndustry"))){
			String wnIndustry =filters.get("wnIndustry").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				wnIndustry = new String(wnIndustry.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			wnIndustry = wnIndustry.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("wnIndustry", wnIndustry);
		}
		// 按审核备类型统计情况：filters.shbType
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("shbType"))){
			String shbType =filters.get("shbType").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				shbType = new String(shbType.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			shbType = shbType.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("shbType", shbType);
		}
		// 预计开工时间趋势：filters.expectStartTime
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("expectStartTime"))){
			String expectStartTime =filters.get("expectStartTime").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				expectStartTime = new String(expectStartTime.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			expectStartTime = expectStartTime.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("expectStartTime", expectStartTime);
		}
		// 按部门统计事项办理情况（部门）：filters.department
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("department"))){
			String department =filters.get("department").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				department = new String(department.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			department = department.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("department", department);
		}
		// 按事项统计各地办理情况（事项）：filters.item
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("item"))){
			String item =filters.get("item").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				item = new String(item.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			item = item.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("item", item);
		}
		// 按层级统计事项办理情况：filters.itemLevle
		if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("itemLevle"))){
			String itemLevle =filters.get("itemLevle").trim().toString();
			String str = System.getProperty("file.encoding");
			if(!Constant.CODE.equalsIgnoreCase(str)){
				itemLevle = new String(itemLevle.getBytes("ISO8859-1"),"UTF-8");
			}				
			//地图所传地区全路径名称去掉国家
			itemLevle = itemLevle.replace("顶层"+Constant.BI_SPLIT,"");
			filters.put("itemLevle", itemLevle);
		}
		return "itemList";
	}
	
	
	/**
	 * 查询项目列表
	 * @return
	 */
	public String showProject(){
		String str = System.getProperty("file.encoding");
		try {
			if(!Constant.CODE.equalsIgnoreCase(str)){
				regParam = new String(regParam.getBytes("ISO8859-1"),"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String filterStr = "";
		if(null!=regParam && !"".equals(regParam) &&!"{}".equals(regParam)){
			String[]temp6 = regParam.split(">>");
			String temp7 = temp6[temp6.length-1];
			filterStr = temp7.replace("'", "");
		}
		// 根据传入参数地区名称获得地区编码
		if(!"".equals(filterStr)){
			List<Object> valList0 = projectVoFileService.getNameByCode(filterStr, "PLACE_NAME");
			if(null!=valList0&&!valList0.isEmpty()){
				PlaceInfo2 placeInfo = (PlaceInfo2)valList0.get(0);
				if(null!=placeInfo){
					projectVo.setProjectRegion(placeInfo.getCode3());
				}
			}
		}
		long start =System.currentTimeMillis();
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage);
		try {
			if(StringUtils.isNotBlank(projectVo.getProjectRegion())){
				pageBo = projectVoFileService.searchMapProject(pageBo,projectVo,filters);
			}else if("1".equals(filters.get("projectOrItem"))){
				pageBo = projectVoFileService.searchProject(pageBo,projectVo,filters);
			}else if("2".equals(filters.get("projectOrItem"))){
				pageBo = projectVoFileService.searchItem(pageBo,projectVo,filters);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectVoFileService.getDataList method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
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
	
	public String showItems(){
		String str = System.getProperty("file.encoding");
		try {
			if(!Constant.CODE.equalsIgnoreCase(str)){
				regParam = new String(regParam.getBytes("ISO8859-1"),"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String filterStr = "";
		if(null!=regParam && !"".equals(regParam) &&!"{}".equals(regParam)){
			String[]temp6 = regParam.split(">>");
			String temp7 = temp6[temp6.length-1];
			filterStr = temp7.replace("'", "");
		}
		// 根据传入参数地区名称获得地区编码
		if(!"".equals(filterStr)){
			List<Object> valList0 = projectVoFileService.getNameByCode(filterStr, "PLACE_NAME");
			if(null!=valList0&&!valList0.isEmpty()){
				PlaceInfo2 placeInfo = (PlaceInfo2)valList0.get(0);
				if(null!=placeInfo){
					projectVo.setProjectRegion(placeInfo.getCode3());
				}
			}
		}
		long start =System.currentTimeMillis();
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage);
		try {
			if(StringUtils.isNotBlank(projectVo.getProjectRegion())){
				pageBo = projectVoFileService.searchMapProject(pageBo,projectVo,filters);
			}else if("1".equals(filters.get("projectOrItem"))){
				pageBo = projectVoFileService.searchProject(pageBo,projectVo,filters);
			}else if("2".equals(filters.get("projectOrItem"))){
				pageBo = projectVoFileService.searchItem(pageBo,projectVo,filters);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectVoFileService.getDataList method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
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

	public IProjectFileService2 getProjectFileService() {
		return projectVoFileService;
	}

	public void setProjectFileService(IProjectFileService2 projectVoFileService) {
		this.projectVoFileService = projectVoFileService;
	}

	public ProjectFileSearchVo getProjectVo() {
		return projectVo;
	}

	public void setProjectVo(ProjectFileSearchVo projectVo) {
		this.projectVo = projectVo;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getRegParam() {
		return regParam;
	}

	public void setRegParam(String regParam) {
		this.regParam = regParam;
	}
	public Map<String, String> getFilters() {
		return filters;
	}
	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}
}
