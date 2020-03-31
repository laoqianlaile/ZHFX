package com.strongit.iss.orm.hibernate;

import org.apache.commons.lang.StringUtils;

public class QueryParameter {
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	protected int pageNo = 1;
	protected int pageSize = -1;
	protected String orderBy = null;
	protected String order = "asc";
	protected boolean autoCount = true;

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isPageSizeSetted() {
		return this.pageSize > -1;
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getFirst() {
		if ((this.pageNo < 1) || (this.pageSize < 1)) {
			return 0;
		}
		return (this.pageNo - 1) * this.pageSize;
	}

	public boolean isFirstSetted() {
		return (this.pageNo > 0) && (this.pageSize > 0);
	}

	public String getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isOrderBySetted() {
		return StringUtils.isNotBlank(this.orderBy);
	}

	public String getOrder() {
		return this.order;
	}

	public void setOrder(String order) {
		if (("asc".equalsIgnoreCase(order)) || ("desc".equalsIgnoreCase(order)))
			this.order = order.toLowerCase();
		else
			throw new IllegalArgumentException(
					"order should be 'desc' or 'asc'");
	}

	public boolean isAutoCount() {
		return this.autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}
}