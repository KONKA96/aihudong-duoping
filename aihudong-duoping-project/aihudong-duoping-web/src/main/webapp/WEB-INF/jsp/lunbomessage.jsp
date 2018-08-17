<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":"
			+ request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="<%=basePath %>resources/css/reset.min.css" rel="stylesheet">
<link href="<%=basePath %>resources/css/style-lunbo.css" rel="stylesheet">

</head>
<body>

<c:forEach items="${picList}" var="pic">
	<div class="preloader" src="/aihudong-duoping-web/upload/${pic }"></div>
</c:forEach>

<div id="wrapper">
<div id="nextBox">NEXT</div>
<input type="range" min="35" max="395" value="35" id="range"/>
</div>
<script src="<%=basePath%>resources/js/lunbo-index.js?v=0.0.2"></script>
<div style="text-align:center;margin:50px 0; font:normal 14px/24px 'MicroSoft YaHei';">
<p>适用浏览器：360、FireFox、Chrome、Opera、傲游、搜狗、世界之窗. 不支持Safari、IE8及以下浏览器。</p>
<p>来源：<a href="http://sc.chinaz.com/" target="_blank">站长素材</a></p>
</div>
</body>
</html>