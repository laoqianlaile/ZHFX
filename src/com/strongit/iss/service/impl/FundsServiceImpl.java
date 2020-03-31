package com.strongit.iss.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.common.Cache;
import com.strongit.iss.common.ICommonUtils;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IFundsService;

@Service
@Transactional
public class FundsServiceImpl extends BaseService implements IFundsService{
	
	@Autowired
	private ICommonUtils commonUtils;

	/**
	 * 专项地图报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月3日下午7:04:47
	 */
	@Override
	public List<Map<String, Object>> getFundByMap(Map<String, String> filters,String querySql)
			throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI1.%f%  as \"itemCode\",");//---建设地点
        SQL.append(" count(vinf.id) as \"cnt\", ");//---项目ID
        SQL.append(" sum(vinf.INVESTMENT_TOTAL) as  \"investMon\",");//---总投资
        SQL.append(" sum(vinf.A00018D27) as \"investMon1\", ");//---专项债券募集的专项建设基金本次申请
        SQL.append(" sum(vinf.A00018D22) as \"investMon2\", ");//---专项债券募集的专项建设基金建议资金
        SQL.append(" sum(vinf.A00018D23) as \"investMon3\"  ");//---专项债券募集的专项建设基金投放资金
        SQL.append(" from  V_FUND_CHILD vinf ");
        SQL.append(" left join view_name_type15 vnt on vinf.file_name = vnt.full_name");
        SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON vinf.SPECIAL_TYPE=DI.ITEM_KEY and DI.GROUP_NO ='19'");
        SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI1 ");
        SQL.append(" ON VINF.Build_Place=DI1.item_key and DI1.GROUP_NO ='1'");
        SQL.append(" where 1 = 1");
        if(StringUtils.isNotBlank(filters.get("BuildPlaceCode"))){
        	SQL.append(commonUtils.getSql(filters.get("BuildPlaceCode")));
        }
		//存在下钻的code
		if(StringUtils.isNotEmpty(filters.get("SpecialTypeCode"))){
			SQL.append(" AND DI.item_full_key like '%" +filters.get("SpecialTypeCode")+"%' ");
		}
	    //批次标题
	    if(StringUtils.isNotBlank(filters.get("FundLevel"))){
	    	SQL.append(" and vinf.file_name = '"+filters.get("FundLevel")+"'");
	    }
	    if("国开行".equals((filters.get("Bank")))){	    	
	    	SQL.append(" and vinf.PROPOSED_BANK = 'A00001'");
	    }else if("农开行".equals((filters.get("Bank")))){	    	
	    	SQL.append(" and vinf.PROPOSED_BANK = 'A00002'");
	    }else if(filters.get("Bank").length()==6){
	    	SQL.append(" and vinf.PROPOSED_BANK = '"+filters.get("Bank")+"'");
	    }
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI1.%f%");
		// 金额排序		
        SQL.append(" ORDER BY \"investMon\" desc, DI1.%f%");	
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
		logger.debug("FundsServiceImpl getFundsPlaceReportByMap 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}

	/**
	 * 专项专项类别报表数据
	 * @orderBy 
	 * @param filters
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月3日下午7:36:22
	 */
	@Override
	public List<Map<String, Object>> getFundBySpecialType(
			Map<String, String> filters,String querySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
        SQL.append(" count(vinf.id) as \"cnt\", ");//---项目ID
        SQL.append(" sum(vinf.INVESTMENT_TOTAL) as  \"investMon\",");//---总投资
        SQL.append(" sum(vinf.A00018D27) as \"investMon1\", ");//---专项债券募集的专项建设基金本次申请
        SQL.append(" sum(vinf.A00018D22) as \"investMon2\", ");//---专项债券募集的专项建设基金建议资金
        SQL.append(" sum(vinf.A00018D23) as \"investMon3\"  ");//---专项债券募集的专项建设基金投放资金
        SQL.append(" from  V_FUND_CHILD vinf ");
		SQL.append(" LEFT JOIN DICTIONARY_ITEMS DI ");
		SQL.append(" ON vinf.SPECIAL_TYPE=DI.ITEM_KEY and DI.GROUP_NO ='19'");
		SQL.append(" where 1 = 1");
		//存在下钻的code
		if(StringUtils.isNotEmpty(filters.get("SpecialTypeCode"))){
			SQL.append(" AND DI.%d% like '" +filters.get("SpecialTypeCode")+"%' ");
		}
		//获取批次号
	    if(StringUtils.isNotBlank(filters.get("FundLevel"))){
	    	SQL.append(" AND vinf.file_name = '"+filters.get("FundLevel")+"'");
	    }
	    if(StringUtils.isNotEmpty(filters.get("Bank")) && filters.get("Bank").length()==6){
	    	SQL.append(" and vinf.PROPOSED_BANK = '"+filters.get("Bank")+"'");
	    }
	    SQL.append(querySql);
        SQL.append(" GROUP BY DI.%f% ");
		SQL.append(" ORDER BY \"investMon1\" desc,\"itemCode\" ");
		String sql=null;
		if(StringUtils.isBlank(filters.get("SpecialTypeLevel"))){
			sql = commonUtils.formatItemKey("0", SQL.toString());
		}else{
			sql = commonUtils.formatItemKey(filters.get("SpecialTypeLevel"), SQL.toString());
		}
		//调取缓存中的结果
		List<Map<String,Object>> list = this.dao.findBySql(sql, new Object[]{});
		for(int i=0;i<list.size();i++){
			if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
				list.get(i).put("itemName", "其他");
			}else{
				list.get(i).put("itemName", Cache.getNameByCode("19", (String)list.get(i).get("itemCode")));
			}
		}
		return list;
	}
}
