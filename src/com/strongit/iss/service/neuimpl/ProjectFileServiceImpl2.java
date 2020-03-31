package com.strongit.iss.service.neuimpl;

import com.strongit.iss.entity.*;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.exception.DAOException;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.neuinterface.IProjectFileService2;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * 项目档案实现类 
 * @author xiangyong
 * @Date 2016年7月23日 下午5:33:32
 */
@Service
@Transactional(readOnly=true)
public class ProjectFileServiceImpl2 extends BaseService implements IProjectFileService2 {
	
	@Override
	@Transactional(readOnly=true)
	public Page<Map<String, Object>> searchMapProject(Page<Map<String, Object>> pageBo,
			ProjectFileSearchVo projectVo, Map<String, String> filters) {
		String filterPlaceCode = "";
		if(projectVo!=null&&!"".equals(projectVo.getProjectRegion())){
			filterPlaceCode = projectVo.getProjectRegion();
		}
    	List<Object> paramList = new ArrayList<Object>(); //用于添加查询的参数，统一传入查询方法
		StringBuffer sql = new StringBuffer("select A.PROJECT_NAME as PROJECTNAME, A.TOTAL_MONEY as INVESTMENTTOTAL, A.PROJECT_TYPE as PROJECTTYPE,"
				+ " A.INDUSTRY as INDUSTRYCODE, A.PLACE_CODE as PROJECTREGION, A.START_YEAR as EXPECTSTARTYEAR, A.PROJECT_CODE as PROJECTCODE"
				+ " from TZXMZH.FA_XMXX A, TZXMZH.DIM_DIVISION B"
				+ " WHERE 1=1 and A.PROJECT_NAME is not null and  A.PROJECT_NAME <>'' "
				+ " and A.PLACE_CODE = B.CODE and B.IS_ALONE = 1");
		if(!"".equals(filterPlaceCode)&&null!=filterPlaceCode){
			sql.append(" and B.CODE ='"+filterPlaceCode+"'");
		}
		if(StringUtils.isNotEmpty(filters.get("is_ent"))){//是否为政府投资标识
			String isEnt=filters.get("is_ent").trim().toString();
			if(isEnt.equals("1")){
				sql.append(" AND A.PROJECT_TZ_TYPE ='01' ");
			}
			if(isEnt.equals("2")){
				sql.append(" AND A.PROJECT_TZ_TYPE ='02' ");
			}
			 
		}
		sql.append(" order by A.START_YEAR desc");
			try {
				pageBo = this.dao.findBySql(pageBo, sql.toString(),paramList.toArray());
			} catch (DAOException e) {
				e.printStackTrace();
			}
		return pageBo;
	}
	
	/**
	 * 根据条件查询项目
	 * @orderBy 
	 * @param pageBo
	 * @param projectVo
	 * @return 按所传每页数量返回ProjectFileInfo对象集合
	 * @author xiangyong
	 * @Date 2016年8月4日下午4:42:12
	 */
   
