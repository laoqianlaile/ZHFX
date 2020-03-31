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
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/report.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/prefixfree.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.transit.min.js"></script>
 <script type="text/javascript" src="${ctx}/common/extend.js"></script>
<script type="text/javascript" src="${ctx}/js/echarts/echarts.js"></script>
<script type="text/javascript" >
//扩展日期格式化 例：new Date().format("yyyy-MM-dd hh:mm:ss")
    $(window).load(function(){     
            $("#zdyw-menu").show();
            $("#zdyw").addClass("active");
            $("#base").removeClass("active");
            $("#base-menu").hide();
            $("li:contains('投资形势分析')").addClass("active");
      
    });
	var flags = '${flags}';
	var type = '${type}';
	$(function(){
		//获取图表数据
		getChartData();
		$.ajax({
			type:"POST",
			typeDate:"json",
			url:"${ctx}/statis/statis!getDataList?flags="+flags,
			error: function () {//请求失败处理函数 
	        	$.dialog.alert("数据获取失败！","warning");
	        }, 
	        success:function(data){
				if(data){
					var row;
					var col;
					var title;
					var arr = [];
				    for(var item in data[1]){
				        arr.push(data[1][item]);
				    }
					col += '<tr>';
					col += '<th><div style="width:250px;">指标</div></th>'
					//col += '<th><div style="width:90px;">标志</div></th>'
						//col += '<th><div style="width:280px;">名称</div></th>'
					for(var i=0;i<data.length;i++){
						row += '<tr>';
						row += '<td>'+data[i]["CNAMEZB"]+'</td>';
						//row += '<td>'+data[i]["FLAG"]+'</td>';
						//row += '<td>'+data[i]["NAME"]+'</td>';
						for (var j = 1; j < arr.length-2; j++) {
								row += '<td>'+data[i]["SJ" + (arr.length-3-j)]+'</td>';
							}
						row += '</tr>';
					}
					for(var i=0;i<arr.length-3;i++){
						var myDate= new Date();
						myDate.setMonth(myDate.getMonth()-i-1);
						col +='<th><div style="width:90px;">'+ myDate.format("yyyy年MM月")+'</div></th>';
					}
					col += '</tr>';
					title = data[0]["NAME"];
					$("#table_thead").append(col);
					$("#table_data").append(row);
					$("#dataTitle").append('国家统计局'+ title);

					var divWidth = $(window).width();
					var divHeight =$(window).height();
					//$("#_fixTable").css("width",divWidth+"px");
					//$("#_fixTable").css("height",divHeight+"px");
					var tableHeadHtml = $("#tableContent").find("thead").html();
					var tableThWidth = $("#_fixTable").width()-10;
					var tableHeight = $("#_fixTable").height()-10;
					$("#_fixTableHeader").css("width",tableThWidth + "px");
					$("#_fixTableColumn").css("height",tableHeight + "px");
					$("#_fixTableHeader thead").append(tableHeadHtml);
					$("#_fixTableColumnHeader thead").append(tableHeadHtml);
					$("#_fixTableColumnBody table").append($("#tableContent").html());
					$("#_fixTableMain").scroll(function(e) {
				        $("#_fixTableHeader").scrollLeft($(this).scrollLeft());
						$("#_fixTableColumnBody").scrollTop($(this).scrollTop());
				    });
				}
			}
		});
	});
	
	/**
	 * 获取图表数据
	 */
	function getChartData() {
		//发起请求
		$.ajax({
			url : '${ctx}/statis/statis!getChartData.action?flags='+flags,
			dataType : 'json',
			success : function (data) {
				//存在对象
				if (data) {
					//画柱状图
					drawBar(data);
				} else {
					//alert('无');
				}
			},
			error : function () {
				
			}
		});
	}
	
	/**
	 * 画柱状图
	 */
	function drawBar(data) {
		//指标数组
		var legend = [];
		//时间数组
		var xAxis = [];
		//金额数组
		var yAxis = [];
		//遍历json对象
		for (var key in data) {
			//若为指标轴
			if (key == 'legend') {
				legend = data[key];
			//若为时间轴
			} else if (key == 'xAxis') {
				xAxis = data[key];
			//若为金额轴
			} else if (key == 'yAxis') {
				yAxis = data[key];
			}
			
		}
		
		//定义数据数组
		var series = [];
		//遍历指标数组
		for (var i = 0; i < legend.length; i++) {
			//将元素放入数组中
			series[i] = {
					"name" : legend[i], //名称
					"type" : "bar", //类型为柱状图
                    "data" : yAxis[i], //金额
                    "y2" : 100
			};
		}
		
		 // 路径配置
        require.config({
            paths: {
                echarts: '${ctx}/js/echarts'
            }
        });

        // 使用
        require(
            [
                'echarts',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('showChart')); 

                var option = {
                    tooltip: {
                        show: true
                    },
                    legend: {
                        data: legend,
                        y:'bottom',
                        textStyle:{
                    		color:'#0df2ff'
                    	}
                    },
                    grid: {
                    	borderColor : '#344151',
                    	y:20,
                    	x2:30,
                    	y2:80
                    },
                    xAxis : [
                        {
                            type : 'category',
                            data : xAxis,
                            axisLine:{
                            	lineStyle:{
                            		color:'#72839f'
                            	}
                            },
                            axisLabel: {
                            	textStyle:{
                            		color:'#91C2F7'
                            	}
                            },
                            splitLine:{
                            	lineStyle:{
                            		color:'#344151'
                            	}
                            }
                        }
                    ],
                    yAxis : [
                        {
                            type : 'value',
                            axisLine:{
                            	lineStyle:{
                            		color:'#72839f'
                            	}
                            },
                            axisLabel: {
                            	textStyle:{
                            		color:'#91C2F7'
                            	}
                            },
                            splitLine:{
                            	lineStyle:{
                            		color:'#344151'
                            	}
                            }
                        }
                    ],
                    series : series
                };

                // 为echarts对象加载数据 
                myChart.setOption(option); 
            }
        );
	}
	
