<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%--五年规划储备项目的查询条件--%>
<script type="text/html"  id="fivePlanTemplate">
	<table id ="fivePlan" class="listSearchdiv">
		<tr>
			<th>入库日期</th>
			<td colspan="3"><span class="dateText"><input id="storeTimeFrom" name="reportParamsMap.storeTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="storeTimeTo" name="reportParamsMap.storeTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		</tr>
		<tr>
			<th>拟开工年份</th>
			<td colspan="3"><span class="dateText"><input id="startTimeFrom" name="reportParamsMap.startTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="starTimeTo" name="reportParamsMap.starTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		<tr>
			<th>拟建成年份</th>
			<td colspan="3"><span class="dateText"><input id="endTimeFrom" name="reportParamsMap.endTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="endTimeTo" name="reportParamsMap.endTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		<tr>
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" data-options="editable:true"></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox" data-options="editable:true"></span></td>
		</tr>
		<tr>
			<input id="buildPlace1" type="hidden" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
			<input id="industry1" type="hidden" name="reportParamsMap.industry">
			<th>所属行业</th>
			<td><span class="dateText"><input type="text" id="industry" name="reportParamsMap.industry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input id="gbIndustry1" type="hidden" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td><span class="dateText"><input type="text" id="gbIndustry" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="panelHeight: 'auto',url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
			<th>是否专项建设基金</th>
			<td><span class="dateText"><input id="isFunds" type="text" name="reportParamsMap.isFunds" class="easyui-combobox" data-options="editable:false,panelHeight: 'auto',valueField: 'value',textField: 'label',data: [{label: '全部',value: ''},{label: '是',value: '1'},{label: '否',value: '0'}],value:''"></span></td>
		</tr>
		<tr>
			<input id="govInvest1" type="hidden" name="reportParamsMap.govInvest">
			<th>政府投资方向</th>
			<td><span class="dateText"><input id="govInvest" type="text" name="reportParamsMap.govInvest" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GovernmentCode',multiple:true,panelWidth:'auto'"></span></td>
			<th>是否PPP</th>
			<td><span class="dateText"><input id="isPPP" type="text"  name="reportParamsMap.isPPP" class="easyui-combobox" data-options="editable:false,panelHeight: 'auto',valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]"></span></td>
		</tr>
	</table>
</script>
<%--三年滚动计划--%>
<script type="text/html"  id="rollPlanTemplate">
	<table id ="rollPlan" class="listSearchdiv">
		<tr>
			<th>入库日期</th>
			<td colspan="3"><span class="dateText"><input id="storeTimeFrom" name="reportParamsMap.storeTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="storeTimeTo" name="reportParamsMap.storeTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		</tr>
		<tr>
			<th>拟开工年份</th>
			<td colspan="3"><span class="dateText"><input id="startTimeFrom" name="reportParamsMap.startTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="starTimeTo" name="reportParamsMap.starTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		<tr>
			<th>拟建成年份</th>
			<td colspan="3"><span class="dateText"><input id="endTimeFrom" name="reportParamsMap.endTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="endTimeTo" name="reportParamsMap.endTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		<tr>
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox" ></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
			<input type="hidden" id="industry1" name="reportParamsMap.industry">
			<th>所属行业</th>
			<td><span class="dateText"><input type="text" id="industry" name="reportParamsMap.industry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<th>是否PPP</th>
			<td><span class="dateText"><input id="isPPP" type="text"  name="reportParamsMap.isPPP" class="easyui-combobox" data-options="editable:false,panelHeight: 'auto',valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]"></span></td>
			<th>是否专项建设基金</th>
			<td><span class="dateText"><input id="isFunds" type="text" name="reportParamsMap.isFunds" class="easyui-combobox" data-options="editable:false,panelHeight: 'auto',valueField: 'value',textField: 'label',data: [{label: '全部',value: ''},{label: '是',value: '1'},{label: '否',value: '0'}],value:''"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="govInvest1" name="reportParamsMap.govInvest">
			<th>政府投资方向</th>
			<td><span class="dateText"><input id="govInvest" type="text" name="reportParamsMap.govInvest" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GovernmentCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
	</table>