	@Override
	@Transactional(readOnly=true)
	public Page<Map<String, Object>> searchProject(Page<Map<String, Object>> pageBo,
			ProjectFileSearchVo projectVo, Map<String, String> filters) {
		// 定义查询结果返回接收变量
		Page<Map<String, Object>> resultPage = null;
		StringBuffer sql = createBaseProjectSQL();
		List<Object> paramList = new ArrayList<Object>(); //用于添加查询的参数，统一传入查询方法
		if(StringUtils.isNotEmpty(filters.get("projectStage"))){//项目阶段统计情况
			String projectStage=filters.get("projectStage").trim().toString();
			sql.append(" AND XMJD.NAME1='"+projectStage+"'");
        }
        if(StringUtils.isNotEmpty(filters.get("goverLevel"))){//按行政层级统计情况
        	String goverLevel=filters.get("goverLevel").trim().toString();
			sql.append(" AND SPCJ.CODE_NAME='"+goverLevel+"'");
        }
        if(StringUtils.isNotEmpty(filters.get("investRange"))){// 按投资规模统计情况
        	String investRange=filters.get("investRange").trim().toString();
        	sql.append(" AND TZGM.CODE_NAME='"+investRange+"' ");
        }
        if(StringUtils.isNotEmpty(filters.get("month"))){//按月份统计情况
        	String month=filters.get("month").trim().toString();
        	sql.append(" AND TO_CHAR(F.APPLY_DATE,'yyyyMM')='"+month+"' " );
        }
        if(StringUtils.isNotEmpty(filters.get("production"))){// 按产业统计情况
        	String production=filters.get("production").trim().toString();
			sql.append(" AND CYLB.CODE_NAME='"+production+"' ");
        }
        if(StringUtils.isNotEmpty(filters.get("gbIndustry"))){// 按行业统计情况（国标行业）
        	String gbIndustry=filters.get("gbIndustry").trim().toString();
			String[] industrys = gbIndustry.split(">>");
			for(int i=0; i<industrys.length; i++ ){
				if(i!=3){
					sql.append(" AND INDU.NAME"+(i+1)+"='"+industrys[i]+"' ");
				}else{
					sql.append(" AND INDU.NAME='"+industrys[i]+"' ");
				}
			}
        }
        if(StringUtils.isNotEmpty(filters.get("wnIndustry"))){// 按行业统计情况（委内行业）
        	String wnIndustry=filters.get("wnIndustry").trim().toString();
			String[] wnIndustrys = wnIndustry.split(">>");
			for(int i=0; i<wnIndustrys.length; i++ ){
				if(i!=1){
					sql.append(" AND WNIN.NAME"+(i+1)+"='"+wnIndustrys[i]+"' ");
				}else{
					sql.append(" AND WNIN.NAME='"+wnIndustrys[i]+"' ");
				}
			}
        }
        if(StringUtils.isNotEmpty(filters.get("shbType"))){//按审核备类型统计情况
        	String shbType=filters.get("shbType").trim().toString();
			sql.append(" AND XMLX.CODE_NAME='"+shbType+"' ");
        }
        if(StringUtils.isNotEmpty(filters.get("expectStartTime"))){//预计开工时间趋势
        	String expectStartTime=filters.get("expectStartTime").trim().toString();
			sql.append(" AND F.START_YEAR_NAME='"+expectStartTime+"' ");
        }

		if(StringUtils.isNotEmpty(filters.get("is_ent"))){//是否为政府投资标识
			String isEnt=filters.get("is_ent").trim().toString();
			if(isEnt.equals("1")){
				sql.append(" AND F.PROJECT_TZ_TYPE ='01' ");
			}
			if(isEnt.equals("2")){
				sql.append(" AND F.PROJECT_TZ_TYPE ='02' ");
			}
			 
		}
		resultPage = this.dao.findBySql(pageBo, sql.toString(),paramList.toArray());
        return resultPage;
	}
		/**
	 * 根据条件查询事项
	 * @orderBy 
	 * @param pageBo
	 * @param projectVo
	 * @return 按所传每页数量返回ProjectFileInfo对象集合
	 * @author xiangyong
	 * @Date 2016年8月4日下午4:42:12
	 */
   
