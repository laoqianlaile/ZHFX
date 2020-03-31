package com.strongit.iss.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * ProjectLabel entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tz_project_label")
public class ProjectLabel implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID:TODO
	 * @since Ver 1.1
	 */
	private static final long serialVersionUID = 6327824034469859451L;
	private String id;
	private String projectId;
	private Label label;
	private String createUserId;
	private String createUserName;
	private Date createTime;
	private String updateUserId;
	private String updateUserName;
	private Date updateTime;
	private Integer deleteflag;
	/** default constructor */
	public ProjectLabel() {
	}

	/** minimal constructor */
	public ProjectLabel(String id) {
		this.id = id;
	}

	/** full constructor */
	public ProjectLabel(String id, String projectId, Label label,
			String createUserId, String createUserName, Date createTime,
			String updateUserId, String updateUserName, Date updateTime,
			Integer deleteflag) {
		this.id = id;
		this.projectId = projectId;
		this.label = label;
		this.createUserId = createUserId;
		this.createUserName = createUserName;
		this.createTime = createTime;
		this.updateUserId = updateUserId;
		this.updateUserName = updateUserName;
		this.updateTime = updateTime;
		this.deleteflag = deleteflag;
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

	@Column(name = "PROJECT_ID")
	public String getProject() {
		return this.projectId;
	}

	public void setProject(String projectId) {
		this.projectId = projectId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LABEL_ID")
	public Label getLabel() {
		return this.label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
	
	@Transient
	public String getLabelName() {
		return label.getName();
	}
	
	public void setLabelName(String labelName) {
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
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	@Column(name = "DELETEFLAG", precision = 1, scale = 0)
	public Integer getDeleteflag() {
		return this.deleteflag;
	}

	public void setDeleteflag(Integer deleteflag) {
		this.deleteflag = deleteflag;
	}

}