<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	
	
	
	 <script type="text/javascript" src="ext-4.2.1.883/bootstrap.js" charset="utf-8"></script>
	 <link rel="stylesheet" href="ext-4.2.1.883/resources/diyskin/resources/a/ext-theme-a-all.css" type="text/css"></link> 
     <script type="text/javascript" src="ext-4.2.1.883/ext-lang-zh_CN.js" charset="utf-8"></script>
	 
	 <link rel="stylesheet" href="ext-4.2.1.883/examples/shared/example.css" type="text/css"></link>
	 <script type="text/javascript" src="ext-4.2.1.883/examples/shared/examples.js" charset="utf-8"></script>
		
	
	 <jsp:include page="global.jsp"></jsp:include>
	 
	 <script>


		var cookieArr = window.document.cookie.split(";");    
             var css = "a";   //更改默认主题   其他主题兼容有问题
             for(var i=0;i<cookieArr.length;i++) { 
             var arr = cookieArr[i].split("="); 
             if(arr[0].trim()=="css") { 
                   css = arr[1]; 
                  } 
                   }; 
             var helpdocs="个人门户";
             window.document.getElementsByTagName("link")[0].href="ext-4.2.1.883/resources/diyskin/resources/"+css+"/ext-theme-"+css+"-all.css";  
	</script>
	
	 <link rel="stylesheet" href="include/coverext4.css" type="text/css"></link>
	 	
	<script>
		
		//查看此页面全部数据 ,临时解决
		var alluser='${loginUser.name}';
		if('${loginUser.name}'=="姬晓勇"||'${loginUser.name}'=="王宏丽"||'${loginUser.name}'=="admin"||'${loginUser.name}'=="高婧婧"||'${loginUser.name}'=="聂红彩"||'${loginUser.name}'=='郭思雨'){
			alluser=null;
		}
	</script>