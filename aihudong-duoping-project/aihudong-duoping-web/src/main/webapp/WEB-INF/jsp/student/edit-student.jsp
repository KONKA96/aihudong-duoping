<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学生信息</title>
    <jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>学生</small></h5>
                        <div class="ibox-tools">
                        	<button class="btn btn-primary" onclick="goback()">返回</button>
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal" id="editForm">
                        	<input type="hidden" name="id" value="${student.id }">
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">用户名</label>

                                <div class="col-sm-10">
                                    <input id="username" name="username" value="${student.username }" type="text" class="form-control" placeholder="用户名">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">真实姓名</label>
                                <div class="col-sm-10">
                                    <input id="truename" name="truename" value="${student.truename }" type="text" class="form-control" placeholder="真实姓名">
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系方式</label>
                                <div class="col-sm-10">
                                    <input name="telephone" value="${student.telephone }" type="text" class="form-control" placeholder="联系方式">
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">电子邮箱</label>
                                <div class="col-sm-10">
                                    <input name="email" value="${student.email }" type="text" class="form-control" placeholder="电子邮箱">
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">性别</label>
                                <div class="col-sm-10">
                                    <select class="form-control m-b" name="sex">
                                    		<option value="0" ${student.sex==0 ? 'selected' :'' }>男</option>
                                    		<option value="1" ${student.sex==1 ? 'selected' :'' }>女</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">院系</label>
                                <div class="col-sm-10">
                                    <select class="form-control m-b" onchange="changeSubject(this)">
                                    	<option>--请选择--</option>
	                                    <c:forEach items="${facultyList }" var="faculty">
	                                    	<option value="${faculty.id}" ${student.subject.faculty.id==faculty.id ? 'selected' : '' }>${faculty.facultyName }</option>
	                                    </c:forEach>
                                    </select>
                                </div>
                                
                                <label class="col-sm-2 control-label">科目</label>
                                <div class="col-sm-10">
                                    <select class="form-control m-b" id="subjectSelected" name="subjectId">
                                    	<option value="">--请选择--</option>
                                    	<c:forEach items="${facultyList }" var="faculty">
                                    		<c:if test="${student.subject.faculty.id==faculty.id}">
                                    			<c:forEach items="${faculty.subjectList }" var="subject">
                                    				<option value="${subject.id}" ${student.subject.id==subject.id ? 'selected' : '' }>${subject.subjectName }</option>
                                    			</c:forEach>
                                    		</c:if>
	                                    </c:forEach>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <div class="col-sm-4 col-sm-offset-2">
                                    <button class="btn btn-primary" type="button" onclick="updateInfo()">保存</button>
                                    <button class="btn btn-white" type="reset">取消</button>
                                    <c:if test="${student.id!=null }">
	                                    <button class="btn btn-danger" type="button" onclick="resetPwd('${student.id}')">重置密码</button>
                                    </c:if>
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
	
	
	function changeSubject(object){
		var index=object.value;
		var arrayId = new Array();
		var arrayName = new Array();
		<c:forEach items="${facultyList }" var="e">
			<c:forEach items="${e.subjectList }" var="subject"> 
				if(${subject.facultyId}==index){
					arrayName.push("${subject.subjectName}"); //js中可以使用此标签，将EL表达式中的值push到数组中
					arrayId.push(${subject.id});
				}
			</c:forEach>
		</c:forEach>
		$("#subjectSelected").empty();
		$("#subjectSelected").append("<option>--请选择--</option>");
		
		for(var i=0;i<arrayName.length;i++){
			$("#subjectSelected").append("<option value='"+arrayId[i]+"'>"+arrayName[i]+"</option>");
		}
	}

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
		}else if($("#truename")[0].value==""){
			alert("姓名必填");
			return false;
		}else if($("#subjectSelected")[0].value==""){
			alert("专业必填");
			return false;
		}
		
		$.ajax({
			url:"/aihudong-duoping-web/student/updateInfo",
			data:$("#editForm").serialize(),
			type:"post",
			success:function(data){
				if(data=='success'){
					alert("操作成功！");
					window.location="/aihudong-duoping-web/student/showAllStudent";
				}else if(data=='exists'){
					alert("用户名不能重复！");
				}else{
					alert("操作失败");
				}
			}
		})
	}
	
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
				url : "/aihudong-duoping-web/student/testStudentOldPwd",
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
					url : "/aihudong-duoping-web/student/updateInfo",
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
	
	function goback(){
		window.history.back();
	}
</script>
</html>
