package com.strongit.iss.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.struts2.ServletActionContext;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.common.Cache;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.ProjectFileSqlConstants;
import com.strongit.iss.common.PropertiesUtil;
import com.strongit.iss.entity.ProjectFileSearchVo;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IProjectFileService;


/**
 * @author XiaXiang
 *
 */
@Service
@Transactional
public class ProjectFileServiceImpl extends BaseService implements IProjectFileService {

	/**
	 * 根据项目ID获取项目履历信息
	 * @orderBy 
	 * @param projectGuid
	 * @param type
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:20:12
	 */
	@Override
	public List<Map<String, Object>> getProjectRecordByGuid(String projectGuid, String type,String moduleCode) {
		//根据项目id模块获取对应的map集合
		Map<String, Object> idMap = this.getIdByprojectGuid(projectGuid, moduleCode);
		//模块为储备的时候项目id = projectGuid 项目id就= map.get("PROJECT_ID")
		if(idMap!=null){
			projectGuid = idMap.get("PROJECT_ID").toString().trim();
		}
		String sql = "";
		//判断类型不为空
		if (StringUtils.isNotBlank(type)) {
			//项目履历
			if ("1".equals(type)) {
				//计划ID
				String rollId = this.getRollProjectIdByGuid(projectGuid);
				sql = String.format(ProjectFileSqlConstants.XM_RECORD_SQL, 
						("'" + projectGuid + "'"), ("'" + rollId + "'"));
			//审核备项目履历
			} else if ("2".equals(type)) {
				//项目CODE
				String proId = this.getProjectCodeByGuid(projectGuid);
				sql = String.format(ProjectFileSqlConstants.SHB_RECORD_SQL, ("'" + proId + "'"));
			}
		}
		//根据sql获取列表数据
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		//返回数据
		return list;
	}

	/**
	 * 根据ID获取PROJECT_CODE
	 * @orderBy 
	 * @param projectGuid
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月19日上午11:26:51
	 */
	public String getProjectCodeByGuid(String projectGuid) {
		String sql = String.format(ProjectFileSqlConstants.GET_PROJECT_CODE, ("'" + projectGuid + "'"));
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		if (null != list && list.size() > 0) {
			if(list.get(0).get("PRO_CODE_AREA")!=null&&!"".equals(list.get(0).get("PRO_CODE_AREA"))){
				return list.get(0).get("PRO_CODE_AREA").toString();
			}else{
				return "";	
			}
		}
		return "";
	}

