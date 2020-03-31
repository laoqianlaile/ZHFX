<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>国家重大建设项目库</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=path%>/themes/default/css/error.css" />
</head>
<body>
	<div class="errorbox">
	  <div class="paragbox">
		<h3>出错信息</h3>
		<p>页面找不到</p>
		<br/>
	  </div>
	</div>
	<div class="picbox"><img src="<%=path%>/themes/default/css/images/page_bg.gif"/></div>
</body>
</html>
