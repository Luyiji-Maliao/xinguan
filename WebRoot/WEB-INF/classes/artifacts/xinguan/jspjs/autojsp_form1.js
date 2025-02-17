
//具体使用请看/WEB-INF/content/tumor/chemicaldrugdanxianglib.jsp.
//jsp里面有详细使用方法
function createJsp(alldatas){
	var sp="";
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
    var shenhedatas=[];
    var ls = (l-2)/2;
    
    var searchcontent=[];
    //审核状态
    var s_state = Ext.create('Ext.form.ComboBox', {
		fieldLabel: '审核状态',
		id: 's_state',
		name: 'state',
		store: Ext.create('Ext.data.Store', {
			fields: ['key', 'value'],
			data: [{
					"key": "未审核",
					"value": "0"
				}, {
					"key": "审核通过",
					"value": "1"
				}, {
					"key": "审核不通过",
					"value": "2"
				}
			]
		}),
		bodyStyle: 'border-width:0 0 0 0;',
		queryMode: 'local',
		displayField: 'key',
		typeAhead: false, 
		editable: false,
		forceSelection: true,
		valueField: 'value',
		listeners: {
			change: function () {
				if (this.getValue() == null)
					this.reset();
			}
		}
	});
  //审核状态
    var s_state1 = Ext.create('Ext.form.ComboBox', {
		fieldLabel: '审核状态',
		id: 's_state1',
		name: 'states',
		anchor:'95%',
		store: Ext.create('Ext.data.Store', {
			fields: ['key', 'value'],
			data: [{
					"key": "审核通过",
					"value": "1"
				}, {
					"key": "审核不通过",
					"value": "2"
				}
			]
		}),
		bodyStyle: 'border-width:0 0 0 0;',
		queryMode: 'local',
		displayField: 'key',
		typeAhead: false, 
		editable: false,
		forceSelection: true,
		valueField: 'value',
		listeners: {
			change: function () {
				if (this.getValue() == null)
					this.reset();
			}
		}
	});
    //部门
    var data = [];
    $.ajaxSettings.async = false;
    $.post("createform!getbumen.action","",function(datas){
    	$.each(datas,function(k,v){
    		data.push({"key":v[1],"value":v[0]})
    	})
    })
    var s_bumen = Ext.create('Ext.form.ComboBox', {
		fieldLabel: '部门',
		id: 's_bumen',
		name: 'bumen',
		store: Ext.create('Ext.data.Store', {
			fields: ['key', 'value'],
			data: data
		}),
		bodyStyle: 'border-width:0 0 0 0;',
		queryMode: 'local',
		displayField: 'key',
		typeAhead: false, 
		editable: false,
		forceSelection: true,
		valueField: 'value',
		listeners: {
			change: function () {
				if (this.getValue() == null)
					this.reset();
			}
		}
	});
    for(var i=0;i<fields.length;i++){  
    	var data = {};
    	data["name"] =fields[i]['dataIndex'];
    	data["id"] = fields[i]['dataIndex'];
    	data["fieldLabel"] = fields[i]['header'];
    	var data1={};
    	if(fields[i]['header']=='审核状态' || fields[i]['header']=='录入人' || fields[i]['header']=='录入日期' || fields[i]['header']=='部门' || fields[i]['header']=='申请人'){
    		if(fields[i]['header']=='审核状态'){
    			searchdatas1.push(s_state);
    		}else if(fields[i]['header']=='部门'){
    			searchdatas1.push(s_bumen);
    		}else if(fields[i]['header']=='录入日期'){
    			data1["id"] = "s_"+fields[i]['dataIndex'];
		    	data1["fieldLabel"] = fields[i]['header'];
		    	data1["xtype"] = "datefield";
		    	data1['flex']=15;
		    	data1['format']= 'Y-m-d';
    			searchdatas2.push(data1);
    		}else{
	    		data1["id"] = "s_"+fields[i]['dataIndex'];
		    	data1["fieldLabel"] = fields[i]['header'];
		    	data1["xtype"] = "textfield";
		    	if(i<ls){
		    		searchdatas1.push(data1);
		    	}else{
		    		searchdatas2.push(data1);
		    	}
    		}
        	searchcontent[i]="s_"+fields[i]['dataIndex'];
    	}
    	if(i<ls+1){
    		cruddatas1.push(data);
    	}else{
    		cruddatas2.push(data);
    	}
    }

    
	//默认的按钮事件	 
    var tbarsdatas=[];
    var  t=tbars.length;
    for(var i=0;i<t;i++){
    	tbars[i]["id"] =  "tbar"+i;
    	tbars[i]["hidden"] =  false;
    	if(tbars[i]["handler"] == undefined ){    	
    		if(tbars[i]['text'] == "查看")		{tbars[i]['handler'] =  function(){ _select(grid, crud_data_window,crud_data_form)};}
    		if(tbars[i]['text'] == "已审批")		{tbars[i]['handler'] =  function(){sp="已审核";main_store.loadPage(1);}}
    		if(tbars[i]['text'] == "未审批")		{tbars[i]['handler'] =  function(){sp="未审核";main_store.loadPage(1);}}
    		if(tbars[i]['text'] == "导出EXCEL")	{tbars[i]['handler'] =  function(){ _export(grid,actionname,searchcontent)};}
    		if(tbars[i]['text'] == "高级搜索") 		{tbars[i]['handler'] =  function(){ _search(searchWindow)};}
    		if(tbars[i]['text'] == "打印") 		{tbars[i]['handler'] =  function(){ _Printing(grid, crud_data_window,crud_data_form)};}
    		if(tbars[i]['text'] == "审核") 		{tbars[i]['handler'] =  function(){ _Reason(grid, shenhe_window,shenhe_form,actionname)};}
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
	    	var data = {};
			for(var s in searchcontent){	
				var as = searchcontent[s].substring(2,searchcontent[s].length);
				var searchvalue = Ext.getCmp(searchcontent[s]).getRawValue();
				var objectk = as;	     	
		    	data[objectk] = searchvalue;
		    	if(Ext.getCmp(searchcontent[s])){
					Ext.apply(store.proxy.extraParams,data);
				}				
			}		
		} 		
		var new_params={id:id}
		Ext.apply(store.proxy.extraParams, new_params);  
		if(sp){
			var new_params={state:sp};
			sp="";
			Ext.apply(store.proxy.extraParams, new_params);  
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
	var shenhe_form = Ext.create('Ext.form.Panel', {
		id : 'update_data_form',
		//labelWidth : 60,
		//labelAlign : 'right',
		//trackResetOnLoad:true,
		buttonAlign : 'center',
		//frame : true,
		url : 'createform!updatestate.action',
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
		items : [s_state1,{
			fieldLabel : '审核原因',
			anchor:'95%',
			xtype : 'textfield',
			name : 'reason'
		},{
			fieldLabel : '审核人',
			anchor:'95%',
			xtype : 'textfield',
			name : 'spr',
			value : username,
			readOnly:true
		},{
			fieldLabel : 'id',
			anchor:'95%',
			xtype : 'textfield',
			name : 'id',
			id : 'ids',
			hidden:true
		}],
		buttons : [ {
			text : '提交',
			id:'subbut1',
			handler : function() {
				if(!Ext.getCmp("s_state1").getValue()){
					Ext.example.msg('消息','请选择审核状态！');
					return false;
				}
				shenhe_form.getForm().submit({
	                    success: function(f, action) {
	                     if (action.result.success) {
	                       	shenhe_window.hide();
	                        grid.getStore().reload();
	                        shenhe_form.getForm().reset();
	                        Ext.example.msg('消息', action.result.msg);
	                        main_store.load({});
	                      }
	                    },
	                     failure:handFailure
			});
		}}, {
			text : '取消',
			handler:function(){
				shenhe_window.hide();
			}
		} ]
	});
	var shenhe_window = Ext.create('Ext.Window', {
		id : "shenhe_window",
		title : '审核',
		closeAction : 'hide',
		items : shenhe_form,
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
	var searchWindow = new Ext.Window({
					title:'高级搜索',
					id:'searchwindow',
					closeAction: 'hide',
					width:815,
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
		 crud_data_form.getForm().loadRecord(records[0]);
		 $.each(records[0].data,function(k,v){
			 if(Ext.getCmp(k)!=undefined){
				 Ext.getCmp(k).readOnly=true;
			 }
		 })
		 crud_data_window.show();
		
	}else{
		Ext.example.msg("提示", "请选择一项");
	}
}

//打印
function  _Printing(grid, crud_data_window,crud_data_form){
	var rowid = grid.getSelectionModel().getSelection()[0].data.id;
	var records = grid.getSelectionModel().getSelection();
	if (records.length == 1) { 
		window.open('createform!printing.action?id='+id+"&rowid="+rowid);
	}else{
		Ext.example.msg("提示", "请选择一项");
	}
}
//查看图片
function showimg(path){
	window.open("http://limsxt.com"+path);
}

//添加
function _add(crud_data_form,crud_data_window,actionname){
	Ext.getCmp("subbut").show();
	crud_data_form.getForm().reset();
	crud_data_form.getForm().findField("formurl").setValue(actionname+"!save.action");
	crud_data_window.show();
}


//审核
function _Reason(grid, shenhe_window,shenhe_form,actionname){
	Ext.getCmp("subbut").show();
	var records = grid.getSelectionModel().getSelection();
	if (records.length == 1) {
		var key=1;
		var num = 1;
		if(records[0].data.state=='审核未完成'){
			 $.each(records[0].data,function(k,v){
				 if(!records[0].data['sprstate'+key]){
					 return false
				 }
				 //alert(records[0].data['sprstate'+(key-1)]);
				 if((records[0].data['sprstate'+(key-1)]=='审核通过' || records[0].data['sprstate'+(key-1)]==undefined) && records[0].data['sprstate'+key]=='未审核' && records[0].data['spr'+key]==username){
					num=2;
					return false
				}else{
					num = 1
				}
				key++;
			 })
			 if(num==2){
				 Ext.getCmp("ids").setValue(records[0].data.id);
				 shenhe_window.show();
			 }else{
				 Ext.example.msg("提示", "您不能审核此条数据！");
			 }
		}else{
			 Ext.example.msg("提示", "您不能审核此条数据！");
		}
	}else{
		Ext.example.msg("提示", "请选择一项");
	}
}

//导出Excel
function _export(grid,actionname,searchcontent){
	var xjyb_jsonArray = [];
	var record =grid.getSelectionModel().getSelection();
	for(var i=0;i<record.length;i++){
		xjyb_jsonArray.push(record[i].data);
	}
	if(xjyb_jsonArray.length>0){//选择导出
		Ext.Ajax.request(
			 { 
			 url: 'createform!createExcel.action',  
			 params: {'iteminfo':Ext.encode(xjyb_jsonArray),"id":id},
			 success: function (response) {
				    window.location.href = "createform!exportExcel.action";
				    Ext.example.msg("提示", "导出完成");
			 },
			 failure: handFailure,
	
			 });
	}else{//条件导出
		if(Ext.getCmp("search_form")){
			var data = {id:id};
			for(var s in searchcontent){	
				var as = searchcontent[s].substring(2,searchcontent[s].length);
				var searchvalue = Ext.getCmp(searchcontent[s]).getRawValue();
				var objectk = as;	     	
		    	data[objectk] = searchvalue;
			}		
			Ext.Ajax.request(
				 { 
				 url: 'createform!createExcel.action',  
				 params: data,
				 success: function (response) {
					    window.location.href = "createform!exportExcel.action";
					    Ext.example.msg("提示", "导出完成");
				 },
				 failure: handFailure,
		
				 }
			);
		}else{
			alert("请检索后导出");
		}
	}
}



//高级搜索
function _search(searchWindow){
	searchWindow.show();
}




