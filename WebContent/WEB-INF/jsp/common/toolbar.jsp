<%@ page language="java" pageEncoding="UTF-8" 
	contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	// 初始化菜单
	$(function () {
		initMenuList()
	});
	/**
	 * 初始化菜单
	 */
	function initMenuList() {
		$.ajax({
	        type : 'POST',
	        dataType : 'text',
			data:{typeModule:'${param.typeModule}'},// 过滤条件
	        url:  '${ctx}/menu/menu!initMenuList',
	        success:function(data){ //请求成功后处理函数。
	        	if (data) {
	        		var map = eval('(' + data + ')');
	        		$('#base-menu').html(map.baseInfo);
//	        		$('#zdyw-menu').html(map.impInfo)
						// 得到页面类型
						var type='${param.type}';
						if(type){
							var _obj= $(".listMenu").find("a[_typeflag='"+type+"']");
							// 加载页面
							loadFlash(_obj[0],_obj.attr("_url"))
						}
	        	}
	        },
	        error: function () {//请求失败处理函数
	            alert("数据获取失败！");
	        }
	    });
	}

    //刷新页面
	function loadFlash(obj,url){
		//清空sessionId
    	$("#sessionId").val("");
    	 // 清空内容
        $("#paramsForm").empty();
        var  _type=$(obj).attr("_typeflag");
        //判断当前按钮是否有查询模块
        var _id="#"+_type+"Template";
        if($(_id).size()>0) {
            // 有查询模块显示查询按钮
            $("#btnSearch").show();
            // 添加模块
            $("#paramsForm").html($(_id).html());
            $.parser.parse( $("#paramsForm"));
        }
        else{
            //没有查询模块隐藏查询，隐藏按钮
            $("#btnSearch").hide();
        }
		// 直接打开大屏页面
		if(url&& url.indexOf("#rootpathdp#")>-1){
			url=url.replace("#rootpathdp#","${ctx}");
			top.window.open(url);
			return false;
		}
		//清空内容
		container.innerHTML="";
		// 设置标题
		$("#nameText").text($(obj).attr("_menutitle"));
		$(".loading").show();
		// 将 gShow.html 替换成 gShow.swf
		if(url&& url.indexOf("gShow.html")>-1){
             url=url.replace("gShow.html","gShow.swf");
		}
		$(".printCls").hide();
		// 将 #roopath# 替换成 http://localhost:8080/BI
		if(url&& url.indexOf("#rootpath#")>-1){
			url=url.replace("#rootpath#","${ctx}");
		}
		// 加载页面
		if(url&& url.indexOf("gShow.swf")>-1){
			  var params=ZHFX.serializeObject($("#paramsForm"));
				 var  paramArrs=[];
				// 参数
				 for( var p in params){
						if($.trim(params[p])) {
							paramArrs.push(p.replace("reportParamsMap.","") + "=" + (params[p]));
						}
						else {
							paramArrs.push(p.replace("reportParamsMap.","") + "=null" );
						}
				 }
				   url=url+"&"+paramArrs.join("&");
				var warnTypes="not_approved,start_warn,money_warn,bid_warn,start_noReport,start_error,record_error,givenCode_noManage,checked_noManage";
				//判断是否预警模块
				if(warnTypes.indexOf(_type)>-1){
				   var	warnUrl="${ctx}/projectFile/projectFile!listWarn?innerPage=1";
					switch (_type){
						case 'not_approved': warnUrl=warnUrl+"&warnCode=0"; break;//未批先建阶段
						case 'start_warn': warnUrl=warnUrl+"&warnCode=4"; break;//开工建设阶段预警
						case 'money_warn': warnUrl=warnUrl+"&warnCode=6"; break;//资金执行延期预警
						case 'bid_warn': warnUrl=warnUrl+"&warnCode=2"; break;//招投标预警
						case 'start_noReport': warnUrl=warnUrl+"&warnCode=3"; break;//开工未报告
						case 'start_error': warnUrl=warnUrl+"&warnCode=5"; break;//异常开工
						case 'record_error': warnUrl=warnUrl+"&warnCode=7"; break;//备案异常
						case 'givenCode_noManage': warnUrl=warnUrl+"&warnCode=8"; break;//已赋码未办理
						case 'checked_noManage': warnUrl=warnUrl+"&warnCode=9"; break;//核准后未办理
					}
					container.innerHTML=ZHFX.fs($("#contentFlash").html(),url+"&bgalpha=0&file=gShow.swf&busername=admin&autoLogin=t&showtb=false&t="+Date.now(),450)+ZHFX.fs($("#contentIframe").html(),warnUrl,650);
				}
				// 用户活跃情况
				else if(_type=="user_info"){
					var _userUrl="${ctx}/activeUserInfo.html";
					container.innerHTML=ZHFX.fs($("#contentIframe").html(),_userUrl,560)+ZHFX.fs($("#contentFlash").html(),url+"&bgalpha=0&file=gShow.swf&busername=admin&autoLogin=t&showtb=false&t="+Date.now(),300);
				}
				else {
					// 加载flash
					container.innerHTML=ZHFX.fs($("#contentFlash").html(),url+"&bgalpha=0&file=gShow.swf&busername=admin&autoLogin=t&showtb=false&t="+Date.now(),"100%");
				}
				$(".printCls").show();
		}
		else{
			// 加载Iframe页面中
			container.innerHTML=ZHFX.fs($("#contentIframe").html(),url,"100%");
		}
            //渲染样式
            $('.listMenu li').removeClass('active');
            $(obj).closest("li").addClass('active');
		    $(obj).closest(".parentCls").addClass('active');
            var _menu=$(obj).closest(".menuCls");
            _menu.show();
            _menu.siblings(".menuCls").hide();
            var id=_menu.prop("id");
            // 基础业务被选中
//            if(id.indexOf("base")>-1){
//                $("#base").addClass("active");
//                $("#zdyw").removeClass("active");
//            }
//            else{
//                $("#zdyw").addClass("active");
//                $("#base").removeClass("active");
//            }
            $("#container").ready(function(){ $(".loading").hide(); });
            // 清空内容
            $("#paramsForm").empty();
            var  _type=$(obj).attr("_typeflag");
            //判断当前按钮是否有查询模块
            var _id="#"+_type+"Template";
            if($(_id).size()>0) {
                // 有查询模块显示查询按钮
                $("#btnSearch").show();
                // 添加模块
                $("#paramsForm").html($(_id).html());
                $.parser.parse( $("#paramsForm"));
            }
            else{
                //没有查询模块隐藏查询，隐藏按钮
                $("#btnSearch").hide();
            }
            // 清空数据
            ZHFX.clearAll();
            // 清除过滤状态
            ZHFX.clearCookie("filterStatus");
            ZHFX.clearCookie("FundLevel");
            ZHFX.clearCookie("Bank");
	}

