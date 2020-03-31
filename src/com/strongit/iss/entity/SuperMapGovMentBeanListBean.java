package com.strongit.iss.entity;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("ITEMS")
public class SuperMapGovMentBeanListBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@XStreamImplicit(itemFieldName="ITEM")
	private List<SuperMapGovMentBean> result;
	
	
	public List<SuperMapGovMentBean> getResult() {
		return result;
	}
	public void setResult(List<SuperMapGovMentBean> result) {
		this.result = result;
	}
}

