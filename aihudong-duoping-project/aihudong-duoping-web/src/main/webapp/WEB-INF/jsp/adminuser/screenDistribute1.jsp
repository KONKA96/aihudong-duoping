<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<jsp:include page="../common/include_css.jsp" />
<jsp:include page="../common/include_js.jsp" />
<body>
   <div style="padding:20px;width:100%;height:100%;">   
               <div id="main" style="width: 800px;height:464px;">  
               </div>  
    </div>
    <!-- ECharts单文件引入 -->
    <script type="text/javascript">
    $(function() {
    	getEchartsData();
    })
    var myChart = echarts.init(document.getElementById("main"));
    function getEchartsData(){
    var admin;
	var adminErjiList=[];    //类别数组（实际用来盛放X轴坐标值）
	var adminYijiList=[];    //销量数组（实际用来盛放Y坐标值）
	var facultyList=[];
	var subjectList=[];
	var teacherList=[];
$.ajax({  
    type : "post",  
    async : true, //异步执行  
    url : "/aihudong-duoping-web/admin/getEchartsTeacherData",  
    dataType : "json", //返回数据形式为json  
    success : function(json) {  
    	/* var json = eval('(' + json + ')');  */
    	admin=json.admin.truename;
           for(var i=0;i<json.adminErjiList.length;i++){
        	   subjectList.push(json.adminErjiList[i].subjectList);
        	    for (var j = 0; j < subjectList.length; j++) {
        	    	 teacherList.push(subjectList[j].teacherList);
					for (var x = 0; x <subjectList[j].teacherList.length; x++) {
						adminErjiList.push(subjectList[j].teacherList[x].truename);
					}
				}
           
    		}
           for(var i=0;i<json.adminYijiList.length;i++){ 
        	   facultyList.push(json.adminYijiList[i].facultyList);
        	   for (var j = 0; j < facultyList.length; j++) {
        		   subjectList.push(facultyList[j].subjectList);
        		   console.log(subjectList);
        	   }
           }
           myChart.hideLoading(); 
           myChart.setOption({
        	   series:[{
        		  /*  name : admin */
        		  data:[
        			  {
  	                    name: admin,
  	                    value: 6,
  	                    children: [
  	                        {
  	                            name: '节点1',
  	                            value: 4,
  	                            children: [
  	                                {
  	                                    name: '叶子节点1',
  	                                    value: 4
  	                                },
  	                                {
  	                                    name: '叶子节点2',
  	                                    value: 4
  	                                },
  	                                {
  	                                    name: '叶子节点3',
  	                                    value: 2
  	                                },
  	                                 {
  	                                    name: '叶子节点4',
  	                                    value: 2
  	                                },
  	                                {
  	                                    name: '叶子节点5',
  	                                    value: 2
  	                                },
  	                                {
  	                                    name: '叶子节点6',
  	                                    value: 4
  	                                }
  	                            ]
  	                        },
  	                        {
  	                            name: '节点2',
  	                            value: 4,
  	                            children: [{
  	                                name: '叶子节点7',
  	                                value: 4
  	                            },
  	                            {
  	                                name: '叶子节点8',
  	                                value: 4
  	                            }]
  	                        },
  	                        {
  	                            name: '节点3',
  	                            value: 1,
  	                            children: [
  	                                {
  	                                    name: '叶子节点9',
  	                                    value: 4
  	                                },
  	                                {
  	                                    name: '叶子节点10',
  	                                    value: 4
  	                                },
  	                                {
  	                                    name: '叶子节点11',
  	                                    value: 2
  	                                },
  	                                 {
  	                                    name: '叶子节点12',
  	                                    value: 2
  	                                }
  	                            ]
  	                        }
  	                    ]
  	                }
        		  ]
        	   }]
           })
},
error : function(errorMsg) {  
    alert("请求数据失败");  
} 
})
    }
    
    var option = {
    	    title : {
    	        text: '树图',
    	        subtext: '虚构数据'
    	    },
    	    toolbox: {
    	        show : true,
    	        feature : {
    	            mark : {show: true},
    	            dataView : {show: true, readOnly: false},
    	            restore : {show: true},
    	            saveAsImage : {show: true}
    	        }
    	    },
    	    calculable : false,

    	    series : [
    	        {
    	            name:[],
    	            type:'tree',
    	            orient: 'horizontal',  // vertical horizontal
    	            rootLocation: {x: 'center',y: 50}, // 根节点位置  {x: 100, y: 'center'}
    	            nodePadding: 1,
    	            itemStyle: {
    	                normal: {
    	                    label: {
    	                        show: false,
    	                        formatter: "{b}"
    	                    },
    	                    lineStyle: {
    	                        color: '#48b',
    	                        /* shadowColor: '#000',
    	                        shadowBlur: 3,
    	                        shadowOffsetX: 3,
    	                        shadowOffsetY: 5, */
    	                        type: 'solid' // 'curve'|'broken'|'solid'|'dotted'|'dashed'

    	                    }
    	                },
    	                emphasis: {
    	                    label: {
    	                        show: true
    	                    }
    	                }
    	            },
    	            
    	            data: [
    	                
    	            ]
    	        }
    	    ]
    	};
    myChart.showLoading();
    
    myChart.setOption(option);
    </script>
</body>
</html>