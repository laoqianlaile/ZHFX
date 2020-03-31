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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="./js/jquery-1.9.1.min.js"></script><
<title>工程测试</title>
</head>
<body>
<script type="text/javascript" charset="utf-8">
 var ctx = "${ctx}";
    // 获取数据
	$.post('${ctx}/login/login!test.action', function(data) {
		$("#show").text(data);
	}, 'text');
</script>
<span id='show'></span>
asdfffffffffffffffffff
</body>
</html>