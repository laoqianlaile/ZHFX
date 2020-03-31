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
	<div class="centerNr" style="padding-left: 0px;padding-right: 0px;">
		<!--查询域 -->
		<div class="search_area">
		<table>  
		   <tr>
			   <th>项目名称</th>
			   <td >
				   <input type="text" id="project_name" name = "" class="easyui-textbox"/>
			   </td>
			   <th>总投资</th>
			   <td>
				   <input id="allCaptial1" name="" type="text"  class="easyui-numberbox"  style="width: 100px;" > -
				   <input id="allCaptial2" name="" type="text" class="easyui-numberbox"   style="width: 100px;">
			   </td>
			  <th width="100">所属行业</th>
			  <td>
				  <input type="text" id="industry_code" name="" >
			  </td>
               <th width="100">国标行业</th>
               <td>
                   <input type="text" id="gbindustry_code" name="" >
               </td>
               <td>
                   <input type="button" class="searchBtn" id="search_btn" value="查询" onclick="javascript:searchByCondition()" />
               </td>
		  </tr>
			<tr>
                <th>项目阶段</th>
                <td>
                    <input type="text" id="projectStage"  data-options="editable:false" value="${filters.proStageCode}"/>
                </td>
                <th>拟开工年份</th>
                <td>
                    <input id="startYear" type="text"    style="width: 100px;" />
                    -
                    <input id="endYear" type="text"     style="width: 100px;" />
                </td>
				<th>项目类型</th>
				<td>
					<input type="text" id="check_level" name="" data-options="editable:false"/>
				</td>
                <th>建设地点</th>
                <td>
                    <input type="text" id="project_region" name=""/>
                </td>
				<td>
					<input type="button" class="searchBtn" id="clear_conditions" value="重置" />
				</td>
			</tr>
		</table>
        <%--    隐藏查询条件 --%>
            <input type="hidden" id="builPlaceCode" value="${filters.BuildPlaceCode}" />
            <input type="hidden" id="builPlaceChineseName" value="${filters.builPlaceChineseName}" />
            <input type="hidden" id="proStageCode" value="${filters.proStageCode}" />
            <input type="hidden" id="GovernmentCode" value="${filters.GovernmentCode}" />
            <input type="hidden" id="IndustryCode" value="${filters.IndustryCode}" />
            <input type="hidden" id="expectStartYear" value="${filters.expectStartYear}" />
            <input type="hidden" id="projectType" value="${filters.projectType}" />
            <input type="hidden" id="actrualStartYear" value="${filters.startYear}" />
            <input type="hidden" id="delayYears" value="${filters.delayYears}" />
	<!-- 列表 -->
		<div style="width: 100%">
	        <div style="height:30px;line-height:30px;color:#ccc;font-size:14px;display: none">
	            <span style="margin-left:10px;"><img src="${ctx}/themes/images/lv1.png" title="一级预警 \n'+_tip+'"/>  一级警告</span>
	            <span style="margin-left:10px;"><img src="${ctx}/themes/images/lv2.png" title="二级预警 \n'+_tip+'"/>  二级警告</span>
	        </div>
			<div style="height:550px;">
				<table id="project_datagrid" rownumbers="true" >
					<thead>
						<tr>
							<th field="pro_name" align="left" width="200" formatter="nameFormatter">项目名称</th>
							<th field="invest_total" align="right" width="120" >总投资（万元）</th>
							<th field="pro_type" align="center" width="100">项目类型</th>
                            <th field="gbindustry" align="left" width="150">国标行业</th>
							<th field="industry" align="left" width="150">所属行业</th>
							<th field="build_place" align="left" width="120">建设地点</th>
							<th field="actual_startyear" align="center" width="120"formatter="formatYear">实际开工年份</th>
							<th field="project_stagename" align="left" width="120" >项目阶段</th>
						</tr>
					</thead>
				</table>
			</div>
        </div>
    </div>
