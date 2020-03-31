
/**
 * Created by tannc on 2016/7/21.
 */
// 全局命名空间
var ZHFX={};
//设置命名空间
;(function (ZHFX){
    /******************************* 变量区***************************/
    // 系统柱状图的颜色配置
    ZHFX.colors={
        industry:"#ff5781", //黄色 行业
        depart:"#ff6d3e" ,// 橙色 部门
        govInvest:"#FF9999", // 分政府投资方向
        projectType:"#ccff00" , //审核备 审批 核准备案
        projectMaturity:"#FF9966",     // 项目成熟度
        fgwSJ:"#0066cc", //发改委司局
        storeLevel : '#f95919', //行政层级储备
        fundsUse : '#ffc001', //资金使用情况
        spec : '#0000FF', //历次专项建设基金
        specType : '#FF0000' //专项类别
    };
    //地图颜色设置
    ZHFX.mapColors = ['#CD0000','#FF3030','#FF4500','#FF7F50','#FF8C00','#FFA54F','#FFB90F','#FFDEAD','#FFE4E1','#FFEFD5'];
    //地图选中色
    ZHFX.mapSelectColor = '#FFFF00';
    
    //X轴字体样式
    ZHFX.XTextStyle = {
    	color : '#fff',
        fontSize : '16',
        fontFamily : '宋体'
    };
    //Y轴字体样式
    ZHFX.YTextStyle = {
    	color : '#fff',
        fontSize : '16',
        fontFamily : '宋体'
    };
    //悬浮字体样式设置
    ZHFX.topTextStyle = {
    	color : '#fff',
        //fontSize : '12',
        //fontFamily : '宋体'
    };
    //图例字体样式设置
    ZHFX.legendTextStyle = {
    	color : '#fff',
        fontSize : '16',
        fontFamily : '宋体'	
    };
    //饼图悬浮字体样式
    ZHFX.pieTextStyle = {
    	color : '#fff',
        fontSize : '14',
        fontFamily : '宋体'
    };
    //坐标轴的分隔线
    ZHFX.axisLine={
        show:true,
        onZero:true,
        lineStyle:{
            color: '#f00',
            width: 2,
            type: 'solid'
        }
    };
    // toolttip 提示字体大小
    ZHFX.tooltipTextStyle={fontSize:20};

    // 定义窗口大小
    ZHFX.winOption = "height=600,width=1200,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
   // // GIS 地图服务地址
   //  ZHFX.GIS_URL="http://59.255.137.11:5240/iserver/services/map-china400/rest/maps/China_4326";
   //  // GIS 地图数据地址
   //  ZHFX.GIS_DATA="http://59.255.137.11:5240/iserver/services/map-test/rest/maps/mymap";
    /******************* 方法区 ***************************/
    //
    ZHFX.getRootPath=function(){ // 得到当前工程路径
        //获取当前网址，如： http://localhost:8088/test/test.jsp
        var curPath=window.document.location.href;
        //获取主机地址之后的目录，如： test/test.jsp
        var pathName=window.document.location.pathname;
        var pos=curPath.indexOf(pathName);
        //获取主机地址，如： http://localhost:8088
        var localhostPaht=curPath.substring(0,pos);
        //获取带"/"的项目名，如：/test
        var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
        return(localhostPaht+projectName);
    }
    ZHFX.toUrl=function(url){  //跳转到指定的页面
        // 跳转到其他的页面上
        window.location.href=this.getRootPath()+url;
    }
    ZHFX.toIframeUrl=function(url){
        top.frames["FGW_Iframe"].location.href=this.getRootPath()+url;
    }
    ZHFX.toIframeSrc=function(url){
        top.frames["FGW_Iframe"].src=this.getRootPath()+url;
    }
    ZHFX.toTopUrl=function(url){  //跳转到顶级页面
        top.window.location.href=this.getRootPath()+url;
    }
    ZHFX.logout=function () {   // 退出程序
        var self=this;
        $.post(this.getRootPath()+'/login/login!logout', function(data) {
            // 跳转到首页
            top.window.location.href=self.getRootPath()+"/index.html";
        }, 'text');
    }
    ZHFX.fs = function(str) {
        if(!str){
            return "";
        }
        // 格式化代码
        /**
         *
         * 增加formatString功能
         *
         * 使用方法：ZHFX.fs('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
         *
         * @returns 格式化后的字符串
         */
        for ( var i = 0; i < arguments.length - 1; i++) {
            // 全局替换忽略大小写
            var reg=new RegExp("\\{"+ i+"\\}",'gi');
            str = str.replace(reg,arguments[i + 1]);
        }
        return str;
    }
    ZHFX.fsAll=function (str) {
        for ( var i = 0; i < arguments.length - 1; i++) {
            str = str.replace(/{i}/g,arguments[i + 1]);
        }
        return str;
    }
    ZHFX.getTimestamp=function(){
        // 时间加随机数
        return (new Date()).getTime()+""+Math.random()*100000;
    }
    ZHFX.getMaxVal=function(params){
        // params 是数组 数组里面的对象是{ name :'北京',value:10000} 的到值域的最大值
        var  maxVal=10;
        if(params){
            for(var i=0,len=params.length;i<len;i++){

                var val=params[i].value;
                var name=params[i].name;
                // 如果等于选中省市的话就直接跳过
                if(parentWin&&parentWin.filters&&parentWin.filters["params.filterCode"]==params[i].itemCode){
                    continue;
                }
                // 跨省区不参与
                if(val&&name!="跨省区"&&name!="市辖区"&&name!="县"){
                    if(parseInt(val)>maxVal){
                        // 设置最大值
                        maxVal=parseInt(val);
                    }
                }
            }
        }
        return parseInt(maxVal);
    }
    ZHFX.getLastMonth=function(d){
        var lm="";
        var m=d.substring(5);
        var y=d.substring(0,4);
        // 当月
        if(m==1){
            lm=(y-1)+"-12"
        }
        else{
            var _m=(m-1)<10?"0"+(m-1):(m-1)
            lm=y+"-"+_m;
        }
        return lm;
    }
    ZHFX.getRateArr=function(data) {
        var sum = 0;
        var _tempData=[];
        if (data) {
            for (var _si = 0,_len = data.length; _si <_len;_si++ ){
                // 合计累计
                if(data[_si]) {
                    sum += data[_si];
                }
            }
            for (var _sj = 0,_lenj = data.length; _sj <_lenj;_sj++ ){
                var val=((data[_sj]||0)/sum *100);
                // 合计累计
                _tempData.push(val.toFixed(2) );
            }
            return _tempData;

        }
        else{
            return [];
        }

    }
    ZHFX.formatTitle=function(tIdx){
        switch(tIdx){
            case "0" : return "项目分布";
            case "1" : return "投资分布";
            case "2" : return "未来三年资金需求";
            case "3" : return "本次下达投资分布";
            case "4" : return "申请2017年资金";
            case "5" : return "2017年需求资金分布";
            case "6" : return "2018年需求资金分布";
            case "7" : return "2019年需求资金分布";
        }
        return "";
    }
    ZHFX.formatDispatchTitle=function(tIdx){
        switch(tIdx){
            case "0" : return "调度项目分布";
            case "1" : return "调度到位资金分布";
            case "2" : return "调度支付资金分布";
            case "3" : return "调度完成资金分布";
            case "4" : return "调度项目投资分布";
            case "5" : return "调度项目开工分布";
            case "6" : return "调度项目完工分布";
        }
        return "";
    }
    ZHFX.setDispatchTitle=function(tIdx){
        _tIdx=tIdx;
        var  titleText =$("#titleText").text();
        var idxof=titleText.indexOf("调度");
        $("#titleText").text('' + _areaInfo + this.formatDispatchTitle(tIdx));
    }
    // 设置专项建设基金标题
    ZHFX.formatFundTitle=function(tIdx){
        switch(tIdx){
            case "0" : return "项目分布";
            case "2" : return "投资分布";
            case "4" : return "本次申请专项建设基金分布";
            case "6" : return "建议安排资金分布";
            case "8" : return "投放资金分布";
        }
        return "";
    }
    ZHFX.setFundTitle=function(tIdx){
        _tIdx=tIdx;
        var  titleText =$("#titleTextbatchNo").text();
        var _tt=titleText.replace("项目分布",this.formatFundTitle(tIdx))
            .replace("投资分布",this.formatFundTitle(tIdx))
            .replace("本次申请专项建设基金分布",this.formatFundTitle(tIdx))
            .replace("建议安排资金分布",this.formatFundTitle(tIdx))
            .replace("投放资金分布",this.formatFundTitle(tIdx));

        $("#titleTextbatchNo").text(_tt);
    }

   /***
    *   打开word 文档
    * @orderBy 
    * @param  
    * @return 
    * @author tannc
    * @Date 2016/10/29 18:35
    **/
   ZHFX.openmydoc=function(path) {
       var doc = new ActiveXObject("Word.Application");
       doc.visible = true;
       doc.Documents.Open(path);
   }
   
   /***
    *  收集参数
    * @orderBy 
    * @param  
    * @return 
    * @author tannc
    * @Date 2016/10/30 11:04
    **/
   ZHFX.filters={};
   // 收集Flash页面上的参数
   ZHFX.collectParams=function(value,name){
	   //获取sessionId
	   var sessionId = $("#sessionId").val();
	   ZHFX.setCookie("sessionId",encodeURI(sessionId));
       // 页面过滤状态
       if(name=="filterStatus"||name=="FundLevel"||name=="Bank"){
    	   switch(value){
               case "未编制三年滚动计划项目":value="unfinish";break
               case "各级编制三年滚动计划":value="rollplan";break
               case "上报国家三年滚动计划":value="report";break
               case "国开行":value="A00001";break
               case "农发行":value="A00002";break
    	   }
           // 设置客户端Cookie
          ZHFX.setCookie(name,encodeURI(value));
       }
       if(name) {
    	   if(value){
    		   value = value.replace("顶层>>","");
    		   value = value.replace("顶层","");
    		   value = encodeURI(encodeURI(value));
    	   }
           ZHFX.filters["filters."+name] = value;
           ZHFX.setCookie(name,value);
       }
   }
   // 收集当前页面
        ZHFX.collectPage=function(){

    }
    //清除所有参数
    ZHFX.clearAll=function(){
        ZHFX.filters={};
    }
   // 清除指定的参数
    ZHFX.clear=function(){
        if(arguments){
            for(var i=0,len=arguments.length;i<len;i++){
                delete ZHFX.filters[arguments[i]];
            }
        }
    }
    /**
     * 展现更多
     * url: 访问数据的URL
     * cptName 打开页面的URL
     */
    ZHFX.showMore=function(url,cptName,moduleCode) {
    	loadSessionId();
    	var sessionId = $("#sessionId").val();
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            dataType: 'json',
            url: this.getRootPath() + url + "&sessionId="+$("#sessionId").val(),
            data: ZHFX.filters,
            error: function () {//请求失败处理函数
                $.dialog.alert("数据获取失败！", "warning");
            },
            success: function (data) { //请求成功后处理函数。
                // 对于多个数据集的，uuid用连续的数字区分
                var requestParam = '';
                requestParam = requestParam + "&uuid1=" + (data.uuid1)+"&moduleCode="+(moduleCode||"");
                requestParam = requestParam + "&reportRequestUrl=" + (data.reportRequestUrl);
                var _winUrl = cjkEncode(ZHFX.getRootPath() + "/ReportServer?reportlet=loadData/" + cptName + ".cpt&__bypagesize__=true" + requestParam);
                var winOption = ZHFX.winOption;
                window.open(_winUrl, window, winOption);
            }
        });
    }

    /**
     * 展现更多
     * cptName: cpt 的名称
     * paramsNameStr  参数名称字符串，多个参数名称用“|”分隔
     */
    ZHFX.showNenuMore=function(cptName,paramsNameStr) {
        if(arguments.length==2) {
			
			var _paramsName = paramsNameStr.split("|");
			var  _filters=ZHFX.filters;
            var _params="";
            if(_filters){
                var  _filtersArr=[];
                for(var att in _paramsName){
                    if(typeof(_filters["filters."+_paramsName[att]])!="undefined")
						_filtersArr.push(_paramsName[att]+"="+_filters["filters."+_paramsName[att]]);
                }
                // 数组
                if(_filtersArr.length>0){
                    _params="&"+_filtersArr.join("&");
                }
            }
            var _winUrl = cjkEncode(ZHFX.getRootPath() + "/ReportServer?reportlet=tzxmzh/" + cptName + ".cpt&__bypagesize__=true"+_params );
			var winOption = ZHFX.winOption;
            window.open(_winUrl, window, winOption);
        }
        // 为了适配之前已经做错误的功能
        else if(arguments.length==1){
            var  _filters=ZHFX.filters;
            var _params="";
            if(_filters){
                var  _filtersArr=[];
                for(var att in _filters){
                    _filtersArr.push(att.substr(8)+"="+_filters[att]);
                }
                // 数组
                if(_filtersArr.length>0){
                    _params="&"+_filtersArr.join("&");
                }
            }
            var _winUrl = cjkEncode(ZHFX.getRootPath() + "/ReportServer?reportlet=tzxmzh/" + cptName + ".cpt&__bypagesize__=true"+_params );
			var winOption = ZHFX.winOption;
            window.open(_winUrl, window, winOption);
        }
    }
	/**
     * 展现投资平台链接
     * neuUrl: 除IP端口应用名称外的部分链接地址
     */
    ZHFX.showNenuLink=function(neuUrl) {
		var _NenuRootPath = "https://www.tzxm.gov.cn:8081/tzxmspweb/tzxmweb/";
		//var _NenuRootPath = "http://localhost:8086/tzxmspall/tzxmapp/";
		var _winUrl = _NenuRootPath + neuUrl;
		var winOption = ZHFX.winOption;
        window.open(_winUrl, window, winOption);
	}

        /**
         *  得到Cookie
         * @param sName
         * @returns {null}
         */
       ZHFX.getCookie=function (sName)
        {
            var aCookie = document.cookie.split(";");
            for (var i=0; i < aCookie.length; i++)
            {
                var aCrumb = aCookie[i].split("=");
                if (sName == aCrumb[0].replace(/(^\s*)|(\s*$)/g, ""))
                {
                    return unescape(aCrumb[1]);
                }
            }

            // a cookie with the requested name does not exist
            return null;

        }
        /**
         * 设置Cookie
         * @param sName
         * @param sValue
         */
         ZHFX.setCookie=function(sName, sValue)
        {
            var date = new Date();
            // 有效期3小时
            date.setTime(date.getTime() + Number(3) * 3600 * 1000);
            document.cookie = sName + "=" + sValue + "; path=/;expires = " + date.toGMTString();

        }
        //清除cookie
        ZHFX.clearCookie=function(name) {
            ZHFX.setCookie(name, "", -1);
        }


    /**
     *  展现地区列表
     * @param areaPath
     *     地区路径
     * @param moduleName
     *      模块名称
     *        fiveplan-五年储备项目
     *         rollplan-三年滚动计划
     *         funds-专项建设基金
     *         budgetreport --中央预算内申报
     *         budgetissue  中央预算内下达
     *         budgetdiapatch  中央预算内调度
     */
    ZHFX.showListMore=function(areaPath,moduleName){
        // 选中模块  selected  过滤模块
        // 地区路径 内部页面
        var param="?areaPath="+areaPath+"&innerPage=1&moduleName="+moduleName+"&selected="+ZHFX.filters["selected"];
        // 地图弹出的列表
        window.open(this.getRootPath()+"/projectFile/projectFile!list"+param,window,ZHFX.winOption);
    }
    /**
     *   轮播图片
     * @param imgUrl  图片链接
     */
   ZHFX.showImgList=function(imgUrl,showImgCode){
       window.open(this.getRootPath()+'/projectFile/slicebox?showImgCode='+showImgCode+'&imgUrl='+imgUrl,'_blank','height=700,width=1200,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0')
   }
    /**
     *  展现项目列表
     */
    ZHFX.showNvDialog=function(code){
        window.open(this.getRootPath()+'/projectFile/projectFile!listNv?filters.proStageCode='+code+'&innerPage=1&filters.BuildPlaceCode='+ZHFX.filters["filters.BuildPlaceCode"]||"",'_blank',"height=600,width=1300;menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=0");
    }
    /**
     *  刷新预警预测
     */
    ZHFX.refreshIframe=function(code,dp){
        if(dp){
            dp = dp.replace("顶层>>","");
            dp = encodeURI(encodeURI(dp));
        }
        $("#contentViewId").attr("src",this.getRootPath()+'/projectFile/projectFile!listWarn?warnCode='+code+'&innerPage=1&filters.BuildPlaceCode='+dp||"");
      }
   /**
    *  打开窗口
    */
   ZHFX.showWindow=function(url){
       window.open(this.getRootPath()+url,'_blank','height=600,width=1200,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0')
   }
    ZHFX.showOuterWindow=function(url){
        window.open(url,'_blank','height=600,width=1200,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0')
    }

    /**
     *
     * @requires jQuery
     *
     * 将form表单元素的值序列化成对象
     *
     * @returns object
     */
    ZHFX.serializeObject = function(form) {
        var o = {};
        $.each(form.serializeArray(), function(index) {
            if (o[this['name']]) {
                o[this['name']] = o[this['name']] + "," + this['value'];
            } else {
                o[this['name']] = this['value'];
            }
        });
        return o;
    };
    // GIS 服务器地址
    ZHFX.SERVER_IP="http://59.255.137.13:8090";
})(ZHFX);


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
};


