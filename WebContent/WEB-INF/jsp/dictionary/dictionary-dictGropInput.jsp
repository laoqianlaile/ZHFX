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
<title>综合分析</title>
<style type="text/css">
	 input {
		background-color: #0a5c92;
	}
</style>
</head>
<body class="easyui-layout" data-options="fit:true">  
<!--新增画面内容开始 -->
<div  class="body">
	<form id="dicForm">
		<table fit="true" class="table2" style="margin: 0px">
		<input type="hidden" id="groupNo" name="dictionary.groupNo" value="${dictionary.groupNo}"/>
			<tr>
				<td>
					<span class="redX_z">字典组编号</span>
				</td>
				<td>
					<input id="groupKey" name="dictionary.groupKey" class="easyui-validatebox" required="required" type="text" data-options="validType:['maxLength[10]','sqlkey']"/>
					<b class="red-star">*</b>
					<span style="margin-left:9px;">(</span>
				    <span >带“*”为必填</span>
				    <span >)</span>
				</td>
			</tr>
			<tr>
				<td >
					<span class="redX_z">字典组名称</span>
				</td>
				<td>
					<input id="groupName" class="easyui-validatebox" name="dictionary.groupName" type="text" required="required" data-options="validType:['maxLength[15]','sqlkey']"/>
					<b class="red-star">*</b>
				</td>
			</tr>
			<tr>
				<td >
					<span class="redX_z">状态</span>
				</td>
				<td>
					<input style="width: 160px;" type="text" data-options="editable:false"name="dictionary.enabled" id="enabled"/>
					<b class="red-star">*</b>
				</td>
			</tr>		
		</table>
		<div>
			<!-- 以下定义隐藏域 -->
			<input id="isAdd" name="conditions.isAdd" type="hidden" />
			<input type="hidden" id="version" name="conditions.version" ></input>
		</div>
	</form>
</div>
<script type="text/javascript">
var groupNo='${param.groupNo}';
	//字典组新增弹窗；
	//初始化默认显示启用  
	function openDictGroupAddDialog2(groupNo){
		debugger;
		$("#dicForm #enabled").combobox("setValue",'1');
		if(groupNo != ''){
		        	$("#groupKey").val('${dictionary.groupKey}');
		        	$("#groupKey").attr("readonly", true)
		        	$("#groupName").val('${dictionary.groupName}');
		        	$("#enabled").combobox('setValue','${dictionary.enabled}');
		        	$("#isAdd").val("false");
		        	$("#version").val('${dictionary.version}');
		}else{
			$("#groupKey").val('');
	    	$("#groupName").val('');
	    	$("#version").val('1');
			$("#enabled").combobox("setValue","1");
			$("#isAdd").val("true");
		}
	};
	///////////////////////////////////////////////////////
	// 新增字典组信息
	function saveDictGroup2(){
		var isAdd = $("#isAdd").val();
		var info = "字典组新增成功";
		var url = '${ctx}/dictionary/dictionary!saveDictGroup';
		if(isAdd=="false"){
			info ="字典组修改成功";
		}
		// 执行下表单验证操作
		var valid = $('#dicForm').form('validate');
		if(!valid){
			return false;
		}
		var reponseText=$.ajax({
			async : false,
	        cache:false,
	        data:  $('#dicForm').form().serializeArray(),
	        type: 'POST',  
	        dataType : 'json', 
	    	url: url,
	        error: function () {//请求失败处理函数  
	        }
		}).responseText;
		
		var  obj=null;
		if(reponseText){
	        obj = $.parseJSON(reponseText);
		}
		return obj; 
	}
	
	$('#enabled').combobox({
		data : [{
			name : '启用',
			value : 1
		},{
			name : '禁用',
			value : 0
		}],
        width: 150,
        valueField : 'value', 
        textField : 'name'
	});
	//////////////////////////////////////////
	$(function(){
		openDictGroupAddDialog2(groupNo);		
	});
	//////////////////////////////////////
</script>
</body>
</html>