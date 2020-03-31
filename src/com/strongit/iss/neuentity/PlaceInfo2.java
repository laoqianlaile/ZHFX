package com.strongit.iss.neuentity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="TZXMZH.DIM_DIVISION")
public class PlaceInfo2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; // 数据唯一标识
	private String code1; // 一级编号
	private String name1; // 一级名称
	private String sName1; // 一级简称
	private String code2; // 二级编号
	private String name2; // 二级名称
	private String sName2; // 二级简称
	private String code3; // 三级编号
	private String name3; // 三级名称
	private String sName3; // 三级简称
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid.hex")
	@Column(name = "UUID", nullable = false, unique = true, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "CODE1", updatable = false, insertable = false)
	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}
	@Column(name = "NAME1", updatable = false, insertable = false)
	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}
	@Column(name = "CODE2", updatable = false, insertable = false)
	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}
	@Column(name = "NAME2", updatable = false, insertable = false)
	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}
	@Column(name = "CODE", updatable = false, insertable = false)
	public String getCode3() {
		return code3;
	}

	public void setCode3(String code3) {
		this.code3 = code3;
	}
	@Column(name = "NAME", updatable = false, insertable = false)
	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}
	@Column(name = "S_NAME1", updatable = false, insertable = false)
	public String getsName1() {
		return sName1;
	}

	public void setsName1(String sName1) {
		this.sName1 = sName1;
	}
	@Column(name = "S_NAME2", updatable = false, insertable = false)
	public String getsName2() {
		return sName2;
	}

	public void setsName2(String sName2) {
		this.sName2 = sName2;
	}
	@Column(name = "S_NAME", updatable = false, insertable = false)
	public String getsName3() {
		return sName3;
	}

	public void setsName3(String sName3) {
		this.sName3 = sName3;
	}
	
}
