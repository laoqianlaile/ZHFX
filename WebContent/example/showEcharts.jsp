<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
 <%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="${ctx}/js/jquery-1.9.1.min.js"></script>
<script src="${ctx}/template/ercharts/echarts.js"></script>
<title>加载报表</title>
</head>
<body style="width:100%;height:100%">
<!--    查询条件 -->
<!--    <div class="self_def_search"> -->
<!--         <label for="year">年份</label><input id="year" name="year"/>  -->
<!--         <label for="buildSite">建设地点</label><input id="buildSite" name="buildSite"/>  -->
<!--         <label for="buildSiteIds">建设地点下拉框查询</label><input id='buildSiteIds' name="buildSiteIds"/>  -->
<!--         相关区间查询   -->
<!--    </div> -->
<!--    主界面 -->
    <div style="width:750px;height:500px;" id="main"> </div>
    <script>
    
		 require.config({
				paths: {
					echarts: '${ctx}/template/ercharts'
				}
			});
		 require(
			[
				'echarts',				
				'echarts/chart/bar'
			],
			function (ec) {
				var myChart = ec.init(document.getElementById('main')); 
				// Ajax 取值  
				var data=getData();
				option = {
					    title : {
					        text: '自定义报表获取业务包里面的数据' 
					    },
					    tooltip : {
					        trigger: 'axis'
					    },
					    legend: {
					        data:['地区'] 
					    },
					    toolbox: {
					        show : false,
					        feature : {
					            mark : {show: true},
					            dataView : {show: true, readOnly: false},
					            magicType : {show: true, type: ['line', 'bar']},
					            restore : {show: true},
					            saveAsImage : {show: true}
					        }
					    },
					    calculable : true,
					    xAxis : [
					        {
					            type : 'category',
					            data : data.dataX
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
					        {
					            name:'地区',
					            type:'bar',
					            data:data.dataY
					        }
					    ]
					};
				// 初始化图表 	                    
				 myChart.setOption(option);
			});
		 // 得到演示数据 
		 function getData(){
			 var obj={};
			 // X轴对应的数据 
			 obj.dataX=[];
			 // Y 轴对应的数据  
			 obj.dataY=[];
			 // 查询数据的条件
			 var widget={
					    "type": 5,
					    "name": "reportid",
					    "init_time": 1468635753250,
					    "bounds": {
					        "left": 280,
					        "top": 110,
					        "width": 450,
					        "height": 380
					    },
					    "linkages": [],
					    "dimensions": {
					        "f7d8df2798a25539": {
					            "name": "ITEM_VALUE",
					            "_src": {
					                "id": "fb013b78e3e3a23f",
					                "field_id": "fb013b78e3e3a23f",
					                "table_id": "672ae4453e81e67c"
					            },
					            "type": 1,
					            "used": true,
					            "dimension_map": {
					                "5a25dbcb68ca8266": {
					                    "_src": {
					                        "id": "fb013b78e3e3a23f",
					                        "field_id": "fb013b78e3e3a23f",
					                        "table_id": "672ae4453e81e67c"
					                    },
					                    "target_relation": [
					                        [
					                            {
					                                "primaryKey": {
					                                    "field_id": "fb013b78e3e3a23f",
					                                    "table_id": "672ae4453e81e67c"
					                                },
					                                "foreignKey": {
					                                    "field_id": "7a60ca5e61439fc9",
					                                    "table_id": "672ae4453e81e67c"
					                                }
					                            }
					                        ]
					                    ]
					                }
					            },
					            "settings": {},
					            "sort": {},
					            "group": {}
					        },
					        "5a25dbcb68ca8266": {
					            "name": "TOTAL",
					            "_src": {
					                "id": "7a60ca5e61439fc9",
					                "field_id": "7a60ca5e61439fc9",
					                "table_id": "672ae4453e81e67c"
					            },
					            "type": 2,
					            "used": true
					        }
					    },
					    "view": {
					        "10000": [
					            "f7d8df2798a25539"
					        ],
					        "20000": [],
					        "30000": [
					            "5a25dbcb68ca8266"
					        ],
					        "40000": []
					    },
					    "settings": {},
					    "filter_value": {},
					    "real_data": true,
					    "clicked": {},
					    "filter": {
					        "filter_type": 80,
					        "filter_value": []
					    },
					    "expander": {
					        "x": {
					            "type": true,
					            "value": [
					                []
					            ]
					        },
					        "y": {
					            "type": true,
					            "value": [
					                []
					            ]
					        }
					    },
					    "page": -1
					};
			 //// 请求数据
			 $.ajax({
             cache: true,
             type: "POST",
             url:"${ctx}/ReportServer?op=fr_bi_dezi&cmd=widget_setting",  
             data:{widget:window.encodeURIComponent(JSON.stringify(widget)),sessionID:parent.parent.Data.SharingPool.get("sessionID")||top.Data.SharingPool.get("sessionID")},// 将对象转化成JSON 
             async: false,// 同步请求数据 
             dataType:"json",
             error: function(request) {
                 alert("Connection error");
             },
             success: function(data) {
             	// 请求到了数据
                if(data){
             	   //得到数值的长度
             	   var len=data.data.c.length;
             	   var dd=data.data.c;
             	   for(var i=0;i<len;i++){
             		   // 封装数据  
             		  obj.dataX.push(dd[i].n||"空"); 
             		  obj.dataY.push(dd[i].s[0]||0);
             	   }
                }
             }
         });
			 // 返回到前端 
			 return obj;
		 }
		 // 帆软BI系统的SessionID
		 function getSessionId(){	
			 var sessionid=null;
			 $.ajax({  
		          type : "post",  
		          url : "${ctx}/login/login!getSessionId",  		         
		          async : false, // 同步请求 
		          dataType:'text',
		          success : function(data){  
		        	  // 同步请求的SessionID
		        	  sessionid=data;
		          }  
		          });  
			 return sessionid;
		 }
		 </script>  
</body>
</html>