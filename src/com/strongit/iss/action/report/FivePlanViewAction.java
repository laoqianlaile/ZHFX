package com.strongit.iss.action.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongit.iss.common.ICommonUtils;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.CommonUtils;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.entity.ViewBean;
import com.strongit.iss.entity.ViewBeanListBean;
import com.strongit.iss.service.IFivePlanService;
import com.strongit.iss.service.IQueryConditionService;
import com.strongit.iss.service.impl.ReportCacheServiceImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class FivePlanViewAction extends BaseActionSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ReportCacheServiceImpl reportCacheService;
	@Autowired
	private ICommonUtils commonUtils;
	
	@Autowired
	private IQueryConditionService queryConditionService;
	@Autowired
	private IFivePlanService fivePlanService;
	
	private String sessionId;
	
    //页面过滤条件
	private Map<String,String> filters=new HashMap<String,String>();
	//判断维度
	private String searchFiledName;
	
	// 报表缓存ID
	private String reportCacheUUID;
	//报表url
	//private String reportRequestUrl = PropertiesUtil.getInfoByName("REPORT_URL");
	//private String  reportRequestUrl = "localhost:8080/BI";
	String realPath = this.getRequest().getRequestURL().toString();
	String projectName = this.getRequest().getContextPath();
	int endLength = realPath.indexOf(projectName) + projectName.length();
	String tmp = realPath.substring(0,endLength).toString();
	//动态拼接url		
	private String reportRequestUrl = tmp.replace("http://", "");
	
	/**
	 * 五年项目储备
	 * @orderBy 
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:47:44
	 */
	public String getFivePlanReportData(){
		//获取session中的的检索条件
		Map<String,String> queryMap = (Map<String,String>) this.getSession().getAttribute(sessionId+"_cache");
		//获取查询条件组装的sql文
		String querySql = queryConditionService.getIQueryCondition(queryMap);
		Map<String,String> resultInfo = new HashMap<String, String>();
		//转译相关参数
		Map<String, String> newFilters = commonUtils.codeMapFullName(filters);
		//拼接过滤条件
		String searchSql = "";
		//String searchSql = commonUtils.searchSql("vinf",newFilters);
		//校验排序条件
		String orderbySql = commonUtils.orderbySql(searchFiledName,filters);
		List<Map<String, Object>> list = Lists.newArrayList();
		//获取建设地点查询条件
		for (Map.Entry<String,String> entry:newFilters.entrySet()){
			String key=entry.getKey();
			String value=entry.getValue();
			// 建设地点
			if("BuildPlaceCode".equals(key)&&!value.isEmpty()){
				searchSql = " AND vinf.BUILD_PLACE_FULL_KEY like '%"+value+"%' ";
			}
		}
		
		
		if(searchFiledName.equals("BUILDPLACE")){
			//建设地点
			list = fivePlanService.getFivePlanPlaceReportByMap(newFilters,searchSql,querySql,orderbySql);
		}else if(searchFiledName.equals("GBINDUSTRY")){
			// 开启项目
			for (Map.Entry<String,String> entry:newFilters.entrySet()){
				String key=entry.getKey();
				String value=entry.getValue();
				// 国标行业
				if("GBIndustryCode".equals(key)&&!value.isEmpty()){
					searchSql = searchSql + " AND vinf.GB_INDUSTRY_FULL_KEY like '%"+value+"%' ";
				}
			}
			//国标行业
			list = fivePlanService.getFivePlanGbindustryReportByMap(newFilters,searchSql,querySql,orderbySql);
		}else if(searchFiledName.equals("INDUSTRY")){
			// 开启项目
			for (Map.Entry<String,String> entry:newFilters.entrySet()){
				String key=entry.getKey();
				String value=entry.getValue();
				// 所属行业
				if("IndustryCode".equals(key)&&!value.isEmpty()){
					searchSql = searchSql + " AND vinf.INDUSTRY_FULL_KEY like '%"+value+"%' ";
				}
			}
			//所属行业
			list = fivePlanService.getFivePlanIndustryReportByMap(newFilters,searchSql,querySql,orderbySql);
		}else if(searchFiledName.equals("MAJORSTRATEGY")){
			//重大战略
			 list = fivePlanService.getFivePlanMajorstrategyReportByMap(newFilters,searchSql,querySql,orderbySql);
		}else if(searchFiledName.equals("RECORDDEPT")){
			//申报部门
			 list = fivePlanService.getFivePlanCreatedepartmentguidReportByMap(newFilters,searchSql,querySql,orderbySql);
		}else if(searchFiledName.equals("STORELEVEL")){
			//储备情况
			 list = fivePlanService.getFivePlanStorelevelReportByMap(newFilters,searchSql,querySql,orderbySql);
		}else if(searchFiledName.equals("STORETIME")){
			//储备入库时间
			 list = fivePlanService.getFivePlanStoretimeReportByMap(newFilters,searchSql,querySql,orderbySql);
		}else if(searchFiledName.equals("PROTYPE")){
			//项目类型
			 list = fivePlanService.getFivePlanProtypeReportByMap(newFilters,searchSql,querySql,orderbySql);
		}else{
			return null;
		}
		
		
		List<ViewBean> depList = Lists.newArrayList();
		for(Map<String ,Object> map:list){
			String itemCode=(String) map.get("itemCode");
			String itemName=(String) map.get("itemName");
			//项目个数
			String cnt=String.valueOf(map.get("cnt"));
			//总投资
			String investMon1=String.valueOf(map.get("investMon1"));
			
			
			depList.add(new ViewBean(cnt,itemName,itemCode,investMon1));
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
	 * 根据getReportDataFromCache方法得到uuid获取缓存数据 
	 * @orderBy 
	 * @return
	 * @throws Exception
	 * @author wuwei
	 * @Date 2016年10月31日上午10:33:12
	 */
	public String getReportDataFromCache() throws Exception {
		Struts2Utils.renderText(reportCacheService.getReport(reportCacheUUID));
		return null;
	}
	
    public Map<String, String> getFilters() {
       return filters;
    }
    public void setFilters(Map<String, String> filters) {
		commonUtils.codeMapFullName(filters);
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
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
