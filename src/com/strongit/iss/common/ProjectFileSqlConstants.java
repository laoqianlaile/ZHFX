package com.strongit.iss.common;

/**
 * @author XiaXiang
 *
 */
public class ProjectFileSqlConstants {
	/**项目履历**/
	public static String XM_RECORD_SQL = "select * from "
			+ " ( select '储备项目编制' AS OPERATE, CREATE_TIME as OPTIME, "
			+ " CREATE_DEPARTMENT_FULLNAME as OPDEPART, create_user_name as OPUSER "
			+ " from PROJECT  where ID = %1$s "
			+ " UNION all "
			+ " select DECODE(REPORT_DEPARTMENT_FULLNAME, DEPARTMENT_FULLNAME, '储备项目提交审核', '储备项目报送') as OPERATE, "
			+ " REPORT_TIME as OPTIME, REPORT_DEPARTMENT_FGW_FULLNAME as OPDEPART, "
			+ " create_user_name as OPUSER from PROJECT_CHECK_STATUS "
			+ " where PROJECT_ID = %1$s AND STATUS <> 'BACKED' "
			+ " union all "
			+ " select '储备项目入库' as OPERATE, STORE_TIME as OPTIME, "
			+ " STORE_DEPARTMENT_FGW_FULLNAME as OPDEPART, "
			+ " create_user_name as OPUSER from PROJECT_STORE_STATUS "
			+ " where PROJECT_ID = %1$s "
			+ " union "
			+ " select '计划编制' AS OPERATE, CREATE_TIME as OPTIME, "
			+ " CREATE_DEPARTMENT_FULLNAME as OPDEPART, create_user_name as OPUSER "
			+ " from PLAN_PROJECT where ID = %2$s "
			+ " UNION "
			+ " select decode(STATUS, 'DELETED', '计划上级删除', 'PASS', '计划审核通过', 'SUBMITED', '计划提交审核', 'SELF', '纳入本级计划并报送', 'BACKED', '被退回') as OPERATE, "
			+ " UPDATE_TIME as OPTIME, "
			+ " DECODE(DEPARTMENT_FULLNAME, DEPARTMENT_FGW_FULLNAME, '', DEPARTMENT_FULLNAME) as OPDEPART, "
			+ " create_user_name as OPUSER "
			+ " from PLAN_PROJECT_CHECK_STATUS where ROLL_PLAN_PROJECT_ID = %2$s "
			+ " AND STATUS IN ('DELETED', 'PASS', 'SUBMITED', 'SELF', 'BACKED') "
			+ " union "
			+ " select '计划提交审核' as OPDEPART, p.UPDATE_TIME as OPTIME, "
			+ " DECODE(p.DEPARTMENT_FULLNAME, p.DEPARTMENT_FGW_FULLNAME, '', p.DEPARTMENT_FULLNAME) as OPDEPART, "
			+ " create_user_name as OPUSER "
			+ " from PLAN_PROJECT_CHECK_STATUS p where "
			+ " p.ROLL_PLAN_PROJECT_ID = %2$s AND p.STATUS = 'SUBMITED' "
			+ " union all "
			+ " select '年度计划 编辑' as OPDEPART,CREATE_TIME as OPTIME,"
			+ " CREATE_DEPARTMENT_FULLNAME as OPDEPART,CREATE_USER_NAME as OPUSER  "
			+ " from YEAR_PLAN_PROJECT where SOURCE_ID=%2$s and project_id=%1$s "
			+ " union all "
			+ " select '专项债计划 编辑' as OPDEPART,CREATE_TIME as OPTIME,"
			+ " CREATE_DEPARTMENT_FULLNAME as OPDEPART,CREATE_USER_NAME as OPUSER  "
			+ " from BOND_PLAN_PROJECT where SOURCE_ID=%2$s and project_id=%1$s "
			+ " union all "
			+ " select '项目调度' as OPDEPART,pl.CREATE_TIME as OPTIME,"
			+ " pdl.CREATE_DEPARTMENT_FULLNAME as OPDEPART,"
			+ " pl.CREATE_USER_NAME as OPUSER from "
			+ " PROJECT_DISPATCH_TASK_PROJECT_LOG pl"
			+ " left join PROJECT_DISPATCH_TASK_LOG pdl on pl.TASK_ID=pdl.id"
			+ " where PROJECT_ID=%2$s and STORE_ID=%1$s) table1"
			+ " order by OPTIME asc";
	
