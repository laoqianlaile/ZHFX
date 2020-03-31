package com.strongit.iss.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 预警信息表
 * @author tanghw
 *
 */
@Entity
@Table(name = "early_warning")
public class EarlyWarning implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 预警分类(下拉)
	 */
	private String warningType;
	/**
	 * 指标名称（文本）
	 */
	private String warningName;
	/**
	 * 计算公式说明
	 */
	private String calculationFormula;
	/**
	 * 是否预警指标
	 */
	private String iswarning;
	/**
	 * 预警值
	 */
	private String warningValue;
	/**
	 * 预警等级（下拉）
	 */
	private String warningLevel;
	/**
	 * 预警区间（开始值）
	 */
	private String statrWarningValue;
	/**
	 * 预警区间（结束值）
	 */
	private String endWorningValue;
	/**
	 * 创建用户ID
	 */
	private String createUser;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新用户ID
	 */
	private String updateUser;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 排序
	 */
	private Long sort;
	
	
	/**
	 * 获取get和set方法
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日上午11:17:29
	 */
	@Id
	@GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy="uuid.hex")
	@Column(name="id", nullable=false, unique=true, length=32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "warning_type")
	public String getWarningType() {
		return warningType;
	}
	public void setWarningType(String warningType) {
		this.warningType = warningType;
	}
	@Column(name = "warning_name")
	public String getWarningName() {
		return warningName;
	}
	public void setWarningName(String warningName) {
		this.warningName = warningName;
	}
	@Column(name = "calculation_formula")
	public String getCalculationFormula() {
		return calculationFormula;
	}
	public void setCalculationFormula(String calculationFormula) {
		this.calculationFormula = calculationFormula;
	}
	@Column(name = "iswarning")
	public String getIswarning() {
		return iswarning;
	}
	public void setIswarning(String iswarning) {
		this.iswarning = iswarning;
	}
	@Column(name = "warning_value")
	public String getWarningValue() {
		return warningValue;
	}
	public void setWarningValue(String warningValue) {
		this.warningValue = warningValue;
	}
	@Column(name = "warning_level")
	public String getWarningLevel() {
		return warningLevel;
	}
	public void setWarningLevel(String warningLevel) {
		this.warningLevel = warningLevel;
	}
	@Column(name = "statr_warning_value")
	public String getStatrWarningValue() {
		return statrWarningValue;
	}
	public void setStatrWarningValue(String statrWarningValue) {
		this.statrWarningValue = statrWarningValue;
	}
	@Column(name = "end_worning_value")
	public String getEndWorningValue() {
		return endWorningValue;
	}
	public void setEndWorningValue(String endWorningValue) {
		this.endWorningValue = endWorningValue;
	}
	@Column(name = "CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "UPDATE_USER")
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "SORT")
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
}
