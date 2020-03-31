package com.strongit.iss.servlet;

import java.io.IOException;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.strongit.iss.common.SpringContextUtil;
import com.strongit.iss.task.LoadSystemCacheTask;

public class LoadSystemCacheServlet extends HttpServlet {
	private static final long serialVersionUID = -3597401954763706411L;
	
	protected final Logger logger = Logger.getLogger(getClass());

	// 是否自动加载缓存（1-加载，其他-不加载）
	private String running = "0";
	
	// 获取系统缓存加载间隔时间（单位：分钟），0表示只加载一次
	private int intervalMin = 30;
	// 加载缓存任务
	private LoadSystemCacheTask loadSystemCacheTask;
	

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			// 判断是否需要自动加载缓存数据
			running = getInitParameter("running");
			if ("1".equals(running)) {
				if (loadSystemCacheTask == null) {
					loadSystemCacheTask = (LoadSystemCacheTask) SpringContextUtil.getBean("loadSystemCacheTask");
//					loadSystemCacheTask = new LoadSystemCacheTask();
				}
				
				// 获取配置的定时获取间隔时间
				String intervalMinConfig = getInitParameter("intervalMin");
				if (StringUtils.isNotBlank(intervalMinConfig)) {
					intervalMin = Integer.parseInt(intervalMinConfig);
				}
				
				if (intervalMin == 0) { // 只执行一次
					loadSystemCacheTask.run();
				} else { // 定时加载
					Timer timer = new Timer();
					timer.schedule(loadSystemCacheTask, 0, intervalMin * 60 * 1000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 禁止通过WEB方式访问本Servlet
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

	/**
	 * 禁止通过WEB方式访问本Servlet
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

}
 