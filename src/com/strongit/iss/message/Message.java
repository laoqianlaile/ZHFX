package com.strongit.iss.message;
/**
 * 
 * 思创数码科技股份有限公司 南昌研发中心
 * @author：杜峰
 * 创建日期：2015年7月16日下午2:43:15
 * 功能描述：消息实体类
 */
@SuppressWarnings("serial") 
public class Message implements java.io.Serializable {
	public static final int MESSAGE_TYPE_INFO=0;
	public static final int MESSAGE_TYPE_WARNING=1;
	public static final int MESSAGE_TYPE_ERROR=2;
	public static final int  MESSAGE_TYPE_CONFIRM=3;
	
	/**
	 * 消息编码
	 */
	private String code;
	/**
	 * 消息内容
	 */
	private String content;
	
	/**
	 * 消息类型
	 */
	private int messageType;
	
	/**
	 * 获取消息实体的消息类型
	 * @return 消息实体的消息类型
	 */
	public int getMessageType() {
		return messageType;
	}
	/**
	 * 设置消息实体的消息类型
	 * @param messageType 消息实体的消息类型
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	/**
	 * 获取消息实体的消息内容
	 * @return 消息实体的消息内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置消息实体的消息内容
	 * @param content 消息实体的消息内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 消息体构造函数
	 * @param code 消息编号
	 * @param messageType 消息类型 
	 * @param content 消息内容
	 */
	public  Message(String code,int messageType,String content){
		this.setCode(code);
		this.messageType = messageType;
		this.content = content;
	}
	public Message() {
		
	}
}
