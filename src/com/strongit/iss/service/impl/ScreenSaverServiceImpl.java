package com.strongit.iss.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.common.MD5;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IScreenSaverService;

@Service
@Transactional
/**
 * 
 * @author zhaochao
 * @date 2018-09-06
 */
public class ScreenSaverServiceImpl extends BaseService implements IScreenSaverService {


	
	@Autowired
	private ReportCacheServiceImpl reportCacheService;
    @Override
    public  Map<String,Object>  login(String username, String pwd){
    	Map<String,Object> mp=new HashMap<String,Object>();
    	MD5 md5 = new MD5();
    	String pwd1 = md5.encode(pwd);
    	System.out.println("pwd结果**************************************"+pwd);
    	System.out.println("pwd1结果**************************************"+pwd1);
    	String sql = " select e.employee_guid,e.employee_fullname,e.employee_pwd from employee e where e.employee_loginName='"+username+"'";
    	List<Object[]> result = this.dao.findBySqlObj(sql);
    	System.out.println("result结果**************************************"+result);
    	List<Map<String,Object>> users=this.dao.findBySql(sql);
    	System.out.println("users结果**************************************"+users);
    	if(users !=null&&users.size() != 0){
    		String passwprd = (String) users.get(0).get("EMPLOYEE_PWD");
    		System.out.println("passwprd结果**************************************"+passwprd);
    		if("3a0c96248355e4fe54092acf5511ab8e".equals(passwprd)){
    			mp = users.get(0);
    		}
    	}
    	System.out.println("结果**************************************"+mp);
          return mp;
    }
	/**
	 * 获取总用户数
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:36:04
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String getAllUserCounts() {
		//获取所有用户数sql
		String sql = " SELECT COUNT(1) FROM EMPLOYEE T WHERE T.EMPLOYEE_ISDELETED = 0 ";
		//创建查询
		List list = null;
		if(list == null){
			Query query = this.dao.createSqlQuery(sql, new Object[]{});
			list = query.list();

		}
		//查询值
		String counts = (null == list ? "" : list.get(0).toString());
		//返回值
		return counts;
	}

	/**
	 * 活跃用户个数
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:36:04
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public String getActiveUserCounts(String date) {
		if (StringUtils.isEmpty(date)) {
			return "0";
		}
		if("NaN-aN-aN".equals(date)){
			date= DateFormatUtils.format(new Date(),"yyyy-MM-dd");
		}
		//活跃用户个数sql
		String sql = " select count(*) from "
                       +"(SELECT T.LOG_GUID FROM LOGIN_LOG T WHERE T.LOGIN_RESULT = 1 "
                       + " and t.Login_time >= to_date('"+date+"','yyyy-mm-dd') "
                       //+" AND TO_DATE(T.LOGIN_TIME,'YYYY-MM-DD') >= TO_DATE(DATEADD(month,-2,GETDATE()),'YYYY-MM-DD') "
                       + "  ) t";
		//创建查询
		List list = null;
		if(list == null){
			Query query = this.dao.createSqlQuery(sql, new Object[]{});
			list = query.list();
			//reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		String counts = (null == list ? "" : list.get(0).toString());
		return counts;
	}

	/**
	 * 五年规划项目储备相关信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:36:04
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getFivePlanInfo() {
		//五年规划项目储备相关信息sql
		String sql = " SELECT COUNT(T.PROJECT_ID), NVL(SUM(T.INVESTMENT_TOTAL),0) " //项目个数，总投资
				+ " FROM PROJECT_INFO_EXT_MASTER T   WHERE   EXISTS (SELECT PSS.PROJECT_ID FROM PROJECT_STORE_STATUS PSS WHERE T.PROJECT_ID = PSS.PROJECT_ID ) ";
		
		List<Object[]> list = (List<Object[]>)reportCacheService.getEverObject(MD5.encode(sql));
		if(list == null){
		//创建查询
			Query query = this.dao.createSqlQuery(sql, new Object[]{});
			list = query.list();
			reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 三年滚动计划相关信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:36:04
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getThreePlanInfo() {
		//三年滚动计划相关信息sql
		String sql = " SELECT COUNT(T.PLAN_PROJECT_ID), NVL(SUM(T.INVESTMENT_TOTAL),0) " //项目个数，总投资
				+ " FROM PLAN_PROJECT_INFO_EXT_MASTER T";
		//创建查询
		List<Object[]> list = (List<Object[]>)reportCacheService.getEverObject(MD5.encode(sql));
		if(list == null){
			Query query = this.dao.createSqlQuery(sql, new Object[]{});
			list = query.list();
			reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 项目审核备办理信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:36:04
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getProjectAuditInfo() {
		//项目审核备办理信息sql
		//String sql = "select count(PROJECT_CODE), NVL(sum(TOTAL_MONEY),0) from TZXMZH.APPLY_PROJECT_INFO";
		String sql = "select sum(num),round(sum(total_money),2) from tzxmzh.fa_xmxx";
		//创建查询
		List<Object[]> list = (List<Object[]>)reportCacheService.getEverObject(MD5.encode(sql));
		if(list == null){
			Query query = this.dao.createSqlQuery(sql, new Object[]{});
			list = query.list();
			reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 中央预算内投资信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:36:04
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getCentralBudgetInfo() {
		//中央预算内投资信息sql
		String sql = " SELECT COUNT(T.YEAR_PLAN_PROJECT_ID),NVL(SUM(T.INVESTMENT_TOTAL), 0), "
				+ " NVL(SUM(T1.CUR_ALLOCATED), 0) "
				+ " FROM YEAR_PLAN_PROJECT_INFO_EXT_MASTER T LEFT JOIN YEAR_PLAN_PROJECT_INFO_EXT_INVEST T1  "
				+ " ON T.YEAR_PLAN_PROJECT_ID = T1.YEAR_PLAN_PROJECT_ID AND T1.PROJECT_ITEM_EXT_ID='A00016' ";
		//创建查询
		List<Object[]> list = (List<Object[]>)reportCacheService.getEverObject(MD5.encode(sql));
		if(list == null){
			Query query = this.dao.createSqlQuery(sql, new Object[]{});
			list = query.list();
			reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 专项建设项目信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:36:04
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getSpecialConstructionInfo() {
		//专项建设项目信息sql
		String sql = " SELECT COUNT(T.BOND_PLAN_PROJECT_ID),NVL(SUM(T.INVESTMENT_TOTAL), 0), "
				+ " NVL(SUM(T1.PUTIN_CAPTIAL), 0) "
				+ " FROM BOND_PLAN_PROJECT_INFO_EXT_MASTER T LEFT JOIN BOND_PLAN_PROJECT_INFO_EXT_INVEST T1 "
				+ " ON T.BOND_PLAN_PROJECT_ID = T1.BOND_PLAN_PROJECT_ID AND T1.PROJECT_ITEM_EXT_ID='A00018' ";
		//创建查询
		List<Object[]> list = (List<Object[]>)reportCacheService.getEverObject(MD5.encode(sql));
		if(list == null){
			Query query = this.dao.createSqlQuery(sql, new Object[]{});
			list = query.list();
		    	reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取活跃用户地区信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:36:04
	 */
/*	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPositionInfo(String date) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
//		String sql = " select t.BELONG_AREA from T_FGW_STATIS_DEPARTMENT t, T_FGW_STATIS_DEPARTMENT_EMPLOY t1, "
//				+ " T_FGW_STATIS_EMPLOYEE t2, T_FGW_STATIS_LOGIN_LOG t3 "
//				+ " where t.DEPT_CODE = t1.DEPARTMENT_GUID and t1.EMPLOYEE_GUID = t2.EMPLOYEE_GUID "
//				+ " and t2.EMPLOYEE_GUID = t3.USER_GUID ";
		//获取活跃用户地区信息
		String sql = " SELECT T.EMPLOYEE_OFFICEZIPCODE FROM EMPLOYEE T,LOGIN_LOG T1 "
				+ " WHERE T.EMPLOYEE_OFFICEZIPCODE IS NOT NULL AND T1.LOGIN_USER_ID = T.EMPLOYEE_LOGINNAME "
				+ " and t1.Login_time >= to_date('2016-08-16','yyyy-mm-dd') ";
		
		String sql = " select p.areaname,p.geocoord_x,p.geocoord_y,t2.employee_officezipcode from t_position p "
				+ " left join(select t.employee_officezipcode from employee t,login_log t1 "
				+ " where t.employee_officezipcode is not null and t1.login_user_id = t.employee_loginname"
				+ " and date_format(t1.login_time, 'Y%-m%-d%') >= date_format('2016-08-20', 'Y%-m%-d%'))t2 "
				+ " on p.code = t2.employee_officezipcode ";
				//+ " AND TO_DATE(T1.LOGIN_TIME,'YYYY-MM-DD') >= TO_DATE(DATEADD(month,-2,GETDATE()),'YYYY-MM-DD') ";
		//创建查询
		List<String> list = null;//(List<String>)reportCacheService.getEverObject(MD5.encode(sql));
		if(list == null){
			Query query = this.dao.createSqlQuery(sql, new Object[]{});
			list = query.list();
			//reportCacheService.putEverObject(MD5.encode(sql), list);
		}
		return list;
	}*/
	
