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
	<link rel="stylesheet" href="${ctx }/js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript"  src="${ctx}/js/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<title>综合分析</title>
</head>
<div class="body">
	<div data-options="region:'center'" style="overflow: float;">
		<div id="menuTreeView" class="ztree" style="float:left"></div>
		
	</div>
</div>
<script type="text/javascript">
	var roleGuid='${param.roleGuid}';
	//私有标签树设置
	var privateLabelSetting = {
	    view: {
			selectedMulti: true,
			showLine: true, //是否显示线，true为显示，false为不显示
			showIcon:false,
			dblClickExpand:true
		},
		data : {
			key : {
				name : "text"
			},
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pid"
			}
		},
		check:{
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "", "N": "" }
		},
		/* callback:{
			onCheck: createResourceTree
		} */
	};
	/**
	 * 加载标签树
	 */
	$.ajax({
		cache : false,
		type : 'POST',
		dataType : "json",
		traditional : true,
		url : "${ctx}/role/role!getMenusData",//请求的action路径  
		error : function() {//请求失败处理函数  
		// 		            alert('请求失败');  
		},
		success : function(data) { //请求成功后处理函数。    
			$.fn.zTree.init($("#menuTreeView"),
					privateLabelSetting, data);
			var zTree = $.fn.zTree.getZTreeObj("menuTreeView");
			zTree.expandAll(true);
// 			var nodes = zTree.transformToArray(zTree.getNodes());
// 			for(var i = 0;i < nodes.length;i++){
// 				var id = nodes[i].pid;
// 				for(var j = 0;j < nodes.length;j++){
// 					if(id == "-1"||id=="-2"){
// 					node = zTree.getNodeByParam("id", id, null);
// 					node.nocheck = true;
// 					zTree.updateNode(node);
// 					} 
// 				}
// 			}
			getLabelsByProjectID();
		}
	});
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取角色授权信息，生成相应的树形结构
	 */
	 function getLabelsByProjectID() {
		$.ajax({
			// 	        async : false,  
			cache : false,
			type : 'POST',
			dataType : "json",
			traditional : true,
			url : "${ctx}/role/role!getRoleMenuByRoleGuid?roleGuid="+roleGuid,//请求的action路径  
			error : function() {//请求失败处理函数  
			// 	            alert('请求失败');  
			},
			success : function(data) { //请求成功后处理函数。    
				// 根据后台获取到的数据，设置部门树和角色树选中的节点
				var zTree = $.fn.zTree
						.getZTreeObj("menuTreeView");
				if (data != null && data.length > 0) {
					var nodes = zTree.getNodes();
					// 循环设置选中节点
					setMuiltesCheckNodes(nodes, "menuTreeView", data);
				}
			}
		});
	};
	/**
	 * 标签树数据选中状态
	 */
	 function setMuiltesCheckNodes(treeNode, treeId, data) {
			// 循环设置选中节点
			for (var i = 0; i < treeNode.length; i++) {
				for (var j = 0; j < data.length; j++) {
					// 如果当前树节点的角色Id与后台获取到的角色Id一致，则将该树的该节点设置为选中状态
					if (treeNode[i].id == data[j].id) {
						$.fn.zTree.getZTreeObj(treeId).checkNode(treeNode[i],
								true, true);
						break;
					}
				}
				if (treeNode[i].children != null
						&& treeNode[i].children.length > 0) {
					// 如果当前节点存在子节点，则递归调用
					setMuiltesCheckNodes(treeNode[i].children, treeId, data);
				}
			}
		};
	
	/**
	 * 角色授权
	 */
	 function updateMenus(){
		debugger;
		var newProjectLabels = $.fn.zTree.getZTreeObj("menuTreeView").getCheckedNodes(true);
		var menuGuids = '';
		$.each(newProjectLabels, function(i, val) {
			menuGuids += val.id;
			menuGuids += ',';
		});
		var responseText = $.ajax({
			async : false,
			cache : false,
			data : {"menuParams.menuGuids":menuGuids,
					"menuParams.roleGuid":roleGuid	
			},
			type : 'POST',
			dataType : 'text',
			url : '${ctx}/role/role!updateMenus',
			error : function() {//请求失败处理函数  
				$.dialog.alert("操作失败！","error"); 
				return false;
			}
		}).responseText;
		return responseText;
		
	};

</script>	
</body>
</html>
