<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>职位管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="职位管理">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<jsp:include page="/include/header4.jsp"></jsp:include>	
<script type="text/javascript">
	
Ext.onReady(function() {
    Ext.QuickTips.init();
    var pageSize = 23;
	//gridColumnstate();
    /**------------------------存储Grid中的主数据--------------------------------*/
     
    Ext.define('mainPage', {
     extend: 'Ext.data.Model',
     idProperty : 'xx',  //不加此属性时，默认 idProperty : 'id' ，修改后获取到的数据还是原来的 （注意实体id命名）
     fields: [
       {
			name : 'id' 
		}, {
			name : 'posName'
		}, {
			name : 'description'
		},{
			name:'department.id'
		},{
			name:'department.deptName'
		}
     ]
 });

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:23,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
       	 actionMethods : 'post', 
         url: 'position.action',
         reader: {
            // type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
 });
  
	
	
	
	main_store.on("beforeload",function(store,options){
		var searchField = Ext.getCmp("searchField");
		if(searchField){
			var new_params={filter_LIKES_posName:searchField.getValue()};			
			Ext.apply(store.proxy.extraParams, new_params);  
		}
		//Ext.apply(store.proxy.extraParams, {filter_LIKES_department00deptName_OR_department00description:'IT'}); 
		
		});
		
		 main_store.loadPage(1);
	
	
	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid =Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : '职位管理',
		width : 600,
		region:'center',
		loadMask : true,
		stripeRows : true,
		frame : true,
		store : main_store,
		columns:[{
			header : '编号', 
			width : 50,
			sortable : true,
			dataIndex : 'id'
		},{
			header : '职位',
			dataIndex : 'posName'
		}, {
			header : '描述',
			dataIndex : 'description'
		},{
			header:'部门',
			dataIndex:'department.deptName'
		}],
		listeners:{  
		       itemdblclick : function(grid,row){  
					var record = grid.getSelectionModel().getLastSelected(); 
				  if (record) {
				  	  crud_data_form.getForm().reset();
					  Ext.getCmp('saveorupdate').setValue("修改");
					  Ext.getCmp('deptId').setValue(record.get("department.id"));
					  crud_data_window.show();
					  crud_data_form.getForm().loadRecord(record);
					  crud_department_combox.setValue(record.get("department.id"));
				  } else {
				 	  Ext.example.msg("提示","请选择要修改的数据");
				  }
		       }
	 
		  } , 
		tbar : [ 
		{
			text : '添加',
			id:'tbar0',
			hidden:true,
			icon : 'img/add.png',
			handler : function() {
				
				crud_data_form.getForm().reset();
			    Ext.getCmp('saveorupdate').setValue("添加");				
				Ext.getCmp('id').setValue();
				crud_data_window.show();
			}
		}, 
		{
			text : '修改',
			hidden:true,
			id:'tbar1', 
			icon : 'img/update.png',
			handler : function() {
				var record = grid.getSelectionModel().getSelection(); 
				  if (record.length) {
					  crud_data_form.getForm().reset();
					  Ext.getCmp('saveorupdate').setValue("修改");
					  crud_department_combox.setValue(record[0].get("department.id"));
					  Ext.getCmp('deptId').setValue(record[0].get("department.id"));
					  crud_data_window.show();
					  crud_data_form.getForm().loadRecord(record[0]);
				  } else {
				 	  Ext.example.msg("提示","请选择要修改的数据");
				  }
			}
		},{
			id:'searchField',
			xtype:'textfield',
			width:200,
			emptyText:'请输入要查找的职位',
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
		}],
		bbar : new Ext.PagingToolbar( {
			store : main_store,
			displayInfo : true,
			pageSize : pageSize
		})
	});
	
/**---------------------------结束显示数据的Grid------------------------------------*/	
	
/**--------------------------------CRUD Form -------------------------------------*/
	/**
	 department(部门) 
	 */
	 
	
	var crud_department_combox =Ext.create('Ext.form.ComboBox',{
		    typeAhead: false,
		   	lazyRender:true,
		   	name:'departmentId',
		  // multiSelect:true,  //多选
		   	fieldLabel:'<font color="red">*</font>所属部门',
		     queryMode:'local',  //这个需要加上。。
		    store: new Ext.data.Store({ 
       			 singleton : true, 
       			 pageSize:100,
       			 proxy: { 
                type: 'ajax', 
                url : 'department.action', 
                actionMethods : 'post', 
                reader: { 
                    type: 'json', 
                    totalProperty: 'totalCount', 
                    root: 'result' 
                } 
      	    }, 
		        fields:['id', 'deptName'], 
		        autoLoad:true 
   		 	}),
		    valueField: 'id',
		    displayField: 'deptName',
	         emptyText:'请选择',  
		    editable:false,
		    allowBlank:false,
		    listeners:{
				'select':function(){
				Ext.getCmp('deptId').setValue(this.value);   //获取id为combo的值
				}
			}
		});	
		
	var crud_data_form = new Ext.form.FormPanel( {
			id : 'crud_data_form',
			fieldDefaults: {
		        labelAlign: 'right',
		        anchor:'95%',
		        labelWidth:75
		    },
			buttonAlign : 'center',
			url : 'position!save.action',
			autoHeight : true,
			defaults : {
				xtype : 'textfield',
				
			},
			width : 285,
			items : [{
					xtype:'hidden',
					name:'id',
					id:'id'
				},{
					xtype:'hidden',
					id:'saveorupdate',
					name:'saveorupdate'
				},{
					xtype:'hidden',
					id:'deptId',
					name:'deptId'
				},{
				fieldLabel : '<font color="red">*</font>职位',
				name : 'posName',
				allowBlank:false
			},
			 crud_department_combox,
			{
				fieldLabel : '描述',
				xtype : 'textarea',
				name : 'description'
			}],
			buttons : [ {
				text : '提交',
				handler : function() {
					crud_data_form.getForm().submit({
		                    success: function(f, action) {
		                     if (action.result.success) {
		                       	crud_data_window.hide();
		                       Ext.getCmp("searchField").setValue("");
		                       grid.getStore().reload();   
		                        //main_store.loadPage(1);
		                       	crud_data_form.getForm().reset();
		                        Ext.example.msg('消息', action.result.msg);
		                      }
		                    },
		                     failure:handFailure
				});
			}}, {
				text : '取消',
				handler:function(){
					crud_data_window.hide();
				}
			} ]
		});
/**--------------------------------CRUD Form -------------------------------------*/
 /**--------------------------------CRUD Window---------------------------------------------*/

 	var crud_data_window = Ext.create('Ext.window.Window', {
		id : "crud_data_window",
		title : '职位管理',
		closeAction : 'hide',
		modal : true,
		items : crud_data_form
	});
 
 
 /**--------------------------------CRUD Window-------------------------------------------*/	
	
//用户页面权限
if(Ext.getCmp("show_data_grid")){
	roleHaveRight();
}

var view = Ext.create('Ext.Viewport',{
		layout:'border',
		items:grid
	});
	
    });
   </script>
  </head>
  
  <body>
  </body>
</html>
