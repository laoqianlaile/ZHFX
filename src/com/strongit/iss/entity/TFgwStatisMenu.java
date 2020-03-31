/**
 * @author XiaXiang
 * @Date 2016年10月15日下午3:23:23
 */
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
 * @author XiaXiang
 *
 */
@Entity
@Table(name = "T_FGW_STATIS_MENU")
public class TFgwStatisMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	//主键
	private String guid;
	//名称
	private String name;
	//父级菜单主键
	private String parentGuid;
	// 功能模块的标题
	private  String menuTitle;
	// 菜单模块的高度
	private Long  pageHeight;
	// 菜单模块的宽度
	private Long  pageWidth;
	//父级菜单名称
	private String parentName;
	//标识（0-基础业务 2-重点业务）
	private String menuFlag;
	//URL路径
	private String url;
	//是否启用（0-禁止 1-启用）
	private String enable;
	//创建用户ID
	private String createUser;
	//创建时间
	private Date createTime;
	//更新用户ID
	private String updateUser;
	//更新时间
	private Date updateTime;
	//类型
	private String typeFlag;
	//排序
	private Long sort;
	
	/**
	 * @return the guid
	 */
	@Id
	@GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy="uuid.hex")
	@Column(name="GUID", nullable=false, unique=true, length=32)
	public String getGuid() {
		return guid;
	}
	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}
	/**
	 * @return the name
	 */
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parentGuid
	 */
	@Column(name = "PARENT_GUID")
	public String getParentGuid() {
		return parentGuid;
	}
	/**
	 * @param parentGuid the parentGuid to set
	 */
	public void setParentGuid(String parentGuid) {
		this.parentGuid = parentGuid;
	}
	
	/**
	 * @return the parentName
	 */
	@Transient
	public String getParentName() {
		return parentName;
	}
	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	/**
	 * @return the menuFlag
	 */
	@Column(name = "MENU_FLAG")
	public String getMenuFlag() {
		return menuFlag;
	}
	/**
	 * @param menuFlag the menuFlag to set
	 */
	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}
	/**
	 * @return the url
	 */
	@Column(name = "URL")
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the enable
	 */
	@Column(name = "ENABLE")
	public String getEnable() {
		return enable;
	}
	/**
	 * @param enable the enable to set
	 */
	public void setEnable(String enable) {
		this.enable = enable;
	}
	/**
	 * @return the createUser
	 */
	@Column(name = "CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}
	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	/**
	 * @return the createTime
	 */
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updateUser
	 */
	@Column(name = "UPDATE_USER")
	public String getUpdateUser() {
		return updateUser;
	}
	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * @return the updateTime
	 */
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the typeFlag
	 */
	@Column(name = "TYPE_FLAG")
	public String getTypeFlag() {
		return typeFlag;
	}
	/**
	 * @param typeFlag the typeFlag to set
	 */
	public void setTypeFlag(String typeFlag) {
		this.typeFlag = typeFlag;
	}

	@Column(name = "MENU_TITLE")
	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	@Column(name = "PAGE_HEIGHT")
	public Long getPageHeight() {
		return pageHeight;
	}
	public void setPageHeight(Long pageHeight) {
		this.pageHeight = pageHeight;
	}
	@Column(name = "PAGE_WIDTH")
	public Long getPageWidth() {
		return pageWidth;
	}
	public void setPageWidth(Long pageWidth) {
		this.pageWidth = pageWidth;
	}
	@Column(name = "_SORT")
	public Long getSort() {
		return sort;
	}
	/**
	 * @param sort the typeFlag to set
	 */
	public void setSort(Long sort) {
		this.sort = sort;
	}
}
