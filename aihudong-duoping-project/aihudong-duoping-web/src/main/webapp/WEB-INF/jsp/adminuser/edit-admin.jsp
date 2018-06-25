<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理员</title>
    <jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>管理员</small></h5>
                        <div class="ibox-tools">
                        	<button class="btn btn-primary" onclick="goback()">返回</button>
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal" id="editForm">
                        	<input type="hidden" name="id" value="${admin.id }">
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">用户名</label>

                                <div class="col-sm-10">
                                    <input id="username" name="username" value="${admin.username }" type="text" class="form-control" placeholder="用户名">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">密码</label>
                                <div class="col-sm-10">
                                    <input id="password" name="password" value="${admin.password }" type="text"  class="form-control" placeholder="密码">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">真实姓名</label>
                                <div class="col-sm-10">
                                    <input id="truename" name="truename" value="${admin.truename }" type="text" class="form-control" placeholder="真实姓名">
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系方式</label>
                                <div class="col-sm-10">
                                    <input name="telephone" value="${admin.telephone }" type="text" class="form-control" placeholder="联系方式">
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">电子邮箱</label>
                                <div class="col-sm-10">
                                    <input name="email" value="${admin.email }" type="text" class="form-control" placeholder="电子邮箱">
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">性别</label>
                                <div class="col-sm-10">
                                    <select class="form-control m-b" name="sex">
                                    		<option value="0" ${admin.sex==0 ? 'selected' :'' }>男</option>
                                    		<option value="1" ${admin.sex==1 ? 'selected' :'' }>女</option>
                                    </select>
                                </div>
                            </div>
                            <c:if test="${sessionScope.admin.power==0 }">
	                            <div class="form-group">
	                                <label class="col-sm-2 control-label">权限</label>
	                                <div class="col-sm-10">
	                                    <select class="form-control m-b" name="power" onchange="selectAdmin(this)">
	                                    	<option value="">--请选择--</option>
		                                    <option value="1" ${admin.power==1 ? 'selected' : '' }>一级管理员</option>
		                                    <option value="2" ${admin.power==2 ? 'selected' : '' }>操作管理员</option>
	                                    </select>
	                                </div>
	                            </div>
                            </c:if> 
                            
                               <!--  <div id="yijiadmin" class="form-group" style="display:none;">
	                                <label class="col-sm-2 control-label">一级管理员</label>
	                                <div class="col-sm-10">
	                                    <select id="yijiSelect" class="form-control m-b" name="higherId">
	                                    	
	                                    </select>
	                                </div>
	                            </div> -->
                            
                            <div class="form-group">
                                <div class="col-sm-4 col-sm-offset-2">
                                    <button class="btn btn-primary" type="button" onclick="updateInfo()">保存</button>
                                    <button class="btn btn-white" type="reset">取消</button>
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
	/* function selectAdmin(obj){
		if(obj.value==1||obj.value==''){
			$("#yijiSelect").empty();
			$("#yijiadmin").css("display","none");
		}else if(obj.value==2){
			$("#yijiadmin").css("display","block");
			$.ajax({
				url:"/aihudong-duoping-web/admin/selectAllYiJiAdmin",
				type:"post",
				success:function(data){
					$("#yijiSelect").append("<option value=''>---请选择---</option>");
					for (var i = 0; i < data.length; i++) {
						$("#yijiSelect").append("<option value='"+data[i].id+"'>"+data[i].truename+"</option>");
					}
				}
			})
		}
	} */

	$(document).ready(function () {
	    $('.i-checks').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green',
	    });
	});
	
	function updateInfo(){
		if($("#username")[0].value==""){
			alert("用户名必填");
			return false;
		}else if($("#password")[0].value==""){
			alert("密码必填");
			return false;
		}else if($("#truename")[0].value==""){
			alert("姓名必填");
			return false;
		}
		
		$.ajax({
			url:"/aihudong-duoping-web/admin/updateAdmin",
			data:$("#editForm").serialize(),
			type:"post",
			success:function(data){
				if(data=='success'){
					alert("操作成功！");
					window.location="/aihudong-duoping-web/admin/showAllAdmin";
				}else if(data=='exist'){
					alert("用户名不能重复！");
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
