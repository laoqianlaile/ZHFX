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
<title></title>
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/iframe.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
    <script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
    <script src="${ctx}/js/base.js"></script>
    <script src="${ctx}/js/echarts/echarts-all.js"></script>
    <script src="${ctx}/js/loadRegionMap.js"></script>
    <script type="text/javascript" src="${ctx}/common/extend.js"></script>
</head>
<body>
    <div class="chartDiv divContent">
        <span class="jbg topLeft"></span>
        <span class="jbg topRight"></span>
        <span class="jbg bottomLeft"></span>
        <span class="jbg bottomRight"></span>
        <div class="MapLink">
        	<input type="hidden" id="provinceSpanValue" />
        	<input type="hidden" id="citySpanValue" />
        	<a id="chianSpan"  onclick="drillUp('china')">中国</a>
        	<a id="provinceSpan"  onclick="drillUp('province')"></a>
        	<a id="citySpan"  onclick="drillUp('city')"></a>
        </div>
        <div class="total_box">
	        <div class="totalInfo totalNum" onclick="switchTab('0');showHide('#fzd-gs');">
	            <i>总个数</i>
	            <p id="totalCount"></p>
	        </div>
	        <div class="totalInfo totalMoney active" onclick="switchTab('1');showHide('#fzd-ztz');">
	            <i>总投资</i>
	            <p id="totalInv"></p>
	        </div>
        </div>
        <!--  中间的内容区域 -->
        <h6 class="chartDivTitle" style="padding:0;line-height:normal;top:0px;right:20px;left:0;z-index:1000;">
            <span class="r" >
<!--             <a class="more" href="javaScript:void(0)">更多>></a> -->
            </span>
         <div class="mkhf">
