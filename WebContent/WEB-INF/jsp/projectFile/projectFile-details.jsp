<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- add css -->
	<link type="text/css" rel="stylesheet" href="${ctx}/book/css/style.css">
	<link type="text/css" rel="stylesheet" href="${ctx}/book/css/fonts.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/global.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
	<!-- add js -->
	<script src="${ctx}/book/js/jquery.js" type="text/javascript"></script>
	<script src="${ctx}/book/js/jquery.fullscreen.js" type="text/javascript"></script>
	<script src="${ctx}/book/js/turn.js" type="text/javascript"></script>
	<script src="${ctx}/book/js/jquery.address-1.6.min.js" type="text/javascript"></script>
	<script src="${ctx}/book/js/onload.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/common/extend.js"></script>
	<style>
		html, body {
			margin: 0;
			padding: 0;
			overflow:auto !important;
		}
	</style>

	<script type="text/javascript">
		function bookLoad(Book_Obj){
			$(".loading").hide();
			loadData();
            // 启动监听任务
            time();
		}
        //启动及关闭按钮
        var timeout = 0;
        function time()
        {
            // 所有的数据都查询完了
            if(timeout===11) {
                 // 渲染书籍
                //加载图书
                window.Book_v7.ready();
                window.Book_v7.load();
                return
            };
            setTimeout(time,100); //time是指本身,延时递归调用自己,100为间隔调用时间,单位毫秒
        }
	</script>
</head>
<body>
<!-- 进度条 -->
<div class="loading" >
	<div class="loadbg"></div>
	<span class="loadimg"><img src="${ctx}/book/images/loading.gif"/></span>