// 设置地图的映射名字
var nameMap={
    "北京":"北京市",
    "天津":"天津市",
    "河北":"河北省",
    "山西":"山西省",
    "内蒙古":"内蒙古",
    "吉林":"吉林省",
    "黑龙江":"黑龙江省",
    "辽宁":"辽宁省",
    "安徽":"安徽省",
    "上海":"上海市",
    "江苏":"江苏省",
    "山东":"山东省",
    "浙江":"浙江省",
    "江西":"江西省",
    "河南":"河南省",
    "福建":"福建省",
    "湖北":"湖北省",
    "广东":"广东省",
    "湖南":"湖南省",
    "海南":"海南省",
    "重庆":"重庆市",
    "四川":"四川省",
    "广西":"广西",
    "云南":"云南省",
    "西藏":"西藏",
    "青海":"青海省",
    "宁夏":"宁夏",
    "新疆":"新疆",
    "陕西":"陕西省",
    "贵州":"贵州省",
    "台湾":"台湾省",
    "香港":"香港",
    "澳门":"澳门",
    "甘肃":"甘肃省"
};

/**
 * 格式化金额数字
 */
function formatAmount(value) {
	if (value) {
		if (Number(value) >= Number(100000000)) {
			return Number(Number(value)/Number(100000000)).toFixed(2) + '万亿元';
		} else if (Number(value) >= Number(10000) && Number(value) < Number(100000000)) {
			return Number(Number(value)/Number(10000)).toFixed(2) + '亿元';
		} else if (Number(value) > Number(0)) {
			return Number(value).toFixed(2) + '万元';
		} else {
			return Number(0) + '元';
		}
	} else {
		return Number(0) + '元';
	}
}
/**
 * 格式化金额数字
 */
