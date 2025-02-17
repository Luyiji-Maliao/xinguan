<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String cip=request.getRemoteAddr();
%>
<%!

public static String getIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  

%> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>优迅信息管理登陆</title>
<!-- 引入jquery.js -->
    <script type="text/javascript" src="js/jquery.min.js"></script>
     <script type="text/javascript" src="js/jq-slideVerify.js"></script>
<jsp:include page="/include/header4.jsp"></jsp:include>	
		<link rel="icon" href="${ctx}/static/images/favicon1.ico">
    	<link rel="shortcut icon" href="${ctx}/static/images/favicon1.ico">
		
		<link type="text/css" href="${ctx}/static/comp/jquery-ui-bootstrap/css/bootstrap.css?2" rel="stylesheet">
		
		<link type="text/css" href="${ctx}/static/comp/jquery-ui-bootstrap/css/custom-theme/jquery-ui-1.10.2.custom.css?3" rel="stylesheet"/>
		<link type="text/css" href="${ctx}/static/comp/jquery-ui-bootstrap/css/font-wesome/font-awesome.min.css?1" rel="stylesheet"/>
		
		<link href="${ctx}/static/comp/jquery-ui-bootstrap/css/layout-default-1.3.0.css?1" rel="stylesheet">
		<link href="${ctx}/static/comp/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css?1" rel="stylesheet">
		<link href="${ctx}/static/comp/jQuery-Validation-Engine/css/validationEngine.jquery.css?1" rel="stylesheet">
		<link href="${ctx}/static/comp/jQuery-Validation-Engine/css/template.css?1" rel="stylesheet">
				
		<link href="${ctx}/static/css/application.css?1" rel="stylesheet">
		<link href="${ctx}/static/css/icon.css?1" rel="stylesheet">
		<link href="${ctx}/static/css/theme/theme.blur.css?4" rel="stylesheet">
<style type="text/css">
/*
 *
 * Bio-LIMS Software Confidential
 *
 * (C) COPYRIGHT Bio-LIMS Software, Inc.
 *

 *
 */


body
{ 
font-size: 12px;
} 
 


