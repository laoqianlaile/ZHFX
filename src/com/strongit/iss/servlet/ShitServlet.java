package com.strongit.iss.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShitServlet extends HttpServlet {
	
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
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		/** 1.生成图片* 2.保存图片上的文本到session域中* 3.把图片响应给客户端*/
		VerifyCode vc=new VerifyCode();
		BufferedImage image=vc.getImage();
		//保存图片上的文本到session域
		request.getSession().setAttribute("session_code", vc.getText());
		VerifyCode.output(image, response.getOutputStream());
	}
}
