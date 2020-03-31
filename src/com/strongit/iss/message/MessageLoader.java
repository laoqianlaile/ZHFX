package com.strongit.iss.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * 思创数码科技股份有限公司 南昌研发中心
 * @author：杜峰
 * 创建日期：2015年7月16日下午2:33:34
 * 功能描述：消息加载类
 */
public class MessageLoader {
	private static Logger logger = LoggerFactory.getLogger(MessageLoader.class);

	private static final String PREFIX_ERROR="E";
	private static final String PREFIX_INFORMATION="I";
	private static final String PREFIX_WARNING="W";
	private static final String PREFIX_CONFIRM="C";
	
	private static Properties messages;
	public MessageLoader(){
		
	}
	public MessageLoader(String pathName) throws InvalidPropertiesFormatException, IOException{
		setPathName(pathName);
		
	}
	public  void  setPathName(String pathName) throws IOException{
		InputStream s = MessageLoader.class.getClassLoader().getResourceAsStream(pathName);
		messages = new Properties();
		messages.load(s);
		logger.debug("load messages success.");
	}
	/**
	 * 根据消息编号和参数值序列，获取一个消息实体
	 * @param code 消息编号
	 * @param args 
	 * @return
	 */
	public static  Message getMessage(String code,Object... args){
		//校验消息编号的合法性
		if (code == null ||"".equals(code)){
			logger.warn("Message code is empty!");
			return null;
		}
		if (!messages.containsKey(code)){
			logger.warn("Message code {} is not exist!",code);
			return new Message(code,Message.MESSAGE_TYPE_INFO,code);
		}
		//构造消息内容主体
		String format = messages.getProperty(code);
		String context=format;
		if(args!=null&&args.length>0)
			context= String.format(format,args);
		String prefix = code.substring(0,1);
		
		//根据消息编号的首字母，判断消息类型
		int msgType = Message.MESSAGE_TYPE_INFO;
		switch(prefix){
		case PREFIX_ERROR: 
			msgType=Message.MESSAGE_TYPE_ERROR;
			break;
		case PREFIX_INFORMATION:
			msgType = Message.MESSAGE_TYPE_INFO;
			break;
		case PREFIX_WARNING:
			msgType = Message.MESSAGE_TYPE_WARNING;
			break;
		case PREFIX_CONFIRM:{
			msgType = Message.MESSAGE_TYPE_CONFIRM;
			break;
		}
		default:
			msgType = Message.MESSAGE_TYPE_INFO;
		}
		
		Message msg = new Message(code, msgType, context);
		logger.debug("Message:{} created.",code);
		return msg;
	}
}
