package com.strongit.iss.action.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongit.iss.common.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.entity.ViewBean;
import com.strongit.iss.entity.ViewBeanListBean;
import com.strongit.iss.service.IFundsService;
import com.strongit.iss.service.IQueryConditionService;
import com.strongit.iss.service.impl.ReportCacheServiceImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class FundsViewAction extends BaseActionSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ReportCacheServiceImpl reportCacheService;
	@Autowired
	private ICommonUtils commonUtils;
	
	@Autowired
	private IFundsService fundsService;
	
	@Autowired
	private IQueryConditionService queryConditionService;
	private String sessionId;
	
    //页面过滤条件
	private Map<String,String> filters=new HashMap<String,String>();
	// 报表缓存ID
	private String reportCacheUUID;
	//报表url
	private String reportRequestUrl;
	//报表参数用于区分是哪张报表
	private String searchFiledName;
	
	/**
	 * 专项建设基金
	 * @orderBy 
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:47:44
	 */
	public String getBudgetReportData(){
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
		//专项类别
		if(Constant.REPORT_SPECIAL_TYPE.equals(searchFiledName)){
			list = fundsService.getFundBySpecialType(filters,querySql);
		}else{
			list = fundsService.getFundByMap(filters,querySql);
		}
		List<ViewBean> depList = Lists.newArrayList();
		for(Map<String ,Object> map:list){
			String itemCode=(String) map.get("itemCode");
			String itemName=(String) map.get("itemName");
			String cnt=String.valueOf(map.get("cnt"));
			String investMon1=String.valueOf(map.get("investMon"));
			String investMon2=String.valueOf(map.get("investMon1"));
			String investMon3=String.valueOf(map.get("investMon2"));
			String investMon4=String.valueOf(map.get("investMon3"));
			depList.add(new ViewBean(cnt,itemName,itemCode,investMon1,investMon2,investMon3,investMon4));
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