	@Override
	@Transactional(readOnly=true)
	public Page<Map<String, Object>> searchItem(Page<Map<String, Object>> pageBo,
			ProjectFileSearchVo projectVo, Map<String, String> filters) {
		Page<Map<String, Object>> resultPage = null;
		StringBuffer sql = createBaseItemSQL();
		List<Object> paramList = new ArrayList<Object>(); //用于添加查询的参数，统一传入查询方法
		if(StringUtils.isNotEmpty(filters.get("item"))){//按事项统计各地办理情况（事项）	
        	String item=filters.get("item").trim().toString();
			sql.append(" AND I.STANDARD_SORT_NAME='"+item+"' ");
        }
		if(StringUtils.isNotEmpty(filters.get("department"))){//按部门统计事项办理情况
        	String dept_name=filters.get("department").trim().toString();
        	sql.append(" AND B.CODE_NAME='"+dept_name+"' ");
        }

        if(StringUtils.isNotEmpty(filters.get("itemLevel"))){//按层级统计事项办理情况
        	String item=filters.get("itemLevel").trim().toString();
        	sql.append(" AND F.CENTRAL_APPROVAL_ITEMID IN(SELECT STANDARD_SORT_CODE FROM TZXMZH.DIM_ITEMSORT WHERE IS_DEL='0' AND STANDARD_SORT_NAME = '"+item+"')  ");
        }
		if(StringUtils.isNotEmpty(filters.get("is_ent"))){//是否为政府投资标识
			String isEnt=filters.get("is_ent").trim().toString();
			if(isEnt.equals("1")){
				sql.append(" AND XMXX.PROJECT_TYPE ='01' ");
			}
			if(isEnt.equals("2")){
				sql.append(" AND XMXX.PROJECT_TYPE ='02' ");
			}
			 
		}
		resultPage = this.dao.findBySql(pageBo, sql.toString(),paramList.toArray());
        return resultPage;		
	}
	
