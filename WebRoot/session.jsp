<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML >
<html>
  <head>
    <base href="<%=basePath%>">
    
   	<script language="JavaScript">
   	//alert("4");
   	//alert("未登录或登陆过期 ,请重新登录");
    top.location.href = '<%=basePath%>';

</script> 
  </head>

</html>
