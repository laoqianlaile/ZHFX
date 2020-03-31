package com.strongit.iss.entity;

import java.io.Serializable;

/**
 * 项目列表查询vo实体
 * @author xiangyong
 */
public class ProjectFileSearchVo implements Serializable {
        private String planStartYear1 ;     //拟开工年份1（查询时第一个时间）
		private String planStartYear2 ;     //拟开工年份2（查询时第二个时间）下同
		private String planStartMonth1 ;    //拟开工月份1
		private String planStartMonth2 ;    //拟开工月份2
		private String planEndYear1 ;       //拟竣工年份1
		private String planEndYear2 ;       //拟竣工年份2
		private String projectName ;        //项目名称
		private String fitIndPolicyCode;    //政府投资方向
		private String projectRegion;       //建设地点
		private String industryCode;        //所属行业
	    private String gbIndustryCode;     //国标行业
		private String projectImplePro;     //项目实施进展
		private String isPpp;               //是否ppp
		private String isSpecFunds;         //是否专项建设基金
		private String projectApplyTime1;   //项目申报日期1
		private String projectApplyTime2;   //项目申报日期2
		private String yearPlanRepTime1;    //年度计划申报日期1
		private String yearPlanRepTime2;    //年度计划申报日期2
		private String specialRepTime1;     //专项建设基金申报日期1
		private String specialRepTime2;     //专项建设基金申报日期2
		private String  allCaptial1;        //总投资规模1
		private String  allCaptial2;        //总投资规模2
		private String cupApplyBudCaptial1; //中央预算内申请规模1
		private String cupApplyBudCaptial2; //中央预算内申请规模2
		private String cupApplySpeCaptial1; //专项建设基金申请规模1
		private String cupApplySpeCaptial2; //专项建设基金申请规模2
		private String cupNeedSpeCaptial1;  //专项建设基金需求规模1
		private String cupNeedSpeCaptial2;  //专项建设基金需求规模2
		private String cupNeedBudCaptial1;  //中央预算内需求规模1
		private String cupNeedBudCaptial2;  //中央预算内需求规模2
		private String checkLevel;          //审批阶段
		private String projectStatus;       //项目状态
		private String labelId;       //标签id
	    private String proStageCode;// 项目阶段
		
