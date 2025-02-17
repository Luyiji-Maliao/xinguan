
//具体使用请看/WEB-INF/content/tumor/chemicaldrugdanxianglib.jsp.
//jsp里面有详细使用方法
function createJsp(alldatas){
	var alldata  = eval(alldatas);  
	var title = alldata.title;				//标题
	var actionname =  alldata.actionname;	//url路径
	
	var fields =  alldata.fields;           //字段
	var tbars =  alldata.tbars;				//按钮

	var datas=[];
    var  l=fields.length;
    for(var i=0;i<l;i++){      	
    	var data = {};
    	data["name"] =fields[i]['dataIndex'];
    	datas.push(data);
    }
    
    //修改，查看窗口,高级搜索窗口(更新人，更新时间不用显示）
    var cruddatas1=[];
    var cruddatas2=[];
    var searchdatas1=[];
    var searchdatas2=[];
    var ls = (l-2)/2;
    
    var searchcontent=[];
    for(var i=0;i<l-2;i++){  
    	var data = {};
    	data["name"] =fields[i]['dataIndex'];
    	data["id"] = fields[i]['dataIndex'];
    	data["fieldLabel"] = fields[i]['header'];
    	
    	var data1={};
    	data1["id"] = "s_"+fields[i]['dataIndex'];
    	data1["fieldLabel"] = fields[i]['header'];
    	data1["xtype"] = "textfield";
    	
    	searchcontent[i]="s_"+fields[i]['dataIndex'];
    	if(i<ls){
    		cruddatas1.push(data);
    		searchdatas1.push(data1);
    	}else{
    		cruddatas2.push(data);
    		searchdatas2.push(data1);
    	}
    }

   
    
	//默认的按钮事件	 
    var tbarsdatas=[];
    var  t=tbars.length;
    for(var i=0;i<t;i++){
    	tbars[i]["id"] =  "tbar"+i;
    	tbars[i]["hidden"] =  true;
    	if(tbars[i]["handler"] == undefined ){    	
    		if(tbars[i]['text'] == "查看")		{tbars[i]['handler'] =  function(){ _select(grid, crud_data_window,crud_data_form)};}
    		if(tbars[i]['text'] == "添加")		{tbars[i]['handler'] =  function(){ _add(crud_data_form,crud_data_window,actionname)};}
    		if(tbars[i]['text'] == "设置使用权限") 		{tbars[i]['handler'] =  function(){ _addqx(grid,searchWindows,1)};}
    		if(tbars[i]['text'] == "设置统计权限") 		{tbars[i]['handler'] =  function(){ _addqx(grid,searchWindows,2)};}
    		if(tbars[i]['text'] == "删除") 		{tbars[i]['handler'] =  function(){ _deletes(grid, crud_data_window,crud_data_form,main_store)};}
    		if(tbars[i]['text'] == "修改审批人") 		{tbars[i]['handler'] =  function(){ updatespr(grid, crud_data_window,crud_data_form)};}
    		if(tbars[i]['text'] == "统计") 		{tbars[i]['handler'] =  function(){ _tongji(grid, crud_data_window,crud_data_form)};}
		}
    }
    
    
   

    
    
    Ext.QuickTips.init();
    var pageSize = 20;
    var returnStart=0;
    /**------------------------存储Grid中的主数据--------------------------------*/
    Ext.define('mainPage', {
     extend: 'Ext.data.Model',
     fields: datas
    });

	 var main_store = Ext.create('Ext.data.Store', {
	     model: 'mainPage',
	     pageSize:pageSize,//不加，点击下一页时默认25条
	     proxy: {
	         type: 'ajax',
	         actionMethods: {
	                read   : 'POST', // by default GET
	            },
	         url: actionname+'.action',
	         reader: {
	             root: 'result',
	             totalProperty : 'totalCount'
	         }
	     },
	     
	 });
  
	
	
	
	main_store.on("beforeload",function(store,options){
		/**
		var searchField = Ext.getCmp("searchField");
		if(searchField){
			var new_params={filter_LIKES_${ab.searchfield}:searchField.getValue()};			
			Ext.apply(store.proxy.extraParams, new_params);  
		}

		**/
		if(Ext.getCmp("search_form")){			
			for(var s in searchcontent){				
				var as = searchcontent[s].substring(2,searchcontent[s].length);
				var searchvalue = Ext.getCmp(searchcontent[s]).getRawValue();
				var objectk = "filter_LIKES_"+as;	     	
		    	var data = {};
		    	data[objectk] = searchvalue;
		    	if(Ext.getCmp(searchcontent[s])){
					Ext.apply(store.proxy.extraParams,data);
				}				
			}		
		} 
		
		Ext.apply(store.proxy.extraParams,{"filter_ISLIKES_statisticshority":isqx});

	});
		
		 main_store.loadPage(1);
	
	
	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid = Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : title,
		width : 400,
		region:'center',
		loadMask : true,
		stripeRows : true,
		frame : true,
		cellTip : true,  //鼠标悬浮提示
		store : main_store,
		selModel : {
				    selType : 'checkboxmodel',
				    mode : 'SIMPLE'
				},
		viewConfig:{  
		   enableTextSelection:true  //可复制内容
		} ,
		columns : fields,	
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
		tbar :tbars
	});
	
