package com.strongit.iss.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.strongit.iss.common.Constant;

public class TimeoutInterceptor  extends AbstractInterceptor{
	private static String[] actions = {"Register","DeptAction"};
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		boolean exclude = false;
		String action = actionInvocation.getAction().toString();
		for(String a : actions){
			if(action.indexOf(a) > -1){
				exclude = true;
			}
		}
		// 当前用户为空
		Object userId=request.getAttribute(Constant.SYS_USER_ID);
		if(null==userId||StringUtils.isBlank(userId.toString())) {
			// 跳转到登录页
			return Action.LOGIN;
		}
		//过滤企业注册的action
		if(isAjaxRequest(request)&&!exclude){
			Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(obj.toString().equalsIgnoreCase("anonymousUser")){
				renderTimeout(response);
				return null;
			}
		}else{
			return actionInvocation.invoke();
		}

		return actionInvocation.invoke();
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		if (StringUtils.isNotBlank(header) && "XMLHttpRequest".equals(header)) {
			return true;
		} else {
			return false;
		}
	}

	private void renderTimeout(HttpServletResponse response){
		response.setHeader("sessionstatus", "timeout");//在响应头设置session状态 
	    try {
	    	//打印一个返回值，没这一行，在tabs页中无法跳出（导航栏能跳出），具体原因不明
			response.getWriter().print("timeout");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
