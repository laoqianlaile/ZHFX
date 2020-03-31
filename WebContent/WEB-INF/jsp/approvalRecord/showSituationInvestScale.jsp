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
        <div class="mkhf" style="top: 50px;z-index: inherit">
<!--             <ul> -->
<!--                 <li class="click_on"><a href="javascript:tabView('industry');parentWin.refreshWin();" >委内行业</a></li> -->
<!--                 <li><a href="javascript:tabView('GBindustry');parentWin.refreshWin();" >国标行业</a></li> -->
<!--             </ul> -->
        </div>
        <h6 style="width: 400px;z-index: 10;">
        <span>
            	分投资规模情况
           <i title="设为主控" onclick="parentWin.switchWin('bar')" style="margin-right:11px;top:7px;">
           </i>
       </span>
            <a class="more" href="javaScript:void(0)">更多>></a>
        </h6>
    <%--发改委行业--%>
    <div id="GBindustry" style="position:absolute;top:0px;left:0px;right:0px;bottom:0px;">
        <h6 class="chartDivTitle"><span class="r"><a href="javascript:void(0)" onclick="Tab(this,'.chartDivTitle a','click-on');showHide('#xmcbhz11');" class="click-on">个数</a><a href="javascript:void(0)" onclick="Tab(this,'.chartDivTitle a','click-on');showHide('#xmcbhz22');">总投资</a></span></h6>
        <br>
        <div class="chartHeight"  id="xmcbhz11" style="visibility: hidden;">
            <div class="chartCenter" id="numGBChart"> </div>
        </div>
        <div class="chartHeight"  id="xmcbhz22">
            <div class="chartCenter" id="myGBChart"> </div>
        </div>
    </div>
