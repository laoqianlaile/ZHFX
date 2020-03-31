package com.strongit.iss.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.common.Cache;
import com.strongit.iss.common.Constant;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IRollPlanService;

import edu.emory.mathcs.backport.java.util.Arrays;

@Service
@Transactional
public class RollPlanServiceImpl extends BaseService implements IRollPlanService{

    @Autowired
    private ReportCacheServiceImpl reportCacheService;
	
	@Override
	/**
	 * 三年滚动计划地图报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:56:15
	 */
	public List<Map<String, Object>> getRollPlanPlaceReportByMap(
			Map<String, String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
        
        SQL.append(" select ");
        SQL.append(" SUBSTR(vinf.build_place, 0, @@@)||'####'  as \"itemCode\",  ");//-- 建设地点
		        SQL.append(" count(plan_project_id) as \"cnt\",   ");//-- 项目个数 
		        SQL.append(" sum(INVESTMENT_TOTAL)/10000  as \"investMon1\",  ");//-- 总投资（亿元）
		        SQL.append(" sum(A00180SN)/10000 as \"investMon2\", ");//--   未来三年资金需求（亿元）
		        SQL.append(" sum(A001802017)/10000 as \"investMon3\", ");//--   2017年资金需求（亿元）
		        SQL.append(" sum(A001802018)/10000 as \"investMon4\", ");// --   2018年资金需求（亿元）
		        SQL.append(" sum(A001802019)/10000 as \"investMon5\", ");// --   2019年资金需求（亿元）
		        SQL.append(" sum(vinf.A00016TotalMenoy)/10000 as \"A00016TotalMenoy\", ");//--   中央预算内投资（亿元）
		        SQL.append(" sum(vinf.A00018TotalMenoy)/10000 as \"A00018TotalMenoy\"  ");//--   专项建设基金投资（亿元）
		SQL.append(" from v_roll_plan_child vinf  where 1=1 ");
		SQL.append(searchSql); //获取过滤条件
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
					SQL.append(" AND vinf.BUILD_PLACE like '" + code.substring(0, 2) + "%'");
				}// 展现第二级
				else if ("".equals(code.substring(6, 6))) {
					// 截取位数
			    	zeroNun="";
			    	num=6;
			    	filterSql=" AND vinf.BUILD_PLACE like '" +code.substring(0, 6)+"%'";
				}
		    }else{
			    // 展现第一级
			    if("0000".equals(code.substring(2, 6))){
			    	// 截取位数
			    	zeroNun="00";
			    	num=4;
			    	filterSql=" AND vinf.BUILD_PLACE like '" +code.substring(0,2)+"%'";
			    }// 展现第二级
			    else if("00".equals(code.substring(4, 6))){
			    	// 截取位数
			    	zeroNun="";
			    	num=6;
			    	//申报单位
			    	filterSql=" AND vinf.BUILD_PLACE like '" +code.substring(0,4)+"%'";	    	 
			    } // 展现第三级
			    else if("".equals(code.substring(6, 6))){
			    	// 截取位数
			    	zeroNun="";
			    	num=6;
			    	filterSql=" AND vinf.BUILD_PLACE like '" +code.substring(0,6)+"%'";
			    }
		    }
	    }
	    SQL.append(filterSql);
        SQL.append(" GROUP BY SUBSTR(vinf.BUILD_PLACE,0,@@@)||'####'");
        SQL.append(" ORDER BY SUBSTR(vinf.BUILD_PLACE,0,@@@)||'####'  asc ");
