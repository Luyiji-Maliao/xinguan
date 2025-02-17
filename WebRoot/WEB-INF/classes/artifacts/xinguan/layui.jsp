<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>开始使用layui</title>
  <link rel="stylesheet" href="layui/css/layui.css">
  <script charset="utf-8" src="layui/layui.js"></script>
		<!-- 引入 echarts.js -->
		<script type="text/javascript" src="js/echarts.min.js"></script>
		<!-- 引入jquery.js -->
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/jquery.form.js"></script>
  <style>
  	#LAY_layedit_1{
  		overflow:auto;
  	}
  	img{
  		width:100%;
  	}
  </style>
 
</head>
<body>
 
 <div id="south">
			<form name='screenshotForm'  id='screenshotForm'  method="post">
			
			<span style="display: block; width: auto; text-align: center">
			<textarea id="mobileRandom" name="mobileRandom" style="display: none;"></textarea>
				<button type="button" id='pss'
					style="border-radius: 3px; padding: 3px 3px 3px 3px; border-width: 1px; border-style: solid; color: white; width: 70px; display: inline-block; border-color: #126daf; background-image: -webkit-linear-gradient(top, #4b9cd7, #3892d3 50%, #358ac8 51%, #3892d3); font-size: 12px; background-color: #3892d3; position: relative; zoom: 1; cursor: pointer; white-space: nowrap; vertical-align: middle; text-decoration: none;" >
					通过</button>
				<button type="button" id='npss'
					style="border-radius: 3px; padding: 3px 3px 3px 3px; border-width: 1px; border-style: solid; color: white; width: 70px; display: inline-block; border-color: #126daf; background-image: -webkit-linear-gradient(top, #4b9cd7, #3892d3 50%, #358ac8 51%, #3892d3); font-size: 12px; background-color: #3892d3; position: relative; zoom: 1; cursor: pointer; white-space: nowrap; vertical-align: middle; text-decoration: none;">
					不通过</button> 
				<input id="tumourReportReviewId_h" name="tumourReportReviewId_h" type="hidden" /> 
				<input id="sampleNo_h" name="sampleNo_h" type="hidden" /> 
				<input id="tumourName_h" name="tumourName_h" type="hidden" /> 
				<input id="tumProduct_h" name="tumProduct_h" type="hidden" /> 
				<input id="tumPackage_h" name="tumPackage_h" type="hidden" /> 
				<input id="reportState_h" name="reportState_h" type="hidden" /> 
				<input id="custReportReviewName_h" name="custReportReviewName_h" type="hidden" /> 
			</span>
		</div>
<script>
var ue;
var cc;
var layedit;
layui.use('layedit', function(){
  layedit = layui.layedit;
//  layedit.build('demo'); //建立编辑器
  layedit.set({
  uploadImage: {
    url: 'reportreviewphp!upload.action', //接口url
    type: 'post' //默认post
  }
});
//console.log(cc);
cc=layedit.build('mobileRandom', {
  tool: [
  'face' //表情
  ,'image' //插入图片
  ]
}); 
ue=layedit.getContent();//编辑器内容
layedit.sync(cc);

//console.log(layedit.getContent(cc));
});
//$("#mobileRandom").val(cc);
//$("#mobileRandom").val(ue);//编辑器内容
$("#tumourReportReviewId_h").val("954");//window.parent.id
$("#sampleNo_h").val("1212");//window.parent.idsampleNo
$("#tumourName_h").val("1213");//window.parent.tumourname
$("#tumProduct_h").val("1214");//window.parent.tumProduct
$("#tumPackage_h").val("1215");//window.parent.tumPackage
$("#custReportReviewName_h").val("1216");//window.parent.custReportReviewName
$("#pss").click(function (){
	alert(layedit.getContent(cc));
	document.getElementById("mobileRandom").value = layedit.getContent(cc);
	document.getElementById("reportState_h").value = "客服已审核";
    $("#screenshotForm").ajaxSubmit({
    	 url: "heredityinterface!custReportReview.action",
         type: "post",
         success: function (data) {
	    	$(window).attr('location','http://www.scisoonbbs.com/limsceshi/bggl.php/Tumourreportreview/kf_examine');
	    }
    });
    return false;
});
$("#npss").click(function () {
	document.getElementById("reportState_h").value = "不通过";
    $("#screenshotForm").ajaxSubmit(function (data) {
    	alert(data.str);
    	$(window).attr('location','http://www.scisoonbbs.com/limsceshi/bggl.php/Tumourreportreview/kf_examine');
    });
});
</script>

</body>
</html>