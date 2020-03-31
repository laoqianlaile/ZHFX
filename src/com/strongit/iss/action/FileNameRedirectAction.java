package com.strongit.iss.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

public class FileNameRedirectAction extends SimpleActionSupport {
	private static final long serialVersionUID = 7036941468397752690L;
	private String prefix = "/WEB-INF/jsp/";

	private String suffix = ".jsp";
	private String toPage = "";

	public void setToPage(String toPage) {
		this.toPage = toPage;
	}

	public String getPrefix() {
		return this.prefix;
	}
   /**
    * <pre>
    * @description
    *      --  添加get 方法 Struts 2.X的bug
    * @author ChunXinMeng
    * @date   2015年8月28日上午9:52:18 
    * @since v1.0
    * @return
    */
	public String getToPage() {
		return toPage;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void redirect() throws Exception {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(obj instanceof String){
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		String page = request.getParameter("toPage");
		this.logger.debug("redirect to page:" + page);
		if ((page == null) || ("".equals(page))) {
			this.logger.warn("redirect to page is null!!!");
			response.sendError(404);
		} else {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("redirect to page :" + this.prefix + page);
			}
			request.getRequestDispatcher(this.prefix + page).forward(request,
					response);
		}
	}
}