</div>
<%--引入公用文件--%>
<%-- <jsp:include page="./../common/toolbar.jsp"></jsp:include> --%>
<!-- BEGIN FLIPBOOK STRUCTURE -->
<div data-template="true" data-cat="book7" id="fb7-ajax">

	<!-- BEGIN HTML BOOK -->
	<div data-current="book7" class="fb7" id="fb7">
		<!--目录弹出层start-->
		<div class="mlopenDiv" style="display:none;">
			<h1>目录</h1>
			<div class="mlNr" id="project_nood">
				<ul>
					<li><a href="javascript:void(0)" onClick="setPage(6)">一、项目履历&nbsp;&nbsp;....................................................................................&nbsp;6</a></li>
					<li><a href="javascript:void(0)" onClick="setPage(8)">二、基本信息&nbsp;&nbsp;....................................................................................&nbsp;8</a></li>
					<li><a href="javascript:void(0)" onClick="setPage(15)">三、审核备办理事项&nbsp;&nbsp;......................................................................&nbsp;15</a></li>
					<li><a href="javascript:void(0)" onClick="setPage(16)">四、投资情况&nbsp;&nbsp;..................................................................................&nbsp;16</a></li>
					<li><a href="javascript:void(0)" onClick="setPage(17)">五、计划下达情况&nbsp;&nbsp;..........................................................................&nbsp;17</a></li>
					<li><a href="javascript:void(0)" onClick="setPage(18)">六、资金到位完成情况&nbsp;&nbsp;..................................................................&nbsp;18</a></li>
				</ul>
			</div>
		</div>
		<!--目录弹出层end-->

		<!-- preloader -->
		<div class="fb7-preloader">
			<div id="wBall_1" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_2" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_3" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_4" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_5" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_6" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_7" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_8" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_9" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_10" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_11" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_12" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_13" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_14" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_15" class="wBall">
				<div class="wInnerBall"></div>
			</div>
			<div id="wBall_16" class="wBall">
				<div class="wInnerBall"></div>
			</div>
		</div>

		<!-- background for book -->
		<div class="fb7-bcg-book"></div>

		<!-- BEGIN CONTAINER BOOK -->
		<div id="fb7-container-book">

			<!-- BEGIN deep linking -->
			<section id="fb7-deeplinking">
				<ul>
					<li data-address="page1" data-page="1"></li>
					<li data-address="page2" data-page="2"></li>
					<li data-address="page3" data-page="3"></li>
					<li data-address="page4" data-page="4"></li>
					<li data-address="page5" data-page="5"></li>
					<li data-address="page6" data-page="6"></li>
					<li data-address="page7" data-page="7"></li>
					<li data-address="page8" data-page="8"></li>
					<li data-address="page9" data-page="9"></li>
					<li data-address="page10" data-page="10"></li>
					<li data-address="page11" data-page="11"></li>
					<li data-address="page12" data-page="12"></li>
					<li data-address="page13" data-page="13"></li>
					<li data-address="page14" data-page="14"></li>
					<li data-address="page15" data-page="15"></li>
					<li data-address="page16" data-page="16"></li>
					<li data-address="page17" data-page="17"></li>
					<li data-address="page18" data-page="18"></li>
				</ul>
			</section>
			<!-- END deep linking -->

			<!-- BEGIN ABOUT -->
			<%-- <section id="fb7-about"></section> --%>
			<!-- END ABOUT -->

			<!-- BEGIN PAGES -->
			<div id="fb7-book" class="fb7-first fb7-noshadow">

				<!-- BEGIN PAGE 1 -->
				<div class="fb7-second fb7-noshadow fb7_book_xmda">

					<!-- begin container page book -->
					<div class="fb7-cont-page-book fmbg">
						<!-- description for page -->
						<div class="fb7-book-fm">
							<div id="" class="titleName">项目档案</div>
							<div id="projectNameTitle" class="srcTitle"></div>
							<div id="proLegalUnitTitle" class="unitName"></div>
							<div id="storeTimeTitle" class="timek"></div>
						</div>
					</div>
					<!-- end container page book -->
				</div>
				<!-- END PAGE 1 -->
				<%--摘要--%>
				<jsp:include page="./include/briefly.jsp"></jsp:include>
                <%--摘要--%>
				<!-- BEGIN PAGE 2 -->
				<div class="fb7-double fb7-first fb7-noshadow">

					<!-- begin container page book -->
					<div class="fb7-cont-page-book">

						<!-- description for page  -->
						<!--<div class="fb7-page-book">
                        </div>-->
						<div class="fb7-page-book" id="project_title">
							<h1 class="titleL">目录</h1>
							<hr class="line" />
							<ul class="mlist">
								<li><a href="javascript:void(0)" onClick="setPage(6)">一、项目履历&nbsp;&nbsp;&nbsp;................................................................................&nbsp;6</a></li>
								<li><a href="javascript:void(0)" onClick="setPage(8)">二、基本信息&nbsp;&nbsp;&nbsp;................................................................................&nbsp;8</a></li>
								<li><a href="javascript:void(0)" onClick="setPage(15)">三、审核备办理事项&nbsp;&nbsp;&nbsp;...................................................................&nbsp;15</a></li>
								<li><a href="javascript:void(0)" onClick="setPage(16)">四、投资情况&nbsp;&nbsp;&nbsp;................................................................................&nbsp;16</a></li>
								<li><a href="javascript:void(0)" onClick="setPage(17)">五、计划下达情况&nbsp;&nbsp;&nbsp;........................................................................&nbsp;17</a></li>
								<li><a href="javascript:void(0)" onClick="setPage(18)">六、资金到位完成情况&nbsp;&nbsp;&nbsp;...............................................................&nbsp;18</a></li>
							</ul>
							<hr class="line" />
						</div>

						<!-- begin number page -->
						<div class="fb7-meta">
							<span class="fb7-num">5</span>
						</div>
						<!-- end number page -->
					</div>
					<!-- end container page book -->
				</div>
				<!-- END PAGE 2 -->
				<!-- BEGIN PAGE 3 -->
				<jsp:include page="./include/projectRecord.jsp"></jsp:include>
				<!-- END PAGE 3 -->
				<jsp:include page="./include/basicInfo.jsp"></jsp:include>
				<!-- BEGIN PAGE 4 -->

				<!-- END PAGE 4-5 -->
				<jsp:include page="./include/handlingMatters.jsp"></jsp:include>
				<!-- BEGIN PAGE 6-7 -->
				<jsp:include page="./include/investmentSituation.jsp"></jsp:include>

				<jsp:include page="./include/issuedSituation.jsp"></jsp:include>
				<%--完成资金情况--%>
				<jsp:include page="./include/finishSituation.jsp"></jsp:include>
               <%--最新调度信息--%>
				<jsp:include page="./include/dispatchInfo.jsp"></jsp:include>
				<!-- END PAGE end -->
			</div>
			<!-- END PAGES -->
			<!-- arrows -->
			<a class="fb7-nav-arrow prev"></a>
			<a class="fb7-nav-arrow next"></a>
			<!-- shadow -->
			<div class="fb7-shadow"></div>
		</div>
		<!-- END CONTAINER BOOK -->

		<!-- BEGIN FOOTER -->
		<div id="fb7-footer">

			<div class="fb7-bcg-tools"></div>
			<div class="fb7-menu" id="fb7-center">
				<ul>
					<!-- margin left -->
					<li></li>

					<!-- icon download -->
					<!--<li>
                       <a title="Pdf File Or Zip" class="fb7-download" href="img/file.pdf"></a>
                   </li>-->

					<!-- icon_zoom_in -->
					<li>
