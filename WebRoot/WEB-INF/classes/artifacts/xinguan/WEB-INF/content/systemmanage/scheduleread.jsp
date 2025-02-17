<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
       
    <title>待办任务</title>
    
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
		.xiugai{
	background:#E5E5E5;
}
</style>
<script>

	 Ext.onReady(function() {  
  Ext.QuickTips.init();
	var pageSize = 20;
	
	

	//  待办事件  end
	//待办分类的添加与修改 s
      var schedulesortForm =Ext.create('Ext.form.Panel',{
				width:200,
				labelWidth:50,
				url:'schedulesort!save.action',
				buttonAlign:'center',
				items:[
				{
					xtype:'hidden',
					name:'id',
					id:'id'
				},{
					xtype:'hidden',
					name:'saveorupdate',
					id:'saveorupdates'
				},{
					xtype:'textfield',
					id:'title',
					name:'title',
					fieldLabel:"标题"
				},{
					xtype:'hidden',
					name:'layout',
					value:'fit'
				},{
					xtype:'hidden',
					name:'userName',
					value:'${loginUser.name}'
				},{
					xtype:'hidden',
					name:'ssStatus',
					value:'1'
				}
				],
				buttons:[
					{
						text:'提交',
						handler:function(){
							schedulesortForm.getForm().submit({
								success: function(f, action) {
										if(action.result.success){
											schedulesortWin.hide();
											location.href='schedule!modulepage.action';
										}else{
											Ext.example.msg('消息', action.result.msg);
										}
										
										},
								 failure: handFailure
							});
					}
				}]
			});
													
			var schedulesortWin = new Ext.Window({
				title:'代办分类',
				modal:true,
				closeAction:'hide',
				items:schedulesortForm
				}); 
  			
         //待办分类的添加与修改 e
            var portal = Ext.create('Ext.panel.Panel',{  
                        region : 'center',  
                        title:'已办任务', 
                        layout: 'border',
                        items: [{
                            xtype: 'portalpanel',
                            region: 'center',
                       		 items : [{  
                        			id:'p0',
                                    //columnWidth : .33,
                                    
                                   // style : 'padding:10px 0 10px 10px',  
                                    items : []  
                                }, {  
                                	id:'p1',
                                    //columnWidth : .33,  
                                   // style : 'padding:10px 0 10px 10px',  
                                    items : []  
                                }, {  
                                	id:'p2',
                                   // columnWidth : .33,  
                                   // style : 'padding:10px',  
                                    items : []  
                                }]
                                }] 
  
                    });  
  
            var viewport = new Ext.Viewport({  
                        layout : 'fit',  
                        items : [portal]  
                    });  
  
            function savePosition() {  
                var result = [];  
                var items = portal.items.get(0).items;  
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
               
                	 Ext.Ajax.request({
						url:'evercookies!save.action',
						async: true, //同步请求
						params: { cookieName:"schedulesortcookie",cookieContent:Ext.encode(result) },
						success:function(response){
						
							location.href='schedule!modulepage.action';
						},
						failure:function(){
						Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
						}
					});
                
            }  
              
            
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
//folderSort: true 
}); 
//Ext.ux.tree.TreeGrid is no longer a Ux. You can simply use a tree.TreePanel 
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
				/*user_store.setBaseParam("start",0);
				user_store.setBaseParam("limit",30);
				user_store.setBaseParam("filter_LIKES_name",null);
				user_store.setBaseParam("filter_EQI_departmentId",deptid);
				user_store.load();*/
				usergrid.getStore().load();
        }
    }
});
	
	
	
	//部门，接收人 e
	
	//接收人 combobox s

		//待办员工添加函数 s
		function scheduleDeptUser(){
			var record =usergrid.getSelectionModel().getSelection();  
				
				  if (record.length) {
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
		//modal : true,
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
    editable:false,
    allowBlank:false
    
   
}); 	
//分类  combobox e  
           

 var gridid;
