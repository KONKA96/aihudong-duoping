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
   <a href="/aihudong-duoping-web/index/getAccessToken?code=011J6t4O09hHCb2rvB2O0uil4O0J6t4N&state=127.0.0.1" >登录了</a>
</div>

</body>

</html>
