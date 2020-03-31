package com.strongit.iss.orm.hibernate;


public class ProjectPage<T> extends Page<T> {
	public ProjectPage(int pageSize) {
		this.pageSize = pageSize;
	}

	public ProjectPage(int pageSize, boolean autoCount) {
		this.pageSize = pageSize;
		this.autoCount = autoCount;
	}
	//总投资
	private Double totalInvestment;

	public Double getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(Double totalInvestment) {
		this.totalInvestment = totalInvestment;
	}
	
	//累计分解投资
	private Double decomposeInvest;

	public Double getDecomposeInvest() {
		return decomposeInvest;
	}

	public void setDecomposeInvest(Double decomposeInvest) {
		this.decomposeInvest = decomposeInvest;
	}
	
	//“待分解资金”（取主项目的下达资金）
	private Double bundPlanInvest;

	public Double getBundPlanInvest() {
		return bundPlanInvest;
	}

	public void setBundPlanInvest(Double bundPlanInvest) {
		this.bundPlanInvest = bundPlanInvest;
	}
	

}