<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<title>角色管理</title>
		<jsp:include page="/include/header4.jsp"></jsp:include>
		<script type="text/javascript" src="js/TreeUtil.js"> </script>
		<script type="text/javascript">

		
		
Ext.onReady(function(){
	Ext.QuickTips.init();
	var pageSize = 20;
   Ext.define('mainPage', {
     extend: 'Ext.data.Model',
     idProperty : 'xx',  //不加此属性时，默认 idProperty : 'id' ，修改后获取到的数据还是原来的 （注意实体id命名）
     fields: [
       {
			name : 'id'
		}, {
			name : 'roleName'
		}, {
			name : 'description'
		},{
			name:'isSysNeeded'
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
         url: 'role.action',
         reader: {
            // type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
     //autoLoad: true
 });
  // main_store.loadPage(1);
	
	
	
	main_store.on("beforeload",function(store,options){
		var searchField = Ext.getCmp("searchField");
		if(searchField){
			var new_params={filter_LIKES_roleName:searchField.getValue()};			
			Ext.apply(store.proxy.extraParams, new_params);  
		}
		var rn='${rolename}';
		if(rn!=null&&rn!=""){
			Ext.apply(store.proxy.extraParams, {filter_LIKES_roleName:rn});  
		}
		
		});
	main_store.loadPage(1);

	
/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid = Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : '显示角色',
		width : 600,
		region:'center',
		loadMask : true,
		stripeRows : true,
		frame : true,
		store : main_store,
		viewConfig : {
			forceFit : true
		},
		columns : [{
		header : '编号',
		width : 50,
		sortable : true,
		dataIndex : 'id'
	}, {
		header : '角色名称',
		width:150,
		dataIndex : 'roleName'
	}, {
		header : '角色描述',
		width : 400,
		dataIndex : 'description'
	},{
		header:'查看权限',
		align:'center',
        xtype: 'actioncolumn',
        width: 80,
        items: [ {
                icon   : 'img/users.png',  
                altText:'xxxxx',
                align:'center',
               // tooltip: '角色人员',
                handler: function(grid, rowIndex, colIndex) {
	                 var rec = main_store.getAt(rowIndex);
	                 Ext.Ajax.request({
						url:'role!roleuser.action',
						params: { id: rec.get("id"),},
						success:function(response){
							var resultData = Ext.decode(response.responseText);
							var myModel = Ext.define("rights", {  
					            extend : "Ext.data.Model",  
					            fields : [ {  
					                type : "string",  
					                name : "moduleNames"  
					            }, {  
					                type : "string",  
					                name : "rights"  
					            }]  
					        });  
						  var myStore = Ext.create("Ext.data.Store", {
						      model: "rights"
						 });
							for(var i=0;i<resultData.moduleNames.split("*").length;i++){
								if(resultData.jspPages.split("*")[i]!=''){
									myStore.add({moduleNames: resultData.moduleNames.split("*")[i],rights: resultData.rights.split("*")[i]});  
								}
							}
							  var myModel2 = Ext.define("names", {  
							            extend : "Ext.data.Model",  
							            fields : [ {  
							                type : "string",  
							                name : "names"  
							            }]  
							        });  
							  var myStore2 = Ext.create("Ext.data.Store", {
							      model: "names"
							 });
						
							myStore2.add({names: resultData.names});  

							// 表格
							var ItemGrid = new Ext.grid.Panel({
								columns : [{
									text : "模块",
									width:350,
									dataIndex : "moduleNames"
								}, {
									text : "权限",
									width:790,
									dataIndex : "rights"
								}],
								//forceFit: false,
						        height: 320,
						        autoScroll: true,
						       // frame: true,
						        split: true,
						       // layout: "fit",
						        //height:document.documentElement.clientHeight,
						        margin: 0,
						        store: myStore,
						        columnLines: true,
						        viewConfig: {
						            stripeRows: true,//在表格中显示斑马线
						            enableTextSelection: true //可以复制单元格文字 "GGXH", "XMSL", "XMDJ", "XMJE", "SL", "SE", "SPBM", "TaxItem"],
						        }
							});


							// 表格2
							var NameGrid = new Ext.grid.Panel({
								columns : [  {
									text : "拥有该角色的人员：",
									width:1150,
									dataIndex : "names"
								}],
								//forceFit: false,
						        height: 120,
						        //autoScroll: true,
						       // frame: true,
						        split: true,
						       // layout: "fit",
						       // height:document.documentElement.clientHeight,
						        margin: 0,
						        store: myStore2,
						        columnLines: true,
						        viewConfig: {
						            stripeRows: true,//在表格中显示斑马线
						            enableTextSelection: true //可以复制单元格文字 "GGXH", "XMSL", "XMDJ", "XMJE", "SL", "SE", "SPBM", "TaxItem"],
						        }
							});
							

							var DataFrom = Ext.create('Ext.form.Panel', {
						        hidden: true,
						        bodyPadding: 0,
						        //width: 1500,
						       // header: true,
						        //height: 800,
						       // autoScroll: true,
						        defaults: {
						            anchor: '100%'
						        },
						        defaultType: 'textfield',
						        items: [ItemGrid,NameGrid],
						        buttons: [{
						            text: '关闭',
						            handler: function () {
						            	new_windows.close();
						            }
						        }]
						    });
							
							// 窗口
							var new_windows = Ext.create("Ext.window.Window", {
								title : "角色权限表",
								width : 1150,
								height : 500,
								closable: true,
						       // closeAction: 'hide',
						        modal: true,
						        frame: true,
						        layout: {
						            type: 'border',
						            padding: 0
						        },
						        items: [{
						            region: 'center',
						            xtype: 'tabpanel',
						            items: DataFrom
						        }]
							});

							new_windows.show();
						},
						failure:function(){
							Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
						}
				});
             }
         }]
	},{
		header:'设置权限',
		align:'center',
        xtype: 'actioncolumn',
        width: 80,
        items: [ {
                icon   : 'img/users.png',  
                altText:'xxxxx',
                align:'center',
                tooltip: '分配权限',
                handler: function(grid, rowIndex, colIndex) {
                	 var  role_right=[];		//保存角色权限
                    var rec = main_store.getAt(rowIndex);
                    if(rec.get('roleName')=='超级管理'){
                    Ext.example.msg("提示","超级管理员不可操作");                   
                    	return ;
                    }
                   // alert("rec.get(id)"+rec.get("id"));
                    Ext.Ajax.request({
						scope: this,
						//url:'role!listRoleRights.action',  //返回一级节点（父节点）
						url:'role!listRoleRightsByUser.action',  //返回一级节点（父节点）
						params: { id: rec.get("id") },
						success:function(response,options){
							var menudata = eval(response.responseText);  //一级节点信息 
							var roleRightsWindow = Ext.create('Ext.window.Window',{
								title:'设置权限',
								//closeAction:'hide',
								autoScroll:true,
								width:900,
								modal:true,
								height:500,
								constrain:true,
								layout:'column',
								draggable:true,
								defaults: {
									height:200,
									width:215
							    },
							    layoutConfig: {
							        columns: 4
							    },
							    listeners:{
							    	beforeshow:function(){
							    		
							    	}
							    },
							    buttons:[{
							    	text:'设置权限',
							    	handler:function(){
							    		var items = roleRightsWindow.items;
							    		var menuids = [];
							    		items.each(function(item){
							    			var selectedNodes = TreeUtil.getSelected(item.getRootNode());
							    			for(var i = 0; i < selectedNodes.length ;i++){
							    				var node = selectedNodes[i];
							    				//alert("node.id:"+node.data.id);
							    				menuids.push(node.data.id);
							    			}
							    			
							    		});
							    		
							    		Ext.Ajax.request({
							    			url:'role!update.action',
							    			params: { id: rec.get("id"),rightsIds:menuids.join(","),roleright:role_right },
							    			success:function(response){
							    				var obj = Ext.decode(response.responseText);
							    				Ext.example.msg("提示",obj.msg);
							    				roleRightsWindow.close();
							    			},
							    			failure:function(){
							    				Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
							    			}
							    		});
							    	}
							    },{
							    	text:'取消',
							    	handler:function(){
							    		roleRightsWindow.close();
							    	}
							    }]
							});
							for(var i=0;i<menudata.length;i++){
								 var treeStore = Ext.create('Ext.data.TreeStore', {
					                proxy: {
					                    type: 'ajax',
					                    url: 'role!listRoleRightsByUser.action?id='+rec.get("id")
					                },
					                fields: ['text', 'id', 'leaf', 'checked','roleright','right_web_num']
					            });
					           // alert("menudata[i].id"+menudata[i].id);
								var tree = Ext.create('Ext.tree.Panel',{
									id : menudata[i].id,
									frame:true,
									store:treeStore,
									title : menudata[i].text,
									iconCls:'titleIcon',
									border : false,
									autoScroll : true,
									//dataUrl: 'role!listRoleRightsByUser.action?id='+rec.get("id"),
								    root: {
								        nodeType: 'async',
								        text: menudata[i].text,
								        draggable: false,
								        checked:menudata[i].checked,
								        expanded:true,
								        id: menudata[i].id
								    },
									
									listeners:{  
							            "checkchange": function(node,checked){  
							            	
							                TreeUtil.cascadeCheck(node,checked);  
							            },
							              "itemdblclick": function(m,n) {
							              		if(n.data.leaf){  //是否为叶子
							              		var items = roleRightsWindow.items;
							    				var menus = [];
							    				items.each(function(item){
							    				//alert();
							    				var selectedNodes = TreeUtil.getSelected(item.getRootNode());
							    				//alert("selectedNodes.length:"+selectedNodes.length);
							    				for(var i = 0; i < selectedNodes.length ;i++){
							    					var node = selectedNodes[i];
							    					menus.push(node.data.id);
							    				}
							    			    });
							    			    var i=0;
							              	    for( i = 0; i < menus.length;i++){
							              		    if(menus[i] == n.data.id){//判断当前叶子是否被选中 
							              		    var	roleright = [];
							              			var items = [];
							              		var right_web_num=	n.data.right_web_num.split(",");
							              			for(var i=0;i< right_web_num.length;i++){
							              			//alert('roleright'+i);
							              			items.push({boxLabel:right_web_num[i],name:'roleright'+i,inputValue:i});
							              		}
								            	if(n.data.roleright!=null){ //是否已选择一个或多个操作权限
								             	    roleright=n.data.roleright.split(",");
									          	    for(var i=0;i<roleright.length;i++){
									                items.splice(roleright[i],1,{boxLabel:right_web_num[roleright[i]],name:'roleright'+roleright[i],inputValue:roleright[i],checked: true});
									       	    	}
								            	}
								            	var rolerightCheckgroup = Ext.create('Ext.form.CheckboxGroup',{
					          						columns: 4,
					           				   	    items:items
					        					});
								              	
								              	
					       					
					        					var rolerightForm = Ext.create('Ext.form.Panel',{
													width:400,
													labelWidth:10,
													buttonAlign:'center',
													items:[
														{
															xtype:'hidden',
															name:'rightid',
															value:n.data.id
														},rolerightCheckgroup
													],
													buttons:[
													{
														text:'保存',
														handler:function(){
																
																var  rolerightcheckgroup=rolerightCheckgroup.getChecked();	
																
																var rolerighting = "";
																for (var i = 0;i < rolerightcheckgroup.length;i++) {
																	//alert("rolerightcheckgroup[i].name:"+rolerightcheckgroup[i].inputValue);
																	//alert(rolerightcheckgroup[i].name.substr(9));
																	//rolerighting += rolerightcheckgroup[i].name.substr(rolerightcheckgroup[i].name.length-1,1)+",";
																	rolerighting +=rolerightcheckgroup[i].name.substr(9)+",";
																}
																
																
																for(var i = 0; i<role_right.length;i++){
																	
																	if(role_right[i].substring(0,role_right[i].indexOf(","))==n.data.id){
																			role_right.splice(i,1);
																	}
																}
																role_right.push(n.data.id+","+rolerighting.substr(0,rolerighting.length-1));
															rolerightWindow.hide();
														}
													},{
														text:'取消',
														handler:function(){
															rolerightWindow.hide();
														}
													}
													]
												});
							
												var rolerightWindow = Ext.create('Ext.window.Window',{
				                    			title:'角色权限控制',
				                    			modal:true,
				                    			closeAction : 'hide',
				                    			items:rolerightForm
				                    			});
							
												rolerightWindow.show();
							      			    break;
							              		}
							              	}	
							              	if(menus.length==i){//未选中
							              		 Ext.example.msg('提醒', '请先选择权限，再设置当前操作权限');
							              	}
							              }                								
            						 }
							             
							        }
								});
								tree.expandAll();
								roleRightsWindow.add(tree);
							}
							roleRightsWindow.show();
						}
					});
                }
         }]
     }],
		//selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
		listeners:{  
			itemdblclick : function(grid,row){  
						Ext.getCmp("subbut").hide();
						var record = grid.getSelectionModel().getSelection();  
					 	  if (record.length) {
						      update_data_window.show();
						 	  update_data_form.getForm().loadRecord(record[0]);
						  }else{
							  Ext.example.msg("提示","请选择要查看的数据");
							  }
		       }
	 
		  } , 
		tbar : [{
			text : '查看',
			id:'tbar0',
			hidden:true,
			icon : 'img/show.png',
			handler : function() {
				/*Ext.each(update_data_form.getForm().getFields().items, function(field){
	                field.setValue(''); //重置 
	        	 });*/
	        	 update_data_form.getForm().reset();
	        	 update_data_window.setTitle("查看角色");
				Ext.getCmp("subbut").hide();
				var record = grid.getSelectionModel().getSelection();  
			 	  if (record.length) {
				      update_data_window.show();
				 	  update_data_form.getForm().loadRecord(record[0]);
				  }else{
					 Ext.example.msg("提示","请选择要查看的数据");
					  }
			}
		}, {
			text : '添加',
			id:'tbar1',
			hidden:true,
			icon : 'img/add.png',
			handler : function() {
			update_data_window.setTitle("添加角色");
				Ext.each(update_data_form.getForm().getFields().items, function(field){
	                field.setValue(''); //重置 
	        	 });
				Ext.getCmp("saveorupdate").setValue("添加");
				Ext.getCmp("subbut").show();
				update_data_window.show();
			}
		},{
			text : '修改',
			id:'tbar2',
			hidden:true,
			icon : 'img/update.png',
			handler : function() {
			update_data_window.setTitle("修改角色");
			var record = grid.getSelectionModel().getSelection();  
			  if (record.length) {
				  if(record[0].get("isSysNeeded")==1){
		    		    Ext.getCmp("saveorupdate").setValue("修改");
				  		Ext.getCmp("subbut").show();
				  		update_data_window.show();
				    	update_data_form.getForm().loadRecord(record[0]);
				  }else{
				  	    Ext.example.msg("提示","系统定义角色，不可更改");
				  }
			   }else{
		    	    Ext.example.msg("提示","请选择要修改的数据");
		       }
			}
		},{
			text : '删除',
			id:'tbar3',
			hidden:true,
			icon : 'img/delete2.gif',
			handler : function() {
			var record = grid.getSelectionModel().getSelection();  
			  if (record.length) {
			  Ext.Ajax.request({
					url:'role!delete.action',
					params: { id: record[0].get("id")},
					success:function(response){
							if(response.responseText==0){
								Ext.Msg.alert('提示',"已删除");
								main_store.loadPage(1);
							}else{
								Ext.Msg.alert('提示',"该角色有人员，无法删除");
							}
							
						},
					failure:function(){
							Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
					}
				 });
				 }else{
				 	Ext.example.msg("提示","请选择一条数据");
				 }
			}
		},'-',{
			id:'searchField',
			xtype:'textfield',
			width:200,
			emptyText:'请输入要查找角色名称',
			value:'${rolename}',
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
			store :main_store,
			displayInfo : true,
			pageSize : pageSize
		})
		
	});
	
/**---------------------------结束显示数据的Grid------------------------------------*/	
	//用户页面权限
if(Ext.getCmp("show_data_grid")){
	roleHaveRight();
}
	

 /**--------------------------------修改数据Form-------------------------------------*/
	
	var update_data_form = Ext.create('Ext.form.Panel', {
			id : 'update_data_form',
			//labelWidth : 60,
			//labelAlign : 'right',
			//trackResetOnLoad:true,
			buttonAlign : 'center',
			//frame : true,
			url : 'role!save.action',
			autoHeight : true,
			fieldDefaults: {
		        labelAlign: 'right',
		        labelWidth:70
		       
		    },
			defaults : {
				xtype : 'textfield',
				width : 200
			},
			width : 285,
			items : [{
					xtype:'hidden',
					name:'id'
				},{
					xtype:'hidden',
					id:'saveorupdate',
					name:'saveorupdate'
				},{
				fieldLabel : '<font color="red">*</font>角色名称',
				anchor:'95%',
				name : 'roleName',
				allowBlank:false
			}, {
				fieldLabel : '角色描述',
				anchor:'95%',
				xtype : 'textarea',
				name : 'description'
			}],
			buttons : [ {
				text : '提交',
				id:'subbut',
				handler : function() {
					update_data_form.getForm().submit({
		                    success: function(f, action) {
		                     if (action.result.success) {
		                       	update_data_window.hide();
		                        Ext.getCmp("searchField").setValue("");
		                        grid.getStore().reload();
		                        update_data_form.getForm().reset();
		                        Ext.example.msg('消息', action.result.msg);
		                      }
		                    },
		                     failure:handFailure
				});
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
		title : '修改角色',
		closeAction : 'hide',
		modal : true,
		items : update_data_form
	});
 
 
 /**--------------------------------结束修改数据Window-------------------------------------------*/
 
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
