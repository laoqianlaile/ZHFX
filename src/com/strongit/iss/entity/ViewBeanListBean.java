package com.strongit.iss.entity;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("ITEMS")
public class ViewBeanListBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@XStreamImplicit(itemFieldName="ITEM")
	private List<ViewBean> result;
	
	
	public List<ViewBean> getResult() {
		return result;
	}
	public void setResult(List<ViewBean> result) {
		this.result = result;
	}
}

