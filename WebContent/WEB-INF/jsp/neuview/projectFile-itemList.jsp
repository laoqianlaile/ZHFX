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
<script src="${ctx}/js/base.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui1.4/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/common/extend.js"></script>
<script type="text/javascript">
   $(function(){
	   $("#toolbar > span:eq(3)").remove();
   })
</script>
<style>
	.search_area table th, .search_area table td, .search_area span input[type='text']{
		color : white
	}
</style>
</head>

<body>
<%--<div id="logo"></div>--%>
<%--<jsp:include page="./../common/toolbar.jsp"></jsp:include>--%>
<div>
        <div class="centerNr">
        	<%--<span class="loginInfo"><img src="${ctx}/themes/images/user.png">${SYS_USER_INFO}<a href="javascript:ZHFX.logout()">【退出】</a></span>--%>
            <%--<span class="currenttWz">当前位置：基础业务 > 项目档案</span>--%>
            <div>
               <!--查询域 -->
<!--                 <div class="search_area"> -->
<!-- 					<table >   -->
<!-- 					   <tr> -->
<!-- 						  <th width="150">所属行业</th> -->
<!-- 						  <td> -->
<!-- 							  <input type="text" id="industry_code" name="" > -->
<!-- 						  </td> -->
<!-- 						  <th>拟开工年份</th> -->
<!-- 						  <td> -->
<!-- 							  <input type="text" id="project_region" name=""/> -->
<!-- 						  </td> -->
						  
<!-- 						   <th>项目类型</th> -->
<!-- 						  <td> -->
<!-- 							  <input id="fit_ind_policy_code" type="text" name="fitIndPolicyCode" class="easyui-combotree" 
							  data-options="url:'${ctx}/prosearch/projectSearch!getIndustry.action?groupNo=9',multiple:true">   -->
<!-- 						  </td> -->
						  
<!-- 					  </tr> -->
<!-- 					  <tr> -->
<!-- 					      <th>项目实施进展</th> -->
<!-- 					  	  <td> -->
<!-- 					  		  <input type="text" id="project_imple_pro" name ="" data-options="editable:false"/> -->
<!-- 					  	  </td> -->
<!-- 					  	  <th>是否PPP</th> -->
<!-- 						  <td> -->
<!-- 							  <input id="is_ppp" type="text" name="" data-options="editable:false"/> -->
<!-- 						  </td> -->
<!-- 						  <th>是否专项建设基金</th> -->
<!-- 						  <td> -->
<!-- 							  <input id="is_spec_funds" type="text" name="" data-options="editable:false"/> -->
<!-- 						  </td> -->
<!-- 					  </tr> -->
					  
<!-- 					  <tr> -->
<!-- 						  <th>项目名称</th> -->
<!-- 					  	  <td > -->
<!-- 					  		  <input type="text" id="project_name" name = "" class="easyui-textbox"/> -->
<!-- 					  	  </td> -->
<!-- 					  	  <th>拟竣工年份</th> -->
<!-- 					      <td colspan="5"> -->
<!-- 						      <input type="text" id="expect_end_year1" data-options="editable:false" name="" /> - -->
<!-- 						      <input type="text" id="expect_end_year2" data-options="editable:false" name="" /> -->
<!-- 					      </td> -->
<!-- 					  </tr> -->
					  
<!-- 					  <tr> -->
<!-- 					    <th>拟开工年、月</th> -->
<!-- 					    <td colspan="5"> -->
<!-- 						    <input type="text" id="expect_start_year1" data-options="editable:false" style="width:120px;"/> 年  -->
<!-- 						    <input type="text" id="expect_start_month1" data-options="editable:false" style="width:120px;" /> 月- -->
<!-- 						    <input type="text" id="expect_start_year2" data-options="editable:false" style="width:120px;" /> 年  -->
<!-- 						    <input type="text" id="expect_start_month2" data-options="editable:false" style="width:120px;" /> 月 -->
<!-- 					    </td> -->
<!-- 					  </tr> -->
<!-- 					</table> -->
					
