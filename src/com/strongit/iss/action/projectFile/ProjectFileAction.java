package com.strongit.iss.action.projectFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Cache;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.Datagrid;
import com.strongit.iss.common.JsonTreeData;
import com.strongit.iss.common.PropertiesUtil;
import com.strongit.iss.common.RecursiveData;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.common.TreeNode;
import com.strongit.iss.common.TreeNodeUtil;
import com.strongit.iss.entity.Label;
import com.strongit.iss.entity.ProjectFileSearchVo;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.ILabelService;
import com.strongit.iss.service.IProjectFileService;

/**
 * @author XiaXiang
 *
 */
@SuppressWarnings("serial")
public class ProjectFileAction extends BaseActionSupport<Object> {
	
	@Autowired
	private IProjectFileService projectFileService;
	@Autowired
	private ILabelService labelService;

	
	private String projectGuid;//项目ID
	private String page;  //分页查询页数
	private String rows;  //分页查询行数
	//页面检索条件
	private Map<String, String> paramsMap = new HashMap<String, String>();
	//封装项目查询vo类
	private ProjectFileSearchVo projectVo = new ProjectFileSearchVo();
	
	//定义地图传参map
	private Map<String, String> filters = new HashMap<String, String>();
	//定义跳转到ProjectFile-list页面来源
	private String innerPage;
	//项目模块值
	private String moduleCode;
	
	/** 自定义标签传参 */
	private Map<String, String> labelParams = new HashMap<String, String>();
	/** 项目ID */
	private String projectId;
	/**
	 * 项目档案详情
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午3:25:17
	 */
	public String details() {
//		projectGuid = "426DF604-E783-4BD4-9A02-CBAAF6DEA001"; //Struts2Utils.getRequest().getParameter("projectGuid");
		return "details";
	}
	
	public String listWarn() {
//		projectGuid = "426DF604-E783-4BD4-9A02-CBAAF6DEA001"; //Struts2Utils.getRequest().getParameter("projectGuid");
		return "listWarn";
	}
	
