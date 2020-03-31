package com.strongit.iss.common;

/**
 * 
 * @author li
 *
 */
public class MonthReportSqlConstants {
	
	/**
	 * 获取分行业情况
	 */
	public static String INDUSTRY_SQL = "select td.ITEM_VALUE,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate,"
			+" td.pcode from (select count(p.project_guid) as cnt,t.ITEM_VALUE,t.pcode,sum(p.x05) as investtotal "
			+" from issusrw.project p left join(select di.item_key,substr(di.item_fullname, 1, instr(di.item_fullname||'-', '-', 1, 1)-1)as ITEM_VALUE,"
			+" substr(di.item_full_key, 1, instr(di.item_full_key||'-', '-', 1, 1)-1)as pcode "
			+" from issusrw.dictionary_items di where group_no =2)t on p.x06=t.item_key "
			+" where substring(replace(p.x13, '', ''), 0, 6) >= %1$s and substring(replace(p.x13, '', ''), 0, 6) <= %2$s "
			+" group by t.ITEM_VALUE,t.Pcode) td left join "
			+" (select count(p.project_guid) as cnt,t.ITEM_VALUE,t.pcode,sum(p.x05) as investtotal "
			+" from issusrw.project p left join(select di.item_key,substr(di.item_fullname, 1, instr(di.item_fullname||'-', '-', 1, 1)-1)as ITEM_VALUE,"
			+" substr(di.item_full_key, 1, instr(di.item_full_key||'-', '-', 1, 1)-1)as pcode "
			+" from issusrw.dictionary_items di where group_no =2)t on p.x06=t.item_key "
			+" where substring(replace(p.x13, '', ''), 0, 6) >= %3$s and substring(replace(p.x13, '', ''), 0, 6) <= %4$s "
			+" group by t.ITEM_VALUE,t.Pcode)lt "
			+" on td.ITEM_VALUE=lt.item_value order by td.pcode";
	public static String INDUSTRYP_SQL = "select td.ITEM_VALUE,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate,"
			+" td.code,td.pcode from (select count(p.project_guid) as cnt,t.ITEM_VALUE,t.pcode,t.code,sum(p.x05) as investtotal "
			+" from issusrw.project p left join(select di.item_key,"
			+" substr(di.item_fullname,instr(di.item_fullname, '-', 1, 1)+1, instr(di.item_fullname, '-', 1, 2)-instr(di.item_fullname, '-', 1, 1) -1)as ITEM_VALUE,"
			+" substr(di.item_full_key, 1, instr(di.item_full_key||'-', '-', 1, 1)-1)as pcode, "
			+" substr(di.item_full_key,instr(di.item_full_key, '-', 1, 1)+1, instr(di.item_full_key, '-', 1, 2)-instr(di.item_full_key, '-', 1, 1) -1)as code"
			+" from issusrw.dictionary_items di where group_no =2)t on p.x06=t.item_key where length(t.code)=2 and"
			+" substring(replace(p.x13, '', ''), 0, 6) >= %1$s and substring(replace(p.x13, '', ''), 0, 6) <= %2$s "
			+" group by t.ITEM_VALUE,t.Pcode,t.code) td left join "
			+" (select count(p.project_guid) as cnt,t.ITEM_VALUE,t.pcode,t.code,sum(p.x05) as investtotal "
			+" from issusrw.project p left join(select di.item_key,"
			+" substr(di.item_fullname,instr(di.item_fullname, '-', 1, 1)+1, instr(di.item_fullname, '-', 1, 2)-instr(di.item_fullname, '-', 1, 1) -1)as ITEM_VALUE,"
			+" substr(di.item_full_key, 1, instr(di.item_full_key||'-', '-', 1, 1)-1)as pcode, "
			+" substr(di.item_full_key,instr(di.item_full_key, '-', 1, 1)+1, instr(di.item_full_key, '-', 1, 2)-instr(di.item_full_key, '-', 1, 1) -1)as code"
			+" from issusrw.dictionary_items di where group_no =2)t on p.x06=t.item_key where length(t.code)=2 and"
			+" substring(replace(p.x13, '', ''), 0, 6) >= %3$s and substring(replace(p.x13, '', ''), 0, 6) <= %4$s "
			+" group by t.ITEM_VALUE,t.Pcode,t.code)lt "
			+" on td.ITEM_VALUE=lt.item_value order by td.code";
	
