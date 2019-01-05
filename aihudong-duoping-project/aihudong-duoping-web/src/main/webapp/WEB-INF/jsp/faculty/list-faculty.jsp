<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>院系管理</title>
<jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>
							<small>院系管理</small>
						</h5>
						<div class="ibox-tools">
							<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
							</a>
						</div>
					</div>
					<div class="ibox-content">
						<div class="dataTables_wrapper form-inline" role="grid">
							<!-- 查询条件 -->
							<form id="searchForm" method="post" action="/aihudong-duoping-web/faculty/showAllFaculty">
								<div class="row">
									<div class="col-sm-2">
										<div class="dataTables_length">
											<a href="/aihudong-duoping-web/faculty/toUpdate" class="btn btn-primary ">新增院系</a>
										</div>
									</div>
									<div class="col-sm-10">
										<div class="input-group" style="float: right;">
											<input type="text" class="form-control" name="facultyName" value="${faculty.facultyName }" placeholder="院系名称">
											<div class="input-group-btn">
												<input type="submit" class="btn btn-primary" value="搜索">
											</div>
										</div>
									</div>
								</div>
							</form>
							<!-- 查询条件结束 -->
							<table
								class="table table-striped table-bordered table-hover dataTables-example">
								<thead>
									<tr>
										<th>ID</th>
										<th>院系名称</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${facultyList }" var="faculty">
										<tr class="gradeA">
											<td>${faculty.id }</td>
											<td>
												<a href="javascript:;"
												onclick="dialogShow(${faculty.id })"> <i class="fa fa-plus">${faculty.facultyName }</i>
											</a>
											</td>
											<td><a href="javascript:;"
												onclick="addFacultyValue(${faculty.id });"><i
													style="margin-left: 5px;" class="fa fa-plus"></i></a> 
												<a href="/aihudong-duoping-web/faculty/toUpdate?id=${faculty.id }"><i
													style="margin-left: 5px;" class="fa fa-edit"></i></a> 
												<a href="javascript:;" onclick="deleteFaculty(${faculty.id })"><i style="margin-left: 5px;" class="fa fa-trash"></i></a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<!-- 分页 -->
							<jsp:include page="../common/include_page.jsp">
 								<jsp:param value="/aihudong-duoping-web/faculty/showAllFaculty" name="pageTitle"/>
							</jsp:include>
							<!-- 分页结束 -->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="../common/include_js.jsp" />
	
	
	
	<!-- 弹窗 -->
	<div id="diaLogDiv" style="display: none;">
		<div class="ibox-content">
			<div class="dataTables_wrapper form-inline" role="grid">
				<table
					class="table table-striped table-bordered table-hover dataTables-example">
					<thead>
						<tr>
							<th>ID</th>
							<th>科目</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="tbody">
					
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		
		function deleteFaculty(id){
			var f=window.confirm("你确认要删除吗?");
			if(f){
				$.ajax({
					url:"/aihudong-duoping-web/faculty/deleteFaculty",
					data:"id="+id,
					type:"post",
					success:function(data){
						if(data=='success'){
							alert("操作成功！");
							window.location="/aihudong-duoping-web/faculty/showAllFaculty";
						}else{
							alert("操作失败");
						}
					}
				})
			}
		}
	
		function deleteAttrValue(id){
			var f=window.confirm("你确认要删除吗?");
			if(f){
				$.ajax({
					url:"/aihudong-duoping-web/subject/deleteSubject",
					data:"id="+id,
					type:"post",
					success:function(data){
						if(data=='success'){
							alert("操作成功！");
							window.location="/aihudong-duoping-web/faculty/showAllFaculty";
						}else{
							alert("操作失败");
						}
					}
				})
			}
		}
	
		function dialogShow(id){
			 $("#tbody").empty();
			 $.ajax({
					url:"/aihudong-duoping-web/faculty/getSubjectFromFaculty",
					data:"id="+id,
					type:"post",
					dataType:"json",
					async:false,
					success:function(data){
						/* data=eval('('+data+')'); */
						for(var i=0;i<data.length;i++){
							$("#tbody").append("<tr><td>"+data[i].id+"</td><td>"+data[i].subjectName+"</td><td><a href='javascript:;' onclick='deleteAttrValue("+data[i].id+")''><i style='margin-left: 5px;' class='fa fa-trash'></i></a></td></tr>");
						}
					}
				})
			
			
			layer.open({
				type : 1,
				skin : 'layui-layer-rim', //加上边框
				area : [ '880px', '540px' ], //宽高
				content : $("#diaLogDiv").html()//内容
			});
		}
		
		function addFacultyValue(id){
			swal({   
				title: "新增科目",   
				text: "",   
				type: "input",   
				showCancelButton: true,   
				closeOnConfirm: false,   
				animation: "slide-from-top",   
				inputPlaceholder: "科目",
				confirmButtonText: "确定",
                cancelButtonText: "取消",
				//inputValue:"123"  //回显时使用该属性
			}, 
			function(inputValue){   
				if (inputValue === false) 
					return false;      
				if (inputValue === "") {     
					swal.showInputError("科目不能为空!");     
					return false   
				}else{
					//在这里触发ajax进行新增
					$.ajax({
						url:"/aihudong-duoping-web/subject/updateInfo?subjectName="+inputValue+"&facultyId="+id,
						type:"post",
						success:function(data){
							if(data=='success'){
								alert("操作成功！");
								window.location="/aihudong-duoping-web/faculty/showAllFaculty";
							}else if(data=='exist'){
								alert("科目已存在！");
							}else{
								alert("操作失败！");
							}
						}
					})
				}
				
			});
		}
		
	</script>
</body>
</html>
