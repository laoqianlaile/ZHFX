<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/common/include/meta.jsp"%>
<title>系统出错了...</title>
<style>
	table { font-family: "宋体"; font-size: 9pt; line-height: 150%; text-decoration: none; } 
	input { font-family: "宋体"; font-size: 9pt; line-height: 120%; color: #000000; text-decoration: none; } 
	.input { font-family: "宋体"; font-size: 12px; border: 1px solid #868585; background-color: #DEDEDE; } 
	.biankuang{ border: 1px solid #868585; background-color: #F4F3F3; } 
	a:link { font-family: "宋体"; font-size: 12px; line-height: 150%; text-decoration: none; color: #000000; } 
	a:visited { font-family: "宋体"; font-size: 12px; line-height: 150%; color: #000000; text-decoration: none; } 
	a:hover { font-family: "宋体"; font-size: 12px; line-height: 150%; color: #FF0000; text-decoration: none; } 
	a:active { font-family: "宋体"; font-size: 12px; line-height: 150%; color: #000000; text-decoration: none; } 
	#contentborder {
		BORDER-RIGHT: #506eaa 1px solid;
		BACKGROUND: white;
		PADDING-BOTTOM: 10px;
		OVERFLOW: auto;
		BORDER-LEFT: #506eaa 1px solid;
		WIDTH: 100%;
		BORDER-BOTTOM: #506eaa 1px solid;
		HEIGHT: 100%;
    	margin-left:2px;
	}
	.contentbodymargin {
		BACKGROUND: #d4d0c8;
		MARGIN: 0px 2px 2px 0;
	}
</style>
<script>
	function showTrace(){
		var trace = document.getElementById("trace");
		if(trace.style.display=="none"){
			trace.style.display="block";
			document.getElementById("showTraceBtn").value="隐藏错误详细信息";
		}else{
			trace.style.display="none";
			document.getElementById("showTraceBtn").value="显示错误详细信息";
		}
	}
	function cancel(){
		window.close();
	}
</script>
</head>
<base target="_self" />
<body class=contentbodymargin  style="overflow: auto;">
<DIV id=contentborder cellpadding="0">
	<table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td width="8%">&nbsp;</td>
    <td width="1%">&nbsp;</td>
    <td width="91%">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td rowspan="2" width="90%">
	<img src="<%=path%>/common/error/images/2006425927210.jpg">提示信息：

		<font color="#999900">
		<#if Request["tipMessage"]?exists>
            ${Request["tipMessage"]!""}
    	</#if>
		</font>
		<br>
		<br>
	<!-- 错误信息：-->

		<font color="red">
		<#if Request["extMessage"]?exists>
            ${Request["extMessage"]!""}
    	</#if>
		</font>
		<br>
		<br>
	<input id="showTraceBtn" type="button" value="显示更多信息" onclick="showTrace()" />
	<div style="font-size: 9pt; display: none" id="trace">
		<br>
        <textarea name="textarea" rows="20" readonly wrap=off class=input-area style="WIDTH: 100%; HEIGHT: 100%">
		<#if Request["detailMessage"]?exists>
            ${Request["detailMessage"]!""}
    	</#if>
		</textarea>
        </div>
	</td>
  </tr>
    <tr height="100%">
		<td>&nbsp;</td>
		<td align="left">&nbsp;</td>
   	</tr>
    <tr height="32">
		<td>&nbsp;</td>
		<td align="left">&nbsp;</td>
         <td align="left">
      	</td>

    </tr>

  <tr height="32">
  	<#if Request["backlocation"]?exists>
    	<td colspan="3" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${Request["backlocation"]}">返 回</a></td>
    <#else>
    	<td colspan="3" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.back();">返 回</a></td>
    </#if>
  </tr>

</table>
    </DIV>
</body>
</html>