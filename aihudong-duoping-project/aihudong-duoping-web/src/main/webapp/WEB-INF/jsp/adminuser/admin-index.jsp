<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="../common/include_css.jsp" />
<jsp:include page="../common/include_js.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="ibox-content">
		<div class="dataTables_wrapper form-inline" role="grid">
			<form method="post" id="searchForm">
				<div class="row">
					<!-- <div class="col-sm-2">
				<div class="dataTables_length">
					<a href="/aihudong-duoping-web/admin/toUpdate" class="btn btn-primary ">新增用户</a>
				</div>
			</div> -->
					<div class="col-sm-10" id="group">
						<div class="input-group">
							<!-- 根据教师、学生、屏幕查询 -->
							<select class="form-control" name="category"
								onchange="selectScreen(this)">
								<option value="">---种类---</option>
								<c:if test="${admin.power==0 || admin.power==1 }">
									<option value="1" selected>教师</option>
									<option value="2">学生</option>
								</c:if>
								<c:if test="${admin.power==0 || admin.power==2 }">
									<option value="4">屏幕</option>
								</c:if>
							</select>
						</div>
						<div id="facultySelect" class="input-group">
							<!-- 根据院系、专业查询 -->
							<select class="form-control" onchange="changeSubject(this)" name="facultyId">
								<option value="">---院系---</option>
								<c:forEach items="${facultyList }" var="faculty">
									<option value="${faculty.id}">${faculty.facultyName }</option>
								</c:forEach>
							</select>
						</div>
						<div id="subjectSelect" class="input-group">
							<select class="form-control" id="subjectSelected" name="subjectId">
								<option value="">---专业---</option>
							</select>
						</div>
						<div id='time' class='input-group'>
							<select class='form-control' name='interval'>
								<option value=''>---时间---</option>
								<option value='1'>一天</option>
								<option value='7' selected>一周</option>
								<option value='30'>一个月</option>
								<option value='180'>半年</option>
							</select>
						</div>
						&nbsp;&nbsp;日期：&nbsp;&nbsp;
						<input type="text" id="test1" class="form-control" name="time">
						<div class="input-group" style="float: right;">
							<!-- 真实姓名 -->
							<%-- <input type="text" name="username" value="${teacher.username }"
						class="form-control" placeholder="关键字查找"> --%>
							<div class="input-group-btn">
								<button class="btn btn-primary">搜索</button>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div id="main" style="height: 400px; width: 900px"></div>
	<c:if test="${admin.power==0 || admin.power==1 }">
	<table
		class="table table-striped table-bordered table-hover dataTables-example"
		style="width:450px;">
		<caption align="top">教师表</caption> 
		<thead>
			<tr>
				<th>序号</th>
				<th>Id</th>
				<th>姓名</th>
				<th>时长</th>
			</tr>
		</thead>
		<tbody id="tbodyTeacher">
		</tbody>
	</table>
	<table
		class="table table-striped table-bordered table-hover dataTables-example"
		style="width:450px;">
		<caption align="top">学生表</caption> 
		<thead>
			<tr>
				<th>序号</th>
				<th>Id</th>
				<th>姓名</th>
				<th>时长</th>
			</tr>
		</thead>
		<tbody id="tbodyStudent">
		</tbody>
	</table>
	</c:if>
</body>
<script type="text/javascript">
		$.ajax({
			url:"/aihudong-duoping-web/record/getTeacherOrderByTime",
			type:"post",
			success:function(data){
				for (var i = 0; i < data.teacherRecordList.length; i++) {
					$("#tbodyTeacher").append("<tr class='gradeA'><td>"+(i+1)+"</td><td>"+data.teacherRecordList[i].t_id+" </td><td>"+data.teacherRecordList[i].truename +"</td><td>"+data.teacherRecordList[i].time +"/h</td></tr>");
				}
			}
		})
		
		$.ajax({
			url:"/aihudong-duoping-web/record/getStudentOrderByTime",
			type:"post",
			success:function(data){
				for (var i = 0; i < data.studentRecordList.length; i++) {
					$("#tbodyStudent").append("<tr class='gradeA'><td>"+(i+1)+"</td><td>"+data.studentRecordList[i].stu_id+" </td><td>"+data.studentRecordList[i].truename +"</td><td>"+data.studentRecordList[i].time +"/h</td></tr>");
				}
			}
		})

	function selectScreen(obj){
		$("#time").remove();
		$("#selectCate").remove();
		if(obj.value==4){
			$("#facultySelect").css("display","none");
			$("#subjectSelect").css("display","none");
			$("#group").append("<div id='selectCate' class='input-group'><select class='form-control' name='bingfa'><option value=''>---请选择---</option><option value='1'>---时长---</option><option value='2'>---并发数---</option></select></div>");
		}else{
			$("#facultySelect").css("display","inline-block");
			$("#subjectSelect").css("display","inline-block");
			$("#group").append("<div id='time' class='input-group'><select class='form-control' name='interval'><option value=''>---时间---</option><option value='1'>一天</option><option value='7' selected>一周</option><option value='30'>一个月</option><option value='180'>半年</option></select></div>");
		}
	}

