package com.strongit.iss.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.common.Constant;
import com.strongit.iss.entity.TCityCustomCoordinates;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.ISuperMapService;

@Service
@Transactional
/**
 * @author zhaochao
 * @date 2018-09-06
 */
public class SuperMapServiceImpl extends BaseService implements ISuperMapService{

    @Autowired
    private ReportCacheServiceImpl reportCacheService;
    
   
    
    /**
	 *  <pre>
	 *   获取全国数据信息
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  filters
	 * 			-- 查询条件
	 * @param searchSql
	 * 			-- 查询条件
	 * @param orderbySql
	 * 			-- 排序
	 * 			
	 * @return
	 *     --  filters==null或者filters== ""  返回全国数据信息
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月15日上午10:40:13
	 * </pre>
	 */
	@Override
	public List<Map<String, Object>> getQueryByGeometry(
			Map<String, String> filters,String searchSql,String orderbySql) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
	        
	        
	        SQL.append(" select di.item_key as \"itemCode\", di.item_value as \"itemName\","
	        		+ "  case when table1.cnt is null then 0 else table1.cnt end as \"cnt\","
	        		+ "  case when table1.investMon1 is null then 0 else to_char(table1.investMon1) end as \"investMon1\", "
	        		+ "  di.smx, di.smy   ");
	        SQL.append(" from dictionary_items di left join   ");
	        SQL.append(" 	(  ");
	        		SQL.append(" 	select SUBSTR(vgmc.build_place, 0, @@@)||'####' as build_place,   ");
	        		SQL.append("  	count(vgmc.YEAR_PLAN_PROJECT_ID) as cnt,   ");
	        		SQL.append(" 	sum(nvl(vgmc.INVESTMENT_TOTAL,0))/10000 as investMon1   ");
	        		SQL.append(" 	from v_gov_management_child vgmc   ");
	        		SQL.append("  	where vgmc.build_place is not null and vgmc.PROFLAG !='2' ");
	        			
	        		
    		//画几何图形区域的地区编码集合
	        if(StringUtils.isNotBlank(filters.get("drawPolygonCode"))){
	        	String drawPolygonCodeVal = filters.get("drawPolygonCode").toString().replace(",", "','");
	        	SQL.append(" and vgmc.build_place in  ('"+drawPolygonCodeVal+"')");
	        }
	       
	        //将所属行业加入过滤条件
	        if(StringUtils.isNotBlank(filters.get(Constant.CODE_COLUMN_INDUSTRY))){
	        	String industryVal = filters.get(Constant.CODE_COLUMN_INDUSTRY).toString().replace(",", "','");
	        	SQL.append(" and vgmc.INDUSTRY in  ('"+industryVal+"')");
	        } 		
	        		
	        //将开工时间1加入过滤条件
	        if(StringUtils.isNotBlank(filters.get("actualStartTime1"))){
	        	String actualStartTime1Val = filters.get("actualStartTime1").toString().replace(",", "','");
	        	SQL.append(" and vgmc.actual_start_time1 >= '"+actualStartTime1Val+"'");
	        } 		
	        
	        
	        //将开工时间2加入过滤条件
	        if(StringUtils.isNotBlank(filters.get("actualStartTime2"))){
	        	String actualStartTime2Val = filters.get("actualStartTime2").toString().replace(",", "','");
	        	SQL.append(" and vgmc.actual_start_time1 <= '"+actualStartTime2Val+"'");
	        } 
	        
	        //将竣工时间1加入过滤条件
	        if(StringUtils.isNotBlank(filters.get("actualEndTime1"))){
	        	String actualEndTime1Val = filters.get("actualEndTime1").toString().replace(",", "','");
	        	SQL.append(" and vgmc.actual_end_time1 >= '"+actualEndTime1Val+"'");
	        } 
	        
	        
	        //将竣工时间2加入过滤条件
	        if(StringUtils.isNotBlank(filters.get("actualEndTime2"))){
	        	String actualEndTime2Val = filters.get("actualEndTime2").toString().replace(",", "','");
	        	SQL.append(" and vgmc.actual_end_time1 <= '"+actualEndTime2Val+"'");
	        }
	        
	        	
	        		
	        		SQL.append("  	group by SUBSTR(vgmc.build_place, 0, @@@)   ");
	        SQL.append(" 	)table1 on di.item_key = table1.build_place  ");
	        SQL.append(" where di.group_no = '1'    and di.smx is not null  ");
	        
	      
	        