<!-- 						<a title="返回列表" class="return-list" onclick="window.location.reload(true);"></a> -->
					</li>
					<li>
						<a title="放大" class="fb7-zoom-in"></a>
					</li>

					<!-- icon_zoom_out -->
					<li>
						<a title="缩小" class="fb7-zoom-out"></a>
					</li>

					<!-- icon_zoom_auto -->
					<li>
						<a title="字体" class="fb7-zoom-auto"></a>
					</li>

					<!-- icon_zoom_original -->
					<li>
						<a title="1:1" class="fb7-zoom-original"></a>
					</li>

					<!-- icon_allpages -->
					<li>
						<a title="目录" class="fb7-show-all"></a>
					</li>

					<!-- icon_zoom_word -->
					<li>
						<a title="导出word" class="fb7-zoom-word" id="word_btn"></a>
					</li>

					<!-- icon_home -->
					<li>
						<a title="返回主页" class="fb7-home"></a>
					</li>

					<!-- icon fullscreen -->
					<li>
						<a title="全屏" class="fb7-fullscreen"></a>
					</li>

					<!-- margin right -->
					<li></li>
				</ul>
			</div>

			<div class="fb7-menu" id="fb7-right">
				<ul>
					<!-- icon page manager -->
					<li class="fb7-goto">
						<label for="fb7-page-number" id="fb7-label-page-number"></label>
						<input type="text" id="fb7-page-number">
						<button type="button">Go</button>
					</li>
					<li class="fb7-goto">
						<%--<input type="button" class="book_word_back" id="word_btn" title="导出word" />--%>
					</li>
				</ul>
			</div>
		</div>
		<!-- END FOOTER -->

		<!-- BEGIN THUMBS -->
		<div id="fb7-all-pages" class="fb7-overlay">

			<section class="fb7-container-pages">

				<div id="fb7-menu-holder">

					<ul id="fb7-slider" style="left:50%;margin-left:-280px!important;">

						<!-- PAGE 2 - THUMB -->
						<li class="2" >
							<img alt="" src="../book/images/xmda_01.jpg">
						</li>

					</ul>

				</div>

			</section>
		</div>
		<!-- END THUMBS -->
	</div>
	<!-- END HTML BOOK -->
</div>
<!-- END FLIPBOOK STRUCTURE -->

<!-- CONFIGURATION FLIPBOOK -->
<script>
	jQuery('#fb7-ajax').data('config',
			{
				"page_width":"1280",
				"page_height":"720",
				"zoom_step":"0",
				"go_to_page":"Page",
				"gotopage_width":"40",
				"zoom_double_click":"1",
				"tooltip_visible":"true",
				"toolbar_visible":"true",
				"deeplinking_enabled":"true",
				"double_click_enabled":"true",
				"rtl":"false"
			})
	var scalevalue;
	$(function(){
		var currentW = $(window).width();
		var bl = currentW/1920;
		$(".mlopenDiv").css("transform","scale("+bl+")");
		$("#fb7").delegate(".close","click",function(){
			$(".opendivL").hide();
			$(".opendivR").hide();
		});
		scalevalue = bl;
	})

	var oldH  = $(window).height();
	$(window).resize(function(){
		var bl;
		var currentW = $(window).width();
		var currentH = $(window).height();
		if(currentH!=oldH) {//窗口高度发生改变时
			bl = ($(window).height())/1080;
		}else{
			bl = currentW/1920;
		}
		$(".mlopenDiv").css("transform","scale("+bl+")");
		oldH = currentH;
		scalevalue = bl;
	});

