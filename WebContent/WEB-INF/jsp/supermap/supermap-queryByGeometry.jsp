﻿<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>几何查询</title>
        <style type="text/css">
            body{
                margin: 0;
                overflow: hidden;
                background: #fff;
            }
            #map{
                position: relative;
                border:1px solid #3473b7;
            }
            #toolbar{
                position: relative;
            }
        </style>
        <link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui1.4/themes/icon.css">
        <script src='${ctx}/js/forJavaScript/libs/SuperMap.Include.js'></script>
        <script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
        <script type="text/javascript" src="${ctx}/js/easyui1.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/easyui1.4/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
		<script type="text/javascript" src="${ctx}/common/extend.js"></script>
        <script type="text/javascript">
        //定义地图标点展现是按照哪个类型展示，0表示按照项目总投资，1表示按照项目个数
        var projectShowFlag = "0"; 
        //起始显示所有省级数据,1为省级;2为市级;3为县级
        var level = 1;
        //从左至右分别存储省市县的数据集
        var polygonLevel1 = [],polygonLevel2 = [],polygonLevel3 = [];
        var polygonShen;
        //表示当前钻取显示 下级行政区划信息 (zp)
        var flag = true;
        //获取当前点击地图的地区编码（县级）
        var xianJiClickCode = "";
        //获取当前点击地图的地区经纬度坐标
        var xianJiClickSmX = "";
        var xianJiClickSmY = "";
        //定义是否是双击事件，0：是；1：否
        var dblClickfFlag = 1;
        //标识是否执行完上一次的事件动作,默认为false
        var isCompleteFlag = false;
        //定义查询控件的建设地点
        var projectRegionSearch="";
        //定义查询控件的委内行业
        var industryCodeSearch;
        //定义查询控件的开工时间1
        var actualStartTime1Search;
      	//定义查询控件的开工时间2
	    var actualStartTime2Search;
	    //定义查询控件的竣工时间1
	    var actualEndTime1Search; 
	    //定义查询控件的竣工时间2
	    var actualEndTime2Search;
	    //点击地图所获取的地区编码（作为获取该地区的项目信息的条件）
	    var buildPlaceCode = "";
        //缓存操作动作前的地图级别
        var sessionZoom = 2;
        //画几何图形的中心坐标
        var drawPointX= "";
        var drawPointY= "";
        //画几何图形的半径
        var drawRadius= "";
        //画几何图形中的地区编码
        var drawPolygonCode = "";
        //标识几何图形：0表示画点，1表示画圆，2表示画矩形，3表示画多边形
        var drawPolygonFlag = "";
        //画几何图形映射的面积
        var drawPolygonArea = "";
        //画点的geometry全局变量
        var geometryInfo;
        //定义自定义区域数据集对象
        var markDateList = "";
        //markFlag：0为画几何图形保存项目信息标识，1为查看几何图形项目明细信息标识
        var markFlag = "0";
        //标记的自定义名称
        var markName = "";
        //将集合图形Geometry对象转成字符串保留到自定义表中
        var myGeometryStr = "";
        //缓存过滤条件
        var sessionParams = new Object();
        //定义地图相关的全局变量
        var map, local, layer,vector, vector1,dataAdded=false,  baseLayer, layersID, themeLayer,vectorLayer,  drawPolygon, markerLayer,markerLayerDefined,markerlayerdrawPoint, drawLine,marker,
            style = {
                strokeColor: "#304DBE",
                strokeWidth: 1,
                pointerEvents: "visiblePainted",
                fillColor: "#304DBE",
                fillOpacity: 0.5
            },
            host = document.location.toString().match(/file:\/\//)?"http://localhost:8090":'http://' + document.location.host,
                    //url = "http://59.255.137.11:5240/iserver/services/map-china400/rest/maps/China_4326";
            		//url = "http://59.255.137.11/iserver/services/map-china400/rest/maps/China_4326";
            		url = ZHFX.SERVER_IP+"/iserver/services/map-china400/rest/maps/China_4326";
            function init(){
            	
                layer = new SuperMap.Layer.TiledDynamicRESTLayer("World", url, {transparent: true, cacheEnabled: true}, {maxResolution:"auto"});
                layer.events.on({"layerInitialized":addLayer});
                //画圈图层对象
                vectorLayer = new SuperMap.Layer.Vector("Vector Layer");
                //声明地图气泡显示对象（默认全局的气泡）
                markerLayer = new SuperMap.Layer.Markers("Markers");
                //声明地图气泡显示对象（默认自定义的气泡）
                markerLayerDefined = new SuperMap.Layer.Markers("Markers");
                //几何圆查询
                drawPolygon1 = new SuperMap.Control.DrawFeature(vectorLayer, SuperMap.Handler.RegularPolygon,{handlerOptions:{sides:50}});
                drawPolygon1.events.on({"featureadded": drawCircleCompleted});

                //多边形查询
                drawPolygon2 = new SuperMap.Control.DrawFeature(vectorLayer, SuperMap.Handler.Polygon);
                drawPolygon2.events.on({"featureadded": drawPolygonCompleted});

                //点查询
                drawPoint = new SuperMap.Control.DrawFeature(vectorLayer, SuperMap.Handler.Point);
                drawPoint.events.on({"featureadded": drawPointCompleted});

                //线查询
                drawLine = new SuperMap.Control.DrawFeature(vectorLayer, SuperMap.Handler.Path);
                drawLine.events.on({"featureadded": drawPointCompleted});

                //矩形
                drawRectangle = new SuperMap.Control.DrawFeature(vectorLayer, SuperMap.Handler.Box);
                drawRectangle.events.on({"featureadded": drawRectangleCompleted});

                map = new SuperMap.Map("map",{controls: [
                    new SuperMap.Control.LayerSwitcher(),
                    new SuperMap.Control.ScaleLine(),
                    new SuperMap.Control.Zoom(),
                    new SuperMap.Control.Navigation({
                        dragPanOptions: {
                            enableKinetic: true
                        }}),
                    drawPolygon1,drawPolygon2,drawPoint,drawLine,drawRectangle]
                });
                
                map.addControl(new SuperMap.Control.MousePosition());
                baseLayer= new SuperMap.Layer.TiledDynamicRESTLayer("World", url, null,{maxResolution:"auto"});
                //地图上分各个地区显示的红色圈对象
            	vector = new SuperMap.Layer.Vector("vector");
                //地图上标记的红色圈对象
                vector1 = new SuperMap.Layer.Vector("vector");
            	baseLayer.events.on({"layerInitialized":addLayer});
            
            	//禁掉地图的双击事件
            	map.controls[3].handlers.click.double = false;
                //双击事件
            	dblclick1();
            	//加载各个地方圆圈
            	addData();
            	//加载各个地方气泡图形
            	addDataMarker();
            	
            	
            
            }
            
            //给地图绑定双击事件
            function dblclick1(){
         	   //双击事件
             	$(map.div).dblclick(function(data){
             		//标记为是双击事件
             		dblClickfFlag = 0;
       		    	//获取地图的地理位置
           			getRegionInfo(data);
           			

                 });
             	
         	   
//              	$('.li_close').click(function(data){
             		
             		
//              		alert("kkkkkkk");
             		
             		
//              	});
           }
            
			//地图添加事件
            function addLayer() {
                map.addLayers([layer, vectorLayer,vector,vector1,markerLayer,markerLayerDefined]);
                map.setCenter(new SuperMap.LonLat(115,36), 2);
                map.addLayer(baseLayer);
                map.allOverlays = true;
            
                //地图事件结束后触发事件
                map.events.on({"zoomend":function(data){
                	//根据缩放事件结束后获取当前地图的级别
	                var zoom1 = map.getZoom();
                 	//定义是否是双击事件，0：是；1：否
                 	if(dblClickfFlag==1 && isCompleteFlag == true && sessionZoom>zoom1){
                 		
                 		if(zoom1 == 4){
 	                		onClicks();
                 		}else if(zoom1 == 2){//展示第二级的各个市的项目信息
 	                		level = 1;
 	                		onClicks();
 	                	}else if(zoom1 == 1){//展示第一级的各个省、直辖市的项目信息
 	                		//重新加载地图页面
 	                		reloadSuperMap();
 	                	}
 	                	//执行完成后将最新的级别赋值给sessionZoom
 	                	sessionZoom = zoom1;
                 	}
                 }});
            }
			
         	// 追加地图 点击事件
			function onClicks(){
			  //返回地图当前缩放级别。
              var zoom = map.getZoom();
              //地图当前级别不能超过下面的范围，否则无法正常显示地图
              if(zoom<1||zoom>6){
            	  flag = false;
              }else{
            	  flag = true;
              }
              
          	  // 钻取显示 下级行政区划信息 
          	  if (flag) {
				   //排除县级继续往下转取的情况
               	   if (xianJiClickCode==null) {
                		  alert("不存在下一级行政区划！");
                		  return false;
                    }
                    //下转到县级
//                     if (level == 3 || zoom>5) {
//                     	 alert("不存在下一级行政区划！");
//                		  	return false;
                    	
//                     }
                    
                    //获取县级的数据集
	                var params = new Object();
                    //查询控件的查询条件
               	 	params["params.region"]=projectRegionSearch;
       	          	params["params.INDUSTRY"]=industryCodeSearch;
       	          	params["params.actualStartTime1"]=actualStartTime1Search;
       	          	params["params.actualStartTime2"]=actualStartTime2Search;
       	          	params["params.actualEndTime1"]=actualEndTime1Search;
       	          	params["params.actualEndTime2"]=actualEndTime2Search;
                    
                    
                    if(level == 3){
	                    if(sessionZoom > zoom && zoom == 4){	
	                    	//关闭信息框
	                    	closeInfoWin();
	                    	//清除地图上的覆盖物
	                    	removeData();
	                    	//缩放到指定范围，重新定位中心点。
	                    	var bounds = new SuperMap.Bounds();
	                    	//SmX:"88.311099"SmY:"43.363668"
	                    	bounds.extend(new SuperMap.LonLat(xianJiClickSmX,xianJiClickSmY));
	                    	map.zoomToExtent(bounds,true);
	
							var gb = "";
	                    	//缩放到指定的级别
	                   		gb = xianJiClickCode.substring(0,2) +"0000";
	                   		level = 2;
	                   		//滚动鼠标缩小地图
	                   		map.zoomTo(zoom-1);
	                    	
	                		//获取点击地图某一区域的编码值
	                		buildPlaceCode = gb;
	                		params["params.BuildPlaceCode"]=gb;
	                		
	                    	//重新加载地图数据
	                		addData(params);
	                    }
                    }else if (level == 2) {
                    	if(zoom == 5){
                    	  	//关闭信息框
                        	closeInfoWin();
                        	//清除地图上的覆盖物
                        	removeData();
                        	//缩放到指定范围，重新定位中心点。
                        	var bounds = new SuperMap.Bounds();
                        	//SmX:"88.311099"SmY:"43.363668"
                        	bounds.extend(new SuperMap.LonLat(xianJiClickSmX,xianJiClickSmY));
                        	map.zoomToExtent(bounds,true);

                        	//缩放到指定的级别
                       		var gb = xianJiClickCode.substring(0,4) +"00";
                       		level = 3;
                       		//地图下转
                       		map.zoomTo(zoom+1);
                        	
                    		//获取点击地图某一区域的编码值
                    		buildPlaceCode = gb;
                    		params["params.BuildPlaceCode"]=gb;
                        	//重新加载地图数据
                    		addData(params);
                    	}else{
                    		//缩放到指定的级别
                        	if(sessionZoom > zoom){
                        		//滚动鼠标缩小地图
                        		map.zoomTo(zoom-1);
                        	}else{
                        		//地图下转
                        		map.zoomTo(zoom+1);
                        	}
                    	}
                    } else if (level == 1) {
                    	if(zoom == 2){
                    		//关闭信息框
                        	closeInfoWin();
                        	//清除地图上的覆盖物
                        	removeData();
                        	
                        	isCompleteFlag = false;
                        	//缩放到指定范围，重新定位中心点。
                        	var bounds = new SuperMap.Bounds();
                        	//SmX:"88.311099"SmY:"43.363668"
                        	bounds.extend(new SuperMap.LonLat(xianJiClickSmX,xianJiClickSmY));
                        	map.zoomToExtent(bounds,true);
                        	var gb = "";
                        	//缩放到指定的级别
                        	if(sessionZoom > zoom){
                        		//滚动鼠标缩小地图
                        		map.zoomTo(zoom-1);
                        	}else{
                        		gb = xianJiClickCode.substring(0,2) +"0000";
                        		level = 2;
                        		//地图下转
                        		map.zoomTo(zoom+1);
                        	}
                    		
                    		//获取点击地图某一区域的编码值
                    		buildPlaceCode = gb;
                    		params["params.BuildPlaceCode"]=gb;
                        	//重新加载地图数据
                    		addData(params);
                    	}else if(zoom > 2){
                    		//关闭信息框
                        	closeInfoWin();
                        	//清除地图上的覆盖物
                        	removeData();
                        	
                        	isCompleteFlag = false;
                        	//缩放到指定范围，重新定位中心点。
                        	var bounds = new SuperMap.Bounds();
                        	//SmX:"88.311099"SmY:"43.363668"
                        	bounds.extend(new SuperMap.LonLat(xianJiClickSmX,xianJiClickSmY));
                        	map.zoomToExtent(bounds,true);
                        	var gb = "";
                        	//缩放到指定的级别
                        	if(sessionZoom > zoom){
                        		//滚动鼠标缩小地图
                        		map.zoomTo(zoom-1);
                        	}else{
                        		gb = xianJiClickCode.substring(0,2) +"0000";
                        		level = 2;
                        		//地图下转
                        		map.zoomTo(3);
                        	}
                    		
                    		//获取点击地图某一区域的编码值
                    		buildPlaceCode = gb;
                    		params["params.BuildPlaceCode"]=gb;
                        	//重新加载地图数据
                    		addData(params);
                    	}else{
                    		//缩放到指定的级别
                        	if(sessionZoom > zoom){
                        		//滚动鼠标缩小地图
                        		map.zoomTo(zoom-1);
                        	}else{
                        		//地图下转
                        		map.zoomTo(zoom+1);
                        	}
                    	}
                    }
                    //缓存起来过滤条件
                    sessionParams = params;
                }
          	    //每次获取完数据后，给双击事件标识设置为否
          		dblClickfFlag=1;
          	    //标识是否执行完上一次的事件动作
          		isCompleteFlag = true;
			}
            
			//清除地图的覆盖物（清除按钮触发）
            function clearStatus(){
            	//清除画几何图形区域
                clearDrawPolygon();
                vectorLayer.removeAllFeatures();
                markerLayerDefined.clearMarkers();
            }
            //画圆
            function drawGeometry1() {
            	//清除画几何图形区域
                clearDrawPolygon();
                //先清除上次的显示结果
                clearStatus();
                drawPolygon1.activate();
            }
            //画多边形
            function drawGeometry2() {
            	//清除画几何图形区域
                clearDrawPolygon();
            	//禁掉双击事件
            	$(map.div).unbind("dblclick");
                //先清除上次的显示结果
                clearStatus();
                drawPolygon2.activate();
            }
            //画点（标记）
            function drawGeometry3() {
            	//清除画几何图形区域
                clearDrawPolygon();
                //先清除上次的显示结果
                clearStatus();
                drawPoint.activate();
            }
            //画线
            function drawGeometry4() {
            	//清除画几何图形区域
                clearDrawPolygon();
                //先清除上次的显示结果
                clearStatus();
                drawLine.activate();
            }
            
            //画矩形
            function drawGeometry5() {
            	//清除画几何图形区域
                clearDrawPolygon();
                //先清除上次的显示结果
                clearStatus();
                drawRectangle.activate();
            }
            
			//画圈结束后事件
            function drawCircleCompleted(drawGeometryArgs) {
				debugger;
				
				
                //标识几何图形：0表示画点，1表示画圆，2表示画矩形，3表示画多边形
                drawPolygonFlag = 1;
                //画几何图形的中心坐标
                drawPointX= (drawGeometryArgs.feature.geometry.bounds.left + drawGeometryArgs.feature.geometry.bounds.right)/2;
                drawPointY= (drawGeometryArgs.feature.geometry.bounds.bottom + drawGeometryArgs.feature.geometry.bounds.top)/2;
                //画几何图形的半径
                drawRadius= (drawGeometryArgs.feature.geometry.bounds.right - drawGeometryArgs.feature.geometry.bounds.left )/2;
                //画几何图形映射的面积
                drawPolygonArea = drawGeometryArgs.feature.geometry.getGeodesicArea();
                //圆圈的半径大于零才能弹出项目明细
                //if(drawRadius>0){
	                var feature = new SuperMap.Feature.Vector();
	                feature.geometry = drawGeometryArgs.feature.geometry,
	                        feature.style = style;
	                vectorLayer.addFeatures(feature);
	                //var urlData="http://59.255.137.11:5240/iserver/services/map-test/rest/maps/mymap";
	                //var urlData="http://59.255.137.11/iserver/services/map-test/rest/maps/mymap";
	                var urlData=ZHFX.SERVER_IP+"/iserver/services/map-test/rest/maps/mymap";
	                var queryParam, queryByGeometryParameters, queryService;
	                queryParam = new SuperMap.REST.FilterParameter({name: "行政区划_1@tt"});
	                queryByGeometryParameters = new SuperMap.REST.QueryByGeometryParameters({
	                    queryParams: [queryParam],
	                    geometry: drawGeometryArgs.feature.geometry,
	                    spatialQueryMode: SuperMap.REST.SpatialQueryMode.INTERSECT
	                });
	               
	                
	                queryService = new SuperMap.REST.QueryByGeometryService(urlData, {
	                    eventListeners: {
	                        "processCompleted": processCompleted,
	                        "processFailed": processFailed
	                    }
	                });
	                queryService.processAsync(queryByGeometryParameters);
                //}
            }
			
			
            //画多边形结束后事件
            function drawPolygonCompleted(drawGeometryArgs) {
            	//标识几何图形：0表示画点，1表示画圆，2表示画矩形，3表示画多边形
                drawPolygonFlag = 3;
                var feature = new SuperMap.Feature.Vector();
                feature.geometry = drawGeometryArgs.feature.geometry,
                        feature.style = style;
                vectorLayer.addFeatures(feature);
                //将多边形的geometry对象转化成字符串
                myGeometryStr = drawGeometryArgs.feature.geometry.toString();
				//var myFeometry = new Supermap.Geometry.fromWKT(geoStr);
                
				
                //画几何图形映射的面积
                drawPolygonArea = drawGeometryArgs.feature.geometry.getGeodesicArea();
                //var urlData="http://59.255.137.11:5240/iserver/services/map-test/rest/maps/mymap";
                //var urlData="http://59.255.137.11/iserver/services/map-test/rest/maps/mymap";
                var urlData=ZHFX.SERVER_IP+"/iserver/services/map-test/rest/maps/mymap";
                
                var queryParam, queryByGeometryParameters, queryService;
                queryParam = new SuperMap.REST.FilterParameter({name: "行政区划_1@tt"});
                queryByGeometryParameters = new SuperMap.REST.QueryByGeometryParameters({
                    queryParams: [queryParam],
                    geometry: drawGeometryArgs.feature.geometry,
                    spatialQueryMode: SuperMap.REST.SpatialQueryMode.INTERSECT
                });
                //画几何图形的中心坐标
                drawPointX= (drawGeometryArgs.feature.geometry.bounds.left + drawGeometryArgs.feature.geometry.bounds.right)/2;
                drawPointY= (drawGeometryArgs.feature.geometry.bounds.bottom + drawGeometryArgs.feature.geometry.bounds.top)/2;
                
                
                queryService = new SuperMap.REST.QueryByGeometryService(urlData, {
                    eventListeners: {
                        "processCompleted": processCompleted,
                        "processFailed": processFailed
                    }
                });
                queryService.processAsync(queryByGeometryParameters);
                
            }
			
            //画点结束事件
            function drawPointCompleted(drawGeometryArgs) {
            	//将画点的geometry对象赋给geometryInfo全局变量
            	geometryInfo = drawGeometryArgs.feature.geometry;
            	
            	//标识几何图形：0表示画点，1表示画圆，2表示画矩形，3表示画多边形
                drawPolygonFlag = 0;
                //画几何图形的中心坐标
                drawPointX = drawGeometryArgs.feature.geometry.x;
                drawPointY = drawGeometryArgs.feature.geometry.y;
                
                closeInfoWin();
	   	        var icon = new SuperMap.Icon();
	   	        var popup = new SuperMap.Popup.FramedCloud("popwin",
	   	        new SuperMap.LonLat(drawPointX,drawPointY),
	   	        null,
	   	       	setMark(),
	   	        icon,
	   	        true);
	   	        infowin = popup;
	   	        map.addPopup(popup);
	   	        
	   	    
                
            }
            
            
 
            
            
            //矩形
            function drawRectangleCompleted(obj) {
            	//标识几何图形：0表示画点，1表示画圆，2表示画矩形，3表示画多边形
                drawPolygonFlag = 2;
                drawRectangle.deactivate();
                //先清除上次的显示结果
                clearStatus();
         
                var feature = obj.feature;
                feature.style = style;
                vectorLayer.addFeatures(feature);
                
                //将矩形转化成多边形
                var points =[new SuperMap.Geometry.Point(obj.feature.geometry.bounds.left,obj.feature.geometry.bounds.bottom),
                             new SuperMap.Geometry.Point(obj.feature.geometry.bounds.right,obj.feature.geometry.bounds.bottom),
                             new SuperMap.Geometry.Point(obj.feature.geometry.bounds.right,obj.feature.geometry.bounds.top),
                             new SuperMap.Geometry.Point(obj.feature.geometry.bounds.left,obj.feature.geometry.bounds.top)
                         ],
                           linearRings = new SuperMap.Geometry.LinearRing(points),
                           polygonMap = new SuperMap.Geometry.Polygon([linearRings]);
                
                
                //将多边形的geometry对象转化成字符串
                myGeometryStr = polygonMap.toString();
                
                
                //画几何图形映射的面积
                drawPolygonArea = obj.feature.geometry.getArea();
                //将经纬度面积转换成地理面积，单位为平方米
                drawPolygonArea = drawPolygonArea *111*111*1000000
                var queryBounds = feature.geometry.bounds;
                
                //画几何图形的中心坐标
                drawPointX= (queryBounds.left + queryBounds.right)/2;
                drawPointY= (queryBounds.bottom + queryBounds.top)/2;
                //画几何图形的半径
                drawRadius= (queryBounds.right - queryBounds.left )/2;
                var queryParam, queryByBoundsParams, queryService;
                queryParam = new SuperMap.REST.FilterParameter({name: "行政区划_1@tt"});//FilterParameter设置查询条件，name是必设的参数，（图层名称格式：数据集名称@数据源别名）
                queryByBoundsParams = new SuperMap.REST.QueryByBoundsParameters({queryParams: [queryParam], bounds: queryBounds});//queryParams查询过滤条件参数数组。bounds查询范围
                // 服务地址
                //var urlData="http://59.255.137.11:5240/iserver/services/map-test/rest/maps/mymap";
                //var urlData="http://59.255.137.11/iserver/services/map-test/rest/maps/mymap";
                var urlData=ZHFX.SERVER_IP+"/iserver/services/map-test/rest/maps/mymap";
                queryService = new SuperMap.REST.QueryByBoundsService(urlData, {
                    eventListeners: {
                        "processCompleted": processCompleted,
                        "processFailed": processFailed
                    }
                });
                queryService.processAsync(queryByBoundsParams);//向服务端传递参数，然后服务端返回对象
                
            }

            //画几何图形事件结束后触发的函数
            function processCompleted(queryEventArgs) {
                drawPolygon1.deactivate();
                drawPolygon2.deactivate();
                drawPoint.deactivate();
                drawLine.deactivate();
                drawRectangle.deactivate();
                
                var i, j, result = queryEventArgs.result;
                //画几何图形中的地区编码
                drawPolygonCode = "";
                if (result && result.recordsets) {
                    for (i=0, recordsets=result.recordsets, len=recordsets.length; i<len; i++) {
                        if (recordsets[i].features) {
                            for (j=0; j<recordsets[i].features.length; j++) {
                            	//DIC_ITEM_CODE  地区编码
                                var feature = recordsets[i].features[j];
                                var point = feature.geometry;
                                drawPolygonCode = drawPolygonCode + "," + result.recordsets[i].features[j].data.DIC_ITEM_CODE;
                                
//                                 if(point.CLASS_NAME == SuperMap.Geometry.Point.prototype.CLASS_NAME){
//                                     var size = new SuperMap.Size(44, 33),
//                                             offset = new SuperMap.Pixel(-(size.w/2), -size.h),
//                                             icon = new SuperMap.Icon("../js/forJavaScript/images/markerbig.png", size, offset);
//                                     markerLayer.addMarker(new SuperMap.Marker(new SuperMap.LonLat(point.x, point.y), icon));
//                                 }else{
//                                     feature.style = style;
//                                     vectorLayer.addFeatures(feature);
//                                 }
                            }
                        }
                    }
                }
                debugger;
                	//获取几何图形圈中的项目数据
                    getDrawPolygonInfo();
                
            }
            
            //异常提示信息
            function processFailed(e) {
                alert(e.error.errorMsg);
            }
            
            //清除地图的覆盖物（清除按钮触发）
            function clearFeatures() {
                vectorLayer.removeAllFeatures();
                //vectorLayer1.removeAllFeatures();
                markerLayer.clearMarkers();
            }
            
            
            //后台传来的结果集(地图中分各个地区展示黄色圆圈，红色气泡的值)
            var dataList = "";
            
            //加载全国各个地区的项目信息
            function addData(params){
            	
            			//将当前地图级别存储起来（方便判断滚动鼠标是放大还是缩小）
            	        sessionZoom = map.getZoom();
            		    // 同步加载 全国的项目信息
	                    $.ajax({
	                        url: "${ctx}/supermap/superMap!getQueryByGeometry",
	                        data : params,
	                        dataType: "json",
	                        async:false,
	                        success: function (data) {
	                        	dataList = data;
	                        	
	                        	//清除上一级的气泡
	                            markerLayer.clearMarkers();
	                        	 //定义地图各个地方的数据集数组removeLayer
	           	                var circleVectorArr=[]; 
	                        	var totalRadVal=0; 
	                        	var moneyRadVal=0;
	                        	//加载的地区个数
	                        	var dataListSize = dataList.length;
	                        	//遍历数据对象生成总项目个数和总投资平均数
	                        	for(var i=0; i<dataListSize; i++){
	                        		moneyRadVal = moneyRadVal + dataList[i]["investMon1"]/1;
	                        		totalRadVal = totalRadVal + dataList[i]["cnt"]/1;
	                        	}
	                        	
	                        	moneyRadVal = moneyRadVal/dataListSize;
	                        	totalRadVal = totalRadVal/dataListSize;
	                        	
	                			 // 遍历数据对象在地图上生成不同大小的圈和气泡
	                            for (var i=0; i<dataListSize; i++) {
	       	                 		//删除地图上的各个数据集图形
	       	                 		vector.removeAllFeatures();
	       	                 		//根据经纬定位地图相应的位置
	       	                 		var centerPoint=new SuperMap.Geometry.Point(dataList[i]["smx"],dataList[i]["smy"]);
	       	                 		var radiusVal = 0;
	       	                 		
	       	                 		//定义地图标点展现是按照哪个类型展示，0表示按照项目总投资，1表示按照项目个数
	       	                 		if(projectShowFlag=="0"){
	       	                 			radiusVal = dataList[i]["investMon1"]/moneyRadVal;
	       	                 			if(level==1){
	       	                 				
	       	                 			}else if(level==2){
	       	                 				if(radiusVal>0.5){
	       	                 					radiusVal = 0.5 + (radiusVal -0.5)*0.1;
	       	                 				}
	       	                 			}else{
	       	                 				radiusVal = radiusVal*0.05;
	       	                 			}
	       	                 		}else{
	       	                 			radiusVal = dataList[i]["cnt"]/totalRadVal;
		       	                 		if(level==1){
	       	                 				
	       	                 			}else if(level==2){
	       	                 				if(radiusVal>0.5){
	       	                 					radiusVal = 0.5 + (radiusVal -0.5)*0.1;
	       	                 				}
	       	                 			}else{
	       	                 				radiusVal = radiusVal*0.05;
	       	                 			}
	       	                 		}
	       	                 		//定义地图上的圆圈的大小
	       	                 		var circleP2=createCircle(centerPoint,radiusVal,25,360,360);
	       	                 		//创建地图上的圆圈对象
	       	                 		var circleVector2= new SuperMap.Feature.Vector(circleP2);
	       	                 		circleVector2.style={
	       	                 			strokeColor:"#91B9EA",
	       	                 			fillColor:"#91B9EA",
	       	                 			// fillColor:"#DC143C",
	       	                 			strokeWidth:1,
	       	                 			fillOpacity:0.5,
	       	                 			// fontWeight:"bold",
	       	                 			label:dataList[i]["itemName"],
	       	                 		 	labelSelect:"true",
	       	                 			fontSize:"0.5em",
	       	                 			fontColor:"#000000"
	       	                 		};
	       	                 		circleVectorArr[i]=circleVector2;  
	                            }
	                			 
	                			 
	                			 
	                			 
	                   		
	                   		   vector.addFeatures(circleVectorArr);
		                   		//加载各个地方气泡图形
		                       	addDataMarker();
	                        }
	                    });
            }
            
            //创建各个地区圆圈对象
            function createCircle(origin, radius, sides,r,angel){
            	var rR = r*Math.PI/(180*sides);
            	var rotatedAngle, x, y;
            	var points = [];
            	for(var i=0; i<sides; ++i) {
            		rotatedAngle = rR*i;
            		x = origin.x + (radius * Math.cos(rotatedAngle));
            		y = origin.y + (radius * Math.sin(rotatedAngle));
            		points.push(new SuperMap.Geometry.Point(x, y));
            	}
            	rotatedAngle = r*Math.PI/180;
            	x = origin.x + (radius * Math.cos(rotatedAngle));
            	y = origin.y + (radius * Math.sin(rotatedAngle));
            	points.push(new SuperMap.Geometry.Point(x, y));
            	
            	var ring = new SuperMap.Geometry.LinearRing(points);
            	ring.rotate(parseFloat(angel),origin);
            	var geo = new SuperMap.Geometry.Collection([ring]);
            		geo.origin = origin;
            		geo.radius = radius;
            		geo.r = r;
            		geo.angel = angel;
            		geo.sides = sides;
            		geo.polygonType = "Curve";
            	return geo;
            }
            
            //清除地图上的覆盖物
            function removeData(){
            	dataAdded =false;
            	vector.removeAllFeatures();
            	vector.refresh();
            }
			
            
            //项目总投资addMoney
            function addMoney(){
            	//当切换地图展现
            	if(projectShowFlag == "1"){
	            	//关闭信息框
	            	closeInfoWin();
	            	projectShowFlag = "0"; 
	            	//清除地图上的覆盖物
	            	removeData();
	            	//返回最初地图级别
	            	map.zoomTo(map.getZoom());
	            	//重新加载地图项目信息
	            	addData(sessionParams);
            	}
            }
            
            //项目个数 addTotals
            function addTotals(){
            	//当切换地图展现
            	if(projectShowFlag == "0"){
	            	//关闭信息框
	            	closeInfoWin();
	            	projectShowFlag = "1"; 
	            	//清除地图上的覆盖物
	            	removeData();
	            	//返回最初地图级别
	            	map.zoomTo(map.getZoom()); 
	            	//重新加载地图项目信息
	            	addData(sessionParams);
            	}
            }
            
            
            //当滚动鼠标至全国地图为第一级时，则重新加载地图
            function reloadSuperMap(){
            	//关闭信息框
            	closeInfoWin();
            	//清除地图上的覆盖物
            	removeData();
            	//返回最初地图级别
            	map.zoomTo(map.getZoom());
            	//重新加载地图项目信息
            	addData();
            }
            
            
          var proTotals = "";
      	  var proMoney = "";
          //添加气泡效果数据
          function addDataMarker(){
        	  markerLayer.removeMarker(marker);
	          var size = new SuperMap.Size(34,26);
	          var offset = new SuperMap.Pixel(-(size.w/2), -size.h);
	          var icon = new SuperMap.Icon('../js/forJavaScript/images/markerbig.png', size, offset);
	          
	          // 遍历对象
              for (var i=0; i<dataList.length; i++) {
	              marker =new SuperMap.Marker(new SuperMap.LonLat(dataList[i]["smx"],dataList[i]["smy"]),icon) ;
		          marker.events.on({
			          "click":openInfoWin,
			          "touchstart":openInfoWin, //假如要在移动端的浏览器也实现点击弹框，则在注册touch类事件
			          "scope": marker
		          });
		          markerLayer.addMarker(marker);
		          
              }
          }
          
          //打开对应的信息框
          var infowin = null;
          function openInfoWin(){
	          closeInfoWin();
	          var marker = this;
	          var lonlat = marker.getLonLat();
	          var size = new SuperMap.Size(0, 33);
	          var offset = new SuperMap.Pixel(11, -30);
	          var icon = new SuperMap.Icon("../js/forJavaScript/images/markerbig.png", size, offset);
	          var popup = new SuperMap.Popup.FramedCloud("popwin",
	          new SuperMap.LonLat(lonlat.lon,lonlat.lat),
	          null,
	          proInfoDetail(lonlat.lon,lonlat.lat),
	          icon,
	          true);
	          infowin = popup;
	          map.addPopup(popup);
          }
          
          //点击气泡弹出区域明细
          function proInfoDetail(prox,proy){
              var sContent = '<div class="quyuPopcont">' +
                      '<div class="quyuBar">' +
                      '<label>区域名称：'+ getPlaceName(prox,proy) +' </label>' +
                      '<input type="button" class="btn_change defaultBtn" onclick="showClickDetail('+getPlaceCode(prox,proy)+')" value="项目明细" />' +
                      '</div>' +
                      '<p>' +
                      '<label>区域内项目个数：</label>' +
                      '<label  id = "sum">'+ getProTotals(prox,proy) +'个</label><br>' +
                      '</p>' +
                      '<p>' +
                      '<label>区域内项目总金额：</label>' +
                      '<label  id = "money"> '+ getProMoney(prox,proy) +'亿元 </label><br>' +
                      '</p>' +
                      '</div>';
              return sContent;
          }
          
          //获取项目个数
          function getProTotals(prox,proy){
        	  // 遍历对象
              for (var i=0; i<dataList.length; i++) { 
            	  if(dataList[i]["smx"]==prox && dataList[i]["smy"]==proy){
            		  //分别给项目个数变量赋值
                	  return dataList[i]["cnt"];
            	  }
              }
        	  return ;
          }
          
          //获取项目总金额
          function getProMoney(prox,proy){
        	  // 遍历对象
              for (var i=0; i<dataList.length; i++) { 
            	  if(dataList[i]["smx"]==prox && dataList[i]["smy"]==proy){
            		//分别给项目总投资变量赋值
            	 	 return dataList[i]["investMon1"];
            	  }
              }
        	  return ;
          }
          
          
          //获取地区名称
          function getPlaceName(prox,proy){
        	  // 遍历对象
              for (var i=0; i<dataList.length; i++) { 
            	  if(dataList[i]["smx"]==prox && dataList[i]["smy"]==proy){
            		//分别给项目总投资变量赋值
            	 	 return dataList[i]["itemName"];
            	  }
              }
        	  return ;
          }
          
          //获取地区编码
          function getPlaceCode(prox,proy){
        	  // 遍历对象
              for (var i=0; i<dataList.length; i++) { 
            	  if(dataList[i]["smx"]==prox && dataList[i]["smy"]==proy){
            		//分别给项目总投资变量赋值
            	 	 return dataList[i]["itemCode"];
            	  }
              }
        	  return ;
          }
          
          
          //弹出明细报表信息(点击气泡弹出该地区的项目明细)
          function showClickDetail(placeCode){
        	  //getSuperMapGovMentReportData
        	  var params = new Object();
        	  //获取点击地图某一区域的编码值
      		  buildPlaceCode = placeCode;
        	  //点击气泡获取该点地区编码
        	  params["params.BuildPlaceCode"] = placeCode;
        	  //查询控件相应的值
        	  params["params.region"] = projectRegionSearch;
        	  params["params.INDUSTRY"] = industryCodeSearch;
        	  params["params.actualStartTime1"]=actualStartTime1Search;
	          params["params.actualStartTime2"]=actualStartTime2Search;
	          params["params.actualEndTime1"]=actualEndTime1Search;
	          params["params.actualEndTime2"]=actualEndTime2Search;
        	  
        	  // 异步加载 全国的项目信息
              $.ajax({
                  url: "${ctx}/supermap/superMap!getSuperMapGovMentReportData",
                  data : params,
                  dataType: "json",
                  cache:false,
                  async:false,
                  error: function () {//请求失败处理函数
	      				$.dialog.alert("数据获取失败！","warning");
	      		  },
      			  success:function(data){ //请求成功后处理函数。
      				// 对于多个数据集的，uuid用连续的数字区分
      				var requestParam="";
      				requestParam = requestParam + "&uuid1="+(data.uuid1);
      				requestParam = requestParam + "&reportRequestUrl="+(data.reportRequestUrl);
      				var _winUrl=cjkEncode("/BI/ReportServer?reportlet=loadData/superMapGovMent1.cpt&__bypagesize__=true"+requestParam);
      				var winOption = "height=600,width=800,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
      			    window.open(_winUrl,window, winOption);
      			  }
              });
          }
          
          
          //关闭信息框
          function closeInfoWin(){
	          if(infowin){
		          try{
			          infowin.hide();
			          infowin.destroy();
		          }
		          catch(e){
		        	  
		          }
	          }
          }
          
          //清除画几何图形区域
          function clearDrawPolygon(){
        	    //删除地图上的各个数据集图形
            	vector1.removeAllFeatures();
            	//删除标记弹框
            	closeInfoWin();
            	
          }
       
          
          //根据查询组条件重新加载地图数据
          function datagridInfo(projectRegion,industryCode,actualStartTime1,actualStartTime2,actualEndTime1,actualEndTime2){
        	    projectRegionSearch = projectRegion;
        	    industryCodeSearch = industryCode;
        	    actualStartTime1Search = actualStartTime1;
        	    actualStartTime2Search = actualStartTime2;
        	    actualEndTime1Search = actualEndTime1; 
        	    actualEndTime2Search = actualEndTime2;
	        	//关闭信息框
	          	closeInfoWin();
	          	//清除地图上的覆盖物
	          	//removeData();
	            //前台组装的查询条件
	            var params =new Object();
	          	params["params.region"]=projectRegion;
	          	params["params.INDUSTRY"]=industryCode;
	          	params["params.actualStartTime1"]=actualStartTime1;
	          	params["params.actualStartTime2"]=actualStartTime2;
	          	params["params.actualEndTime1"]=actualEndTime1;
	          	params["params.actualEndTime2"]=actualEndTime2;
	            //缓存起来过滤条件
                sessionParams = params;
	          	//重新加载地图项目信息
	          	addData(params);
          }
          
          
          //获取点击地图的某一地理位置信息
          function getRegionInfo(data){
        	  //markerLayer1.clearMarkers();
        	  //获取点击的xy坐标值
              var xval = data.offsetX;
              var yval = data.offsetY;
              var _lonlat = map.getLonLatFromViewPortPx(new SuperMap.Pixel(xval,yval));
              //标记双击在地图的某个位置（调试用的，标记点击的位置）
              //markerLayer.addMarker(new SuperMap.Marker(new SuperMap.LonLat(_lonlat.lon, _lonlat.lat)));
              // 查询所在地区
              centerPoint=new SuperMap.Geometry.Point(_lonlat.lon, _lonlat.lat);
              queryByDistance();
              
          }
          
          
          function queryByDistance() {
              var queryByDistanceParams = new SuperMap.REST.QueryByDistanceParameters({
                  queryParams: new Array(new SuperMap.REST.FilterParameter({name: "行政区划_1@tt"})),
                  distance:1,
                  expectCount:1,
                  isNearest:true,
                  geometry: centerPoint
              });
              //var urlData="http://59.255.137.11:5240/iserver/services/map-test/rest/maps/mymap";
              //var urlData="http://59.255.137.11/iserver/services/map-test/rest/maps/mymap";
              var urlData=ZHFX.SERVER_IP+"/iserver/services/map-test/rest/maps/mymap";
              var queryByDistanceService = new SuperMap.REST.QueryByDistanceService(urlData);
              queryByDistanceService.events.on({
                  "processCompleted": processCompletedClick,
                  "processFailed": processFailed
              });
              queryByDistanceService.processAsync(queryByDistanceParams);
          }
          
          function processCompletedClick(queryEventArgs) {
              vectorLayer.removeAllFeatures();
              var i, j, result = queryEventArgs.result;
              for(i = 0;i < result.recordsets.length; i++) {
                  for(j = 0; j < result.recordsets[i].features.length; j++) {
                      //var point = result.recordsets[i].features[j].geometry;
                      //获取当前点击地图的地区编码（县级）
                      xianJiClickCode = result.recordsets[i].features[j].data.DIC_ITEM_CODE;
                      //获取当前点击地图的地区经纬度坐标
                      xianJiClickSmX = result.recordsets[i].features[j].data.SmX;
                      xianJiClickSmY = result.recordsets[i].features[j].data.SmY;
                      //SmX:"88.311099"SmY:"43.363668"
                       // console.dir(result.recordsets[i].features[j].data);
                      //markerLayer2.addMarker(new SuperMap.Marker(new SuperMap.LonLat(point.x, point.y)));
                  }
              }
              // 追加地图 点击事件
              onClicks();
          }
          
          
          ////////////////////////////////////////////////////
          //以下为自定义区域相关的代码
          ////////////////////////////////////////////////////
          
          //后台传来的结果集(几何图形中的数据集)
          var drawPolygonDataList = "";
          //获取几何图形圈中的项目数据
          function getDrawPolygonInfo(){
        	    //前台组装的查询条件
	            var params =new Object();
	           
        	    params["params.region"]=projectRegionSearch;
	          	params["params.BuildPlaceCode"] = buildPlaceCode;
	          	//画几何图形区域的地区编码集合
	          	if(drawPolygonCode != ""){
	          		params["params.drawPolygonCode"]=drawPolygonCode.substring(1,drawPolygonCode.length);
	          	}else{
	          		params["params.drawPolygonCode"]="kkkkkk";
	          	}
	          	
	          	params["params.INDUSTRY"]=industryCodeSearch;
	          	params["params.actualStartTime1"]=actualStartTime1Search;
	          	params["params.actualStartTime2"]=actualStartTime2Search;
	          	params["params.actualEndTime1"]=actualEndTime1Search;
	          	params["params.actualEndTime2"]=actualEndTime2Search;
        	  
	         
    		    // 同步加载 全国的项目信息
                $.ajax({
                    url: "${ctx}/supermap/superMap!getQueryByGeometry",
                    data : params,
                    dataType: "json",
                    async:false,
                    success: function (data) {
                    	drawPolygonDataList = data;
                    	
                    	var drawPolygonCustTotal = 0;
                    	var drawPolygonCustMoney = 0;
                    	for(var i=0; i < drawPolygonDataList.length; i++){
                    		drawPolygonCustMoney = drawPolygonCustMoney + drawPolygonDataList[i]["investMon1"]/1;
                    		drawPolygonCustTotal = drawPolygonCustTotal + drawPolygonDataList[i]["cnt"]/1;
                    	}
                    	
                    	//弹出所画区域项目明细信息
                    	drawPolygonOpenWin(drawPolygonCustTotal,drawPolygonCustMoney);
                    	
                    	
                    	
                    
                    }
                });
        	  
          }
          
          //弹出所画区域项目明细信息
          function drawPolygonOpenWin(drawPolygonCustTotal,drawPolygonCustMoney){
              //画几何图形结束弹出下面信息
              var sContent = '<div class="quyuPopcont" id="save_div">' +
                      '<div class="quyuBar">' +
                      '<label>区域名称：</label>' +
                      '<input name="coorNames" id="coorNames" type="text" />' +
                      '<input type="button" onclick="saveCoordinates()" class="btn_change" value="保存" />' +
                      '<input type="button" onclick="showDrawPolygonDetail()" value="项目明细" />' +
                      '</div>' +
                      '<p>' +
                      '<label>区域面积：</label>' +
                      '<label  id = "area">' + (drawPolygonArea/1000000).toFixed(2) + ' 平方公里</label>' +
                      '<p>' +
                      '<td>' +
                      '<label>区域内项目个数：</label>' +
                      '<label  id = "sum">' + drawPolygonCustTotal + ' 个</label>' +
                      '</p>' +
                      '<p>' +
                      '<label>区域内项目总金额：</label>' +
                      '<label  id = "money">' + drawPolygonCustMoney + ' 亿元</label>' +
                      '</p>' +
                      '<p>' +
                      '<label>区域内项目密度：</label>' +
                      '<label>' + (drawPolygonCustTotal*1000000/drawPolygonArea).toFixed(4) + ' 个/平方公里 </label>' +
                      '</p>' +
                      '</div>';
        	  		
                  //画几何图形结束后弹出
    	          var popup = new SuperMap.Popup.FramedCloud("popwin",
    	          new SuperMap.LonLat(drawPointX,drawPointY),
    	          null,
    	          sContent,
    	          new SuperMap.Icon(),
    	          true);
    	          infowin = popup;
    	          map.addPopup(popup);
    	          
    	          //0为画几何图形保存项目信息标识，1为查看几何图形项目明细信息标识
    	          if("1" == markFlag){
            		  $("#coorNames").val(markName);
            		  $("#saveButton").remove();
            		  markFlag = "0";
            	  }
    	          
    	         //判断当前画的几何图形是否是多边形，是则重新绑定双击事件
    	         if(drawPolygonFlag==3){
    	        	 dblclick1();
    	         }
          }
          
          
          //弹出明细报表信息（几何图形区域中的项目明细）
          function showDrawPolygonDetail(){
        	  
        	  var params = new Object();
        	  //点击气泡获取该点地区编码
        	  params["params.BuildPlaceCode"] = buildPlaceCode;
        	  //查询控件相应的值
        	  params["params.region"] = projectRegionSearch;
        	  //画几何图形区域的地区编码集合
	          params["params.drawPolygonCode"]=drawPolygonCode.substring(1,drawPolygonCode.length);
        	  params["params.INDUSTRY"] = industryCodeSearch;
        	  params["params.actualStartTime1"]=actualStartTime1Search;
	          params["params.actualStartTime2"]=actualStartTime2Search;
	          params["params.actualEndTime1"]=actualEndTime1Search;
	          params["params.actualEndTime2"]=actualEndTime2Search;
        	  
        	  // 异步加载 全国的项目信息
              $.ajax({
                  url: "${ctx}/supermap/superMap!getSuperMapGovMentReportData",
                  data : params,
                  dataType: "json",
                  cache:false,
                  async:false,
                  error: function () {//请求失败处理函数
	      				$.dialog.alert("数据获取失败！","warning");
	      		  },
      			  success:function(data){ //请求成功后处理函数。
      				// 对于多个数据集的，uuid用连续的数字区分
      				var requestParam="";
      				requestParam = requestParam + "&uuid1="+(data.uuid1);
      				requestParam = requestParam + "&reportRequestUrl="+(data.reportRequestUrl);
      				var _winUrl=cjkEncode("/BI/ReportServer?reportlet=loadData/superMapGovMent1.cpt&__bypagesize__=true"+requestParam);
      				var winOption = "height=600,width=800,top=50,left=300,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
      			    window.open(_winUrl,window, winOption);
      			  }
              });
          }
          
          
          
          //保存自定义几何图形区域的项目信息
          function saveCoordinates() {
        	  var re = /^[\u4e00-\u9fa5a-z0-9]+$/gi;
        	  $("#coorNames").val($.trim($("#coorNames").val()));
        	  if ($("#coorNames").val()=="") {
        		  alert("请输入自定义区域名称！");
        		  $("#coorNames").focus();
        		  return false;
        	  }
        	  if ($("#coorNames").val().length > 20) {
        		  alert("自定义区域名称超长！请输入20位以内的汉字、数字或英文字母！");
        		  $("#coorNames").focus();
        		  return false;
        	  }
        	  if (!re.test($("#coorNames").val())) {
        		  alert("自定义区域名称只能输入汉字、数字或英文字母！");
        		  $("#coorNames").focus();
        		  return false;
        	  }
        	  
        	  var params = new Object();
        	  //自定义名称
        	  params["params.coorNames"] = $("#coorNames").val();
        	  
        	  //标识几何图形：0表示画点，1表示画圆，2表示画矩形，3表示画多边形
        	  params["params.drawPolygonFlag"] = drawPolygonFlag;
        	  //自定义区域 坐标
        	  params["params.coorDinates"] = drawPointX +","+drawPointY;
        	  //将矩形、多边形的几何图形的  对象保存至coorDinates，面积保存至radius中
 			  if(drawPolygonFlag == "2" || drawPolygonFlag == "3"){
 				  //自定义区域 坐标
 	        	  params["params.myGeometryStr"] = myGeometryStr;
 	        	  //自定义区域半径
 	        	  params["params.radius"] = drawPolygonArea;
        	  }else{
        		  
            	  //自定义区域半径
            	  params["params.radius"] = drawRadius;
        	  }
        	  
        	  
        	  
        	  
        	  //保存到数据库
              $.ajax({
            	 url: "${ctx}/supermap/superMap!saveCoordinates",
                 type : 'POST',
                 data : params,
                 async : false,
                 success : function(data) {
                	 if (data == "no") {
                		 alert("自定义区域名称已存在！");
               		     $("#coorNames").focus();
            		     return false;
                	 }
                	 //先清除上次的显示结果
                	 clearStatus();
                	 //获取自定义区域的数据
             		 getTools();
                	 alert("保存成功！");
                 },
                 error : function(data) {
                     alert("自定义区域数据获取失败，请刷新页面！");
                 }
             });
        	  
          }
          
          
          
          // 点击工具 button 
          function getTools(){
	              $.ajax({
	            	  url: "${ctx}/supermap/superMap!searchCoordinates.action",
	            	  dataType: "json",
		              async : false,
		              success : function(data) {
		                  //成功
		                  var content = "";
		                      markDateList = eval(data);
		                  for(var i = 0; i < markDateList.length; i++){
		                      var name = markDateList[i].NAME;
		                      var id = markDateList[i].SYSID;
		                      //content = content + "<p id='" + id + "' style='overflow:hidden;text-overflow:ellipsis;white-space:nowrap;'><span class='switch_close' onclick=\"delOption('" + id + "','" + name + "')\"></span><span onclick=\"switchColor('" + id + "','" + name + "')\">" + name + "</span></p>";
		                     
		                      content = content + "<li  id='" + id + "'><a href=\"javascript:switchColor('" + id + "','" + name + "')\">" + name + "</a><em class='li_close'  onclick=\"delOption('" + id + "','" + name + "')\"></em></li>";
		                      
		                      
		                  }
		                  $("#kkkkkk").html(content);
		              },
		              error : function(data) {
		                  alert("自定义区域数据获取失败，请刷新页面！");
		              }
		          });
          }
          

          
          //标记信息窗口
          function setMark(){
        	  
        	  var sContent = '<div style="text-align:left" id="mark_div">' +
              '<table style="line-height:normal;">' + 
              '    <tr>' + 
              '        <td>' + 
              '            <label style="font-size: 16px;">信息窗口</label><br><br>' + 
              '        </td>' + 
              '    </tr>' + 
              '    <tr>' + 
              '        <td>' + 
              '            <select id="markLen">' +
              '                <option value="0.9">附近100公里处</option>' +
              '                <option value="1.8">附近200公里处</option>' +
              '                <option value="2.7">附近300公里处</option>' +
              '                <option value="4.5">附近500公里处</option>' +
              '            </select>' +
              '            <input type="button" onclick="searchMarkLen()" class="btn_blue" value="搜索" />' +
              '        </td>' + 
              '    </tr>' + 
              '</table>' + 
              '</div>';
              
              //清除画点区域遮挡物
              drawPoint.deactivate();
              
              return sContent;
          }
          
          
          
          //点击标记框中搜索按钮触发事件
          function searchMarkLen(){
        	  //获取标记区域的半径
        	  var len = parseFloat($("#markLen").val());
        	  //用于保存到数据库中的字段值
        	  drawRadius = $("#markLen").val();
        	  //在地图上标记制作相应的几何图形
        	  createDrawPolygonMark(len,"1");
        	  
          }
          
          //在地图上标记制作相应的几何图形
          function createDrawPolygonMark(len,polygonTypeFlag){
        	  
        	  //给标记几何图形生成气泡
       	 	  var size = new SuperMap.Size(44, 33),
     			offset = new SuperMap.Pixel(-(size.w/2), -size.h),
     			icon = new SuperMap.Icon("../js/forJavaScript/images/markerbig.png", size, offset);
		   	    marker = new SuperMap.Marker(new SuperMap.LonLat(drawPointX,drawPointY), icon);
		   	    marker.events.on({
			          "click":getDrawPolygonInfo,
			          "touchstart":getDrawPolygonInfo, //假如要在移动端的浏览器也实现点击弹框，则在注册touch类事件
			          "scope": marker
		        });
		   	 markerLayerDefined.addMarker(marker);
        	  
              
            //根据经纬定位地图相应的位置
          	var centerPoint=new SuperMap.Geometry.Point(drawPointX,drawPointY);
	          
	        //定义地图上的圆圈的大小
       		var circleP2=createCircle(centerPoint,len,25,360,360);
	        
       	 
       	 
       		//创建地图上的圆圈对象
       		var circleVector2= new SuperMap.Feature.Vector(circleP2);
       		
       		circleVector2.style={
       			strokeColor:"#CAFF70",
       			fillColor:"#DC143C",
       			strokeWidth:1,
       			fillOpacity:0.8,
       			fontWeight:"bold",
       			label:"",
       		 	labelSelect:"true"
       		};
       		
       	  	vector1.addFeatures(circleVector2);
       		//删除标记弹框
       	 	if(infowin){
       	 		map.removePopup(infowin);
       		}
       		//获取标记区域的地区编码信息
       	    searchMarkQueryByDistance(centerPoint,len);
       	    //获取标记区域的地理面积
    	    drawPolygonArea = 3.1415926*len*len*111000*111000;
       		
          }
          
          
          
          //在地图上标记制作相应的几何图形
          function createDrawPolygonMarkOther(geometryStr,polygonTypeFlag){
        	  
        	  //给标记几何图形生成气泡
       	 	  var size = new SuperMap.Size(44, 33),
     			offset = new SuperMap.Pixel(-(size.w/2), -size.h),
     			icon = new SuperMap.Icon("../js/forJavaScript/images/markerbig.png", size, offset);
		   	    marker = new SuperMap.Marker(new SuperMap.LonLat(drawPointX,drawPointY), icon);
		   	    marker.events.on({
			          "click":getDrawPolygonInfo,
			          "touchstart":getDrawPolygonInfo, //假如要在移动端的浏览器也实现点击弹框，则在注册touch类事件
			          "scope": marker
		        });
		   	 markerLayerDefined.addMarker(marker);
       	 
       	 
       		//创建地图上的圆圈对象
       		var circleVector2= new SuperMap.Feature.Vector();
       		
       		circleVector2.geometry =  new SuperMap.Geometry.fromWKT(geometryStr);
       		circleVector2.style={
       			strokeColor:"#CAFF70",
       			fillColor:"#DC143C",
       			strokeWidth:1,
       			fillOpacity:0.8,
       			fontWeight:"bold",
       			label:"",
       		 	labelSelect:"true"
       		};
       		
       	  	vector1.addFeatures(circleVector2);
       		//删除标记弹框
       	 	if(infowin){
       	 		map.removePopup(infowin);
       		}
       		//获取标记区域的地区编码信息
       	    searchMarkQueryByDistance(new SuperMap.Geometry.fromWKT(geometryStr),0.0000001);
       		
       		
          }
          
       
          
          //获取标记区域的地区编码信息
          function searchMarkQueryByDistance(centerPoint,len) {
        	   
        	  
        		//var urlData="http://59.255.137.11:5240/iserver/services/map-test/rest/maps/mymap";
        		//var urlData="http://59.255.137.11/iserver/services/map-test/rest/maps/mymap";
        		var urlData=ZHFX.SERVER_IP+"/iserver/services/map-test/rest/maps/mymap";
        		var queryByDistanceParams = new SuperMap.REST.QueryByDistanceParameters({
        			queryParams: new Array(new SuperMap.REST.FilterParameter({name: "行政区划_1@tt"})),
        			returnContent: true,
        			distance: len,
        			geometry: centerPoint
        		});
        		
        		
        		var queryByDistanceService = new SuperMap.REST.QueryByDistanceService(urlData);
        		queryByDistanceService.events.on({
        			"processCompleted": searchMarkprocessCompleted,
        			"processFailed": processFailed
        		});
        		queryByDistanceService.processAsync(queryByDistanceParams);
        	}
          
            //画标记区域图形结束事件
        	function searchMarkprocessCompleted(queryEventArgs) {
        		var i, j, result = queryEventArgs.result;
        		//画几何图形中的地区编码
                drawPolygonCode = "";
        		for(i = 0;i < result.recordsets.length; i++) {
        			for(j = 0; j < result.recordsets[i].features.length; j++) {
        				//DIC_ITEM_CODE  地区编码
                        drawPolygonCode = drawPolygonCode + "," + result.recordsets[i].features[j].data.DIC_ITEM_CODE;
        				
        			}
        		}
        		
        		//获取几何图形圈中的项目数据
                getDrawPolygonInfo();
        	}
          
          
          
        	// 删除自定义区域
            function delOption(id, name){
        		
        		
          	  //弹出提示框 
                //top.art.dialog.confirm('您确定要删除自定义区域“'+name+'”吗？', function(){
                //alert('您确定要删除自定义区域“'+name+'”吗？', function(){	
                	
                	//声明一对象，存放需删除自定义数据的ID
                	var params =  new Object();
                	params["params.CusId"]=id;
                    $.ajax({
                        url: "${ctx}/supermap/superMap!deleteCusCoordinates.action",
                        data:params,
                        type:"POST",
                        async:false,         
                        success:function(json){   
                            var data=json;
                            if(data=="ok"){
                            	//先清除上次的显示结果
                           	 	clearStatus();
                            	// 页面删除该条记录
                          	    $("#" + id).remove();
                          	    alert("删除成功");
                                //删除自定义区域的信息
                                //$("#coordinates p").removeClass("switch_nav");
          	  	  			    //$("#" + id).addClass("switch_nav");
                                
                            }else{
                                 alert('删除失败！请关闭当前页面重试！');
                            }
                        },
                        error : function() {
                            alert("删除失败！请关闭当前页面重试！");
                        }
                    });
//                   },function(){
                	  
//                   	//art.dialog.close();
//                   }); 
            }
        	
        	
        	//单击标记自定义区域中的值，地图做出相应的变化
            function switchColor(id, name) {
            	//先清除上次的显示结果
                clearStatus();
          	    //$("#coordinates p").removeClass("switch_nav");
          	    //$("#" + id).addClass("switch_nav");
          	    //查看标记弹框项目明细
          	    markFlag = "1";
          	    
          	    for(var i=0; i<markDateList.length; i++){
          	    	if(markDateList[i].SYSID==id){
	          	    	if(markDateList[i].FLAG == "2" || markDateList[i].FLAG == "3"){
	          	    		   
	          	    		
	          	    		   //画几何图形的中心坐标
		          	           drawPointX= markDateList[i].COORDINATES.split(",")[0];
		          	           drawPointY= markDateList[i].COORDINATES.split(",")[1];
		          	           //画几何图形的半径
		          	           drawPolygonArea= markDateList[i].RADIUS;
		          	           markName = markDateList[i].NAME;
		          	           //在地图上标记制作相应的几何图形
		          	           createDrawPolygonMarkOther(markDateList[i].MYGEOMETRYSTR,markDateList[i].FLAG);
	          	    	}else{
		          	    	   //画几何图形的中心坐标
		          	           drawPointX= markDateList[i].COORDINATES.split(",")[0];
		          	           drawPointY= markDateList[i].COORDINATES.split(",")[1];
		          	           //画几何图形的半径
		          	           drawRadius= markDateList[i].RADIUS;
		          	           markName = markDateList[i].NAME;
		          	           //在地图上标记制作相应的几何图形
		          	           createDrawPolygonMark(parseFloat(drawRadius),markDateList[i].FLAG);
	          	    	}  
          	    	}
          	    }
            }
        	
        	
        	
        	$(function(){
        		$('.btn_zg').click(function(){
        			$(this).siblings('.btn_zg').removeClass('btn_active');
        			$(this).addClass('btn_active');
        		});
        		$('.btn_ej').hover(function(){
        			$(this).addClass('btn_hover');
        		},function(){
        			$(this).removeClass('btn_hover');
        		});
        		$('.btn_ej li').click(function(){
        			$(this).siblings('li').removeClass('active');
        			$(this).addClass('active');
        			
        		});
         	    
        		//获取自定义区域的数据
        		getTools();
        	});
          
          
            
        </script>
    </head>
    <body onload="init()">
        <div id="toolbar">
            <input type="button" class="btn" value="矩形" onclick="drawGeometry5()" />
            <input type="button" class="btn" value="圆" onclick="drawGeometry1()" />
            <input type="button" class="btn" value="多边形" onclick="drawGeometry2()" />
            <input type="button" class="btn" value="清除" onclick="clearStatus()" />
<!--             <input type="button" class="btn" value="工具" id="toolsId" onclick="getTools()" /> -->
            <input type="button" class="btn" value="标记" onclick="drawGeometry3()" />
            <div class="btn btn_ej">
        		自定义区域
        		<ul id="kkkkkk">

        		</ul>
        	</div>
        </div>
       	<div id="toolbar_r">
            <input type="button" class="btn btn_zg btn_active" value="项目总投资" onclick="addMoney()" />
            <input type="button" class="btn btn_zg" value="项目个数" onclick="addTotals()" />
       	</div>
        <div class="tab_switch" style="padding-top: 0; margin-top: 0">
             <!-- <div class="btn" >自定义区域</div> -->
             <div class="switch_content" id="coordinates" style="width:160px;overflow-x:hidden;overflow-y:auto;text-align:left">
            </div>
       </div>
        <div id="map"></div>
    </body>
</html>