<!-- 					<table style="display:none;"> -->
<!-- 						<tr> -->
<!-- 						   <th width="150">项目申报日期</th> -->
<!-- 						   申报日期 时间段 -->
<!-- 						   <td> -->
<!-- 						  	  <input id="proApplyStartTime" name="" type="text" class="easyui-datebox" data-options="editable:false"> -  -->
<!-- 						 	  <input id="proApplyEndTime" name="" type="text"  class="easyui-datebox" data-options="editable:false"> -->
<!-- 						  </td> -->
<!-- 						  <th>年度计划申报日期</th> -->
<!-- 						  <td> -->
<!-- 							  <input id="yearApplyStartTime" name="" type="text"  class="easyui-datebox" data-options="editable:false"> -  -->
<!-- 							  <input id="yearApplyEndTime" name="" type="text" class="easyui-datebox" data-options="editable:false"> -->
<!-- 						  </td> -->
<!-- 					  </tr> -->
<!-- 					  <tr> -->
<!-- 						  <th>专项建设基金申报日期</th> -->
<!-- 						  <td> -->
<!-- 							  <input id="specialStartTime" name="" type="text"  class="easyui-datebox" data-options="editable:false"> -  -->
<!-- 							  <input id="specialEndTime" name="" type="text" class="easyui-datebox" data-options="editable:false"> -->
<!-- 						  </td> -->
<!-- 						  <th>中央预算内申请规模</th> -->
<!-- 						  <td> -->
<!-- 							  <input type="text" id="centSizeL"  name="" class="easyui-numberbox"/> -  -->
<!-- 							  <input type="text" id="centSizeR"  name="" class="easyui-numberbox"/> -->
<!-- 						  </td> -->
<!-- 					  </tr> -->
<!-- 					  <tr> -->
<!-- 					  	<th>专项建设基金需求规模</th> -->
<!-- 						  <td> -->
<!-- 							  <input type="text" id="specialReqSizeL"  name="" class="easyui-numberbox"/> -  -->
<!-- 							  <input type="text" id="specialReqSizeR"  name="" class="easyui-numberbox"/> -->
<!-- 						  </td> -->
<!-- 						  <th>专项建设基金申请规模</th> -->
<!-- 						  <td> -->
<!-- 							  <input type="text" id="specialSizeL"  name="" class="easyui-numberbox"/> -  -->
<!-- 							  <input type="text" id="specialSizeR"  name="" class="easyui-numberbox"/> -->
<!-- 						  </td> -->
<!-- 					  </tr> -->
<!-- 					  <tr> -->
<!-- 					  	 <th>中央预算内需求规模</th> -->
<!-- 						  <td> -->
<!-- 							  <input type="text" id="centerReqSizeL"  name="" class="easyui-numberbox"/> -  -->
<!-- 							  <input type="text" id="centerReqSizeR" name="" class="easyui-numberbox"/> -->
<!-- 						  </td> -->
<!-- 						  <th>项目状态</th> -->
<!-- 						  <td> -->
<!-- 							  <input type="text" id="project_status" data-options="editable:false"/> -->
<!-- 						  </td> -->
<!-- 					  </tr> -->
<!-- 					  <tr> -->
<!-- 						  <th>审批阶段</th> -->
<!-- 						  <td> -->
<!-- 							  <input type="text" id="check_level" name="" data-options="editable:false"/> -->
<!-- 						  </td> -->
						  
