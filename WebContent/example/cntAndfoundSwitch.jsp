<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
 <%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="${ctx}/js/jquery-1.9.1.min.js"></script>
<title>项目个数以及金额的切换</title>
</head>
<body style="width:100%;height:100%">
<button  id="btn1" type="button" >项目个数</button><button id="btn2" type="button">项目金额</button>
<!--  中间的内容区域 -->
 <div style="height:600px;width:9000px;">
     <iframe id="swithcFrame" frameborder="0" height="600px" width="1000px" scrolling="no" src="${ctx}/example/showmodule.jsp"></iframe>
 </div>
</body> 
<script type="text/javascript">
	$(function(){
		//切换项目个数
		$("#btn1").click(function(){
		});
		//切换金额 
		$("#btn2").click(function(){
		 });
	});

</script>
</html>