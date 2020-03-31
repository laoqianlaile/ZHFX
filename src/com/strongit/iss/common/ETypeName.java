package com.strongit.iss.common;

/**
 * @author XiaXiang
 * 总览类型枚举
 */
public enum ETypeName {
	/**项目库总览**/
	DEFVIEW("defView", "总览"),
	/**五年规划储备**/
	FIVEPLAN("fivePlan", "五年规划项目储备"),
	/**三年滚动计划**/
	ROLLPLAN("rollPlan", "三年滚动投资计划"),
	/**审核备办理**/
	PROJECTDEAL("projectDeal", "项目审核备办理"),
	/**项目办理**/
	PROJECTVIEW("projectView", "项目办理"),
	/**目录办理**/
	CONTENTVIEW("contentView", "目录概况"),
	/**事项办理**/
	ITEMVIEW("itemView", "事项办理"),
	/**年度计划申报**/
	YEARREPORT("yearReport", "年度计划申报"),
	/**年度计划下达**/
	YEARISSUED("yearIssued", "年度计划下达"),
	/**年度计划调度**/
	YEARDISPATCH("yearDispatch", "年度计划调度"),
	/**专项建设基金**/
	FUND("fund", "专项建设基金项目"),
	/**项目档案**/
	PROJECTFILE("projectFile", "项目档案"),
	/**审核备项目信息趋势情况**/
	APPROVALRECORD("approvalRecord", "审核备项目信息趋势情况"),
	/**审核备项目信息分维度汇总**/
	DISTRIBUTIONSITUATION("distributionSituation", "审核备项目信息分维度汇总"),
	/**简政放权**/
	RELEASE("release", "简政放权"),
	/**放管结合**/
	MANAGE("manage", "放管结合"),
	/**优化服务**/
	OPTIMIZATION("optimization", "优化服务"),
	/**政府投资精细化管理**/
	FINEMANAGEMENT("fineManagement", "政府投资精细化管理"),
	/**预测预警分析**/
	FORECAST("forecast", "预测预警分析"),
	/**项目实施进展监测预警分析**/
	PROJECTIMPLEMENT("projectImplement", "项目实施进展监测预警分析"),
	/**项目办理进展监测预警分析**/
	PROJECTHANDLE("projectHandle", "项目办理进展监测预警分析"),
	/**用户分布情况**/
	USER("user", "用户分布情况"),
	/**综合分析报告**/
	REPORT("report", "综合分析报告");
	
	private String code ;
	
	private String description;
	
	public String getCode() {
		return this.code;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	private ETypeName(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public static ETypeName getByCode(String code) {
		for(ETypeName _enum : ETypeName.values()){
			if(_enum.getCode().equals(code)){
				return _enum;
			}
		}
		return null;
	}
}
