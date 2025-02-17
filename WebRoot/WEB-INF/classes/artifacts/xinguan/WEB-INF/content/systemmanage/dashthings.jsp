<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
       
    <title>首页</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--     
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<jsp:include page="/include/header4.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="ext-4.2.1.883/examples/portal/portal.css" />
       	   	    <script type="text/javascript" src="ext-4.2.1.883/examples/portal/classes/Portlet.js"></script>
    <script type="text/javascript" src="ext-4.2.1.883/examples/portal/classes/PortalColumn.js"></script>
   	    <script type="text/javascript" src="ext-4.2.1.883/examples/portal/classes/PortalDropZone.js"></script>
   	    <script type="text/javascript" src="ext-4.2.1.883/examples/portal/classes/PortalPanel.js"></script>
   	

	<script type="text/javascript"	src="js/jquery-1.7.2.min.js"></script>
     <style>
.x-grid-record-red table{
   background: #FFFF99;
}
.textarea{
	background:#E5E5E5;
}
.xiugai{
	background:#E5E5E5;
}
</style>
<script>

	 Ext.onReady(function() {  
  Ext.QuickTips.init();
	var pageSize = 15;
	
	
	//  1. start
	
		/**------------------------存储Grid中的主数据--------------------------------*/
		 Ext.define('scheduleStore', {
     extend: 'Ext.data.Model',
     fields: [
      					 {
				  		    name:'id'
						},{
							name : 'scheduleSortId'
						},{
							name : 'scheduleTitle'
						},{
							name : 'scheduleSender'
						},{
							name:'scheduleReceiver'
						},{
							name:'scheduleSenderDate'
						},{
							name:'scheduleTime'
						},{
							name:'scheduleLink'
						},{
							name:'scheduleContent'
						},{
							name:'earlyWarning'
						},{
							name:'priorityLevel'
							}
     ]
 });

 var schedule_store = Ext.create('Ext.data.Store', {
     model: 'scheduleStore',
     pageSize:1000,//不加，点击下一页时默认25条
     //remoteSort: true, //请求后台排序
     proxy: {
         type: 'ajax',
         actionMethods: {
                read   : 'POST', // by default GET
            },
         url: 'schedule.action',
         reader: {
            // type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
     //autoLoad: true
 });
  
	schedule_store.on("beforeload",function(store,options){
			var new_params={filter_EQS_scheduleReceiver:'${loginUser.name}',filter_EQS_scheduleStatus:'1',filter_EQS_receiverStatus:'1'};	
			Ext.apply(store.proxy.extraParams, new_params);  
		});
		
		schedule_store.loadPage(1);
		
		/**------------------------存储Grid中的主数据--------------------------------*/
								
/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid = new Ext.grid.GridPanel( {
		id : 'show_data_grid',	
		height:400,
		loadMask : true,
		stripeRows : true,
		//closable:true,
		//frame : true,
		store : schedule_store,		
		columns : [{
		header : '事件标题',
		width:110,
		dataIndex : 'scheduleTitle'
	},{
		header : '优先级',
		width:80,
		dataIndex : 'priorityLevel'
	},{
		header : '时间节点',
		width:100,
		dataIndex : 'scheduleTime'
	},{
		header : '提前几天提醒',
		width:100,
		dataIndex : 'earlyWarning'
	},{
		header : '发起时间',
		width:130,
		dataIndex : 'scheduleSenderDate'
	}, {
			header : '发起人',
			width:70,
			dataIndex : 'scheduleSender'
		}],
		 viewConfig: 
         { 
			  getRowClass : function(record,rowIndex,rowParams,store){
			  var d1=new Date(record.data.scheduleSenderDate.replace(/-/g,"/"));
			  var d2=new Date(record.data.scheduleTime.replace(/-/g,"/"));
			  var num = (d2-d1)/(1000*3600*24);//求出两个时间的时间差，这个是天数  
			  var days = parseInt(Math.ceil(num));//转化为整天（小于零的话剧不用转了）  
            if(record.data.earlyWarning!=null&&days<=record.data.earlyWarning){
                return 'x-grid-record-red';
            }else{
                return '';
            }     
        }
	            },
		listeners:{  
		itemdblclick : function(grid,row){  
						Ext.getCmp("saveorupdate").setValue("修改");
						Ext.getCmp("subbut").setText("修改");
						var record = grid.getSelectionModel().getSelection();  
					 	  if (record.length) {
					 		 	Ext.getCmp("scheduleSender").addClass('xiugai');
					 	  		Ext.getCmp("scheduleSender").setReadOnly(true);
					 	  		Ext.getCmp("scheduleSender").show();
					 	  		
					 	  		if(record[0].get("scheduleSender")=="${loginUser.name}"){
						 	  		
					 	  			//设置只读以及背景色 s
						 	  		Ext.getCmp("scheduleTitle").removeClass('xiugai');
						 	  		Ext.getCmp("scheduleTitle").setReadOnly(false);
						 	  		$("#scheduleContent").css("background","#FFFFFF");
						 	  		Ext.getCmp("scheduleContent").setReadOnly(false);
						 	  		Ext.getCmp("scheduleReceiver").addClass('xiugai');
						 	  		Ext.getCmp("scheduleReceiver").setReadOnly(true);
						 	  		Ext.getCmp("receiveradd").setDisabled(true);
						 	  		Ext.getCmp("scheduleTime").removeClass('xiugai');
						 	  		Ext.getCmp("scheduleTime").setReadOnly(false);
									//设置只读以及背景色 e
					 	  		}else{
					 	  			//设置只读以及背景色 s
					 	  			Ext.getCmp("scheduleTitle").addClass('xiugai');
						 	  		Ext.getCmp("scheduleTitle").setReadOnly(true);
						 	  		Ext.getCmp("scheduleContent").setReadOnly(true);
						 	  		$("#scheduleContent").css("background","#E5E5E5");
						 	  		Ext.getCmp("scheduleReceiver").addClass('xiugai');
						 	  		Ext.getCmp("scheduleReceiver").setReadOnly(true);
						 	  		Ext.getCmp("receiveradd").setDisabled(true);
						 	  		Ext.getCmp("scheduleTime").addClass('xiugai');
						 	  		Ext.getCmp("scheduleTime").setReadOnly(true);
									//设置只读以及背景色 e
					 	  		}
					 	  		
								Ext.getCmp("schedulewc").show();//完成按钮显示
								Ext.getCmp("schedulebh").show();//驳回按钮显示 
								Ext.getCmp("priorityLevel").show();//任务优先级
								ss_combox.show();//显示任务类别
							  	if(record[0].get("scheduleLink")==""){//任务链接是否为空
									Ext.getCmp("schedulecl").hide();
								}else{
									Ext.getCmp("schedulecl").show();
								}	
						        crud_data_win.show();
						 	    crud_data_form.getForm().loadRecord(record[0]);
						  }else{
							  Ext.example.msg("提示","请选择要操作的数据");
							  }
		     		  }
		 		 } ,
		 tbar : [ 
						{
							text:'新建',
							icon:'img/new.png',
							handler:function(){
								Ext.getCmp("subbut").show();
										
									crud_data_form.getForm().reset();					
										
										Ext.getCmp("saveorupdate").setValue("添加");
										Ext.getCmp("subbut").setText("添加");
										//设置只读以及背景色 s
					 	  					
							 	  		Ext.getCmp("scheduleTitle").removeClass('xiugai');
							 	  		Ext.getCmp("scheduleTitle").setReadOnly(false);
							 	  		$("#scheduleContent").css("background","#FFFFFF");
							 	  		Ext.getCmp("scheduleContent").setReadOnly(false);
							 	  		Ext.getCmp("scheduleReceiver").removeClass('xiugai');
							 	  		Ext.getCmp("scheduleReceiver").setReadOnly(true);
							 	  		Ext.getCmp("scheduleSender").hide();
							 	  		Ext.getCmp("receiveradd").setDisabled(false);
							 	  		Ext.getCmp("scheduleTime").removeClass('xiugai');
							 	  		Ext.getCmp("scheduleTime").setReadOnly(false);
										//设置只读以及背景色 e
										Ext.getCmp("scheduleStatus").setValue("1");
										Ext.getCmp("sid").setValue();
										Ext.getCmp("priorityLevel").hide();
										Ext.getCmp("schedulecl").hide();//处理按钮隐藏
										Ext.getCmp("schedulewc").hide();//完成按钮隐藏
										Ext.getCmp("schedulebh").hide();//驳回按钮隐藏 
										ss_combox.hide();//隐藏任务类别
										ss_combox.setValue('');
										crud_data_win.show();
							}
						},'->',{
							text:'待办详情',
							icon:'img/show.png',
							handler:function(){
									  helpdocs="待办任务";
									  //parent.window.treelocation();
								      location.href='schedule!modulepage.action';
							}
						}]
	});

/**---------------------------结束显示数据的Grid------------------------------------*/
	//部门，接收人 s
	
	
	var deptid;	   
var store = Ext.create('Ext.data.TreeStore', { 
proxy: { 
type: 'ajax', 
actionMethods: 'POST', // by default GET
url: 'department!departmentShowJson.action'
},
 fields : [{
    name: 'id',
    type: 'int'
  }, {
    name: 'deptName',
    type: 'string'
  }]  
}); 
var treegrid = Ext.create('Ext.tree.Panel', { 
title: '部门',  
height: 300, 
id:'tree_data_grid',
region:'west',
//collapsible: true, 
useArrows: true, //在tree中使用Vista-style样式的箭头。 ...
rootVisible: false, //false，隐藏根节点。
store: store, 
width:300,
multiSelect: false, //设置为true则启用'MULTI'多行选择模式
singleExpand: true,// true，如果每个分支只有1个节点可能展开。 ..
columns: [
{ 
xtype: 'treecolumn', 
text: 'ID', 
flex: 2, //此配置项将被应用到布局管理的容器的子项中. 每个含有flex属性的子项将会被根据当前子项的flex值与所有其他含flex值子项的值的和 的相对比例进行伸缩('hbox'中横向, 'vbox'中纵向). 任何'flex=0'或'flex=undefined'的子项将不被伸缩(即组件原始尺寸不会被修改).
sortable: true, 
dataIndex: 'id' 
},{
    header: '部门',
    width: 100,
    dataIndex: 'deptName',
    align: 'center'
   
}],
	listeners: {
		'itemclick':function(m,n) {
				deptid=n.data.id;
				usergrid.getStore().load();
        }
    }
});
	
	//部门，接收人 e
	
	//接收人 combobox s

		/**------------------------存储Grid中的主数据--------------------------------*/
	
		//待办员工添加函数 s
		function scheduleDeptUser(){
			var record =usergrid.getSelectionModel().getSelection();  
				
				  if (record) {
				  	for(var i=0;i<record.length;i++){
				  		var temp=Ext.getCmp("scheduleReceiver").getValue();
				  		if(temp.indexOf(","+record[i].get("name")+",")<0){
				  			if(temp==""){
								temp=",";				  				
				  			}
					  		var temp2=temp+record[i].get("name")+",";
					  		Ext.getCmp("scheduleReceiver").setValue(temp2);
				  		}
				  	}
				  } else {
				 	  Ext.example.msg("提示","请选择要添加的数据");
				  }
		}
		
		//待办员工添加函数 e
		
 var sm = Ext.create('Ext.selection.CheckboxModel',{checkOnly:true});   //只能通过checkbox选中
		
		var usergrid = new Ext.grid.GridPanel( {
		title : '员工',
		width : 300,
		height:320,
		region:'center',
		loadMask : true,
		stripeRows : true,
		//frame : true,
		store : new Ext.data.Store({ 
			pageSize:1000,
   			singleton : true, 
   			listeners:{ 
				beforeload:function(store,o){ 
  				var new_params={filter_LIKES_name:null,filter_EQI_departmentId:deptid};	
  				Ext.apply(store.proxy.extraParams, new_params); 
				} 
				} ,
   			proxy: { 
            type: 'ajax', 
            url : 'user.action', 
            actionMethods : 'post', 
            reader: { 
                type: 'json', 
                totalProperty: 'totalCount', 
                root: 'result' 
            } 
  	    }, 
	        fields:[{
				name : 'id' 
			},{
				name : 'name'
			}], 
	        autoLoad:true 
		 	}) ,
		 	columns:[{
				hidden : false,
				header : '编号', 
				width : 50,
				sortable : true,
				dataIndex : 'id'
			}, {
				hidden : false,
				header : '真实姓名',
				dataIndex : 'name'
			}],
		//cm : cm1,
		selModel:sm,
		//selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
		tbar : [{
			xtype:'textfield',
			width:100,
			emptyText:'请输入用户姓名',
			enableKeyEvents:true,
			listeners:{
				keyup:function(tf,e){
					var key = e.getKey();
					if(key == e.ENTER){
						user_store.setBaseParam("start",0);
						user_store.setBaseParam("limit",100);
						user_store.setBaseParam("filter_EQI_departmentId",null);
						user_store.setBaseParam("filter_LIKES_name",this.getValue());
						user_store.load();
					}
				}
			}
		}, 
		{
			text:'继续添加',
			icon:'img/show.png',
			handler:function(){
				scheduleDeptUser();
			}
		},{
			text:'确认添加',
			icon:'img/show.png',
			handler:function(){
				  scheduleDeptUser();
				  tree_data_win.hide();
			}
		}]
		});
		
		var tree_data_win = new Ext.Window( {
		id : "tree_data_win",
		title : '人员选择',
		closeAction : 'hide',
		width:600,
		 height: 350,
		layout: {
            type: 'border',
            padding: 5
        },
		items : [treegrid,usergrid]
	});
  	//接收人 combobox e
 	
  	     
  			//分类  combobox s
  			
var ss_combox = Ext.create('Ext.form.ComboBox',{
	    typeAhead: false,
	   	lazyRender:true,
	   	name:'scheduleSortId',
	   // multiSelect:true,  //多选
	   	fieldLabel:'任务类别',
	     queryMode:'local',  
	    store: new Ext.data.Store({ 
	       pageSize:100, 
		   singleton : true, 
		   listeners:{ 
					beforeload:function(store,o){ 
	  				var new_params={filter_EQS_userName:'${loginUser.name}'};	
	  				Ext.apply(store.proxy.extraParams, new_params); 
					} 
					} ,
		   proxy: { 
	        type: 'ajax', 
	        url : 'schedulesort!listcombo.action', 
	        actionMethods : 'post', 
	        reader: { 
	            type: 'json', 
	            totalProperty: 'totalCount', 
	            root: 'result' 
	        } 
	}, 
	fields:['id', 'title'], 
	autoLoad:true 
	}),
    valueField: 'id',
    displayField: 'title',
    editable:false
   // allowBlank:false
    
   
}); 
	
	
  			//分类  combobox e 
 /*待办 CRUD S*/
          
           var crud_data_form = new Ext.form.FormPanel( {
			id : 'crud_data_form',
			border:false,
			fieldDefaults: {
		        labelAlign: 'right',
		        labelWidth:70
		       
		    },
			buttonAlign : 'center',
			frame : true,
			url : 'schedule!save.action',
			autoHeight : true,
			defaults : {
				xtype : 'textfield',
				anchor: '90%'
				//width : 250
			},
			width : 420,
			items : [{
					xtype:'hidden',
					id:'sid',
					name:'id'
				},{
					xtype:'hidden',
					fieldLabel : '添加/修改',
					id:'saveorupdate',
					name:'saveorupdate'
				},{
					xtype:'hidden',
					fieldLabel : '任务状态',
					name:'scheduleStatus',
					id:'scheduleStatus',
					value:'1'
				},{
					xtype:'hidden',
					fieldLabel : '发送日期',
					name:'scheduleSenderDate'
				},{
					xtype:'hidden',
					fieldLabel : '是否接受',
					name:'receiverStatus',
					id:'receiverStatus',
					value:'1'
				},{
					fieldLabel : '任务链接',
					id:'scheduleLink',
					xtype:'hidden',
					value:'',
					name : 'scheduleLink'
				},{
					fieldLabel : '任务标题',
					id:'scheduleTitle',
					name : 'scheduleTitle',
					allowBlank:false
				},
			    {
					fieldLabel : '任务内容',
					xtype : 'textarea',
					id:'scheduleContent',
					name : 'scheduleContent'
				},{
					fieldLabel : '发起人',
					id:'scheduleSender',
					name : 'scheduleSender',
				},
				{
					fieldLabel : '接收人',
					xtype:'fieldcontainer',
					layout: 'hbox',
					
					 items: [
                           {
                           		xtype:'textfield',
                               id:'scheduleReceiver',
							   name : 'scheduleReceiver',
							   width : 200,
							   allowBlank:false,
                              
                           },
                           {
                               xtype: 'button',
                               id:'receiveradd',
                               text : '添加',
                               handler:function(){
		                		 tree_data_win.show();
                               }
                               
                           }
                        ]
					
				},{
                    xtype     : 'datefield',
                    name      : 'scheduleTime',
                    id:'scheduleTime',
                    format :'Y-m-d', 
                    editable:false,
                    fieldLabel: '时间节点' 
                },
				{
					fieldLabel : '任务提醒',
					xtype:'fieldcontainer',
					layout: 'hbox',
					
					 items: [ {
                               xtype: 'displayfield',
                               value: '提前'
                           },
                           {
                           		xtype:'numberfield',
                                allowDecimals:false, 
                                width : 200,
								minValue:0, 
								id:'earlyWarning',
								name : 'earlyWarning'
                              
                           },
                           {
                               xtype: 'displayfield',
                               value: '天'
                           }
                        ]
					
				},
                {
               		xtype:'numberfield',
               		fieldLabel : '任务优先级',
                    allowDecimals:false, 
					minValue:0,
					id:'priorityLevel',
					name : 'priorityLevel'
                  
               },ss_combox
                ],
			buttons : [ {
				text : '添加',
				id:'subbut',
				handler : function() {
					crud_data_form.getForm().submit({
		                    success: function(f, action) {
		                     if (action.result.success) {
		                       	crud_data_win.hide();
		                        crud_data_form.getForm().reset();
		                        schedule_store.reload();
		                        Ext.example.msg('消息', action.result.msg);
		                      }
		                    },
		                     failure:handFailure
				});
			}},{
							text:'完成',
							id:'schedulewc',
							handler:function(){
							var record = grid.getSelectionModel().getSelection();  
					 	  if (record.length) {
					 	  		Ext.MessageBox.confirm('提示', '确认完成任务',function(b,t){
					 	  			if( b == 'yes'){  
					 	  				  Ext.Ajax.request({
											url:'schedule!updateStatus.action',
											params: {id: record[0].get("id")},
											success:function(response){
												  schedule_store.reload();
												  crud_data_win.hide();
												  Ext.example.msg('消息', "任务已完成");
											},
											failure:function(){
												Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
											}
										});
                					} 
					 	  		}); 
						    
						  }else{
							  Ext.example.msg("提示","请选择要完成的任务"); 
							  }
					
							}
						},{
							text:'驳回',
							id:'schedulebh',
							handler:function(){
							var record = grid.getSelectionModel().getSelection();  
					 	  if (record.length) {
					 	  		Ext.MessageBox.confirm('提示', '确认驳回任务',function(b,t){
					 	  			if( b == 'yes'){  
					 	  				  Ext.Ajax.request({
											url:'schedule!receiveStatus.action',
											params: {id: record[0].get("id")},
											success:function(response){
												  schedule_store.reload();
												  crud_data_win.hide();
												  Ext.example.msg('消息', "任务已驳回");
											},
											failure:function(){
												Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
											}
										});
                					} 
					 	  		}); 
						    
						  }else{
							  Ext.example.msg("提示","请选择要驳回的任务"); 
							  }
					
							}
						}, {
				text : '处理',
				id:'schedulecl',
				handler:function(){
						var record = grid.getSelectionModel().getSelection();
		 	 			 if (record.length) {
		 	  				  Ext.Ajax.request({
								url:'schedule!updateStatus.action',
								params: {id: record[0].get("id")},
								success:function(response){
									  location.href=Ext.getCmp("scheduleLink").getValue().trim();
								},
								failure:function(){
									Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
								}
							});
    				
			  }else{
				  Ext.example.msg("提示","请选择要完成的任务"); 
				  }
				}
			}, {
				text : '取消',
				handler:function(){
					crud_data_win.hide();
				}
			} ]
		});

 /**--------------------------------修改数据Window---------------------------------------------*/

 	var crud_data_win = new Ext.Window( {
		id : "crud_data_win",
		title : '待办任务管理',
		closeAction : 'hide',
		modal : true,
		items : crud_data_form
	});
 
 
 /**--------------------------------结束修改数据Window-------------------------------------------*/
           
           
           /*待办 CRUD E*/
	//  1. end
	
	//  2. start
	
		/**------------------------存储Grid中的主数据--------------------------------*/
		 Ext.define('informsStore', {
     extend: 'Ext.data.Model',
     fields: [
       {
			name:'id'
		},{
			name : 'informsTitle'
		},{
			name : 'informsSender'
		},{
			name : 'informsReceiver'
		},{
			name:  'informsSenderDate'
		},{
			name:  'informsLink'
		},{
			name:'informsContent'
		},{
			name:'informsStatus',
		}
     ]
 });
var filter_EQS_informsStatusi=1
 var informs_store = Ext.create('Ext.data.Store', {
     model: 'informsStore',
     pageSize:1000,//不加，点击下一页时默认25条
     //remoteSort: true, //请求后台排序
     proxy: {
         type: 'ajax',
         actionMethods: {
                read   : 'POST', 
            },
         url: 'informs.action',
         reader: {
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
 });
  
	informs_store.on("beforeload",function(store,options){
			var new_params={filter_EQS_informsReceiver:'${loginUser.name}',filter_EQS_informsStatus:filter_EQS_informsStatusi};
			Ext.apply(store.proxy.extraParams, new_params);  
		});
		
		 informs_store.loadPage(1);

	

/**---------------------------结束Grid的列模型-----------------------------------*/									

/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid2 = new Ext.grid.GridPanel( {
		id : 'show_data_grid2',	
		height:400,
		loadMask : true,
		stripeRows : true,
		border:false,
		store : informs_store,		
		columns : [{
		header : '消息标题',
		width:110,
		dataIndex : 'informsTitle'
	}, {
		header : '发送人',
		width:70,
		dataIndex : 'informsSender'
	}, {
		header : '发送时间',
		width:130,
		dataIndex : 'informsSenderDate'
	}],
		listeners:{  
		itemdblclick : function(grid,row){  
						Ext.getCmp("subbuts").hide();
						Ext.getCmp("informsgd").show();
						var record = grid.getSelectionModel().getSelection();  
					 	  if (record.length) {
					 			//设置只读以及背景色 s
					 	  		Ext.getCmp("informsTitle").addClass('xiugai');
					 	  		Ext.getCmp("informsTitle").setReadOnly(true);
					 	  		$("#informsContent").css("background","#E5E5E5");
					 	  		Ext.getCmp("informsContent").setReadOnly(true);
					 	  		Ext.getCmp("informsReceiver").addClass('xiugai');
					 	  		Ext.getCmp("informsReceiver").setReadOnly(true);
					 	  		Ext.getCmp("informsreceiveradd").setDisabled(true);
								//设置只读以及背景色 e
						      informs_data_win.show();
						 	  informs_data_form.getForm().loadRecord(record[0]);
						  }else{
							  Ext.example.msg("提示","请选择要查看的数据");
							  }
		     		  }
		 		 } ,
		 tbar : [ 
						{
							text:'新建',
							icon:'img/new.png',
							handler:function(){
								Ext.getCmp("subbuts").show();
								Ext.getCmp("informsgd").hide();
										
											informs_data_form.getForm().reset();					
										
										//设置只读以及背景色 s
							 	  		Ext.getCmp("informsTitle").removeClass('xiugai');
							 	  		Ext.getCmp("informsTitle").setReadOnly(false);
							 	  		$("#informsContent").css("background","#FFFFFF");
							 	  		Ext.getCmp("informsContent").setReadOnly(false);
							 	  		Ext.getCmp("informsReceiver").removeClass('xiugai');
							 	  		Ext.getCmp("informsReceiver").setReadOnly(true);
							 	  		Ext.getCmp("informsreceiveradd").setDisabled(false);
										//设置只读以及背景色 e
										Ext.getCmp("informsStatus").setValue("1");
										Ext.getCmp("iid").setValue();
										informs_data_win.show();
							}
						},{
							text:'未读消息',
							icon:'img/new.png',
							handler:function(){
								
								if(this.text=="未读消息"){
									this.setText("已读消息");
									
									//grid2.getStore().getProxy().extraParams.filter_EQS_informsStatus="0";
									//grid2.getStore().getProxy().extraParams.filter_EQS_informsReceiver='${loginUser.name}';
				    				filter_EQS_informsStatusi=0;
				    				grid2.getStore().load();
								}
								else if(this.text=="已读消息"){
									this.setText("未读消息");
									//grid2.getStore().getProxy().extraParams.filter_EQS_informsStatus="1";
									//grid2.getStore().getProxy().extraParams.filter_EQS_informsReceiver='${loginUser.name}';
				    				filter_EQS_informsStatusi=1;	
				    				grid2.getStore().load();
								}
							}
						}]
		
	});

/**---------------------------结束显示数据的Grid------------------------------------*/
//部门，接收人 informs
	
	var deptid1;	   
var store = Ext.create('Ext.data.TreeStore', { 
proxy: { 
type: 'ajax', 
actionMethods: 'POST', // by default GET
url: 'department!departmentShowJson.action'
},
 fields : [{
    name: 'id',
    type: 'int'
  }, {
    name: 'deptName',
    type: 'string'
  }]  
}); 
var treegrid2 = Ext.create('Ext.tree.Panel', { 
title: '部门',  
height: 300, 
id:'tree_data_grid2',
region:'west',
//collapsible: true, 
useArrows: true, //在tree中使用Vista-style样式的箭头。 ...
rootVisible: false, //false，隐藏根节点。
store: store, 
width:300,
multiSelect: false, //设置为true则启用'MULTI'多行选择模式
singleExpand: true,// true，如果每个分支只有1个节点可能展开。 ..
columns: [
{ 
xtype: 'treecolumn', 
text: 'ID', 
flex: 2, //此配置项将被应用到布局管理的容器的子项中. 每个含有flex属性的子项将会被根据当前子项的flex值与所有其他含flex值子项的值的和 的相对比例进行伸缩('hbox'中横向, 'vbox'中纵向). 任何'flex=0'或'flex=undefined'的子项将不被伸缩(即组件原始尺寸不会被修改).
sortable: true, 
dataIndex: 'id' 
},{
    header: '部门',
    width: 100,
    dataIndex: 'deptName',
    align: 'center'
   
}],
	listeners: {
		'itemclick':function(m,n) {
				deptid1=n.data.id;
				usergrid2.getStore().load();
        }
    }
});

		//待办员工添加函数 s
		function informsDeptUser(){
			var record =usergrid2.getSelectionModel().getSelection();  
				
				  if (record) {
				  	for(var i=0;i<record.length;i++){
				  		var temp=Ext.getCmp("informsReceiver").getValue();
				  		if(temp.indexOf(","+record[i].get("name")+",")<0){
				  			if(temp==""){
								temp=",";				  				
				  			}
					  		var temp2=temp+record[i].get("name")+",";
					  		Ext.getCmp("informsReceiver").setValue(temp2);
				  		}
				  	}
				  } else {
				 	  Ext.example.msg("提示","请选择要添加的数据");
				  }
		}
	
	//部门，接收人  inform e
			var sm = Ext.create('Ext.selection.CheckboxModel',{checkOnly:true});   //只能通过checkbox选中
	
		var usergrid2 = new Ext.grid.GridPanel( {
		title : '员工',
		width : 300,
		height:320,
		loadMask : true,
		stripeRows : true,
		selModel:sm,
		store : new Ext.data.Store({ 
			pageSize:1000,
   			singleton : true, 
   			listeners:{ 
				beforeload:function(store,o){ 
  				var new_params={filter_LIKES_name:null,filter_EQI_departmentId:deptid1};	
  				Ext.apply(store.proxy.extraParams, new_params); 
				} 
				} ,
   			proxy: { 
            type: 'ajax', 
            url : 'user.action', 
            actionMethods : 'post', 
            reader: { 
                type: 'json', 
                totalProperty: 'totalCount', 
                root: 'result' 
            } 
  	    }, 
	        fields:[{
				name : 'id' 
			},{
				name : 'name'
			}], 
	        autoLoad:true 
		 	}) ,
		 	columns:[{
				hidden : false,
				header : '编号', 
				width : 50,
				sortable : true,
				dataIndex : 'id'
			}, {
				hidden : false,
				header : '真实姓名',
				dataIndex : 'name'
			}],
		tbar : [ {
			xtype:'textfield',
			width:100,
			emptyText:'请输入用户姓名',
			enableKeyEvents:true,
			listeners:{
				keyup:function(tf,e){
					var key = e.getKey();
					if(key == e.ENTER){
						user_store.setBaseParam("start",0);
						user_store.setBaseParam("limit",100);
						user_store.setBaseParam("filter_EQI_departmentId",null);
						user_store.setBaseParam("filter_LIKES_name",this.getValue());
						user_store.load();
					}
				}
			}
		},
				{
					text:'继续添加',
					icon:'img/show.png',
					handler:function(){
					informsDeptUser();
					}
				},{
					text:'确认添加',
					icon:'img/show.png',
					handler:function(){
						  informsDeptUser();
						  tree2_data_win.hide();
					}
				}]
		});
		
		var tree2_data_win = new Ext.Window( {
		id : "tree2_data_win",
		title : '人员选择',
		closeAction : 'hide',
		 width:600,
		 height: 350,
		layout: {
            type: 'border',
            padding: 5
        },
		items : [treegrid2,usergrid2]
	});
/*待办 CRUD S*/
          
           var informs_data_form = new Ext.form.FormPanel( {
			id : 'informs_data_form',
			fieldDefaults: {
		        labelAlign: 'right',
		        labelWidth:70
		       
		    },
			buttonAlign : 'center',
			frame : true,
			url : 'informs!save.action',
			autoHeight : true,
			defaults : {
				xtype : 'textfield',
				anchor: '90%'
				//width : 200
			},
			width : 400,
			items : [{
					xtype:'hidden',
					id:'iid',
					name:'id'
				},{
					xtype:'hidden',
					fieldLabel : '消息状态',
					name:'informsStatus',
					id:'informsStatus',
					value:'1'
				},{
					fieldLabel : '消息标题',
					id:'informsTitle',
					name : 'informsTitle',
					allowBlank:false
				},
				{
					fieldLabel : '接收人',
					xtype:'fieldcontainer',
					layout: 'hbox',
					 items: [
                           {
                           		xtype:'textfield',
                               id:'informsReceiver',
							   name : 'informsReceiver',
							   readOnly:true,
							   allowBlank:false,
                              
                           },
                           {
                               xtype: 'button',
                               id:'informsreceiveradd',
                               text : '添加',
                               handler:function(){
		                		 tree2_data_win.show();
                               }
                               
                           }
                        ]
					
				}
				,
			    {
					fieldLabel : '消息内容',
					xtype : 'textarea',
					id:'informsContent',
					name : 'informsContent'
				}],
			buttons : [ {
				text : '发送',
				id:'subbuts',
				handler : function() {
					informs_data_form.getForm().submit({
		                    success: function(f, action) {
		                     if (action.result.success) {
		                       informs_data_win.hide();
		                       
		                        informs_data_form.getForm().reset();
		                        informs_store.reload();
		                        Ext.example.msg('消息', action.result.msg);
		                      }
		                    },
		                     failure:handFailure
				});
			}},{
				text:'归档',
				id:'informsgd',
				handler:function(){
				var record = grid2.getSelectionModel().getSelection();  
		 	  if (record.length) {
			      Ext.Ajax.request({
						url:'informs!updateStatus.action',
						params: {id: record[0].get("id")},
						success:function(response){
							  informs_store.reload();
							  informs_data_win.hide();
							  Ext.example.msg('消息', "消息已归档");
						},
						failure:function(){
							Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
						}
					});
			  }else{
				  Ext.example.msg("提示","请选择要归档的消息"); 
				  }
		
				}
			}, {
				text : '取消',
				handler:function(){
					informs_data_win.hide();
				}
			} ]
		});

 /**--------------------------------修改数据Window---------------------------------------------*/

 	var informs_data_win = new Ext.Window( {
		id : "informs_data_win",
		title : '消息中心',
		closeAction : 'hide',
		modal : true,
		items :informs_data_form
	});
 
 
 /**--------------------------------结束修改数据Window-------------------------------------------*/
           
           
           /*待办 CRUD E*/
	//  2. end
	
        
  
            var pl = [{  
                        id : '001',  
                        title : '待办事项',   
                        layout : 'fit', 
                        items:grid
                    }, {  
                        id : '002',  
                        title : '消息中心',  
                        layout : 'fit',
                        items : grid2 
                    }];  
  
            var portal = Ext.create('Ext.panel.Panel',{
            			title:'个人门户',  
                        region : 'center',  
                        layout: 'border',
                        tbar:[{
                                     	text:'换肤',
                                     	handler:function(){
											plw.show();
                                     	}
                             }],
                        items: [{
                            xtype: 'portalpanel',
                            region: 'center',
                       		 items : [{  
                        			id:'p0',
                                    items : []  
                                }, {  
                                	id:'p1',
                                   // style : 'padding:10px 0 10px 10px',  
                                    items : []  
                                }, {  
                                	id:'p2',
                                   // style : 'padding:10px',  
                                    items : []  
                                }]
                                }] 
  
                    });  
  
            var viewport = new Ext.Viewport({  
                        layout : 'fit',  
                        items : [portal]  
                    });  
  //////换肤start

		 var themes = [ 
		    ['默认','i'],
			['浅蓝', 'a'], 
			['青色', 'b'], 
			['绿色', 'c'], 
			['黄色', 'd'], 
			['橙色', 'e'], 
			['暗紫', 'g'], 
			['紫色','h'],
			['pink','m'],
			['红色','n']										           
			]; 
			var Mystore=new Ext.data.SimpleStore({ 
						fields:['theme','css'], 
						data:themes 
				 }); 
									          
			//   定义下拉列表框 
			var Mycombo=new Ext.form.ComboBox({ 
					fieldLabel:'更换皮肤', 
					id:'css', 
					triggerAction:'all', 
					store:Mystore, 
					displayField:'theme', 
					valueField:'css', 
					value:'默认', 
					mode:'local', 
					anchor:'95%', 
					handleHeight:10, 
					resizable:true, 
					selectOnfocus:true, 
					typeAhead:true 
				}); 
			//定义事件							          
			Mycombo.on({ 
				"select":function(){ 
					 var css =   Mycombo.getValue(); 
					 //设置cookies 
					 var date=new Date(); 
					 date.setTime(date.getTime()+30*24*3066*1000); 
					 document.cookie="css="+css+";expires="+date.toGMTString(); 
    				// top.location.href='user!remain.action';
    				//alert(parent.tiaozhuan);
    				parent.tiaozhuan=1;
    				top.location.href='/uscilims/welcome.jsp';
    			} 
           });          										
             var MyPanel=new Ext.form.FormPanel({ 
             width:300, 
             height:150, 
             labelWidth:70, 
             items:[Mycombo] 
         }); 
         
         //window
         var plw = new Ext.Window({
						title:'主题',
						closeAction: 'hide',	
						autoScroll:true,
						modal:true,
						items:MyPanel
					});

//////换肤 end
            function savePosition() {  
                var result = [];  
                var items = portal.items;  
                for (var i = 0; i < items.getCount(); i++) {  
                    var c = items.get(i);  
                    c.items.each(function(portlet) {  
                                var o = {  
                                    id : portlet.getId(),  
                                    col : i  
                                };  
                                result.push(o); ;  
                            });  
                }  
               
                top.location.href='user!remain.action';
            }  
              
            var cookieName = 'rankInfo';  
              
            function setCookie(info) {  
                var expiration = new Date((new Date()).getTime() + 15 * 60000);  
                document.cookie = cookieName + "=" + info + "; expires ="+ expiration.toGMTString();  
            }  
              
            function getCookie() {  
                var allcookies = document.cookie;  
                var cookie_pos = allcookies.indexOf(cookieName);  
                if(cookie_pos!=-1){  
                        cookie_pos += cookieName.length + 1;  
                        var cookie_end = allcookies.indexOf(";", cookie_pos);  
                        var rankInfo = allcookies.substring(cookie_pos, cookie_end);  
                        return rankInfo;  
                    }  
                return null;  
            }  
              
            function delCookie(){  
            var exp = new Date();  
            exp.setTime(exp.getTime() - 1);  
            var cval=getCookie(cookieName);  
            if(cval!=null) document.cookie= cookieName + "="+cval+";expires="+exp.toGMTString();  
            }  
  
              
            removeAll = function(){  
                var items = portal.items;  
                for (var i = 0; i < items.getCount(); i++) {  
                     var c = items.get(i);  
                     c.items.each(function(portlet){  
                          c.remove(portlet);  
                     });  
                }             
            }  
              
init = function(rankInfo) {  
    for (var i = 0; i < rankInfo.length; i++) {  
     for (var j = 0; j < pl.length; j++) {  
      if (rankInfo[i].id == pl[j].id) {  
       portal.items.get(0).items.get(rankInfo[i].col).add(pl[j]);
      }  
     }  
    }  
    portal.doLayout();  
   }      
              
            var rankInfoDefault=[{"id" : "001","col" : 0}, {"id" : "002","col" : 1}];  
            
            eval("var rankInfo_cookie ="+ getCookie());  
            init(rankInfo_cookie==null?rankInfoDefault:rankInfo_cookie);  
  
  
        });  


</script>
  </head>
  
  <body>
    
  </body>
</html>