	/**
	 * 数据下钻跳转到列表
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年10月30日上午11:00:08
	 */
	public String WNlist() {
		try {
			// 建设地点
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("BuildPlaceCode"))){
				String builPlaceCode =filters.get("BuildPlaceCode").trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					builPlaceCode = new String(builPlaceCode.getBytes("ISO8859-1"),"UTF-8");
				}				
				//地图所传地区全路径名称去掉国家
				String builPlaceName = builPlaceCode.replace("中国"+Constant.BI_SPLIT,"");
				String builPlaceChineseName = builPlaceName.replace(Constant.BI_SPLIT, "-");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(builPlaceName);
				filters.put("builPlaceCode", code);
				filters.put("builPlaceChineseName", builPlaceChineseName);
			}
			// 重大战略情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("majorstrategyName"))){
				String majorstrategy  = URLDecoder.decode(filters.get("majorstrategyName"), "UTF-8");
				String majorstrategyName =majorstrategy.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					majorstrategyName = new String(majorstrategyName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				majorstrategyName = majorstrategyName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(majorstrategyName);
				filters.put("majorstrategyName", code);
			}
			// 国标行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				String GBIndustry  = URLDecoder.decode(filters.get("GovernmentCode"), "UTF-8");
				String GBIndustryName =GBIndustry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					GBIndustryName = new String(GBIndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				GBIndustryName = GBIndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(GBIndustryName);
				filters.put("GovernmentCode", code);
			}
			// 委内行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("IndustryCode"))){
				String Industry  = URLDecoder.decode(filters.get("IndustryCode"), "UTF-8");
				String IndustryName =Industry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					IndustryName = new String(IndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				IndustryName = IndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(IndustryName);
				filters.put("IndustryCode", code);
			}
			// 申报部门
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("departmentName"))){
				String departmentName  = URLDecoder.decode(filters.get("departmentName"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					departmentName = new String(departmentName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				departmentName = departmentName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("departmentName", departmentName);
			}
			// 储备层级
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("storeLevel"))){
				String storeLevel  = URLDecoder.decode(filters.get("storeLevel"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					storeLevel = new String(storeLevel.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				storeLevel = storeLevel.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("storeLevel", storeLevel);
			}
			// 五年储备入库趋势
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("storeTime"))){
				String storeTime  = URLDecoder.decode(filters.get("storeTime"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					storeTime = new String(storeTime.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				storeTime = storeTime.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("storeTime", storeTime);
			}
			// 项目类型
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "WNlist";
	}
	
	public String SBlist() {
		try {
			// 建设地点
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("BuildPlaceCode"))){
				String builPlaceCode =filters.get("BuildPlaceCode").trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					builPlaceCode = new String(builPlaceCode.getBytes("ISO8859-1"),"UTF-8");
				}				
				//地图所传地区全路径名称去掉国家
				String builPlaceName = builPlaceCode.replace("中国"+Constant.BI_SPLIT,"");
				String builPlaceChineseName = builPlaceName.replace(Constant.BI_SPLIT, "-");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(builPlaceName);
				filters.put("builPlaceCode", code);
				filters.put("builPlaceChineseName", builPlaceChineseName);
			}
			// 政府投资方向
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("goverInvestDir"))){
				String goverInvestDir  = URLDecoder.decode(filters.get("goverInvestDir"), "UTF-8");
				goverInvestDir =goverInvestDir.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					goverInvestDir = new String(goverInvestDir.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				goverInvestDir = goverInvestDir.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(goverInvestDir);
				if(StringUtils.isEmpty(code)){
					for(int i=0; ;i++){
					goverInvestDir =StringUtils.substringBeforeLast(goverInvestDir,">>");
				    code=Cache.getCodeByFullName(goverInvestDir);
					    if(StringUtils.isNotEmpty(code)){
					    	break;
					    }
					}
				}
				filters.put("goverInvestDir", code);
			}
			// 国标行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				String GBIndustry  = URLDecoder.decode(filters.get("GovernmentCode"), "UTF-8");
				String GBIndustryName =GBIndustry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					GBIndustryName = new String(GBIndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				GBIndustryName = GBIndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(GBIndustryName);
				filters.put("GovernmentCode", code);
			}
			// 投资规模情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("tzgm"))){
				String tzgm  = URLDecoder.decode(filters.get("tzgm"), "UTF-8");
				tzgm =tzgm.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					tzgm = new String(tzgm.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				tzgm = tzgm.replace("顶层"+Constant.BI_SPLIT,"");
				filters.put("tzgm", tzgm);
			}
			// 重大战略情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("majorstrategyName"))){
				String majorstrategy  = URLDecoder.decode(filters.get("majorstrategyName"), "UTF-8");
				String majorstrategyName =majorstrategy.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					majorstrategyName = new String(majorstrategyName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				majorstrategyName = majorstrategyName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(majorstrategyName);
				String label  = URLDecoder.decode(filters.get("label"), "UTF-8");
				filters.put("label", label);
				filters.put("majorstrategyName", code);
			}
			// 委内司局情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("wnsj"))){
				String falg =URLDecoder.decode(filters.get("falg"), "UTF-8").trim();;
				String wnsj  = URLDecoder.decode(filters.get("wnsj"), "UTF-8");
				wnsj =wnsj.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					wnsj = new String(wnsj.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				wnsj = wnsj.replace("顶层"+Constant.BI_SPLIT,"");
				filters.put("falg",falg);
				filters.put("wnsj", wnsj);
			}
			// 申报部门
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("departmentName"))){
				String departmentName  = URLDecoder.decode(filters.get("departmentName"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					departmentName = new String(departmentName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				departmentName = departmentName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("departmentName", departmentName);
			}
			// 项目类型
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			// 申报时间
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("applyTime"))){
				String applyTime  = URLDecoder.decode(filters.get("applyTime"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					applyTime = new String(applyTime.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				applyTime = applyTime.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("applyTime", applyTime);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "SBlist";
	}
	
	public String XDlist() {
		try {
			// 建设地点
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("BuildPlaceCode"))){
				String builPlaceCode =filters.get("BuildPlaceCode").trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					builPlaceCode = new String(builPlaceCode.getBytes("ISO8859-1"),"UTF-8");
				}				
				//地图所传地区全路径名称去掉国家
				String builPlaceName = builPlaceCode.replace("中国"+Constant.BI_SPLIT,"");
				String builPlaceChineseName = builPlaceName.replace(Constant.BI_SPLIT, "-");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(builPlaceName);
				filters.put("builPlaceCode", code);
				filters.put("builPlaceChineseName", builPlaceChineseName);
			}
			// 政府投资方向
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("goverInvestDir"))){
				String goverInvestDir  = URLDecoder.decode(filters.get("goverInvestDir"), "UTF-8");
				goverInvestDir =goverInvestDir.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					goverInvestDir = new String(goverInvestDir.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				goverInvestDir = goverInvestDir.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(goverInvestDir);
				if(StringUtils.isEmpty(code)){
					for(int i=0; ;i++){
					goverInvestDir =StringUtils.substringBeforeLast(goverInvestDir,">>");
				    code=Cache.getCodeByFullName(goverInvestDir);
					    if(StringUtils.isNotEmpty(code)){
					    	break;
					    }
					}
				}
				filters.put("goverInvestDir", code);
			}
			// 国标行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				String GBIndustry  = URLDecoder.decode(filters.get("GovernmentCode"), "UTF-8");
				String GBIndustryName =GBIndustry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					GBIndustryName = new String(GBIndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				GBIndustryName = GBIndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(GBIndustryName);
				filters.put("GovernmentCode", code);
			}
			// 投资规模情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("tzgm"))){
				String tzgm  = URLDecoder.decode(filters.get("tzgm"), "UTF-8");
				tzgm =tzgm.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					tzgm = new String(tzgm.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				tzgm = tzgm.replace("顶层"+Constant.BI_SPLIT,"");
				filters.put("tzgm", tzgm);
			}
			// 重大战略情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("majorstrategyName"))){
				String majorstrategy  = URLDecoder.decode(filters.get("majorstrategyName"), "UTF-8");
				String majorstrategyName =majorstrategy.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					majorstrategyName = new String(majorstrategyName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				majorstrategyName = majorstrategyName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(majorstrategyName);
				filters.put("majorstrategyName", code);
			}
			// 委内司局情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("wnsj"))){
				String wnsj  = URLDecoder.decode(filters.get("wnsj"), "UTF-8");
				wnsj =wnsj.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					wnsj = new String(wnsj.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				wnsj = wnsj.replace("顶层"+Constant.BI_SPLIT,"");
				filters.put("wnsj", wnsj);
			}
			// 申报部门
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("departmentName"))){
				String departmentName  = URLDecoder.decode(filters.get("departmentName"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					departmentName = new String(departmentName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				departmentName = departmentName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("departmentName", departmentName);
			}
			// 项目类型
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			// 申报时间
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("issuedTime"))){
				String issuedTime  = URLDecoder.decode(filters.get("issuedTime"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					issuedTime = new String(issuedTime.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				issuedTime = issuedTime.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("issuedTime", issuedTime);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "XDlist";
	}

	public String DDlist() {
		try {
			// 建设地点
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("BuildPlaceCode"))){
				String builPlaceCode =filters.get("BuildPlaceCode").trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					builPlaceCode = new String(builPlaceCode.getBytes("ISO8859-1"),"UTF-8");
				}				
				//地图所传地区全路径名称去掉国家
				String builPlaceName = builPlaceCode.replace("中国"+Constant.BI_SPLIT,"");
				String builPlaceChineseName = builPlaceName.replace(Constant.BI_SPLIT, "-");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(builPlaceName);
				filters.put("builPlaceCode", code);
				filters.put("builPlaceChineseName", builPlaceChineseName);
			}
			// 调度任务分别情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("dispatch"))){
				String dispatch  = URLDecoder.decode(filters.get("dispatch"), "UTF-8");
				dispatch =dispatch.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					dispatch = new String(dispatch.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				dispatch = dispatch.replace("顶层"+Constant.BI_SPLIT,"");
				filters.put("dispatch", dispatch);
			}
			// 调度项目进展情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("process"))){
				String process  = URLDecoder.decode(filters.get("process"), "UTF-8");
				process =process.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					process = new String(process.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				process = process.replace("顶层"+Constant.BI_SPLIT,"");
				String code=Cache.getCodeByFullName(process);
				filters.put("process", code);
			}
			// 国标行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				String GBIndustry  = URLDecoder.decode(filters.get("GovernmentCode"), "UTF-8");
				String GBIndustryName =GBIndustry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					GBIndustryName = new String(GBIndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				GBIndustryName = GBIndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(GBIndustryName);
				filters.put("GovernmentCode", code);
			}
			// 竣工或拟竣工时间
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("endTime"))){
				String endTime  = URLDecoder.decode(filters.get("endTime"), "UTF-8");
				endTime =endTime.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					endTime = new String(endTime.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				endTime = endTime.replace("顶层"+Constant.BI_SPLIT,"");
				filters.put("endTime", endTime);
			}
			// 竣工或拟竣工时间
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("wholeProcessPlace"))){
				String wholeProcessPlace  = URLDecoder.decode(filters.get("wholeProcessPlace"), "UTF-8");
				wholeProcessPlace =wholeProcessPlace.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					wholeProcessPlace = new String(wholeProcessPlace.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				wholeProcessPlace = wholeProcessPlace.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(wholeProcessPlace);
				filters.put("wholeProcessPlace", code);
			}
			// 重大战略情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("majorstrategyName"))){
				String majorstrategy  = URLDecoder.decode(filters.get("majorstrategyName"), "UTF-8");
				String majorstrategyName =majorstrategy.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					majorstrategyName = new String(majorstrategyName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				majorstrategyName = majorstrategyName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(majorstrategyName);
				filters.put("majorstrategyName", code);
			}
			// 委内司局情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("wnsj"))){
				String wnsj  = URLDecoder.decode(filters.get("wnsj"), "UTF-8");
				wnsj =wnsj.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					wnsj = new String(wnsj.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				wnsj = wnsj.replace("顶层"+Constant.BI_SPLIT,"");
				filters.put("wnsj", wnsj);
			}
			// 申报部门
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("departmentName"))){
				String departmentName  = URLDecoder.decode(filters.get("departmentName"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					departmentName = new String(departmentName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				departmentName = departmentName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("departmentName", departmentName);
			}
			// 项目类型
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			// 申报时间
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("issuedTime"))){
				String issuedTime  = URLDecoder.decode(filters.get("issuedTime"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					issuedTime = new String(issuedTime.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				issuedTime = issuedTime.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("issuedTime", issuedTime);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "DDlist";
	}
	
	/**
	 * 跳转项目档案的主页面
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年10月30日上午11:00:08
	 */
	public String list() {
		try {
			// 建设地点
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("BuildPlaceCode"))){
				String builPlaceCode =filters.get("BuildPlaceCode").trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					builPlaceCode = new String(builPlaceCode.getBytes("ISO8859-1"),"UTF-8");
				}				
				//地图所传地区全路径名称去掉国家
				String builPlaceName = builPlaceCode.replace("中国"+Constant.BI_SPLIT,"");
				String builPlaceChineseName = builPlaceName.replace(Constant.BI_SPLIT, "-");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(builPlaceName);
				filters.put("builPlaceCode", code);
				filters.put("builPlaceChineseName", builPlaceChineseName);
			}
			// 重大战略情况
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				String GBIndustry  = URLDecoder.decode(filters.get("GovernmentCode"), "UTF-8");
				String GBIndustryName =GBIndustry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					GBIndustryName = new String(GBIndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				GBIndustryName = GBIndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(GBIndustryName);
				filters.put("GovernmentCode", code);
			}
			// 国标行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				String GBIndustry  = URLDecoder.decode(filters.get("GovernmentCode"), "UTF-8");
				String GBIndustryName =GBIndustry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					GBIndustryName = new String(GBIndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				GBIndustryName = GBIndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(GBIndustryName);
				filters.put("GovernmentCode", code);
			}
			// 委内行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("IndustryCode"))){
				String Industry  = URLDecoder.decode(filters.get("IndustryCode"), "UTF-8");
				String IndustryName =Industry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					IndustryName = new String(IndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				IndustryName = IndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(IndustryName);
				filters.put("IndustryCode", code);
			}
			// 申报部门
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get(""))){
				String startYear  = URLDecoder.decode(filters.get("startYear"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					startYear = new String(startYear.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				startYear = startYear.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("startYear", startYear);
			}
			// 储备层级
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			// 五年储备入库趋势
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			// 项目类型
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			// 专项类型
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("zxlb"))){
				String zxlb  = URLDecoder.decode(filters.get("zxlb"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					zxlb = new String(zxlb.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				zxlb = zxlb.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取code
				String code=Cache.getCodeByFullName(zxlb);
				filters.put("zxlb", code);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "list";
	}
	/**
	 * 跳转项目档案的主页面
	 * @orderBy
	 * @return
	 * @author tanghw
	 * @Date 2016年10月30日上午11:00:08
	 */
	public String listXN() {
		return "listXN";
	}
	/**
	 *
	 * @return
     */
	public String listNv() {
		try {
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("BuildPlaceCode"))){
				String builPlace  = URLDecoder.decode(filters.get("BuildPlaceCode"), "UTF-8");
				String builPlaceCode =builPlace.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					builPlaceCode = new String(builPlaceCode.getBytes("ISO8859-1"),"UTF-8");
				}
				//地图所传地区全路径名称去掉国家
				String builPlaceName = builPlaceCode.replace("中国"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(builPlaceName);
				filters.put("BuildPlaceCode", code);
			}
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				String GBIndustry  = URLDecoder.decode(filters.get("GovernmentCode"), "UTF-8");
				String GBIndustryName =GBIndustry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					GBIndustryName = new String(GBIndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				GBIndustryName = GBIndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(GBIndustryName);
				filters.put("GovernmentCode", code);
			}
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("IndustryCode"))){
				String Industry  = URLDecoder.decode(filters.get("IndustryCode"), "UTF-8");
				String IndustryName =Industry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					IndustryName = new String(IndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				IndustryName = IndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(IndustryName);
				filters.put("IndustryCode", code);
			}
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("expectStartYear"))){
				String expectStartYear  = URLDecoder.decode(filters.get("expectStartYear"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					expectStartYear = new String(expectStartYear.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				expectStartYear = expectStartYear.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("expectStartYear", expectStartYear);
			}
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("startYear"))){
				String startYear  = URLDecoder.decode(filters.get("startYear"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					startYear = new String(startYear.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				startYear = startYear.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("startYear", startYear);
			}
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("delayYears"))){
				String delayYears  = URLDecoder.decode(filters.get("delayYears"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					delayYears = new String(delayYears.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				delayYears = delayYears.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("delayYears", delayYears);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "listNv";
	}
	/**
	 * 获取项目履历信息
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午3:31:17
	 */
	public String getProjectRecord() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//履历类型
		String type = Struts2Utils.getRequest().getParameter("type");
		//履历列表
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getProjectRecordByGuid(projectGuid,type,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getProjectRecordByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取审核备项目履历信息
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午3:31:17
	 */
	public String getShbProjectRecord() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//履历类型
		String type = Struts2Utils.getRequest().getParameter("type");
		//履历列表
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getProjectRecordByGuid(projectGuid, type,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getProjectRecordByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 获取基本信息
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:47:13
	 */
	public String getBaseInfo() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//基本信息
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getBaseInfoByGuid(projectGuid,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getBaseInfoByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		if (null != list && list.size() > 0) {
			Struts2Utils.renderJson(list.get(0));
		}
		return null;
	}
	/**
	 * 获取量化建设规模
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日上午11:16:16
	 */
	public String getQuaInfo() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//基本信息
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getQuaInfoByGuid(projectGuid,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getQuaInfoByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	}
	/**
	 * 审核备办理事项
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:49:17
	 */
	public String getMatterInfo() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//审核备办理事项
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getMatterInfoByGuid(projectGuid,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getMatterInfoByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 投资情况
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:50:07
	 */
	public String getInvestmentInfo() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		Map<String, Object> map = new HashMap<String, Object>();
		//基本信息
		List<Map<String, Object>> baseList = projectFileService.getBaseInfoByGuid(projectGuid,moduleCode);
		if (null != baseList && baseList.size() > 0) {
			map.put("belongYear", baseList.get(0).get("BELONG_YEAR"));
			map.put("isBond", baseList.get(0).get("ISBOND"));
		}
		//投资情况
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getInvestmentInfoByGuid(projectGuid,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getInvestmentInfoByGuid method cost time : "
				+ (end - start) + " mills.");
		map.put("invList", list);
		//返回数据
		Struts2Utils.renderJson(map);
		return null;
	}
	
	/**
	 * 计划下达情况
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:50:37
	 */
	public String getIssuedInfo() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//履历列表
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getIssuedInfoByGuid(projectGuid,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getIssuedInfoByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 资金到位完成情况
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:51:13
	 */
	public String getFinishInfo() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//履历列表
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getFinishInfoByGuid(projectGuid,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getFinishInfoByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	}
	
	/**
	 * 各期项目调度数据
	 * @orderBy 
	 * @return
	 * @author XiaXiang
	 * @Date 2016年10月18日下午4:51:53
	 */
	public String getDispatchInfo() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//履历列表
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getDispatchInfoByGuid(projectGuid,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getDispatchInfoByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	}
	/**
	 * 项目前期工作数据
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日上午11:37:11
	 */
	public String getPreworkInfo() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//前期工作
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getPreworkInfoByGuid(projectGuid,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getPreworkInfoByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	}
	/**
	 * 项目资金构成信息
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年10月27日下午12:12:25
	 */
	public String getInvestConstituteInfo() {
		//项目ID
		projectGuid = Struts2Utils.getRequest().getParameter("projectGuid");
		//前期工作
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getInvestConstituteInfoByGuid(projectGuid,moduleCode);
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getInvestConstituteInfoByGuid method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String showWarningProject(){
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage); 
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null&&null!=user.get("employee_guid")){
			userId = user.get("employee_guid").toString();
		}
		long start =System.currentTimeMillis();
		try {
			Map<String,String> reportParamsMap =(Map<String,String>)this.getSession().getAttribute(filters.get("sessionId")+"_cache");
			pageBo = projectFileService.searchWarningProject(userId,pageBo,projectVo,filters,reportParamsMap);

//				pageBo = projectFileService.searchProject(userId,pageBo,projectVo,filters);
//			pageBo = projectFileService.searchFileProject(pageBo, projectVo, filters);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.searchFileProject method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		if (null == list) {
			list = new ArrayList<Map<String, Object>>();
		}
		// 封装到datagrid中让easyui控件渲染
		Datagrid<Map<String, Object>> dg = new Datagrid<Map<String, Object>>(pageBo.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;
	
	}
	
	/**
	 * 
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年10月29日下午7:31:49
	 */
	public String showProject(){
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage); 
		if (paramsMap!=null) {
			//竣工年份
			projectVo.setPlanEndYear1(paramsMap.get("planEndYear1"));
			projectVo.setPlanEndYear2(paramsMap.get("planEndYear2"));
			//中央预算内申请规模
			projectVo.setCupApplyBudCaptial1(paramsMap.get("centerApplyScaleStart"));
			projectVo.setCupApplyBudCaptial2(paramsMap.get("centerApplyScaleEnd"));
			//专项申请规模
			projectVo.setCupApplySpeCaptial1(paramsMap.get("specialApplyScaleStart"));
			projectVo.setCupApplySpeCaptial2(paramsMap.get("specialApplyScaleEnd"));
			//中央预算内需求规模
			projectVo.setCupNeedBudCaptial1(paramsMap.get("centerDemandScaleStart"));
			projectVo.setCupNeedBudCaptial2(paramsMap.get("centerDemandScaleEnd"));
			//专项需求规模
			projectVo.setCupNeedSpeCaptial1(paramsMap.get("specialDemandScaleStart"));
			projectVo.setCupNeedSpeCaptial2(paramsMap.get("specialDemandScaleEnd"));
			//开工年份
			projectVo.setPlanStartYear1(paramsMap.get("planStartYear1"));
			projectVo.setPlanStartYear2(paramsMap.get("planStartYear2"));
			//开工月份
			projectVo.setPlanStartMonth1(paramsMap.get("startMonth1"));
			projectVo.setPlanStartMonth2(paramsMap.get("startMonth2"));
			//所属行业
			projectVo.setIndustryCode(paramsMap.get("industryCode"));
			//建设地点
			projectVo.setProjectRegion(paramsMap.get("projectRegion"));
			//是否专项建设
			projectVo.setIsSpecFunds(paramsMap.get("isSpecFunds"));
			//项目类型
			projectVo.setCheckLevel(paramsMap.get("checkLevel"));
			//政府投资方向
			projectVo.setFitIndPolicyCode(paramsMap.get("fitIndPolicyCode"));
			//是否ppp
			projectVo.setIsPpp(paramsMap.get("isPpp"));
			//项目名称
			projectVo.setProjectName(paramsMap.get("projectName"));
			//标签id
			projectVo.setLabelId(paramsMap.get("labelId"));
			//项目申报日期
			projectVo.setProjectApplyTime1(paramsMap.get("projectApplyTime1"));
			projectVo.setProjectApplyTime2(paramsMap.get("projectApplyTime2"));
			//总投资
			projectVo.setAllCaptial1(paramsMap.get("allCaptial1"));
			projectVo.setAllCaptial2(paramsMap.get("allCaptial2"));
		}
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null&&null!=user.get("employee_guid")){
			userId = user.get("employee_guid").toString();
		}
		long start =System.currentTimeMillis();
		try {
			Map<String,String> reportParamsMap =(Map<String,String>)this.getSession().getAttribute(filters.get("sessionId")+"_cache");
			pageBo = projectFileService.searchProject(userId,pageBo,projectVo,filters,reportParamsMap);

//				pageBo = projectFileService.searchProject(userId,pageBo,projectVo,filters);
//			pageBo = projectFileService.searchFileProject(pageBo, projectVo, filters);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.searchFileProject method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		if (null == list) {
			list = new ArrayList<Map<String, Object>>();
		}
		// 封装到datagrid中让easyui控件渲染
		Datagrid<Map<String, Object>> dg = new Datagrid<Map<String, Object>>(pageBo.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;
	
	}
	
	/**
	 * 
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年10月29日下午7:31:49
	 */
	public String showWNProject(){
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage); 
		if (paramsMap!=null) {
			//竣工年份
			projectVo.setPlanEndYear1(paramsMap.get("planEndYear1"));
			projectVo.setPlanEndYear2(paramsMap.get("planEndYear2"));
			//中央预算内申请规模
			projectVo.setCupApplyBudCaptial1(paramsMap.get("centerApplyScaleStart"));
			projectVo.setCupApplyBudCaptial2(paramsMap.get("centerApplyScaleEnd"));
			//专项申请规模
			projectVo.setCupApplySpeCaptial1(paramsMap.get("specialApplyScaleStart"));
			projectVo.setCupApplySpeCaptial2(paramsMap.get("specialApplyScaleEnd"));
			//中央预算内需求规模
			projectVo.setCupNeedBudCaptial1(paramsMap.get("centerDemandScaleStart"));
			projectVo.setCupNeedBudCaptial2(paramsMap.get("centerDemandScaleEnd"));
			//专项需求规模
			projectVo.setCupNeedSpeCaptial1(paramsMap.get("specialDemandScaleStart"));
			projectVo.setCupNeedSpeCaptial2(paramsMap.get("specialDemandScaleEnd"));
			//开工年份
			projectVo.setPlanStartYear1(paramsMap.get("planStartYear1"));
			projectVo.setPlanStartYear2(paramsMap.get("planStartYear2"));
			//开工月份
			projectVo.setPlanStartMonth1(paramsMap.get("startMonth1"));
			projectVo.setPlanStartMonth2(paramsMap.get("startMonth2"));
			//所属行业
			projectVo.setIndustryCode(paramsMap.get("industryCode"));
			//建设地点
			projectVo.setProjectRegion(paramsMap.get("projectRegion"));
			//是否专项建设
			projectVo.setIsSpecFunds(paramsMap.get("isSpecFunds"));
			//项目类型
			projectVo.setCheckLevel(paramsMap.get("checkLevel"));
			//政府投资方向
			projectVo.setFitIndPolicyCode(paramsMap.get("fitIndPolicyCode"));
			//是否ppp
			projectVo.setIsPpp(paramsMap.get("isPpp"));
			//项目名称
			projectVo.setProjectName(paramsMap.get("projectName"));
			//标签id
			projectVo.setLabelId(paramsMap.get("labelId"));
			//项目申报日期
			projectVo.setProjectApplyTime1(paramsMap.get("projectApplyTime1"));
			projectVo.setProjectApplyTime2(paramsMap.get("projectApplyTime2"));
			//总投资
			projectVo.setAllCaptial1(paramsMap.get("allCaptial1"));
			projectVo.setAllCaptial2(paramsMap.get("allCaptial2"));
		}
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null&&null!=user.get("employee_guid")){
			userId = user.get("employee_guid").toString();
		}
		long start =System.currentTimeMillis();
		try {
			Map<String,String> reportParamsMap =(Map<String,String>)this.getSession().getAttribute(filters.get("sessionId")+"_cache");
			pageBo = projectFileService.searchWNProject(userId,pageBo,projectVo,filters,reportParamsMap);

//				pageBo = projectFileService.searchProject(userId,pageBo,projectVo,filters);
//			pageBo = projectFileService.searchFileProject(pageBo, projectVo, filters);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.searchFileProject method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		if (null == list) {
			list = new ArrayList<Map<String, Object>>();
		}
		// 封装到datagrid中让easyui控件渲染
		Datagrid<Map<String, Object>> dg = new Datagrid<Map<String, Object>>(pageBo.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;
	
	}
	
	public String showSBProject(){
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage); 
		if (paramsMap!=null) {
			//竣工年份
			projectVo.setPlanEndYear1(paramsMap.get("planEndYear1"));
			projectVo.setPlanEndYear2(paramsMap.get("planEndYear2"));
			//中央预算内申请规模
			projectVo.setCupApplyBudCaptial1(paramsMap.get("centerApplyScaleStart"));
			projectVo.setCupApplyBudCaptial2(paramsMap.get("centerApplyScaleEnd"));
			//专项申请规模
			projectVo.setCupApplySpeCaptial1(paramsMap.get("specialApplyScaleStart"));
			projectVo.setCupApplySpeCaptial2(paramsMap.get("specialApplyScaleEnd"));
			//中央预算内需求规模
			projectVo.setCupNeedBudCaptial1(paramsMap.get("centerDemandScaleStart"));
			projectVo.setCupNeedBudCaptial2(paramsMap.get("centerDemandScaleEnd"));
			//专项需求规模
			projectVo.setCupNeedSpeCaptial1(paramsMap.get("specialDemandScaleStart"));
			projectVo.setCupNeedSpeCaptial2(paramsMap.get("specialDemandScaleEnd"));
			//开工年份
			projectVo.setPlanStartYear1(paramsMap.get("planStartYear1"));
			projectVo.setPlanStartYear2(paramsMap.get("planStartYear2"));
			//开工月份
			projectVo.setPlanStartMonth1(paramsMap.get("startMonth1"));
			projectVo.setPlanStartMonth2(paramsMap.get("startMonth2"));
			//所属行业
			projectVo.setIndustryCode(paramsMap.get("industryCode"));
			//建设地点
			projectVo.setProjectRegion(paramsMap.get("projectRegion"));
			//是否专项建设
			projectVo.setIsSpecFunds(paramsMap.get("isSpecFunds"));
			//项目类型
			projectVo.setCheckLevel(paramsMap.get("checkLevel"));
			//政府投资方向
			projectVo.setFitIndPolicyCode(paramsMap.get("fitIndPolicyCode"));
			//是否ppp
			projectVo.setIsPpp(paramsMap.get("isPpp"));
			//项目名称
			projectVo.setProjectName(paramsMap.get("projectName"));
			//标签id
			projectVo.setLabelId(paramsMap.get("labelId"));
			//项目申报日期
			projectVo.setProjectApplyTime1(paramsMap.get("projectApplyTime1"));
			projectVo.setProjectApplyTime2(paramsMap.get("projectApplyTime2"));
			//总投资
			projectVo.setAllCaptial1(paramsMap.get("allCaptial1"));
			projectVo.setAllCaptial2(paramsMap.get("allCaptial2"));
		}
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null&&null!=user.get("employee_guid")){
			userId = user.get("employee_guid").toString();
		}
		long start =System.currentTimeMillis();
		try {
			Map<String,String> reportParamsMap =(Map<String,String>)this.getSession().getAttribute(filters.get("sessionId")+"_cache");
			pageBo = projectFileService.searchSBProject(userId,pageBo,projectVo,filters,reportParamsMap);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.searchFileProject method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		if (null == list) {
			list = new ArrayList<Map<String, Object>>();
		}
		// 封装到datagrid中让easyui控件渲染
		Datagrid<Map<String, Object>> dg = new Datagrid<Map<String, Object>>(pageBo.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;
	
	}
	
	public String showXDProject(){
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage); 
		if (paramsMap!=null) {
			//竣工年份
			projectVo.setPlanEndYear1(paramsMap.get("planEndYear1"));
			projectVo.setPlanEndYear2(paramsMap.get("planEndYear2"));
			//中央预算内申请规模
			projectVo.setCupApplyBudCaptial1(paramsMap.get("centerApplyScaleStart"));
			projectVo.setCupApplyBudCaptial2(paramsMap.get("centerApplyScaleEnd"));
			//专项申请规模
			projectVo.setCupApplySpeCaptial1(paramsMap.get("specialApplyScaleStart"));
			projectVo.setCupApplySpeCaptial2(paramsMap.get("specialApplyScaleEnd"));
			//中央预算内需求规模
			projectVo.setCupNeedBudCaptial1(paramsMap.get("centerDemandScaleStart"));
			projectVo.setCupNeedBudCaptial2(paramsMap.get("centerDemandScaleEnd"));
			//专项需求规模
			projectVo.setCupNeedSpeCaptial1(paramsMap.get("specialDemandScaleStart"));
			projectVo.setCupNeedSpeCaptial2(paramsMap.get("specialDemandScaleEnd"));
			//开工年份
			projectVo.setPlanStartYear1(paramsMap.get("planStartYear1"));
			projectVo.setPlanStartYear2(paramsMap.get("planStartYear2"));
			//开工月份
			projectVo.setPlanStartMonth1(paramsMap.get("startMonth1"));
			projectVo.setPlanStartMonth2(paramsMap.get("startMonth2"));
			//所属行业
			projectVo.setIndustryCode(paramsMap.get("industryCode"));
			//建设地点
			projectVo.setProjectRegion(paramsMap.get("projectRegion"));
			//是否专项建设
			projectVo.setIsSpecFunds(paramsMap.get("isSpecFunds"));
			//项目类型
			projectVo.setCheckLevel(paramsMap.get("checkLevel"));
			//政府投资方向
			projectVo.setFitIndPolicyCode(paramsMap.get("fitIndPolicyCode"));
			//是否ppp
			projectVo.setIsPpp(paramsMap.get("isPpp"));
			//项目名称
			projectVo.setProjectName(paramsMap.get("projectName"));
			//标签id
			projectVo.setLabelId(paramsMap.get("labelId"));
			//项目申报日期
			projectVo.setProjectApplyTime1(paramsMap.get("projectApplyTime1"));
			projectVo.setProjectApplyTime2(paramsMap.get("projectApplyTime2"));
			//总投资
			projectVo.setAllCaptial1(paramsMap.get("allCaptial1"));
			projectVo.setAllCaptial2(paramsMap.get("allCaptial2"));
		}
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null&&null!=user.get("employee_guid")){
			userId = user.get("employee_guid").toString();
		}
		long start =System.currentTimeMillis();
		try {
			Map<String,String> reportParamsMap =(Map<String,String>)this.getSession().getAttribute(filters.get("sessionId")+"_cache");
			pageBo = projectFileService.searchXDProject(userId,pageBo,projectVo,filters,reportParamsMap);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.searchFileProject method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		if (null == list) {
			list = new ArrayList<Map<String, Object>>();
		}
		// 封装到datagrid中让easyui控件渲染
		Datagrid<Map<String, Object>> dg = new Datagrid<Map<String, Object>>(pageBo.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;
	
	}
	
	public String showDDProject(){
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage); 
		if (paramsMap!=null) {
			//竣工年份
			projectVo.setPlanEndYear1(paramsMap.get("planEndYear1"));
			projectVo.setPlanEndYear2(paramsMap.get("planEndYear2"));
			//中央预算内申请规模
			projectVo.setCupApplyBudCaptial1(paramsMap.get("centerApplyScaleStart"));
			projectVo.setCupApplyBudCaptial2(paramsMap.get("centerApplyScaleEnd"));
			//专项申请规模
			projectVo.setCupApplySpeCaptial1(paramsMap.get("specialApplyScaleStart"));
			projectVo.setCupApplySpeCaptial2(paramsMap.get("specialApplyScaleEnd"));
			//中央预算内需求规模
			projectVo.setCupNeedBudCaptial1(paramsMap.get("centerDemandScaleStart"));
			projectVo.setCupNeedBudCaptial2(paramsMap.get("centerDemandScaleEnd"));
			//专项需求规模
			projectVo.setCupNeedSpeCaptial1(paramsMap.get("specialDemandScaleStart"));
			projectVo.setCupNeedSpeCaptial2(paramsMap.get("specialDemandScaleEnd"));
			//开工年份
			projectVo.setPlanStartYear1(paramsMap.get("planStartYear1"));
			projectVo.setPlanStartYear2(paramsMap.get("planStartYear2"));
			//开工月份
			projectVo.setPlanStartMonth1(paramsMap.get("startMonth1"));
			projectVo.setPlanStartMonth2(paramsMap.get("startMonth2"));
			//所属行业
			projectVo.setIndustryCode(paramsMap.get("industryCode"));
			//建设地点
			projectVo.setProjectRegion(paramsMap.get("projectRegion"));
			//是否专项建设
			projectVo.setIsSpecFunds(paramsMap.get("isSpecFunds"));
			//项目类型
			projectVo.setCheckLevel(paramsMap.get("checkLevel"));
			//政府投资方向
			projectVo.setFitIndPolicyCode(paramsMap.get("fitIndPolicyCode"));
			//是否ppp
			projectVo.setIsPpp(paramsMap.get("isPpp"));
			//项目名称
			projectVo.setProjectName(paramsMap.get("projectName"));
			//标签id
			projectVo.setLabelId(paramsMap.get("labelId"));
			//项目申报日期
			projectVo.setProjectApplyTime1(paramsMap.get("projectApplyTime1"));
			projectVo.setProjectApplyTime2(paramsMap.get("projectApplyTime2"));
			//总投资
			projectVo.setAllCaptial1(paramsMap.get("allCaptial1"));
			projectVo.setAllCaptial2(paramsMap.get("allCaptial2"));
		}
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null&&null!=user.get("employee_guid")){
			userId = user.get("employee_guid").toString();
		}
		long start =System.currentTimeMillis();
		try {
			Map<String,String> reportParamsMap =(Map<String,String>)this.getSession().getAttribute(filters.get("sessionId")+"_cache");
			pageBo = projectFileService.searchDDProject(userId,pageBo,projectVo,filters,reportParamsMap);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.searchFileProject method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		if (null == list) {
			list = new ArrayList<Map<String, Object>>();
		}
		// 封装到datagrid中让easyui控件渲染
		Datagrid<Map<String, Object>> dg = new Datagrid<Map<String, Object>>(pageBo.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;
	
	}
	
	/**
	 *
	 * @orderBy
	 * @return
	 * @author tanghw
	 * @Date 2016年10月29日下午7:31:49
	 */
	public String showNvProject(){
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage);
		if (paramsMap!=null) {
			//所属行业
			projectVo.setIndustryCode(paramsMap.get("industryCode"));
			//国标行业
			projectVo.setGbIndustryCode(paramsMap.get("gbIndustryCode"));
			//建设地点
			projectVo.setProjectRegion(paramsMap.get("projectRegion"));
			//项目类型
			projectVo.setCheckLevel(paramsMap.get("checkLevel"));
			//项目名称
			projectVo.setProjectName(paramsMap.get("projectName"));
			//总投资
			projectVo.setAllCaptial1(paramsMap.get("allCaptial1"));
			projectVo.setAllCaptial2(paramsMap.get("allCaptial2"));
			// 项目拟开工年份-区间开始
			projectVo.setPlanStartYear1(paramsMap.get("startYear"));
			// 项目拟开工年份-区间结束
			projectVo.setPlanStartYear2(paramsMap.get("endYear"));
           // 项目阶段
			projectVo.setProStageCode(paramsMap.get("projectStage"));


		}
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
		}
		long start =System.currentTimeMillis();
		try {
			Map<String,String> reportParamsMap =(Map<String,String>)this.getSession().getAttribute(filters.get("sessionId")+"_cache");
			if(StringUtils.isNotEmpty(filters.get("builPlaceCodeMap"))){
				String builPlaceMap  = URLDecoder.decode(filters.get("builPlaceCodeMap"), "UTF-8");
				String builPlaceCodeMap =builPlaceMap.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					builPlaceCodeMap = new String(builPlaceCodeMap.getBytes("ISO8859-1"),"UTF-8");
				}
				//地图所传地区全路径名称去掉国家
				String builPlaceNameMap = builPlaceCodeMap.replace("中国"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(builPlaceNameMap);
				filters.put("builPlaceCodeMap", code);
			}
			pageBo = projectFileService.searchNVProject(userId,pageBo,projectVo,filters,reportParamsMap);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.searchFileProject method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		if (null == list) {
			list = new ArrayList<Map<String, Object>>();
		}
		// 封装到datagrid中让easyui控件渲染
		Datagrid<Map<String, Object>> dg = new Datagrid<Map<String, Object>>(pageBo.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;

	}
	/**
	 * 获取toolbar搜索框的下拉框树
	 * @orderBy 
	 * @return
	 * @author StrongSoft
	 * @Date 2016年10月30日下午1:31:24s
	 */
	public String getFromCache() {
		//页面下拉框二位指标值对应的PropertiesUtil的key
		String propertiesKey=this.getRequest().getParameter("propertiesKey");
		//根据键值对获取groupNo后获取二维指标表的的所有选项
		List<Map<String, Object>> list = Cache.getByGroupNo(PropertiesUtil.getInfoByName(propertiesKey));
		List<TreeNode> nodes=null;
		if("IndustryExtendCode".equals(propertiesKey)){
			// 递归简称
			nodes= RecursiveData.RecursiveShortValue(list);
		}
		else {
			nodes= RecursiveData.RecursiveDept(list);
		}
		Struts2Utils.renderJson(nodes);
		return null;
	}
	/**
	 *  获取前三个调度项目实施信息
	 * @orderBy 
	 * @return
	 * @author tanghw
	 * @Date 2016年11月2日下午2:19:12
	 */
	public String getDispatchDetails(){
		long start =System.currentTimeMillis();
		List<Map<String, Object>> list = projectFileService.getTopThirdDispatchDetails();
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		//返回数据
		Struts2Utils.renderJson(list);
		return null;
	} 
	public String exportWord() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		//获取相对路径
		String filePath=ServletActionContext.getServletContext().getRealPath("/files/download/");
		String zipPath = filePath+"/";
		//获取Word文件名
		String fileName="项目档案.docx";
		//基本信息
		List<Map<String, Object>> baseList = projectFileService.getBaseInfoByGuid(projectGuid,moduleCode);
		if (null != baseList && baseList.size() > 0) {
			fileName = baseList.get(0).get("PRO_NAME")+"项目档案.docx";	
		}
		String name=fileName;
		long start =System.currentTimeMillis();
		  try{
				File file = new File(zipPath+fileName);
				if (file != null && file.exists()) {
					response.setContentType("text/html;charset=utf-8");
					response.setContentType("application/octet-stream;");
					response.setHeader(
							"Content-disposition",
							"attachment; filename="
									+ new String(name.getBytes("GBK"), "ISO8859-1"));
					response.setHeader("Content-Length",
							String.valueOf(file.length()));
					// 设置缓存
					inputStream = new BufferedInputStream(new FileInputStream(file));
					outputStream = new BufferedOutputStream(
							response.getOutputStream());
					byte[] buff = new byte[2048];
					int bytesRead;
					while (-1 != (bytesRead = inputStream
							.read(buff, 0, buff.length))) {
						outputStream.write(buff, 0, bytesRead);
					}
				} else {
					this.projectFileService.exportWord(projectGuid,moduleCode,zipPath,fileName);
					 file = new File(zipPath+fileName);
					response.setContentType("text/html;charset=utf-8");
					response.setContentType("application/octet-stream;");
					response.setHeader(
							"Content-disposition",
							"attachment; filename="
									+ new String(name.getBytes("GBK"), "ISO8859-1"));
					response.setHeader("Content-Length",
							String.valueOf(file.length()));
					// 设置缓存
					inputStream = new BufferedInputStream(new FileInputStream(file));
					outputStream = new BufferedOutputStream(
							response.getOutputStream());
					byte[] buff = new byte[2048];
					int bytesRead;
					while (-1 != (bytesRead = inputStream
							.read(buff, 0, buff.length))) {
						outputStream.write(buff, 0, bytesRead);
					}
				}
		  }catch (Exception e) {
	        	logger.error(e.getMessage(), e);
		    } finally {
		    	if (inputStream != null) {
		    		inputStream.close();
		    	}
		     if (outputStream != null) {
		    	 outputStream.close();
			 }
	       }
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		//返回数据
//		Struts2Utils.renderJson("1");
		return null;
	} 
	/**
	 * 获取标签树数据
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016年12月17日下午3:37:56
	 */
	public void getCustomLabelsData() throws Exception{
		List<JsonTreeData> treeDataList = new ArrayList<JsonTreeData>();
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
		}
		List<Label> list= labelService.getCustomLabelsData(userId,moduleCode);
		JsonTreeData treeData = new JsonTreeData();
		treeData.setId("all");
		treeData.setPid("");
		treeData.setText("全部");
		treeData.setState("open");
		treeDataList.add(treeData);
		if(list.size() > 0){
			for(Label label : list){
				treeData = new JsonTreeData();
				treeData.setId(label.getId());
				treeData.setPid(label.getParentId());
				treeData.setText(label.getName());
				treeData.setState("open");
				treeDataList.add(treeData);
			}
		}
		List<JsonTreeData> newTreeDataList = TreeNodeUtil.getFatherNode(treeDataList);
		Struts2Utils.renderJson(newTreeDataList);
		
	}
	//自定义标签弹出页
	public String openCustomLabelDialog() throws Exception{
		return "customLabel";
	}
	/**
	 * 保存自定义标签	
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016年12月17日下午6:36:52
	 */
	public void saveCustomerLabel() throws Exception{
		try {
			Label label= new Label();
			String isAdd=labelParams.get("isAdd");
			Boolean isADD = true;
			//新增
			if(isAdd.equals("0")){
				label.setName(labelParams.get("addLabel"));
				label.setParentId(labelParams.get("txtSelectLabel"));
				label.setType(labelParams.get("labelType"));
				//判断标签属于哪个模块
				//为空标签属于哪个模块
				if(StringUtils.isNotBlank(labelParams.get("moduleCode"))){
					label.setModuleCode(labelParams.get("moduleCode"));
				}else{
					label.setModuleCode(Constant.FIVE_PLAN);
				}
			}else{
				//添加已有标签时 父级标签id以存放在label的id上传参
				isADD=false;
				label.setId(labelParams.get("txtSelectLabel"));
			}
			//转换项目id格式
			String ids=labelParams.get("projectids");
			String[] projectGuids = ids.split(",");
			List<String> projects = Arrays.asList(projectGuids);
			@SuppressWarnings("unchecked")
			Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
			String userId = null;
			if(user!=null){
				userId = user.get("employee_guid").toString();
			}
			this.labelService.addRelateUserLabel(userId, label, projects,isADD);
				this.renderText("ok");
		} catch (Exception e) {
			this.renderText(e.getMessage());
		}
	}
	/**
	 * 标签删除	 
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016年12月17日下午6:53:51
	 */
	public void delLabel() throws Exception{
		try {
			String labelID=labelParams.get("labelID");
			this.labelService.deleteLabelById(labelID);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	/**
	 * 标签改名
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016年12月17日下午6:54:50
	 */
	public void changeLabelName() throws Exception{
		try {
			String labelID=labelParams.get("labelID");
			String labelText=labelParams.get("labelText");
			labelService.updateLabelInfoById(labelID, labelText);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	//修改项目标签弹出页
	public String openEditProjectLabelDialog() throws Exception{
		return "editProjectLabels";
	}
	/**
	 * 修改项目标签	
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016年12月17日下午7:19:12
	 */
	@SuppressWarnings("unchecked")
	public void updateProjectLabels() throws Exception{
		try {
			Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
			String userId = null;
			if(user!=null){
				userId = user.get("employee_guid").toString();
			}
			String labelIDs = labelParams.get("labelIDs");
			String[] projectIdAry =labelIDs.split(",");
			List<String> labelIDsList = Arrays.asList(projectIdAry);
			String projectId = labelParams.get("projectID");
			labelService.updateProLabelSByContition(userId, projectId, labelIDsList);
			Struts2Utils.renderText(Constant.RESULT_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Struts2Utils.renderText(Constant.RESULT_FAILURE);
		}
	}
	/**
	 * 根据项目ID获取项目标签
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016年12月17日下午7:31:39
	 */
	public void getPrivateLabelsByProjectID() throws Exception{
		List<JsonTreeData> treeDataList = new ArrayList<JsonTreeData>();
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
		}
		List<Label> list= labelService.getProLabelSByContition(projectId, userId,moduleCode);
		if(list.size() > 0){
			for(Label label : list){
				JsonTreeData treeData = new JsonTreeData();
				treeData.setId(label.getId());
				treeData.setPid(label.getParentId());
				treeData.setText(label.getName());
				treeData.setState("open");
				treeDataList.add(treeData);
			}
		}
		Struts2Utils.renderJson(treeDataList);
		
	}
	/**
	 * 获取全部项目分类下拉信息
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2017年1月10日上午11:03:41
	 */
	public void getCustomLabelsData2() throws Exception{
		List<JsonTreeData> treeDataList = new ArrayList<JsonTreeData>();
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
		}
		List<Label> list= labelService.getCustomLabelsData(userId,moduleCode);
		JsonTreeData treeData = new JsonTreeData();
		if(list.size() > 0){
			for(Label label : list){
				treeData = new JsonTreeData();
				treeData.setId(label.getId());
				treeData.setPid(label.getParentId());
				treeData.setText(label.getName());
				treeData.setState("open");
				treeDataList.add(treeData);
			}
		}
		List<JsonTreeData> newTreeDataList = TreeNodeUtil.getFatherNode(treeDataList);
		Struts2Utils.renderJson(newTreeDataList);
		
	}
	/**
	 * @return the projectGuid
	 */
	public String getProjectGuid() {
		return projectGuid;
	}

	/**
	 * @param projectGuid the projectGuid to set
	 */
	public void setProjectGuid(String projectGuid) {
		this.projectGuid = projectGuid;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}
	public ProjectFileSearchVo getProjectVo() {
		return projectVo;
	}
	public void setProjectVo(ProjectFileSearchVo projectVo) {
		this.projectVo = projectVo;
	}
	public Map<String, String> getParamsMap() {
		return paramsMap;
	}
	public void setParamsMap(Map<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}
	public Map<String, String> getFilters() {
		return filters;
	}
	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}
	public String getInnerPage() {
		return innerPage;
	}
	public void setInnerPage(String innerPage) {
		this.innerPage = innerPage;
	}
	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public Map<String, String> getLabelParams() {
		return labelParams;
	}
	public void setLabelParams(Map<String, String> labelParams) {
		this.labelParams = labelParams;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	
	
	/**
	 * 获取三年滚动计划
	 * @return
	 * @author zpd
	 * @Date 2017-11-03
	 */
	public String showSNProject(){
		Page<Map<String, Object>> pageBo = new Page<Map<String, Object>>(12, true);
		// 当前页
		int intPage = (page == null || page == "0") ? 1 : Integer.parseInt(page);
		// 每页显示条数
		int number = (rows == null || rows == "0") ? 12 : Integer.parseInt(rows);
		pageBo.setPageSize(number);
		pageBo.setPageNo(intPage);
		if (paramsMap!=null) {
			//所属行业
			projectVo.setIndustryCode(paramsMap.get("industryCode"));
			//国标行业
			projectVo.setGbIndustryCode(paramsMap.get("gbIndustryCode"));
			//建设地点
			projectVo.setProjectRegion(paramsMap.get("projectRegion"));
			//项目类型
			projectVo.setCheckLevel(paramsMap.get("checkLevel"));
			//项目名称
			projectVo.setProjectName(paramsMap.get("projectName"));
			//总投资
			projectVo.setAllCaptial1(paramsMap.get("allCaptial1"));
			projectVo.setAllCaptial2(paramsMap.get("allCaptial2"));
			// 项目拟开工年份-区间开始
			projectVo.setPlanStartYear1(paramsMap.get("startYear"));
			// 项目拟开工年份-区间结束
			projectVo.setPlanStartYear2(paramsMap.get("endYear"));
           // 项目阶段
			projectVo.setProStageCode(paramsMap.get("projectStage"));


		}
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
		}
		long start =System.currentTimeMillis();
		try {
			Map<String,String> reportParamsMap =(Map<String,String>)this.getSession().getAttribute(filters.get("sessionId")+"_cache");
			if(StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				String builPlaceMap  = URLDecoder.decode(filters.get("GovernmentCode"), "UTF-8");
				String builPlaceCodeMap =builPlaceMap.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					builPlaceCodeMap = new String(builPlaceCodeMap.getBytes("ISO8859-1"),"UTF-8");
				}
				//地图所传地区全路径名称去掉国家
				String builPlaceNameMap = builPlaceCodeMap.replace("中国"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(builPlaceNameMap);
				filters.put("GovernmentCode", code);
			}
			pageBo = projectFileService.searchSNProject(userId,pageBo,projectVo,filters,reportParamsMap);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		long end =System.currentTimeMillis();
		logger.debug("execute projectFileService.searchFileProject method cost time : "
				+ (end - start) + " mills.");
		List<Map<String, Object>> list = pageBo.getResult();
		System.out.println("execute projectFileService.getTopThirdDispatchDetails method cost time : "
				+ (end - start) + " mills.");
		if (null == list) {
			list = new ArrayList<Map<String, Object>>();
		}
		// 封装到datagrid中让easyui控件渲染
		Datagrid<Map<String, Object>> dg = new Datagrid<Map<String, Object>>(pageBo.getTotalCount(), list);
		// 返回前端JSON
		Struts2Utils.renderJson(dg);
		return null;

	}
	
	/**
	 * 跳转三年滚动计划主页面
	 * @orderBy 
	 * @return
	 * @author zpd
	 * @Date 2017-11-03
	 */
	public String listSNGD() {
		try {
			// 建设地点
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("BuildPlaceCode"))){
				String builPlaceCode =filters.get("BuildPlaceCode").trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					builPlaceCode = new String(builPlaceCode.getBytes("ISO8859-1"),"UTF-8");
				}				
				//地图所传地区全路径名称去掉国家
				String builPlaceName = builPlaceCode.replace("中国"+Constant.BI_SPLIT,"");
				String builPlaceChineseName = builPlaceName.replace(Constant.BI_SPLIT, "-");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(builPlaceName);
				filters.put("builPlaceCode", code);
				filters.put("builPlaceChineseName", builPlaceChineseName);
			}
			
			// 国标行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("GovernmentCode"))){
				String GBIndustry  = URLDecoder.decode(filters.get("GovernmentCode"), "UTF-8");
				String GBIndustryName =GBIndustry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					GBIndustryName = new String(GBIndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传国标行业全路径名称去掉顶级
				GBIndustryName = GBIndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(GBIndustryName);
				filters.put("GovernmentCode", code);
			}
			// 委内行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("IndustryCode"))){
				String Industry  = URLDecoder.decode(filters.get("IndustryCode"), "UTF-8");
				String IndustryName =Industry.trim().toString();
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					IndustryName = new String(IndustryName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				IndustryName = IndustryName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(IndustryName);
				filters.put("IndustryCode", code);
			}
			// 申报部门
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("startYear"))){
				String startYear  = URLDecoder.decode(filters.get("startYear"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					startYear = new String(startYear.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				startYear = startYear.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("startYear", startYear);
			}
			// 储备层级
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			// 五年储备入库趋势
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			// 项目类型
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectType"))){
				String projectType  = URLDecoder.decode(filters.get("projectType"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectType = new String(projectType.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectType = projectType.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectType", projectType);
			}
			//国标投资方向
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("goverInvestDir"))){
				String goverInvestDir  = URLDecoder.decode(filters.get("goverInvestDir"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					goverInvestDir = new String(goverInvestDir.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				goverInvestDir = goverInvestDir.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(goverInvestDir);;
				if(StringUtils.isEmpty(code)){
					for(int i=0; ;i++){
					goverInvestDir =StringUtils.substringBeforeLast(goverInvestDir,">>");
				    code=Cache.getCodeByFullName(goverInvestDir);
					    if(StringUtils.isNotEmpty(code)){
					    	break;
					    }
					}
				}
				filters.put("goverInvestDir", code);
			}
			//选择的指标名称
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("index"))){
				String index  = URLDecoder.decode(filters.get("index"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					index = new String(index.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				index = index.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				//String code=Cache.getCodeByFullName(goverInvestDir);
				filters.put("index", index);
			}
			//项目成熟度
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectMaturity"))){
				String projectMaturity  = URLDecoder.decode(filters.get("projectMaturity"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectMaturity = new String(projectMaturity.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectMaturity = projectMaturity.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
//				String code=Cache.getCodeByFullName(projectMaturity);
				filters.put("projectMaturity", projectMaturity);
			}
			//项目类型名称
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("projectTypeName"))){
				String projectTypeName  = URLDecoder.decode(filters.get("projectTypeName"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					projectTypeName = new String(projectTypeName.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				projectTypeName = projectTypeName.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				filters.put("projectTypeName", projectTypeName);
			}
			//一级国标行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("gbIndustry"))){
				String gbIndustry  = URLDecoder.decode(filters.get("gbIndustry"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					gbIndustry = new String(gbIndustry.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				gbIndustry = gbIndustry.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
//				if(gbIndustry.startsWith("其他")){
//					code="1";
//			     }else if(gbIndustry.endsWith("其他")){
//						code =StringUtils.substringBeforeLast(gbIndustry,">>");
//						code=Cache.getCodeByFullName(code);
//				} 
//				else{
//			       code=Cache.getCodeByFullName(gbIndustry);
//				}
				String code=Cache.getCodeByFullName(gbIndustry);
				if(StringUtils.isEmpty(code)){
					for(int i=0; ;i++){
						gbIndustry =StringUtils.substringBeforeLast(gbIndustry,">>");
				    code=Cache.getCodeByFullName(gbIndustry);
					    if(StringUtils.isNotEmpty(code)){
					    	break;
					    }
					}
				}
				filters.put("gbIndustry", code);
			}
			//重大战略名称
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("majorStrategic"))){
				String majorStrategic  = URLDecoder.decode(filters.get("majorStrategic"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					majorStrategic = new String(majorStrategic.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				majorStrategic = majorStrategic.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
				String code=Cache.getCodeByFullName(majorStrategic);
				filters.put("majorStrategic", code);
			}
			//委内行业
			if(!filters.isEmpty()&&StringUtils.isNotEmpty(filters.get("wnIndustry"))){
				String wnIndustry  = URLDecoder.decode(filters.get("wnIndustry"), "UTF-8");
				String str = System.getProperty("file.encoding");
				if(!Constant.CODE.equalsIgnoreCase(str)){
					wnIndustry = new String(wnIndustry.getBytes("ISO8859-1"),"UTF-8");
				}
				//所传委内行业全路径名称去掉顶级
				wnIndustry = wnIndustry.replace("顶层"+Constant.BI_SPLIT,"");
				// 根据全路径名称获取地区code
			/*	String code=null;
				if(wnIndustry.startsWith("其他")){
					code="1";
			     }else if(wnIndustry.endsWith("其他")){
						code =StringUtils.substringBeforeLast(wnIndustry,">>");
						code=Cache.getCodeByFullName(code);
					} 
				else{
			       code=Cache.getCodeByFullName(wnIndustry);
				}*/
				String code=Cache.getCodeByFullName(wnIndustry);
				if(StringUtils.isEmpty(code)){
					for(int i=0; ;i++){
					wnIndustry =StringUtils.substringBeforeLast(wnIndustry,">>");
				    code=Cache.getCodeByFullName(wnIndustry);
					    if(StringUtils.isNotEmpty(code)){
					    	break;
					    }
					}
				}
				filters.put("wnIndustry", code);
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "listSNGD";
	}
}
