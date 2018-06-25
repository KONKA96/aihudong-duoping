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
                                <label class="col-sm-2 control-label">密码</label>
                                <div class="col-sm-10">
                                    <input id="password" name="password" value="${student.password }" type="text"  class="form-control" placeholder="密码">
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
					console.log(${subject.id});
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
		}else if($("#password")[0].value==""){
			alert("密码必填");
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
	
	function goback(){
		window.history.back();
	}
</script>
</html>
