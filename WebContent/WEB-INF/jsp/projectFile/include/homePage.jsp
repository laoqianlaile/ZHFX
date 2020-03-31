<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<div class="book_btn">
		<a href="#LV0"><button class='book_btn_back'></button></a>
		<c:if test="${innerPage == 1}">
		<a href="" onClick="history.go(-1)"><button class='book_back_list'></button></a>
		</c:if>
		<c:if test="${innerPage == ''}">
		<a href="javascript:document.location.reload();"><button class='book_back_list' ></button></a>
		</c:if>
	</div>
	<div class="book_cover">
		<div class="book_content">
			<div class="cover_name">项目档案</div>
			<h1 class="cover_title" id="projectNameTitle"></h1>
			<h2 class="cover_title" >
				<p id="proLegalUnitTitle"></p>
				<p id="storeTimeTitle"></p>
			</h2>
		</div>
	</div>