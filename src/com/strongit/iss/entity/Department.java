package com.strongit.iss.entity;

public class Department implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//部门编号
	private String departmentGuid;
	//父级编号
	private String parentGuid;
	//部门名称
	private String departmentName;
	
	public String getDepartmentGuid() {
		return departmentGuid;
	}
	public void setDepartmentGuid(String departmentGuid) {
		this.departmentGuid = departmentGuid;
	}
	public String getParentGuid() {
		return parentGuid;
	}
	public void setParentGuid(String parentGuid) {
		this.parentGuid = parentGuid;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
