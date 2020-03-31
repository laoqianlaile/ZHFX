package com.strongit.iss.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.SpringContextUtil;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.service.IScreenSaverService;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Created by tannc on 2017/3/19 10:44. 首页登录的servlet 登录验证
 */
public class LoginServlet extends HttpServlet {
	
	private IScreenSaverService screenSaverService;
	private static ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	/**
	 * 禁止通过WEB方式访问本Servlet
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 禁止通过WEB方式访问本Servlet
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("m");
		screenSaverService = (IScreenSaverService) SpringContextUtil.getBean("screenSaverService");
		// 登录方法
		if (Constant.LOGIN_SERVLET_METHOD.equals(method)) {
			try {
				/**
				 * 校验验证码* 1.从session中获取正取的验证码* 2.从表单中获取用户填写的验证码* 3.进行比较*
				 * 4.如果相同，向下运行，否则保存错误信息到request域，转发到login.jsp
				 */
				String sessionCode = (String) request.getSession().getAttribute("session_code");
				// 获得的验证码里面对应的文本内容
				String paramCode = request.getParameter("verifyCode");
				// 用户输入文本框里面的内容
				PrintWriter out = null;
				out = response.getWriter();
				if (!paramCode.equalsIgnoreCase(sessionCode)) {
//					request.setAttribute("msg", "验证码错误！");
//					request.getRequestDispatcher("/index.jsp").forward(request, response);
					try {
						out.print(Constant.RESULT_CHECK_FAILURE);
						out.flush();
					} finally {
						if (null != out) {
							out.close();
						}
					}
					throw new BusinessServiceException("验证码错误！");
					// 打回到登录界面。
				}else{
					String username = request.getParameter("username");
					String password = request.getParameter("password");
					Map<String, Object> result = screenSaverService.login(username, password);
					response.setContentType("text/plain; charset=UTF-8");
					// 以下两句为取消在本地的缓存
					response.setHeader("Cache-Control", "no-cache");
					response.setHeader("Pragma", "no-cache");
					if (result == null || result.isEmpty()) {
						try {
							out.print(Constant.RESULT_FAILURE);
							out.flush();
						} finally {
							if (null != out) {
								out.close();
							}
						}
					} else {
						try {
							// 跳转到首页
							// response.sendRedirect(request.getContextPath()+"/index.jsp"
							// );
							// request.getRequestDispatcher("/index.jsp").forward(request,response);
							out.print(Constant.RESULT_SUCCESS);
							out.flush();
							// 结果存放在Session 中
							request.getSession().setAttribute(Constant.SYS_USER_INFO, result);
						} catch (Exception e) {
							// e.printStackTrace();
						} finally {
							if (null != out) {
								out.close();
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		// 得到首页地图数据的方法
		else if (Constant.ALL_PERSON_METHOD.equals(method)) {
			response.setContentType("application/json; charset=UTF-8");
			// 以下两句为取消在本地的缓存
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			Map<String, Object> map = new HashMap<String, Object>();
			// 总用户个数
			String allUserCounts = screenSaverService.getAllUserCounts();
			map.put("allUserCounts", allUserCounts);
			try {
				mapper.writeValue(response.getWriter(), map);
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
		// 已登录的判断
		else if (Constant.LOGININFO_METHOD.equals(method)) {
			response.setContentType("text/plain; charset=UTF-8");
			// 以下两句为取消在本地的缓存
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			PrintWriter out = null;
			out = response.getWriter();
			try {
				out.print(request.getSession().getAttribute(Constant.SYS_USER_INFO));
				out.flush();
			} finally {
				if (null != out) {
					out.close();
				}
			}
		} // 得到首页地图数据的方法
		else if (Constant.MAP_DATA_METHOD.equals(method)) {
			response.setContentType("application/json; charset=UTF-8");
			// 以下两句为取消在本地的缓存
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			// 开始时间（系统时间）
			String date = DateFormatUtils.format(DateUtils.addDays(new Date(), -7), "yyyy-MM-dd");
			// date="2017-02-08";
			// 获取活跃用户地区信息
			List<Map<String, Object>> list = screenSaverService.getPositionInfo(date);
			// 结束时间(系统时间)
			String json = "";
			// 把获取到的活跃地区信息转化成坐标信息
			for (int i = 0; i < list.size(); i++) {
				json += "{'name':'" + list.get(i).get("areaname") + "','geoCoord':[" + list.get(i).get("geocoordx")
						+ "," + list.get(i).get("geocoordy") + "]},";
			}
			// 去除多余的“，”
			if (StringUtils.isNotBlank(json)) {
				json = json.substring(0, json.length() - 1);
			}
			try {
				mapper.writeValue(response.getWriter(), "[" + json + "]");
			} catch (IOException e) {
				e.printStackTrace();
				throw new IllegalArgumentException(e);
			}
		}
	}
}
