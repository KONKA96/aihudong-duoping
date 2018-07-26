<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>消息</title>
    <jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>消息</small></h5>
                        <div class="ibox-tools">
                        	<button class="btn btn-primary" onclick="goback()">返回</button>
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal" action="/aihudong-web/message/updateMessage" method="post" enctype="multipart/form-data">
                        	<input type="hidden" name="id" value="${message.id }">
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">计划名称</label>

                                <div class="col-sm-10">
                                    <input id="username" name="messageName" value="${message.messageName }" type="text" class="form-control" placeholder="计划名称">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">选择模式</label>
                                <div class="col-sm-10">
                                    <select class="form-control m-b" name="messageType">
                                    		<option value="1" ${message.messageType==1 ? 'selected' :'' }>消息框模式</option>
                                    		<option value="2" ${message.messageType==2 ? 'selected' :'' }>全屏幕模式</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">上传文件</label>
                                <div class="col-sm-10">
									<input type="file" name="fileList" multiple="multiple" max="10" class="inputPic" accept="image/*">  
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">推送时间</label>
                                <div class="col-sm-5">
                                    <div class="form-group">
						                <label for="dtp_input1" class="col-md-2 control-label">start time</label>
						                <div class="input-group date form_datetime col-md-5" data-date="2018-08-08T05:25:07Z" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="dtp_input1">
						                    <input class="form-control" name="startTimeString" size="16" type="text" value="<fmt:formatDate value='${message.startTime }' pattern='yyyy-MM-dd HH:mm:ss'/>" readonly>
						                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
						                    <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
						                </div>
						                <input type="hidden" id="dtp_input1" value="" /><br/>
						            </div>
                                </div>
                                <div class="col-sm-5">
                                    <div class="form-group">
						                <label for="dtp_input1" class="col-md-2 control-label">end time</label>
						                <div class="input-group date form_datetime col-md-5" data-date="2018-08-08T05:25:07Z" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="dtp_input1">
						                    <input class="form-control" name="endTimeString" size="16" type="text" value="<fmt:formatDate value='${message.endTime }' pattern='yyyy-MM-dd HH:mm:ss'/>" readonly>
						                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
						                    <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
						                </div>
						                <input type="hidden" id="dtp_input1" value="" /><br/>
						            </div>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">接收终端</label>
                                <div class="col-sm-10">
                                	<c:forEach items="${zoneList }" var="zone">
                                		<div class="col-sm-5" style="border:1px solid blue;">
                                		<input type="checkbox" id="zone${zone.id }" value="${zone.id }" name="zoneList[0].zoneName" onclick="checkZone(${zone.id},this)"
                                		<c:if test="${fn:contains(message.zoneId,zone.id)}"> checked="checked" </c:if>>${zone.zoneName } (校区)
                                		<c:forEach items="${zone.buildingList }" var="building">
                                			<div style="border-bottom:1px solid black;">
	                                		<input type="checkbox" id="building${building.id }" value="${building.id }" name="buildingList[0].buildingName" class="zone${zone.id }" onclick="checkBuilding(${building.id},this,${zone.id})"
	                                		<c:if test="${fn:contains(message.buildingId,building.id)}"> checked="checked" </c:if>> ${building.buildingName } (教学楼)
                                			<br/>
                                			<c:forEach items="${building.roomList }" var="room">
                                				<input type="checkbox" value="${room.id }" name="roomList[0].id" class="zone${zone.id } building${building.id}" onclick="checkRoom(${building.id},this,${zone.id})"
	                                			<c:if test="${fn:contains(message.roomId,room.id)}"> checked="checked" </c:if>> ${room.num } (房间)
                                			</c:forEach>
                                			</div>
                                		</c:forEach>
                                		<br/>
                                		</div>
                                	</c:forEach>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-4 col-sm-offset-2">
                                    <input class="btn btn-primary" type="submit" value="保存">
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

	$(document).ready(function () {
	    $('.i-checks').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green',
	    });
	});
	
	
	function goback(){
		window.history.back();
	}
	
	
	function checkZone(id,obj){
		var zoneId="zone"+id;
		var ele=document.getElementsByClassName(zoneId);
		if(obj.checked==true){
			for (var i = 0; i < ele.length; i++) {
				ele[i].checked=true;
			}
		}else{
			for (var i = 0; i < ele.length; i++) {
				ele[i].checked=false;
			}
		}
		
	}
	
	function checkBuilding(id,obj,zId){
		//下级全选和清空
		var buildingId="building"+id;
		var ele=document.getElementsByClassName(buildingId);
		
		//校区的选中和清空
		var zoneId="zone"+zId;
		var childNodes=document.getElementsByClassName(zoneId);
		
		var zone=document.getElementById(zoneId);
		if(obj.checked==true){
			for (var i = 0; i < ele.length; i++) {
				ele[i].checked=true;
			}
			zone.checked=true;
			
		}else{
			for (var i = 0; i < ele.length; i++) {
				ele[i].checked=false;
			}
			var count=0;
			for (var i = 0; i < childNodes.length; i++) {
				if(childNodes[i].checked==true){
					break;
				}else{
					count++;
				}
			}
			if(count==childNodes.length){
				zone.checked=false;
			}
		}
		
	}
	
	function checkRoom(bid,obj,zId){
		//下级全选和清空
		var buildingId="building"+bid;
		var ele=document.getElementsByClassName(buildingId);
		
		//校区的选中和清空
		var zoneId="zone"+zId;
		var childNodes=document.getElementsByClassName(zoneId);
		
		var zone=document.getElementById(zoneId);
		var building=document.getElementById(buildingId);
		
		if(obj.checked==true){
			zone.checked=true;
			building.checked=true;
		}else{
			counti=0;
			for (var i = 0; i < ele.length; i++) {
				if(ele[i].checked==true){
					break;
				}else{
					counti++;
				}
			}
			if(counti==ele.length){
				building.checked=false;
			}
			
			countj=0;
			for (var i = 0; i < childNodes.length; i++) {
				if(childNodes[i].checked==true){
					break;
				}else{
					countj++;
				}
			}
			if(countj==childNodes.length){
				zone.checked=false;
			}
		}
	}
	
	//日历插件
	$('.form_datetime').datetimepicker({
        weekStart: 0, //一周从哪一天开始
        todayBtn:  1, //
        autoclose: 1,
        todayHighlight: 1,
        startView: 0,
        forceParse: 0,
        showMeridian: 1
    });
</script>
</html>