</script>
<style>
</style>
</head>
<body>
<div id="logo"></div>
<%--引入公用文件--%>
<jsp:include page="./../common/toolbar.jsp"></jsp:include>
<div id="contentDiv">
    <span class="loginInfo"><img src="${ctx}/themes/images/user.png">${SYS_USER_INFO}<a href="javascript:ZHFX.logout()">【退出】</a></span>
    <span class="currenttWz">当前位置：重点业务 > 投资形势分析 >${title}</span>
	<div class="chartDiv" style="right:50%;left:20px;">
		<span class="jbg topLeft"></span>
		<span class="jbg topRight"></span>
		<span class="jbg bottomLeft"></span>
		<span class="jbg bottomRight"></span>
		<h6 class="chartDivTitle" id="dataTitle"></h6>	
		<div class="chartHeight noIframe" style="padding-left:5px;">
			<div id="showChart" style="height:400px;width:100%;"></div>
			<br/>
			<div id="_fixTable" class="tableDiv" style="height:400px;width:100%;margin:0px auto;">
				<div id="_fixTableMain" class="tableBody">
					<table border="0" cellpadding="0" cellspacing="0" id="tableContent" class="x-table report-table">
						<thead id="table_thead">
						</thead>
						<tbody id="table_data">
							
						</tbody>
					</table>
				</div>
				<div id="_fixTableHeader" class="tableH">
					<table border="0" cellpadding="0" cellspacing="0" class="x-table report-table">
						<thead>
						</thead>
					</table>
				</div>
				<div id="_fixTableColumn" class="tableContentTh" style="width:252px;">
					<div id="_fixTableColumnHeader" style="width: 100%;overflow: hidden; position: absolute; z-index: 999999;">
						<table class="x-table report-table">
							<thead>
							</thead>
				    	</table>
			    	</div>
			    	<div id="_fixTableColumnBody" style="width: 100%; height: 100%; overflow: hidden; position: absolute; z-index: 99999; top: 0px;">
			    		<table class="x-table report-table">
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="${flag}" id="flag"  name="flag" />
</div>
</body>
</html>