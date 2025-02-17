<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<!DOCTYPE html>
<html>
	<head>
		<title>北京优迅医学检验所有限公司信息管理系统</title>
		
			<script type="text/javascript">
				window.onload=function(e){
					if('${browsertype}'!=null&&'${browsertype}'=="1"){//火狐 
						if('${tiaozhuaninit}'!=null&&'${tiaozhuaninit}'=="1"){
							   window.location.href='user!remain.action?tiaozhuaninit=0';
							}
						}
				
				}
				var tiaozhuan=0;
				window.onunload=function(e)
				{
					if('${browsertype}'!=null&&'${browsertype}'=="1"){//火狐 
					var  event = e || window.event;
					if(event.clientX>document.body.clientWidth && event.clientY < 0 || event.altKey){
				     //alert("你关闭了浏览器");
						window.location.href='login!outlogin.action';
					}else{
						if(tiaozhuan==0){
						  window.location.href='user!remain.action';
						}
					     //alert("你正在刷新页面");
					   
					}
					}else{
					
						window.location.href='login!outlogin.action';
					}
				}
				
				function outlogin(){
					window.location.href='login!outlogin.action';
					tiaozhuan=1;
				}
</script>
		<jsp:include page="/include/header4.jsp"></jsp:include>
		<link rel="icon" href="${ctx}/static/images/favicon1.ico">
    	<link rel="shortcut icon" href="${ctx}/static/images/favicon1.ico">
		<script type="text/javascript" src="js/content/systemmanage/main1.js" charset="utf-8"></script>

		<style type="text/css">
	#loading-mask{
		position:absolute;
		left:0;
		top:0;
		width:100%;
		height:100%;
		z-index:9200000;
		background-color:white;
	}
	#loading{
		position:absolute;
		left:45%;
		top:40%;
		padding:2px;
		z-index:9200001;
		height:auto;
	}
	#loading img{
	margin-botton:5px;
	}
	
	#loading .loading-indicator{
		background:white;
		color:#555;
		font-size:13px;
		padding:10px;
		margin:0px;
		text-align:center;
		height:auto
	}
	.home{
		background-image: url(img/gif-0596.gif) !important;
	}
	.titleIcon{
		background-image: url(img/folder_wrench.png) !important;
	}
	.menuIcon{
		background-image: url(img/textfile.png) !important;
	}
	
	table.bgnavbar {
    background-color: #305f90;
    background-image: url("img/bg_navbar.gif");
    background-repeat: no-repeat;
    /*color: #c9e3f2;*/
    font-family: Arial,Helvetica,sans-serif;
    font-size: 11px;
    font-style: normal;
    font-weight: bold;
    vertical-align: top;
}


.txtappname {
   /* color: #ffffff;*/
    font-family: Arial,Helvetica,sans-serif;
    font-size: 12px;
    font-style: normal;
    font-weight: bold;
    position: relative;
}
.navbar {
    /*color: #ffffff;*/
    line-height: 27px;
    padding-bottom: 0;
    padding-top: 0;
    vertical-align: top;
}
.powerwhite {
    /*color: #ffffff;*/
    display: inline;
    font-size: 12px;
    font-weight: normal;
    padding-left: 10px;
    padding-right: 10px;
    text-decoration: none;
    vertical-align: top;
}
.hyperlink {
    cursor: pointer;
    font-size: 8pt;
}
.text {
    font-family: Arial,Helvetica,sans-serif;
    font-size: 12px;
}
</style>

	</head>
	<body >
	<input type="hidden" id="wcollapseid" name="wcollapse" value="true">
<input type="hidden" id="alwaysopen" name="alwaysopen" value="false">
	
	<div id="north" class="x-hide-display" >

<table id="bgn" width="100%" border="0" cellpadding="0" cellspacing="0"
	class="bgnavbar1" >
	<tr>
		<td width="0" rowspan="2" nowrap><!-- <img id="appimage" border="0"
			src="img/appimg.gif" align="left" hspace="0" title="BIO-LIMS"> -->
		</td>
		<td nowrap><span class="txtappname">&nbsp; 北京优迅医学检验所有限公司信息管理系统 </span></td>
		<td align="right">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				
			<td class="navbar" nowrap valign="top"><span title='' 	 class='text hyperlink powerwhite'>欢迎你，${loginUser.name }</span></td>
			<td class="navbar" nowrap valign="top"><span title='锁定导航栏' id='showbtn'  class='text hyperlink powerwhite' onclick='javascript:openwest();setalwaysopen();' ><img border='0' src='img/btn_goto.gif'>导航</span></td>
			<!-- <td class="navbar" nowrap valign="top"><span id='sslink' class='text hyperlink powerwhite'  title='个人门户'	onclick="personalportal()"><img border='0' title='Start Center' src='${ctx}/img/btn_startcenter.gif'>个人门户</span></td> -->
			<td class="navbar" nowrap valign="top"><span id='sslink' class='text hyperlink powerwhite'  title='主题'	onclick="bgtheme()"><img border='0' title='Start Center' src='${ctx}/img/btn_startcenter.gif'>主题</span></td>
			<td class="navbar" valign="top" nowrap><span id='profilelink' class='text hyperlink powerwhite' title='个人设置' onclick='hyperlink_onclicks();' ><img border='0' src='${ctx}/img/btn_profile.gif' >个人设置</span></td>
			<td class="navbar" valign="top" nowrap><span id='profilelink' class='text hyperlink powerwhite' title='更新日志' onclick='operateWin();' ><img border='0' src='${ctx}/img/btn_profile.gif' >更新日志</span></td>
			<td class="navbar" valign="top" nowrap><span id='profilelink' class='text hyperlink powerwhite' title='权限' onclick='pageRight();' ><img border='0' src='${ctx}/img/btn_profile.gif' >权限</span></td>
			<td class="navbar" valign="top" nowrap><span id='profilelink' class='text hyperlink powerwhite' title='权限申请单' onclick='pageRightImg();' ><img border='0' src='${ctx}/img/btn_profile.gif' >权限图</span></td>
			<td class="navbar" nowrap valign="top"><span id='signoutlink' class='text hyperlink powerwhite' title='退出' onclick="outlogin()"><img border='0' src='${ctx}/img/btn_signout.gif' >退出</span></td>
			<!-- <td class="navbar" nowrap valign="top"><span id='helplink' class='text hyperlink powerwhite' title='帮助' onclick="javascript:helpdoc();"><img border='0'   src='${ctx}/img/btn_help.gif' >帮助</span></td> -->
			<a href="http://localhost:8080/uscilims/documents/daibanwendang.docx"></a>
			<a href="http://localhost:8080/uscilims/documents/xiaoxizhongxin.docx"></a>
			</tr>
		</table>
		</td>
	</tr>
	
</table>
</div>
	
	
	</body>
</html>