a:link {color: #BC4D58; text-decoration: none}
a:visited {color: #BC4D58; text-decoration: none}
a:hover {color: #BC4D58; text-decoration: underline}
a:active {color: #BC4D58; text-decoration: underline}

td { cursor:default;
	}
a.txtbtn {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-style: normal;
	font-weight: bold;
	color: #000000;
	text-decoration: none;
	text-align: middle;
	}
a.txtbtn:visited {color: #000000}
a.txtbtn:hover {color: #CC6600;
				text-decoration: none;
			}
a.txtbtn:active {color: #CC6600}


.bgpage {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-style: normal;
	font-weight: normal;
	color: #000000;
	margin: 0px;
	padding: 0px;
	background-color: #FFFFFF;
	background-repeat: no-repeat;
	background-position: left 65px;
}
.welcometo {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 16px;
	font-style: normal;
	font-weight: bold;
	color: #7392AF;
	overflow: visible;
	position: absolute;
	visibility: visible;
	z-index: 1;
	width: 300px;
	left: 42px;
	top: 82px;
}
.mainbgtable {
	text-align: right;
	overflow: visible;
	position: absolute;
	visibility: visible;
	z-index: 2;
	left: 0px;
	top: 230px;
	width: 825px;
	padding: 2px;
}
.inputheader {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-style: normal;
	font-weight: bold;
	color: #063D71;
	text-align: right;
	padding: 2px;
	}
.forgotpassword {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 10px;
	font-style: normal;
	font-weight: normal;
}
.newuserheader {
	text-align: right;
	color: #063D71;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-style: normal;
	font-weight: bold;
	height: 35px;
}
.languageheader {
	font-weight: bold;
	color: #063D71;
	text-align: left;
	height: 18px;
}
.languagelist {
	text-align: left;
	height: 125px;
	vertical-align: top;
	left: 545px;
	overflow: visible;
	position: absolute;
	visibility: visible;
	z-index: 5;
	top: 105px;
}
.languagetext{ 	font-family: Arial, Helvetica, sans-serif;
				font-size: 11px;
				font-style: normal;
			}
.inputfield {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-style: normal;
	font-weight: normal;
	color: #000000;
}
.companylogo {
	text-align: right;
	overflow: visible;
	position: absolute;
	visibility: visible;
	z-index: 3;
	left: 700px;
	top: 105px;
	width: 125px;
	height: 125px;
	vertical-align: top;
}

.loginwait{	position:absolute;
			left:0;
			top:0;
			cursor:wait;
			display:block;
			filter:alpha( opacity=15 );
			}
			
.languagetable{ 
				margin-top:0px;
			}
			
.languagecolumn{ vertical-align:top;
				 padding-right:10px
			}
			
.selectedlang{ 	cursor: default;
				color:#063D71;
			}
.form-horizontal .control-group{
	margin-bottom: 15px;
}
</style>

<script>
	Ext.onReady(function() {
					   Ext.QuickTips.init();
					   
					   /**--------------------------------修改数据Form-------------------------------------*/
											
											var update_data_form = Ext.create('Ext.form.Panel', {
													id : 'update_data_form',
													buttonAlign : 'center',
													url : 'login!updatePwd.action',
													//autoHeight : true,
													//height:200,
													fieldDefaults: {
												        labelAlign: 'right',
												        labelWidth:70
												       
												    },
													defaults : {
														xtype : 'textfield',
														width : 200,
														height:30,
													},
													width : 300,
													height:300,
													items : [{
														fieldLabel : '<font color="red">*</font>账号',
														anchor:'95%',
														name : 'username', 
														id:'account',
														
														readOnly:true,
														allowBlank:false
													}, {
														fieldLabel : '<font color="red">*</font>密码',
														anchor:'95%',
														name : 'password',
														id:'password1',
														//minLength:4,
														//maxLength:12,
														allowBlank:false
													},{
														fieldLabel : '<font color="red">*</font>确认密码',
														anchor:'95%',
														id:'password2',
														//minLength:4,
														//maxLength:12,
														allowBlank:false
													},{
														xtype : 'tbtext',
														text:'<font style="color:blue">温馨提示：</font><br/>1.密码 长度至少8位<br/>2.必须包含数字、字母、特殊字符 三种<br/>3.不能包含3位及以上相同字符的重复<br/>4.不能包含3位及以上字符组合的重复<br/>5.不能包含空格、制表符、换页符等空白字符'
													}],
													buttons : [ {
														text : '提交',
														id:'subbut',
														handler : function() {
															if(Ext.getCmp("password1").getValue()==Ext.getCmp("password2").getValue()){
															
																 Ext.Ajax.request({
																	url : 'login!checkPassword.action',
																	type: 'post',
																	params :{"str":Ext.getCmp("password1").getValue()},
																	success:function(response){
																	  if(response.responseText=='ok'){
																	    update_data_form.getForm().submit({
														                    success: function(f, action) {
														                     if (action.result.success) {//密码修改成功 
														                         update_data_window.hide();
														                         Ext.Msg.confirm('提示', '密码修改成功,是否进入系统？',function (btn) {
					    															
					    															if (btn == 'yes') {		
					    															  window.location = "user!initmain.action";
					    															}});										                     
														                       
														                      }
														                    },
														                     failure:handFailure
																		});
																	     	
																	  }else{
																	     Ext.Msg.alert("提示",response.responseText);
																	  }
																		
																	},
																	failure:function(){
																		Ext.Msg.alert("系统异常");	
																	}
										
																});	
															}else{//两次密码不相同 
																Ext.Msg.alert("提示","两次密码不相同");
															}
															
													}}, {
														text : '取消',
														handler:function(){
															update_data_window.hide();
														}
													} ]
												});

 /**--------------------------------修改数据Window---------------------------------------------*/

											 	var update_data_window = new Ext.Window( {
													id : "update_data_window",
													title : '修改密码',
													closeAction : 'hide',
													modal : true,
													items : update_data_form
												});
 
 
 /**--------------------------------结束修改数据Window-------------------------------------------*/
					   
					   
	//验证码			
	var SlideVerifyPlug = window.slideVerifyPlug;
			var slideVerify = new SlideVerifyPlug('#verify-wrap',{
				wrapWidth:'260',//设置 容器的宽度 ,不设置的话，会设置成100%，需要自己在外层包层div,设置宽度，这是为了适应方便点；
	            initText:'拖动滑块验证',  //设置  初始的 显示文字
	            sucessText:'验证通过',//设置 验证通过 显示的文字
	            getSuccessState:function(res){
	           		//当验证完成的时候 会 返回 res 值 true
	           		//console.log(res);
	           	}
		});			
//登录				    
this.ajax_login=function(){
			if(!slideVerify.slideFinishState){
				Ext.Msg.alert("验证","请拖动滑块验证");
				return;
			}
			var browsertype=navigator.userAgent.toLowerCase().indexOf('firefox')>0 ? "1":"0";
			var account=document.getElementById("username").value;
			var password=document.getElementById("password").value;
			if(account.trim().length>0&&password.trim().length>0){
					   if(password!="0000"){		
					   Ext.Ajax.request({
							url : 'login!login.action',
							type: 'post',
							params :{"username":account,"password":password,browsertype:browsertype},
							success:function(a){
							   var da = Ext.decode(a.responseText);
								if(da.success){
								      Ext.Ajax.request({
										url : 'login!checkPassword.action',
										type: 'post',
										params :{"str":password},
										success:function(response){
										  if(response.responseText=='ok'){
										     if(da.msg!="登陆成功"&&da.msg.indexOf(".action")<0){
											    Ext.MessageBox.show({
											    title:'警告',
											    msg:da.msg,
											    modal:true,
											   // prompt:true,
											   // value:'请输入',
											    fn:function(){
											    	window.location = "user!initmain.action";
											    },
											    buttons:Ext.Msg.YES,
											    icon:Ext.Msg.WARNING
											}) 
											}else{
										         if(da.msg.indexOf(".action")>0){
                                                     window.location = da.msg;
												 }else{
                                                     window.location = "user!initmain.action";
												 }
											}
										  }else{
										    Ext.Msg.alert('温馨提示', '为保证LIMS系统安全，公司决定不符合密码修改规则的登录账号必须修改密码，修改规则如下：<br/>1.密码 长度至少8位<br/>2.必须包含数字、字母、特殊字符 三种<br/>3.不能包含3位及以上相同字符的重复<br/>4.不能包含3位及以上字符组合的重复<br/>5.不能包含空格、制表符、换页符等空白字符',function(){
												account=document.getElementById("username").value;
											      Ext.getCmp('account').setRawValue(account);
												  update_data_window.show();	 
											});
										  }
											
										},
										failure:function(){
											Ext.Msg.alert("系统异常");	
										}
			
									});	
								}
								if(!da.success){
									Ext.Msg.alert("提示",da.msg);
									}	
							},
							failure:function(){
								Ext.Msg.alert("系统异常");	
							}

						})	
					 }else{//验证 默认密码与数据库密码是否一样 
							Ext.Ajax.request({
							url : 'login!checkPwd.action',
							type: 'post',
							params :{"username":account,"password":password,browsertype:browsertype},
							success:function(a){
								var da = Ext.decode(a.responseText);
								 Ext.example.msg('消息', da.msg,3000);
							     if(da.success){//修改密码
							     		Ext.getCmp("account").setValue(account);					     	
										update_data_window.show();	
								  }
								
							},
							failure:function(){
								Ext.Msg.alert("系统异常");	
							}

						})
					 }
				   // alert(password);
				  
				   /* if(password!="0000"){
				    	
				    	$.ajax( {  
					      url:'login!login.action',// 跳转到 action  
					      data:{"username":account,"password":password,browsertype:browsertype},  
					     type:'post',  
					    // cache:false,  
					    // dataType:'json',  
					     success:function(da) {
						     if(da.success){
										window.location = "user!initmain.action";
										}
							if(!da.success){
										alert(da.msg);
								}	
					     },
					     error:function(){
								//alert("系统异常");	
							}
					    });
				    }else{
				    	//验证 默认密码与数据库密码是否一样 
				    	$.ajax( {  
					      url:'login!checkPwd.action',// 跳转到 action  
					      data:{"username":account,"password":password},  
					     type:'post',  
					     success:function(da) {
					    	 alert(da.msg);
						     if(da.success){//修改密码
								alert("修改密码");		
							  }
								
					     },
					     error:function(){
								alert("系统异常");	
							}
					    });
				    }*/
				     
				    
				}
		}
		
		});
		function isTrueUserPwd(s){ 
			//强：字母+数字+特殊字符 
			var patrn=/^(?![a-zA-z]+$)(?!\d+$)(?![!@.#$%^*\-\+,\/]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@.#$%^*\-\+,\/]+$)(?![\d!@.#$%^*\-\+,\/]+$)[a-zA-Z\d!@.#$%^*\-\+,\/]+$/;
			if(s.length > 7 && patrn.test(s)){
				return true;
			}else{
				return false;
				
			}
		}	
		
</script>
</head>



<body class="bgpage" style="background-image: url(img/login/bjt2.png)">

<table width="80%" border="0" cellpadding="0" cellspacing="0"
	class="releasestamp">
	<tr>
		<td><!-- release --></td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" class="companylogo">
	<tr>
		<td valign="top"><img src="img/login/companylogo.gif">
		</td>
	</tr>
</table>
<div class="container">
<form id="loginForm"  class="form-horizontal"  onsubmit="return false">
<table border="0" cellpadding="0" cellspacing="0" class="mainbgtable">
	<tr>
		<td align="right">
		<table border="0" cellpadding="0" cellspacing="0" class="inputheader">
			<tr>
				<td style=" font-size: 12px;"></td>
				<td>
				  <div class="control-group">
                
                <div class="input-prepend">
                    <span class="add-on icon-user"></span>
                    <input type="text" id="username" name="username" value="${param.account}"
                            class="input-xlarge validate[required]" placeholder="请输入用户名">
                </div>
            </div>
			</tr>
			<tr>
			<td height="3">
			</td>
			</tr>
			<tr>
				<td style="font-size: 12px;"></td>
				<td>
				<div class="control-group">
             
                <div class="input-prepend">
                    <span class="add-on icon-key"></span>
                    <input type="password" id="password" name="password"
                            class="input-xlarge validate[required]" placeholder="请输入密码">
                </div>
            </div>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td id="loginbtn" class="pushbutton" align="center">
				<table id="loginbtn_table" border="0" cellspacing="0"
					cellpadding="0">
					<tr style="height:2px;"></tr><!--为登陆按钮上方的空白部分的高度  -->
					
					<tr>
						<div class="verify-wrap" id="verify-wrap"></div>
					</tr><!--滑动验证-->
					
					<tr>
						
						<td id="loginbtn_middle" nowrap align="center"
							class="text  pushbutton_default "
							valign="middle" >
			 <div class="control-group">
                <input id="submitForm" onclick="ajax_login()"  type="submit" class="btn btn-login pull-left" value="登录">
            </div>
						</td>
						<td></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form> 
</div>


</body>


<script src="${ctx}/static/comp/jquery-ui-bootstrap/js/jquery-1.10.1.min.js?1" type="text/javascript"></script>
<script src="${ctx}/static/comp/jquery-ui-bootstrap/js/bootstrap.min.js?1" type="text/javascript"></script>
<script src="${ctx}/static/comp/jquery-ui-bootstrap/js/jquery-ui-1.10.2.custom.min.js?1" type="text/javascript"></script>
<script src="${ctx}/static/comp/jquery-ui-bootstrap/js/jquery.layout-latest.js?1" type="text/javascript"></script>

<script src="${ctx}/static/comp/jquery-ui-bootstrap/js/jquery.blockUI.js?1" type="text/javascript"></script>

<script src="${ctx}/static/comp/jquery-ui-bootstrap/js/bootstrap.file-input.js?1" type="text/javascript"></script>

<script src="${ctx}/static/comp/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js?1" type="text/javascript"></script>
<script src="${ctx}/static/comp/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js?1" type="text/javascript"></script>

<script src="${ctx}/static/comp/jQuery-Validation-Engine/js/jquery.validationEngine.js?1" charset="utf-8" type="text/javascript"></script>
<script src="${ctx}/static/comp/jQuery-Validation-Engine/js/languages/jquery.validationEngine-zh_CN.js?1" charset="utf-8" type="text/javascript"></script>


<script src="${ctx}/static/js/application.js?11" type="text/javascript"></script>
<script type="text/javascript">
    $(function() {
        $("#account").focus();
        $("#loginForm").validationEngine();
        
			if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE7.0") 
			{ 
				$("#loginbtn").hide();
				alert("不支持IE7版本，请修改IE版本");
			} 
        
    });
</script>

</html>