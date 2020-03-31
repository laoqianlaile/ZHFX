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
<!-- 字典项新增画面内容开始 -->
<div  class="body">
	<form id="itemForm">
		<table fit="true" class="table2" style="margin: 0px">
			<tr>
						<td>层级关系
						</td>
						<td>
							<input type="radio" id="isParentY" name="isParent" value="Y" />同级
							<input type="radio" name="isParent" checked id="isParentN" value="N" />子级
						</td>
					</tr>
					<tr>
						<td>
							<span class="redX_z">字典项编号</span>
						</td>
						<td >
							<input class="easyui-textbox" id="itemKeyId" type="text" name="dictionaryItems.itemKey" required="required" data-options="validType:['maxLength[30]','sqlkey']"/>
							<b class="red-star">*</b>
						</td>
					</tr>
					<tr>
						<td>
							<span class="redX_z">字典项名称</span>
						</td>
						<td>
							<input class="easyui-textbox" id="itemValueId" type="text" name="dictionaryItems.itemValue" required="required" data-options="validType:['maxLength[100]','sqlkey']"/>
							<b class="red-star">*</b>
						</td>
					</tr>
					<tr>
						<td>
							<span class="redX_z">状态</span>
						</td>
						<td>
							<input class="easyui-combobox" name="dictionaryItems.enabled" id="sltEnabled" style="width:160px">
							<b class="red-star">*</b>
						</td>
					</tr>
					<tr id="TrArea">
						<td>
							<span class="redX_z">标记</span>
						</td>
						<td>
							<select class="easyui-combobox" id="sltFlag"  name="dictionaryItems.flag">
							<option value="0"></option>
							<option value="1">省级以上行政区划</option>
							<option value="2">直辖市</option>
							<option value="3">单列市</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<span class="redX_z">排序</span>
						</td>
						<td>
							<input class="easyui-numberbox" id="sort" data-options="min:0,max:999,precision:0" type="text" name="dictionaryItems.pxh" required="required" maxlength="3"/>
							<b class="red-star">*</b>
						</td>
					</tr>
		</table>
		<div>
			<!-- 以下定义隐藏域 -->
			<input id="itemParent" name="dictionaryItems.parentItemId"  type="hidden"/>
			<input id="groupNo2" name="dictionary.groupNo" type="hidden" />
			<input type="hidden" id="groupNo" name="groupNo" value="${groupNo}"/>
			<input type="hidden" id="version" name="suggest.version" value='${version}'></input>
		</div>
	</form>
</div>
<script type="text/javascript">
	var itemPid =null;
	var itemId =null;
	////////////////////////////////////
	//字典项新增弹窗；
	function openDictItemAddDialog(groupNo){
		if(groupNo!=null&&groupNo!=""){
			if(groupNo==1){
				$("#TrArea").css("display", "table-row");
		    } else {
		    	$("#TrArea").css("display", "none");
		    }
				$("#sltEnabled").combobox("setValue","1");
				$("#groupNo2").val(groupNo);
		}	
	};
	///////////////////////////////////////////
	// 新增字典项信息
	function saveDictItems(){
		var info = "字典项新增成功";
		var url = '${ctx}/dictionary/dictionary!saveDictItems';
		var itemParent = null;
		if($("#isParentY").prop("checked")) {
				//同级
				if(itemPid&&itemPid!="null") {
					itemParent = itemPid;
				}
			} else {
				//子级
				if(itemId&&itemId!="null") {
					itemParent = itemId;
				}
			}
		$("#itemParent").val(itemParent);
		// 执行下表单验证操作
		var valid = $('#itemForm').form('validate');
		if(!valid){
			return false;
		}
		var reponseText=$.ajax({
			async : false,
	        cache:false,
	        data:  $('#itemForm').form().serializeArray(),
	        type: 'POST',  
	        dataType : 'text', 
	    	url: url,
	        error: function () {//请求失败处理函数  
	        }
		}).responseText;
		
		var  obj=null;
		if(reponseText){
			obj = $.parseJSON(reponseText);
	    }
		return obj; 
	};
	//////////////
	//x下拉框
	$('#sltEnabled').combobox({
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
		debugger;
		var groupNo='${param.groupNo}';
		 itemPid = '${param.itemPid}';
		 itemId ='${param.itemId}';
		 openDictItemAddDialog(groupNo);		
	});
	//////////////////////////////////////
</script>
</body>
</html>