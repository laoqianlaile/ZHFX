package com.strongit.iss.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 系统配置变量表
 * @author li
 *
 */
@Entity
@Table(name = "T_SYS_CONFIG")
public class TSysConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	//主键
	private String sysid;
	//配置变量名称
	private String configName;
	//配置编码
	private String configCode;
	// 配置变量
	private  String configInfo;
	// 数据来源
	private String  source;
	//类型
//	private String  configType;
	private String  type;
	//创建用户ID
	private String createUser;
	//创建时间
	private Date createTime;
	//更新用户ID
	private String updateUser;
	//更新时间
	private Date updateTime;
	//排序
	private Long sort;
	
	/**
	 * 是否启用
	 *  0-禁用
	 *  1-启用
     */
	private  Long enbale;	

	//启用名称（非持久化）
	private  String enbaleName;
	
	//备注
	private String remark;
	
	
	/**
	 * 获取get和set方法
	 * @orderBy 
	 * @return
	 * @author li
	 * @Date 2016年12月7日上午11:17:29
	 */
	@Id
	@GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy="uuid.hex")
	@Column(name="SYSID", nullable=false, unique=true, length=32)
	public String getSysid() {
		return sysid;
	}
	public void setSysid(String sysid) {
		this.sysid = sysid;
	}
	@Column(name = "CONFIG_NAME")
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	@Column(name = "CONFIG_CODE")
	public String getConfigCode() {
		return configCode;
	}
	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	@Column(name = "CONFIG_INFO")
	public String getConfigInfo() {
		return configInfo;
	}
	public void setConfigInfo(String configInfo) {
		this.configInfo = configInfo;
	}
	@Column(name = "SOURCE")
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@Column(name = "CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "UPDATE_USER")
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "SORT")
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	
//	@Column(name = "CONFIGTYPE")
//	public String getConfigType() {
//		return configType;
//	}
//	public void setConfigType(String configType) {
//		this.configType = configType;
//	}
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "ENABLE")
	public Long getEnbale() {
		return enbale;
	}
	public void setEnbale(Long enbale) {
		this.enbale = enbale;
	}
	
	@Transient
	public String getEnbaleName() {
		return enbaleName;
	}
	public void setEnbaleName(String enbaleName) {
		this.enbaleName = enbaleName;
	}
	
	@Transient
	public String getTypeName() {
		String typeName="";
		if("0".equals(this.type)){
			typeName="当前全国人口数";   
		}else if("1".equals(this.type) ){
			typeName="当前年度";
		}else if("2".equals(this.type) ){
			typeName="下一年度";
		}else if("3".equals(this.type) ){
			typeName="每年各司局的下达标准";
		}else{
			typeName="下达总预算";
		}
		
		return typeName;
	}
	
	
	
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
