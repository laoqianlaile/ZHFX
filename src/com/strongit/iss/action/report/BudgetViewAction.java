package com.strongit.iss.action.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongit.iss.common.ICommonUtils;

import org.springframework.beans.factory.annotation.Autowired;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.common.collect.Lists;
import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.CommonUtils;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.entity.ViewBean;
import com.strongit.iss.entity.ViewBeanListBean;
import com.strongit.iss.service.IBudgetService;
import com.strongit.iss.service.IQueryConditionService;
import com.strongit.iss.service.impl.ReportCacheServiceImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class BudgetViewAction extends BaseActionSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ReportCacheServiceImpl reportCacheService;
	@Autowired
	private IBudgetService budgetService;
	@Autowired
	private IQueryConditionService queryConditionService;
	@Autowired
	private ICommonUtils commonUtils;
	
    //页面过滤条件
	private Map<String,String> filters=new HashMap<String,String>();
	// 报表缓存ID
	private String reportCacheUUID;
	//报表url
	private String reportRequestUrl;
	//报表参数用于区分是哪张报表
	private String searchFiledName;
	//sessionId
	private String sessionId;
	
	/**
	 * 加载中央预算内申报报表数据
	 * @orderBy 
	 * @author wuwei
	 * @Date 2016年10月21日下午2:47:44
	 */
	public String getBudgetReportData(){
		//获取session中的的检索条件
		Map<String,String> queryMap = (Map<String,String>) this.getSession().getAttribute(sessionId+"_cache");
		//获取查询条件组装的sql文
		String querySql = queryConditionService.getIQueryCondition(queryMap);
		filters = commonUtils.codeMapFullName(filters);
		String realPath = this.getRequest().getRequestURL().toString();
		String projectName = this.getRequest().getContextPath();
		int endLength = realPath.indexOf(projectName) + projectName.length();
		String tmp = realPath.substring(0,endLength);
		//动态拼接url
		reportRequestUrl = tmp.replace("http://", "");
		Map<String,String> resultInfo = new HashMap<String, String>();
		List<Map<String, Object>> list = null;
		//行业
		if(Constant.REPORT_INDUSTRY.equals(searchFiledName)){
			list = budgetService.getBudgetReportByIndustry(filters,querySql);
		}
		//国标行业
		else if(Constant.REPORT_GBINDUSTRY.equals(searchFiledName)){
			list = budgetService.getBudgetReportByGBIndustry(filters,querySql);
		}//申报时间
		else if(Constant.REPORT_TIME.equals(searchFiledName)){
			list = budgetService.getBudgetReportBySubmitTime(filters,querySql);
		}
		//委内司局
		else if(Constant.REPORT_WNSJ.equals(searchFiledName)){
			list = budgetService.getBudgetReportByWNSJ(filters,querySql);
		}//政府投资方向
		else if(Constant.REPORT_GOVERNMENT_INVEST_DIRECTION.equals(searchFiledName)){
			list = budgetService.getBudgetReportByGovernment(filters,querySql);
		}//重大战略
		else if(Constant.REPORT_MAJOR_STRATEGY.equals(searchFiledName)){
			list = budgetService.getBudgetReportByMajor(filters,querySql);
		}//审核备类型
		else if(Constant.REPORT_PROJECT_TYPE.equals(searchFiledName)){
			list = budgetService.getBudgetReportByProjectType(filters,querySql);
		}//投资规模
		else if(Constant.REPORT_PROJECT_TZGM.equals(searchFiledName)){
			list = budgetService.getBudgetReportByTZGM(filters,querySql);
		}
		//申报部门
		else if(Constant.REPORT_DEPARTMENT_NAME.equals(searchFiledName)){
			//展现一级
			if(StringUtil.isEmpty(filters.get("DEPARTMENTNAME"))){
				list = budgetService.getBudgetReportByDepartmentTypeLv1(filters,querySql);
			}//展现二级
			else{
				list = budgetService.getBudgetReportByDepartmentTypeLv2(filters,querySql);
			}
		}
		//地区报表
		else{
			list = budgetService.getBudgetReportByMap(filters,querySql);
		}		
		List<ViewBean> depList = Lists.newArrayList();
		for(Map<String ,Object> map:list){
			String itemCode=(String) map.get("itemCode");
			String itemName=(String) map.get("itemName");
			String cnt=String.valueOf(map.get("cnt"));
			String investMon=String.valueOf(map.get("investMon"));
			String budgetInvestMon=String.valueOf(map.get("budgetInvestMon"));			
			String applyInvestMon=String.valueOf(map.get("applyInvestMon"));
			depList.add(new ViewBean(itemCode,itemName,cnt,investMon,budgetInvestMon,applyInvestMon));
		}    		
		XStream xstream=new XStream(new DomDriver());
		xstream.processAnnotations(ViewBeanListBean.class);
		ViewBeanListBean bean=new ViewBeanListBean();
		bean.setResult(depList);
		//转成xml格式
		String convertXml = xstream.toXML(bean);
		String headAndBody = CommonUtils.XMLHEARDERS + convertXml;
		//存入缓存中
		String uuid1 = reportCacheService.putReport(headAndBody);
		resultInfo.put("uuid1",uuid1);
		resultInfo.put("reportRequestUrl", reportRequestUrl);
		Struts2Utils.renderText(resultInfo);
		return null;
	}	
	
	/**
	 * 加载中央预算内下达报表数据
	 * @orderBy 
	 * @return
	 * @author wuwei
	 * @Date 2016年10月31日下午10:32:18
	 */
	public String getBudgetIssuedData(){
		//获取session中的的查询条件
		Map<String,String> queryMap = (Map<String,String>) this.getSession().getAttribute(sessionId+"_cache");
		//获取查询条件组装的sql文
		String querySql = queryConditionService.getIQueryCondition(queryMap);
		filters = commonUtils.codeMapFullName(filters);
		String realPath = this.getRequest().getRequestURL().toString();
		String projectName = this.getRequest().getContextPath();
		int endLength = realPath.indexOf(projectName) + projectName.length();
		String tmp = realPath.substring(0,endLength);
		//动态拼接url		
		reportRequestUrl = tmp.replace("http://", "");
		Map<String,String> resultInfo = new HashMap<String, String>();
		List<Map<String, Object>> list = null;
		//行业
		if(Constant.REPORT_INDUSTRY.equals(searchFiledName)){
			list = budgetService.getBudgetIssuedByIndustry(filters,querySql);
		}
		//国标行业
		else if(Constant.REPORT_GBINDUSTRY.equals(searchFiledName)){
			list = budgetService.getBudgetIssuedByGBIndustry(filters,querySql);
		}//下达时间
		else if(Constant.REPORT_TIME.equals(searchFiledName)){
			list = budgetService.getBudgetIssuedByIssuedTime(filters,querySql);
		}		
		//委内司局
		else if(Constant.REPORT_WNSJ.equals(searchFiledName)){
			list = budgetService.getBudgetIssuedByWNSJ(filters,querySql);
		}//政府投资方向
		else if(Constant.REPORT_GOVERNMENT_INVEST_DIRECTION.equals(searchFiledName)){
			list = budgetService.getBudgetIssuedByGovernment(filters,querySql);
		}//投资规模
		else if(Constant.REPORT_PROJECT_TZGM.equals(searchFiledName)){
			list = budgetService.getBudgetIssuedByTZGM(filters,querySql);
		}
		//地区报表
		else{
			list = budgetService.getBudgetIssuedByMap(filters,querySql);
		}		
		List<ViewBean> depList = Lists.newArrayList();
		for(Map<String ,Object> map:list){
			String itemCode=(String) map.get("itemCode");
			String itemName=(String) map.get("itemName");
			String cnt=String.valueOf(map.get("cnt"));
			String investMon=String.valueOf(map.get("investMon"));
			String budgetInvestMon=String.valueOf(map.get("budgetInvestMon"));			
			String applyInvestMon=String.valueOf(map.get("issuedInvestMon"));
			depList.add(new ViewBean(itemCode,itemName,cnt,investMon,budgetInvestMon,applyInvestMon));
		}    		
		XStream xstream=new XStream(new DomDriver());
		xstream.processAnnotations(ViewBeanListBean.class);
		ViewBeanListBean bean=new ViewBeanListBean();
		bean.setResult(depList);
		//转成xml格式
		String convertXml = xstream.toXML(bean);
		String headAndBody = CommonUtils.XMLHEARDERS + convertXml;
		//存入缓存中
		String uuid1 = reportCacheService.putReport(headAndBody);
		resultInfo.put("uuid1",uuid1);
		resultInfo.put("reportRequestUrl", reportRequestUrl);
		Struts2Utils.renderText(resultInfo);
		return null;
	}
		
	/**
	 * 加载中央预算内项目调度报表数据
	 * @orderBy 
	 * @author zhoupeng
	 * @Date 2016年10月21日下午2:47:44
	 */
	public String getBudgetDispatchReportData(){
		//获取session中的的检索条件
		Map<String,String> queryMap = (Map<String,String>) this.getSession().getAttribute(sessionId+"_cache");
		//获取查询条件组装的sql文
		String querySql = queryConditionService.getIQueryCondition(queryMap);
		Map<String,String> resultInfo = new HashMap<String, String>();
		String realPath = this.getRequest().getRequestURL().toString();
		String projectName = this.getRequest().getContextPath();
		int endLength = realPath.indexOf(projectName) + projectName.length();
		String tmp = realPath.substring(0,endLength);
		//动态拼接url		
		reportRequestUrl = tmp.replace("http://", "");		
		//转译相关参数
		Map<String, String> newFilters = commonUtils.codeMapFullName(filters);
		//拼接过滤条件
		String searchSql = "";
		String orderbySql = commonUtils.orderbySql(searchFiledName,newFilters);		
		List<Map<String, Object>> list = Lists.newArrayList();
		//获取建设地点查询条件
		for (Map.Entry<String,String> entry:newFilters.entrySet()){
			String key=entry.getKey();
			String value=entry.getValue();
			// 建设地点
			if("BuildPlaceCode".equals(key)&&!value.isEmpty()){
				searchSql = " AND VINF.BUILD_PLACE_FULL_KEY like '%"+value+"%' ";
			}
		}
		if(searchFiledName.equals("BUILDPLACE")){
			//建设地点
			list = budgetService.getBudgetPlaceDispatch(newFilters,querySql,searchSql,orderbySql);
		}else if(searchFiledName.equals("GOVDIRECTION")){
			//政府投资方向
			list = budgetService.getBudgetGovinvestdirectiondispatch(newFilters,querySql,searchSql,orderbySql);
		}else if(searchFiledName.equals("GBINDUSTRY")){
			// 开启项目
			for (Map.Entry<String,String> entry:newFilters.entrySet()){
				String key=entry.getKey();
				String value=entry.getValue();
				// 国标行业
				if("GBIndustryCode".equals(key)&&!value.isEmpty()){
					searchSql = searchSql + " AND VINF.GB_INDUSTRY_FULL_KEY like '%"+value+"%' ";
				}
			}
			//国标行业
			list = budgetService.getBudgetGbindustrydispatch(newFilters,querySql,searchSql,orderbySql);
		}else if(searchFiledName.equals("INDUSTRY")){
			// 开启项目
			for (Map.Entry<String,String> entry:newFilters.entrySet()){
				String key=entry.getKey();
				String value=entry.getValue();
				// 所属行业
				if("IndustryCode".equals(key)&&!value.isEmpty()){
					searchSql = searchSql + " AND VINF.INDUSTRY_FULL_KEY like '%"+value+"%' ";
				}
			}
			//所属行业
			list = budgetService.getBudgetIndustryDispatch(newFilters,querySql,searchSql,orderbySql);
		}else if (searchFiledName.equals("EXPECTENDYEAR")){
			//预计建成时间
			list = budgetService.getBudgetEndtimeDispatch(newFilters,querySql,searchSql,orderbySql);
		}else if(searchFiledName.equals("IMAGEPROGRESS")){
			//形象进度
			list = budgetService.getBudgetImageprogressDispatch(newFilters,querySql,searchSql,orderbySql);
		}else{
			return null;
		}		
		List<ViewBean> depList = Lists.newArrayList();
		for(Map<String ,Object> map:list){
			String cnt=String.valueOf(map.get("cnt"));
			String itemName=(String) map.get("itemName");
			String itemCode=(String) map.get("itemCode");
			String investMon1=String.valueOf(map.get("investMon"));//合计总投资
			String investMon2=String.valueOf(map.get("investMon1"));//---中央预算内累计下达资金（亿元）
			String investMon3=String.valueOf(map.get("investMon2"));//---中央预算内本次下达资金（亿元）
			String investMon4=String.valueOf(map.get("investMon3"));//---中央预算内本报告期完成（亿元）
			String investMon5=String.valueOf(map.get("investMon4"));//---中央预算内本报告期到位（亿元）
			String investMon6=String.valueOf(map.get("investMon5")); //---中央预算内本报告期支付（亿元）
			String startCnt=String.valueOf(map.get("startCnt"));//---开工个数
			String endCnt=String.valueOf(map.get("endCnt"));// ---完工个数
			
			depList.add(new ViewBean(cnt,itemName,itemCode,investMon1,investMon2,investMon3,investMon4,investMon5,investMon6,startCnt,endCnt));
		}    		
		XStream xstream=new XStream(new DomDriver());
		xstream.processAnnotations(ViewBeanListBean.class);
		ViewBeanListBean bean=new ViewBeanListBean();
		bean.setResult(depList);
		//转成xml格式
		String convertXml = xstream.toXML(bean);
		String headAndBody = CommonUtils.XMLHEARDERS + convertXml;
		//存入缓存中
		String uuid1 = reportCacheService.putReport(headAndBody);
		resultInfo.put("uuid1",uuid1);
		resultInfo.put("reportRequestUrl", reportRequestUrl);
		Struts2Utils.renderText(resultInfo);
		return null;
	}
	
    public Map<String, String> getFilters() {
       return filters;
    }
    public void setFilters(Map<String, String> filters) {
		this.filters = filters;
    }    
    public String getReportCacheUUID() {
		return reportCacheUUID;
	}
	public void setReportCacheUUID(String reportCacheUUID) {
		this.reportCacheUUID = reportCacheUUID;
	}

	public String getSearchFiledName() {
		return searchFiledName;
	}

	public void setSearchFiledName(String searchFiledName) {
		this.searchFiledName = searchFiledName;
	}

	public String getReportRequestUrl() {
		return reportRequestUrl;
	}

	public void setReportRequestUrl(String reportRequestUrl) {
		this.reportRequestUrl = reportRequestUrl;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}	
}
