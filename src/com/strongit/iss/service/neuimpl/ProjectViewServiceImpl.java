package com.strongit.iss.service.neuimpl;

import com.strongit.iss.common.Cache;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.MD5;
import com.strongit.iss.common.PropertiesUtil;
import com.strongit.iss.entity.ViewBean;
import com.strongit.iss.entity.ViewList;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.service.BaseService;



import com.strongit.iss.service.impl.ReportCacheServiceImpl;
import com.strongit.iss.service.impl.ReportCacheServiceImpl;
import com.strongit.iss.service.neuinterface.IProjectViewService;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *   总览视图实现类
 * Created by tannc on 2016/8/4 21:4
 */
@Service
@Transactional
public class ProjectViewServiceImpl  extends BaseService implements IProjectViewService {
    @Autowired
    private ReportCacheServiceImpl reportCacheService;
    // 地图的查询的主要SQL

    /***
     *  查询地图的数据
     *  @orderBy  申报时间升序、建设地点Code 升序
     *  @module  所属模块
     *   @see  Constant
     *   @return
     *        不存在返回空对象
     * @Author tannongchun
     * @Date 2016/8/4 21:46
     */
    @Override
    public List<ViewBean> findMapData(String module, String type,Map<String,String> params) throws BusinessServiceException {
     String mapSql="SELECT count(*) as \"cnt\",SUM(A.TOTAL_MONEY)as \"investMon\",C.CODE1 AS \"itemCode\",C.NAME1 AS \"itemName\",SUBSTR(A.APPLY_DATE,1,4)"
     		+ " AS \"reportDate\"   "
     		+ "FROM TZXMSPZH.APPLY_PROJECT_INFO A,     TZXMSPZH.V_R_BASE_DIVISION C WHERE    A.DIVISION_CODE =C.CODE AND A.IS_DEL        ='0' "
     		+ "AND A.VALIDITY_FLAG ='1' AND A.PROJECT_CODE <> '' AND A.PROJECT_CODE IS NOT NULL  group by (C.NAME1,C.CODE1,SUBSTR(A.APPLY_DATE,1,4)) order by SUBSTR(A.APPLY_DATE,1,4) asc" ;
            StringBuilder sql=new StringBuilder(mapSql);
        // 五年储备
//            if(Constant.FIVE_PLAN.equals(module)){
//                sql.append(" WHERE  1=1 ");
//            }
//            sql.append(getFilterByModule(module));
//        String zeroNun="0000";
//        Integer num=2;
//        String filterSql="";
//        String filterCode=params.get(Constant.filterCode);
//        // 展现第一级
//        if(Constant.LEVEL_1.equals(params.get(Constant.LEVEL))){
//            // 截取位数
//            zeroNun="00";
//            num=4;
//           filterSql=" AND inf.PROJECT_REGION like '" +filterCode.substring(0,2)+"%'";
//
//        }
//        // 展现第二级
//        if(Constant.LEVEL_2.equals(params.get(Constant.LEVEL))){
//            zeroNun="";
//            // 截取位数
//            num=6;
//            filterSql=" AND inf.PROJECT_REGION  like '" +filterCode.substring(0,4)+"%'";
//        }
//     // 展现第二级
//        if(Constant.LEVEL_3.equals(params.get(Constant.LEVEL))){
//            zeroNun="";
//            // 截取位数
//            num=6;
//            filterSql=" AND inf.PROJECT_REGION  = '" +filterCode.substring(0,6)+"'";
//        }
//        sql.append( filterSql);
//        sql.append(" group by  to_char(inf.STORE_TIME,'yyyy-MM'),SUBSTR(inf.PROJECT_REGION,1,@@@)||'####'" );
//        sql.append(" order by  to_char(inf.STORE_TIME ,'yyyy-MM') asc,SUBSTR(inf.PROJECT_REGION,1,@@@)||'####' asc ");
        long startMilis=System.currentTimeMillis();
//        String exSQL=sql.toString().replaceAll("@@@", num.toString()).replaceAll("####", zeroNun);
        
        SQLQuery query=this.dao.createSqlQuery(mapSql);
        // 将结果转成Bean
        query.setResultTransformer(Transformers.aliasToBean(ViewBean.class));
        // 设置转化的结果
        query.addScalar("cnt", Hibernate.STRING);
        query.addScalar("reportDate", Hibernate.STRING);
        query.addScalar("itemCode", Hibernate.STRING);
        query.addScalar("investMon", Hibernate.STRING);
        query.addScalar("itemName", Hibernate.STRING);
        List list = null ;//  (List)reportCacheService.getEverObject(MD5.encode(sql.toString()));
        if(list == null){
            list = query.list();
           // reportCacheService.putEverObject(MD5.encode((exSQL)), list);
        }
        long endMilis=System.currentTimeMillis();
        logger.debug("ViewServiceImpl   findMapData Calculated consumption time  Millisecond" +(endMilis-startMilis));
        if(null==list){
            list=new ArrayList<ViewBean>();
        }
        // 将CODE转成NAME
//        else{
//            for(com.strongit.iss.entity.ViewBean vv:(List<ViewBean>)list){
//                //  存入对应的CODE
//                //vv.setItemCode(vv.getItemName());
//                String vName= Cache.getNameByCode(PropertiesUtil.getInfoByName("REGION_AREA"),vv.getItemCode());
//                // 对应的值写回去
//                vv.setItemName(vName);
//
//            }
//        }
        return list;
    }
    /***
     *   查询维度的信息
     *  @orderBy  维度的Code 升序
     *  @ param dimension  所属维度
     *   @return
     *        不存在返回空对象
     * @Author tannongchun
     * @Date 2016/8/4 21:46
     */
    @Override
    public List<ViewBean> findDimensionData(String dimension, String type, Map<String,String> params) throws BusinessServiceException {
        Map<String,Object>  dims=getDimension(dimension);
        StringBuilder sql = null;
        if(null!=type&&"1".equals(type)){
        	// 按层级统计项目数和总投资
        	 sql=new StringBuilder(" SELECT B.CODE_VALUE AS \"itemCode\",B.CODE_NAME AS \"itemName\",COUNT(A.ID) AS \"cnt\",SUM(A.TOTAL_MONEY) AS \"investMon\"  ");
             sql.append("    FROM TZXMSPZH.FA_XMXX A,TZXMSPZH.DIM_SPCJ B ");
             sql.append(" WHERE A.SPCJ=B.CODE_VALUE");
             sql.append(" GROUP BY B.CODE_VALUE,B.CODE_NAME");
             sql.append(" ORDER BY B.CODE_VALUE,B.CODE_NAME;");
        }else if(null!=type&&"0".equals(type)){
        	// 按国标行业统计项目数和总投资
             sql=new StringBuilder(" SELECT B.CODE_VALUE AS \"itemCode\",B.CODE_NAME AS \"itemName\",COUNT(A.ID) AS \"cnt\",SUM(A.TOTAL_MONEY) AS \"investMon\" "
             		+ "FROM TZXMSPZH.FA_XMXX A,TZXMSPZH.DIM_SPCJ B "
             		+ "WHERE A.SPCJ=B.CODE_VALUE"
             		+ "GROUP BY B.CODE_VALUE,B.CODE_NAME"
             		+ "ORDER BY B.CODE_VALUE,B.CODE_NAME;");
                                
                               /// 过滤条件占位符
//                                sql.append(" %1$s");
//                                sql.append(" WHERE length(di.dic_item_fullcode )>=6");
//                                sql.append("  group by   SUBSTR(di.dic_item_fullcode,1,").append(dims.get("len")).append(")order by   SUBSTR(di.dic_item_fullcode,1,").append(dims.get("len")).append(") asc");
        }
        else if(null!=type&&"2".equals(type)){
        	// 按投资规模项目数和总投资
             sql=new StringBuilder(" SELECT B.CODE_VALUE AS \"itemCode\",B.CODE_NAME AS \"itemName\",COUNT(A.ID) AS \"cnt\",SUM(A.TOTAL_MONEY) AS \"investMon\" "
             		+ " FROM TZXMSPZH.FA_XMXX A,TZXMSPZH.DIM_SPCJ B "
             		+ " WHERE A.SPCJ=B.CODE_VALUE"
             		+ " GROUP BY B.CODE_VALUE,B.CODE_NAME"
             		+ " ORDER BY B.CODE_VALUE,B.CODE_NAME;");
                                
                               /// 过滤条件占位符
//                                sql.append(" %1$s");
//                                sql.append(" WHERE length(di.dic_item_fullcode )>=6");
//                                sql.append("  group by   SUBSTR(di.dic_item_fullcode,1,").append(dims.get("len")).append(")order by   SUBSTR(di.dic_item_fullcode,1,").append(dims.get("len")).append(") asc");
        }
                                long startMilis=System.currentTimeMillis();
//         String fStr="";
//        //过滤
//        String code=params.get(Constant.filterCode);
//        if(StringUtils.isBlank(code)){
//            fStr="";
//        }
//      // 省级
//        else if("0000".equals(code.substring(2,6))){
//            fStr=" and project_region like '"+code.substring(0,2)+"%'";
//        }
//           //市级
//        else if("00".equals(code.substring(4,6))){
//            fStr=" and project_region like '"+code.substring(0,4)+"%'";
//        }
//          //县级
//        else {
//            fStr=" and project_region ='"+code+"'";
//        }
//        String exSql=String.format(sql.toString(),fStr);
         String exSql=String.format(sql.toString());
         SQLQuery query=this.dao.createSqlQuery(exSql);
//        // 将结果转成Bean
       query.setResultTransformer(Transformers.aliasToBean(ViewBean.class));
        // 设置转化的结果
        query.addScalar("cnt", Hibernate.STRING);
        query.addScalar("itemCode", Hibernate.STRING);
        query.addScalar("investMon", Hibernate.STRING);
        query.addScalar("itemName", Hibernate.STRING);
        // 执行查询语句
        List<ViewBean> list = null;
        if(list == null){
            list = query.list();
//            reportCacheService.putEverObject(MD5.encode(exSql), list);
        }
        long endMilis=System.currentTimeMillis();
        logger.debug("ViewServiceImpl   findDimensionData Calculated consumption time  Millisecond" +(endMilis-startMilis));
//        if(null==list){
//            list=new ArrayList<ViewBean>();
//        }
//        else{
//            for(ViewBean vv:(List<ViewBean>)list){
//                // 项目资金
//                String vName= Cache.getNameByCode(dims.get("group").toString(),vv.getItemCode());
//                // 对应的值写回去
//                vv.setItemName(vName);
//            }
//        }
        return list;

    }

