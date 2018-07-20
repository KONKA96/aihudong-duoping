<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<jsp:include page="common/include_css.jsp" />
<jsp:include page="common/include_js.jsp" />
<head>
    <base href="<%=basePath%>">

    <title>微信扫码登录</title>

</head>

<body>
<div>
   <a href="/aihudong-duoping-web/index/getAccessToken?code=06126i7F0EKzEk2pxk6F0YQv7F026i7w" >登录了</a>
</div>

</body>

</html>
