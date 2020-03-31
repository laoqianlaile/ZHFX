/**
 *  扩展工具类
 *   设置全局的命名空间
 *   package // 得到包
 *   package.table //得到包中对应的表
 *   package.table.field // 得到包中对应的数据
 */
var Utils=window.top.Utils || {};
Utils={
		isValue:false,// 是否初始化数据    默认表示没有初始化
		 UtilDatas:{
			 packageIds:{}, //包的ID集合
			 packageTableIds:{},// 包中的表对应的集合
			 packageTableFieldId:{} // 字段对应的ID			 
		 },// 存放缓存文件	
		 getRootPath:function(){ // 得到当前工程路径
		    //获取当前网址，如： http://localhost:8088/test/test.jsp
		    var curPath=window.document.location.href;
		    //获取主机地址之后的目录，如： test/test.jsp
		    var pathName=window.document.location.pathname;
		    var pos=curPath.indexOf(pathName);
		    //获取主机地址，如： http://localhost:8088
		    var localhostPaht=curPath.substring(0,pos);
		    //获取带"/"的项目名，如：/test
		    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
		    return(localhostPaht+projectName);
		},	
		initData:function(){ // 初始化文件
			// 得到所有包和所有的请求
			var pkgsurl=this.getRootPath()+"?op=fr_bi_configure&cmd=get_business_package_group";
			//  请求每个包中的数据
			var tableurl=this.getRootPath()+"?op=fr_bi_configure&cmd=get_tables_of_one_package";
			// 请求所有的包中数据 
			$.ajax({
                cache: true,
                type: "POST",
                url:pkgsurl,              
                async: false,
                dataType:"json",
                error: function(request) {
                    alert("Connection error");
                },
                success: function(data) {
                	// 请求到了数据
                   if(data){
                	   // 得到分组
                	   for(var pkg in data.packages){
                		   // 包名-属性对应起来
                		  this.UtilDatas.packageIds[data.packages[pkg].name]=pkg;
                		   // 请求包中的数据
                	   }
                   }
                }
            });
			//每个包中的表
			for(var pkgids in this.UtilDatas.packageIds){  
				// 包的Id
				var packageid=this.UtilDatas.packageIds[pkgids];
     		   // 请求包中的数据
				$.ajax({
	                cache: true,
	                type: "POST",
	                url:tableurl,// 请求表中的数据    
	                data:{id:packageid},// package的Id
	                async: false,
	                dataType:"json",
	                error: function(request) {
	                    alert("Connection error");
	                },
	                success: function(data) {
	                	// 请求到了数据
	                   if(data){
	                	   // 得到分组
	                	   for(var tid in data.table_data){
	                		   // 表名-属性对应起来  packgeId@表名 得打对应的ID;
	                		  this.UtilDatas.packageTableIds[packageid+"@"+data.table_data[tid].table_name]=tid;
	                		   // 遍历包中字段
	                		 // for()
	                		  
	                	   }
	                   }
	                }
	            });
     	   }
		 }
}