function formatFund(value) {
	if (value) {
		if (Number(value) >= Number(100000000)) {
			return Number(Number(value)/Number(100000000)).toFixed(2);
		} else if (Number(value) >= Number(10000) && Number(value) < Number(100000000)) {
			return Number(Number(value)/Number(10000)).toFixed(2);
		} else if (Number(value) > Number(0)) {
			return Number(value).toFixed(2);
		} else {
			return Number(0);
		}
	} else {
		return Number(0);
	}
}

/**
 * 格式化项目个数
 */
function formatCount(value) {
    if (value) {
        if (Number(value) >= Number(10000)) {
            return Number(Number(value)/Number(10000)) + '万个';
        } else if (Number(value) > Number(0)) {
            return parseInt(value) + '个';
        } else {
            return Number(0) + '个';
        }
    } else {
        return Number(0) + '个';
    }
}

/**
 * 格式化项目个数
 */
function formatCnt(value) {
    if (value) {
        if (Number(value) >= Number(10000)) {
            return Number(Number(value)/Number(10000)).toFixed(2);
        } else if (Number(value) > Number(0)) {
            return parseInt(value) + '个';
        } else {
            return Number(0);
        }
    } else {
        return Number(0);
    }
}

/***
 *    登录人次
 * @orderBy 
 * @param  
 * @return 
 * @author tannc
 * @Date 2016/9/13 22:51
 **/
