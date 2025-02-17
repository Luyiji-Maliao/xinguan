<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="S" uri="/struts-tags" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<c:set value="${pageContext.request.contextPath}" var="path" scope="page"/>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>北京优迅医学核酸检测信息采集系统</title>
<%--	<jsp:include page="/include/header4.jsp"></jsp:include>--%>
	<script type="text/javascript" src="./js/jquery-1.7.2.min.js"></script>
<style>
	*{
		font-size: 18px;
	}
</style>
</head>
<body bgcolor="#D2F0FF">
	<h1 style="text-align:center;color:#5555FF;font-size: 38px;margin:30px 0px 30px 0px">北京优迅医学核酸检测信息采集系统</h1>	<br/>
	<input type="hidden" id="hid" value="${inputmsg}">
	<input type="hidden" id="hidtext" value="${ZengJiantext}">
	<input type="hidden" id="isSamplenum" value="${isSamplenum}">
	<input type="button" id="jiansuo" name="jiansuo" value="检索" style="position:absolute;top:360px ;left: 570px">
	<form action="xgcollectinfo!modulepage.action" method="POST" id="subform">
		<table border="0" cellpadding="0" cellspacing="10" id="alltable" style="float:left;width:50%;">
			<tr>
				<td colspan="2">
					<button type="button" onclick="connect()">连接</button>
					<button type="button" onclick="readCert()">读卡</button>
					<button type="button" onclick="return submitform()">提交</button>
<%--					<button type="button" onclick="location.reload()">清空</button>--%>
					<button type="button" id="reset">清空全部</button>
					<button type="button" onclick="changecodemethod('减码')">减码</button>
					<button type="button" onclick="changecodemethod('增码')">增码</button>
<%--					<button type="button" onclick="noidcard()">无身份证</button>--%>
					<button type="button" class="stop" onclick="red()">手动读卡</button>
					<button type="button" onclick="addMode()">添加样本</button>
					<span id="fiveoneCount" style="display: none">
						数量：
						<span style="color: red">0</span>
					</span>
				</td>
			</tr>
			<tr>
				<td align="right">合管编码：</td>
				<td>
					<input type="text" onblur="fiveoneblur()" id="fiveone" name="fiveone" size="49" style="width:400px;line-height:20px" value="${ZengNum}">
				</td>
			</tr>
			<tr>
				<td align="right">编码方式：</td>
				<td><input type="text" id="codemethod" name="codemethod" size="49" style="width:400px;line-height:20px" readonly></td>
			</tr>
			<tr id="clonelast">
				<td align="right">采样地点：</td>
				<td>
					<input type="text" id="inputAddress" name="inputAddress" size="100" style="width:400px;line-height:20px" value="${inputAddress}" >
					<input type="hidden" id="isCF" name="isCF" value="false">
				</td>
			</tr>
				<tr style="height: 25px">

				</tr>
				<tr>
					<td align="right">样本编号：</td>
					<td>
						<input type="text" id="samplenum0" name="samplenum" size="49" style="width:400px;line-height:20px" value="">
					</td>
				</tr>
				<tr>
					<td align="right">姓名：</td>
					<td><input type="text" id="partyName0" name="partyName" size="49" style="width:400px;;line-height:20px"></td>
				</tr>
				<tr>
					<td align="right">证件号码：</td>
					<td><input oninput='cardnumChange(this)' type="text" id="certNumber0" name="certNumber" size="49" style="color:#FF0000;width:400px;line-height:20px"></td>
				</tr>
				<tr>
					<td align="right">联系电话：</td>
					<td><input type="text" id="phone0" name="phone" size="49" style="color:#FF0000;width:400px;;line-height:20px"></td>
				</tr>
				<tr style="display:none;">
					<td align="right">性别：</td>
					<td><input type="text" id="gender0" name="gender" size="49" style="width:400px;line-height:20px" readonly="readonly"></td>
				</tr>
				<tr style="display: none">
					<td align="right">证件类型：</td>
					<td><input type="text" id="certType0" name="certType" size="49" style="color:#FF0000;width:400px;"></td>
				</tr>
				<tr style="display:none;">
					<td align="right">民族/国籍：</td>
					<td><input type="text" id="nation0" name="nation" size="49" style="width:400px;" readonly="readonly"></td>
				</tr>
				<tr style="display:none;">
					<td align="right">出生日期：</td>
					<td><input type="text" id="bornDay0" name="bornDay" size="49" style="width:400px;" readonly="readonly"></td>
				</tr>
				<tr style="display:none;">
					<td align="right">住址：</td>
					<td><input type="text" id="certAddress0" name="certAddress" size="49" style="width:400px;" readonly="readonly"></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center;">
						<img id="PhotoStr0" name="PhotoStr" src="" alt="Base64 image" />
						<input type="button" onclick="delthis(this)" id="delone0" name="delone" value="删除">
