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
    <title>审核备项目月报</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/global.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/base.css" />
    <script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/base.js"></script>
    <script type="text/javascript" src="${ctx}/js/prefixfree.min.js"></script>
    <script type="text/javascript" src="${ctx}/common/extend.js"></script>
    <style>
    </style>
</head>
<body>
<div >
    <div class="search_area" style="margin:0 20px;">
    	<table>
    		<tr>
    			<th width="100">报告名称</th>
    			<td>
    				<span class="textbox numberbox" style="height:22px;">
    					<input type="text" class="textbox-text validatebox-text textbox-prompt" autocomplete="off" placeholder="" style="margin-left: 0px; margin-right: 0px; padding-top: 2px; padding-bottom: 2px; width: 139px;">
    					<input type="hidden" class="textbox-value" value="">
    				</span>
    			</td>
    			<td>
					<input type="button" class="searchBtn" id="search_btn" value="查询">
				</td>
    		</tr>
    	</table>
    </div>
    <div class='bookcase'>
	    <ul class="chartLi">
	    	<li>
	            <div class="chartLi_month" onclick="showDialog(201601,201601)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年1月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201601,201602)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年2月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201601,201603)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年3月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201601,201604)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年4月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201601,201605)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年5月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201601,201606)">             
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年6月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201601,201607)">             
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年7月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201601,201608)">             
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年8月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201601,201609)">             
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年9月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201601,201610)">             
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2016年10月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
			<li>
				<div class="chartLi_month" onclick="showDialog(201601,201611)">
					<span class="jbg topLeft"></span>
					<span class="jbg topRight"></span>
					<span class="jbg bottomLeft"></span>
					<span class="jbg bottomRight"></span>
					<h6>2016年11月</h6>
					<!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
				</div>
			</li>
			<li>
				<div class="chartLi_month" onclick="showDialog(201601,201612)">
					<span class="jbg topLeft"></span>
					<span class="jbg topRight"></span>
					<span class="jbg bottomLeft"></span>
					<span class="jbg bottomRight"></span>
					<h6>2016年12月</h6>
					<!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
				</div>
			</li>
	    </ul>
	</div>
    <div class="bookcase_title">往期报告</div>
    <div class='bookcase'>
	    <ul class="chartLi">
	    	<li>
	            <div class="chartLi_month" onclick="showDialog(201501,201501)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年1月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201502)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年2月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201503)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年3月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201504)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年4月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201505)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年5月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201506)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年6月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201507)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年7月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201508)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年8月 </h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201509)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年9月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201510)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年10月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201511)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年11月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	        <li>
	            <div class="chartLi_month" onclick="showDialog(201501,201512)">
	                <span class="jbg topLeft"></span>
	                <span class="jbg topRight"></span>
	                <span class="jbg bottomLeft"></span>
	                <span class="jbg bottomRight"></span>
	                <h6>2015年12月</h6>
	                <!-- <i>全国固定资产投资审批、核准、备案项目信息</i> -->
	            </div>
	        </li>
	    </ul>
	</div>
</div>
<script type="text/javascript" src="${ctx}/js/draggabilly.pkgd.js"></script>
<script type="text/javascript">
    $(function(){
        $('.chartDiv').draggabilly({ handle: '.chartDivTitle' });
        $('.selectTree').draggabilly({ handle: '.selectTree-Title' });
        $("#base-menu").hide();
        $("#zdyw-menu").show();
        $("#base").removeClass("active");
        $("#zdyw").addClass("active");
            $("li:contains('审核备项目月报')").addClass("active");
            //String replayMonth = (String)session.getAttribute("replayMonth");
    		//request.setAttribute("201606",replayMonth);
    });
    
    
  	 function showDialog(replaymonth1,replaymonth2){
		 	ZHFX.toUrl("/monthReport/monthReport!details?replaymonth1="+replaymonth1+"&replaymonth2="+replaymonth2);//传入当前月份ID
		}  
</script>
</body>
</html>