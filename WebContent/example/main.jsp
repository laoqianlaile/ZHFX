<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
 <%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/global.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/base.js"></script>
<script type="text/javascript" src="${ctx}/js/prefixfree.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.transit.min.js"></script>
<title>综合分析展现</title>
</head>
<body>
<div id="logo"></div>

<div id="toolbar">
	<span><a href="#" title="返回上一页" class="btn-back" onclick="history.go(-1)"></a></span>
	<span><a href="home1.html" title="主页" class="btn-index"></a></span>
	<span><a href="#" title="菜单" class="btn-list"></a>
		<ul class="listMenu">
			<h6>
				<a href="javascript:void(0)" id="base" class="active">基础业务</a>
				<a href="javascript:void(0)" id="zdyw">重点业务</a>
			</h6>
			<div id="base-menu">
				<li class="active"><a href="default.html">总览</a></li>
				<li><a href="wngh.html">五年规划项目储备</a>
					<ul>
						<li><a href="wngh-1.html">入库储备项目</a></li>
						<li><a href="wngh-2.html">初审储备项目</a></li>
					</ul>
				</li>
				<li><a href="sngd.html">三年滚动投资计划</a>
					<ul>
						<li><a href="sngd-1.html">滚动计划编制</a></li>
						<li><a href="sngd-2.html">滚动计划审核</a></li>
					</ul>
				</li>
				<li><a href="xmsh.html">项目审核备办理</a>
					<ul>
						<li><a href="xmsh-1.html">项目办理</a></li>
						<li><a href="xmsh-2.html">事项办理</a></li>
					</ul>
				</li>
				<li><a href="zyys.html">中央预算内投资项目管理</a>
					<ul>
						<li><a href="zyys-1.html">年度计划申报</a></li>
						<li><a href="zyys-2.html">年度计划下达</a></li>
						<li><a href="zyys-3.html">年度计划调度</a></li>
					</ul>
				</li>
				<li><a href="zxjs.html">专项建设基金项目</a>
					<ul>
						<li><a href="zxjs-1.html">专项基金项目申报</a></li>
						<li><a href="zxjs-2.html">专项基金项目投放</a></li>
					</ul>
				</li>
				<li><a href="xmda.html">项目档案</a></li>
			</div>
			<div id="zdyw-menu" style="display:none;">
				<li><a href="tzxs.html">投资形势分析</a></li>
                <li><a href="fgf.html">放管服改革</a>
					<ul>
						<li><a href="fgf-1.html">简政放权</a></li>
						<li><a href="fgf-2.html">放管结合</a></li>
						<li><a href="fgf-3.html">优化服务</a></li>
					</ul>
				</li>
				<li><a href="zftz.html">政府投资精细化管理</a></li>
				<li><a href="ycyj.html">预测预警分析</a>
					<ul>
						<li><a href="ycyj-1.html">项目实施进展监测预警分析</a></li>
						<li><a href="ycyj-2.html">项目办理进展监测预警分析</a></li>
						<li><a href="ycyj-3.html">用户分布情况</a></li>
					</ul>
				</li>
				<li><a href="zhfx.html">综合分析报告</a></li>
			</div>
		</ul>
	</span>
	<span><a href="#" title="搜索" class="btn-search"></a>
		<ul class="listSearch">
			<h6>搜索条件</h6>
			<table class="listSearchdiv">
	  <tr>
		  <th>项目申报日期</th>
		  <td><input type="text"></td>
		  <th>年度计划申报日期</th>
		  <td><input type="text"></td>
	  </tr>
	  <tr>
		  <th>专项建设基金申报日期</th>
		  <td><input type="text"></td>
		  <th>建设地点</th>
		  <td><input type="text"></td>
	  </tr>
	  <tr>
		  <th>所属行业</th>
		  <td><input type="text"></td>
		  <th>中央预算内申请规模</th>
		  <td><input type="text"></td>
	  </tr>
	  <tr>
		  <th>专项建设基金申请规模</th>
		  <td><input type="text"></td>
		  <th>中央预算内需求规模</th>
		  <td><input type="text"></td>
	  </tr>
	  <tr>
		  <th>专项建设基金需求规模</th>
		  <td><input type="text"></td>
		  <th>开工或拟开工年月</th>
		  <td><input type="text"></td>
	  </tr>
	  <tr>
		  <th>竣工或拟建成年份</th>
		  <td><input type="text"></td>
		  <th>投资总体进展</th>
		  <td><input type="text"></td>
	  </tr>
	  <tr>
		  <th>项目实施进展</th>
		  <td><input type="text"></td>
		  <th>数据来源</th>
		  <td><input type="text"></td>
	  </tr>
	  <tr>
		  <th>审批阶段</th>
		  <td><input type="text"></td>
		  <th>项目状态</th>
		  <td><input type="text"></td>
	  </tr>
	    <tr>
		  <th>政府投资方向</th>
		  <td><input type="text"></td>
		  <th>是否PPP</th>
		  <td><input type="text"></td>
	  </tr>
	  <tr>
		  <th>是否专项建设基金</th>
		  <td colspan="3"><input type="text"></td>
	  </tr>
	  </table>
			<div><button>搜索</button></div>
		</ul>
	</span>
	<span><a href="#" title="图例" class="btn-chart"></a>
		<ul class="listChart">
			<li onclick="addBox('test','分重大战略','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu01.gif" /></a></li>
			<li onclick="addBox('test2','分重大战略2','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu02.gif" /></a></li>
			<li onclick="addBox('test3','分重大战略3','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu03.gif" /></a></li>
			<li onclick="addBox('test4','分重大战略4','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu04.gif" /></a></li>
			<li onclick="addBox('test5','分重大战略5','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu05.gif" /></a></li>
			<li onclick="addBox('test6','分重大战略6','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu06.gif" /></a></li>
			<li onclick="addBox('test7','分重大战略7','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu07.gif" /></a></li>
			<li onclick="addBox('test8','分重大战略8','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu08.gif" /></a></li>
			<li onclick="addBox('test9','分重大战略9','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu09.gif" /></a></li>
			<li onclick="addBox('test10','分重大战略10','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu10.gif" /></a></li>
			<li onclick="addBox('test11','分重大战略11','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu11.gif" /></a></li>
			<li onclick="addBox('test12','分重大战略12','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu12.gif" /></a></li>
			<li onclick="addBox('test13','分重大战略13','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu13.gif" /></a></li>
			<li onclick="addBox('test14','分重大战略14','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu14.gif" /></a></li>
			<li onclick="addBox('test15','分重大战略15','pie3d.html','pie3d.html')"><a href="javascript:void(0)"><img src="../themes/images/img_menu15.gif" /></a></li>
		</ul>
	</span>
	<span><a href="#" title="导出WORD" class="btn-word"></a></span>
	<span><a href="#" title="导出PDF" class="btn-pdf"></a></span>
	<span><a href="#" title="打印" class="btn-print"></a></span>