	/**获取计划IDsql**/
	public static String GET_ROLLOLAN_ID = " select t.ID from PLAN_PROJECT t where t.PROJECT_ID = %1$s ";
	/**根据计划项目id获取储备项目IDsql**/
	public static String GET_ID_BY_ROLLOLAN_ID = " select t.PROJECT_ID from PLAN_PROJECT t where t.ID = %1$s ";
	/**根据年度计划项目id获取计划项目ID和储备项目IDsql**/
	public static String GET_ID_BY_YEARPLAN_ID = " select t.PROJECT_ID,T.SOURCE_ID from YEAR_PLAN_PROJECT t where t.ID = %1$s ";
	/**根据专项债计划项目id获取计划项目ID储备项目IDsql**/
	public static String GET_ID_BY_BONDPLAN_ID = " select t.PROJECT_ID,T.SOURCE_ID from BOND_PLAN_PROJECT t where t.ID = %1$s ";
	
	/**获取PROJECT_CODE**/
	public static String GET_PROJECT_CODE = " select PM.* from PLAN_PROJECT t "
			+ " left join PLAN_PROJECT_INFO_EXT_MASTER PM on PM.PLAN_PROJECT_ID = t.ID "
			+ " where t.PROJECT_ID = %1$s ";
	
	/**审核备项目履历**/
	public static String SHB_RECORD_SQL = "select b.project_name, a.project_code, a.item_name || c.code_name as bzcz, "
			+ " a.dealed_date, a.DEPT_NAME from tzxmzh.r_comm_projects b left join tzxmzh.approve_item_info a "
			+ " on a.project_code = b.project_code left join tzxmzh.app_codelist c "
			+ " on a.current_state = c.code_value where "
			+ " c.code_kind = 'SXZT' and a.project_code = %1$s ";
	
	/**储备项目基本信息**/
	public static String BASIC_SQL = " select *,to_char(BELONG_YEAR) as BELONG_YEAR from  PROJECT t "
			+ " left join PROJECT_INFO_EXT_MASTER IEM "
			+ " on t.id = IEM.project_id "
			+ " where t.id = %1$s ";
	/**三年滚动项目基本信息**/
	public static String PLAN_BASIC_SQL = " select *,to_char(BELONG_YEAR) as BELONG_YEAR from  PLAN_PROJECT t "
			+ " left join PLAN_PROJECT_INFO_EXT_MASTER IEM "
			+ " on t.id = IEM.PLAN_PROJECT_ID "
			+ " where t.id = %1$s ";
	/**年度计划项目基本信息**/
	public static String YEARPLAN_BASIC_SQL = " select *,to_char(BELONG_YEAR) as BELONG_YEAR from  YEAR_PLAN_PROJECT t "
			+ " left join YEAR_PLAN_PROJECT_INFO_EXT_MASTER IEM "
			+ " on t.id = IEM.YEAR_PLAN_PROJECT_ID "
			+ " where t.id = %1$s ";
	/**专项债项目基本信息**/
	public static String BONDPLAN_BASIC_SQL = " select *,to_char(BELONG_YEAR) as BELONG_YEAR from  BOND_PLAN_PROJECT t "
			+ " left join BOND_PLAN_PROJECT_INFO_EXT_MASTER IEM "
			+ " on t.id = IEM.BOND_PLAN_PROJECT_ID "
			+ " where t.id = %1$s ";
	/**储备量化建设规模**/
	public static String QUA_SQL = " select PIE.NAME AS PIE_NAME,_NUMBER from  PROJECT_INFO_EXT_QUA t "
			+ " left join PROJECT_ITEM_EXT pie on t.TYPE = pie.code "
			+ " left join PROJECT_ITEM_TYPE PIT ON PIE.PROJECT_ITEM_TYPE_ID = PIT.ID"
			+ " WHERE t.PROJECT_ID = %1$s and PIT.CODE = %2$s";
	/**三年滚动项目量化建设规模**/
	public static String PLAN_QUA_SQL = " select PIE.NAME AS PIE_NAME,_NUMBER from  PLAN_PROJECT_INFO_EXT_QUA t "
			+ " left join PROJECT_ITEM_EXT pie on t.TYPE = pie.code "
			+ " left join PROJECT_ITEM_TYPE PIT ON PIE.PROJECT_ITEM_TYPE_ID = PIT.ID"
			+ " WHERE t.PLAN_PROJECT_ID = %1$s and PIT.CODE = %2$s";
	/**年度计划量化建设规模**/
	public static String YEARPLAN_QUA_SQL = " select PIE.NAME AS PIE_NAME,_NUMBER from  YEAR_PLAN_PROJECT_INFO_EXT_QUA t "
			+ " left join PROJECT_ITEM_EXT pie on t.TYPE = pie.code "
			+ " left join PROJECT_ITEM_TYPE PIT ON PIE.PROJECT_ITEM_TYPE_ID = PIT.ID"
			+ " WHERE t.YEAR_PLAN_PROJECT_ID = %1$s and PIT.CODE = %2$s";
	/**专项债量化建设规模**/
	public static String BONDPLAN_QUA_SQL = " select PIE.NAME AS PIE_NAME,_NUMBER from  BOND_PLAN_PROJECT_INFO_EXT_QUA t "
			+ " left join PROJECT_ITEM_EXT pie on t.TYPE = pie.code "
			+ " left join PROJECT_ITEM_TYPE PIT ON PIE.PROJECT_ITEM_TYPE_ID = PIT.ID"
			+ " WHERE t.BOND_PLAN_PROJECT_ID = %1$s and PIT.CODE = %2$s";
	
//	public static String SUB_BASIC_SQL = " select IEM.BELONG_YEAR, IEM.ISBOND from  PROJECT t "
//			+ " left join PROJECT_INFO_EXT_MASTER IEM "
//			+ " on t.id = IEM.project_id "
//			+ " where t.id = %1$s ";
	
