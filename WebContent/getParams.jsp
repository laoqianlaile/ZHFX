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
	<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/icon.css">
	<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/easyui1.4/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/base.js"></script>
    <script type="text/javascript"  src="${ctx}/js/gbasebi.js"></script>
	<style>
		*{
			color : red;
		}
	</style>
	<script type="text/javascript">
		var pArea,pIndustry;
		var para='<%=request.getParameter("paramName") %>';
		//alert(para);
		$(function () {
			$("#showTxt").text(decodeURIComponent(para));
		});
		//传值
		var arrs=[];
	   function testAlert(p,p2) {
		   arrs.push(p);
		   alert(p2);
		   alert(arrs.join("#"));
	   }
       // 更多展现
	   function showMessage(){
	   	   // 页面的值
		   arrs.push($("#pp1").val());
		   arrs.push($("#pp2").val());
		   var params=arrs.join("#");
	   	  window.open()
	   }
  $(function (){
	  //刷新页面
	  $("#btn").bind("click",function () {
		//  debugger;
		  pArea=$("#pp1").val();
		  pIndustry=$("#pp2").val();
		  // 定义容器
		  // container.innerHTML="";
		  var v=$("#movie").val();
//		  alert(v);
		 // v=v+"&pArea="+pArea+"&pIndustry="+pIndustry;
		  v=v+"&pArea=湖北省,湖南省&pIndustry=农业";
		  $("#myobject1").prop("src",v);
		  // 回写URL刷新页面参数
		  $("#movie").val(v);
		  alert($("#container").html());
		  container.innerHTML=$("#container").html();

	  });

  });

	</script>
</head>
<body >
<div style="background-color: #0a5c92;width: 200px;height: 300px">
   <div id="showTxt"></div>
</div>

</body>
</html>