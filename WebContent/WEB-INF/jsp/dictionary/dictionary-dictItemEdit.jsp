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
	<form id="itemForm1">
		<table fit="true" class="table2" style="margin: 0px">
			<tr>
				<td>层级关系
				</td>
				<td>
					<input type="radio" id="isParentY1" name="isParent1" value="Y" />同级
					<input type="radio" name="isParent1" checked id="isParentN1" value="N" />子级
				</td>
			</tr>
			<tr>
				<td>
					<span class="redX_z">字典项编号</span>
				</td>
				<td >
					<input class="easyui-textbox" id="itemKeyId1" type="text" name="dictionaryItems.itemKey" required="required" data-options="validType:['maxLength[30]','sqlkey']" />
					<b class="red-star">*</b>
				</td>
			</tr>
			<tr>
				<td>
					<span class="redX_z">字典项名称</span>
				</td>
				<td>
					<input class="easyui-textbox" id="itemValueId1" type="text" name="dictionaryItems.itemValue" required="required" data-options="validType:['maxLength[100]','sqlkey']"/>
					<b class="red-star">*</b>
				</td>
			</tr>
			<tr>
				<td>
					<span class="redX_z">状态</span>
				</td>
				<td>
					<input style="width: 160px;" type="text"  name="dictionaryItems.enabled" id="sltEnabled1" />
					<b class="red-star">*</b>
				</td>
			</tr>
			<tr>
				<td>父级字典项</td>
				<td>
					<span id="parentGuids" title="双击取消选择"><input id="parentGuid"name="dictionaryItems.parentItemId"  type="text"  style="width: 277px;"/></span>
				</td>
			</tr>
			<tr id="TrArea1">
				<td>
					<span class="redX_z">标记</span>
				</td>
				<td>
					<input class="easyui-combobox" id="sltFlag1" name="dictionaryItems.parentItemId"  type="text"  style="width: 277px;"/>
				</td>
			</tr>
			<tr>
				<td>
					<span class="redX_z">排序</span>
				</td>
				<td>
					<input class="easyui-numberbox" data-options="min:0,max:999,precision:0" id="sort" type="text" name="dictionaryItems.pxh" required="required" maxlength="3"/>
					<b class="red-star">*</b>
				</td>
			</tr>
		</table>
		<div>
			
				<!-- 以下定义隐藏域 -->
				<input id="groupNo" name="dictionary.groupNo" type="hidden" value="${groupNo}"/>
				<input id="itemGuid" name="dictionaryItems.itemGuid"  type="hidden" value='${param.itemGuid}'/>
				<input id="itemPid" name="itemPid"  type="hidden" value="${itemPid}"/>
				<input id="gid" name="itemGuid" type="hidden" value='${param.itemGuid}'/>
				<input id="flag" name="dictionaryItems.flag" type="hidden" />
				<input id="createUserId" name="dictionaryItems.createUserId"  type="hidden" value='${param.createUserId}'/>
				<input id="createUserName" name="dictionaryItems.createUserName"  type="hidden" value='${param.createUserName}'/>
				<input id="createTime" name="dictionaryItems.createTime"  type="hidden" value='${param.createTime}'/>
				<input id="dataType" name="dictionaryItems.dataType"  type="hidden" value='${param.dataType}'/>
			</div>
	</form>
</div>
<script type="text/javascript">

	var gid = $("#gid").val();
	var groupNo='${param.groupNo}';
	var itemGuid='${param.itemGuid}';
	//父级字典项
	$("#parentGuid").combotree({
	 	url:'${ctx}/dictionary/dictionary!getParentDictItemsBygroupNo?itemGuid='+gid+'&groupNo='+groupNo,
	});
	//启用、禁用
	$('#sltEnabled1').combobox({
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
	 $("#parentGuids").dblclick(function(){
		 $("#parentGuid").combotree("clear");
		});		
	 //标记
	 $('#sltFlag1').combobox({
		data : [{
			name : '单列市',
			value : 3
		},{
			name : '直辖市',
			value : 2
		},{
			name : '省级以上行政区划',
			value : 1
		},{
			name : '',
			value : 0
		}],
        width: 150,
        valueField : 'value', 
        textField : 'name'
	});
	////////////////////////////////////
	//字典项编辑弹窗；
	function openDictItemEditDialog(itemGuid){
		debugger;
		var groupNo = '${groupNo}';
		if(itemGuid!=null&&itemGuid!=""){
			$.ajax({
				async : false,  
		        cache:false,  
		        type: 'POST',  
		        dataType : 'text', 
		    	url:'${ctx}/dictionary/dictionary!getDictItemsByGroupGuid?conditions.dictGuid='+itemGuid+'&groupNo='+groupNo,
		        error: function () {//请求失败处理函数  
		            alert('请求失败');  
		            $('#item_info_edit_dialog').dialog('close');
		        }, 
		        success:function(data){
		        	data = $.parseJSON(data);
		        	debugger;
		        	if(groupNo==1){
						$("#TrArea1").css("display", "table-row");
				    } else {
				    	$("#TrArea1").css("display", "none");
				    }
		        	$("#itemKeyId1").val(data[0].itemKey);
		        	$("#itemValueId1").val(data[0].itemValue);
		        	$("#sltEnabled1").combobox('setValue',Number(data[0].enabled));
		        	$("#sltFlag1").combobox('setValue',data[0].flag);
		        	$("#sort").val(data[0].pxh);
		        	$("#createUserId").val(data[0].createUserId);
		        	$("#createUserName").val(data[0].createUserName);
		        	$("#createTime").val(data[0].createTime);
		        	$("#dataType").val(data[0].dataType);
		        	$("#parentGuid").combotree('setValue',data[0].parentItemId);
		        }  
			});
		}	
	};
	///////////////////////////////////////////
	// 更新字典项信息
	function updateDictItems(){
		var flag = $("#sltFlag1").combobox('getValue');
		$("#flag").val(flag);
		var info = "字典项修改成功";
		var url = '${ctx}/dictionary/dictionary!saveDictItems';	
		$("#isAdd").val("false");
		$("input[name='dictionaryItems.isParent']").attr("disabled", true);
		// 执行下表单验证操作
		var valid = $('#itemForm1').form('validate');
		if(!valid){
			return false;
		}
		var ss = $('#itemForm1').form().serializeArray();
		var reponseText=$.ajax({
			async : false,
	        cache:false,
	        data:  $('#itemForm1').form().serializeArray(),
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
	///////////////////////////////////////////////////////////////////////	//////////////////////////////////////////
	$(function(){
		///////////////////////////////
		 openDictItemEditDialog(itemGuid);
	});
	//////////////////////////////////////
</script>
</body>
</html>