	/**
	 * 获取分地区情况
	 */
	public static String AREA_SQL = "select td.ITEM_VALUE,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate,"
			+" td.ITEM_KEY,td.EXTEND_FLAG from (SELECT DI.ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,DI.ITEM_KEY,DI.EXTEND_FLAG, "
			+" SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P"
			+" LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON SUBSTR(P.X04, 1, 2)||'0000' =DI.ITEM_KEY "
			+" AND DI.GROUP_NO=1 WHERE DI.EXTEND_FLAG IS NOT NULL "
			+" AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %1$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %2$s "
			+" GROUP BY DI.ITEM_VALUE,DI.EXTEND_FLAG,DI.ITEM_KEY ) td left join "
			+" (SELECT DI.ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,DI.ITEM_KEY,DI.EXTEND_FLAG,"
			+" SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P"
			+" LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON SUBSTR(P.X04, 1, 2)||'0000' =DI.ITEM_KEY "
			+" AND DI.GROUP_NO=1 WHERE DI.EXTEND_FLAG IS NOT NULL "
			+" AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %3$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %4$s "
			+" GROUP BY DI.ITEM_VALUE,DI.EXTEND_FLAG,DI.ITEM_KEY ) lt "
			+" on td.ITEM_VALUE=lt.item_value order by td.EXTEND_FLAG,td.ITEM_KEY";
	
	public static String ALLAREA_TOTAL = " select td.ITEM_VALUE,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt, 2)as cntratio,"
			+ " round(td.investtotal/10000, 2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal, 2)as inveatratio,"
			+ " round(td.cnt*100.0/(sum(td.cnt) over()), 2)as cntrate,round(td.investtotal*100.0/(sum(td.investtotal) over()), 2)as investrate,"
			+ " td.EXTEND_FLAG from(SELECT count(p.project_guid)as cnt,sum(p.X05)as investtotal,DI.EXTEND_FLAG,case "
			+ " when DI.EXTEND_FLAG=1 then '东部地区' when DI.EXTEND_FLAG=2 then '中部地区' when DI.EXTEND_FLAG=3 then '西部地区' end as ITEM_VALUE "
			+ " FROM ISSUSRW.PROJECT p left join  ISSUSRW.dictionary_items di ON SUBSTR(P.X04, 1, 2)||'0000' =DI.ITEM_KEY AND  DI.GROUP_NO=1 "
			+ " where SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) > '201601' AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) < '201606' "
			+ " group by DI.EXTEND_FLAG)td left join(SELECT count(p.project_guid)as cnt,sum(p.X05)as investtotal,DI.EXTEND_FLAG,"
			+ " case when DI.EXTEND_FLAG=1 then '东部地区' when DI.EXTEND_FLAG=2 then '中部地区' when DI.EXTEND_FLAG=3 then '西部地区' end as ITEM_VALUE "
			+ " FROM ISSUSRW.PROJECT p left join  ISSUSRW.dictionary_items di ON SUBSTR(P.X04, 1, 2)||'0000' =DI.ITEM_KEY AND  DI.GROUP_NO=1 "
			+ " where SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) > '201501' AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) < '201506' "
			+ " group by DI.EXTEND_FLAG)lt on td.ITEM_VALUE=lt.ITEM_VALUE order by td.EXTEND_FLAG";
	
