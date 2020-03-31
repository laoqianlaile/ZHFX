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
import com.strongit.iss.service.IQueryConditionService;
import com.strongit.iss.service.IRollPlanService;
import com.strongit.iss.service.impl.ReportCacheServiceImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class RollPlanViewAction extends BaseActionSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ReportCacheServiceImpl reportCacheService;
	
	@Autowired
	private IRollPlanService rollPlanService;
	@Autowired
	private ICommonUtils commonUtils;
	@Autowired
	private IQueryConditionService queryConditionService;
	private String sessionId;
	
	//判断维度
	private String searchFiledName;
   
	//页面过滤条件
	private Map<String,String> filters=new HashMap<String,String>();
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
	 * 三年滚动计划
	 * @orderBy 
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:47:44
	 */
	public String getRollPlanReportData(){
		
		Map<String,String> queryMap = (Map<String,String>) this.getSession().getAttribute(sessionId+"_cache");
		//获取查询条件组装的sql文
		String querySql = queryConditionService.getIQueryCondition(queryMap);
		
		Map<String,String> resultInfo = new HashMap<String, String>();
		//转译相关参数
		Map<String, String> newFilters = commonUtils.codeMapFullName(filters);
		//拼接过滤条件
		String searchSql = "";
		//各级编制或上报国家三年滚动计划
		if("report".equals(newFilters.get("filterStatus"))){
			searchSql = " and  SUBSTRING(vinf.DEPARTMENT_FGW_GUID, 0, 6) = 'GUOJIA' AND vinf.STATUS = 'TO_CHECK' ";
		}
			
		
		//校验排序条件
		String orderbySql = commonUtils.orderbySql(searchFiledName,filters);
		List<Map<String, Object>> list = Lists.newArrayList();
		//获取建设地点查询条件
		for (Map.Entry<String,String> entry:newFilters.entrySet()){
			String key=entry.getKey();
			String value=entry.getValue();
			// 建设地点
			if("BuildPlaceCode".equals(key)&&!value.isEmpty()){
				searchSql = searchSql + " AND vinf.BUILD_PLACE_FULL_KEY like '%"+value+"%' ";
			}//else if("GovernmentCode".equals(key)&&!value.isEmpty()){
				//政府投资方向
			//	searchSql = searchSql + " AND vinf.GOVERNMENT_INVEST_DIRECTION_FULL_KEY like '%"+value+"%' ";
			//}
		}
		if(searchFiledName.equals("BUILDPLACE")){
			//建设地点
			list = rollPlanService.getRollPlanPlaceReportByMap(newFilters,searchSql,orderbySql,querySql);
		}else if(searchFiledName.equals("GOVDIRECTION")){
			//获取建设地点查询条件
			for (Map.Entry<String,String> entry:newFilters.entrySet()){
				String key=entry.getKey();
				String value=entry.getValue();
				//政府投资方向 
				if("GovernmentCode".equals(key)&&!value.isEmpty()){
					searchSql = searchSql + " AND vinf.GOVERNMENT_INVEST_DIRECTION_FULL_KEY like '%"+value+"%' ";
				}
			}
			//政府投资方向
			list = rollPlanService.getRollPlanGovernmentReportByMap(newFilters,searchSql,orderbySql,querySql);
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
			list = rollPlanService.getRollPlanGbindustryreportByMap(newFilters,searchSql,orderbySql,querySql);
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
			list = rollPlanService.getRollPlanIndustryReportByMap(newFilters,searchSql,orderbySql,querySql);
		}else if(searchFiledName.equals("MAJORSTRATEGY")){
			//重大战略
			 list = rollPlanService.getRollPlanMajorstrategyReportByMap(newFilters,searchSql,orderbySql,querySql);
		}else if(searchFiledName.equals("PROTYPE")){
			//项目类型
			 list = rollPlanService.getRollPlanProtypeReportByMap(newFilters,searchSql,orderbySql,querySql);
		}else if(searchFiledName.equals("BUILDSTAGE")){
			//项目阶段
			 list = rollPlanService.getRollPlanStageReportByMap(newFilters,searchSql,orderbySql,querySql);
		}else if (searchFiledName.equals("EXPECTSTARTTIME")){
			//预计开工时间
			list = rollPlanService.getRollPlanExpectstartyearReportByMap(newFilters,searchSql,orderbySql,querySql);
		}else if(searchFiledName.equals("EDITROLLTIME")){
			//编入三年滚动计划时间
			list = rollPlanService.getRollPlanCreatetimeReportByMap(newFilters,searchSql,orderbySql,querySql);
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
			//未来三年资金需求（亿元）
			String investMon2=String.valueOf(map.get("investMon2"));
			//2017年资金需求（亿元）
			String investMon3=String.valueOf(map.get("investMon3"));
			//2018年资金需求（亿元）
			String investMon4=String.valueOf(map.get("investMon4"));
			//2019年资金需求（亿元）
			String investMon5=String.valueOf(map.get("investMon5"));
			//中央预算内投资（亿元）
			String investMon6=String.valueOf(map.get("A00016TotalMenoy"));
			//专项建设基金投资（亿元）
			String investMon7=String.valueOf(map.get("A00018TotalMenoy"));
			
			
			depList.add(new ViewBean(cnt,itemName,itemCode,investMon1,investMon2,investMon3,investMon4,investMon5,investMon6,investMon7));
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