</script>
<%--中央预算内申报--%>
<script type="text/html"  id="yearReportTemplate">
	<table id ="yearReport" class="listSearchdiv">
		<tr>
			<th>申报日期</th>
			<td colspan="3"><span class="dateText"><input id="createTimeFrom" name="reportParamsMap.createTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="createTimeTo" name="reportParamsMap.createTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		</tr>
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox" ></span></td>
		</tr>
		<tr>
			<th>中央预算内(2017)申请资金</th>
			<td colspan="3"><span class="dateText"><input id="budTotalFrom" name="reportParamsMap.budTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="budTotalTo" name="reportParamsMap.budTotalTo" type="text" class="easyui-numberbox" ></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
			<input type="hidden" id="gbIndustry1" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td colspan="3"><span class="dateText"><input id="gbIndustry" type="text" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="govInvest1" name="reportParamsMap.govInvest">
			<th>政府投资方向</th>
			<td><span class="dateText"><input id="govInvest" type="text" name="reportParamsMap.govInvest" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GovernmentCode',multiple:true,panelWidth:'auto'"></span></td>
			<th>是否PPP</th>
			<td><span class="dateText"><input id="isPPP" type="text"  name="reportParamsMap.isPPP" class="easyui-combobox" data-options="editable:false,panelHeight: 'auto',valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]"></span></td>
		</tr>
	</table>
</script>
<%--中央预算内下达--%>
<script type="text/html"  id="yearIssuedTemplate">
	<table id ="yearIssued" class="listSearchdiv">
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox" ></span></td>
		</tr>
		<tr>
			<th>中央预算内下达资金</th>
			<td colspan="3"><span class="dateText"><input id="IssuedTotalFrom" name="reportParamsMap.IssuedTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="IssuedTotalTo" name="reportParamsMap.IssuedTotalTo" type="text" class="easyui-numberbox" ></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
			<input type="hidden" id="gbIndustry1" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td colspan="3"><span class="dateText"><input id="gbIndustry" type="text" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="govInvest1" name="reportParamsMap.govInvest">
			<th>政府投资方向</th>
			<td><span class="dateText"><input id="govInvest" type="text" name="reportParamsMap.govInvest" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GovernmentCode',multiple:true,panelWidth:'auto'"></span></td>
			<th>是否PPP</th>
			<td><span class="dateText"><input id="isPPP" type="text"  name="reportParamsMap.isPPP" class="easyui-combobox" data-options="editable:false,panelHeight: 'auto',valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]"></span></td>
		</tr>
	</table>
</script>
<%--中央预算内调度--%>
<script type="text/html"  id="yearDispatchTemplate">
	<table id ="yearDispatch" class="listSearchdiv">
		<tr>
			<th>拟开工年份</th>
			<td colspan="3"><span class="dateText"><input id="startTimeFrom" name="reportParamsMap.startTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="starTimeTo" name="reportParamsMap.starTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		<tr>
			<th>拟建成年份</th>
			<td colspan="3"><span class="dateText"><input id="endTimeFrom" name="reportParamsMap.endTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="endTimeTo" name="reportParamsMap.endTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		<tr>
		<tr>
			<th>实际开工时间</th>
			<td colspan="3"><span class="dateText"><input id="actualStartTimeFrom" name="reportParamsMap.actualStartTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="actualStarTimeTo" name="reportParamsMap.actualStarTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		<tr>
			<th>实际竣工时间</th>
			<td colspan="3"><span class="dateText"><input id="actualEndTimeFrom" name="reportParamsMap.actualEndTimeFrom" type="text" class="easyui-datebox" data-options="editable:false"></span> - <span class="dateText"><input id="actualEndTimeTo" name="reportParamsMap.actualEndTimeTo" type="text"  class="easyui-datebox" data-options="editable:false"></span></td>
		<tr>
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox" ></span></td>
		</tr>
		<tr>
			<th>中央预算内到位资金</th>
			<td colspan="3"><span class="dateText"><input id="finishTotalFrom" name="reportParamsMap.finishTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="finishTotalTo" name="reportParamsMap.finishTotalTo" type="text" class="easyui-numberbox" ></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
			<input type="hidden" id="gbIndustry1" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td colspan="3"><span class="dateText"><input id="gbIndustry" type="text" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<th>是否PPP</th>
			<td><span class="dateText"><input id="isPPP" type="text"  name="reportParamsMap.isPPP" class="easyui-combobox" data-options="editable:false,panelHeight: 'auto',valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]"></span></td>
			<input type="hidden" id="govInvest1" name="reportParamsMap.govInvest">
			<th>政府投资方向</th>
			<td><span class="dateText"><input id="govInvest" type="text" name="reportParamsMap.govInvest" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GovernmentCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
	</table>
