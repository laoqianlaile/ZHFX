<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综合分析</title>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/global.css" />

<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${ctx}/common/extend.js"></script>
<script type="text/javascript"  src="${ctx}/js/gbasebi.js"></script>
<script type="text/javascript" src="${ctx}/js/forJavaScript/libs/SuperMap.Include.js"></script>

<link rel="stylesheet" type="text/css" href="${ctx}/js/timeliner/css/timeliner.css" />
<script type="text/javascript" src="${ctx}/js/timeliner/jquery.timeliner.min.js"></script>
<script type="text/javascript">
	
	$(document).ready(function(){
		loadhtml();
		// EXAMPLE 1:
		$('#example1').timeliner({
			containerwidth: 1920,
			containerheight: 1080,
			autoplay: true,
			showtotaltime:false,
			showtooltiptime: false
		});
		loadSession();
		// 自动刷新
		myrefresh();
	});
	
	// Callback function examples:
	function start_callback(id){
		if(id=='example1'){
			$('#callback_log').html('Timeliner "'+id+'" started');
		}
	}
	function newslide_callback(id, slide){
		if(id=='example1'){
			$('#callback_log').html('Timeliner "'+id+'" changed to slide '+slide);
		}
	}
	function end_callback(id){
		if(id=='example1'){
			$('#callback_log').html('Timeliner "'+id+'" ended');
		}
	}
	function paused_callback(id, slide){
		if(id=='example1'){
			$('#callback_log').html('Timeliner "'+id+'" paused at slide '+slide);
		}
	}
	function resumed_callback(id, slide){
		if(id=='example1'){
			$('#callback_log').html('Timeliner "'+id+'" resumed at slide '+slide);
		}
	}
	function click_callback(id, slide){
		if(id=='example1'){
			$('#callback_log').html('Clicked on slide '+slide+' of Timeliner "'+id+'"');
		}
	}

	function myrefresh(){
		var dateStr = new Date();
		var t = dateStr.getHours();
		if(t == '5'){
			window.location.reload();
		}
		setTimeout('myrefresh()',3600000); //指定1小时刷新一次
	}

	function loadSession(){
		$.ajax({
			async : true,
			cache:false,
			type: 'POST',
			url: '${ctx}/login/login!getSessionId.action',
		});
		setTimeout('loadSession()',600000); //保持session状态
	}
	
	//获取内容
	function loadhtml(){
		$.ajax({
			async : false,
			cache:false,
			type: 'POST',
			url: '${ctx}/menu/menu!loadData.action',
			success:function(data){
				debugger;
				var _htmlContent="";
				if(data){
					for(var i=0,len=data.length;i<len;i++){
						_htmlContent=_htmlContent+ZHFX.fs($("#dpContentTemplate").html(),data[i]["type"],data[i]["id"],data[i]["url"]);
					}					
				}
				$("#example1").append(_htmlContent);
			},	        
			error: function () {//请求失败处理函数
	            alert("数据获取失败！");
	        }
		});
	}		
</script>
</head>
<body>
	<div id="timeline-content">

		<ul id="example1" class="timeliner">


		</ul>
	</div>
	<script type="text/html" id="dpContentTemplate">
	
	<li title="{0}" lang="10">
	    <iframe id="{1}" height="100%" width="100%" frameborder="0" src="{2}&file=gShow.swf&busername=admin&autoLogin=t&showtb=false&bgcolor=0x061733"></iframe>
	</li>
	</script>
</body>
</html>