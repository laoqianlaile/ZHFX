//项目分布
var _tIdx="0";
var winOption = "height=600,width=1200,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
/**
 *   加载专项建设基金项目
 * Created by tannc on 2016/9/29.
 */

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
         //   parentWin.filters = {};
          //  parentWin.filters["params.level"] = 1;
            parentWin.filters["params.filterCode"] ="";
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
        //    parentWin.filters = {};
            //过滤条件
            parentWin.filters["params.level"] = 1;
            parentWin.filters["params.filterCode"] = $('#provinceSpanValue').val();
            //清空值
            $('#citySpan').text('');
            $('#citySpanValue').val('');
            //隐藏
            $('#citySpan').hide();
            break;
        //市
        case 'city' :
            //市
            _mapViewType = $.trim($('#citySpan').text());
            //过滤初始化
        //    parentWin.filters = {};
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
    loadMapData(_module||'unfinish');
}

// 地图的属性
var investOption = {
    tooltip : {'trigger':'item',
        formatter:function(param){
            var _tiphtml="";
            if(param){
                _tiphtml=param[1]+"<br/>";
                _tiphtml=_tiphtml+"专项建设基金总投资："+(formatAmount(param.value||0)||"-");
                // 返回格式化信息
                return _tiphtml;
            }
            else
                return '';
        }},
    dataRange: {
        min: 0,
        max : 53000,
        text:["万"],           // 文本，默认为数值文本
        calculable : true,
        x: 10,
        y2:50,
        color: ZHFX.mapColors,
        textStyle:{
            "color": "#fff"
        }
    },
    series :[{
        name:'专项建设基金总投资',
        type:'map',
        itemStyle:{
            normal:{
                label:{show:true},
                borderColor: '#ffffff',
                borderWidth: 1
            },
            emphasis:{
                color:'#f3f39d',
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
    }]
};
// 地图的属性
var applyOption = {
    tooltip : {'trigger':'item',
        formatter:function(param){
            var _tiphtml="";
            if(param){
                _tiphtml=param[1]+"<br/>";
                _tiphtml=_tiphtml+"本次申请专项建设基金："+(formatAmount(param.value||0)||"-");
                // 返回格式化信息
                return _tiphtml;
            }
            else
                return '';
        }},
    dataRange: {
        min: 0,
        max : 53000,
        text:["万"],           // 文本，默认为数值文本
        calculable : true,
        x: 10,
        y2:50,
        color: ZHFX.mapColors,
        textStyle:{
            "color": "#fff"
        }
    },
    series :[{
        name:'本次申请专项建设基金',
        type:'map',
        itemStyle:{
            normal:{
                label:{show:true},
                borderColor: '#ffffff',
                borderWidth: 1
            },
            emphasis:{
                color:'#f3f39d',
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
    }]
};
// 建议安排
var suggestOption = {
    tooltip : {'trigger':'item',
        formatter:function(param){
            var _tiphtml="";
            if(param){
                _tiphtml=param[1]+"<br/>";
                _tiphtml=_tiphtml+"建议安排资金:"+(formatAmount(param.value||0)||"-");
                // 返回格式化信息
                return _tiphtml;
            }
            else
                return '';
        }},
    dataRange: {
        min: 0,
        max : 53000,
        text:["万"],           // 文本，默认为数值文本
        calculable : true,
        x: 10,
        y2:50,
        color: ZHFX.mapColors,
        textStyle:{
            "color": "#fff"
        }
    },
    series :[{
        name:'建议安排资金',
        type:'map',
        itemStyle:{
            normal:{
                label:{show:true},
                borderColor: '#ffffff',
                borderWidth: 1
            },
            emphasis:{
                color:'#f3f39d',
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
    }]
};
// 投放资金
var putInOption = {
    tooltip : {'trigger':'item',
        formatter:function(param){
            var _tiphtml="";
            if(param){
                _tiphtml=param[1]+"<br/>";
                _tiphtml=_tiphtml+"投放资金:"+(formatAmount(param.value||0)||"-");
                // 返回格式化信息
                return _tiphtml;
            }
            else
                return '';
        }},
    dataRange: {
        min: 0,
        max : 53000,
        text:["万"],           // 文本，默认为数值文本
        calculable : true,
        x: 10,
        y2:50,
        color: ZHFX.mapColors,
        textStyle:{
            "color": "#fff"
        }
    },
    series :[{
        name:'投放资金',
        type:'map',
        itemStyle:{
            normal:{
                label:{show:true},
                borderColor: '#ffffff',
                borderWidth: 1
            },
            emphasis:{
                color:'#f3f39d',
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
    }]
};
// 地图的属性
var numOption = {
    tooltip : {
        'trigger':'item',
        formatter:function(param){
            var _tiphtml="";
            if(param){
                _tiphtml=param[1]+"<br/>";
                _tiphtml=_tiphtml+"专项建设基金项目个数："+(formatCount(param.value||0)||"-");
                // 返回格式化信息
                return _tiphtml;
            }
            else
                return '';
        }},
    dataRange: {
        min: 0,
        max : 53000,
        text:['个'],           // 文本，默认为数值文本
        calculable : true,
        x: 10,
        y2:50,
        color: ZHFX.mapColors,
        textStyle:{
            "color": "#fff"
        }
    },
    series :[ {
        name:'资金需求',
        type:'map',
        itemStyle:{
            normal:{
                label:{show:true},
                borderColor: '#ffffff',
                borderWidth: 1
            },
            emphasis:{
                color:'#f3f39d',
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
    } ]
};

//项目个数
var numChart = echarts.init(document.getElementById('numChart'));
//总投资
var investChart = echarts.init(document.getElementById('investChart'));
//本次申请专项建设基金
var applyChart = echarts.init(document.getElementById('applyChart'));
//建议安排资金
var suggestChart = echarts.init(document.getElementById('suggestChart'));
//投放资金
var putInChart = echarts.init(document.getElementById('putInChart'));



// 初始化清空对那个
function initClearObj() {
    numOption.options=[];
    investOption.options=[];
    applyOption.options=[];
    suggestOption.options=[];
    putInOption.options=[];
}
var _module = "";
var _mapViewType = null;
/**
 *  加载数据
 *  module: 查询的模块
 *  type:查询的类别   num 项目个数、总投资 :invest
 */
function  loadMapData(module) {
    _module=module;
    // 过滤数据
    var params = parentWin.filters;
    // 默认展现未开工
    numChart.showLoading({
        text: '正在努力的加载数据中...'
    });
    // 加载信息
    $.ajax({
        type: "POST",
        url: _fundsUrl, //总览的视图
        data : params, //默认展现已开工计划
        dataType : 'json',
        success: function(data){
            initClearObj();
            // 项目个数
            var numMaps = [];
            //总投资
            var investMaps = [];
            // 本次申请专项
            var applyMaps=[];
            // 建议安排
            var suggestMaps=[];
            // 投放资金
            var putInMaps=[];

            //总个数
            var totalCount = Number(0);
            //总投资
            var totalInv = Number(0);
            //本次申请
            var totalApp = Number(0);
            // 建议安排
            var totalSug=Number(0);
            // 投放资金
            var totalPut=Number(0);
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
                    var value = 0,numValue = 0, appVal = 0,sugVal=0,putVal=0;
                    value = parseInt(data[i]["investMon"])||'-';
                    numValue = parseInt(data[i]["cnt"])||'-';
                    appVal = parseInt(data[i]["applyMon"])||'-';
                    sugVal = parseInt(data[i]["suggestMon"])||'-';
                    putVal = parseInt(data[i]["putInMon"])||'-';
                    // 总投资和总个数
                    totalCount += Number(data[i]["cnt"]||0);
                    totalInv += Number(data[i]["investMon"]||0);
                    totalApp += Number(data[i]["applyMon"]||0);
                    totalSug += Number(data[i]["suggestMon"]||0);
                    totalPut += Number(data[i]["putInMon"]||0);
                    // 存放数据
                    numMaps.push({name:provice,value:numValue,itemCode:proviceCode});
                    investMaps.push({name:provice,value:value,itemCode:proviceCode});
                    applyMaps.push({name:provice,value:appVal,itemCode:proviceCode});
                    suggestMaps.push({name:provice,value:sugVal,itemCode:proviceCode});
                    putInMaps.push({name:provice,value:putVal,itemCode:proviceCode});
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
            titleText = _globalTitle + areaText  +  ZHFX.formatFundTitle(_tIdx);;
             $('#titleTextbatchNo').text(titleText);
            $('#totalCount').text(formatCount(totalCount));
            $('#totalInv').text(formatAmount(totalInv));
            $('#totalApp').text(formatAmount(totalApp));
            $('#totalSug').text(formatAmount(totalSug));
            $('#totalPut').text(formatAmount(totalPut));
            // 项目个数
            numOption.dataRange.max = parseInt(ZHFX.getMaxVal(numMaps)+1)||10;
            numOption.series[0].data = numMaps;
            numOption.series[0].mapType = _mapViewType||"china";
            // 总投资
            investOption.dataRange.max = parseInt(ZHFX.getMaxVal(investMaps)+1)||10;
            investOption.series[0].data = investMaps;
            investOption.series[0].mapType = _mapViewType||"china";
            //本次申请专项建设基金
            applyOption.dataRange.max = parseInt(ZHFX.getMaxVal(applyMaps)+1)||10;
            applyOption.series[0].data = applyMaps;
            applyOption.series[0].mapType = _mapViewType||"china";
            //建议安排
            suggestOption.dataRange.max = parseInt(ZHFX.getMaxVal(suggestMaps)+1)||10;
            suggestOption.series[0].data = suggestMaps;
            suggestOption.series[0].mapType = _mapViewType||"china";
            //建议安排
            suggestOption.dataRange.max = parseInt(ZHFX.getMaxVal(suggestMaps)+1)||10;
            suggestOption.series[0].data = suggestMaps;
            suggestOption.series[0].mapType = _mapViewType||"china";
            //投放资金
            putInOption.dataRange.max = parseInt(ZHFX.getMaxVal(putInMaps)+1)||10;
            putInOption.series[0].data = putInMaps;
            putInOption.series[0].mapType = _mapViewType||"china";
            //加载隐藏
            numChart.hideLoading();
            // 渲染对象
            numChart.setOption(numOption,true);
            investChart.setOption(investOption,true);
            applyChart.setOption(applyOption,true);
            suggestChart.setOption(suggestOption,true);
            putInChart.setOption(putInOption,true);
        }
    });
}

// 选中地图,实现下钻
numChart.on(echarts.config.EVENT.MAP_SELECTED,function (param) {
    // 清空过滤
   // parentWin.filters={};
    // 访问到了县级 直接结束
    var code=findCode(numChart.getSeries()[0].data,param.target)
    if(!code){
        return false;
    }
    // 访问到了县级 直接结束
    if(records.length>=3){
        parentWin.filters["params.level"]=records.length-1;
        parentWin.filters["params.filterCode"]=code;
        parentWin.refreshWin();
        window.open(ctx + '/fiveplan/projectFile!list1?code='+ code +'&codeName=projectRegion&type=8', window, winOption);
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
        //显示省中文名称
        $('#provinceSpan').text(param.target);
    } else if (code.substring(4,6) == '00') {
        //显示市
        $('#citySpan').show();
        //显示>
        //$('#markPoint2').show();
        //给市隐藏域赋值
        $('#citySpanValue').val(code);
        //显示市中文名称
        $('#citySpan').text(param.target);
    }
    var len = mapType.length;
    var mt = records[records.length-1]||'china';
    // 已经访问过
    if (isVisited(mt)) {
        // 判断
        if (!isVisited(param.target)) {
            // 存放访问记录
            records.push(param.target);
            recordcodes.push(code);
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
    loadMapData(_module);
    numChart.setOption(numOption, true);
});

/**
 * 总投资
 */
investChart.on(echarts.config.EVENT.MAP_SELECTED,function (param) {
    var code=findCode(investChart.getSeries()[0].data,param.target)
    if(!code){
        return false;
    }
    // 访问到了县级 直接结束
    if(records.length>=3){
        parentWin.filters["params.level"]=records.length-1;
        parentWin.filters["params.filterCode"]=code;
        parentWin.refreshWin();
        window.open('/fiveplan/projectFile!list1?&code='+ code +'&codeName=projectRegion&type=8', window, winOption);
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
    var len = mapType.length;
    var mt = records[records.length-1]||'china';
    // 已经访问过
    if (isVisited(mt)) {
        // 判断
        if (!isVisited(param.target)) {
            // 存放访问记录
            records.push(param.target);
            recordcodes.push(code);
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
    // /存放
    _mapViewType=mt;
    loadMapData(_module);
    investChart.setOption(investOption, true);
});

// 选中地图,实现下钻
applyChart.on(echarts.config.EVENT.MAP_SELECTED,function (param) {
    // 清空过滤
   // parentWin.filters={};
    // 访问到了县级 直接结束
    var code=findCode(applyChart.getSeries()[0].data,param.target)
    if(!code){
        return false;
    }
    // 访问到了县级 直接结束
    if(records.length>=3){
        parentWin.filters["params.level"]=records.length-1;
        parentWin.filters["params.filterCode"]=code;
        parentWin.refreshWin();
        window.open('/fiveplan/projectFile!list1?&code='+ code +'&codeName=projectRegion&type=8', window, winOption);
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
        //显示省中文名称
        $('#provinceSpan').text(param.target);
    } else if (code.substring(4,6) == '00') {
        //显示市
        $('#citySpan').show();
        //显示>
        //$('#markPoint2').show();
        //给市隐藏域赋值
        $('#citySpanValue').val(code);
        //显示市中文名称
        $('#citySpan').text(param.target);
    }
    var len = mapType.length;
    var mt = records[records.length-1]||'china';
    // 已经访问过
    if (isVisited(mt)) {
        // 判断
        if (!isVisited(param.target)) {
            // 存放访问记录
            records.push(param.target);
            recordcodes.push(code);
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
    loadMapData(_module);
    applyChart.setOption(applyOption, true);
});
// 选中地图,实现下钻
suggestChart.on(echarts.config.EVENT.MAP_SELECTED,function (param) {
    // 清空过滤
   // parentWin.filters={};
    // 访问到了县级 直接结束
    var code=findCode(suggestChart.getSeries()[0].data,param.target)
    if(!code){
        return false;
    }
    // 访问到了县级 直接结束
    if(records.length>=3){
        parentWin.filters["params.level"]=records.length-1;
        parentWin.filters["params.filterCode"]=code;
        parentWin.refreshWin();
        window.open('/fiveplan/projectFile!list1?&code='+ code +'&codeName=projectRegion&type=8', window, winOption);
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
        //显示省中文名称
        $('#provinceSpan').text(param.target);
    } else if (code.substring(4,6) == '00') {
        //显示市
        $('#citySpan').show();
        //显示>
        //$('#markPoint2').show();
        //给市隐藏域赋值
        $('#citySpanValue').val(code);
        //显示市中文名称
        $('#citySpan').text(param.target);
    }
    var len = mapType.length;
    var mt = records[records.length-1]||'china';
    // 已经访问过
    if (isVisited(mt)) {
        // 判断
        if (!isVisited(param.target)) {
            // 存放访问记录
            records.push(param.target);
            recordcodes.push(code);
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
    loadMapData(_module);
    suggestChart.setOption(suggestOption, true);
});
//  下达资金
putInChart.on(echarts.config.EVENT.MAP_SELECTED,function (param) {
    // 清空过滤
   // parentWin.filters={};
    // 访问到了县级 直接结束
    var code=findCode(putInChart.getSeries()[0].data,param.target)
    if(!code){
        return false;
    }
    // 访问到了县级 直接结束
    if(records.length>=3){
        parentWin.filters["params.level"]=records.length-1;
        parentWin.filters["params.filterCode"]=code;
        parentWin.refreshWin();
        window.open('/fiveplan/projectFile!list1?&code='+ code +'&codeName=projectRegion&type=8', window, winOption);
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
        //显示省中文名称
        $('#provinceSpan').text(param.target);
    } else if (code.substring(4,6) == '00') {
        //显示市
        $('#citySpan').show();
        //显示>
        //$('#markPoint2').show();
        //给市隐藏域赋值
        $('#citySpanValue').val(code);
        //显示市中文名称
        $('#citySpan').text(param.target);
    }
    var len = mapType.length;
    var mt = records[records.length-1]||'china';
    // 已经访问过
    if (isVisited(mt)) {
        // 判断
        if (!isVisited(param.target)) {
            // 存放访问记录
            records.push(param.target);
            recordcodes.push(code);
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
    loadMapData(_module);
    putInChart.setOption(putInOption, true);
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

function initMapChart(){
    for(var i=0;i<5;i++){
        if(parentWin.win){
            parentWin.win["viewFundsMap"] = window;
        }else{
            parentWin = parentWin.parent;
        }
    }
    // 默认展现中国地图
    _mapViewType="china";
    //默认隐藏省
    $('#provinceSpan').hide();
//默认隐藏市
    $('#citySpan').hide();
    // 装载数据 五年滚动计划 项目金额
    loadMapData("unfinish");
}