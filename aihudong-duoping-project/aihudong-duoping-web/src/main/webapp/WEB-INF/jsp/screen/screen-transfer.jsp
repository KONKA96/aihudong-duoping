<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>屏幕转移</title>
<jsp:include page="../common/include_css.jsp" />
<jsp:include page="../common/include_js.jsp" />
</head>
<body>
	<br />
	<br />
	<form class="form-horizontal" id="editForm">
	<input type="hidden" name="id" value="${screen.id }" />
	<div
		style="width: 700px; height: 500px; text-align: center; margin: 0 auto;">
		屏幕ID：<input type="text" name="id" class="btn btn-primary" disabled="disabled"
			value="${screen.id }" />
		<div>
			<br />
			<br />
			<div>
				教学楼&nbsp;&nbsp;:&nbsp;&nbsp;<input type="text" class="form-control" disabled="disabled"
					value="${screen.room.building.buildingName }"
					style="display: inline-block; width: 100px;" /> &nbsp;&nbsp; 
					<i style="margin-left: 5px;" class="fa fa-arrow-right"></i>
				<div class="form-group" style="display:inline-block;">
						<select id="buildingSelected" class="form-control m-b" style="width:150px;"
							onchange="changeBuilding(this)">
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

			</div>
			<br />
			<br /> 教室&nbsp;&nbsp;:&nbsp;&nbsp;<input type="text" disabled="disabled"
				class="form-control" value="${screen.room.num }"
				style="display: inline-block; width: 100px;" /> &nbsp;&nbsp; <i
				style="margin-left: 5px;" class="fa fa-arrow-right"></i>
			<div class="form-group" style="display:inline-block;">
					<select id="roomSelected" class="form-control m-b" name="roomId" style="width:150px;">
						<option value="">--请选择--</option>
						<c:forEach items="${zoneList }" var="zone">
							<c:if test="${zone.id==screen.room.building.zone.id }"></c:if>
							<c:forEach items="${zone.buildingList }" var="building">
								<c:if test="${building.id==screen.room.building.id}">
									<c:forEach items="${building.roomList }" var="room">
										<%-- <c:if test="${room.id==screen.room.id }"> --%>
											<option value="${room.id}"
												${room.id==screen.room.id ? 'selected' : '' }>${room.num }</option>
										<%-- </c:if> --%>
									</c:forEach>
								</c:if>
							</c:forEach>
						</c:forEach>
					</select>
			</div>
		</div>
		<br /> <button class="btn btn-primary" type="button" onclick="updateInfo()">保存</button>
		<button type="button" class="btn btn-default">取消</button>
	</div>
	</form>
</body>
<script type="text/javascript">
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

function updateInfo(){
	$.ajax({
		url:"/aihudong-duoping-web/screen/updateScreen",
		data:$("#editForm").serialize(),
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
</script>
</html>