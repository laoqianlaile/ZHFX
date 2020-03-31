<%@ page language="java" pageEncoding="UTF-8" 
	contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui1.4/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui1.4/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui1.4/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript">
	/**
	 * @requires jQuery,EasyUI
	 *
	 * 扩展datagrid的datebox、datetimebox格式化
	 *
	 */
	$.fn.datebox.defaults.formatter = function(date){
		var vDate = new Date(date);
		return vDate.format('yyyy-MM-dd');
	}
	$.fn.datebox.defaults.parser = function(s) {
		if (!s)
			return new Date();
		var ss = s.split('-');
		var y = parseInt(ss[0], 10);
		var m = parseInt(ss[1], 10);
		var d = parseInt(ss[2], 10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
			return new Date(y, m - 1, d);
		} else {
			return new Date();
		}
	};
	
	//当前用户ID
	var curUser = '${SYS_USER_ID}';
	
	$(function(){
		$('#sshy').combotree({
		    url:'tree_data.json'
		});
		initMenuList();
	});
	
	/**
	 * 检查模版是否要另存为
	 */
	function checkSaveAs() {
		//debugger;
		//iframe的ID
		var frameId = $('iframe').attr('id');
		//iframe的地址
		var src = $('iframe').attr('src');
		//alert($('#editLink').is(":hidden"));
		//编辑按钮显示，表示打开的是模版页面
		if (!$('#editLink').is(":hidden")) {
			/**
			 * 先获取模版信息
			 */
			// ID
			//var BIwin = window.frames[frameId].contentWindow||;
			var reportId = $('iframe')[0].contentWindow.Data.SharingPool.get('reportId');
			// 名称
			var reportName = $('iframe')[0].contentWindow.Data.SharingPool.get('reportName');
			
			//将名称存入session
			saveSessionForReportName(reportName, frameId);
			
			// 创建者
			var createBy = $('iframe')[0].contentWindow.Data.SharingPool.get('createBy');
			
			//alert(reportId+","+reportName+","+curUser);
			//创建用户不相等
			if (createBy != curUser) {
				// 保存模版信息到我创建的
				$.post('${ctx}/ReportServer?op=fr_bi&cmd=report_save_as',
						{
							report_id : reportId,
							report_name : reportName,
							create_by : createBy,
							report_location : -1,
							real_time : false
						},
						function(data) { 
							//保存完毕之后，改变iframe的src
							if (data) {
								//转为json对象
								var json = eval('(' + data + ')');
								if (json) {
									//获取保存之后的模版ID
									var id = json['reportId'];
									//得到id索引位置
									var index = src.indexOf('&id=');
									//改变地址（非编辑状态）
									var src1 = src.substring(0, index) + '&id=' + id 
								    + '&show=_bi_show_&createBy=' + curUser;
									//改变地址（编辑状态）
									src = src.substring(0, index) + '&id=' + id 
									    + '&show=_bi_show_&createBy=' + curUser +'&edit=_bi_edit_';
									//将非编辑状态地址存入session中
									saveSessionForReportName(src1,(frameId+'Src'));
									//改变iframe引用地址
									$('iframe').attr('src', src);
								}
							}
				}, 'text');
			} else {
				//若当前未处于编辑状态
				if (src.indexOf('&edit=_bi_edit_') < 0) {
					//改变引用地址，变为编辑状态
					document.getElementById($('iframe').attr('id')).src = src+'&edit=_bi_edit_';
					setTimeout(reinitIframe(),5000);
				}
			}
		}
	}
	
	/**
	 * 存入session
	 * @reportName 需存储的值
	 * @frameId 标识存储的ID
	 */
	function saveSessionForReportName(reportName, frameId) {
		$.post('${ctx}/screenSaver/screenSaver!saveSessionForReportName.action',
				{
					report_name : reportName,
					frameId : frameId,
				},
				function (data) {
					//alert(data);
				}, 
		'text');
	}
	
	/**
	 * 点击编辑
	 */

	
	/**
	 * 初始化菜单
	 */
	function initMenuList() {
		$.ajax({
	        type : 'POST',
	        dataType : 'text',
	        url: ctx + '/menu/menu!initMenuList',
	        success:function(data){ //请求成功后处理函数。
	        	if (data) {
	        		var map = eval('(' + data + ')');
	        		$('#base-menu').html(map.baseInfo);
	        		$('#zdyw-menu').html(map.impInfo);
	        	}
	        },
	        error: function () {//请求失败处理函数
	            alert("数据获取失败！");
	        }
	    });
	}
	
