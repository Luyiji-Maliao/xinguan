<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>查看考勤</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="考勤">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<jsp:include page="/include/header4.jsp"></jsp:include>	
<script>
	
Ext.onReady(function() {
    Ext.QuickTips.init();
    var pageSize = 2000;
	//gridColumnstate();
    /**------------------------存储Grid中的主数据--------------------------------*/
    Ext.define('mainPage', {
     extend: 'Ext.data.Model',
     fields: [
     	 {
			name : 'attUserName' 
		},{
			name:'attCount'
		}
     ]
 });

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:2000,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
         actionMethods: 'POST', // by default GET
         url: 'attendance!showAttendance.action',
         reader: {
             type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
 });
 main_store.on("beforeload",function(store,options){
		var searchField = Ext.getCmp("searchField");
		if(searchField){
		Ext.apply(store.proxy.extraParams, {attDate:searchField.getValue().trim()});  
		}
		
	});
  main_store.loadPage(1);
	
	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid = new Ext.grid.GridPanel( {
		id : 'show_data_grid',
		title : '考勤',
		width : 200,
		region:'center',
		loadMask : true,
		stripeRows : true,
		frame : true,
		store : main_store,
		columns:[{
			hidden : false,
			header : '员工',
			dataIndex : 'attUserName'
		},{
			hidden : false,
			header:'考勤',
			dataIndex:'attCount'
		}],
		tbar : [ 
		{
			id:'searchField',
			xtype:'textfield',
			width:200,
			emptyText:'请输入日期 ，格式 （年-月 2015-01）', 
			enableKeyEvents:true,
			listeners:{
				keyup:function(tf,e){
					var key = e.getKey();
					if(key == e.ENTER){
						 main_store.loadPage(1);
					}
				}
			}
		},"-",{
			text:'搜索',
			icon:'img/simplesearch.png',
			handler:function(){
				 main_store.loadPage(1);
			}
		}]
	});
	
/**---------------------------结束显示数据的Grid------------------------------------*/	
	

var view = new Ext.Viewport({
		layout:'border',
		items:grid
	});
	
    });
   </script>
  </head>
  
  <body>
  </body>
</html>