    /**
     *
     * @orderBy
     * @param type
     * @param module
     * @return
     * @throws BusinessServiceException
     * @author wuwei
     * @Date 2016年8月16日下午2:28:49
     */
    @SuppressWarnings("unchecked")
	public List<ViewBean> findDimensionData1(String type,String module, Map<String,String> params) throws BusinessServiceException {
      //Map<String,String> map = getMaps(type);
      Map<String,String> map = getRollMaps(type);
      StringBuilder SQL = new StringBuilder();
      SQL.append(" SELECT COUNT(inf.PROJECT_GUID)AS \"cnt\",");
      SQL.append(" SUM(inf.INVESTMENT_TOTAL) AS \"investMon\",");
      if("report".equalsIgnoreCase(type)){
    	  SQL.append("to_char(inf.STORE_TIME ,'yyyy-MM') as  \"reportDate\",");
      }
      SQL.append(" "+map.get("name")+" AS \"itemCode\"");
      //SQL.append(" SUBSTR(inf.PROJECT_REGION,1,%1$d)||'%2$s' as \"itemCode\"");
      SQL.append(" FROM T_FGW_STATIS_PROJECT_INFO inf ");
      if(Constant.REPORT_UNIT.equalsIgnoreCase(type)){
    	  SQL.append(" LEFT JOIN T_FGW_STATIS_DEPARTMENT DP ON inf.CREATE_UNIT = DP.DEPARTMENT_GUID ");
      }
      // 五年储备
      if(Constant.FIVE_PLAN.equals(module)){
    	  SQL.append(" WHERE  1=1 ");
      }
      SQL.append(getFilterByModule(module));
      //过滤
      String fStr = "";
      Integer num = 2;
      String zeroNum="0000";
      //过滤
      String code=params.get(Constant.filterCode);
      if(StringUtils.isBlank(code)){
      	fStr = "";
      	//sql = String.format(sql.toString(),2,"0000",fStr);
      }
      // 省级
      else if("0000".equals(code.substring(2,6))){
      	fStr = " and inf.PROJECT_REGION like '"+code.substring(0,2)+"%' and inf.PROJECT_REGION <> '" + code + "' ";
      	num = 4;
      	zeroNum = "00";
      	//sql = String.format(sql.toString(),4,"00",fStr);
      }
      //市级
      else if("00".equals(code.substring(4,6))){
      	fStr = " and inf.PROJECT_REGION like '"+code.substring(0,4)+"%' and inf.PROJECT_REGION <> '" + code + "' ";
      	num = 6;
      	zeroNum = "";
      	//sql = String.format(sql.toString(),6,"",fStr);
      }
      SQL.append(" %3$s GROUP BY "+map.get("name")+"");
      //SQL.append(" GROUP BY SUBSTR(inf.PROJECT_REGION,1,%1$d)||'%2$s' ");
      if(Constant.REPORT_UNIT.equalsIgnoreCase(type)){
    	  SQL.append(",to_char(inf.STORE_TIME, 'yyyy-MM')");
      }
      SQL.append(" ORDER BY "+map.get("name")+" ASC");
      //SQL.append(" ORDER BY SUBSTR(inf.PROJECT_REGION,1,%1$d)||'%2$s' ASC");
      if(Constant.REPORT_UNIT.equalsIgnoreCase(type)){
    	  SQL.append(",to_char(inf.STORE_TIME, 'yyyy-MM')");
      }
      String sql = String.format(SQL.toString(), num, zeroNum, fStr);
      long startMilis=System.currentTimeMillis();
      SQLQuery query=this.dao.createSqlQuery(sql);
      // 将结果转成Bean
      query.setResultTransformer(Transformers.aliasToBean(ViewBean.class));
      // 设置转化的结果
      query.addScalar("cnt", Hibernate.STRING);
      query.addScalar("itemCode", Hibernate.STRING);
      query.addScalar("investMon", Hibernate.STRING);
      if(Constant.REPORT_UNIT.equalsIgnoreCase(type)){
    	  query.addScalar("reportDate", Hibernate.STRING);
      }
//      ViewBean.groupNo = map.get("groupNo");
      // 执行查询语句

        List list = (List)reportCacheService.getEverObject(MD5.encode(SQL.toString()));
        if(list == null){
            list = query.list();
            //reportCacheService.putEverObject(MD5.encode(SQL.toString()), list);
        }
      long endMilis=System.currentTimeMillis();
      logger.debug("ViewServiceImpl   findDimensionData Calculated consumption time  Millisecond" +(endMilis-startMilis));
      if(null==list){
          list=new ArrayList<ViewBean>();
      }
      else{
          for (ViewBean vv : (List<ViewBean>)list) {
        	  String vName = "";
        	  if(Constant.FGW_INDUSTRY.equals(type)){
        		  vName = Cache.getNameByCode(PropertiesUtil.getInfoByName("FGW_INDUSTRY").toString(),vv.getItemCode());
        	  } else if (Constant.BUILD_PLACE.equals(type) || Constant.REPORT_UNIT.equals(type)) {
        		  vName = Cache.getNameByCode(PropertiesUtil.getInfoByName("REGION_AREA").toString(),vv.getItemCode());
        	  }
              // 对应的值写回去
              vv.setItemName(vName);
          }
      }
      return list;
  }
    