</script>
<div id="toolbar">
	<span><a href="#" title="返回上一页" class="btn-back" onclick="history.go(-1)"></a></span>
	<span><a href="#" title="主页" class="btn-index" onclick="goHomePage();"></a></span>
	<span class="MenuList"><a href="#" title="菜单" class="btn-list"></a>
		<ul class="listMenu">
			<%--<h6>--%>
		<%--&lt;%&ndash;	//	<a href="javascript:void(0)" id="base" class="active">基础业务</a>&ndash;%&gt;--%>
				<%--&lt;%&ndash;<a href="javascript:void(0)" id="zdyw">重点业务</a>&ndash;%&gt;--%>
			<%--</h6>--%>
			<div id="base-menu" class="menuCls">
				
			</div>
			<div id="zdyw-menu" class="menuCls" style="display:none;">

			</div>
		</ul>
	</span>
    <span id="btnSearchSpan"><a href="#" title="搜索" class="btn-search" id="btnSearch"></a>
		<ul class="listSearch">
			<div>
			   <h6>搜索条件</h6>
                <form action="" id="paramsForm" name="paramsForm">

                </form>
	           <div> <button  style="width:49%"  onclick="paramsFormReset();">重置</button> <button  style="width: 50%" onclick="searchFilters()">搜索</button> </div>
	        </div>
		</ul>
	</span>
	<!-- 	<span><a href="#" title="导出WORD" class="btn-word"></a></span> -->
	<span class="printCls"><a href="#" title="导出PDF" class="btn-pdf"  onclick="exportPDF()"></a></span>
	<span class="printCls"><a href="#" title="打印" class="btn-print" onclick="toPrint()"></a></span>
	<!-- sessionId用来存储页面的检索条件 -->
	<input type="hidden" id="sessionId" name="sessionId" value="1"/>
