<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<jsp:include page="common/include_css.jsp" />
<jsp:include page="common/include_js.jsp" />
</head>
<body>
	<script type="text/javascript">
		if(window.top.location.href!=location.href)      
		{         
		    window.top.location.href=location.href;      
		}  
	</script>
	<div style="width: 400px;margin-left: auto;margin-right: auto;margin-top: 200px;">
		<form class="form-horizontal" id="editForm">
			<div class="form-group">
				<label for="inputEmail3" class="col-sm-4 control-label">用户名</label>
				<div class="col-sm-8">
					<input type="text" class="form-control" 
						placeholder="请输入用户名" name="username">
					<p class="bg-danger" id="umAlt"></p>
				</div>
			</div>
			<div class="form-group">
				<label for="inputPassword3" class="col-sm-4 control-label">密码</label>
				<div class="col-sm-8">
					<input type="password" class="form-control" id="inputPassword3"
						placeholder="请输入密码" name="password">
					<p class="bg-danger" id="pwAlt"></p>
				</div>
			</div>

			<!-- <label for="inputPassword3" class="col-sm-4 control-label">角色：</label>
			<div class="col-sm-8">
				
				<select class="form-control m-b" name="power">
					<option selected>--请选择--</option>
					<option value="0">--根级管理员--</option>
					<option value="1">--一级管理员--</option>
					<option value="2">--操作管理员--</option>
				</select>
			</div> -->

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="button" class="btn btn-default" onclick="exeLogin()">登陆</button>
				</div>
			</div>
		</form>
	
	</div>
	<script type="text/javascript">
	$(document).keydown(function(event){
		if(event.keyCode==13){
			exeLogin();
		}
});
	
		function exeLogin(){
			$.ajax({
				url:"/aihudong-duoping-web/login/adminLogin",
				data:$("#editForm").serialize(),
				type:"post",
				success:function(data){
					if(data=='success'){
						alert("登录成功！");
						window.location="/aihudong-duoping-web/admin/test";
					}else if(data=='none'){
						alert("用户名不存在！");
					}else if(data=='error'){
						alert("密码错误！");
					}else {
						alert("登录失败！");
					}
				}
			})
		}
	</script>
</body>
</html>