	private static StringBuffer createBaseItemSQL(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT                                                   ");
		sql.append("	XMXX.PROJECT_CODE AS projectCode,									 ");//-- 项目代码
		sql.append("	XMXX.PROJECT_NAME AS projectName,									 ");//-- 项目名称
		sql.append("	SXQD.ITEM_NAME AS itemName,										 ");//-- 审批事项（事项名称）
		sql.append("	B.CODE_NAME AS deptName,										 ");//-- 审批部门
		sql.append("	F.IS_DEALED_DATE AS approveDate,									 ");//-- 办理时间
		sql.append("	CASE F.IS_DEAL_COMPLETED                             ");
		sql.append("	WHEN '1' THEN '已办结'                               ");
		sql.append("	ELSE '未办结' END AS state 							 ");//	-- 办理状态
		sql.append("FROM  TZXMZH.FA_SXSL  F               							 ");//-- 事项事实表
		sql.append("INNER JOIN TZXMZH.DIM_SPCJ  S              					 ");//-- 审批层级维度
		sql.append("    ON F.ITEM_SPCJ = S.CODE_VALUE                        ");
		sql.append("INNER JOIN TZXMZH.DIM_DIVISION  D          					 ");//-- 申报地区维度
		sql.append("	ON F.ITEM_SPQH = D.CODE                              ");
		sql.append("INNER JOIN TZXMZH.DIM_ZYBM   B             					 ");//-- 中央部门维度
		sql.append("    ON F.DEPT_CENTER_MAP = B.CODE_VALUE                  ");
		sql.append("INNER JOIN TZXMZH.DIM_ITEMSORT	I          						 ");//-- 中央审批事项维度	
		sql.append("    ON F.CENTRAL_APPROVAL_ITEMID = I.STANDARD_SORT_CODE  ");
		sql.append("INNER JOIN TZXMZH.ENMU_CQAQ C              					 ");//-- 超期按期标签    
		sql.append("    ON F.OUTOFDATENUM =C.CODE_VALUE                      ");
		sql.append("INNER JOIN TZXMZH.ENMU_ZBBJ  Z             					 ");//-- 再办办结标签
		sql.append("    ON F.IS_DEAL_COMPLETED =Z.CODE_VALUE                 ");
		sql.append("INNER JOIN TZXMZH.FA_SXQD SXQD                                  ");//-- 事项清单实例
		sql.append("	ON F.ITEM_CODE = SXQD.ITEM_CODE                      ");
//		sql.append("	AND SXQD.VERSION_STATE ='1'                          ");
		sql.append("INNER JOIN TZXMZH.FA_XMXX XMXX                                  ");//-- 项目信息实例
		sql.append("	ON F.PROJECTUUID = XMXX.ID                           ");
		sql.append("WHERE 1=1                                                ");
		return sql;
	}
	
	
	private static StringBuffer createBaseProjectSQL(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT                                       ");
		sql.append("	F.PROJECT_NAME AS PROJECTNAME,           ");
		sql.append("	F.TOTAL_MONEY AS INVESTMENTTOTAL,        ");
		sql.append("	XMLX.CODE_NAME AS PROJECTTYPE,           ");
		sql.append("	INDU.NAME1 AS INDUSTRYCODE,              ");
		sql.append("	PLACE.NAME AS PROJECTREGION,             ");
		sql.append("	F.APPLY_DATE AS EXPECTSTARTYEAR          ");
		sql.append("FROM TZXMZH.FA_XMXX F                       		 ");//-- _MAP
		//sql.append(" INNER JOIN	ENMU_AREA AREA				 ");//	-- 地区配置表
		//sql.append("  	ON	MAP.AREA_TYPE = AREA.CODE_VALUE  ");
		sql.append("INNER JOIN	TZXMZH.DIM_INDUSTRY INDU				 ");//	-- 国标行业维度
		sql.append("	ON	F.INDUSTRY = INDU.CODE               ");
		sql.append("INNER JOIN 	TZXMZH.DIM_TZGM TZGM					 ");//	-- 投资规模维度
		sql.append("	ON	F.TZGM = TZGM.CODE_VALUE             ");
		sql.append("INNER JOIN	TZXMZH.DIM_CYLB CYLB					 ");//	-- 产业类别维度
		sql.append("	ON F.CYLB = CYLB.CODE_VALUE              ");
		sql.append("INNER JOIN	TZXMZH.DIM_SPCJ SPCJ					 ");//	-- 审批层级维度
		sql.append("	ON F.SPCJ = SPCJ.CODE_VALUE              ");
		sql.append("INNER JOIN	TZXMZH.DIM_XMLX XMLX					 ");//	-- 项目类型维度
		sql.append("	ON F.PROJECT_TYPE = XMLX.CODE_VALUE		 ");
		sql.append("LEFT JOIN	TZXMZH.DIM_XMJD XMJD					 ");//	-- 项目阶段维度
		sql.append("	ON F.PROJECT_STATE = XMJD.CODE           ");
		sql.append("LEFT JOIN	TZXMZH.DIM_ZYBM ZYBM					 ");//	-- 中央部门维度
		sql.append("	ON F.CENTER_DEPT_CODE = ZYBM.CODE_VALUE  ");
		sql.append("INNER JOIN	TZXMZH.DIM_WNINDUSTRY WNIN				 ");//	-- 委内行业维度
		sql.append("	ON F.WNINDUSTRY = WNIN.CODE              ");
		sql.append("INNER JOIN TZXMZH.DIM_PLACE PLACE					 ");//	-- 建设地点维度	
		sql.append("	ON F.PLACE_CODE = PLACE.CODE             ");
		sql.append("WHERE 1=1             						 ");
		return sql;
	}

//    private static boolean stringIsEmpty(str){
//    	retrun (str == null || str.trim().equals("")) ? true: false;
//    }

		@Override
		@Transactional(readOnly=true)
		public List getNameByCode(String code,String kind) throws BusinessServiceException {	
			List<Object> resume = null;
			if(kind.equals("SHLX")){
				String resumeHql="FROM Code2Name resume  WHERE resume.code = ?"+" and resume.kind = ?";
				resume= this.dao.find(resumeHql,code,kind);
			}
			if(kind.equals("INDUSTRY")){
				String resumeHql="FROM IndustryInfo resume  WHERE resume.code4 = ?";
				resume= this.dao.find(resumeHql,code);
			}
			if(kind.equals("PLACE")){
				String resumeHql="FROM PlaceInfo2 resume  WHERE resume.code3 = ?";
				resume= this.dao.find(resumeHql,code);
			}
			if(kind.equals("PLACE_NAME")){
				String resumeHql="FROM PlaceInfo2 resume  WHERE resume.name3 = ?";
				resume= this.dao.find(resumeHql,code);
			}
			return resume;
		}

}