	/**项目审核备办理信息**/
//	public static String SHBBL_SQL = " select b.project_name, a.project_code, a.item_name||c.code_name  as bzcz, "
//				+ " a.dealed_date, a.DEPT_NAME from tzxmzh.r_comm_projects b "
//				+ " left join tzxmzh.approve_item_info a on "
//				+ " a.project_code = b.project_code left join tzxmzh.app_codelist c "
//				+ " on a.current_state = c.code_value where c.code_kind = 'SXZT' "
//				+ " and a.project_code = %1$s ";
	
	public static String SHBBL_SQL =" SELECT A.PROJECT_CODE, ITEM_NAME, FILETITLE_NAME,  "
			+ " APPROVAL_NUMBER, CODE_NAME, DEALED_DATE, "
			+ " LIMIT_DAYS, BJSJ, B.NAME1, COMPLETED_DATE, VALIDITY_DATE "
			+ " FROM TZXMZH.APPROVE_ITEM_INFO A "
			+ " LEFT JOIN TZXMZH.DIM_DIVISION B ON A.DIVISION_CODE = B.CODE "
			+ " LEFT JOIN TZXMZH.APP_CODELIST C ON A.CURRENT_STATE = C.CODE_VALUE "
			+ " WHERE C.CODE_KIND = 'SXZT' AND A.PROJECT_CODE  = %1$s";
	