	/**
	 * 获取活跃用户地区信息
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年10月27日上午11:36:04
	 */
	@Override
	public List<Map<String, Object>> getPositionInfo(String date) {
		if (StringUtils.isEmpty(date)) {
			Date now = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");			
			date = df.format(now);
		}
		//获取活跃用户地区信息
/*		String sql = " select p.areaname,p.geocoord_x as x,p.geocoord_y as y,t2.employee_officezipcode as zipcode from kppw.t_position p "
				+ " left join(select t.employee_officezipcode from kppw.employee t,kppw.login_log t1 "
				+ " where t.employee_officezipcode is not null and t1.login_user_id = t.employee_loginname"
				+ " and date_format(t1.login_time, 'Y%-m%-d%') >= date_format('2016-09-23', 'Y%-m%-d%'))t2 "
				+ " on p.code = t2.employee_officezipcode ";*/
		String sql = "select p.areaname,p.geocoordx,p.geocoordy,t.employee_officezipcode "
				+ "from t_position p inner join employee t on p.code = t.employee_officezipcode "
				+ "inner join login_log t1 on t1.login_user_id = t.employee_loginname "
				+ "where t.employee_officezipcode is not null "
				+ "and t1.login_time >= to_date('"+date+"','yyyy-MM-dd')";
		//+ " AND TO_DATE(T1.LOGIN_TIME,'YYYY-MM-DD') >= TO_DATE(DATEADD(month,-2,GETDATE()),'YYYY-MM-DD') ";
		//创建查询
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
			//reportCacheService.putEverObject(MD5.encode(sql), list);
		return list;
	}
	

}
