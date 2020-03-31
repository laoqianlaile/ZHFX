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
	<link rel="stylesheet" type="text/css" href="${ctx}/homeLayout/hemes/css/global.css"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/homeLayout/themes/css/home.css"/>
	<style type="text/css">
		#logo_home{
			width:480px;
			height:86px; /*修改高度*/
			background:url(${ctx}/themes/css/images/logo.png) center center no-repeat;
			position:absolute;
			z-index:1;
			top:10px;
			left:50%;
			margin-left:-200px;
		}
	</style>
</head>
<body>
<div id="logo_home"></div>
<div id="container">
	<div class="ui_base u_p3d">
		<div class="ball_c ball_c_1" onclick="ZHFX.toUrl('/common/common!commonView?type=defView&typeModule=0')"  style="cursor: pointer">
			<img src="${ctx}/homeLayout/themes/images/img_1.png">
			<span>政府投资管理</span>
		</div>
		<div class="ball_c ball_c_2" onclick="ZHFX.toUrl('/common/common!commonView?type=operateSatuation&typeModule=2')"  style="cursor: pointer">
			<img src="${ctx}/homeLayout/themes/images/img_2.png">
			<span>企业投资项目<br>管理</span>
		</div>
		<div class="base u_p3d base_1">
			<div class="pan pan_1"></div>
			<div class="ball_base u_p3d ball_1">
				<div class="ball"  onclick="ZHFX.toUrl('/common/common!commonView?type=warn_analyze&typeModule=5')"  style="cursor: pointer">
					<img src="${ctx}/homeLayout/themes/images/img_3.png">
					<span>预测预警<br>分析</span>
				</div>
			</div>
		</div>
		<div class="base u_p3d base_2">
			<div class="pan pan_2"></div>
			<div class="ball_base u_p3d ball_2">
				<div class="ball"  onclick="ZHFX.toUrl('/common/common!commonView?type=life_cycle_overview&typeModule=3')" style="cursor: pointer">
					<img src="${ctx}/homeLayout/themes/images/img_4.png">
					<span>项目监测</span>
				</div>
			</div>
		</div>
		<div class="base u_p3d base_3">
			<div class="pan pan_3"></div>
			<div class="ball_base u_p3d ball_3">
				<div class="ball" onclick="ZHFX.toUrl('/common/common!commonView?type=plat_service&typeModule=1')"  style="cursor: pointer">
					<img src="${ctx}/homeLayout/themes/images/img_5.png">
					<span>平台服务<br>情况</span>
				</div>
			</div>
		</div>

	</div>
</div>

<canvas id="c"></canvas>

<script src='${ctx}/homeLayout/js/jquery-1.11.3.min.js'></script>
<script src='${ctx}/homeLayout/js/dat.gui.min.js'></script>
<script src="${ctx}/homeLayout/js/index.js"></script>
<script>
	$(function(){
		$('.ball').hover(function(){
			$('.base').addClass('animation_paused');
		},function(){
			$('.base').removeClass('animation_paused');
		});
		$('.ball').click(function(){
			$('.base').toggleClass('animation_paused');
		});
	});
</script>
<script type="text/javascript" src="${ctx}/common/extend.js"></script>
</body>
</html>
