<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<jsp:include page="../common/include_css.jsp" />
<body>
	<div class="panel panel-default">
		<div class="panel-heading">当前状态</div>
		<div class="panel-body">
			<ul>
				<li>剩余时间：${license.remainingTime}</li>
				<li>总屏幕数：${license.screenNum}</li>
				<li>总用户数：${license.peopleNum}</li>
				<li>总并发数：${license.screenNumSametime}</li>
				<li>版本号：${license.licenseId}</li>
				<li>软件大小：${license.licenseSize}</li>
				<li>权限类型：${license.licenseType}</li>
			</ul>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-heading">更新后状态</div>
		<div class="panel-body">
			<c:forEach items="${licenseList }" var="license">
				<ul>
					<li>剩余时间：${license.remainingTime}</li>
					<li>总屏幕数：${license.screenNum}</li>
					<li>总用户数：${license.peopleNum}</li>
					<li>总并发数：${license.screenNumSametime}</li>
					<li>版本号：${license.licenseId}</li>
					<li>软件大小：${license.licenseSize}</li>
					<li>权限类型：${license.licenseType}</li>
				</ul>
				<button type="button" class="btn btn-primary">检查更新</button>
			</c:forEach>
		</div>
	</div>
</body>
</html>