<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- add css -->
	<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/global.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/base.css" />
	<!-- add js -->
	<script src="${ctx}/book/js/jquery.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/common/extend.js"></script>
</head>
<body>
<!-- 进度条 -->
<div class="loading" style="top:0;">
	<div class="loadbg"></div>
	<span class="loadimg"><img src="${ctx}/book/images/loading.gif"/></span>
</div>
<div>

</div>
	<div class="book_wrap monthReport2">
	    <jsp:include page="./list/homePage.jsp"></jsp:include>
	    <jsp:include page="./list/catalog.jsp"></jsp:include>
		<jsp:include page="./list/briefing.jsp"></jsp:include>
		<jsp:include page="./list/statisticsByIndustry.jsp"></jsp:include>
		<jsp:include page="./list/statisticsByArea.jsp"></jsp:include>
		<jsp:include page="./list/statisticsByProperty.jsp"></jsp:include>		
		<jsp:include page="./list/statisticsByProjectType.jsp"></jsp:include>
		<jsp:include page="./list/statisticsByInvestScale.jsp"></jsp:include>
		<jsp:include page="./list/statisticsByStartTime.jsp"></jsp:include>
	</div>
	
	<script type="text/javascript">
	//传入批复符文
	
	//var guid1 = '201601';
	//var guid2 = '201606';
	var guid1 = '${replaymonth1}';
	var guid2 = '${replaymonth2}';
	
	var yearid=guid2.substring(0,4);
	var monthid=guid2.substring(4);
		
		//日期格式转换（时间戳-->yyyy-MM-dd）
		function formatDate(times){
			if(times){
				var time = new Date(times);
				var y = time.getFullYear();
				var m = time.getMonth()+1;
				var d = time.getDate();
				var h = time.getHours();
				var mm = time.getMinutes();
				var s = time.getSeconds();
				return y+'-'+add0(m)+'-'+add0(d);
			}else{
				return '';
			}
		}
		
		function format(times){
			if(times){
				var time = new Date(times);
				var y = time.getFullYear();
				var m = time.getMonth()+1;
				var d = time.getDate();
				var h = time.getHours();
				var mm = time.getMinutes();
				var s = time.getSeconds();
				return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
			}else{
				return '';
			}
		}
		
		/**
		 * 格式化null
		 */
		function formatNull(date){
			if (date) {
				return date;
			} else {
				return '';
			}
		}
		
		//日期格式转换（时间戳-->yyyy-MM-dd HH:mm:ss）
		function add0(m) {
			//用于生成月、天、时、分、秒位数（一位时在其前添加0，如：2月3号-->02月03号）
			return m<10?'0'+m:m 
		}
		
		$(function(){
			//$("li:contains('项目档案')").addClass("active"); //生成页面右侧工具栏
			// 加载项目档案
			loadData();
		});
		
		/**
		 * 加载数据
		 */
		function loadData() {
			initIndustryInfo(); //加载分行业情况数据
			initAreaInfo();     //加载分地区情况数据
			initProjecttypeInfo();//加载分项目类型情况数据
			initEastAreaInfo();//加载东北三省情况数据
			initPropertyInfo();//加载分产业情况数据
			initInvesttypeInfo();//加载投资类型情况数据
			initInvestScaleInfo();//加载投资规模情况数据
			initStartTimeInfo();//加载开工时间情况数据
			//initCreatWordInfo();
		}
		
		
		/**
		 * 分行业情况统计表
		 */
		function initIndustryInfo(){
			$.ajax({
				type:'POST',
				dataType:'json',
				url:'${ctx}/monthReport/monthReport!getIndustryInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2,
				success:function(data){
					console.log(data);
					var str = '';
					var str2 = '';
					var str3 = '';
/* 					if (data) {
						for (var i = 0; i < data.length; i++) {
							if(i==0){
								str += '<tr style="font-weight:bold;">'
								+  '<td  width="200">'+data[i]["ITEM_VALUE"]+'</td>'
								+  '<td  width="90">'+data[i]["cnt"]+'</td>'
								+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
								+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
								+  '</tr>';
							}
							else{
								str += '<tr>'
									+  '<td  width="90">'+data[i]["ITEM_VALUE"]+'</td>'
									+  '<td  width="90">'+data[i]["cnt"]+'</td>'
									+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
									+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
									+  '</tr>';
								
							}
							
							
						}
					} */
					// 分页模板
					var _pageTemplate=$("#_pageTemplate").html();
					var pageContent="";
					// 每页Html 内容体
					var  pageHtmlContent="";
					if (data) {
				        for(var i = 0,len=data.length-1; i <=len; i++){				        	
				                var tablepage=0;  
				                //第一页
				                if(tablepage==0&&i<=24){
				                        if(i==0|| (data[i]["code"]||"").length!=2){
				                                str += '<tr style="font-weight:bold;">'
				                                +  '<th  width="180" style="font-weight:bold;1text-align:left;">'+data[i]["ITEM_VALUE"]+'</th>'
				                                +  '<td  width="90">'+data[i]["cnt"]+'</td>'
				                                +  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
				                                +  '<td  width="90">'+data[i]["investtotal"]+'</td>'
				                                +  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
				                                +  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
				                                +  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
				                                +  '</tr>';
				                        }
				                        else{
				                                str += '<tr>'
				                                +  '<th  width="180" style="text-indent:12px;">'+data[i]["ITEM_VALUE"]+'</th>'
				                                +  '<td  width="90">'+data[i]["cnt"]+'</td>'
				                                +  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
				                                +  '<td  width="90">'+data[i]["investtotal"]+'</td>'
				                                +  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
				                                +  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
				                                +  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
				                                +  '</tr>';                               
				                        }
				                                
				                        tablepage=+1;
				                }
				                // 其他页
				                else{
				                	// 页面内容
				                	if((data[i]["code"]||"").length!=2){
				                		pageContent += '<tr style="font-weight:bold;">'
		                                +  '<td  width="180" style="font-weight:bold;background-color:#d1d1d1;text-align:left;">'+data[i]["ITEM_VALUE"]+'</td>'
		                                +  '<td  width="90">'+data[i]["cnt"]+'</td>'
		                                +  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
		                                +  '<td  width="90">'+data[i]["investtotal"]+'</td>'
		                                +  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
		                                +  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
		                                +  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
		                                +  '</tr>';
			                        }
			                        else{
			                        	pageContent += '<tr>'
			                                +  '<td  width="180" style="text-indent:12px;background-color:#d1d1d1;font-weight:normal;text-align:left;">'+data[i]["ITEM_VALUE"]+'</td>'
			                                +  '<td  width="90">'+data[i]["cnt"]+'</td>'
			                                +  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
			                                +  '<td  width="90">'+data[i]["investtotal"]+'</td>'
			                                +  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
			                                +  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
			                                +  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
			                                +  '</tr>';                               
			                        }
				                	// 记录是27的整数或者最后一条记录
				                	if((i-24)%26==0||i==len){				                		 			                		
				                		pageHtmlContent=pageHtmlContent+ZHFX.fs(_pageTemplate,tablepage,pageContent);
				                		// 记录分页数	
				                		 tablepage=+1;
				                		// 重新初始化
				                		 pageContent="";
				                	}
				                	
				                }
				               
				        }                                        
				}
					
					str3 += +yearid+"年"+monthid+"月";
					str2 += +yearid+"年"+monthid+"月,全国审批、核准和备案手续项目、计划总投资、项目数量同比增长和计划总投资同比增长分别为"
					+data[i=0]["cnt"]+"个，"+data[i=0]["cntratio"]+"%"+"，"+data[i=0]["investtotal"]+"亿元，"+data[i=0]["inveatratio"]+"%"+"。";
					//动态添加数据行到表格
					$("#statistics_industry_table").append(str);
					// 后面页面
					$("#industry_book_page").after(pageHtmlContent);
					//console.log("yemian "+pageHtmlContent);
					$("#statistics_b1_table").append(str2);
					$("#statistics_time1").append(str3);
					$("#statistics_time9").append(str3);
					$("#statistics_time10").append(str3+"全国固定资产投资审批、核准、备案项目信息月报");
					$("#statistics_time11").append(str3+"30日");
					$(".loading").hide();
				},
				error:function(){
					alert("数据获取失败！","warning");
				}
			});
		}
		
		/**
		 * 分地区情况统计表
		 */
		function initAreaInfo(){
			$.ajax({
				type:'POST',
				dataType:'json',
				url:'${ctx}/monthReport/monthReport!getAreaInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2,
				success:function(data){
					//debugger;
					console.log(data);
					var str = '';
					var str2 = '';
					var str3 = '';
					if (data) {
						for (var i = 0; i < data.length; i++) {
							if(i==0){
								str += '<tr style="font-weight:bold;">'
								+  '<th  width="180" style="font-weight: bold;">'+data[i]["ITEM_VALUE"]+'</th>'
								+  '<td  width="90">'+data[i]["cnt"]+'</td>'
								+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
								+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
								+  '</tr>';
							}
							else{
								if(data[i]["ITEM_KEY"]==undefined){
									str += '<tr style="font-weight:bold;">'
										+  '<th  width="180" style="font-weight: bold;">'+data[i]["ITEM_VALUE"]+'</th>'
										+  '<td  width="90">'+data[i]["cnt"]+'</td>'
										+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
										+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
										+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
										+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
										+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
										+  '</tr>';
								}
									else{
										str += '<tr>'
											+  '<th  width="180" style="text-indent:12px;">'+data[i]["ITEM_VALUE"]+'</th>'
											+  '<td  width="90">'+data[i]["cnt"]+'</td>'
											+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
											+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
											+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
											+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
											+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
											+  '</tr>';
									}
								
								
						}
						}
					
						str2 += "东、中、西部地区"+"项目数量分别为"+1.8+"万个、"+"9811"+"个、"+9459+"个，同比分别增长"+41+"%、"+4+"%、"+44
						+"%。计划投资额分别为"+3.7+"亿、"+1.9+"亿、"+1.3+"亿元，"+"东、中、西部地区"+"同比分别增长"+41+"%、"+4+"%、"+44+"%。";
												
					}
					str3 += +yearid+"年"+monthid+"月";
					//动态添加数据行到表格
					$("#statistics_area_table").append(str);
					$("#statistics_b5_table").append(str2);
					$("#statistics_time2").append(str3);
				},
				error:function(){
					alert("数据获取失败！","warning");
				}
			});
		}
		
		/**
		 * 东北三省情况统计表
		 */
		function initEastAreaInfo(){
			$.ajax({
				type:'POST',
				dataType:'json',
				url:'${ctx}/monthReport/monthReport!getEastAreaInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2,
				success:function(data){
					console.log(data);
					var str = '';
					var str2 = '';
					var str3 = '';
					if (data) {
						//debugger;
						for (var i = 0; i < data.length; i++) {
							if(i==0){
							str += '<tr style="font-weight:bold;">'
								+  '<th  width="180" style="font-weight: bold;">'+data[i]["ITEM_VALUE"]+'</th>'
								+  '<td  width="90">'+data[i]["cnt"]+'</td>'
								+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
								+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
								+  '</tr>';
							}
							else{
								str += '<tr>'
									+  '<th  width="180" style="text-indent:12px;">'+data[i]["ITEM_VALUE"]+'</th>'
									+  '<td  width="90">'+data[i]["cnt"]+'</td>'
									+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
									+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
									+  '</tr>';
								
							}
					
					      }
					}
/* 					str2 += '<tr>'
						+  '<td  width="90">'+data[i=1]["cnt"]+'</td>'
						+  '<td  width="90">'+data[i=1]["cntratio"]+"%"+'</td>'
						+  '<td  width="90">'+data[i=1]["investtotal"]+'</td>'
						+  '<td  width="90">'+data[i=1]["inveatratio"]+"%"+'</td>'
						+  '<td  width="90">'+data[i=1]["cntrate"]+"%"+'</td>'
						+  '<td  width="90">'+data[i=1]["investrate"]+"%"+'</td>'
						+  '</tr>'; */
					str2 +="东北地区项目数量、计划投资额、项目个数同比增长、计划投资额同比增长分别为"
					+data[i=0]["cnt"]+"个，"+data[i=0]["cntratio"]+"%"+"，"+data[i=0]["investtotal"]+"亿元，"+data[i=0]["inveatratio"]+"%"+"。";
						
					str3 += +yearid+"年"+monthid+"月";
					//动态添加数据行到表格
					$("#statistics_eastarea_table").append(str);
					$("#statistics_b6_table").append(str2);
					$("#statistics_time3").append(str3);
				},
				error:function(){
					alert("数据获取失败！","warning");
				}
			});
		}
		
		/**
		 * 分产业情况统计表
		 */
		function initPropertyInfo(){
			$.ajax({
				type:'POST',
				dataType:'json',
				url:'${ctx}/monthReport/monthReport!getPropertyInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2,
				success:function(data){
					//debugger;
					console.log(data);
					var str = '';
					var str2 = '';
					var str3 = '';
					if (data) {
						for (var i = 0; i < data.length; i++) {
							if(i==0){
								str += '<tr style="font-weight:bold;">'
								+  '<th  width="180" style="font-weight: bold;">'+data[i]["CY"]+'</th>'
								+  '<td  width="90">'+data[i]["cnt"]+'</td>'
								+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
								+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
								+  '</tr>';
							}
							else{
								str += '<tr>'
									+  '<th  width="180" style="text-indent:12px;">'+data[i]["CY"]+'</th>'
									+  '<td  width="90">'+data[i]["cnt"]+'</td>'
									+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
									+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
									+  '</tr>';
								
							}
															
						}
						str2 += data[i=0]["cnt"]+"个项目中，"+data[i=1]["CY"]+data[i=1]["cnt"]+"个，同比增长"+data[i=1]["cntratio"]+"%，计划投资额"+data[i=1]["investtotal"]+"亿元，同比增加"+data[i=1]["inveatratio"]+"%；"
						     + data[i=2]["CY"]+data[i=2]["cnt"]+"个，同比增长"+data[i=2]["cntratio"]+"%"+"，计划投资额"+data[i=2]["investtotal"]+"亿元，同比增加"+data[i=2]["inveatratio"]+"%；"
						     + data[i=3]["CY"]+data[i=3]["cnt"]+"个，同比增长"+data[i=3]["cntratio"]+"%"+"，计划投资额"+data[i=3]["investtotal"]+"亿元，同比增加"+data[i=3]["inveatratio"]+"%。";
												
					}
					str3 += +yearid+"年"+monthid+"月";
					//动态添加数据行到表格
					$("#statistics_property_table").append(str);
					$("#statistics_b4_table").append(str2);
					$("#statistics_time4").append(str3);
				},
				error:function(){
					alert("数据获取失败！","warning");
				}
			});
		}
		
		/**
		 * 分项目类型统计表
		 */
		function initProjecttypeInfo(){
			$.ajax({
				type:'POST',
				dataType:'json',
				url:'${ctx}/monthReport/monthReport!getProjectTypeInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2,
				success:function(data){
					//debugger;
					console.log(data);
					var str = '';
					var str2 = '';
					var str3 = '';
					if (data) {
						for (var i = 0; i < data.length; i++) {
							if(i==0){
								str += '<tr style="font-weight:bold;">'
								+  '<th  width="180" style="font-weight: bold;">'+data[i]["ITEM_VALUE"]+'</th>'
								+  '<td  width="90">'+data[i]["cnt"]+'</td>'
								+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
								+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
								+  '</tr>';
							}
							else{
								if((data[i]["ITEM_VALUE"]=="审批")||(data[i]["ITEM_VALUE"]=="核准")||(data[i]["ITEM_VALUE"]=="备案")){
								str += '<tr style="font-weight:bold;">'
									+  '<th  width="180" style="font-weight: bold;">'+data[i]["ITEM_VALUE"]+'</th>'
									+  '<td  width="90">'+data[i]["cnt"]+'</td>'
									+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
									+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
									+  '</tr>';
								
							}
								else{
									str += '<tr>'
										+  '<th  width="180" style="text-indent:12px;">'+data[i]["ITEM_VALUE"]+'</th>'
										+  '<td  width="90">'+data[i]["cnt"]+'</td>'
										+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
										+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
										+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
										+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
										+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
										+  '</tr>';
								}
							}
						}
						str2 += "全国各地审批项目"+data[i=1]["cnt"]+"个，计划投资额"+data[i=1]["investtotal"]+"亿元，项目个数同比增加"+data[i=1]["cntratio"]+"%，计划投资额同比增加"
						+data[i=1]["inveatratio"]+"%；核准项目"+data[i=6]["cnt"]+"个，计划投资额"+data[i=6]["investtotal"]+"亿元，同比分别增加"+data[i=6]["cntratio"]+"%和"
						+data[i=6]["inveatratio"]+"%；备案项目"+data[i=7]["cnt"]+"个，计划投资额"+data[i=7]["investtotal"]+"亿元，项目个数同比增加"+data[i=7]["cntratio"]+"%，计划投资额同比增加"+data[i=7]["inveatratio"]+"%。";
									
					}
					str3 += +yearid+"年"+monthid+"月";

					//动态添加数据行到表格
					$("#statistics_projecttype_table").append(str);	
					$("#statistics_b3_table").append(str2);
					$("#statistics_time5").append(str3);
				},
				error:function(){
					alert("数据获取失败！","warning");
				}
			});
		}
		
		/**
		 * 分投资类型统计表
		 */
		function initInvesttypeInfo(){
			$.ajax({
				type:'POST',
				dataType:'json',
				url:'${ctx}/monthReport/monthReport!getInvestTypeInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2,
				success:function(data){
					console.log(data);
					var str = '';
					var str2 = '';
					var str3 = '';
					if (data) {
						for (var i = 0; i < data.length; i++) {
							if(i==0){
								str += '<tr style="font-weight:bold;">'
								+  '<th  width="180" style="font-weight: bold;">'+data[i]["TZLX"]+'</th>'
								+  '<td  width="90">'+data[i]["cnt"]+'</td>'
								+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
								+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
								+  '</tr>';
							}
							else{
								str += '<tr>'
									+  '<th  width="180" style="text-indent:12px;">'+data[i]["TZLX"]+'</th>'
									+  '<td  width="90">'+data[i]["cnt"]+'</td>'
									+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
									+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
									+  '</tr>';
								
							}
						}
						str2 += data[i=1]["TZLX"]+"计划投资额同比增长"+data[i=1]["inveatratio"]+"%"+","
						+data[i=2]["TZLX"]+"计划投资额同比增长"+data[i=2]["inveatratio"]+"%"+".";
					}

					str3 += +yearid+"年"+monthid+"月";
					//动态添加数据行到表格
					$("#statistics_investtype_table").append(str);
					$("#statistics_b2_table").append(str2);
					$("#statistics_time6").append(str3);
				},
				error:function(){
					alert("数据获取失败！","warning");
				}
			});
		}
		
		/**
		 * 分投资规模情况统计表
		 */
		function initInvestScaleInfo(){
			$.ajax({
				type:'POST',
				dataType:'json',
				url:'${ctx}/monthReport/monthReport!getInvestScaleInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2,
				success:function(data){
					console.log(data);
					var str = '';
					var str2 = '';
					if (data) {
						for (var i = 0; i < data.length; i++) {
							if(i==0){
								str += '<tr style="font-weight:bold;">'
								+  '<th  width="180" style="font-weight: bold;">'+data[i]["TZGM"]+'</th>'
								+  '<td  width="90">'+data[i]["cnt"]+'</td>'
								+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
								+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
								+  '</tr>';
							}
							else{
								str += '<tr>'
									+  '<th  width="180" style="text-indent:12px;">'+data[i]["TZGM"]+'</th>'
									+  '<td  width="90">'+data[i]["cnt"]+'</td>'
									+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
									+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
									+  '</tr>';
								
							}
						}
					}
					str2 += +yearid+"年"+monthid+"月";
					//动态添加数据行到表格
					$("#statistics_investscale_table").append(str);
					$("#statistics_time7").append(str2);
				},
				error:function(){
					alert("数据获取失败！","warning");
				}
			});
		}
		
		/**
		 * 分预计开工时间统计表
		 */
		function initStartTimeInfo(){
			$.ajax({
				type:'POST',
				dataType:'json',
				url:'${ctx}/monthReport/monthReport!getStartTimeInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2,
				success:function(data){
					console.log(data);
					var str = '';
					var str2 = '';
					if (data) {
						for (var i = 0; i < data.length; i++) {
							if(i==0){
								str += '<tr style="font-weight:bold;">'
								+  '<th  width="180" style="font-weight: bold;">'+data[i]["X11"]+'</th>'
								+  '<td  width="90">'+data[i]["cnt"]+'</td>'
								+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
								+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
								+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
								+  '</tr>';
							}
							else{
								str += '<tr>'
									+  '<th  width="180" style="text-indent:12px;">'+data[i]["X11"]+'</th>'
									+  '<td  width="90">'+data[i]["cnt"]+'</td>'
									+  '<td  width="90">'+data[i]["cntratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investtotal"]+'</td>'
									+  '<td  width="90">'+data[i]["inveatratio"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["cntrate"]+"%"+'</td>'
									+  '<td  width="90">'+data[i]["investrate"]+"%"+'</td>'
									+  '</tr>';
								
							}
						}
					}
					str2 += +yearid+"年"+monthid+"月";
					//动态添加数据行到表格
					$("#statistics_starttime_table").append(str);
					$("#statistics_time8").append(str2);
				},
				error:function(){
					alert("数据获取失败！","warning");
				}
			});
		}
		

		$(".book_word_back").click(function (){
			window.location='${ctx}/monthReport/monthReport!getWordInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2;			
// 			$.ajax({
// 				type:'POST',
// 				dataType:'json',
// 				url:'${ctx}/monthReport/monthReport!getWordInfo.action?replaymonth1='+guid1+'&replaymonth2='+guid2,
// 				success:function(data){
// 					if(data="1"){
// 						alert("生成Word文档成功！","warning")
// 					}	
// 				},
// 				error:function(){
// 					alert("数据获取失败！","warning");
// 				}
// 			});
		});
		
	</script>
	
</body>
</html>