package com.strongit.iss.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Label entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tz_label")
public class Label implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID:TODO
	 * @since Ver 1.1
	 */
	private static final long serialVersionUID = -7711386634002476200L;
	private String id;
	private String parentId;
	private String name;
	private String createUserId;
	private String createUserName;
	private Date creatTime;
	private String updateUserId;
	private String updateUserName;
	private Date updateTime;
	private Integer enabled;
	private String type;
	private String moduleCode;
	private Set<ProjectLabel> projectLabels = new HashSet<ProjectLabel>(0);

	// Constructors

	/** default constructor */
	public Label() {
	}

	/** minimal constructor */
	public Label(String id) {
		this.id = id;
	}

	/** full constructor */
	public Label(String id, String parentId, String name, String createUserId,
			String createUserName, Date creatTime, String updateUserId,
			String updateUserName, Date updateTime, Integer enabled,String moduleCode,
			Set<ProjectLabel> projectLabels) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.createUserId = createUserId;
		this.createUserName = createUserName;
		this.creatTime = creatTime;
		this.updateUserId = updateUserId;
		this.updateUserName = updateUserName;
		this.updateTime = updateTime;
		this.enabled = enabled;
		this.projectLabels = projectLabels;
		this.moduleCode = moduleCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 38)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PARENT_ID", length = 38)
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "NAME", length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Column(name = "ENABLED", precision = 1, scale = 0)
	public Integer getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	@Column(name = "TYPE", length = 20)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "label")
	public Set<ProjectLabel> getProjectLabels() {
		return this.projectLabels;
	}

	public void setProjectLabels(Set<ProjectLabel> projectLabels) {
		this.projectLabels = projectLabels;
	}
	@Column(name = "MODULE_CODE", length = 38)
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

}