	/**储备投资情况**/
	public static String INVEST_SQL = " select * from PROJECT_ITEM_EXT pie "
			+ " inner join PROJECT_INFO_EXT_INVEST t "
			+ " on pie.id=t.project_item_ext_Id and t.PROJECT_ID = %1$s "
			+ " where project_item_type_id='A00011' order by pie.`SORT` asc ";
	/**三年滚动投资情况**/
	public static String PLAN_INVEST_SQL = " select * from PROJECT_ITEM_EXT pie "
			+ " inner join PLAN_PROJECT_INFO_EXT_INVEST t "
			+ " on pie.id=t.project_item_ext_Id and t.PLAN_PROJECT_ID = %1$s "
			+ " where project_item_type_id='A00011' order by pie.`SORT` asc ";
	/**年度计划投资情况**/
	public static String YEARPLAN_INVEST_SQL = " select * from PROJECT_ITEM_EXT pie "
			+ " inner join YEAR_PLAN_PROJECT_INFO_EXT_INVEST t "
			+ " on pie.id=t.project_item_ext_Id and t.YEAR_PLAN_PROJECT_ID = %1$s "
			+ " where project_item_type_id='A00011' order by pie.`SORT` asc ";
	/**专项债投资情况**/
	public static String BONDPLAN_INVEST_SQL = " select * from PROJECT_ITEM_EXT pie "
			+ " inner join BOND_PLAN_PROJECT_INFO_EXT_INVEST t "
			+ " on pie.id=t.project_item_ext_Id and t.BOND_PLAN_PROJECT_ID = %1$s "
			+ " where project_item_type_id='A00011' order by pie.`SORT` asc ";
	/**计划下达情况**/
	public static String INVEST_DOWL_SQL = " SELECT EXPORT_FILE_NO,FILE_NAME,ISSUED_MONEY, "
			+ " SUBSTR(DPT.DEPARTMENT_FULL_CODENAME, 1, INSTR(DPT.DEPARTMENT_FULL_CODENAME||'#', '#', 1, 1)-1) AS SIJU, "
			+ " ISSUSED_TIME,IS_BUNDLED FROM YEAR_PLAN_PROJECT YPP  "
			+ " LEFT JOIN PROJECT_DISPATCH_FUNDS_ISSUED PFI ON YPP.SOURCE_ID = PFI.PLAN_PROJECT_ID "
			+ " LEFT JOIN  DEPARTMENT DPT ON DPT.DEPARTMENT_GUID= YPP.INTRANET_ACCEPT_DEPT_GUID"
			+ " WHERE EXPORT_FILE_NO IS NOT NULL  AND YPP.PROJECT_ID = %1$s ";
//专项债
//			+ " union all select EXPORT_FILE_NO,FILE_NAME,CUR_ALLOCATED,"
//			+ " substr(dpt.department_full_codename, 1, instr(dpt.department_full_codename||'#', '#', 1, 1)-1) as siju, "
//			+ " ISSUSED_TIME,IS_BUNDLED from BOND_PLAN_PROJECT bpp"
//			+ " left join BOND_PLAN_PROJECT_INFO_EXT_INVEST bpi on bpp.id = bpi.BOND_PLAN_PROJECT_ID "
//			+ " left join PROJECT_DISPATCH_FUNDS_ISSUED pfi on bpp.SOURCE_ID = pfi.PLAN_PROJECT_ID "
//			+ " left join  DEPARTMENT dpt on dpt.department_guid= bpp.INTRANET_ACCEPT_DEPT_GUID "
//			+ " where bpi.PROJECT_ITEM_EXT_ID='A00016' and EXPORT_FILE_NO is not null and bpp.SOURCE_ID = %1$s ";
	/**资金到位完成情况**/
	public static String INVEST_PUT_SQL = " SELECT  REPORT_NUMBER,SUM(SUM_MONEY) AS SUMMONEY, "
			+ " SUM(DECODE(TYPE, '1', CUR_MONEY, 0)) AS PUTMONEY,"
			+ " SUM(DECODE(TYPE, '2', CUR_MONEY, 0)) AS COMMONEY,"
			+ " SUM(DECODE(TYPE, '3', CUR_MONEY, 0)) AS PAYMONEY FROM "
			+ " (SELECT REPORT_NUMBER,TYPE,SUM_MONEY,CUR_MONEY FROM PROJECT_DISPATCH_IMPL PDI  "
			+ "	LEFT JOIN PROJECT_DISPATCH_IMPL_INVEST PDII "
			+ " ON PDI.ID= PDII.PROJECT_IMPL_ID "
			+ " WHERE PDI.PROJECT_ID = %1$s "
	        + " UNION ALL "
	        + " SELECT REPORT_NUMBER,TYPE,SUM_MONEY,CUR_MONEY FROM PROJECT_DISPATCH_IMPL_HISTORY PDIH  "
			+ "	LEFT JOIN PROJECT_DISPATCH_IMPL_INVEST_HISTORY PDIIH "
			+ " ON PDIH.ID= PDIIH.PROJECT_IMPL_ID "
			+ " WHERE PDIH.PROJECT_ID = %1$s ) table1 "
			+ " GROUP BY REPORT_NUMBER ORDER BY REPORT_NUMBER";
	/**储备项目前期工作**/
	public static String PREWORK_SQL = " select * from  PROJECT_INFO_EXT_PREWORK t WHERE t.PROJECT_ID = %1$s ";
	/**三年滚动项目前期工作**/
	public static String PLAN_PREWORK_SQL = " select * from  PLAN_PROJECT_INFO_EXT_PREWORK t WHERE t.PLAN_PROJECT_ID = %1$s ";
	/**年度计划项目前期工作**/
	public static String YEARPLAN_PREWORK_SQL = " select * from  YEAR_PLAN_PROJECT_INFO_EXT_PREWORK t WHERE t.YEAR_PLAN_PROJECT_ID = %1$s ";
	/**专项债计划项目前期工作**/
	public static String BONDPLAN_PREWORK_SQL = " select * from  BOND_PLAN_PROJECT_INFO_EXT_PREWORK t WHERE t.BOND_PLAN_PROJECT_ID = %1$s ";
	
