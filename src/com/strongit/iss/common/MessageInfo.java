package com.strongit.iss.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Properties;

/**
 * <pre>
 *    用于发送消息到前台 JSP
 *  发送信息到前端 @see com.strongit.iss.util.Struts2Utils
 *  eg：
 *     	MessageInfo info1=new MessageInfo(MessageInfo.ERROR,"E_2015",MessageInfo.tipQuestion,null);
 *     Struts2Utils.renderText(info1)
 * @author：tannc   
 * @date：2015年8月31日下午7:29:08
 * @version:v1.0
 * @CopyRight:思创数码科技股份有限公司
 */
public class MessageInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 成功
	 */
	public static final int SUCCESS = 1;
	/**
	 * 警告
	 */
	public static final int WARN = 2;
	/**
	 * 失败
	 */
	public static final int ERROR = 0;
	
	/**
	 * 错误消息提示类型
	 */
	public static final String tipError="error";	
	
	/**
	 * 提示消息提示类型
	 */
	public static final String tipTip="info";
	
	/**
	 * 疑问信息提示类型
	 */
	public static final String tipQuestion="question";
	
	/**
	 *  警告信息提示类型
	 */
	public static final String tipWarning="warning";
	
	/**
	 *  配置文件处理
	 */
	private static Properties properties; 
	
	public MessageInfo() {
		super();	
	}
   /**
    * 加载配置文件
    */
	static{
		properties=new Properties();
		InputStream is=null;
//		System.err.println("loading ........ FileInputStream is");
			try {
//			 is=new FileInputStream("messageInfos.properties");
			 is=Object.class.getResourceAsStream("/resources/messageInfos.properties");    
			 // 设置编码格式
			   properties.load(new InputStreamReader(is,"UTF-8"));			
//			   System.err.println("loading End ........ FileInputStream is");
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{				
				if(null!=is){
					try {
						is.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
		
		}
	}
    /**
     * <pre>
     * @param code
     *         失败，成功，警告的编码
     * @param msgNo
     *       	错误编码
     * @param tipCode
     * 			错误提示类型
     * @param obj
     * 			-- 其他对象
     */
	public MessageInfo(int code, String msgNo, String tipCode, Object obj) {		
		super();		
		properties.getProperty(msgNo);
	}
	
	
 
}