	/*东三省情况*/
	public static String EASTAREA_SQL = "select td.ITEM_VALUE,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal*100.0/(sum(td.investtotal) over()),2) as investrate,"
			+" td.ITEM_KEY from (SELECT DI.ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,DI.ITEM_KEY, "
			+" SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P"
			+" LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON SUBSTR(P.X04, 1, 2)||'0000'   =DI.ITEM_KEY "
			+" AND DI.GROUP_NO=1 WHERE di.item_key in('210000','220000','230000') "
			+" AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %1$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %2$s "
			+" GROUP BY DI.ITEM_VALUE,DI.ITEM_KEY ) td left join "
			+" (SELECT DI.ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,DI.ITEM_KEY, "
			+" SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P"
			+" LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON SUBSTR(P.X04, 1, 2)||'0000'   =DI.ITEM_KEY "
			+" AND DI.GROUP_NO=1 WHERE di.item_key in('210000','220000','230000') "
			+" AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %3$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %4$s "
			+" GROUP BY DI.ITEM_VALUE,DI.ITEM_KEY ) lt "
			+" on td.ITEM_VALUE=lt.item_value order by td.ITEM_KEY";
	
	/**
	 * 获取分项目类型情况
	 */
	public static String PROJECTTYPE_SQL = "select td.ITEM_VALUE,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate,"
			+" td.ITEM_KEY,td.ITEM_PARENT from (SELECT DI.ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,DI.ITEM_KEY,DI.ITEM_PARENT, "
			+" SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P"
			+" LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON P.X03 =DI.ITEM_KEY "
			+" AND DI.GROUP_NO=3 WHERE DI.ITEM_VALUE IS NOT NULL "
			+" AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %1$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %2$s "
			+" GROUP BY DI.ITEM_PARENT,DI.ITEM_VALUE,DI.ITEM_KEY ) td left join "
			+" (SELECT DI.ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,DI.ITEM_KEY,DI.ITEM_PARENT,"
			+" SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P"
			+" LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON P.X03   =DI.ITEM_KEY "
			+" AND DI.GROUP_NO=3 WHERE DI.ITEM_VALUE IS NOT NULL "
			+" AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %3$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %4$s "
			+" GROUP BY DI.ITEM_PARENT,DI.ITEM_VALUE,DI.ITEM_KEY ) lt "
			+" on td.ITEM_VALUE=lt.item_value order by td.ITEM_PARENT,td.ITEM_KEY";
	public static String PLAN_SQL = "select td.ITEM_VALUE,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate"
			+" from(SELECT '审批' as ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL "
			+" FROM ISSUSRW.PROJECT P LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON P.X03=DI.ITEM_KEY "
			+" AND DI.GROUP_NO=3 where substring(replace(p.x13, '', ''), 0, 6) >= %1$s and substring(replace(p.x13, '', ''), 0, 6) <= %2$s "
			+" and di.item_key in('A00004','A00005','A00006','A00007'))td left join "
			+" (SELECT '审批' as ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL "
			+" FROM ISSUSRW.PROJECT P LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON P.X03=DI.ITEM_KEY "
			+" AND DI.GROUP_NO=3 where substring(replace(p.x13, '', ''), 0, 6) >= %3$s and substring(replace(p.x13, '', ''), 0, 6) <= %4$s "
			+" and di.item_key in('A00004','A00005','A00006','A00007'))lt on td.item_value=lt.item_value";
	
	
	/*分投资类型*/
	public static String INVESTTYPE_SQL = " select td.TZLX,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate "
			+ " from (SELECT count(p.project_guid) as cnt,sum(p.X05) as investtotal,case "
			+ " when p.x03 in('A00004','A00005','A00006','A00007') then '政府投资' "
			+ " when p.x03 in('A00002','A00003') then '企业投资' end as TZLX FROM ISSUSRW.PROJECT p "
			+ " where SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %1$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %2$s group by TZLX) td "
			+ " left join (SELECT count(p.project_guid) as cnt,sum(p.X05) as investtotal,case "
			+ " when p.x03 in('A00004','A00005','A00006','A00007') then '政府投资' "
			+ " when p.x03 in('A00002','A00003') then '企业投资' end as TZLX FROM ISSUSRW.PROJECT p "
			+ " where SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %3$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %4$s group by TZLX) lt"
			+ " on td.TZLX=lt.TZLX order by td.TZLX";
	public static String INVESTTYPETOTAL_SQL = " select '合计' as TZLX,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate "
			+ " from (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %1$s and substring(replace(p.x13, '', ''), 0, 6) <= %2$s) td,"
			+ " (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %3$s and substring(replace(p.x13, '', ''), 0, 6) <= %4$s) lt ";	

	
	/**
	 * 获取分产业情况
	 */
	public static String PROPERTY_SQL = " select td.CY,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate "
			+ " from (SELECT case when di.extend_flag=1 then '第一产业' when di.extend_flag=2 then '第二产业' "
			+ " when di.extend_flag=3 then '第三产业' end as CY,count(p.project_guid) as cnt,sum(p.X05) as investtotal "
			+ " FROM ISSUSRW.PROJECT p left join ISSUSRW.dictionary_items di  on  p.x06=di.item_key and di.group_no ='2' "
			+ " where SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %1$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %2$s group by CY) td "
			+ " left join (SELECT case when di.extend_flag=1 then '第一产业' when di.extend_flag=2 then '第二产业' "
			+ " when di.extend_flag=3 then '第三产业' end as CY,count(p.project_guid) as cnt,sum(p.X05) as investtotal "
			+ " FROM ISSUSRW.PROJECT p left join ISSUSRW.dictionary_items di  on  p.x06=di.item_key and di.group_no ='2' "
			+ " where SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %3$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %4$s group by CY) lt"
			+ " on td.CY=lt.CY where lt.investtotal is not null and lt.cnt is not null order by td.CY";
	public static String PROPERTYTOTAL_SQL = " select '合计' as CY,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate "
			+ " from (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %1$s and substring(replace(p.x13, '', ''), 0, 6) <= %2$s) td,"
			+ " (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %3$s and substring(replace(p.x13, '', ''), 0, 6) <= %4$s) lt ";	
	
