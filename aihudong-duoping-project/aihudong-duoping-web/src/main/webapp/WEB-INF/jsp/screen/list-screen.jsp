<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>屏幕管理</title>
<jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>
							<small>屏幕管理</small>
						</h5>
						<div class="ibox-tools">
							<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
							</a>
						</div>
					</div>
					<div class="ibox-content">
						<div class="dataTables_wrapper form-inline" role="grid">
							<!-- 查询条件 -->
							<form method="post" id="searchForm"
								action="/aihudong-duoping-web/screen/showAllScreen">
								<div class="row">
									<c:if test="${admin.power!=1 }">
									<div class="col-sm-2">
										<div class="dataTables_length">
											<a href="/aihudong-duoping-web/screen/toUpdateScreen"
												class="btn btn-primary ">新增屏幕</a>
										</div>
									</div>
									</c:if>
									<div class="col-sm-10">
										<div class="input-group">
											<select class="form-control m-b" onchange="changeZone(this)" name="room.building.zone.id">
												<option value="">--请选择--</option>
												<c:forEach items="${zoneList }" var="zone">
													<option value="${zone.id}"
														${zone.id==screen.room.building.zone.id ? 'selected' : '' }>${zone.zoneName }</option>
												</c:forEach>
											</select>
										</div>
										<div class="input-group">
											<select id="buildingSelected" class="form-control m-b"
												onchange="changeBuilding(this)" name="room.building.id">
												<option value="">--请选择--</option>
												<c:forEach items="${zoneList }" var="zone">
													<c:if test="${zone.id==screen.room.building.zone.id }"></c:if>
													<c:forEach items="${zone.buildingList }" var="building">
														<option value="${building.id}"
															${building.id==screen.room.building.id ? 'selected' : '' }>${building.buildingName }</option>
													</c:forEach>
												</c:forEach>
											</select>
										</div>

										<div class="input-group">
											<select id="roomSelected" class="form-control m-b"
												name="roomId">
												<option value="">--请选择--</option>
												<c:forEach items="${zoneList }" var="zone">
													<c:if test="${zone.id==screen.room.building.zone.id }"></c:if>
													<c:forEach items="${zone.buildingList }" var="building">
														<c:if test="${building.id==screen.room.building.id}">
															<c:forEach items="${building.roomList }" var="room">
																<c:if test="${room.id==screen.roomId }">
																	<option value="${room.id}"
																		${room.id==screen.roomId ? 'selected' : '' }>${room.num }</option>
																</c:if>
															</c:forEach>
														</c:if>
													</c:forEach>
												</c:forEach>
											</select>
										</div>

										<div class="input-group" style="float: right;">
											<!-- 关键字 -->
											<input type="text" name="duration"
												value="${screen.duration }" class="form-control"
												placeholder="关键字查找">
											<div class="input-group-btn">
												<input type="submit" class="btn btn-primary" value="搜索">
											</div>
										</div>
									</div>
								</div>
							</form>
							<!-- 查询条件结束 -->
							<c:if test="${admin.power==0 }">
								<form method="POST" enctype="multipart/form-data" id="form1"
									action="/aihudong-duoping-web/screen/uploadScreenExcel">
									<table>
										<tr>
											<!-- <td><label for="upfile" class="btn btn-success">Excel批量导入</label></td>
											<td><input id="upfile" type="file" name="upfile"
												class="btn btn-info" style="display: none"></td>
											<td><input type="submit" value="提交"
												onclick="return checkData()" class="btn btn-primary"></td> -->
											<td><a href="/aihudong-duoping-web/screen/exportExcel" class="btn btn-success">Excel批量导出</a></td>
										</tr>
									</table>
								</form>
							</c:if>
							<table
								class="table table-striped table-bordered table-hover dataTables-example">
								<thead>
									<tr>
										<th>校区</th>
										<th>教学楼</th>
										<th>教室</th>
										<th>屏幕账号</th>
										<th>屏幕分配者</th>
										<th>使用总时长</th>
										<th>使用总人次</th>
										<th>屏幕类型</th>
										<c:if test="${admin.power==0 || admin.power==2}">
											<th>操作</th>
										</c:if>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${screenList }" var="screen">
										<tr class="gradeA">
											<td>${screen.room.building.zone.zoneName }</td>
											<td>${screen.room.building.buildingName }</td>
											<td>${screen.room.num }</td>
											<td>${screen.username }</td>
											<td>${screen.admin.truename }</td>
											<td>${screen.duration }</td>
											<td>${screen.number }</td>
											<td>
												<c:if test="${screen.type=='1' }">触摸屏</c:if>
												<c:if test="${screen.type=='2' }">文档屏</c:if>
												<c:if test="${screen.type=='3' }">投影</c:if>
												<c:if test="${screen.type=='4' }">电视</c:if>
												<c:if test="${screen.type=='5' }">临时屏幕</c:if>
											</td>
											<c:if test="${admin.power==0 || admin.power==2}">
												<td><a
													href="/aihudong-duoping-web/screen/toUpdateScreen?id=${screen.id }"><i
														style="margin-left: 5px;" class="fa fa-edit"></i></a> <a
													href="javascript:;" onclick="deleteScreen('${screen.id }')"><i
														style="margin-left: 5px;" class="fa fa-trash"></i></a> <a
													href="/aihudong-duoping-web/screen/screenTransfer?id=${screen.id }"><i
														style="margin-left: 5px;" class="fa fa-arrow-right"></i></a></td>
											</c:if>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<!-- 分页 -->
							<jsp:include page="../common/include_page.jsp">
								<jsp:param value="/aihudong-duoping-web/screen/showAllScreen"
									name="pageTitle" />
							</jsp:include>
							<!-- 分页结束 -->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="../common/include_js.jsp" />

