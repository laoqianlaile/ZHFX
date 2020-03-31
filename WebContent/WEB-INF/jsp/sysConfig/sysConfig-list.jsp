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
	<style>
		.search_area table th, .search_area table td, .search_area span input[type='text']{
			color : white
		}
		.messager-question+div{ 
		  color:#000000;
		}
	</style>
</head>
<body>
	<div >
        <div class="centerNr">
            <div>
               <!--查询域 -->
                <div class="search_area">
					<table>  
					   <tr>
						  <th width="200">名称</th>
						  <td>
							  <input type="text" id="name" name="" >
						  </td>
						  <th width="200">类型</th>
						  <td>
								<input style="width: 220px;" type="text" class="easyui-combobox"  required="required" id="type" />
						  </td>
						  <td>
							<input type="button" class="searchBtn" onclick="searchData();" id="search_btn" value="查询" />
						  </td>
						  
						  <td>
							<input type="button" class="searchBtn" onclick="add();" id="add_btn" value="新增" />
						  </td>
						  <td>
							<input type="button" class="searchBtn" onclick="edit();" id="edit_btn" value="编辑" />
						  </td>
						  <td>
							<input type="button" class="searchBtn" onclick="del();" id="delete_btn" value="删除" />
						  </td>
						  <td>
							<input type="button" class="searchBtn" onclick="enableBtn();" id="enable_btn" value="启用" />
						  </td>
						  <td>
							<input type="button" class="searchBtn" onclick="disableBtn();" id="disable_btn" value="禁用" />
						  </td>
					  </tr>
					</table>	
				</div>
				
				<!-- 列表 -->
				<div class="box" style="padding-top:10px;height:500px">
					<!-- 列表 -->
					<table id="dic_datagrid"> 
						
					</table>
				</div>
            </div>
        </div>
	</div>
	
	<script type="text/javascript">
		var dicgrid = null;
		//列显示
		var columns = [ [
						{
							field : 'sysid',
							title : '主键',
							hidden : true,
							align : 'left',
							width : 80
						},
						{
							field : 'configCode',
							title : '配置变量编码',
							align : 'center',
							width : 200
						},
						{
							field : 'configName',
							title : '配置变量名称',
							align : 'left',
							width : 200
						},
						{
							field : 'configInfo',
							title : '配置变量',
							align : 'left',
							width : 200
						},{
							field : 'source',
							title : '数据来源',
							align : 'left',
							width : 200
						},
                        {
                            field : 'typeName',
                            title : '类型',
                            align : 'left',
                            width : 200
                        },
                        {
                            field : 'sort',
                            title : '排序',
                            align : 'center',
                            width : 100
                        },
                        {
                            field : 'enbaleName',
                            title : '是否启用',
                            align : 'center',
                            width : 100
                        }
					  ] ];

		$(function (){
			//初始化列表
			getDicGrid();
			$("*").css("color",'#00d0fe');
			//初始化下拉框
			initCombobox();
		});
		
		/**
		 * 初始化列表
		 */
		function getDicGrid() {
			var param = {
					url : '${ctx}/sysConfig/sysConfig!getConfigList',
					fit: true,//自适应宽度
					pagination : true, //底部分页
					pagePosition : 'bottom', //'top','bottom','both'.
					rownumbers : true,//显示行数
					singleSelect : true,
					height: 550,
					//striped : true,//显示条纹
					pageSize : 20,//每页记录数
					remoteSort : false,//是否通过远程服务器对数据排序
					sortOrder : 'desc',//默认排序方式 'desc' 'asc'
					columns :columns,
					singleSelect : false,//为true时只能选择单行
					onDblClickRow:function(rowIndex, rowData){
						
					},
				    frozenColumns:[[
					                {field:'ck',checkbox:true}
									]],  	
					onLoadSuccess : function(data) {
						//取消所有的已选择项
						$(this).datagrid('clearSelections');
					},
					onRowContextMenu : function(e, rowIndex, rowData) {
						
					}
			};
			if(null != dicgrid){
				$(dicgrid).datagrid("reload");
			} else{
				dicgrid = $('#dic_datagrid').datagrid(param);
			}
		}
		
		/**
		 * 搜索
		 */
		function searchData() {
			//属性名称
			var name = $('#name').val();
			//属性类型
			var type = $('#type').combobox('getValue');
			//重新加载列表
			$('#dic_datagrid').datagrid('reload', {
				name : name,
				type : type
			});
		}
		
		/**
		 * 新增
		 */
		function add() {
			var dialog = $('<div id="dialog"></div>').dialog({
				title: '新增配置',
				width: 800,
				height: 500,
				modal: true,
				maximizable : false,
				href : '${ctx}/sysConfig/sysConfig!input.action',
				buttons : [ {
					text : '保存提交',
					handler : function() {
						$.messager.progress({
							title : '提示信息！',
							text : '数据处理中，请稍后......'
						});
						//验证不通过，不可以提交
						if (!$('#dicForm').form('validate')) {
							$.messager.progress('close');	$.messager.progress('close');
							return;
						}
						var formData = getFormData();
						saveInfo(formData);
						dialog.dialog('destroy');
					}
				},{
					text : '关闭',
					handler : function() {
						dialog.dialog('destroy');
					}
				} ],
				onClose : function() {
					dialog.dialog('destroy');
				}
			});
		}
		
		/**
		 * 保存表单数据
		 */
		function saveInfo(formData) {
			$.ajax({
				url : '${ctx}/sysConfig/sysConfig!save.action',
				dataType : 'text',
				data : {
					entity : encodeURIComponent(JSON.stringify(formData.TSysConfig))
				},
				success : function(data) {
					$.messager.progress('close');
					searchData();
				}
			});
		}
		
		/**
		 * 编辑
		 */
		 function edit() {
			var row = dicgrid.datagrid("getSelections");
			if (row.length==1) {
				var dialog = $('<div id="dialog"></div>').dialog({
					title: '编辑配置',
					width: 800,
					height: 500,
					modal: true,
					maximizable : false,
					href : '${ctx}/sysConfig/sysConfig!input.action?sysid='+row[0].sysid,
					buttons : [{
						text : '保存提交',
						//scriptCharset: "utf-8",
					   // contentType: "application/json; charset=utf-8",
						handler : function() {
							$.messager.progress({
								title : '提示信息！',
								text : '数据处理中，请稍后......'
							});
							//验证不通过，不可以提交
							if(!$('#dicForm').form('validate')){
								$.messager.progress('close');
								return;
							}
							var formData = getFormData();
							saveInfo(formData);
							dialog.dialog('destroy');
						}
					},{
						text : '关闭',
						handler : function() {
							dialog.dialog('destroy');
						}
					}],
					onClose : function() {
						dialog.dialog('destroy');
					}
				});
			} else {
				alert('请选择一条信息！');
			}
		}
		
		/**
		 * 删除
		 */
		function del() {
			var row = dicgrid.datagrid("getSelections"); 
			if (row.length>0) {
				$.messager.confirm("操作提示", "您确认删除所选项吗？", function(r) {
					if (r) {
						var idStr = "";
						for(var i=0;i<row.length;i++){
							idStr = idStr + row[i].sysid + ",";
						}
						idStr = idStr.substring(0,idStr.length-1);
						$.ajax({
							url : '${ctx}/sysConfig/sysConfig!deleteList.action',
							dataType : 'text',
							data : {
								sysid : idStr
							},
							success : function(data) {
								if (data) {
									searchData();
								}
							}
						});
					}
				});
			} else {
				alert('请选择一条信息！');
			}
		}
		
		
		/**
		 * 启用 enableBtn
		 */
		 function enableBtn() {
				var row = dicgrid.datagrid("getSelections");
				if (row.length>0) {
					$.messager.confirm("操作提示", "您确认启用所选项吗？", function(r) {
						if (r) {
							var idStr = "";
							for(var i=0;i<row.length;i++){
								idStr = idStr + row[i].sysid + ",";
							}
							idStr = idStr.substring(0,idStr.length-1);
							$.ajax({
								url : '${ctx}/sysConfig/sysConfig!enableBtn.action',
								dataType : 'text',
								data : {
									sysid : idStr
								},
								success : function(data) {
									if (data) {
										searchData();
									}
								}
							});
						}
					});
				} else {
					alert('请选择一条信息！');
				}
			}
		 
		 
		 
		 /**
		 * 禁用 disableBtn
		 */
		 function disableBtn() {
				var row = dicgrid.datagrid("getSelections");
				if (row.length>0) {
					$.messager.confirm("操作提示", "您确认禁用所选项吗？", function(r) {
						if (r) {
							var idStr = "";
							for(var i=0;i<row.length;i++){
								idStr = idStr + row[i].sysid +",";
							}
							idStr = idStr.substring(0,idStr.length-1);
							$.ajax({
								url : '${ctx}/sysConfig/sysConfig!disableBtn.action',
								dataType : 'text',
								data : {
									sysid : idStr
								},
								success : function(data) {
									if (data) {
										searchData();
									}
								}
							});
						}
					});
				} else {
					alert('请选择一条信息！');
				}
			}
		 
		 
			 /**
			 * 初始化下拉框
			 */
			function initCombobox() {
				//初始化类型下拉框
				$('#type').combobox({
					data : [{
						name : '全部',
						value : ""
					},{
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
					}],
					missingMessage:'请选择配置变量类型.', 
			        width: 150,
			        valueField : 'value', 
			        textField : 'name',
			        value : ""
				});
				
			}
		 
		 
		
	</script>
</body>
</html>