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
							field : 'id',
							title : '主键',
							hidden : true,
							align : 'left',
							width : 80
						},
						{
							field : 'warningType',
							title : '预警分类',
							align : 'center',
							width : 200,
							formatter:typeFormatter
						},
						{
							field : 'warningName',
							title : '指标名称',
							align : 'left',
							width : 300
						},
						{
							field : 'calculationFormula',
							title : '计算公式说明',
							align : 'left',
							width : 500
						},{
							field : 'iswarning',
							title : '是否预警指标',
							align : 'center',
							width : 100,
							formatter:iswarningFormatter
						},
                        {
                            field : 'warningValue',
                            title : '预警值',
                            align : 'left',
                            width : 100
                        },
                        {
                            field : 'warningLevel',
                            title : '预警等级',
                            align : 'center',
                            width : 100,
							formatter:levelFormatter
                        },
                        {
                            field : 'statr_warning_value',
                            title : '预警区间',
                            align : 'left',
                            width : 100,
                            formatter:fromToFormatter
                        },
                        {
                            field : 'sort',
                            title : '排序',
                            align : 'left',
                            width : 100
                        }
					  ] ];

		$(function (){
			//初始化列表
			getDicGrid();
			$("*").css("color",'#00d0fe')
		});
		
		/**
		 * 初始化列表
		 */
		function getDicGrid() {
			var param = {
					url : '${ctx}/earlyWarning/earlyWarning!getEarlyWarningList',
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
					onDblClickRow:function(rowIndex, rowData){
						
					},
					 	
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
			
			//重新加载列表
			$('#dic_datagrid').datagrid('reload', {
				name : name
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
				href : '${ctx}/earlyWarning/earlyWarning!input.action',
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
				url : '${ctx}/earlyWarning/earlyWarning!save.action',
				dataType : 'text',
				data : {
					entity : encodeURIComponent(JSON.stringify(formData.EarlyWarning))
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
			var row = dicgrid.datagrid("getSelected");
			if (row) {
				var dialog = $('<div id="dialog"></div>').dialog({
					title: '编辑配置',
					width: 800,
					height: 500,
					modal: true,
					maximizable : false,
					href : '${ctx}/earlyWarning/earlyWarning!input.action?earlyWarningId='+row.id,
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
			var row = dicgrid.datagrid("getSelected");
			if (row) {
				$.messager.confirm("操作提示", "确认要删除"+row.warningName+"吗？", function(r) {
					if (r) {
						$.ajax({
							url : '${ctx}/earlyWarning/earlyWarning!delete.action',
							dataType : 'text',
							data : {
								earlyWarningId : row.id
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
		 * 预警分类
		 */
		function typeFormatter(value,row,index){
			if(value=='0'){
				return '分解情况预警';
			}else if(value=='1'){
				return '开工预警';
			}else if(value=='2'){
				return '资金预警';
			}else if(value=='3'){
				return '其他预警'
			}else{
				return value;
			}
		}
		/**
		 *是否预警指标
		 */
		function iswarningFormatter(value,row,index){
			if(value=='0'){
				return '否';
			}else if(value=='1'){
				return '是';
			}else{
				return value;
			}
		}
		/**
		 * 预警等级
		 */
		function levelFormatter(value,row,index){
			if(value=='1'){
				return '1级';
			}else if(value=='2'){
				return '2级';
			}else{
				return value;
			}
		}
		/**
		 * 预警区间
		 */
		function fromToFormatter(value,row,index){
			if(row.statrWarningValue!=''&& row.endWorningValue!=""){
				var a=  row.statrWarningValue+'~'+row.endWorningValue;
				return a;
			}else if(row.statrWarningValue!=''&& row.endWorningValue==""){
				var a= '>'+row.statrWarningValue
				return a;
			}else if(row.statrWarningValue==''&& row.endWorningValue!=""){
				var a= '<'+row.statrWarningValue;
				return a;
			}else{
				return "";
			}
		}
	</script>
</body>
</html>