<%--						<input type="button" onclick="clearthis(this)" id="clearone0" name="clearone" value="清空">--%>
					</td>
				</tr>

			<tr style="display:none;">
				<td align="right">结果：</td>
				<td><textarea id="result" name="result" rows="8" cols="47" style="width:400px;" readonly="readonly"></textarea></td>
			</tr>
			<tr style="display: none">
				<td align="right">读卡时间：</td>
				<td><input type="text" id="timeElapsed" name="timeElapsed" size="49" style="width:400px;line-height:20px" readonly="readonly"></td>
			</tr>
			<tr>
				<td></td>
				<td>
					<div>
						当前读卡方式：
						<span id="dkspan">自动读卡</span>
					</div>
				</td>
			</tr>
		</table>
	</form>
	<table style="float:left;width:50%;">
		<tr style="height:40px;">
			<td colspan="4" style="font-size:20px;line-height:40px;color:red;">
				今日已采集：
				<span id="NowCount"><s:property value="#request.NowCount" /> </span>
			</td></tr>
		<tr><td>样本编号</td><td>姓名</td><td>身份证号</td><td>联系电话</td></tr>
		<s:iterator value="#request.xlist">
			<tr class="ten" >
				<td class="ids" style="display: none">
					<S:property value="id"/>
				</td>
				<td class="samplenums" >
					<S:property value="samplenum"/>
				</td>
				<td style="white-space:nowrap;
                text-overflow:ellipsis;
                overflow:hidden;
				width: 100px;
				display: inline-block;
				cursor:pointer;">
					<span class="mhover">
						<S:property value="partyName"/>
					</span>
					<span class="mall" style="border: 1px solid black;padding: 3px;background-color: white;font-size: 14px">
						<S:property value="partyName"/>
					</span>
				</td>
				<td>
					<S:property value="certNumber"/>
				</td>
				<td>
					<S:property value="phone"/>
				</td>
				<td>
					<a href="###" class="del">删除</a>
				</td>
			</tr>
		</s:iterator>
	</table>
	<object id="CertCtl" type="application/cert-reader" width="0" height="0">
		<p style="color:#FF0000;">控件不可用，可能未正确安装控件及驱动，或者控件未启用。</p>
	</object>
	<table border="0" cellpadding="0" cellspacing="0" style="margin:0 auto;width:980px;display:none;">
		<tr>
			<td><input type="button" value="连接仪器" onclick="connect()"></td>
			<td><input type="button" value="状态" onclick="getStatus()"></td>
			<td><input type="button" value="版本" onclick="getVerSion()"></td>
			<td><input type="button" value="SamID" onclick="getSamId()"></td>
			<td><input type="button" value="读卡" onclick="readCert()"></td>
			<td><input type="button" value="断开" onclick="disconnect()"></td>
		</tr>
	</table>


	<script>
		$(function () {
			$(".mall").hide();
			//当鼠标移入元素事件
			$(".mhover").mouseover(function (e) {
				$(this).next(".mall").css({"position":"absolute","top":e.pageY+15,"left":e.pageX+15}).show();
			});
			//鼠标在元素上移动时事件
			$(".mhover").mousemove(function (e) {
				$(this).next(".mall").css({ "color": "black", "position": "absolute", "top": e.pageY + 15, "left": e.pageX + 15 });
			});
			$(".mhover").mouseout(function () {
				$(this).next(".mall").hide();
			});
			//键盘事件
			document.onkeydown = function(e) {
				//Shift+Enter提交表单
				if (13 == e.keyCode && e.shiftKey)
				{
					submitform();
				}else if(13 == e.keyCode){ //Enter添加一个样本
					addMode();
				}
			};
			var nocard = 0;
			//永远只显示10个th
			$(".ten").each(function (i,n) {
				if(i<=9){
					$(this).show();
				}
				if(i>=10){
					$(this).hide();
				}
			});

			//删除事件
			$(".del").click(function () {
				var ids = $(this).parent().parent().find(".ids").text();
				var a = $(this).parent().parent();
				if(confirm('确定删除吗 ？')){
					$.post("xgcollectinfo!updIsShow.action",{"ids":ids},function (response) {
						a.remove();
						var abc = $("#NowCount").html();
						if(abc != 0){
							abc = abc-1 ;
							$("#NowCount").html(abc);
						}
						$(".ten").each(function (i,n) {
							if(i<=9){
								$(this).show();
							}
							if(i>=10){
								$(this).hide();
							}
						});
					})
				}

			});

			//清空全部
			$("#reset").click(function () {
				// $("#subform")[0].reset();
				var a = $("#samplenum").val();
				$(':input','#subform')
						.not(':button, :submit, :reset, :hidden')
						.val('')
						.removeAttr('checked')
						.removeAttr('selected');
				$("#samplenum").val(a);
				$("[name='PhotoStr']").attr("src","");
				if($("#hidtext").val() == ""){
					$("#codemethod").val("增码")
				}else{
					$("#codemethod").val($("#hidtext").val())
				}
				if($("#dkspan").html() != "手动读卡"){  //当前方式为自动则不开始刷新
					interval = setInterval(function(){autoshibie()},500);
				}
			})

			if($("#isSamplenum").val()!=""){  //判断 不能再减了
				alert($("#isSamplenum").val());
			}
			if($("#hidtext").val() == ""){
				$("#codemethod").val("增码")
			}else{
				$("#codemethod").val($("#hidtext").val())
			}

			//检索事件
			$("#jiansuo").click(function () {
				var name = $("#inputAddress").parent().parent().next().next().next().find("input[name='partyName']").val();
				var cer = $("#inputAddress").parent().parent().next().next().next().next().find("input[name='certNumber']").val();
				var pho = $("#inputAddress").parent().parent().next().next().next().next().next().find("input[name='phone']").val();
				if(name == "" && cer == "" && pho == ""){
				    alert("请填写要检索的信息");
				    return false;
                }
				$.post("xgreserve!jiansuo.action",{"name":name,"certNumber":cer,"phone":pho},function (data) {
					var c = 0;
					$(data).each(function () {
						c++;
					})
					if(c > 5){
                        $("input[name='checkinfo']").parent().parent().parent().remove();
						alert("相关结果数大于5条，请再详细一点");
					}else if(c == 0){
                        $("input[name='checkinfo']").parent().parent().parent().remove();
					    alert("未检索到相关信息");
                    }else {
						$("input[name='checkinfo']").parent().parent().parent().remove();
						$.each(data, function (index, val) {
							var checktr = "<tr>\n" +
									"\t\t\t\t<td colspan=\"2\">\n" +
									"\t\t\t\t\t<div>\n" +
									"\t\t\t\t\t\t<input type=\"checkbox\" onclick=\"checkboxclick(this)\" name=\"checkinfo\" style=\"margin-left: 60px\">\n" +
									"\t\t\t\t\t\t<span name=\"checkname\" id=\"checkname\">"+val[1]+"</span>\n" +
									"\t\t\t\t\t\t<span name=\"checkcert\" id=\"checkcert\">"+val[2]+"</span>\n" +
									"\t\t\t\t\t\t<span name=\"checkphone\" id=\"checkphone\">"+val[3]+"</span>\n" +
									"\t\t\t\t\t</div>\n" +
									"\t\t\t\t</td>\n" +
									"\t\t\t</tr>";
							$("#inputAddress").parent().parent().next().next().next().next().next().after(checktr);
						})
					}
				})
			});


		});

		//单选框点击事件
		function checkboxclick(i) {
			if($(i).attr("checked") == "checked"){
				var name = $(i).next().html();
				var cer = $(i).next().next().html();
				var pho = $(i).next().next().next().html();
				$("#inputAddress").parent().parent().next().next().next().find("input[name='partyName']").val(name);
				$("#inputAddress").parent().parent().next().next().next().next().find("input[name='certNumber']").val(cer);
				$("#inputAddress").parent().parent().next().next().next().next().next().find("input[name='phone']").val(pho);
				$("input[name='checkinfo']").attr("checked",false);
				$(i).attr("checked",true);
			}
		}

		//身份证号input事件
		function cardnumChange(i) {
			if($(i).val().length == 18 || $(i).val().length == 8 || $(i).val().length == 9 || $(i).val().length == 10){
				var certNumber = $(i).val();
				$.post("xgcollectinfo!getPhoneByCardnum.action",{"certNumber":certNumber},function (jsonArray) {
					if(jsonArray != ""){
						$(i).parent().parent().next().find("input[name='phone']").val(jsonArray);
					}else{
						$(i).parent().parent().next().find("input[name='phone']").val("");
					}
				})
			}else{
				$(i).parent().parent().next().find("input[name='phone']").val("");
			}
		}
		var interval = setInterval(function(){
			autoshibie();
			connect();
		},500);

		//提交之后根据合管编号拼接样本编号
		fiveoneblur();

		//合管编号blur事件
		function fiveoneblur() {
			var fiveonenum = $("#fiveone").val();
			if(fiveonenum != ""){
				if(/[a-z]/i.test(fiveonenum)){  //包含字母
					var num = fiveonenum.substr(fiveonenum.match(/[a-z]/ig).length);
					//合管编号是否都为0
					if(/[1-9]/.test(num)){
						var a = $("#fiveone").val()+"-1";
						$("#inputAddress").parent().parent().next().next().find("input[name='samplenum']").val(a);
					}else{
						$("#inputAddress").parent().parent().next().next().find("input[name='samplenum']").val("");
						alert("合管编号不正确！");
						$("#fiveone").val("");
					}
				}else{
					if(/[1-9]/.test(fiveonenum)){
						var a = $("#fiveone").val()+"-1";
						$("#inputAddress").parent().parent().next().next().find("input[name='samplenum']").val(a);
					}else{
						$("#inputAddress").parent().parent().next().next().find("input[name='samplenum']").val("");
						alert("合管编号不正确！");
						$("#fiveone").val("");
					}
				}
			}

		}
		var c = $("#fiveoneCount").find("span").html();
		function delthis(button) {
			var els = document.getElementsByName("samplenum");
			// $("#alltable").find("input[name='partyName']").eq(0).remove();
			// $("#alltable").find("input[name='samplenum']").eq(0).remove();
			// $("#alltable").find("input[name='certNumber']").eq(0).remove();
			// $("#alltable").find("input[name='phone']").eq(0).remove();
			// $("#alltable").find("input[name='gender']").eq(0).remove();
			// $("#alltable").find("input[name='certType']").eq(0).remove();
			// $("#alltable").find("input[name='nation']").eq(0).remove();
			// $("#alltable").find("input[name='bornDay']").eq(0).remove();
			// $("#alltable").find("input[name='certAddress']").eq(0).remove();
			// $("#alltable").find("input[name='certAddress']").eq(0).remove();
			// $("#alltable").find("input[name='certAddress']").eq(0).remove();
			if(els.length>1){
				if($("input[name='checkinfo']").length >0){
					$("input[name='checkinfo']").parent().parent().parent().remove();
				}
				for(var i = 1;i<=10;i++){
					$(button).parent().parent().prev().remove();
				}
				$(button).parent().parent().remove();
				$("#fiveoneCount").find("span").html($("#fiveoneCount").find("span").html()-1);
			}else{
				alert("不能再删除");
			}

		}

		function clearthis(button) {
			$(button).prev().prev().attr("src","")
			$(button).parent().parent().prev().find("input").val("");
			$(button).parent().parent().prev().prev().find("input").val("");
			$(button).parent().parent().prev().prev().prev().find("input").val("");
			$(button).parent().parent().prev().prev().prev().prev().find("input").val("");
			$(button).parent().parent().prev().prev().prev().prev().prev().find("input").val("");
			$(button).parent().parent().prev().prev().prev().prev().prev().prev().find("input").val("");
			$(button).parent().parent().prev().prev().prev().prev().prev().prev().prev().find("input").val("");
			$(button).parent().parent().prev().prev().prev().prev().prev().prev().prev().prev().find("input").val("");
			//清空当前文本框之后开始自动读卡和连接
			interval = setInterval(function(){
				autoshibie();
				connect();
			},500);
		}

		// document.onkeydown=keyListener;
		// function keyListener(e){
		//
		// 	// 当按下回车键，执行我们的代码
		// 	// if(e.keyCode == 13){
		// 	// 	addMode();
		// 	// }
		// }

		function addMode() {
			var sa = "";
			$("input[name='samplenum']").each(function () {
				if(this.value == ""){
					sa += " 请录入样本编号";
					return false;
				}else if(this.value.indexOf("-") == -1){
					sa += " 请录入正确的样本编号";
					return false;
				}
			})
			$("input[name='inputAddress']").each(function () {
				if(this.value == ""){
					sa += " 请录入采样地点";
					return false;
				}
			})
			$("input[name='partyName']").each(function () {
				if(this.value == ""){
					sa += " 请录入姓名";
					return false;
				}
			})
			$("input[name='certNumber']").each(function () {
				if(this.value == ""){
					sa += " 请录入证件号码";
					return false;
				}
				if(this.value.length != 18){
					sa += " 请录入正确的证件号码";
					return false;
				}
			})
			$("input[name='phone']").each(function () {
				if(this.value == ""){
					sa += " 请录入联系电话";
					return false;
				}
			})
			if(sa != ""){
				alert(sa);
				return false;
			}
			var equacertnum = [];
			$("input[name='certNumber']").each(function (i,value1) {
				equacertnum.push(this.value);
			})
			var s = equacertnum.join(",") + ",";
			for(var i = 0; i < equacertnum.length; i++) {
				if(s.replace(equacertnum[i] + ",", "").indexOf(equacertnum[i] + ",") > -1) {
					alert("提示！该合管里身份证号重复！：" + equacertnum[i]);
					return false;
				}
			}
			//获取上一个最新的一个生产的样本信息的id编号
			var d =$("#inputAddress").parent().parent().next().next().find("input[name='samplenum']").attr("id").substring(9);
			d = Number(d)+1;
			$("#fiveoneCount").find("span").html(c);
			var one = $("#inputAddress").parent().parent().next().next().find("input[name='samplenum']").val();
			var trs = "<tr style=\"height: 25px\">\n" +
					"\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td align=\"right\">样本编号：</td>\n" +
					"\t\t\t\t\t<td>\n" +
					"\t\t\t\t\t\t<input type=\"text\" id=\"samplenum"+d+"\" name=\"samplenum\" size=\"49\" style=\"width:400px;line-height:20px\" value=\"${ZengNum}\">\n" +
					"\t\t\t\t\t</td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td align=\"right\">姓名：</td>\n" +
					"\t\t\t\t\t<td><input type=\"text\" id=\"partyName"+d+"\" name=\"partyName\" size=\"49\" style=\"width:400px;;line-height:20px\"></td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td align=\"right\">证件号码：</td>\n" +
					"\t\t\t\t\t<td><input oninput='cardnumChange(this)' type=\"text\" id=\"certNumber"+d+"\" name=\"certNumber\" size=\"49\" style=\"color:#FF0000;width:400px;line-height:20px\"></td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td align=\"right\">联系电话：</td>\n" +
					"\t\t\t\t\t<td><input type=\"text\" id=\"phone"+d+"\" name=\"phone\" size=\"49\" style=\"color:#FF0000;width:400px;;line-height:20px\"></td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr style=\"display:none;\">\n" +
					"\t\t\t\t\t<td align=\"right\">性别：</td>\n" +
					"\t\t\t\t\t<td><input type=\"text\" id=\"gender"+c+"\" name=\"gender\" size=\"49\" style=\"width:400px;line-height:20px\" readonly=\"readonly\"></td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr style=\"display: none\">\n" +
					"\t\t\t\t\t<td align=\"right\">证件类型：</td>\n" +
					"\t\t\t\t\t<td><input type=\"text\" id=\"certType"+c+"\" name=\"certType\" size=\"49\" style=\"color:#FF0000;width:400px;\"></td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr style=\"display:none;\">\n" +
					"\t\t\t\t\t<td align=\"right\">民族/国籍：</td>\n" +
					"\t\t\t\t\t<td><input type=\"text\" id=\"nation"+c+"\" name=\"nation\" size=\"49\" style=\"width:400px;\" readonly=\"readonly\"></td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr style=\"display:none;\">\n" +
					"\t\t\t\t\t<td align=\"right\">出生日期：</td>\n" +
					"\t\t\t\t\t<td><input type=\"text\" id=\"bornDay"+d+"\" name=\"bornDay\" size=\"49\" style=\"width:400px;\" readonly=\"readonly\"></td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr style=\"display:none;\">\n" +
					"\t\t\t\t\t<td align=\"right\">住址：</td>\n" +
					"\t\t\t\t\t<td><input type=\"text\" id=\"certAddress"+d+"\" name=\"certAddress\" size=\"49\" style=\"width:400px;\" readonly=\"readonly\"></td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td colspan=\"2\" style=\"text-align:center;\">\n" +
					"\t\t\t\t\t\t<img id=\"PhotoStr"+d+"\" name=\"PhotoStr\" src=\"\" alt=\"Base64 image\" />\n" +
					"\t\t\t\t\t\t<input type=\"button\" onclick=\"delthis(this)\" id=\"delone0\" name=\"delone\" value=\"删除\">\n" +
					"\t\t\t\t\t</td>\n" +
					"\t\t\t\t</tr>";
			$("#clonelast").after(trs);
			var fiveone = $("#fiveone").val();

			var newnum = one.split("-");//获取-之前的字符串
			var a = Number(newnum[1])+Number(1);
			$("#inputAddress").parent().parent().next().next().find("input[name='samplenum']").val(fiveone+"-"+a);
			//添加样本之后开始自动读卡和连接
			interval = setInterval(function(){
				autoshibie();
				connect();
			},500);
			$("input[name='checkinfo']").parent().parent().parent().remove();
		}
		function red() {
			if($(".stop").text()=="手动读卡"){
				$(".stop").text('自动读卡');
				$("#dkspan").text("手动读卡");
				clearInterval(interval);
				clearForm();
			}else{
				 interval = setInterval(function(){
					autoshibie();
					connect();
				},500);
				$(".stop").text('手动读卡');
				$("#dkspan").text("自动读卡");
				clearForm();
			}
		}
		var t;
		$(document).ready(function(){
			$("#fiveone").focus();
		})

		function autoshibie()
		{
			if($("#partyName").val()||$("#certNumber").val()){
				clearInterval(interval);
			}
			readCert();   //读取身份证信息
			var result = $("#result").val();
			var resultObj = toJson(result);
			if (resultObj && resultObj.resultFlag == 0){
				clearInterval(interval);
			}
		}
		//提交表单
		function submitform(){
			var abc =[];
			var form = $("#subform");
			$("input[name='samplenum']").each(function (){
				abc.push($(this).attr("id").substring(9));
			});
			if(nocard == 0 ){
				var sa = "";
				$("input[name='inputAddress']").each(function () {
					if(this.value == ""){
						sa += " 请录入采样地点";
						return false;
					}
				});
				$("input[name='samplenum']").each(function () {
					if(this.value == ""){
						sa += " 请录入样本编号";
						return false;
					}
				});
				$("input[name='partyName']").each(function () {
					if(this.value == ""){
						sa += " 请录入姓名";
						return false;
					}
				});
				$("input[name='certNumber']").each(function () {
					if(this.value == ""){
						sa += " 请录入证件号码";
						return false;
					}
					if(this.value.length != 18){
						sa += " 请录入正确的证件号码";
						return false;
					}
				});
				$("input[name='phone']").each(function () {
					if(this.value == ""){
						sa += " 请录入手机号";
						return false;
					}
				});
				if(sa != ""){
					alert(sa);
					return false;
				}
				var equacertnum = [];
				$("input[name='certNumber']").each(function (i,value1) {
					equacertnum.push(this.value);
				})

				var s = equacertnum.join(",") + ",";
				for(var i = 0; i < equacertnum.length; i++) {
					if(s.replace(equacertnum[i] + ",", "").indexOf(equacertnum[i] + ",") > -1) {
						alert("提示！该合管里身份证号重复！：" + equacertnum[i]);
						return false;
					}
				}

				var equasamplenum = [];
				$("input[name='samplenum']").each(function (i,value1) {
					equasamplenum.push(this.value);
				})
				var s = equasamplenum.join(",") + ",";
				for(var i = 0; i < equasamplenum.length; i++) {
					if(s.replace(equasamplenum[i] + ",", "").indexOf(equasamplenum[i] + ",") > -1) {
						alert("提示！该合管里样本编号重复！：" + equasamplenum[i]);
						return false;
					}
				}
				var newObj = [];
				var j = 0;
				for(var i of abc) {
					newObj[j] = {
						fiveone: $("#fiveone").val(),
						samplenum: $("#samplenum"+i).val(),
						gender: $("#gender"+i).val(),
						phone: $("#phone"+i).val(),
						certType: $("#certType"+i).val(),
						nation: $("#nation"+i).val(),
						bornDay: $("#bornDay"+i).val(),
						inputAddress: $("#inputAddress").val(),
						partyName: $("#partyName"+i).val(),
						certAddress: $("#certAddress"+i).val(),
						certNumber: $("#certNumber"+i).val() }
					j++;
				}
				var jsonStr = JSON.stringify(newObj);
				var codemethod = $("#codemethod").val()
				$.post("uscilims/xgcollectinfo!checked.action",{"iteminfo":jsonStr,"codemethod":codemethod},function (jsonArray) {
					if(jsonArray == ""){
						$.post("xgcollectinfo!againsave.action",{"iteminfo":jsonStr,"codemethod":codemethod},function (){
							form.submit();
						});
					}else if(jsonArray.indexOf("|") != -1){
						var cf = jsonArray.split("|");
						alert("样本编号重复："+cf[1]);
						return false;
					}else if(jsonArray.indexOf("身份证号") != -1){
						if(confirm(jsonArray+"\n仍然要继续录入吗？")){
							$.post("xgcollectinfo!againsave.action",{"iteminfo":jsonStr,"codemethod":codemethod},function (){
									form.submit();
							});
						}
					}
				})
				return false;
			}

		}
		// var int=self.setInterval("readCert()",1000)

		
		//读取身份证信息；
		function readCert() 
		{
			nocard = 0;
			var CertCtl = document.getElementById("CertCtl");

			try {
				var startDt = new Date();
				var result = CertCtl.readCert();
				var endDt = new Date();


				document.getElementById("timeElapsed").value = (endDt.getTime() - startDt.getTime()) + "毫秒";
				document.getElementById("result").value = result;

				var thsample = $("#inputAddress").parent().parent().next().next();

				var resultObj = toJson(result);
				if (resultObj.resultFlag == 0)
				{
					$(thsample).next().find("input[name='partyName']").val(resultObj.resultContent.partyName);
					$(thsample).next().next().find("input[name='certNumber']").val(resultObj.resultContent.certNumber);
					$(thsample).next().next().next().next().find("input[name='gender']").val(resultObj.resultContent.gender);
					$(thsample).next().next().next().next().next().find("input[name='certType']").val(resultObj.resultContent.certType);
					$(thsample).next().next().next().next().next().next().find("input[name='nation']").val(resultObj.resultContent.nation);
					$(thsample).next().next().next().next().next().next().next().find("input[name='bornDay']").val(resultObj.resultContent.bornDay);
					$(thsample).next().next().next().next().next().next().next().next().find("input[name='certAddress']").val(resultObj.resultContent.certAddress);
					$(thsample).next().next().next().next().next().next().next().next().next().find("[name='PhotoStr']").attr("src","data:image/jpeg;base64,"+resultObj.resultContent.identityPic);


					var cer = $(thsample).next().next().find("input[name='certNumber']").val();
					if(cer.length == 18 || cer.length == 8 || cer.length == 9 || cer.length == 10){
						$.post("xgcollectinfo!getPhoneByCardnum.action",{"certNumber":cer},function (jsonArray) {
							if(jsonArray != ""){
								$(thsample).next().next().next().find("input[name='phone']").val(jsonArray);
							}else{
								$(thsample).next().next().next().find("input[name='phone']").val("");
							}
						})
						$(thsample).next().next().next().find("input[name='phone']").focus();
					}
				}

			} catch(e) 
			{
					// alert(e);
			}
		}
		
		//清除表单元素
		function clearForm()
		{
			var phonelen = 0;
			$("input[name='phone']").each(function () {
				phonelen++;
			})
			phonelen = phonelen-1;
			document.getElementById("timeElapsed").value = "";
			document.getElementsByName("partyName").value = "";
			document.getElementsByName("gender").value = "";
			document.getElementsByName("nation").value = "";
			document.getElementsByName("bornDay").value = "";
			document.getElementsByName("certAddress").value = "";
			document.getElementsByName("certNumber").value = "";
			document.getElementsByName("result").value = "";
			document.getElementsByName("certType").value = "";
			document.getElementsByName("certType").value = "";
			$("input[name='phone']").attr('src',"");

		}
		
		//将结果数据转成json字符串
		function toJson(str)
		{
			return eval('('+str+')');  	
		}
		
		//修改编码方式
		function changecodemethod(str)
		{
			$("#hidtext").val(str);
			$("#codemethod").val(str);
		}
		
		//取消自动识别
		function noidcard()
		{
			nocard = 1;
			clearInterval(interval);
		}
		
		//连接身份证识别器
		function connect()
		{
			nocard = 0;
			// clearForm();
			//可能是这里的原因清除了表单元素
			var CertCtl = document.getElementById("CertCtl");
			try {
				var result = CertCtl.connect();
				document.getElementById("result").value = result;
			} catch (e) 
			{
			}
		}
	</script>
</body>
</html>
