<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312"> 
<title>JS网页图片查看器－可控制图片放大缩小还原移动效果</title> 
<META HTTP-EQUIV="imagetoolbar" CONTENT="no"> 
<style type="text/css"> 
body { font-family: "Verdana", "Arial", "Helvetica", "sans-serif"; font-size: 12px; line-height: 180%; } 
td { font-size: 12px; line-height: 150%; } 
</style> 
<SCRIPT language=JavaScript> 
drag = 0 
move = 0 
//计数器
var bitemp = 0;
// 拖拽对象 
// 参见：http://blog.sina.com.cn/u/4702ecbe010007pe 
var ie=document.all; 
var nn6=document.getElementById&&!document.all; 
var isdrag=false; 
var y,x; 
var oDragObj; 

function moveMouse(e) { 
if (isdrag) { 
oDragObj.style.top = (nn6 ? nTY + e.clientY - y : nTY + event.clientY - y)+"px"; 
oDragObj.style.left = (nn6 ? nTX + e.clientX - x : nTX + event.clientX - x)+"px"; 
return false; 
} 
} 

function initDrag(e) { 
var oDragHandle = nn6 ? e.target : event.srcElement; 
var topElement = "HTML"; 
while (oDragHandle.tagName != topElement && oDragHandle.className != "dragAble") { 
oDragHandle = nn6 ? oDragHandle.parentNode : oDragHandle.parentElement; 
} 
if (oDragHandle.className=="dragAble") { 
isdrag = true; 
oDragObj = oDragHandle; 
nTY = parseInt(oDragObj.style.top+0); 
y = nn6 ? e.clientY : event.clientY; 
nTX = parseInt(oDragObj.style.left+0); 
x = nn6 ? e.clientX : event.clientX; 
document.onmousemove=moveMouse; 
return false; 
} 
} 
document.onmousedown=initDrag; 
document.onmouseup=new Function("isdrag=false"); 

function clickMove(s){ 
if(s=="up"){ 
dragObj.style.top = parseInt(dragObj.style.top) + 100; 
}else if(s=="down"){ 
dragObj.style.top = parseInt(dragObj.style.top) - 100; 
}else if(s=="left"){ 
dragObj.style.left = parseInt(dragObj.style.left) + 100; 
}else if(s=="right"){ 
dragObj.style.left = parseInt(dragObj.style.left) - 100; 
} 

} 

function smallit(){ 
if(bitemp>0){
var height1=images1.height; 
var width1=images1.width; 
images1.height=height1/1.2; 
images1.width=width1/1.2; 
bitemp--;
}
} 

function bigit(){ 
if(bitemp<10){
var height1=images1.height; 
var width1=images1.width; 
images1.height=height1*1.2; 
images1.width=width1*1.2; 
bitemp++;
}
} 
function realsize() 
{ 
images1.height=images2.height; 
images1.width=images2.width; 
images1.style.filter="progid:DXImageTransform.Microsoft.BasicImage( Rotation='0')";
block1.style.left = 0; 
block1.style.top = 0; 
bitemp = 0;
} 
function featsize() 
{ 
var width1=images2.width; 
var height1=images2.height; 
var width2=360; 
var height2=200; 
var h=height1/height2; 
var w=width1/width2; 
if(height1<height2&&width1<width2) 
{ 
images1.height=height1; 
images1.width=width1; 
} 
else 
{ 
if(h>w) 
{ 
images1.height=height2; 
images1.width=width1*height2/height1; 
} 
else 
{ 
images1.width=width2; 
images1.height=height1*width2/width1; 
} 
} 
block1.style.left = 0; 
block1.style.top = 0; 
} 

</SCRIPT> 
<script language="JavaScript" type="text/JavaScript"> 

function MM_reloadPage(init) { //reloads the window if Nav4 resized 
if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) { 
document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }} 
else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload(); 
} 
MM_reloadPage(true); 

var i=1;
function playImg()
{
	images1.style.filter="progid:DXImageTransform.Microsoft.BasicImage( Rotation="+i+")";
	i++;
	if(i>4)
	{i=1};
}


</script> 
<style type="text/css"> 
<!-- 
td, a { font-size:12px; color:#000000 } 
#Layer1 { position:absolute; z-index:100; top: 75px; } 
#Layer2 { position:absolute; z-index:1;} 
--> 
</style> 
</head> 
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" oncontextmenu="return false" ondragstart="return false" onselectstart ="return false" onselect="document.selection.empty()" oncopy="document.selection.empty()" onbeforecopy="return false" onmouseup="document.selection.empty()" style="overflow-y:hidden;overflow-x:hidden;"> 

<div id="Layer1"><table border="0" cellspacing="2" cellpadding="0"><tr><td></td><td><img src="img/tpck/up.gif" width="20" height="20" style="cursor:hand" onClick="clickMove('up')" title="向上"></td> <td></td> </tr> <tr> <td><img src="img/tpck/left.gif" width="20" height="20" style="cursor:hand" onClick="clickMove('left')" title="向左"></td> <td><img src="img/tpck/zoom.gif" width="20" height="20" style="cursor:hand" onClick="realsize();" title="还原"></td> <td><img src="img/tpck/right.gif" width="20" height="20" style="cursor:hand" onClick="clickMove('right')" title="向右"></td> </tr> <tr> <td> </td> <td><img src="img/tpck/down.gif" width="20" height="20" style="cursor:hand" onClick="clickMove('down')" title="向下"></td> <td> </td> </tr> <tr> <td> </td> <td><img src="img/tpck/zoom_in.gif" width="20" height="20" style="cursor:hand" onClick="bigit();" title="放大"></td> <td> </td> </tr> <tr> <td> </td> <td><img src="img/tpck/zoom_out.gif" width="20" height="20" style="cursor:hand" onClick="smallit();" title="缩小"></td> <td> </td> </tr> <tr> <td> </td> <td><img src="img/tpck/turn.gif" width="20" height="20" style="cursor:hand" onClick="playImg();" title="旋转"></td> <td> </td> </tr> </table> </div> <div algin="center" id='hiddenPic' style='position:absolute; left:0px; top:0px; width:0px; height:0px; z-index:1; visibility: hidden;'><img name='images2' height="560" src='img/tpck/test.jpg' border='0'></div> <div algin="center" id='block1' onmouseout='drag=0' onmouseover='dragObj=block1; drag=1;' style='z-index:10; height: 0; left: 0px; position: absolute; top: 0px; width: 0' class="dragAble"> <img name='images1' height="560" src='img/tpck/test.jpg' border='0'></div> 
</body> 
</html>