/**---------------------------结束显示数据的Grid------------------------------------*/
/**	---------------------------CRUD START------------------------------------*/

/**-------------------------------修改窗口开始  ---------------------------------------------*/
	crud_data_form= new Ext.form.FormPanel({
		id: 'data_form',
				buttonAlign: 'center',
			border: false,
			width: 920,
			autoHeight: true,
			defaults: {
				anchor: '95%',
				bodyStyle: 'border-width:0 0 0 0;',
			},
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 120,
				anchor: '95%',
				bodyStyle: 'border-width:0 0 0 0;',
			},
			autoScroll: true,
			items: [{
					layout: 'column',
					defaults: {
						anchor: '100%',
						bodyStyle: 'border-width:0 0 0 0;',
					},
					items: [{
							layout: 'form',
							defaults: {
								xtype: 'textfield'
							},
							columnWidth: .45,
							items: cruddatas1
						}, {
							layout: 'form',
							defaults: {
								xtype: 'textfield'
							},
							columnWidth: .45,
							items: cruddatas2
						}, {
							layout: 'form',
							defaults: {
								xtype: 'textfield'
							},
							columnWidth: .1,
							items: [{
						        fieldLabel: 'formurl',
								xtype: 'hidden',
								id:'formurl'
								}]
							
						}
					]
				}
			],
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
			}]

			
			
});

	  
	  
	var crud_data_window = Ext.create('Ext.Window', {
		id : "crud_data_window",
		title : title,
		closeAction : 'hide',
		items : crud_data_form,
		listeners: {
			hide: function(){
				
			}
		}
	});
