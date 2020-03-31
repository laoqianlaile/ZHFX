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
</head>
<div  class="body">
	<form id="funForm">
		<table style="margin-left:100px;">
			<tr >
				<td style="text-align:left;vertical-align: top; width:100px;" >
					<div >
						<input type="radio" id="labelIsAdded2" class="classa" name="labelIsAdded" value="1" checked="checked" />使用已有项目分类
						<br>
						<input type="radio" id="labelIsAdded1" class="classa" name="labelIsAdded" value="0" />添加新项目分类
					</div>
				</td>
				<td style="text-align:left;vertical-align: top;">
					<div  >
					     <span id="paLabel" >选择项目分类</span>
		    			 <input type="text" id="txtSelectLabel"  class="easyui-combotree" required="true" style="width: 175px;" />	
					</div>
					<div id="newLabel" style="display:none;">
						<span >项目分类</span>
						<input id="addLabel" type="text" class="easyui-textbox"  style="width: 175px;"  />
					</div>
				</td>
			</tr>
		</table>
		</form>
</div>
<script type="text/javascript">
	var labelRoot;
	$(".classa").change( 
		function() { 
		var selectedvalue = $("input[name='labelIsAdded']:checked").val(); 
		if (selectedvalue == 1) {
			$("#paLabel").html("选择项目分类");
			$("#newLabel").css("display", "none");
		} 
		else { 
			 $("#paLabel").html("父项目分类");
			 $("#newLabel").css("display", "block");
			 $("#addLabel").attr("required","required");
			 
		} 
		});
	$(function(){
		//初始化标签下拉树
		$('#txtSelectLabel').combotree( {
			//获取数据URL
			url : '${ctx}/projectFile/projectFile!getCustomLabelsData2',
			onSelect:function(item){
				var parent = item;  
	            var tree = $('#txtSelectLabel').combotree('tree');  
	            labelParent = tree.tree('getParent', parent.target);
	            labelRoot = tree.tree('getRoot', parent.target);
			}
		});
	});
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  	/**
	*  保存标签
	*/
	function saveCustomLabel(ids,moduleCode){
			var isAdd=$("input[name='labelIsAdded']:checked").val();
			var newLabel=$.trim($("#addLabel").textbox("getValue"));
			if ((newLabel==""||newLabel==undefined)&&isAdd=="0"){
				$.messager.alert("提示","请您创建新项目分类！","info");
				return false;
			}
			var parentLabel=$("#txtSelectLabel").combotree("getValue");
			var parentLabelName=$("#txtSelectLabel").combotree("getText");
			if ((parentLabelName==""||parentLabelName==undefined)&&isAdd=="1"){
				$.messager.alert("提示","请您选择项目分类！","info");
				return false;
			}
			//不能选择公有标签或者私有标签作为项目的标签
			if((parentLabel=="-2"||parentLabel=="-1")&&(newLabel==""||newLabel==undefined)){
				$.messager.alert("提示","请您创建私有项目分类！","info");
				return false;
			}else if((newLabel==""||newLabel==undefined)&&(parentLabelName!="公有项目分类"||parentLabelName!="私有项目分类")){//
				newLabel=parentLabelName;
				//parentLabel=labelParent.text;
				
			}
			var labelType="";
			if(labelRoot.text=="公有项目分类"||parentLabelName=="公有项目分类"){
				labelType="0";
			}else if(labelRoot.text=="私有项目分类"||parentLabelName=="私有项目分类"){
				labelType="1";
			}
			var url="${ctx}/projectFile/projectFile!saveCustomerLabel";
			 responseText =$.ajax({
				        type : 'POST',
						async : false,
				    	url: url,
				    	data:{
								"labelParams.addLabel": newLabel,
								"labelParams.txtSelectLabel":$("#txtSelectLabel").combotree("getValue"),
								"labelParams.txtSelectLabelName":parentLabelName,
								"labelParams.labelType":labelType,
								"labelParams.projectids": ids,
								"labelParams.isAdd": isAdd,
								"labelParams.moduleCode":moduleCode
							 },
				        error: function (data) {//请求失败处理函数 
				        	//$.messager.alert("提示信息","导入失败","error");
				        },  
				        success:function(data){ //请求成功后处理函数。
				        	$("#tt4").datagrid('reload');
				        	
				       	}  
					}).responseText;	
					return responseText;
					
	};
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
</script>	
</body>
</html>