</script>
<div id="toolbar">
	<span><a href="#" title="返回上一页" class="btn-back" onclick="history.go(-1)"></a></span>
	<span><a href="#" title="主页" class="btn-index" onclick="goHomePage();"></a></span>
	<span class="MenuList"><a href="#" title="菜单" class="btn-list"></a>
		<ul class="listMenu">
			<h6>
				<a href="javascript:void(0)" id="base" class="active">基础业务</a>
				<a href="javascript:void(0)" id="zdyw">重点业务</a>
			</h6>
			<div id="base-menu">
				
			</div>
			<div id="zdyw-menu" style="display:none;">
				<li>
					<a href="javascript:ZHFX.toUrl('/approvalRecord/common-approvalRecord')">审核备项目信息汇总</a>
					<ul>
						<li><a href="javascript:ZHFX.toUrl('/approvalRecord/common-approvalRecord')">审核备项目信息趋势情况</a></li>
						<li><a href="javascript:ZHFX.toUrl('/approvalRecord/common-distributionSituation')">审核备项目信息分维度汇总</a></li>
					</ul>
				</li>
<!-- 				<li><a href="javascript:ZHFX.toUrl('/statis/statis!investTrend')">投资形势分析</a></li> -->
				<li><a href="javascript:ZHFX.toUrl('/release/release-overview')">放管服改革</a>
					<ul>
						<li><a href="javascript:ZHFX.toUrl('/release/release-overview')">简政放权</a></li>
						<li><a href="javascript:ZHFX.toUrl('/release/release-manage')">放管结合</a></li>
						<li><a href="javascript:ZHFX.toUrl('/release/release-optimization')">优化服务</a></li>
					</ul>
				</li>
				<li><a href="javascript:ZHFX.toUrl('/fine/fineManagement-view')">政府投资精细化管理</a></li>
				<li><a href="javascript:void(0)">预测预警分析</a>
					<ul>
						<li><a href="javascript:void(0)">项目实施进展监测预警分析</a></li>
						<li><a href="javascript:void(0)">项目办理进展监测预警分析</a></li>
						<li><a href="javascript:void(0)">用户分布情况</a></li>
					</ul>
				</li>
				<li><a href="javascript:ZHFX.toUrl('/analysis/analysis-report')">综合分析报告</a></li>
			</div>
		</ul>
	</span>
	<span><a href="#" title="搜索" class="btn-search" id="btnSearch"></a>
		<ul class="listSearch">
			<div>
			<h6>搜索条件</h6>
			<form action="" id="paramsForm" name="paramsForm">
				<div>
					<!-- 系统获取的系统年份 -->
					<input type="hidden" value="${systemYear}" id="systemYear"  name="systemYear" />
					<!-- 多数据集uuid -->
					<input type="hidden" id="uuid1" name="" value="" />
					<input type="hidden" id="uuid2" name="" value="" />
					<input type="hidden" id="uuid3" name="" value="" />
					<input type="hidden" id="uuid4" name="" value="" />
					<!-- 报表请求地址，如 ip:端口号/工程名称 -->
					<input type="hidden" id="reportRequestUrl" name="reportRequestUrl" value="${reportRequestUrl}"/>
					<!--其他 请求地址，如 ip:端口号/工程名称 -->
					<input type="hidden" id="reportUrl" name="reportUrl" value="${reportUrl}"/>
					<!-- 模块的类型，如 五年储备 -->
					<input type="hidden" id="type" name="type" value="${type}"/>
					<!-- 报表的类型，如 分地区 -->
					<input type="hidden" id="reportType" name="reportType" value="${reportType}"/>
					<!-- 图表的名字 -->
					<input type="hidden" id="chartName" name="chartName" value="${chartName}"/>
				</div>
			<table class="listSearchdiv">
	  <tr>
		  <th>项目申报日期</th>
		  <!-- 申报日期 时间段 -->
		  <td colspan="3"><span class="dateText"><input id="proApplyStartTimeId" name="reportParamsMap.projectDateStart" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="proApplyEndTimeId" name="reportParamsMap.projectDateEnd" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
	  </tr>
	  <tr>
	  <th>年度计划申报日期</th>
		  <td colspan="3"><span class="dateText"><input id="yearApplyStartTimeId" name="reportParamsMap.yearPlanDateStart" type="text"  class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="yearApplyEndTimeId" name="reportParamsMap.yearPlanDateEnd" type="text" class="easyui-datebox" data-options="editable:false"></span></td>
	  </tr>
	  <tr>
		  <th>专项建设基金申报日期</th>
		  <td colspan="3"><span class="dateText"><input id="specialStartTimeId" name="reportParamsMap.specialDateStart" type="text"  class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="specialEndTimeId" name="reportParamsMap.specialDateEnd" type="text" class="easyui-datebox" data-options="editable:false"></span></td>
	  </tr>
	  <tr>
	  <th>竣工或拟建成年份</th>
		  <td colspan="3"><span class="dateText"><input type="text" id="yearChoose1" name="reportParamsMap.endYear1" class="easyui-combobox" data-options="editable:false"/></span> - <span class="dateText"><input type="text" name="reportParamsMap.endYear2" id="yearChoose2" class="easyui-combobox" data-options="editable:false"></span></td>
	  </tr>
	  <tr>
		  <th>中央预算内申请规模</th>
		  <td colspan="3"><span class="dateText"><input type="text" id="centSizeLId"  name="reportParamsMap.centerApplyScaleStart" class="easyui-numberbox"></span> - <span class="dateText"><input type="text" id="centSizeRId"  name="reportParamsMap.centerApplyScaleEnd" class="easyui-numberbox"></span></td>
	  </tr>
	  <tr>
	  <th>专项建设基金需求规模</th>
		  <td colspan="3"><span class="dateText"><input type="text" id="specialReqSizeLId"  name="reportParamsMap.specialDemandScaleStart" class="easyui-numberbox" ></span> - <span class="dateText"><input type="text" id="specialReqSizeRId"  name="reportParamsMap.specialDemandScaleEnd" class="easyui-numberbox"></span></td>
	  </tr>
	  <tr>
		  <th>专项建设基金申请规模</th>
		  <td colspan="3"><span class="dateText"><input type="text" id="specialSizeLId"  name="reportParamsMap.specialApplyScaleStart" class="easyui-numberbox"></span> - <span class="dateText"><input type="text" id="specialSizeRId"  name="reportParamsMap.specialApplyScaleEnd" class="easyui-numberbox"></span></td>
	  </tr>
	  <tr>
	  <th>中央预算内需求规模</th>
		  <td colspan="3"><span class="dateText"><input type="text" id="centerReqSizeLId"  name="reportParamsMap.centerDemandScaleStart" class="easyui-numberbox"></span> - <span class="dateText"><input type="text" id="centerReqSizeRId" name="reportParamsMap.centerDemandScaleEnd" class="easyui-numberbox"></span></td>
	  </tr>
	  <tr>
		  <th>开工或拟开工时间</th>
		  <td colspan=3><span class="dateText"><input type="text" id="startYear1" style="width:60px;" name="reportParamsMap.startYear1" data-options="editable:false" class="easyui-combobox"></span> 年&nbsp;<span class="dateText"><input type="text" name="reportParamsMap.StartMonth1" id="startMonth1" style="width:60px;" data-options="editable:false" class="easyui-combobox"></span>月-&nbsp;<span class="dateText"><input type="text" id="startYear2" style="width:60px;" name="reportParamsMap.startYear2" data-options="editable:false" class="easyui-combobox"></span> 年&nbsp;<span class="dateText"><input type="text" name="reportParamsMap.startMonth2" id="startMonth2" style="width:60px;" data-options="editable:false" class="easyui-combobox"></span>月</td>

	  </tr>
	  <tr>
		  <th>所属行业</th>
