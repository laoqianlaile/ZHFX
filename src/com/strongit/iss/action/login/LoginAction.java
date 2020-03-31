package com.strongit.iss.action.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.Struts2Utils;

/**
 * @author XiaXiang
 *
 */
@SuppressWarnings("serial")
public class LoginAction extends BaseActionSupport<Object> {
	
	/**
	 *  测试工程
	 * @return
	 */
	public String test(){
		//返回参数
		for(int num = 0;num<1000;num++){
			//userService.getTestData();
			Struts2Utils.renderText("第:"+num+"<br>");	
			System.out.println("------------------------------------------------------------------------------------------------------------");
			System.out.println("------------------------------------------------第:"+num+"--------------------------------------------------");
			System.out.println("------------------------------------------------------------------------------------------------------------");
		}
		return null;
	}
	/**
	 *  获取BI系统的sessionId
	 * @return
	 */
	public String getSessionId(){
		try {
			String sessionID = Struts2Utils.getRequest().getSession().getId();//SessionDealWith.generateSessionID(this.getRequest(), this.getResponse(), new BIWeblet());
			Struts2Utils.renderText(sessionID);	
		} catch (Exception e) {
			// 记录日志 
			LOG.debug("登录", e.getMessage());
			e.printStackTrace();
		}
		
		
		return null;
	}
	/**
	 *  得到用户信息  部门-角色-用户名称
	 * @return
	 * @throws Exception
	 */
	public String getUserInfo() throws Exception{
		return null;
	}

     /***
      *   记录用户日志，将用户信息存放在Session缓存里面
      * @Author tannongchun
      * @Date 2016/7/23 12:34
      */
	public String recordUserInfo() {
		try {
			// 用户Id
//			String uinfo = "";
			// 保存用户信息在Session中
			//Struts2Utils.getSession().setAttribute(Constant.SYS_USER_INFO, uinfo);
			// 保存当前用户的ID
			//Struts2Utils.getSession().setAttribute(Constant.SYS_USER_ID, userid);
			Struts2Utils.renderText(true);
		}catch (Exception e){
			// 记录异常日志
			logger.debug(e.getMessage());
		}
		return null;
	}

	/**
	 *   得到当前用户的Id
	 *   @see
	 * @return
     */
	 public String getSessionUserid(){
	 	// 得到当前用户的ID
		Object userid = this.getSession().getAttribute(Constant.SYS_USER_ID);
		 // 当前登录用户为空
		 if(null == userid || StringUtils.isBlank(userid.toString())) {
		 	// 当前用户没有登录
		 	Struts2Utils.renderText("");
		 }
		 else{
		 	// 返回当前登录人的ID
			 Struts2Utils.renderText(userid.toString());
		 }
		 return null;
	 }
	/***
	 *  退出登录
	 * @Author tannongchun
	 * @Date 2016/7/25 18:15
	 */
	public String logout(){
		// 系统工程清空缓存
		this.getSession().setAttribute(Constant.SYS_USER_INFO,null);
		this.getSession().setAttribute(Constant.SYS_USER_ID,null);
		Struts2Utils.renderText(Constant.RESULT_SUCCESS);
		return  null;
	}
}
