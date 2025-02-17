<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>文档</title>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script> 

<script>
$.ajax({
		         type:"post",//请求方式
		         url    :"login!online.action",
		         dataType: "json",
		         data:{sampleTrackId:${sampleTrackId}},
		         success : function(result){
		            alert(result);

		        }
		    });
</script>
</head>
<body>

</body>
</html>