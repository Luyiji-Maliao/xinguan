<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(request.getAttribute("returnStart")==null){
	request.setAttribute("returnStart",0);
}

if(request.getAttribute("returnEntity")==null||request.getAttribute("returnEntity").toString().isEmpty()){
	request.setAttribute("returnEntity","1");
	
}
%>
<!--全局函数 ：提交错误回调，登录过期，返回当前页	 ，时间 ，多条件跳转，列的显示与隐藏参数，页面操作权限读取  -->
<script>
	/**---------------------处理提交错误回调函数---------------------*/
	function handFailure(form, action) {
       	switch (action.failureType) {
            case Ext.form.Action.CLIENT_INVALID:
                Ext.Msg.show({
                	title:'表单验证',
                	icon:Ext.Msg.ERROR,
                	msg:'请认真填写每一项数据'
                });
            break;
            case Ext.form.Action.CONNECT_FAILURE:
                Ext.Msg.show({
                	title:'通信错误',
                	icon:Ext.Msg.ERROR,
                	msg:'系统错误，请联系管理员！'
                });
                
            break;
            case Ext.form.Action.SERVER_INVALID:
               Ext.Msg.show({
                	title:'错误提示',
                	icon:Ext.Msg.ERROR,
                	msg:action.result.msg
                });
		}
   }
/**------------------------结束处理提交错误回调函数---------------------------*/
	//登录过期		
	Ext.Ajax.on('requestcomplete',checkUserSessionStatus, this);
	function checkUserSessionStatus(conn,response,options){
		var str=response.responseText;
		if(str =='9999'){
			alert("未登录或登陆过期 ,请重新登录");
			window.parent.location = '<%=basePath%>';
		}}
	//返回当前页
	var returnStart = "${returnStart}";
	var pages = (returnStart/20)+1;
	//alert((returnStart/20)+1);
	//时间
	 function converDate(date,days){ 
       var d=new Date(date); 
       d.setDate(d.getDate()+days); 
       var m=d.getMonth()+1;
       var nd=d.getDate(); 
      
       if(m<10){
       	m="0"+m;
       }
       
       if(nd<10){
        nd="0"+d.getDate();
       }
       return d.getFullYear()+'-'+m+'-'+nd; 
     }
     //时间差
	/* function converDate(date,days){ 
       var d=new Date(date); 
       d.setDate(d.getDate()+days); 
       var m=d.getMonth()+1;
       var nd=d.getDate(); 
      
       if(m<10){
       	m="0"+m;
       }
       
       if(nd<10){
        nd="0"+d.getDate();
       }
       return d.getFullYear()+'-'+m+'-'+nd; 
     }*/
     //多条件跳转

	var skip=new Object; 
	Ext.onReady(function(){
		 skip.somesearchskip=function somesearchskip(s){	
		    
			if(Ext.getCmp("searchField")){						
				if(Ext.getCmp("s_simplesearch")){
					Ext.getCmp("s_simplesearch").setValue(Ext.getCmp("searchField").getValue());									
				}
			}
			var st = Ext.getCmp("search_form").getForm().getValues();
			var re=JSON.stringify(st);
			var o = JSON.parse(re);
			var ren=JSON.stringify(o);
			window.location=s+'&returnEntity='+encodeURI(ren.replace(/"([^"]*)"/g, "'$1'")); 	      	 			        	 				        	 		        	 		
		};
		 
		 //给高级搜索赋值
		 skip.somesearchvalue=function somesearchvalue(){	
			 var re1=decodeURI("${returnEntity}");
		  		re = eval("(" +re1 + ")");
		  		JSON.stringify(re).replace(/\{|}/g, '').replace(/\"|"/g, '').split(',').forEach(function(param){
		  		  param = param.split(':');
		  		  var name = param[0];
		  		  var val = param[1];
		  		  if(name.length>0&&(name.substring(name.length-1)=='2')){
		  			  Ext.getCmp(name).setValue(val=='[]'?"":val);
		  		  }else if(name.length>1&&(name.substring(0,2)=='s_')){
			  	 	  Ext.getCmp(name).setValue(val=='[]'?"":val);
			  	 }else{
		  			  Ext.getCmp("s_"+name).setValue(val=='[]'?"":val);
		  		 }
		  		
	  		});
	        Ext.getCmp("searchField").setValue(Ext.getCmp("s_simplesearch").getValue());	      	 			        	 				        	 		        	 		
		};
		//store before时高级搜索
		skip.samesearchbefore=function samesearchbefore(data_store){
			var re1=decodeURI("${returnEntity}");
	    	re = eval("(" + re1 + ")");
	    	JSON.stringify(re).replace(/\{|}/g, '').replace(/\"|"/g, '').split(',').forEach(function(param){
		  		  param = param.split(':');
		  		  var namep = param[0];
		  		  var val = param[1];
		  		 if(namep.length>0&&(namep=='simplesearch')){
		  			var new_params={searchField:val};
					Ext.apply(data_store.proxy.extraParams, new_params);
		  		  }else{
		  			var new_params={};
		  			new_params[namep]=val;
					Ext.apply(data_store.proxy.extraParams, new_params);
		  		  }
	    	});
		};
	});