<script type="text/javascript">
	var project_datagrid=null;
	$(function (){
		//初始化项目列表
		project_datagrid = $("#project_datagrid").datagrid({
			fit: true,
			url: "projectFile!showNvProject.action",
			queryParams:{
                'filters.warnCode':'${param.warnCode}',
                'filters.builPlaceCode':$("#builPlaceCode").val(),
                'filters.builPlaceChineseName':$("#builPlaceChineseName").val(),
                'filters.proStageCode':$("#proStageCode").val(),
                'filters.GovernmentCode':$("#GovernmentCode").val(),
                'filters.IndustryCode':$("#IndustryCode").val(),
                'filters.expectStartYear':$("#expectStartYear").val(),
                'filters.startYear':$("#actrualStartYear").val(),
                'filters.projectType':$("#projectType").val(),
                'filters.delayYears':$("#delayYears").val(),
                'filters.builPlaceCodeMap':ZHFX.getCookie("BuildPlaceCode")
			},
			border: true,
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景
			height: 580,
			pageSize:10,
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
        //定义跳转到ProjectFile-list页面来源
        var innerPage = '${innerPage}';
        if(innerPage=='1'){
            // 省市级查询
            var _buildPlaceCode = ZHFX.getCookie('BuildPlaceCode');
            // 省市级查询
            $("#buildPlaceCode").val(_buildPlaceCode);
        }

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
	// 取值于 IndustryExtendCode  A
	$("#industry_code").combotree({
		url:'${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryExtendCode',
		multiple:true
	});
        //国标行业（可多选）
        // 取值于 IndustryExtendCode  A
        $("#gbindustry_code").combotree({
            url:'${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',
            multiple:true
        });
    // 年份数组
    var options=[];
    for(var i=2010;i<=2030;i++){
        options.push({"id":i,"text":i+"年"});
    }
    // 拟开工情况 - 区间开始
    $("#startYear").combobox({
        valueField:'id',
        textField:'text',
        data:options
    });
    // 拟开工情况 - 区间结束
    $("#endYear").combobox({
        valueField:'id',
        textField:'text',
        data:options
    });
    //项目阶段
    $("#projectStage").combobox({
        valueField:'id',
        textField:'text',
        data:[{
            "id":0+"",
            "text":"谋划阶段",
             selected:false
        },{
            "id":2,
            "text":"项目决策阶段"
        },{
            "id":4,
            "text":"报建阶段"
        },{
            "id":6,
            "text":"开工建设阶段"
        },{
            "id":8,
            "text":"竣工验收"
        }]

    });
	/**
	 * 重置查询条件
	 */
	$("#clear_conditions").click(function(){
		$("#project_name").textbox("clear");                       //项目名称
		$("#project_region").combotree("clear");          		 //建设地点
		$("#industry_code").combotree("clear");            		 //所属行业
		$("#allCaptial1").numberbox("clear");      	   //总投资1
		$("#allCaptial2").numberbox("clear");      	   //总投资2
		$("#check_level").combotree("clear")         //审批阶段
        $("#startYear").combobox("clear");
        $("#endYear").combobox("clear");
        $("#projectStage").combobox("clear");
        $("#gbindustry_code").combotree("clear");
      //    $("#gbindustry_code").combotree("clear");  //国标行业
    });
    });
	/**
	 * 查询按钮
	 */
	 function searchByCondition(){
		var projectName = $("#project_name").textbox("getValue");                          //项目名称
		var projectRegion=$("#project_region").combotree("getValues").toString();          //建设地点
		var industryCode=$("#industry_code").combotree("getValues").toString();            //所属行业
		var allCaptial1=$("#allCaptial1").numberbox("getValue");   //总投资1
		var allCaptial2=$("#allCaptial2").numberbox("getValue");   //总投资2
		var checkLevel=$("#check_level").combotree("getValues").toString();   //审批阶段
         // 项目阶段
        var projectStage=$("#projectStage").combobox("getValue");
        // 拟开工年份-区间开始
        var  startYear=$("#startYear").combobox("getValue")||"";
        //  拟开工年份-区间结束
        var  endYear=$("#endYear").combobox("getValue")||"";
        //国标行业
        var gbIndustryCode=$("#gbindustry_code").combobox("getValue")||"";
        // 国标行业
      //  var gbindustry_code =$("#gbindustry_code").combobox("getValue")||"";
		// 查询加载
		project_datagrid.datagrid("load", {
			 "paramsMap.projectName"          :projectName,
			 "paramsMap.projectRegion"        :projectRegion,
			 "paramsMap.industryCode"         :industryCode,
            "paramsMap.industryCode"         :gbIndustryCode,
			 "paramsMap.allCaptial1"    	  :allCaptial1,
			 "paramsMap.allCaptial2"   		  :allCaptial2,
			 "paramsMap.checkLevel"           :checkLevel,
            "paramsMap.projectStage"           :projectStage,
            "paramsMap.startYear"           :startYear,
            "paramsMap.endYear"           :endYear,
            'filters.warnCode':'${param.warnCode}', // 预警情况
            'filters.builPlaceCode':$("#builPlaceCode").val(),  // 选择建设地点
            'filters.builPlaceChineseName':$("#builPlaceChineseName").val(),
            'filters.proStageCode':$("#proStageCode").val()  // 选中项目阶段
		});
	}
	function nameFormatter(value,row,index){
		var img="";
		var  _lv=0;
		var _tip="";
		if(value){
			if(row.isimg==0){
				img='<img src="${ctx}/themes/images/pic.png"  title="此项目有调度图片"/>'+img;
			}
			return img+'<a style="font-family:Microsoft YaHei;text-decoration: underline" title="'+value+'" onclick="showDialog(\''+index+'\')" >'+value+'</a>';
		}
	}
	function investFormatter(value,row,index){
		if(value){
			return value/10000;
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
		ZHFX.showOuterWindow("https://59.255.137.5/tzxmapp/pages/afgw/statistics/chart/projectall.jsp?id="+ rows[index].id+"&project_code="+rows[index].project_code+"&area_code="+rows[index].area_code+"&plate_id="+rows[index].plate_id);//传入当前行的项目ID
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