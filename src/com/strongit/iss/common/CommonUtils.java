package com.strongit.iss.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CommonUtils  implements  ICommonUtils{
	//定义xml的格式
	public static final String XMLHEARDERS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	//定义时间类型格式
	public static String DATE_FORMAT_YMD = "yyyy-mm-dd";
	private String lv = "LV";
	//字典表所取维度的字段
	private String Code_Field = "_ITEM_KEY";
	
	/**
	 * 动态替换具体的层级维度
	 * @orderBy 
	 * @param Level
	 * @param SQL
	 * @return
	 * @author wuwei
	 * @Date 2016年10月24日上午10:34:05
	 */
	public String formatItemKey(String Level,String SQL) {
		String TABLE_COLUMN = "";
		if (SQL == null) {
			SQL = new String();
		}
		TABLE_COLUMN = lv + (Integer.valueOf(Level)+1) + Code_Field;
		SQL = SQL.replace("%f%", TABLE_COLUMN);
		// 具体哪个维度对应数据库表字段名称
		TABLE_COLUMN = lv + (Integer.valueOf(Level)) + Code_Field;
		//替换维度
		SQL = SQL.replace("%d%", TABLE_COLUMN);
		return SQL;
	}
	
	/**
	 * 根据建设地点获取地图所需条件
	 * @orderBy 
	 * @param buildPlace
	 * @return
	 * @author wuwei
	 * @Date 2017年3月16日下午6:02:41
	 */
	public String getSql(String buildPlace){
		String sql = "";
	    //地区下钻到北京，上海，重庆，天津，香港，澳门，台湾时直接钻到县级
		if(Arrays.asList(Constant.ARRAY).contains(buildPlace)){
			// 下钻过滤
			if ("0000".equals(buildPlace.substring(2, 6))) {
			    sql=" AND VINF.BUILD_PLACE like '" + buildPlace.substring(0, 2) + "%'";
			}else{
				sql=" AND VINF.BUILD_PLACE = '" + buildPlace.substring(0, 6) + "'";
			}
		}else{
			// 展现市级
			if("0000".equals(buildPlace.substring(2, 6))){
			    sql=" AND VINF.BUILD_PLACE like '" +buildPlace.substring(0,2)+"%'";
			}// 展现县级
			else if("00".equals(buildPlace.substring(4, 6))){
			    sql=" AND VINF.BUILD_PLACE like '" +buildPlace.substring(0,4)+"%'";	    	 
			}else{
			    sql=" AND VINF.BUILD_PLACE = '" + buildPlace.substring(0, 6) + "'";
			}
	    }
		return sql;
	}

	/**
	 *   将fullName 映射成为CODE
	 * @param filters
     */
	@Override
	public    Map<String, String> codeMapFullName (Map<String, String> filters){
			Map<String, String> newFilters = new HashMap<String,String>();
		
			if(null!=filters&&!filters.isEmpty()){
				// 开启项目
				for (Map.Entry<String,String> entry:filters.entrySet()){
					String key=entry.getKey();
					String value=entry.getValue();
					// 去掉前面的中国
					if(StringUtils.isNotBlank(value)){
						value=value.replace("中国"+Constant.BI_SPLIT,"");
						//当地图返回到全国地图级别时，需当做第一级处理
						if(value.equals("中国")){
							value=value.replace("中国","");
						}
					}
					// 建设地点
					if("BuildPlaceCode".equals(key)){
						String code=Cache.getCodeByFullName(value);
						newFilters.put(key,code);
						// 设置行业级别
						if(StringUtils.isNotBlank(value)) {
							newFilters.put("BuildPlaceLevel",String.valueOf(value.split(Constant.BI_SPLIT).length));
						}
					}
					//发改委行业
					else if("IndustryCode".equals(key)){
						String code=Cache.getCodeByFullName(value);
						newFilters.put(key,code);
						// 设置行业级别
						if(StringUtils.isNotBlank(value)) {
							newFilters.put("IndustryLevel",String.valueOf(value.split(Constant.BI_SPLIT).length));
						}
					}
					//国标行业code
					else if("GBIndustryCode".equals(key)){
						String code=Cache.getCodeByFullName(value);
						newFilters.put(key,code);
						// 设置行业级别
						if(StringUtils.isNotBlank(value)) {
							newFilters.put("GBIndustryLevel",String.valueOf(value.split(Constant.BI_SPLIT).length));
						}
					}
					//政府投资方向code
					else if("GovernmentCode".equals(key)){
						String code=Cache.getCodeByFullName(value);
						newFilters.put(key,code);
						// 设置行业级别
						if(StringUtils.isNotBlank(value)) {
							newFilters.put("GovernmentLevel",String.valueOf(value.split(Constant.BI_SPLIT).length));
						}
					}
					//申报部门
					else if("RecordDdptCode".equals(key)){
						newFilters.put(key,value);
						// 设置申报部门
						if(StringUtils.isNotBlank(value) && !value.equals("顶层")) {
							newFilters.put("RecororderbySqlptCode",String.valueOf(value.split(Constant.BI_SPLIT).length));
						}
					}
					//所属专项建设资金的批次
					else if("FundLevel".equals(key)){
						newFilters.put(key,value);
					}
					//所属银行
					else if("Bank".equals(key)){
						newFilters.put(key,value);
					}
					//是否编入三年滚动计划  上报国家三年滚动计划
					else if("filterStatus".equals(key)){
						newFilters.put(key,value);
					}
					//所属专项建设资金的批次
					else if("DEPARTMENTNAME".equals(key)){
						newFilters.put(key,value);
					}
					//专项类别
					else if("SpecialTypeCode".equals(key)){
						String code=Cache.getCodeByFullName(value);
						newFilters.put(key,code);
						// 设置申报部门
						if(StringUtils.isNotBlank(value)) {
							newFilters.put("SpecialTypeLevel",String.valueOf(value.split(Constant.BI_SPLIT).length));
						}
					}else{
						newFilters.put(key,value);
					}
				}
			}
			return newFilters;
	}
	
	
	/**
	 *   拼接过滤条件
	 * @param filters
     */
	@Override
	public    String searchSql (String tableStr,Map<String, String> newFilters){
			//拼装的sql
			String searchStr = "";
			if(null!=newFilters&&!newFilters.isEmpty()){
				// 开启项目
				for (Map.Entry<String,String> entry:newFilters.entrySet()){
					String key=entry.getKey();
					String value=entry.getValue();
					// 建设地点
					if("BuildPlaceCode".equals(key)){
						
						searchStr = searchStr + " AND " +tableStr + ".BUILD_PLACE_FULL_KEY like '%"+value+"%' ";
							
					}
					//发改委行业
					else if("IndustryCode".equals(key)){
						
						searchStr = searchStr + " AND " + tableStr + ".INDUSTRY_FULL_KEY like '%"+value+"%' ";
							
					}
					//国标行业code
					else if("GBIndustryCode".equals(key)){
						
						searchStr = searchStr + " AND " + tableStr + ".GB_INDUSTRY_FULL_KEY like '%"+value+"%' ";
							
					}
					//政府投资方向code
					else if("GovernmentCode".equals(key)){
						
						searchStr = searchStr + " AND " + tableStr + ".GOVERNMENT_INVEST_DIRECTION_FULL_KEY like '%"+value+"%' ";
						
					}
					//未编制三年滚动计划项目
					else if("unfinish".equals(key)){
						
						searchStr = searchStr + " AND " + tableStr + ".IS_PLAN = '0' ";
						
					}//上报国家三年滚动计划
					else if("report".equals(value)){
						
						searchStr = searchStr + " AND SUBSTRING("+tableStr +".DEPARTMENT_FGW_GUID, 0, 6) = 'GUOJIA' AND "+tableStr+".STATUS = 'TO_CHECK'";
						
					}
				}
			}
			return searchStr;
	}
	
	
	/**
	 *   校验排序条件
	 * @param filters
     */
	@Override
	public    String orderbySql (String orderbyStr,Map<String, String> newFilters){
			//获取排序字段信息
			String orderbyValue = newFilters.get("orderby");
			if(null != orderbyValue && orderbyValue.substring(0, orderbyValue.length()-1).equals(orderbyStr)){
				return orderbyValue.substring(orderbyValue.length()-1, orderbyValue.length());
			}else if(null != orderbyValue && orderbyValue.split("##")[0].equals(orderbyStr)&& orderbyValue.split("##")[1].equals("总投资") ){
				return "1";
			}else if(null != orderbyValue && orderbyValue.split("##")[0].equals(orderbyStr)&& orderbyValue.split("##")[1].equals("未来三年需求资金") ){
				return "2";
			} 
			return null;
	}

}
