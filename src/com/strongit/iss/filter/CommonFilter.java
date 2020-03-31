package com.strongit.iss.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.MD5;
import com.strongit.iss.common.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;

public class CommonFilter implements Filter  {

    // private static String[] actions = {"index","screenSaver"};
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 过滤器--过滤掉没有登录用户的连接跳转
	 * @orderBy 
	 * @param req
	 * @param res
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 * @author tanghw
	 * @Date 2016年12月6日下午5:31:32
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {	
		//获取登录连接
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;        
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());
        List<String> urls=Lists.newArrayList();
		//添加能通过的连接
		urls.add("/");
		urls.add("/index.html");
		urls.add("/screenPro.html");
		urls.add("/indexForword.jsp");
		urls.add("/themes/css/login.css");
		urls.add("/js/jquery-1.11.3.min.js");
		urls.add("/js/prefixfree.min.js");
		urls.add("/js/base.js");
		urls.add("/js/md5.js");
		urls.add("/themes/css/global.css");
		urls.add("/common/extend.js");
		urls.add("/screenPro.html");
		urls.add("/LoginServlet");
		urls.add("/js/echarts/echarts.js");
		urls.add("/js/jquery-1.11.3.min.js");
		urls.add("/common/extend.js");
		urls.add("/js/echarts/chart/map.js");
		urls.add("/LoginServlet");
		urls.add("/themes/css/layout.css");
		urls.add("/themes/css/images/logo.png");
		urls.add("/themes/css/images/time-bg.png");
		urls.add("/themes/css/images/userNum-bg.png");
		urls.add("/themes/css/images/bg.jpg");
		urls.add("/themes/css/images/login-bg.png");
		urls.add("/themes/css/images/ico-dl.png");
		//判断包含以下字符的连接允许通过
		//boolean isVisited=url.startsWith("/book")||url.startsWith("/common")||url.startsWith("/data")||url.startsWith("/dd_img")||url.startsWith("/themes");
//		boolean isVisited2=url.startsWith("/js")||url.startsWith("/json")||url.startsWith("/template")||url.startsWith("/theme2")||url.startsWith("/files")
//				||url.startsWith("/ReportServer")||url.startsWith("/fiveplan")||url.startsWith("/budgetview")||url.startsWith("/auditpreparation")
//				||url.startsWith("/fundsview")||url.startsWith("/govmanagementview")||url.startsWith("/rpllplanview")||||url.startsWith("/DealDataServlet");
		boolean isVisited2=url.startsWith("/DealDataServlet")||url.startsWith("/neusoft")||url.startsWith("/ShitServlet");
		// 判断当前用户已登录
		Map<String,Object> userMap = (Map<String, Object>) request.getSession().getAttribute(Constant.SYS_USER_INFO);
		if (userMap == null) {
			if (urls.contains(url)||isVisited2) {
				filterChain.doFilter(request, response);
				return;
			}
		else {
             String  uiToken=request.getParameter("uiToken");
             String  tokenT=request.getParameter("tokenT");
				// 免登陆认证
				if(StringUtils.isNotBlank(uiToken)&&MD5.encode(PropertiesUtil.getInfoByName("uiToken")+tokenT).equals(uiToken)){
					request.getSession().setAttribute(Constant.SYS_USER_INFO, new HashMap<String, Object>());
					filterChain.doFilter(request, response);
					return;
				}
				else {
//				log.info("被拦截：跳转到login页面！");
					request.getRequestDispatcher("/NOT_FOUND").forward(request, response);
				}
				return ;
//				response.setStatus(404);
//                 throw  new RuntimeException("访问页面不存在ww");
			}
		}
			filterChain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}