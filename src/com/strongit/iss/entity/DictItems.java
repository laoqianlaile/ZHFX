package com.strongit.iss.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.*;

/**
 * DictionaryItems entity.
 * 
 * @author liupj
 * @date 20160122
 */
@Entity
@Table(name = "DICTIONARY_ITEMS", uniqueConstraints = @UniqueConstraint(columnNames = {"GROUP_NO", "ITEM_KEY" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "dictionary")
public class DictItems extends OtherBaseEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1867368117483238933L;

	// Fields

    /*PK*/
    @Id
    @GeneratedValue(generator = "defaultGenerator")
    @GenericGenerator(name = "defaultGenerator", strategy = "uuid")
    @Column(name = "ITEM_GUID", unique = true, nullable = false, length = 38)
	private String itemGuid;

    /*所属字典组*/
    @ManyToOne
    @JoinColumn(name = "GROUP_NO", nullable = false,referencedColumnName = "GROUP_NO")
    @JsonBackReference
	private Dict dictionary;

    /*字典项编号*/
    @Column(name = "ITEM_KEY", nullable = false, length = 30)
	private String itemKey;

    /*字典项值（简称）*/
    @Column(name = "ITEM_VALUE", nullable = false, length = 100)
	private String itemValue;

    /*字典项值（全称）*/
    @Column(name = "ITEM_FULL_VALUE", nullable = false, length = 500)
	private String itemFullValue;

    @Column(name = "ITEM_PARENT")
    private String parentItemId;

    /**
     * 所有父级字典项编号（中间用半角‘-’分割）
     */
    @Column(name="ITEM_PARENT_ALL", length = 500)
    private String itemParentAll;

    /*扩展标识（标识行政区划：1-省级以上行政区划；2-直辖市；3-单列市）*/
    @Column(name = "FLAG", length = 1)
	private String flag;

    /*启用状态（0-禁用；1-启用）*/
    @Column(name = "ENABLED", nullable = false, precision = 1, scale = 0)
	private Integer enabled;

    /*排序号（有效位10位，小数位4位）*/
    @Column(name = "PXH", precision = 10, scale = 4)
	private Double pxh;

    
	// Constructors

	/* default constructor */
	public DictItems() {
	}

    /* mini constructor */
    public DictItems(String itemGuid) {
        this.itemGuid = itemGuid;
    }

	/** full constructor */
	public DictItems(Dict dictionary, String itemKey,
			String itemValue, String itemFullValue,String parentItemId,
			String flag, Integer enabled, String operateUser, Date operateTime, Double pxh) {
		this.dictionary = dictionary;
		this.itemKey = itemKey;
		this.itemValue = itemValue;
		this.itemFullValue = itemFullValue;
		this.parentItemId = parentItemId;
		this.flag = flag;
		this.enabled = enabled;
		this.pxh = pxh;
		setUpdateUserName(operateUser);
		setUpdateTime(operateTime);
	}

	// Property accessors


    public String getItemGuid() {
        return itemGuid;
    }

    public void setItemGuid(String itemGuid) {
        this.itemGuid = itemGuid;
    }

    public Dict getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dict dictionary) {
        this.dictionary = dictionary;
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

    public String getItemFullValue() {
        return itemFullValue;
    }

    public void setItemFullValue(String itemFullValue) {
        this.itemFullValue = itemFullValue;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getParentItemId() {
        return parentItemId;
    }

    public void setParentItemId(String parentItemId) {
        this.parentItemId = parentItemId;
    }

    public String getItemParentAll() {
        return itemParentAll;
    }

    public void setItemParentAll(String itemParentAll) {
        this.itemParentAll = itemParentAll;
    }
    

    @Override
    public String toString() {
        return "DictionaryItems{" +
                "itemGuid='" + itemGuid + '\'' +
                ", dictionary=" + dictionary +
                ", itemKey='" + itemKey + '\'' +
                ", itemValue='" + itemValue + '\'' +
                ", itemFullValue='" + itemFullValue + '\'' +
                ", flag='" + flag + '\'' +
                ", enabled=" + enabled +
                ", pxh=" + pxh +
                ", operateUser='" + getUpdateUserName() + '\'' +
                ", operateTime=" + getUpdateTime() +
                '}';
    }

	public Double getPxh() {
		return pxh;
	}

	public void setPxh(Double pxh) {
		this.pxh = pxh;
	}
	
}