</div>
<div id="contentDiv">
    <span class="loginInfo" style="right:54px;"><img src="../themes/images/user.png">${SYS_USER_INFO}<a href="javascript:ZHFX.logout()">【退出】</a></span>
    <span class="currenttWz">当前位置：基础业务 > 总览</span>
<!-- 左边的菜单 -->
 <!-- <div style="height:600px;width:200px;float:left;background-color:#F0F8FF">
 <ol> 
	<li><a url="${ctx}/example/showmodule.jsp"><div>模板的获取</div></a></li>
	<li><a url="${ctx}/example/showReport.jsp"><div>单独报表</div></a></li>
	<li><a url="${ctx}/example/searchReport.jsp"><div>报表查询</div></a></li>
	<li><a url="${ctx}/example/showEcharts.jsp"><div>Echarts报表</div></a></li>
	<li><a url="${ctx}/example/cntAndfoundSwitch.jsp"><div>项目个数和金额切换</div></a></li>
 </ol>
 </div> 
 -->
<!--  中间的内容区域 -->
     <iframe id="centerFrame" allowtransparency="true" frameborder="0" height="1200px" width="100%" scrolling="no" src="${ctx}/ReportServer?op=fr_bi&cmd=bi_init&id=25&createBy=-999"></iframe>
</div>
</body>
 <script type="text/javascript">
 // 我创建的模板
	$.post('${ctx}/login/login!getUserInfo', function(data) { 
		$("#userInfo").text(data);
	}, 'text');
    // 事件的委托代理
	$("li").delegate("a", "click", function(){    
		// 获取自定义属性的值 
		var url=$(this).attr("url");
		// 加载页面 
		$("#centerFrame").prop("src",url);
	});
 </script>
 
</html>