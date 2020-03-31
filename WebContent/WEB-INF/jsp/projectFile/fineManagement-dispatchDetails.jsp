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
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/iframe.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/base.css" />
    <script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="${ctx}/common/extend.js"></script>
</head>
<body>
<div class="chartDiv divContent">
			<span class="jbg topLeft"></span>
			<span class="jbg topRight"></span>
			<span class="jbg bottomLeft"></span>
			<span class="jbg bottomRight"></span>
			<h6 class="chartDivTitle">最新调度情况</h6>
			<div class="chartHeight">	
				<ul class="zxddList" id="_tplHTML">					
					
				</ul>
			</div>
		</div>
<script type="text/javascript">
debugger;
	$(function(){
		// 自动执行
		getData();
	});
	function getData(){
		$.ajax({
			async : false,
			cache:false,
			type: 'POST',
			dataType : 'json',
			url: '${ctx}/projectFile/projectFile!getDispatchDetails.action',
			error: function () {//请求失败处理函数
				$.dialog.alert("数据获取失败！","warning");
			},
			success:function(data){ //请求成功后处理函数。
				var template=$("#template").html();
			    var _tplHTML="";
			    if(data){
					for(var i = 0;i < data.length;i++){
						debugger;
						_tplHTML=_tplHTML+ZHFX.fs(template,data[i]["IMAGE_URL"].split(",")[0],data[i]["PRO_NAME"],data[i]["IMAGE_PROGRESS"],data[i]["YEAR_BUILD_CONTENT"],data[i]["ID"]); 
					}
					$("#_tplHTML").append(_tplHTML);
			    }
			}
		});
	}
	function openWin(id){
		var winOption = "height=600,width=1200,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
		window.open('${ctx}/projectFile/projectFile!details?projectGuid='+id,window,winOption);
		console.log()
	}
</script>
<script type="text/html" id="template">
			<li><img src="${ctx}{0}" width="153" height="102" />
			<div>
				<p><i>项目名称：</i><a href="javascript:void(0);" onclick="openWin('{4}')" >{1}</a></p>
				<p><i>形象进度：</i>{2}</p>
				<p><i>年度建设内容：</i>{3}</p>
			</div>
			</li>
</script>
</body>
</html>