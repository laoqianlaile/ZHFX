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
<title>报表查询</title>

<script type="text/javascript">
$(function(){
	$("#btn").click(function(){
		var buildSite=$("#buildSite").val();
		var WIN = $("#reportFrame")[0].contentWindow;
		WIN.FILTERS = [buildSite];
		// true-强制刷新 false-数据是否变化视情况刷新 
		WIN.BI.Utils.broadcastAllWidgets2Refresh(true);
		
	})
})

</script>
</head>
<body style="width:100%;height:100%">
   <div style="height:30px;"><label for="buildSite">建设地点</label><input id='buildSite' type="text"/> <button id="btn" type="button">查询</button></div>
   <iframe id="reportFrame" frameborder="0" height="600px" width="100%" src="${ctx}/ReportServer?op=fr_bi&cmd=bi_init&id=17&createBy=-999"></iframe>
</body>
</html>