<%-- 		  url:'${pageContext.request.contextPath}/prosearch/projectSearch!getFromCache.action?groupNo=A00003', --%>
		  <td><span class="dateText"><input type="text" id="indId" name="reportParamsMap.industry" class="easyui-combotree" data-options="multiple:true,panelWidth:'auto'"></span></td>
<%-- 		  url:'${pageContext.request.contextPath}/prosearch/projectSearch!getFromCache.action?groupNo=A00001', --%>
		  <th>建设地点</th>
		  <td><span class="dateText"><input type="text" id="areaId" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="multiple:true,panelWidth:'auto'"></span></td>
	  </tr>
	  <tr>
		  <th>项目实施进展</th>
<%-- 		  ,url:'${pageContext.request.contextPath}/prosearch/projectSearch!getFromCache.action?groupNo=A00017' --%>
		  <td><span class="dateText"><input type="text" id="prosId" name="reportParamsMap.projectImplementationProgress" class="easyui-combotree" data-options="panelHeight: 'auto'"></span></td>
		   <th>是否专项建设基金</th>
		  <td><span class="dateText"><input id="spcId" type="text" name="reportParamsMap.whetherSpecial" class="easyui-combobox" data-options="editable:false,panelHeight: 'auto',valueField: 'value',textField: 'label',data: [{label: '全部',value: ''},{label: '是',value: '1'},{label: '否',value: '0'}],value:''"></span></td>
	  </tr>
	  <tr>
		  <th>审批阶段</th>