	/**
	 * 获取分投资规模情况
	 */
	public static String INVESTSCALE_SQL = " select td.TZGM,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate "
			+ " from (SELECT count(p.project_guid) as cnt,sum(p.X05) as investtotal,case when 0 <= p.X05 and p.X05 < 500 then '500万元以下' "
			+ " when 500 <= p.X05  and p.X05 < 1000 then '500~1000万元' when 1000 <= p.X05  and p.X05 < 5000 then '1000~5000万元' "
			+ " when 5000 <= p.X05  and p.X05 < 10000 then '5000~10000万元' when 10000 <= p.X05  and p.X05 < 100000 then '1~10亿元' "
			+ " when 100000 <= p.X05 then '10亿元以上' end as TZGM FROM ISSUSRW.PROJECT p "
			+ " where SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %1$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %2$s group by TZGM) td "
			+ " left join (SELECT count(p.project_guid) as cnt,sum(p.X05) as investtotal,case when 0 <= p.X05 and p.X05 < 500 then '500万元以下' "
			+ " when 500 <= p.X05  and p.X05 < 1000 then '500~1000万元' when 1000 <= p.X05  and p.X05 < 5000 then '1000~5000万元' "
			+ " when 5000 <= p.X05  and p.X05 < 10000 then '5000~10000万元' when 10000 <= p.X05  and p.X05 < 100000 then '1~10亿元' "
			+ " when 100000 <= p.X05 then '10亿元以上' end as TZGM FROM ISSUSRW.PROJECT p "
			+ " where SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %3$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %4$s group by TZGM) lt"
			+ " on td.TZGM=lt.TZGM order by td.TZGM ";
	public static String INVESTSCALETOTAL_SQL = " select '合计' as TZGM,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate "
			+ " from (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %1$s and substring(replace(p.x13, '', ''), 0, 6) <= %2$s) td,"
			+ " (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %3$s and substring(replace(p.x13, '', ''), 0, 6) <= %4$s) lt ";
	