</script>
<%--专项建设基金--%>
<script type="text/html"  id="fundTemplate">
	<table id ="fund" class="listSearchdiv">
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox"></span></td>
		</tr>
		<tr>
			<th>专项建设基金投放资金</th>
			<td colspan="3"><span class="dateText"><input id="putInCaptialFrom" name="reportParamsMap.putInCaptialFrom" type="text"  class="easyui-numberbox"></span> - <span class="dateText"><input id="putInCaptialTo" name="reportParamsMap.putInCaptialTo" type="text" class="easyui-numberbox"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="industry1" name="reportParamsMap.industry">
			<th>所属行业</th>
			<td colspan="3"><span class="dateText"><input type="text" id="industry" name="reportParamsMap.industry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="gbIndustry1" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td colspan="3"><span class="dateText"><input id="gbIndustry" type="text" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
	</table>
</script>

<%--项目监测的情况--%>
<script type="text/html"  id="plan_stageTemplate">
	<table id ="plan_stage" class="listSearchdiv">
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="proType1" name="reportParamsMap.proType">
			<th>项目类型</th>
			<td colspan="3"><span class="dateText"><input type="text" id="proType" name="reportParamsMap.proType" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=ProjectTypeCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="industry1" name="reportParamsMap.industry">
			<th>所属行业</th>
			<td colspan="3"><span class="dateText"><input type="text" id="industry" name="reportParamsMap.industry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryExtendCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="gbIndustry1" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td colspan="3"><span class="dateText"><input id="gbIndustry" type="text" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
	</table>
</script>
<script type="text/html"  id="build_stageTemplate">
	<table id ="build_stage" class="listSearchdiv">
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="proType1" name="reportParamsMap.proType">
			<th>项目类型</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.proType" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=ProjectTypeCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="industry1" name="reportParamsMap.industry">
			<th>所属行业</th>
			<td colspan="3"><span class="dateText"><input type="text" id="industry" name="reportParamsMap.industry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryExtendCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="gbIndustry1" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td colspan="3"><span class="dateText"><input id="gbIndustry" type="text" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
	</table>
</script>
<script type="text/html"  id="decision_stageTemplate">
	<table id ="decision_stage" class="listSearchdiv">
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="proType1" name="reportParamsMap.proType">
			<th>项目类型</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.proType" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=ProjectTypeCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="industry1" name="reportParamsMap.industry">
			<th>所属行业</th>
			<td colspan="3"><span class="dateText"><input type="text" id="industry" name="reportParamsMap.industry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryExtendCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="gbIndustry1" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td colspan="3"><span class="dateText"><input id="gbIndustry" type="text" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
	</table>
</script>
<script type="text/html"  id="start_stageTemplate">
	<table id ="start_stage" class="listSearchdiv">
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="proType1" name="reportParamsMap.proType">
			<th>项目类型</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.proType" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=ProjectTypeCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="industry1" name="reportParamsMap.industry">
			<th>所属行业</th>
			<td colspan="3"><span class="dateText"><input type="text" id="industry" name="reportParamsMap.industry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryExtendCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="gbIndustry1" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td colspan="3"><span class="dateText"><input id="gbIndustry" type="text" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
	</table>
</script>
<script type="text/html"  id="finish_stageTemplate">
	<table id ="finish_stage" class="listSearchdiv">
		<tr>
			<th>项目总投资</th>
			<td colspan="3"><span class="dateText"><input id="invTotalFrom" name="reportParamsMap.invTotalFrom" type="text"  class="easyui-numberbox" ></span> - <span class="dateText"><input id="invTotalTo" name="reportParamsMap.invTotalTo" type="text" class="easyui-numberbox"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="proType1" name="reportParamsMap.proType">
			<th>项目类型</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.proType" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=ProjectTypeCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="buildPlace1" name="reportParamsMap.buildPlace">
			<th>建设地点</th>
			<td colspan="3"><span class="dateText"><input type="text" id="buildPlace" name="reportParamsMap.buildPlace" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=BuildPlaceCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="industry1" name="reportParamsMap.industry">
			<th>所属行业</th>
			<td colspan="3"><span class="dateText"><input type="text" id="industry" name="reportParamsMap.industry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}${ctx}/projectFile/projectFile!getFromCache.action?propertiesKey=IndustryExtendCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
		<tr>
			<input type="hidden" id="gbIndustry1" name="reportParamsMap.gbIndustry">
			<th>国标行业</th>
			<td colspan="3"><span class="dateText"><input id="gbIndustry" type="text" name="reportParamsMap.gbIndustry" class="easyui-combotree" data-options="url:'${pageContext.request.contextPath}/projectFile/projectFile!getFromCache.action?propertiesKey=GBIndustryCode',multiple:true,panelWidth:'auto'"></span></td>
		</tr>
	</table>
</script>