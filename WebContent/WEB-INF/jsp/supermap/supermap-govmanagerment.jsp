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
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/prefixfree.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.transit.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/common/extend.js"></script>
<script type="text/javascript" src="${ctx}/js/forJavaScript/libs/SuperMap.Include.js"></script>

<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/icon.css">

<script src="${ctx}/js/base.js"></script>

</head>


<body>
<div style="height:100%;margin:0 20px;">
	<div class="search_area">
		<table>
			<tr>
			  <th width="150">所属行业</th>
			  <td>
				  <input type="text" id="industry_code" name="" >
			  </td>
			  <th>建设地点</th>
			  <td>
				  <input type="text" id="project_region" name=""/>
			  </td>
			  <th>开工时间</th>
			  <td>
				  <input id="actualStartTime1" name="" type="text"  class="easyui-datebox" data-options="editable:false">  
			  </td>
			  <th>至</th>
			  <td>
				  <input id="actualStartTime2" name="" type="text" class="easyui-datebox" data-options="editable:false">
			  </td>
			</tr>
			<tr>
			  <th>竣工时间</th>
			  <td>
				  <input id="actualEndTime1" name="" type="text"  class="easyui-datebox" data-options="editable:false">  
			  </td>
			  <th>至</th>
			  <td> 
				  <input id="actualEndTime2" name="" type="text" class="easyui-datebox" data-options="editable:false">
			  </td>
			  <td colspan="4">
					<input type="button" class="searchBtn" id="search_btn" value="查询" />
					<input type="button" class="searchBtn" id="clear_conditions" value="重置" />
			  </td>
			</tr>
		</table>	
	</div>
	<div class="chartDiv" style="top:100px;left:20px;right:20px;bottom:10px;">
		<div class="chartHeight" style="top:0px;left:0px;right:0px;bottom:0px;">
			<iframe allowtransparency="true" frameborder="0" id="ddd" name="kkk" width="100%" height="100%" scrolling="no" src="${ctx}/supermap/supermap-queryByGeometry"></iframe>
		</div>
	</div>
</div>

<script type="text/javascript">


$(function(){
	
	
	
});


/**
 * 重置查询条件
 */
$("#clear_conditions").click(function(){
	$("#project_region").combotree("clear"); //建设地点
	$("#industry_code").combotree("clear");  //所属行业
	$("#actualStartTime1").datebox("clear"); //开工时间1
	$("#actualStartTime2").datebox("clear"); //开工时间2
	$("#actualEndTime1").datebox("clear"); //竣工时间1
	$("#actualEndTime2").datebox("clear"); //竣工时间2
	
});

/**
 * 查询按钮
 */
$("#search_btn").click(function(){
	var projectRegion=$("#project_region").combotree("getValues").toString();          //建设地点
	var industryCode=$("#industry_code").combotree("getValues").toString();            //所属行业
	var actualStartTime1=$("#actualStartTime1").datebox("getValue");//开工时间1
	var actualStartTime2=$("#actualStartTime2").datebox("getValue");//开工时间2
	var actualEndTime1=$("#actualEndTime1").datebox("getValue");//竣工时间1
	var actualEndTime2=$("#actualEndTime2").datebox("getValue");//竣工时间2
	kkk.datagridInfo(projectRegion,industryCode,actualStartTime1,actualStartTime2,actualEndTime1,actualEndTime2);
});



//建设地点（可多选）
$("#project_region").combotree({
	url:'${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',
	multiple:true
});

//所属行业（可多选）
$("#industry_code").combotree({
	url:'${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryCode',
	multiple:true
});

//
	        
</script>
</body>
</html>