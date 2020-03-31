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
<div class="chartDiv divContent">
    <span class="jbg topLeft"></span>
    <span class="jbg topRight"></span>
    <span class="jbg bottomLeft"></span>
    <span class="jbg bottomRight"></span>
    <!--  中间的内容区域 -->
    <h6 class="chartDivTitle">
	        <span class="r">
		        <a href="javascript:void(0)" onclick="Tab(this,'.chartDivTitle a','click-on');showHide('#fzf-gs');" class="click-on">
		        	个数
		        </a>
		        <a href="javascript:void(0)" onclick="Tab(this,'.chartDivTitle a','click-on');showHide('#fzf-ztz');">
		       		 总投资
		        </a>
	        </span>
<%--         <span style="cursor: pointer" onclick="ZHFX.toIframeSrc('/common/subcommon/common-fivePlan')"> --%>
        <span>
        	分项目类型情况
        </span>
        <i title="设为主控"></i>
        <a class="more" href="javaScript:void(0)">更多>></a>
    </h6>
    <div class="chartHeight" id="fzf-gs" style="visibility: hidden;">
        <div style="width:100%;height:100%;left:0px;position:absolute;" id="numChart"> </div>
    </div>
    <div class="chartHeight" id="fzf-ztz">
        <div style="width:100%;height:100%;left:0px;position:absolute;" id="myChart"> </div>
    </div>
</div>
    <script type="text/javascript">
  		//定义父窗口
    	var parentWin = parent;
    	var orderby="orderbymon";
  		//页面初始化加载
    	window.onload = function(){
        	for (var i=0;i<5;i++) {
            	if (parentWin.win) {
            		//注册项目类型
                	parentWin.win["viewProjectType"] = window;
            	}else{
                	parentWin = parentWin.parent;
            	}
        	}
     		// 装载数据 
        	loadData();
     		
            //单击更多触发事件
         	$('.chartDivTitle').find(".more").bind("click",function(){
         		var url = "${ctx}/view/overView!getAuditPreparationByProject?orderby="+orderby;
         		var name=parentWin.filters["params.moduleName"];
                 var params =parentWin.filters;
         		loadMoreData(url,params);
         	});
            
            //个数
            $(".r a:eq(0)").bind("click",function(){
            	Tab(this,'.chartDivTitle a','click-on');
            	showHide('#fzf-gs');
            	// 项目个数 
            	orderby="orderbycnt";
         		loadData();
         	});
            // 总投资
            $(".r a:eq(1)").bind("click",function(){
            	Tab(this,'.chartDivTitle a','click-on');
            	 showHide('#fzf-ztz');
            	// 总投资  
             	orderby="orderbymon";
          		loadData();
         	});
    	}
    	var option = {
    		    tooltip : {
    		        trigger: 'item',
    		        formatter : function(params) {
                        var str = '';
                        if (params) {
                        str += params.name + '</br>' 
                        + '审核备项目总投资' + '：' + formatAmount(params.value) + '</br>'
                        +'占比：'+params.percent+'%';
                    }
			        	return str;
			        }
    		    },
    		    legend: {
    		    	show : false,
                    x : 'center',
                    y : 'top',
    		    	textStyle:ZHFX.legendTextStyle,
    		        data:[]
    		    },
    		    calculable : false,
    		    series : [
    		        {
    		            name:'分重大战略',
                        type:'pie',
                        radius : '45%',
                        center: ['50%', '50%'],
                        roseType : 'radius',
                        sort : 'ascending',
                        data:[],
    		            itemStyle: {
			            	normal: {
			            		label : {
			                        show : true,
	                                 formatter:function(p){

	                                     var value=p.value;
	                                     if ((formatAmount(value)+'').indexOf('.00') > 0) {
	                                         return  p.name +"\n"+(formatAmount(value)+'').replace('.00','')+'\n'+p.percent+'%';
	                                     } else if ('0元' != formatAmount(value)) {
	                                         return  p.name +"\n"+ formatAmount(value)+'\n'+p.percent+'%';
	                                     } else {
	                                         return p.name;
	                                     }
	                                 },
			                        textStyle : ZHFX.topTextStyle
			                    }
			            	}
			            }
    		        }
    		    ]
    		};
         // 项目个数
        var numOption = {
            tooltip : {
                trigger: 'item',
                formatter : function(params) {
                    var str = '';
                    if (params) {
                        str += params.name + '</br>' 
                        	+ '审核备项目个数' + '：' + formatCount(params.value) + '</br>'
                        	+ '占比：'+params.percent+'%';
                    }
                    return str;
                }
            },
            legend: {
            	show : false,
                x : 'center',
                y : 'top',
                textStyle:ZHFX.legendTextStyle,
                data:[]
            },
            calculable : false,
            series : [
                {
                    name:'分重大战略',
                    type:'pie',
                    radius : '45%',
                    center: ['50%', '50%'],
                    roseType : 'radius',
                    sort : 'ascending',
                    data:[],
                    itemStyle: {
                        normal: {
                            label : {
                                show : true,
                                formatter:function(p){
                                    var value=p.value;
                                    if ((formatCount(value)+'').indexOf('.00') > 0) {
                                        return  p.name +"\n"+(formatCount(value)+'').replace('.00','')+'\n'+p.percent+'%';
                                    } else if ('0个' != formatCount(value)) {
                                        return  p.name +"\n"+ formatCount(value)+'\n'+p.percent+'%';
                                    } else {
                                        return p.name;
                                    }
                                },
                                textStyle : ZHFX.topTextStyle
                            }
                        }
                    }
                }
            ]
        };

    	//初始化数据
    	var myChart = echarts.init(document.getElementById('myChart'));
        var numChart = echarts.init(document.getElementById('numChart'));
    	/**
    	 * 加载数据
    	 */
    	function loadData() {
    		var params = parentWin.filters;
            myChart.showLoading({
                text: '正在努力的加载数据中...'
            });
    		// 异步加载项目审备核类型
            $.ajax({
                url: "${ctx}/view/overView!getAuditPreparationByProject",
                data : params,
                dataType: "json",
                success: function (data) {
                	var legendAxisData = [];
                    var investMon = [];
                    // 项目个数
                    var legendNumCount = [];
                    var counts = [];
                	if (data) {
                		// 遍历对象
                        for (var i=0,len=data.length; i<len; i++) {
                        	//名称
                            var VipArea=data[i]["itemName"];
                            // 值大小
                           var val= parseInt(data[i]["investMon"]);
                           var valNum=parseInt(data[i]["cnt"]);
                            var valStr='';
                            var valNumStr="";
                            if ((formatAmount(val)+'').indexOf('.00') > 0) {
                                valStr= (formatAmount(val)+'').replace('.00','');
                            } else if ('0元' != formatAmount(val)) {
                                valStr= formatAmount(val);
                            } else {
                                valStr= 0;
                            }
                            //总投资
                            investMon.push({name:VipArea,value:val});
                            //x轴数据
                            legendAxisData.push(VipArea);
                            //项目个数
                            valNumStr=formatCount(valNum);
                            counts.push({name:VipArea,value:valNum});
                            //x轴数据
                            legendNumCount.push(VipArea);
                        }
                    }
                	//总投资
                    option.legend.data = legendAxisData;
                	//设置数据
                    option.series[0].data = investMon;
                    // 项目个数
                    numOption.legend.data = legendNumCount;
                    numOption.series[0].data = counts;
                    //加载隐藏
                    myChart.hideLoading();
                    //渲染对象
                    myChart.setOption(option,true);
                    numChart.setOption(numOption,true);
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