    /**
     * 三年滚动-根据类型获取属性
     * @orderBy 
     * @param type
     * @return
     * @author XiaXiang
     * @Date 2016年8月24日下午3:23:17
     */
    private Map<String, String> getRollMaps(String type) {
    	Map<String,String> map=new HashMap<String,String>();
        if(Constant.FGW_INDUSTRY.equals(type)){
        	map.put("code","SUBSTR(inf.INDUSTRY_CODE,0,4)");
        	map.put("name", "SUBSTR(inf.INDUSTRY_CODE,0,4)||'00'");
        	map.put("groupNo", "A00003");
        }
        if(Constant.BUILD_PLACE.equals(type)){
        	map.put("name", "SUBSTR(inf.PROJECT_REGION,1,%1$d)||'%2$s'");
        	map.put("groupNo", "A00001");
        }
        if(Constant.REPORT_UNIT.equals(type)){
        	map.put("name", "SUBSTR(DP.BELONG_AREA,1,%1$d)||'%2$s'");
            map.put("groupNo", "A00001");
        }
        return map;
	}
    
    /**
     *
     * @orderBy
     * @param type
     * @return
     * @author wuwei
     * @Date 2016年8月16日下午3:54:28
     */
    private Map<String,String> getMaps(String type) {
    	Map<String,String> map=new HashMap<String,String>();
        if(Constant.FGW_INDUSTRY.equals(type)){
        	map.put("code","SUBSTR(PI.INDUSTRY_CODE,0,4)");
        	map.put("name", "SUBSTR(PI.INDUSTRY_CODE,0,4)||'00'");
        	map.put("groupNo", "A00003");
        }
        if(Constant.BUILD_PLACE.equals(type)){
           map.put("name", "SUBSTR(PI.PROJECT_REGION,0,2)||'0000'");
           map.put("groupNo", "A00001");
        }
        if(Constant.REPORT_UNIT.equals(type)){
        	map.put("name", "SUBSTR(DP.BELONG_AREA,0,2)||'0000'");
            map.put("groupNo", "A00001");
        }
        return map;
	}
	/***
     *   得到模块对应的过滤条件
     * @Author tannongchun
     * @Date 2016/8/4 22:05
     */
    private String getFilterByModule(String module){
         // 五年储备
        if(Constant.FIVE_PLAN.equals(module)){
            return "";
        }
        // 三年滚动计划
       else if(Constant.ROLL_PLAN.equals(module)){
            StringBuilder sb=new StringBuilder("WHERE  inf.is_roll_plan=1");
            return sb.toString();
        }
               // 中央预算内
        else if(Constant.BUDGET.equals(module)){
            StringBuilder sb=new StringBuilder();
            sb.append(" WHERE  inf.is_year_plan=1");
            return sb.toString();
        }
        // 专项建设基金
      else   if(Constant.FUNDS.equals(module)){
            StringBuilder sb=new StringBuilder();
            sb.append("WHERE  inf.is_spe_plan=1");
            return sb.toString();
        }
        // 默认展现五年储备
        else {
            return "";
        }
    }

