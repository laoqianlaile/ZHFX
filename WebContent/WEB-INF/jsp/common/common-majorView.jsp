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
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/newIndex.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${ctx}/common/extend.js"></script>
<style>
</style>
<script type="text/javascript">
	$(function(){
	    $("#toolbar span").hover(function(){
			$(this).find("ul").css("transform","translateX(-100%)");
		},function(){
			if(!$(".panel").is(":visible")){
				$(this).find("ul").css("transform","translateX(100%)");
			}
		});
	    
	    /*
		$(".btn-Sort").hover(function(){
			$(this).parent().removeClass();
			var addClassName = $(this).attr("id");
			$(this).parent().addClass(addClassName);
		},function(){
			$(this).parent().removeClass();
		});
	    */
		//加载菜单
		getMenuList()
	});
	/**
	 * 初始化菜单
	 */
	function getMenuList() {
		$.ajax({
	        type : 'POST',
	        dataType : 'text',
	        url:  '${ctx}/common/common!getMenuList?type=2',
	        success:function(data){ //请求成功后处理函数。
	        	if (data) {
	        		if(data.indexOf("approvalRecord")==-1){
	        			$('#approvalRecord').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#approvalRecord').addClass('inactive');
	        		}
	        		if(data.indexOf("release")==-1){
	        			$('#release').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#release').addClass('inactive');
	        		}
	        		if(data.indexOf("fineManagement")==-1){
	        			$('#fineManagement').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#fineManagement').addClass('inactive');
	        		}
	        		if(data.indexOf("forecast")==-1){
	        			$('#forecast').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#forecast').addClass('inactive');
	        		}
	        		if(data.indexOf("report")==-1){
	        			$('#report').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#report').addClass('inactive');
	        		}
	        	}else{
        			$('#approvalRecord').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#release').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#fineManagement').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#forecast').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#report').removeAttr('onclick');//去掉a标签中的onclick事件
	        	}
	        },
	        error: function () {//请求失败处理函数
	            alert("数据获取失败！");
	        }
	    });
	}
</script>

</head>

<body><div id="logo_home"></div>
<%-- <div id="head"><span>重点业务</span></div> --%>
<span id="loadClassZD1" class="bgImg1"></span>
<span id="loadClassZD2" class="bgImg2"></span>
<span id="loadClassZD3" class="bgImg3"></span>
<span id="loadClassZD4" class="bgImg4"></span>
<span id="loadClassZD5" class="bgImg5"></span>
<div id="toolbar">
	<span><a href="#" title="返回上一页" class="btn-back" onClick="history.go(-1)"></a></span>
	<span><a href="javascript:ZHFX.toTopUrl('/index.html')" title="主页" class="btn-index"></a></span>
	<span><a href="#" title="菜单" class="btn-list"></a>
		<ul class="listMenu">
			<div id="base-menu">
				<li><a href="javascript:ZHFX.toUrl('/common/common!overview')">基础业务</a></li>
				<li class="active"><a href="javascript:ZHFX.toUrl('${ctx}/common/common!majorView')">重点业务</a></li>
			</div>
		</ul>
	</span>
</div>
<div id="homeBg">
	<div id="zd-contentDiv">
<!-- 		<div id="zd-btn-Div"> -->
		<div id="zd-menuDiv">
		  <div class="btn-Sort zdbtn-1" id="approvalRecord" onClick="ZHFX.toUrl('/common/common!commonView?type=approvalRecord')"><span></span></div>
		  <div class="btn-Sort zdbtn-2" id="release" onClick="ZHFX.toUrl('/common/common!commonView?type=release')"><span></span></div>
		  <div class="btn-Sort zdbtn-3" id="fineManagement"  onClick="ZHFX.toUrl('/common/common!commonView?type=fineManagement')" ><span></span></div>
		  <div class="btn-Sort zdbtn-4" id="forecast" onClick="ZHFX.toUrl('/common/common!commonView?type=forecast')"><span></span></div>
		  <div class="btn-Sort zdbtn-5" id="report" onClick="ZHFX.toUrl('/common/common!commonView?type=report')"><span></span></div>
		</div>
	</div>
</div>
<div class='systemLink' style="display: none">
	<a href="http://kpp.ndrc.gov.cn/" target="_blank">
		<img title='国家重大建设项目库' src="${ctx}/themes/images/img_link_gjzdjsxmk.png">
	</a>
	<a href="https://59.255.137.5/" target="_blank">
		<img title='投资项目在线审批监管平台' src="${ctx}/themes/images/img_link_tzxmzxspjgpt.png">
	</a>
</div>
</body>
</html>