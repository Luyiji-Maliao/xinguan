<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="分析结果">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
<jsp:include page="/include/header4.jsp"></jsp:include>	

		<style type="text/css">
			.add {  
				background-image: url(img/add.gif) !important;  
			}  
			.up {  
			    background-image: url(img/up.gif) !important;  
			}  
			.delete {  
			    background-image: url(img/delete.gif) !important;  
			}  
			.delete2 {  
			    background-image: url(img/delete2.gif) !important;  
			}  
			
	</style>
<script type="text/javascript" src="./jspjs/autojsp3.js"></script>

<script>
Ext.onReady(function(){
	var alldatas = {
		'fields': ${fields},
		'title': "${title}",
		'actionname': "${actionname}",		
		'tbars': [{
				"text": "查看",
				"icon": "img/up.gif"			
			},
			{
				"text": "添加",
				"icon": "img/up.gif",				
			},
	
			{
				"text": "修改",
				"icon": "img/newsave.png",				
			},
			{
				"text": "导出EXCEL",
				"icon": "img/up.gif",				
			},
			{
				"text": "高级搜索",
				"icon": "img/simplesearch.png"			//自己定义方法"handler" : function (){deleteAll()}。在下面定义deleteAll()
			}
		]
	};
	createJsp(alldatas);	

	
	
});
</script>

  </head>
  
  <body>
  </body>
</html>
