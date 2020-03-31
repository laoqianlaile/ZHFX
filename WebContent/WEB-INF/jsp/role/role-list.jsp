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
						  </td>						  <td>
							<input type="button" class="searchBtn" onclick="searchData();" id="search_btn" value="查询" />
						  </td>
						  <td>
							<input type="button" class="searchBtn" onclick="roleMenuBtn();" id="roleMenu_btn" value="角色授权" />
						  </td>
					  </tr>
					</table>	
				</div>
				
				<!-- 列表 -->
				<div class="box" style="padding-top:10px;height:500px">
					<!-- 列表 -->
					<table id="role_datagrid"> 
						
					</table>
				</div>
            </div>
        </div>
	</div>
	
	<script type="text/javascript">
		var rolegrid = null;
		//列显示
		var columns = [ [
						{
							field : 'roleGuid',
							title : '主键',
							hidden : true,
							align : 'left',
							width : 80
						},
						{
							field : 'roleName',
							title : '角色名称',
							align : 'center',
							width : 200
						},
						{
							field : 'roleDesc',
							title : '角色描述',
							align : 'left',
							width : 500
						},
						{
							field : 'roleType',
							title : '角色类型',
							align : 'center',
							width : 200,
							formatter:formatRoleType
						}
					  ] ];

		$(function (){
			//初始化列表
			getRoleGrid();
			$("*").css("color",'#00d0fe');
		});
		
		/**
		 * 初始化列表
		 */
		function getRoleGrid() {
			var param = {
					url : '${ctx}/role/role!getRoleData.action',
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
			if(null != rolegrid){
				$(rolegrid).datagrid("reload");
			} else{
				rolegrid = $('#role_datagrid').datagrid(param);
			}
		}
		//角色类型装换
		function formatRoleType(value,rowData,rowIndex){
			if(value=="02"){
				return "项目审核";
			}else if(value=="09"){
				return "普通角色";
			}else{
				return value;
			}
		}
		/**
		 * 搜索
		 */
		function searchData() {
			//属性名称
			var name = $('#name').val();
			//重新加载列表
			$('#role_datagrid').datagrid('reload', {
				roleName : name
			});
		}
	/**
	 * 角色授权
	 */
	function roleMenuBtn(){
		var rows=$("#role_datagrid").datagrid("getSelections");
		if(rows.length > 0){
			if(rows.length > 1){
				alert("只能选择一个角色授权！");
				return false;
			}
		}else{
			alert("至少选择一个角色授权！");
			return false;
		}
		var roleGuid = rows[0].roleGuid;
		var dialog = $('<div id="dialog"></div>').dialog({
			title:"角色授权",
			width: 900,
			height: 500,
			lock: true,
			min:true,
			max:true,
			href : '${ctx}/role/role!openRoleMenuDialog?roleGuid='+roleGuid,
			buttons : [ {
				text : '授权',
				handler : function() {
			    	var message=updateMenus();
			    	if(message){
						if(message == "0"){
							// 用户更新或增加成功
							alert("角色授权成功！");
					    		// 重新加载一览画面
					    		$("#tt4").datagrid('reload');
					    		dialog.dialog('destroy');
						} else{
							// 错误提示
							 $.dialog.tips(message.info);
						}
					// 操作失败
					} else{
						$.dialog.alert("操作失败！","error");
					}
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
	};
	</script>
</body>
</html>