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
		$(".btn-Sort").hover(function(){
			$(this).parent().removeClass();
			var addClassName = $(this).attr("id");
			$(this).parent().addClass(addClassName);
		},function(){
			$(this).parent().removeClass();
		});
		getMenuList()
		//登录成功后，预先保存模版相关信息到session中
		//preSaveSessionInfo();
	});
	/**
	 * 初始化菜单
	 */
	function getMenuList() {
		$.ajax({
	        type : 'POST',
	        dataType : 'text',
	        url:  '${ctx}/common/common!getMenuList?type=0',
	        success:function(data){ //请求成功后处理函数。
	        	if (data) {
	        		if(data.indexOf("defView")==-1){
	        			$('#defView').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#bgImg0').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#defView').addClass('inactive');
	        			$('#bgImg0').addClass('inactive');
	        		}
	        		if(data.indexOf("fivePlan")==-1){
	        			$('#fivePlan').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#fivePlan').addClass('inactive');
	        		}
	        		if(data.indexOf("rollPlan")==-1){
	        			$('#rollPlan').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#rollPlan').addClass('inactive');
	        		}
	        		if(data.indexOf("yearReport")==-1){
	        			$('#yearReport').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#yearReport').addClass('inactive');
	        		}
	        		if(data.indexOf("fund")==-1){
	        			$('#fund').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#fund').addClass('inactive');
	        		}
	        		if(data.indexOf("projectDeal")==-1){
	        			$('#projectDeal').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#projectDeal').addClass('inactive');
	        		}
	        		if(data.indexOf("projectView")==-1){
	        			$('#projectView').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#projectView').addClass('inactive');
	        		}
	        		if(data.indexOf("itemView")==-1){
	        			$('#itemView').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#itemView').addClass('inactive');
	        		}
	        		if(data.indexOf("plateType")==-1){
	        			$('#plateType').removeAttr('onclick');//去掉a标签中的onclick事件
	        			$('#plateType').addClass('inactive');
	        		}
	        	}else{
        			$('#bgImg0').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#defView').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#fivePlan').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#rollPlan').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#yearReport').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#fund').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#projectDeal').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#projectView').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#itemView').removeAttr('onclick');//去掉a标签中的onclick事件
        			$('#plateType').removeAttr('onclick');//去掉a标签中的onclick事件
	        	}
	        },
	        error: function () {//请求失败处理函数
	            alert("数据获取失败！");
	        }
	    });
	}
</script>
</head>

<body>
<div id="logo_home"></div>
<%-- <div id="head"><span>基础业务</span></div> --%>
<span id="loadClass1" class="bgImg1"></span>
<span id="loadClass2" class="bgImg2"></span>
<span id="loadClass3" class="bgImg3"></span>
<span id="loadClass4" class="bgImg4"></span>
<span id="loadClass5" class="bgImg5"></span>
<span id="loadClass6" class="bgImg6"></span>
<div id="toolbar">
	<span><a href="#" title="返回上一页" class="btn-back" onclick="history.go(-1)"></a></span>
	<span><a href="javascript:ZHFX.toTopUrl('/index.html')" title="主页" class="btn-index"></a></span>
	<span class="MenuList"><a href="#" title="菜单" class="btn-list"></a>
		<ul class="listMenu">
			<div id="base-menu">
				<li class="active"><a href="javascript:ZHFX.toUrl('/common/common!overview')">基础业务</a></li>
				<li><a href="javascript:ZHFX.toUrl('/common/common!majorView')">重点业务</a></li>
			</div>
		</ul>
	</span>
</div>
<div id="homeBgjc" >
	<%-- <div id="home-contentDiv">
		<div id="btn-Div">
		  <div class="btn-Sort btn-1" id="bgImg1" onclick="ZHFX.toUrl('/common/common!commonView?type=defView')"><span></span></div>
		  <div class="btn-Sort btn-2" id="bgImg2" onclick="ZHFX.toUrl('/common/common!commonView?type=fivePlan')"><span></span></div>
		  <div class="btn-Sort btn-3" id="bgImg3" onclick="ZHFX.toUrl('/common/common!commonView?type=rollPlan')"><span></span></div>
		  <div class="btn-Sort btn-4" id="bgImg4" onclick="ZHFX.toUrl('/common/common!commonView?type=projectDeal')"><span></span></div>
		  <div class="btn-Sort btn-5" id="bgImg5" onclick="ZHFX.toUrl('/common/common!commonView?type=yearReport')"><span></span></div>
		  <div class="btn-Sort btn-6" id="bgImg6" onclick="ZHFX.toUrl('/common/common!commonView?type=fund')"><span></span></div>
		</div>
	</div> --%>
	
	<div id="home_menu_wrap">
		<div class="home_menu_content">
			<div class="home_menu_left">
				<a class="home_link link_l1" id="projectDeal" onclick="ZHFX.toUrl('/common/common!commonView?type=projectDeal')" href="javascript:;"></a>
				<a class="home_link link_l2" id="projectView" onclick="ZHFX.toUrl('/common/common!commonView?type=projectView')" href="javascript:;"></a>
				<a class="home_link link_l3" id="itemView" onclick="ZHFX.toUrl('/common/common!commonView?type=itemView')" href="javascript:;"></a>
				<a class="home_link link_l4" id="plateType" onclick="ZHFX.toUrl('/common/common!commonView?type=plateType')" href="javascript:;"></a>
			</div>
			<div class="home_menu_right">
				<a class="home_link link_r1" id="bgImg0" onclick="ZHFX.toUrl('/common/common!commonView?type=defView')" href="javascript:;"></a>
				<a class="home_link link_r2" id="defView" onclick="ZHFX.toUrl('/common/common!commonView?type=defView')" href="javascript:;"></a>
				<a class="home_link link_r3" id="fivePlan" onclick="ZHFX.toUrl('/common/common!commonView?type=fivePlan')"  href="javascript:;"></a>
				<a class="home_link link_r4" id="rollPlan" onclick="ZHFX.toUrl('/common/common!commonView?type=rollPlan')"  href="javascript:;"></a>
				<a class="home_link link_r5" id="yearReport"onclick="ZHFX.toUrl('/common/common!commonView?type=yearReport')" href="javascript:;"></a>
				<a class="home_link link_r6" id="fund" onclick="ZHFX.toUrl('/common/common!commonView?type=fund')" href="javascript:;"></a>
			</div>
		</div>
	</div>
</div>
<div class='systemLink' style="display:none">
	<a href="http://kpp.ndrc.gov.cn/" target="_blank">
		<img title='国家重大建设项目库' src="${ctx}/themes/images/img_link_gjzdjsxmk.png">
	</a>
	<a href="https://59.255.137.5/" target="_blank">
		<img title='投资项目在线审批监管平台' src="${ctx}/themes/images/img_link_tzxmzxspjgpt.png">
	</a>
</div>
</body>
</html>