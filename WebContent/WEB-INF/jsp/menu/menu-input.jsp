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
			<input id="guid" type="hidden" value="${guid}" />
			<tr>
				<th style="width:147px;">业务模块：</th>
				<td style="width:250px;">
					<input type="text" id="business" style="width:100px;" class="easyui-combobox" required="required" missingMessage="请输入业务模块."  />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">上级菜单：</th>
				<td>
					<input type="text" id="parent" style="width:220px;" class="easyui-combobox"  />
				</td>	
			</tr>
			<tr>
				<th style="width:147px;">菜单名称：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-validatebox" required="required" missingMessage="请输入菜单名称." id="menuName" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">模块标题名称：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-validatebox" required="required" missingMessage="请输入模块名称." id="menuTitle" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">页面高度(px)：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-numberbox"   id="pageHeight" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">页面宽度(px)：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-numberbox"   id="pageWidth" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">排序：</th>
				<td style="width:250px;">
					<input style="width: 220px;" type="text" class="easyui-numberbox"   id="sort" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">链接地址：</th>
				<td style="width:250px;">
					<input style="width: 600px;" type="text" class="easyui-validatebox" required="required" missingMessage="请输入链接地址." id="url" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">类型：</th>
				<td style="width:250px;">
					<input style="width: 100px;" type="text" class="easyui-validatebox" missingMessage="请输入类型." id="typeFlag" />
				</td>
			</tr>
			<tr>
				<th style="width:147px;">是否启用：</th>
				<td style="width:250px;">
					<input style="width: 100px;" type="text" required="required" id="enable" />
				</td>
			</tr>
		</table>
	</form>
	<div>
		<p>
          1.工程路径 #rootpath#  例如：#rootpath#/projectFile/projectFile!list  对应的URL ：http://127.0.0.1:8080/BI/projectFile/projectFile!list
		</p>
	</div>
</div>

<script type="text/javascript">
	// 菜单的命名空间
	var MEUN_NS={};
	// 设置闭包保证变量不受外界干扰
	;(function (MEUN_NS){
		/**
		 * 初始化下拉框
		 */
		MEUN_NS.initCombobox=function() {
			//初始化是否启用下拉框
			$('#business').combobox({
				data : [{
					name : '政府投资管理',
					value : 0
				},{
					name : '企业投资项目管理',
					value : 2
				},{
					name : '平台服务情况',
					value : 1
				},{
					name : '项目监测',
					value : 3
				},{
					name : '预警预测分析',
					value : 5
				},{
					name : '不显示',
					value : 6
				},{
					name : '大屏',
					value : 4
				}],
				missingMessage:'请选择业务模块.',
				width: 100,
				valueField : 'value',
				textField : 'name',
				value : 0
			});
			//初始化上级菜单下拉框
			$('#parent').combobox({
				url : '${ctx}/menu/menu!getParentList.action',
				missingMessage:'请选择上级菜单.',
				width: 100,
				valueField : 'value',
				textField : 'name'
			});
			//初始化是否启用下拉框
			$('#enable').combobox({
				data : [{
					name : '启用',
					value : 1
				},{
					name : '停用',
					value : 0
				}],
				missingMessage:'请选择是否启用.',
				width: 100,
				valueField : 'value',
				textField : 'name',
				value : 1
			});

		}

		/*
		 * 页面显示值初始化
		 */
		MEUN_NS.initData=function () {
			$.ajax({
				url : '${ctx}/menu/menu!getBaseInfo.action',
				dataType : 'text',
				data : {
					guid : $('#guid').val()
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
							else if ('enable' == name || 'parent' == name || 'business' == name) {
								$('#dicForm #' + name).combobox('setValue', map[name]);
							}
							else {
								$('#dicForm #' + name).val(map[name]);
							}

						}
					}
				}
			});
		}

	})(MEUN_NS)


	
	/**
	 * 获取表单数据
	 */
	function getFormData() {
		//主键
		var guid = $('#guid').val();
		//业务模块
		var business = $('#business').combobox('getValue');
		//上级菜单
		var parent = $('#parent').combobox('getValue');
		//菜单名称
		var name = $('#menuName').val();
		//链接地址
		var url = $('#url').val();
		//类型
		var typeFlag = $('#typeFlag').val();
		//是否启用
		var enable = $('#enable').combobox('getValue');
		//新增四个
		var  pageHeight=$("#pageHeight").numberbox("getValue");
		var  pageWidth=$("#pageWidth").numberbox("getValue");
		var  menuTitle=$("#menuTitle").val();
		var  sort=$("#sort").numberbox("getValue");
		var obj = {
			'guid' : guid,
			'menuFlag' : business,
			'parentGuid' : parent,
			'name' : name,
			'url' : url,
			'typeFlag' : typeFlag,
			'enable' : Number(enable),
			'pageHeight':pageHeight,
			'pageWidth':pageWidth,
			'menuTitle':menuTitle,
			'sort':sort
		};
		return {TFgwStatisMenu : obj};
	}
	$(document).ready(function (){
		//debugger;
		//初始化下拉框
		MEUN_NS.initCombobox();
		//页面显示值初始化
		MEUN_NS.initData();
	});

</script>

</body>
</html>