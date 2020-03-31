package com.strongit.iss.entity;

import java.io.Serializable;

import com.strongit.iss.common.Cache;

public class ViewList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String groupNo="";

	//名称
	private String name;
	//五年记录数
	private String fivePlanCount;
	//五年资金
	private String fivePlanInvest;
	//滚动项目个数
	private String rollPlanCount;
	//滚动资金
	private String rollPlanInvest;
	//中央项目个数
	private String budgetCount;
	//中央资金
	private String budgetInvest;
	//专项项目个数
	private String fundCount;
	//专项资金
	private String fundInvest;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = Cache.getNameByCode(groupNo, name);
	}
	public String getFivePlanCount() {
		return fivePlanCount;
	}
	public void setFivePlanCount(String fivePlanCount) {
		this.fivePlanCount = fivePlanCount;
	}
	public String getFivePlanInvest() {
		return fivePlanInvest;
	}
	public void setFivePlanInvest(String fivePlanInvest) {
		this.fivePlanInvest = fivePlanInvest;
	}
	public String getRollPlanCount() {
		return rollPlanCount;
	}
	public void setRollPlanCount(String rollPlanCount) {
		this.rollPlanCount = rollPlanCount;
	}
	public String getRollPlanInvest() {
		return rollPlanInvest;
	}
	public void setRollPlanInvest(String rollPlanInvest) {
		this.rollPlanInvest = rollPlanInvest;
	}
	public String getBudgetCount() {
		return budgetCount;
	}
	public void setBudgetCount(String budgetCount) {
		this.budgetCount = budgetCount;
	}
	public String getBudgetInvest() {
		return budgetInvest;
	}
	public void setBudgetInvest(String budgetInvest) {
		this.budgetInvest = budgetInvest;
	}
	public String getFundCount() {
		return fundCount;
	}
	public void setFundCount(String fundCount) {
		this.fundCount = fundCount;
	}
	public String getFundInvest() {
		return fundInvest;
	}
	public void setFundInvest(String fundInvest) {
		this.fundInvest = fundInvest;
	}
	
}
