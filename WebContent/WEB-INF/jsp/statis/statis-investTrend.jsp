
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
<link rel="stylesheet" type="text/css" href="${ctx}/themes/css/base.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/base.js"></script>
<script type="text/javascript" src="${ctx}/js/prefixfree.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.transit.min.js"></script>
 <script type="text/javascript" src="${ctx}/common/extend.js"></script>
<style>
</style>
<script type="text/javascript"> 
    $(window).load(function(){     
            $("#zdyw-menu").show();
            $("#zdyw").addClass("active");
            $("#base").removeClass("active");
            $("#base-menu").hide();
            $("li:contains('投资形势分析')").addClass("active");
      
    });
</script>    
</head>
<body>
<div id="logo"></div>
<%--引入公用文件--%>
<jsp:include page="./../common/toolbar.jsp"></jsp:include>
<div id="contentDiv">
    <span class="loginInfo"><img src="${ctx}/themes/images/user.png">${SYS_USER_INFO}<a href="javascript:ZHFX.logout()">【退出】</a></span>
    <span class="currenttWz">当前位置：重点业务 > 投资形势分析</span>
    <ul class="chartLi">
        <li>
            <div onclick="ZHFX.toUrl('/statis/statis!dicData?flags=A1301&type=1')">
                <span class="jbg topLeft"></span>
                <span class="jbg topRight"></span>
                <span class="jbg bottomLeft"></span>
                <span class="jbg bottomRight"></span>
                <img src="${ctx}/themes/images/yhfw-6.png"/>
                <i>固定资产投资情况</i>
            </div>
        </li>
        <li>
            <div onclick="ZHFX.toUrl('/statis/statis!dicData?flags=A1302&type=2')">
                <span class="jbg topLeft"></span>
                <span class="jbg topRight"></span>
                <span class="jbg bottomLeft"></span>
                <span class="jbg bottomRight"></span>
                <img src="${ctx}/themes/images/yhfw-6.png"/>
                <i>按计划管理渠道分固定资产投资</i>
            </div>
        </li>
        <li>
            <div onclick="ZHFX.toUrl('/statis/statis!dicData?flags=A1304&type=3')">
                <span class="jbg topLeft"></span>
                <span class="jbg topRight"></span>
                <span class="jbg bottomLeft"></span>
                <span class="jbg bottomRight"></span>
                <img src="${ctx}/themes/images/yhfw-6.png"/>
                <i>固定资产投资资金来源情况</i>
            </div>
        </li>
        <li>
            <div onclick="ZHFX.toUrl('/statis/statis!dicData?flags=A130501&type=4')">
                <span class="jbg topLeft"></span>
                <span class="jbg topRight"></span>
                <span class="jbg bottomLeft"></span>
                <span class="jbg bottomRight"></span>
                <img src="${ctx}/themes/images/yhfw-6.png"/>
                <i>按行业固定资产投资情况-第一产业固定资产投资额</i>
            </div>
        </li>
        <li>
            <div onclick="ZHFX.toUrl('/statis/statis!dicData?flags=A130502&type=5')">
                <span class="jbg topLeft"></span>
                <span class="jbg topRight"></span>
                <span class="jbg bottomLeft"></span>
                <span class="jbg bottomRight"></span>
                <img src="${ctx}/themes/images/yhfw-6.png"/>
                <i>按行业固定资产投资情况-第二产业固定资产投资额</i>
            </div>
        </li>
        <li>
            <div onclick="ZHFX.toUrl('/statis/statis!dicData?flags=A130503&type=6')">
                <span class="jbg topLeft"></span>
                <span class="jbg topRight"></span>
                <span class="jbg bottomLeft"></span>
                <span class="jbg bottomRight"></span>
                <img src="${ctx}/themes/images/yhfw-6.png"/>
                <i>按行业固定资产投资情况-第三产业固定资产投资额</i>
            </div>
        </li>
        <li>
            <div onclick="ZHFX.toUrl('/statis/statis!dicData?flags=A130701&type=7')">
                <span class="jbg topLeft"></span>
                <span class="jbg topRight"></span>
                <span class="jbg bottomLeft"></span>
                <span class="jbg bottomRight"></span>
                <img src="${ctx}/themes/images/yhfw-6.png"/>
                <i>分行业固定资产投资情况-农林牧渔业固定资产投资额</i>
            </div>
        </li>
    </ul>
</div>
</body>
</html>