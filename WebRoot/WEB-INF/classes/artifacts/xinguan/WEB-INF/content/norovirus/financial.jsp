<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新冠报告补录</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="新冠报告补录">
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
         { name:'id'},
         { name:'openid'},
         { name:'name'},
         { name:'sex'},
         { name:'sending'},
         { name:'age'},
         { name:'sfz'},
         { name:'phone'},
         { name:'outtradeno'},
         { name:'totalfee'},
         { name:'tuikuanjine'},
         { name:'issuccessed'},
         { name:'yuyuenum'},
         { name:'qudao'},
         { name:'tuikuan'},
         { name:'tuikuanyuanyin'},
         { name:'tuikuandate'},
         { name:'tuikuanname'},
         { name:'shenqingdate'},
         { name:'shenqingname'},
         { name:'shijijine'},
     ]
 });

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:23,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
       	 actionMethods : 'post',
         url: 'appointmentinfoxgwx!list1.action',
         reader: {
            // type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
 });




	main_store.on("beforeload",function(store,options){
        var searchSfz = Ext.getCmp("searchSfz");
        var searchName = Ext.getCmp("searchName");
        var searchYuyuenum = Ext.getCmp("searchYuyuenum");
        if(searchSfz){
            Ext.apply(store.proxy.extraParams, {
                searchSfz:searchSfz.getValue()
            });
        }
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
		//Ext.apply(store.proxy.extraParams, {filter_LIKES_department00deptName_OR_department00description:'IT'});

		});

		 main_store.loadPage(1);

    var caiyangdian = [];
    var fk_jsonArray = [];
	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid =Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : '新冠预约退款',
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
            header : '预约ID',
            width : 60,
            sortable : true,
            dataIndex : 'id'
        }, {
            header : '姓名',
            dataIndex : 'name'
        },{
            header:'商户单号',
            width: 170,
            dataIndex:'outtradeno'
        },{
            header : '身份证号',
            width: 170,
            dataIndex : 'sfz'
        },{
            header : '预约号',
            dataIndex : 'yuyuenum'
        },{
            header:'联系电话',
            dataIndex:'phone'
        },{
            header:'是否付款到账',
            dataIndex:'issuccessed'
        },{
            header:'销售渠道',
            dataIndex:'qudao'
        },{
            header:'付款总金额',
            dataIndex:'totalfee'
        },{
            header:'申请退款金额',
            dataIndex:'tuikuanjine'
        },{
            header:'实际退款金额',
            dataIndex:'shijijine'
        },{
            header:'申请退款原因',
            dataIndex:'tuikuanyuanyin'
        },{
            header:'退款状态',
            dataIndex:'tuikuan'
        },{
            header:'申请时间',
            width: 140,
            dataIndex:'shenqingdate'
        },{
            header:'申请退款人',
            width: 120,
            dataIndex:'shenqingname'
        },{
            header:'退款时间',
            width: 140,
            dataIndex:'tuikuandate'
        },{
            header:'确定退款人',
            width: 120,
            dataIndex:'tuikuanname'
        }],
		listeners:{

		  } ,
		tbar : [
		{
                text : '确认已退款',
                id:'tbar0',
                hidden:true,
                icon : 'img/add.png',
                handler : function() {

                    var record = grid.getSelectionModel().getSelection();
                    fk_jsonArray = [];
                    if(record.length<=0){
                        Ext.example.msg("提示","请选择一条要操作的数据");
                        return;
                    }
                    for (var i = 0; i < record.length; i++) {
                        if(record[i].data.tuikuan == '已退款'){
                            Ext.example.msg("提示", "确认退款失败,姓名为“"+record[i].data.name+"”不能重复确认");
                            return;
                        }else{
                            fk_jsonArray.push(record[i].data);
                        }

                    }

                    tuikuaninfo_window.show();



                }
            },{
                text : '批量确认',
                id:'tbar1',
                hidden:true,
                icon : 'img/add.png',
                handler : function() {
                    xjyb_window.show();
                }
            },{
                text : '批量确认模板',
                icon : 'img/0114.gif',
                id:'tbar2',
                hidden:true,
                handler:function(){
                    window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/xgtuikuan.xls';
                }
            },{
                text : '导出退款信息',
                id:'tbar3',
                hidden:true,
                icon:'img/up.gif',
                handler:function(){
                    var xjyb_jsonArray = [];
                    var record =grid.getSelectionModel().getSelection();

                    for(var i=0;i<record.length;i++){
                        xjyb_jsonArray.push(record[i].data);
                    }
                    if(xjyb_jsonArray.length>0){
                        Ext.Ajax.request(
                            {
                                url: 'appointmentinfoxgwx!tuikuanExcel.action',
                                params: {'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                success: function (response) {
                                    if(response.responseText=="否"){
                                        Ext.example.msg("提示", "最多导出30000条数据！");
                                        return;
                                    }
                                    Ext.example.msg("提示",response.responseText,4000);
                                    if(response.responseText=="EXCEL已导出"){
                                        window.location.href='schedulesort!downloadfile.action?helpdocs=xinguantuikuan.xls';
                                    }
                                },
                                failure: handFailure,


                            });
                    }else{
                        searchWindow.show();
                    }
                }
            },{
                id:'searchYuyuenum',
                xtype:'textfield',
                width:220,
                emptyText:'请输入要查找的预约号或商户单号',
                enableKeyEvents:true,
                listeners:{
                    keyup:function(tf,e){
                        var key = e.getKey();
                        if(key == e.ENTER){
                            /*main_store.setBaseParam("start",0);
                            main_store.setBaseParam("limit",pageSize);
                            main_store.setBaseParam("sort",'outSampleId'); Ext3后台排序参数
                            main_store.setBaseParam("dir",'DESC'); Ext3后台排序参数
                            main_store.load();  */
                            main_store.loadPage(1);
                        }
                    }
                }
            },"-",{
                id:'searchSfz',
                xtype:'textfield',
                width:200,
                emptyText:'请输入要查找的身份证或手机号',
                enableKeyEvents:true,
                listeners:{
                    keyup:function(tf,e){
                        var key = e.getKey();
                        if(key == e.ENTER){
                            /*main_store.setBaseParam("start",0);
                            main_store.setBaseParam("limit",pageSize);
                            main_store.setBaseParam("sort",'outSampleId'); Ext3后台排序参数
                            main_store.setBaseParam("dir",'DESC'); Ext3后台排序参数
                            main_store.load();  */
                            main_store.loadPage(1);
                        }
                    }
                }
            },"-",{
                id:'searchName',
                xtype:'textfield',
                width:200,
                emptyText:'请输入要查找的客户姓名',
                enableKeyEvents:true,
                listeners:{
                    keyup:function(tf,e){
                        var key = e.getKey();
                        if(key == e.ENTER){
                            /*main_store.setBaseParam("start",0);
                            main_store.setBaseParam("limit",pageSize);
                            main_store.setBaseParam("sort",'outSampleId'); Ext3后台排序参数
                            main_store.setBaseParam("dir",'DESC'); Ext3后台排序参数
                            main_store.load();  */
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
		bbar : new Ext.PagingToolbar({
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




    xjyb_form = Ext.create('Ext.form.Panel', {
        labelWidth : 50,
        labelAlign : 'right',
        buttonAlign : 'center',
        //width :600,
        autoHeight:true,
        items : [{
            width:500,
            height:300,
            id:'xjyb_textarea',
            xtype : 'textarea',
            name : 'textarea'
        }],
        buttons : [ {
            text : '提交',
            handler : function() {
                localStore.removeAll();
                var str=Ext.getCmp("xjyb_textarea").getValue().replace(/\n$/,"");
                var len = str.split("\n");//获取数据
                var xjyb_jsonArray=[];
                var trStr;
                var i;

                if(len.length>0){
                    for(var i=0;i<len.length;i++){
                        //excel表格同一行的多个cell是以空格 分割的，此处以空格为单位对字符串做 拆分操作。。
                        trStr = len[i].split("\t");

                        localStore.insert(0,{
                            outtradeno:trStr[0].trim(),
                            shijijine:trStr[1].trim(),
                            tuikuandate:trStr[2].trim(),
                        });
                    };
                }

                for(i=0;i<localStore.getCount();i++){
                    var record = localStore.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }

                xjyb_form.getForm().submit({
                    url: 'financial!querentuikuan.action',
                    timeout: 100000000,
                    async: false,
                    params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    success: function(f, action) {
                        if (action.result.success) {
                                Ext.example.msg('消息', action.result.msg);
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });
                                xjyb_window.hide();

                        }else{
                            Ext.example.msg('消息', action.result.msg);
                        }
                    },
                    failure:handFailure
                });
            }
        },
            {
                text : '取消',
                handler:function(){

                    xjyb_window.hide();
                }
            } ]
    });

    /*---------------------------批量添加---------------------------*/

    Ext.define('State',{
        extend : 'Ext.data.Model',
        fields   : [
            { name:'id',type:'string'},
            { name:'openid',type:'string'},
            { name:'name',type:'string'},
            { name:'sex',type:'string'},
            { name:'sending',type:'string'},
            { name:'age',type:'string'},
            { name:'sfz',type:'string'},
            { name:'phone',type:'string'},
            { name:'outtradeno',type:'string'},
            { name:'totalfee',type:'string'},
            { name:'tuikuanjine',type:'string'},
            { name:'issuccessed',type:'string'},
            { name:'yuyuenum',type:'string'},
            { name:'qudao',type:'string'},
            { name:'tuikuandate',type:'string'},
            { name:'tuikuanyuanyin',type:'string'},
            { name:'tuikuanname',type:'string'},
            { name:'tuikuan',type:'string'},
            { name:'shenqingdate',type:'string'},
            { name:'shenqingname',type:'string'},
            { name:'shijijine',type:'string'},
        ]
    });

    var  cp=Ext.create('Ext.form.Panel', {
        id : 'cp_form',
        buttonAlign : 'center',
        width : 300,
        autoHeight : true,
        border:false,
        standardSubmit:true,
        fieldDefaults: {
            labelAlign: 'right',
            labelWidth:70
        },
        items:[{
            fieldLabel: 'reportPath',
            xtype:'hidden',
            id:'reportPath',
            name:'reportPath'
        }
        ]
    });


    //2.定义store,这里区分是内存中的还是远程服务器的
    //2.1 内存中的
    var localStore = Ext.create('Ext.data.Store',{
        model:State
        // data : localData1
    });

    xjyb_window = Ext.create('Ext.window.Window',{
        title : '批量确认',
        closeAction : 'hide',
        modal : true,
        items : xjyb_form
    });

    //B超
    var tuikuan_combox =Ext.create('Ext.form.ComboBox',{
        name:'tuikuan',
        id:'tuikuan',
        mode: 'local',
        fieldLabel:'退款状态',
        triggerAction: 'all',
        store: Ext.create('Ext.data.ArrayStore',{
            idIndex :0,
            fields:[
                {name : 'saveresult'},
                {name : 'showresult'}
            ],
            data :[
                [ '', '' ],
                [ '申请退款', '申请退款' ],
                [ '已退款', '已退款' ]
            ]
        }),
        valueField: 'saveresult',
        displayField: 'showresult',
        editable:false
    });

    var searchForm=Ext.create('Ext.form.FormPanel',{
        //url:'nucleicacids!request_entity.action',   //多条件跳转（当前页面）功能
        id : 'search_form',
        trackResetOnLoad:true,
        buttonAlign:'center',
        border:false,
        autoWidth:true,
        border:false,
        autoHeight : true,
        fieldDefaults: {
            labelAlign: 'right',
            labelWidth:100

        },
        items : [{
            layout : 'column',
            defaults : {
                anchor : '100%',
                bodyStyle : 'border-width:0 0 0 0;',
            },
            items : [{
                layout : 'form',
                columnWidth : .9,
                items : [{
                    xtype: 'fieldcontainer',
                    fieldLabel: '申请退款日期',
                    defaults: {
                        xtype     : 'datefield',
                    },
                    items: [
                        {
                            name: 'ininputTime',
                            format :'Y-m-d',
                            editable:false,
                            id:'ininputTime'
                        }, {
                            xtype: 'displayfield',
                            value: '至'
                        },
                        {
                            name : 'osoinputTime',
                            format :'Y-m-d',
                            editable:false,
                            id:'osoinputTime',
                        }
                    ]
                },tuikuan_combox

                ]
            }
            ]
        }
        ],

        buttons:[{
            text:'导出退款信息',
            handler:function(){

                    Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                    Ext.Ajax.request(
                        {
                            url: 'appointmentinfoxgwx!tuikuanExcel.action',
                            timeout: 100000000,
                            params: {
                                'ininputTime':Ext.getCmp("ininputTime").getValue(),
                                'osoinputTime':Ext.getCmp("osoinputTime").getValue(),
                                'tuikuanstate' :Ext.getCmp("tuikuan").getValue(),
                            },
                            success: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示",response.responseText,4000);
                                if(response.responseText=="EXCEL已导出"){
                                    window.location.href='schedulesort!downloadfile.action?helpdocs=xinguantuikuan.xls';
                                }
                            },
                            failure: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示","导出失败",4000);
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
        title:'导出',
        id:'searchwindow',
        closeAction: 'hide',
        width:300,
        autoScroll:true,
        modal:true,
        items:searchForm
    });

    var tuikuaninfo_form = Ext.create('Ext.form.Panel', {
        labelWidth : 50,
        labelAlign : 'right',
        buttonAlign : 'center',
        width :400,
        autoHeight:true,
        autoScroll : true,
        //frame:true,
        border: false,
        fieldDefaults: {
            labelAlign: 'right',
            labelWidth: 120
        },
        items : [ {
            //xtype: 'container',
            layout: 'column',
            border: false,
            items: [
                {
                    columnWidth: .9,
                    layout: 'form',
                    border: false,
                    defaults: {
                        xtype: 'textfield'
                    },
                    items: [{
                        fieldLabel: '实际退款金额(分)',
                        id: 'jine_textarea',
                        allowBlank: false,
                        editable:false,
                        name: 'jine_textarea'
                    }
                    ]
                },
            ]
        }],
        buttons : [ {
            text : '提交',
            handler : function() {
                localStore.removeAll();
                var jine=Ext.getCmp("jine_textarea").getValue();
                if(jine.length>0) {

                    for (var i = 0; i < fk_jsonArray.length; i++) {
                        fk_jsonArray[i].shijijine = jine;
                    }
                    Ext.Ajax.request({
                        url : 'financial!querentuikuan.action',
                        actionMethods : 'post',
                        async : false,
                        reader : {
                            type : 'json'
                        },
                        params : {
                            'itemsxjyb' : Ext.encode(fk_jsonArray),
                        },
                        success : function (form, action) {
                            if (action.success) {
                                Ext.example.msg("提示", "确认退款成功");
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });
                                tuikuaninfo_window.hide();
                            }else{
                                Ext.example.msg("提示", "确认退款失败");
                            }

                        },
                        failure : function (form, action) {
                            Ext.example.msg("提示", "错误，请联系管理员！");
                        }
                    });
                    /* }
                 });*/

                }else{
                    Ext.MessageBox.alert("提示","退款金额不能为空");
                }
            }
        },
            {
                text : '取消',
                handler:function(){
                    tuikuaninfo_window.hide();
                }
            } ]
    });

    var tuikuaninfo_window = Ext.create('Ext.window.Window',{
        title : '退款信息',
        closeAction : 'hide',
        modal : true,
        items : tuikuaninfo_form
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
		title : '诺如病毒报告自动化',
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
