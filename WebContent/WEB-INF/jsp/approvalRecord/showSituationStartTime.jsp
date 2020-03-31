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
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/iframe.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
    <script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
    <script src="${ctx}/js/base.js"></script>
    <script src="${ctx}/js/echarts/echarts-all.js"></script>
    <script type="text/javascript" src="${ctx}/common/extend.js"></script>
</head>
<body>
	<div id="fhyhz" class="chartDiv divContent">
        <span class="jbg topLeft"></span>
        <span class="jbg topRight"></span>
        <span class="jbg bottomLeft"></span>
        <span class="jbg bottomRight"></span>
        <!--  中间的内容区域 -->
        <h6 class="chartDivTitle">
	        <span class="r" >
		        <a href="javascript:void(0)" onclick="Tab(this,'.chartDivTitle a','click-on');showHide('#fhy-gs');" class="click-on">
		       		 个数
		        </a>
		        <a href="javascript:void(0)" onclick="Tab(this,'.chartDivTitle a','click-on');showHide('#fhy-ztz');" >
		       		 总投资
		        </a>
	        </span>
        	分预计开工时间趋势
        	<i title="设为主控"></i>
        	<a class="more" href="javaScript:void(0)">更多>></a>
        </h6>
        <div class="chartHeight" id="fhy-gs" style="visibility: hidden;">
            <div class="chartCenter" id="myNumChart" > </div>
        </div>
        <div class="chartHeight" id="fhy-ztz">
            <div class="chartCenter" id="myChart"> </div>
        </div>
    </div>
    <script type="text/javascript">
    	var orderby = "";
  		//定义父窗口
    	var parentWin = parent;
        // 默认按照金额排序 
        var orderby="orderbymon";
  		//页面初始化加载
    	window.onload = function(){
        	for (var i=0;i<5;i++) {
            	if (parentWin.win) {
            		//将当前窗口定义到父级窗口
                	parentWin.win["viewFiveTrends"] = window;
            	}else{
                	parentWin = parentWin.parent;
            	}
        	}
     		// 装载数据 
        	loadData();
     		
     		//单击更多触发事件
        	$('#fhyhz').find(".more").bind("click",function(){
        		var url = "${ctx}/view/overView!getAuditPreparationByStartTime?orderby="+orderby;
        		var name=parentWin.filters["params.moduleName"];
                var params =parentWin.filters;
        		loadMoreData(url,params);
        	});
    	}
    	
  		var option = {
  			    tooltip : {
  			        trigger: 'axis',
  			      	formatter : function(params) {
			        	var str = '';
			        	if (null != params && params.length > 0) {
			        		str += params[0].name + '</br>';
			        		for (var i = 0; i < params.length; i++) {
			        			str += params[i].seriesName + '：' + formatAmount(params[i].value) + '</br>';
			        		}
			        	}
			        	return str;
			        }
  			    },
  			    legend: {
                    textStyle:ZHFX.legendTextStyle,
  			        data:[]
  			    },
  			  	grid:{
                 	borderWidth:0,
                 	x2:100
          		},
  			    calculable : false,
  			    xAxis : [
                    {
                        type: 'category',
                        boundaryGap: false,
                        splitLine: {show:false},
                        data: [],
                        axisLabel: {
                            rotate:'-30',
                            textStyle: ZHFX.XTextStyle
                        }
                    }
  			    ],
  			    yAxis : [
  			        {
  			            type : 'value',
                        splitLine:{
                            show:true,
                            lineStyle: {
                                color: '#333e50',
                                type:'dashed'

                            }
                        },
  			            axisLabel : {
                            textStyle: ZHFX.YTextStyle,
  			            	formatter : function(value) {
		                    	if ((formatAmount(value)+'').indexOf('.00') > 0) {
	                         		return (formatAmount(value)+'').replace('.00','');
	                         	} else if ('0元' != formatAmount(value)) {
	                          		return formatAmount(value);
	                          	} else {
	                         		return 0;
	                         	}
		                    }
  			            }
  			        }
  			    ],
  			    series : [
  			        {
  			            name:'审核备项目总投资',
  			            type:'line',
  			            data:[],
  			          	itemStyle : {
			            	normal : {
			            		label : {
			            			show : true,
			            			position : 'top',
			            			formatter: function (params) {
			            				return formatFund(params.value);
			                        },
			                        textStyle: ZHFX.topTextStyle
			            		}
			            	}
			            }
  			        }
  			    ]
  			};
  		
  		var optionNum = {
  			    tooltip : {
  			        trigger: 'axis',
  			      	formatter : function(params) {
			        	var str = '';
			        	if (null != params && params.length > 0) {
			        		str += params[0].name + '</br>';
			        		for (var i = 0; i < params.length; i++) {
			        			str += params[i].seriesName + '：' + formatCount(params[i].value) + '</br>';
			        		}
			        	}
			        	return str;
			        }
  			    },
  			    legend: {
                    textStyle:ZHFX.legendTextStyle,
  			        data:[]
  			    },
  			  	grid:{
                 	borderWidth:0,
                 	x2:100
          		},
  			    calculable : false,
  			    xAxis : [
                    {
                        type: 'category',
                        boundaryGap: false,
                        splitLine: {show:false},
                        data: [],
                        axisLabel: {
                            rotate:'-30',
                            textStyle: ZHFX.XTextStyle
                        }
                    }
  			    ],
  			    yAxis : [
  			        {
  			            type : 'value',
                        splitLine:{
                            show:true,
                            lineStyle: {
                                color: '#333e50',
                                type:'dashed'

                            }
                        },
  			            axisLabel : {
                            textStyle: ZHFX.YTextStyle,
                            formatter : function(value) {
		                    	if ('0个' != formatCount(value)) {
	                          		return formatCount(value);
	                          	} else {
	                         		return 0;
	                         	}
		                    }
  			            }
  			        }
  			    ],
  			    series : [
  			        {
  			            name:'审核备项目个数',
  			            type:'line',
  			            data:[],
  			          	itemStyle : {
			            	normal : {
			            		label : {
			            			show : true,
			            			position : 'top',
			            			formatter: function (params) {
			            				return formatCnt(params.value);
			                        },
			                        textStyle: ZHFX.topTextStyle
			            		}
			            	}
			            }
  			        }
  			    ]
  			};
  		
    	//初始化数据
    	var myChart = echarts.init(document.getElementById('myChart'));
    	var myNumChart = echarts.init(document.getElementById('myNumChart'));
    	
    	/**
    	 * 加载数据
    	 */
    	function loadData() {
    		myChart.showLoading({
                text: '正在努力的加载数据中...'
            });
    		var params = parentWin.filters;
    		// 异步加载 全国项目进展情况
            $.ajax({
                url: "${ctx}/view/overView!getAuditPreparationByStartTime",
                data : params,
                dataType: "json",
                success: function (data) {
                	console.log(data);
                	var legendAxisData = [];
                    var investMon = [];
                    var counts={
                    		cnt:[]
                        };
                    var names = [];
                	if (data) {
                	    var  totalFive=0;
                	    var  totalCnt=0;
                		// 遍历对象
                        for (var i=0,len=data.length; i<len; i++) {
                        	var date = '';
                        	if (data[i]["itemName"] && data[i]["itemName"].length >= 4) {
                        		date = data[i]["itemName"].substring(0,4) + '年'; 
                        			 //+ data[i]["itemName"].substring(4,data[i]["itemName"].length) + '月';
                        	}
                        	if (legendAxisData.indexOf(date) < 0) {
	                        	legendAxisData.push(date);
                        	}
                            totalFive=totalFive+parseInt(data[i]["investMon"]);
                   			investMon.push(totalFive);
                   			totalCnt = totalCnt +parseInt(data[i]["cnt"])
                   			counts.cnt.push(totalCnt);                    	
                        }
                    }
                	option.xAxis[0].data = legendAxisData;
                	//设置数据
                    option.series[0].data = investMon;
                    //总投资
                    optionNum.xAxis[0].data = legendAxisData;
                	//设置数据
                    optionNum.series[0].data = counts.cnt;
                    //加载隐藏
                    myChart.hideLoading();
                    //渲染对象
                    myChart.setOption(option,true);
                    myNumChart.setOption(optionNum,true);
                }
            });
    	}
    	
    	//刷新
        function refreshWin() {
            // 联动设置
            loadData();
        }
    </script>
</body>
</html>