function formatTimes(value) {
    if (value) {
        if (Number(value) >= Number(10000)) {
            return Number(Number(value)/Number(10000)) + '万人次';
        } else if (Number(value) > Number(0)) {
            return parseInt(value) + '人次';
        } else {
            return Number(0) + '人次';
        }
    } else {
        return Number(0) + '人次';
    }
}
/***
 *
 * @orderBy
 * @param   args
 *      参数
 * @return
 * @author tannc
 * @Date 2016/9/12 20:05
 **/

function showZhfxDialog(args){
    //showModalDialog("${ctx}/show/showDetails",args,"dialogWidth=400px;dialogHeight=600px");
    if(navigator.userAgent.indexOf("Chrome") >0 ){
        var winOption = "height=600,width=800,top=50,left=350,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
        return  window.open("/show/showDetails",window, winOption);
    }
    else{
        return window.showModalDialog(url, window, 'dialogHeight: ' + height + 'px; dialogWidth: ' + width + 'px;edge: Raised; center: Yes; help: Yes;scroll:no; resizable: no; status: no;resizable:yes');
    }
}

/**
 * 加载中央预算内申报更多数据报表
 * @param params 参数
 */
function loadMoreDataBudgetReport(params) {
	$.ajax({
		async : false,
		cache:false,
		type: 'POST',
		dataType : 'json',
		url: url,
		data : params,
		error: function () {//请求失败处理函数
			$.dialog.alert("数据获取失败！","warning");
		},
		success:function(data){ //请求成功后处理函数。
			// 对于多个数据集的，uuid用连续的数字区分
			var requestParam;
			requestParam = requestParam + "&uuid1="+(data.uuid1);
			requestParam = requestParam + "&reportRequestUrl="+(data.reportRequestUrl);
			var _winUrl=cjkEncode("/report/ReportServer?reportlet=loadMoreData/budgetReport.cpt&__bypagesize__=true"+requestParam);
			var winOption = "height=600,width=800,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
		    window.open(_winUrl,window, winOption);
		}
	});
}

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
/**
 *  切换tab 页 0-项目个数 1是总投资
 */