<%-- 		  ,url:'${pageContext.request.contextPath}/prosearch/projectSearch!getFromCache.action?groupNo=A00012' --%>
		  <td><span class="dateText"><input type="text" id="levelId" name="reportParamsMap.approvalStage" class="easyui-combotree" data-options="panelHeight: 'auto'"></span></td>
		  <th>项目状态</th>
<%-- 		  ,url:'${pageContext.request.contextPath}/prosearch/projectSearch!getFromCache.action?groupNo=A00008' --%>
		  <td><span class="dateText"><input type="text" id="stateId" name="reportParamsMap.projectStatus" class="easyui-combotree" data-options="panelHeight: 'auto'"></span></td>
	  </tr>
	    <tr>
		  <th>政府投资方向</th>
<%-- 		  url:'${pageContext.request.contextPath}/prosearch/projectSearch!getFromCache.action?groupNo=A00009', --%>
		  <td><span class="dateText"><input id="dicId" type="text" name="reportParamsMap.govermentInvestDirection" class="easyui-combotree" data-options="multiple:true,panelWidth:'auto'"></span></td>
		  <th>是否PPP</th>
		  <td><span class="dateText"><input id="pppId" type="text"  name="reportParamsMap.whetherPPP" class="easyui-combobox" data-options="editable:false,panelHeight: 'auto',valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]"></span></td>
	  </tr>
	  <tr>
	  </tr>
	  </table>
	  </form>
	  <div><button onclick="commit()">搜索</button></div>
	  
	  </div>
		</ul>
	</span>
	<span>
		<a id="editLink" href="javascript:void(0);" title="编辑菜单" class="btn-chart" onclick="ZHFX.toUrl('/menu/menu-manageList')"></a>
	</span>
