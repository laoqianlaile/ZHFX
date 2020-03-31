package com.strongit.iss.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author XiaXiang
 * @Date 2016年8月1日上午9:32:40
 */
public class PropertiesUtil {
	//实例化日志对象
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	
	//Properties 对象
	private static Properties pro;
	
	/**
	 * 构造无参方法
	 */
	public PropertiesUtil() {
		
	}
	
	/**
	 * 构造有参方法
	 * @param pathName 文件路径
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	public PropertiesUtil(String pathName) throws InvalidPropertiesFormatException, IOException {
		// 根据文件路径读取文件
		setPathName(pathName);
	}
	
	/**
	 * 根据文件路径读取文件
	 * @orderBy 
	 * @param pathName 文件路径
	 * @throws IOException
	 * @author XiaXiang
	 * @Date 2016年8月1日上午10:24:03
	 */
	public void setPathName(String pathName) throws IOException{
		//读取文件
		InputStreamReader in = new InputStreamReader(
				PropertiesUtil.class.getClassLoader().getResourceAsStream(pathName), "UTF-8");
		pro = new Properties();
		//加载文件
		pro.load(in);
		//日志输出成功
		logger.debug("load PropertiesUtil success.");
		in.close();
	}
	
	/**
	 * 根据属性名称获取属性值
	 * @orderBy
	 * @param name 传入的属性名称
	 * @return 返回属性值（默认为String，不存在时返回为null）
	 * @author XiaXiang
	 * @Date 2016年8月1日上午9:35:03
	 */
	public static String getInfoByName(String name) {
		//检验name是否为空
		if (StringUtils.isBlank(name)) {
			//日志输出警告
			logger.warn("properties name is empty!");
			//返回null
			return null;
		}
		//检验是否包含name
		if (!pro.containsKey(name)){
			//日志输出警告
			logger.warn("properties name is not exist!");
			//返回null
			return null;
		}
		//根据属性名称获取属性值
		String value = pro.getProperty(name);
		//返回属性值
		return value;
	}
	
}
