<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="common/include_css.jsp" />
<jsp:include page="common/include_js.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<button onclick="test()">aaa</button>
	<button onclick="test2()">bbb</button>
</body>
<script type="text/javascript">
	

	function test2(){
		
		/* var salt = "c5ee9cde31f25f6ba7119361b8d16498";
		var create_parameters= "name=" + name + "&meetingID=" + meetingID +"&attendeePW=" + attendeePW + "&moderatorPW=" +moderatorPW;
		var urlCreste = checksum("creat" +create_parameters + salt); */
		
		
		var obj = {};
		 obj['meetingID'] = "123";
		 obj['checksum'] = "aa599efb9fdf37d1dd36cc628feb4263423a04a8";
		 /* obj['type'] = 3;
		 obj['encryption'] = 0; */
	    
	    $.ajax({
			url : "http://192.168.10.184/bigbluebutton/api/getMeetingInfo?meetingID=111&checksum=62badb2a1364165b4b31be03a6cb90f9f0ce3f80",
			type : "post",
			//contentType: 'application/json', // 这句不加出现415错误:Unsupported Media Type
			contentType:'text/xml',
	        //data: "meetingID",
			success : function(data) {
				console.log(data);
			}
		})
	}
	
	function checksum(s) {
        var checksum = "";
        checksum= hex_sha1(s);
        return checksum;
	}
	
	
	function test(){
		var obj = {};
	    obj['username'] = "1234";
	    obj['password'] = "1234" ;
		
		$.ajax({
			url : "/aihudong-duoping-web/front/userLogin",
			type : "post",
			contentType: 'application/json', // 这句不加出现415错误:Unsupported Media Type
	        data: JSON.stringify(obj),
			success : function(data) {
				if (data == 'success') {
					alert("操作成功！");
					
				} else {
					alert("操作失败");
				}
			}
		})
	}
</script>
</html>