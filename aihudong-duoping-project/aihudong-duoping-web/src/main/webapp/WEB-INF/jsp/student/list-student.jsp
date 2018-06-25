<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学生管理</title>
    <jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>学生管理</small></h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                    	<div  class="dataTables_wrapper form-inline" role="grid">
                    		<!-- 查询条件 -->
	                    	<form method="post" id="searchForm" action="/aihudong-duoping-web/student/showAllStudent">
		                    	<div class="row">
		                    		<div class="col-sm-2">
		                    			<div class="dataTables_length">
		                    				<a href="/aihudong-duoping-web/student/toUpdate" class="btn btn-primary ">新增学生</a>
		                    			</div>
		                    		</div>
		                    		<div class="col-sm-10">
		                    			<div class="input-group">
		                    				<!-- 根据院系、专业查询 -->
		                    				<select class="form-control" onchange="changeSubject(this)" name="facultyId">
		                    					<option value="">--请选择--</option>
			                                    <c:forEach items="${facultyList }" var="faculty">
			                                    	<option value="${faculty.id}" ${student.subject.faculty.id==faculty.id ? 'selected' : '' }>${faculty.facultyName }</option>
			                                    </c:forEach>
                                    		</select>
		                    			</div>
		                    			<div class="input-group">
		                    				<select class="form-control" id="subjectSelected" name="subjectId">
		                    					<option value="">--请选择--</option>
		                    					<c:forEach items="${facultyList }" var="faculty">
													<c:if test="${requestScope.student.subject.faculty.id==faculty.id}">
														<c:forEach items="${faculty.subjectList }" var="subject">
															<option value="${subject.id}"
																${requestScope.student.subjectId==subject.id ? 'selected' : '' }>${subject.subjectName }</option>
														</c:forEach>
													</c:if>
												</c:forEach>
                                    		</select>
		                    			</div>
		                    			
		                    			<div class="input-group" style="float:right;">
		                    				<!-- 真实姓名 -->
				                            <input type="text" name="username" value="${student.username }" class="form-control" placeholder="关键字查找">
				                            <div class="input-group-btn">
				                                <input type="submit"  class="btn btn-primary" value="搜索">
				                            </div>
				                        </div>
		                    		</div>
		                    	</div>
	                    	</form>
                    		<!-- 查询条件结束 -->
                    		<c:if test="${admin.power==0 }">
                    		<form method="POST"  enctype="multipart/form-data" id="form1" action="/aihudong-duoping-web/student/uploadExcel">  
						        <table>  
						        <tr>  
						        	<td><label for="upfile"class="btn btn-success">Excel批量导入</label></td> 
						        	<td><input id="upfile" type="file" name="upfile" class="btn btn-info" style="display:none"></td>  
						            <td><input type="submit" value="提交" onclick="return checkData()" class="btn btn-primary"></td>  
						         </tr>  
						        </table>    
						    </form>
						    </c:if>
	                        <table class="table table-striped table-bordered table-hover dataTables-example">
	                            <thead>
	                                <tr>
	                                	<th>用户名</th>
	                                    <th>真实姓名</th>
	                                    <th>院系</th>
	                                    <th>科目</th>
	                                    <th>联系方式</th>
	                                    <th>性别</th>
	                                    <th>备注</th>
	                                    <th>操作</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                            	<c:forEach items="${studentList }" var="student">
	                            		<tr class="gradeA">
	                            			 <td>${student.username }</td>
		                            		 <td>${student.truename }</td>
		                            		 <td>${student.subject.faculty.facultyName }</td>
		                            		 <td>${student.subject.subjectName }</td>
		                            		 <td>${student.telephone }</td>
		                            		 <td>
		                            		 	<c:if test="${student.sex==0 }">男</c:if>
		                            		 	<c:if test="${student.sex==1 }">女</c:if>
		                            		 </td>
		                            		 <td>${student.remake }</td>
			                                 <td>
			                                 	<a href="/aihudong-duoping-web/student/toUpdate?id=${student.id }"><i style="margin-left:5px;" class="fa fa-edit"></i></a>
			                                 	<a href="javascript:;" onclick="deleteStudent('${student.id }')"><i style="margin-left:5px;" class="fa fa-trash"></i></a>
			                                 </td>
		                            	</tr>
		                            </c:forEach>
	                            </tbody>
	                        </table>
							<!-- 分页 -->
							<jsp:include page="../common/include_page.jsp">
 								<jsp:param value="/aihudong-duoping-web/student/showAllStudent" name="pageTitle"/>
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
	$("#subjectSelected").append("<option value=''>--请选择--</option>");
	
	for(var i=0;i<arrayName.length;i++){
		$("#subjectSelected").append("<option value='"+arrayId[i]+"'>"+arrayName[i]+"</option>");
	}
}

	function deleteStudent(id){
		var f=window.confirm("你确定要删除这项吗？");
		if(f){
			$.ajax({
				url:"/aihudong-duoping-web/student/deleteStudent",
				data:"id="+id,
				type:"post",
				success:function(data){
					if(data=='success'){
						alert("操作成功！");
						window.location="/aihudong-duoping-web/student/showAllStudent";
					}else{
						alert("操作失败");
					}
				}
			})
		}
		
	}
</script>
</html>