<!--             <ul> -->
<!--                 <li class="click_on" ><a href="javascript:loadData('fiveplan');parentWin.refreshWin();">五年规划储备项目</a></li> -->
<!--                 <li ><a href="javascript:loadData('unfinish');parentWin.refreshWin();">未编制三年滚动计划项目</a></li> -->
<!--             </ul> -->
        </div>
        </h6>
        <h1 id="titleText"></h1>
        <%--<h2 id="titleText2">总投资:<span id="totalInv"></span>&nbsp;&nbsp;个数:<span id="totalCount"></span></h2>--%>
        <div class="chartHeight" style="visibility: hidden;" id="fzd-gs" style="margin-right: 300px;"  >
           <div style="width:80%;height:100%;" id="num"> </div>
       </div>
        <div class="chartHeight" id="fzd-ztz">
            <div style="width:80%;height:100%;" id="main" style="margin-right: 300px;" > </div>
        </div>
    </div>

        <script>
      	//全局变量--存储地区名称
        var _mapViewType = null;
      	var _type = null;
        // 投资分布
        var _tIdx="1";
      	//定义父窗口
        var parentWin = parent;        // 默认按照金额排序 
        var orderby="orderbymon";     	
        var winOption = "height=600,width=1200,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";

      	//页面初始化加载
        window.onload = function(){
            for (var i=0;i<5;i++) {
                if (parentWin.win) {
                	//将当前窗口定义到父级窗口
                    parentWin.win["viewApprovalMap"] = window;
                }else{
                    parentWin = parentWin.parent;
                }
            }
         	// 装载数据 五年滚动计划 项目金额
            loadData("fiveplan","area");
         	
            //单击更多触发事件
         	$('.chartDivTitle').find(".more").bind("click",function(){
         		var url = "${ctx}/view/overView!getAuditPreparationByPlace?orderby="+orderby;
         		var name=parentWin.filters["params.moduleName"];
                 var params =parentWin.filters;
         		loadMoreData(url,params);
         	});
            
            
        }

        //默认隐藏省
        $('#provinceSpan').hide();
      	//默认隐藏市
		$('#citySpan').hide();
        
        /*
         * 实现上钻功能
         * param： 地区参数：china：全国，province：省，city：市
         */
        function drillUp(param) {
        	//判断地区所属
        	switch(param) {
        		//全国
        		case 'china' :
        			//判断地区记录长度
        			switch(records.length) {
        				case 3 : 
        					//去掉省市
	        				records.pop();
	        				records.pop();
	        				break;
        				case 2 : 
        					//去掉省
            				records.pop();
            				break;
        				case 1 :
        					break;
        				default : 
        					break;
        			}
        			//全国
        			_mapViewType = 'china';
        			//过滤条件
        			parentWin.filters = {};
        			//清空值
        			$('#provinceSpan').text('');
        			$('#provinceSpanValue').val('');
        			$('#citySpan').text('');
        			$('#citySpanValue').val('');
        			//隐藏
        			$('#provinceSpan').hide();
        			$('#citySpan').hide();
        			break;
        		//省
        		case 'province' :
        			//判断地区记录长度
        			switch(records.length) {
        				case 3 : 
        					//去掉市
	        				records.pop();
	        				break;
        				case 2 : 
            				break;
        				case 1 :
        					break;
        				default : 
        					break;
        			}
        			//省
        			_mapViewType = $.trim($('#provinceSpan').text());
        			//过滤初始化
        			parentWin.filters = {};
        			//过滤条件
        			parentWin.filters["params.level"] = 1;
        			parentWin.filters["params.filterCode"] = $('#provinceSpanValue').val();
        			//清空值
        			$('#citySpan').text('');
        			$('#citySpanValue').val('');
        			//隐藏
        			$('#citySpan').hide();
        			//$('#markPoint2').hide();
        			break;
        	    //市
        		case 'city' :
        			//市
        			_mapViewType = $.trim($('#citySpan').text());
        			//过滤初始化
        			parentWin.filters = {};
        			//过滤条件
        			parentWin.filters["params.level"] = 2;
        			parentWin.filters["params.filterCode"] = $('#citySpanValue').val();
        			break;
        		default :
        			break;
        	}
        	//刷新
        	parentWin.refreshWin();
        	//重新加载数据
        	loadData("fiveplan","area");
        }

        //总投资
        var myChart = echarts.init(document.getElementById('main'));
        //项目个数
        var numChart = echarts.init(document.getElementById('num'));
           	// 初始化清空对那个
            function initClearObj() {
                option = [];
                numOption = [];
                //optionNum.options=[];
            }
            /**
             *  加载数据
             *  module: 查询的模块
             *  type:查询的类别   num 项目个数、总投资 :invest
             */
             function  loadData(module,type) {
                //将当前类型存入主窗口全局变量
                //parentWin.filters["params.isFinish"] = module;
                // 过滤的类型
               // _type=module;
                // 过滤数据
                var params = parentWin.filters;
                //params["module"] = "fiveplan";
               // params["type"] = type||"area";
                myChart.showLoading({
                    text: '正在努力的加载数据中...'
                });
                // 加载信息
                $.ajax({
                        type: "POST",
                        url: "${ctx}/view/overView!getAuditPreparationByPlace", //总览的视图
                        data : params, //默认展现五年滚动计划
                        dataType:'json',
                        success: function(data){
                            // 遍历设置最大阈值
                            var option={
                                tooltip : {
                                    'trigger':'item',
                                     textStyle:ZHFX.tooltipTextStyle,
                                    formatter : function(params) {
                                        var str = params.name + '</br>' + '总投资' + '：' + formatAmount(params.value);
                                        return str;
                                    }
                                },
                                dataRange: {
                                    min: 0,
                                    max : 53000,
                                    text:['万',''],           // 文本，默认为数值文本
                                    calculable : true,
                                    x: 10,
                                    y2:50,
                                    color: ZHFX.mapColors,
                                    textStyle:{
                                        "color": "#fff"
                                    }
                                },
                                series : [
                                    {
                                        name:'投资分布',
                                        type:'map',
                                        itemStyle:{
                                            normal:{
                                                label:{show:true},
                                                borderColor: '#ffffff',
                                                borderWidth: 1
                                            },
                                            emphasis:{
                                                color:ZHFX.mapSelectColor,
                                                label:{show:true}
                                            }
                                        },
                                        mapLocation: {
                                            "y": "top",
                                            "height":"85%"
                                        },
                                        selectedMode: 'single',
                                        data:[],
                                        nameMap:nameMap
                                    }
                                ]
                            };

                            // 项目个数
                            // 地图的属性
                            var numOption = {
                                tooltip : {
                                    'trigger':'item',
                                    textStyle:ZHFX.tooltipTextStyle,
                                    formatter : function(params) {
                                        var str = params.name + '</br>' + '个数' +  '：' + formatCount(params.value);
                                        return str;
                                    }
                                },
                                dataRange: {
                                    min: 0,
                                    max : 53000,
                                    text:['个',''],           // 文本，默认为数值文本
                                    calculable : true,
                                    x: 10,
                                    y2:50,
                                    color: ZHFX.mapColors,
                                    textStyle:{
                                        "color": "#fff"
                                    }
                                },
                                series : [
                                    {
                                        name:'投资项目个数分布',
                                        type:'map',
                                        mapLocation: {
                                            "y": "top",
                                            "height":"85%"
                                        },
                                        selectedMode: 'single',
                                        data:[],
                                        nameMap:nameMap,
                                        itemStyle:{
                                            normal:{
                                                label:{show:true},
                                                borderColor: '#ffffff',
                                                borderWidth: 1
                                            },
                                            emphasis:{
                                                color:ZHFX.mapSelectColor,
                                                label:{show:true}
                                            }
                                        },
                                    }
                                ]
                            };
                        	//debugger;
                            initClearObj();
                            // 最大总投资
                            var maxVal=0;
                            var maps = [];
                            // 项目个数
                            var numMaps = [];
                            //总个数
                            var totalCount = Number(0);
                            //总投资
                            var totalInv = Number(0);
                            if(data){
                                // 遍历对象
                                for(var i=0,len=data.length;i<len;i++){
                                    var provice = data[i]["itemName"];
                                    var proviceCode = data[i]["itemCode"];
                                    switch(provice){
                                        case"内蒙古自治区": provice="内蒙古" ; break;
                                        case"广西壮族自治区": provice="广西" ; break;
                                        case"西藏自治区": provice="西藏" ; break;
                                        case"宁夏回族自治区": provice="宁夏" ; break;
                                        case"新疆维吾尔自治区": provice="新疆" ; break;
                                        case"香港特别行政区": provice="香港" ; break;
                                        case"澳门特别行政区": provice="澳门" ; break;
                                    }
                                    var value = 0,numValue=0;
                                    value = parseInt(data[i]["investMon"])||'-';
                                    numValue = parseInt(data[i]["cnt"])||'-';
                                    // 总投资和总个数
                                    totalCount += Number(data[i]["cnt"]||0);
                                    totalInv += Number(data[i]["investMon"]||0);
                                 	// 存放数据
                                    maps.push({name:provice,value:value,itemCode:proviceCode});
                                    numMaps.push({name:provice,value:numValue,itemCode:proviceCode});
                                }
                            }
                          	//定义标题名称
                            var titleText = '';
                            //地区信息（省/市名称）
                            var areaText = '';
                            //选中市
                            if (null != $('#citySpan').text() 
                        			&& '' != $('#citySpan').text()) {
                        		areaText = $('#citySpan').text();
                        	//选中省
                        	} else if (null != $('#provinceSpan').text() 
                            		&& '' != $('#provinceSpan').text()) {
                            	areaText = $('#provinceSpan').text();
                            } else {
                            	areaText = '全国';
                            }

                            titleText =   ""+areaText+ formatTitle(_tIdx);
                            $('#titleText').text(titleText);
                            $('#totalCount').text(formatCount(totalCount));
                            $('#totalInv').text(formatAmount(totalInv));
                            option.dataRange.max = parseInt(ZHFX.getMaxVal(maps)+1)||10;
                            option.series[0].data = maps;
                            //实现下钻
                            option.series[0].mapType = _mapViewType||"china";
                            // 项目个数
                            numOption.dataRange.max = parseInt(ZHFX.getMaxVal(numMaps)+1)||10;
                            numOption.series[0].data = numMaps;
                            //实现下钻
                            numOption.series[0].mapType = _mapViewType||"china";
                            //加载隐藏
                            myChart.hideLoading();
                           	// 渲染对象
                            myChart.setOption(option,true);
                            numChart.setOption(numOption,true);
                        }
                    });
                }
            
            //刷新
            function refreshWin() {
               //alert("viewFiveMap");
            }       
            
            /**
             * 格式化标题
             */
            function formatTitle(tIdx) {
            	switch(tIdx){
	                case "0" : return "个数分布";
	                case "1" : return "投资分布";
	            }
	            return "";
            }
            
            /**
             * 格式化标题
             */
            function switchTab(tIdx) {
            	var titleText = $("#titleText").text();
            	switch(tIdx){
	                case "0" : 
	                	orderby="orderbycnt";
	                	if (titleText.lastIndexOf('投资分布')) {
	                		 var _tt = titleText.replace("投资分布", "分布");
	                         $("#titleText").text(_tt);
	                	}
						break;
	                case "1" : 
	                	orderby="orderbymon";
	                	if (titleText.lastIndexOf('分布')) {
	                		 var _tt = titleText.replace("分布", "投资分布");
	                         $("#titleText").text(_tt);
	                	}
	                	break;
	            }
            }
            
         	// 选择个数
            numChart.on(echarts.config.EVENT.MAP_SELECTED,function (param) {
            	// 清空过滤
                parentWin.filters={};
                // 访问到了县级 直接结束
                var code=findCode(numChart.getSeries()[0].data,param.target);
                if(!code){
                    return false;
                } 
            	// 访问到了县级 直接结束
                if(code.substring(4,6) != '00'){
                	//省市县等级
                	parentWin.filters["params.level"] = records.length-1;
                	//省市县编码
                    parentWin.filters["params.filterCode"] = code;
                	//刷新
                    parentWin.refreshWin();
                   // window.open('${ctx}/fiveplan/projectFile!list1?&code='+ code +'&codeName=projectRegion&type=1&filterParams='+_type, window, winOption);
                    return false;
                }
            	//将省市值存入隐藏域
            	if (code.substring(2,6) == '0000') {
            		//显示省
            		$('#provinceSpan').show();
            		//显示>
            		//$('#markPoint1').show();
            		//给省隐藏域赋值
            		$('#provinceSpanValue').val(code);
            		//增加标点>
            		//$('#markPoint1').text('>');
            		//显示省中文名称
            		$('#provinceSpan').text(param.target);
            	} else if (code.substring(4,6) == '00') {
            		//显示市
            		$('#citySpan').show();
            		//显示>
            		//$('#markPoint2').show();
            		//给市隐藏域赋值
            		$('#citySpanValue').val(code);
            		//增加标点>
            		//$('#markPoint2').text('>');
            		//显示市中文名称
            		$('#citySpan').text(param.target);
            	}
                //parentWin.mainParam = param.target;
               // parentWin.refreshWin(parentWin.mainParam);
                var len = mapType.length;
                var mt = records[records.length-1]||'china';
             	// 已经访问过
                if (isVisited(mt)) {
                    // 判断
                    if (!isVisited(param.target)) {
                        // 存放访问记录
                        records.push(param.target);
                    }
                    // 全国选择时指定到选中的省份
                    var selected = param.selected;
                    for (var i in selected) {
                        if (selected[i]) {
                            mt = i;
                            while (len--) {
                                if (mapType[len] == mt) {
                                    curIndx = len;
                                }
                            }
                            break;
                        }
                    }
                }
                else {
                    curIndx = 0;
                    mt = 'china';
                }
             
              	//钻取
                parentWin.filters["params.level"]=records.length-1;
                parentWin.filters["params.filterCode"]=code;              
                // 刷新页面
                parentWin.refreshWin();
                _mapViewType=mt;
                loadData("fiveplan","area");
                // 维度对应的地图
                numChart.setOption(option, true);
                //parentWin.refreshWin();
            });

        // 选中地图,实现下钻
        myChart.on(echarts.config.EVENT.MAP_SELECTED,function (param) {
            // 清空过滤
            parentWin.filters={};
            // 访问到了县级 直接结束
            var code=findCode(myChart.getSeries()[0].data,param.target);
            if(!code){
                return false;
            }
            // 访问到了县级 直接结束
            if(code.substring(4,6) != '00'){
                //省市县等级
                parentWin.filters["params.level"] = records.length-1;
                //省市县编码
                parentWin.filters["params.filterCode"] = code;
                //刷新
                parentWin.refreshWin();
            // window.open('${ctx}/fiveplan/projectFile!list1?&code='+ code +'&codeName=projectRegion&type=1&filterParams='+_type, window, winOption);
                return false;
            }
            //将省市值存入隐藏域
            if (code.substring(2,6) == '0000') {
                //显示省
                $('#provinceSpan').show();
                //显示>
                //$('#markPoint1').show();
                //给省隐藏域赋值
                $('#provinceSpanValue').val(code);
                //增加标点>
                //$('#markPoint1').text('>');
                //显示省中文名称
                $('#provinceSpan').text(param.target);
            } else if (code.substring(4,6) == '00') {
                //显示市
                $('#citySpan').show();
                //显示>
                //$('#markPoint2').show();
                //给市隐藏域赋值
                $('#citySpanValue').val(code);
                //增加标点>
                //$('#markPoint2').text('>');
                //显示市中文名称
                $('#citySpan').text(param.target);
            }
            //parentWin.mainParam = param.target;
            // parentWin.refreshWin(parentWin.mainParam);
            var len = mapType.length;
            var mt = records[records.length-1]||'china';
            // 已经访问过
            if (isVisited(mt)) {
                // 判断
                if (!isVisited(param.target)) {
                    // 存放访问记录
                    records.push(param.target);
                }
                // 全国选择时指定到选中的省份
                var selected = param.selected;
                for (var i in selected) {
                    if (selected[i]) {
                        mt = i;
                        while (len--) {
                            if (mapType[len] == mt) {
                                curIndx = len;
                            }
                        }
                        break;
                    }
                }
            }
            else {
                curIndx = 0;
                mt = 'china';
            }

            //钻取
            parentWin.filters["params.level"]=records.length-1;
            parentWin.filters["params.filterCode"]=code;
            // 刷新页面
            parentWin.refreshWin();
            _mapViewType=mt;
            loadData("fiveplan","area");
            // 维度对应的地图
            myChart.setOption(option, true);
        });

        /**
             *  判断图标是否被访问过
             *
             */
            function isVisited( name){
                var  len=records.length;
                // 如果记录集合里面没有值
                if(len===0){
                    curIndx=0;
                    // 把全国放在里面
                    records.push("china");
                }
                for(var i= 0;i<len;i++){
                    // 以及时访问过的
                    if(name==records[i]){
                        return records[i];
                    }
                }
                return false;
            }
        	//刷新
            function refreshWin() {
                // 联动设置
            	loadData("fiveplan","area");
            }
		 </script>
</body>
</html>