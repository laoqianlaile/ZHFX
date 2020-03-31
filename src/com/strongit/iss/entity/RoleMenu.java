package com.strongit.iss.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ROLE_MENU")
public class RoleMenu  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleMenuGuid;
	private String menuGuid;
	private String roleGuid;
	private String createUserId;
	private String createUserName;
	private Date creatTime;
	private String updateUserId;
	private String updateUserName;
	private Date updateTime;
	@Id
	@Column(name = "ROLE_MENU_GUID", unique = true, nullable = false, length = 38)
	public String getRoleMenuGuid() {
		return roleMenuGuid;
	}
	public void setRoleMenuGuid(String roleMenuGuid) {
		this.roleMenuGuid = roleMenuGuid;
	}
	@Column(name = "MENU_GUID", length = 38)
	public String getMenuGuid() {
		return menuGuid;
	}
	public void setMenuGuid(String menuGuid) {
		this.menuGuid = menuGuid;
	}
	@Column(name = "ROLE_GUID", length = 38)
	public String getRoleGuid() {
		return roleGuid;
	}
	public void setRoleGuid(String roleGuid) {
		this.roleGuid = roleGuid;
	}
	@Column(name = "CREATE_USER_ID", length = 38)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_USER_NAME", length = 100)
	public String getCreateUserName() {
		return this.createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", length = 7)
	public Date getCreatTime() {
		return this.creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	@Column(name = "UPDATE_USER_ID", length = 38)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "UPDATE_USER_NAME", length = 100)
	public String getUpdateUserName() {
		return this.updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
