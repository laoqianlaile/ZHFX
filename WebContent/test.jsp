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
	<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/icon.css">
	<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/easyui1.4/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/base.js"></script>
    <script type="text/javascript"  src="${ctx}/js/gbasebi.js"></script>
	<style>
		*{
			color : red;
		}
	</style>
	<script type="text/javascript">
		var pArea,pIndustry;

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

		//传值
		var arrs=[];
	   function testAlert(p,p2) {
		   arrs.push(p);
		   alert(p2);
		   alert(arrs.join("#"));
	   }
       // 更多展现
	   function showMessage(){
	   	   // 页面的值
		   arrs.push($("#pp1").val());
		   arrs.push($("#pp2").val());
		   var params=arrs.join("#");
	   	  window.open("${ctx}/getParams.jsp?paramName="+encodeURIComponent(encodeURIComponent(params)));
	   }
  $(function (){
	  //刷新页面
	  $("#btn").bind("click",function () {
		//  debugger;
		  pArea=$("#pp1").val();
		  pIndustry=$("#pp2").val();
		  // 定义容器
		  // container.innerHTML="";
		  var v=$("#movie").val();
//		  alert(v);
		 // v=v+"&pArea="+pArea+"&pIndustry="+pIndustry;
		  v=v+"&pArea=湖北省,湖南省&pIndustry=农业";
		  $("#myobject1").prop("src",v);
		  // 回写URL刷新页面参数
		  $("#movie").val(v);
		  alert($("#container").html());
		  container.innerHTML=$("#container").html();

	  });

  });

	</script>
</head>
<body >
<div style="background-color: #0a5c92;width: 200px;height: 300px">
	<label >地区</label> <input type="text" style="width: 200px;height: 20px;" name="pp1" id="pp1">
	<label >行业</label> <input type="text" style="width: 200px;height: 20px;" name="pp2" id="pp2">
	<button  id="btn" > 查询</button>
</div>
<div  id="container">
	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="myobject"  width="1209" height="730" name="myobject"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
		<param id="movie" name="movie" value="http://59.255.137.11:8080/gservice/gShow.swf?file=gShow.swf&busername=tannongchun&fid=bb7f098b57f04ebc0157f46b8141014a&autoLogin=t" />
		<param name="quality" value="high" />
		<param id="gShowBgcolor" name="bgcolor" value="b91a1a" />
		<param name="allowScriptAccess" value="always" />
		<param name="allowFullScreen" value="true" />
		<embed src="http://59.255.137.11:8080/gservice/gShow.swf?file=gShow.swf&busername=tannongchun&fid=bb7f098b57f04ebc0157f46b8141014a&autoLogin=t" quality="high" bgcolor="b91a1a"
			   name="myobject1" width="640" height="440" id="myobject1"
			   align="middle"
			   play="true" loop="false" quality="high"
			   allowScriptAccess="always"
			   type="application/x-shockwave-flash"
			   allowFullScreen="true"
			   pluginspage="http://www.adobe.com/go/getflashplayer"> </embed>
	</object>
</div>
</body>
</html>