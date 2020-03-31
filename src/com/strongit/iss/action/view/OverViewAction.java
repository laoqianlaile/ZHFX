package com.strongit.iss.action.view;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.service.IOverViewService;
import com.strongit.iss.service.impl.ReportCacheServiceImpl;

public class OverViewAction extends BaseActionSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ReportCacheServiceImpl reportCacheService;
	
	@Autowired
	private IOverViewService overViewService;
	
    //页面过滤条件
	private Map<String,String> params=new HashMap<String,String>();
	
	/**
	 * 审核备地图
	 * @orderBy 
	 * @return
	 * @author wuwei
	 * @Date 2016年11月3日上午10:10:29
	 */
	public String getAuditPreparationByPlace(){
		Struts2Utils.renderJson(overViewService.getAuditPreparationByPlace(params));
		return null;
	}
	
	/**
	 * 审核备国标行业
	 * @orderBy 
	 * @return
	 * @author wuwei
	 * @Date 2016年11月3日上午10:11:45
	 */
	public String getAuditPreparationByGBIndustry(){
		Struts2Utils.renderJson(overViewService.getAuditPreparationByGBIndustry(params));
		return null;
	}
	
	/**
	 * 审核备趋势
	 * @orderBy 
	 * @return
	 * @author wuwei
	 * @Date 2016年11月3日上午10:13:27
	 */
	public String getAuditPreparationTrends(){
		Struts2Utils.renderJson(overViewService.getAuditPreparationTrends(params));
		return null;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}	
}
