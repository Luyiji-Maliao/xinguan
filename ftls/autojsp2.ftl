<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="des">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<jsp:include page="/include/header4.jsp"></jsp:include>	
		
<script type="text/javascript">
	
Ext.onReady(function() {
    Ext.QuickTips.init();
    var pageSize = 3;
    var returnStart=0;
    /**------------------------存储Grid中的主数据--------------------------------*/
    Ext.define('mainPage', {
     extend: 'Ext.data.Model',
     fields: [
		<#list lac as item>
		{
			name : '${item.colName}' 
		}, 
		</#list>
		
        		{
			name : '${ab.beannametoLower}id' 
		}
     ]
 });

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:pageSize,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
         actionMethods: {
                read   : 'POST', // by default GET
            },
         url: '${ab.beannametoLower}.action',
         reader: {
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
     
 });
  
	
	
	
	main_store.on("beforeload",function(store,options){
		var searchField = Ext.getCmp("searchField");
		if(searchField){
			var new_params={filter_LIKES_${ab.searchfield}:searchField.getValue()};			
			Ext.apply(store.proxy.extraParams, new_params);  
		}

		//多条件
		if(Ext.getCmp("search_form")){
		
			<#list lac as item>
			if(Ext.getCmp("${item.colsearch}")){
				Ext.apply(store.proxy.extraParams,{
					filter_EQS_${item.colName}:Ext.getCmp("${item.colsearch}").getRawValue()
				});
				}
			</#list>	
			
		} 		

	});
		
		 main_store.loadPage(1);
	
	
	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid = Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : '${ab.beanname}',
		width : 400,
		region:'center',
		loadMask : true,
		stripeRows : true,
		frame : true,
		store : main_store,
		selModel : {
				    selType : 'checkboxmodel',
				    mode : 'SIMPLE'
				},
		viewConfig:{  
		   enableTextSelection:true  //可复制内容
		} ,
		   listeners:{  
					//双击  
			       itemdblclick:function(grid,row){  
			           //alert('rowclick')  ;
			       }  
   		} ,
		columns : [
		<#list lac as item>
		 	<#if !item_has_next>
				 {
				 header : '${item.colHeader}',
				 dataIndex : '${item.colName}'
				 }
			<#else>
				 {
				 header : '${item.colHeader}',
				 dataIndex : '${item.colName}'
				 }, 
			</#if>
		
		</#list>
		
		],
		
		bbar : new Ext.PagingToolbar( {
			store :main_store,
			displayInfo : true,
			pageSize : pageSize,
			listeners : {  
                    "change" : function(bbar, p){  
                      	if(p!= undefined){  //没有数据时 p is undefined
                      		returnStart=(p.currentPage-1)*pageSize
                      	}
                    }  
                }
		}),
		tbar : [ 
		{
			text : '查看',
			id:'tbar0',
			hidden:true,
			icon : 'img/iconfont/chakan.png',
			handler : function() {
			Ext.getCmp("subbut").hide();
			var records = grid.getSelectionModel().getSelection();
			if (records.length == 1) {
				 Ext.getCmp("formurl").setValue("");
				 crud_data_window.show();
				 crud_data_form.getForm().loadRecord(records[0]);
			}else{
				Ext.example.msg("提示", "请选择一项");
				}
			}
		},
		{
			text : '添加',
			id:'tbar1',
			hidden:true,
			icon : 'img/iconfont/tianjia.png',
			handler : function() {
			Ext.getCmp("subbut").show();
			crud_data_form.getForm().reset();
			Ext.getCmp("formurl").setValue("${ab.beannametoLower}!save.action");
			crud_data_window.show();
			}
		},
		{
			text : '修改',
			id:'tbar2',
			hidden:true,
			icon : 'img/iconfont/xiugai.png',
			handler : function() {
			Ext.getCmp("subbut").show();
			var records = grid.getSelectionModel().getSelection();
			if (records.length == 1) {
				 Ext.getCmp("formurl").setValue("${ab.beannametoLower}!update.action");
				 crud_data_window.show();
				 crud_data_form.getForm().loadRecord(records[0]);
			}else{
				Ext.example.msg("提示", "请选择一项");
				}
			}
		},
		{
			text : '删除',
			id:'tbar3',
			hidden:true,
			icon : 'img/iconfont/shanchu.png',
			handler : function() {
			var records = grid.getSelectionModel().getSelection();
			if (records.length == 1) {
				Ext.Msg.confirm('提示', '确认删除数据？',function (btn) {
					if (btn == 'yes') {
						Ext.Ajax.request(
								 { 
								 url: '${ab.beannametoLower}!delete.action',  
								 params: {'${ab.beannametoLower}id': records[0].get('${ab.beannametoLower}id')},
								 success: function (response) {
								 			  main_store.load({
										        params: {
						                        	start:returnStart,
						                        	limit:pageSize
								                       	}
								                       });
											 Ext.example.msg("提示",response.responseText,3000); 
										 	
								 },
								 failure: handFailure,
								 });
						}
				});
			}else{
				Ext.example.msg("提示", "请选择一项");
				}
			}
		},
		,{
			text : '高级搜索',
			id:'tbar4',
			hidden:true,
			icon : 'img/iconfont/gaojisousuo.png',
			handler : function() {
				searchWindow.show();
			}
		},{
			text : '导出',
			id:'tbar5',
			hidden:true,
			icon : 'img/iconfont/daochu.png',
			handler : function() {
				    var xjyb_jsonArray = [];
					var record =grid.getSelectionModel().getSelection();
					for(var i=0;i<record.length;i++){
				  		xjyb_jsonArray.push(record[i].data);
				  	}
					if(xjyb_jsonArray.length>0){//选择导出
						Ext.Ajax.request(
							 { 
							 url: '${ab.beannametoLower}!createExcel.action',  
							 params: {'iteminfo':Ext.encode(xjyb_jsonArray)},
							 success: function (response) {
								    window.location.href = "${ab.beannametoLower}!exportExcel.action";
								    Ext.example.msg("提示", "导出完成");
							 },
							 failure: handFailure,
	
							 });
					}else{//条件导出
						if(Ext.getCmp("search_form")){
							Ext.getCmp("search_form").getForm().submit({
									url: '${ab.beannametoLower}!createExcel.action',
				                    success: function(f, action) {
				                     if (action.result.msg=="有数据") {
				                        window.location.href = "${ab.beannametoLower}!exportExcel.action";
										Ext.example.msg("提示", "导出完成");
				                      }else{
				                      	Ext.example.msg("提示", "无数据");
				                      }
				                    },
				                     failure:handFailure
							});
						}else{
							alert("请检索后导出");
						}
					}
					
			}
		},
		{
			id:'searchField',
			xtype:'textfield',
			width:200,
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
/**	---------------------------CRUD START------------------------------------*/
crud_data_form= new Ext.form.FormPanel({
	id: 'data_form',
	labelAlign : 'right',
	buttonAlign: 'center',
	border:false,	
	readOnly:true,
	bodyStyle:'overflow-x:auto;overflow-y:auto;',
	width : 600,
	height:130,
	fieldDefaults : {
		labelAlign : 'right',
		labelWidth : 100,
		anchor : '100%',
		bodyStyle : 'border-width:0 0 0 0;',
	},
	buttons : [ {
		text : '提交',
		id:'subbut',
		tabIndex: 3, 
		handler : function() {
		crud_data_form.getForm().submit({
			url:Ext.getCmp("formurl").getValue(),
			waitMsg : '正在提交数据',
               success: function(f, action) {
                if (action.result.success) {
					 main_store.load({
										        params: {
						                        	start:returnStart,
						                        	limit:pageSize
								                       	}
								                       });
                    crud_data_window.hide();
                 }
               },
                failure:handFailure
		});
		}
	}],
		items: [
	{
            layout:'column',
            border:false,
            items:[
		<#list cud as autob>	
		{
                columnWidth:${ab.extcolumnWidth},
                border:false,
                layout: 'form',
                defaults:{
            		xtype:'textfield',
            		anchor:'95%'
                },
                items: [
		<#list autob.extcol as ec>
            		<#if ec.extcolType=="datefield">
			   {
		                    fieldLabel: '${ec.colHeader}',
		                    xtype:'datefield',
		                    format: 'Y-m-d',
		                    id:'${ec.colName}',
		        		  name:'${ec.colName}'
		        			
			  },
			<#else>
			 	 {
					fieldLabel:'${ec.colHeader}',
					name:'${ec.colName}',
					xtype:'${ec.extcolType}',
					id:'${ec.colName}'
         	            	 },
			</#if>	
	     </#list> 
             ]
            
	},
	</#list>
]}
		,{
        fieldLabel: 'formurl',
		xtype: 'hidden',
		id:'formurl'
		}
		,{
			fieldLabel: 'inputtime',
			xtype: 'hidden',
			id:'inputtime',
			name:'inputtime'
		},{
			fieldLabel: 'inputname',
			xtype: 'hidden',
			id:'inputname',
			name:'inputname'
		},{
			fieldLabel: 'updatetime',
			xtype: 'hidden',
			id:'updatetime',
			name:'updatetime'
		},{
			fieldLabel: 'updatename',
			xtype: 'hidden',
			id:'updatename',
			name:'updatename'
		},
		{
		fieldLabel: 'id',
		xtype: 'hidden',
		id:'${ab.beannametoLower}id',
		name:'${ab.beannametoLower}id'
		}
	]
});

var crud_data_window = Ext.create('Ext.Window', {
	id : "crud_data_window",
	title : '${ab.javabean}',
	closeAction : 'hide',
	items : crud_data_form,
	listeners: {
		hide: function(){
			
		}
	}
});
/**	---------------------------CRUD END------------------------------------*/
/**--------------------Search START-----------------------**/
searchForm=	 new Ext.form.FormPanel({
	id:'search_form',
	labelAlign : 'right',
	autoHeight : true,
	buttonAlign: 'center',
	autoScroll : true,
	fieldDefaults : {
		labelAlign : 'right',
		labelWidth : 100,
		anchor : '100%',
		bodyStyle : 'border-width:0 0 0 0;',
	},
	buttons : [ {
						text:'查询',
						handler:function(){
				        	main_store.load({
				    			params:{
				    			start:0,
				    			limit:pageSize,
				    		}
				    		});
							searchWindow.hide();				
						}
					},{
						text:'取消',
						handler:function(){
						 searchWindow.hide();
						}
						},{
						text:'重置',
						handler:function(){
						 searchForm.form.reset();
						}
						}],
		items: [
									
								{
            layout:'column',
            border:false,
            items:[
		<#list ll as autob>	
		{
                columnWidth:${ab.extcolumnWidth},
                border:false,
                layout: 'form',
                defaults:{
            		xtype:'textfield',
            		anchor:'95%'
                },
                items: [
		<#list autob.extcol as ec>
                {
					fieldLabel:'${ec.colHeader}',
					name:'${ec.colName}',
					id:'${ec.colsearch}'
                },
	     </#list> 
             ]
            
	},
	</#list>
]}
		]
	
});
var searchWindow = new Ext.Window({
				title:'高级搜索',
				id:'searchwindow',
				closeAction: 'hide',
				width:600,
				autoScroll:true,
				modal:true,
				items:searchForm
			});
/**--------------------Search END-----------------------**/
//用户页面权限
if(Ext.getCmp("show_data_grid")){
	roleHaveRight();
}

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
