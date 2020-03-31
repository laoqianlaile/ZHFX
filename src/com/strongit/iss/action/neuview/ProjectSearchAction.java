package com.strongit.iss.action.neuview;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Cache;
import com.strongit.iss.common.RecursiveData;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.common.TreeNode;
import com.strongit.iss.service.IProjectSearchService;

/**
 * 得到搜索条件下拉框的树结构数据
 * @author lbw
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class ProjectSearchAction extends BaseActionSupport{
	@Autowired
	private IProjectSearchService projectSearchService;
	
	public String getIndustry(){
		String groupNo=this.getRequest().getParameter("groupNo");		
		List<Map<String, Object>> list=projectSearchService.dicIndustry(groupNo);
	    List<TreeNode>	nodes=RecursiveData.RecursiveDept(list);
		Struts2Utils.renderJson(nodes);
		return null;
	}
	/**
	 * 获取toolbar搜索框的下拉框树
	 * @orderBy 
	 * @return
	 * @author lbw
	 * @Date 2016年8月5日下午1:51:20
	 */
	public String getFromCache() {
		String groupNo=this.getRequest().getParameter("groupNo");
		List<Map<String, Object>> list = Cache.getByGroupNo(groupNo);
		List<TreeNode>	nodes=RecursiveData.RecursiveDept(list);
		Struts2Utils.renderJson(nodes);
		return null;
	}
	/**
	 * 获取项目申报单位树
	 * @orderBy 
	 * @return
	 * @author xiangyong
	 * @Date 2016年8月2日下午2:34:05
	 */
	public String getProjectUnitTree(){
		List<Map<String, Object>> list=projectSearchService.getProjectUnit();
	    List<TreeNode>	nodes=RecursiveData.RecursiveDept(list);
		Struts2Utils.renderJson(nodes);
		return null;
	}
}
