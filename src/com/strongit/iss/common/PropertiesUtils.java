package com.strongit.iss.common;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtils {
	protected final static Logger logger = Logger.getLogger(PropertiesUtils.class);
	 /**
	  * 读取appconfig.properties配置文件属性
	  * @param name
	  * @return
	  */
	 public static String getProperty(String name) {
		 String result = null;
		 try {
			Properties props = PropertiesLoaderUtils.loadAllProperties("../config/appconfig.properties");
			result = props.getProperty(name);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		 return result;
	 }
	 /**
	  * 读取path配置文件属性
	  * @param name
	  * @param path
	  * @return
	  */
	 public static String getProperty(String name,String path){
		 String result = null;
		 try {
			Properties props = PropertiesLoaderUtils.loadAllProperties(path);
			result = props.getProperty(name);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		 return result;
	 }

}