	        //SQL.append(searchSql); //获取过滤条件
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
						SQL.append(" AND di.item_key like '" + code.substring(0, 2) + "%'");
					}// 展现第二级
					else if ("".equals(code.substring(6, 6))) {
						// 截取位数
				    	zeroNun="";
				    	num=6;
				    	filterSql=" AND di.item_key like '" +code.substring(0, 6)+"%'";
					}
			    }else{
				    // 展现第一级
				    if("0000".equals(code.substring(2, 6))){
				    	// 截取位数
				    	zeroNun="00";
				    	num=4;
				    	filterSql=" AND di.item_key like '" +code.substring(0,2)+"%'";
				    }// 展现第二级
				    else if("00".equals(code.substring(4, 6))){
				    	// 截取位数
				    	zeroNun="";
				    	num=6;
				    	//申报单位
				    	filterSql=" AND di.item_key like '" +code.substring(0,4)+"%'";	    	 
				    } // 展现第三级
				    else if("".equals(code.substring(6, 6))){
				    	// 截取位数
				    	zeroNun="";
				    	num=6;
				    	filterSql=" AND di.item_key like '" +code.substring(0,6)+"%'";
				    }
			    }
		    }
		    SQL.append(filterSql);
		    
		    if(StringUtils.isNotBlank(code)){
		        if("0000".equals(code.substring(2, 6))){
		        	SQL.append(" and length(di.item_full_key)=13   ");
		        }else if("00".equals(code.substring(4, 6))){
		        	SQL.append(" and length(di.item_full_key)=20  ");
		        }else{
		        	SQL.append(" and length(di.item_full_key)=20  ");
		        }
	        }else{
	        	SQL.append(" and length(di.item_full_key)=6   ");
	        }
		    
		    //将建设地点加入过滤条件中
	        if(StringUtils.isNotBlank(filters.get("region"))){
	        	 String regionVal = filters.get("region").toString().replace(",", "','");
	        	 SQL.append(" and di.item_key in  ('"+regionVal+"')");
	        }
		    SQL.append(" order by di.item_key asc  ");
		    
	        String sql=SQL.toString().replaceAll("@@@", num.toString()).replaceAll("####", zeroNun);

		
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list  =this.dao.findBySql(sql, new Object[]{});
		long endMilis=System.currentTimeMillis();
		logger.debug("SuperMapServiceImpl getAuditPreparationByGBIndustry 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}
	
	
	
	
	
	
	
    
    
    
 
    
	/**
	 *  <pre>
	 *   获取全国数据信息
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  filters
	 * 			-- 查询条件
	 * 			
	 * @return
	 *     --  filters==null或者filters== ""  返回全国数据信息
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月19日下午2:56:15
	 * </pre>
	 */
	@Override
	public List<Map<String, Object>> getSuperMapGovReportByMap(Map<String,String> filters) throws BusinessServiceException {
		StringBuilder SQL = new StringBuilder();
		SQL.append(" select vgmc.year_plan_project_id as \"yearPlanProjectId\", vgmc.PRO_CODE_COUNTRY as \"proCodeCountry\",   ");
		SQL.append(" vgmc.PRO_NAME as \"proName\",  ");
		SQL.append(" di2.item_value as \"proType\",  ");
		SQL.append(" di1.item_value as \"buildPlace\",  ");
		SQL.append(" to_char(vgmc.INVESTMENT_TOTAL) as \"investmentTotal\",  ");
		SQL.append(" di3.item_value as \"industry\",  ");
		SQL.append(" case when vgmc.actual_start_time1 is null then '' else vgmc.actual_start_time1 end as \"actualStartTime\",  ");
		SQL.append(" case when vgmc.actual_end_time1 is null then '' else  vgmc.actual_end_time1 end as \"actualEndTime\"   ");
		SQL.append(" from v_gov_management_child vgmc  ");
		SQL.append(" left join dictionary_items di1  on vgmc.build_Place = di1.item_key and di1.group_no = '1'  ");
		SQL.append(" left join dictionary_items di2  on vgmc.PRO_TYPE = di2.item_key and di2.group_no = '3'  ");
		SQL.append(" left join dictionary_items di3  on vgmc.industry = di3.item_key and di3.group_no = '8'  ");

		
		SQL.append(" where vgmc.PROFLAG !='2'  ");
		//将建设地点加入过滤条件中
        if(StringUtils.isNotBlank(filters.get("region"))){
        	 String regionVal = filters.get("region").toString().replace(",", "','");
        	 SQL.append(" and vgmc.BUILD_PLACE in  ('"+regionVal+"')");
        }
        
        //画几何图形区域的地区编码集合
        if(StringUtils.isNotBlank(filters.get("drawPolygonCode"))){
        	String drawPolygonCodeVal = filters.get("drawPolygonCode").toString().replace(",", "','");
        	SQL.append(" and vgmc.build_place in  ('"+drawPolygonCodeVal+"')");
        }
       
        //将所属行业加入过滤条件
        if(StringUtils.isNotBlank(filters.get(Constant.CODE_COLUMN_INDUSTRY))){
        	String industryVal = filters.get(Constant.CODE_COLUMN_INDUSTRY).toString().replace(",", "','");
        	SQL.append(" and vgmc.INDUSTRY in  ('"+industryVal+"')");
        } 	
        
        //将开工时间1加入过滤条件
        if(StringUtils.isNotBlank(filters.get("actualStartTime1"))){
        	String actualStartTime1Val = filters.get("actualStartTime1").toString().replace(",", "','");
        	SQL.append(" and vgmc.actual_start_time1 >= '"+actualStartTime1Val+"'");
        } 		
        
        
        //将开工时间2加入过滤条件
        if(StringUtils.isNotBlank(filters.get("actualStartTime2"))){
        	String actualStartTime2Val = filters.get("actualStartTime2").toString().replace(",", "','");
        	SQL.append(" and vgmc.actual_start_time1 <= '"+actualStartTime2Val+"'");
        } 
        
        //将竣工时间1加入过滤条件
        if(StringUtils.isNotBlank(filters.get("actualEndTime1"))){
        	String actualEndTime1Val = filters.get("actualEndTime1").toString().replace(",", "','");
        	SQL.append(" and vgmc.actual_end_time1 >= '"+actualEndTime1Val+"'");
        } 
        
        
        //将竣工时间2加入过滤条件
        if(StringUtils.isNotBlank(filters.get("actualEndTime2"))){
        	String actualEndTime2Val = filters.get("actualEndTime2").toString().replace(",", "','");
        	SQL.append(" and vgmc.actual_end_time1 <= '"+actualEndTime2Val+"'");
        }
        
        
        String filterSql="";
	    String code=filters.get(Constant.BUILD_PLACE_GROUPNO);
	    if(StringUtils.isNotBlank(code)){
		    if(Arrays.asList(Constant.ARRAY).contains(code)){
				// 下钻过滤
				if ("0000".equals(code.substring(2, 6))) {
					SQL.append(" AND di1.item_key like '" + code.substring(0, 2) + "%'");
				}// 展现第二级
				else if ("".equals(code.substring(6, 6))) {
					filterSql=" AND di1.item_key like '" +code.substring(0, 6)+"%'";
				}
		    }else{
			    // 展现第一级
			    if("0000".equals(code.substring(2, 6))){
			    	filterSql=" AND di1.item_key like '" +code.substring(0,2)+"%'";
			    }// 展现第二级
			    else if("00".equals(code.substring(4, 6))){
			    	//申报单位
			    	filterSql=" AND di1.item_key like '" +code.substring(0,4)+"%'";	    	 
			    } // 展现第三级
			    else if("".equals(code.substring(6, 6))){
			    	filterSql=" AND di1.item_key like '" +code.substring(0,6)+"%'";
			    }
		    }
	    }
	    SQL.append(filterSql);
	    
		
		String sql=SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list =this.dao.findBySql(sql, new Object[]{});
		long endMilis=System.currentTimeMillis();
		logger.debug("SuperMapServiceImpl getSuperMapGovReportByMap 方法执行查询花费毫秒数:" + (endMilis-startMilis));
		return list;
	}
    
    
    
	/**
	 *  <pre>
	 *   保存空间政府精细化管理项目信息
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  filters
	 * 			-- 查询条件
	 * @param employeeGuid
	 * 			-- 当前用户ID
	 * 			
	 * @return
	 *     --  filters==null或者filters== ""  返回保存事件成功
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月26日下午2:56:15
	 * </pre>
	 */
    public boolean saveCusCoordinates(Map<String,String> filters,String employeeGuid){
    	//判断是否有重复的自定义名称
    	List<Map<String, Object>> projectList = searchCoordinates(filters,employeeGuid);
    	if(projectList.size()>0){
    		return false;
    	}
    	
    	TCityCustomCoordinates cityCustomCoordinates = new TCityCustomCoordinates();
    	//自定义区域形状判定
    	if(StringUtils.isNotBlank(filters.get("drawPolygonFlag"))){
    		cityCustomCoordinates.setFlag(filters.get("drawPolygonFlag").toString());
    	}
    	//自定义区域形状判定
    	if(StringUtils.isNotBlank(filters.get("coorDinates"))){
    		cityCustomCoordinates.setCoorDinates(filters.get("coorDinates").toString());
    	}
    	//圆形半径
    	if(StringUtils.isNotBlank(filters.get("radius"))){
    		cityCustomCoordinates.setRadius(filters.get("radius").toString());
    	}
    	//单位
    	if(StringUtils.isNotBlank(employeeGuid)){
    		cityCustomCoordinates.setUnit(employeeGuid);
    	}
    	//自定义区域名称
    	if(StringUtils.isNotBlank(filters.get("coorNames"))){
    		cityCustomCoordinates.setName(filters.get("coorNames").toString());
    	}
    	//几何图形对象
    	if(StringUtils.isNotBlank(filters.get("myGeometryStr"))){
    		cityCustomCoordinates.setMyGeometryStr(filters.get("myGeometryStr").toString());
    	}
    	
    	
    	Date date=new Date();
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	cityCustomCoordinates.setCreateTime(sdf.format(date));
    	this.dao.save(cityCustomCoordinates);
    	return true;
    }
    
    
 
    
    
    /**
	 *  <pre>
	 *   保存空间政府精细化管理项目信息
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  markInfo
	 * 			-- 查询条件
	 * 			
	 * @return
	 *     --  markInfo==null  返回保存事件成功
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月26日下午2:56:15
	 * </pre>
	 */
    @Override
	public void saveEntity(TCityCustomCoordinates markInfo) {
		this.dao.save(markInfo);
	}
    
    
    /**
	 *  <pre>
	 *   根据当前用户获取自定义区域的结果集数据
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  filters
	 * 			-- 查询条件
	 * @param searchSql
	 * 			-- 查询条件
	 * @param orderbySql
	 * 			-- 排序
	 * 			
	 * @return
	 *     --  filters==null  返回当前用户所有数据
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月26日下午2:56:15
	 * </pre>
	 */
	public List<Map<String, Object>> searchCoordinates(Map<String,String> filters,String employeeGuid) {
		
	    StringBuilder SQL = new StringBuilder();
	    SQL.append("  select * from T_CITY_CUSTOM_COORDINATES table1");
	    
	    if(StringUtils.isNotBlank(employeeGuid)&&StringUtils.isNotBlank(filters.get("coorNames"))){
	    	//判断是否有
	    	SQL.append("  where table1.unit ='"+ employeeGuid +"' and table1.name = '"+filters.get("coorNames").toString()+"'");
	    }
	    
	    String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		List<Map<String,Object>> list  =this.dao.findBySql(sql, new Object[]{});
		long endMilis=System.currentTimeMillis();
		logger.debug("SuperMapServiceImpl searchCoordinates 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return list;
	}
	
	
	
	/**
	 *  <pre>
	 *  删除空间政府精细化管理自定义区域的数据
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @param  filters
	 * 			-- 查询条件
	 * 			
	 * @return
	 *     --  filters==null或者filters== ""  不做删除处理
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月29日下午2:56:15
	 * </pre>
	 */
	public boolean deleteCusCoordinates(Map<String,String> filters) {
		
	    StringBuilder SQL = new StringBuilder();
	    
	    if(StringUtils.isNotBlank(filters.get("CusId"))){
	    	SQL.append("  delete  from T_CITY_CUSTOM_COORDINATES tt where tt.sysid = '"+filters.get("CusId")+"'");
	    }
	    
	    String sql = SQL.toString();
		long startMilis=System.currentTimeMillis();
		//执行查询并将结果转化为ListMap
		this.dao.executeSql(sql, new Object[]{});
		long endMilis=System.currentTimeMillis();
		logger.debug("SuperMapServiceImpl deleteCusCoordinates 方法执行查询花费毫秒数" + (endMilis-startMilis));
		return true;
	}





    


}
