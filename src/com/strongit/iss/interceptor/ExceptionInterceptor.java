package com.strongit.iss.interceptor;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ExceptionInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 7349429543348538903L;
	protected final Logger logger = Logger.getLogger(getClass());
	
	private static final String CONTENT_TYPE_PLAIN = "text/plain;charset=UTF-8";
//	private static final String CONTENT_TYPE_HTML  = "text/html;charset=UTF-8";
//	private static final String CONTENT_TYPE_XML   = "text/xml;charset=UTF-8";

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		String result = null;

		try {
			result = actionInvocation.invoke();
		} catch (Exception e) {
			//result = setInvokeResult(actionInvocation, e);
		}
		
		return result;
	}
	
//	private String setInvokeResult(ActionInvocation actionInvocation, Exception e) {
//		String result = null;
//		
//		// 打印异常日志
//		logger.error(actionInvocation.toString(), e);
//		
//		// 将异常放入stack
//		result = "error";
//		actionInvocation.getStack().push(new ExceptionHolder(e)); 
//		
//		// 判断是否是ajax请求，如果是，则转换为ajax异常，并放入stack中
//		HttpServletRequest request = ServletActionContext.getRequest();
//		if (isAjaxRequest(request)) {
//			try{
//				User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//				if(user != null){
//					if (e instanceof BusinessServiceException) {
//						renderErrText(e.getMessage());
//					}else if(e instanceof JDBCConnectionException){
//						renderErrText("很抱歉，您当前的网络可能不稳定，请重试！");//
//					} else {
//						renderErrText(e.getMessage());
//						//renderErrText("很抱歉，您当前的网络可能不稳定，请重试！");
//					}
//				}else{
//					HttpServletResponse response = ServletActionContext.getResponse();
//					response.setCharacterEncoding("UTF-8");
//		            response.addHeader("Error-Json", "{code:302,msg:'会话超时',script:''}");
//		            response.setStatus(401);
//				}
//			}catch(Exception e1){
//				HttpServletResponse response = ServletActionContext.getResponse();
//				response.setCharacterEncoding("UTF-8");
//	            response.addHeader("Error-Json", "{code:302,msg:'会话超时',script:''}");
//	            response.setStatus(401);
//			}
//		}
//
//		return result;
//	}
	
//	private boolean isAjaxRequest(HttpServletRequest request) {
//		String header = request.getHeader("X-Requested-With");
//		if (StringUtils.isNotBlank(header) && "XMLHttpRequest".equals(header)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	protected String renderText(String text) {
//		return Utils.render(false, CONTENT_TYPE_PLAIN, text);
//	}
//	
//	protected String renderErrText(String text) {
//		return Utils.render(true, CONTENT_TYPE_PLAIN, text);
//	}
}
