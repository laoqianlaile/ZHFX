package com.strongit.iss.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * @author Liupj
 * @date 2016/1/19
 */
@MappedSuperclass
public class OtherBaseEntity implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3678275123210766523L;

	/**
	 * 操作版本（乐观锁，用于并发控制）
	 */
	@Version
    @Column(name = "VERSION", length = 10, nullable = false)
	protected Integer version;
	
	// 创建人Id
//	@Column(name = "CREATE_USER_ID", length = 38, nullable = false)
	@Column(name = "CREATE_USER_ID", length = 38)
	protected String createUserId;
	
	// 创建人姓名
//	@Column(name = "CREATE_USER_NAME", length = 100, nullable = false)
	@Column(name = "CREATE_USER_NAME", length = 100)
	protected String createUserName;
	
	// 创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false)
	protected Date createTime;
	
	// 更新人ID
//	@Column(name = "UPDATE_USER_ID", length = 38, nullable = false)
	@Column(name = "UPDATE_USER_ID", length = 38)
	protected String updateUserId;

	// 更新人
//	@Column(name = "UPDATE_USER_NAME", length = 100, nullable = false)
	@Column(name = "UPDATE_USER_NAME", length = 100)
	protected String updateUserName;

	// 更新时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", nullable = false)
	protected Date updateTime;
	// 地区编码
	@Column(name = "AREA_CODE")
	protected String areaCode;
	//数据类型（1-本系统数据，2-外部系统数据）
	@Column(name="DATA_TYPE",length = 1, nullable = false)
	protected Integer dataType;

    /*乐观锁*/
	// 版本号，用于并发控制
    public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	/*创建人Id*/
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/*创建人姓名*/
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/*创建时间*/
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/*更新人ID*/
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	/*更新人*/
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	/*更新时间*/
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	/*Appended by zouyw 2016/5/27  新增所有表都共有的数据类型字段  开始*/
	/*地区编码*/
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public Integer getDataType() {
			return dataType;
	}
	 
	public void setDataType(Integer dataType) {
			this.dataType = dataType;
	}

    
    public OtherBaseEntity(){}
    /**
     * 构造函数 
     * @param version
     * @param createUserId
     * @param createUserName
     * @param createTime
     * @param updateUserId
     * @param updateUserName
     * @param updateTime
     * @param areaCode
     * @param dataType
     */
	public OtherBaseEntity(String createUserId, String createUserName,
			Date createTime, String updateUserId,String updateUserName,Date updateTime,
			String areaCode,Integer dataType) {
		this.createUserId = createUserId;
		this.createUserName = createUserName;
		this.createTime = createTime;
		this.updateUserId = updateUserId;
		this.updateUserName =  updateUserName;
		this.updateTime = updateTime;
		this.areaCode = areaCode;
		this.dataType = dataType;
	}
    
}
