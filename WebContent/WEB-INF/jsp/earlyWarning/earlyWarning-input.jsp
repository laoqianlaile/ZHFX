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

<body>
<div  class="body">
	<form id="dicForm">
		<table id="basicInformation">
			<input id="earlyWarningId" type="hidden" value="${earlyWarningId}" />
			<tr>
				<th style="width:147px;">预警分类：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-combobox" data-options="editable:false"  required="required" missingMessage="请选择预警分类." id="warningType" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">指标名称：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-validatebox"  required="required" missingMessage="请输入指标名称." id="warningName" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">计算公式说明：</th>
				<td style="width:250px;">
<!-- 					<input style="width: 220px;" type="" class="easyui-validatebox"  id="calculationFormula" /> -->
					<textarea rows="3" cols="30" class="easyui-validatebox"  id="calculationFormula" ></textarea>
				</td>
			</tr>
			<tr>
				<th style="width:147px;">是否预警指标：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-combobox"  data-options="editable:false" missingMessage="请选择是否预警指标." id="iswarning" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">排序：</th>
				<td style="width:250px;">
					<input style="width: 100px;" type="text" class="easyui-numberbox"   id="sort" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">预警值：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-numberbox" missingMessage="请输入预警值." id="warningValue" />
				</td>
			<tr>
				<th style="width:147px;">预警等级：</th>
				<td style="width:250px;">
				<span id="cancelSelect_warningLevel" title="双击取消选择">
					<input style="width: 220px;" type="text" class="easyui-combobox" data-options="editable:false" missingMessage="请选择预警等级." id="warningLevel" />
				</span>
				</td>
			</tr>
			<tr>
				<th style="width:147px;">预警区间：</th>
				<td style="width:450px;">
					<input style="width: 220px;" type="text" class="easyui-numberbox"  id="statrWarningValue" />-<input style="width: 220px;" type="text" class="easyui-numberbox"  id="endWorningValue" />
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
	$(document).ready(function (){
		//
		//初始化下拉框
		initCombobox();
		//页面显示值初始化
		initData();
	});
	// 清空项目类型输入信息
	$("#cancelSelect_warningLevel").dblclick(function(){
		$("#warningLevel").combobox("clear");
	});
	/**
	 * 初始化下拉框
	 */
	function initCombobox() {
		//初始化类型下拉框
			$('#warningType').combobox({
			data : [{
				name : '分解情况预警',
				value : 0
			},{
				name : '开工预警',
				value : 1
			},{
				name : '资金预警',
				value : 2
			},{
				name : '其他预警',
				value : 3
			}],
// 			missingMessage:'请选择配置变量类型.', 
	        width: 150,
	        valueField : 'value', 
	        textField : 'name',
	        value : 0
		});
		$('#iswarning').combobox({
			data : [{
				name : '否',
				value : 0
			},{
				name : '是',
				value : 1
			}],
// 			missingMessage:'请选择配置变量类型.', 
	        width: 150,
	        valueField : 'value', 
	        textField : 'name',
	        value : 0
		});
		$('#warningLevel').combobox({
			data : [{
				name : '1级',
				value : 1
			},{
				name : '2级',
				value : 2
			}],
// 			missingMessage:'请选择预警级别.', 
	        width: 150,
	        valueField : 'value', 
	        textField : 'name'
		});
	}
	
	/*
	 * 页面显示值初始化
	 */
	function initData() {
		$.ajax({
			url : '${ctx}/earlyWarning/earlyWarning!getBaseInfo.action',
			dataType : 'text',
			data : {
				earlyWarningId : $('#earlyWarningId').val()
			},
			success : function(data) {
				if (data) {
					var map = JSON.parse(data);
					
					//遍历map
					for (var name in map) {
						//根据key值判断值为下拉框还是文本框显示
						if("iswarning"==name||"warningType"==name||"warningLevel"==name){
							$('#dicForm #' + name).combobox('setValue', map[name]);
						}else if("calculationFormula"==name||"warningName"==name){
							$('#dicForm #' + name).val(map[name]);
						}
						else if(map[name]!=null&&map[name]!='') {
							$('#dicForm #' + name).numberbox('setValue',Number(map[name]));
						}

					}
				}else{
					$('#dicForm #warningLevel').combobox('clear');
				}
			}
		});
	}
	
	/**
	 * 获取表单数据
	 */
	function getFormData() {
		
		//主键
		var id = $('#earlyWarningId').val();
		//类型
		var warningType = $('#warningType').combobox('getValue');
		//	指标名称
		var warningName = $('#warningName').val();
		//配置变量编码
		var calculationFormula = $('#calculationFormula').val();
		//类型
		var iswarning = $('#iswarning').combobox('getValue');
		//配置变量名称
		var warningValue = $('#warningValue').val();
		//类型
		var warningLevel = $('#warningLevel').combobox('getValue');
		//配置变量
		var statrWarningValue = $('#statrWarningValue').val();
		//数据来源o
		var endWorningValue = $('#endWorningValue').val();
		//排序
		var sort = $('#sort').val();
		var obj = {
			'id' : id,
			'warningType' : warningType,
			'warningName':warningName,
			'calculationFormula' : calculationFormula,
			'iswarning' : iswarning,
			'warningValue' : warningValue,
			'warningLevel' : warningLevel,
			'statrWarningValue' : statrWarningValue,
			'endWorningValue' :endWorningValue,
			'sort':sort
		};
		return {EarlyWarning : obj};
	}
</script>

</body>
</html>