    /***
     *   得到模块的金字塔数据
     * @Orderby 根据需求文档说明排序的
     * @param module
     *         -- module == null or module=='' 返回总览的金字塔
     *         -- 否则返回对应模块的金字塔
     * @retrun
     *        -- 没有查询结果，返回 空对象
     *        -- 有查询结果 返回对应的查询结果
     * @Author tannongchun
     * @Date 2016/8/6 20:39
     */
    @Override
    public List<ViewBean>  getPyramidData(String module,Map<String,String> params ){
        StringBuilder sb=getPyramidSql(module,params);
        long startMilis=System.currentTimeMillis();
        //System.out.println("0000000000000000000000000000000000000000000000000\n"+sb.toString());
        SQLQuery query=this.dao.createSqlQuery(sb.toString());
        // 将结果转成Bean
        query.setResultTransformer(Transformers.aliasToBean(ViewBean.class));
        // 设置转化的结果
        query.addScalar("cnt", Hibernate.STRING);
        query.addScalar("itemName", Hibernate.STRING);
        query.addScalar("investMon", Hibernate.STRING);
        // 执行查询语句
        List list = (List)reportCacheService.getEverObject(MD5.encode(sb.toString()));
        if(list == null){
            list = query.list();
            reportCacheService.putEverObject(MD5.encode(sb.toString()), list);
        }
        long endMilis=System.currentTimeMillis();
        logger.debug("ViewServiceImpl   getPyramidData Calculated consumption time  Millisecond" +(endMilis-startMilis));
        if(null==list){
            list=new ArrayList<ViewBean>();
        }
        return list;
    }

    /***
     *  得到维度的长度 len ,分组dic_group
     * @Author tannongchun
     * @Date 2016/8/5 0:54
     */
    private Map<String,Object> getDimension( String dimension){
        Map<String,Object> map=new HashMap<String,Object>();
        if(Constant.MAJOR_STRATEGIC.equals(dimension)){
            //  一级code的长度
            map.put("len",6);
            // 查询的维度
            map.put("dimension","FIT_MAJOR_STRATEGIC");
            // 分组code
            map.put("group","A00014");
        }
        // 发改委行业
        if(Constant.FGW_INDUSTRY.equals(dimension)){
            //  一级code的长度
            map.put("len",6);
            // 查询的维度
            map.put("dimension","INDUSTRY_CODE");
            // 分组code
            map.put("group","A00003");
        }
        // 符合政府投资方向
        if(Constant.GOV_DIRECTION.equals(dimension)){
            //  一级code的长度
            map.put("len",6);
            // 查询的维度
            map.put("dimension","FIT_IND_POLICY_CODE");
            // 分组code
            map.put("group","A00009");
        }
        return map;
    }