<!-- 	<span><a href="#" title="导出WORD" class="btn-word"></a></span> -->
<!-- 	<span><a href="#" title="导出PDF" class="btn-pdf"></a></span> -->
<!-- 	<span><a href="#" title="打印" class="btn-print"></a></span> -->
</div>

<script type="text/javascript">

	/**
	 * 返回主页操作
	 */
	function goHomePage() {
		//选中基础业务模块
		if ($('#base').hasClass('active')) {
			ZHFX.toUrl('/common/common!overview');
		//选中重点业务模块
		} else if ($('#zdyw').hasClass('active')) {
			ZHFX.toUrl('/common/common!majorView');
		}
	}

function commit(){
	if($("#startYear1").combobox('getValue') != ""){
		if($("#startMonth1").combobox('getValue') == ""){
			$("#startMonth1").combobox('setValue','1');
		}
	}
	if($("#startYear2").combobox('getValue') != ""){
		if($("#startMonth2").combobox('getValue') == ""){
			$("#startMonth2").combobox('setValue','1');
		}
	}
	if($("#proApplyEndTimeId").datebox('getValue') !="" && $("#proApplyStartTimeId").datebox('getValue')>$("#proApplyEndTimeId").datebox('getValue')){
		alert("项目申报日期开始时间不能大于结束时间");
		return 0;
	}else if($("#yearApplyEndTimeId").datebox('getValue') != "" && $("#yearApplyStartTimeId").datebox('getValue')>$("#yearApplyEndTimeId").datebox('getValue')){
		alert("年度计划申报日期开始时间不能大于结束时间");
		return 0;
	}else if($("#specialEndTimeId").datebox('getValue') != "" && $("#specialStartTimeId").datebox('getValue')>$("#specialEndTimeId").datebox('getValue')){
		alert("专项建设基金申报日期开始时间不能大于结束时间");
		return 0;
	}else if($("#yearChoose2").combobox('getValue') != "" && $("#yearChoose1").combobox('getValue')>$("#yearChoose2").combobox('getValue')){
		alert("竣工或拟建成年份开始时间不能大于结束时间");
		return 0;
	}else if($("#centSizeRId").numberbox('getValue') != "" && $("#centSizeLId").numberbox('getValue')>$("#centSizeRId").numberbox('getValue')){
		alert("中央预算内申请规模最小值不能大于最大值");
		return 0;
	}else if($("#specialReqSizeRId").numberbox('getValue') != "" && $("#specialReqSizeLId").numberbox('getValue')>$("#specialReqSizeRId").numberbox('getValue')){
		alert("专项建设基金需求规模最小值不能大于最大值");
		return 0;
	}else if($("#specialSizeRId").numberbox('getValue') !="" && $("#specialSizeLId").numberbox('getValue')>$("#specialSizeRId").numberbox('getValue')){
		alert("专项建设基金申请规模最小值不能大于最大值");
		return 0;
	}else if($("#centerReqSizeRId").numberbox('getValue') != "" && $("#centerReqSizeLId").numberbox('getValue')>$("#centerReqSizeRId").numberbox('getValue')){
		alert("中央预算内需求规模最小值不能大于最大值");
		return 0;
	}else if($("#startYear2").combobox('getValue') != ""){
			if($("#startYear1").combobox('getValue')>$("#startYear2").combobox('getValue') || ($("#startYear1").combobox('getValue')==$("#startYear2").combobox('getValue') && $("#startMonth1").combobox('getValue')>$("#startMonth2").combobox('getValue'))){
				alert("开工或拟开工开始时间不能大于结束时间");
				return 0;
			}
	}else{
		autoSubmit();
	}
	
}
$(document).ready(function(){
	$(".listMenu h6 a").click(function(){
		$('.listMenu h6 a').removeClass('active');
		$(this).addClass('active');
		$('.listMenu > div').hide();
		var divId = $(this).attr('id');
		$('#'+divId+'-menu').show();
	});
	/*$("#btnSearch").click(function(){
		if($(this).attr("class")=="btn-search"){
			$(this).next().css("transform", "translateX(-100%)");
			$(this).addClass("close");
		} else {
			$(this).next().css("transform", "translateX(100%)");
			$(this).removeClass("close");
		}
	});*/
	//下拉框填充月份
	var data1 = [];//创建月份数组
	var startMonth=1;//起始月份
	var thisMonth=new Date().getUTCMonth()+1;//本月
	//数组添加值（1-12月）为固定值
	for(startMonth;startMonth<13;startMonth++){
	data1.push({label:startMonth,value:startMonth});
	}
	
	$("#startMonth1").combobox({//拟开工月开始时间
		valueField:'value',    
		textField:'label', 
		data:data1
		});
	$("#startMonth2").combobox({//拟开工月结束时间
			valueField:'value',    
			textField:'label',  
			data:data1
			});
	//下拉框填充年份
	var data2 = [];//创建年度数组
	var startYear;//起始年份
	//debugger;
	var thisYear=new Date().getUTCFullYear();//今年
	var endYear=thisYear+50;//结束年份
	//数组添加值（2012-2016）//根据情况自己修改
	for(startYear=endYear-65;startYear<=endYear;startYear++){
		data2.push({label:startYear,value:startYear});
     }
	
	$("#startYear1").combobox({//拟开工年开始时间
		valueField:'value',    
		textField:'label',  
		data:data2
		});
	$("#startYear2").combobox({//拟开工年结束时间
		valueField:'value',    
		textField:'label', 
		data:data2
		});
	$("#yearChoose1").combobox({//竣工或拟建成年份开始时间
		valueField:'value',    
		textField:'label',  
		data:data2
		});
	$("#yearChoose2").combobox({//竣工或拟建成年份结束时间
		valueField:'value',    
		textField:'label', 
		data:data2
		});
});

	//扩展日期格式化 例：new Date().format("yyyy-MM-dd hh:mm:ss")
	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, //month
			"d+" : this.getDate(), //day
			"h+" : this.getHours(), //hour
			"m+" : this.getMinutes(), //minute
			"s+" : this.getSeconds(), //second
			"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
			"S" : this.getMilliseconds()
			//millisecond
		}
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	}

	/**
	 * @requires jQuery,EasyUI
	 *
	 * 扩展datagrid的datebox、datetimebox格式化
	 *
	 */
	 
	$.fn.datebox.defaults.formatter = function(date){
		var vDate = new Date(date);
		return vDate.format('yyyy-MM-dd');
	}
	
    
	$.fn.datebox.defaults.parser = function(s) {
		if (!s)
			return new Date();
		var ss = s.split('-');
		var y = parseInt(ss[0], 10);
		var m = parseInt(ss[1], 10);
		var d = parseInt(ss[2], 10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
			return new Date(y, m - 1, d);
		} else {
			return new Date();
		}
	}
	
	$(function(){
	    $("#toolbar span").hover(function(){
			$(this).find("ul").css("transform","translateX(-100%)");
		},function(){
			//if(!$(".panel").is(":visible")){
				$(this).find("ul").css("transform","translateX(100%)");
			//}
		})
	})

	
	//cjkEncode方法的实现代码，放在网页head中或者用户自己的js文件中
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

	
	$('.dateText').each(function() {
		$(this).dblclick(function() {
			$(this).find('.easyui-datebox').datebox('clear');
		});
		$(this).dblclick(function() {
			$(this).find('.easyui-combotree').combotree('clear');
		});
		$(this).dblclick(function() {
			$(this).find('.easyui-combobox').combobox('clear');
		});
		$(this).dblclick(function() {
			$(this).find('.easyui-numberbox').numberbox('clear');
		});
	});				
</script>
