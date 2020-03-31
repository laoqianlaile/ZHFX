package com.strongit.iss.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.strongit.iss.common.Cache;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.ICommonUtils;
import com.strongit.iss.common.MD5;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IOverViewService;

@Service
@Transactional
public class OverViewServiceImpl extends BaseService implements IOverViewService{

    @Autowired
    private ReportCacheServiceImpl reportCacheService;
	@Autowired
	private ICommonUtils commonUtils;
	
    /**
     * 审核备地图
     * @orderBy 
     * @param params
     * @return
     * @throws BusinessServiceException
     * @author wuwei
     * @Date 2016年11月3日上午10:39:47
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAuditPreparationByPlace(
			Map<String, String> params) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT SUBSTR(inf.X04,0,@@@)||'####' as \"itemCode\",");
		SQL.append(" COUNT (inf.PROJECT_GUID) AS \"cnt\",");
		SQL.append(" SUM (inf.X05) AS \"investMon\"");
		SQL.append(" FROM V_SHB_WUWEI1 inf");
		SQL.append(" WHERE 1 = 1");
	    String zeroNun="0000";
	    Integer num=2;
	    String filterSql="";
	    if(StringUtils.isNotBlank(params.get("filterCode"))){
		    if(Arrays.asList(Constant.ARRAY).contains(params.get("filterCode"))){
				//地区下钻到北京，上海，重庆，天津，香港，澳门，台湾时直接钻到县级
				if ("0000".equals(params.get("filterCode").substring(2, 6))) {
					// 截取位数
			    	zeroNun="";
			    	num=6;
					SQL.append(" AND inf.X04 like '" + params.get("filterCode").substring(0, 2) + "%'");
				}
		    }else{
			    // 展现市级
			    if("0000".equals(params.get("filterCode").substring(2, 6))){
			    	// 截取位数
			    	zeroNun="00";
			    	num=4;
			    	filterSql=" AND inf.X04 like '" +params.get("filterCode").substring(0,2)+"%'";
			    }// 展现县级
			    else if("00".equals(params.get("filterCode").substring(4, 6))){
			    	// 截取位数
			    	zeroNun="";
			    	num=6;
			    	filterSql=" AND inf.X04 like '" +params.get("filterCode").substring(0,4)+"%'";	    	 
			    }
		    }
	    }
	    SQL.append(filterSql);
		if(StringUtils.isNotBlank(params.get("AuditPreparationCode"))){
			SQL.append(" AND inf.x03 = '"+params.get("AuditPreparationCode")+"'");
		}
		SQL.append(" GROUP BY SUBSTR(inf.X04,0,@@@)||'####'");
		// 个数排序
		if((Constant.ORDERBY_CNT).equals(params.get(Constant.ORDERBY))){			
		    SQL.append(" ORDER BY \"cnt\" desc, SUBSTR(inf.X04,0,@@@)||'####'");
		}
		// 金额排序
		else{		
			 SQL.append(" ORDER BY \"investMon\" desc, SUBSTR(inf.X04,0,@@@)||'####'");
		}
		String sql = SQL.toString().replaceAll("@@@", num.toString()).replaceAll("####", zeroNun);
		//调取缓存中的结果
		List<Map<String,Object>> list = (List<Map<String,Object>>) reportCacheService.getEverObject(MD5.encode(sql));
		if(list == null){
			long startMilis=System.currentTimeMillis();
			//执行查询并将结果转化为ListMap
			list =this.dao.findBySql(sql, new Object[]{});
			for(int i=0;i<list.size();i++){
				if(null == list.get(i).get("itemCode")){
					list.get(i).put("itemName", "其他");
				}else if("99".equals(list.get(i).get("itemCode").toString().substring(0, 2))){
					list.get(i).put("itemName", "跨省区");
				}else{
					list.get(i).put("itemName", Cache.getNameByCode("1",(String)list.get(i).get("itemCode")));
				}
			}
			long endMilis=System.currentTimeMillis();
			logger.debug("OverViewServiceImpl getAuditPreparationByPlace 方法执行查询花费毫秒数" + (endMilis-startMilis));
			//将数据存入缓存中
			reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		return list;
	}

	/**
	 * 审核备分国标行业
	 * @orderBy 
	 * @param params
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月3日上午10:40:13
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAuditPreparationByGBIndustry(
			Map<String, String> params) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT DI.%f%  as \"itemCode\",");
		SQL.append(" COUNT (inf.PROJECT_GUID) AS \"cnt\",");
		SQL.append(" SUM (inf.X05) AS \"investMon\"");
		SQL.append(" FROM V_SHB_WUWEI1 inf");
        SQL.append(" left join dictionary_items di");
        SQL.append(" ON inf.X06 = di.item_key and di.group_no ='2'");
        SQL.append(" WHERE 1 = 1");
		if(StringUtils.isNotBlank(params.get("AuditPreparationCode"))){
			SQL.append(" AND inf.x03 = '"+params.get("AuditPreparationCode")+"'");
		}
		//存在下钻的code
		if(StringUtils.isNotEmpty(params.get("IndustryCode"))){
			SQL.append(" AND DI.%d% = '" +params.get("IndustryCode")+"' ");
		}
		SQL.append(" GROUP BY DI.%f%");
		// 个数排序
		if(Constant.ORDERBY_CNT.equals(params.get(Constant.ORDERBY))){			
		    SQL.append(" ORDER BY \"cnt\" desc, DI.%f% ");
		}
		// 金额排序
		else if(Constant.ORDERBY_MON.equals(params.get(Constant.ORDERBY))){		
			 SQL.append(" ORDER BY \"investMon\" desc, DI.%f% ");
		}
		String sql=null;
		if(StringUtil.isEmpty(params.get("IndustryCode"))){
			sql = commonUtils.formatItemKey("0", SQL.toString());
		}else{
			sql = commonUtils.formatItemKey(String.valueOf(params.get("IndustryCode").length()), SQL.toString());
		}
		//调取缓存中的结果
		List<Map<String,Object>> list = (List<Map<String,Object>>) reportCacheService.getEverObject(MD5.encode(sql));
		if(list == null){
			long startMilis=System.currentTimeMillis();
			//执行查询并将结果转化为ListMap
			list =this.dao.findBySql(sql, new Object[]{});
			for(int i=0;i<list.size();i++){
				if(null == list.get(i).get("itemCode") || "".equals(list.get(i).get("itemCode"))){
					list.get(i).put("itemName", "未填写");
				}else{
					list.get(i).put("itemName", Cache.getNameByCode("2",(String)list.get(i).get("itemCode")));
				}
			}
			long endMilis=System.currentTimeMillis();
			logger.debug("OverViewServiceImpl getAuditPreparationByGBIndustry 方法执行查询花费毫秒数" + (endMilis-startMilis));
			//将数据存入缓存中
			reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		return list;
	}

	/**
	 * 审核备趋势
	 * @orderBy 
	 * @param params
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年11月3日上午10:42:46
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAuditPreparationTrends(
			Map<String, String> params) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append(" SELECT ");
		SQL.append(" SUBSTR (inf.X13,0,6) AS \"itemName\",");
		SQL.append(" COUNT (inf.PROJECT_GUID) AS \"cnt\",");
		SQL.append(" SUM (inf.X05) AS \"investMon\"");
		SQL.append(" FROM V_SHB_WUWEI1 inf");
		SQL.append(" left join  dictionary_items di2  on  inf.x06=di2.item_key and di2.group_no ='2'");
		SQL.append(" WHERE 1 = 1 ");
		if(StringUtils.isNotBlank(params.get("filterCode"))) {
			// 下钻过滤
			if ("0000".equals(params.get("filterCode").substring(2, 6))) {
				//建设地点
				SQL.append(" AND inf.X04 like '" + params.get("filterCode").substring(0, 2) + "%'");
			}
			// 展现第二级
			else if ("00".equals(params.get("filterCode").substring(4, 6))) {
				//建设地点
				SQL.append(" AND inf.X04 like '" + params.get("filterCode").substring(0, 4) + "%'");
			}
			// 县级
			else if ("".equals(params.get("filterCode").substring(6, 6))) {
			//建设地点
			SQL.append(" AND inf.X04 like '" + params.get("filterCode").substring(0, 6) + "%'");
			}		
		}
		if(StringUtils.isNotBlank(params.get("IndustryCode"))){
			SQL.append(" AND di2.%d% = '" + params.get("IndustryCode") + "'");
		}
		if(StringUtils.isNotBlank(params.get("AuditPreparationCode"))){
			SQL.append(" AND inf.x03 = '"+params.get("AuditPreparationCode")+"'");
		}
		SQL.append(" GROUP BY SUBSTR (inf.X13,0,6)");	
		SQL.append(" ORDER BY SUBSTR (inf.X13,0,6)");
		String sql = null;
		if(StringUtils.isNotBlank(params.get("IndustryCode"))){
			sql = commonUtils.formatItemKey(String.valueOf(params.get("IndustryCode").length()), SQL.toString());
		}else{
			sql=SQL.toString();
		}
		//调取缓存中的结果
		List<Map<String,Object>> list = (List<Map<String,Object>>) reportCacheService.getEverObject(MD5.encode(sql));
		if(list == null){
			//执行查询并将结果转化为ListMap
			list =this.dao.findBySql(sql, new Object[]{});
			//将数据存入缓存中
			reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		return list;
	}

}
