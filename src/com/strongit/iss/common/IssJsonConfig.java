package com.strongit.iss.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class IssJsonConfig extends JsonConfig {

	public IssJsonConfig() {
		super();
		// 设置输出的时间格式
		this.registerJsonValueProcessor(java.sql.Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		this.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		this.registerJsonValueProcessor(BigDecimal.class, new JsonBigDecimalValueProcessor());
	}

	public IssJsonConfig(String... excludes) {
		super();

		// 设置输出的时间格式
		this.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		this.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		this.registerJsonValueProcessor(BigDecimal.class, new JsonBigDecimalValueProcessor());
		// 过滤属性
		if (excludes != null && excludes.length > 0) {
			this.setExcludes(excludes);
		}
	}
}
class JsonBigDecimalValueProcessor implements JsonValueProcessor{
	/**
	 * @param value
	 * @param jsonConfig
	 * @return Object
	 */
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	/**
	 * @param key
	 * @param value
	 * @param jsonConfig
	 * @return Object
	 */
	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}

	/**
	 * process
	 * 
	 * @param value
	 * @return
	 */
	private Object process(Object value) {
		try {
			if (value instanceof BigDecimal) {
				return String.valueOf(value);
			}
			return value == null ? "" : value.toString();
		} catch (Exception e) {
			return "";
		}
	}
}
class JsonDateValueProcessor implements JsonValueProcessor {
	
	private String dateformat = "yyyy-MM-dd";

	public JsonDateValueProcessor() {
		super();
	}

	public JsonDateValueProcessor(String dateformat) {
		super();
		
		this.dateformat = dateformat;
	}

	/**
	 * @param value
	 * @param jsonConfig
	 * @return Object
	 */
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	/**
	 * @param key
	 * @param value
	 * @param jsonConfig
	 * @return Object
	 */
	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}

	/**
	 * process
	 * 
	 * @param value
	 * @return
	 */
	private Object process(Object value) {
		try {
			if (value instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateformat, Locale.UK);
				return sdf.format((Date) value);
			}
			return value == null ? "" : value.toString();
		} catch (Exception e) {
			return "";
		}
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}
	
}