    /**
     *  获取金子塔的SQL
     * @return
     */
    private StringBuilder getPyramidSql(String module,Map<String,String> params){
          StringBuilder sb =new StringBuilder();
        if(StringUtils.isBlank(module)) {
            String fStr="";
            sb.append(" select count(1)as \"cnt\",sum(investment_total) as \"investMon\",max('中央预算内和专项建设基金')as \"itemName\"");
            sb.append(" from t_fgw_statis_project_info");
            sb.append(" WHERE (is_year_plan=1 or is_spe_plan=1) %1$s");
            sb.append(" union all");
            sb.append(" select count(1)as \"cnt\",sum(investment_total) as \"investMon\",max('审批核准备案') as \"itemName\"");
            sb.append(" from t_fgw_statis_project_info");
            sb.append("  where PROJECT_CODE is not null %1$s ");
            sb.append("  union all");
            sb.append(" select count(1)as \"cnt\",sum(investment_total)as \"investMon\",max('三年滚动计划')as \"itemName\"");
            sb.append(" from t_fgw_statis_project_info ");
            sb.append("  where  is_roll_plan=1  %1$s");
            sb.append(" union all");
            sb.append(" select count(1)  as \"cnt\",sum(investment_total) as \"investMon\",  max('项目储备')as \"itemName\"");
            sb.append(" from t_fgw_statis_project_info WHERE 1=1 %1$s");
            //过滤
            String code=params.get(Constant.filterCode);
            if(StringUtils.isBlank(code)){
                fStr="";
            }
            // 省级
           else if("0000".equals(code.substring(2,6))){
                fStr=" and project_region like '"+code.substring(0,2)+"%'";
            }
            //市级
            else if("00".equals(code.substring(4,6))){
                fStr=" and project_region like '"+code.substring(0,4)+"%'";
            }
            //县级
            else {
                fStr=" and project_region ='"+code+"'";
            }
              String fs=String.format(sb.toString(),fStr);
              sb=new StringBuilder(fs);
        }
        // 五年滚动计划
        else if(Constant.FIVE_PLAN.equals(module)){
            sb.append("SELECT   count(project_guid)  as \"cnt\", sum(investment_total) as \"investMon\", ");
            sb.append(" max('中央预算内和专项建设基金')  as \"itemName\" ");
            sb.append(" FROM t_fgw_statis_project_info   where (is_year_plan=1 or is_spe_plan=1)  %1$s ");
            sb.append(" UNION ALL");
            sb.append(" SELECT count(1)  as \"cnt\",sum(investment_total) as \"investMon\",  max('五年规划储备')as \"itemName\"");
            sb.append(" FROM t_fgw_statis_project_info  %2$s ");
            String fStr;
            String  fStr2="";
            //过滤
            String code=params.get(Constant.filterCode);
            if(StringUtils.isBlank(code)){
                fStr="";
                fStr2="";
            }
            // 省级
            else if("0000".equals(code.substring(2,6))){
                fStr=" where  project_region like '"+code.substring(0,2)+"%'";
                fStr2=" and  project_region like '"+code.substring(0,2)+"%'";
            }
            //市级
            else if("00".equals(code.substring(4,6))){
                fStr=" where project_region like '"+code.substring(0,4)+"%'";
                fStr2="  and  project_region like '"+code.substring(0,4)+"%'";
            }
            //县级
            else {
                fStr=" where  project_region ='"+code+"'";
                fStr2=" and  project_region ='"+code+"'";
            }
            String fs=String.format(sb.toString(),fStr2,fStr);
            sb=new StringBuilder(fs);
        }
        // 三年滚动计划
        else if (Constant.ROLL_PLAN.equals(module)){
                sb.append("SELECT   count(project_guid)  as \"cnt\", sum(investment_total) as \"investMon\", ");
                sb.append(" max('中央预算内和专项建设基金')  as \"itemName\" ");
                sb.append(" FROM t_fgw_statis_project_info   where (is_year_plan=1 or is_spe_plan=1)  %1$s ");
                sb.append(" UNION ALL");
                sb.append(" select count(1)  as \"cnt\",sum(investment_total) as \"investMon\",  max('三年滚动计划')as \"itemName\"");
                sb.append(" from t_fgw_statis_project_info");
                // 三年滚动计划
                sb.append(" where  is_roll_plan=1  %1$s");

            String fStr="";
            //过滤
            String code=params.get(Constant.filterCode);
            if(StringUtils.isBlank(code)){
                fStr="";
            }
            // 省级
            else if("0000".equals(code.substring(2,6))){
                fStr=" and project_region like '"+code.substring(0,2)+"%'";
            }
            //市级
            else if("00".equals(code.substring(4,6))){
                fStr=" and project_region like '"+code.substring(0,4)+"%'";
            }
            //县级
            else {
                fStr=" and project_region ='"+code+"'";
            }
            String fs=String.format(sb.toString(),fStr);
            sb=new StringBuilder(fs);
        }
        // 中央预算内
        else if(Constant.BUDGET.equals(module)){
            sb.append(" select COUNt(decode(cr.FUNDS_TYPE_CODE, 'A00016', decode(cr.type, '2', cr.CUR_MONEY, null), null)) as \"cnt\",");
            sb.append(" SUM(decode(cr.FUNDS_TYPE_CODE, 'A00016', decode(cr.type, '2', cr.CUR_MONEY, null), null))   as \"investMon\",");
            sb.append(" max('完成资金')   as \"itemName\"");
            sb.append(" from T_FGW_STATIS_CAPIRAL_REACH cr");
            sb.append(" where  cr.dispatch_guid in  ( select first_value(t.dispatch_guid) over (partition by t.project_guid order by t.report_number desc) ");
            sb.append("  from t_fgw_statis_project_dispatch t)  %1$s");
            sb.append(" union all ");
            sb.append(" select");
            sb.append(" COUNt(decode(cr.FUNDS_TYPE_CODE, 'A00016', decode(cr.type, '3', cr.CUR_MONEY, null), null)) as \"cnt\",");
            sb.append(" SUM(decode(cr.FUNDS_TYPE_CODE, 'A00016', decode(cr.type, '3', cr.CUR_MONEY, null), null))   as \"investMon\",");
            sb.append(" max('支付资金')   as \"itemName\"");
            sb.append(" from T_FGW_STATIS_CAPIRAL_REACH cr");
             sb.append(" where  cr.dispatch_guid in  ( select first_value(t.dispatch_guid) over (partition by t.project_guid order by t.report_number desc) ");
            sb.append("  from t_fgw_statis_project_dispatch t)  %1$s");
            sb.append(" union all  ");
            sb.append(" select");
            sb.append(" COUNt(decode(cr.FUNDS_TYPE_CODE, 'A00016', decode(cr.type, '1', cr.CUR_MONEY, null), null)) as \"cnt\",");
            sb.append(" SUM(decode(cr.FUNDS_TYPE_CODE, 'A00016', decode(cr.type, '1', cr.CUR_MONEY, null), null))   as \"investMon\",");
            sb.append(" max('到位资金')   as \"itemName\"");
            sb.append(" from T_FGW_STATIS_CAPIRAL_REACH cr");
            sb.append(" where  cr.dispatch_guid in  ( select first_value(t.dispatch_guid) over (partition by t.project_guid order by t.report_number desc) ");
            sb.append("  from t_fgw_statis_project_dispatch t)  %1$s");
            sb.append(" union all    ");
            sb.append(" select  ");
            sb.append(" count(decode(cr.FUNDS_TYPE_CODE, 'A00016', cr.TOTAL_ISSUED_CAPTIAL, null))  as  \"cnt\",");
            sb.append(" SUM(decode(cr.FUNDS_TYPE_CODE, 'A00016', cr.TOTAL_ISSUED_CAPTIAL, null))  as  \"investMon\",");
            sb.append(" max('下达资金')   as \"itemName\"");
            sb.append(" from T_FGW_STATIS_CAPIRAL_SOURCE cr  %2$s");
            sb.append(" union all  ");
            sb.append(" select  ");
            sb.append(" count(decode(cr.FUNDS_TYPE_CODE, 'A00016', cr.TOTAL_ISSUED_CAPTIAL, null))  as  \"cnt\",");
            sb.append(" SUM(decode(cr.FUNDS_TYPE_CODE, 'A00016', cr.TOTAL_ISSUED_CAPTIAL, null))  as  \"investMon\",");
            sb.append(" max('资金需求')   as \"itemName\"");
            sb.append(" from T_FGW_STATIS_CAPIRAL_SOURCE cr   %2$s");

            String fStr="";
            String fStr2="";
            //过滤
            String code=params.get(Constant.filterCode);
            if(StringUtils.isBlank(code)){
                fStr="  AND exists( SELECT 1 from  t_fgw_statis_project_info  where project_guid=cr.project_guid and  is_year_plan=1 )";
                fStr2=" where   exists( SELECT 1 from  t_fgw_statis_project_info  where project_guid=cr.project_guid and  is_year_plan=1 )";

            }
            // 省级
            else if("0000".equals(code.substring(2,6))){
                fStr="  and  exists( SELECT 1 from  t_fgw_statis_project_info  where project_guid=cr.project_guid and is_year_plan=1 and project_region like '"+code.substring(0,2)+"%')";
                fStr2=" where   exists( SELECT 1 from  t_fgw_statis_project_info  where project_guid=cr.project_guid and is_year_plan=1 and project_region like '"+code.substring(0,2)+"%')";
            }
            //市级
            else if("00".equals(code.substring(4,6))){
                fStr=" and  exists( SELECT 1 from  t_fgw_statis_project_info where project_guid=cr.project_guid and is_year_plan=1  and project_region like '"+code.substring(0,4)+"%')";
                fStr2=" where   exists( SELECT 1 from  t_fgw_statis_project_info where project_guid=cr.project_guid and is_year_plan=1  and project_region like '"+code.substring(0,4)+"%')";
            }
            //县级
            else {
                fStr=" and exists( SELECT 1 from  t_fgw_statis_project_info where  project_guid=cr.project_guid and is_year_plan=1 and  project_region ='"+code+"')";
                fStr2=" where   exists( SELECT 1 from  t_fgw_statis_project_info where  project_guid=cr.project_guid and is_year_plan=1 and  project_region ='"+code+"')";
            }
            String fs=String.format(sb.toString(),fStr,fStr2);
            sb=new StringBuilder(fs);
        }
        // 专项建设基金
        else if(Constant.FUNDS.equals(module)){
            sb.append(" SELECT");
            sb.append(" COUNT(decode(cs.FUNDS_TYPE_CODE, 'A00180', cs.PUTIN_CAPTIAL, null))  as  cnt,");
            sb.append(" SUM(decode(cs.FUNDS_TYPE_CODE, 'A00180', cs.PUTIN_CAPTIAL, null))  as  investMon,");
            sb.append(" MAX('投放资金')   as itemName");
            sb.append(" FROM T_FGW_STATIS_CAPIRAL_SOURCE cs %1$s");
            sb.append(" UNION ALL ");
            sb.append(" SELECT   ");
            sb.append(" COUNT(decode(cs.FUNDS_TYPE_CODE, 'A00180', cs.SUG_ARRANGE_CAPTIAL, null))  as  cnt,");
            sb.append(" SUM(decode(cs.FUNDS_TYPE_CODE, 'A00180', cs.SUG_ARRANGE_CAPTIAL, null))  as  investMon,");
            sb.append(" MAX('建议安排资金')   as itemName");
            sb.append(" FROM T_FGW_STATIS_CAPIRAL_SOURCE cs  %1$s");
            sb.append(" UNION ALL ");
            sb.append(" SELECT  ");
            sb.append(" count(decode(cs.FUNDS_TYPE_CODE, 'A00180', cs.CUR_APPLY_SPE_CAPTIAL, null))  as  cnt,");
            sb.append(" SUM(decode(cs.FUNDS_TYPE_CODE, 'A00180', cs.CUR_APPLY_SPE_CAPTIAL, null))  as  investMon,");
            sb.append(" MAX('本次申请专项基金')   as itemName");
            sb.append(" FROM T_FGW_STATIS_CAPIRAL_SOURCE cs  %1$s");
            String fStr="";
            //过滤
            String code=params.get(Constant.filterCode);
            if(StringUtils.isBlank(code)){
                fStr=" where   exists( SELECT 1 from  t_fgw_statis_project_info  where project_guid=cs.project_guid and  is_spe_plan=1 )";
            }
            // 省级
            else if("0000".equals(code.substring(2,6))){
                fStr=" where   exists( SELECT 1 from  t_fgw_statis_project_info  where project_guid=cs.project_guid and is_spe_plan=1 and project_region like '"+code.substring(0,2)+"%')";
            }
            //市级
            else if("00".equals(code.substring(4,6))){
                fStr=" where   exists( SELECT 1 from  t_fgw_statis_project_info where project_guid=cs.project_guid and is_spe_plan=1  and project_region like '"+code.substring(0,4)+"%')";
            }
            //县级
            else {
                fStr=" where   exists( SELECT 1 from  t_fgw_statis_project_info where  project_guid=cs.project_guid and is_spe_plan=1 and  project_region ='"+code+"')";
            }
            String fs=String.format(sb.toString(),fStr);
            sb=new StringBuilder(fs);


        }
           return sb;
    }
    /**
     * 得到饼状结构的数据
     */
	public List<ViewBean> getPieData(String module, Map<String,String> params)
			throws BusinessServiceException {
		String sql =
			"SELECT count(inf.project_guid)     as \"cnt\" ,"+
			//" SUBSTR(di.dic_item_fullcode, 1, 6) as \"itemName\","+
			" SUBSTR(di.dic_item_code,1,%1$d)||'%2$s' as \"itemCode\"," +
			" SUM(inf.INVESTMENT_TOTAL)          as \"investMon\""+
			" from"+
			" T_FGW_STATIS_PROJECT_INFO inf"+
			" left join T_FGW_STATIS_DEPARTMENT d"+
			" on"+
			" inf.STORE_DEPT=d.DEPARTMENT_GUID"+
			" left join T_FGW_statis_dictionary_items di"+
			" on"+
			" di.dic_item_code=d.BELONG_AREA AND di.dic_group='A00001'"+
			" where 1=1 %3$s" +
			" group by"+
			//" SUBSTR(di.dic_item_fullcode, 1, 6), " +
			" SUBSTR(di.dic_item_code,1,%1$d)||'%2$s' "+
			" order by"+
			" SUBSTR(di.dic_item_code,1,%1$d)||'%2$s' desc;";
        //过滤
        String fStr = "";
        //过滤
        String code=params.get(Constant.filterCode);
        if(StringUtils.isBlank(code)){
        	fStr="";
        	sql = String.format(sql.toString(),2,"0000",fStr);
        }
        // 省级
        else if("0000".equals(code.substring(2,6))){
        	fStr=" and di.dic_item_code like '"+code.substring(0,2)+"%'";
        	sql = String.format(sql.toString(),4,"00",fStr);
        }
        //市级
        else if("00".equals(code.substring(4,6))){
        	fStr=" and di.dic_item_code like '"+code.substring(0,4)+"%'";
        	sql = String.format(sql.toString(),6,"",fStr);
        }
		StringBuffer sbf = new StringBuffer(sql);
		SQLQuery query=this.dao.createSqlQuery(sbf.toString());
        // 将结果转成Bean
        query.setResultTransformer(Transformers.aliasToBean(ViewBean.class));
        // 设置转化的结果
        query.addScalar("cnt", Hibernate.STRING);
        query.addScalar("itemCode", Hibernate.STRING);
        query.addScalar("investMon", Hibernate.STRING);
        // 执行查询语句
        // 执行查询语句
        List list = (List)reportCacheService.getEverObject(MD5.encode(sbf.toString()));
        if(list == null){
            list = query.list();
            reportCacheService.putEverObject(MD5.encode(sbf.toString()), list);
        }
	        if(null==list){
	            list=new ArrayList<ViewBean>();
	        }
	        else{
	            for(ViewBean vv:(List<ViewBean>)list){
//	                vv.setItemCode(vv.getItemName());
	                String vName= Cache.getNameByCode(PropertiesUtil.getInfoByName("REGION_AREA").toString(),vv.getItemCode());
	                // 对应的值写回去
	                vv.setItemName(vName);
	            }
	        }
		return list;
	}
	/**
	 * 得到所有的资金和个数
	 * @orderBy
	 * @param type
	 * @return
	 * @throws BusinessServiceException
	 * @author wuwei
	 * @Date 2016年8月18日下午3:29:36
	 */
	public List<ViewBean> getAllInfo(String type,Map<String,String> params) throws BusinessServiceException {
		Map<String,String> map = getMaps(type);
		StringBuilder SQL = new StringBuilder();
		SQL.append(" SELECT B.CODE_VALUE AS \"itemCode\",B.CODE_NAME AS \"itemName\",COUNT(A.ID) AS \"cnt\",SUM(A.TOTAL_MONEY) AS \"investMon\"");
		SQL.append(" FROM TZXMSPZH.FA_XMXX A,TZXMSPZH.DIM_TZGM B");
		SQL.append(" WHERE A.TZGM=B.CODE_VALUE");
		SQL.append(" GROUP BY B.CODE_VALUE,B.CODE_NAME");
		SQL.append(" ORDER BY B.CODE_VALUE,B.CODE_NAME;");
	    long startMilis=System.currentTimeMillis();
//        String  filterSql="";
//        // 省级
//        if(Constant.LEVEL_1.equals(params.get(Constant.LEVEL)))
//        {
//            // 地区下钻联动的地区CODE
//            String filterCode=params.get(Constant.filterCode);
//            // 省级
//            filterSql=" and  pi.project_region like '"+filterCode.substring(0,2)+"%'";
//        }
//        // 市级
//        else  if(Constant.LEVEL_2.equals(params.get(Constant.LEVEL))){
//            // 地区下钻联动的地区CODE
//            String filterCode=params.get(Constant.filterCode);
//            // 省级
//            filterSql=" and  pi.project_region like '"+filterCode.substring(0,4)+"%'";
//        }
//        // 县级
//        else  if(Constant.LEVEL_3.equals(params.get(Constant.LEVEL))){
//            // 地区下钻联动的地区CODE
//            String filterCode=params.get(Constant.filterCode);
//            // 省级
//            filterSql=" and  pi.project_region like '"+filterCode.substring(0,6)+"%'";
//        }

//	    SQLQuery query=this.dao.createSqlQuery(String.format(SQL.toString(),filterSql));
	    SQLQuery query=this.dao.createSqlQuery(String.format(SQL.toString()));
	    // 将结果转成Bean
	    query.setResultTransformer(Transformers.aliasToBean(ViewList.class));
	    if(Constant.FGW_INDUSTRY.equals(type)){
	    	ViewList.groupNo = map.get("groupNo");
	    }
	    // 设置转化的结果
	    query.addScalar("itemCode", Hibernate.STRING);
	    query.addScalar("itemName", Hibernate.STRING);
	    query.addScalar("cnt", Hibernate.STRING);
	    query.addScalar("investMon", Hibernate.STRING);
	    // 执行查询语句
        List list =null;// (List)reportCacheService.getEverObject(MD5.encode(SQL.toString()));
        if(list == null){
            list = query.list();
            //reportCacheService.putEverObject(MD5.encode(SQL.toString()), list);
        }
	    ViewList.groupNo = "";
	    long endMilis=System.currentTimeMillis();
	    logger.debug("ViewServiceImpl   findDimensionData Calculated consumption time  Millisecond" +(endMilis-startMilis));
	    return list;
	}
	/**
     *
     * @orderBy
     * @param type
     * @param module
     * @return
     * @throws BusinessServiceException
     * @author wuwei
     * @Date 2016年8月16日下午2:28:49
     */
	@Override
	public List<ViewBean> fivePlanData(String module, String type, Map<String,String> params) {
		Map<String,String> map = getMaps(type);
	    StringBuilder SQL = new StringBuilder();
	    SQL.append(" SELECT COUNT(PI.PROJECT_GUID)AS \"cnt\",");
	    SQL.append(" SUM(PI.INVESTMENT_TOTAL) AS \"investMon\",");
	    SQL.append(" SUBSTR(PI.PROJECT_REGION,1,@@@)||'####' as \"itemCode\",");
	    SQL.append(" PI.EXPECT_STARTYEAR||PI.EXPECT_NY as \"reportDate\"");

	    //SQL.append(" "+map.get("name")+" AS \"name\"");
	    SQL.append(" FROM T_FGW_STATIS_PROJECT_INFO PI");
	    if(Constant.REPORT_UNIT.equalsIgnoreCase(type)){
	    	SQL.append(" LEFT JOIN T_FGW_STATIS_DEPARTMENT DP ON PI.CREATE_UNIT = DP.DEPARTMENT_GUID");
	    }
	    //  SQL.append(getFilterByModule(module)); 只展现2015年之后的数据
	    SQL.append(" WHERE length( PI.EXPECT_STARTYEAR)=4 AND EXPECT_STARTYEAR >2015 ");
    
	    String zeroNun="0000";
	    Integer num=2;
	    String filterSql="";
	    String filterCode=params.get(Constant.filterCode);
	    // 展现第一级
	    if(Constant.LEVEL_1.equals(params.get(Constant.LEVEL))){
	    	// 截取位数
	    	zeroNun="00";
	    	num=4;
	    	filterSql=" AND PI.PROJECT_REGION like '" +filterCode.substring(0,2)+"%'";

	    }
	    // 展现第二级
	    if(Constant.LEVEL_2.equals(params.get(Constant.LEVEL))){
	    	// 截取位数
	    	zeroNun="";
	    	num=6;
	    	filterSql=" AND PI.PROJECT_REGION  like '" +filterCode.substring(0,4)+"%'";
	    }
	    // 展现第二级
	    if(Constant.LEVEL_3.equals(params.get(Constant.LEVEL))){
	    	// 截取位数
	    	zeroNun="";
	    	num=6;
	    	filterSql=" AND PI.PROJECT_REGION  = '" +filterCode.substring(0,6)+"'";
	    }
	    SQL.append(filterSql);
	    //SQL.append(" GROUP BY "+map.get("name")+"");
	    SQL.append(" GROUP BY ");
	    SQL.append(" SUBSTR(PI.PROJECT_REGION,1,@@@)||'####' ");
	    SQL.append(" ,PI.EXPECT_STARTYEAR||PI.EXPECT_NY");
	    //SQL.append(" ORDER BY "+map.get("name")+" ASC");
	    SQL.append(" ORDER BY ");
	    SQL.append(" PI.EXPECT_STARTYEAR||PI.EXPECT_NY");
	    long startMilis=System.currentTimeMillis();
	    String exSQL = SQL.toString().replaceAll("@@@", num.toString()).replaceAll("####", zeroNun);
	    SQLQuery query=this.dao.createSqlQuery(exSQL);
	    // 将结果转成Bean
	    query.setResultTransformer(Transformers.aliasToBean(ViewBean.class));
	    // 设置转化的结果
	    query.addScalar("cnt", Hibernate.STRING);
	    //query.addScalar("name", Hibernate.STRING);
	    query.addScalar("itemCode", Hibernate.STRING);
	    query.addScalar("investMon", Hibernate.STRING);

	    query.addScalar("reportDate", Hibernate.STRING);

//	    ViewBean.groupNo = map.get("groupNo");
	    // 执行查询语句
        // 执行查询语句
        List list = (List)reportCacheService.getEverObject(MD5.encode(SQL.toString()));
        if(list == null){
            list = query.list();
            //reportCacheService.putEverObject(MD5.encode(SQL.toString()), list);
        }
        long endMilis=System.currentTimeMillis();
        logger.debug("ViewServiceImpl   findDimensionData Calculated consumption time  Millisecond" +(endMilis-startMilis));
        if(null==list){
            list=new ArrayList<ViewBean>();
        }
        // 将CODE转成NAME
        else{
            for(ViewBean vv:(List<ViewBean>)list){
                //  存入对应的CODE
                String vName= Cache.getNameByCode(PropertiesUtil.getInfoByName("REGION_AREA"),vv.getItemCode());
                // 对应的值写回去
                vv.setItemName(vName);

            }
        }  
	    return list;
	}
}
