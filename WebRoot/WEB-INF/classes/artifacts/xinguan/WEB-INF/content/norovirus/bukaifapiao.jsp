<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新冠补开发票</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="新冠报告补录">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="./js/jquery-1.7.2.min.js"></script>
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
         { name:'id'},
         { name:'sampleNo'},
		 {name:'samplenum'},
         { name:'isfapiao'},
         { name:'shuihao'},
         { name:'fapiaotaitou'},
         { name:'zhucedizhi'},
         { name:'zhucedianhua'},
         { name:'kaihuyinhang'},
         { name:'yinhangzhanghao'},
         { name:'fapiaotype'},
         { name:'accessname'},
         { name:'accessphone'},
         { name:'accessaddress'},
         { name:'accessemail'},
         { name:'yuyueid'},
         { name:'bukaistate'},
         { name:'bukainame'},
         { name:'bukaitime'},
         { name:'yuyuenum'},
         { name:'outtradeno'},
         { name:'name'},
         { name:'sfz'},
         { name:'kaipiaodate'},
         { name:'kaipiaono'},
         { name:'inputname'},
         { name:'inputtime'},
     ]
 });

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:23,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
       	 actionMethods : 'post',
         url: 'xgbukaifapiao.action',
         reader: {
            // type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
 });




	main_store.on("beforeload",function(store,options){
        var searchYuyuenum = Ext.getCmp("searchYuyuenum");
        var searchName = Ext.getCmp("searchName");
        var searchSfz = Ext.getCmp("searchSfz");
        var searchSampleNo = Ext.getCmp("searchSampleNo");
        var searchBukaistate = Ext.getCmp("searchBukaistate");
        var ininputTime = Ext.getCmp("ininputTime");
        var osoinputTime = Ext.getCmp("osoinputTime");
        if(searchYuyuenum){
            Ext.apply(store.proxy.extraParams, {
                searchYuyuenum:searchYuyuenum.getValue()
            });
        }
        if(searchName){
            Ext.apply(store.proxy.extraParams, {
                searchName:searchName.getValue()
            });
        }
        if(searchSfz){
            Ext.apply(store.proxy.extraParams, {
                searchSfz:searchSfz.getValue()
            });
        }
        if(ininputTime){
            Ext.apply(store.proxy.extraParams, {
                ininputTime:ininputTime.getValue()
            });
        }
        if(osoinputTime){
            Ext.apply(store.proxy.extraParams, {
                osoinputTime:osoinputTime.getValue()
            });
        }
        if(searchSampleNo){
            Ext.apply(store.proxy.extraParams, {
                searchSampleNo:searchSampleNo.getValue()
            });
        }
        if(searchBukaistate){
            Ext.apply(store.proxy.extraParams, {
                searchBukaistate:searchBukaistate.getValue()
            });
        }
	});

    
    	 /**-------------------------搜索 (start)--------------------------**/	 
    
      var bukai_combox =Ext.create('Ext.form.ComboBox',{
        name:'searchBukaistate',
        id:'searchBukaistate',
        mode: 'local',
        fieldLabel:'补开状态',
        triggerAction: 'all',
        store: Ext.create('Ext.data.ArrayStore',{
            idIndex :0,
            fields:[
                {name : 'saveresult'},
                {name : 'showresult'}
            ],
            data :[
                [ '全部', '全部' ],
                [ '未补开', '未补开' ],
                [ '已补开', '已补开' ]
            ]
        }),
        valueField: 'saveresult',
        displayField: 'showresult',
        editable:false
    });
    
	 var search_form = Ext.create('Ext.form.FormPanel', {
		 	id : 'search_form',
			buttonAlign : 'center',
			border : false,
			width : 400,
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
							columnWidth : .98,
							items : [{
					                id:'searchYuyuenum',
					                name:'searchYuyuenum',
					                xtype:'textfield',
					                fieldLabel:'预约号'
					            },{
					                id:'searchSfz',
					                name:'searchSfz',
					                xtype:'textfield',
					                fieldLabel:'身份证号'
					            },{
					                id:'searchName',
					                name:'searchName',
					                xtype:'textfield',
					                fieldLabel:'客户姓名'
					            },bukai_combox,{
				                    xtype: 'fieldcontainer',
				                    fieldLabel: '申请日期',
				                    layout : 'hbox',
				                    items: [
				                        {
				                            name: 'ininputTime',
				                            xtype : 'datefield',
				                            format :'Y-m-d',
				                            editable:false,
				                            id:'ininputTime',
				                            flex : 15
				                        }, {
				                            xtype: 'displayfield',
				                            value: '至'
				                        },
				                        {
				                            name : 'osoinputTime',
				                            xtype : 'datefield',
				                            format :'Y-m-d',
				                            editable:false,
				                            id:'osoinputTime',
				                            flex : 15
				                        }
				                    ]
				                }
							]
						}
					]
				}
			],
			buttons : [{
					text : '查询',
					handler : function () {
						main_store.loadPage(1);
						searchWindow.hide();
					}
				}, {
					text : '取消',
					handler : function () {
						searchWindow.hide();
					}
				}, {
					text : '重置',
					handler : function () {
						search_form.form.reset();
					}
				}
			]
		});
	var searchWindow = Ext.create('Ext.Window', {
			id : "searchWindow",
			closeAction : 'hide',
			title : "高级搜索",
			modal : true,
			items : search_form

		});
    
     /**-------------------------搜索 (end)--------------------------**/
    
    
    
     /**-------------------------补开(start)--------------------------**/
    
    var bukai_form = Ext.create('Ext.form.FormPanel', {
		 	id : 'bukaiForm',
			buttonAlign : 'center',
			border : false,
			width : 350,
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
							columnWidth : .98,
							items : [{
									id:'bukaiid',
					                name:'id',
					                hidden:true,
					                xtype:'textfield',
					                fieldLabel:'编号'
					            },{
					            	id:'kaipiaono',
					                name:'kaipiaono',
					                xtype:'textfield',
					                fieldLabel:'开票号'
					            },{
					            	id:'kaipiaodate',
					                name:'kaipiaodate',
					                xtype:'textfield',
					                fieldLabel:'开票时间'
					            }
							]
						}
					]
				}
			],
			buttons : [{
					text : '提交',
					handler : function () {
						/* bukai_form.getForm().submit({
							url : 'xgbukaifapiao!querenbukai.action',
							method : "POST",
							success: function (form,action) {
								if(action.result.msg=='确认补开成功'){
									Ext.example.msg("提示",action.result.msg);
								}else{
									Ext.Msg.alert("提示",action.result.msg);
								}
								bukai_form.form.reset();
								bukai_window.hide();
								main_store.loadPage(1);
	                       },
	                       failure: handFailure,
	                    });*/
	                    $.post('xgbukaifapiao!querenbukai.action',{
	                    		id:Ext.getCmp("bukaiid").getValue(),
	                    		kaipiaono:Ext.getCmp("kaipiaono").getValue(),
	                    		kaipiaodate:Ext.getCmp("kaipiaodate").getValue()
	                    	},function(response){
		                    	if(response.msg=='确认补开成功'){
									Ext.example.msg("提示",response.msg);
								}else{
									Ext.Msg.alert("提示",response.msg);
								}
								bukai_form.form.reset();
								bukai_window.hide();
								main_store.loadPage(1);
	                    	})
					}
				}, {
					text : '取消',
					handler : function () {
						bukai_window.hide();
					}
				}
			]
		});
    
    var bukai_window = Ext.create('Ext.Window', {
			id : "bukaiWindow",
			closeAction : 'hide',
			title : "补开发票",
			modal : true,
			items : bukai_form

		});
     /**-------------------------补开(end)--------------------------**/
    
    /**-------------------------批量修改(start)--------------------------**/
    	 Ext.define('plxg_model',{
			extend:'Ext.data.Model',
			fields:[
			{
				name:'sampleNo',
				type:'string'
			},
			{
				name:'kaipiaodate',
				type:'string'
			},{
				name:'kaipiaono',
				type:'string'
			}]
		});
		
		plxg_store = Ext.create('Ext.data.Store',{
			model:plxg_model
		});
		
		plxg_form = Ext.create('Ext.form.FormPanel',{
			labelWidth:50,
			labelAlign : 'right',
			trackResetOnLoad:true,
			buttonAlign : 'center',
			border:false,
			//width :600,
			autoHeight:true,
			items : [{
				width:500,
				height:300,
				id:'plxg_textarea',
				xtype : 'textarea',
				name : 'textarea',
				emptyText: '温馨提示：可直接复制EXCEL表中的对应列到此处，格式如下：样本编号-开票号-开票时间',
			}],
			buttons : [ {
				text : '提交',
				handler : function() {
					 plxg_store.removeAll();
					 var str=Ext.getCmp("plxg_textarea").getValue().replace(/\n$/,"");
					 var len = str.split("\n");//获取数据 
					 var plxg_jsonArray=[];
					 var trStr;
					 for(var i=0;i<len.length;i++){
				           //excel表格同一行的多个cell是以空格 分割的，此处以空格为单位对字符串做 拆分操作。。
				          trStr = len[i].split("\t");
				          if(trStr.length!=3){
				            Ext.example.msg("提示","样本编号为"+trStr[0].trim()+"的数据有未填项",3000);
									return;
				          }
				          if(trStr[0].trim()==null||trStr[0].length==0){
				          Ext.example.msg("提示","有数据的样本编号未填写",3000);
									return;
				          }
				          if(trStr[1].trim()==null||trStr[1].length==0){
				          Ext.example.msg("提示","样本编号为"+trStr[0].trim()+"的开票号未填写",3000);
									return;
				          }
				          if(trStr[2].trim()==null||trStr[2].length==0){
				          Ext.example.msg("提示","样本编号为"+trStr[0].trim()+"的开票时间未填写",3000);
									return;
				          }
				          plxg_store.insert(0, {
				        	  sampleNo : trStr[0],
				        	  kaipiaono : trStr[1],
				        	  kaipiaodate : trStr[2]
							});
					 };
					for(i=0;i<plxg_store.getCount();i++){
				    	var record = plxg_store.getAt(i);
				     	plxg_jsonArray.push(record.data);
				     }
					 plxg_form.getForm().submit({
						url: 'xgbukaifapiao!checkBatchUpdate.action',  
						params:{'itemsxjyb':Ext.encode(plxg_jsonArray)},
						waitMsg : '正在提交数据',
	                    success: function(f, action) {
		                     if (action.result.msg.indexOf('已补开')>0) {
		                    	 Ext.MessageBox.confirm("提示",action.result.msg,function( button,text ){  
	                                   if( button == 'yes'){
		                                   	plxg_form.getForm().submit({
		              							 url: 'xgbukaifapiao!batchUpdate.action',  
		              							 params:{'itemsxjyb':Ext.encode(plxg_jsonArray)},
		              							 waitMsg : '正在提交数据',
		              		                     success: function(f, action) {
		              		                     if (action.result.success) {
		              		                        Ext.example.msg('消息', action.result.msg);
			              		                      main_store.load({
			              		                		params : {
			              		                				start : returnStart,
			              		                				limit : 23,
			              		                			}
			              		                	});
		              		                	
		              		                        plxg_window.hide();	
		              		                      }else{
													 Ext.Msg.alert('消息', action.result.msg);
		              		                      }
		              		                    },
		              		                     failure:handFailure
		              						});
		                                }  
	                                }   
	                            );   
		                        plxg_window.hide();	
		                     }else if(action.result.msg=='无异常信息'){
		                    	  plxg_form.getForm().submit({
              							 url: 'xgbukaifapiao!batchUpdate.action',  
              							 params:{'itemsxjyb':Ext.encode(plxg_jsonArray)},
              							 waitMsg : '正在提交数据',
              		                     success: function(f, action) {
              		                     if (action.result.success) {
											 Ext.Msg.alert('消息', action.result.msg);
	              		                      main_store.load({
	              		                		params : {
	              		                				start : returnStart,
	              		                				limit : 23,
	              		                			}
	              		                	});
              		                	
              		                        plxg_window.hide();	
              		                      }else{
											 Ext.Msg.alert('消息', action.result.msg);
              		                      }
              		                    },
              		                     failure:handFailure
              						});	
		                     }else{
								 Ext.Msg.alert('消息', action.result.msg);
		                     }
	                    },
	                     failure:handFailure
					});
				}
			},{
				text : '取消',
				handler:function(){
					plxg_window.hide();
				}
			}]
		});
		plxg_window = new Ext.Window({
			title : '批量修改',
			closeAction : 'hide',
			modal : true,
			items : plxg_form
		});
    
    
    
    /**-------------------------批量修改(end)--------------------------**/
    
  
    
	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid =Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : '新冠补开发票',
		width : 600,
		region:'center',
		loadMask : true,
		stripeRows : true,
		frame : true,
		viewConfig:{
            enableTextSelection:true  //可复制内容
        } ,
		store : main_store,
        selModel : {
            selType : 'checkboxmodel',
            mode : 'SIMPLE'
        },
		columns:[{
            header : '编号',
            width : 50,
            sortable : true,
            dataIndex : 'id'
        },{
            header:'样本编号',
            dataIndex:'sampleNo'
        },{
			header:'线下样本编号',
			dataIndex:'samplenum'
		},{
            header:'预约号',
            dataIndex:'yuyuenum'
        },{
            header:'商户单号',
            width:170,
            dataIndex:'outtradeno'
        },{
            header:'姓名',
            dataIndex:'name'
        },{
            header:'身份证号',
            width:170,
            dataIndex:'sfz'
        },{
            header:'是否需要发票',
            dataIndex:'isfapiao'
        },{
            header:'补开状态',
            width: 120,
            dataIndex:'bukaistate'
        },{
            header:'开票时间',
            width: 120,
            dataIndex:'kaipiaodate'
        },{
            header:'开票号',
            width: 120,
            dataIndex:'kaipiaono'
        }, {
            header : '税号',
            width: 170,
            dataIndex : 'shuihao'
        },{
            header : '发票抬头',
            dataIndex : 'fapiaotaitou'
        },{
            header : '注册地址',
            width: 170,
            dataIndex : 'zhucedizhi'
        },{
            header:'注册电话',
            dataIndex:'zhucedianhua'
        },{
            header:'开户银行',
            dataIndex:'kaihuyinhang'
        },{
            header:'银行账号',
            dataIndex:'yinhangzhanghao'
        },{
            header:'发票类型',
            dataIndex:'fapiaotype'
        },{
            header:'发票收件人姓名',
            width: 170,
            dataIndex:'accessname'
        },{
            header:'发票收件人电话',
            width: 170,
            dataIndex:'accessphone'
        },{
            header:'发票收件人地址',
            width: 170,
            dataIndex:'accessaddress'
        },{
            header:'发票收件人邮箱',
            width: 170,
            dataIndex:'accessemail'
        },{
        	hidden:true,
            header:'预约id',
            width: 120,
            dataIndex:'yuyueid'
        },{
            header:'确认补开人',
            width: 120,
            dataIndex:'bukainame'
        },{
            header:'确认补开时间',
            width: 120,
            dataIndex:'bukaitime'
        },{
            header:'申请人',
            width: 120,
            dataIndex:'inputname'
        },{
            header:'申请时间',
            width: 120,
            dataIndex:'inputtime'
        }],
		listeners:{

		  } ,
		tbar : [
		{
                text : '确认补开发票',
                id:'tbar0',
                hidden:true,
                icon : 'img/add.png',
                handler : function() {
                    var fk_jsonArray = [];
                    var record = grid.getSelectionModel().getSelection();
                    console.log(record);
                    if(record.length==1){
                    	if(record[0].data.bukaistate!='已补开'){
                    		Ext.getCmp("bukaiid").setValue(record[0].data.id);
                    		Ext.getCmp("kaipiaodate").setValue(record[0].data.kaipiaodate);
                    		Ext.getCmp("kaipiaono").setValue(record[0].data.kaipiaono);
                    		bukai_window.show();
                    	}else{
                    		Ext.example.msg("提示","样本"+record[0].data.sampleNo+":已补开，不能重复补开");
                    	}
                    	
                    }else if(record.length>1){
                    	Ext.example.msg("提示","请选择一条要操作的数据");
                    }
                    else{
                    	Ext.example.msg("提示","请选择要操作的数据");
                    }
                }
            },{
                text : '导出财务',
                id:'tbar1',
                hidden:true,
                icon:'img/xia.png',
                handler:function(){
                    var xjyb_jsonArray = [];
                    iscaiwu = '是';
                    var record =grid.getSelectionModel().getSelection();

                    for(var i=0;i<record.length;i++){
                        xjyb_jsonArray.push(record[i].data);
                    }
                    search_form.getForm().submit({
                    	params : {
							'itemsxjyb':Ext.encode(xjyb_jsonArray),
							'searchSampleNo':Ext.getCmp('searchSampleNo').getValue()
						},
						timeout: 100000000,
						url : 'xgbukaifapiao!caiwuExcel.action',
						method : "POST",
						success: function (form,action) {
							
                           if(action.result.msg=="否"){
                               Ext.example.msg("提示", "最多导出30000条数据！");
                               return;
                           }
                           Ext.example.msg("提示",action.result.msg,4000);
                           if(action.result.msg=="EXCEL已导出"){
                               window.location.href='schedulesort!downloadfile.action?helpdocs=xinguanbukaicaiwu.xls';
                           }
                       },
                       failure: handFailure,
                    })
                }
             },{
                text : '批量修改',
                id:'tbar2',
                hidden:true,
                icon:'img/edit.gif',
                handler:function(){
                    plxg_store.removeAll();
                    Ext.getCmp('plxg_textarea').setValue();
                    plxg_window.show();
                }
             },{
                text : '高级搜索',
                id:'tbar3',
                hidden:true,
                icon:'img/search.png',
                handler:function(){
                    searchWindow.show();
                }
             },"-",{
                id:'searchSampleNo',
                xtype:'textfield',
                width:200,
                emptyText:'请输入要查找的样本编号',
                enableKeyEvents:true,
                listeners:{
                    keyup:function(tf,e){
                        var key = e.getKey();
                        if(key == e.ENTER){
                            main_store.loadPage(1);
                        }
                    }
                }
            },{
                text:'搜索',
                icon:'img/simplesearch.png',
                handler:function(){
                    main_store.loadPage(1);
                }
            }],
		bbar : new Ext.PagingToolbar({
			store : main_store,
			displayInfo : true,
			pageSize : pageSize
		})
	});
	
	Ext.getCmp('searchBukaistate').setValue('全部');
	main_store.loadPage(1);

/**---------------------------结束显示数据的Grid------------------------------------*/
	
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