/**	---------------------------CRUD END------------------------------------*/
/**--------------------Search START-----------------------**/
	var searchForm = Ext.create('Ext.form.FormPanel', {
	 	id : 'search_form',
		buttonAlign : 'center',
		border : false,
		width : 800,
		autoHeight : true,
		defaults : {
			anchor : '95%',
			bodyStyle : 'border-width:0 0 0 0;',
		},
		fieldDefaults : {
			labelAlign : 'right',
			labelWidth : 82,
			anchor : '95%',
			bodyStyle : 'border-width:0 0 0 0;',
		},
		autoScroll : true,
		items : [{
				layout : 'column',
				defaults : {
					anchor : '100%',
					bodyStyle : 'border-width:0 0 0 0;',
				},
				items : [{
						layout : 'form',
						columnWidth : .5,
						items : searchdatas1
					},{
						layout : 'form',
						columnWidth : .49,
						items :searchdatas2
					}
				]
			}
		],
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
			}]
	});
	var searchWindows = function(grid,key,ids){Ext.Ajax.request({
			scope: this,
			//url:'role!listRoleRights.action',  //返回一级节点（父节点）
			url:'createform!getbumen.action',  //返回一级节点（父节点）
			params: {},
			success:function(response,options){
				var menudata = eval(response.responseText);  //一级节点信息 
				var text = "权限设置";
				if(key==1){
					var keys="userauthority"
					text = "<span style='color:red'>使用权限设置（如不设置权限，默认为全部人员可使用！）</span>"
				}else{
					var keys="statisticshority"
					text = "<span style='color:red'>统计权限设置（如不设置权限，默认为拥有此模块权限的所有人都可操作此条审批单！）</span>"
				}
				var roleRightsWindow = Ext.create('Ext.window.Window',{
					title:text,
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
				    				if(node.data.id){
					    				menuids.push(node.data.id);
				    				}
				    			}
				    			
				    		});
				    		//json=JSON.stringify(menuids);
				    		console.log(menuids.length);
				    		if(menuids.length>0){
					    		var menuids=","+menuids.join(',')+",";
				    		}else{
					    		var menuids="";
				    		}
				    		//console.log(json);return;
				    		Ext.getCmp("subbut").hide();
				    		var records = grid.getSelectionModel().getSelection();
				    		var id = records[0].get("createformid");
				    		Ext.Ajax.request({
				    			url:'createform!qxupdate.action',
				    			params: { menuids: menuids,id:id,key:key},
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
		                    url: 'createform!getusers.action?id='+menudata[i][0]+"&ids="+ids+"&key="+keys
		                },
		                fields: ['text','userid', 'id', 'leaf', 'checked','name','posname']
		            });
		           // alert("menudata[i].id"+menudata[i].id);
					var tree = Ext.create('Ext.tree.Panel',{
					
						frame:true,
						store:treeStore,
						
						iconCls:'titleIcon',
						border : false,
						autoScroll : true,
						//dataUrl: 'role!listRoleRightsByUser.action?id='+rec.get("id"),
					    root: {
					        nodeType: 'async',
					        text: menudata[i][1],
					        draggable: false,
					        checked:false,
					        expanded:true,
					        id:""
					    },
						listeners:{  
				            "checkchange": function(node,checked){  
				                TreeUtil.cascadeCheck(node,checked);  
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




/**--------------------Search END-----------------------**/
//用户页面权限
if(Ext.getCmp("show_data_grid")){
	roleHaveRight();
}
var view = new Ext.Viewport({
		layout:'border',
		items:grid
});
    var  gridandStore = [];
    gridandStore[0]=grid;
    gridandStore[1]=main_store;
	return gridandStore;
}








//查看
function  _select(grid, crud_data_window,crud_data_form){
	Ext.getCmp("subbut").hide();
	var records = grid.getSelectionModel().getSelection();
	if (records.length == 1) {    		
		location.href='createform!see.action?createformid='+records[0].get("createformid");
	}else{
		Ext.example.msg("提示", "请选择一项");
	}
}
//修改审批人
function  updatespr(grid, crud_data_window,crud_data_form){
	Ext.getCmp("subbut").hide();
	var records = grid.getSelectionModel().getSelection();
	if (records.length == 1) {    		
		location.href='createform!updatespr.action?createformid='+records[0].get("createformid");
	}else{
		Ext.example.msg("提示", "请选择一项");
	}
}



//添加
function _add(crud_data_form,crud_data_window,actionname){
//	Ext.getCmp("subbut").show();
//	crud_data_form.getForm().reset();
//	crud_data_form.getForm().findField("formurl").setValue(actionname+"!save.action");
//	crud_data_window.show();
//	location.href='/WEB-INF/content/approvalform/addform.jsp';
//	Ext.Ajax.request(
//	 { 
//	 url: 'createform!addform.action',  
//	 params: {},
//	 success: function (response) {
//		   
//	 },
//	 failure: handFailure,
//
//	 });
	location.href='createform!addform.action';
}


//修改
function _update(grid,crud_data_form,crud_data_window,actionname){
	Ext.getCmp("subbut").show();
	var records = grid.getSelectionModel().getSelection();
	if (records.length == 1) {
		 crud_data_form.getForm().findField("formurl").setValue(actionname+"!update.action");
		 crud_data_window.show();
		 crud_data_form.getForm().loadRecord(records[0]);
	}else{
		Ext.example.msg("提示", "请选择一项");
	}
}

//导出Excel
function _export(grid,actionname){
	var xjyb_jsonArray = [];
	var record =grid.getSelectionModel().getSelection();
	for(var i=0;i<record.length;i++){
			xjyb_jsonArray.push(record[i].data);
	}
	if(xjyb_jsonArray.length>0){//选择导出
		Ext.Ajax.request(
			 { 
			 url: actionname+'!createExcel.action',  
			 params: {'iteminfo':Ext.encode(xjyb_jsonArray)},
			 success: function (response) {
				    window.location.href = actionname+"!exportExcel.action";
				    Ext.example.msg("提示", "导出完成");
			 },
			 failure: handFailure,
	
			 });
	}else{//条件导出
		if(Ext.getCmp("search_form")){
			Ext.getCmp("search_form").getForm().submit({
					url: actionname+'!createExcel.action',  
	                success: function(f, action) {
	                 if (action.result.msg=="有数据") {
	                    window.location.href = actionname+"!exportExcel.action";
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



//高级搜索
function _search(searchWindow){
	searchWindow.show();
}

//删除
function _deletes(grid, crud_data_window,crud_data_form,main_store){
	Ext.getCmp("subbut").hide();
	var records = grid.getSelectionModel().getSelection();
	if (records.length == 1) {    		
		Ext.Msg.confirm('系统提示','此操作会删除该条审批单！请谨慎！',
		function(btn){
			if(btn=='yes'){
				var str = records[0].get("createformid");
				Ext.Ajax.request(
					 { 
					 url: 'createform!delete.action',  
					 params: {'createformid':str},
					 success: function (response) {
						    Ext.example.msg("提示", "删除完成");
							main_store.loadPage(1);
					 }
				});
			}
		},this); 
	}else{
		Ext.example.msg("提示", "请选择一项");
	}
}
//添加权限
function _addqx(grid,searchWindows,id){
	Ext.getCmp("subbut").hide();
	var records = grid.getSelectionModel().getSelection();
	if (records.length == 1) { 
		searchWindows(grid,id,records[0].data.createformid);
	}else{
		Ext.example.msg("提示", "请选择一项");
	}
}
//统计
function _tongji(grid,searchWindows,id){
	var records = grid.getSelectionModel().getSelection();
	if (records.length == 1) { 
		var str = encodeURI(encodeURI(records[0].get("formname")));
		location.href="createform!tongji.action?createformid="+records[0].get("createformid")+"&formname="+str;
	}else{
		Ext.example.msg("提示", "请选择一项");
	}	
}