</script>
<script type="text/javascript">
	//项目ID
	var guid = '${projectGuid}';
	//项目模块
	var moduleCode = '${moduleCode}';

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
	function formatYear(times){
		if(times){
			var time = new Date(times);
			var y = time.getFullYear();
			var m = time.getMonth()+1;
			var d = time.getDate();
			var h = time.getHours();
			var mm = time.getMinutes();
			var s = time.getSeconds();
			return y;
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
			return y+'-'+add0(m)+'-'+add0(d)+'-'+add0(h)+':'+add0(mm)+':'+add0(s);
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
	//是否打捆项目
	function formatIsBund(isBund) {
		//0：正常 1：为打捆项目
		if(isBund){
			if(isBund='1'){
				return '是';
			}else if(isBund='0'){
				return '否';
			}else{
				return	isBund;
			}
		}else{
			return '否';
		}
	}
	/**
	 * 图片
	 */
	function formatImage(image){
		if(image){
			if(image.substring(0,4)=="http"){
				return image
			}else{
				return '${ctx}/'+image;
			}
		}
	}
	/**
	 * 加载数据
	 */
	function loadData() {
		initProjectRecord(); //加载项目履历数据
		initAppInfo(); //加载审核备项目履历
		initBase(); //加载基本信息数据
		initPrework();//前期工作
		initInvestConstitute();//资金构成
		initQua();//量化建设规模
		initMatter();   //审核备办理事项WWWW
		initSource();   //投资情况
		initPlanDown(); //计划下达情况
		initInvestPut();//资金到位完成情况
		initDispatch(); //项目调度-最新报告期
	}
	/**
	 * 项目前期工作加载
	 */
	function initPrework(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getPreworkInfo.action?projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
                timeout=timeout+1;
				var str = '';
				if (data) {
					for (var i = 0; i < data.length; i++) {
						str += '<tr>'
								+  '<td >'+formatNull(data[i]["REPLY_NO"])+'</td>'
								+  '<td >'+formatNull(data[i]["REPLY_ORG"])+'</td>'
								+  '<td >'+formatDate(data[i]["REPLY_DATE"])+'</td>'
								+  '</tr>';
					}
				}
				//动态添加数据行到项目前期工作表格
				$("#prework_table").append(str);
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}
	/**
	 * 资金构成加载
	 */
	function initInvestConstitute(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getInvestConstituteInfo.action?projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
                timeout=timeout+1;
				var str = '';
				if (data) {
					for (var i = 0; i < data.length; i++) {
						str += '<tr>'
								+  '<td >'+formatNull(data[i]["NAME"])+'</td>'
								+  '<td >'+formatNull(data[i]["TOTAL_INVESTMENT"])+'</td>'
								+  '</tr>';
					}
				}
				//动态添加数据行到项目前期工作表格
				$("#invest_briefly_table").append(str);
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}
	/**
	 * 项目履历数据加载
	 */
	function initProjectRecord(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getProjectRecord.action?type=1&projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
				var str = '';
                timeout=timeout+1;
				if (data) {
					for (var i = 0; i < data.length; i++) {
						str += '<tr>'
								+  '<td  width="140">'+data[i]["OPERATE"]+'</td>'
								+  '<td >'+format(data[i]["OPTIME"])+'</td>'
								+  '<td >'+data[i]["OPDEPART"]+'</td>'
								+  '<td >'+formatNull(data[i]["OPUSER"])+'</td>'
								+  '<td >'+(!data[i]["remark"]?"":data[i]["remark"])+'</td>'
								+  '</tr>';
					}
				}
				//动态添加数据行到项目履历表格
				$("#resume_table").append(str);
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}

	/**
	 * 审核备项目履历数据加载
	 */
	function initAppInfo(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getShbProjectRecord.action?type=2&projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
                timeout=timeout+1;
				var str = '';
				if (data) {
					for (var i = 0; i < data.length; i++) {
						str += '<tr>'
								+  '<td  style="width: 40%;">'+formatNull(data[i]["bzcz"])+'</td>'
								+  '<td style="width: 25%;">'+format(data[i]["dealed_date"])+'</td>'
								+  '<td style="width: 20%;">'+formatNull(data[i]["DEPT_NAME"])+'</td>'
								+  '<td style="width: 10%;">'+formatNull(data[i]["OPUSER"])+'</td>'
								+  '<td style="width: 5%;">'+(!data[i]["remark"]?"":data[i]["remark"])+'</td>'
								+  '</tr>';
					}
				}
				//动态添加数据行到项目履历表格
				$("#app_table").append(str);
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}

	/**
	 * 基本信息数据加载
	 */
	function initBase(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getBaseInfo.action?projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
                timeout=timeout+1;
				if (data) {
					var map = data;
					for (var key in map) {
						if (key == 'STORE_TIME') {
							$('#' + key).text(formatDate(map[key]));
						} else if(key == 'EXPECT_ENDYEAR'||key == 'EXPECT_STARTYEAR'){
							$('#BRIEFLY_' + key).text(formatYear(map[key]));
							$('#' + key).text(formatYear(map[key]));
						} else if(key == 'EXPECT_STARTYEAR'){
							$('#BRIEFLY_' + key).text(formatYear(map[key]));
							$('#' + key).text(formatYear(map[key]));
						}else{
							$('#BRIEFLY_' + key).text(formatNull(map[key]));
							$('#' + key).text(formatNull(map[key]));
						}
					}
					if(formatDate(map["EXPECT_STARTYEAR"])){
						$("#BRIEFLY_EXPECT_STARTYEAR").text(formatYear(map["EXPECT_STARTYEAR"])+'-'+formatNull(map["EXPECT_NY"]));//开工年月
					}
					$("#storeTimeTitle").text("入库时间："+formatDate(map["STORE_TIME"]).substring(0,4)+"年"+formatDate(map["STORE_TIME"]).substring(5,7)+"月");//封面申报日期
					$("#projectNameTitle").text(formatNull(map["PRO_NAME"]));//封面项目名称
					$("#proLegalUnitTitle").text("项目单位："+formatNull(map["PRO_ORG"]));//封面法人单位
				}
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}
	/**
	 * 量化建设规模数据加载
	 */
	function initQua(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getQuaInfo.action?projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
				var str = '';
                timeout=timeout+1;
				if (data) {
					for (var i = 0; i < data.length; i++) {
						str += '<tr>'
								+  '<td >'+data[i]["PIE_NAME"]+'</td>'
								+  '<td >'+data[i]["_NUMBER"]+'</td>'
								+  '</tr>';
					}
				}
				//动态添加数据行到项目履历表格
				$("#baseNum_table").append(str);
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}
	/**
	 * 审核备办理事项
	 */
	function initMatter(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getMatterInfo.action?projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
                timeout=timeout+1;
				var str = '';
				if (data) {
					for (var i = 0; i < data.length; i++) {
						str += '<tr>'
								+  '<th width="100">'+(data[i]["ITEM_NAME"]||'')+'</th>'
								+  '<td >'+(data[i]["FILETITLE_NAME"]||'')+'</td>'
								+  '<td >'+(data[i]["APPROVAL_NUMBER"]||'')+'</td>'
								+  '<td >'+(data[i]["CODE_NAME"]||'')+'</td>'
								+  '<td >'+(formatDate(data[i]["DEALED_DATE"])||'')+'</td>'
								+  '<td >'+(data[i]["LIMIT_DAYS"]||'')+'</td>'
								+  '<td >'+(data[i]["BJSJ"]||'')+'</td>'
								+  '<td >'+(data[i]["NAME1"]||'')+'</td>'
								+  '<td >'+(formatDate(data[i]["COMPLETED_DATE"])||'')+'</td>'
								+  '<td >'+(data[i]["VALIDITY_DATE"]||'')+'</td>'
// 								+  '<td ><img src="../'+data[i]["approvalAttach"]+'"/></td>'
								+  '</tr>';
					}
				}
				//动态添加数据行到项目履历表格
				$("#matter_table").append(str);
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}

	/**
	 * 投资情况
	 */
	function initSource(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getInvestmentInfo.action?projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
                timeout=timeout+1;
				var _contactHtml = "";
				if (data) {
					var map = data;
					$("#currentYear").text(map.belongYear);
					//为专项建设
					if (map.isBond == 'A00001') {
						$("#isSpecFunds").text("是");
						_contactHtml = $("#_specFundsYes").html()+'<tbody class="th_bold" >';
						var _tempContent = $("#_specFundsYesContent").html();
						for(var i = 0; i < map.invList.length; i++) {
							_contactHtml=_contactHtml+ZHFX.fs(_tempContent,
											map.invList[i]["NAME"],
											formatNull(map.invList[i]["TOTAL_INVESTMENT"]),
											formatNull(map.invList[i]["CAPTIAL_CASH"]),
											formatNull(map.invList[i]["GAP_CAPTIAL_2015"]),
											formatNull(map.invList[i]["GAP_CAPTIAL_2016"]),
											formatNull(map.invList[i]["GAP_CAPTIAL_2017"]),
											formatNull(map.invList[i]["CUR_APPLY_SPE_CAPTIAL"]),
											formatNull(map.invList[i]["TOTAL_ISSUED_CAPTIAL"]),
											formatNull(map.invList[i]["TOTAL_COMPLETE_CAPTIAL"]),
											formatNull(map.invList[i]["sugArrangeCaptial"]),
											formatNull(map.invList[i]["putinCaptial"]),
											formatNull(map.invList[i]["declareScale"]),
											formatNull(map.invList[i]["sugScale"]),
											formatNull(map.invList[i]["putinScale"]),
											(!map.invList[i]["remark"]?"":map.invList[i]["remark"])
									);
						}
						//为中央预算内
					} else {
						$("#isSpecFunds").text("否");
						_contactHtml = $("#_specFundsNo").html()+'<tbody class="th_bold" >';
						var _tempContent = $("#_specFundsNoContent").html();
						// 遍历对象
						for(var i = 0; i < map.invList.length; i++) {
							_contactHtml=_contactHtml+ZHFX.fs(_tempContent,
											map.invList[i]["NAME"],
											formatNull(map.invList[i]["TOTAL_INVESTMENT"]),
											formatNull(map.invList[i]["CAPTIAL_CASH"]),
											formatNull(map.invList[i]["TOTAL_ISSUED_CAPTIAL"]),
											formatNull(map.invList[i]["TOTAL_COMPLETE_CAPTIAL"]),
											formatNull(map.invList[i]["TOTAL_INVESTMENT"]),
											formatNull(map.invList[i]["APPLY_CAPTIAL_2016"]),
											formatNull(map.invList[i]["APPLY_CAPTIAL_2017"]),
											formatNull(map.invList[i]["APPLY_CAPTIAL_2018"]),
											formatNull(map.invList[i]["CUR_PERIOD_ALLOCATED"]),
											formatNull(map.invList[i]["declareScale"]),
											formatNull(map.invList[i]["sugScale"]),
											formatNull(map.invList[i]["putinScale"]),
											(!map.invList[i]["REMARKS"]?"":map.invList[i]["REMARKS"])
									);
						}
						_contactHtml+="</tbody>";
					}
				}
				//添加投资明细到里面
				$("#invest_show_table").append(_contactHtml);
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}

	/**
	 * 计划下达情况数据加载
	 */
	function initPlanDown(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getIssuedInfo.action?projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
				var str = '';
				var str2 = '';
                timeout=timeout+1;
				if (data) {
					for (var i = 0; i < data.length; i++) {
						str += '<tr>'
								+  '<td >'+data[i]["EXPORT_FILE_NO"]+'</td>'
								+  '<td >'+formatNull(data[i]["FILE_NAME"])+'</td>'
								+  '<td >'+data[i]["ISSUED_MONEY"]+'</td>'
								+  '<td >'+data[i]["SIJU"]+'</td>'
								+  '<td >'+formatDate(data[i]["ISSUSED_TIME"])+'</td>'
								+  '<td >'+formatIsBund(data[i]["IS_BUNDLED"])+'</td>'
								+  '</tr>';
						str2 += '<tr>'
								+  '<td >'+data[i]["EXPORT_FILE_NO"]+'</td>'
								+  '<td >'+data[i]["ISSUED_MONEY"]+'</td>'
								+  '<td >'+formatDate(data[i]["ISSUSED_TIME"])+'</td>'
								+  '</tr>';
					}
				}
				//动态添加到简要情况的计划下达情况数据表格
				$("#briefly_down_table").append(str2);
				//动态添加数据行到计划下达情况数据表格
				$("#plan_down_table").append(str);
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}

	/**
	 * 资金到位完成情况
	 */
	function initInvestPut(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getFinishInfo.action?projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
				var str = '';
                timeout=timeout+1;
				if (data) {
					for (var i = 0; i < data.length; i++) {
						str += '<tr>'
								+  '<td >'+data[i]["REPORT_NUMBER"]+'</td>'
								+  '<td >'+formatNull(data[i]["SUMMONEY"])+'</td>'
								+  '<td >'+formatNull(data[i]["PUTMONEY"])+'</td>'
								+  '<td >'+formatNull(data[i]["COMMONEY"])+'</td>'
								+  '<td >'+formatNull(data[i]["PAYMONEY"])+'</td>'
								+  '</tr>';
					}
				}
				//动态添加数据行到资金到位完成情况表格
				$("#invest_table").append(str);
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}

	/**
	 * 各期项目调度数据加载
	 */
	function initDispatch(){
		$.ajax({
			type:'POST',
			dataType:'json',
			url:'${ctx}/projectFile/projectFile!getDispatchInfo.action?projectGuid='+guid+'&moduleCode='+moduleCode,
			success:function(data){
                timeout=timeout+1;
				var pageNum = 18;
				if (data) {
					if(data.length>0){
						//生成目录
						//目录链接
						var hrelUrl ='<li><a href="javascript:void(0)" onClick="setPage(19)">七、项目调度&nbsp;&nbsp;..................................................................................&nbsp;19</a></li>';
						 hrelUrl +='<li><a href="javascript:void(0)" onClick="setPage(21)">八、调度图片&nbsp;&nbsp;..................................................................................&nbsp;21</a></li>';
						//目录添加
						$("#project_nood ul").append(hrelUrl);
						$("#project_title ul").append(hrelUrl);
					}
					var _htmlContent='';
					var _htmlAddPage='';
					for (var i = 0; i < data.length; i++) {
						//添加页面
						//一条调度信息添加3业，定义三页页面的页数
						var addNum1 = pageNum+1;
						var addNum2 = pageNum+2;
						var addNum3 = pageNum+3;
						var addPage ='<li data-address="page'+addNum1+'" data-page="'+addNum1+'"></li><li data-address="page'+addNum2+'" data-page="'+addNum2+'"></li><li data-address="page'+addNum3+'" data-page="'+addNum3+'"></li>';
						_htmlAddPage = _htmlAddPage+addPage;
						//调度信息模板
						var _dispatchInfo = $("#_dispatchInfoTempl").html();
						// 到位资金模板
						var _placeTemplate=$("#_placeTemplate").html();
						// 完成资金模板
						var _finishTemplate=$("#_finishTemplate").html();
						// 调度图片模块模板
						var  _dispatchImageInfo=$("#_dispatchImageInfo").html();
						// 图片的模板
						var  _imgTemplate=$("#_imgTemplate").html();
						//内容体: 调度信息整体内容、到位资金内容、完成资金内容体、调度图片模块内容体、调度图片拼装体
						var dispatchInfo='', placeContent='',finishContent='',imgInfo='',imgContent='';
						// 到位信息
						var placeMons=data[i]["dispatchInvestDowllist"];
						// 完成信息
						var finishMons=data[i]["dispatchInvestPutlist"];
						//到位内容体拼装
						if(placeMons){
							for(var p=0,len=placeMons.length;p<len;p++){
								placeContent=placeContent+ZHFX.fs(_placeTemplate,
												formatNull(placeMons[p]["NAME"]),formatNull(placeMons[p]["SUM_MONEY"]),
												formatNull(placeMons[p]["PREV_MONEY"]),formatNull(placeMons[p]["CUR_MONEY"]));

							}
						}
						// 完成资金内容体拼装
						if(finishMons){
							for(var f=0,len=finishMons.length;f<len;f++){
								finishContent=finishContent+ZHFX.fs(_finishTemplate,
												formatNull(finishMons[f]["NAME"]),formatNull(finishMons[f]["FINISH_PREV_MONEY"]),
												formatNull(finishMons[f]["FINISH_CUR_MONEY"]),formatNull(finishMons[f]["PAY_PREV_MONEY"]),
												formatNull(finishMons[f]["PAY_CUR_MONEY"]));

							}
						}
						// 图片信息转换类型
						var image = [];
						//最新一期调度
// 						if(i==0){
						try {
							if(data[i]["IMAGE_URL"]!=""){
								for (var img = 0; img < data[i]["IMAGE_URL"].split(",").length; img++) {
									image.push(data[i]["IMAGE_URL"].split(",")[img]);//解析图片字符串为数组
								}
							}

						} catch (error) {
						}
						// 图片的替换符
						var _imgTpl = '<a  class ="showImg'+i+'" href="javascript:ZHFX.showImgList(\'{0}\','+i+')"><img src="{0}"/></a>';

						//图片内容体拼装
						if(image.length>0){
							for (var num = 0,len=parseInt(image.length / 3); num <len ; num++) {
								//遍历图片链接数组按格式生成图片组合
								imgContent = imgContent + ZHFX.fs(_imgTemplate, ZHFX.fs(_imgTpl, formatImage(image[3 * num])),ZHFX.fs(_imgTpl,  formatImage(image[3 * num + 1])),ZHFX.fs(_imgTpl,  formatImage(image[3 * num + 2])));
							}
							// 对图的个数取余
							var _numMod = image.length % 3;
							switch (_numMod) {
								case 1:
									// 如果除以3是余一位
									imgContent = imgContent + ZHFX.fs(_imgTemplate, ZHFX.fs(_imgTpl,  formatImage(image[image.length-1])),'','');
									break;
								case 2:
									// 如果除以3是余两位
									imgContent = imgContent + ZHFX.fs(_imgTemplate, ZHFX.fs(_imgTpl,  formatImage(image[image.length - 2])), ZHFX.fs(_imgTpl, formatImage(image[image.length-1])),'');
									break;
							}
						}
						//调度图片值只出现在第一个调度中
						imgInfo = imgInfo + ZHFX.fs(_dispatchImageInfo,imgContent);
// 						}
						//调度所有信息内容体拼装
						var dispatchReport, reportTime;//设置报告期、报告时间；用于接收值动态填充
						i >= 1 ? (dispatchReport = "项目调度-往月报告期") : (dispatchReport = "项目调度-最新报告期");//调度次数大于1，之前的为往月报告期
						i >= 1 ? (reportTime = data[i]["REPORT_NUMBER"]) : (reportTime = "");//最近一次调度，调度期为“”
						dispatchInfo=ZHFX.fs(_dispatchInfo,i,dispatchReport,reportTime,
								formatDate(data[i]["ACTUAL_START_TIME"]),formatDate(data[i]["ACTUAL_END_TIME"]),data[i]["BIDDING_MODE"],formatNull(data[i]["BUILD_UNIT"]),
								formatNull(data[i]["REPORT_NUMBER"]),data[i]["IMAGE_PROGRESS"],formatNull(data[i]["YEAR_BUILD_CONTENT"]),formatNull(data[i]["PROBLEMS_SUGGESTIONS"]),
								placeContent,finishContent,imgInfo,addNum1,addNum2,addNum3);
						// 历史调度
						_htmlContent=_htmlContent+dispatchInfo;
						pageNum = pageNum+3;
					}
					//添加页面
					$("#fb7-deeplinking ul").append(_htmlAddPage);
					//页面添加调度信息
					$("#book_page").last().after(_htmlContent);
				}
			},
			error:function(){
				alert("数据获取失败！","warning");
			}
		});
	}
///////////////////////////////////////////////////////////////////////////
$("#word_btn").click(function(){
	window.location='${ctx}/projectFile/projectFile!exportWord.action?projectGuid='+guid+'&moduleCode='+moduleCode;
// 		$.ajax({
// 			type:'POST',
// 			dataType:'json',
// 			url:'${ctx}/projectFile/projectFile!exportWord.action?projectGuid='+guid+'&moduleCode='+moduleCode,
// 			success:function(data){
// 				if (data=='1') {
// 					alert("word.导出成功,保存在E盘!");
// 				}
// 				//动态添加数据行到资金到位完成情况表格
// 			},
// 			error:function(){
// 				alert("数据获取失败！","warning");
// 			}
// 		});
	});
</script>

<script type="text/html" id="_specFundsYes">
	<thead>
	<tr>
		<th rowspan="2" width="120">资金类别</th>
		<th rowspan="2">总投资(万元)</th>
		<th rowspan="2">资本金<br>(万元)</th>
		<th colspan="3">专项建设基金资本金缺口(万元)</th>
		<th rowspan="2">2016年本次<br>申请专项<br>建设基金</th>
		<th rowspan="2">累计下达<br>(安排)资金<br>(万元)</th>
		<th rowspan="2">累计完成<br>投资(万元)</th>
		<th rowspan="2">建议资金<br>(万元)</th>
		<th rowspan="2">投<br>放资金</th>
		<th rowspan="2">申报<br>比例</th>
		<th rowspan="2">建议<br>比例</th>
		<th rowspan="2">投放<br>比例<br></th>
		<th rowspan="2">备注</th>
	</tr>
	<tr>
		<th>2015年</th>
		<th>2016年</th>
		<th>2017年</th>
	</tr>
	</thead>

</script>
<%--专项建设基金的内容--%>
<script type="text/html" id="_specFundsYesContent">
	<tr>
		<th width="120">{0}</th>
		<td>{1}</td>
		<td>{2}</td>
		<td>{3}</td>
		<td>{4}</td>
		<td>{5}</td>
		<td>{6}</td>
		<td>{7}</td>
		<td>{8}</td>
		<td>{9}</td>
		<td>{10}</td>
		<td>{11}</td>
		<td>{12}</td>
		<td>{13}</td>
		<td>{14}</td>
	</tr>
</script>

<script type="text/html" id="_specFundsNo">
	<thead>
	<tr>
		<th rowspan="2" width="120">资金类别</th>
		<th rowspan="2">总投资(万元)</th>
		<th rowspan="2">资本金<br>(万元)</th>
		<th rowspan="2">累计下达<br>(安排)资金<br>(万元)</th>
		<th rowspan="2">累计完成<br>投资(万元)</th>
		<th colspan="4">资金需求(万元)</th>
		<th rowspan="2">本次下达资金</th>
		<th rowspan="2">申报<br>比例</th>
		<th rowspan="2">建议<br>比例</th>
		<th rowspan="2">投放<br>比例<br></th>
		<th rowspan="2">备注</th>
	</tr>
	<tr>
		<th>合计</th>
		<th>2016年</th>
		<th>2017年</th>
		<th>2018年</th>
	</tr>
	</thead>

</script>
<%--专项建设基金的内容--%>
<script type="text/html" id="_specFundsNoContent">
	<tr>
		<th width="120">{0}</th>
		<td>{1}</td>
		<td>{2}</td>
		<td>{3}</td>
		<td>{4}</td>
		<td>{5}</td>
		<td>{6}</td>
		<td>{7}</td>
		<td>{8}</td>
		<td>{9}</td>
		<td>{10}</td>
		<td>{11}</td>
		<td>{12}</td>
		<td>{13}</td>
	</tr>
</script>
</body>
</html>