	/**
	 * 获取预计开工时间情况
	 */
	public static String STARTTIME_SQL = " select td.X11,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate "
			+ " from (SELECT X11,count(p.project_guid) as cnt,sum(p.X05) as investtotal "
			+ " FROM ISSUSRW.PROJECT P WHERE SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %1$s "
			+ " AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %2$s and P.X11 >='2016' group by X11) td "
			+ " left join (SELECT X11,count(p.project_guid) as cnt,sum(p.X05) as investtotal "
			+ " FROM ISSUSRW.PROJECT P WHERE SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %3$s "
			+ " AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %4$s and P.X11 >='2016' "
			+ " group by X11 )lt on td.X11=lt.X11 where lt.investtotal is not null and lt.cnt is not null order by td.X11";
	public static String STARTTIMETOTAL_SQL = " select '合计' as X11,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate "
			+ " from (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %1$s and substring(replace(p.x13, '', ''), 0, 6) <= %2$s) td,"
			+ " (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %3$s and substring(replace(p.x13, '', ''), 0, 6) <= %4$s) lt ";
	/**
	 * 获取总计信息
	 */
	public static String TOTAL_SQL = " select '合计' as ITEM_VALUE,td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+" round(td.cnt*100.0/(sum(td.cnt) over()),2) as cntrate,round(td.investtotal  *100.0/(sum(td.investtotal) over()),2) as investrate "
			+ " from (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %1$s and substring(replace(p.x13, '', ''), 0, 6) <= %2$s) td,"
			+ " (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %3$s and substring(replace(p.x13, '', ''), 0, 6) <= %4$s) lt ";
	
	/**
	 * 获取东三省总计信息
	 */
	public static String EASTTOAL_SQL = " select '东北地区' as ITEM_VALUE, td.cnt,round(((td.cnt-lt.cnt)*100.0)/lt.cnt,2) as cntratio,"
			+" round(td.investtotal/10000,2)as investtotal,round(((td.investtotal-lt.investtotal)*100.0)/lt.investtotal,2) as inveatratio,"
			+ " round(td.cnt*100.0/(tt.cnt),2) as cntrate,round(td.investtotal*100.0/(tt.investtotal),2) as investrate "
			+ " from (select sum(t1.cnt) as cnt,sum(t1.investtotal) as investtotal "
			+ " from(SELECT DI.ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,DI.ITEM_KEY, "
			+ " SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P"
			+ " LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON SUBSTR(P.X04, 1, 2)||'0000'   =DI.ITEM_KEY "
			+ " AND DI.GROUP_NO=1 WHERE di.item_key in('210000','220000','230000') "
			+ " AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %1$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %2$s "
			+ " GROUP BY DI.ITEM_VALUE,DI.ITEM_KEY )t1)td,"
			+ " (select sum(t2.cnt) as cnt,sum(t2.investtotal) as investtotal "
			+ " from(SELECT DI.ITEM_VALUE,COUNT(P.PROJECT_GUID) AS CNT,DI.ITEM_KEY, "
			+ " SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P"
			+ " LEFT JOIN ISSUSRW.DICTIONARY_ITEMS DI ON SUBSTR(P.X04, 1, 2)||'0000'   =DI.ITEM_KEY "
			+ " AND DI.GROUP_NO=1 WHERE di.item_key in('210000','220000','230000') "
			+ " AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) >= %3$s AND SUBSTRING(REPLACE(P.X13, '', ''), 0, 6) <= %4$s "
			+ " GROUP BY DI.ITEM_VALUE,DI.ITEM_KEY )t2)lt,"
			+ " (SELECT COUNT(P.PROJECT_GUID) AS CNT,SUM(P.X05) AS INVESTTOTAL FROM ISSUSRW.PROJECT P "
			+ " where substring(replace(p.x13, '', ''), 0, 6) >= %1$s and substring(replace(p.x13, '', ''), 0, 6) <= %2$s) tt";
			
			

}
