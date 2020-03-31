package com.strongit.iss.entity;
import java.io.Serializable;

public class SuperMapGovMentBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     *  年度计划项目ID
     */
	private String yearPlanProjectId;
	/**
     *  项目项目编码
     */
	private String proCodeCountry;
	/**
     *  项目名称
     */
	private String proName;
   

	/**
     * 项目类型
     */
    private  String proType;

    /**
     *  建设地点
     */   
    private String buildPlace;
    /**
     * 总投资
     */
    private String investmentTotal;
    /**
     * 委内行业
     */
    private String industry;
    /**
     * 开工时间
     */
    private String actualStartTime;
    /**
     * 竣工时间
     */
    private String actualEndTime;
    

	public SuperMapGovMentBean() {
		super();
	}
	

	



	/**
	 * 政府精细化管理
	 * @orderBy 
	 * @return
	 * @author 周朋
	 * @Date 2016年12月19日下午3:38:51
	 */
	public SuperMapGovMentBean(String yearPlanProjectId, String proCodeCountry, String proName, String proType,
			String buildPlace, String investmentTotal, String industry,
			String actualStartTime, String actualEndTime) {
		super();
		this.yearPlanProjectId = yearPlanProjectId;
		this.proCodeCountry = proCodeCountry;
		this.proName = proName;
		this.proType = proType;
		this.buildPlace = buildPlace;
		this.investmentTotal = investmentTotal;
		this.industry = industry;
		this.actualStartTime = actualStartTime;
		this.actualEndTime = actualEndTime;
	}

	 	public String getYearPlanProjectId() {
			return yearPlanProjectId;
		}

		public void setYearPlanProjectId(String yearPlanProjectId) {
			this.yearPlanProjectId = yearPlanProjectId;
		}
	
	    public String getProCodeCountry() {
			return proCodeCountry;
		}

		public void setProCodeCountry(String proCodeCountry) {
			this.proCodeCountry = proCodeCountry;
		}
		
		public String getProName() {
			return proName;
		}

		public void setProName(String proName) {
			this.proName = proName;
		}

		public String getProType() {
			return proType;
		}

		public void setProType(String proType) {
			this.proType = proType;
		}

		public String getBuildPlace() {
			return buildPlace;
		}

		public void setBuildPlace(String buildPlace) {
			this.buildPlace = buildPlace;
		}

		public String getInvestmentTotal() {
			return investmentTotal;
		}

		public void setInvestmentTotal(String investmentTotal) {
			this.investmentTotal = investmentTotal;
		}

		public String getIndustry() {
			return industry;
		}

		public void setIndustry(String industry) {
			this.industry = industry;
		}

		public String getActualStartTime() {
			return actualStartTime;
		}

		public void setActualStartTime(String actualStartTime) {
			this.actualStartTime = actualStartTime;
		}

		public String getActualEndTime() {
			return actualEndTime;
		}

		public void setActualEndTime(String actualEndTime) {
			this.actualEndTime = actualEndTime;
		}
	
}
