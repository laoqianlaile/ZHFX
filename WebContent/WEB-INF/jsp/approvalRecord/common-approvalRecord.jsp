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
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/global.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/prefixfree.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.transit.min.js"></script>
<script type="text/javascript" src="${ctx}/common/extend.js"></script>
</head>

<body>
<body>
<table width="100%" height="800px">
    <tr>
        <td colspan="2"  style="width:100%;height: 400px">
            <iframe allowtransparency="true" frameborder="0" width="100%" scrolling="no"  height="100%" src="${ctx}/approvalRecord/showApprovalRecordYearTrend"></iframe>
        </td>
    </tr>
    <tr>
        <td colspan="2"  style="width:100%;height: 400px">
            <iframe allowtransparency="true" frameborder="0"width="100%" scrolling="no"  height="100%" src="${ctx}/approvalRecord/showApprovalRecordMonthTrend"></iframe>
        </td>
    </tr>
    <tr>
        <td style="width:60%;height: 600px;padding-right:10px;">
            <iframe allowtransparency="true" frameborder="0" width="100%" scrolling="no"  height="100%" src="${ctx}/approvalRecord/showApprovalRecordMap"></iframe>
        </td>
        <td style="width:40%;height: 600px">
            <iframe allowtransparency="true" frameborder="0" width="100%" scrolling="no"  height="100%" src="${ctx}/approvalRecord/showApprovalRecordIndustry"></iframe>
        </td>
    </tr>
</table>
<script type="text/javascript">
    // 初始化主窗口是地图  fiters 过滤参数
	   var mainWin='',win=[],filters={};
	        //刷新
	        function refreshWin(code) {
	          for(var key in win){
	              // 主的不刷新
	              if(key!=mainWin){
	                  // 传参数
	                  win[key].refreshWin(code);
	              }
	          }
	        }
</script>
</body>
</html>