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
			<input id="sysid" type="hidden" value="${sysid}" />
			<tr>
				<th style="width:147px;">配置变量编码：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-validatebox" required="required" missingMessage="请输入配置编码." id="configCode" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">类型：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-combobox"  required="required" id="type1" />
				</td>
			</tr>
<!-- 			<tr>
				<th style="width:147px;">配置变量名称：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-validatebox"  required="required" missingMessage="请输入配置变量名称." id="configName" />
				</td>
			</tr> -->
			<tr>
				<th style="width:147px;">配置变量名称：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-combobox"  required="required" id="type2" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">配置变量：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-numberbox"  required="required" missingMessage="请输入配置变量." id="configInfo" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">排序：</th>
				<td style="width:250px;">
					<input style="width: 100px;" type="text" class="easyui-numberbox"   id="sort" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">数据来源：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-validatebox" missingMessage="请输入数据来源." id="source" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">是否启用：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-combobox"  required="required" id="enbale" />
				</td>
			</tr>	
			<tr>
				<th style="width:147px;">备注：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-validatebox" missingMessage="请输入备注." id="remark" />
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
	$(document).ready(function (){
		//debugger;
		//初始化下拉框
		initCombobox();
		//页面显示值初始化
		initData();
	});
	
	
	/**
	 * 初始化下拉框
	 */
	function initCombobox() {
		
		//初始化类型下拉框
		$('#type2').combobox({
			data : [{
				name : '基础司下达标准',
				value : 0
			},{
				name : '农经司下达标准',
				value : 1
			},
			{
				name : '投资司下达标准',
				value : 2
			},
			{
				name : '社会司下达标准',
				value : 3
			},
			{
				name : '地区司下达标准',
				value : 4
			},
			{
				name : '能源局-新能源和可再生能源司下达标准',
				value : 5
			},
			{
				name : '经贸司下达标准',
				value : 6
			},
			{
				name : '高技术司下达标准',
				value : 7
			},
			{
				name : '产业司下达标准',
				value : 8
			},
			{
				name : '振兴司下达标准',
				value : 9
			},
			{
				name : '环资司下达标准',
				value : 10
			},
			{
				name : '能源局-煤炭司下达标准',
				value : 11
			},
			{
				name : '就业司下达标准',
				value : 12
			},
			{
				name : '西部司下达标准',
				value : 13
			},
			{
				name : '当前全国人口数',
				value : 14
			},
			{
				name : '当前年度',
				value : 15
			},
			{
				name : '下一年度',
				value : 16
			},
			{
				name : '当前报告期',
				value : 17
			},
			{
				name : '下达总预算',
				value : 18
			}],
			missingMessage:'请选择配置变量名称.', 
	        width: 150,
	        valueField : 'value', 
	        textField : 'name',
	        value : 0
		});
		
		//初始化类型下拉框
		$('#type1').combobox({
			data : [{
				name : '当前全国人口数',
				value : 0
			},{
				name : '当前年度',
				value : 1
			},
			{
				name : '下一年度',
				value : 2
			},
			{
				name : '每年各司局的下达标准',
				value : 3
			},
			{
				name : '下达总预算',
				value : 4
			},
			{
				name : '当前报告期',
				value : 5
			}],
			missingMessage:'请选择配置变量类型.', 
	        width: 150,
	        valueField : 'value', 
	        textField : 'name',
	        value : 0
		});
		
		//初始化是否启用下拉框
		$('#enbale').combobox({
			data : [{
				name : '是',
				value : 1
			},{
				name : '否',
				value : 0
			}],
			missingMessage:'请选择是否启用.', 
	        width: 150,
	        valueField : 'value', 
	        textField : 'name',
	        value : 1
		});
		
	}
	
	/*
	 * 页面显示值初始化
	 */
	function initData() {
		$.ajax({
			url : '${ctx}/sysConfig/sysConfig!getBaseInfo.action',
			dataType : 'text',
			data : {
				sysid : $('#sysid').val()
			},
			success : function(data) {
				if (data) {
					var map = JSON.parse(data);
					//遍历map
					for (var name in map) {
						//根据key值判断值为下拉框还是文本框显示
						if("sort"==name){
							$('#dicForm #' + name).numberbox('setValue', map[name]);
						}
						else if ('type' == name) {
							$('#dicForm #type1').combobox('setValue', map[name]);
						}else if('configInfo' == name){
							$('#dicForm #configInfo').numberbox('setValue', map[name]);
						}else if('enbale' == name){
							$('#dicForm #enbale').combobox('setValue', map[name]);
						}else if('configName' == name){
							$('#dicForm #type2').combobox('setValue', map[name]);
						}else {
							$('#dicForm #' + name).val(map[name]);
						}

					}
				}
			}
		});
	}
	
	/**
	 * 获取表单数据
	 */
	function getFormData() {
		//主键
		var sysid = $('#sysid').val();
		//配置变量编码
		var configCode = $('#configCode').val();
		//配置变量名称
		//var configName = $('#configName').val();
		var configName = $('#type2').combobox('getValue');
		//配置变量
		var configInfo = $('#configInfo').val();
		//数据来源o
		var source = $('#source').val();
		//是否启用
		var enbale = $('#enbale').combobox('getValue');
		//排序
		var sort = $('#sort').val();
		//类型
		var type = $('#type1').combobox('getValue');
		//备注
		var remark = $('#remark').val();
		var obj = {
			'sysid' : sysid,
			'configCode' : configCode,
			'configName' : configName,
			'configInfo' : configInfo,
			'source' : source,
			'type' : type,
			'enbale' : Number(enbale),
			'sort':Number(sort),
			'remark':remark
		};
		return {TSysConfig : obj};
	}
</script>

</body>
</html>