	/**储备资金构成**/
	public static String CONSTITULE_INVEST_SQL = " SELECT PIE.NAME, PIEI.TOTAL_INVESTMENT FROM PROJECT_INFO_EXT_INVEST PIEI "
			+ " LEFT JOIN PROJECT_ITEM_EXT PIE ON PIEI.PROJECT_ITEM_EXT_ID= PIE.ID "
			+ " WHERE PIE.PROJECT_ITEM_TYPE_ID='A00011' AND  PIE.ID!='A00180' "
			+ " AND  PIEI.TOTAL_INVESTMENT IS NOT NULL"
			+ " AND (PIE.PARENT_ID IS NULL OR PIE.PARENT_ID  ='')"
			+ " AND PIEI.PROJECT_ID= %1$s ";
	/**三年滚动资金构成**/
	public static String PLAN_CONSTITULE_INVEST_SQL = " SELECT PIE.NAME, PIEI.TOTAL_INVESTMENT FROM PLAN_PROJECT_INFO_EXT_INVEST PIEI "
			+ " LEFT JOIN PROJECT_ITEM_EXT PIE ON PIEI.PROJECT_ITEM_EXT_ID= PIE.ID "
			+ " WHERE PIE.PROJECT_ITEM_TYPE_ID='A00011' AND  PIE.ID!='A00180' "
			+ " AND  PIEI.TOTAL_INVESTMENT IS NOT NULL"
			+ " AND (PIE.PARENT_ID IS NULL OR PIE.PARENT_ID  ='')"
			+ " AND PIEI.PLAN_PROJECT_ID= %1$s ";
	/**年度计划资金构成**/
	public static String YEARPLAN_CONSTITULE_INVEST_SQL = " SELECT PIE.NAME, PIEI.TOTAL_INVESTMENT FROM YEAR_PLAN_PROJECT_INFO_EXT_INVEST PIEI "
			+ " LEFT JOIN PROJECT_ITEM_EXT PIE ON PIEI.PROJECT_ITEM_EXT_ID= PIE.ID "
			+ " WHERE PIE.PROJECT_ITEM_TYPE_ID='A00011' AND  PIE.ID!='A00180' "
			+ " AND  PIEI.TOTAL_INVESTMENT IS NOT NULL"
			+ " AND (PIE.PARENT_ID IS NULL OR PIE.PARENT_ID  ='')"
			+ " AND PIEI.YEAR_PLAN_PROJECT_ID= %1$s ";
	/**专项债计划资金构成**/
	public static String BONDPLAN_CONSTITULE_INVEST_SQL = " SELECT PIE.NAME, PIEI.TOTAL_INVESTMENT FROM BOND_PLAN_PROJECT_INFO_EXT_INVEST PIEI "
			+ " LEFT JOIN PROJECT_ITEM_EXT PIE ON PIEI.PROJECT_ITEM_EXT_ID= PIE.ID "
			+ " WHERE PIE.PROJECT_ITEM_TYPE_ID='A00011' AND  PIE.ID!='A00180' "
			+ " AND  PIEI.TOTAL_INVESTMENT IS NOT NULL"
			+ " AND (PIE.PARENT_ID IS NULL OR PIE.PARENT_ID  ='')"
			+ " AND PIEI.BOND_PLAN_PROJECT_ID= %1$s ";
	
