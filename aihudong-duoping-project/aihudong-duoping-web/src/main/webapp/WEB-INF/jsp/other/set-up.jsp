<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>其他设置</title>
</head>
<jsp:include page="../common/include_css.jsp" />
<jsp:include page="../common/include_js.jsp" />

<body>
	<form id="editForm" class="form-horizontal"
		action="/aihudong-web/setup/changeSetUp" method="post">
		<div class="panel panel-default">
			<input type="hidden" name="id" value="${setUp.id }">
			<div class="panel-heading">其他设置</div>
			<div class="panel-body">
				<div class="switch switch-large">
					水印：<input type="checkbox" name="waterMark"
						${setUp.waterMark=='on' ? 'checked' : ''} />
				</div>

				<div class="switch switch-large">
					logo：<input type="checkbox" name="logo"
						${setUp.logo=='on' ? 'checked' : ''} />
				</div>
				
				<div class="switch switch-large">
					画笔：<input type="checkbox" name="brush"
						${setUp.brush=='on' ? 'checked' : ''} />
				</div>
				
				<div class="switch switch-large">
					黑板：<input type="checkbox" name="blackboard"
						${setUp.blackboard=='on' ? 'checked' : ''} />
				</div>
				
				<div class="switch switch-large">
					复制：<input type="checkbox" name="copy"
						${setUp.copy=='on' ? 'checked' : ''} />
				</div>
				
				<div class="switch switch-large">
					黑屏：<input type="checkbox" name="blackScreen"
						${setUp.blackScreen=='on' ? 'checked' : ''} />
				</div>
				
				<div class="switch switch-large">
					分享桌面：<input type="checkbox" name="sharingTheDesktop"
						${setUp.sharingTheDesktop=='on' ? 'checked' : ''} />
				</div>
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-4 col-sm-offset-2">
				<button class="btn btn-primary" type="button" onclick="updateInfo()">保存</button>
			</div>
		</div>
	</form>
</body>

<script type="text/javascript">
	function updateInfo() {
		$
				.ajax({
					url : "/aihudong-duoping-web/setup/changeSetUp",
					data : $("#editForm").serialize(),
					type : "post",
					success : function(data) {
						if (data == 'success') {
							alert("操作成功！");
							window.location = "/aihudong-duoping-web/setup/toIndex";
						}else {
							alert("操作失败");
						}
					}
				})
	}
</script>
</html>