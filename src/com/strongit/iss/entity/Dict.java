package com.strongit.iss.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

/**
 * Dictionary entity.
 * 
 * @author liupj
 * @date 20160122
 */
@Entity
@Table(name = "DICTIONARY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "dictionary")
public class Dict extends OtherBaseEntity implements java.io.Serializable {
	private static final long serialVersionUID = -1269900588010176325L;

	// Fields

    /*字典组编号 PK*/
    @Id
    @GeneratedValue(generator = "defaultGenerator")
    @GenericGenerator(name = "defaultGenerator", strategy = "increment")
    @Column(name = "GROUP_NO", unique = true, nullable = false, length = 10)
	private Long groupNo;

    /*字典组KEY*/
    @Column(name = "GROUP_KEY", unique = true, nullable = false, length = 50)
    private String groupKey;

    /*字典组名称*/
    @Column(name = "GROUP_NAME", nullable = false, length = 50)
	private String groupName;

    /*启用状态（0-禁用；1-启用）*/
    @Column(name = "ENABLED", nullable = false, precision = 1, scale = 0)
	private Integer enabled;

//    /*操作者*/
//    @Column(name = "OPERATE_USER", nullable = false, length = 38)
//	private String operateUser;
//
//    /*操作时间*/
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "OPERATE_TIME", nullable = false)
//	private Date operateTime = new Date();

    /*字典明细列表*/
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dictionary")
    @OrderBy(value = "itemKey asc")
    @Where(clause = "enabled = 1")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "dictionary")
    @JsonManagedReference
	private Set<DictItems> dictionaryItemses = new HashSet<>();

	// Constructors

	/** default constructor */
	public Dict() {
	}

	/** minimal constructor */
	public Dict(Long groupNo) {
		this.groupNo = groupNo;
	}

	/** full constructor */
	public Dict(String groupKey, String groupName, Integer enabled, String operateUser, Date operateTime) {
        this.groupKey = groupKey;
		this.groupName = groupName;
		this.enabled = enabled;
	}

    public Long getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Long groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

//    public String getOperateUser() {
//        return operateUser;
//    }
//
//    public void setOperateUser(String operateUser) {
//        this.operateUser = operateUser;
//    }
//
//    public Date getOperateTime() {
//        return operateTime;
//    }
//
//    public void setOperateTime(Date operateTime) {
//        this.operateTime = operateTime;
//    }
 
	
    public Set<DictItems> getDictionaryItemses() {
        return dictionaryItemses;
    }

    public void setDictionaryItemses(Set<DictItems> dictionaryItemses) {
        this.dictionaryItemses = dictionaryItemses;
    }

    @Override
    public String toString() {
        return "Dictionary {" +
                "groupNo='" + groupNo + '\'' +
                ", groupKey='" + groupKey + '\'' +
                ", groupName='" + groupName + '\'' +
                ", enabled=" + enabled +
                ", createUserId='" + getCreateUserId() + '\'' +
                ", createUserName='" + getCreateUserName() + '\'' +
                ", createTime=" + getCreateTime() +
                ", UpdateUserId='" + getUpdateUserId() + '\'' +
                ", UpdateUserName='" + getUpdateUserName() + '\'' +
                ", UpdateTime=" + getUpdateTime() + '}';
    }
}