//		// 个数排序
//		if(Constant.ORDERBY_CNT.equals(filters.get(Constant.ORDERBY))){			
//		    SQL.append(" ORDER BY \"cnt\" desc, SUBSTR(vinf.BUILD_PLACE,0,@@@)||'####' ");
//		}
//		// 金额排序
//		else if(Constant.ORDERBY_MON.equals(filters.get(Constant.ORDERBY))){		
//			 SQL.append(" ORDER BY \"investMon\" desc, SUBSTR(vinf.BUILD_PLACE,0,@@@)||'####' ");
//		}
//		//资金需求
//		else if("orderbyapply".equals(filters.get(Constant.ORDERBY))){
//			SQL.append(" ORDER BY \"applyInvestMon\" desc, SUBSTR(vinf.BUILD_PLACE,0,@@@)||'####' ");
//		}
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
		logger.debug("OverViewServiceImpl getRollPlanPlaceReportByMap 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	
	
	/**
	 * 三年滚动计划政府投资方向报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:56:15
	 */
	@Override
	public List<Map<String, Object>> getRollPlanGovernmentReportByMap(
			Map<String, String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		//参数的序号表示分相应的级别显示
		String kkk=filters.get("GovernmentLevel");  
		//需要查询的维度级别
		String proStageStr = " vinf.GOVERNMENT_INVEST_DIRECTION ";
		//获取分政府投资方向统计项目信息
		SQL.append(" select ");
		//SQL.append(" vinf.GOVERNMENT_INVEST_DIRECTION,-- 政府投资方向 ");
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
		SQL.append(" count(plan_project_id) as \"cnt\",  "); //项目个数 
		SQL.append(" sum(INVESTMENT_TOTAL)/10000  as \"investMon1\", ");//总投资（亿元） 
		SQL.append(" sum(A00180SN)/10000 as \"investMon2\", ");  //未来三年资金需求（亿元） 
		SQL.append(" sum(A001802017)/10000 as \"investMon3\",  "); //2017年资金需求（亿元）
		SQL.append(" sum(A001802018)/10000 as \"investMon4\", ");//2018年资金需求（亿元）
		SQL.append(" sum(A001802019)/10000 as \"investMon5\",  ");//2019年资金需求（亿元）
		SQL.append(" sum(vinf.A00016TotalMenoy)/10000 as \"A00016TotalMenoy\",");//中央预算内投资（亿元） 
		SQL.append(" sum(vinf.A00018TotalMenoy)/10000 as \"A00018TotalMenoy\"   ");//专项建设基金投资（亿元）
		SQL.append(" from v_roll_plan_child vinf   ");
		SQL.append(" left join dictionary_items di1 on vinf.GOVERNMENT_INVEST_DIRECTION = di1.item_key and di1.group_no='9' ");
		SQL.append(" where 1=1 ");
		SQL.append(searchSql); //获取过滤条件
		SQL.append(querySql);
		SQL.append("group by " +proStageStr);

        //根据前端传来的参数判断按相应的字段排序
		if("0".equals(orderbySql)){
			SQL.append(" order by count(plan_project_id) desc  ");
		}else if("2".equals(orderbySql)){
			SQL.append(" order by sum(A00180SN) desc  "); 
		}else{
			SQL.append(" order by sum(INVESTMENT_TOTAL) desc ");
		}
		
		String sql=SQL.toString();
        //sql = CommonUtils.formatYearApplyCaptionCloumn(filter		List<Map<String,Object>> 
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
		logger.debug("RollPlanServiceImpl getRollPlanGovernmentReportByMap 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
	}

	
	/**
	 * 三年滚动计划项目类型报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:56:15
	 */
	@Override
	public List<Map<String, Object>> getRollPlanProtypeReportByMap(
			Map<String, String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException {
			StringBuilder SQL = new StringBuilder();
			//获取分项目类型统计项目信息
			SQL.append(" select ");
			SQL.append(" vinf.PRO_TYPE as \"itemCode\", "); //-- 项目类型 
			SQL.append(" count(plan_project_id) as \"cnt\",  ");//-- 项目个数 
			SQL.append(" sum(INVESTMENT_TOTAL)/10000  as \"investMon1\",  ");//-- 总投资（亿元）
			SQL.append(" sum(A00180SN)/10000 as \"investMon2\",  ");//--  未来三年资金需求（亿元）
			SQL.append(" sum(A001802017)/10000 as \"investMon3\",  ");// --   2017年资金需求（亿元）
			SQL.append(" sum(A001802018)/10000 as \"investMon4\",  ");//--   2018年资金需求（亿元）
			SQL.append(" sum(A001802019)/10000 as \"investMon5\",  ");//--   2019年资金需求（亿元）
			SQL.append(" sum(vinf.A00016TotalMenoy)/10000 as \"A00016TotalMenoy\",  ");//--   中央预算内投资（亿元）
			SQL.append(" sum(vinf.A00018TotalMenoy)/10000 as \"A00018TotalMenoy\"   ");//--   专项建设基金投资（亿元）
			SQL.append(" from v_roll_plan_child vinf  ");//-- where
			SQL.append(" where 1=1 ");
			SQL.append(searchSql); //获取过滤条件
			SQL.append(querySql);
			SQL.append(" group by vinf.PRO_TYPE  ");//-- 项目类型
			//根据前端传来的参数判断按相应的字段排序
			if("0".equals(orderbySql)){
				SQL.append(" order by count(plan_project_id) desc  ");
			}else if("2".equals(orderbySql)){
				SQL.append(" order by sum(A00180SN) desc  "); 
			}else{
				SQL.append(" order by sum(INVESTMENT_TOTAL) desc ");
			}
			
			
			String sql=SQL.toString();
			long startMilis=System.currentTimeMillis();
			//执行查询并将结果转化为ListMap
			List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
			for(int i=0;i<list.size();i++){
				if(null == list.get(i).get("itemCode")){
					list.get(i).put("itemName", "谋划阶段");
				}else {
					list.get(i).put("itemName", Cache.getNameByCode("3",(String)list.get(i).get("itemCode")));
				}
			}
			long endMilis=System.currentTimeMillis();
			logger.debug("RollPlanServiceImpl getRollPlanProtypeReportByMap 方法执行查询花费毫秒数:" + (endMilis-startMilis));
			return list;
	}

	
	
	/**
	 * 三年滚动计划项目阶段报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:56:15
	 */
	@Override
	public List<Map<String, Object>> getRollPlanStageReportByMap(
			Map<String, String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		//获取分项目阶段统计项目信息
		SQL.append(" select  ");
				//SQL.append(" case when vinf.STAGE ='1' then '谋划阶段' when vinf.STAGE ='2' then '前期阶段' when vinf.STAGE ='3' then '拟开工阶段' when vinf.STAGE ='4' then '在建阶段' when vinf.STAGE ='5' then '竣工阶段' else '其他' end as \"itemName\"  , ");//-- 阶段（1-谋划阶段；2-前期阶段；3-拟开工阶段；4-在建阶段；5-竣工阶段）
		SQL.append(" case when vinf.STAGE ='1' then '谋划阶段' when vinf.STAGE ='2' then '前期阶段' when vinf.STAGE ='3' then '开工建设阶段' else '其他' end as \"itemName\"  , ");//-- 阶段（1-谋划阶段；2-前期阶段；3-拟开工阶段；4-在建阶段；5-竣工阶段）
		        SQL.append(" count(plan_project_id) as \"cnt\",  ");//-- 项目个数 
				SQL.append(" sum(INVESTMENT_TOTAL)/10000  as \"investMon1\",  ");//-- 总投资（亿元）
				SQL.append(" sum(A00180SN)/10000 as \"investMon2\",  ");//--  未来三年资金需求（亿元）
				SQL.append(" sum(A001802017)/10000 as \"investMon3\",  ");//--   2017年资金需求（亿元）
				SQL.append(" sum(A001802018)/10000 as \"investMon4\",  ");//--   2018年资金需求（亿元）
				SQL.append(" sum(A001802019)/10000 as \"investMon5\",  ");//--   2019年资金需求（亿元）
				SQL.append(" sum(vinf.A00016TotalMenoy)/10000 as \"A00016TotalMenoy\", ");//--   中央预算内投资（亿元） 
				SQL.append(" sum(vinf.A00018TotalMenoy)/10000 as \"A00018TotalMenoy\"  ");//--   专项建设基金投资（亿元） 
				SQL.append(" from v_roll_plan_child vinf   ");//-- where
				SQL.append(" where  vinf.STAGE in ('1','2','3') ");
				SQL.append(searchSql); //获取过滤条件
				SQL.append(querySql);
		SQL.append(" group by  vinf.STAGE  ");//-- 阶段（1-谋划阶段；2-前期阶段；3-拟开工阶段；4-在建阶段；5-竣工阶段）
		//根据前端传来的参数判断按相应的字段排序
		if("0".equals(orderbySql)){
			 SQL.append(" order by vinf.STAGE asc  ");
			//SQL.append(" order by count(plan_project_id) desc  ");
		}else if("2".equals(orderbySql)){
			SQL.append(" order by sum(A00180SN) desc  "); 
		}else{
			SQL.append(" order by sum(INVESTMENT_TOTAL) desc ");
		}
		       
		
		String sql=SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		long endMilis=System.currentTimeMillis();
		logger.debug("RollPlanServiceImpl getRollPlanStageReportByMap 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
	}

	
	/**
	 * 三年滚动计划国标行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:56:15
	 */
	@Override
	public List<Map<String, Object>> getRollPlanGbindustryreportByMap(
			Map<String, String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		//参数的序号表示分相应的级别显示
		String kkk=filters.get("GBIndustryLevel");  
		//需要查询的维度级别
		String proStageStr = " vinf.GB_INDUSTRY ";//-- 国标行业
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
				SQL.append(" count(plan_project_id) as \"cnt\",  ");//-- 项目个数  
				SQL.append(" sum(INVESTMENT_TOTAL)/10000  as \"investMon1\",   ");//-- 总投资（亿元）
				SQL.append(" sum(A00180SN)/10000 as \"investMon2\",  ");//--  未来三年资金需求（亿元） 
				SQL.append(" sum(A001802017)/10000 as \"investMon3\", ");//--   2017年资金需求（亿元）  
				SQL.append(" sum(A001802018)/10000 as \"investMon4\",   ");//--   2018年资金需求（亿元）
				SQL.append(" sum(A001802019)/10000 as \"investMon5\",   ");//--   2019年资金需求（亿元）
				SQL.append(" sum(vinf.A00016TotalMenoy)/10000 as \"A00016TotalMenoy\",  ");// --   中央预算内投资（亿元）
				SQL.append(" sum(vinf.A00018TotalMenoy)/10000 as \"A00018TotalMenoy\"   ");//--   专项建设基金投资（亿元）
				SQL.append(" from  v_roll_plan_child vinf    ");//-- where
				SQL.append(" left join dictionary_items di1 on vinf.GB_INDUSTRY = di1.item_key and di1.group_no='2' ");
				SQL.append(" where 1=1 ");
				SQL.append(searchSql); //获取过滤条件
				SQL.append(querySql);
		SQL.append(" group by "+proStageStr);//-- 国标行业
		//根据前端传来的参数判断按相应的字段排序
		if("0".equals(orderbySql)){
			SQL.append(" order by count(plan_project_id) desc  ");
		}else if("2".equals(orderbySql)){
			SQL.append(" order by sum(A00180SN) desc  "); 
		}else{
			SQL.append(" order by sum(INVESTMENT_TOTAL) desc ");
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
		logger.debug("RollPlanServiceImpl getRollPlanStageReportByMap 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
	}

	
	
	/**
	 * 三年滚动计划所属行业报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:56:15
	 */
	@Override
	public List<Map<String, Object>> getRollPlanIndustryReportByMap(
			Map<String, String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		//参数的序号表示分相应的级别显示
		String kkk=filters.get("IndustryLevel");  
		//需要查询的维度级别
		String proStageStr = " vinf.INDUSTRY ";//-- 所属行业
		//获取分所属行业统计项目信息
		if(null==kkk){
			//获取第一级item_full_key
			proStageStr="substr(di1.item_full_key, 1, instr(di1.item_full_key||'-', '-', 1, 1)-1)   ";
		}else{
			//获取第二级
			
		}
		SQL.append(" select  "); 
				SQL.append(proStageStr + " as \"itemCode\",  "); //-- 所属行业
				SQL.append(" count(plan_project_id) as \"cnt\",    "); //-- 项目个数
				SQL.append(" sum(INVESTMENT_TOTAL)/10000  as \"investMon1\",  "); // -- 总投资（亿元）
				SQL.append(" sum(A00180SN)/10000 as \"investMon2\",   "); //--  未来三年资金需求（亿元）
				SQL.append(" sum(A001802017)/10000 as \"investMon3\",   "); //--   2017年资金需求（亿元）
				SQL.append(" sum(A001802018)/10000 as \"investMon4\",   "); //--   2018年资金需求（亿元）
				SQL.append(" sum(A001802019)/10000 as \"investMon5\",   "); //--   2019年资金需求（亿元）
				SQL.append(" sum(vinf.A00016TotalMenoy)/10000 as \"A00016TotalMenoy\",   "); //--   中央预算内投资（亿元）
				SQL.append(" sum(vinf.A00018TotalMenoy)/10000 as \"A00018TotalMenoy\"   ");  //--   专项建设基金投资（亿元）
		SQL.append(" from  v_roll_plan_child vinf   "); //-- where
		SQL.append(" left join dictionary_items di1 on vinf.INDUSTRY = di1.item_key and di1.group_no='8' ");
		SQL.append(" where 1=1 ");
		SQL.append(searchSql); //获取过滤条件
		SQL.append(querySql);
		SQL.append(" group by "+proStageStr); //-- 所属行业
		//根据前端传来的参数判断按相应的字段排序
		if("0".equals(orderbySql)){
			SQL.append(" order by count(plan_project_id) desc  ");
		}else if("2".equals(orderbySql)){
			SQL.append(" order by sum(A00180SN) desc  "); 
		}else{
			SQL.append(" order by sum(INVESTMENT_TOTAL) desc ");
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
		logger.debug("RollPlanServiceImpl getRollPlanIndustryReportByMap 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
	}

	
	
	/**
	 * 三年滚动计划重大战略报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:56:15
	 */
	@Override
	public List<Map<String, Object>> getRollPlanMajorstrategyReportByMap(
			Map<String, String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		//获取分重大战略统计项目信息
		SQL.append(" select "); 
				SQL.append(" vinf.MAJOR_STRATEGY as \"itemCode\",   "); //-- 重大战略
				SQL.append(" count(plan_project_id) as \"cnt\",    "); //-- 项目个数
				SQL.append(" sum(INVESTMENT_TOTAL)/10000  as \"investMon1\",   "); //-- 总投资（亿元）
				SQL.append(" sum(A00180SN)/10000 as \"investMon2\",   "); //--  未来三年资金需求（亿元）
				SQL.append(" sum(A001802017)/10000 as \"investMon3\",   "); //--   2017年资金需求（亿元）
				SQL.append(" sum(A001802018)/10000 as \"investMon4\",   "); //--   2018年资金需求（亿元）
				SQL.append(" sum(A001802019)/10000 as \"investMon5\", "); //--   2019年资金需求（亿元） 
				SQL.append(" sum(vinf.A00016TotalMenoy)/10000 as \"A00016TotalMenoy\",   "); //--   中央预算内投资（亿元）
				SQL.append(" sum(vinf.A00018TotalMenoy)/10000 as \"A00018TotalMenoy\"    "); //--   专项建设基金投资（亿元）
		SQL.append(" from	v_roll_plan_child vinf   "); //-- where
		SQL.append(" where 1=1 ");
		SQL.append(searchSql); //获取过滤条件
		SQL.append(querySql);
		SQL.append(" group by vinf.MAJOR_STRATEGY  "); //-- 重大战略 
		//根据前端传来的参数判断按相应的字段排序
		if("0".equals(orderbySql)){
			SQL.append(" order by count(plan_project_id) desc  ");
		}else if("2".equals(orderbySql)){
			SQL.append(" order by sum(A00180SN) desc  "); 
		}else{
			SQL.append(" order by sum(INVESTMENT_TOTAL) desc ");
		}
		
		
		String sql=SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode")){
				list.get(i).put("itemName", "其他");
			}else {
				list.get(i).put("itemName", Cache.getNameByCode("14",(String)list.get(i).get("itemCode")));
			}
		}
		long endMilis=System.currentTimeMillis();
		logger.debug("RollPlanServiceImpl getRollPlanMajorstrategyReportByMap 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
	}

	
	
	/**
	 * 三年滚动计划预计开工时间报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:56:15
	 */
	@Override
	public List<Map<String, Object>> getRollPlanExpectstartyearReportByMap(
			Map<String, String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		//获取分预计开工时间统计项目信息
		SQL.append(" select  "); 
				SQL.append(" to_char(vinf.EXPECT_STARTYEAR,'yyyy') as \"itemName\",  "); //-- 预计开工时间 
				SQL.append(" count(plan_project_id) as \"cnt\",  "); //-- 项目个数  
				SQL.append(" sum(INVESTMENT_TOTAL)/10000  as \"investMon1\",   "); //-- 总投资（亿元）
				SQL.append(" sum(A00180SN)/10000 as \"investMon2\",   "); //--  未来三年资金需求（亿元）
				SQL.append(" sum(A001802017)/10000 as \"investMon3\",  "); //--   2017年资金需求（亿元） 
				SQL.append(" sum(A001802018)/10000 as \"investMon4\",  "); //--   2018年资金需求（亿元） 
				SQL.append(" sum(A001802019)/10000 as \"investMon5\",   "); //--   2019年资金需求（亿元）
				SQL.append(" sum(vinf.A00016TotalMenoy)/10000 as \"A00016TotalMenoy\", "); // --   中央预算内投资（亿元）
				SQL.append(" sum(vinf.A00018TotalMenoy)/10000 as \"A00018TotalMenoy\"    "); //--   专项建设基金投资（亿元）
		SQL.append(" from    v_roll_plan_child vinf   "); //-- where
		SQL.append(" where 1=1 ");
		SQL.append(searchSql); //获取过滤条件
		SQL.append(querySql);
		SQL.append(" group by to_char(vinf.EXPECT_STARTYEAR,'yyyy')"); //-- 预计开工时间 
		//根据前端传来的参数判断按相应的字段排序
		if("0".equals(orderbySql)){
			SQL.append(" order by count(plan_project_id) desc  ");
		}else if("2".equals(orderbySql)){
			SQL.append(" order by sum(A00180SN) desc  "); 
		}else{
			SQL.append(" order by sum(INVESTMENT_TOTAL) desc ");
		}
		
		
		String sql=SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		long endMilis=System.currentTimeMillis();
		logger.debug("RollPlanServiceImpl getRollPlanExpectstartyearReportByMap 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
	}

	
	
	/**
	 * 三年滚动计划编入三年滚动计划时间报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author zhoupeng
	 * @Date 2016年10月22日下午2:56:15
	 */
	@Override
	public List<Map<String, Object>> getRollPlanCreatetimeReportByMap(
			Map<String, String> filters,String searchSql,String orderbySql,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append(" select ");
				SQL.append(" to_char(vinf.CREATE_TIME,'yyyy-MM') as \"itemName\",  ");//-- 编入三年滚动计划时间
				SQL.append(" count(plan_project_id) as \"cnt\",  ");//-- 项目个数 
				SQL.append(" sum(INVESTMENT_TOTAL)/10000  as \"investMon1\",  ");//-- 总投资（亿元）
				SQL.append(" sum(A00180SN)/10000 as \"investMon2\",  ");//--  未来三年资金需求（亿元）
				SQL.append(" sum(A001802017)/10000 as \"investMon3\",  ");//--   2017年资金需求（亿元）
				SQL.append(" sum(A001802018)/10000 as \"investMon4\",  ");//--   2018年资金需求（亿元）
				SQL.append(" sum(A001802019)/10000 as \"investMon5\",  ");//--   2019年资金需求（亿元）
				SQL.append(" sum(vinf.A00016TotalMenoy)/10000 as \"A00016TotalMenoy\",  ");//--   中央预算内投资（亿元）
				SQL.append(" sum(vinf.A00018TotalMenoy)/10000 as \"A00018TotalMenoy\"   ");//--   专项建设基金投资（亿元）
				SQL.append(" from	v_roll_plan_child vinf   ");//-- where
				SQL.append(" where 1=1 ");
				SQL.append(searchSql); //获取过滤条件
				SQL.append(querySql);
		SQL.append(" group by to_char(vinf.CREATE_TIME,'yyyy-MM') ");//-- 编入三年滚动计划时间
		//根据前端传来的参数判断按相应的字段排序
		if("0".equals(orderbySql)){
			SQL.append(" order by count(plan_project_id) desc  ");
		}else if("2".equals(orderbySql)){
			SQL.append(" order by sum(A00180SN) desc  "); 
		}else{
			SQL.append(" order by sum(INVESTMENT_TOTAL) desc ");
		}
		       
		
		
		String sql=SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		long endMilis=System.currentTimeMillis();
		logger.debug("RollPlanServiceImpl getRollPlanCreatetimeReportByMap 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
	}


}
