package com.strongit.iss.action.report;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Struts2Utils;

public class QueryConditionAction extends BaseActionSupport<Object>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sessionId;
	//页面查询条件
	private Map<String,String> reportParamsMap=new HashMap<String,String>();
	
	/**
	 * 将map存入session中
	 * @orderBy 
	 * @author wuwei
	 * @Date 2017年3月2日下午1:57:25
	 */
	public String saveReportParamsMap() {
		HttpSession session=this.getRequest().getSession();
		sessionId =this.getRequest().getSession().getId();		
		session.setAttribute(sessionId+"_cache", reportParamsMap);
		Struts2Utils.renderText(sessionId);
		return null;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Map<String, String> getReportParamsMap() {
		return reportParamsMap;
	}
	public void setReportParamsMap(Map<String, String> reportParamsMap) {
		this.reportParamsMap = reportParamsMap;
	}
}