<!-- 					  </tr> -->
<!-- 					</table>	 -->
<!-- 					<div class="opy"> -->
<!-- 						<input type="button" class="searchBtn" id="search_btn" value="查询" /> -->
<!-- 						<input type="button" class="searchBtn" id="clear_conditions" value="重置" /> -->
<!-- 					<span class="open" onclick="toggleYs();"></span> 	 -->
<!-- 					</div> -->
					
				</div>
				
				<!-- 列表 -->
				<div class="box" style="padding-top:10px;height:1350px">
					<table id="project_datagrid" rownumbers="true"> 
						<thead>
							<tr>
								<th field="projectName" align="left" width="200" formatter="nameFormatter">项目名称</th>
								<th field="itemName" align="center" width="100">审批事项</th>
								<th field="deptName" align="center" width="100">审批部门</th>
								<th field="approveDate" align="center" width="100">办理时间</th>
								<th field="state" align="center" width="100">办理状态</th>
								<!-- <th field="appStage" align="center" width="120">项目成熟度</th>   -->
								<input type="hidden" id="regParam" value="${regParam}" />
								<input type="hidden" id="is_ent" value="${filters.is_ent}" />
								<input type="hidden" id="projectOrItem" value="${filters.projectOrItem}" />
								<input type="hidden" id="projectStage" value="${filters.projectStage}" />
								<input type="hidden" id="goverLevel" value="${filters.goverLevel}" />
								<input type="hidden" id="investRange" value="${filters.investRange}" />
								<input type="hidden" id="month" value="${filters.month}" />
								<input type="hidden" id="production" value="${filters.production}" />
								<input type="hidden" id="gbIndustry" value="${filters.gbIndustry}" />
								<input type="hidden" id="wnIndustry" value="${filters.wnIndustry}" />
								<input type="hidden" id="shbType" value="${filters.shbType}" />
								<input type="hidden" id="expectStartTime" value="${filters.expectStartTime}" />
								<input type="hidden" id="department" value="${filters.department}" />
								<input type="hidden" id="item" value="${filters.item}" />
								<input type="hidden" id="itemLevle" value="${filters.itemLevle}" />							
							</tr>
						</thead>
					</table>
				</div>
            </div>
        </div>
</div>

