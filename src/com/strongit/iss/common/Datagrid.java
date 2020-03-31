package com.strongit.iss.common;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * easyui分页组件datagrid、combogrid数据模型.
 * 
 * @author 尔演&Eryan eryanwcp@gmail.com
 * @date 2012-10-16 上午9:57:59
 */
@SuppressWarnings("serial")
public class Datagrid<T> implements Serializable {
	/**
	 * 总记录数
	 */
	private long total;
	
	/** 针对项目统计总投资 */
	private double totalInvestment;
	/**
	 * 动态列
	 */
	private List<Column> columns = Lists.newArrayList();
	/**
	 * 列表行
	 */
	private List<T> rows;
	/**
	 * 脚列表
	 */
	private List<Map<String, Object>> footer;
	
	/**
	 * 脚显示一条信息
	 */
	private String footerMsg;
	public Datagrid() {
		super();
	}

	/**
	 * 
	 * @param total
	 *            总记录数
	 * @param rows
	 *            列表行
	 */
	public Datagrid(long total, List<T> rows) {
		this(total, null, rows, null);
	}

	/**
	 * 
	 * @param total
	 *            总记录数
	 * @param columns
	 *            动态列
	 * @param rows
	 *            列表行
	 * @param footer
	 *            脚列表	
	 *            
	 */
	public Datagrid(long total, List<Column> columns, List<T> rows,
			List<Map<String, Object>> footer) {
		super();
		this.total = total;
		this.columns = columns;
		this.rows = rows;
		this.footer = footer;
		}

	
	
	/**
	 * 
	 * @param total
	 *            总记录数
	 * @param columns
	 *            动态列
	 * @param rows
	 *            列表行
	 * @param footer
	 *            脚列表
	 * @param totalInvestment
	 *            总投资
	 *            
	 */
	public Datagrid(long total, List<Column> columns, List<T> rows,
			List<Map<String, Object>> footer,double totalInvestment) {
		super();
		this.total = total;
		this.columns = columns;
		this.rows = rows;
		this.footer = footer;
		this.totalInvestment = totalInvestment;
		NumberFormat nf = NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		this.footerMsg = "合计总投资： " + nf.format(totalInvestment) + " 万元";
	}
	public Datagrid(int total, List<T> rows,
			Double totalInvestment) {
		this(total, null, rows, null,totalInvestment);
	}
	/**
	 * 
	 * @param total
	 *            总记录数
	 * @param columns
	 *            动态列
	 * @param rows
	 *            列表行
	 * @param footer
	 *            脚列表
	 * @param totalInvestment
	 *            总投资
	 *
	 * @param decomposeInvest
	 *            累计分解资金
	 *            
	 * @param bundPlanInvest
	 *            待分解资金
	 *            
	 */
	public Datagrid(long total, List<Column> columns, List<T> rows,
			List<Map<String, Object>> footer,double totalInvestment,double decomposeInvest,double bundPlanInvest) {
		super();
		this.total = total;
		this.columns = columns;
		this.rows = rows;
		this.footer = footer;
		this.totalInvestment = totalInvestment;
		NumberFormat nf = NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		this.footerMsg = "合计总投资： " + nf.format(totalInvestment) + " 万元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"待分解资金："+nf.format(bundPlanInvest)+" 万元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;已分解资金："+nf.format(decomposeInvest)+ " 万元";
	}
	public Datagrid(int total, List<T> rows,
			Double totalInvestment,Double decomposeInvest,Double bundPlanInvest) {
		this(total, null, rows, null,totalInvestment,decomposeInvest,bundPlanInvest);
	}
	
	
	/**
	 * 总记录数
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * 设置总记录数
	 * 
	 * @param total
	 *            总记录数
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * 列表行
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * 设置列表行
	 * 
	 * @param rows
	 *            列表行
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	/**
	 * 脚列表
	 */
	public List<Map<String, Object>> getFooter() {
		return footer;
	}

	/**
	 * 设置脚列表
	 * 
	 * @param footer
	 *            脚列表
	 */
	public void setFooter(List<Map<String, Object>> footer) {
		this.footer = footer;
	}

	/**
	 * 动态列
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * 设置动态列
	 * 
	 * @param columns
	 *            动态列
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public double getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(double totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public String getFooterMsg() {
		return footerMsg;
	}
    /**
     *  <pre>
     * @param footerMsg	
     * 			-- 在分页处显示一条信息
     * @author tannc
     * @createtime 2015年10月7日下午2:41:41
     * @version 1.0
     */
	public void setFooterMsg(String footerMsg) {
		this.footerMsg = footerMsg;
	}
  
}
