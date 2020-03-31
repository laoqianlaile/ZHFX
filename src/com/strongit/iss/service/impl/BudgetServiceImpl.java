package com.strongit.iss.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.common.Cache;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.ICommonUtils;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IBudgetService;

@Service
@Transactional
public class BudgetServiceImpl extends BaseService implements IBudgetService{
	
	@Autowired
	private ICommonUtils commonUtils;
	
	@Override
	/**
	 * 中央预算内申报地图报表数据
	 * @orderBy
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月21日下午2:56:15
	 */
	public List<Map<String, Object>> getBudgetReportByMap(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
        SQL.append(" FROM v_budget_report_wuwei1 VINF ");
        SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
        SQL.append(" ON VINF.Build_Place=DI.item_key and DI.GROUP_NO ='1'");
        SQL.append(" where 1 = 1");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.%f%");
		// 金额排序		
        SQL.append(" ORDER BY \"investMon\" desc, DI.%f%");	
        String sql;
		if(StringUtils.isBlank(filters.get("BuildPlaceLevel"))){
			sql = commonUtils.formatItemKey("0", SQL.toString());
		}else{
			sql = commonUtils.formatItemKey(filters.get("BuildPlaceLevel"), SQL.toString());
		}
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "00".equals(list.get(i).get("itemCode").toString().substring(0, 2))){
				list.get(i).put("itemName", "其他");
			}else if("99".equals(list.get(i).get("itemCode").toString().substring(0, 2))){
				list.get(i).put("itemName", "跨省区");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("1",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByMap 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内申报发改委行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月22日下午1:16:43
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportByIndustry(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON VINF.INDUSTRY=DI.item_key and DI.GROUP_NO ='8'");
		SQL.append(" where 1 = 1");
		//存在下钻的code
		if(StringUtils.isNotEmpty(filters.get("IndustryCode"))){
			SQL.append(" AND DI.%d% like '" +filters.get("IndustryCode")+"%' ");
		}
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.%f%");
		SQL.append(" ORDER BY \"applyInvestMon\" desc, DI.%f%");	
        String sql = SQL.toString();
		if(StringUtils.isBlank(filters.get("IndustryLevel"))){
			sql = commonUtils.formatItemKey("0", SQL.toString());
		}else{
			sql = commonUtils.formatItemKey(filters.get("IndustryLevel"), SQL.toString());
		}
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("8",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByIndustry 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内申报国标行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午9:55:59
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportByGBIndustry(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON VINF.GB_INDUSTRY=DI.ITEM_KEY and DI.GROUP_NO ='2'");
		SQL.append(" where 1 = 1");
		//存在下钻的code
		if(StringUtils.isNotEmpty(filters.get("GBIndustryCode"))){
			SQL.append(" AND DI.%d% like '" +filters.get("GBIndustryCode")+"%' ");
		}
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.%f%");
		SQL.append(" ORDER BY \"applyInvestMon\" desc, DI.%f%");
        String sql = SQL.toString();
		if(StringUtils.isBlank(filters.get("GBIndustryLevel"))){
			sql = commonUtils.formatItemKey("0", SQL.toString());
		}else{
			sql = commonUtils.formatItemKey(filters.get("GBIndustryLevel"), SQL.toString());
		}
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("2",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByGBIndustry 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内申报重大战略报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午10:42:36
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportByMajor(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.LV1_ITEM_KEY  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON VINF.MAJOR_STRATEGY=DI.ITEM_KEY and DI.GROUP_NO ='14'");
		SQL.append(" where 1= 1");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.LV1_ITEM_KEY");
		SQL.append(" ORDER BY \"applyInvestMon\" desc, DI.LV1_ITEM_KEY");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("14",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByMajor 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内申报政府投资方向报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午10:47:12
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportByGovernment(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON VINF.GOVERNMENT_INVEST_DIRECTION=DI.ITEM_KEY and DI.GROUP_NO ='9'");
		SQL.append(" where 1 = 1");
		//存在下钻的code
		if(StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
			SQL.append(" AND DI.%d% like '" +filters.get("GovernmentCode")+"%' ");
		}
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.%f%");
		SQL.append(" ORDER BY \"applyInvestMon\" desc, DI.%f%");
        String sql = SQL.toString();
		if(StringUtils.isBlank(filters.get("GovernmentLevel"))){
			sql = commonUtils.formatItemKey("0", sql);
		}else{
			sql = commonUtils.formatItemKey(filters.get("GovernmentLevel"), sql);
		}
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("9",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByGovernment 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内申报审核备类型报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午10:51:45
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportByProjectType(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.LV1_ITEM_KEY  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON VINF.pro_type=DI.ITEM_KEY and DI.GROUP_NO ='3'");
		SQL.append(" where 1 = 1");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.LV1_ITEM_KEY ");
		SQL.append(" ORDER BY \"applyInvestMon\" desc, DI.LV1_ITEM_KEY");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "谋划阶段");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("3",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByProjectType 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内申报申报部门报表数据（地方发改委，中央部门，央企）
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午11:01:37
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportByDepartmentTypeLv1(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT decode(substr(DEP.DEPARTMENT_FULL_TYPE, 1, instr(DEP.DEPARTMENT_FULL_TYPE||'#', '#', 1, 1)-1),'FGW','FGW','DEPT','DEPT','CENTRE-COM','CENTRE-COM',null)  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DEPARTMENT DEP ");
		SQL.append(" ON VINF.CREATE_DEPARTMENT_GUID = DEP.DEPARTMENT_GUID");
		SQL.append(" where 1 = 1");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY decode(substr(DEP.DEPARTMENT_FULL_TYPE, 1, instr(DEP.DEPARTMENT_FULL_TYPE||'#', '#', 1, 1)-1),'FGW','FGW','DEPT','DEPT','CENTRE-COM','CENTRE-COM',null)");
		SQL.append(" ORDER BY \"applyInvestMon\" desc, decode(substr(DEP.DEPARTMENT_FULL_TYPE, 1, instr(DEP.DEPARTMENT_FULL_TYPE||'#', '#', 1, 1)-1),'FGW','FGW','DEPT','DEPT','CENTRE-COM','CENTRE-COM',null)");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode")){
				list.get(i).put("itemName", "其他");
			}else if("FGW".equals(list.get(i).get("itemCode").toString())){
				list.get(i).put("itemName", "地方发改委");
			}else if("CENTRE-COM".equals(list.get(i).get("itemCode").toString())){
				list.get(i).put("itemName", "央企");
			}else if("DEPT".equals(list.get(i).get("itemCode").toString())){
				list.get(i).put("itemName", "中央部门");
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByDepartmentTypeLv1 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内申报申报部门报表数据（发改委）
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午11:10:57
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportByDepartmentTypeLv2(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT substr(DEP.DEPARTMENT_FULL_GUID, 1, instr(DEP.DEPARTMENT_FULL_GUID||'#', '#', 1, 1)-1)  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DEPARTMENT DEP ");
		SQL.append(" ON VINF.CREATE_DEPARTMENT_GUID = DEP.DEPARTMENT_GUID");
		SQL.append(" WHERE 1 = 1 ");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
		if(filters.get("DEPARTMENTNAME").equals("地方发改委")){
			SQL.append(" AND DEP.DEPARTMENT_FULL_TYPE LIKE 'FGW%'");
		}else if(filters.get("DEPARTMENTNAME").equals("央企")){
			SQL.append(" AND DEP.DEPARTMENT_FULL_TYPE LIKE 'CENTRE-COM%'");
		}else if(filters.get("DEPARTMENTNAME").equals("中央部门")){
			SQL.append(" AND DEP.DEPARTMENT_FULL_TYPE LIKE 'DEPT%'");
		}
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY substr(DEP.DEPARTMENT_FULL_GUID, 1, instr(DEP.DEPARTMENT_FULL_GUID||'#', '#', 1, 1)-1)");
		SQL.append(" ORDER BY \"applyInvestMon\" desc, substr(DEP.DEPARTMENT_FULL_GUID, 1, instr(DEP.DEPARTMENT_FULL_GUID||'#', '#', 1, 1)-1)");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getDepNameById((String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByDepartmentTypeLv2 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内申报委内司局报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午11:19:31
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportByWNSJ(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT substr(DEP.DEPARTMENT_FULL_GUID, 1, instr(DEP.DEPARTMENT_FULL_GUID||'#', '#', 1, 1)-1)  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DEPARTMENT DEP ");
		SQL.append(" ON VINF.INTRANET_ACCEPT_DEPT_GUID = DEP.DEPARTMENT_GUID");
		SQL.append(" where 1 = 1 ");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY substr(DEP.DEPARTMENT_FULL_GUID, 1, instr(DEP.DEPARTMENT_FULL_GUID||'#', '#', 1, 1)-1)");
		SQL.append(" ORDER BY \"applyInvestMon\" desc, substr(DEP.DEPARTMENT_FULL_GUID, 1, instr(DEP.DEPARTMENT_FULL_GUID||'#', '#', 1, 1)-1)");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getDepNameById((String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByWNSJ 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内申报申报时间报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日上午11:26:15
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportBySubmitTime(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT TO_CHAR(VINF.SUBMIT_TIME,'YYYY-MM')  as \"itemName\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" where 1 = 1 ");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY TO_CHAR(VINF.SUBMIT_TIME,'YYYY-MM')");	
		SQL.append(" ORDER BY TO_CHAR(VINF.SUBMIT_TIME,'YYYY-MM')");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemName") ||"".equals(list.get(i).get("itemName"))){
				list.get(i).put("itemName", "其他");
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportBySubmitTime 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内下达地图
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:10:01
	 */
	@Override
	public List<Map<String, Object>> getBudgetIssuedByMap(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.cur_allocated,0)) as \"issuedInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
        SQL.append(" FROM v_budget_issued_wuwei1 VINF ");
        SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
        SQL.append(" ON VINF.Build_Place=DI.item_key and DI.GROUP_NO ='1'");
        SQL.append(" where 1 = 1 ");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
        //下达年份
        if(StringUtils.isNotBlank(filters.get("Year"))){
        	SQL.append(" AND VINF.issued_year = '"+filters.get("Year")+"'");
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.%f%");
		// 金额排序		
        SQL.append(" ORDER BY \"investMon\" desc, DI.%f%");
        String sql;
		if(StringUtils.isBlank(filters.get("BuildPlaceLevel"))){
			sql = commonUtils.formatItemKey("0", SQL.toString());
		}else{
			sql = commonUtils.formatItemKey(filters.get("BuildPlaceLevel"), SQL.toString());
		}
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "00".equals(list.get(i).get("itemCode").toString().substring(0, 2))){
				list.get(i).put("itemName", "其他");
			}else if("99".equals(list.get(i).get("itemCode").toString().substring(0, 2))){
				list.get(i).put("itemName", "跨省区");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("1",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByMap 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内下达发改委行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:17:05
	 */
	@Override
	public List<Map<String, Object>> getBudgetIssuedByIndustry(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.cur_allocated,0)) as \"issuedInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_issued_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON VINF.INDUSTRY=DI.ITEM_KEY and DI.GROUP_NO ='8'");
		SQL.append(" where 1 = 1 ");
		//存在下钻的code
		if(StringUtils.isNotEmpty(filters.get("IndustryCode"))){
			SQL.append(" AND DI.%d% like '" +filters.get("IndustryCode")+"%' ");
		}
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
        //下达年份
        if(StringUtils.isNotBlank(filters.get("Year"))){
        	SQL.append(" AND VINF.issued_year = '"+filters.get("Year")+"'");
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.%f%");
		SQL.append(" ORDER BY \"issuedInvestMon\" desc, DI.%f%");
		String sql = null;
		if(StringUtils.isBlank(filters.get("IndustryLevel"))){
			sql = commonUtils.formatItemKey("0", SQL.toString());
		}else{
			sql = commonUtils.formatItemKey(filters.get("IndustryLevel"), SQL.toString());
		}        
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("8",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetIssuedByIndustry 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内下达国标行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:20:01
	 */
	@Override
	public List<Map<String, Object>> getBudgetIssuedByGBIndustry(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.cur_allocated,0)) as \"issuedInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_issued_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON VINF.GB_INDUSTRY=DI.ITEM_KEY and DI.GROUP_NO ='2'");
		SQL.append(" where 1 = 1 ");
		//存在下钻的code
		if(StringUtils.isNotEmpty(filters.get("GBIndustryCode"))){
			SQL.append(" AND DI.%d% like '" +filters.get("GBIndustryCode")+"%' ");
		}
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
        //下达年份
        if(StringUtils.isNotBlank(filters.get("Year"))){
        	SQL.append(" AND VINF.issued_year = '"+filters.get("Year")+"'");
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.%f%");
		SQL.append(" ORDER BY \"issuedInvestMon\" desc, DI.%f%");
		String sql = null;
		if(StringUtils.isBlank(filters.get("GBIndustryLevel"))){
			sql = commonUtils.formatItemKey("0", SQL.toString());
		}else{
			sql = commonUtils.formatItemKey(filters.get("GBIndustryLevel"), SQL.toString());
		}  
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("2",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetIssuedByGBIndustry 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内下达重大战略报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:21:52
	 */
	@Override
	public List<Map<String, Object>> getBudgetIssuedByMajor(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.LV1_ITEM_KEY  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.cur_allocated,0)) as \"issuedInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_issued_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON VINF.MAJOR_STRATEGY=DI.ITEM_KEY and DI.GROUP_NO ='14'");
		SQL.append(" where 1 = 1 ");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
        //下达年份
        if(StringUtils.isNotBlank(filters.get("Year"))){
        	SQL.append(" AND VINF.issued_year = '"+filters.get("Year")+"'");
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.LV1_ITEM_KEY ");
		SQL.append(" ORDER BY \"issuedInvestMon\" desc, DI.LV1_ITEM_KEY ");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("14",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetIssuedByMajor 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内下达政府投资方向报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:24:14
	 */
	@Override
	public List<Map<String, Object>> getBudgetIssuedByGovernment(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.cur_allocated,0)) as \"issuedInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_issued_wuwei1 VINF ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON VINF.GOVERNMENT_INVEST_DIRECTION=DI.ITEM_KEY and DI.GROUP_NO ='9'");
		SQL.append(" where 1 = 1 ");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
		//存在下钻的code
		if(StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
		SQL.append(" AND DI.%d% like '" +filters.get("GovernmentCode")+"%' ");
		}
        //下达年份
        if(StringUtils.isNotBlank(filters.get("Year"))){
        	SQL.append(" AND VINF.issued_year = '"+filters.get("Year")+"'");
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.%f%");
		SQL.append(" ORDER BY \"issuedInvestMon\" desc, DI.%f%");
		String sql =null;
		if(StringUtils.isBlank(filters.get("GovernmentLevel"))){
			sql = commonUtils.formatItemKey("0", SQL.toString());
		}else{
			sql = commonUtils.formatItemKey(filters.get("GovernmentLevel"), SQL.toString());
		}
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("9",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetIssuedByGovernment 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内下达委内司局报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:26:14
	 */
	@Override
	public List<Map<String, Object>> getBudgetIssuedByWNSJ(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT substr(DEP.DEPARTMENT_FULL_GUID, 1, instr(DEP.DEPARTMENT_FULL_GUID||'#', '#', 1, 1)-1)  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.cur_allocated,0)) as \"issuedInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM V_BUDGET_Issued_WUWEI1 VINF ");
		SQL.append(" LEFT JOIN DEPARTMENT DEP ");
		SQL.append(" ON VINF.INTRANET_ACCEPT_DEPT_GUID = DEP.DEPARTMENT_GUID");
		SQL.append(" where 1 = 1 ");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
        //下达年份
        if(StringUtils.isNotBlank(filters.get("Year"))){
        	SQL.append(" AND VINF.issued_year = '"+filters.get("Year")+"'");
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY substr(DEP.DEPARTMENT_FULL_GUID, 1, instr(DEP.DEPARTMENT_FULL_GUID||'#', '#', 1, 1)-1)");
		SQL.append(" ORDER BY \"issuedInvestMon\" desc, substr(DEP.DEPARTMENT_FULL_GUID, 1, instr(DEP.DEPARTMENT_FULL_GUID||'#', '#', 1, 1)-1)");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getDepNameById((String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetIssuedByWNSJ 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内下达下达时间报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年10月24日下午12:27:37
	 */
	@Override
	public List<Map<String, Object>> getBudgetIssuedByIssuedTime(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append(" SELECT TO_CHAR(VINF.ISSUSED_TIME,'YYYY-MM')  as \"itemName\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.cur_allocated,0)) as \"issuedInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_issued_wuwei1 VINF ");
		SQL.append(" where 1 = 1 ");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
        //下达年份
        if(StringUtils.isNotBlank(filters.get("Year"))){
        	SQL.append(" AND VINF.issued_year = '"+filters.get("Year")+"'");
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY TO_CHAR(VINF.ISSUSED_TIME,'YYYY-MM')");	
		SQL.append(" ORDER BY TO_CHAR(VINF.ISSUSED_TIME,'YYYY-MM')");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemName") ||"".equals(list.get(i).get("itemName"))){
				list.get(i).put("itemName", "其他");
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetIssuedByIssuedTime 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}
	
	/**
	 * 中央预算内调度分地区报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:37
	 */
	@Override
	public List<Map<String, Object>> getBudgetPlaceDispatch(
			Map<String,String> filters,String querySql,String searchSql,String orderbySql) throws BusinessServiceException {

		StringBuilder SQL = new StringBuilder();
		SQL.append(" select  ");
		SQL.append(" SUBSTR(VINF.build_place, 0, @@@)||'####'  as \"itemCode\",  ");//-- 建设地点
		SQL.append(" sum(VINF.INVESTMENT_TOTAL)/10000 as investMon, ");// ---中央预算内总投资（亿元）
		SQL.append(" sum(VINF.totalD01)/10000 as investMon, ");// ---合计总投资（亿元）
		SQL.append(" count(VINF.YEAR_PLAN_PROJECT_ID) as cnt, ");//---被调度项目ID
		SQL.append(" sum(VINF.A00016D08)/10000 as investMon1,  ");//---中央预算内累计下达资金（亿元）
		SQL.append(" sum(VINF.A00016D16)/10000 as investMon2, ");//---中央预算内本次下达资金（亿元）
		SQL.append(" sum(VINF.A00016WC)/10000 as investMon3, ");//---中央预算内本报告期完成（亿元）
		SQL.append(" sum(VINF.A00016DW)/10000 as investMon4, ");//---中央预算内本报告期到位（亿元）
		SQL.append(" sum(VINF.A00016ZF)/10000 as investMon5, ");//---中央预算内本报告期支付（亿元）
		SQL.append(" sum(VINF.actual_start_time) as startCnt, ");//---开工个数
		SQL.append(" sum(VINF.actual_end_time) as  endCnt ");// ---完工个数				
		SQL.append(" from v_dispatch_child VINF  ");
		SQL.append(" where VINF.PROFLAG != '2' ");
	    //拼接页面查询条件
	    SQL.append(querySql);
	    String zeroNun="0000";
	    Integer num=2;
	    String filterSql="";
	    String code=filters.get(Constant.BUILD_PLACE_GROUPNO);
	    if(StringUtils.isNotBlank(code)){
		    if(Arrays.asList(Constant.ARRAY).contains(code)){
				// 下钻过滤
				if ("0000".equals(code.substring(2, 6))) {
					// 截取位数
			    	zeroNun="";
			    	num=6;
					SQL.append(" AND VINF.BUILD_PLACE like '" + code.substring(0, 2) + "%'");
				}// 展现第二级
				else if ("".equals(code.substring(6, 6))) {
					// 截取位数
			    	zeroNun="";
			    	num=6;
			    	filterSql=" AND VINF.BUILD_PLACE like '" +code.substring(0, 6)+"%'";
				}
		    }else{
			    // 展现第一级
			    if("0000".equals(code.substring(2, 6))){
			    	// 截取位数
			    	zeroNun="00";
			    	num=4;
			    	filterSql=" AND VINF.BUILD_PLACE like '" +code.substring(0,2)+"%'";
			    }// 展现第二级
			    else if("00".equals(code.substring(4, 6))){
			    	// 截取位数
			    	zeroNun="";
			    	num=6;
			    	//申报单位
			    	filterSql=" AND VINF.BUILD_PLACE like '" +code.substring(0,4)+"%'";	    	 
			    } // 展现第三级
			    else if("".equals(code.substring(6, 6))){
			    	// 截取位数
			    	zeroNun="";
			    	num=6;
			    	filterSql=" AND VINF.BUILD_PLACE like '" +code.substring(0,6)+"%'";
			    }
		    }
	    }
	    SQL.append(filterSql);
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY SUBSTR(VINF.BUILD_PLACE,0,@@@)||'####'");
        SQL.append(" ORDER BY SUBSTR(VINF.BUILD_PLACE,0,@@@)||'####'  asc ");
        String sql=SQL.toString().replaceAll("@@@", num.toString()).replaceAll("####", zeroNun);
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode")){
				list.get(i).put("itemName", "其他");
			}else if("99".equals(list.get(i).get("itemCode").toString().substring(0, 2))){
				list.get(i).put("itemName", "跨省区");
			}else {
				list.get(i).put("itemName", Cache.getNameByCode("1",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("RollPlanServiceImpl getBudgetEndtimeDispatch 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
	}

	
	/**
	 * 中央预算内调度分拟建成时间报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:37
	 */
	@Override
	public List<Map<String, Object>> getBudgetEndtimeDispatch(
			Map<String,String> filters,String querySql,String searchSql,String orderbySql) throws BusinessServiceException {
		
		StringBuilder SQL = new StringBuilder();
		SQL.append(" select  ");
				SQL.append(" VINF.END_TIME  as \"itemName\", ");//---竣工或者拟建成时间
				//SQL.append(" sum(VINF.totalD01)/10000 as investMon, ");// ---合计总投资（亿元）
				SQL.append(" sum(VINF.INVESTMENT_TOTAL)/10000 as investMon, ");// ---中央预算内总投资（亿元）
				SQL.append(" count(VINF.YEAR_PLAN_PROJECT_ID) as cnt, ");//---被调度项目ID
				SQL.append(" sum(VINF.A00016D08)/10000 as investMon1,  ");//---中央预算内累计下达资金（亿元）
				SQL.append(" sum(VINF.A00016D16)/10000 as investMon2, ");//---中央预算内本次下达资金（亿元）
				SQL.append(" sum(VINF.A00016WC)/10000 as investMon3, ");//---中央预算内本报告期完成（亿元）
				SQL.append(" sum(VINF.A00016DW)/10000 as investMon4, ");//---中央预算内本报告期到位（亿元）
				SQL.append(" sum(VINF.A00016ZF)/10000 as investMon5, ");//---中央预算内本报告期支付（亿元）
				SQL.append(" sum(VINF.actual_start_time) as startCnt, ");//---开工个数
				SQL.append(" sum(VINF.actual_end_time) as  endCnt ");// ---完工个数
				
		SQL.append(" from v_dispatch_child VINF  ");
		SQL.append(" where VINF.PROFLAG != '2' ");
		SQL.append(searchSql); //获取过滤条件
	    //拼接页面查询条件
	    SQL.append(querySql);
		SQL.append(" group by VINF.END_TIME  ");//---竣工或者拟建成时间
		//根据前端传来的参数判断按相应的字段排序
		SQL.append(" order by VINF.END_TIME asc ");
		
		

		
		
		String sql=SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		long endMilis=System.currentTimeMillis();
		logger.debug("RollPlanServiceImpl getBudgetEndtimeDispatch 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
		
		
	}

	
	/**
	 * 中央预算内调度分形象进度报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:37
	 */
	@Override
	public List<Map<String, Object>> getBudgetImageprogressDispatch(
			Map<String,String> filters,String querySql,String searchSql,String orderbySql) throws BusinessServiceException {
			StringBuilder SQL = new StringBuilder();
			SQL.append(" select  ");
					SQL.append(" VINF.IMAGE_PROGRESS as \"itemCode\", ");//---竣工或者拟建成时间
					//SQL.append(" sum(VINF.totalD01)/10000 as investMon, ");// ---合计总投资（亿元）
					SQL.append(" sum(VINF.INVESTMENT_TOTAL)/10000 as investMon, ");// ---中央预算内总投资（亿元）
					SQL.append(" count(VINF.YEAR_PLAN_PROJECT_ID) as cnt, ");//---被调度项目ID
					SQL.append(" sum(VINF.A00016D08)/10000 as investMon1,  ");//---中央预算内累计下达资金（亿元）
					SQL.append(" sum(VINF.A00016D16)/10000 as investMon2, ");//---中央预算内本次下达资金（亿元）
					SQL.append(" sum(VINF.A00016WC)/10000 as investMon3, ");//---中央预算内本报告期完成（亿元）
					SQL.append(" sum(VINF.A00016DW)/10000 as investMon4, ");//---中央预算内本报告期到位（亿元）
					SQL.append(" sum(VINF.A00016ZF)/10000 as investMon5, ");//---中央预算内本报告期支付（亿元）
					SQL.append(" sum(VINF.actual_start_time) as startCnt, ");//---开工个数
					SQL.append(" sum(VINF.actual_end_time) as  endCnt ");// ---完工个数
					
			SQL.append(" from v_dispatch_child VINF  ");
			SQL.append(" where VINF.PROFLAG != '2' ");
			SQL.append(searchSql); //获取过滤条件
		    //拼接页面查询条件
		    SQL.append(querySql);
			SQL.append(" group by VINF.IMAGE_PROGRESS  ");//---竣工或者拟建成时间
			//根据前端传来的参数判断按相应的字段排序
			
			//SQL.append(" order by sum(VINF.totalD01) desc ");
			
			
			if("0".equals(orderbySql)){
				SQL.append(" order by count(VINF.YEAR_PLAN_PROJECT_ID) desc  ");
			}else if("3".equals(orderbySql)){
				SQL.append(" order by sum(VINF.A00016D16) desc  "); 
			}else{
				SQL.append(" order by sum(VINF.INVESTMENT_TOTAL) desc ");
			}
			
			
			String sql=SQL.toString();
			long startMilis=System.currentTimeMillis();
			//执行查询并将结果转化为ListMap
			List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
			for(int i=0;i<list.size();i++){
				if(null == list.get(i).get("itemCode")){
					list.get(i).put("itemName", "其他");
				}else {
					list.get(i).put("itemName", Cache.getNameByCode("17",(String)list.get(i).get("itemCode")));
				}
			}
			long endMilis=System.currentTimeMillis();
			logger.debug("RollPlanServiceImpl getBudgetImageprogressDispatch 方法执行查询花费毫秒数:" + (endMilis-startMilis));
			return list;
	}

	
	
	/**
	 * 中央预算内调度分国标行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:37
	 */
	@Override
	public List<Map<String, Object>> getBudgetGbindustrydispatch(
			Map<String,String> filters,String querySql,String searchSql,String orderbySql) throws BusinessServiceException {
			StringBuilder SQL = new StringBuilder();
			//参数的序号表示分相应的级别显示
			String kkk=filters.get("GBIndustryLevel");
			//需要查询的维度级别
			String proStageStr = " VINF.GB_INDUSTRY ";//-- 国标行业
			//获取分国标行业统计项目信息
			if(null==kkk){
				//获取第一级item_full_key
				proStageStr="substr(di1.item_full_key, 1, instr(di1.item_full_key||'-', '-', 1, 1)-1)   ";
			}else if("1".equals(kkk)){
				//获取第二级
				proStageStr="substr(di1.item_full_key, instr(di1.item_full_key||'-', '-', 1, 1)+1, instr(di1.item_full_key||'-', '-', 1, 2)-instr(di1.item_full_key||'-', '-', 1, 1)-1) ";
			}else if("2".equals(kkk)){
				//获取第三级
				proStageStr="substr(di1.item_full_key, instr(di1.item_full_key||'-', '-', 1, 2)+1, instr(di1.item_full_key||'-', '-', 1, 3)-instr(di1.item_full_key||'-', '-', 1, 2)-1) ";
			}else{
				//获取第四级
				
			}
			
			
			//获取分国标行业统计项目信息
			SQL.append(" select  ");
					SQL.append(proStageStr+" as \"itemCode\",  ");//-- 国标行业
					//SQL.append(" sum(VINF.totalD01)/10000 as investMon, ");// ---合计总投资（亿元）
					SQL.append(" sum(VINF.INVESTMENT_TOTAL)/10000 as investMon, ");// ---中央预算内总投资（亿元）
					SQL.append(" count(VINF.YEAR_PLAN_PROJECT_ID) as cnt, ");//---被调度项目ID
					SQL.append(" sum(VINF.A00016D08)/10000 as investMon1,  ");//---中央预算内累计下达资金（亿元）
					SQL.append(" sum(VINF.A00016D16)/10000 as investMon2, ");//---中央预算内本次下达资金（亿元）
					SQL.append(" sum(VINF.A00016WC)/10000 as investMon3, ");//---中央预算内本报告期完成（亿元）
					SQL.append(" sum(VINF.A00016DW)/10000 as investMon4, ");//---中央预算内本报告期到位（亿元）
					SQL.append(" sum(VINF.A00016ZF)/10000 as investMon5, ");//---中央预算内本报告期支付（亿元）
					SQL.append(" sum(VINF.actual_start_time) as startCnt, ");//---开工个数
					SQL.append(" sum(VINF.actual_end_time) as  endCnt ");// ---完工个数
					
			SQL.append(" from v_dispatch_child VINF  ");
			SQL.append(" left join dictionary_items di1 on VINF.GB_INDUSTRY = di1.item_key and di1.group_no='2' ");
			SQL.append(" where VINF.PROFLAG != '2' ");
			SQL.append(searchSql); //获取过滤条件
		    //拼接页面查询条件
		    SQL.append(querySql);
			SQL.append(" group by "+proStageStr); //-- 国标行业
			//根据前端传来的参数判断按相应的字段排序
			//SQL.append(" order by sum(VINF.totalD01) desc ");
			
			
			if("0".equals(orderbySql)){
				SQL.append(" order by count(VINF.YEAR_PLAN_PROJECT_ID) desc  ");
			}else if("3".equals(orderbySql)){
				SQL.append(" order by sum(VINF.A00016D16) desc  "); 
			}else{
				SQL.append(" order by sum(VINF.INVESTMENT_TOTAL) desc ");
			}
			
			
			String sql=SQL.toString();
			long startMilis=System.currentTimeMillis();
			//执行查询并将结果转化为ListMap
			List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
			for(int i=0;i<list.size();i++){
				if(null == list.get(i).get("itemCode")){
					list.get(i).put("itemName", "其他");
				}else {
					list.get(i).put("itemName", Cache.getNameByCode("2",(String)list.get(i).get("itemCode")));
				}
			}
			long endMilis=System.currentTimeMillis();
			logger.debug("RollPlanServiceImpl getBudgetGbindustrydispatch 方法执行查询花费毫秒数:" + (endMilis-startMilis));
			return list;
	}

	
	/**
	 * 中央预算内调度分所属行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:37
	 */
	@Override
	public List<Map<String, Object>> getBudgetIndustryDispatch(
			Map<String,String> filters,String querySql,String searchSql,String orderbySql) throws BusinessServiceException {
			StringBuilder SQL = new StringBuilder();
			//参数的序号表示分相应的级别显示
			String kkk=filters.get("IndustryLevel");
			//需要查询的维度级别
			String proStageStr = " VINF.INDUSTRY ";//-- 所属行业
			//获取分所属行业统计项目信息
			if(null==kkk){
				//获取第一级item_full_key
				proStageStr="substr(di1.item_full_key, 1, instr(di1.item_full_key||'-', '-', 1, 1)-1)   ";
			}else{
				//获取第二级
				
			}
			SQL.append(" select  "); 
					SQL.append(proStageStr + " as \"itemCode\",  "); //-- 所属行业
					//SQL.append(" sum(VINF.totalD01)/10000 as investMon, ");// ---合计总投资（亿元）
					SQL.append(" sum(VINF.INVESTMENT_TOTAL)/10000 as investMon, ");// ---中央预算内总投资（亿元）
					SQL.append(" count(VINF.YEAR_PLAN_PROJECT_ID) as cnt, ");//---被调度项目ID
					SQL.append(" sum(VINF.A00016D08)/10000 as investMon1,  ");//---中央预算内累计下达资金（亿元）
					SQL.append(" sum(VINF.A00016D16)/10000 as investMon2, ");//---中央预算内本次下达资金（亿元）
					SQL.append(" sum(VINF.A00016WC)/10000 as investMon3, ");//---中央预算内本报告期完成（亿元）
					SQL.append(" sum(VINF.A00016DW)/10000 as investMon4, ");//---中央预算内本报告期到位（亿元）
					SQL.append(" sum(VINF.A00016ZF)/10000 as investMon5, ");//---中央预算内本报告期支付（亿元）
					SQL.append(" sum(VINF.actual_start_time) as startCnt, ");//---开工个数
					SQL.append(" sum(VINF.actual_end_time) as  endCnt ");// ---完工个数
					
			SQL.append(" from v_dispatch_child VINF  ");
			SQL.append(" left join dictionary_items di1 on VINF.INDUSTRY = di1.item_key and di1.group_no='8' ");
			SQL.append(" where VINF.PROFLAG != '2' ");
			SQL.append(searchSql); //获取过滤条件
		    //拼接页面查询条件
		    SQL.append(querySql);
			SQL.append(" group by "+proStageStr); //-- 所属行业
			//根据前端传来的参数判断按相应的字段排序
			//SQL.append(" order by sum(VINF.totalD01) desc ");
			
			
			if("0".equals(orderbySql)){
				SQL.append(" order by count(VINF.YEAR_PLAN_PROJECT_ID) desc  ");
			}else if("3".equals(orderbySql)){
				SQL.append(" order by sum(VINF.A00016D16) desc  "); 
			}else{
				SQL.append(" order by sum(VINF.INVESTMENT_TOTAL) desc ");
			}
			
			
			String sql=SQL.toString();
			long startMilis=System.currentTimeMillis();
			//执行查询并将结果转化为ListMap
			List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
			for(int i=0;i<list.size();i++){
				if(null == list.get(i).get("itemCode")){
					list.get(i).put("itemName", "其他");
				}else {
					list.get(i).put("itemName", Cache.getNameByCode("8",(String)list.get(i).get("itemCode")));
				}
			}
			long endMilis=System.currentTimeMillis();
			logger.debug("RollPlanServiceImpl getBudgetIndustryDispatch 方法执行查询花费毫秒数:" + (endMilis-startMilis));
			return list;
	}

	
	
	/**
	 * 中央预算内调度分政府投资方向
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月24日下午12:27:37
	 */
	@Override
	public List<Map<String, Object>> getBudgetGovinvestdirectiondispatch(
			Map<String,String> filters,String querySql,String searchSql,String orderbySql) throws BusinessServiceException {
			StringBuilder SQL = new StringBuilder();
			//参数的序号表示分相应的级别显示
			String kkk=filters.get("GovernmentLevel");
			//需要查询的维度级别
			String proStageStr = " VINF.GOVERNMENT_INVEST_DIRECTION ";
			//获取分政府投资方向统计项目信息
			SQL.append(" select ");
			//SQL.append(" VINF.GOVERNMENT_INVEST_DIRECTION,-- 政府投资方向 ");
			if(null==kkk){
				//获取第一级item_full_key
				proStageStr="substr(di1.item_full_key, 1, instr(di1.item_full_key||'-', '-', 1, 1)-1) ";
			}else if("1".equals(kkk)){
				//获取第二级
				proStageStr="substr(di1.item_full_key, instr(di1.item_full_key||'-', '-', 1, 1)+1, instr(di1.item_full_key||'-', '-', 1, 2)-instr(di1.item_full_key||'-', '-', 1, 1)-1) ";
			}else if("2".equals(kkk)){
				//获取第三级
				proStageStr="substr(di1.item_full_key, instr(di1.item_full_key||'-', '-', 1, 2)+1, instr(di1.item_full_key||'-', '-', 1, 3)-instr(di1.item_full_key||'-', '-', 1, 2)-1) ";
			}else{
				//获取第四级
				
			}
			
			SQL.append(proStageStr+" as \"itemCode\",  ");
					//SQL.append(" sum(VINF.totalD01)/10000 as investMon, ");// ---合计总投资（亿元）
					SQL.append(" sum(VINF.INVESTMENT_TOTAL)/10000 as investMon, ");// ---中央预算内总投资（亿元）
					SQL.append(" count(VINF.YEAR_PLAN_PROJECT_ID) as cnt, ");//---被调度项目ID
					SQL.append(" sum(VINF.A00016D08)/10000 as investMon1,  ");//---中央预算内累计下达资金（亿元）
					SQL.append(" sum(VINF.A00016D16)/10000 as investMon2, ");//---中央预算内本次下达资金（亿元）
					SQL.append(" sum(VINF.A00016WC)/10000 as investMon3, ");//---中央预算内本报告期完成（亿元）
					SQL.append(" sum(VINF.A00016DW)/10000 as investMon4, ");//---中央预算内本报告期到位（亿元）
					SQL.append(" sum(VINF.A00016ZF)/10000 as investMon5, ");//---中央预算内本报告期支付（亿元）
					SQL.append(" sum(VINF.actual_start_time) as startCnt, ");//---开工个数
					SQL.append(" sum(VINF.actual_end_time) as  endCnt ");// ---完工个数
					
			SQL.append(" from v_dispatch_child VINF  ");
			SQL.append(" left join dictionary_items di1 on VINF.GOVERNMENT_INVEST_DIRECTION = di1.item_key and di1.group_no='9' ");
			SQL.append(" where VINF.PROFLAG != '2' ");
			SQL.append(searchSql); //获取过滤条件
		    //拼接页面查询条件
		    SQL.append(querySql);
			SQL.append("group by " +proStageStr);
			//根据前端传来的参数判断按相应的字段排序
			//SQL.append(" order by sum(VINF.totalD01) desc ");
			
			
			if("0".equals(orderbySql)){
				SQL.append(" order by count(VINF.YEAR_PLAN_PROJECT_ID) desc  ");
			}else if("3".equals(orderbySql)){
				SQL.append(" order by sum(VINF.A00016D16) desc  "); 
			}else{
				SQL.append(" order by sum(VINF.INVESTMENT_TOTAL) desc ");
			}
			
			
			String sql=SQL.toString();
			long startMilis=System.currentTimeMillis();
			//执行查询并将结果转化为ListMap
			List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
			for(int i=0;i<list.size();i++){
				if(null == list.get(i).get("itemCode")){
					list.get(i).put("itemName", "其他");
				}else {
					list.get(i).put("itemName", Cache.getNameByCode("9",(String)list.get(i).get("itemCode")));
				}
			}
			long endMilis=System.currentTimeMillis();
			logger.debug("RollPlanServiceImpl getBudgetGovinvestdirectiondispatch 方法执行查询花费毫秒数:" + (endMilis-startMilis));
			return list;
	}

	/**
	 * 中央预算内申报投资规模
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月23日下午7:01:52
	 */
	@Override
	public List<Map<String, Object>> getBudgetReportByTZGM(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT VINF.TZGM  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.apply_captial_2017,0)) as \"applyInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_report_wuwei1 VINF ");
		SQL.append(" where 1= 1");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY VINF.TZGM");
		SQL.append(" ORDER BY \"applyInvestMon\" desc, VINF.TZGM");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			list.get(i).put("itemName", list.get(i).get("itemCode"));
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetReportByTZGM 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 中央预算内下达投资规模
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月23日下午7:04:56
	 */
	@Override
	public List<Map<String, Object>> getBudgetIssuedByTZGM(
			Map<String,String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT VINF.TZGM  as \"itemCode\",");
		SQL.append(" SUM(NVL(VINF.INVESTMENT_TOTAL,0)) as \"investMon\",");
		SQL.append(" SUM(NVL(VINF.TOTAL_INVESTMENT,0)) as \"budgetInvestMon\",");
		SQL.append(" SUM(NVL(VINF.cur_allocated,0)) as \"issuedInvestMon\",");
		SQL.append(" COUNT(VINF.ID) as \"cnt\"");
		SQL.append(" FROM v_budget_issued_wuwei1 VINF ");
		SQL.append(" where 1 = 1 ");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
        //下达年份
        if(StringUtils.isNotBlank(filters.get("Year"))){
        	SQL.append(" AND VINF.issued_year = '"+filters.get("Year")+"'");
        }
	    //拼接页面查询条件
	    SQL.append(querySql);
        SQL.append(" GROUP BY VINF.TZGM ");
		SQL.append(" ORDER BY \"issuedInvestMon\" desc, VINF.TZGM ");
        String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			list.get(i).put("itemName", list.get(i).get("itemCode"));
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("BudgetServiceImpl getBudgetIssuedByTZGM 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}
}
