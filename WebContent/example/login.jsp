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
<title>登录系统</title>
 <script type="text/javascript" src="${ctx}/ReportServer?op=emb&resource=finereport.js"></script>  
 
</head>
<body>
	<form>
	  <div><span>登录名称</span> <input type="text" id="username" /></div>
	   <div><span>密码</span> <input type="password" id="password"/></div>
	   <button type="button" onclick="doSubmit()">登录</button>
	</form>
	<div>
		<abbr> 提示：系统管理员  账号密码  sa   123 </abrr>
		<abbr> 提示： 谭农春  账号密码  tannc   123456 </abbr>
	</div>
</body>
<script type="text/javascript">
     // 提交按钮
	function doSubmit() {  
	    var username = FR.cjkEncode(document.getElementById("username").value); //获取输入的用户名  
	    var password = FR.cjkEncode(document.getElementById("password").value);  //获取输入的参数   
	jQuery.ajax({  
		     url:"${ctx}/ReportServer?op=fs_load&cmd=sso",//单点登录的报表服务器  
		     dataType:"jsonp",//跨域采用jsonp方式  
		     data:{"fr_username":username,"fr_password":password},  
		     jsonp:"callback",  
		     timeout:5000,//超时时间（单位：毫秒）  
		     success:function(data) {  
		            if (data.status === "success") { 
		              window.location=data.url;//登录成功，直接跳转到平台系统页面 
			           if(username!="sa"){
			               window.location="${ctx}/ReportServer?op=fr_bi&cmd=bi_init&id=15&createBy=-999&edit=_bi_edit_";//跳转到自定义的平台 
			           }
		            }   
		            else if (data.status === "fail"){  
		               alert("fail");//登录失败（用户名或密码错误）  
		            }  
		     },  
		     error:function(){  
		          alert("error"); // 登录失败（超时或服务器其他错误）  
		     }  
		    });       
	}
</script>
</html>