</body>
<script type="text/javascript">
function changeZone(object){
	$.ajax({
		url:"/aihudong-duoping-web/screen/getZone",
		data:"id="+object.value,
		type:"post",
		success:function(data){
			$("#buildingSelected").empty();
			$("#roomSelected").empty();
			$("#roomSelected").append("<option value=''>---请选择---</option>");
			$("#buildingSelected").append("<option value=''>---请选择---</option>");
			for(var i=0;i<data.length;i++){
				if(data[i].zoneId==object.value){
					$("#buildingSelected").append("<option value='"+data[i].id+"'>"+data[i].buildingName+"</option>");
				}
			}
		}
	})
}
function changeBuilding(object){
	$.ajax({
		url:"/aihudong-duoping-web/screen/getRoom",
		data:"id="+object.value,
		type:"post",
		success:function(data){
			$("#roomSelected").empty();
			$("#roomSelected").append("<option value=''>---请选择---</option>");
			for(var i=0;i<data.length;i++){
				$("#roomSelected").append("<option value='"+data[i].id+"'>"+data[i].num+"</option>");
			}
		}
	})
}

//JS校验form表单信息  
function checkData(){  
   var fileDir = $("#upfile").val();  
   var suffix = fileDir.substr(fileDir.lastIndexOf("."));  
   if("" == fileDir){  
       alert("选择需要导入的Excel文件！");  
       return false;  
   }  
   if(".xls" != suffix && ".xlsx" != suffix ){  
       alert("选择Excel格式的文件导入！");  
       return false;  
   }  
   return true;  
} 


	function deleteScreen(id){
		var f=window.confirm("你确定要删除这项吗？");
		if(f){
			$.ajax({
				url:"/aihudong-duoping-web/screen/deleteScreen",
				data:"id="+id,
				type:"post",
				success:function(data){
					if(data=='success'){
						alert("操作成功！");
						window.location="/aihudong-duoping-web/screen/showAllScreen";
					}else{
						alert("操作失败");
					}
				}
			})
		}
		
	}
</script>
</html>
