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
<link rel="stylesheet" type="text/css" href="${ctx }/js/ztree/css/zTreeStyle/zTreeStyle.css">

<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script src="${ctx}/js/base.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/common/extend.js"></script>
<script type="text/javascript" src="${ctx}/js/ztree/js/jquery.ztree.all-3.5.min.js"></script>
</head>
		 <body class="easyui-layout" data-options="fit:true">  
		 	<!-- 页面工具栏 -->
		 	<div data-options="region:'north'" style="height:30px;background-color: #041e35;">
				<table width="100%">
					<tr >
						<td width="75%">
							<div id="reportButtons">
								<a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;" onclick="dictGroupInput('add')">新增字典组</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;" onclick="dictGroupInput('edit')">编辑字典组</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;" onclick="dictItemInput()">新增字典项</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;" onclick="dictItemEdit()">编辑字典项</a>
							</div>
						</td>
						<td width="25%">
						</td>
					</tr>
				</table>
			</div>
			<!-- 中间左侧列表 -->
			<div data-options="region:'west'" style="width:200px; overflow: auto;background-color: #041e35;">
				<ul id="dictGroupList" class="ztree" style="margin-top:0; width:180px;"></ul>
			</div>
			<!-- 中间右侧列表（以下div展示用户列表信息 ）url: '${ctx}/rule/dictionary!getDictItemsData',-->
			<div data-options="region:'center',split:true">
				<!-- 列表表格展示 -->
				<table id="datagrid" class="easyui-datagrid"
					data-options="fitColumns:true,singleSelect:true,fit:true,
						                rownumbers: true,
						            ">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'itemGuid'" width="80" align="center" hidden>itemGuid</th>
							<th data-options="field:'dictionary.groupNo'" width="80" align="center" hidden>字典组</th>
							<th data-options="field:'parentItemId'" width="80" align="center" >父字典项</th>
							<th data-options="field:'itemKey'" width="80" align="center">字典编号</th>
							<th data-options="field:'itemValue'" width="100" align="left">字典值</th>
							<th data-options="field:'pxh'" width="100" align="center">排序</th>
							<th data-options="field:'enabled'" width="80" align="center" formatter="enabledFormater">状态</th>
						</tr>
					</thead>
				</table>
		</div>	
	<script type="text/javascript">
	// z-tree定义基本元素
	var groupNo='';
	var tree_groupNo='';
	var setting = {
	    view: {
			selectedMulti: false,
			showLine: true, //是否显示线，true为显示，false为不显示
			showIcon: false,
			dblClickExpand: false
		},
	    aysnc:{
	    	enable: true
	    	
	    },
		data: {
			key:{
				name: "text"
			},
	        simpleData: {  
	            enable: true,
	            idKey : "id"
	        }
		},
		callback:{
			beforeClick: zTreeBeforeClick
		}
	};
	function zTreeBeforeClick(treeId, treeNode, clickFlag){
		tree_groupNo=treeNode.id;
		groupNo = treeNode.id;
		loadGrid();
	}
	function loadGrid(){
		$('#datagrid').datagrid({ 
			url:'${ctx}/dictionary/dictionary!getDictionaryData?groupNo='+groupNo
		}); 
	}   
	//点击树形节点后，展开子节点
	function zTreeNodeExpand(event, treeId, treeNode) {
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.getSelectedNodes();
		if (nodes.length>0) {
			treeObj.expandNode(nodes[0], true, false, true);
		}
	}
