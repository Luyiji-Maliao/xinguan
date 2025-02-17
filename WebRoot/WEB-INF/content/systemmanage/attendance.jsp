<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>考勤</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="考勤">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<jsp:include page="/include/header4.jsp"></jsp:include>	
<!-- <script type="text/javascript" src="js/swfupload/swfupload.js"></script> -->
<!-- 	<script type="text/javascript" src="js/swfupload/uploaderPanel.js"></script> -->
<script type="text/javascript" src="js/ext4xupload/swfupload/swfupload.js"></script>	
	<script type="text/javascript" src="js/ext4xupload/uploadPanel/UploadPanel.js"></script>  
		
<script type="text/javascript">
	
Ext.onReady(function() {
    Ext.QuickTips.init();
    var pageSize = 20;
	//gridColumnstate();
    /**------------------------存储Grid中的主数据--------------------------------*/
    Ext.define('mainPage', {
     extend: 'Ext.data.Model',
     fields: [
        {
			name : 'id' 
		}, {
			name : 'attDeptName'
		}, {
			name : 'attUserName' 
		}, {
			name : 'attJobNumber'
		},{
			name:'attDate'
		},{
			name:'attWeek'
		},{
			name:'attClasses'
		},{
			name:'attState'
		},{
			name:'attSignin'
		},{
			name:'attSignout'
		}
     ]
 });

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:20,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
         actionMethods: {
                read   : 'POST', // by default GET
            },
         url: 'attendance.action',
         reader: {
            // type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
     //autoLoad: true
 });
  
	
	
	
	main_store.on("beforeload",function(store,options){
		var searchField = Ext.getCmp("searchField");
		if(searchField){
			var new_params={filter_LIKES_attUserName:searchField.getValue()};			
			Ext.apply(store.proxy.extraParams, new_params);  
		}
		});
		
		 main_store.loadPage(1);
	
	
	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid = Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : '考勤管理',
		width : 400,
		region:'center',
		loadMask : true,
		stripeRows : true,
		frame : true,
		store : main_store,
		viewConfig:{  
		   enableTextSelection:true  //可复制内容
		} ,
		   listeners:{  
					//双击  
			       itemdblclick:function(grid,row){  
			           //alert('rowclick')  ;
			       }  
   		} ,
		columns : [{
			hidden : false,
			header : 'ID', 
			width : 50,
			sortable : true,
			dataIndex : 'id'
		},{
			hidden : false,
			header : '部门',
			width : 255,
			dataIndex : 'attDeptName'
		}, {
			hidden : false,
			header : '员工',
			dataIndex : 'attUserName'
		},{
			hidden : false,
			header:'卡号',
			dataIndex:'attJobNumber'
		},{
			hidden : false,
			header:'日期',
			dataIndex:'attDate'
		},{
			hidden : false,
			header:'周',
			dataIndex:'attWeek'
		},{
			hidden : false,
			header:'班次',
			dataIndex:'attClasses'
		},{
			hidden : false,
			header:'考勤',
			dataIndex:'attState'
		},{
			hidden : false,
			header:'签到',
			dataIndex:'attSignin'
		},{
			hidden : false,
			header:'签退',
			dataIndex:'attSignout'
		}],
		
		//selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
		bbar : new Ext.PagingToolbar( {
			store :main_store,
			displayInfo : true,
			pageSize : pageSize
		}),
		tbar : [ 
		{
			text : '查看',
			id:'tbar0',
			hidden:true,
			icon : 'img/show.png',
			handler : function() {
		  		location.href="attendance!showattendance.action";
			}
		},
		{
			text : '上传',
			id:'tbar1',
			hidden:true,
			icon : 'img/add.png',
			handler : function() {
				fileupload.show();
			}
		},
		{
			id:'searchField',
			xtype:'textfield',
			width:200,
			emptyText:'请输入要查找的员工', 
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
	
//用户页面权限
if(Ext.getCmp("show_data_grid")){
	roleHaveRight();
}

	
var panel=Ext.create('Ext.ux.uploadPanel.UploadPanel',{
            header: false,
            addFileBtnText : '选择文件...',
            uploadBtnText : '上传',
            removeBtnText : '移除所有',
            cancelBtnText : '取消上传',
            file_size_limit : 100,//MB
            width : 750, 
            height : 300, 
            post_params : {'fileFileName':"fileFileName1111"},
            flash_url : "js/ext4xupload/swfupload/swfupload.swf", 
	        flash9_url : "js/ext4xupload/swfupload/swfupload_fp9.swf",
            upload_url : 'uploadimg!filesupload.action'
            }); 
            fileupload = Ext.widget('window', {
                title: '文件上传',
                closeAction: 'hide',
                layout: 'fit',
                resizable: false,
                modal: true,
                items: panel
            });
	
	

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
