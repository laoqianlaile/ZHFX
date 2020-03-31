package com.strongit.iss.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.strongit.iss.util.Utils;

public class SimpleActionSupport extends ActionSupport {
	private static final long serialVersionUID = 3041101283373495109L;
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String CONTENT_TYPE_PLAIN = "text/plain;charset=UTF-8";
	private static final String CONTENT_TYPE_HTML  = "text/html;charset=UTF-8";
	private static final String CONTENT_TYPE_XML   = "text/xml;charset=UTF-8";
	
	protected String renderHTMLEncodedText(String text) {
		return renderText(Utils.HTMLEncode(text));
	}
	
	protected String renderText(String text) {
		return Utils.render(false, CONTENT_TYPE_PLAIN, text);
	}

	protected String renderHtml(String html) {
		return Utils.render(false, CONTENT_TYPE_HTML, html);
	}

	protected String renderXML(String xml) {
		return Utils.render(false, CONTENT_TYPE_XML, xml);
	}
	
	protected String renderHTMLEncodedErrText(String text) {
		return renderErrText(Utils.HTMLEncode(text));
	}
	
	protected String renderErrText(String text) {
		return Utils.render(true, CONTENT_TYPE_PLAIN, text);
	}

	protected String renderErrHtml(String html) {
		return Utils.render(true, CONTENT_TYPE_HTML, html);
	}

	protected String renderErrXML(String xml) {
		return Utils.render(true, CONTENT_TYPE_XML, xml);
	}

	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	protected HttpSession getSession() {
		return getRequest().getSession();
	}
}