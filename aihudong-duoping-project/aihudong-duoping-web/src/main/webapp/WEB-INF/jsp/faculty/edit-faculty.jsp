<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>院系详情</title>
    <jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>院系详情</small></h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal" id="editForm">
                        	<input type="hidden" value="${faculty.id }" name="id">
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">院系名称</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="facultyName" value="${faculty.facultyName }">
                                </div>
                            </div>
                            <div class="hr-line-dashed"></div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">科目</label>
                                <div class="col-md-2">
                                	<a href="javascript:void(0);" onclick="addAttrValue();" class="btn btn-primary ">添加科目</a>
                                </div>
                            </div>
                            <div class="hr-line-dashed"></div>
                            <div class="form-group">
                            	<div id="valueDiv">
	                            	<!-- 属性值 -->
	                            	<!-- 显示现有属性值-->
	                            	<c:if test="${faculty!=null }">
	                            		<c:forEach items="${faculty.subjectList }" var="i" varStatus="subject">
	                            			<div class='form-group' >
	                            					<input type="hidden" value="${i.id }" name="subjectList[${subject.count-1}].id">
				                            		<label class='col-sm-2 control-label'></label>
				                            		<div class='col-md-8'>
				                            		
				                            			<input type='text' value="${i.subjectName }" class='form-control' name="subjectList[${subject.count-1}].subjectName" placeholder='科目名称'>
				                            		</div>
				                            		<div class='col-md-2'>
				                            			<a href='javascript:void(0);' class='btn btn-primary' onclick="delAjax(this)">删除科目</a>
				                            		</div>
				                            </div>
	                            		</c:forEach>
	                            	</c:if>
	                            	<c:if test="${faculty==null }">
		                            <div class='form-group' >
		                            		<label class='col-sm-2 control-label'></label>
		                            		<div class='col-md-8'>
		                            		
		                            			<input type='text' class='form-control' name="subjectList[0].subjectName" placeholder='科目名称'>
		                            		</div>
		                            		<div class='col-md-2'>
		                            			<a href='javascript:void(0);' class='btn btn-primary' onclick="delAjax(this)">删除科目</a>
		                            		</div>
		                            </div>
		                            </c:if>
                                </div>
                            </div>
                            <div class="hr-line-dashed"></div>
                            <div class="form-group">
                                <div class="col-sm-4 col-sm-offset-2">
                                    <button class="btn btn-primary" type="button" onclick="saveAttr()">保存内容</button>
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
	function saveAttr(){
		$.ajax({
			url:"/aihudong-duoping-web/faculty/updateInfo",
			data:$("#editForm").serialize(),
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

	var index = 1;
	$(document).ready(function () {
	    $('.i-checks').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green',
	    });
	    <c:if test="${faculty!=null }">index=${fn:length(faculty.subjectList) }</c:if>
	    
	});
	
	function addAttrValue(){
		var html = "<div class='form-group' id='attrValue"+index+"'><label class='col-sm-2 control-label'></label>"
				 + "<div class='col-md-8'>"
				 + "<input type='text' class='form-control' name='subjectList["+index+"].subjectName' placeholder='科目名称'>"
				 + "</div>"
				 + "<div class='col-md-2'>"
				 + "<a onclick='deleteValue("+index+");' href='javascript:void(0);' class='btn btn-primary '>删除科目</a>"
				 + "</div></div>";
				 
		index++;
		$("#valueDiv").append(html);
	}
	
	function deleteValue(index){
		$("#attrValue"+index).remove();
		index--;
	}
	
	
	function delAjax(obj){
		$(obj).parent().parent().remove();
	}
</script>
</html>