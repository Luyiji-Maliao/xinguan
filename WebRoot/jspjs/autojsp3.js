
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
    		if(tbars[i]['text'] == "修改")		{tbars[i]['handler'] =  function(){ _update(grid,crud_data_form,crud_data_window,actionname)};}
    		if(tbars[i]['text'] == "导出EXCEL")	{tbars[i]['handler'] =  function(){ _export(grid,actionname)};}
    		if(tbars[i]['text'] == "高级搜索") 		{tbars[i]['handler'] =  function(){ _search(searchWindow)};}
    		
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
		width : 600,
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
		 crud_data_window.show();
		 crud_data_form.getForm().loadRecord(records[0]);
	}else{
		Ext.example.msg("提示", "请选择一项");
	}
}



//添加
function _add(crud_data_form,crud_data_window,actionname){
	Ext.getCmp("subbut").show();
	crud_data_form.getForm().reset();
	crud_data_form.getForm().findField("formurl").setValue(actionname+"!save.action");
	crud_data_window.show();
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