$(function() {
	getEchartsData();
	$('.btn-primary').on('click',function() {
		getEchartsData();
		return false;
	})
	laydate.render({
		  elem: '#test1', //指定元素
		  istime: false,
		  istoday: true,
		  key: '2018-1-10',
		  issure: true
		});
	$("#test1").value=new Date();
})

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
$("#subjectSelected").append("<option value=''>---专业---</option>");

for(var i=0;i<arrayName.length;i++){
	$("#subjectSelected").append("<option value='"+arrayId[i]+"' >"+arrayName[i]+"</option>");
}
}

//基于准备好的dom，初始化echarts图表
var myChart = echarts.init(document.getElementById("main"));
var option = {
	title : {
		text : '产品使用时长统计',
		subtext : '模拟'
	},
	tooltip : {
		trigger : 'axis'
	},
	legend : {
		data : [ '时长']
	},
	//右上角工具条
	toolbox : {
		show : true,
		feature : {
			mark : {
				show : true
			},
			dataView : {
				show : true,
				readOnly : false
			},
			magicType : {
				show : true,
				type : [ 'line', 'bar' ]
			},
			restore : {
				show : true
			},
			saveAsImage : {
				show : true
			}
		}
	},
	calculable : true,
	xAxis : [ {
		type : 'category',
		boundaryGap : false,
		data : [  ]
	} ],
	yAxis : [ {
		type : 'value',
		axisLabel : {
			formatter : '{value} /h'
		}
	} ],
	series : [ {
		name : '时长',
		type : 'line',
		data : [ ],
		markPoint : {
			data : [ {
				type : 'max',
				name : '最大值'
			}, {
				type : 'min',
				name : '最小值'
			} ]
		},
		markLine : {
			data : [ {
				type : 'average',
				name : '平均值'
			} ]
		}
	}]
};
myChart.showLoading();

function getEchartsData(){
	var names=[];    //类别数组（实际用来盛放X轴坐标值）
	var nums=[];    //销量数组（实际用来盛放Y坐标值）
$.ajax({  
    type : "post",  
    async : true, //异步执行  
    data:$("#searchForm").serialize(),
    url : "/aihudong-duoping-web/echarts/getEchartsData",  
    dataType : "json", //返回数据形式为json  
    success : function(json) {  
    	/* var json = eval('(' + json + ')');  */
           for(var i=0;i<json.xAxisData.length;i++){       
               names.push(json.xAxisData[i]);    //挨个取出类别并填入类别数组
             }
            for(var i=0;i<json.seriesList.length;i++){       
                nums.push(json.seriesList[i].hour);    //挨个取出销量并填入销量数组
              }
           myChart.hideLoading();  
           myChart.setOption({  
        	               
           xAxis:{  
              data:names  
           },   
           series:{
        	   data:nums
           },
           yAxis :[{
       		type : 'value',
	       		axisLabel : {
	       			formatter : '{value} /'+json.type
	       		}
       		}],
           markPoint : {
			data : [ {
				type : 'max',
				name : '最大值'
			}, {
				type : 'min',
				name : '最小值'
			} ]
			},
			markLine : {
				data : [ {
					type : 'average',
					name : '平均值'
				} ]
			}   
          });
    },  
    error : function(errorMsg) {  
        alert("请求数据失败");  
    }  
});  }
/* window.onload=function(){ */
	
/* } */
// 为echarts对象加载数据
myChart.setOption(option);
</script>
</html>