	/**项目实施情况**/
	public static String PROJECT_DISPATCH_IMPL =" SELECT  *  FROM "
				+ " (SELECT * FROM PROJECT_DISPATCH_IMPL PDI  "
				+ " WHERE PDI.PROJECT_ID = %1$s "
			    + " UNION ALL "
			    + " SELECT * FROM PROJECT_DISPATCH_IMPL_HISTORY PDIH  "
				+ " WHERE PDIH.PROJECT_ID = %1$s ) table1"
				+ " ORDER BY REPORT_NUMBER desc";
	/**调度资金到位情况**/
	public static String DISPATCH_INVEST_DOWL_SQL =" SELECT NAME,SUM_MONEY ,"
			+ " PREV_MONEY,CUR_MONEY FROM"
			+ " (SELECT NAME, SUM_MONEY, PREV_MONEY, CUR_MONEY ,`SORT`"
			+ " FROM PROJECT_DISPATCH_IMPL PDI "
			+ " LEFT JOIN PROJECT_DISPATCH_IMPL_INVEST PDII "
			+ " ON PDI.ID = PDII.PROJECT_IMPL_ID "
			+ " LEFT JOIN PROJECT_ITEM_EXT PIE "
			+ " ON  PDII.PROJECT_ITEM_EXT_ID =PIE.ID "
			+ " WHERE PDI.ID = %1$s  AND PDII.TYPE = '1'"
			+ " UNION ALL "
			+ " SELECT NAME, SUM_MONEY, PREV_MONEY, CUR_MONEY ,`SORT` "
			+ " FROM  PROJECT_DISPATCH_IMPL_HISTORY PDIH "
			+ " LEFT JOIN PROJECT_DISPATCH_IMPL_INVEST_HISTORY PDIIH "
			+ " ON PDIH.ID = PDIIH.PROJECT_IMPL_ID "
			+ " LEFT JOIN PROJECT_ITEM_EXT PIE "
			+ " ON PDIIH.PROJECT_ITEM_EXT_ID =PIE.ID"
			+ " WHERE PDIH.ID = %1$s  AND PDIIH.TYPE = '1')table1 "
			+ " ORDER BY `SORT`";
	/**调度资金完成情况**/
	public static String DISPATCH_INVEST_PUT_SQL =" SELECT DECODE(name,null, '其他', name) AS NAME , DECODE(`SORT`,null, 100, `SORT`),"
			+ " MAX(DECODE(TYPE,'2',PREV_MONEY,NULL)) AS FINISH_PREV_MONEY ,"
			+ " MAX(DECODE(TYPE,'2',CUR_MONEY,NULL)) AS FINISH_CUR_MONEY ,"
			+ " MAX(DECODE(TYPE,'3',PREV_MONEY,NULL)) AS PAY_PREV_MONEY ,"
			+ " MAX(DECODE(TYPE,'3',CUR_MONEY,NULL)) AS PAY_CUR_MONEY FROM "
			+ " (SELECT name,TYPE,PREV_MONEY,CUR_MONEY,`SORT` "
			+ " FROM PROJECT_DISPATCH_IMPL PDI "
			+ " LEFT JOIN PROJECT_DISPATCH_IMPL_INVEST PDII "
			+ " ON PDI.ID = PDII.PROJECT_IMPL_ID "
			+ " LEFT JOIN PROJECT_ITEM_EXT PIE "
			+ " ON PDII.PROJECT_ITEM_EXT_ID =PIE.ID "
			+ " WHERE PDI.ID = %1$s  AND PDII.TYPE != '1' "
			+ " UNION ALL "
			+ " SELECT name,TYPE,PREV_MONEY,CUR_MONEY,`SORT` "
			+ " FROM PROJECT_DISPATCH_IMPL_HISTORY PDIH "
			+ " LEFT JOIN PROJECT_DISPATCH_IMPL_INVEST_HISTORY PDIIH "
			+ " ON PDIH.ID = PDIIH.PROJECT_IMPL_ID "
			+ " LEFT JOIN PROJECT_ITEM_EXT PIE "
			+ " ON PDIIH.PROJECT_ITEM_EXT_ID =PIE.ID"
			+ " WHERE PDIH.ID = %1$s  AND PDIIh.TYPE != '1')table1 "
			+ " GROUP BY NAME,`SORT` ORDER BY DECODE(`SORT`,null, 100, `SORT`) ASC ";
	/**项目档案一览查询字段**/
	public static String SEARCH_PROJECT_SQL =" SELECT P.ID,P.STAGE,PIEM.PRO_NAME ,PIEM.INVESTMENT_TOTAL "
			+ ",PIEM.PRO_TYPE,PIEM.INDUSTRY,PIEM.BUILD_PLACE,PIEM.EXPECT_STARTYEAR "
			+ " FROM PROJECT P , PROJECT_INFO_EXT_MASTER PIEM ";
	/**最新的三个调度项目**/
	public static String THIRD_DISPATCH_SQL ="select P.ID,PIEM.PRO_NAME,PDI.IMAGE_PROGRESS,"
			+ " PDI.YEAR_BUILD_CONTENT,PDI.IMAGE_URL "
			+ " from  PROJECT_DISPATCH_IMPL pdi "
			+ " left join project p "
			+ " on p.id = pdi.project_id "
			+ " LEFT JOIN PROJECT_INFO_EXT_MASTER PIEM "
			+ " ON P.ID = PIEM.PROJECT_ID "
			+ " order by pdi.UPDATE_TIME desc limit 3";
}