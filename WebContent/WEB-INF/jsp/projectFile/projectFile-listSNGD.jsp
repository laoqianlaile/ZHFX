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
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/base.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/icon.css">
<link rel="stylesheet" href="${ctx }/js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css"></link>

<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script src="${ctx}/js/base.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/common/extend.js"></script>
<script type="text/javascript" src="${ctx}/js/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
   $(function(){
	   $("#toolbar > span:eq(3)").remove();
   })
</script>
<style>
	.search_area table th, .search_area table td, .search_area span input[type='text']{
		color : white
	}
	.box .datagrid-body td * {
    color: yellow;
	}
</style>
</head>

<body>
	<div class="centerNr">
		<!--查询域 -->
		<div class="search_area">
		<table>  
		   <tr>
			  <th width="150">所属行业</th>
			  <td>
				  <input type="text" id="industry_code" name="" >
			  </td>
			  <th>建设地点</th>
			  <td>
				  <input type="text" id="project_region" name=""/>
			  </td>
			   <th>政府投资方向</th>
			  <td>
				  <input id="fit_ind_policy_code" type="text" name="">
			  </td>
		  </tr>
		  <tr>
			  <th>项目类型</th>
			  <td>
				  <input type="text" id="check_level" name="" data-options="editable:false"/>
			  </td>
		  	  <th>是否PPP</th>
			  <td>
				  <input id="is_ppp" type="text" name="" data-options="editable:false"/>
			  </td>
			  <th>是否专项建设基金</th>
			  <td>
				  <input id="is_spec_funds" type="text" name="" data-options="editable:false"/>
			  </td>
		  </tr>
		  <tr>
			  <th>项目名称</th>
		  	  <td >
		  		  <input type="text" id="project_name" name = "" class="easyui-textbox"/>
		  	  </td>
		  	  <th>拟竣工年份</th>
		      <td colspan="5">
			      <input type="text" id="expect_end_year1" data-options="editable:false" name="" /> -
			      <input type="text" id="expect_end_year2" data-options="editable:false" name="" />
		      </td>
		  </tr>
		  <tr>
		    <th>拟开工年、月</th>
		    <td colspan="5">
			    <input type="text" id="expect_start_year1" data-options="editable:false" style="width:120px;"/> 年 
			    <input type="text" id="expect_start_month1" data-options="editable:false" style="width:120px;" /> 月-
			    <input type="text" id="expect_start_year2" data-options="editable:false" style="width:120px;" /> 年 
			    <input type="text" id="expect_start_month2" data-options="editable:false" style="width:120px;" /> 月
		    </td>
		  </tr>
		</table>

		<table style="display:none;">
			<tr>
			   <th width="150">项目申报日期</th>
			   <!-- 申报日期 时间段 -->
			   <td>
			  	  <input id="proApplyStartTime" name="" type="text" class="easyui-datebox" data-options="editable:false"> - 
			 	  <input id="proApplyEndTime" name="" type="text"  class="easyui-datebox" data-options="editable:false">
			  </td>
			</tr>
			<tr>
			  <th>总投资</th>
			  <td>
				  <input id="allCaptial1" name="" type="text"  class="easyui-numberbox" > - 
				  <input id="allCaptial2" name="" type="text" class="easyui-numberbox" >
			  </td>

		  </tr>
		</table>
		<div class="opy">
			<c:if test="${innerPage!=1}">
			<input type="button" class="searchBtn" id=customLabel value="项目分类" onclick="javascript:customLabel()"/>
			<input type="button" class="searchBtn" id=customLabel value="修改分类" onclick="javascript:editProjectLabel()"/>
			</c:if>
			<input type="button" class="searchBtn" id="search_btn" value="查询" onclick="javascript:searchByCondition()" />
			<input type="button" class="searchBtn" id="clear_conditions" value="重置" />
			<span class="open" onclick="toggleYs();"></span>
			</div>
		</div>
		<!-- 左边树 -->
		<c:if test="${innerPage!=1}">
		<div class="xmda_leftDiv" >
			<h6>项目分类</h6>
			<div style="overflow:auto;height:515px;" >
		  	 <ul id="treeView_4" class="ztree" style="border:0px;position:relative;top:0;width:100%;"></ul>
			</div>
		</div>
		</c:if>
		<!-- 列表 -->
		<c:if test="${innerPage!=1}">
		<div class="box xmda_contDiv">
		</c:if>
		<c:if test="${innerPage==1}">
		<div style="width: 100%">
		</c:if>
	        <div style="height:30px;line-height:30px;color:#ccc;font-size:14px;">
	            <span style="margin-left:10px;"><img src="${ctx}/themes/images/lv1.png" title="一级预警 \n'+_tip+'"/>  一级警告</span>
	            <span style="margin-left:10px;"><img src="${ctx}/themes/images/lv2.png" title="二级预警 \n'+_tip+'"/>  二级警告</span>
	        </div>
			<div style="height:550px;">
				<table id="project_datagrid" rownumbers="true" >
					<thead>
						<tr>
							<th field="PRO_NAME" align="left" width="200" formatter="nameFormatter">项目名称</th>
							<th field="INVESTMENT_TOTAL" align="center" width="120">总投资（万元）</th>
							<th field="PRO_TYPE" align="center" width="100">项目类型</th>
							<th field="INDUSTRY" align="center" width="150">所属行业</th>
							<th field="BUILD_PLACE" align="center" width="120">建设地点</th>
							<th field="EXPECT_STARTYEAR" align="center" width="120"formatter="formatYear">拟开工年份</th>
							<th field="STAGE" align="center" width="120" >项目成熟度</th>
						</tr>
					</thead>
				</table>
			</div>
        </div>
    </div>