//		//操作按钮编辑
//		editHandler=function(value,row,index){
//			return eu.fs($("#dict_opertate_module").html(),row.guid);
//		};
	//启用状态
	function enabledFormater(value,row,index){
		if(row.enabled==1){
			return "启用";
		}else{
			return "禁用";
		}
		
	};
	//页面展示树设定数据
	function initTreeData(){
		$.ajax({  
	        async : false,  
	        cache:false,  
	        type: 'POST',  
	        dataType : "json",  
	        traditional: true,
	        url: "${ctx}/dictionary/dictionary!getDictGroupData.action",//请求的action路径  
	        error: function () {//请求失败处理函数  
	        	alert('请求失败','error');  
	        },  
	        success:function(data){ //请求成功后处理函数。
	        	var treeObj  = $.fn.zTree.init($("#dictGroupList"), setting, data);
	        	if(groupNo){
	        		var nodes = treeObj.getNodes();
					if(nodes != ""){
						for(var i=0;i<nodes.length;i++){
							if(nodes[i].id == groupNo){
								treeObj.selectNode(nodes[i],false);
							}
						}
					}
	        	}
	        	var node = null;
	        }  
	    });  
	}
	
	//导出xml
	function downloadDict(){
		window.location.href="${ctx}/dictionary/dictionary!downloadDict";
	}

	function getSelectModule() {
	       var zTree = $.fn.zTree.getZTreeObj("dictGroupList");
	       dictGroup = zTree.getSelectedNodes();
	       alert(dictGroup+'sv');
	       if(null==dictGroup||dictGroup == ""){
	    	   zTree.selectNode('4');
	          //return "";
	       }
		   return zTree.getSelectedNodes()[0].id;
	 };
	//treegrid延迟加载子节点
	function myLoadFilter(data,parentId){
		function setData(){
			var todo = [];
			for(var i=0; i<data.length; i++){
				todo.push(data[i]);
			}
			while(todo.length){
				var node = todo.shift();
				if (node.children){
					node.state = 'closed';
					node.children1 = node.children;
					node.children = undefined;
					todo = todo.concat(node.children1);
				}
			}
		}
		setData(data);
		var tg = $(this);
		var opts = tg.treegrid('options');
		opts.onBeforeExpand = function(row){
			if (row.children1){
				tg.treegrid('append',{
					parent: row[opts.idField],
					data: row.children1
				});
				row.children1 = undefined;
				tg.treegrid('expand', row[opts.idField]);
			}
			return row.children1 == undefined;
		};
		return data;
	};
	$(function (){
		//初始化列表
		initTreeData();
		//初始化项目列表
		project_datagrid = $("#datagrid").datagrid({
			fit: true,
			border: true,
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景
			height: 550,
			pageSize:20,
			singleSelect : true,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			remoteSort : false,
			pagination : true,//分页
			onLoadSuccess : function(data) {
				//取消所有的已选择项
				$(this).datagrid('clearSelections');
			}
		});
	});
	//字典组新增/编辑弹窗；
	function dictGroupInput(type){
		 groupNo="";
		var title="新增字典组";
		if(type=="edit"){
			if((tree_groupNo!=null&&tree_groupNo!="")){
				 groupNo=tree_groupNo;
				 title="编辑项目字典组";
			}else if(tree_groupNo==null||tree_groupNo==""){
				alert( "请选择字典组！","warning");  
				return false;
			}
		}
		var dialog = $('<div id="dialog"></div>').dialog({
			title:title,
			width: 900,
			height: 500,
			lock: true,
			min:true,
			max:true,
			href : '${ctx}/dictionary/dictionary!dictGropInput?groupNo='+groupNo,
			buttons : [ {
				text : '保存提交',
				handler : function() {
			    	var message=saveDictGroup2();
			    	if(message){
						if(message.flag == "true"){
							 $.dialog.tips(message.content,3);
								//树刷新
					    		initTreeData();
					    		// 重新加载一览画面
					    		//$("#treeGrid").treegrid('reload');
					    		$('#datagrid').datagrid('unselectAll');
					    		 if(type=="edit"){
					    			loadGrid();
					    		 }
					    		 dialog.dialog('destroy');
						}
						else{
							alert(message.content,"error");
							return false;
						}
					}
						// 操作失败 
						else{
					    	return false;
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
///////////////////////////////////////////////////////////	
	//字典项新增弹窗；
	function dictItemInput(){
		debugger;
		 if(tree_groupNo==null||tree_groupNo==""){
			 alert( "请选择字典组！","warning");   
				return false;
			}
		 if($('#datagrid').datagrid('getSelections').length > 1){
			 alert('只能选择一个字典项进行新增字典项',"warning");
				return false;
			}
		var node = $('#datagrid').datagrid('getSelections');
		var itemPid=null;
		var itemId=null;
		if(node[0]){
			 itemPid = node[0].parentItemId||null;
			 itemId = node[0].itemKey;	
		}
		var dialog = $('<div id="dialog"></div>').dialog({
			title:"新增字典项",
			width: 900,
			height: 500,
			lock: true,
			min:true,
			max:true,
			href : '${ctx}/dictionary/dictionary!dictItemInput?groupNo='+tree_groupNo+'&itemPid='+itemPid+'&itemId='+itemId,
			buttons : [ {
				text : '保存提交',
				handler : function() {
					debugger;
					var message=saveDictItems();
			    	if(message){
						if(message.flag == "true"){
							 alert(message.content);
								//树刷新
					    		initTreeData();
					    		// 重新加载一览画面
					    		//$("#treeGrid").treegrid('reload');
					    		 $('#datagrid').datagrid('unselectAll');
					    		loadGrid();
					    		dialog.dialog('destroy');
						}
						else{
							alert(message.content,"error");
							return false;
						}
					}
						// 操作失败 
						else{
					    	return false;
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
///////////////////////////////////////////////////////////	
	//字典项编辑弹窗；
	function dictItemEdit(){
		if($('#datagrid').datagrid('getSelections').length > 0){
				if($('#datagrid').datagrid('getSelections').length > 1){
					alert('只能选择一个字典项修改',"warning");
					return false;
				}
			}else{
				alert('至少选择一个字典项修改',"warning");
				return false;
			}
		var node = $('#datagrid').datagrid('getSelections');
		var itemGuid = node[0].itemGuid;
		var dialog = $('<div id="dialog"></div>').dialog({
			title:"编辑字典项",
			width: 900,
			height: 500,
			lock: true,
			min:true,
			max:true,
			href : '${ctx}/dictionary/dictionary!dictItemEdit?itemGuid='+itemGuid+'&groupNo='+tree_groupNo,
			buttons : [ {
				text : '保存提交',
				handler : function() {
			    	var message=updateDictItems();
			    	if(message){
						if(message.flag == "true"){
							 alert(message.content);
								//树刷新
					    		initTreeData();
					    		// 重新加载一览画面
					    		//$("#treeGrid").treegrid('reload');
					    		 $('#datagrid').datagrid('unselectAll');
					    		loadGrid();
					    		dialog.dialog('destroy');
						}
						else{
							alert(message.content,"error");
							return false;
						}
					}
						// 操作失败 
						else{
					    	return false;
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