</div>

<script type="text/javascript">
	$(function(){
		// 排查检索图标
		$("#toolbar span:not('#btnSearchSpan')").hover(function(){
			 $(this).find("ul").css("transform","translateX(-100%)");
		},function(){
			// 查询框
			if($('#btnSearchSpan').find("#btnSearch[class*='close']").size()==1){
				$("#btnSearchSpan").find("ul").css("transform","translateX(100%)");
				$("#btnSearch").removeClass('close');
			}
				$(this).find("ul").css("transform","translateX(100%)");
			});
         // 查询按钮
		$("#btnSearch").click(function(){
			if($(this).closest("#btnSearchSpan").find("#btnSearch[class*='close']").size()==0){
				$(this).closest("#btnSearchSpan").find("ul").css("transform","translateX(-100%)");
				// 添加关闭图标
				$(this).addClass('close');
			}
			else{
				$(this).closest("#btnSearchSpan").find("ul").css("transform","translateX(100%)");
				$(this).removeClass('close');
			}});
	})
	/**
	 * 返回主页操作
	 */
	function goHomePage() {
		ZHFX.toUrl('/common/common!overview');
//		//选中基础业务模块
//		if ($('#base').hasClass('active')) {
//			ZHFX.toUrl('/common/common!overview');
//		//选中重点业务模块
//		} else if ($('#zdyw').hasClass('active')) {
//			ZHFX.toUrl('/common/common!majorView');
//		}
	}
	/***
	 *   加载页面的时候清除Cookies   filterStatus
	 * @orderBy 
	 * @param  
	 * @return 
	 * @author tannc
	 * @Date 2016/11/2 13:53
	 **/
	window.onload=function(){
		// 清除过滤状态
		ZHFX.clearCookie("filterStatus");
	}

	/**
	 *  导出PDF
     */
	function   exportPDF() {
		try {
			document.getElementById('myobject').exportPDF();
		}catch (err){
			document.getElementById('myobject1').exportPDF();
		}
	}
	/**
	 *  打印
     */
	function  toPrint(){
		try {
			document.getElementById('myobject').toPrint();
		}catch (err){
			document.getElementById('myobject1').toPrint();
		}
	}
	/**
	 *  重置表单的参数
     */
	function paramsFormReset(){
		// 直接重置
		document.getElementById("paramsForm").reset();
       	// 清空数据
		initMenuList()
	}
	/**
	 *  查询过滤
     */
	function  searchFilters(){
		// 分隔符
		var spl=1;
		// flash 的URL
	 	var  url=$("#movie").val();
     	//获取查询条件
     	var params=ZHFX.serializeObject($("#paramsForm"));
	 	var  paramArrs=[];
		// 参数
	 	for( var p in params){
	 		var str="";
			str = p.replace('reportParamsMap.','');
			if(params[p]) {
				if(p.indexOf("reportParamsMap.startTimeFrom")>-1||p.indexOf("reportParamsMap.endTimeFrom")>-1){
					paramArrs.push(str + "=" + params[p] + " 00:00:00");
				} else if(p.indexOf("reportParamsMap.starTimeTo")>-1||p.indexOf("reportParamsMap.endTimeTo")>-1){
					paramArrs.push(str + "=" + params[p] + " 23:59:59");
				} else{
					paramArrs.push(str + "=" + params[p]);
				}
			}
			else {
				paramArrs.push(str + "=null" );
			}
	 	}
	   url=url.split("&spl=1")[0];
	   url=url+"&spl=1&"+paramArrs.join("&");
	   $("#movie").val(url);
	   $("#myobject1").prop("src",url);
	   // 刷新页面
	   container.innerHTML=$("#container").html();
	}
	
	//保存检索条件
	function  loadSessionId(){
				$.ajax({
					async : false,
					cache:false,
					type: 'POST',
					dataType : 'text',
					url: '${ctx}/report/queryCondition!saveReportParamsMap',
					data : $('#paramsForm').serializeArray(),
					error: function () {//请求失败处理函数
						$.dialog.alert("数据获取失败！","warning");
					},
					success:function(data){
						debugger;
						$("#sessionId").val(data);
					}
				});
	}
</script>
<%-- 注意加载查所需要的模板  --%>
<jsp:include page="./loadQueryCondition.jsp"></jsp:include>