</div>
    <script type="text/javascript">
  		//定义父窗口
    	var parentWin = parent;
        // 默认按照金额排序 
        var orderby="orderbymon";
  		//页面初始化加载
    	window.onload = function(){
        	for (var i=0;i<5;i++) {
            	if (parentWin.win) {
            		//将当前窗口定义到父级窗口
                	parentWin.win["viewApprovalIndustry"] = window;
            	}else{
                	parentWin = parentWin.parent;
            	}
        	}
     		
            // 国标航行业
            loadGBData();
            //单击更多触发事件
         	$('#fhyhz').find(".more").bind("click",function(){         		
         		var url="";         		
         		url= "${ctx}/view/overView!getAuditPreparationByInvest?orderby="+orderby;
                var params =parentWin.filters;
         		loadMoreData(url,params);
         	});
            
            // 国标行业
          	//个数
            $("#GBindustry .r a:eq(0)").bind("click",function(){
            	Tab(this,'.chartDivTitle a','click-on');
            	showHide('#xmcbhz11');
            	// 项目个数 
            	orderby="orderbycnt";
         		loadData();
         		loadGBData();
         	});
            // 总投资
            $("#GBindustry .r a:eq(1)").bind("click",function(){
            	Tab(this,'.chartDivTitle a','click-on');
            	showHide('#xmcbhz22');
            	// 总投资  
             	orderby="orderbymon";
          		loadData();
          		loadGBData();
         	});
    	}

        // 切换视图
        function tabView(id) {
            switch (id){
                case 'industry': 
                	$("#industry").css("visibility","visible").show(); 
                	$("#GBindustry").css("visibility","hidden").hide();
                	$("#GBindustry").removeClass("click_on");  
                	$("#industry").addClass("click_on");
                	Tab($("#industry .r a:eq(1)"),'.chartDivTitle a','click-on');
                	break;  // 申报单位
                case 'GBindustry': 
                	$("#GBindustry").css("visibility","visible").show(); 
                	$("#industry").css("visibility","hidden").hide();
                	$("#industry").removeClass("click_on");  
                	$("#GBindustry").addClass("click_on");
                	Tab($("#GBindustry .r a:eq(1)"),'.chartDivTitle a','click-on');
                	break;   /// 行业
            }
        }
    	
        /// 国标行业
        var GBoption = {
            tooltip : {
                trigger: 'axis',
                formatter : function(params) {
                    var str = '';
                    if (null != params && params.length > 0) {
                        str += params[0].name + '</br>';
                        for (var i = 0; i < params.length; i++) {
                            str += '审核备项目总投资' + '：' + formatAmount(params[i].value) + '</br>';
                        }
                    }
                    return str;
                }
            },
            legend: {
                data:[]
            },
            grid:{
	               	borderWidth:0,
               	x: 100,
               	x2:100,
               	y: 80,
               	y2:100
        	},
            calculable : false,
            xAxis : [
                {
                    splitLine: {show:false},
                    axisLabel: {
                        rotate:'-25',
                        textStyle: ZHFX.XTextStyle
                    },
                    type : 'category',
                    data : []
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    axisLabel: {
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
                    },
                    splitLine:{
                        show:true,
                        lineStyle: {
                            color: '#333e50',
                            type:'dashed'

                        }
                    },
                }
            ],
            series : [
                {
                    name:'',
                    type:'bar',
                    data:[],
                    itemStyle: {
                        normal: {
                            color: 'orange',
                            label : {
                            	show : true,
		            			position : 'top',
		            			formatter: function (params) {
		            				return formatFund(params.value);
		                        },
                                textStyle : ZHFX.topTextStyle
                            }
                        }
                    }
                }
            ]
        };
        // 项目个数
        var numGBOption = {
            tooltip : {
                trigger: 'axis',
                formatter : function(params) {
                    var str = '';
                    if (null != params && params.length > 0) {
                        str += params[0].name + '</br>';
                        for (var i = 0; i < params.length; i++) {
                            str += '审核备项目个数' + '：' + formatCount(params[i].value) + '</br>';
                        }
                    }
                    return str;
                }
            },
            legend: {
                data:[]
            },
            grid:{
               	borderWidth:0,
               	x: 100,
               	x2:100,
               	y: 80,
               	y2:100
        	},
            calculable : false,
            xAxis : [
                {
                    splitLine: {show:false},
                    axisLabel: {
                        rotate:'-25',
                        textStyle: ZHFX.XTextStyle
                    },
                    type : 'category',
                    data : []
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    axisLabel: {
                        textStyle: ZHFX.YTextStyle,
                        formatter : function(value) {
                            if ((formatCount(value)+'').indexOf('.00') > 0) {
                                return (formatCount(value)+'').replace('.00','');
                            } else if ('0个' != formatCount(value)) {
                                return formatCount(value);
                            } else {
                                return 0;
                            }
                        }
                    },
                    splitLine:{
                        show:true,
                        lineStyle: {
                            color: '#333e50',
                            type:'dashed'

                        }
                    },
                }
            ],
            series : [
                {
                    name:'',
                    type:'bar',
                    data:[],
                    itemStyle: {
                        normal: {
                            color: 'orange',
                            label : {
                            	show : true,
		            			position : 'top',
		            			formatter: function (params) {
		            				return formatCnt(params.value);
		                        },
                                textStyle : ZHFX.topTextStyle
                            }
                        }
                    }
                }
            ]
        };

        // 国标行业
        var myGBChart = echarts.init(document.getElementById('myGBChart'));
        var numGBChart = echarts.init(document.getElementById('numGBChart'));
    	
    	// 加载国标数据
        /**
         * 加载国标行业数据
         */
        function loadGBData() {
            var params = parentWin.filters;
            myGBChart.showLoading({
                text: '正在努力的加载数据中...'
            });
            // 异步加载 全国项目进展情况
            $.ajax({
                url: "${ctx}/view/overView!getAuditPreparationByInvest?orderby="+orderby,
                data : params,
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    var legendAxisData = [];
                    var investMon = [];
                    // 项目个数
                    var counts=[];
                    if (data) {
                        // 遍历对象
                        for (var i=0,len=data.length; i<len; i++) {
                            //名称
                            var VipArea = data[i]["itemName"];
                            //总投资
                            investMon.push(parseInt(data[i]["investMon"]));
                            counts.push(parseInt(data[i]["cnt"]));
                            //x轴数据
                            legendAxisData.push(VipArea);
                        }
                    }
                    //总投资
                    GBoption.xAxis[0].data = legendAxisData;
                    //设置数据
                    GBoption.series[0].data = investMon;
                    //项目个数
                    numGBOption.xAxis[0].data = legendAxisData;
                    //设置数据
                    numGBOption.series[0].data = counts;
                    //加载隐藏
                    myGBChart.hideLoading();
                    //渲染对象
                    myGBChart.setOption(GBoption,true);
                    numGBChart.setOption(numGBOption,true);
                }
            });
        }

    	//刷新
        function refreshWin() {
            // 联动设置
            loadGBData();
        }
    </script>
</body>
</html>