		public String getPlanStartYear1() {
			return planStartYear1;
		}
		public void setPlanStartYear1(String planStartYear1) {
			this.planStartYear1 = planStartYear1;
		}
		public String getPlanStartYear2() {
			return planStartYear2;
		}
		public void setPlanStartYear2(String planStartYear2) {
			this.planStartYear2 = planStartYear2;
		}
		public String getPlanStartMonth1() {
			return planStartMonth1;
		}
		public void setPlanStartMonth1(String planStartMonth1) {
			this.planStartMonth1 = planStartMonth1;
		}
		public String getPlanStartMonth2() {
			return planStartMonth2;
		}
		public void setPlanStartMonth2(String planStartMonth2) {
			this.planStartMonth2 = planStartMonth2;
		}
		public String getPlanEndYear1() {
			return planEndYear1;
		}
		public void setPlanEndYear1(String planEndYear1) {
			this.planEndYear1 = planEndYear1;
		}
		public String getPlanEndYear2() {
			return planEndYear2;
		}
		public void setPlanEndYear2(String planEndYear2) {
			this.planEndYear2 = planEndYear2;
		}
		public String getProjectName() {
			return projectName;
		}
		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}
		public String getFitIndPolicyCode() {
			return fitIndPolicyCode;
		}
		public void setFitIndPolicyCode(String fitIndPolicyCode) {
			this.fitIndPolicyCode = fitIndPolicyCode;
		}
		public String getProjectRegion() {
			return projectRegion;
		}
		public void setProjectRegion(String projectRegion) {
			this.projectRegion = projectRegion;
		}
		public String getIndustryCode() {
			return industryCode;
		}
		public void setIndustryCode(String industryCode) {
			this.industryCode = industryCode;
		}
		public String getProjectImplePro() {
			return projectImplePro;
		}
		public void setProjectImplePro(String projectImplePro) {
			this.projectImplePro = projectImplePro;
		}
		public String getIsPpp() {
			return isPpp;
		}
		public void setIsPpp(String isPpp) {
			this.isPpp = isPpp;
		}
		public String getIsSpecFunds() {
			return isSpecFunds;
		}
		public void setIsSpecFunds(String isSpecFunds) {
			this.isSpecFunds = isSpecFunds;
		}
		public String getProjectApplyTime1() {
			return projectApplyTime1;
		}
		public void setProjectApplyTime1(String projectApplyTime1) {
			this.projectApplyTime1 = projectApplyTime1;
		}
		public String getProjectApplyTime2() {
			return projectApplyTime2;
		}
		public void setProjectApplyTime2(String projectApplyTime2) {
			this.projectApplyTime2 = projectApplyTime2;
		}
		public String getYearPlanRepTime1() {
			return yearPlanRepTime1;
		}
		public void setYearPlanRepTime1(String yearPlanRepTime1) {
			this.yearPlanRepTime1 = yearPlanRepTime1;
		}
		public String getYearPlanRepTime2() {
			return yearPlanRepTime2;
		}
		public void setYearPlanRepTime2(String yearPlanRepTime2) {
			this.yearPlanRepTime2 = yearPlanRepTime2;
		}
		public String getSpecialRepTime1() {
			return specialRepTime1;
		}
		public void setSpecialRepTime1(String specialRepTime1) {
			this.specialRepTime1 = specialRepTime1;
		}
		public String getSpecialRepTime2() {
			return specialRepTime2;
		}
		public void setSpecialRepTime2(String specialRepTime2) {
			this.specialRepTime2 = specialRepTime2;
		}
		public String getAllCaptial1() {
			return allCaptial1;
		}
		public void setAllCaptial1(String allCaptial1) {
			this.allCaptial1 = allCaptial1;
		}
		public String getAllCaptial2() {
			return allCaptial2;
		}
		public void setAllCaptial2(String allCaptial2) {
			this.allCaptial2 = allCaptial2;
		}
		public String getCupApplyBudCaptial1() {
			return cupApplyBudCaptial1;
		}
		public void setCupApplyBudCaptial1(String cupApplyBudCaptial1) {
			this.cupApplyBudCaptial1 = cupApplyBudCaptial1;
		}
		public String getCupApplyBudCaptial2() {
			return cupApplyBudCaptial2;
		}
		public void setCupApplyBudCaptial2(String cupApplyBudCaptial2) {
			this.cupApplyBudCaptial2 = cupApplyBudCaptial2;
		}
		public String getCupApplySpeCaptial1() {
			return cupApplySpeCaptial1;
		}
		public void setCupApplySpeCaptial1(String cupApplySpeCaptial1) {
			this.cupApplySpeCaptial1 = cupApplySpeCaptial1;
		}
		public String getCupApplySpeCaptial2() {
			return cupApplySpeCaptial2;
		}
		public void setCupApplySpeCaptial2(String cupApplySpeCaptial2) {
			this.cupApplySpeCaptial2 = cupApplySpeCaptial2;
		}
		public String getCupNeedSpeCaptial1() {
			return cupNeedSpeCaptial1;
		}
		public void setCupNeedSpeCaptial1(String cupNeedSpeCaptial1) {
			this.cupNeedSpeCaptial1 = cupNeedSpeCaptial1;
		}
		public String getCupNeedSpeCaptial2() {
			return cupNeedSpeCaptial2;
		}
		public void setCupNeedSpeCaptial2(String cupNeedSpeCaptial2) {
			this.cupNeedSpeCaptial2 = cupNeedSpeCaptial2;
		}
		public String getCupNeedBudCaptial1() {
			return cupNeedBudCaptial1;
		}
		public void setCupNeedBudCaptial1(String cupNeedBudCaptial1) {
			this.cupNeedBudCaptial1 = cupNeedBudCaptial1;
		}
		public String getCupNeedBudCaptial2() {
			return cupNeedBudCaptial2;
		}
		public void setCupNeedBudCaptial2(String cupNeedBudCaptial2) {
			this.cupNeedBudCaptial2 = cupNeedBudCaptial2;
		}
		public String getCheckLevel() {
			return checkLevel;
		}
		public void setCheckLevel(String checkLevel) {
			this.checkLevel = checkLevel;
		}
		public String getProjectStatus() {
			return projectStatus;
		}
		public void setProjectStatus(String projectStatus) {
			this.projectStatus = projectStatus;
		}
		public String getLabelId() {
			return labelId;
		}
		public void setLabelId(String labelId) {
			this.labelId = labelId;
		}
		public String getProStageCode() {
			return proStageCode;
		}

		public void setProStageCode(String proStageCode) {
			this.proStageCode = proStageCode;
		}

	public String getGbIndustryCode() {
		return gbIndustryCode;
	}

	public void setGbIndustryCode(String gbIndustryCode) {
		this.gbIndustryCode = gbIndustryCode;
	}
}