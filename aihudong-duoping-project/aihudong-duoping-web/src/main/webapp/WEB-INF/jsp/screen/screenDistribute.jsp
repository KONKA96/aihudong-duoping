<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<jsp:include page="../common/include_css.jsp" />
<jsp:include page="../common/include_js.jsp" />
<body>
	<c:if test="${admin.power==0 }">
	<div style="display:inline-block;">
	<div class=" alert alert-info" 
		style="width: 450px; height: 35px; padding-top: 0px; padding-left: 0; margin-bottom: 0; margin-top: 10px">
		<button type="button" class=" btn btn-link" data-toggle="collapse"
			data-target="#demo1">
			<span class=" glyphicon glyphicon-plus"></span>
		</button>
		<span onclick="showAdminInfo(${admin.id})">${admin.truename }&nbsp;&nbsp;屏幕数：${admin.screenNum }&nbsp;&nbsp;剩余屏幕数：${admin.screenRemain }</span> 
		<button onclick="addAdmin(${admin.id},1)">新增一级管理员</button>
	</div>
	<div id="demo1" class="collapse in"
		style="margin-left: 40px; margin-bottom: 10px; margin-top: 0px">
		<c:forEach items="${adminYijiList }" var="admin1">
			<div class=" alert alert-info" 
				style="margin-top: 20px; width: 410px; height: 35px; padding-top: 7px; padding-left: 15px; margin-bottom: 0">
				<button type="button" class=" btn btn-link" data-toggle="collapse"
					data-target="#demo${admin1.id }">
					<span class=" glyphicon glyphicon-plus"></span>
				</button>
				<span onclick="showAdminInfo(${admin1.id})">${admin1.truename }&nbsp;&nbsp;屏幕数：${admin1.screenNum }&nbsp;&nbsp;剩余屏幕数：${admin1.screenRemain }</span>
				<button onclick="addAdmin(${admin1.id},2)">新增操作管理员</button>
			</div>
			<div id="demo${admin1.id }" class="collapse in"
				style="margin-left: 80px; margin-bottom: 10px; margin-top: 0px">
				<c:forEach items="${admin1.adminList }" var="admin2">
					<c:if test="${admin2.id!=null }">
						<div class=" alert alert-info" 
							style="margin-top: 10px; width: 330px; height: 35px; padding-top: 7px; padding-left: 15px; margin-bottom: 0">
							<span onclick="showAdminInfo(${admin2.id})">${admin2.truename }&nbsp;&nbsp;屏幕数：${admin2.screenNum }&nbsp;&nbsp;剩余屏幕数：${admin2.screenRemain }</span>
						</div>
					</c:if>
				</c:forEach>

			</div>
		</c:forEach>
	</div>
	</div>
	</c:if>
	<c:if test="${admin.power==1 }">
		<div style="display:inline-block;">
			<div class=" alert alert-info" 
				style="margin-top: 20px; width: 410px; height: 35px; padding-top: 7px; padding-left: 15px; margin-bottom: 0">
				<button type="button" class=" btn btn-link" data-toggle="collapse"
					data-target="#demo${admin.id }">
					<span class=" glyphicon glyphicon-plus"></span>
				</button>
				<span onclick="showAdminInfo(${admin.id})">${admin.truename }&nbsp;&nbsp;屏幕数：${admin.screenNum }&nbsp;&nbsp;剩余屏幕数：${admin.screenRemain }</span>
				<button onclick="addAdmin(${admin1.id},2)">新增操作管理员</button>
			</div>
			<div id="demo${admin.id }" class="collapse in"
				style="margin-left: 80px; margin-bottom: 10px; margin-top: 0px">
				<c:forEach items="${admin.adminList }" var="admin1">
						<div class=" alert alert-info" 
							style="margin-top: 10px; width: 330px; height: 35px; padding-top: 7px; padding-left: 15px; margin-bottom: 0">
							<span onclick="showAdminInfo(${admin1.id})">${admin1.truename }&nbsp;&nbsp;屏幕数：${admin1.screenNum }&nbsp;&nbsp;剩余屏幕数：${admin1.screenRemain }</span>
						</div>
				</c:forEach>

			</div>
		</div>
	</c:if>
	<!-- 弹窗 -->
	<div id="diaLogDiv" style="display: none;">
		<div class="ibox-content">
			<div class="dataTables_wrapper form-inline" role="grid">
			 <form class="form-horizontal" id="editForm">
				<table
					class="table table-striped table-bordered table-hover dataTables-example">
					<thead>
						<tr>
							<th>真实姓名</th>
							<th>性别</th>
							<th>电话</th>
							<th>email</th>
							<th>屏幕数</th>
						</tr>
					</thead>
					<tbody id="tbody">
					
					</tbody>
				</table>
					<button class="btn btn-primary" type="button" onclick="updateInfo()">保存</button>
				</form>
			</div>
		</div>
	</div>
	
	
	<!-- 弹窗 -->
	<div id="diaLogDivA" style="display: none;">
		<div class="ibox-content">
			<div class="dataTables_wrapper form-inline" role="grid">
			 <form class="form-horizontal" id="editFormA">
				<table
					class="table table-striped table-bordered table-hover dataTables-example">
					<thead>
						<tr>
							<th>用户名</th>
							<th>真实姓名</th>
							<th>性别</th>
							<th>屏幕数</th>
						</tr>
					</thead>
					<tbody id="tbodyA">
						<tr>
							<input id="powerInput" type="hidden" name="power" value="">
							<input id="higherInput" type="hidden" name="higherId" value="">
							<td><input type="text" class='form-control' name="username"></td>
							<td><input type="text" class='form-control' name="truename"></td>
							<td><input type="text" class='form-control' name="sex"></td>
							<td><input type="text" class='form-control' name="screenNum"></td>
						</tr>
					</tbody>
				</table>
					<button class="btn btn-primary" type="button" onclick="saveInfo()">保存</button>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	function saveInfo(){
		$.ajax({
			url:"/aihudong-duoping-web/admin/insertAdminScreen",
			data:$("#editFormA").serialize(),
			type:"post",
			success:function(data){
				if(data=='success'){
					alert("操作成功！");
					window.location="/aihudong-duoping-web/screen/screenDistribute";
				}else if(data=='error'){
					alert("分配的屏幕数量不能大于上一级的屏幕数量！");
				}else if(data=='exist'){
					alert("用户名不能重复！");
				}else{
					alert("操作失败！");
				}
			}
		})
	}
	
	function addAdmin(higherId,role){
		$("#diaLogDivA").css("display","inline-block");
		$("#powerInput").val(role);
		$("#higherInput").val(higherId);
	}
	
	function updateInfo(){
		$.ajax({
			url:"/aihudong-duoping-web/admin/updateScreenNum",
			data:$("#editForm").serialize(),
			type:"post",
			success:function(data){
				if(data=='success'){
					alert("操作成功！");
					window.location="/aihudong-duoping-web/screen/screenDistribute";
				}else if(data=='error'){
					alert("分配的屏幕数量不能大于上一级的屏幕数量！");
				}else if(data=='error2'){
					alert("分配的屏幕数量不能小于已经分配的屏幕数量！");
				}else{
					alert("操作失败！");
				}
			}
		})
	}
		function showAdminInfo(id){
			$("#tbody").empty();
			$.ajax({
				url:"/aihudong-duoping-web/admin/getAdminInfo",
				data:"id="+id,
				type:"post",
				dataType:"json",
				async:false,
				success:function(data){
					/* data=eval('('+data+')'); */
					$('#adminId').val(data.id);
					var sex;
					if(data.sex==0){sex='男';}else{sex='女';}
					$("#tbody").append("<tr><input type='hidden' name='id' class='form-control' value='"+data.id+"'><input type='hidden' name='power' class='form-control' value='"+data.power+"'><td>"+data.truename+"</td><td>"+sex+"</td><td>"+data.telephone+"</td><td>"+data.email+"</td><td><input type='text' name='screenNum' class='form-control' value='"+data.screenNum+"'></td></tr>");
				}
			})
			$("#diaLogDiv").css("display","inline-block");
			/* layer.open({
				type : 1,
				skin : 'layui-layer-rim', //加上边框
				area : [ '800px', '200px' ], //宽高
				content : $("#diaLogDiv").html()//内容
			}); */
		}
	
    </script>
</body>
</html>