function switchTab(indx){
  switch(indx){
      case "0"://项目个数
          _tIdx=indx;
          orderby="orderbycnt"; 
          // 项目分布
        var  titleText =$("#titleText").text();
          if(titleText.lastIndexOf("投资分布")>-1) {
              var _tt = titleText.replace("投资分布", "项目分布");
              $("#titleText").text(_tt);
              return 0;
          }
          if(titleText.lastIndexOf("未来三年资金需求")>-1) {
              var _tt = titleText.replace("未来三年资金需求", "项目分布");
              $("#titleText").text(_tt);
              return 0;
          }
          if(titleText.lastIndexOf("申请2017年资金")>-1) {
              var _tt = titleText.replace("申请2017年资金", "项目分布");
              $("#titleText").text(_tt);
              return 0;
          }
          case "1"://投资分布
              _tIdx=indx;
              orderby="orderbymon"; 
              var  titleText =$("#titleText").text();
              if(titleText.lastIndexOf("项目分布")>-1) {
                  var _tt = titleText.replace("项目分布", "投资分布");
                  $("#titleText").text(_tt);
                  return 0;
              }
              if(titleText.lastIndexOf("未来三年资金需求")>-1) {
                  var _tt = titleText.replace("未来三年资金需求", "投资分布");
                  $("#titleText").text(_tt);
                  return 0;
              }
              if(titleText.lastIndexOf("申请2017年资金")>-1) {
                  var _tt = titleText.replace("申请2017年资金", "投资分布");
                  $("#titleText").text(_tt);
                  return 0;
              }
           return 0;
          case "2"://投资分布
              _tIdx=indx;
              var  titleText =$("#titleText").text();
              if(titleText.lastIndexOf("项目分布")>-1) {
                  var _tt = titleText.replace("项目分布", "未来三年资金需求");
                  $("#titleText").text(_tt);
                  return 0;
              }
              if(titleText.lastIndexOf("投资分布")>-1) {
                  var _tt = titleText.replace("投资分布", "未来三年资金需求");
                  $("#titleText").text(_tt);
                  return 0;
              }
           return 0;
          case "4"://投资分布
              _tIdx=indx;
              orderby="orderbyapply";
              var  titleText =$("#titleText").text();
              if(titleText.lastIndexOf("项目分布")>-1) {
                  var _tt = titleText.replace("项目分布", "申请2017年资金");
                  $("#titleText").text(_tt);
                  return 0;
              }
              if(titleText.lastIndexOf("投资分布")>-1) {
                  var _tt = titleText.replace("投资分布", "申请2017年资金");
                  $("#titleText").text(_tt);
                  return 0;
              }
           return 0;
          case "5"://投资分布
              _tIdx=indx;
              orderby="orderbyapply";
              $("#titleText").text(_area + '2017年需求资金分布');
           return 0;
          case "6"://投资分布
              _tIdx=indx;
              orderby="orderbyapply";
              $("#titleText").text(_area + '2018年需求资金分布');
              return 0;
          case "7"://投资分布
              _tIdx=indx;
              orderby="orderbyapply";
              $("#titleText").text(_area + '2019年需求资金分布');
              return 0;
  }
}