var othersgrid;
var sortcom; 
           /*待办 CRUD S*/
          
         var crud_data_form = Ext.create('Ext.form.Panel',{
			id : 'crud_data_form',
			border:false,
			fieldDefaults: {
		        labelAlign: 'right',
		        labelWidth:70
		       
		    },
			buttonAlign : 'center',
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
					fieldLabel : '是否接受',
					name:'receiverStatus',
					id:'receiverStatus',
					value:'1'
				},
                {
					fieldLabel : '任务链接',
					id:'scheduleLink',
					xtype:'hidden',
					value:'',
					name : 'scheduleLink'
				},{
					xtype:'hidden',
					fieldLabel : '时间',
					name:'scheduleSenderDate',
					id:'scheduleSenderDate'
				},{
					fieldLabel : '任务名称',
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
                    xtype     : 'datefield',
                    name      : 'scheduleTime',
                    id:'scheduleTime',
                    format :'Y-m-d', 
                    editable:false,
                    fieldLabel: '时间节点' 
                },{
					fieldLabel : '发起人',
					id:'scheduleSender',
					name : 'scheduleSender',
				},
				{
					fieldLabel : '接收人',
					xtype:'fieldcontainer',
					layout: 'hbox',
					 //anchor: '100%',
					 items: [
                           {
                           		xtype:'textfield',
                               id:'scheduleReceiver',
                               //width : 200,
							   name : 'scheduleReceiver',
							   allowBlank:false,
                              
                           },
                           {
                               xtype: 'button',
                               id:'receiveradd',
                               name:'receiveradd',
                               text : '添加',
                               handler:function(){
		                		 tree_data_win.show();
                               }
                               
                           }
                        ]
					
				},
				{
					fieldLabel : '任务提醒',
					xtype:'fieldcontainer',
					layout: 'hbox',
					//combineErrors: false,
					 items: [ {
                               xtype: 'displayfield',
                               value: '提前'
                           },
                           {
                           		xtype:'numberfield',
                                allowDecimals:false, 
								minValue:0, 
								//width : 200,
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
		                       /* Ext.getCmp(gridid).getStore().setBaseParam("start",0);
				     			Ext.getCmp(gridid).getStore().setBaseParam("limit",100);
				     		    Ext.getCmp(gridid).getStore().setBaseParam("filter_EQS_scheduleStatus","1");
				     		    Ext.getCmp(gridid).getStore().setBaseParam("filter_EQS_receiverStatus",'1');
				    			Ext.getCmp(gridid).getStore().setBaseParam("filter_EQS_scheduleReceiver",'${loginUser.name}');
				    		    Ext.getCmp(gridid).getStore().setBaseParam("filter_EQI_scheduleSortId",gridid.substring(14,gridid.length));
				    			Ext.getCmp(gridid).getStore().load();*/
				    			 Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQS_scheduleStatus="1";
							      Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQS_receiverStatus="1";
							      Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQS_scheduleReceiver='${loginUser.name}';
							      Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQI_scheduleSortId=gridid.substring(14,gridid.length);
							      Ext.getCmp(gridid).getStore().load();
		                       if(Ext.getCmp("saveorupdate").getValue()=="修改"){
		                    	   if(sortcom!=ss_combox.getValue()){
			                       		othersgrid.items.each(function(item,index,length){
												item.items.each(function(i){
													if(i.title==ss_combox.getRawValue()){
														var othergrid="show_data_grid"+i.id;
														/*Ext.getCmp(othergrid).getStore().setBaseParam("start",0);
									     				Ext.getCmp(othergrid).getStore().setBaseParam("limit",100);
									     		    	Ext.getCmp(othergrid).getStore().setBaseParam("filter_EQS_scheduleStatus","1");
									     		    	Ext.getCmp(othergrid).getStore().setBaseParam("filter_EQS_receiverStatus",'1');
									    				Ext.getCmp(othergrid).getStore().setBaseParam("filter_EQS_scheduleReceiver",'${loginUser.name}');
									    		   	    Ext.getCmp(othergrid).getStore().setBaseParam("filter_EQI_scheduleSortId",othergrid.substring(14,othergrid.length));
									    				Ext.getCmp(othergrid).getStore().load();*/
									    				  Ext.getCmp(othergrid).getStore().getProxy().extraParams.filter_EQS_scheduleStatus="1";
													      Ext.getCmp(othergrid).getStore().getProxy().extraParams.filter_EQS_receiverStatus="1";
													      Ext.getCmp(othergrid).getStore().getProxy().extraParams.filter_EQS_scheduleReceiver='${loginUser.name}';
													      Ext.getCmp(othergrid).getStore().getProxy().extraParams.filter_EQI_scheduleSortId=othergrid.substring(14,othergrid.length);
													      Ext.getCmp(othergrid).getStore().load();
														return false;
													}
												})
											}); 
			                       	}
			                       }
		                        crud_data_form.getForm().reset();
		                        Ext.example.msg('消息', action.result.msg);
		                      }
		                    },
		                     failure:handFailure
				});
			}},{
				text:'完成',
				id:'schedulewc',
				handler:function(){
				var record = Ext.getCmp(gridid).getSelectionModel().getSelection();  
		 	  if (record.length) {
		 	  		Ext.MessageBox.confirm('提示', '确认完成任务',function(b,t){
		 	  			if( b == 'yes'){  
		 	  				  Ext.Ajax.request({
								url:'schedule!updateStatus.action',
								params: {id: record[0].get("id")},
								success:function(response){
										/*Ext.getCmp(gridid).getStore().setBaseParam("start",0);
						     			Ext.getCmp(gridid).getStore().setBaseParam("limit",100);
						     		    Ext.getCmp(gridid).getStore().setBaseParam("filter_EQS_scheduleStatus","1");
						     		    Ext.getCmp(gridid).getStore().setBaseParam("filter_EQS_receiverStatus",'1');
						    			Ext.getCmp(gridid).getStore().setBaseParam("filter_EQS_scheduleReceiver",'${loginUser.name}');
						    		    Ext.getCmp(gridid).getStore().setBaseParam("filter_EQI_scheduleSortId",gridid.substring(14,gridid.length));
						    			Ext.getCmp(gridid).getStore().load();*/
						    			  Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQS_scheduleStatus="1";
									      Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQS_receiverStatus="1";
									      Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQS_scheduleReceiver='${loginUser.name}';
									      Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQI_scheduleSortId=gridid.substring(14,gridid.length);
									      Ext.getCmp(gridid).getStore().load();
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
				var record = Ext.getCmp(gridid).getSelectionModel().getSelection();  
		 	  if (record.length) {
		 	  		Ext.MessageBox.confirm('提示', '确认驳回任务',function(b,t){
		 	  			if( b == 'yes'){  
		 	  				  Ext.Ajax.request({
								url:'schedule!receiveStatus.action',
								params: {id: record[0].get("id")},
								success:function(response){
									   /* Ext.getCmp(gridid).getStore().setBaseParam("start",0);
						     			Ext.getCmp(gridid).getStore().setBaseParam("limit",100);
						     		    Ext.getCmp(gridid).getStore().setBaseParam("filter_EQS_scheduleStatus","1");
						     		    Ext.getCmp(gridid).getStore().setBaseParam("filter_EQS_receiverStatus",'1');
						    			Ext.getCmp(gridid).getStore().setBaseParam("filter_EQS_scheduleReceiver",'${loginUser.name}');
						    		    Ext.getCmp(gridid).getStore().setBaseParam("filter_EQI_scheduleSortId",gridid.substring(14,gridid.length));
						    			Ext.getCmp(gridid).getStore().load();*/
						    			  Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQS_scheduleStatus="1";
									      Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQS_receiverStatus="1";
									      Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQS_scheduleReceiver='${loginUser.name}';
									      Ext.getCmp(gridid).getStore().getProxy().extraParams.filter_EQI_scheduleSortId=gridid.substring(14,gridid.length);
									      Ext.getCmp(gridid).getStore().load();
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
			var record = Ext.getCmp(gridid).getSelectionModel().getSelection();  
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
},{
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
var tempcao="";           
init = function(rankInfo) {  
var p=[];            
Ext.Ajax.request({
url: 'schedulesort!ssc.action',
method: 'post',
async: true, //同步请求
success: function(response) {
	p = Ext.decode(response.responseText);//获取所有分类
	var i = 0;
	 for (i = 0; i <rankInfo.length; i++) {
    	var j=0;
    	
       for ( j = 0; j < p.length; j++) { 
    	   p[j].tools=eval(p[j].tools);
       	 if (rankInfo[i].id == p[j].id) {
       	 	portal.items.get(0).items.get(rankInfo[i].col).add(p[j]);
       	 	//绑定关闭事件 
       	 	Ext.getCmp(p[j].id).addListener("beforeclose",function(){
       		 if(this.title=='其他'){
       			 Ext.example.msg('消息','不可删除'); 
       			 return false;
       			 } 
       			 Ext.Ajax.request({
       			 url:'schedulesort!deleteSort.action',
       			 params: {id:this.id},
       			 success:function(response){
       			  //Ext.example.msg('消息','已删除'); 
       			 savePosition();
       			 },
       			 failure:handFailure
       			 });
           	 })
       	 
       	 		//  待办事件. start
	
		/**------------------------存储Grid中的主数据--------------------------------*/
		
		
	
	
		/**------------------------存储Grid中的主数据--------------------------------*/
       	 	Ext.create('Ext.grid.Panel',{
				id : 'show_data_grid'+p[j].id,	
				height:200,
				store :new Ext.data.Store({ 
				pageSize:1000,
       			singleton : true, 
       			proxy: { 
                type: 'ajax', 
                url : 'schedule.action', 
                actionMethods : 'post', 
                reader: { 
                    type: 'json', 
                    totalProperty: 'totalCount', 
                    root: 'result' 
                } 
      	    }, 
		        fields:[{
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
							name:'scheduleLink'
						},{
							name:'scheduleContent'
						},{
							name:'scheduleTime'
						},{
							name:'earlyWarning'
						},{
							name:'priorityLevel'
						}]
		       // autoLoad:true 
   		 	}) ,	
		    columns : [{
		header : '任务标题',
		width:110,
		dataIndex : 'scheduleTitle'
	},{
		header : '优先级',
		width:80,
		dataIndex : 'priorityLevel'
	}, {
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
			});
			 		
				      Ext.getCmp('show_data_grid'+p[j].id).getStore().getProxy().extraParams.filter_EQS_scheduleStatus="0";
				      Ext.getCmp('show_data_grid'+p[j].id).getStore().getProxy().extraParams.filter_EQS_receiverStatus="1";
				      Ext.getCmp('show_data_grid'+p[j].id).getStore().getProxy().extraParams.filter_EQS_scheduleReceiver='${loginUser.name}';
				      Ext.getCmp('show_data_grid'+p[j].id).getStore().getProxy().extraParams.filter_EQI_scheduleSortId=p[j].id;
				      Ext.getCmp('show_data_grid'+p[j].id).getStore().load();
		var pid=p[j].id;
       	 Ext.getCmp(pid).add(Ext.getCmp('show_data_grid'+p[j].id));	
       	 	break;  
       	 }
       }
       
     }  
     portal.doLayout();  
    
}
}); 
   
   }      
           //查找是否有“其他”待办（没有则添加）
             Ext.Ajax.request({
						url:'schedulesort!checkScheduleSort.action',
						async: true, //同步请求
						success:function(response){
							  //加载待办
					            Ext.Ajax.request({
											url:'evercookies!readCookie.action',
											params: { cookieName:"schedulesortcookie"},
											async: true, //同步请求
											success:function(response){
												var schedulesortcookie=[{"id" : "1","col" : 0}];
												if(response.responseText!=0){
													schedulesortcookie=eval(response.responseText);
												}
												
												init(schedulesortcookie)
											},
											failure:function(){
											Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
											}
										});
						},
						failure:function(){
						Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
						}
					});  
         
  
        });  


</script>
  </head>
  
  <body>
    
  </body>
</html>
