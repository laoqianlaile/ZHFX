package com.strongit.iss.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



/**
 * 空间政府精细化管理中自定义表
 * @author zhoupeng
 * 2016年12月27日
 */
@Entity
@Table(name = "T_CITY_CUSTOM_COORDINATES")
public class TCityCustomCoordinates implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	private String sysid;


	/**
     * 自定义区域形状判定
     */
    private String flag;
    
    /**
     * 自定义区域 坐标
     */
    private String coorDinates;
    
    /**
     * 圆形半径
     */
    private String radius;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 名称
     */
    private String name;
    /**
     * 几何图形对象字符串
     */
    private String myGeometryStr;
    /**
     * 创建时间
     */
    private String createTime;

	

	/**
	 * 政府精细化管理（地图自定义区域）获取get和set方法
	 * @orderBy 
	 * @return
	 * @author 周朋
	 * @Date 2016年12月27日下午3:38:51
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
		
		@Column(name = "FLAG")
		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}
		
		@Column(name = "COORDINATES")
		public String getCoorDinates() {
			return coorDinates;
		}

		public void setCoorDinates(String coorDinates) {
			this.coorDinates = coorDinates;
		}

		@Column(name = "RADIUS")
		public String getRadius() {
			return radius;
		}

		public void setRadius(String radius) {
			this.radius = radius;
		}

		@Column(name = "UNIT")
		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		@Column(name = "NAME")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		@Column(name = "MYGEOMETRYSTR")
		public String getMyGeometryStr() {
			return myGeometryStr;
		}

		public void setMyGeometryStr(String myGeometryStr) {
			this.myGeometryStr = myGeometryStr;
		}
		
		

		@Column(name = "CREATE_TIME")
		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

	
}
