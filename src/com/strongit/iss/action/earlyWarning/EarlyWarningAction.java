package com.strongit.iss.action.earlyWarning;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Datagrid;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.entity.EarlyWarning;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.IEarlyWarningService;

import net.sf.json.JSONObject;
/**
 * 
 * @author tanghw
 *
 */
@SuppressWarnings("serial")
public class EarlyWarningAction  extends BaseActionSupport<EarlyWarning>{
	private Page<EarlyWarning> earlyWarningPage = new Page<EarlyWarning>(20, true);
	//第一页
	protected int page = 1;
	//页容量
	protected int rows = 20;
	
	//预警信息主键
	private String earlyWarningId;
	
	@Autowired
	private IEarlyWarningService earlyWarningService;
	
	/**
	 * 预警信息一览页面
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年12月12日下午3:23:37
	 */
	public String list() {
		return "list";
	}
	/**
	 * 获取预警信息list
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年12月12日下午3:22:36
	 */
	public String getEarlyWarningList() {
		bulidPage();
		String warningName = Struts2Utils.getRequest().getParameter("name");
		long start =System.currentTimeMillis();
		earlyWarningPage = earlyWarningService.getEarlyWarningList(earlyWarningPage, warningName);
		long end =System.currentTimeMillis();
		logger.debug("execute earlyWarningService.getEarlyWarningList method cost time : "
				+ (end - start) + " mills.");
		List<EarlyWarning> list = earlyWarningPage.getResult();
		//formatList(list);
		// 封装到datagrid中让easyui控件渲染
		Datagrid<EarlyWarning> dg = new Datagrid<EarlyWarning>(earlyWarningPage.getTotalCount(), list);
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
		earlyWarningPage.setPageNo(page);
		earlyWarningPage.setPageSize(rows);
	}
	/**
	 * 预警信息编辑页面
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年12月12日下午3:55:08
	 */
	public String input() {
		return "input";
	}
	
	/**
	 *根据预警信息id获取预警信息
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年12月12日下午4:00:39
	 */
	public String getBaseInfo() {
		//定义一个map
		Map<String, Object> map = new HashMap<String, Object>();
		//主键ID
//		String guid = Struts2Utils.getRequest().getParameter("w");
		//主键不为空
		if (StringUtils.isNotBlank(earlyWarningId)) {
			EarlyWarning earlyWarning = earlyWarningService.getById(earlyWarningId);
			if (null != earlyWarning) {
				//主键ID
				map.put("id", earlyWarning.getId());
				//预警分类
				map.put("warningType", earlyWarning.getWarningType());
				// 指标名名称
				map.put("warningName", earlyWarning.getWarningName());
				// 计算公式说明
				map.put("calculationFormula", earlyWarning.getCalculationFormula());
				// 是否预警指标
				map.put("iswarning", earlyWarning.getIswarning());
				//预警值
				map.put("warningValue",earlyWarning.getWarningValue());
				// 预警等级
				map.put("warningLevel",earlyWarning.getWarningLevel());
				//预警区间（开始值）
				map.put("statrWarningValue",earlyWarning.getStatrWarningValue());
				//预警区间（结束值）
				map.put("endWorningValue",earlyWarning.getEndWorningValue());
				//排序
				map.put("sort", earlyWarning.getSort());
				//传值到前台
				Struts2Utils.renderJson(map);
			}
		}
		return null;
	}
	
	/**
	 * 保存预警信息
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年12月12日下午4:01:11
	 */
	public String save() {
		//获取页面上传递过来的参数
		String entity = Struts2Utils.getRequest().getParameter("entity");
		//定义预警信息对象
		EarlyWarning earlyWarning = null;
		//若json字符串不为空
		if (StringUtils.isNotBlank(entity)) {
			try {
				//解码，格式为UTF-8
				entity = URLDecoder.decode(entity, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//将json字符串转换为java实体对象
			earlyWarning = (EarlyWarning) JSONObject.toBean(
					JSONObject.fromObject(entity), EarlyWarning.class);
		}
		//若对象不为空
		if (null != earlyWarning) {
			if (StringUtils.isNotBlank(earlyWarning.getId())) {
				//更新预警信息
				earlyWarningService.updateEntity(earlyWarning);
			} else {
				//保存预警信息
				earlyWarningService.saveEntity(earlyWarning);
			}
		}
		return renderText("true");
	}
	
	/**
	 * 删除预警信息
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年12月12日下午4:01:22
	 */
	public String delete() {
		//主键ID不为空
		if (StringUtils.isNotBlank(earlyWarningId)) {
			//根据主键ID获取对象信息
			EarlyWarning earlyWarning = earlyWarningService.getById(earlyWarningId);
			//若对象不为空
			if (null != earlyWarning) {
				//删除
				earlyWarningService.deleteEntity(earlyWarning);
			}
		}
		return renderText("true");
	}
	
	
	public Page<EarlyWarning> getEarlyWarningPage() {
		return earlyWarningPage;
	}
	public void setEarlyWarningPage(Page<EarlyWarning> earlyWarningPage) {
		this.earlyWarningPage = earlyWarningPage;
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
	public String getEarlyWarningId() {
		return earlyWarningId;
	}
	public void setEarlyWarningId(String earlyWarningId) {
		this.earlyWarningId = earlyWarningId;
	}
	
	
}
