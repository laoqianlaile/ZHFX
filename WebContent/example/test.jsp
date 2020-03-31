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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>验证工程主项目</title>
<style type='text/css'>
a{
  display:block;
  margin-top:20px;
  height:30px;
  border-style: dotted;  
}
a:link{color:#0F0000;}   
a:active{color:#ff0000;}   
a:visited{color:#ff0;}   
a:hover{color:#00ff00;} 
</style>
</head>
<body>
<a href="${ctx}/example/login.jsp"><div>登录综合分析系统</div></a>
<a href="${ctx}/example/user.jsp"><div>1.得到当前系统的用户名称，以及用户信息</div></a>
<a href="#"><div>2.个数、投资金额之间的图表切换</div></a>
<a href="#"><div>3.实现项目名称，日期时间，建设地点的图表查询</div></a>
<a href="${ctx}/example/showmodule.jsp"><div>4.获取当前用户的全部模块</div></a>
<a href="#"><div>5.Service层要取cube包里面的数据并展现</div></a>
<a href="#"><div>6.用户Echarts里面的柱状图，替换BI里面目前的柱状图</div></a>
<a href="#"><div>7.共享模式编辑之后保存为私有模板，同一模板下，有私有模板就展现私有模板</div></a>
<a href="#"><div>8.怎么配置定时更新Cube文件</div></a>
<a href="#"><div>9.Iframe 内嵌 BI报表</div></a>
<a href="#"><div>1.得到当前系统的用户名称，以及用户信息</div></a>

</body>
</html>