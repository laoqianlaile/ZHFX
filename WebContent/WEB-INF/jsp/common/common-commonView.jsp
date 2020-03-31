<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>综合分析</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui1.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui1.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/global.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
	<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/base.js"></script>
	<script type="text/javascript" src="${ctx}/common/extend.js"></script>
	<script type="text/javascript"  src="${ctx}/js/parser.js"></script>
	<script type="text/javascript"  src="${ctx}/js/swfobject.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui1.4/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
</head>
<body>
<%--<!-- 进度条 -->--%>
<%--<div class="loading">--%>
	<%--<div class="loadbg"></div>--%>
	<%--<span class="loadimg"><img src="${ctx}/book/images/loading.gif"/></span>--%>
<%--</div>--%>
	<div id="logo"></div>
	<div id="head"><span id="nameText"></span></div>
	<span id="loadClass1" class="bgImg1"></span>
	<span id="loadClass2" class="bgImg2"></span>
	<span id="loadClass3" class="bgImg3"></span>
	<span id="loadClass4" class="bgImg4"></span>
	<span id="loadClass5" class="bgImg5"></span>
	<span id="loadClass6" class="bgImg6"></span>
	  <%--引入公用文件--%>
	<jsp:include page="./toolbar.jsp"></jsp:include>
	<div id="contentDiv">
	    <span class="loginInfo"><img src="${ctx}/themes/images/user.png">${SYS_USER_INFO.employee_fullname}<a href="javascript:ZHFX.logout()">【退出】</a></span>
	    <span class="currenttWz">当前位置：<span id="currentPo"></span></span>
		<div  id="container" style="height:100%;"></div>
	</div>
	<script type="text/html" id="contentFlash">
        <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
				codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab"
                id="myobject" name="myobject" width="100%" height="{1}">
			<param id="movie" name="movie" value="{0}">
			<param name="quality" value="high">
			<param name="allowScriptAccess" value="always">
			<param name="allowFullScreen" value="true">
            <param name="wmode" value="Opaque">
            <param name="wmode" value="transparent">
			<embed src="{0}" quality="high" id="myobject1" name="myobject1"
				   width="100%" height="{1}" align="middle"
				   play="true" loop="false" quality="high" wmode="transparent"
				   allowScriptAccess="always" allowFullScreen="true"
				   pluginspage="http://www.adobe.com/go/getflashplayer"
                   type="application/x-shockwave-flash"></embed>
		</object>
    </script>
	<script type="text/html" id="contentIframe">
		<iframe id="contentViewId" name="contentViewId" allowtransparency="true" frameborder="0" width="100%" height="{1}" scrolling="yes" src="{0}"></iframe>
	</script>
  </body>
</html>