/**
    列的显示与隐藏参数 
    */
	var userId = '${loginUser.id}';	
	var uri = '${pageContext.request.requestURI}'; 
	var pageName = uri.substring(uri.lastIndexOf("/")+1); 
 /**
    列的显示与隐藏保存
    */
   
 function gridchangesave(colIndex, hidden){
		 var msgString = colIndex + ':' + hidden;
            Ext.Ajax.request({
				url : 'gridcolumn!change.action',
				params : {
				msgString : msgString,
				userId : userId,
				pageName : pageName
			}
		});
	}
	/**
      *列的显示与隐藏读取
      */
   var cm;	
	function gridColumnstate(){		
		//alert(pageName);
		Ext.Ajax.request({
			url:'gridcolumn!find.action',
			params : {
				userIdCopy : userId,
				pageNameCopy : pageName
			},
			success:function(response){
				var data = Ext.decode(response.responseText);
				var items = [];
				for(var i=0;i<data.length;i++){
				
					var column = data[i];
					cm.getColumnById(1).hidden=true;
					var columnId = parseInt(column.columnId);
					var columnState = (column.columnState == 'true' ? true : false);
					cm.getColumnById(columnId).hidden = columnState;
				}
			}
		});
		
	}

/*
*页面操作权限读取 
*/
function roleHaveRight(){
Ext.Ajax.request( {
			url : 'role!roleright.action',
			params:{jspPage:pageName},
			success : function(response) {
				var role_right=response.responseText.split(",");
				for(var i = 0;i < role_right.length;i++){
					Ext.getCmp("tbar"+role_right[i]).show();					
				}
			}
		});
		}

/*
*页面操作权限读取 新产线(销售通讯录/送检医院/寄送样)
*/
function roleHaveRightLines(jspUrl){
Ext.Ajax.request( {
			url : 'role!roleright.action',
			params:{jspPage:jspUrl},
			success : function(response) {
				var role_right=response.responseText.split(",");
				for(var i = 0;i < role_right.length;i++){
					Ext.getCmp("tbar"+role_right[i]).show();					
				}
			}
		});
		}
/*获取项目根目录*/		
 function getRootPaths() 
    { 
     var pathName = window.location.pathname.substring(1); 
     var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/')); 
     return window.location.protocol + '//' + window.location.host + '/'+ webName + '/'; 
    } 
   	var gbxz="suoyou";
   	
//处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
    function banBackSpace(e){   
        var ev = e || window.event;//获取event对象   
        var obj = ev.target || ev.srcElement;//获取事件源   
        var t = obj.type || obj.getAttribute('type');//获取事件源类型  
        //获取作为判断条件的事件类型
        var vReadOnly = obj.getAttribute('readonly');
        //处理null值情况
        vReadOnly = (vReadOnly == "") ? false : vReadOnly;
        //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
        //并且readonly属性为true或enabled属性为false的，则退格键失效
        var flag1=(ev.keyCode == 8 && (t=="password" || t=="text" || t=="textarea") 
                    && vReadOnly=="readonly")?true:false;
        //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
        var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
                    ?true:false;        
        
        //判断
        if(flag2){
            return false;
        }
        if(flag1){   
            return false;   
        }   
    }

window.onload=function(){
    //禁止后退键 作用于Firefox、Opera
    document.onkeypress=banBackSpace;
    //禁止后退键  作用于IE、Chrome
    document.onkeydown=banBackSpace;
}	
	</script>
		