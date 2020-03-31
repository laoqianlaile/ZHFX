/**
 * @author XiaXiang
 * @Date 2016年10月9日下午1:11:02
 */
package com.strongit.iss.neuentity;


/**
 * @author XiaXiang
 *
 */
public class ProjectInfo2 {
	private String id; // 数据唯一标识
	private String projectCode; // 项目编号
	private String projectName; // 项目名称
	private String projectType; // 项目类型
	private String projectRegion; // 建设地点
	private String industryCode; // 所属行业
	private Long investmentTotal; // 总投资
	private String expectStartyear; // 拟开工年份
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
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
	public Long getInvestmentTotal() {
		return investmentTotal;
	}
	public void setInvestmentTotal(Long investmentTotal) {
		this.investmentTotal = investmentTotal;
	}
	public String getExpectStartyear() {
		return expectStartyear;
	}
	public void setExpectStartyear(String expectStartyear) {
		this.expectStartyear = expectStartyear;
	}
	
}
