package com.strongit.iss.common;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class JsonTreeData {
	public String id;		//json id
	public String pid;		//
	public String text;		//json 显示文本
	public String state;	//json 'open','closed'	
	public  static final String OPEN="open";
	public  static final String CLOSED="closed";
	private Integer storeLevel;
	/**
	 * 自定义属性
	 */
	private Map<String, Object> attributes = Maps.newHashMap();
	
	//数据字典使用by zhufw
	public String guid;
	//本级储备库高级查询第num个查询条件
	private String num;
	//本级储备库高级查询项目指标编码-指标类型通用
	private String code;
	//本级储备库高级查询项目指标类别
	private String type;
	// 分类阶段-指标类型用
	private String stage;
	// 排序-指标类型用
	private Integer sort;
	// 创建者-指标类型用
	private String createUser;
	// 创建时间-指标类型用
	private Date createTime;
	//全部
	private String all;
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public Integer enabled;//启用 禁用
	public String groupNo;//1   2   3   4
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	public List<JsonTreeData> children;		//
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<JsonTreeData> getChildren() {
		return children;
	}
	public void setChildren(List<JsonTreeData> children) {
		this.children = children;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public Integer getStoreLevel() {
		return storeLevel;
	}
	public void setStoreLevel(Integer storeLevel) {
		this.storeLevel = storeLevel;
	}
    public String getStage() {
        return stage;
    }
    public void setStage(String stage) {
        this.stage = stage;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
	public String getAll() {
		return all;
	}
	public void setAll(String all) {
		this.all = all;
	}
	
}
