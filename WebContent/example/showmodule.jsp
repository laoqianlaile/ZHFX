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
<title>工程测试</title>
</head>
<body>
<script type="text/javascript" charset="utf-8">
// 用户名称 
 var ctx = "${ctx}";
    // 我创建的模板
	$.post('${ctx}/ReportServer?op=fr_bi&cmd=get_folder_report_list', function(data) {
		$("#show").text(data);
	}, 'text');
    //分享的模板 记录相关的标识 
    $.post('${ctx}/ReportServer?op=fr_bi&cmd=get_share_2_me_report_list', function(data) { 
		$("#shareModule").text(data);
	}, 'text');
    
    //得到菜单 http://localhost:8080/WebReport/ReportServer?op=fs_main&cmd=module_getrootreports     参数{id: -1}
    $.post('${ctx}/ReportServer?op=fs_main&cmd=module_getrootreports',{id:-1},function(data) { 
		$("#shareMenu").text(data);
	}, 'text');
    
</script>
我创建的模板  ReportServer?op=fr_bi&cmd=get_folder_report_list
<span id='show'></span>
<div style="margin-top:30px;">分享给我的模板  op=fr_bi&cmd=get_share_2_me_report_list</div>
<div id="shareModule"></div>
<div style="margin-top:30px;">菜单 ?op=fs_main&cmd=module_getrootreports     参数{id: -1}</div>
<div id="shareMenu"></div>
</body>
</html>