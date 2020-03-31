package com.strongit.iss.entity;

public class DictionaryItems implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//字典表分组编号
	private String groupNo;
	//字典表code
	private String itemKey;
	//字典表code对应的名称
	private String itemValue;
	//字典表code
	private String itemFullKey;
	//字典表code对应的名称
	private String itemFullValue;
	//字典表code对应的名称
	private String shortFullValue;
	//字典表ITEM_PARENT
	private String itemParent;
	// 字典简称
	private String shortValue;
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getItemKey() {
		return itemKey;
	}
	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public String getItemFullKey() {
		return itemFullKey;
	}
	public void setItemFullKey(String itemFullKey) {
		this.itemFullKey = itemFullKey;
	}
	public String getItemFullValue() {
		return itemFullValue;
	}
	public void setItemFullValue(String itemFullValue) {
		this.itemFullValue = itemFullValue;
	}
	public String getItemParent() {
		return itemParent;
	}
	public void setItemParent(String itemParent) {
		this.itemParent = itemParent;
	}
	public String getShortFullValue() {
		return shortFullValue;
	}
	public void setShortFullValue(String shortFullValue) {
		this.shortFullValue = shortFullValue;
	}

	public String getShortValue() {
		return shortValue;
	}

	public void setShortValue(String shortValue) {
		this.shortValue = shortValue;
	}
}