<script type="text/javascript">
function cjkEncode(text) {
	if (text == null) {
		return "";
	}
	var newText = "";
	for (var i = 0; i < text.length; i++) {
		var code = text.charCodeAt (i);
		if (code >= 128 || code == 91 || code == 93) {  //91 is "[", 93 is "]".
			newText += "[" + code.toString(16) + "]";
		} else {
			newText += text.charAt(i);
		}
	}
	return newText;
}
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
	//debugger;
	var thisYear=new Date().getUTCFullYear();//今年
	var endYear=thisYear+50;//结束年份
	//数组添加值（2012-2016）//根据情况自己修改
	for(startYear=endYear-99;startYear<=endYear;startYear++){
		data2.push({label:startYear,value:startYear});
     }
		
	$(function (){
		//初始化项目列表
		project_datagrid = $("#project_datagrid").datagrid({
			fit: true,
			url: "projectFile!showItems.action",
			queryParams:{
				'regParam':$("#regParam").val(),
				'filters.is_ent':$("#is_ent").val(),
				'filters.projectOrItem':$("#projectOrItem").val(),
				'filters.projectStage':$("#projectStage").val(),
				'filters.goverLevel':$("#goverLevel").val(),
				'filters.investRange':$("#investRange").val(),
				'filters.month':$("#month").val(),
				'filters.production':$("#production").val(),
				'filters.gbIndustry':$("#gbIndustry").val(),
				'filters.wnIndustry':$("#wnIndustry").val(),
				'filters.shbType':$("#shbType").val(),
				'filters.expectStartTime':$("#expectStartTime").val(),
				'filters.department':$("#department").val(),
				'filters.item':$("#item").val(),
				'filters.itemLevle':$("#itemLevle").val()
			},
			border: true,
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景
			height: 585,
			pageSize:50,
			singleSelect : true,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			remoteSort : false,
			pagination : true,//分页
			onLoadSuccess : function(data) {
				//取消所有的已选择项
				$(this).datagrid('clearSelections');
			}
		});
		$("li:contains('项目档案')").addClass("active");
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
		url:'${ctx}/prosearch/projectSearch!getFromCache.action?groupNo=A00012',
		multiple:true
	});
	
	//建设地点（可多选）
	$("#project_region").combotree({
		url:'${ctx}/prosearch/projectSearch!getFromCache.action?groupNo=A00001',
		multiple:true
	});
	
	//所属行业（可多选）
	$("#industry_code").combotree({
		url:'${ctx}/neuview/projectSearch!getFromCache.action?groupNo=A00003',
		multiple:true
	});
	
	//政府投资方向（可多选）
	$("#fit_ind_policy_code").combotree({
		url:'${ctx}/prosearch/projectSearch!getFromCache.action?groupNo=A00009',
		multiple:true
	});
	
	//项目实施进展
	$("#project_imple_pro").combotree({
		url:'${ctx}/prosearch/projectSearch!getFromCache.action?groupNo=A00017',
	});
	
	//项目状态
	$("#project_status").combotree({
		url:'${ctx}/prosearch/projectSearch!getFromCache.action?groupNo=A00008',
	});
	
	//是否PPP
	$("#is_ppp").combobox({
		valueField:'id',
		textField:'text',
		data:[{text: '是',id: '1'},{text: '否',id: '0'}]
	});
	
	//是否专项建设基金
	$("#is_spec_funds").combobox({
		valueField:'id',
		textField:'text',
		data:[{text: '全部',id: ''},{text: '是',id: '1'},{text: '否',id: '0'}]
	});
	
	/**
	 * 重置查询条件
	 */
	$("#clear_conditions").click(function(){
		$("#expect_start_year1").combobox("setValue","");                //拟开工年份1（查询时第一个时间）
		$("#expect_start_year2").combobox("setValue","");                //拟开工年份2（查询时第二个时间）下同
		$("#expect_start_month1").combobox("setValue","");               //拟开工月份1
		$("#expect_start_month2").combobox("setValue","");               //拟开工月份2
		$("#expect_end_year1").combobox("setValue","");                  //拟竣工年份1
		$("#expect_end_year2").combobox("setValue","");                  //拟竣工年份2
		$("#project_name").textbox("setValue","");                       //项目名称
		$("#fit_ind_policy_code").combotree("setValue",null);              //政府投资方向
		$("#project_region").combotree("setValue","");          		 //建设地点
		$("#industry_code").combotree("setValue","");            		 //所属行业
		$("#project_imple_pro").combotree("setValue","");     			 //项目实施进展
		$("#is_ppp").combobox("setValue","");                            //是否ppp
		$("#is_spec_funds").combobox("setValue","");             		 //是否专项建设基金
		$("#proApplyStartTime").datebox("setValue","");    //项目申报日期1
		$("#proApplyEndTime").datebox("setValue","");      //项目申报日期2
		$("#yearApplyStartTime").datebox("setValue","");   //年度计划申报日期1
		$("#yearApplyEndTime").datebox("setValue","");     //年度计划申报日期2
		$("#specialStartTime").datebox("setValue","");     //专项建设基金申报日期1
		$("#specialEndTime").datebox("setValue","");       //专项建设基金申报日期2
		$("#centSizeL").numberbox("setValue","");      	   //中央预算内申请规模1
		$("#centSizeR").numberbox("setValue","");      	   //中央预算内申请规模2
		$("#specialSizeL").numberbox("setValue","");   	   //专项建设基金申请规模1
		$("#specialSizeR").numberbox("setValue","");   	   //专项建设基金申请规模2
		$("#specialReqSizeL").numberbox("setValue","");    //专项建设基金需求规模1
		$("#specialReqSizeR").numberbox("setValue","");    //专项建设基金需求规模2
		$("#centerReqSizeL").numberbox("setValue","");     //中央预算内需求规模1
		$("#centerReqSizeR").numberbox("setValue","");     //中央预算内需求规模2
		$("#check_level").combotree("setValue","")         //审批阶段
		$("#project_status").combobox("setValue","");      //项目状态
	});
	/**
	 * 查询按钮
	 */
	$("#search_btn").click(function(){
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
		var fitIndPolicyCode=$("#fit_ind_policy_code").combotree("getValues").toString();  //政府投资方向
		var projectRegion=$("#project_region").combotree("getValues").toString();          //建设地点
		var industryCode=$("#industry_code").combotree("getValues").toString();            //所属行业
		var projectImplePro=$("#project_imple_pro").combotree("getValue");     //项目实施进展
		var isPpp=$("#is_ppp").combobox("getValue");                          //是否ppp
		var isSpecFunds=$("#is_spec_funds").combobox("getValue");             //是否专项建设基金
		var projectApplyTime1=$("#proApplyStartTime").datebox("getValue");  //项目申报日期1
		var projectApplyTime2=$("#proApplyEndTime").datebox("getValue");    //项目申报日期2
		var yearPlanRepTime1=$("#yearApplyStartTime").datebox("getValue");  //年度计划申报日期1
		var yearPlanRepTime2=$("#yearApplyEndTime").datebox("getValue");    //年度计划申报日期2
		var specialRepTime1=$("#specialStartTime").datebox("getValue");     //专项建设基金申报日期1
		var specialRepTime2=$("#specialEndTime").datebox("getValue");       //专项建设基金申报日期2
		var cupApplyBudCaptial1=$("#centSizeL").numberbox("getValue");      //中央预算内申请规模1
		var cupApplyBudCaptial2=$("#centSizeR").numberbox("getValue");      //中央预算内申请规模2
		var cupApplySpeCaptial1=$("#specialSizeL").numberbox("getValue");   //专项建设基金申请规模1
		var cupApplySpeCaptial2=$("#specialSizeR").numberbox("getValue");   //专项建设基金申请规模2
		var cupNeedSpeCaptial1=$("#specialReqSizeL").numberbox("getValue"); //专项建设基金需求规模1
		var cupNeedSpeCaptial2=$("#specialReqSizeR").numberbox("getValue"); //专项建设基金需求规模2
		var cupNeedBudCaptial1=$("#centerReqSizeL").numberbox("getValue");  //中央预算内需求规模1
		var cupNeedBudCaptial2=$("#centerReqSizeR").numberbox("getValue");  //中央预算内需求规模2
		var checkLevel=$("#check_level").combotree("getValues").toString();   //审批阶段
		var projectStatus=$("#project_status").combobox("getValue");          //项目状态
		project_datagrid.datagrid("load", {
			 planStartYear1       :planStartYear1,
			 planStartYear2       :planStartYear2,
			 planStartMonth1      :planStartMonth1,
			 planStartMonth2      :planStartMonth2,
			 planEndYear1         :planEndYear1,
			 planEndYear2         :planEndYear2,
			 projectName          :projectName,
			 fitIndPolicyCode     :fitIndPolicyCode,
			 projectRegion        :projectRegion,
			 industryCode         :industryCode,
			 projectImplePro      :projectImplePro,
			 isPpp                :isPpp,
			 isSpecFunds          :isSpecFunds,
			 projectApplyTime1    :projectApplyTime1,
			 projectApplyTime2    :projectApplyTime2,
			 yearPlanRepTime1     :yearPlanRepTime1,
			 yearPlanRepTime2     :yearPlanRepTime2,
			 specialRepTime1      :specialRepTime1,
			 specialRepTime2      :specialRepTime2,
			 cupApplyBudCaptial1  :cupApplyBudCaptial1,
			 cupApplyBudCaptial2  :cupApplyBudCaptial2,
			 cupApplySpeCaptial1  :cupApplySpeCaptial1,
			 cupApplySpeCaptial2  :cupApplySpeCaptial2,
			 cupNeedSpeCaptial1   :cupNeedSpeCaptial1,
			 cupNeedSpeCaptial2   :cupNeedSpeCaptial2,
			 cupNeedBudCaptial1   :cupNeedBudCaptial1,
			 cupNeedBudCaptial2   :cupNeedBudCaptial2,
			 checkLevel           :checkLevel,
			 projectStatus        :projectStatus
		});
	});
	
	/**
	 * 验证查询条件是否合理
	 */
	function checkData(){
		if(($("#proApplyStartTime").datebox('getValue')>$("#proApplyEndTime").datebox('getValue'))&& $("#proApplyEndTime").datebox('getValue') != ""){
			alert("项目申报日期开始时间不能大于结束时间");
			return 0;
		}else if(($("#yearApplyStartTime").datebox('getValue')>$("#yearApplyEndTime").datebox('getValue'))&& $("#yearApplyEndTime").datebox('getValue')!=""){
			alert("年度计划申报日期开始时间不能大于结束时间");
			return 0;
		}else if(($("#specialStartTime").datebox('getValue')>$("#specialEndTime").datebox('getValue'))&& $("#specialEndTime").datebox('getValue')!=""){
			alert("专项建设基金申报日期开始时间不能大于结束时间");
			return 0;
		}else if(($("#expect_end_year1").combobox('getValue')>$("#expect_end_year1").combobox('getValue'))&& $("#expect_end_year1").combobox('getValue')!=""){
			alert("竣工或拟建成年份开始时间不能大于结束时间");
			return 0;
		}else if(($("#centSizeL").numberbox('getValue')>$("#centSizeR").numberbox('getValue'))&& $("#centSizeR").numberbox('getValue')!=""){
			alert("中央预算内申请规模最小值不能大于最大值");
			return 0;
		}else if(($("#specialReqSizeL").numberbox('getValue')>$("#specialReqSizeR").numberbox('getValue'))&& $("#specialReqSizeR").numberbox('getValue')!=""){
			alert("专项建设基金需求规模最小值不能大于最大值");
			return 0;
		}else if(($("#specialSizeL").numberbox('getValue')>$("#specialSizeR").numberbox('getValue'))&& $("#specialSizeR").numberbox('getValue')!=""){
			alert("专项建设基金申请规模最小值不能大于最大值");
			return 0;
		}else if(($("#centerReqSizeL").numberbox('getValue')>$("#centerReqSizeR").numberbox('getValue'))&& $("#centerReqSizeR").numberbox('getValue')!=""){
			alert("中央预算内需求规模最小值不能大于最大值");
			return 0;
		}else if(
				$("#expect_start_year1").combobox('getValue')>$("#expect_start_year2").combobox('getValue') || 
				($("#expect_start_year1").combobox('getValue')==$("#expect_start_year2").combobox('getValue') 
						&& $("#expect_start_month1").combobox('getValue')>$("#expect_start_month2").combobox('getValue'))){
			alert("开工或拟开工开始时间不能大于结束时间");
			return 0;
		}
	}
	
	/**
	 * 项目名称操作列
	 */
	function nameFormatter(value,row,index){
		if(value){
			//alert(value);
			return '<a style="font-family:Microsoft YaHei;color:#ffffff;text-decoration: underline" title="'+value+'" onclick="showDialog(\''+index+'\')" >'+value+'</a>';
		}
	}
	
	/**
	 * 跳转到该项目的项目档案页
	 */
	 function showDialog(index){
		var rows = $('#project_datagrid').datagrid('getRows');
//	 	ZHFX.toUrl("/fiveplan/projectFile!details?projectGuid="+ rows[index].projectGuid);//传入当前行的项目ID
//	 	alert(rows[index].projectCode);
//	 	window.location.href("http://www.baidu.com");
//      http://web41.ndrc.gov.cn:6888/tzxmspall/tzxmapp/pages/queryMajorProjects/queryMajorProjects.jsp?projectCode=2015-000052-76-01-500282
//	 	window.open("http://web41.ndrc.gov.cn:6888/tzxmspall/tzxmapp/pages/queryMajorProjects/queryMajorProjects.jsp?projectCode="+rows[index].projectCode);
	 	//ZHFX.toUrl("http://www.baidu.com");//传入当前行的项目ID
		window.open("https://59.255.137.5/tzxmapp/pages/queryMajorProjects/queryMajorProjects.jsp?other_flag=1&projectCode="+rows[index].projectCode,"blank");
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
	
	
</script>
</body>
</html>