/**
 *  切换tab 下达页 0-项目个数 1是总投资
 */
function switchISSUETab(indx){
    switch(indx){
        case "0"://项目个数
            _tIdx=indx;
            orderby="orderbycnt";
            // 项目分布
            var  titleText =$("#titleText").text();
            if(titleText.lastIndexOf("项目分布")==-1) {
                var _tt = titleText.replace("下达投资分布", "项目分布");
                _tt=_tt.replace("投资分布","项目分布");
                $("#titleText").text(_tt);
                return 0;
            }
        case "1"://投资分布
            _tIdx=indx;
            orderby="investmon";
            var  titleText =$("#titleText").text();
            if(titleText.lastIndexOf("投资分布")>-1&&titleText.lastIndexOf("下达投资分布")>-1) {
                var _tt = titleText.replace("下达投资分布", "投资分布");
                _tt=_tt.replace("项目分布","投资分布");
                $("#titleText").text(_tt);
                return 0;
            }
            else if(titleText.lastIndexOf("项目分布")>-1){
                var _tt = titleText.replace("下达投资分布", "投资分布");
                _tt=_tt.replace("项目分布","投资分布");
                $("#titleText").text(_tt);
                return 0;
            }
            return 0;
        case "3"://下达投资分布
            _tIdx=indx;
            orderby="orderbyapply";
            var  titleText =$("#titleText").text();
            if(titleText.lastIndexOf("投资分布")>-1&&titleText.lastIndexOf("下达投资分布")==-1) {
                var _tt = titleText.replace("项目分布", "下达投资分布");
                _tt=_tt.replace("投资分布","下达投资分布");
                $("#titleText").text(_tt);
                return 0;
            }
            else if(titleText.lastIndexOf("项目分布")>-1){
                var _tt = titleText.replace("项目分布", "下达投资分布");
              //  _tt=_tt.replace("投资分布","下达投资分布");
                $("#titleText").text(_tt);
                return 0;
            }
            return 0;
    }
}

/**
 * 设置iframe的src，根据菜单表查出
 */
function setIframeSrc(type, id) {
	$.ajax({
        //async : false,
        //cache : false,
        type : 'POST',
        dataType : 'text',
        url: ctx + '/menu/menu!getSrcByName',
        data : {type : type},
        success:function(data){ //请求成功后处理函数。
        	if (data) {
        		$('#' + id).attr('src', data);
        	}
        },
        error: function () {//请求失败处理函数
            alert("数据获取失败！");
        }
    });

	
}