<input type="hidden" id="moduleCode" value="${filters.moduleCode}" />
<input type="hidden" id="builPlaceCode" value="${filters.builPlaceCode}" />
<input type="hidden" id="builPlaceChineseName" value="${filters.builPlaceChineseName}" />
<input type="hidden" id="goverInvestDir" value="${filters.goverInvestDir}" />
<input type="hidden" id="index" value="${filters.index}" />
<input type="hidden" id="projectMaturity" value="${filters.projectMaturity}" />
<input type="hidden" id="projectTypeName" value="${filters.projectTypeName}" />
<input type="hidden" id="gbIndustry" value="${filters.gbIndustry}" />
<input type="hidden" id="majorStrategic" value="${filters.majorStrategic}" />
<input type="hidden" id="wnIndustry" value="${filters.wnIndustry}" />
<input type="hidden" id="innerPage" />
<input type="hidden" id="filterStatus" />
<input type="hidden" id="fundLevel" />
<input type="hidden" id="bank" />
<input type="hidden" id="sessionId" />
<script type="text/javascript">
	//下拉框填充月份
	var data1 = [];//创建月份数组
	var startMonth=1;//起始月份
	var thisMonth=new Date().getUTCMonth()+1;//本月
	//数组添加值（1-12月）为固定值
	for(startMonth;startMonth<13;startMonth++){
		data1.push({label:startMonth,value:startMonth});
	}
	//下拉框填充年份
	var data2 = [];//创建年度数组
	var startYear;//起始年份
	var thisYear=new Date().getUTCFullYear();//今年
	var endYear=thisYear+15;//结束年份
	//数组添加值（2012-2016）//根据情况自己修改
	for(startYear=endYear-40;startYear<=endYear;startYear++){
		data2.push({label:startYear,value:startYear});
     }
	//地图跳转参数map
	//定义跳转到ProjectFile-list页面来源
	var innerPage = '${innerPage}';
	//放入隐藏域
	$("#innerPage").val(innerPage);
	var filterStatus="";
	//专项债行业
	var bank ="";
	//专项债批次号
	var fundLevel = "";
	if(innerPage=='1'){
		//获取模块过滤状态
		filterStatus = ZHFX.getCookie('filterStatus');
		//放入隐藏域
		$("#filterStatus").val(filterStatus);
		//获取 专项债信息
		//获取批次号
		fundLevel = ZHFX.getCookie('FundLevel');
		$("#fundLevel").val(fundLevel);
		//获取银行
		bank= ZHFX.getCookie('Bank');
		$("#bank").val(bank);
		//sessionId
		sessionId= ZHFX.getCookie('sessionId');
		$("#sessionId").val(sessionId);
	}
	//选中标签id
	var nodeOfLabel='';
	$(function (){
		//初始化项目列表
		project_datagrid = $("#project_datagrid").datagrid({
			fit: true,
			url: "projectFile!showSNProject.action",
			queryParams:{
				'filters.moduleCode':$("#moduleCode").val(),
	        	'filters.warnCode':'${param.warnCode}',
				'filters.builPlaceCode':$("#builPlaceCode").val(),
				'filters.builPlaceChineseName':$("#builPlaceChineseName").val(),
				'filters.innerPage':$("#innerPage").val(),
				'filters.filterStatus':$("#filterStatus").val(),
				'filters.fundLevel':$("#fundLevel").val(),
				'filters.bank':$("#bank").val(),
				'filters.goverInvestDir':$("#goverInvestDir").val(),
				'filters.sessionId':$("#sessionId").val(),
				'filters.index':$("#index").val(),
				'filters.projectMaturity':$('#projectMaturity').val(),
				'filters.projectTypeName':$('#projectTypeName').val(),
				'filters.gbIndustry':$('#gbIndustry').val(),
				'filters.majorStrategic':$('#majorStrategic').val(),
				'filters.wnIndustry':$('#wnIndustry').val()
			},
			border: true,
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景
			height: 580,
			pageSize:20,
			singleSelect : false,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			remoteSort : false,
			pagination : true,//分页
		    frozenColumns:[[
			                {field:'ck',checkbox:true}
							]], 
			onLoadSuccess : function(data) {
				//取消所有的已选择项
				$(this).datagrid('clearSelections');
				ZHFX.setCookie("BuildPlaceCode","");
			}
		});
		$("li:contains('项目档案')").addClass("active");
		initLabelTreeData();
	});
		
	$("#expect_start_month1").combobox({//拟开工月开始时间
		valueField:'value',    
		textField:'label',
		data:data1
	});
	$("#expect_start_month2").combobox({//拟开工月结束时间
		valueField:'value',    
		textField:'label',
		data:data1
	});
	$("#expect_end_year1").combobox({//竣工或拟建成年份开始时间
		valueField:'value',    
		textField:'label',  
		data:data2
	});
	$("#expect_end_year2").combobox({//竣工或拟建成年份结束时间
		valueField:'value',    
		textField:'label', 
		data:data2
	});
	
	$("#expect_start_year1").combobox({//拟开工年开始时间
		valueField:'value',    
		textField:'label',  
		data:data2
	});
	$("#expect_start_year2").combobox({//拟开工年结束时间
		valueField:'value',    
		textField:'label',  
		data:data2
	});
		
	//审批阶段（可多选）
	$("#check_level").combotree({
		url:'${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=ProjectTypeCode',
		multiple:true
	});
	
	//建设地点（可多选）
	$("#project_region").combotree({
		url:'${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',
		multiple:true
	});
	
	//所属行业（可多选）
	$("#industry_code").combotree({
		url:'${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryCode',
		multiple:true
	});
	
	//政府投资方向（可多选）
	$("#fit_ind_policy_code").combotree({
		url:'${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=GovernmentCode',
		multiple:true
	});
	
	//项目实施进展
	$("#project_imple_pro").combotree({
		url:'${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=ProgressCode',
	});
	//是否PPP
	$("#is_ppp").combobox({
		valueField:'id',
		textField:'text',
		data:[{text: '全部',id: ''},{text: '是',id: 'A00001'},{text: '否',id: 'A00002'}]
	});
	
	//是否专项建设基金
	$("#is_spec_funds").combobox({
		valueField:'id',
		textField:'text',
		data:[{text: '全部',id: ''},{text: '是',id: 'A00001'},{text: '否',id: 'A00002'}]
	});
	
	/**
	 * 重置查询条件
	 */
	$("#clear_conditions").click(function(){
		$("#expect_start_year1").combobox("clear");                //拟开工年份1（查询时第一个时间）
		$("#expect_start_year2").combobox("clear");                //拟开工年份2（查询时第二个时间）下同
		$("#expect_start_month1").combobox("clear");               //拟开工月份1
		$("#expect_start_month2").combobox("clear");               //拟开工月份2
		$("#expect_end_year1").combobox("clear");                  //拟竣工年份1
		$("#expect_end_year2").combobox("clear");                  //拟竣工年份2
		$("#project_name").textbox("clear");                       //项目名称
		$("#fit_ind_policy_code").combotree("clear");              //政府投资方向
		$("#project_region").combotree("clear");          		 //建设地点
		$("#industry_code").combotree("clear");            		 //所属行业
// 		$("#project_imple_pro").combotree("clear");     			 //项目实施进展
		$("#is_ppp").combobox("clear");                            //是否ppp
		$("#is_spec_funds").combobox("clear");             		 //是否专项建设基金
		$("#proApplyStartTime").datebox("clear");    //项目申报日期1
		$("#proApplyEndTime").datebox("clear");      //项目申报日期2
		$("#allCaptial1").numberbox("clear");      	   //总投资1
		$("#allCaptial2").numberbox("clear");      	   //总投资2

		$("#check_level").combotree("clear")         //审批阶段
	});
	/**
	 * 查询按钮
	 */
	 function searchByCondition(){
		if(checkData() == 0){ //验证查询条件是否合理(不合理时checkData会返回0)
 			return;
 		};
		var planStartYear1 = $("#expect_start_year1").combobox("getValue");                //拟开工年份1（查询时第一个时间）
		var planStartYear2 = $("#expect_start_year2").combobox("getValue");                //拟开工年份2（查询时第二个时间）下同
		var planStartMonth1 = $("#expect_start_month1").combobox("getValue");              //拟开工月份1
		var planStartMonth2 = $("#expect_start_month2").combobox("getValue");              //拟开工月份2
		var planEndYear1 = $("#expect_end_year1").combobox("getValue");                    //拟竣工年份1
		var planEndYear2 = $("#expect_end_year2").combobox("getValue");                    //拟竣工年份2
		var projectName = $("#project_name").textbox("getValue");                          //项目名称
		var fitIndPolicyCode=$("#fit_ind_policy_code").combotree("getValues").toString();          //政府投资方向
		var projectRegion=$("#project_region").combotree("getValues").toString();          //建设地点
		var industryCode=$("#industry_code").combotree("getValues").toString();            //所属行业
// 		var projectImplePro=$("#project_imple_pro").combotree("getValue");     //项目实施进展
		var isPpp=$("#is_ppp").combobox("getValue");                          //是否ppp
		var isSpecFunds=$("#is_spec_funds").combobox("getValue");             //是否专项建设基金
		var projectApplyTime1=$("#proApplyStartTime").datebox("getValue");  //项目申报日期1
		var projectApplyTime2=$("#proApplyEndTime").datebox("getValue");    //项目申报日期2
		var allCaptial1=$("#allCaptial1").numberbox("getValue");   //总投资1
		var allCaptial2=$("#allCaptial2").numberbox("getValue");   //总投资2
		var checkLevel=$("#check_level").combotree("getValues").toString();   //审批阶段
		project_datagrid.datagrid("load", {
			 "paramsMap.planStartYear1"       :planStartYear1,
			 "paramsMap.planStartYear2"       :planStartYear2,
			 "paramsMap.planStartMonth1"      :planStartMonth1,
			 "paramsMap.planStartMonth2"      :planStartMonth2,
			 "paramsMap.planEndYear1"         :planEndYear1,
			 "paramsMap.planEndYear2"         :planEndYear2,
			 "paramsMap.projectName"          :projectName,
			 "paramsMap.fitIndPolicyCode"     :fitIndPolicyCode,
			 "paramsMap.projectRegion"        :projectRegion,
			 "paramsMap.industryCode"         :industryCode,
			 "paramsMap.isPpp"                :isPpp,
			 "paramsMap.isSpecFunds"          :isSpecFunds,
			 "paramsMap.projectApplyTime1"    :projectApplyTime1,
			 "paramsMap.projectApplyTime2"    :projectApplyTime2,
			 "paramsMap.allCaptial1"    	  :allCaptial1,
			 "paramsMap.allCaptial2"   		  :allCaptial2,
			 "paramsMap.checkLevel"           :checkLevel,
			 "paramsMap.labelId"           :nodeOfLabel,
			 'filters.moduleCode':$("#moduleCode").val(),
			 'filters.builPlaceCode':$("#builPlaceCode").val(),
			 'filters.builPlaceChineseName':$("#builPlaceChineseName").val(),
			 'filters.innerPage':$("#innerPage").val(),
			 'filters.filterStatus':$("#filterStatus").val(),
			 'filters.fundLevel':$("#fundLevel").val(),
			 'filters.bank':$("#bank").val(),
			 'filters.warnCode':'${param.warnCode}',
			 'filters.sessionId':$("#sessionId").val(),
			 'filters.goverInvestDir':$("#goverInvestDir").val(),
			 'filters.index':$("#index").val(),
			 'filters.projectMaturity':$('#projectMaturity').val(),
			 'filters.projectTypeName':$('#projectTypeName').val(),
			 'filters.gbIndustry':$('#gbIndustry').val(),
			 'filters.majorStrategic':$('#majorStrategic').val(),
			 'filters.wnIndustry':$('#wnIndustry').val()
		});
	};
	
	/**
	 * 验证查询条件是否合理
	 */
	function checkData(){
		if(($("#proApplyStartTime").datebox('getValue')>$("#proApplyEndTime").datebox('getValue'))&& $("#proApplyEndTime").datebox('getValue') != ""){
			alert("项目申报日期开始时间不能大于结束时间");
			return 0;
		}else if(($("#expect_end_year1").combobox('getValue')>$("#expect_end_year2").combobox('getValue'))&& $("#expect_end_year2").combobox('getValue')!=""){
			alert("拟竣工年份开始时间不能大于结束时间");
			return 0;
		}else if(($("#allCaptial1").numberbox('getValue')>$("#allCaptial2").numberbox('getValue'))&& $("#allCaptial2").numberbox('getValue')!=""){
			alert("总投资最小值不能大于最大值");
			return 0;
		}else if(
				$("#expect_start_year1").combobox('getValue')>$("#expect_start_year2").combobox('getValue') || 
				($("#expect_start_year1").combobox('getValue')==$("#expect_start_year2").combobox('getValue') 
						&& $("#expect_start_month1").combobox('getValue')>$("#expect_start_month2").combobox('getValue'))){
			alert("拟开工开始时间不能大于结束时间");
			return 0;
		}
	}
	
	/**
	 * 项目名称操作列
	 */
	function nameFormatter(value,row,index){

		var img="";

		var  _lv=0;
		var _tip="";
		if(value){
			if(row.lv==1||row.mon_lv==1){
				_lv=1;
				if(row.lv==1&&row.months){
					_tip=_tip+"报告期超过项目计划下达时间（"+row.months+"月）";
				}
				if(row.mon_lv==1&&row.mon_rate){
					_tip=_tip+"支付资金是完成投资"+Number(row.mon_rate).toFixed(1)+"倍";
				}
			}
			// 二级预警
			else if(row.lv==2){
				_lv=2;
				if(row.months){
					_tip=_tip+"报告期超过项目计划下达时间（"+row.months+"月）";
				}
			}

			// 未开工一级预警
			if(_lv==1 ){
				img='<img src="${ctx}/themes/images/lv1.png" title="1级预警 \n'+_tip+'"/>';
			}
			else if(_lv==2){
				img='<img src="${ctx}/themes/images/lv2.png" title="2级预警 \n'+_tip+'"/>';
			}

			if(row.isimg==0){
				img=img+'<img src="${ctx}/themes/images/pic.png"  title="此项目有调度图片"/>';
			}
			return img+'<a style="font-family:Microsoft YaHei;text-decoration: underline" title="'+value+'" onclick="showDialog(\''+index+'\')" >'+value+'</a>';
		}
	}

	//日期格式转换
	function formatYear(times){
		if(times){
			var time = new Date(times);
			var y = time.getFullYear();
			var m = time.getMonth()+1;
			var d = time.getDate();
			var h = time.getHours();
			var mm = time.getMinutes();
			var s = time.getSeconds();
			return y;
		}else{
			return '';
		}
	}
	/**
	 * 跳转到该项目的项目档案页
	 */
	 function showDialog(index){
		var rows = $('#project_datagrid').datagrid('getRows');
		//alert(window.document.location);
		ZHFX.showWindow("/projectFile/projectFile!details?projectGuid="+ rows[index].ID+"&innerPage="+$("#innerPage").val()+"&moduleCode="+$("#moduleCode").val());//传入当前行的项目ID
	}
	
	/* 切换 */
	function toggleYs(){
		$(".search_area table:eq(1)").toggle();
		if($(".search_area .open").hasClass("close")){
			$(".search_area .open").removeClass("close");
		}else{
			$(".search_area .open").addClass("close");
		}
	}
	////////////////////////////////////////////////////////////////////////
	// z-tree定义基本元素
	var setting = {
		view : {
			selectedMulti : false,
			showLine : true, //是否显示线，true为显示，false为不显示
			showIcon : false,
			dblClickExpand : false
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
		check : {
			enable : false,
			chkStyle : "checkbox",
			chkboxType : {
				"Y" : "p",
				"N" : "s"
			}
		},
		edit: {  
	           enable: true, 
	           editNameSelectAll: true, 
	           removeTitle : "删除",
	           renameTitle : "修改",
	           showRenameBtn: setRenameBtn4,
	           showRemoveBtn: setRemoveBtn4                 
	       },  
		callback : {
			beforeClick : zTreeBeforeClick4,
			beforeRemove: zTreeBeforeRemove4,
			onRename: zTreeOnRename4
		}
	};
	//父节点不显示按钮
	function setRenameBtn4(treeId, treeNode) {
		if(treeNode.id=="-2"||treeNode.id=='私有标签'){
			return false;
		}else if(treeNode.id=="-1"||treeNode.id=='私有标签'){
			return false;
		}else if (treeNode.id=='公有标签') {
			return false;
		}else
			return true;
	}
	function setRemoveBtn4(treeId, treeNode) {
		if(treeNode.isParent){
			return false;
		}
		else if(treeNode.id=="-2"||treeNode.id=='私有标签'){
			return false;
		}else if(treeNode.id=="-1"||treeNode.id=='私有标签'){
			return false;
		}else if (treeNode.id=='公有标签') {
			return false;
		}else
			return true;
		//return !treeNode.isParent;
	}
	function zTreeBeforeClick4(treeId, treeNode, clickFlag){
		tree_LabelID=treeNode.id;
		nodeOfLabel=treeNode.id;
		searchByCondition();
	}
	//删除标签事件
	function zTreeBeforeRemove4(treeId, treeNode){
	          //选中节点  
	          var node = treeNode.id;  
	          //删除标签
	             var a=delLabel(treeNode);
	             if(!a){
	             	return false;
	             }
	}
	//删除标签后台交互
	function delLabel(label){
		
		//判断是否是公有标签或者私有标签，是不能删除
		if(label.id=="-2"||label.id=="-1"||label.id=='私有标签'){

			return false;
		}
		//判断是否有叶子节点，有不能删除
		if(label.children!=undefined){
			var childrenNodes = label.children;
			if (childrenNodes.length>0) {
				return false;
			}
		}
		$.messager.confirm("操作提示", "确认要删除吗？", function(r) {
			if (r) {
				$.ajax({
					type:"post",
					aysnc:false,
					cache:false,
					dataType:"text",
					url:"${ctx}/projectFile/projectFile!delLabel",
					data:{
						"labelParams.labelID":label.id
					},
					error:function(){
						
					},
					success:function(){
// 						showSuccessTip("删除成功！");
						initLabelTreeData();
						$("#tt4").datagrid('reload');
						
					}
				});
			}
		});
	};
	function zTreeOnRename4(){
		 //选中节点  
	          var nodes = $.fn.zTree.getZTreeObj("treeView_4").getSelectedNodes(); 
		 if(nodes[0].text==''||null==nodes[0].text||nodes[0].text==undefined){
			 alert("项目分类名字不能为空！");
			 return false;					 
		 }
		 var flag=true;
		$.ajax({
			type:"post",
			aysnc:false,
			cache:false,
			dataType:"text",
			url:"${ctx}/projectFile/projectFile!changeLabelName",
			data:{
				"labelParams.labelID":nodes[0].id,
				"labelParams.labelText":nodes[0].text
			},
			error:function(){
				showFailureTip("修改失败！");
				$("#tt4").datagrid('reload');
			},
			success:function(data){
				if(data){
					var json = $.parseJSON(data);
					  // 根据处理状态返回相对应提示信息
				    if (json.messageType == "0") {
				    	//成功
				    	
					   	showSuccessTip(json.content,function(){
					   		initLabelTreeData();
							$("#tt4").datagrid('reload');
					   	});
			    		
					}else {							
						// 失败
						showFailureTip(json.content,function(){initLabelTreeData();});
						flag=false;
					}
				}
				
			
			}
		});
		
		return flag;
	}
	//页面展示树设定数据
	function initLabelTreeData() {
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			dataType : "json",
			traditional : true,
			url : "${ctx}/projectFile/projectFile!getCustomLabelsData?moduleCode="+$("#moduleCode").val(),//请求的action路径  
			error : function() {//请求失败处理函数  
				showFailureTip("请求失败！");
			},
			success : function(data) { //请求成功后处理函数。    
				$.fn.zTree.init($("#treeView_4"), setting, data);
			}
		});
		var treeObj = $.fn.zTree.getZTreeObj("treeView_4"); 
		treeObj.expandAll(true);
	}
	//新增项目分类
	function customLabel(){
		var ids=[];
		var rows=$("#project_datagrid").datagrid("getSelections");
		if(rows.length==0&&rows==""){
			alert("至少选择一条项目！","warning");
			return false;
		}else{
			for(var i=0;i<rows.length;i++){
				if(i!=rows.length-1){
		        	ids=ids+rows[i].ID+",";
		        }else{
		          	ids=ids+rows[i].ID;
		        }
			}
			var dialog = $('<div id="dialog"></div>').dialog({
				title:"自定义项目分类",
				width: 900,
				height: 500,
				lock: true,
				min:true,
				max:true,
				href : '${ctx}/projectFile/projectFile!openCustomLabelDialog',
				buttons : [ {
					text : '保存',
					handler : function() {
						var message=saveCustomLabel(ids,$("#moduleCode").val());
				    	if(message){
							if(message == "ok"){
								// 用户更新或增加成功
						    		// 重新加载一览画面
						    		$("#project_datagrid").datagrid('reload');
						    		initLabelTreeData();
						    		alert("添加项目分类成功！");
						    		dialog.dialog('destroy');
							} else{
								// 错误提示
								 alert(message.info);
							}
						// 操作失败
						} else{
							//$.dialog.alert("操作失败！","error");
						}
				    	return false;
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
	};
	//修改项目标签
	function editProjectLabel(){
		var rows=$("#project_datagrid").datagrid("getSelections");
		if(rows.length > 0){
			if(rows.length > 1){
				alert("只能选择一条项目修改！");
				return false;
			}
		}else{
			alert("至少选择一条项目修改！");
			return false;
		}
		var projectId = rows[0].ID;
		var dialog = $('<div id="dialog"></div>').dialog({
			title:"项目分类修改",
			width: 900,
			height: 500,
			lock: true,
			min:true,
			max:true,
			href : '${ctx}/projectFile/projectFile!openEditProjectLabelDialog?projectId='+projectId,
			buttons : [ {
				text : '修改',
				handler : function() {
			    	var message=updateProjectLabels();
			    	if(message){
						if(message == "0"){
							// 用户更新或增加成功
							alert("修改项目分类成功！");
					    		// 重新加载一览画面
					    		$("#tt4").datagrid('reload');
					    		initLabelTreeData();
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