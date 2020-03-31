package com.strongit.iss.neuentity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="TZXMZH.FA_XMXX")
public class ProjectFileInfo2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; // 数据唯一标识
	private String projectCode; // 项目编号
	private String projectName; // 项目名称
	private String projectType; // 项目类型
	private String projectRegion; // 建设地点
	private String industryCode; // 所属行业
	private Long investmentTotal; // 总投资
	private String expectStartyear; // 拟开工年份

	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid.hex")
	@Column(name = "ID", nullable = false, unique = true, length = 100)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PROJECT_CODE", updatable = false, insertable = false)
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	@Column(name = "PROJECT_NAME", updatable = false, insertable = false)
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Column(name = "PROJECT_TYPE", updatable = false, insertable = false)
	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	

	@Column(name = "INDUSTRY", updatable = false, insertable = false)
	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	@Column(name = "TOTAL_MONEY", updatable = false, insertable = false)
	public Long getInvestmentTotal() {
		return investmentTotal;
	}

	public void setInvestmentTotal(Long investmentTotal) {
		this.investmentTotal = investmentTotal;
	}

	@Column(name = "START_YEAR", updatable = false, insertable = false)
	public String getExpectStartyear() {
		return expectStartyear;
	}

	public void setExpectStartyear(String expectStartyear) {
		this.expectStartyear = expectStartyear;
	}

	@Column(name = "PLACE_CODE", updatable = false, insertable = false)
	public String getProjectRegion() {
		
		return projectRegion;
	}

	public void setProjectRegion(String projectRegion) {
		this.projectRegion = projectRegion;
	}

}
