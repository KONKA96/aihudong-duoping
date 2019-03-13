<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>屏幕</title>
    <jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>屏幕</small></h5>
                        <div class="ibox-tools">
                        	<button class="btn btn-primary" onclick="goback()">返回</button>
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal" id="editForm">
                        	<input type="hidden" name="id" value="${screen.id }">
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">屏幕名称</label>
                                <div class="col-sm-10">
                                    <input name="title" value="${screen.title }" type="text" class="form-control" placeholder="屏幕名称">
                                </div>
                            </div>
                        	
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">屏幕登录名</label>
                                <div class="col-sm-10">
                                    <input name="username" value="${screen.username }" type="text" class="form-control" placeholder="屏幕登录名">
                                </div>
                            </div>
                        	
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">校区</label>
                                <div class="col-sm-10">
                                    <select class="form-control m-b" onchange="changeZone(this)">
                                    	<option value="">--请选择--</option>
	                                    <c:forEach items="${zoneList }" var="zone">
	                                    	
	                                    	<option value="${zone.id}" ${zone.id==screen.room.building.zone.id ? 'selected' : '' }>${zone.zoneName }</option>
	                                    </c:forEach>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">教学楼</label>
                                <div class="col-sm-10">
                                    <select id="buildingSelected" class="form-control m-b" onchange="changeBuilding(this)">
                                    	<option value="">--请选择--</option>
	                                    <c:forEach items="${zoneList }" var="zone">
	                                    	<c:if test="${zone.id==screen.room.building.zone.id }"></c:if>
	                                    	<c:forEach items="${zone.buildingList }" var="building">
	                                    		<option value="${building.id}" ${building.id==screen.room.building.id ? 'selected' : '' }>${building.buildingName }</option>
	                                   		</c:forEach>
	                                    </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">教室</label>
                                <div class="col-sm-10">
                                    <select id="roomSelected" class="form-control m-b" name="roomId">
                                    	<option value="">--请选择--</option>
	                                    <c:forEach items="${zoneList }" var="zone">
	                                    	<c:if test="${zone.id==screen.room.building.zone.id }"></c:if>
	                                    	<c:forEach items="${zone.buildingList }" var="building">
	                                    		<c:if test="${building.id==screen.room.building.id}">
	                                    			<c:forEach items="${building.roomList }" var="room">
	                                    				<c:if test="${room.id==screen.room.id }">
	                                    					<option value="${room.id}" ${room.id==screen.room.id ? 'selected' : '' }>${room.num }</option>
	                                    				</c:if>
	                                    			</c:forEach>
	                                    		</c:if>
	                                   		</c:forEach>
	                                    </c:forEach>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">屏幕类型</label>
                                <div class="col-sm-10">
                                   <select class="form-control m-b" name="type">
                                   		<option value="1" ${screen.type==1 ? 'selected' : ''}>触摸屏</option>
                                   		<option value="2" ${screen.type==2 ? 'selected' : ''}>文档屏</option>
                                   		<option value="3" ${screen.type==3 ? 'selected' : ''}>投影</option>
                                   		<option value="4" ${screen.type==4 ? 'selected' : ''}>电视</option>
                                   		<option value="5" ${screen.type==5 ? 'selected' : ''}>临时屏幕</option>
                                   </select>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <div class="col-sm-4 col-sm-offset-2">
                                    <button class="btn btn-primary" type="button" onclick="updateInfo()">保存</button>
                                    <button class="btn btn-white" type="reset">取消</button>
                                    <button class="btn btn-danger" type="button" onclick="resetPwd('${screen.id}')">重置密码</button>
                                </div>
                            </div>
                        </form> 
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
				console.log(data);
				$("#roomSelected").append("<option value=''>---请选择---</option>");
				for(var i=0;i<data.length;i++){
					$("#roomSelected").append("<option value='"+data[i].id+"'>"+data[i].num+"</option>");
				}
			}
		})
	}
	
	$(document).ready(function () {
	    $('.i-checks').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green',
	    });
	});
	
	
	/* 重置用户密码 */
	function resetPwd(id){
		swal({
			title : "请输入新密码",
			text : "",
			type : "input",
			showCancelButton : true,
			closeOnConfirm : false,
			closeOnCancel : true,
			animation : "slide-from-top",
			inputPlaceholder : "密码",
			confirmButtonText : "确定",
			cancelButtonText : "取消",
		}, function(inputValue) {
			$.ajax({
				url : "/aihudong-duoping-web/screen/testScreenOldPwd",
				data : "id="+id+"&password="+inputValue ,
				type : "post",
				success : function(data) {
					if (data == 'success') {
						repeat(id,inputValue);
					} else if(data=='same'){
						swal("密码与旧密码不能相同!", "请重试", "error");
					}else{
						swal("用户不存在!", "请重试", "error");
					}
				}
			})
		})
	}
	
	function repeat(id,pwd){
		swal({
			title : "请再次输入新密码",
			text : "",
			type : "input",
			showCancelButton : true,
			closeOnConfirm : false,
			closeOnCancel : true,
			animation : "slide-from-top",
			inputPlaceholder : "密码",
			confirmButtonText : "确定",
			cancelButtonText : "取消",
		},function(inputValue){
			if(inputValue!=pwd){
				swal("两次输入的密码不一致!", "请重试", "error");
			}else{
				$.ajax({
					url : "/aihudong-duoping-web/screen/updateScreen",
					data : "id="+id+"&password="+inputValue ,
					type : "post",
					success : function(data) {
						if (data == 'success') {
							swal("重置成功!", "", "success");
						}else{
							swal("操作失败!", "请重试", "error");
						}
					}
				})
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
				}else if(data=='same'){
					alert("用户名不能相同！");
				}else{
					alert("操作失败");
				}
			}
		})
	}
	
	function goback(){
		window.history.back();
	}
</script>
</html>
