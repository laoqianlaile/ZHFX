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
<title>加载报表</title>
</head>
<body style="width:100%;height:100%;scroll:hidden;" >
   <iframe id="reportFrame" frameborder="0" height="600px" width="1000px" scrolling="no" style="scroll:hidden;" src="${ctx}/ReportServer?op=fr_bi&cmd=bi_init&id=1&createBy=1&edit=_bi_view_"></iframe>
</body>
</html>