	/**
	 * 根据项目ID获取计划ID
	 * @orderBy 
	 * @param projectGuid
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月19日上午10:23:29
	 */
	public String getRollProjectIdByGuid(String projectGuid) {
		String sql = String.format(ProjectFileSqlConstants.GET_ROLLOLAN_ID, ("'" + projectGuid + "'"));
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		if (null != list && list.size() > 0) {
			return list.get(0).get("ID").toString();
		}
		return "";
	}
	/**
	 * 根据项目模块和项目id获取id计划（计划获取储备，年度获取计划和储备，专项债获取计划和储备）
	 * @orderBy 
	 * @param projectGuid
	 * @param moduleCode
	 * @return
	 * @author tanghw
	 * @Date 2016年11月17日下午2:15:42
	 */
	private Map<String, Object> getIdByprojectGuid(String projectGuid,String moduleCode){
		String sql = "";
		//模块为FIVE_PLAN-五年储备
		if(Constant.FIVE_PLAN.equals(moduleCode)){
			return null;
		//ROLL_PLAN-三年滚动计划
		}else if(Constant.ROLL_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.GET_ID_BY_ROLLOLAN_ID, ("'" + projectGuid + "'"));
		//FUNDS-专项建设基金
		}else if(Constant.FUNDS.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.GET_ID_BY_BONDPLAN_ID, ("'" + projectGuid + "'"));
		//BUDGETDIAPATCH-中央预算内调度//B-中央预算内下达//B-中央预算内申报	--年度计划
		}else if(Constant.BUDGETDIAPATCH.equals(moduleCode)||Constant.BUDGETISSUE.equals(moduleCode)||Constant.BUDGETREPORT.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.GET_ID_BY_YEARPLAN_ID, ("'" + projectGuid + "'"));
		}else{
			return null;	
		}
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		return list.get(0);
	}
	/**
	 * 获取基本信息
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:52:20
	 */
	@Override
	public List<Map<String, Object>> getBaseInfoByGuid(String projectGuid,String moduleCode) {
		String sql = String.format(ProjectFileSqlConstants.BASIC_SQL, ("'" + projectGuid + "'"));
		//模块为FIVE_PLAN-五年储备
		if(Constant.FIVE_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.BASIC_SQL, ("'" + projectGuid + "'"));
		//ROLL_PLAN-三年滚动计划
		}else if(Constant.ROLL_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.PLAN_BASIC_SQL, ("'" + projectGuid + "'"));
		//FUNDS-专项建设基金
		}else if(Constant.FUNDS.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.BONDPLAN_BASIC_SQL, ("'" + projectGuid + "'"));
		//BUDGETDIAPATCH-中央预算内调度//B-中央预算内下达//B-中央预算内申报	--年度计划
		}else if(Constant.BUDGETDIAPATCH.equals(moduleCode)||Constant.BUDGETISSUE.equals(moduleCode)||Constant.BUDGETREPORT.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.YEARPLAN_BASIC_SQL, ("'" + projectGuid + "'"));
		}
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		// 字段code值转换成页面显示value值
		if (list != null ) {
			list = this.convertItemKeyToValue(list);
			
		}
		return list;
	}
	/**
	 * 获取量化建设规模
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日上午11:16:49
	 */
	public List<Map<String, Object>> getQuaInfoByGuid(String projectGuid,String moduleCode){
		String sql = String.format(ProjectFileSqlConstants.QUA_SQL, ("'" + projectGuid + "'"), ("'" + PropertiesUtil.getInfoByName(Constant.QUA_PROJECT_ITEM_TYPE_NAME) + "'"));
		//模块为FIVE_PLAN-五年储备
		if(Constant.FIVE_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.QUA_SQL, ("'" + projectGuid + "'"), ("'" + PropertiesUtil.getInfoByName(Constant.QUA_PROJECT_ITEM_TYPE_NAME) + "'"));
		//ROLL_PLAN-三年滚动计划
		}else if(Constant.ROLL_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.PLAN_QUA_SQL, ("'" + projectGuid + "'"), ("'" + PropertiesUtil.getInfoByName(Constant.QUA_PROJECT_ITEM_TYPE_NAME) + "'"));
		//FUNDS-专项建设基金
		}else if(Constant.FUNDS.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.BONDPLAN_QUA_SQL, ("'" + projectGuid + "'"), ("'" + PropertiesUtil.getInfoByName(Constant.QUA_PROJECT_ITEM_TYPE_NAME) + "'"));
		//BUDGETDIAPATCH-中央预算内调度//B-中央预算内下达//B-中央预算内申报	--年度计划
		}else if(Constant.BUDGETDIAPATCH.equals(moduleCode)||Constant.BUDGETISSUE.equals(moduleCode)||Constant.BUDGETREPORT.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.YEARPLAN_QUA_SQL, ("'" + projectGuid + "'"), ("'" + PropertiesUtil.getInfoByName(Constant.QUA_PROJECT_ITEM_TYPE_NAME) + "'"));
		}
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		return list;
	}
	/**
	 * 审核备办理事项
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:53:24
	 */
	@Override
	public List<Map<String, Object>> getMatterInfoByGuid(String projectGuid,String moduleCode) {
		//根据项目id模块获取对应的map集合
		Map<String, Object> idMap = this.getIdByprojectGuid(projectGuid, moduleCode);
		//模块为储备的时候项目id = projectGuid 项目id就= map.get("PROJECT_ID")
		if(idMap!=null){
			projectGuid = idMap.get("PROJECT_ID").toString().trim();
		}
		//项目CODE
		String proId = this.getProjectCodeByGuid(projectGuid);
		String sql = String.format(ProjectFileSqlConstants.SHBBL_SQL, ("'" + proId + "'"));
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		return list;
	}

	/**
	 * 投资情况
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:54:20
	 */
	@Override
	public List<Map<String, Object>> getInvestmentInfoByGuid(String projectGuid,String moduleCode) {
		String sql = String.format(ProjectFileSqlConstants.INVEST_SQL, ("'" + projectGuid + "'"));
		//模块为FIVE_PLAN-五年储备
		if(Constant.FIVE_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.INVEST_SQL, ("'" + projectGuid + "'"));
		//ROLL_PLAN-三年滚动计划
		}else if(Constant.ROLL_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.PLAN_INVEST_SQL, ("'" + projectGuid + "'"));
		//FUNDS-专项建设基金
		}else if(Constant.FUNDS.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.BONDPLAN_INVEST_SQL, ("'" + projectGuid + "'"));
		//BUDGETDIAPATCH-中央预算内调度//B-中央预算内下达//B-中央预算内申报	--年度计划
		}else if(Constant.BUDGETDIAPATCH.equals(moduleCode)||Constant.BUDGETISSUE.equals(moduleCode)||Constant.BUDGETREPORT.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.YEARPLAN_INVEST_SQL, ("'" + projectGuid + "'"));
		}
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		return list;
	}

	/**
	 * 计划下达情况
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:54:51
	 */
	@Override
	public List<Map<String, Object>> getIssuedInfoByGuid(String projectGuid,String moduleCode) {
		//根据项目id模块获取对应的map集合
		Map<String, Object> idMap = this.getIdByprojectGuid(projectGuid, moduleCode);
		//模块为储备的时候项目id = projectGuid 项目id就= map.get("PROJECT_ID")
		if(idMap!=null){
			projectGuid = idMap.get("PROJECT_ID").toString().trim();
		}
		String sql = String.format(ProjectFileSqlConstants.INVEST_DOWL_SQL, ("'" + projectGuid + "'"));
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		return list;
	}

	/**
	 * 资金到位完成情况
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:55:25
	 */
	@Override
	public List<Map<String, Object>> getFinishInfoByGuid(String projectGuid,String moduleCode) {
		//根据项目id模块获取对应的map集合
		Map<String, Object> idMap = this.getIdByprojectGuid(projectGuid, moduleCode);
		//模块为储备的时候项目id = projectGuid 项目id就= map.get("PROJECT_ID")
		if(idMap!=null){
			projectGuid = idMap.get("PROJECT_ID").toString().trim();
		}
		String sql = String.format(ProjectFileSqlConstants.INVEST_PUT_SQL, ("'" + projectGuid + "'"));
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		return list;
	}

	/**
	 * 各期项目调度数据
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:56:08
	 */
	@Override
	public List<Map<String, Object>> getDispatchInfoByGuid(String projectGuid,String moduleCode) {
		//根据项目id模块获取对应的map集合
		Map<String, Object> idMap = this.getIdByprojectGuid(projectGuid, moduleCode);
		//模块为储备的时候项目id = projectGuid 项目id就= map.get("PROJECT_ID")
		if(idMap!=null){
			projectGuid = idMap.get("PROJECT_ID").toString().trim();
		}
		//根据项目id获取项目所有调度情况实施信息根据调度期号倒序排序
		String sql = String.format(ProjectFileSqlConstants.PROJECT_DISPATCH_IMPL, ("'" + projectGuid + "'"));
		List<Map<String, Object>> dispatchlist = this.dao.findBySql(sql, new Object[]{});
		// 字段code值转换成页面显示value值
		if (dispatchlist != null ) {
			dispatchlist = this.convertItemKeyToValue(dispatchlist);
		}
		//循环获取调度项目的资金到位情况和资金完成情况
		for(int i=0;i<dispatchlist.size();i++){
			//获取调度实施情况表id
			String DprojectTmplId = dispatchlist.get(i).get("ID").toString();
			//根据调度实施id获取调度实施资金到位情况信息
			String dispatchInvestDowlSql = String.format(ProjectFileSqlConstants.DISPATCH_INVEST_DOWL_SQL, ("'" + DprojectTmplId + "'"));
			List<Map<String, Object>> dispatchInvestDowllist = this.dao.findBySql(dispatchInvestDowlSql, new Object[]{});
			//根据调度实施id获取调度实施资金完成情况信息
			String dispatchInvestPutSql = String.format(ProjectFileSqlConstants.DISPATCH_INVEST_PUT_SQL, ("'" + DprojectTmplId + "'"));
			List<Map<String, Object>> dispatchInvestPutlist = this.dao.findBySql(dispatchInvestPutSql, new Object[]{});
			//对应的调度信息map放入对应的资金到位情况信息
			dispatchlist.get(i).put("dispatchInvestDowllist", dispatchInvestDowllist);
			//对应的调度信息map放入对应的资金完成情况信息
			dispatchlist.get(i).put("dispatchInvestPutlist", dispatchInvestPutlist);
		}
		return dispatchlist;
	}
	
	/**
	 * 根据项目id获取项目前期工作信息
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日上午11:39:40
	 */
	@Override
	public List<Map<String, Object>> getPreworkInfoByGuid(String projectGuid,String moduleCode) {
		String sql = String.format(ProjectFileSqlConstants.PREWORK_SQL, ("'" + projectGuid + "'"));
		//模块为FIVE_PLAN-五年储备
		if(Constant.FIVE_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.PREWORK_SQL, ("'" + projectGuid + "'"));
		//ROLL_PLAN-三年滚动计划
		}else if(Constant.ROLL_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.PLAN_PREWORK_SQL, ("'" + projectGuid + "'"));
		//FUNDS-专项建设基金
		}else if(Constant.FUNDS.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.BONDPLAN_PREWORK_SQL, ("'" + projectGuid + "'"));
		//BUDGETDIAPATCH-中央预算内调度//B-中央预算内下达//B-中央预算内申报	--年度计划
		}else if(Constant.BUDGETDIAPATCH.equals(moduleCode)||Constant.BUDGETISSUE.equals(moduleCode)||Constant.BUDGETREPORT.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.YEARPLAN_PREWORK_SQL, ("'" + projectGuid + "'"));
		}
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		return list;
	}
	/**
	 * 根据项目id获取项目资金构成信息
	 * @orderBy 
	 * @param projectGuid
	 * @param projectGuid 项目模块
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日下午12:13:05
	 */
	@Override
	public List<Map<String, Object>> getInvestConstituteInfoByGuid(String projectGuid,String moduleCode) {
		String sql = String.format(ProjectFileSqlConstants.CONSTITULE_INVEST_SQL, ("'" + projectGuid + "'"));
		//模块为FIVE_PLAN-五年储备
		if(Constant.FIVE_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.CONSTITULE_INVEST_SQL, ("'" + projectGuid + "'"));
		//ROLL_PLAN-三年滚动计划
		}else if(Constant.ROLL_PLAN.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.PLAN_CONSTITULE_INVEST_SQL, ("'" + projectGuid + "'"));
		//FUNDS-专项建设基金
		}else if(Constant.FUNDS.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.BONDPLAN_CONSTITULE_INVEST_SQL, ("'" + projectGuid + "'"));
		//BUDGETDIAPATCH-中央预算内调度//B-中央预算内下达//B-中央预算内申报	--年度计划
		}else if(Constant.BUDGETDIAPATCH.equals(moduleCode)||Constant.BUDGETISSUE.equals(moduleCode)||Constant.BUDGETREPORT.equals(moduleCode)){
			sql = String.format(ProjectFileSqlConstants.YEARPLAN_CONSTITULE_INVEST_SQL, ("'" + projectGuid + "'"));
		}
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		return list;
	}
	/**
	 * 根据项目id获取项目调度项目实施情况
	 * @orderBy 
	 * @param projectGuid
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日下午4:29:59
	 */
	@Override
	public List<Map<String, Object>> getDispatchTmplInfoByGuid(String projectGuid) {
		//根据项目id获取项目所有调度情况实施信息根据调度期号倒序排序
		String sql = String.format(ProjectFileSqlConstants.PROJECT_DISPATCH_IMPL, ("'" + projectGuid + "'"));
		List<Map<String, Object>> dispatchlist = this.dao.findBySql(sql, new Object[]{});
		//循环获取调度项目的资金到位情况和资金完成情况
		for(int i=0;i<dispatchlist.size();i++){
			//获取调度实施情况表id
			String DprojectTmplId = dispatchlist.get(i).get("ID").toString();
			//根据调度实施id获取调度实施资金到位情况信息
			String dispatchInvestDowlSql = String.format(ProjectFileSqlConstants.DISPATCH_INVEST_DOWL_SQL, ("'" + DprojectTmplId + "'"));
			List<Map<String, Object>> dispatchInvestDowllist = this.dao.findBySql(dispatchInvestDowlSql, new Object[]{});
			//根据调度实施id获取调度实施资金完成情况信息
			String dispatchInvestPutSql = String.format(ProjectFileSqlConstants.DISPATCH_INVEST_PUT_SQL, ("'" + DprojectTmplId + "'"));
			List<Map<String, Object>> dispatchInvestPutlist = this.dao.findBySql(dispatchInvestPutSql, new Object[]{});
			//对应的调度信息map放入对应的资金到位情况信息
			dispatchlist.get(i).put("dispatchInvestDowllist", dispatchInvestDowllist);
			//对应的调度信息map放入对应的资金完成情况信息
			dispatchlist.get(i).put("dispatchInvestPutlist", dispatchInvestPutlist);
		}
		return dispatchlist;
	}
	/**
	 * 在查询结果中，根据字典字段的key值，增加一个对应value的字段值
	 * @orderBy 
	 * @param list
	 * @return
	 * @author tanghw
	 * @Date 2016年10月29日下午3:49:49
	 */
	public List<Map<String, Object>> convertItemKeyToValue(List<Map<String, Object>> Info){
		
		// 返回集合变量
	
		List<Map<String, Object>> covertResult = new ArrayList<Map<String,Object>>();
		//定义键值对的值对象
		String keyValue = "";
		
		//获取字典项信息
		for (Map<String, Object> obj : Info) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 循环map对象的key值，判断是否需要进行code-value转换
			for(String key : obj.keySet()) {
				map.put(key, obj.get(key));
				// 如果循环的字段为：建设地点
				if (Constant.CODE_COLUMN_BUILD_PLACE.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.BUILD_PLACE_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：国标行业
				} else if (Constant.CODE_COLUMN_GB_INDUSTRY.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.GB_INDUSTRY_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：项目类型	
				} else if (Constant.CODE_COLUMN_PRO_TYPE.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.PRO_TYPE_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：所属行业（发改委行业）
				} else if (Constant.CODE_COLUMN_INDUSTRY.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.INDUSTRY_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：国别
				}else if (Constant.CODE_COLUMN_COUNTRY.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.COUNTRY_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：符合规划
				}else if (Constant.CODE_COLUMN_PLAN.equalsIgnoreCase(key)) { 
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.PLAN_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				 // 如果循环的字段为：符合重大战略
				}else if (Constant.CODE_COLUMN_MAJOR_STRATEGY.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.MAJOR_STRATEGY_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：符合政府投资方向
				}else if (Constant.CODE_COLUMN_GOVERNMENT_INVEST_DIRECTION.equalsIgnoreCase(key)) { 
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.GOVERNMENT_INVEST_DIRECTION_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：是否PPP(字典项)
				}else if (Constant.CODE_COLUMN_ISPPP.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.ISPPP_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：政府参与方式
				}else if (Constant.CODE_COLUMN_GOVERNMENT_JOIN_TYPE.equalsIgnoreCase(key)) {
					//因为政府参与方式是多选所有要进行拆分拼接
					if(null!=obj.get(key)&&"".equals(obj.get(key))){
						//值拆分
						String[] values = obj.get(key).toString().split(",");
						//遍历获取刚刚政府参与方式名称
						for (int i = 0; i < values.length; i++) {
							if(i>0){
								keyValue = keyValue+",";
							}
							keyValue =keyValue+ Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.GOVERNMENT_JOIN_TYPE_GROUPNO), String.valueOf(obj.get(key)));
						}
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：形象进度
				}else if (Constant.CODE_COLUMN_IMAGE_PROGRESS.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.IMAGE_PROGRESS_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：(一带一路)类型
				}else if (Constant.CODE_COLUMN_ONE_ONE_TYPE.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.ONE_ONE_TYPE_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为：建议银行
				}else if (Constant.CODE_COLUMN_PROPOSED_BANK.equalsIgnoreCase(key)) { 
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.PROPOSED_BANK_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为： 专项类别
				}else if (Constant.CODE_COLUMN_SPECIAL_TYPE.equalsIgnoreCase(key)) { 
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.SPECIAL_TYPE_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				 // 如果循环的字段为： 招投标形式
				}else if (Constant.CODE_COLUMN_BIDDING_MODE.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.BIDDING_MODE_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				// 如果循环的字段为： 建设性质
				}else if (Constant.CODE_COLUMN_BUILD_NATURE.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.BUILD_NATURE_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
					// 如果循环的字段为：项目成熟度
				}else if (Constant.CODE_COLUMN_STAGE.equalsIgnoreCase(key)) {
					// 获取CODE对应的VALUE
					keyValue = Cache.getNameByCode(PropertiesUtil.getInfoByName(Constant.STAGE_GROUPNO), String.valueOf(obj.get(key)));
					if (StringUtils.isNotBlank(keyValue)) {
						// 设置页面展示值
						map.put(key,keyValue);
					} else {
						// 设置页面展示值
						map.put(key, "");
					}
				}
				
				
			}
			// 返回转换后的结合对象
			covertResult.add(map);
		}
		return covertResult;
	}
	
	/*@Override
	public Page<Map<String, Object>> searchWarningProject1(String userId,Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo
			,Map<String, String> filters,Map<String, String> reportParamsMap) {
		Page<Map<String, Object>> resultPage = null;
		String filterSql =" SELECT * FROM PROJECT P left join project_dispatch_impl yp on p.id=yp.project_id"
						  +" left join PROJECT_INFO_EXT_MASTER PM on P.ID = PM.PROJECT_ID"
						  +" left join DICTIONARY_ITEMS dic on PM.BUILD_PLACE = dic.item_key and dic.group_no   ='1'"
						  +" INNER JOIN ("
						  +" SELECT DISTINCT"
						  +" PROJECT_ID                     ,"
						  +" MAX(IS_PLAN)     AS IS_PLAN    ,"
						  +" MAX(STORE_LEVEL) AS STORE_LEVEL,"
						  +" MAX(STORE_TIME)  AS STORE_TIME"
						  +" FROM"
						  +" PROJECT_STORE_STATUS"
						  +" GROUP BY"
						  +" PROJECT_ID"
						  +" ) AS PSL"
						  +"ON"
						  +"P.ID = PSL.PROJECT_ID"
						  +"left join department dept on P.CREATE_DEPARTMENT_GUID = dept.department_guid"
						  +"where substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1) in ('FGW', 'DEPT', 'CENTRE-COM')"
						  +"and P.DELETE_FLAG         ='0' and exists"
						  +"(select 1 from v_warning_analysis vwa where vwa.project_id = p.id and vwa.warning_code=" +filters.get("warnCode")+")";
			//获取参数
			resultPage = this.dao.findBySql(pageBo, filterSql);
			// 字段code值转换成页面显示value值
			if (resultPage != null ) {
				List<Map<String, Object>> rs = this.convertItemKeyToValue(resultPage.getResult());
	
				resultPage.setResult(rs);
			}
			return resultPage;		
	}*/
	
	/**
	 * 根据条件查询项目
	 * @orderBy 
	 * @param userId
	 * @param pageBo
	 * @param projectVo
	 * @param reportParamsMap
	 * @return
	 * @author tanghw
	 * @Date 2016年10月29日下午7:50:21
	 */
	@Override
	public Page<Map<String, Object>> searchProject(String userId,Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo
			,Map<String, String> filters,Map<String, String> reportParamsMap) {
		// 定义查询结果返回接收变量
		Page<Map<String, Object>> resultPage = null;
		//定义查询的字段
		String filterSql ="SELECT P.ID,P.STAGE,PM.PRO_NAME ,PM.INVESTMENT_TOTAL,PM.PRO_TYPE,PM.INDUSTRY,PM.BUILD_PLACE,PM.EXPECT_STARTYEAR ";
		List<Object> paramList = new ArrayList<Object>();
		if (!filters.isEmpty()) {
			// 为查询共通SQL拼过滤条件
				// 模块名称不为空且不为五年储备的情况下
				if (StringUtils.isNotEmpty(filters.get("moduleCode"))&&!Constant.FIVE_PLAN.equals(filters.get("moduleCode").trim().toString())){
					//ROLL_PLAN-三年滚动计划
					if(Constant.ROLL_PLAN.equals(filters.get("moduleCode").trim().toString())){
						filterSql += getCommonSQL("plan_project","project_id")+"  left join PLAN_PROJECT_INFO_EXT_MASTER PM on p.id = PM.plan_project_id "
								  + " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' ";
						//模块过滤状态
						//报送到国家
						if(StringUtils.isNotEmpty(filters.get("filterStatus"))
								&&Constant.REPORT_UNIT.equals(filters.get("filterStatus"))){
							//报送到国家即判断滚动计划项目的审核状态表的审核部门等级为4
							filterSql +=" left join PLAN_PROJECT_CHECK_STATUS PCS on PCS.ROLL_PLAN_PROJECT_ID = P.ID"
									+ " left join DEPARTMENT D on PCS.DEPARTMENT_FGW_GUID = D.DEPARTMENT_GUID "
									+ " WHERE D.STORE_LEVEL= '4' AND P.DELETE_FLAG ='0' and SUBSTRING(PCS.DEPARTMENT_FGW_GUID, 0, 6) = 'GUOJIA'";
						//全部
						}else{
							filterSql += "  where  P.DELETE_FLAG ='0' ";
						}
					//FUNDS-专项建设基金
					}else if(Constant.FUNDS.equals(filters.get("moduleCode").trim().toString())){
						filterSql += " from BOND_PLAN_PROJECT P left join BOND_PLAN_PROJECT_INFO_EXT_MASTER PM on PM.BOND_PLAN_PROJECT_ID = P.ID"
								+ " left join BOND_PLAN_PROJECT_FILE_NO bfn on p.BOND_PLAN_FILE_NO = bfn.id "
								+ " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' "
								//专项建设投放资金
								+ " left join BOND_PLAN_PROJECT_INFO_EXT_INVEST PI on P.id = PI.BOND_PLAN_PROJECT_ID and PI.PROJECT_ITEM_EXT_ID = 'A00018'"
								+ " WHERE  bfn.file_name is not null and P.DELETE_FLAG ='0' and P.STATUS='PUTIN' ";
						//第几批专项建设基金项目
						if(StringUtils.isNotEmpty(filters.get("fundLevel"))&&!"undefined".equals(filters.get("fundLevel"))){
							try {
								filterSql += " and bfn.FILE_NAME like '%"+new String( filters.get("fundLevel").getBytes( "ISO8859-1" ), "utf8" ).trim()+"%' ";
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//项目所用银行
						if(StringUtils.isNotEmpty(filters.get("bank"))&&!"undefined".equals(filters.get("bank"))){
							filterSql += " and PM.PROPOSED_BANK = '"+filters.get("bank").trim().toString()+"'";
						}
					//B-中央预算内申报	
					}else if(Constant.BUDGETREPORT.equals(filters.get("moduleCode").trim().toString())){
						filterSql += getCommonSQL("YEAR_PLAN_PROJECT","")+"  left join YEAR_PLAN_PROJECT_INFO_EXT_MASTER PM on P.id = PM.YEAR_PLAN_PROJECT_ID "
								+ " left join year_plan_project_file_no ypfn on p.year_plan_file_no = ypfn.id "
								+ " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' "
								//中央预算内下达资金
								+ " left join year_plan_project_info_ext_invest PI on P.id = PI.year_plan_project_id and PI.PROJECT_ITEM_EXT_ID = 'A00016'"
								+ " WHERE  P.DELETE_FLAG ='0' and ypfn.CREATE_TIME >= to_date('2016-07-01', 'yyyy-mm-dd') "
								+ " and not exists (select yppb.project_id from  YEAR_PLAN_PROJECT_BUNDLED yppb where p.id = yppb.project_id)";
					//B-中央预算内下达
					}else if(Constant.BUDGETISSUE.equals(filters.get("moduleCode").trim().toString())){
						filterSql += getCommonSQL("YEAR_PLAN_PROJECT","")+"   left join YEAR_PLAN_PROJECT_INFO_EXT_MASTER PM on P.id = PM.YEAR_PLAN_PROJECT_ID "
								+ " left join PROJECT_DISPATCH_FUNDS_ISSUED PDFI on P.ID=PDFI.SOURCE_ID "
								+ " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' "
								//中央预算内下达资金
								+ " left join year_plan_project_info_ext_invest PI on P.id = PI.year_plan_project_id and PI.PROJECT_ITEM_EXT_ID = 'A00016'"
								+ " WHERE  P.EXPORT_FILE_NO IS NOT NULL AND  P.DELETE_FLAG ='0' AND P.ID=PDFI.SOURCE_ID AND P.EXPORT_FILE_NO = PDFI.ISSUED_FILE_NO ";
					//BUDGETDIAPATCH-中央预算内调度
					}else if(Constant.BUDGETDIAPATCH.equals(filters.get("moduleCode").trim().toString())){
						filterSql += getCommonSQL("YEAR_PLAN_PROJECT","")+"   left join YEAR_PLAN_PROJECT_INFO_EXT_MASTER PM on P.id = PM.YEAR_PLAN_PROJECT_ID "
						        + " left join PROJECT_DISPATCH_IMPL PDI on P.id = PDI.SOURCE_ID "
						        + " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' "
						        //中央预算内到位资金
						        + " left join PROJECT_DISPATCH_IMPL_INVEST  PII on P.id = PII.PROJECT_IMPL_ID and PII.PROJECT_ITEM_EXT_ID = 'A00016'"
						        + " WHERE  P.DELETE_FLAG ='0' and PDI.REPORT_NUMBER IS NOT NULL  ";
						//模块过滤状态s
						//开工项目
						if(StringUtils.isNotEmpty(filters.get("filterStatus"))
								&&Constant.START_DIAPATCH_PROJECTS.equals(filters.get("filterStatus"))){
							filterSql += " and PDI.ACTUAL_START_TIME IS NOT NULL  and date_format(PDI.ACTUAL_START_TIME,'%Y%m') <= PDI.REPORT_NUMBER ";
						}
						//竣工项目
						if(StringUtils.isNotEmpty(filters.get("filterStatus"))
								&&Constant.END_DIAPATCH_PROJECTS.equals(filters.get("filterStatus"))){
							filterSql += " and PDI.ACTUAL_END_TIME IS NOT NULL  and date_format(PDI.ACTUAL_END_TIME,'%Y%m') <= PDI.REPORT_NUMBER ";
						}
					}
				//模块为FIVE_PLAN-五年储备或者来源是直接点目录项目档案
				}else{
					String warnSQL="";
					if(StringUtils.isNotBlank(filters.get("warnCode"))){
						//select COUNT(1) from v_life_cycle  vlc where vlc.project_stage=8 and vlc.projectflag=0;
						warnSQL=" and  exists(select 1 from  v_warning_analysis  vwa where  vwa.id = p.id and vwa.warning_code= "+filters.get("warnCode")+") ";
					}
					filterSql +=  getCommonSQL("project","id")+"  left join PROJECT_INFO_EXT_MASTER PM on P.ID = PM.PROJECT_ID "
							+ " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' "
							+ " INNER JOIN(SELECT DISTINCT PROJECT_ID,MAX(IS_PLAN)AS IS_PLAN,"
							+ " MAX(STORE_LEVEL) AS STORE_LEVEL,MAX(STORE_TIME)  AS STORE_TIME  "
							+ " FROM PROJECT_STORE_STATUS GROUP BY PROJECT_ID) AS PSL ON P.ID = PSL.PROJECT_ID"
							+ " left join department dept  on  P.CREATE_DEPARTMENT_GUID = dept.department_guid "
							+ " where substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  in ('FGW', 'DEPT','CENTRE-COM') "
							+ " and P.DELETE_FLAG ='0' " +warnSQL;
					//未编制三年滚动计划项目
					if(StringUtils.isNotEmpty(filters.get("filterStatus"))
							&&Constant.UNFINISH_PROJECTS.equals(filters.get("filterStatus"))){
						filterSql += " and PSL.IS_PLAN ='0' ";
					}
			}
			//判断地图所选地区是否为空
			if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))||StringUtils.isNotEmpty(filters.get("builPlaceChineseName"))){
				String place=filters.get("builPlaceCode").trim().toString();
				String placeName = filters.get("builPlaceChineseName").trim().toString();
				String placeName_province = placeName.substring(0, placeName.indexOf("-"));
				String placeName_city = placeName.substring(placeName.indexOf("-"), placeName.lastIndexOf("-"));
				String placeName_county = placeName.substring(placeName.indexOf("-"), placeName.length());
			   // 省级
				if(place.endsWith("0000")){
//						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, i)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_province+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
//						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_city+"%' ";
				}
				// 县级
				else{
				//过滤项目建设地点为地图所选地点
//					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				filterSql += " AND dic.item_full_value  like '%"+placeName_county+"%' ";
				}
				
			}
		}
		// 专项类别
		if(StringUtils.isNotEmpty(filters.get("zxlb"))){
			// 项目阶段code
			String zxlb=filters.get("zxlb").trim().toString();
			filterSql += " AND PM.SPECIAL_TYPE = '"+zxlb+"' ";
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
				//过滤项目建设地点为地图所选地点
				filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				}
			}
		}
		//判断是否选中了项目标签
		if(projectVo.getLabelId()!=null && !projectVo.getLabelId().isEmpty()&&!"all".equals(projectVo.getLabelId())){
			filterSql +=" and exists(select * from "
					+ " (SELECT PROJECT_ID FROM TZ_LABEL l,TZ_PROJECT_LABEL pl WHERE l.ID = pl.LABEL_ID "
					+ " and l.CREATE_user_id = '"+userId+"' and l.id='"+projectVo.getLabelId()+"' "
					+ " GROUP BY pl.PROJECT_ID )lb "
					+ " where P.ID = lb.project_id)";
		}
		//项目档案图查询条件拼装sql
		filterSql += this.getFiltersCondition(projectVo);
		//地图查询条件拼装sql
		if(reportParamsMap!=null){
			filterSql += this.getSearchFiltersCondition(reportParamsMap);
		}
		//排序
		filterSql += " ORDER BY p.UPDATE_TIME desc,p.id desc ";
		//获取参数
		resultPage = this.dao.findBySql(pageBo, filterSql,paramList.toArray());
		// 字段code值转换成页面显示value值
		if (resultPage != null ) {
			List<Map<String, Object>> rs = this.convertItemKeyToValue(resultPage.getResult());
			
			resultPage.setResult(rs);
		}
		return resultPage;		
	}
	
	@Override
	public Page<Map<String, Object>> searchWNProject(String userId,Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo
			,Map<String, String> filters,Map<String, String> reportParamsMap) {
		// 定义查询结果返回接收变量
		Page<Map<String, Object>> resultPage = null;
		//定义查询的字段
		String filterSql ="SELECT P.ID,P.STAGE,PM.PRO_NAME ,PM.INVESTMENT_TOTAL,PM.PRO_TYPE,PM.INDUSTRY,PM.BUILD_PLACE,PM.EXPECT_STARTYEAR ";
		List<Object> paramList = new ArrayList<Object>();
		if (!filters.isEmpty()) {
			// 为查询共通SQL拼过滤条件
				// 五年储备
					String warnSQL="";
					if(StringUtils.isNotBlank(filters.get("warnCode"))){
						//select COUNT(1) from v_life_cycle  vlc where vlc.project_stage=8 and vlc.projectflag=0;
						warnSQL=" and  exists(select 1 from  v_warning_analysis  vwa where  vwa.id = p.id and vwa.warning_code= "+filters.get("warnCode")+") ";
					}
					filterSql +=  getCommonSQL("project","id")+"  left join PROJECT_INFO_EXT_MASTER PM on P.ID = PM.PROJECT_ID "
							+ " INNER JOIN(SELECT DISTINCT PROJECT_ID,MAX(IS_PLAN)AS IS_PLAN,"
							+ " MAX(STORE_LEVEL) AS STORE_LEVEL,MAX(STORE_TIME)  AS STORE_TIME  "
							+ " FROM PROJECT_STORE_STATUS GROUP BY PROJECT_ID) AS PSL ON P.ID = PSL.PROJECT_ID"
							+ " left join department dept  on  P.CREATE_DEPARTMENT_GUID = dept.department_guid "
							+ " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' "
							+ " where substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  in ('FGW', 'DEPT','CENTRE-COM') "
							+ " and P.DELETE_FLAG ='0' " +warnSQL;
					//未编制三年滚动计划项目
					if(StringUtils.isNotEmpty(filters.get("filterStatus"))
							&&Constant.UNFINISH_PROJECTS.equals(filters.get("filterStatus"))){
						filterSql += " and PSL.IS_PLAN ='0' ";
					}
			//判断地图所选地区是否为空
			if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))||StringUtils.isNotEmpty(filters.get("builPlaceChineseName"))){
				String place=filters.get("builPlaceCode").trim().toString();
				String placeName = filters.get("builPlaceChineseName").trim().toString();
				String placeName_province = placeName.substring(0, placeName.indexOf("-"));
				String placeName_city = placeName.substring(placeName.indexOf("-"), placeName.lastIndexOf("-"));
				String placeName_county = placeName.substring(placeName.indexOf("-"), placeName.length());
			   // 省级
				if(place.endsWith("0000")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, i)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_province+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_city+"%' ";
				}
				// 县级
				else{
				//过滤项目建设地点为地图所选地点
//				filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				filterSql += " AND dic.item_full_value  like '%"+placeName_county+"%' ";
				}
				
			}
			if(StringUtils.isNotEmpty(filters.get("majorstrategyName"))){
				String majorstrategyName=filters.get("majorstrategyName").trim().toString();
				if("A00001".equals(majorstrategyName)){
					filterSql += " AND PM.MAJOR_STRATEGY like '%"+majorstrategyName+"%' ";
				}else if("A00002".equals(majorstrategyName)||"A00003".equals(majorstrategyName)){
					filterSql += " AND PM.MAJOR_STRATEGY like '"+majorstrategyName+"%' ";
				}else{
					filterSql += " AND ((PM.MAJOR_STRATEGY not like '%A00001%' and PM.MAJOR_STRATEGY not like '%A00002%' and PM.MAJOR_STRATEGY not like '%A00003%') or MAJOR_STRATEGY is null) ";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
				
			}
			// FIXME zhaochao 这里逻辑还未验证，因为图标无法下钻
			// 国标行业
			if(StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				// 项目阶段code
				String governmentCode=filters.get("GovernmentCode").trim().toString();
				filterSql += " AND PM.GB_INDUSTRY = '"+governmentCode+"' ";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 委内行业
			if(StringUtils.isNotEmpty(filters.get("IndustryCode"))){
				// 项目阶段code
				String IndustryCode=filters.get("IndustryCode").trim().toString();
				filterSql += " AND PM.INDUSTRY = '"+IndustryCode+"' ";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 申报部门
			if(StringUtils.isNotEmpty(filters.get("departmentName"))){
				// 项目阶段code
				String departmentName=filters.get("departmentName").trim().toString();
				String departmentname = departmentName.replace("顶层"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("地方发改委"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("中央部门"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("央企"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(departmentname);
				if("地方发改委".equals(departmentName.substring(0, 5))){
					filterSql += " AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'FGW' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}else if("中央部门".equals(departmentName.substring(0, 4))){
					filterSql += "AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'DEPT' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}else{
					filterSql += "AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'CENTRE-COM' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
		}
		// 储备层级
		if(StringUtils.isNotEmpty(filters.get("storeLevel"))){
			// 项目阶段code
			String storeLevel=filters.get("storeLevel").trim().toString();
			switch(storeLevel){
			case "县级": filterSql += " AND PSL.STORE_LEVEL = '1'" ; break;
			case "市级": filterSql += " AND PSL.STORE_LEVEL = '2'" ; break;
			case "省级": filterSql += " AND PSL.STORE_LEVEL = '3'" ; break;
			case "国家": filterSql += " AND PSL.STORE_LEVEL = '4'" ; break;
			}
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				}
			}
		}
		// 储备入库时间（趋势图）
		if(StringUtils.isNotEmpty(filters.get("storeTime"))){
			// 项目阶段code
			String storeTime=filters.get("storeTime").trim().toString();
			filterSql += " AND to_char(PSL.STORE_TIME, 'yyyy-mm') = '"+storeTime+"'" ;
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				}
			}
		}
		// 项目类型
		if(StringUtils.isNotEmpty(filters.get("projectType"))){
			// 项目阶段code
			String projectType=filters.get("projectType").trim().toString();
			if("审批".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
				filterSql += " AND PM.PRO_TYPE like '%A00001%' ";
			}else if("核准".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
				filterSql += " AND PM.PRO_TYPE like '%A00002%' ";
			}else if("备案".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
				filterSql += " AND PM.PRO_TYPE like '%A00003%' ";
			}else{
				filterSql += "AND ((PM.PRO_TYPE not like '%A00001%' and PM.PRO_TYPE not like '%A00002%' and PM.PRO_TYPE not like '%A00003%') or PM.PRO_TYPE is null)";
			}
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				}
			}
		}
		//判断是否选中了项目标签
		if(projectVo.getLabelId()!=null && !projectVo.getLabelId().isEmpty()&&!"all".equals(projectVo.getLabelId())){
			filterSql +=" and exists(select * from "
					+ " (SELECT PROJECT_ID FROM TZ_LABEL l,TZ_PROJECT_LABEL pl WHERE l.ID = pl.LABEL_ID "
					+ " and l.CREATE_user_id = '"+userId+"' and l.id='"+projectVo.getLabelId()+"' "
					+ " GROUP BY pl.PROJECT_ID )lb "
					+ " where P.ID = lb.project_id)";
		}
		//项目档案图查询条件拼装sql
		filterSql += this.getFiltersCondition(projectVo);
		//地图查询条件拼装sql
		if(reportParamsMap!=null){
			filterSql += this.getSearchFiltersCondition(reportParamsMap);
		}
		//排序
		filterSql += " ORDER BY p.UPDATE_TIME desc,p.id desc ";
		//获取参数
		resultPage = this.dao.findBySql(pageBo, filterSql,paramList.toArray());
		// 字段code值转换成页面显示value值
		if (resultPage != null ) {
			List<Map<String, Object>> rs = this.convertItemKeyToValue(resultPage.getResult());
			
			resultPage.setResult(rs);
		}
		return resultPage;		
	}
	
	@Override
	public Page<Map<String, Object>> searchSBProject(String userId,Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo
			,Map<String, String> filters,Map<String, String> reportParamsMap) {
		// 定义查询结果返回接收变量
			Page<Map<String, Object>> resultPage = null;
			//定义查询的字段
			String filterSql ="SELECT P.ID,P.STAGE,PM.PRO_NAME ,PM.INVESTMENT_TOTAL,PM.PRO_TYPE,PM.INDUSTRY,PM.BUILD_PLACE,PM.EXPECT_STARTYEAR ";
			List<Object> paramList = new ArrayList<Object>();
			// 为查询共通SQL拼过滤条件
			//解决委内司局数据对不上	
			if("1".equals(filters.get("falg"))){
				filterSql += getCommonSQL("YEAR_PLAN_PROJECT","")+"  left join YEAR_PLAN_PROJECT_INFO_EXT_MASTER PM on P.id = PM.YEAR_PLAN_PROJECT_ID "
						+ " left join year_plan_project_file_no ypfn on p.year_plan_file_no = ypfn.id "
						+ " left join year_plan_project_info_ext_invest PI on P.id = PI.year_plan_project_id and PI.PROJECT_ITEM_EXT_ID = 'A00016'"
						+ " left join department dept  on  P.CREATE_DEPARTMENT_GUID = dept.department_guid "
						+ " left join department dep1  on  P.intranet_accept_dept_guid = dep1.department_guid "
						+ " WHERE P.DELETE_FLAG ='0' and ypfn.CREATE_TIME >= to_date('2016-07-01', 'yyyy-mm-dd')";
						
						
			}else if("2".equals(filters.get("label"))||"budgetreport".equals(filters.get("moduleCode"))){
				filterSql += getCommonSQL("YEAR_PLAN_PROJECT","")+"left join year_plan_project_info_ext_master pm on p.id = pm.year_plan_project_id"
						+ " left join year_plan_project_info_ext_invest ypiei on p.id = ypiei.year_plan_project_id and ypiei.project_item_ext_id ='A00016' "
						+ "left join year_plan_project_file_no ypfn on p.year_plan_file_no = ypfn.id  where P.DELETE_FLAG ='0' and ypfn.CREATE_TIME >= to_date('2016-07-01', 'yyyy-mm-dd')";
			}
			else {
			filterSql += getCommonSQL("YEAR_PLAN_PROJECT","")+"  left join YEAR_PLAN_PROJECT_INFO_EXT_MASTER PM on P.id = PM.YEAR_PLAN_PROJECT_ID "
					+ " left join year_plan_project_file_no ypfn on p.year_plan_file_no = ypfn.id "
					+ " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' "
					//中央预算内下达资金
					+ " left join year_plan_project_info_ext_invest PI on P.id = PI.year_plan_project_id and PI.PROJECT_ITEM_EXT_ID = 'A00016'"
					+ " left join department dept  on  P.CREATE_DEPARTMENT_GUID = dept.department_guid "
					+ " left join department dep1  on  P.intranet_accept_dept_guid = dep1.department_guid "
					+ " WHERE substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  in ('FGW', 'DEPT','CENTRE-COM') "
					+ " AND P.DELETE_FLAG ='0' and ypfn.CREATE_TIME >= to_date('2016-07-01', 'yyyy-mm-dd') "
					+ " and not exists (select yppb.project_id from  YEAR_PLAN_PROJECT_BUNDLED yppb where p.id = yppb.project_id)";
			}
			//判断地图所选地区是否为空
			if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))||StringUtils.isNotEmpty(filters.get("builPlaceChineseName"))){
				String place=filters.get("builPlaceCode").trim().toString();
				String placeName = filters.get("builPlaceChineseName").trim().toString();
				String placeName_province = placeName.substring(0, placeName.indexOf("-"));
				String placeName_city = placeName.substring(placeName.indexOf("-"), placeName.lastIndexOf("-"));
				String placeName_county = placeName.substring(placeName.indexOf("-"), placeName.length());
			   // 省级
				if(place.endsWith("0000")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, i)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_province+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_city+"%' ";
				}
				// 县级
				else{
				//过滤项目建设地点为地图所选地点
//				filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				filterSql += " AND dic.item_full_value  like '%"+placeName_county+"%' ";
				}
				
			}
			// 政府投资方向
			if(StringUtils.isNotEmpty(filters.get("goverInvestDir"))){
				// 项目阶段code
				String goverInvestDir=filters.get("goverInvestDir").trim().toString();
				if("A00010".equals(filters.get("goverInvestDir"))){
					filterSql += " and PM.GOVERNMENT_INVEST_DIRECTION  is null";
				}else{
					filterSql += " and PM.GOVERNMENT_INVEST_DIRECTION = '"+goverInvestDir+"'";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 国标行业
			if(StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				// 项目阶段code
				String governmentCode=filters.get("GovernmentCode").trim().toString();
				filterSql += " AND PM.GB_INDUSTRY = '"+governmentCode+"' ";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 投资规模
			if(StringUtils.isNotEmpty(filters.get("tzgm"))){
				// 项目阶段code
				String tzgm=filters.get("tzgm").trim().toString();
				switch(tzgm){
				case "500万以下": filterSql += " AND PM.investment_total<500 "; break;
				case "500万（含）~3000万" : filterSql += " AND 500<=PM.investment_total and PM.investment_total<3000 "; break;
				case "3000万（含）~5000万": filterSql += " AND 3000<=PM.investment_total and PM.investment_total<5000 "; break;
				case "5000万（含）~1亿": filterSql += " AND 5000<=PM.investment_total and PM.investment_total<10000 "; break;
				case "1亿（含）~10亿": filterSql += " AND 10000<=PM.investment_total and PM.investment_total<100000 "; break;
				case "10亿（含）~30亿": filterSql += " AND 100000<=PM.investment_total and PM.investment_total<300000 "; break;
				case "30亿（含）以上": filterSql += " AND 300000<=PM.investment_total "; break;
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			if(StringUtils.isNotEmpty(filters.get("majorstrategyName"))){
				String majorstrategyName=filters.get("majorstrategyName").trim().toString();
				if("A00001".equals(majorstrategyName)||"A00002".equals(majorstrategyName)||"A00003".equals(majorstrategyName)){
					filterSql += " AND PM.MAJOR_STRATEGY = '"+majorstrategyName+"' ";
				}else{
					filterSql += " AND ((PM.MAJOR_STRATEGY not like '%A00001%' and PM.MAJOR_STRATEGY not like '%A00002%' and PM.MAJOR_STRATEGY not like '%A00003%') or MAJOR_STRATEGY is null) ";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
				
			}
			if(StringUtils.isNotEmpty(filters.get("wnsj"))){
				String wnsj=filters.get("wnsj").trim().toString();
				filterSql += " AND substr(dep1.department_full_codename, 1, instr(dep1.department_full_codename||'#', '#', 1, 1)-1) = '"+wnsj+"' ";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
				
			}
			// 申报部门
			if(StringUtils.isNotEmpty(filters.get("departmentName"))){
				// 项目阶段code
				String departmentName=filters.get("departmentName").trim().toString();
				String departmentname = departmentName.replace("顶层"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("地方发改委"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("中央部门"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("央企"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(departmentname);
				if("地方发改委".equals(departmentName.substring(0, 5))){
					filterSql += " AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'FGW' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}else if("中央部门".equals(departmentName.substring(0, 4))){
					filterSql += "AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'DEPT' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}else{
					filterSql += "AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'CENTRE-COM' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 项目类型
			if(StringUtils.isNotEmpty(filters.get("projectType"))){
				// 项目阶段code
				String projectType=filters.get("projectType").trim().toString();
				if("审批".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00001%' ";
				}else if("核准".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00002%' ";
				}else if("备案".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00003%' ";
				}else{
					filterSql += "AND ((PM.PRO_TYPE not like '%A00001%' and PM.PRO_TYPE not like '%A00002%' and PM.PRO_TYPE not like '%A00003%') or PM.PRO_TYPE is null)";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
		// 申报时间
		if(StringUtils.isNotEmpty(filters.get("applyTime"))){
			// 项目阶段code
			String applyTime=filters.get("applyTime").trim().toString();
			filterSql += " AND to_char(ypfn.submit_time, 'yyyy-mm') = '"+applyTime+"'" ;
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				}
			}
		}
		//判断是否选中了项目标签
		if(projectVo.getLabelId()!=null && !projectVo.getLabelId().isEmpty()&&!"all".equals(projectVo.getLabelId())){
			filterSql +=" and exists(select * from "
					+ " (SELECT PROJECT_ID FROM TZ_LABEL l,TZ_PROJECT_LABEL pl WHERE l.ID = pl.LABEL_ID "
					+ " and l.CREATE_user_id = '"+userId+"' and l.id='"+projectVo.getLabelId()+"' "
					+ " GROUP BY pl.PROJECT_ID )lb "
					+ " where P.ID = lb.project_id)";
		}
		//项目档案图查询条件拼装sql
		filterSql += this.getFiltersCondition(projectVo);
		//地图查询条件拼装sql
		if(reportParamsMap!=null){
			filterSql += this.getSearchFiltersCondition(reportParamsMap);
		}
		//排序
		filterSql += " ORDER BY p.UPDATE_TIME desc,p.id desc ";
		//获取参数
		resultPage = this.dao.findBySql(pageBo, filterSql,paramList.toArray());
		// 字段code值转换成页面显示value值
		if (resultPage != null ) {
			List<Map<String, Object>> rs = this.convertItemKeyToValue(resultPage.getResult());
			
			resultPage.setResult(rs);
		}
		return resultPage;		
	}
	
	@Override
	public Page<Map<String, Object>> searchXDProject(String userId,Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo
			,Map<String, String> filters,Map<String, String> reportParamsMap) {
			// 定义查询结果返回接收变量
			Page<Map<String, Object>> resultPage = null;
			//定义查询的字段
			String filterSql ="SELECT P.ID,P.STAGE,PM.PRO_NAME ,PM.INVESTMENT_TOTAL,PM.PRO_TYPE,PM.INDUSTRY,PM.BUILD_PLACE,PM.EXPECT_STARTYEAR ";
			List<Object> paramList = new ArrayList<Object>();
			// 为查询共通SQL拼过滤条件
			filterSql += getCommonSQL("YEAR_PLAN_PROJECT","")+"   left join YEAR_PLAN_PROJECT_INFO_EXT_MASTER PM on P.id = PM.YEAR_PLAN_PROJECT_ID "
//					+ " left join PROJECT_DISPATCH_FUNDS_ISSUED PDFI on P.ID=PDFI.SOURCE_ID "
					//中央预算内下达资金
					+ " left join year_plan_project_info_ext_invest PI on P.id = PI.year_plan_project_id and PI.PROJECT_ITEM_EXT_ID = 'A00016'"
					+ " left join department dep1  on  P.intranet_accept_dept_guid = dep1.department_guid "
					+ " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' "
					+ " left join year_plan_project_file_no ypfn on P.year_plan_file_no = ypfn.id "
					+ " WHERE  P.EXPORT_FILE_NO IS NOT NULL AND  P.DELETE_FLAG ='0' "
					+ " and P.ID NOT IN( SELECT  YPB.PROJECT_ID FROM YEAR_PLAN_PROJECT_BUNDLED YPB )";
//					+ " AND P.ID=PDFI.SOURCE_ID AND P.EXPORT_FILE_NO = PDFI.ISSUED_FILE_NO ";
			//判断地图所选地区是否为空
			if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))||StringUtils.isNotEmpty(filters.get("builPlaceChineseName"))){
				String place=filters.get("builPlaceCode").trim().toString();
				String placeName = filters.get("builPlaceChineseName").trim().toString();
				String placeName_province = placeName.substring(0, placeName.indexOf("-"));
				String placeName_city = placeName.substring(placeName.indexOf("-"), placeName.lastIndexOf("-"));
				String placeName_county = placeName.substring(placeName.indexOf("-"), placeName.length());
			   // 省级
				if(place.endsWith("0000")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, i)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_province+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_city+"%' ";
				}
				// 县级
				else{
				//过滤项目建设地点为地图所选地点
//				filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				filterSql += " AND dic.item_full_value  like '%"+placeName_county+"%' ";
				}
				
			}
			// 政府投资方向
			if(StringUtils.isNotEmpty(filters.get("goverInvestDir"))){
				// 项目阶段code
				String goverInvestDir=filters.get("goverInvestDir").trim().toString();
				if("1".equals(filters.get("goverInvestDir"))){
					filterSql += " and PM.GOVERNMENT_INVEST_DIRECTION  is null";
				}else{
					filterSql += " and PM.GOVERNMENT_INVEST_DIRECTION = '"+goverInvestDir+"'";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 国标行业
			if(StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				// 项目阶段code
				String governmentCode=filters.get("GovernmentCode").trim().toString();
				filterSql += " AND PM.GB_INDUSTRY = '"+governmentCode+"' ";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 投资规模
			if(StringUtils.isNotEmpty(filters.get("tzgm"))){
				// 项目阶段code
				String tzgm=filters.get("tzgm").trim().toString();
				switch(tzgm){
				case "500万以下": filterSql += " AND PM.investment_total<500 "; break;
				case "500万（含）~3000万" : filterSql += " AND 500<=PM.investment_total and PM.investment_total<3000 "; break;
				case "3000万（含）~5000万": filterSql += " AND 3000<=PM.investment_total and PM.investment_total<5000 "; break;
				case "5000万（含）~1亿": filterSql += " AND 5000<=PM.investment_total and PM.investment_total<10000 "; break;
				case "1亿（含）~10亿": filterSql += " AND 10000<=PM.investment_total and PM.investment_total<100000 "; break;
				case "10亿（含）~30亿": filterSql += " AND 100000<=PM.investment_total and PM.investment_total<300000 "; break;
				case "30亿（含）以上": filterSql += " AND 300000<=PM.investment_total "; break;
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			if(StringUtils.isNotEmpty(filters.get("majorstrategyName"))){
				String majorstrategyName=filters.get("majorstrategyName").trim().toString();
				if("A00001".equals(majorstrategyName)||"A00002".equals(majorstrategyName)||"A00003".equals(majorstrategyName)){
					filterSql += " AND PM.MAJOR_STRATEGY = '"+majorstrategyName+"' ";
				}else{
					filterSql += " AND ((PM.MAJOR_STRATEGY not like '%A00001%' and PM.MAJOR_STRATEGY not like '%A00002%' and PM.MAJOR_STRATEGY not like '%A00003%') or MAJOR_STRATEGY is null) ";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
				
			}
			if(StringUtils.isNotEmpty(filters.get("wnsj"))){
				String wnsj=filters.get("wnsj").trim().toString();
				filterSql += " AND substr(dep1.department_full_codename, 1, instr(dep1.department_full_codename||'#', '#', 1, 1)-1) = '"+wnsj+"' ";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
				
			}
			// 申报部门
			if(StringUtils.isNotEmpty(filters.get("departmentName"))){
				// 项目阶段code
				String departmentName=filters.get("departmentName").trim().toString();
				String departmentname = departmentName.replace("顶层"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("地方发改委"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("中央部门"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("央企"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(departmentname);
				if("地方发改委".equals(departmentName.substring(0, 5))){
					filterSql += " AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'FGW' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}else if("中央部门".equals(departmentName.substring(0, 4))){
					filterSql += "AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'DEPT' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}else{
					filterSql += "AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'CENTRE-COM' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 项目类型
			if(StringUtils.isNotEmpty(filters.get("projectType"))){
				// 项目阶段code
				String projectType=filters.get("projectType").trim().toString();
				if("审批".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00001%' ";
				}else if("核准".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00002%' ";
				}else if("备案".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00003%' ";
				}else{
					filterSql += "AND ((PM.PRO_TYPE not like '%A00001%' and PM.PRO_TYPE not like '%A00002%' and PM.PRO_TYPE not like '%A00003%') or PM.PRO_TYPE is null)";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
		// 申报时间
		if(StringUtils.isNotEmpty(filters.get("issuedTime"))){
			// 项目阶段code
			String issuedTime=filters.get("issuedTime").trim().toString();
			filterSql += " AND to_char(P.issused_time, 'yyyy-mm') = '"+issuedTime+"'" ;
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				}
			}
		}
		//判断是否选中了项目标签
		if(projectVo.getLabelId()!=null && !projectVo.getLabelId().isEmpty()&&!"all".equals(projectVo.getLabelId())){
			filterSql +=" and exists(select * from "
					+ " (SELECT PROJECT_ID FROM TZ_LABEL l,TZ_PROJECT_LABEL pl WHERE l.ID = pl.LABEL_ID "
					+ " and l.CREATE_user_id = '"+userId+"' and l.id='"+projectVo.getLabelId()+"' "
					+ " GROUP BY pl.PROJECT_ID )lb "
					+ " where P.ID = lb.project_id)";
		}
		//项目档案图查询条件拼装sql
		filterSql += this.getFiltersCondition(projectVo);
		//地图查询条件拼装sql
		if(reportParamsMap!=null){
			filterSql += this.getSearchFiltersCondition(reportParamsMap);
		}
		//排序
		filterSql += " ORDER BY p.UPDATE_TIME desc,p.id desc ";
		//获取参数
		resultPage = this.dao.findBySql(pageBo, filterSql,paramList.toArray());
		// 字段code值转换成页面显示value值
		if (resultPage != null ) {
			List<Map<String, Object>> rs = this.convertItemKeyToValue(resultPage.getResult());
			
			resultPage.setResult(rs);
		}
		return resultPage;
		
	}
	
	@Override
	public Page<Map<String, Object>> searchDDProject(String userId,Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo
			,Map<String, String> filters,Map<String, String> reportParamsMap) {
			// 定义查询结果返回接收变量
			Page<Map<String, Object>> resultPage = null;
			//定义查询的字段
			String filterSql ="SELECT distinct P.ID,P.STAGE,PM.PRO_NAME ,PM.INVESTMENT_TOTAL,PM.PRO_TYPE,PM.INDUSTRY,PM.BUILD_PLACE,PM.EXPECT_STARTYEAR ";
			List<Object> paramList = new ArrayList<Object>();
			// 为查询共通SQL拼过滤条件
			//BUDGETDIAPATCH-中央预算内调度
			if(Constant.BUDGETDIAPATCH.equals(filters.get("moduleCode").trim().toString())){
				filterSql += getCommonSQL("YEAR_PLAN_PROJECT","")+" ";
				filterSql=filterSql.replace("yp","pdi");
				filterSql=filterSql.substring(0,185);
				
						if(StringUtils.isNotEmpty(filters.get("dispatch"))){
							filterSql += "  left join ";
						}else{
							filterSql += "  inner join ";
						}
						filterSql += "  YEAR_PLAN_PROJECT_INFO_EXT_MASTER PM on P.id = PM.YEAR_PLAN_PROJECT_ID ";
						if(StringUtils.isNotEmpty(filters.get("dispatch"))){
							filterSql += "  left join ";
						}else{
							filterSql += "  inner join ";
						}
						filterSql += " PROJECT_DISPATCH_IMPL PDI on P.id = PDI.SOURCE_ID "
				        //中央预算内到位资金
				        + " left join PROJECT_DISPATCH_IMPL_INVEST  PII on P.id = PII.PROJECT_IMPL_ID and PII.PROJECT_ITEM_EXT_ID = 'A00016'"
				        + " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' "
				        + " WHERE  P.DELETE_FLAG ='0' and PDI.REPORT_NUMBER IS NOT NULL  ";
				//模块过滤状态s
				//开工项目
				if(StringUtils.isNotEmpty(filters.get("filterStatus"))
						&&Constant.START_DIAPATCH_PROJECTS.equals(filters.get("filterStatus"))){
					filterSql += " and PDI.ACTUAL_START_TIME IS NOT NULL  and date_format(PDI.ACTUAL_START_TIME,'%Y%m') <= PDI.REPORT_NUMBER ";
				}
				//竣工项目
				if(StringUtils.isNotEmpty(filters.get("filterStatus"))
						&&Constant.END_DIAPATCH_PROJECTS.equals(filters.get("filterStatus"))){
					filterSql += " and PDI.ACTUAL_END_TIME IS NOT NULL  and date_format(PDI.ACTUAL_END_TIME,'%Y%m') <= PDI.REPORT_NUMBER ";
				}
			}
			//判断地图所选地区是否为空
			if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))||StringUtils.isNotEmpty(filters.get("builPlaceChineseName"))){
				String place=filters.get("builPlaceCode").trim().toString();
				String placeName = filters.get("builPlaceChineseName").trim().toString();
				String placeName_province = placeName.substring(0, placeName.indexOf("-"));
				String placeName_city = placeName.substring(placeName.indexOf("-"), placeName.lastIndexOf("-"));
				String placeName_county = placeName.substring(placeName.indexOf("-"), placeName.length());
			   // 省级
				if(place.endsWith("0000")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, i)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_province+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_city+"%' ";
				}
				// 县级
				else{
				//过滤项目建设地点为地图所选地点
//				filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				filterSql += " AND dic.item_full_value  like '%"+placeName_county+"%' ";
				}
				
			}
			String filterDispathSql = "";
			// 调度任务分解情况
			if(StringUtils.isNotEmpty(filters.get("dispatch"))){
				// 项目阶段code
				String dispatch=filters.get("dispatch").trim().toString();
				filterDispathSql += "select P.ID,P.STAGE,PM.PRO_NAME ,PM.INVESTMENT_TOTAL,PM.PRO_TYPE,PM.INDUSTRY,PM.BUILD_PLACE,PM.EXPECT_STARTYEAR ";
				switch(dispatch){
				case "非打捆" : filterDispathSql += " "
						+ " from "
						+ " YEAR_PLAN_PROJECT P "
						+ " inner join  "
						+ " YEAR_PLAN_PROJECT_INFO_EXT_MASTER PM "
						+ " on  "
		        		+ " P.id = PM.YEAR_PLAN_PROJECT_ID "
		        		+ " inner join PROJECT_DISPATCH_IMPL pi "
				        + " on "
				        + " P.id = pi.source_id "
				        + " left join PROJECT_DISPATCH_IMPL_INVEST pii "
				        + " on "
				        + " pi.id = pii.project_impl_id "
				        + " left join YEAR_PLAN_PROJECT_INFO_EXT_INVEST ypie "
				        + " on "
				        + " P.id = ypie.YEAR_PLAN_PROJECT_ID "
				        + " left join YEAR_PLAN_PROJECT_BUNDLED yppb "
				        + " on  "
				        + " P.id = yppb.PROJECT_ID "
						+ " where yppb.ID is not null "
						+ " group by "
						+ " P.ID,P.STAGE,PM.PRO_NAME ,PM.INVESTMENT_TOTAL,PM.PRO_TYPE,PM.INDUSTRY,PM.BUILD_PLACE,PM.EXPECT_STARTYEAR "; break;
						
				case "打捆": filterDispathSql += " "
						+ "from "
						+ " YEAR_PLAN_PROJECT P "
						+ " inner join  "
						+ " YEAR_PLAN_PROJECT_INFO_EXT_MASTER PM "
						+ " on  "
                		+ " P.id = PM.YEAR_PLAN_PROJECT_ID "
                		+ " left join YEAR_PLAN_PROJECT_INFO_EXT_INVEST ypie "
                		+ " on "
                		+ " P.id = ypie.YEAR_PLAN_PROJECT_ID "
		                + "left join (  select  distinct yppb.BUNDLED_PROJECT_ID,'1' as kk from PROJECT_DISPATCH_IMPL pdi "
		                + " inner join YEAR_PLAN_PROJECT_BUNDLED yppb "
		                + "on  "
		                + "   pdi.SOURCE_ID = yppb.PROJECT_ID "
		                + ")table2 "
						+ "on P.id = table2.BUNDLED_PROJECT_ID "
						+ " where   P.IS_BUNDLED=1 and P.STATUS = 'PUTIN' "
						+ " group by "
						+ " P.ID,P.STAGE,PM.PRO_NAME ,PM.INVESTMENT_TOTAL,PM.PRO_TYPE,PM.INDUSTRY,PM.BUILD_PLACE,PM.EXPECT_STARTYEAR "; break;
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 调度项目进展情况占比
			if(StringUtils.isNotEmpty(filters.get("process"))){
				// 项目阶段code
				String process=filters.get("process").trim().toString();
				filterSql += " AND  PDI.IMAGE_PROGRESS = '"+process+"' ";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 国标行业
			if(StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				// 项目阶段code
				String governmentCode=filters.get("GovernmentCode").trim().toString();
				filterSql += " AND PM.GB_INDUSTRY = '"+governmentCode+"' ";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 竣工或拟竣工时间
			if(StringUtils.isNotEmpty(filters.get("endTime"))){
				// 项目阶段code
				String endTime=filters.get("endTime").trim().toString();
				filterSql += "AND if(PDI.actual_end_time is null, SUBSTRING(PM.EXPECT_ENDYEAR, 0, 4), SUBSTRING(PDI.actual_end_time, 0, 4)) "
						+ " = '"+endTime+"'";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			//全国调度项目进展情况
			if(StringUtils.isNotEmpty(filters.get("wholeProcessPlace"))){
				String wholeProcessPlace=filters.get("wholeProcessPlace").trim().toString();
			   // 省级
				if(wholeProcessPlace.endsWith("0000")){
					filterSql += " AND PM.BUILD_PLACE  like '"+wholeProcessPlace.substring(0, 2)+"%' ";
				}
				//  市级
				else if(wholeProcessPlace.endsWith("00")){
					filterSql += " AND PM.BUILD_PLACE  like '"+wholeProcessPlace.substring(0, 4)+"%' ";
				}
				// 县级
				else{
				//过滤项目建设地点为地图所选地点
				filterSql += " AND PM.BUILD_PLACE = '"+wholeProcessPlace+"' ";
				}
				
			}
			if(StringUtils.isNotEmpty(filters.get("majorstrategyName"))){
				String majorstrategyName=filters.get("majorstrategyName").trim().toString();
				if("A00001".equals(majorstrategyName)||"A00002".equals(majorstrategyName)||"A00003".equals(majorstrategyName)){
					filterSql += " AND PM.MAJOR_STRATEGY = '"+majorstrategyName+"' ";
				}else{
					filterSql += " AND ((PM.MAJOR_STRATEGY not like '%A00001%' and PM.MAJOR_STRATEGY not like '%A00002%' and PM.MAJOR_STRATEGY not like '%A00003%') or MAJOR_STRATEGY is null) ";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
				
			}
			if(StringUtils.isNotEmpty(filters.get("wnsj"))){
				String wnsj=filters.get("wnsj").trim().toString();
				filterSql += " AND substr(dep1.department_full_codename, 1, instr(dep1.department_full_codename||'#', '#', 1, 1)-1) = '"+wnsj+"' ";
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
				
			}
			// 申报部门
			if(StringUtils.isNotEmpty(filters.get("departmentName"))){
				// 项目阶段code
				String departmentName=filters.get("departmentName").trim().toString();
				String departmentname = departmentName.replace("顶层"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("地方发改委"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("中央部门"+Constant.BI_SPLIT,"");
				departmentname = departmentname.replace("央企"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(departmentname);
				if("地方发改委".equals(departmentName.substring(0, 5))){
					filterSql += " AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'FGW' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}else if("中央部门".equals(departmentName.substring(0, 4))){
					filterSql += "AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'DEPT' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}else{
					filterSql += "AND substr(dept.department_full_type, 1, instr(dept.department_full_type ||'#', '#', 1, 1)-1)  = 'CENTRE-COM' ";
					filterSql += " AND substr(dept.department_full_codename, 1, instr(dept.department_full_codename ||'#', '#', 1, 1)-1) = '"+departmentname+"' ";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
			// 项目类型
			if(StringUtils.isNotEmpty(filters.get("projectType"))){
				// 项目阶段code
				String projectType=filters.get("projectType").trim().toString();
				if("审批".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00001%' ";
				}else if("核准".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00002%' ";
				}else if("备案".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00003%' ";
				}else{
					filterSql += "AND ((PM.PRO_TYPE not like '%A00001%' and PM.PRO_TYPE not like '%A00002%' and PM.PRO_TYPE not like '%A00003%') or PM.PRO_TYPE is null)";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
					String place=filters.get("builPlaceCodeMap").trim().toString();
					// 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				}
			}
		// 申报时间
		if(StringUtils.isNotEmpty(filters.get("issuedTime"))){
			// 项目阶段code
			String issuedTime=filters.get("issuedTime").trim().toString();
			filterSql += " AND to_char(P.issused_time, 'yyyy-mm') = '"+issuedTime+"'" ;
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				}
			}
		}
		//判断是否选中了项目标签
		if(projectVo.getLabelId()!=null && !projectVo.getLabelId().isEmpty()&&!"all".equals(projectVo.getLabelId())){
			filterSql +=" and exists(select * from "
					+ " (SELECT PROJECT_ID FROM TZ_LABEL l,TZ_PROJECT_LABEL pl WHERE l.ID = pl.LABEL_ID "
					+ " and l.CREATE_user_id = '"+userId+"' and l.id='"+projectVo.getLabelId()+"' "
					+ " GROUP BY pl.PROJECT_ID )lb "
					+ " where P.ID = lb.project_id)";
		}
		//项目档案图查询条件拼装sql
		filterSql += this.getFiltersCondition(projectVo);
		//地图查询条件拼装sql
		if(reportParamsMap!=null){
			filterSql += this.getSearchFiltersCondition(reportParamsMap);
		}
		//排序
		filterSql += " ORDER BY p.UPDATE_TIME desc,p.id desc ";
		//获取参数
//		if(StringUtils.isNotEmpty(filterDispathSql)){
//			resultPage = this.dao.findBySql(pageBo, filterDispathSql,paramList.toArray());
//		}else{
			resultPage = this.dao.findBySql(pageBo, filterSql,paramList.toArray());
//		}
		
		// 字段code值转换成页面显示value值
		if (resultPage != null ) {
			List<Map<String, Object>> rs = this.convertItemKeyToValue(resultPage.getResult());
			
			resultPage.setResult(rs);
		}
		return resultPage;
		
	}
	
   /* *
	 * @orderBy
     * @param
	 *    tableName --连接的表名
	 *    connectId -- 连接表的ID
     * @return
     * @author tannc
     * @Date 2016/12/14 21:23
	**/
	private String getCommonSQL(String tableName,String connectId) {
		//	String  commonSql= " , yp.lv,yp.months,yp.mon_lv,yp.mon_rate " +
//		String  commonSql= " , yp.lv,yp.mon_lv,yp.mon_rate " +
//				" FROM %s P" + // 连接的表
//				"  left join  v_early_warning yp  on p.%s=yp.project_id " ;   // 连接的关系
		String  commonSql= " ,  isnull(yp.IMAGE_URL) as isimg " +
				" FROM %s P" + // 连接的表
				"  left join  project_dispatch_impl   yp  on p.%s=yp.project_id " ;   // 连接的关系

		//  年度计划项目
		if("YEAR_PLAN_PROJECT".equalsIgnoreCase(tableName)){
			//	commonSql= " ,pbb.lv,pbb.months,pbb.mon_lv,pbb.mon_rate " +
			commonSql= " , isnull(yp.IMAGE_URL) as isimg  " +
					" from year_plan_project p   " +   // 连接的关系
					"  left join  project_dispatch_impl   yp  on p.project_id=yp.project_id ";
		}
		return String.format(commonSql,tableName, connectId);
	}
	/**
	 * 查询条件拼装sql
	 * @orderBy 
	 * @param projectVo
	 * @return
	 * @author tanghw
	 * @Date 2016年11月17日上午11:10:32
	 */
	private String getFiltersCondition(ProjectFileSearchVo projectVo){
		//拼装条件
		String sql  = "";
		//拟开工年范围区间第一个值
		if(projectVo.getPlanStartYear1()!=null && !projectVo.getPlanStartYear1().isEmpty()){
			sql +=" and date_format(PM.EXPECT_STARTYEAR,'%Y%m') >="+projectVo.getPlanStartYear1()+" ";
		}
		//拟开工月范围区间第一个值
		if(projectVo.getPlanStartMonth1()!=null && !projectVo.getPlanStartMonth1().isEmpty()){
			sql +=" and (date_format(PM.EXPECT_STARTYEAR,'%Y%m') >="+projectVo.getPlanStartYear1()+" OR PM.EXPECT_NY >="+projectVo.getPlanStartMonth1()+")";//判断月时，同时加上判断年（满足大于年即可不判断月）
		}
		//拟开工年范围区间第二个值
		if(projectVo.getPlanStartYear2()!=null && !projectVo.getPlanStartYear2().isEmpty()){
			sql +=" and date_format(PM.EXPECT_STARTYEAR,'%Y%m') <="+projectVo.getPlanStartYear2()+" ";
		}
		//拟开工月范围区间第二个值
		if(projectVo.getPlanStartMonth2()!=null && !projectVo.getPlanStartMonth2().isEmpty()){
			sql +=" and (date_format(PM.EXPECT_STARTYEAR,'%Y%m') <="+projectVo.getPlanStartYear2()+" OR PM.EXPECT_NY <="+projectVo.getPlanStartMonth2()+" )";//判断月时，同时加上判断年（满足小于年即可不判断月）
		}
		//拟竣工年范围区间第一个值
		if(projectVo.getPlanEndYear1()!=null && !projectVo.getPlanEndYear1().isEmpty()){
			sql +=" and date_format(PM.EXPECT_ENDYEAR,'%Y%m') >="+projectVo.getPlanEndYear1()+" ";
		}
		//拟竣工年范围区间第二个值
		if(projectVo.getPlanEndYear2()!=null && !projectVo.getPlanEndYear2().isEmpty()){
			sql +=" and date_format(PM.EXPECT_ENDYEAR,'%Y%m') <="+projectVo.getPlanEndYear2()+" ";
		}
		//所属行业
		if(null!=projectVo.getIndustryCode() && !projectVo.getIndustryCode().isEmpty()){
			sql +=" and PM.INDUSTRY in ('"+projectVo.getIndustryCode().replace(",", "','")+"') ";
		}
		//建设地点
		if(null!=projectVo.getProjectRegion() && !projectVo.getProjectRegion().isEmpty()){
			sql +=" and PM.BUILD_PLACE in ('"+projectVo.getProjectRegion().replace(",", "','")+"') ";
		}
		//政府投资方向
		if(null!=projectVo.getFitIndPolicyCode() && !projectVo.getFitIndPolicyCode().isEmpty()){
			sql +=" and PM.GOVERNMENT_INVEST_DIRECTION in ('"+projectVo.getFitIndPolicyCode().replace(",", "','")+"') ";
		}
		//项目名称
		if(null!=projectVo.getProjectName() && !projectVo.getProjectName().isEmpty()){
			sql +=" and PM.PRO_NAME like '%"+projectVo.getProjectName()+"%' ";
		}
		//是否ppp
		if(null!=projectVo.getIsPpp() && !projectVo.getIsPpp().isEmpty()){
			if(Constant.ISPPP_A00002.equals(projectVo.getIsPpp().trim())){
				sql +=" and (PM.ISPPP ='"+projectVo.getIsPpp()+"' or PM.ISPPP is null)";
			}else{
				sql +=" and PM.ISPPP ='"+projectVo.getIsPpp()+"' ";
			}
		}
		//是否是否专项建设
		if(null!=projectVo.getIsSpecFunds() && !projectVo.getIsSpecFunds().isEmpty()){
			sql +=" and PM.ISBOND ='"+projectVo.getIsSpecFunds()+"' ";
			if(Constant.ISPPP_A00002.equals(projectVo.getIsSpecFunds().trim())){
				sql +=" and (PM.ISBOND ='"+projectVo.getIsSpecFunds()+"' or PM.ISBOND is null)";
			}else{
				sql +=" and PM.ISBOND ='"+projectVo.getIsSpecFunds()+"' ";
			}
		}

		//项目类型
		if(null!=projectVo.getCheckLevel() && !projectVo.getCheckLevel().isEmpty()){
			sql +=" and PM.PRO_TYPE in ('"+projectVo.getCheckLevel().replace(",", "','")+"') ";
		}
		//项目申报日期区间第一个值
		if(projectVo.getProjectApplyTime1()!=null && !projectVo.getProjectApplyTime1().isEmpty()){
			sql +=" and P.CREATE_TIME >='"+projectVo.getProjectApplyTime1()+"' ";
		}
		//项目申报日期区间第二个值
		if(projectVo.getProjectApplyTime2()!=null && !projectVo.getProjectApplyTime2().isEmpty()){
			sql +=" and p.CREATE_TIME <='"+projectVo.getProjectApplyTime2()+"' ";
		}
		//总投资区间第一个值
		if(projectVo.getAllCaptial1()!=null && !projectVo.getAllCaptial1().isEmpty()){
			sql +=" and PM.INVESTMENT_TOTAL >='"+projectVo.getAllCaptial1()+"' ";
		}
		//总投资区间第二个值
		if(projectVo.getAllCaptial2()!=null && !projectVo.getAllCaptial2().isEmpty()){
			sql +=" and pM.INVESTMENT_TOTAL <='"+projectVo.getAllCaptial2()+"' ";
		}
		return sql;
	}
	/**
	 * bi查询条件拼装
	 * @orderBy 
	 * @param projectVo
	 * @return
	 * @author tanghw
	 * @Date 2017年3月2日下午3:37:55
	 */
	private String getSearchFiltersCondition(Map<String, String> reportParamsMap){
		//拼装条件
		String sql  = "";
		//入库日期区间第一个值
		if(reportParamsMap.get("storeTimeFrom")!=null && !reportParamsMap.get("storeTimeFrom").isEmpty()){
			sql +=" and date_format(P.STORE_TIME,'%Y%m') >='"+reportParamsMap.get("storeTimeFrom")+"'";
		}
		//入库日期区间第二个值
		if(reportParamsMap.get("storeTimeTo")!=null && !reportParamsMap.get("storeTimeTo").isEmpty()){
			sql +=" and date_format(P.STORE_TIME,'%Y%m') <='"+reportParamsMap.get("storeTimeTo")+"'";
		}
		//拟开工年范围区间第一个值
		if(reportParamsMap.get("startTimeFrom")!=null && !reportParamsMap.get("startTimeFrom").isEmpty()){
			sql +=" and date_format(PM.EXPECT_STARTYEAR,'%Y%m') >='"+reportParamsMap.get("startTimeFrom")+"'";
		}
		//拟开工年范围区间第二个值
		if(reportParamsMap.get("starTimeTo")!=null && !reportParamsMap.get("starTimeTo").isEmpty()){
			sql +=" and date_format(PM.EXPECT_STARTYEAR,'%Y%m') <='"+reportParamsMap.get("starTimeTo")+"'";
		}
		//拟竣工年范围区间第一个值
		if(reportParamsMap.get("endTimeFrom")!=null && !reportParamsMap.get("endTimeFrom").isEmpty()){
			sql +=" and date_format(PM.EXPECT_ENDYEAR,'%Y%m') >='"+reportParamsMap.get("endTimeFrom")+"'";
		}
		//拟竣工年范围区间第二个值
		if(reportParamsMap.get("endTimeTo")!=null && !reportParamsMap.get("endTimeTo").isEmpty()){
			sql +=" and date_format(PM.EXPECT_ENDYEAR,'%Y%m') <='"+reportParamsMap.get("endTimeTo")+"'";
		}
		//所属行业
		if(null!=reportParamsMap.get("industry") && !reportParamsMap.get("industry").isEmpty()){
			sql +=" and PM.INDUSTRY in ('"+reportParamsMap.get("industry").replace(", ", "','")+"') ";
		}
		//建设地点
		if(null!=reportParamsMap.get("buildPlace") && !reportParamsMap.get("buildPlace").isEmpty()){
			sql +=" and PM.BUILD_PLACE in ('"+reportParamsMap.get("buildPlace").replace(", ", "','")+"') ";
		}
		//政府投资方向
		if(null!=reportParamsMap.get("govInvest") && !reportParamsMap.get("govInvest").isEmpty()){
			sql +=" and PM.GOVERNMENT_INVEST_DIRECTION in ('"+reportParamsMap.get("govInvest").replace(", ", "','")+"') ";
		}
		//统计局的行业标准
		if(null!=reportParamsMap.get("gbIndustry") && !reportParamsMap.get("gbIndustry").isEmpty()){
			sql +=" and PM.GB_INDUSTRY in ('"+reportParamsMap.get("gbIndustry").replace(", ", "','")+"') ";
		}
		//是否ppp
		if(null!=reportParamsMap.get("isPPP") && !reportParamsMap.get("isPPP").isEmpty()){
			if(Constant.ISPPP_A00002.equals(reportParamsMap.get("isPPP").trim())){
				sql +=" and (PM.ISPPP ='"+reportParamsMap.get("isPPP")+"' or PM.ISPPP is null)";
			}else{
				sql +=" and PM.ISPPP ='"+reportParamsMap.get("isPPP")+"' ";
			}
		}
		//是否是否专项建设
		if(null!=reportParamsMap.get("isFunds") && !reportParamsMap.get("isFunds").isEmpty()){
			if(Constant.ISFUNDS_A00002.equals(reportParamsMap.get("isFunds").trim())){
				sql +=" and (PM.ISBOND ='"+reportParamsMap.get("isFunds")+"' or PM.ISBOND is null)";
			}else{
				sql +=" and PM.ISBOND ='"+reportParamsMap.get("isFunds")+"' ";
			}
		}
		//项目申报日期区间第一个值
		if(reportParamsMap.get("createTimeFrom")!=null && !reportParamsMap.get("createTimeFrom").isEmpty()){
			sql +=" and P.CREATE_TIME >='"+reportParamsMap.get("createTimeFrom")+"' ";
		}
		//项目申报日期区间第二个值
		if(reportParamsMap.get("createTimeTo")!=null && !reportParamsMap.get("createTimeTo").isEmpty()){
			sql +=" and p.CREATE_TIME <='"+reportParamsMap.get("createTimeTo")+"' ";
		}
		//总投资区间第一个值
		if(reportParamsMap.get("invTotalFrom")!=null && !reportParamsMap.get("invTotalFrom").isEmpty()){
			sql +=" and PM.INVESTMENT_TOTAL >='"+reportParamsMap.get("invTotalFrom")+"' ";
		}
		//总投资区间第二个值
		if(reportParamsMap.get("invTotalTo")!=null && !reportParamsMap.get("invTotalTo").isEmpty()){
			sql +=" and pM.INVESTMENT_TOTAL <='"+reportParamsMap.get("invTotalTo")+"' ";
		}
		//专项建设基金投放资金
		if(reportParamsMap.get("putinCaptialFrom")!=null && !reportParamsMap.get("putinCaptialFrom").isEmpty()){
			sql +=" and PI.PUTIN_CAPTIAL >='"+reportParamsMap.get("putinCaptialFrom")+"' ";
		}
		//专项建设基金投放资金
		if(reportParamsMap.get("putinCaptialTo")!=null && !reportParamsMap.get("putinCaptialTo").isEmpty()){
			sql +=" and PI.PUTIN_CAPTIAL <='"+reportParamsMap.get("putinCaptialTo")+"' ";
		}
		//中央预算内到位资金
		if(reportParamsMap.get("finishTotalFrom")!=null && !reportParamsMap.get("finishTotalFrom").isEmpty()){
			sql +=" and PII.CUR_MONEY >='"+reportParamsMap.get("finishTotalFrom")+"' ";
		}
		//中央预算内到位资金
		if(reportParamsMap.get("finishTotalTo")!=null && !reportParamsMap.get("finishTotalTo").isEmpty()){
			sql +=" and PII.CUR_MONEY <='"+reportParamsMap.get("finishTotalTo")+"' ";
		}
		//中央预算内下达资金
		if(reportParamsMap.get("issuedTotalFrom")!=null && !reportParamsMap.get("issuedTotalFrom").isEmpty()){
			sql +=" and PI.cur_allocated >='"+reportParamsMap.get("issuedTotalFrom")+"' ";
		}
		//中央预算内下达资金
		if(reportParamsMap.get("issuedTotalTo")!=null && !reportParamsMap.get("issuedTotalTo").isEmpty()){
			sql +=" and PI.cur_allocated <='"+reportParamsMap.get("issuedTotalTo")+"' ";
		}
		//中央预算内(2017)申请资金
		if(reportParamsMap.get("budTotalFrom")!=null && !reportParamsMap.get("budTotalFrom").isEmpty()){
			sql +=" and PI.apply_captial_2017 >='"+reportParamsMap.get("budTotalFrom")+"' ";
		}
		//中央预算内(2017)申请资金
		if(reportParamsMap.get("budTotalTo")!=null && !reportParamsMap.get("budTotalTo").isEmpty()){
			sql +=" and PI.apply_captial_2017 <='"+reportParamsMap.get("budTotalTo")+"' ";
		}
		return sql;
	}
	/**
	 * 根据条件查询项目
	 * @orderBy 
	 * @param pageBo
	 * @param projectVo 页面查询条件
	 * @param     地图参数集合
	 * @return
	 * @author tanghw
	 * @Date 2016年10月31日下午3:51:56
	 */
	@Override
	public Page<Map<String, Object>> searchFileProject(Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo,
			Map<String, String> filters) {
			// 定义查询结果返回接收变量
			Page<Map<String, Object>> resultPage = null;
			String sql = String.format(ProjectFileSqlConstants.SEARCH_PROJECT_SQL);
			List<Object> paramList = new ArrayList<Object>();
			//拼装条件
			sql += " where P.ID = PIEM.PROJECT_ID  and exists (SELECT pss.PROJECT_ID FROM PROJECT_STORE_STATUS pss where P.ID = pss.project_id)";
			//拟开工年范围区间第一个值
			if(projectVo.getPlanStartYear1()!=null && !projectVo.getPlanStartYear1().isEmpty()){
				sql +=" and date_format(PIEM.EXPECT_STARTYEAR,'%Y%m') >=? ";
				paramList.add(projectVo.getPlanStartYear1());
			}
			//拟开工月范围区间第一个值
			if(projectVo.getPlanStartMonth1()!=null && !projectVo.getPlanStartMonth1().isEmpty()){
				sql +=" and (date_format(PIEM.EXPECT_STARTYEAR,'%Y%m') >=? OR PIEM.EXPECT_NY >=?)";//判断月时，同时加上判断年（满足大于年即可不判断月）
				paramList.add(projectVo.getPlanStartYear1());
				paramList.add(projectVo.getPlanStartMonth1());
			}
			//拟开工年范围区间第二个值
			if(projectVo.getPlanStartYear2()!=null && !projectVo.getPlanStartYear2().isEmpty()){
				sql +=" and date_format(PIEM.EXPECT_STARTYEAR,'%Y%m') <=? ";
				paramList.add(projectVo.getPlanStartYear2());
			}
			//拟开工月范围区间第二个值
			if(projectVo.getPlanStartMonth2()!=null && !projectVo.getPlanStartMonth2().isEmpty()){
				sql +=" and (date_format(PIEM.EXPECT_STARTYEAR,'%Y%m') <=? OR PIEM.EXPECT_NY <=? )";//判断月时，同时加上判断年（满足小于年即可不判断月）
				paramList.add(projectVo.getPlanStartYear2());
				paramList.add(projectVo.getPlanStartMonth2());
			}
			//拟竣工年范围区间第一个值
			if(projectVo.getPlanEndYear1()!=null && !projectVo.getPlanEndYear1().isEmpty()){
				sql +=" and date_format(PIEM.EXPECT_ENDYEAR,'%Y%m') >=? ";
				paramList.add(projectVo.getPlanEndYear1());
			}
			//拟竣工年范围区间第二个值
			if(projectVo.getPlanEndYear2()!=null && !projectVo.getPlanEndYear2().isEmpty()){
				sql +=" and date_format(PIEM.EXPECT_ENDYEAR,'%Y%m') <=? ";
				paramList.add(projectVo.getPlanEndYear2());
			}
			//所属行业
			if(null!=projectVo.getIndustryCode() && !projectVo.getIndustryCode().isEmpty()){
				sql +=" and PIEM.INDUSTRY in ('"+projectVo.getIndustryCode().replace(",", "','")+"') ";
			}
			//建设地点
			if(null!=projectVo.getProjectRegion() && !projectVo.getProjectRegion().isEmpty()){
				sql +=" and PIEM.BUILD_PLACE in ('"+projectVo.getProjectRegion().replace(",", "','")+"') ";
			}
			//政府投资方向
			if(null!=projectVo.getFitIndPolicyCode() && !projectVo.getFitIndPolicyCode().isEmpty()){
				sql +=" and PIEM.GOVERNMENT_INVEST_DIRECTION in ('"+projectVo.getFitIndPolicyCode().replace(",", "','")+"') ";
			}
			//项目名称
			if(null!=projectVo.getProjectName() && !projectVo.getProjectName().isEmpty()){
				sql +=" and PIEM.PRO_NAME like ? ";
				paramList.add("%"+projectVo.getProjectName()+"%");
			}
			//是否ppp
			if(null!=projectVo.getIsPpp() && !projectVo.getIsPpp().isEmpty()){
				sql +=" and PIEM.ISPPP =? ";
				paramList.add(Integer.parseInt(projectVo.getIsPpp()));
			}
			//图查询条件拼装sql
			sql += this.getFiltersCondition(filters);
			//排序
			sql += " ORDER BY p.UPDATE_TIME desc ";
			//获取参数
			resultPage = this.dao.findBySql(pageBo, sql,paramList.toArray());
			// 字段code值转换成页面显示value值
			if (resultPage != null ) {
				List<Map<String, Object>> rs = this.convertItemKeyToValue(resultPage.getResult());
				
				resultPage.setResult(rs);
			}
			return resultPage;
	}
	/**地图查询条件拼装sql
	 * @orderBy 
	 * @param
	 * @return
	 * @author tanghw
	 * @Date 2016年10月31日下午5:29:11
	 */
	private String getFiltersCondition(Map<String, String> filters){
		if (!filters.isEmpty()) {
			// 为查询共通SQL拼过滤条件
			String  filterSql = "  ";
			//判断端是否是从地图跳转过来的
			if(StringUtils.isNotEmpty(filters.get("innerPage"))&&"1".equals(filters.get("innerPage").trim().toString())){
				// 模块名称
				if (StringUtils.isNotEmpty(filters.get("moduleCode"))) {
					//模块不为空时查询的储备项目id存在在查询结果id中
					filterSql += " and  exists( ";
					//模块名称有值查询当前模块复合条件的projectId
					//ROLL_PLAN-三年滚动计划
					if(Constant.ROLL_PLAN.equals(filters.get("moduleCode").trim().toString())){
						//模块过滤状态
						//报送到国家
						if(StringUtils.isNotEmpty(filters.get("filterStatus"))
								&&Constant.REPORT_UNIT.equals(filters.get("filterStatus"))){
							//报送到国家即判断滚动计划项目的审核状态表的审核部门等级为4
							filterSql +=" select PP.PROJECT_ID from PLAN_PROJECT PP,PLAN_PROJECT_INFO_EXT_MASTER ppiem ,PLAN_PROJECT_CHECK_STATUS PCS,DEPARTMENT D "
									+ " WHERE PCS.DEPARTMENT_GUID = D.DEPARTMENT_GUID AND PCS.ROLL_PLAN_PROJECT_ID = PP.ID and pp.id = ppiem.plan_project_id"
									+ " and  D.STORE_LEVEL= '4' AND PP.DELETE_FLAG ='0' and P.ID = PP.project_id  ";
						//全部
						}else{
							filterSql += " select PP.PROJECT_ID from PLAN_PROJECT PP ,PLAN_PROJECT_INFO_EXT_MASTER ppiem where  pp.id = ppiem.plan_project_id and P.ID = PP.project_id ";
						}
					//FUNDS-专项建设基金
					}else if(Constant.FUNDS.equals(filters.get("moduleCode").trim().toString())){
						filterSql += " select BPP.PROJECT_ID from BOND_PLAN_PROJECT BPP,BOND_PLAN_PROJECT_INFO_EXT_MASTER ppiem "
								+ " WHERE  BPP.DELETE_FLAG ='0' and ppiem.BOND_PLAN_PROJECT_ID = BPP.ID  and P.ID = BPP.project_id  ";
					//B-中央预算内申报	
					}else if(Constant.BUDGETREPORT.equals(filters.get("moduleCode").trim().toString())){
						filterSql += " select YPP.PROJECT_ID from YEAR_PLAN_PROJECT YPP left join YEAR_PLAN_PROJECT_INFO_EXT_MASTER ppiem on YPP.id = ppiem.YEAR_PLAN_PROJECT_ID "
								+ " left join year_plan_project_file_no ypfn on ypp.year_plan_file_no = ypfn.id WHERE  YPP.DELETE_FLAG ='0' "
								+ " and ypfn.CREATE_TIME >= to_date('2016-07-01', 'yyyy-mm-dd') and P.ID = YPP.project_id  "
								+ " and not exists (select yppb.project_id from  YEAR_PLAN_PROJECT_BUNDLED yppb where ypp.id = yppb.project_id)";
					//B-中央预算内下达
					}else if(Constant.BUDGETISSUE.equals(filters.get("moduleCode").trim().toString())){
						filterSql += " select YPP.PROJECT_ID from YEAR_PLAN_PROJECT YPP ,YEAR_PLAN_PROJECT_INFO_EXT_MASTER ppiem,PROJECT_DISPATCH_FUNDS_ISSUED PDFI"
								+ " WHERE ppiem.YEAR_PLAN_PROJECT_ID = YPP.ID AND  YPP.EXPORT_FILE_NO IS NOT NULL AND  YPP.DELETE_FLAG ='0' "
								+ " AND YPP.ID=PDFI.SOURCE_ID AND YPP.EXPORT_FILE_NO = PDFI.ISSUED_FILE_NO "
								+ " and P.ID = YPP.project_id ";
					//BUDGETDIAPATCH-中央预算内调度
					}else if(Constant.BUDGETDIAPATCH.equals(filters.get("moduleCode").trim().toString())){
						filterSql += " SELECT PDI.PROJECT_ID FROM PROJECT_DISPATCH_IMPL PDI left join YEAR_PLAN_PROJECT_INFO_EXT_MASTER ppiem  "
								+ " on ppiem.YEAR_PLAN_PROJECT_ID = PDI.SOURCE_ID WHERE  P.ID = pdi.project_id  ";
						//模块过滤状态s
						//开工项目
						if(StringUtils.isNotEmpty(filters.get("filterStatus"))
								&&Constant.START_DIAPATCH_PROJECTS.equals(filters.get("filterStatus"))){
							filterSql += " and PDI.ACTUAL_START_TIME IS NOT NULL  and date_format(PDI.ACTUAL_START_TIME,'%Y%m') <= PDI.REPORT_NUMBER ";
						}
						//竣工项目
						if(StringUtils.isNotEmpty(filters.get("filterStatus"))
								&&Constant.END_DIAPATCH_PROJECTS.equals(filters.get("filterStatus"))){
							filterSql += " and PDI.ACTUAL_END_TIME IS NOT NULL  and date_format(PDI.ACTUAL_END_TIME,'%Y%m') <= PDI.REPORT_NUMBER ";
						}
					}
					//项目主要详细查询的是储备的表所以不同模块的主要详细表别名都取ppiem好过滤地图所选地区
					//判断地图所选地区是否为空
					if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))){
						String place=filters.get("builPlaceCode").trim().toString();
					   // 省级
						if(place.endsWith("0000")){
							filterSql += " AND ppiem.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
						}
						//  市级
						else if(place.endsWith("00")){
							filterSql += " AND ppiem.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
						}
						// 县级
						else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND ppiem.BUILD_PLACE = '"+place+"' ";
						}
					}
					//补充括号
					filterSql += " )";
					//因为本身就是查询的储备项目所有模块没五年储备的最后做判断
					//FIVE_PLAN-五年储备
					if(Constant.FIVE_PLAN.equals(filters.get("moduleCode").trim().toString())){
						//模块过滤状态
						//未编制三年滚动计划项目
						if(StringUtils.isNotEmpty(filters.get("filterStatus"))
								&&Constant.UNFINISH_PROJECTS.equals(filters.get("filterStatus"))){
							filterSql = " and P.IS_PLAN ='0' ";
						//没有额外条件下清空查询条件
						}else{
							filterSql="";
						}
						//判断地图所选地区是否为空
						if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))){
							String place=filters.get("builPlaceCode").trim().toString();
						   // 省级
							if(place.endsWith("0000")){
								filterSql += " AND PIEM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
							}
							//  市级
							else if(place.endsWith("00")){
								filterSql += " AND PIEM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
							}
							// 县级
							else{
							//过滤项目建设地点为地图所选地点
							filterSql += " AND PIEM.BUILD_PLACE = '"+place+"' ";
							}
						}
					}
					
				}
//				//判断地图所选地区是否为空
//				if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))){
////					//地图所传地区全路径名称去掉国家
////					String builPlaceName = filters.get("builPlaceCode").trim().toString().replace("中国"+Constant.BI_SPLIT,"");
////					// 根据全路径名称获取地区code
////					String code=Cache.getCodeByFullName(builPlaceName);
//					String place=filters.get("builPlaceCode").trim().toString();
//				   // 省级
//					if(place.endsWith("0000")){
//						filterSql += " AND PIEM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
//					}
//					//  市级
//					else if(place.endsWith("00")){
//						filterSql += " AND PIEM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
//					}
//					// 县级
//					else{
//					//过滤项目建设地点为地图所选地点
//					filterSql += " AND PIEM.BUILD_PLACE = '"+place+"' ";
//					}
//				}
			}
			
			return filterSql;
		}else{
			return "";
		}
	}
	/**
	 * 获取最新前三个调度项目实施信息
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年11月2日下午1:35:06
	 */
	@Override
	public List<Map<String, Object>> getTopThirdDispatchDetails() {
		String sql = String.format(ProjectFileSqlConstants.THIRD_DISPATCH_SQL);
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		//参数转码
		list = this.convertItemKeyToValue(list);
		return list;
	}
	
	 /**
	 * @orderBy 
	 * @param projectGuid
	 * @param moduleCode
	 * @author tanghw
	 * @Date 2016年11月30日下午8:23:36
	 */
	/**
	 * @orderBy 
	 * @param projectGuid
	 * @param moduleCode
	 * @param fileName fileName下载word的路径文件名称
	 * @author tanghw
	 * @Date 2016年12月5日上午9:52:39
	 */
	public void exportWord(String projectGuid,String moduleCode,String filePath,String fileName){
		 //下载准备
		 HttpServletResponse response = ServletActionContext.getResponse();
		 HttpServletRequest request = ServletActionContext.getRequest();
		 //获取参数
		 //基本信息
		 List<Map<String, Object>> baseInfoList=this.getBaseInfoByGuid(projectGuid, moduleCode);
		 //根据项目id获取项目资金构成信息
		 List<Map<String, Object>> investmentConstituteInfo=this.getInvestConstituteInfoByGuid(projectGuid, moduleCode);
		 //项目下达情况
		 List<Map<String, Object>> issuedInfo=this.getIssuedInfoByGuid(projectGuid, moduleCode);
		 //项目批复情况
		 List<Map<String, Object>> preworkInfo=this.getPreworkInfoByGuid(projectGuid, moduleCode);
		 //量化建设规模
		 List<Map<String, Object>> quaInfoList=this.getQuaInfoByGuid(projectGuid, moduleCode);
		 //项目履历
		 //重大库项目履历
		 List<Map<String, Object>> projectRecordInfo1=this.getProjectRecordByGuid(projectGuid, "1", moduleCode);
		//审核备项目履历
		 List<Map<String, Object>> projectRecordInfo2=this.getProjectRecordByGuid(projectGuid, "2", moduleCode);
		 //审核备办理事项
		 List<Map<String, Object>> matterInfoList=this.getMatterInfoByGuid(projectGuid, moduleCode);
		 //投资情况
		 List<Map<String, Object>> investmentInfoList=this.getInvestmentInfoByGuid(projectGuid, moduleCode);
		 //资金到位完成情况
		 List<Map<String, Object>> finishInfoList=this.getFinishInfoByGuid(projectGuid, moduleCode);
		 //调度信息
		 List<Map<String, Object>> dispatchInfoList=this.getDispatchInfoByGuid(projectGuid, moduleCode);
		 //时间转换
		 //获取图片路径    
         String tempFilePath = ServletActionContext.getServletContext().getRealPath( "/") ;
		 SimpleDateFormat time=new SimpleDateFormat("yyyy"); 
		 SimpleDateFormat time1=new SimpleDateFormat("yyyy-MM-dd"); 
		 SimpleDateFormat time2=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          XWPFDocument doc = new XWPFDocument();
          //第一页
         //设置项目名称
          XWPFParagraph projectName = doc.createParagraph();
          projectName.setAlignment(ParagraphAlignment.CENTER);
          XWPFRun r1 = projectName.createRun();
          r1.setBold(true);
          r1.setFontFamily("宋体");
         //项目名称
          r1.setText(baseInfoList.get(0).get("PRO_NAME").toString());
          r1.setFontSize(20);
         //设置项目档案文字
          XWPFParagraph allTitle = doc.createParagraph();
          allTitle.setAlignment(ParagraphAlignment.CENTER);
          XWPFRun runText1=allTitle.createRun();
          runText1.setText("项目档案");
          runText1.setFontSize(25);
          //设置项目单位
          XWPFParagraph proOrg = doc.createParagraph();
          proOrg.setAlignment(ParagraphAlignment.CENTER);
          XWPFRun runText2=proOrg.createRun();
          runText2.setText("项目单位："+baseInfoList.get(0).get("PRO_ORG").toString());
          runText2.setFontSize(20);
          //设置活动类型
          XWPFParagraph storeTime = doc.createParagraph();//设置活动类型
          storeTime.setAlignment(ParagraphAlignment.CENTER);
          XWPFRun runText3=storeTime.createRun();
          if(baseInfoList.get(0).get("STORE_TIME")!=null&&!"".equals(baseInfoList.get(0).get("STORE_TIME"))){
        	  runText3.setText("入库时间："+time.format(baseInfoList.get(0).get("STORE_TIME")).toString()+"年"+time1.format(baseInfoList.get(0).get("STORE_TIME")).toString().substring(5,7)+"月");  
          }else{
        	  runText3.setText("入库时间："); 
          }
          runText3.setFontSize(20);
          //第二页项目简要情况
          //项目简历一级标题
          XWPFParagraph brieflyTitle1 = doc.createParagraph();
          //分页符
          brieflyTitle1.setPageBreak(true);
          // 设置字体对齐方式3
          brieflyTitle1.setAlignment(ParagraphAlignment.LEFT);
          brieflyTitle1.setVerticalAlignment(TextAlignment.TOP);
         XWPFRun runText4=brieflyTitle1.createRun();
         runText4.setText("项目简历");
         runText4.setFontSize(20);
         //项目简历二级标题
         XWPFParagraph brieflyTitle2 = doc.createParagraph();
         // 设置字体对齐方式3
         brieflyTitle2.setAlignment(ParagraphAlignment.LEFT);
         brieflyTitle2.setVerticalAlignment(TextAlignment.TOP);
         XWPFRun runText5=brieflyTitle2.createRun();
         runText5.setText("项目信息");
         runText5.setFontSize(18);
         //项目信息表
    	  XWPFTable brieflyTable1 = doc.createTable(11,2);
          CTTblPr brieflyBlPr1 = brieflyTable1.getCTTbl().getTblPr();
          brieflyBlPr1.getTblW().setType(STTblWidth.DXA);
          brieflyBlPr1.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          brieflyTable1.setCellMargins(20, 20,50, 50);
          //信息表赋值
          List<XWPFTableCell> tableCells = brieflyTable1.getRow(0).getTableCells();
          //获取第一行第一列的宽度
          XWPFTableCell cell = tableCells.get(0);
          //设置第一列的属性
          CTTcPr cellPr = cell.getCTTc().addNewTcPr(); 
          CTTblWidth cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          tableCells.get(0).setText("项目名称");
          tableCells.get(1).setText(baseInfoList.get(0).get("PRO_NAME").toString());  
          
          tableCells = brieflyTable1.getRow(1).getTableCells();
          tableCells.get(0).setText("项目类型");
          tableCells.get(1).setText(baseInfoList.get(0).get("PRO_TYPE").toString()); 
          
          tableCells = brieflyTable1.getRow(2).getTableCells();
	      tableCells.get(0).setText("总投资（万元）");
	      tableCells.get(1).setText(baseInfoList.get(0).get("INVESTMENT_TOTAL").toString()); 
	     
	      tableCells = brieflyTable1.getRow(3).getTableCells();
	      tableCells.get(0).setText("建设地点");
	      tableCells.get(1).setText(baseInfoList.get(0).get("BUILD_PLACE").toString()); 
	      
	      tableCells = brieflyTable1.getRow(4).getTableCells();
	      tableCells.get(0).setText("所属行业");
          tableCells.get(1).setText(baseInfoList.get(0).get("INDUSTRY").toString()); 
	      
	      tableCells = brieflyTable1.getRow(5).getTableCells();
	      tableCells.get(0).setText("国标行业");
	      tableCells.get(1).setText(baseInfoList.get(0).get("GB_INDUSTRY").toString()); 
	      
	      tableCells = brieflyTable1.getRow(6).getTableCells();
	      tableCells.get(0).setText("拟开工年份");
	      tableCells.get(1).setText(time.format(baseInfoList.get(0).get("EXPECT_STARTYEAR"))+"-"+baseInfoList.get(0).get("EXPECT_NY")); 
	      
	      tableCells = brieflyTable1.getRow(7).getTableCells();
	      tableCells.get(0).setText("拟建成年份");
	      tableCells.get(1).setText(time.format(baseInfoList.get(0).get("EXPECT_ENDYEAR"))); 
	      
	      tableCells = brieflyTable1.getRow(8).getTableCells();
	      tableCells.get(0).setText("项目（法人单位）");
	      tableCells.get(1).setText(baseInfoList.get(0).get("PRO_ORG").toString()); 
	      
	      tableCells = brieflyTable1.getRow(9).getTableCells();
	      tableCells.get(0).setText("主要建设规模");
	      tableCells.get(1).setText(baseInfoList.get(0).get("MAIN_BUILD_SCALE").toString()); 
	      
	      tableCells = brieflyTable1.getRow(10).getTableCells();
	      tableCells.get(0).setText("主要建设内容");
	      tableCells.get(1).setText(baseInfoList.get(0).get("MAIN_BUILD_CONTENT").toString()); 
          //项目简历二级标题
          XWPFParagraph brieflyTitle3 = doc.createParagraph();
          // 设置字体对齐方式3
          brieflyTitle3.setAlignment(ParagraphAlignment.LEFT);
          brieflyTitle3.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText6=brieflyTitle3.createRun();
          runText6.setText("资金构成");
          runText6.setFontSize(18);
          //项目信息表
          //判断投资情况是否为空
          if(investmentConstituteInfo.size()>0){
        	  //创建资金构成表
        	  XWPFTable brieflyTable2 = doc.createTable(investmentConstituteInfo.size()+1,2);
              CTTblPr brieflyBlPr2 = brieflyTable2.getCTTbl().getTblPr();
              brieflyBlPr2.getTblW().setType(STTblWidth.DXA);
              brieflyBlPr2.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              brieflyTable2.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = brieflyTable2.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run0.setText("资金类别");
              run1.setText("总投资");  
              //循环生成表格内容
              for(int i = 0;i<investmentConstituteInfo.size();i++){
            	  tableCells = brieflyTable2.getRow(i+1).getTableCells();
            	  tableCells.get(0).setText(investmentConstituteInfo.get(i).get("NAME").toString());
            	  tableCells.get(1).setText(investmentConstituteInfo.get(i).get("TOTAL_INVESTMENT").toString()); 
              }
          }else{
        	  //创建资金构成表
        	  XWPFTable brieflyTable2 = doc.createTable(investmentConstituteInfo.size()+1,2);
              CTTblPr brieflyBlPr2 = brieflyTable2.getCTTbl().getTblPr();
              brieflyBlPr2.getTblW().setType(STTblWidth.DXA);
              brieflyBlPr2.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              brieflyTable2.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = brieflyTable2.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run0.setText("资金类别");
              run1.setText("总投资"); 
          }
          //项目简历二级标题-计划下达情况
          XWPFParagraph brieflyTitle4 = doc.createParagraph();
          brieflyTitle4.setPageBreak(true);
          // 设置字体对齐方式3
          brieflyTitle4.setAlignment(ParagraphAlignment.LEFT);
          brieflyTitle4.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText7=brieflyTitle4.createRun();
          runText7.setText("计划下达情况");
          runText7.setFontSize(18);
          //计划下达情况表
          //判断计划下达情况是否为空
          if(issuedInfo.size()>0){
        	  //创建计划下达情况表
        	  XWPFTable brieflyTable3 = doc.createTable(issuedInfo.size()+1,3);
              CTTblPr brieflyBlPr3 = brieflyTable3.getCTTbl().getTblPr();
              brieflyBlPr3.getTblW().setType(STTblWidth.DXA);
              brieflyBlPr3.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              brieflyTable3.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = brieflyTable3.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run0.setText("下达文号");
              run1.setText("下达资金（万元）");  
              run2.setText("下达时间"); 
              //循环生成表格内容
              for(int i = 0;i<issuedInfo.size();i++){
            	  tableCells = brieflyTable3.getRow(i+1).getTableCells();
            	  if(issuedInfo.get(i).get("EXPORT_FILE_NO")!=null&&!"".equals(issuedInfo.get(i).get("EXPORT_FILE_NO"))){
            		  tableCells.get(0).setText(issuedInfo.get(i).get("EXPORT_FILE_NO").toString());
            	  }else{
            		  tableCells.get(0).setText(null);
            	  }
            	  if(issuedInfo.get(i).get("ISSUED_MONEY")!=null&&!"".equals(issuedInfo.get(i).get("ISSUED_MONEY"))){
            		  tableCells.get(1).setText(issuedInfo.get(i).get("ISSUED_MONEY").toString()); 
            	  }else{
            		  tableCells.get(1).setText(null); 
            	  }
            	  String issusedTime = null;
            	  if(issuedInfo.get(i).get("ISSUSED_TIME")!=null&&!"".equals(issuedInfo.get(i).get("ISSUSED_TIME"))){
            		  issusedTime =time1.format(issuedInfo.get(i).get("ISSUSED_TIME"));
            	  }
            	  tableCells.get(2).setText(issusedTime); 
              }
          }else{
        	//创建计划下达情况表
        	  XWPFTable brieflyTable3 = doc.createTable(issuedInfo.size()+1,3);
              CTTblPr brieflyBlPr3 = brieflyTable3.getCTTbl().getTblPr();
              brieflyBlPr3.getTblW().setType(STTblWidth.DXA);
              brieflyBlPr3.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              brieflyTable3.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = brieflyTable3.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run0.setText("下达文号");
              run1.setText("下达资金（万元）");  
              run2.setText("下达时间"); 
          }
          
          //项目简历二级标题-批复情况
          XWPFParagraph brieflyTitle5 = doc.createParagraph();
          // 设置字体对齐方式3
          brieflyTitle5.setAlignment(ParagraphAlignment.LEFT);
          brieflyTitle5.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText8=brieflyTitle5.createRun();
          runText8.setText("项目批复情况");
          runText8.setFontSize(18);
          //批复情况表
          //判断批复情况否为空
          if(preworkInfo.size()>0){
        	  //创建批复情况表
        	  XWPFTable brieflyTable4 = doc.createTable(preworkInfo.size()+1,3);
              CTTblPr brieflyBlPr4 = brieflyTable4.getCTTbl().getTblPr();
              brieflyBlPr4.getTblW().setType(STTblWidth.DXA);
              brieflyBlPr4.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              brieflyTable4.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = brieflyTable4.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run0.setText("批复文号");
              run1.setText("批复单位");  
              run2.setText("下批复日期"); 
              //循环生成表格内容
              for(int i = 0;i<preworkInfo.size();i++){
            	  tableCells = brieflyTable4.getRow(i+1).getTableCells();
            	  if(preworkInfo.get(i).get("REPLY_NO")!=null&&!"".equals(preworkInfo.get(i).get("REPLY_NO"))){
            		  tableCells.get(0).setText(preworkInfo.get(i).get("REPLY_NO").toString());
            	  }else{
            		  tableCells.get(0).setText(null);
            	  }
            	  if(preworkInfo.get(i).get("REPLY_ORG")!=null&&!"".equals(preworkInfo.get(i).get("REPLY_ORG"))){
            		  tableCells.get(1).setText(preworkInfo.get(i).get("REPLY_ORG").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
            	  String replyDate = null;
            	  if(preworkInfo.get(i).get("REPLY_DATE")!=null&&!"".equals(preworkInfo.get(i).get("REPLY_DATE"))){
            		  replyDate =time1.format(preworkInfo.get(i).get("REPLY_DATE"));
            	  }
            	  tableCells.get(2).setText(replyDate); 
              }
          }else{
        	//创建批复情况表
        	  XWPFTable brieflyTable4 = doc.createTable(preworkInfo.size()+1,3);
              CTTblPr brieflyBlPr4 = brieflyTable4.getCTTbl().getTblPr();
              brieflyBlPr4.getTblW().setType(STTblWidth.DXA);
              brieflyBlPr4.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              brieflyTable4.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = brieflyTable4.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run0.setText("批复文号");
              run1.setText("批复单位");  
              run2.setText("下批复日期"); 
          }
          
          //项目履历
          XWPFParagraph projectRecordTitle = doc.createParagraph();
          projectRecordTitle.setPageBreak(true);
          // 设置字体对齐方式3
          projectRecordTitle.setAlignment(ParagraphAlignment.LEFT);
          projectRecordTitle.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText9=projectRecordTitle.createRun();
          runText9.setText("项目履历");
          runText9.setFontSize(20);
          //项目履历表
          //判断项目履历是否为空
          if(projectRecordInfo1.size()>0){
        	  //创建项目履历表
        	  XWPFTable projectRecordTable = doc.createTable(projectRecordInfo1.size()+1,5);
              CTTblPr projectRecordBlPr = projectRecordTable.getCTTbl().getTblPr();
              projectRecordBlPr.getTblW().setType(STTblWidth.DXA);
              projectRecordBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              projectRecordTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = projectRecordTable.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              run0.setText("编制操作");
              run1.setText("编制时间");  
              run2.setText("编制部门"); 
              run3.setText("编制人员");  
              run4.setText("备注"); 
              //循环生成表格内容 生成表格内容时先做非空判断
              for(int i = 0;i<projectRecordInfo1.size();i++){
            	  tableCells = projectRecordTable.getRow(i+1).getTableCells();
            	  if(projectRecordInfo1.get(i).get("OPERATE")!=null&&!"".equals(projectRecordInfo1.get(i).get("OPERATE"))){
            		  tableCells.get(0).setText(projectRecordInfo1.get(i).get("OPERATE").toString());
            	  }else{
            		  tableCells.get(0).setText(null);
            	  }
            	  String optime = null;
            	  if(projectRecordInfo1.get(i).get("OPTIME")!=null&&!"".equals(projectRecordInfo1.get(i).get("OPTIME"))){
            		  optime =time2.format(projectRecordInfo1.get(i).get("OPTIME"));
            	  }
            	  tableCells.get(1).setText(optime); 
            	  if(projectRecordInfo1.get(i).get("OPDEPART")!=null&&!"".equals(projectRecordInfo1.get(i).get("OPDEPART"))){
            		  tableCells.get(2).setText(projectRecordInfo1.get(i).get("OPDEPART").toString());
            	  }else{
            		  tableCells.get(2).setText(null);
            	  }
            	  if(projectRecordInfo1.get(i).get("OPUSER")!=null&&!"".equals(projectRecordInfo1.get(i).get("OPUSER"))){
            		  tableCells.get(3).setText(projectRecordInfo1.get(i).get("OPUSER").toString());
            	  }else{
            		  tableCells.get(3).setText(null);
            	  }
            	  if(projectRecordInfo1.get(i).get("remark")!=null&&!"".equals(projectRecordInfo1.get(i).get("remark"))){
            		  tableCells.get(4).setText(projectRecordInfo1.get(i).get("remark").toString());
            	  }else{
            		  tableCells.get(4).setText(null);
            	  }
              }
          }else{
        	  //创建项目履历表
        	  XWPFTable projectRecordTable = doc.createTable(projectRecordInfo1.size()+1,5);
              CTTblPr projectRecordBlPr = projectRecordTable.getCTTbl().getTblPr();
              projectRecordBlPr.getTblW().setType(STTblWidth.DXA);
              projectRecordBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              projectRecordTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = projectRecordTable.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              run0.setText("编制操作");
              run1.setText("编制时间");  
              run2.setText("编制部门"); 
              run3.setText("编制人员");  
              run4.setText("备注");   
          }
          
          //审核备办理履历
          XWPFParagraph projectRecord2Title = doc.createParagraph();
          projectRecord2Title.setPageBreak(true);
          // 设置字体对齐方式3
          projectRecord2Title.setAlignment(ParagraphAlignment.LEFT);
          projectRecord2Title.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText10=projectRecord2Title.createRun();
          runText10.setText("审核备办理履历");
          runText10.setFontSize(20);
          //审核备办理履历表
          //判断审核备办理履历是否为空
          if(projectRecordInfo2.size()>0){
        	  //创建审核备办理履历表
        	  XWPFTable projectRecordTable = doc.createTable(projectRecordInfo2.size()+1,5);
              CTTblPr projectRecordBlPr = projectRecordTable.getCTTbl().getTblPr();
              projectRecordBlPr.getTblW().setType(STTblWidth.DXA);
              projectRecordBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              projectRecordTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = projectRecordTable.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              run0.setText("编制操作");
              run1.setText("编制时间");  
              run2.setText("编制部门"); 
              run3.setText("编制人员");  
              run4.setText("备注"); 
            //循环生成表格内容 生成表格内容时先做非空判断
              for(int i = 0;i<projectRecordInfo2.size();i++){
            	  tableCells = projectRecordTable.getRow(i+1).getTableCells();
            	  if(projectRecordInfo2.get(i).get("bzcz")!=null&&!"".equals(projectRecordInfo2.get(i).get("bzcz"))){
            		  tableCells.get(0).setText(projectRecordInfo2.get(i).get("bzcz").toString());
            	  }else{
            		  tableCells.get(0).setText(null);
            	  }
            	  String dealedDate = null;
            	  if(projectRecordInfo2.get(i).get("dealed_date")!=null&&!"".equals(projectRecordInfo2.get(i).get("dealed_date"))){
            		  dealedDate =time2.format(projectRecordInfo2.get(i).get("dealed_date"));
            	  }
            	  tableCells.get(1).setText(dealedDate); 
            	  if(projectRecordInfo2.get(i).get("DEPT_NAME")!=null&&!"".equals(projectRecordInfo2.get(i).get("DEPT_NAME"))){
            		  tableCells.get(2).setText(projectRecordInfo2.get(i).get("DEPT_NAME").toString());
            	  }else{
            		  tableCells.get(2).setText(null);
            	  }
            	  if(projectRecordInfo2.get(i).get("OPUSER")!=null&&!"".equals(projectRecordInfo2.get(i).get("OPUSER"))){
            		  tableCells.get(3).setText(projectRecordInfo2.get(i).get("OPUSER").toString());
            	  }else{
            		  tableCells.get(3).setText(null);
            	  }
            	  if(projectRecordInfo2.get(i).get("remark")!=null&&!"".equals(projectRecordInfo2.get(i).get("remark"))){
            		  tableCells.get(4).setText(projectRecordInfo2.get(i).get("remark").toString());
            	  }else{
            		  tableCells.get(4).setText(null);
            	  }
              }
          }else{
        	//创建审核备办理履历表
        	  XWPFTable projectRecordTable = doc.createTable(projectRecordInfo2.size()+1,5);
              CTTblPr projectRecordBlPr = projectRecordTable.getCTTbl().getTblPr();
              projectRecordBlPr.getTblW().setType(STTblWidth.DXA);
              projectRecordBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              projectRecordTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = projectRecordTable.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              run0.setText("编制操作");
              run1.setText("编制时间");  
              run2.setText("编制部门"); 
              run3.setText("编制人员");  
              run4.setText("备注");   
          }
          
          //项目信息表信息页面设置
          XWPFParagraph projectInfoTitle = doc.createParagraph();
          projectInfoTitle.setPageBreak(true);
          // 设置字体对齐方式3
          projectInfoTitle.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText11=projectInfoTitle.createRun();
          runText11.setText("基本信息");
          runText11.setFontSize(20);
        //项目信息表-1
    	  XWPFTable projectInfoTable1 = doc.createTable(23,2);
          CTTblPr projectInfoBlPr1 = projectInfoTable1.getCTTbl().getTblPr();
          projectInfoBlPr1.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr1.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable1.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable1.getRow(0).getTableCells();
          //获取第一行第一列的宽度
           cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          tableCells.get(0).setText("申报日期");
          if(baseInfoList.get(0).get("STORE_TIME")!=null&&!"".equals(baseInfoList.get(0).get("STORE_TIME"))){
        	  tableCells.get(1).setText(time1.format(baseInfoList.get(0).get("STORE_TIME")));
          }else{
        	  tableCells.get(1).setText(null);
          }
          
          tableCells = projectInfoTable1.getRow(1).getTableCells();
          tableCells.get(0).setText("审核目录分类");
          if(baseInfoList.get(0).get("appDirTypeCode")!=null&&!"".equals(baseInfoList.get(0).get("appDirTypeCode"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("appDirTypeCode").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable1.getRow(2).getTableCells();
          tableCells.get(0).setText("审核目录");
          if(baseInfoList.get(0).get("appDirCode")!=null&&!"".equals(baseInfoList.get(0).get("appDirCode"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("appDirCode").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable1.getRow(3).getTableCells();
          tableCells.get(0).setText("重大库编号");
          if(baseInfoList.get(0).get("projectCode1")!=null&&!"".equals(baseInfoList.get(0).get("projectCode1"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("projectCode1").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable1.getRow(4).getTableCells();
          tableCells.get(0).setText("审批监管平台代码");
          if(baseInfoList.get(0).get("projectCode2")!=null&&!"".equals(baseInfoList.get(0).get("projectCode2"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("projectCode2").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable1.getRow(5).getTableCells();
          tableCells.get(0).setText("项目名称");
          if(baseInfoList.get(0).get("PRO_NAME")!=null&&!"".equals(baseInfoList.get(0).get("PRO_NAME"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_NAME").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
         
          tableCells = projectInfoTable1.getRow(6).getTableCells();
          tableCells.get(0).setText("项目类型");
          if(baseInfoList.get(0).get("PRO_TYPE")!=null&&!"".equals(baseInfoList.get(0).get("PRO_TYPE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_TYPE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable1.getRow(7).getTableCells();
          tableCells.get(0).setText("建设性质");
          if(baseInfoList.get(0).get("BUILD_NATURE")!=null&&!"".equals(baseInfoList.get(0).get("BUILD_NATURE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("BUILD_NATURE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable1.getRow(8).getTableCells();
	      tableCells.get(0).setText("国别");
	      if(baseInfoList.get(0).get("COUNTRY")!=null&&!"".equals(baseInfoList.get(0).get("COUNTRY"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("COUNTRY").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(9).getTableCells();
	      tableCells.get(0).setText("建设地点");
	      if(baseInfoList.get(0).get("BUILD_PLACE")!=null&&!"".equals(baseInfoList.get(0).get("BUILD_PLACE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("BUILD_PLACE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(10).getTableCells();
	      tableCells.get(0).setText("建设地点详情");
	      if(baseInfoList.get(0).get("BUILD_PLACE_DETAIL")!=null&&!"".equals(baseInfoList.get(0).get("BUILD_PLACE_DETAIL"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("BUILD_PLACE_DETAIL").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(11).getTableCells();
	      tableCells.get(0).setText("建设详细地址");
	      if(baseInfoList.get(0).get("BUILD_ADDRESS")!=null&&!"".equals(baseInfoList.get(0).get("BUILD_ADDRESS"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("BUILD_ADDRESS").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(12).getTableCells();
	      tableCells.get(0).setText("国标行业");
	      if(baseInfoList.get(0).get("GB_INDUSTRY")!=null&&!"".equals(baseInfoList.get(0).get("GB_INDUSTRY"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("GB_INDUSTRY").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(13).getTableCells();
	      tableCells.get(0).setText("所属行业");
          if(baseInfoList.get(0).get("INDUSTRY")!=null&&!"".equals(baseInfoList.get(0).get("INDUSTRY"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("INDUSTRY").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable1.getRow(14).getTableCells();
	      tableCells.get(0).setText("总投资（万元）");
	      if(baseInfoList.get(0).get("INVESTMENT_TOTAL")!=null&&!"".equals(baseInfoList.get(0).get("INVESTMENT_TOTAL"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("INVESTMENT_TOTAL").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(15).getTableCells();
	      tableCells.get(0).setText("拟开工年份");
	      tableCells.get(1).setText(time.format(baseInfoList.get(0).get("EXPECT_STARTYEAR")));
	      
	      tableCells = projectInfoTable1.getRow(16).getTableCells();
	      tableCells.get(0).setText("拟开工月份");
	      if(baseInfoList.get(0).get("EXPECT_NY")!=null&&!"".equals(baseInfoList.get(0).get("EXPECT_NY"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("EXPECT_NY").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(17).getTableCells();
	      tableCells.get(0).setText("拟建成年份");
	      tableCells.get(1).setText(time.format(baseInfoList.get(0).get("EXPECT_ENDYEAR")));
	      
	      tableCells = projectInfoTable1.getRow(18).getTableCells();
	      tableCells.get(0).setText("主要建设规模");
	      if(baseInfoList.get(0).get("MAIN_BUILD_SCALE")!=null&&!"".equals(baseInfoList.get(0).get("MAIN_BUILD_SCALE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("MAIN_BUILD_SCALE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(19).getTableCells();
	      tableCells.get(0).setText("量化建设规模	");
          XWPFTableCell newcell = tableCells.get(1);
          CTTcPr tcpr = newcell.getCTTc().addNewTcPr();
          XmlCursor xmlcell =tcpr.newCursor();
          XWPFTable tb2=newcell.insertNewTbl(xmlcell); 
          XWPFTableRow row1=tb2.insertNewTableRow(0);
          XWPFTableCell  cl1= row1.addNewTableCell(); 
          XWPFTableCell  cl2=row1.addNewTableCell();
          //设置第一列的属性
          cellPr = cl1.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(3000));
          XWPFParagraph tb2newPara0 = new XWPFParagraph(cl1.getCTTc().addNewP(), cl1);
          XWPFParagraph tb2newPara1 = new XWPFParagraph(cl2.getCTTc().addNewP(), cl2);
          //固定表头内容属性
          XWPFRun tb2run0 = tb2newPara0.createRun();
          XWPFRun tb2run1 = tb2newPara1.createRun();
          /** 内容居中显示 **/
          tb2newPara0.setAlignment(ParagraphAlignment.CENTER);
          tb2newPara0.setVerticalAlignment(TextAlignment.TOP);
          tb2newPara1.setAlignment(ParagraphAlignment.CENTER);
          tb2newPara1.setVerticalAlignment(TextAlignment.TOP);
          //加粗显示
          tb2run0.setBold(true);
          tb2run1.setBold(true);
          tb2run0.setText("类别");
          //设置第一列的属性
//          cellPr = cl2.getCTTc().addNewTcPr(); 
//          cellw = cellPr.addNewTcW();
//          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
//          cellPr.addNewTcW().setW(BigInteger.valueOf(3000));
          tb2run1.setText("数值");
	      //判断量化建设规模有没有值
	      if(quaInfoList.size()>0){
	    	  for(int i = 0;i<quaInfoList.size();i++){
		    	  XWPFTableRow row=tb2.insertNewTableRow(i+1);                        
	              cl1= row.addNewTableCell(); 	                   
	              cl1.setText(quaInfoList.get(i).get("PIE_NAME").toString());
	              cl2=row.addNewTableCell();
                  if(null!=quaInfoList.get(i).get("_number")) {
                      cl2.setText(quaInfoList.get(i).get("_number").toString());
                  }
                  // 插入空值
                  else{
                      cl2.setText("");
                  }
	    	  }
//        	  //创建资金构成表
//        	  XWPFTable quaInfoTable = doc.createTable(quaInfoList.size()+1,2);
//              CTTblPr quaBlPr = quaInfoTable.getCTTbl().getTblPr();
//              quaBlPr.getTblW().setType(STTblWidth.DXA);
//              quaBlPr.getTblW().setW(BigInteger.valueOf(6800));
//              // 设置上下左右四个方向的距离，可以将表格撑大
//              quaInfoTable.setCellMargins(20, 20, 20, 20);
//              //信息表表头赋值
//              List<XWPFTableCell> quaInfotableCells = quaInfoTable.getRow(0).getTableCells();
//              XWPFTableCell quacell0 = quaInfotableCells.get(0);
//              //获取第一行第一列的宽度
//             //设置第一列的属性
//             cellPr = quacell0.getCTTc().addNewTcPr(); 
//             cellw = cellPr.addNewTcW();
//             cellw.setType(STTblWidth.DXA);
//              XWPFTableCell quacell1 = quaInfotableCells.get(1);
//            //设置第一列的宽度
//              cellPr.addNewTcW().setW(BigInteger.valueOf(4000));
//              XWPFParagraph quanewPara0 = new XWPFParagraph(quacell0.getCTTc().addNewP(), quacell0);
//              XWPFParagraph quanewPara1 = new XWPFParagraph(quacell1.getCTTc().addNewP(), quacell1);
//              //固定表头内容属性
//              XWPFRun quarun0 = quanewPara0.createRun();
//              XWPFRun quarun1 = quanewPara1.createRun();
//              /** 内容居中显示 **/
//              quanewPara0.setAlignment(ParagraphAlignment.CENTER);
//              quanewPara1.setAlignment(ParagraphAlignment.CENTER);
//              //加粗显示
//              quarun0.setBold(true);
//              quarun1.setBold(true);
//              quarun0.setText("类别");
//              quarun1.setText("数值");  
//              //循环生成表格内容
//              for(int i = 0;i<quaInfoList.size();i++){
//            	  quaInfotableCells = quaInfoTable.getRow(i+1).getTableCells();
//            	  quaInfotableCells.get(0).setText(quaInfoList.get(i).get("PIE_NAME").toString());
//            	  if(quaInfoList.get(i).get("DECIMAL")!=null&&!"".equals(quaInfoList.get(i).get("DECIMAL"))){
//            		  quaInfotableCells.get(1).setText(quaInfoList.get(i).get("DECIMAL").toString());
//            	  }else{
//            		  quaInfotableCells.get(1).setText(null);
//            	  }
//              }
//              tableCells.get(1).insertTable(0, quaInfoTable);
          }else{
        	  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(20).getTableCells();
	      tableCells.get(0).setText("*（年度）主要建设内容");
	      if(baseInfoList.get(0).get("MAIN_BUILD_CONTENT")!=null&&!"".equals(baseInfoList.get(0).get("MAIN_BUILD_CONTENT"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("MAIN_BUILD_CONTENT").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(21).getTableCells();
	      tableCells.get(0).setText("备注");
	      if(baseInfoList.get(0).get("REMARK")!=null&&!"".equals(baseInfoList.get(0).get("REMARK"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("REMARK").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      tableCells = projectInfoTable1.getRow(22).getTableCells();
	      tableCells.get(0).setText("入库依据");
	      if(baseInfoList.get(0).get("aaaa")!=null&&!"".equals(baseInfoList.get(0).get("aaaaaa"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("aaaaaa").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
	      
	      //项目信息表信息页面设置--入库依据-2
          XWPFParagraph projectInfoTitle2 = doc.createParagraph();
          //换行
          projectInfoTitle2.setPageBreak(true);
          // 设置字体对齐方式3
          projectInfoTitle2.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle2.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText12=projectInfoTitle2.createRun();
          runText12.setText("入库依据");
          runText12.setFontSize(20);
         //项目信息表
    	  XWPFTable projectInfoTable2 = doc.createTable(5,2);
          CTTblPr projectInfoBlPr2 = projectInfoTable2.getCTTbl().getTblPr();
          projectInfoBlPr2.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr2.getTblW().setW(BigInteger.valueOf(9000));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable2.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable2.getRow(0).getTableCells();
          //获取第一行第一列的宽度
          cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          tableCells.get(0).setText("符合产业政策");
          //获取第一行第二列的宽度
          cell = tableCells.get(1);
          //设置第二列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第二列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(7000));
          
          if(baseInfoList.get(0).get("PROPERTY_POLICY")!=null&&!"".equals(baseInfoList.get(0).get("PROPERTY_POLICY"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PROPERTY_POLICY").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable2.getRow(1).getTableCells();
          tableCells.get(0).setText("符合规划");
          if(baseInfoList.get(0).get("PLAN")!=null&&!"".equals(baseInfoList.get(0).get("PLAN"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PLAN").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable2.getRow(2).getTableCells();
          tableCells.get(0).setText("符合重大战略");
          if(baseInfoList.get(0).get("MAJOR_STRATEGY")!=null&&!"".equals(baseInfoList.get(0).get("MAJOR_STRATEGY"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("MAJOR_STRATEGY").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable2.getRow(3).getTableCells();
          tableCells.get(0).setText("符合政府投资方向");
          if(baseInfoList.get(0).get("GOVERNMENT_INVEST_DIRECTION")!=null&&!"".equals(baseInfoList.get(0).get("GOVERNMENT_INVEST_DIRECTION"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("GOVERNMENT_INVEST_DIRECTION").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable2.getRow(4).getTableCells();
          tableCells.get(0).setText("下达单位");
          if(baseInfoList.get(0).get("ORG_ISSUED")!=null&&!"".equals(baseInfoList.get(0).get("ORG_ISSUED"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("ORG_ISSUED").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          //项目信息表信息页面设置--是否PPP-3
          XWPFParagraph projectInfoTitle3 = doc.createParagraph();
          // 设置字体对齐方式3
          projectInfoTitle3.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle3.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText13=projectInfoTitle3.createRun();
          runText13.setText("PPP项目信息");
          runText13.setFontSize(20);
         //项目信息表
    	  XWPFTable projectInfoTable3 = doc.createTable(3,2);
          CTTblPr projectInfoBlPr3 = projectInfoTable3.getCTTbl().getTblPr();
          projectInfoBlPr3.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr3.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable3.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable3.getRow(0).getTableCells();
        //获取第一行第一列的宽度
          cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第二列的宽度
          cell = tableCells.get(1);
          //设置第二列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第二列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(7000));
          tableCells.get(0).setText("是否PPP");
          if(baseInfoList.get(0).get("ISPPP")!=null&&!"".equals(baseInfoList.get(0).get("ISPPP"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("ISPPP").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable3.getRow(1).getTableCells();
          tableCells.get(0).setText("政府参与方式	");
          if(baseInfoList.get(0).get("GOVERNMENT_JOIN_TYPE")!=null&&!"".equals(baseInfoList.get(0).get("GOVERNMENT_JOIN_TYPE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("GOVERNMENT_JOIN_TYPE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable3.getRow(2).getTableCells();
          tableCells.get(0).setText("拟采用PPP<br>操作模式");
          if(baseInfoList.get(0).get("EXPECT_PPP_OPERATE_MODE")!=null&&!"".equals(baseInfoList.get(0).get("EXPECT_PPP_OPERATE_MODE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("EXPECT_PPP_OPERATE_MODE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          //项目信息表信息页面设置--专项类别-4
          XWPFParagraph projectInfoTitle4 = doc.createParagraph();
          // 设置字体对齐方式3
          projectInfoTitle4.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle4.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText14=projectInfoTitle4.createRun();
          runText14.setText("专项建设基金信息");
          runText14.setFontSize(20);
         //项目信息表
    	  XWPFTable projectInfoTable4 = doc.createTable(6,2);
          CTTblPr projectInfoBlPr4 = projectInfoTable4.getCTTbl().getTblPr();
          projectInfoBlPr4.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr4.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable4.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable4.getRow(0).getTableCells();
        //获取第一行第一列的宽度
          cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第二列的宽度
          cell = tableCells.get(1);
          //设置第二列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第二列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(7000));
          tableCells.get(0).setText("专项类别");
          if(baseInfoList.get(0).get("SPECIAL_TYPE")!=null&&!"".equals(baseInfoList.get(0).get("SPECIAL_TYPE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("SPECIAL_TYPE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable4.getRow(1).getTableCells();
          tableCells.get(0).setText("回报方式");
          if(baseInfoList.get(0).get("RETURN_METHOD")!=null&&!"".equals(baseInfoList.get(0).get("RETURN_METHOD"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("RETURN_METHOD").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable4.getRow(2).getTableCells();
          tableCells.get(0).setText("回报率（%）");
          if(baseInfoList.get(0).get("RETURN_RATE")!=null&&!"".equals(baseInfoList.get(0).get("RETURN_RATE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("RETURN_RATE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable4.getRow(3).getTableCells();
          tableCells.get(0).setText("回报周期（年）");
          if(baseInfoList.get(0).get("RETURN_PERIOD")!=null&&!"".equals(baseInfoList.get(0).get("RETURN_PERIOD"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("RETURN_PERIOD").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable4.getRow(4).getTableCells();
          tableCells.get(0).setText("拉动效果（1:x）");
          if(baseInfoList.get(0).get("PULLING_EFFECT")!=null&&!"".equals(baseInfoList.get(0).get("PULLING_EFFECT"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PULLING_EFFECT").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable4.getRow(5).getTableCells();
          tableCells.get(0).setText("建议银行");
          if(baseInfoList.get(0).get("PROPOSED_BANK")!=null&&!"".equals(baseInfoList.get(0).get("PROPOSED_BANK"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PROPOSED_BANK").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
        //项目信息表信息页面设置--一带一路-5
          XWPFParagraph projectInfoTitle5 = doc.createParagraph();
          // 设置字体对齐方式3
          projectInfoTitle5.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle5.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText15=projectInfoTitle5.createRun();
          runText15.setText("一带一路");
          runText15.setFontSize(20);
         //项目信息表
    	  XWPFTable projectInfoTable5 = doc.createTable(3,4);
          CTTblPr projectInfoBlPr5 = projectInfoTable5.getCTTbl().getTblPr();
          projectInfoBlPr5.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr5.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable5.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable5.getRow(0).getTableCells();
          //获取第一行第一列的宽度
          cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第二列的宽度
          cell = tableCells.get(1);
          //设置第二列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第二列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第三列的宽度
          cell = tableCells.get(2);
          //设置第三列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第三列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第四列的宽度
          cell = tableCells.get(3);
          //设置第四列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第四列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          
          tableCells.get(0).setText("牵头单位");
          if(baseInfoList.get(0).get("ORG_LEAD")!=null&&!"".equals(baseInfoList.get(0).get("ORG_LEAD"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("ORG_LEAD").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          tableCells.get(2).setText("业主单位");
          if(baseInfoList.get(0).get("ORG_OWNER")!=null&&!"".equals(baseInfoList.get(0).get("ORG_OWNER"))){
    		  tableCells.get(3).setText(baseInfoList.get(0).get("ORG_OWNER").toString());
    	  }else{
    		  tableCells.get(3).setText(null);
    	  }
          
          tableCells = projectInfoTable5.getRow(1).getTableCells();
          tableCells.get(0).setText("建设单位");
          if(baseInfoList.get(0).get("ORG_INVEST")!=null&&!"".equals(baseInfoList.get(0).get("ORG_INVEST"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("ORG_INVEST").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          tableCells.get(2).setText("类型");
          if(baseInfoList.get(0).get("ONE_ONE_TYPE")!=null&&!"".equals(baseInfoList.get(0).get("ONE_ONE_TYPE"))){
    		  tableCells.get(3).setText(baseInfoList.get(0).get("ONE_ONE_TYPE").toString());
    	  }else{
    		  tableCells.get(3).setText(null);
    	  }
          
          tableCells = projectInfoTable5.getRow(2).getTableCells();
          tableCells.get(0).setText("推进情况");
          if(baseInfoList.get(0).get("ADVANCE_SITUATION")!=null&&!"".equals(baseInfoList.get(0).get("ADVANCE_SITUATION"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("ADVANCE_SITUATION").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          tableCells.get(2).setText("牵头单位");
          if(baseInfoList.get(0).get("aaaa")!=null&&!"".equals(baseInfoList.get(0).get("aaaa"))){
    		  tableCells.get(3).setText(baseInfoList.get(0).get("aaaa").toString());
    	  }else{
    		  tableCells.get(3).setText(null);
    	  }
          
        //项目信息表信息页面设置--项目责任人(PPP项目为政府方责任人)-6
          XWPFParagraph projectInfoTitle6 = doc.createParagraph();
          // 设置字体对齐方式3
          projectInfoTitle6.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle6.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText16=projectInfoTitle6.createRun();
          runText16.setText("项目责任人(PPP项目为政府方责任人)");
          runText16.setFontSize(20);
         //项目信息表
    	  XWPFTable projectInfoTable6 = doc.createTable(5,2);
          CTTblPr projectInfoBlPr6 = projectInfoTable6.getCTTbl().getTblPr();
          projectInfoBlPr6.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr6.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable6.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable6.getRow(0).getTableCells();
        //获取第一行第一列的宽度
          cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第二列的宽度
          cell = tableCells.get(1);
          //设置第二列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第二列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(7000));
          tableCells.get(0).setText("姓名");
          if(baseInfoList.get(0).get("PRO_USER_NAME")!=null&&!"".equals(baseInfoList.get(0).get("PRO_USER_NAME"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_USER_NAME").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable6.getRow(1).getTableCells();
          tableCells.get(0).setText("手机");
          if(baseInfoList.get(0).get("PRO_USER_PHONE")!=null&&!"".equals(baseInfoList.get(0).get("PRO_USER_PHONE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_USER_PHONE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable6.getRow(2).getTableCells();
          tableCells.get(0).setText("固话");
          if(baseInfoList.get(0).get("PRO_USER_TELEPHONE")!=null&&!"".equals(baseInfoList.get(0).get("PRO_USER_TELEPHONE"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_USER_TELEPHONE").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable6.getRow(3).getTableCells();
          tableCells.get(0).setText("邮箱");
          if(baseInfoList.get(0).get("PRO_USER_MAILBOX")!=null&&!"".equals(baseInfoList.get(0).get("PRO_USER_MAILBOX"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_USER_MAILBOX").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable6.getRow(4).getTableCells();
          tableCells.get(0).setText("微信账号");
          if(baseInfoList.get(0).get("PRO_USER_WXNUMBER")!=null&&!"".equals(baseInfoList.get(0).get("PRO_USER_WXNUMBER"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_USER_WXNUMBER").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          //项目信息表信息页面设置--项目（法人）单位-7
          XWPFParagraph projectInfoTitle7 = doc.createParagraph();
          // 设置字体对齐方式3
          projectInfoTitle7.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle7.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText17=projectInfoTitle7.createRun();
          runText17.setText("项目（法人）单位");
          runText17.setFontSize(20);
         //项目信息表
    	  XWPFTable projectInfoTable7= doc.createTable(6,2);
          CTTblPr projectInfoBlPr7 = projectInfoTable7.getCTTbl().getTblPr();
          projectInfoBlPr7.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr7.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable7.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable7.getRow(0).getTableCells();
        //获取第一行第一列的宽度
          cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第二列的宽度
          cell = tableCells.get(1);
          //设置第二列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第二列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(7000));
          tableCells.get(0).setText("项目（法人单位）");
          if(baseInfoList.get(0).get("PRO_ORG")!=null&&!"".equals(baseInfoList.get(0).get("PRO_ORG"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_ORG").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable7.getRow(1).getTableCells();
          tableCells.get(0).setText("证照类型");
          if(baseInfoList.get(0).get("legalUnitType")!=null&&!"".equals(baseInfoList.get(0).get("legalUnitType"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("legalUnitType").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable7.getRow(2).getTableCells();
          tableCells.get(0).setText("证照号码");
          if(baseInfoList.get(0).get("legalUnitNum")!=null&&!"".equals(baseInfoList.get(0).get("legalUnitNum"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("legalUnitNum").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable7.getRow(3).getTableCells();
          tableCells.get(0).setText("联系人");
          if(baseInfoList.get(0).get("legalUnitContacts")!=null&&!"".equals(baseInfoList.get(0).get("legalUnitContacts"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("legalUnitContacts").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable7.getRow(4).getTableCells();
          tableCells.get(0).setText("联系电话");
          if(baseInfoList.get(0).get("legalUnitTel")!=null&&!"".equals(baseInfoList.get(0).get("legalUnitTel"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("legalUnitTel").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable7.getRow(5).getTableCells();
          tableCells.get(0).setText("电子邮箱");
          if(baseInfoList.get(0).get("legalUnitMail")!=null&&!"".equals(baseInfoList.get(0).get("legalUnitMail"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("legalUnitMail").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          //项目信息表信息页面设置-项目申报单位-8
          XWPFParagraph projectInfoTitle8 = doc.createParagraph();
          // 设置字体对齐方式3
          projectInfoTitle8.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle8.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText18=projectInfoTitle8.createRun();
          runText18.setText("项目申报单位");
          runText18.setFontSize(20);
         //项目信息表
    	  XWPFTable projectInfoTable8= doc.createTable(6,2);
          CTTblPr projectInfoBlPr8 = projectInfoTable8.getCTTbl().getTblPr();
          projectInfoBlPr8.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr8.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable8.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable8.getRow(0).getTableCells();
        //获取第一行第一列的宽度
          cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第二列的宽度
          cell = tableCells.get(1);
          //设置第二列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第二列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(7000));
          tableCells.get(0).setText("项目申报单位");
          if(baseInfoList.get(0).get("CREATE_DEPARTMENT_FULLNAME")!=null&&!"".equals(baseInfoList.get(0).get("CREATE_DEPARTMENT_FULLNAME"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("CREATE_DEPARTMENT_FULLNAME").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable8.getRow(1).getTableCells();
          tableCells.get(0).setText("证照类型");
          if(baseInfoList.get(0).get("applyDeptType")!=null&&!"".equals(baseInfoList.get(0).get("applyDeptType"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("applyDeptType").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable8.getRow(2).getTableCells();
          tableCells.get(0).setText("证照号码");
          if(baseInfoList.get(0).get("applyDeptNum")!=null&&!"".equals(baseInfoList.get(0).get("applyDeptNum"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("applyDeptNum").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable8.getRow(3).getTableCells();
          tableCells.get(0).setText("联系人");
          if(baseInfoList.get(0).get("applyDeptContacts")!=null&&!"".equals(baseInfoList.get(0).get("applyDeptContacts"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("applyDeptContacts").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable8.getRow(4).getTableCells();
          tableCells.get(0).setText("联系电话");
          if(baseInfoList.get(0).get("applyDeptTel")!=null&&!"".equals(baseInfoList.get(0).get("applyDeptTel"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("applyDeptTel").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable8.getRow(5).getTableCells();
          tableCells.get(0).setText("电子邮箱");
          if(baseInfoList.get(0).get("applyDeptMail")!=null&&!"".equals(baseInfoList.get(0).get("applyDeptMail"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("applyDeptMail").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          //项目信息表信息页面设置-项目联系人一-9
          XWPFParagraph projectInfoTitle9 = doc.createParagraph();
          // 设置字体对齐方式3
          projectInfoTitle9.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle9.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText19=projectInfoTitle9.createRun();
          runText19.setText("项目联系人一");
          runText19.setFontSize(20);
         //项目信息表
    	  XWPFTable projectInfoTable9= doc.createTable(5,2);
          CTTblPr projectInfoBlPr9 = projectInfoTable9.getCTTbl().getTblPr();
          projectInfoBlPr9.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr9.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable9.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable9.getRow(0).getTableCells();
        //获取第一行第一列的宽度
          cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第二列的宽度
          cell = tableCells.get(1);
          //设置第二列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第二列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(7000));
          tableCells.get(0).setText("姓名");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_NAME1")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_NAME1"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_NAME1").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable9.getRow(1).getTableCells();
          tableCells.get(0).setText("手机");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_PHONE1")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_PHONE1"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_PHONE1").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable9.getRow(2).getTableCells();
          tableCells.get(0).setText("固话");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_TELEPHONE1")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_TELEPHONE1"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_TELEPHONE1").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable9.getRow(3).getTableCells();
          tableCells.get(0).setText("邮箱");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_MAILBOX1")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_MAILBOX1"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_MAILBOX1").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable9.getRow(4).getTableCells();
          tableCells.get(0).setText("微信账号");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_WXNUMBER1")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_WXNUMBER1"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_WXNUMBER1").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
        //项目信息表信息页面设置-项目联系人二-10
          XWPFParagraph projectInfoTitle10 = doc.createParagraph();
          // 设置字体对齐方式3
          projectInfoTitle10.setAlignment(ParagraphAlignment.LEFT);
          projectInfoTitle10.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText20=projectInfoTitle10.createRun();
          runText20.setText("项目联系人二");
          runText20.setFontSize(20);
         //项目信息表
    	  XWPFTable projectInfoTable10= doc.createTable(5,2);
          CTTblPr projectInfoBlPr10 = projectInfoTable10.getCTTbl().getTblPr();
          projectInfoBlPr10.getTblW().setType(STTblWidth.DXA);
          projectInfoBlPr10.getTblW().setW(new BigInteger("9000"));
          // 设置上下左右四个方向的距离，可以将表格撑大
          projectInfoTable10.setCellMargins(20, 20, 20, 20);
          //信息表赋值
          tableCells = projectInfoTable10.getRow(0).getTableCells();
          //获取第一行第一列的宽度
          cell = tableCells.get(0);
          //设置第一列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第一列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(2000));
          //获取第一行第二列的宽度
          cell = tableCells.get(1);
          //设置第二列的属性
          cellPr = cell.getCTTc().addNewTcPr(); 
          cellw = cellPr.addNewTcW();
          cellw.setType(STTblWidth.DXA);
          //设置第二列的宽度
          cellPr.addNewTcW().setW(BigInteger.valueOf(7000));
          tableCells.get(0).setText("姓名");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_NAME2")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_NAME2"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_NAME2").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable10.getRow(1).getTableCells();
          tableCells.get(0).setText("手机");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_PHONE2")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_PHONE2"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_PHONE2").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable10.getRow(2).getTableCells();
          tableCells.get(0).setText("固话");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_TELEPHONE2")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_TELEPHONE2"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_TELEPHONE2").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable10.getRow(3).getTableCells();
          tableCells.get(0).setText("邮箱");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_MAILBOX2")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_MAILBOX2"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_MAILBOX2").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
          tableCells = projectInfoTable10.getRow(4).getTableCells();
          tableCells.get(0).setText("微信账号");
          if(baseInfoList.get(0).get("PRO_CONTACTOR_WXNUMBER2")!=null&&!"".equals(baseInfoList.get(0).get("PRO_CONTACTOR_WXNUMBER2"))){
    		  tableCells.get(1).setText(baseInfoList.get(0).get("PRO_CONTACTOR_WXNUMBER2").toString());
    	  }else{
    		  tableCells.get(1).setText(null);
    	  }
          
        //审核备办理事项表页面设置
          XWPFParagraph matterInfoTitle = doc.createParagraph();
          matterInfoTitle.setPageBreak(true);
          // 设置字体对齐方式3
          matterInfoTitle.setAlignment(ParagraphAlignment.LEFT);
          matterInfoTitle.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText21=matterInfoTitle.createRun();
          runText21.setText("审核备办理事项");
          runText21.setFontSize(20);
          //审核备办理事项表
          //判断审核备办理事项是否为空
          if(matterInfoList.size()>0){
        	  //创建资金构成表
        	  XWPFTable matterTable = doc.createTable(matterInfoList.size()+1,10);
              CTTblPr matterBlPr = matterTable.getCTTbl().getTblPr();
              matterBlPr.getTblW().setType(STTblWidth.DXA);
              matterBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              matterTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = matterTable.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFTableCell cell5 = tableCells.get(5);
              XWPFTableCell cell6 = tableCells.get(6);
              XWPFTableCell cell7 = tableCells.get(7);
              XWPFTableCell cell8 = tableCells.get(8);
              XWPFTableCell cell9 = tableCells.get(9);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              XWPFParagraph newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
              XWPFParagraph newPara6 = new XWPFParagraph(cell6.getCTTc().addNewP(), cell6);
              XWPFParagraph newPara7 = new XWPFParagraph(cell7.getCTTc().addNewP(), cell7);
              XWPFParagraph newPara8 = new XWPFParagraph(cell8.getCTTc().addNewP(), cell8);
              XWPFParagraph newPara9 = new XWPFParagraph(cell9.getCTTc().addNewP(), cell9);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              XWPFRun run5 = newPara5.createRun();
              XWPFRun run6 = newPara6.createRun();
              XWPFRun run7 = newPara7.createRun();
              XWPFRun run8 = newPara8.createRun();
              XWPFRun run9 = newPara9.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              newPara5.setAlignment(ParagraphAlignment.CENTER);
              newPara6.setAlignment(ParagraphAlignment.CENTER);
              newPara7.setAlignment(ParagraphAlignment.CENTER);
              newPara8.setAlignment(ParagraphAlignment.CENTER);
              newPara9.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              run5.setBold(true);
              run6.setBold(true);
              run7.setBold(true);
              run8.setBold(true);
              run9.setBold(true);
              run0.setText("事项名称");
              run1.setText("文件标题");  
              run2.setText("文号");
              run3.setText("办理状态");
              run4.setText("办理时间");
              run5.setText("办理时限");
              run6.setText("办理时长");
              run7.setText("审批部门所在行政");
              run8.setText("批复时间");
              run9.setText("批复有效期");
              //循环生成表格内容
              for(int i = 0;i<matterInfoList.size();i++){
            	  tableCells = matterTable.getRow(i+1).getTableCells();
            	  if(matterInfoList.get(i).get("ITEM_NAME")!=null&&!"".equals(matterInfoList.get(i).get("ITEM_NAME"))){
            		  tableCells.get(0).setText(matterInfoList.get(i).get("ITEM_NAME").toString());
            	  }else{
            		  tableCells.get(0).setText(null);
            	  }
            	  if(matterInfoList.get(i).get("FILETITLE_NAME")!=null&&!"".equals(matterInfoList.get(i).get("FILETITLE_NAME"))){
            		  tableCells.get(1).setText(matterInfoList.get(i).get("FILETITLE_NAME").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
            	  if(matterInfoList.get(i).get("APPROVAL_NUMBER")!=null&&!"".equals(matterInfoList.get(i).get("APPROVAL_NUMBER"))){
            		  tableCells.get(2).setText(matterInfoList.get(i).get("APPROVAL_NUMBER").toString());
            	  }else{
            		  tableCells.get(2).setText(null);
            	  }
            	  if(matterInfoList.get(i).get("CODE_NAME")!=null&&!"".equals(matterInfoList.get(i).get("CODE_NAME"))){
            		  tableCells.get(3).setText(matterInfoList.get(i).get("CODE_NAME").toString());
            	  }else{
            		  tableCells.get(3).setText(null);
            	  }
            	  String dealedDate = null;
            	  if(matterInfoList.get(i).get("DEALED_DATE")!=null&&!"".equals(matterInfoList.get(i).get("DEALED_DATE"))){
            		  dealedDate =time1.format(matterInfoList.get(i).get("DEALED_DATE"));
            	  }
            	  tableCells.get(4).setText(dealedDate); 
            	  if(matterInfoList.get(i).get("LIMIT_DAYS")!=null&&!"".equals(matterInfoList.get(i).get("LIMIT_DAYS"))){
            		  tableCells.get(5).setText(matterInfoList.get(i).get("LIMIT_DAYS").toString());
            	  }else{
            		  tableCells.get(5).setText(null);
            	  }
            	  if(matterInfoList.get(i).get("BJSJ")!=null&&!"".equals(matterInfoList.get(i).get("BJSJ"))){
            		  tableCells.get(6).setText(matterInfoList.get(i).get("BJSJ").toString());
            	  }else{
            		  tableCells.get(6).setText(null);
            	  }
            	  if(matterInfoList.get(i).get("NAME1")!=null&&!"".equals(matterInfoList.get(i).get("NAME1"))){
            		  tableCells.get(7).setText(matterInfoList.get(i).get("NAME1").toString());
            	  }else{
            		  tableCells.get(7).setText(null);
            	  }
            	  String completeddate = null;
            	  if(matterInfoList.get(i).get("COMPLETED_DATE")!=null&&!"".equals(matterInfoList.get(i).get("COMPLETED_DATE"))){
            		  completeddate =time1.format(matterInfoList.get(i).get("COMPLETED_DATE"));
            	  }
            	  tableCells.get(8).setText(completeddate);
            	  
            	  if(matterInfoList.get(i).get("VALIDITY_DATE")!=null&&!"".equals(matterInfoList.get(i).get("VALIDITY_DATE"))){
            		  tableCells.get(9).setText(matterInfoList.get(i).get("VALIDITY_DATE").toString());
            	  }else{
            		  tableCells.get(9).setText(null);
            	  }
              }
          }else{
        	  //创建资金构成表
        	  XWPFTable matterTable = doc.createTable(matterInfoList.size()+1,10);
              CTTblPr matterBlPr = matterTable.getCTTbl().getTblPr();
              matterBlPr.getTblW().setType(STTblWidth.DXA);
              matterBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              matterTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = matterTable.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFTableCell cell5 = tableCells.get(5);
              XWPFTableCell cell6 = tableCells.get(6);
              XWPFTableCell cell7 = tableCells.get(7);
              XWPFTableCell cell8 = tableCells.get(8);
              XWPFTableCell cell9 = tableCells.get(9);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              XWPFParagraph newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
              XWPFParagraph newPara6 = new XWPFParagraph(cell6.getCTTc().addNewP(), cell6);
              XWPFParagraph newPara7 = new XWPFParagraph(cell7.getCTTc().addNewP(), cell7);
              XWPFParagraph newPara8 = new XWPFParagraph(cell8.getCTTc().addNewP(), cell8);
              XWPFParagraph newPara9 = new XWPFParagraph(cell9.getCTTc().addNewP(), cell9);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              XWPFRun run5 = newPara5.createRun();
              XWPFRun run6 = newPara6.createRun();
              XWPFRun run7 = newPara7.createRun();
              XWPFRun run8 = newPara8.createRun();
              XWPFRun run9 = newPara9.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              newPara5.setAlignment(ParagraphAlignment.CENTER);
              newPara6.setAlignment(ParagraphAlignment.CENTER);
              newPara7.setAlignment(ParagraphAlignment.CENTER);
              newPara8.setAlignment(ParagraphAlignment.CENTER);
              newPara9.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              run5.setBold(true);
              run6.setBold(true);
              run7.setBold(true);
              run8.setBold(true);
              run9.setBold(true);
              //赋值
              run0.setText("事项名称");
              run1.setText("文件标题");  
              run2.setText("文号");
              run3.setText("办理状态");
              run4.setText("办理时间");
              run5.setText("办理时限");
              run6.setText("办理时长");
              run7.setText("审批部门所在行政");
              run8.setText("批复时间");
              run9.setText("批复有效期");
          }
          
        //投资情况表页面设置
          XWPFParagraph investmentInfoTitle = doc.createParagraph();
          // 设置字体对齐方式3
          investmentInfoTitle.setAlignment(ParagraphAlignment.LEFT);
          investmentInfoTitle.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText22=investmentInfoTitle.createRun();
          runText22.setText("投资情况");
          runText22.setFontSize(20);
          //审核备办理事项表
          //判断审核备办理事项是否为空
          if(investmentInfoList.size()>0){
        	  //创建投资情况表
        	  XWPFTable investmentTable = doc.createTable(investmentInfoList.size()+4,15);
              CTTblPr investmentBlPr = investmentTable.getCTTbl().getTblPr();
              investmentBlPr.getTblW().setType(STTblWidth.DXA);
              investmentBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              investmentTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              if("A00001".equals(baseInfoList.get(0).get("ISBOND").toString())){
            	//表头第一行
                  tableCells = investmentTable.getRow(0).getTableCells();
                  XWPFTableCell cell0 = tableCells.get(0);
                  XWPFTableCell cell1 = tableCells.get(1);
                  XWPFTableCell cell2 = tableCells.get(2);
                  XWPFTableCell cell3 = tableCells.get(3);
                  XWPFTableCell cell6 = tableCells.get(6);
                  XWPFTableCell cell7 = tableCells.get(7);
                  XWPFTableCell cell8 = tableCells.get(8);
                  XWPFTableCell cell9 = tableCells.get(9);
                  XWPFTableCell cell10 = tableCells.get(10);
                  XWPFTableCell cell11= tableCells.get(11);
                  XWPFTableCell cell12 = tableCells.get(12);
                  XWPFTableCell cell13= tableCells.get(13);
                  XWPFTableCell cell14 = tableCells.get(14);
                  XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                  XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                  XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
                  XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                  XWPFParagraph newPara6 = new XWPFParagraph(cell6.getCTTc().addNewP(), cell6);
                  XWPFParagraph newPara7 = new XWPFParagraph(cell7.getCTTc().addNewP(), cell7);
                  XWPFParagraph newPara8 = new XWPFParagraph(cell8.getCTTc().addNewP(), cell8);
                  XWPFParagraph newPara9 = new XWPFParagraph(cell9.getCTTc().addNewP(), cell9);
                  XWPFParagraph newPara10 = new XWPFParagraph(cell10.getCTTc().addNewP(), cell10);
                  XWPFParagraph newPara11 = new XWPFParagraph(cell11.getCTTc().addNewP(), cell11);
                  XWPFParagraph newPara12 = new XWPFParagraph(cell12.getCTTc().addNewP(), cell12);
                  XWPFParagraph newPara13 = new XWPFParagraph(cell13.getCTTc().addNewP(), cell13);
                  XWPFParagraph newPara14 = new XWPFParagraph(cell14.getCTTc().addNewP(), cell14);
                  //固定表头内容属性
                  XWPFRun run0 = newPara0.createRun();
                  XWPFRun run1 = newPara1.createRun();
                  XWPFRun run2 = newPara2.createRun();
                  XWPFRun run3 = newPara3.createRun();
                  XWPFRun run6 = newPara6.createRun();
                  XWPFRun run7 = newPara7.createRun();
                  XWPFRun run8 = newPara8.createRun();
                  XWPFRun run9 = newPara9.createRun();
                  XWPFRun run10 = newPara10.createRun();
                  XWPFRun run11 = newPara11.createRun();
                  XWPFRun run12 = newPara12.createRun();
                  XWPFRun run13 = newPara13.createRun();
                  XWPFRun run14 = newPara14.createRun();
                  /** 内容居中显示 **/
                  newPara0.setAlignment(ParagraphAlignment.CENTER);
                  newPara1.setAlignment(ParagraphAlignment.CENTER);
                  newPara2.setAlignment(ParagraphAlignment.CENTER);
                  newPara3.setAlignment(ParagraphAlignment.CENTER);
                  newPara6.setAlignment(ParagraphAlignment.CENTER);
                  newPara7.setAlignment(ParagraphAlignment.CENTER);
                  newPara8.setAlignment(ParagraphAlignment.CENTER);
                  newPara9.setAlignment(ParagraphAlignment.CENTER);
                  newPara10.setAlignment(ParagraphAlignment.CENTER);
                  newPara11.setAlignment(ParagraphAlignment.CENTER);
                  newPara12.setAlignment(ParagraphAlignment.CENTER);
                  newPara13.setAlignment(ParagraphAlignment.CENTER);
                  newPara14.setAlignment(ParagraphAlignment.CENTER);
                  //加粗显示
                  run0.setBold(true);
                  run1.setBold(true);
                  run2.setBold(true);
                  run3.setBold(true);
                  run6.setBold(true);
                  run7.setBold(true);
                  run8.setBold(true);
                  run9.setBold(true);
                  run10.setBold(true);
                  run11.setBold(true);
                  run12.setBold(true);
                  run13.setBold(true);
                  run14.setBold(true);
                  //赋值
                  run0.setText("资金类别");
                  run1.setText("总投资(万元)");  
                  run2.setText("资本金(万元)");
                  run3.setText("专项建设基金资本金缺口(万元)");
                  run6.setText("2016年本次<br>申请专项<br>建设基金");
                  run7.setText("累计下达<br>(安排)资金<br>(万元)");
                  run8.setText("累计完成<br>投资(万元)");
                  run9.setText("建议资金<br>(万元)");
                  run10.setText("投<br>放资金");
                  run11.setText("申报<br>比例");
                  run12.setText("建议<br>比例");
                  run13.setText("投放<br>比例<br>");
                  run14.setText("备注");
                  //表头第二行
                  tableCells = investmentTable.getRow(1).getTableCells();
                   cell3 = tableCells.get(3);
                  XWPFTableCell cell4 = tableCells.get(4);
                  XWPFTableCell cell5 = tableCells.get(5);
                   newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                  XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
                  XWPFParagraph newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
                  //固定表头内容属性
                   run3 = newPara3.createRun();
                  XWPFRun run4 = newPara4.createRun();
                  XWPFRun run5 = newPara5.createRun();
                  /** 内容居中显示 **/
                  newPara3.setAlignment(ParagraphAlignment.CENTER);
                  newPara4.setAlignment(ParagraphAlignment.CENTER);
                  newPara5.setAlignment(ParagraphAlignment.CENTER);
                  //加粗显示
                  run3.setBold(true);
                  run4.setBold(true);
                  run5.setBold(true);
                  run3.setText("2015年");
                  run4.setText("2016年");
                  run5.setText("2017年");
                  //表第三行
                  tableCells = investmentTable.getRow(2).getTableCells();
                  tableCells.get(0).setText("当前年度");
                  tableCells.get(1).setText(baseInfoList.get(0).get("BELONG_YEAR").toString());
                  //表第三行
                  tableCells = investmentTable.getRow(3).getTableCells();
                  tableCells.get(0).setText("是否申请专项建设基金");
                  //是否专项建设基金转换汉字
                  if(baseInfoList.get(0).get("ISBOND").toString().equals("A00001")){
                	  tableCells.get(1).setText("是");
                  }else{
                	  tableCells.get(1).setText("否");
                  }
                  this.mergeCellsVertically(investmentTable, 0, 0, 1);
                  this.mergeCellsVertically(investmentTable, 1, 0, 1);
                  this.mergeCellsVertically(investmentTable, 2, 0, 1);
                  this.mergeCellsHorizontal(investmentTable, 0, 3, 5);
                  this.mergeCellsVertically(investmentTable, 6, 0, 1);
                  this.mergeCellsVertically(investmentTable, 7, 0, 1);
                  this.mergeCellsVertically(investmentTable, 8, 0, 1);
                  this.mergeCellsVertically(investmentTable, 9, 0, 1);
                  this.mergeCellsVertically(investmentTable, 10, 0, 1);
                  this.mergeCellsVertically(investmentTable, 11, 0, 1);
                  this.mergeCellsVertically(investmentTable, 12, 0, 1);
                  this.mergeCellsVertically(investmentTable, 12, 0, 1);
                  this.mergeCellsVertically(investmentTable, 13, 0, 1);
                  this.mergeCellsVertically(investmentTable, 14, 0, 1);
                  this.mergeCellsHorizontal(investmentTable, 2, 1, 13);
                  this.mergeCellsHorizontal(investmentTable, 3, 1, 13);
                  //循环生成表格内容
                  for(int i = 0;i<investmentInfoList.size();i++){
                	  tableCells = investmentTable.getRow(i+4).getTableCells();
                	  if(investmentInfoList.get(i).get("NAME")!=null&&!"".equals(investmentInfoList.get(i).get("NAME"))){
                		  tableCells.get(0).setText(investmentInfoList.get(i).get("NAME").toString());
                	  }else{
                		  tableCells.get(0).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("TOTAL_INVESTMENT")!=null&&!"".equals(investmentInfoList.get(i).get("TOTAL_INVESTMENT"))){
                		  tableCells.get(1).setText(investmentInfoList.get(i).get("TOTAL_INVESTMENT").toString());
                	  }else{
                		  tableCells.get(1).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("CAPTIAL_CASH")!=null&&!"".equals(investmentInfoList.get(i).get("CAPTIAL_CASH"))){
                		  tableCells.get(2).setText(investmentInfoList.get(i).get("CAPTIAL_CASH").toString());
                	  }else{
                		  tableCells.get(2).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("GAP_CAPTIAL_2015")!=null&&!"".equals(investmentInfoList.get(i).get("GAP_CAPTIAL_2015"))){
                		  tableCells.get(3).setText(investmentInfoList.get(i).get("GAP_CAPTIAL_2015").toString());
                	  }else{
                		  tableCells.get(3).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("GAP_CAPTIAL_2016")!=null&&!"".equals(investmentInfoList.get(i).get("GAP_CAPTIAL_2016"))){
                		  tableCells.get(4).setText(investmentInfoList.get(i).get("GAP_CAPTIAL_2016").toString()); 
                	  }else{
                		  tableCells.get(4).setText(null); 
                	  }
                	  if(investmentInfoList.get(i).get("GAP_CAPTIAL_2017")!=null&&!"".equals(investmentInfoList.get(i).get("GAP_CAPTIAL_2017"))){
                		  tableCells.get(5).setText(investmentInfoList.get(i).get("GAP_CAPTIAL_2017").toString());
                	  }else{
                		  tableCells.get(5).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("CUR_APPLY_SPE_CAPTIAL")!=null&&!"".equals(investmentInfoList.get(i).get("CUR_APPLY_SPE_CAPTIAL"))){
                		  tableCells.get(6).setText(investmentInfoList.get(i).get("CUR_APPLY_SPE_CAPTIAL").toString());
                	  }else{
                		  tableCells.get(6).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("TOTAL_ISSUED_CAPTIAL")!=null&&!"".equals(investmentInfoList.get(i).get("TOTAL_ISSUED_CAPTIAL"))){
                		  tableCells.get(7).setText(investmentInfoList.get(i).get("TOTAL_ISSUED_CAPTIAL").toString());
                	  }else{
                		  tableCells.get(7).setText(null);
                	  }
                	  String completeddate = null;
                	  if(investmentInfoList.get(i).get("TOTAL_COMPLETE_CAPTIAL")!=null&&!"".equals(investmentInfoList.get(i).get("TOTAL_COMPLETE_CAPTIAL"))){
                		  completeddate =time1.format(investmentInfoList.get(i).get("TOTAL_COMPLETE_CAPTIAL"));
                	  }
                	  tableCells.get(8).setText(completeddate);
                	  
                	  if(investmentInfoList.get(i).get("sugArrangeCaptial")!=null&&!"".equals(investmentInfoList.get(i).get("sugArrangeCaptial"))){
                		  tableCells.get(9).setText(investmentInfoList.get(i).get("sugArrangeCaptial").toString());
                	  }else{
                		  tableCells.get(9).setText(null);
                	  }
                	  
                	  if(investmentInfoList.get(i).get("putinCaptial")!=null&&!"".equals(investmentInfoList.get(i).get("putinCaptial"))){
                		  tableCells.get(10).setText(investmentInfoList.get(i).get("putinCaptial").toString());
                	  }else{
                		  tableCells.get(10).setText(null);
                	  }
                	  
                	  if(investmentInfoList.get(i).get("declareScale")!=null&&!"".equals(investmentInfoList.get(i).get("declareScale"))){
                		  tableCells.get(11).setText(investmentInfoList.get(i).get("declareScale").toString());
                	  }else{
                		  tableCells.get(11).setText(null);
                	  }
                	  
                	  if(investmentInfoList.get(i).get("sugScale")!=null&&!"".equals(investmentInfoList.get(i).get("sugScale"))){
                		  tableCells.get(12).setText(investmentInfoList.get(i).get("sugScale").toString());
                	  }else{
                		  tableCells.get(12).setText(null);
                	  }
                	  
                	  if(investmentInfoList.get(i).get("putinScale")!=null&&!"".equals(investmentInfoList.get(i).get("putinScale"))){
                		  tableCells.get(13).setText(investmentInfoList.get(i).get("putinScale").toString());
                	  }else{
                		  tableCells.get(13).setText(null);
                	  }
                	  
                	  if(investmentInfoList.get(i).get("REMARKS")!=null&&!"".equals(investmentInfoList.get(i).get("REMARKS"))){
                		  tableCells.get(13).setText(investmentInfoList.get(i).get("REMARKS").toString());
                	  }else{
                		  tableCells.get(13).setText(null);
                	  }
                  }
              }else{
            	//表头第一行
                  tableCells = investmentTable.getRow(0).getTableCells();
                  XWPFTableCell cell0 = tableCells.get(0);
                  XWPFTableCell cell1 = tableCells.get(1);
                  XWPFTableCell cell2 = tableCells.get(2);
                  XWPFTableCell cell3 = tableCells.get(3);
                  XWPFTableCell cell4 = tableCells.get(4);
                  XWPFTableCell cell5 = tableCells.get(5);
                  XWPFTableCell cell9 = tableCells.get(9);
                  XWPFTableCell cell10 = tableCells.get(10);
                  XWPFTableCell cell11= tableCells.get(11);
                  XWPFTableCell cell12 = tableCells.get(12);
                  XWPFTableCell cell13= tableCells.get(13);
                  XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                  XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                  XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
                  XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                  XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
                  XWPFParagraph newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
                  XWPFParagraph newPara9 = new XWPFParagraph(cell9.getCTTc().addNewP(), cell9);
                  XWPFParagraph newPara10 = new XWPFParagraph(cell10.getCTTc().addNewP(), cell10);
                  XWPFParagraph newPara11 = new XWPFParagraph(cell11.getCTTc().addNewP(), cell11);
                  XWPFParagraph newPara12 = new XWPFParagraph(cell12.getCTTc().addNewP(), cell12);
                  XWPFParagraph newPara13 = new XWPFParagraph(cell13.getCTTc().addNewP(), cell13);
                  //固定表头内容属性
                  XWPFRun run0 = newPara0.createRun();
                  XWPFRun run1 = newPara1.createRun();
                  XWPFRun run2 = newPara2.createRun();
                  XWPFRun run3 = newPara3.createRun();
                  XWPFRun run4 = newPara4.createRun();
                  XWPFRun run5 = newPara5.createRun();
                  XWPFRun run9 = newPara9.createRun();
                  XWPFRun run10 = newPara10.createRun();
                  XWPFRun run11 = newPara11.createRun();
                  XWPFRun run12 = newPara12.createRun();
                  XWPFRun run13 = newPara13.createRun();
                  /** 内容居中显示 **/
                  newPara0.setAlignment(ParagraphAlignment.CENTER);
                  newPara1.setAlignment(ParagraphAlignment.CENTER);
                  newPara2.setAlignment(ParagraphAlignment.CENTER);
                  newPara3.setAlignment(ParagraphAlignment.CENTER);
                  newPara4.setAlignment(ParagraphAlignment.CENTER);
                  newPara5.setAlignment(ParagraphAlignment.CENTER);
                  newPara9.setAlignment(ParagraphAlignment.CENTER);
                  newPara10.setAlignment(ParagraphAlignment.CENTER);
                  newPara11.setAlignment(ParagraphAlignment.CENTER);
                  newPara12.setAlignment(ParagraphAlignment.CENTER);
                  newPara13.setAlignment(ParagraphAlignment.CENTER);
                  //加粗显示
                  run0.setBold(true);
                  run1.setBold(true);
                  run2.setBold(true);
                  run3.setBold(true);
                  run4.setBold(true);
                  run5.setBold(true);
                  run9.setBold(true);
                  run10.setBold(true);
                  run11.setBold(true);
                  run12.setBold(true);
                  run13.setBold(true);
                  //赋值
                  run0.setText("资金类别");
                  run1.setText("总投资(万元)");  
                  run2.setText("资本金(万元)");
                  run3.setText("累计下达(安排)资金(万元)");
                  run4.setText("累计完成投资(万元)");
                  run5.setText("资金需求(万元)");
                  run9.setText("本次下达资金");
                  run10.setText("申报比例");
                  run11.setText("建议比例");
                  run12.setText("投放比例");
                  run13.setText("备注");
                  //表头第二行
                  tableCells = investmentTable.getRow(1).getTableCells();
                   cell5 = tableCells.get(5);
                  XWPFTableCell cell6 = tableCells.get(6);
                  XWPFTableCell cell7 = tableCells.get(7);
                  XWPFTableCell cell8 = tableCells.get(8);
                   newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
                  XWPFParagraph newPara6 = new XWPFParagraph(cell6.getCTTc().addNewP(), cell6);
                  XWPFParagraph newPara7 = new XWPFParagraph(cell7.getCTTc().addNewP(), cell7);
                  XWPFParagraph newPara8 = new XWPFParagraph(cell8.getCTTc().addNewP(), cell8);
                  //固定表头内容属性
                   run5 = newPara5.createRun();
                  XWPFRun run6 = newPara6.createRun();
                  XWPFRun run7 = newPara7.createRun();
                  XWPFRun run8 = newPara8.createRun();
                  /** 内容居中显示 **/
                  newPara5.setAlignment(ParagraphAlignment.CENTER);
                  newPara6.setAlignment(ParagraphAlignment.CENTER);
                  newPara7.setAlignment(ParagraphAlignment.CENTER);
                  newPara8.setAlignment(ParagraphAlignment.CENTER);
                  //加粗显示
                  run5.setBold(true);
                  run6.setBold(true);
                  run7.setBold(true);
                  run8.setBold(true);
                  //赋值
                  run5.setText("合计");
                  run6.setText("2016年");
                  run7.setText("2017年");
                  run8.setText("2018年");
                  //表第三行
                  tableCells = investmentTable.getRow(2).getTableCells();
                  tableCells.get(0).setText("当前年度");
                  tableCells.get(1).setText(baseInfoList.get(0).get("BELONG_YEAR").toString());
                  //表第三行
                  tableCells = investmentTable.getRow(3).getTableCells();
                  tableCells.get(0).setText("是否申请专项建设基金");
                //是否专项建设基金转换汉字
                  if(baseInfoList.get(0).get("ISBOND").toString().equals("A00001")){
                	  tableCells.get(1).setText("是");
                  }else{
                	  tableCells.get(1).setText("否");
                  }
                  this.mergeCellsVertically(investmentTable, 0, 0, 1);
                  this.mergeCellsVertically(investmentTable, 1, 0, 1);
                  this.mergeCellsVertically(investmentTable, 2, 0, 1);
                  this.mergeCellsVertically(investmentTable, 3, 0, 1);
                  this.mergeCellsVertically(investmentTable, 4, 0, 1);
                  this.mergeCellsHorizontal(investmentTable, 0, 5, 8);
                  this.mergeCellsVertically(investmentTable, 9, 0, 1);
                  this.mergeCellsVertically(investmentTable, 10, 0, 1);
                  this.mergeCellsVertically(investmentTable, 11, 0, 1);
                  this.mergeCellsVertically(investmentTable, 12, 0, 1);
                  this.mergeCellsVertically(investmentTable, 12, 0, 1);
                  this.mergeCellsVertically(investmentTable, 13, 0, 1);
                  this.mergeCellsHorizontal(investmentTable, 2, 1, 13);
                  this.mergeCellsHorizontal(investmentTable, 3, 1, 13);
                  //循环生成表格内容
                  for(int i = 0;i<investmentInfoList.size();i++){
                	  tableCells = investmentTable.getRow(i+4).getTableCells();
                	  if(investmentInfoList.get(i).get("NAME")!=null&&!"".equals(investmentInfoList.get(i).get("NAME"))){
                		  tableCells.get(0).setText(investmentInfoList.get(i).get("NAME").toString());
                	  }else{
                		  tableCells.get(0).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("TOTAL_INVESTMENT")!=null&&!"".equals(investmentInfoList.get(i).get("TOTAL_INVESTMENT"))){
                		  tableCells.get(1).setText(investmentInfoList.get(i).get("TOTAL_INVESTMENT").toString());
                	  }else{
                		  tableCells.get(1).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("CAPTIAL_CASH")!=null&&!"".equals(investmentInfoList.get(i).get("CAPTIAL_CASH"))){
                		  tableCells.get(2).setText(investmentInfoList.get(i).get("CAPTIAL_CASH").toString());
                	  }else{
                		  tableCells.get(2).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("TOTAL_ISSUED_CAPTIAL")!=null&&!"".equals(investmentInfoList.get(i).get("TOTAL_ISSUED_CAPTIAL"))){
                		  tableCells.get(3).setText(investmentInfoList.get(i).get("TOTAL_ISSUED_CAPTIAL").toString());
                	  }else{
                		  tableCells.get(3).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("TOTAL_COMPLETE_CAPTIAL")!=null&&!"".equals(investmentInfoList.get(i).get("TOTAL_COMPLETE_CAPTIAL"))){
                		  tableCells.get(4).setText(investmentInfoList.get(i).get("TOTAL_COMPLETE_CAPTIAL").toString()); 
                	  }else{
                		  tableCells.get(4).setText(null); 
                	  }
                	  if(investmentInfoList.get(i).get("TOTAL_INVESTMENT")!=null&&!"".equals(investmentInfoList.get(i).get("TOTAL_INVESTMENT"))){
                		  tableCells.get(5).setText(investmentInfoList.get(i).get("TOTAL_INVESTMENT").toString());
                	  }else{
                		  tableCells.get(5).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("APPLY_CAPTIAL_2016")!=null&&!"".equals(investmentInfoList.get(i).get("APPLY_CAPTIAL_2016"))){
                		  tableCells.get(6).setText(investmentInfoList.get(i).get("APPLY_CAPTIAL_2016").toString());
                	  }else{
                		  tableCells.get(6).setText(null);
                	  }
                	  if(investmentInfoList.get(i).get("APPLY_CAPTIAL_2017")!=null&&!"".equals(investmentInfoList.get(i).get("APPLY_CAPTIAL_2017"))){
                		  tableCells.get(7).setText(investmentInfoList.get(i).get("APPLY_CAPTIAL_2017").toString());
                	  }else{
                		  tableCells.get(7).setText(null);
                	  }
                	  String completeddate = null;
                	  if(investmentInfoList.get(i).get("APPLY_CAPTIAL_2018")!=null&&!"".equals(investmentInfoList.get(i).get("APPLY_CAPTIAL_2018"))){
                		  completeddate =time1.format(investmentInfoList.get(i).get("APPLY_CAPTIAL_2018"));
                	  }
                	  tableCells.get(8).setText(completeddate);
                	  
                	  if(investmentInfoList.get(i).get("CUR_PERIOD_ALLOCATED")!=null&&!"".equals(investmentInfoList.get(i).get("CUR_PERIOD_ALLOCATED"))){
                		  tableCells.get(9).setText(investmentInfoList.get(i).get("CUR_PERIOD_ALLOCATED").toString());
                	  }else{
                		  tableCells.get(9).setText(null);
                	  }
                	  
                	  if(investmentInfoList.get(i).get("declareScale")!=null&&!"".equals(investmentInfoList.get(i).get("declareScale"))){
                		  tableCells.get(10).setText(investmentInfoList.get(i).get("declareScale").toString());
                	  }else{
                		  tableCells.get(10).setText(null);
                	  }
                	  
                	  if(investmentInfoList.get(i).get("sugScale")!=null&&!"".equals(investmentInfoList.get(i).get("sugScale"))){
                		  tableCells.get(11).setText(investmentInfoList.get(i).get("sugScale").toString());
                	  }else{
                		  tableCells.get(11).setText(null);
                	  }
                	  
                	  if(investmentInfoList.get(i).get("putinScale")!=null&&!"".equals(investmentInfoList.get(i).get("putinScale"))){
                		  tableCells.get(12).setText(investmentInfoList.get(i).get("putinScale").toString());
                	  }else{
                		  tableCells.get(12).setText(null);
                	  }
                	  
                	  if(investmentInfoList.get(i).get("REMARKS")!=null&&!"".equals(investmentInfoList.get(i).get("REMARKS"))){
                		  tableCells.get(13).setText(investmentInfoList.get(i).get("REMARKS").toString());
                	  }else{
                		  tableCells.get(13).setText(null);
                	  }
                  }
              }
          }else{
        	  //创建投资情况表表
        	  XWPFTable investmentTable = doc.createTable(4,15);
              CTTblPr investmentBlPr = investmentTable.getCTTbl().getTblPr();
              investmentBlPr.getTblW().setType(STTblWidth.DXA);
              investmentBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              investmentTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
        	  if("A00001".equals(baseInfoList.get(0).get("ISBOND").toString())){
        		//表头第一行
                  tableCells = investmentTable.getRow(0).getTableCells();
                  XWPFTableCell cell0 = tableCells.get(0);
                  XWPFTableCell cell1 = tableCells.get(1);
                  XWPFTableCell cell2 = tableCells.get(2);
                  XWPFTableCell cell3 = tableCells.get(3);
                  XWPFTableCell cell6 = tableCells.get(6);
                  XWPFTableCell cell7 = tableCells.get(7);
                  XWPFTableCell cell8 = tableCells.get(8);
                  XWPFTableCell cell9 = tableCells.get(9);
                  XWPFTableCell cell10 = tableCells.get(10);
                  XWPFTableCell cell11= tableCells.get(11);
                  XWPFTableCell cell12 = tableCells.get(12);
                  XWPFTableCell cell13= tableCells.get(13);
                  XWPFTableCell cell14 = tableCells.get(14);
                  XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                  XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                  XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
                  XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                  XWPFParagraph newPara6 = new XWPFParagraph(cell6.getCTTc().addNewP(), cell6);
                  XWPFParagraph newPara7 = new XWPFParagraph(cell7.getCTTc().addNewP(), cell7);
                  XWPFParagraph newPara8 = new XWPFParagraph(cell8.getCTTc().addNewP(), cell8);
                  XWPFParagraph newPara9 = new XWPFParagraph(cell9.getCTTc().addNewP(), cell9);
                  XWPFParagraph newPara10 = new XWPFParagraph(cell10.getCTTc().addNewP(), cell10);
                  XWPFParagraph newPara11 = new XWPFParagraph(cell11.getCTTc().addNewP(), cell11);
                  XWPFParagraph newPara12 = new XWPFParagraph(cell12.getCTTc().addNewP(), cell12);
                  XWPFParagraph newPara13 = new XWPFParagraph(cell13.getCTTc().addNewP(), cell13);
                  XWPFParagraph newPara14 = new XWPFParagraph(cell14.getCTTc().addNewP(), cell14);
                  //固定表头内容属性
                  XWPFRun run0 = newPara0.createRun();
                  XWPFRun run1 = newPara1.createRun();
                  XWPFRun run2 = newPara2.createRun();
                  XWPFRun run3 = newPara3.createRun();
                  XWPFRun run6 = newPara6.createRun();
                  XWPFRun run7 = newPara7.createRun();
                  XWPFRun run8 = newPara8.createRun();
                  XWPFRun run9 = newPara9.createRun();
                  XWPFRun run10 = newPara10.createRun();
                  XWPFRun run11 = newPara11.createRun();
                  XWPFRun run12 = newPara12.createRun();
                  XWPFRun run13 = newPara13.createRun();
                  XWPFRun run14 = newPara14.createRun();
                  /** 内容居中显示 **/
                  newPara0.setAlignment(ParagraphAlignment.CENTER);
                  newPara1.setAlignment(ParagraphAlignment.CENTER);
                  newPara2.setAlignment(ParagraphAlignment.CENTER);
                  newPara3.setAlignment(ParagraphAlignment.CENTER);
                  newPara6.setAlignment(ParagraphAlignment.CENTER);
                  newPara7.setAlignment(ParagraphAlignment.CENTER);
                  newPara8.setAlignment(ParagraphAlignment.CENTER);
                  newPara9.setAlignment(ParagraphAlignment.CENTER);
                  newPara10.setAlignment(ParagraphAlignment.CENTER);
                  newPara11.setAlignment(ParagraphAlignment.CENTER);
                  newPara12.setAlignment(ParagraphAlignment.CENTER);
                  newPara13.setAlignment(ParagraphAlignment.CENTER);
                  newPara14.setAlignment(ParagraphAlignment.CENTER);
                  //加粗显示
                  run0.setBold(true);
                  run1.setBold(true);
                  run2.setBold(true);
                  run3.setBold(true);
                  run6.setBold(true);
                  run7.setBold(true);
                  run8.setBold(true);
                  run9.setBold(true);
                  run10.setBold(true);
                  run11.setBold(true);
                  run12.setBold(true);
                  run13.setBold(true);
                  run14.setBold(true);
                  //赋值
                  run0.setText("资金类别");
                  run1.setText("总投资(万元)");  
                  run2.setText("资本金(万元)");
                  run3.setText("专项建设基金资本金缺口(万元)");
                  run6.setText("2016年本次<br>申请专项<br>建设基金");
                  run7.setText("累计下达<br>(安排)资金<br>(万元)");
                  run8.setText("累计完成<br>投资(万元)");
                  run9.setText("建议资金<br>(万元)");
                  run10.setText("投<br>放资金");
                  run11.setText("申报<br>比例");
                  run12.setText("建议<br>比例");
                  run13.setText("投放<br>比例<br>");
                  run14.setText("备注");
                  //表头第二行
                  tableCells = investmentTable.getRow(1).getTableCells();
                   cell3 = tableCells.get(3);
                  XWPFTableCell cell4 = tableCells.get(4);
                  XWPFTableCell cell5 = tableCells.get(5);
                   newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                  XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
                  XWPFParagraph newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
                  //固定表头内容属性
                   run3 = newPara3.createRun();
                  XWPFRun run4 = newPara4.createRun();
                  XWPFRun run5 = newPara5.createRun();
                  /** 内容居中显示 **/
                  newPara3.setAlignment(ParagraphAlignment.CENTER);
                  newPara4.setAlignment(ParagraphAlignment.CENTER);
                  newPara5.setAlignment(ParagraphAlignment.CENTER);
                  //加粗显示
                  run3.setBold(true);
                  run4.setBold(true);
                  run5.setBold(true);
                  run3.setText("2015年");
                  run4.setText("2016年");
                  run5.setText("2017年");
                    //表第三行
                    tableCells = investmentTable.getRow(2).getTableCells();
                    tableCells.get(0).setText("当前年度");
                    tableCells.get(1).setText(baseInfoList.get(0).get("BELONG_YEAR").toString());
                    //表第三行
                    tableCells = investmentTable.getRow(3).getTableCells();
                    tableCells.get(0).setText("是否申请专项建设基金");
                  //是否专项建设基金转换汉字
                    if(baseInfoList.get(0).get("ISBOND").toString().equals("A00001")){
                  	  tableCells.get(1).setText("是");
                    }else{
                  	  tableCells.get(1).setText("否");
                    }
                    this.mergeCellsVertically(investmentTable, 0, 0, 1);
                    this.mergeCellsVertically(investmentTable, 1, 0, 1);
                    this.mergeCellsVertically(investmentTable, 2, 0, 1);
                    this.mergeCellsHorizontal(investmentTable, 0, 3, 5);
                    this.mergeCellsVertically(investmentTable, 6, 0, 1);
                    this.mergeCellsVertically(investmentTable, 7, 0, 1);
                    this.mergeCellsVertically(investmentTable, 8, 0, 1);
                    this.mergeCellsVertically(investmentTable, 9, 0, 1);
                    this.mergeCellsVertically(investmentTable, 10, 0, 1);
                    this.mergeCellsVertically(investmentTable, 11, 0, 1);
                    this.mergeCellsVertically(investmentTable, 12, 0, 1);
                    this.mergeCellsVertically(investmentTable, 12, 0, 1);
                    this.mergeCellsVertically(investmentTable, 13, 0, 1);
                    this.mergeCellsVertically(investmentTable, 14, 0, 1);
                    this.mergeCellsHorizontal(investmentTable, 2, 1, 13);
                    this.mergeCellsHorizontal(investmentTable, 3, 1, 13);
                    //循环生成表格内容
                }else{
                	//表头第一行
                    tableCells = investmentTable.getRow(0).getTableCells();
                    XWPFTableCell cell0 = tableCells.get(0);
                    XWPFTableCell cell1 = tableCells.get(1);
                    XWPFTableCell cell2 = tableCells.get(2);
                    XWPFTableCell cell3 = tableCells.get(3);
                    XWPFTableCell cell4 = tableCells.get(4);
                    XWPFTableCell cell5 = tableCells.get(5);
                    XWPFTableCell cell9 = tableCells.get(9);
                    XWPFTableCell cell10 = tableCells.get(10);
                    XWPFTableCell cell11= tableCells.get(11);
                    XWPFTableCell cell12 = tableCells.get(12);
                    XWPFTableCell cell13= tableCells.get(13);
                    XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                    XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                    XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
                    XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                    XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
                    XWPFParagraph newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
                    XWPFParagraph newPara9 = new XWPFParagraph(cell9.getCTTc().addNewP(), cell9);
                    XWPFParagraph newPara10 = new XWPFParagraph(cell10.getCTTc().addNewP(), cell10);
                    XWPFParagraph newPara11 = new XWPFParagraph(cell11.getCTTc().addNewP(), cell11);
                    XWPFParagraph newPara12 = new XWPFParagraph(cell12.getCTTc().addNewP(), cell12);
                    XWPFParagraph newPara13 = new XWPFParagraph(cell13.getCTTc().addNewP(), cell13);
                    //固定表头内容属性
                    XWPFRun run0 = newPara0.createRun();
                    XWPFRun run1 = newPara1.createRun();
                    XWPFRun run2 = newPara2.createRun();
                    XWPFRun run3 = newPara3.createRun();
                    XWPFRun run4 = newPara4.createRun();
                    XWPFRun run5 = newPara5.createRun();
                    XWPFRun run9 = newPara9.createRun();
                    XWPFRun run10 = newPara10.createRun();
                    XWPFRun run11 = newPara11.createRun();
                    XWPFRun run12 = newPara12.createRun();
                    XWPFRun run13 = newPara13.createRun();
                    /** 内容居中显示 **/
                    newPara0.setAlignment(ParagraphAlignment.CENTER);
                    newPara1.setAlignment(ParagraphAlignment.CENTER);
                    newPara2.setAlignment(ParagraphAlignment.CENTER);
                    newPara3.setAlignment(ParagraphAlignment.CENTER);
                    newPara4.setAlignment(ParagraphAlignment.CENTER);
                    newPara5.setAlignment(ParagraphAlignment.CENTER);
                    newPara9.setAlignment(ParagraphAlignment.CENTER);
                    newPara10.setAlignment(ParagraphAlignment.CENTER);
                    newPara11.setAlignment(ParagraphAlignment.CENTER);
                    newPara12.setAlignment(ParagraphAlignment.CENTER);
                    newPara13.setAlignment(ParagraphAlignment.CENTER);
                    //加粗显示
                    run0.setBold(true);
                    run1.setBold(true);
                    run2.setBold(true);
                    run3.setBold(true);
                    run4.setBold(true);
                    run5.setBold(true);
                    run9.setBold(true);
                    run10.setBold(true);
                    run11.setBold(true);
                    run12.setBold(true);
                    run13.setBold(true);
                    //赋值
                    run0.setText("资金类别");
                    run1.setText("总投资(万元)");  
                    run2.setText("资本金(万元)");
                    run3.setText("累计下达(安排)资金(万元)");
                    run4.setText("累计完成投资(万元)");
                    run5.setText("资金需求(万元)");
                    run9.setText("本次下达资金");
                    run10.setText("申报比例");
                    run11.setText("建议比例");
                    run12.setText("投放比例");
                    run13.setText("备注");
                    //表头第二行
                    tableCells = investmentTable.getRow(1).getTableCells();
                     cell5 = tableCells.get(5);
                    XWPFTableCell cell6 = tableCells.get(6);
                    XWPFTableCell cell7 = tableCells.get(7);
                    XWPFTableCell cell8 = tableCells.get(8);
                     newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
                    XWPFParagraph newPara6 = new XWPFParagraph(cell6.getCTTc().addNewP(), cell6);
                    XWPFParagraph newPara7 = new XWPFParagraph(cell7.getCTTc().addNewP(), cell7);
                    XWPFParagraph newPara8 = new XWPFParagraph(cell8.getCTTc().addNewP(), cell8);
                    //固定表头内容属性
                     run5 = newPara5.createRun();
                    XWPFRun run6 = newPara6.createRun();
                    XWPFRun run7 = newPara7.createRun();
                    XWPFRun run8 = newPara8.createRun();
                    /** 内容居中显示 **/
                    newPara5.setAlignment(ParagraphAlignment.CENTER);
                    newPara6.setAlignment(ParagraphAlignment.CENTER);
                    newPara7.setAlignment(ParagraphAlignment.CENTER);
                    newPara8.setAlignment(ParagraphAlignment.CENTER);
                    //加粗显示
                    run5.setBold(true);
                    run6.setBold(true);
                    run7.setBold(true);
                    run8.setBold(true);
                    //赋值
                    run5.setText("合计");
                    run6.setText("2016年");
                    run7.setText("2017年");
                    run8.setText("2018年");
                    //表第三行
                    tableCells = investmentTable.getRow(2).getTableCells();
                    tableCells.get(0).setText("当前年度");
                    tableCells.get(1).setText(baseInfoList.get(0).get("BELONG_YEAR").toString());
                    //表第三行
                    tableCells = investmentTable.getRow(3).getTableCells();
                    tableCells.get(0).setText("是否申请专项建设基金");
                  //是否专项建设基金转换汉字
                    if(baseInfoList.get(0).get("ISBOND").toString().equals("A00001")){
                  	  tableCells.get(1).setText("是");
                    }else{
                  	  tableCells.get(1).setText("否");
                    }
                    this.mergeCellsVertically(investmentTable, 0, 0, 1);
                    this.mergeCellsVertically(investmentTable, 1, 0, 1);
                    this.mergeCellsVertically(investmentTable, 2, 0, 1);
                    this.mergeCellsVertically(investmentTable, 3, 0, 1);
                    this.mergeCellsVertically(investmentTable, 4, 0, 1);
                    this.mergeCellsHorizontal(investmentTable, 0, 5, 8);
                    this.mergeCellsVertically(investmentTable, 9, 0, 1);
                    this.mergeCellsVertically(investmentTable, 10, 0, 1);
                    this.mergeCellsVertically(investmentTable, 11, 0, 1);
                    this.mergeCellsVertically(investmentTable, 12, 0, 1);
                    this.mergeCellsVertically(investmentTable, 12, 0, 1);
                    this.mergeCellsVertically(investmentTable, 13, 0, 1);
                    this.mergeCellsHorizontal(investmentTable, 2, 1, 13);
                    this.mergeCellsHorizontal(investmentTable, 3, 1, 13);
                    //循环生成表格内容
                }
          }
          
          //计划下达情况
          XWPFParagraph issuedInfoTitle = doc.createParagraph();
          issuedInfoTitle.setPageBreak(true);
          // 设置字体对齐方式3
          issuedInfoTitle.setAlignment(ParagraphAlignment.LEFT);
          issuedInfoTitle.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText23=issuedInfoTitle.createRun();
          runText23.setText("计划下达情况");
          runText23.setFontSize(20);
          //计划下达情况表
          //判断计划下达情况是否为空
          if(issuedInfo.size()>0){
        	  //创建计划下达情况表
        	  XWPFTable issuedInfoTable = doc.createTable(issuedInfo.size()+1,6);
              CTTblPr issuedInfoBlPr = issuedInfoTable.getCTTbl().getTblPr();
              issuedInfoBlPr.getTblW().setType(STTblWidth.DXA);
              issuedInfoBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              issuedInfoTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = issuedInfoTable.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFTableCell cell5 = tableCells.get(5);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              XWPFParagraph newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              XWPFRun run5 = newPara5.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              newPara5.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              run5.setBold(true);
              //赋值
              run0.setText("文号");
              run1.setText("标题");  
              run2.setText("下达/投放资金"); 
              run3.setText("司局");
              run4.setText("下达/投放时间");  
              run5.setText("是否打捆项目"); 
              //循环生成表格内容
              for(int i = 0;i<issuedInfo.size();i++){
            	  tableCells = issuedInfoTable.getRow(i+1).getTableCells();
            	  if(issuedInfo.get(i).get("EXPORT_FILE_NO")!=null&&!"".equals(issuedInfo.get(i).get("EXPORT_FILE_NO"))){
            		  tableCells.get(0).setText(issuedInfo.get(i).get("EXPORT_FILE_NO").toString());
            	  }else{
            		  tableCells.get(0).setText(null);
            	  }
            	  if(issuedInfo.get(i).get("FILE_NAME")!=null&&!"".equals(issuedInfo.get(i).get("FILE_NAME"))){
            		  tableCells.get(1).setText(issuedInfo.get(i).get("FILE_NAME").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
            	  if(issuedInfo.get(i).get("ISSUED_MONEY")!=null&&!"".equals(issuedInfo.get(i).get("ISSUED_MONEY"))){
            		  tableCells.get(2).setText(issuedInfo.get(i).get("ISSUED_MONEY").toString());
            	  }else{
            		  tableCells.get(2).setText(null);
            	  }
            	  if(issuedInfo.get(i).get("SIJU")!=null&&!"".equals(issuedInfo.get(i).get("SIJU"))){
            		  tableCells.get(3).setText(issuedInfo.get(i).get("SIJU").toString());
            	  }else{
            		  tableCells.get(3).setText(null);
            	  }
            	  String issusedTime = null;
            	  if(issuedInfo.get(i).get("ISSUSED_TIME")!=null&&!"".equals(issuedInfo.get(i).get("ISSUSED_TIME"))){
            		  issusedTime =time1.format(issuedInfo.get(i).get("ISSUSED_TIME"));
            	  }
            	  tableCells.get(4).setText(issusedTime); 
            	  if(issuedInfo.get(i).get("IS_BUNDLED")!=null&&!"".equals(issuedInfo.get(i).get("IS_BUNDLED"))){
            		  tableCells.get(5).setText(issuedInfo.get(i).get("IS_BUNDLED").toString());
            	  }else{
            		  tableCells.get(5).setText(null);
            	  }
              }
          }else{
        	//创建计划下达情况表
        	  XWPFTable issuedInfoTable = doc.createTable(issuedInfo.size()+1,6);
              CTTblPr issuedInfoBlPr = issuedInfoTable.getCTTbl().getTblPr();
              issuedInfoBlPr.getTblW().setType(STTblWidth.DXA);
              issuedInfoBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              issuedInfoTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = issuedInfoTable.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFTableCell cell5 = tableCells.get(5);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              XWPFParagraph newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              XWPFRun run5 = newPara5.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              newPara5.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              run5.setBold(true);
              //赋值
              run0.setText("文号");
              run1.setText("标题");  
              run2.setText("下达/投放资金"); 
              run3.setText("司局");
              run4.setText("下达/投放时间");  
              run5.setText("是否打捆项目"); 
          }
          
        //资金到位完成情况
          XWPFParagraph finishInfoTitle = doc.createParagraph();
          // 设置字体对齐方式3
          finishInfoTitle.setAlignment(ParagraphAlignment.LEFT);
          finishInfoTitle.setVerticalAlignment(TextAlignment.TOP);
          XWPFRun runText24=finishInfoTitle.createRun();
          runText24.setText("资金到位完成情况");
          runText24.setFontSize(20);
          //资金到位完成情况表
          //判断资金到位完成情况是否为空
          if(finishInfoList.size()>0){
        	  //创建计划下达情况表
        	  XWPFTable finishInfoTable = doc.createTable(finishInfoList.size()+1,5);
              CTTblPr issuedInfoBlPr = finishInfoTable.getCTTbl().getTblPr();
              issuedInfoBlPr.getTblW().setType(STTblWidth.DXA);
              issuedInfoBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              finishInfoTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = finishInfoTable.getRow(0).getTableCells();
              XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              //赋值
              run0.setText("时间");
              run1.setText("下达资金");  
              run2.setText("累计到位资金"); 
              run3.setText("累计完成资金");
              run4.setText("累计支付");  
              //循环生成表格内容
              for(int i = 0;i<finishInfoList.size();i++){
            	  tableCells = finishInfoTable.getRow(i+1).getTableCells();
            	  if(finishInfoList.get(i).get("REPORT_NUMBER")!=null&&!"".equals(finishInfoList.get(i).get("REPORT_NUMBER"))){
            		  tableCells.get(0).setText(finishInfoList.get(i).get("REPORT_NUMBER").toString());
            	  }else{
            		  tableCells.get(0).setText(null);
            	  }
            	  if(finishInfoList.get(i).get("SUMMONEY")!=null&&!"".equals(finishInfoList.get(i).get("SUMMONEY"))){
            		  tableCells.get(1).setText(finishInfoList.get(i).get("SUMMONEY").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
            	  if(finishInfoList.get(i).get("PUTMONEY")!=null&&!"".equals(finishInfoList.get(i).get("PUTMONEY"))){
            		  tableCells.get(2).setText(finishInfoList.get(i).get("PUTMONEY").toString());
            	  }else{
            		  tableCells.get(2).setText(null);
            	  }
            	  if(finishInfoList.get(i).get("COMMONEY")!=null&&!"".equals(finishInfoList.get(i).get("COMMONEY"))){
            		  tableCells.get(3).setText(finishInfoList.get(i).get("COMMONEY").toString());
            	  }else{
            		  tableCells.get(3).setText(null);
            	  }
            	  if(finishInfoList.get(i).get("PAYMONEY")!=null&&!"".equals(finishInfoList.get(i).get("PAYMONEY"))){
            		  tableCells.get(4).setText(finishInfoList.get(i).get("PAYMONEY").toString());
            	  }else{
            		  tableCells.get(4).setText(null);
            	  }
              }
          }else{
        	  //创建计划下达情况表
        	  XWPFTable finishInfoTable = doc.createTable(finishInfoList.size()+1,5);
              CTTblPr issuedInfoBlPr = finishInfoTable.getCTTbl().getTblPr();
              issuedInfoBlPr.getTblW().setType(STTblWidth.DXA);
              issuedInfoBlPr.getTblW().setW(new BigInteger("9000"));
              // 设置上下左右四个方向的距离，可以将表格撑大
              finishInfoTable.setCellMargins(20, 20, 20, 20);
              //信息表表头赋值
              tableCells = finishInfoTable.getRow(0).getTableCells();
        	  XWPFTableCell cell0 = tableCells.get(0);
              XWPFTableCell cell1 = tableCells.get(1);
              XWPFTableCell cell2 = tableCells.get(2);
              XWPFTableCell cell3 = tableCells.get(3);
              XWPFTableCell cell4 = tableCells.get(4);
              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
              XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
              XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
              //固定表头内容属性
              XWPFRun run0 = newPara0.createRun();
              XWPFRun run1 = newPara1.createRun();
              XWPFRun run2 = newPara2.createRun();
              XWPFRun run3 = newPara3.createRun();
              XWPFRun run4 = newPara4.createRun();
              /** 内容居中显示 **/
              newPara0.setAlignment(ParagraphAlignment.CENTER);
              newPara1.setAlignment(ParagraphAlignment.CENTER);
              newPara2.setAlignment(ParagraphAlignment.CENTER);
              newPara3.setAlignment(ParagraphAlignment.CENTER);
              newPara4.setAlignment(ParagraphAlignment.CENTER);
              //加粗显示
              run0.setBold(true);
              run1.setBold(true);
              run2.setBold(true);
              run3.setBold(true);
              run4.setBold(true);
              //赋值
              run0.setText("时间");
              run1.setText("下达资金");  
              run2.setText("累计到位资金"); 
              run3.setText("累计完成资金");
              run4.setText("累计支付");   
          }
          //调度信息拼接
          //现判断调度信息是否存在
          if(dispatchInfoList.size()>0){
        	  //调度信息存在拼接调度
        	  for(int i=0;i<dispatchInfoList.size();i++){
        		  //调度信息
                  XWPFParagraph dispatchInfoTitle = doc.createParagraph();
                  dispatchInfoTitle.setPageBreak(true);
                  // 设置字体对齐方式3
                  dispatchInfoTitle.setAlignment(ParagraphAlignment.LEFT);
                  dispatchInfoTitle.setVerticalAlignment(TextAlignment.TOP);
                  XWPFRun runText25=dispatchInfoTitle.createRun();
                  runText25.setFontSize(20);
                  if(i>=1){
                	  runText25.setText("项目调度-往月报告期");
                	  //期号
                	  XWPFParagraph dispatchInfoTitle2 = doc.createParagraph();
                      // 设置字体对齐方式3
                      dispatchInfoTitle2.setAlignment(ParagraphAlignment.LEFT);
                      dispatchInfoTitle2.setVerticalAlignment(TextAlignment.TOP);
                      XWPFRun runText26=dispatchInfoTitle2.createRun();
                      runText26.setText(dispatchInfoList.get(i).get("REPORT_NUMBER").toString());
                  }else{
                	  runText25.setText("项目调度-最新报告期");
                  }
                  ///实施情况信息
            	  XWPFParagraph dispatchInfoTitle3 = doc.createParagraph();
            	// 设置字体对齐方式3
                  dispatchInfoTitle3.setAlignment(ParagraphAlignment.LEFT);
                  dispatchInfoTitle3.setVerticalAlignment(TextAlignment.TOP);
                  XWPFRun runText27=dispatchInfoTitle3.createRun();
                  runText27.setText("实施信息");
                  //建表
                  XWPFTable dispatchInfoTable1 = doc.createTable(4,2);
                  CTTblPr  dispatchInfoBlPr1= dispatchInfoTable1.getCTTbl().getTblPr();
                  dispatchInfoBlPr1.getTblW().setType(STTblWidth.DXA);
                  dispatchInfoBlPr1.getTblW().setW(new BigInteger("9000"));
                  // 设置上下左右四个方向的距离，可以将表格撑大
                  dispatchInfoTable1.setCellMargins(20, 20, 20, 20);
                 //信息表赋值-一行
                  tableCells = dispatchInfoTable1.getRow(0).getTableCells();
                  tableCells.get(0).setText("实际开工时间");
                  if(dispatchInfoList.get(i).get("ACTUAL_START_TIME")!=null&&!"".equals(dispatchInfoList.get(i).get("ACTUAL_START_TIME"))){
            		  tableCells.get(1).setText(dispatchInfoList.get(i).get("ACTUAL_START_TIME").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
                //信息表赋值-二行
                  tableCells = dispatchInfoTable1.getRow(1).getTableCells();
                  tableCells.get(0).setText("实际竣工时间	");
                  if(dispatchInfoList.get(i).get("ACTUAL_END_TIME")!=null&&!"".equals(dispatchInfoList.get(i).get("ACTUAL_END_TIME"))){
            		  tableCells.get(1).setText(dispatchInfoList.get(i).get("ACTUAL_END_TIME").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
                //信息表赋值-三行
                  tableCells = dispatchInfoTable1.getRow(2).getTableCells();
                  tableCells.get(0).setText("招投标方式");
                  if(dispatchInfoList.get(i).get("BIDDING_MODE")!=null&&!"".equals(dispatchInfoList.get(i).get("BIDDING_MODE"))){
            		  tableCells.get(1).setText(dispatchInfoList.get(i).get("BIDDING_MODE").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
                //信息表赋值-四行
                  tableCells = dispatchInfoTable1.getRow(3).getTableCells();
                  tableCells.get(0).setText("建设单位");
                  if(dispatchInfoList.get(i).get("BUILD_UNIT")!=null&&!"".equals(dispatchInfoList.get(i).get("BUILD_UNIT"))){
            		  tableCells.get(1).setText(dispatchInfoList.get(i).get("BUILD_UNIT").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
                  
                  //调度信息-----进度详细信息
                  XWPFParagraph dispatchInfoTitle4 = doc.createParagraph();
                  // 设置字体对齐方式3
                  dispatchInfoTitle4.setAlignment(ParagraphAlignment.LEFT);
                  dispatchInfoTitle4.setVerticalAlignment(TextAlignment.TOP);
                  XWPFRun runText28=dispatchInfoTitle4.createRun();
                  runText28.setFontSize(20);
                  ///进度详细信息
                  runText28.setText("进度详细信息");
                  //建表
                  XWPFTable dispatchInfoTable2 = doc.createTable(4,2);
                  CTTblPr  dispatchInfoBlPr2= dispatchInfoTable2.getCTTbl().getTblPr();
                  dispatchInfoBlPr2.getTblW().setType(STTblWidth.DXA);
                  dispatchInfoBlPr2.getTblW().setW(new BigInteger("9000"));
                  // 设置上下左右四个方向的距离，可以将表格撑大
                  dispatchInfoTable2.setCellMargins(20, 20, 20, 20);
                 //信息表赋值-一行
                  tableCells = dispatchInfoTable2.getRow(0).getTableCells();
                  tableCells.get(0).setText("报告期");
                  if(dispatchInfoList.get(i).get("REPORT_NUMBER")!=null&&!"".equals(dispatchInfoList.get(i).get("REPORT_NUMBER"))){
            		  tableCells.get(1).setText(dispatchInfoList.get(i).get("REPORT_NUMBER").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
                //信息表赋值-二行
                  tableCells = dispatchInfoTable2.getRow(1).getTableCells();
                  tableCells.get(0).setText("形象进度");
                  if(dispatchInfoList.get(i).get("IMAGE_PROGRESS")!=null&&!"".equals(dispatchInfoList.get(i).get("IMAGE_PROGRESS"))){
            		  tableCells.get(1).setText(dispatchInfoList.get(i).get("IMAGE_PROGRESS").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
                //信息表赋值-三行
                  tableCells = dispatchInfoTable2.getRow(2).getTableCells();
                  tableCells.get(0).setText("年度建设内容");
                  if(dispatchInfoList.get(i).get("YEAR_BUILD_CONTENT")!=null&&!"".equals(dispatchInfoList.get(i).get("YEAR_BUILD_CONTENT"))){
            		  tableCells.get(1).setText(dispatchInfoList.get(i).get("YEAR_BUILD_CONTENT").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
                //信息表赋值-四行
                  tableCells = dispatchInfoTable2.getRow(3).getTableCells();
                  tableCells.get(0).setText("问题及建议");
                  if(dispatchInfoList.get(i).get("PROBLEMS_SUGGESTIONS")!=null&&!"".equals(dispatchInfoList.get(i).get("PROBLEMS_SUGGESTIONS"))){
            		  tableCells.get(1).setText(dispatchInfoList.get(i).get("PROBLEMS_SUGGESTIONS").toString());
            	  }else{
            		  tableCells.get(1).setText(null);
            	  }
                  
                  //调度信息表 -------------资金到位情况
                  XWPFParagraph dispatchInfoTitle5 = doc.createParagraph();
                  // 设置字体对齐方式3
                  dispatchInfoTitle5.setAlignment(ParagraphAlignment.LEFT);
                  dispatchInfoTitle5.setVerticalAlignment(TextAlignment.TOP);
                  XWPFRun runText29=dispatchInfoTitle5.createRun();
                  runText29.setFontSize(20);
                  ///进度详细信息
                  runText29.setText("资金到位情况");
                  //获取调度信息资金到位情况信息
                  List<Map<String, Object>> dispatchInvestDowllist = (List<Map<String, Object>>) dispatchInfoList.get(i).get("dispatchInvestDowllist");
                  //判断资金到位情况是否为空
                  if(dispatchInvestDowllist.size()>0){
                	  //创建计划下达情况表
                	  XWPFTable dispatchInfoTable3 = doc.createTable(dispatchInvestDowllist.size()+1,4);
                      CTTblPr dispatchInfoBlPr3 = dispatchInfoTable3.getCTTbl().getTblPr();
                      dispatchInfoBlPr3.getTblW().setType(STTblWidth.DXA);
                      dispatchInfoBlPr3.getTblW().setW(new BigInteger("9000"));
                      // 设置上下左右四个方向的距离，可以将表格撑大
                      dispatchInfoTable3.setCellMargins(20, 20, 20, 20);
                      //信息表表头赋值
                      tableCells = dispatchInfoTable3.getRow(0).getTableCells();
                      XWPFTableCell cell0 = tableCells.get(0);
                      XWPFTableCell cell1 = tableCells.get(1);
                      XWPFTableCell cell2 = tableCells.get(2);
                      XWPFTableCell cell3 = tableCells.get(3);
                      XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                      XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                      XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
                      XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                      //固定表头内容属性
                      XWPFRun run0 = newPara0.createRun();
                      XWPFRun run1 = newPara1.createRun();
                      XWPFRun run2 = newPara2.createRun();
                      XWPFRun run3 = newPara3.createRun();
                      /** 内容居中显示 **/
                      newPara0.setAlignment(ParagraphAlignment.CENTER);
                      newPara1.setAlignment(ParagraphAlignment.CENTER);
                      newPara2.setAlignment(ParagraphAlignment.CENTER);
                      newPara3.setAlignment(ParagraphAlignment.CENTER);
                      //加粗显示
                      run0.setBold(true);
                      run1.setBold(true);
                      run2.setBold(true);
                      run3.setBold(true);
                      //赋值
                      run0.setText("资金类别");
                      run1.setText("总投资");  
                      run2.setText("截至上一报告期累计到位资金(万元)"); 
                      run3.setText("截至本报告期累计到位资金(万元)");
                      //循环生成表格内容
                      for(int j = 0;j<dispatchInvestDowllist.size();j++){
                    	  tableCells = dispatchInfoTable3.getRow(j+1).getTableCells();
                    	  if(dispatchInvestDowllist.get(j).get("NAME")!=null&&!"".equals(dispatchInvestDowllist.get(j).get("NAME"))){
                    		  tableCells.get(0).setText(dispatchInvestDowllist.get(j).get("NAME").toString());
                    	  }else{
                    		  tableCells.get(0).setText(null);
                    	  }
                    	  if(dispatchInvestDowllist.get(j).get("SUM_MONEY")!=null&&!"".equals(dispatchInvestDowllist.get(j).get("SUM_MONEY"))){
                    		  tableCells.get(1).setText(dispatchInvestDowllist.get(j).get("SUM_MONEY").toString());
                    	  }else{
                    		  tableCells.get(1).setText(null);
                    	  }
                    	  if(dispatchInvestDowllist.get(j).get("PREV_MONEY")!=null&&!"".equals(dispatchInvestDowllist.get(j).get("PREV_MONEY"))){
                    		  tableCells.get(2).setText(dispatchInvestDowllist.get(j).get("PREV_MONEY").toString());
                    	  }else{
                    		  tableCells.get(2).setText(null);
                    	  }
                    	  if(dispatchInvestDowllist.get(j).get("CUR_MONEY")!=null&&!"".equals(dispatchInvestDowllist.get(j).get("CUR_MONEY"))){
                    		  tableCells.get(3).setText(dispatchInvestDowllist.get(j).get("CUR_MONEY").toString());
                    	  }else{
                    		  tableCells.get(3).setText(null);
                    	  }
                      } 
                  }else{
                	//创建计划下达情况表
                	  XWPFTable dispatchInfoTable3 = doc.createTable(dispatchInvestDowllist.size()+1,4);
                      CTTblPr dispatchInfoBlPr3 = dispatchInfoTable3.getCTTbl().getTblPr();
                      dispatchInfoBlPr3.getTblW().setType(STTblWidth.DXA);
                      dispatchInfoBlPr3.getTblW().setW(new BigInteger("9000"));
                      // 设置上下左右四个方向的距离，可以将表格撑大
                      dispatchInfoTable3.setCellMargins(20, 20, 20, 20);
                      //信息表表头赋值
                      tableCells = dispatchInfoTable3.getRow(0).getTableCells();
                      XWPFTableCell cell0 = tableCells.get(0);
                      XWPFTableCell cell1 = tableCells.get(1);
                      XWPFTableCell cell2 = tableCells.get(2);
                      XWPFTableCell cell3 = tableCells.get(3);
                      XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                      XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                      XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
                      XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                      //固定表头内容属性
                      XWPFRun run0 = newPara0.createRun();
                      XWPFRun run1 = newPara1.createRun();
                      XWPFRun run2 = newPara2.createRun();
                      XWPFRun run3 = newPara3.createRun();
                      /** 内容居中显示 **/
                      newPara0.setAlignment(ParagraphAlignment.CENTER);
                      newPara1.setAlignment(ParagraphAlignment.CENTER);
                      newPara2.setAlignment(ParagraphAlignment.CENTER);
                      newPara3.setAlignment(ParagraphAlignment.CENTER);
                      //加粗显示
                      run0.setBold(true);
                      run1.setBold(true);
                      run2.setBold(true);
                      run3.setBold(true);
                      //赋值
                      run0.setText("资金类别");
                      run1.setText("总投资");  
                      run2.setText("截至上一报告期累计到位资金(万元)"); 
                      run3.setText("截至本报告期累计到位资金(万元)");
                  }
                  
                //调度信息表 -------------资金完成情况
                  XWPFParagraph dispatchInfoTitle6 = doc.createParagraph();
                  // 设置字体对齐方式3
                  dispatchInfoTitle6.setAlignment(ParagraphAlignment.LEFT);
                  dispatchInfoTitle6.setVerticalAlignment(TextAlignment.TOP);
                  XWPFRun runText30=dispatchInfoTitle6.createRun();
                  runText30.setFontSize(20);
                  ///进度详细信息
                  runText30.setText("资金完成情况");
                  //获取调度信息资金到位情况信息
                  List<Map<String, Object>> dispatchInvestPutlist = (List<Map<String, Object>>) dispatchInfoList.get(i).get("dispatchInvestPutlist");
                  //判断资金完成情况是否为空
                  if(dispatchInvestPutlist.size()>0){
                	  //创建资金完成情况表
                	  XWPFTable dispatchInfoTable4 = doc.createTable(dispatchInvestPutlist.size()+2,5);
                      CTTblPr dispatchInfoBlPr4 = dispatchInfoTable4.getCTTbl().getTblPr();
                      dispatchInfoBlPr4.getTblW().setType(STTblWidth.DXA);
                      dispatchInfoBlPr4.getTblW().setW(new BigInteger("9000"));
                      // 设置上下左右四个方向的距离，可以将表格撑大	
                      dispatchInfoTable4.setCellMargins(20, 20, 20, 20);
                      //信息表表头赋值
                      tableCells = dispatchInfoTable4.getRow(0).getTableCells();
                      XWPFTableCell cell0 = tableCells.get(0);
                      XWPFTableCell cell1 = tableCells.get(1);
                      XWPFTableCell cell3 = tableCells.get(3);
                      XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                      XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                      XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                      //固定表头内容属性
                      XWPFRun run0 = newPara0.createRun();
                      XWPFRun run1 = newPara1.createRun();
                      XWPFRun run3 = newPara3.createRun();
                      /** 内容居中显示 **/
                      newPara0.setAlignment(ParagraphAlignment.CENTER);
                      newPara1.setAlignment(ParagraphAlignment.CENTER);
                      newPara3.setAlignment(ParagraphAlignment.CENTER);
                      //加粗显示
                      run0.setBold(true);
                      run1.setBold(true);
                      run3.setBold(true);
                      //赋值
                      run0.setText("资金类别");
                      run1.setText("累计完成投资(万元)");  
                      run3.setText("累计支付情况(万元)"); 
                      tableCells = dispatchInfoTable4.getRow(1).getTableCells();
                       cell0 = tableCells.get(1);
                       cell1 = tableCells.get(2);
                      XWPFTableCell cell2 = tableCells.get(3);
                       cell3 = tableCells.get(4);
                       newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                       newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                      XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
                       newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
                      //固定表头内容属性
                       run0 = newPara0.createRun();
                       run1 = newPara1.createRun();
                      XWPFRun run2 = newPara2.createRun();
                       run3 = newPara3.createRun();
                      /** 内容居中显示 **/
                      newPara0.setAlignment(ParagraphAlignment.CENTER);
                      newPara1.setAlignment(ParagraphAlignment.CENTER);
                      newPara2.setAlignment(ParagraphAlignment.CENTER);
                      newPara3.setAlignment(ParagraphAlignment.CENTER);
                      //加粗显示
                      run0.setBold(true);
                      run1.setBold(true);
                      run2.setBold(true);
                      run3.setBold(true);
                      //赋值
                      run0.setText("截至上一报告期");
                      run1.setText("截至本报告期");  
                      run2.setText("截至上一报告期"); 
                      run3.setText("截至本报告期");
                      this.mergeCellsVertically(dispatchInfoTable4, 0, 0, 1);
                      this.mergeCellsHorizontal(dispatchInfoTable4, 0, 1, 2);
                      this.mergeCellsHorizontal(dispatchInfoTable4, 0, 3, 4);
                      //循环生成表格内容
                      for(int j = 0;j<dispatchInvestPutlist.size();j++){
                    	  tableCells = dispatchInfoTable4.getRow(j+2).getTableCells();
                    	  if(dispatchInvestPutlist.get(j).get("NAME")!=null&&!"".equals(dispatchInvestPutlist.get(j).get("NAME"))){
                    		  tableCells.get(0).setText(dispatchInvestPutlist.get(j).get("NAME").toString());
                    	  }else{
                    		  tableCells.get(0).setText(null);
                    	  }
                    	  if(dispatchInvestPutlist.get(j).get("FINISH_PREV_MONEY")!=null&&!"".equals(dispatchInvestPutlist.get(j).get("FINISH_PREV_MONEY"))){
                    		  tableCells.get(1).setText(dispatchInvestPutlist.get(j).get("FINISH_PREV_MONEY").toString());
                    	  }else{
                    		  tableCells.get(1).setText(null);
                    	  }
                    	  if(dispatchInvestPutlist.get(j).get("FINISH_CUR_MONEY")!=null&&!"".equals(dispatchInvestPutlist.get(j).get("FINISH_CUR_MONEY"))){
                    		  tableCells.get(2).setText(dispatchInvestPutlist.get(j).get("FINISH_CUR_MONEY").toString());
                    	  }else{
                    		  tableCells.get(2).setText(null);
                    	  }
                    	  if(dispatchInvestPutlist.get(j).get("PAY_PREV_MONEY")!=null&&!"".equals(dispatchInvestPutlist.get(j).get("PAY_PREV_MONEY"))){
                    		  tableCells.get(3).setText(dispatchInvestPutlist.get(j).get("PAY_PREV_MONEY").toString());
                    	  }else{
                    		  tableCells.get(3).setText(null);
                    	  }
                    	  if(dispatchInvestPutlist.get(j).get("PAY_CUR_MONEY")!=null&&!"".equals(dispatchInvestPutlist.get(j).get("PAY_CUR_MONEY"))){
                    		  tableCells.get(3).setText(dispatchInvestPutlist.get(j).get("PAY_CUR_MONEY").toString());
                    	  }else{
                    		  tableCells.get(3).setText(null);
                    	  }
                      } 
                  }else{
                	//创建资金完成情况表
                	  XWPFTable dispatchInfoTable4 = doc.createTable(2,5);
                      CTTblPr dispatchInfoBlPr4 = dispatchInfoTable4.getCTTbl().getTblPr();
                      dispatchInfoBlPr4.getTblW().setType(STTblWidth.DXA);
                      dispatchInfoBlPr4.getTblW().setW(new BigInteger("9000"));
                      // 设置上下左右四个方向的距离，可以将表格撑大	
                      dispatchInfoTable4.setCellMargins(20, 20, 20, 20);
                      //信息表表头赋值
                      tableCells = dispatchInfoTable4.getRow(0).getTableCells();
                      tableCells.get(0).setText("资金类别");
                      tableCells.get(1).setText("累计完成投资(万元)");  
                      tableCells.get(3).setText("累计支付情况(万元)"); 
                      tableCells = dispatchInfoTable4.getRow(1).getTableCells();
                      tableCells.get(1).setText("截至上一报告期");
                      tableCells.get(2).setText("截至本报告期");  
                      tableCells.get(3).setText("截至上一报告期"); 
                      tableCells.get(4).setText("截至本报告期");
                      this.mergeCellsVertically(dispatchInfoTable4, 0, 0, 1);
                      this.mergeCellsHorizontal(dispatchInfoTable4, 0, 1, 2);
                      this.mergeCellsHorizontal(dispatchInfoTable4, 0, 3, 4);
                  }
                  
                //调度信息表 -------------调度图片/视频
                  XWPFParagraph dispatchInfoTitle7 = doc.createParagraph();
                  dispatchInfoTitle7.setPageBreak(true);
                  // 设置字体对齐方式3
                  dispatchInfoTitle7.setAlignment(ParagraphAlignment.LEFT);
                  dispatchInfoTitle7.setVerticalAlignment(TextAlignment.TOP);
                  XWPFRun runText31=dispatchInfoTitle7.createRun();
                  runText31.setFontSize(20);
                  ///调度图片/视频
                  runText31.setText("调度图片/视频");
                  //获取调度图片/视频信息
                  String[] imagelist =null;
                  if(dispatchInfoList.get(i).get("IMAGE_URL")!=null&&!"".equals(dispatchInfoList.get(i).get("IMAGE_URL"))){
                	  imagelist = dispatchInfoList.get(i).get("IMAGE_URL").toString().split(",");
            	  }
                  //判断资金完成情况是否为空
                  if(imagelist!=null){
                	  //图片数量
                	  Integer imagelistLength = imagelist.length;
                	  //图片一行三张，现判断有多少满的行图片
                	  Integer allTr = imagelistLength/3;
                	  //余下几张
                	  Integer numMod = imagelistLength%3;
                	  //创建资金完成情况表
                	  //判断生成几行的表
                	  if(numMod>0){
                		  XWPFTable  dispatchInfoTable5 = doc.createTable(allTr+1,3);
                		  CTTblPr dispatchInfoBlPr5 = dispatchInfoTable5.getCTTbl().getTblPr();
                          dispatchInfoBlPr5.getTblW().setType(STTblWidth.DXA);
                          dispatchInfoBlPr5.getTblW().setW(new BigInteger("9000"));
                          // 设置上下左右四个方向的距离，可以将表格撑大	
                          dispatchInfoTable5.setCellMargins(20, 20, 20, 20);
                          //循环生成表格内容
                          for(int j = 0;j<allTr;j++){
                        	  tableCells = dispatchInfoTable5.getRow(j).getTableCells();
                        	  XWPFTableCell cell0 = tableCells.get(0);
                              XWPFTableCell cell1 = tableCells.get(1);
                              XWPFTableCell cell2 = tableCells.get(2);
                              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
                              //固定表头内容属性
                              XWPFRun run0 = newPara0.createRun();
                              XWPFRun run1 = newPara1.createRun();
                              XWPFRun run2 = newPara2.createRun();
                              /** 内容居中显示 **/
                              newPara0.setAlignment(ParagraphAlignment.CENTER);
                              newPara1.setAlignment(ParagraphAlignment.CENTER);
                              newPara2.setAlignment(ParagraphAlignment.CENTER);
                              //加粗显示
                              run0.setBold(true);
                              run1.setBold(true);
                              run2.setBold(true);
                              try {
                                run0.addPicture(new FileInputStream(tempFilePath+imagelist[3*j]), this.getFormat(imagelist[3*j]),tempFilePath+imagelist[3*j],Units.toEMU(100), Units.toEMU(100));
								run1.addPicture(new FileInputStream(tempFilePath+ imagelist[3*j+1]), this.getFormat(imagelist[3*j+1]),tempFilePath+ imagelist[3*j+1],Units.toEMU(100), Units.toEMU(100));
								run2.addPicture(new FileInputStream(tempFilePath+imagelist[3*j+2]), this.getFormat(imagelist[3*j+2]),tempFilePath+imagelist[3*j+2],Units.toEMU(100), Units.toEMU(100));
							} catch (InvalidFormatException | IOException e) {
								e.printStackTrace();
							}
                          } 
                          if(numMod>0){
                        	  switch (numMod) {
    							case 1:
    								 tableCells = dispatchInfoTable5.getRow(allTr).getTableCells();
    								 XWPFTableCell cell0 = tableCells.get(0);
    	                             XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
    	                             //固定表头内容属性
    	                             XWPFRun run0 = newPara0.createRun();
    	                             /** 内容居中显示 **/
    	                             newPara0.setAlignment(ParagraphAlignment.CENTER);
    	                            try {
    	                                run0.addPicture(new FileInputStream(tempFilePath+imagelist[imagelist.length-1]),
    	                                		this.getFormat(imagelist[imagelist.length-1]),tempFilePath+imagelist[imagelist.length-1],Units.toEMU(100), Units.toEMU(100));
    								} catch (InvalidFormatException | IOException e) {
    									e.printStackTrace();
    								}
    								break;
    							case 2:
    								 tableCells = dispatchInfoTable5.getRow(allTr).getTableCells();
    								 tableCells.get(0).setText( new java.io.File(imagelist[imagelist.length-2]).toString());
    								 tableCells.get(1).setText( new java.io.File(imagelist[imagelist.length-1]).toString());
    								  cell0 = tableCells.get(0);
    	                              XWPFTableCell cell1 = tableCells.get(1);
    	                               newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
    	                              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
    	                              //固定表头内容属性
    	                               run0 = newPara0.createRun();
    	                              XWPFRun run1 = newPara1.createRun();
    	                              /** 内容居中显示 **/
    	                              newPara0.setAlignment(ParagraphAlignment.CENTER);
    	                              newPara1.setAlignment(ParagraphAlignment.CENTER);
    	                              try {
    	                                run0.addPicture(new FileInputStream(tempFilePath+imagelist[imagelist.length-2]), 
    	                                		this.getFormat(imagelist[imagelist.length-2]),tempFilePath+imagelist[imagelist.length-2],Units.toEMU(100), Units.toEMU(100));
    									run1.addPicture(new FileInputStream(tempFilePath+imagelist[imagelist.length-1]), 
    											this.getFormat(imagelist[imagelist.length-1]),tempFilePath+imagelist[imagelist.length-1],Units.toEMU(100), Units.toEMU(100));
    								} catch (InvalidFormatException | IOException e) {
    									e.printStackTrace();
    								}
    								break;
    						}
                          }
                	  }else{
                		  XWPFTable dispatchInfoTable5 = doc.createTable(allTr,3); 
                		  CTTblPr dispatchInfoBlPr5 = dispatchInfoTable5.getCTTbl().getTblPr();
                          dispatchInfoBlPr5.getTblW().setType(STTblWidth.DXA);
                          dispatchInfoBlPr5.getTblW().setW(new BigInteger("9000"));
                          // 设置上下左右四个方向的距离，可以将表格撑大	
                          dispatchInfoTable5.setCellMargins(20, 20, 20, 20);
                          //循环生成表格内容
                          for(int j = 0;j<allTr;j++){
                        	  tableCells = dispatchInfoTable5.getRow(j).getTableCells();
                        	  XWPFTableCell cell0 = tableCells.get(0);
                              XWPFTableCell cell1 = tableCells.get(1);
                              XWPFTableCell cell2 = tableCells.get(2);
                              XWPFParagraph newPara0 = new XWPFParagraph(cell0.getCTTc().addNewP(), cell0);
                              XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
                              XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
                              //固定表头内容属性
                              XWPFRun run0 = newPara0.createRun();
                              XWPFRun run1 = newPara1.createRun();
                              XWPFRun run2 = newPara2.createRun();
                              /** 内容居中显示 **/
                              newPara0.setAlignment(ParagraphAlignment.CENTER);
                              newPara1.setAlignment(ParagraphAlignment.CENTER);
                              newPara2.setAlignment(ParagraphAlignment.CENTER);
                              //加粗显示
                              run0.setBold(true);
                              run1.setBold(true);
                              run2.setBold(true);
                              try {
                                run0.addPicture(new FileInputStream(tempFilePath+imagelist[3*j]), this.getFormat(imagelist[3*j]),tempFilePath+imagelist[3*j],Units.toEMU(100), Units.toEMU(100));
								run1.addPicture(new FileInputStream(tempFilePath+ imagelist[3*j+1]), this.getFormat(imagelist[3*j+1]),tempFilePath+ imagelist[3*j+1],Units.toEMU(100), Units.toEMU(100));
								run2.addPicture(new FileInputStream(tempFilePath+imagelist[3*j+2]), this.getFormat(imagelist[3*j+2]),tempFilePath+imagelist[3*j+2],Units.toEMU(100), Units.toEMU(100));
							} catch (InvalidFormatException | IOException e) {
								e.printStackTrace();
							}
                          } 
                	  }
                  }
        	  }
          }
          
         FileOutputStream out;
        try {
        	File file=new File(filePath);
        	if(!file.exists()){
        		file.mkdirs();    
        	}
        	out = new FileOutputStream(filePath+fileName);
            doc.write(out);       
          } catch (IOException e) {
              e.printStackTrace();
          }
         System.out.println("success");
      }

	/**
	 * 根据条件查询项目
	 *
	 * @param userId
	 * @param pageBo
	 * @param projectVo       页面查询条件
	 * @param filters         预警的参数
	 * @param reportParamsMap @return
	 * @orderBy
	 * @author tanghw
	 * @Date 2016年10月31日下午3:51:56
	 */
	@Override
	public Page<Map<String, Object>> searchWarningProject(String userId, Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo, Map<String, String> filters, Map<String, String> reportParamsMap) {
		Page<Map<String, Object>> resultPage = null;
		List<Object> paramList= Lists.newArrayList();
		String filterSql="";
		if (StringUtils.isNotBlank(filters.get("warnCode"))){
			filterSql += " select project_id as id, UID, PROJECT_CODE, plate_id, PRO_NAME, INVESTMENT_TOTAL, pro_type_code, pro_type,"
					+ " INDUSTRY, INDUSTRY_FULL_KEY, GBINDUSTRY, GB_INDUSTRY_FULL_KEY, BUILD_PLACE, BUILD_PLACE_FULL_KEY, expect_startyear,"
					+ " expect_endyear, project_stagename, project_stage, area_code, actual_startyear, isimg, warning_lv, warning_code,"
					+ " warning_name, place1code, place1name, place2code, place2name, place3code, place3name,projectflag, projectinfo"
					+ " from v_warning_analysis vwa ";
			filterSql += " where 1=1 ";
			filterSql += " and vwa.warning_code = '"+filters.get("warnCode")+"' ";
			filterSql += " and vwa.projectflag = 1 ";
		}
		String placeName = filters.get("builPlaceChineseName").trim().toString();
		placeName = placeName.replace("顶层", "");
		if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))||StringUtils.isNotEmpty(placeName)){
			String place=filters.get("builPlaceCode").trim().toString();
			// 省级
			if(!StringUtils.contains(placeName, "-")){
				filterSql += " AND vwa.place1name  like '"+placeName+"' ";
				filterSql += " AND vwa.place2name  is not null ";
				filterSql += " AND vwa.place3name  is not null ";
			}
			//  市级
			else if(placeName.indexOf("-") == placeName.lastIndexOf("-")){
				filterSql += " AND vwa.place2name  like '"+placeName.substring(placeName.indexOf("-")+1, placeName.length())+"' ";
				filterSql += " AND vwa.place3name  is not null ";
			}
			// 县级
			else{
				//过滤项目建设地点为地图所选地点
//			filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				filterSql += " AND vwa.place3name  like '"+placeName.substring(placeName.lastIndexOf("-")+1, placeName.length())+"' ";
			}
			
		}
		// 全局项目阶段查询
		if(StringUtils.isNotEmpty(filters.get("proStageCode"))){
			// 项目阶段code
			String stageCode=filters.get("proStageCode").trim().toString();
			// 项目阶段名称
			String stageName="";
			switch (Integer.parseInt(stageCode)){
				case 0:stageName="谋划阶段";break;
				case 2:stageName="项目决策阶段";break;
				case 4:stageName="报建阶段";break;
				case 6:stageName="开工建设阶段";break;
				case 8:stageName="竣工验收";break;

			}
			filterSql += " AND vwa.project_stagename = '"+stageName+"' ";
		}
		//项目名称
		if(null!=projectVo.getProjectName() && !projectVo.getProjectName().isEmpty()){
			filterSql +=" and vwa.pro_name like '%"+projectVo.getProjectName()+"%' ";
		}
		//项目类型
		if(null!=projectVo.getCheckLevel() && !projectVo.getCheckLevel().isEmpty()){
			filterSql +=" and vwa.pro_type_code in ('"+projectVo.getCheckLevel().replace(",", "','")+"') ";
		}
		//总投资区间第一个值
		if(projectVo.getAllCaptial1()!=null && !projectVo.getAllCaptial1().isEmpty()){
			filterSql +=" and vwa.invest_total >='"+projectVo.getAllCaptial1()+"' ";
		}
		//总投资区间第二个值
		if(projectVo.getAllCaptial2()!=null && !projectVo.getAllCaptial2().isEmpty()){
			filterSql +=" and vwa.invest_total <='"+projectVo.getAllCaptial2()+"' ";
		}
		//拟开工年份第一个值
		if(null!=projectVo.getPlanStartYear1()&&StringUtils.isNotBlank(projectVo.getPlanStartYear1().trim())){
			filterSql +=" and vwa.expect_startyear >='"+projectVo.getPlanStartYear1()+"' ";
		}
		//拟开工年份第二个值
		if(null!=projectVo.getPlanStartYear2()&&StringUtils.isNotBlank(projectVo.getPlanStartYear2().trim())){
			filterSql +=" and vwa.expect_startyear <='"+projectVo.getPlanStartYear2().trim()+"' ";
		}
		//所属行业
		if(null!=projectVo.getIndustryCode() && !projectVo.getIndustryCode().isEmpty()){
			filterSql +=" and vwa.industry_code in ('"+projectVo.getIndustryCode().replace(",", "','")+"') ";
		}
		//国标行业
		if(null!=projectVo.getGbIndustryCode() && !projectVo.getGbIndustryCode().isEmpty()){
			filterSql +=" and vwa.gbindustry_code in ('"+projectVo.getGbIndustryCode().replace(",", "','")+"') ";
		}
		//建设地点
		if(null!=projectVo.getProjectRegion() && !projectVo.getProjectRegion().isEmpty()){
			filterSql +=" and vwa.build_place_code in ('"+projectVo.getProjectRegion().replace(",", "','")+"') ";
		}
//		filterSql +=" order by vwa.sort";
		resultPage = this.dao.findBySql(pageBo, filterSql,paramList.toArray());
		return resultPage;
	}

	@Override
	public Page<Map<String, Object>> searchNVProject(String userId, Page<Map<String, Object>> pageBo, ProjectFileSearchVo projectVo, Map<String, String> filters, Map<String, String> reportParamsMap) {
		Page<Map<String, Object>> resultPage = null;
		List<Object> paramList= Lists.newArrayList();
		String filterSql="";
		if (StringUtils.isNotBlank(filters.get("warnCode"))&&"0".equals(filters.get("warnCode"))){
			filterSql =" select vpd.* from ( ( select vpd.UID, vpd.id, vpd.project_code,";
			filterSql+=" vpd.plate_id, vpd.pro_name, vpd.invest_total, vpd.pro_type_code,";
			filterSql+=" vpd.pro_type, vpd.industry, vpd.industry_code, vpd.gbindustry, vpd.gbindustry_code, vpd.build_place, vpd.build_place_code, vpd.expect_startyear,";
			filterSql+=" vpd.expect_endyear, vpd.project_stagename, vpd.project_stage, vpd.area_code, to_char(yp.ACTUAL_START_TIME) as actual_startyear, isnull(yp.IMAGE_URL) as isimg, vpd.sort";
			filterSql+=" from v_project_details vpd left join project_dispatch_impl yp on vpd.id = yp.project_id";
			filterSql+=" where 1 = 1 and exists ( select 1 from v_warning_analysis vwa where vwa.warning_code = 0 and vwa.id = vpd.id ) )";
		
			filterSql+=" union all";
			filterSql+=" ( select uuid() as UID, NULL AS id  , A.PROJECT_CODE AS project_code  , A.ID AS plate_id, A.PROJECT_NAME AS pro_name, A.TOTAL_MONEY  AS invest_total, CASE WHEN A.PROJECT_TYPE = '01' THEN 'A00001'"; 
			filterSql+=" WHEN A.PROJECT_TYPE = '02' THEN 'A00002' WHEN A.PROJECT_TYPE = '03' THEN 'A00003' END AS pro_type_code, CASE WHEN A.PROJECT_TYPE = '01' THEN '审批' WHEN A.PROJECT_TYPE = '02' THEN '核准'"; 
			filterSql+=" WHEN A.PROJECT_TYPE = '03' THEN '备案' END AS pro_type, B.CODE_NAME AS industry, A.WNINDUSTRY AS industry_code, C.CODE_NAME AS gbindustry, A.INDUSTRY AS gbindustry_code, D.S_NAME AS build_place, A.PLACE_CODE AS build_place_code,";
			filterSql+=" A.START_YEAR  AS expect_startyear, A.END_YEAR AS expect_endyear, CASE WHEN E.CODE1='SHJD' THEN '项目决策阶段' WHEN E.CODE1 = 'BJJD' THEN '报建阶段' END  AS project_stagename, CASE WHEN E.CODE1='SHJD' THEN  '2'"; 
			filterSql+=" WHEN E.CODE1 = 'BJJD' THEN '4' END   AS project_stage, A.DIVISION_CODE AS area_code, to_char(A.START_YEAR)  AS actual_startyear, 1 as isimg, NULL as 'sort'";
			filterSql+=" FROM TZXMZH.R_COMM_PROJECTS A LEFT JOIN TZXMZH.APP_CODELIST B ON B.CODE_VALUE=A.WNINDUSTRY AND B.CODE_KIND='WNHYDM'"; 
			filterSql+=" LEFT JOIN TZXMZH.APP_CODELIST C ON C.CODE_VALUE=A.INDUSTRY AND C.CODE_KIND='HYDM' LEFT JOIN TZXMZH.R_BASE_DIVISION D ON D.CODE =A.PLACE_CODE AND D.IS_ALONE='1'";
			filterSql+=" LEFT JOIN TZXMZH.DIM_XMJD E ON A.PROJECT_STATE = E.CODE WHERE exists ( select * from v_warning_analysis vwa where vwa.warning_code = 0 and vwa.id = A.ID ) ) ) vpd";
			filterSql+=" where 1=1";
		} else{
			filterSql = " select vpd.* , yp.ACTUAL_START_TIME as actual_startyear, isnull(yp.IMAGE_URL) as isimg   from v_project_details vpd  left join  project_dispatch_impl   yp  on vpd.id=yp.project_id ";
			if(StringUtils.isNotBlank(filters.get("warnCode"))&&!"0".equals(filters.get("warnCode"))||"8".equals(filters.get("proStageCode"))){
				filterSql += " left join v_life_cycle vlc on vpd.id=vlc.id and projectflag = 1 "; 
			}else if(StringUtils.isNotEmpty(filters.get("startYear"))){
				filterSql += " left join v_life_cycle vlc on vpd.id=vlc.id and projectflag = 1 "; 
			}
			filterSql += " where 1=1 ";
		}
		// 设置预警的查询条件
		if(StringUtils.isNotBlank(filters.get("warnCode"))&&!"0".equals(filters.get("warnCode"))){
			filterSql+=" and  exists(select 1 from  v_warning_analysis  vwa where   vwa.warning_code= "+filters.get("warnCode")+" and (vwa.id = vpd.id or vpd.plate_id=vwa.id)) ";
		}
		//判断地图所选地区是否为空
		if(StringUtils.isNotBlank(filters.get("warnCode"))){
			if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))){
				String place=filters.get("builPlaceCode").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND vpd.build_place_code = '"+place+"' ";
				}
			}
		}else{
			if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))||StringUtils.isNotEmpty(filters.get("builPlaceChineseName"))){
				String place=filters.get("builPlaceCode").trim().toString();
				String placeName = filters.get("builPlaceChineseName").trim().toString();
				String placeName_county = null;
				if(StringUtils.contains(placeName, "-")&&(placeName.indexOf("-") != placeName.lastIndexOf("-"))){
					placeName_county = placeName.substring(placeName.lastIndexOf("-")+1, placeName.length());
				}
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
//				filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					filterSql += " AND vpd.build_place  like '%"+placeName_county+"%' ";
				}
				
			}
		}
		// 全局项目阶段查询
		if(StringUtils.isNotEmpty(filters.get("proStageCode"))){
			// 项目阶段code
			String stageCode=filters.get("proStageCode").trim().toString();
			// 项目阶段名称
			String stageName="";
			switch (Integer.parseInt(stageCode)){
				case 0:stageName="谋划阶段";break;
				case 2:stageName="项目决策阶段";break;
				case 4:stageName="报建阶段";break;
				case 6:stageName="开工建设阶段";break;
				case 8:stageName="竣工验收";break;

			}
			filterSql += " AND vpd.project_stagename = '"+stageName+"' ";
		}
		// 全局项目阶段查询
		if(StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
			// 项目阶段code
			String governmentCode=filters.get("GovernmentCode").trim().toString();
			filterSql += " AND vpd.gbindustry_code = '"+governmentCode+"' ";
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND vpd.build_place_code = '"+place+"' ";
				}
			}
		}
		if(StringUtils.isNotEmpty(filters.get("IndustryCode"))){
			// 项目阶段code
			String IndustryCode=filters.get("IndustryCode").trim().toString();
			filterSql += " AND vpd.industry_code = (select distinct short_value from DICTIONARY_ITEMS where ITEM_KEY = '"+IndustryCode+"' ) ";
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND vpd.build_place_code = '"+place+"' ";
				}
			}
		}
		if(StringUtils.isNotEmpty(filters.get("expectStartYear"))){
			// 项目阶段code
			Integer expectStartYear= Integer.valueOf(filters.get("expectStartYear").trim().toString().substring(0, 4));
			if(Constant.BEFORE_2014.equals(expectStartYear)){
				filterSql += " AND CAST(substr(vpd.expect_startyear,1,4) as SIGNED) < 2015";
			}else if(Constant.AFTER_2021.equals(expectStartYear)){
				filterSql += " AND CAST(substr(vpd.expect_startyear,1,4) as SIGNED) > 2020";
			}else{
				filterSql += " AND CAST(substr(vpd.expect_startyear,1,4) as SIGNED) = "+expectStartYear+"";
			}
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND vpd.build_place_code = '"+place+"' ";
				}
			}
		}
		if(StringUtils.isNotEmpty(filters.get("startYear"))){
			// 项目阶段code
			String startYear= filters.get("startYear").trim().toString().substring(0, 7);
			filterSql += " AND vlc.state_time = '"+startYear+"'";
			filterSql += " AND vlc.project_stage = 6";
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND vpd.build_place_code = '"+place+"' ";
				}
			}
		}
		if(StringUtils.isNotEmpty(filters.get("delayYears"))){
			String delayYears= filters.get("delayYears").trim().toString();
			filterSql += " AND vlc.overdue_years_char = '"+delayYears+"'";
		}
		if(StringUtils.isNotEmpty(filters.get("projectType"))){
			// 项目阶段code
			String projectType=filters.get("projectType").trim().toString();
			switch(projectType){
			case Constant.UNKOWN : filterSql += " AND vpd.pro_type is NULL "; break;
			default : filterSql += " AND vpd.pro_type = '"+projectType+"' ";
			}
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap").trim().toString())){
				String place=filters.get("builPlaceCodeMap").trim().toString();
				// 省级
				if(place.endsWith("0000")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 2)+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
					filterSql += " AND vpd.build_place_code  like '"+place.substring(0, 4)+"%' ";
				}
				// 县级
				else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND vpd.build_place_code = '"+place+"' ";
				}
			}
		}
		// 查询条件项目阶段查询
		if(StringUtils.isNotBlank(projectVo.getProStageCode())){
			// 项目阶段code
			String sCode=projectVo.getProStageCode().trim().toString();
			// 项目阶段名称
			String sName="";
			switch (Integer.parseInt(sCode)){
				case 0:sName="谋划阶段";break;
				case 2:sName="项目决策阶段";break;
				case 4:sName="报建阶段";break;
				case 6:sName="开工建设阶段";break;
				case 8:sName="竣工验收";break;
			}
			filterSql += " AND vpd.project_stagename = '"+sName+"' ";
		}
		// 设置查询条件
		//项目名称
		if(null!=projectVo.getProjectName() && !projectVo.getProjectName().isEmpty()){
			filterSql +=" and vpd.pro_name like '%"+projectVo.getProjectName()+"%' ";
		}
		//项目类型
		if(null!=projectVo.getCheckLevel() && !projectVo.getCheckLevel().isEmpty()){
			filterSql +=" and vpd.pro_type_code in ('"+projectVo.getCheckLevel().replace(",", "','")+"') ";
		}
		//总投资区间第一个值
		if(projectVo.getAllCaptial1()!=null && !projectVo.getAllCaptial1().isEmpty()){
			filterSql +=" and vpd.invest_total >='"+projectVo.getAllCaptial1()+"' ";
		}
		//总投资区间第二个值
		if(projectVo.getAllCaptial2()!=null && !projectVo.getAllCaptial2().isEmpty()){
			filterSql +=" and vpd.invest_total <='"+projectVo.getAllCaptial2()+"' ";
		}
		//拟开工年份第一个值
		if(null!=projectVo.getPlanStartYear1()&&StringUtils.isNotBlank(projectVo.getPlanStartYear1().trim())){
			filterSql +=" and vpd.expect_startyear >='"+projectVo.getPlanStartYear1()+"' ";
		}
		//拟开工年份第二个值
		if(null!=projectVo.getPlanStartYear2()&&StringUtils.isNotBlank(projectVo.getPlanStartYear2().trim())){
			filterSql +=" and vpd.expect_startyear <='"+projectVo.getPlanStartYear2().trim()+"' ";
		}
		//所属行业
		if(null!=projectVo.getIndustryCode() && !projectVo.getIndustryCode().isEmpty()){
			filterSql +=" and vpd.industry_code in ('"+projectVo.getIndustryCode().replace(",", "','")+"') ";
		}
		//国标行业
		if(null!=projectVo.getGbIndustryCode() && !projectVo.getGbIndustryCode().isEmpty()){
			filterSql +=" and vpd.gbindustry_code in ('"+projectVo.getGbIndustryCode().replace(",", "','")+"') ";
		}
		//建设地点
		if(null!=projectVo.getProjectRegion() && !projectVo.getProjectRegion().isEmpty()){
			filterSql +=" and vpd.build_place_code in ('"+projectVo.getProjectRegion().replace(",", "','")+"') ";
		}
		filterSql +=" order by vpd.sort";
		resultPage = this.dao.findBySql(pageBo, filterSql,paramList.toArray());
		return resultPage;
	}
	
	/**
	 *  横向合并单元格 
	 * @orderBy 
	 * @param table
	 * @param row
	 * @param fromCell
	 * @param toCell
	 * @author tanghw
	 * @Date 2016年12月2日下午2:26:34
	 */
	private static void  mergeCellsHorizontal(XWPFTable table, int row, int fromCell,int toCell){
		for(int cellIndex = fromCell; cellIndex <= toCell; cellIndex++){
			XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
			if(cellIndex == fromCell){
				getCellCTTcPr(cell).addNewHMerge().setVal(STMerge.RESTART);	
			}else{
				getCellCTTcPr(cell).addNewHMerge().setVal(STMerge.CONTINUE);
			}
		}
		
	}
	/**
	 * 纵向合并单元格
	 * @orderBy 
	 * @param table
	 * @param col
	 * @param fromRow
	 * @param toRow
	 * @author tanghw
	 * @Date 2016年12月2日下午2:26:24
	 */
	private static void mergeCellsVertically(XWPFTable table, int col, int fromRow,int toRow){
		for(int rowIndex = fromRow; rowIndex <= toRow; rowIndex++){
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			if (rowIndex == fromRow) {
				getCellCTTcPr(cell).addNewVMerge().setVal(STMerge.RESTART);
			} else {
				getCellCTTcPr(cell).addNewVMerge().setVal(STMerge.CONTINUE);
			}
		}
	}
	private static CTTcPr getCellCTTcPr(XWPFTableCell cell) {
		CTTc cttc = cell.getCTTc();
		CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
		return tcPr;
	}
	/**
	 * 获取图片格式
	 * @orderBy 
	 * @param imgFile
	 * @return
	 * @author tanghw
	 * @Date 2016年12月5日下午2:22:53
	 */
	private int getFormat(String imgFile ){
		 int format = XWPFDocument.PICTURE_TYPE_JPEG;
		 if(imgFile.endsWith(".emf")) format = XWPFDocument.PICTURE_TYPE_EMF;
         else if(imgFile.endsWith(".wmf")) format = XWPFDocument.PICTURE_TYPE_WMF;
         else if(imgFile.endsWith(".pict")) format = XWPFDocument.PICTURE_TYPE_PICT;
         else if(imgFile.endsWith(".jpeg") || imgFile.endsWith(".jpg")) format = XWPFDocument.PICTURE_TYPE_JPEG;
         else if(imgFile.endsWith(".png")) format = XWPFDocument.PICTURE_TYPE_PNG;
         else if(imgFile.endsWith(".dib")) format = XWPFDocument.PICTURE_TYPE_DIB;
         else if(imgFile.endsWith(".gif")) format = XWPFDocument.PICTURE_TYPE_GIF;
         else if(imgFile.endsWith(".tiff")) format = XWPFDocument.PICTURE_TYPE_TIFF;
         else if(imgFile.endsWith(".eps")) format = XWPFDocument.PICTURE_TYPE_EPS;
         else if(imgFile.endsWith(".bmp")) format = XWPFDocument.PICTURE_TYPE_BMP;
         else if(imgFile.endsWith(".wpg")) format = XWPFDocument.PICTURE_TYPE_WPG;
         else {
             System.err.println("Unsupported picture: " + imgFile + ". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");
         }
		return format;
	}

	/**
	 * 三年滚动计划
	 * @orderBy 
	 * @param table
	 * @param col
	 * @param fromRow
	 * @param toRow
	 * @author zpd
	 * @Date 2017-11-03
	 */
	public Page<Map<String, Object>> searchSNProject(String userId, Page<Map<String, Object>> pageBo,
			ProjectFileSearchVo projectVo, Map<String, String> filters, Map<String, String> reportParamsMap) {
			// 定义查询结果返回接收变量
			Page<Map<String, Object>> resultPage = null;
			//定义查询的字段
			String filterSql ="SELECT P.* from (SELECT P.ID,P.STAGE,PM.PRO_NAME ,PM.INVESTMENT_TOTAL,PM.PRO_TYPE,PM.INDUSTRY,PM.BUILD_PLACE,PM.EXPECT_STARTYEAR ";
			List<Object> paramList = new ArrayList<Object>();
			// 为查询共通SQL拼过滤条件
			//ROLL_PLAN-三年滚动计划
			filterSql += getCommonSQL("plan_project","project_id")+"  inner join PLAN_PROJECT_INFO_EXT_MASTER PM on p.id = PM.plan_project_id "
					+ " inner join PLAN_PROJECT_INFO_EXT_INVEST AS PPIE on P.ID = PPIE.PLAN_PROJECT_ID"
					+ " left join DICTIONARY_ITEMS dic  on  PM.BUILD_PLACE = dic.item_key and dic.group_no ='1' ";
			//模块过滤状态
			//报送到国家
			if(StringUtils.isNotEmpty(filters.get("filterStatus"))
					&&Constant.REPORT_UNIT.equals(filters.get("filterStatus"))){
				//报送到国家即判断滚动计划项目的审核状态表的审核部门等级为4
				filterSql +=" left join PLAN_PROJECT_CHECK_STATUS PCS on PCS.ROLL_PLAN_PROJECT_ID = P.ID"
						+ " left join DEPARTMENT D on PCS.DEPARTMENT_FGW_GUID = D.DEPARTMENT_GUID "
						+ " WHERE D.STORE_LEVEL= '4' AND P.DELETE_FLAG ='0' and SUBSTRING(PCS.DEPARTMENT_FGW_GUID, 0, 6) = 'GUOJIA'";
			//全部
			}else{
				filterSql += "  where  P.DELETE_FLAG ='0' ";
			}
			//判断地图所选地区是否为空
			if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))||StringUtils.isNotEmpty(filters.get("builPlaceChineseName"))){
				String place=filters.get("builPlaceCode").trim().toString();
				String placeName = filters.get("builPlaceChineseName").trim().toString();
				String placeName_province = placeName.substring(0, placeName.indexOf("-"));
				String placeName_city = placeName.substring(placeName.indexOf("-"), placeName.lastIndexOf("-"));
				String placeName_county = placeName.substring(placeName.indexOf("-"), placeName.length());
			   // 省级
				if(place.endsWith("0000")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, i)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_province+"%' ";
				}
				//  市级
				else if(place.endsWith("00")){
//					filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					filterSql += " AND dic.item_full_value  like '"+placeName_city+"%' ";
				}
				// 县级
				else{
				//过滤项目建设地点为地图所选地点
//				filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
				filterSql += " AND dic.item_full_value  like '%"+placeName_county+"%' ";
				}
				
			}
				//符合政府投资方向
				if(StringUtils.isNotEmpty(filters.get("goverInvestDir"))&&!"undefined".equals(filters.get("goverInvestDir"))){
					if("A00010".equals(filters.get("goverInvestDir"))){
						filterSql += " and PM.GOVERNMENT_INVEST_DIRECTION  is null";
					}else{
						filterSql += " and PM.GOVERNMENT_INVEST_DIRECTION = '"+filters.get("goverInvestDir").trim().toString()+"'";
					}
					if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))){
						String place=filters.get("builPlaceCode").trim().toString();
					   // 省级
						if(place.endsWith("0000")){
							filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
						}
						//  市级
						else if(place.endsWith("00")){
							filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
						}
						// 县级
						else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
						}
						
					}
				}
			if(StringUtils.isNotEmpty(filters.get("index"))&&!"undefined".equals(filters.get("index"))){
				if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))){
					String place=filters.get("builPlaceCode").trim().toString();
				   // 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				
				}
			}
			//项目成熟度
			if(StringUtils.isNotEmpty(filters.get("projectMaturity"))){
				String b=filters.get("projectMaturity");
				if("谋划阶段".equals(b)){
					filterSql += " and P.STAGE = '"+1 +"'";
				}else if("前期工作阶段".equals(b)){
					filterSql += " and P.STAGE = '"+2 +"'";
				}else if("开工建设阶段".equals(b)){
					filterSql += " and P.STAGE = '"+3 +"'";
				}else if("竣工验收".equals(b)){
					filterSql += " and P.STAGE = '"+4 +"'";
				}else {
					filterSql += " and P.STAGE != '"+1 +"' and P.STAGE != '"+2 +"'and  P.STAGE != '"+3 +"' and P.STAGE != '"+4 +"'";
				}
				
				if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))){
					String place=filters.get("builPlaceCode").trim().toString();
				   // 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
				
				}
			}
			
			
			//项目类型名称
			if(StringUtils.isNotEmpty(filters.get("projectTypeName"))){
				String projectType=filters.get("projectTypeName").trim().toString();
				if("审批".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00001%' ";
				}else if("核准".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00002%' ";
				}else if("备案".equals(projectType.substring(projectType.length() - 2, projectType.length()))){
					filterSql += " AND PM.PRO_TYPE like '%A00003%' ";
				}else{
					filterSql += "AND ((PM.PRO_TYPE not like '%A00001%' and PM.PRO_TYPE not like '%A00002%' and PM.PRO_TYPE not like '%A00003%') or PM.PRO_TYPE is null)";
				}
				if(StringUtils.isNotEmpty(filters.get("builPlaceCode"))&&StringUtils.isNotBlank(filters.get("builPlaceCodeMap"))){
					String place=filters.get("builPlaceCode").trim().toString();
				   // 省级
					if(place.endsWith("0000")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
					}
					//  市级
					else if(place.endsWith("00")){
						filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
					}
					// 县级
					else{
					//过滤项目建设地点为地图所选地点
					filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
					}
					
				}
			}
			
			    // 国标行业（存在问题）
				if(StringUtils.isNotEmpty(filters.get("gbIndustry"))){
					// 项目阶段code
					String gbIndustry=filters.get("gbIndustry").trim().toString();
					if("FGWHY0016001".equals(gbIndustry)){
						filterSql += " AND PM.GB_INDUSTRY  is null ";
					}else{
					filterSql += " AND PM.GB_INDUSTRY = '"+gbIndustry+"' ";
					}
					if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap"))&&StringUtils.isNotBlank(filters.get("builPlaceCodeMap"))){
						String place=filters.get("builPlaceCodeMap").trim().toString();
						// 省级
						if(place.endsWith("0000")){
							filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
						}
						//  市级
						else if(place.endsWith("00")){
							filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
						}
						// 县级
						else{
							//过滤项目建设地点为地图所选地点
							filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
						}
					}
				}
				
				//委内行业(存在问题)
				if(StringUtils.isNotEmpty(filters.get("wnIndustry"))){
					// 项目阶段code
					String wnIndustry=filters.get("wnIndustry").trim().toString();
					if("1".equals(wnIndustry)){
						filterSql += " AND PM.INDUSTRY  is null ";
					}else{
						filterSql += " AND PM.INDUSTRY = '"+wnIndustry+"' ";
					}
					if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap"))&&StringUtils.isNotBlank(filters.get("builPlaceCodeMap"))){
						String place=filters.get("builPlaceCodeMap").trim().toString();
						// 省级
						if(place.endsWith("0000")){
							filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
						}
						//  市级
						else if(place.endsWith("00")){
							filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
						}
						// 县级
						else{
							//过滤项目建设地点为地图所选地点
							filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
						}
					}
				}
				
			   //重大战略名称	
				if(StringUtils.isNotEmpty(filters.get("majorStrategic"))){
					String majorStrategic=filters.get("majorStrategic").trim().toString();
					if("A00001".equals(majorStrategic)){
						filterSql += " AND PM.MAJOR_STRATEGY like '%"+majorStrategic+"%' ";
					}else if("A00002".equals(majorStrategic)||"A00003".equals(majorStrategic)){
						filterSql += " AND PM.MAJOR_STRATEGY like '"+majorStrategic+"%' ";
					}else{
						filterSql += " AND ((PM.MAJOR_STRATEGY not like '%A00001%' and PM.MAJOR_STRATEGY not like '%A00002%' and PM.MAJOR_STRATEGY not like '%A00003%') or MAJOR_STRATEGY is null) ";
					}
					if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap"))&&StringUtils.isNotBlank(filters.get("builPlaceCodeMap"))){
						String place=filters.get("builPlaceCodeMap").trim().toString();
						// 省级
						if(place.endsWith("0000")){
							filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 2)+"%' ";
						}
						//  市级
						else if(place.endsWith("00")){
							filterSql += " AND PM.BUILD_PLACE  like '"+place.substring(0, 4)+"%' ";
						}
						// 县级
						else{
						//过滤项目建设地点为地图所选地点
						filterSql += " AND PM.BUILD_PLACE = '"+place+"' ";
						}
					}
					
				}
			
				//判断是否选中了项目标签
				if(projectVo.getLabelId()!=null && !projectVo.getLabelId().isEmpty()&&!"all".equals(projectVo.getLabelId())){
					filterSql +=" and exists(select * from "
							+ " (SELECT PROJECT_ID FROM TZ_LABEL l,TZ_PROJECT_LABEL pl WHERE l.ID = pl.LABEL_ID "
							+ " and l.CREATE_user_id = '"+userId+"' and l.id='"+projectVo.getLabelId()+"' "
							+ " GROUP BY pl.PROJECT_ID )lb "
							+ " where P.ID = lb.project_id)";
				}
				//项目档案图查询条件拼装sql
				filterSql += this.getFiltersCondition(projectVo);
				//地图查询条件拼装sql
				if(reportParamsMap!=null){
					filterSql += this.getSearchFiltersCondition(reportParamsMap);
				}
				filterSql += " GROUP BY P.ID, p.UPDATE_TIME, P.STAGE, PM.PRO_NAME, "
						+ " PM.INVESTMENT_TOTAL, PM.PRO_TYPE, PM.INDUSTRY, PM.BUILD_PLACE, "
						+ " PM.EXPECT_STARTYEAR, yp.IMAGE_URL"
						+ ") P ";
				//排序
				// filterSql += " ORDER BY p.UPDATE_TIME desc, p.id desc ";
				//获取参数
				resultPage = this.dao.findBySql(pageBo, filterSql,paramList.toArray());
				// 字段code值转换成页面显示value值
				if (resultPage != null ) {
					List<Map<String, Object>> rs = this.convertItemKeyToValue(resultPage.getResult());
					
					resultPage.setResult(rs);
				}
				return resultPage;		
	}
}
