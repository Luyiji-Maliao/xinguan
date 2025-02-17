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
         { name:'isgeli'},
         { name:'subjecttype'},
         { name:'comebjtime'},
         { name:'comebjreason'},
         { name:'yuyuedate'},
         { name:'yuyuetime'},
         { name:'caiyangdidian'},
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
         { name:'isagree'},
         { name:'outtradeno'},
         { name:'totalfee'},
         { name:'issuccessed'},
         { name:'yuyuenum'},
         { name:'xgreason'},
         { name:'subjecttype'},
         { name:'cooperationstr'},
         { name:'sampleNo'},
         { name:'qudao'},
         { name:'shicaiyangdian'},
         { name:'tuikuan'},
         { name:'shijijine'},
         { name:'inputTime'},
         { name:'inputName'},
         { name:'updateTime'},
         { name:'updateName'},
         { name:'kaipiaodate'},
         { name:'kaipiaono'},
         { name:'kaipiaostate'},
         { name:'shouxufei'},
         { name:'jinzhangmoney'},
         { name:'collectionmethod'},
         { name:'kaipiaomoney'},
         { name:'huikuanstate'},
         { name:'duihuanma'},
         { name:'samplebindtime'},
         { name:'reportdate'},
         { name:'englishName'},
         { name:'passport'},
         { name:'englishreport'},
     ]
 });
    var shangdiForm=Ext.create('Ext.form.FormPanel',{
        //url:'nucleicacids!request_entity.action',   //多条件跳转（当前页面）功能
        id : 'shangdi_form',
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
                columnWidth : 1,
                items : [{
                    xtype: 'fieldcontainer',
                    fieldLabel: '到样日期',
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
                },

                ]
            }
            ]
        }
        ],

        buttons:[{
            text:'导出',
            handler:function(){
                if(iscaiwu == '是'){
                    Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                    Ext.Ajax.request(
                        {
                            url: 'appointmentinfoxgwx!shangdiExcel.action',
                            timeout: 100000000,
                            params: {
                                'ininputTime':Ext.getCmp("ininputTime").getValue(),
                                'osoinputTime':Ext.getCmp("osoinputTime").getValue()
                            },
                            success: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示",response.responseText,4000);
                                if(response.responseText=="EXCEL已导出"){
                                    window.location.href='schedulesort!downloadfile.action?helpdocs=上地医院.xls';
                                }
                            },
                            failure: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示","导出失败",4000);
                            }
                        });
                }else{
                    Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                    Ext.Ajax.request(
                        {
                            url: 'appointmentinfoxgwx!caiwuExcel.action',
                            timeout: 100000000,
                            params: {
                                'ininputTime':Ext.getCmp("ininputTime").getValue(),
                                'osoinputTime':Ext.getCmp("osoinputTime").getValue()
                            },
                            success: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示",response.responseText,4000);
                                if(response.responseText=="EXCEL已导出"){
                                    window.location.href='schedulesort!downloadfile.action?helpdocs=yuyuecaiwu.xls';
                                }
                            },
                            failure: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示","导出失败",4000);
                            }
                        });
                }
                shangdiWindow.hide();
            }
        },{
            text:'取消',
            handler:function(){
                shangdiWindow.hide();
            }
        },{
            text:'重置',
            handler:function(){
                shangdiForm.form.reset();
            }
        }]
    });

    var shangdiWindow = new Ext.Window({
        title:'导出',
        id:'shangdiWindow',
        closeAction: 'hide',
        width:300,
        autoScroll:true,
        modal:true,
        items:shangdiForm
    });

    var hangangForm=Ext.create('Ext.form.FormPanel',{
        //url:'nucleicacids!request_entity.action',   //多条件跳转（当前页面）功能
        id : 'hangnag_form',
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
                columnWidth : 1,
                items : [{
                    xtype: 'fieldcontainer',
                    fieldLabel: '到样日期',
                    defaults: {
                        xtype     : 'datefield',
                    },
                    items: [
                        {
                            name: 'ininputTime1',
                            format :'Y-m-d',
                            editable:false,
                            id:'ininputTime1'
                        }, {
                            xtype: 'displayfield',
                            value: '至'
                        },
                        {
                            name : 'osoinputTime1',
                            format :'Y-m-d',
                            editable:false,
                            id:'osoinputTime1',
                        }
                    ]
                },

                ]
            }
            ]
        }
        ],

        buttons:[{
            text:'导出',
            handler:function(){
                if(iscaiwu == '是'){
                    Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                    Ext.Ajax.request(
                        {
                            url: 'appointmentinfoxgwx!hangangExcel.action',
                            timeout: 100000000,
                            params: {
                                'ininputTime':Ext.getCmp("ininputTime1").getValue(),
                                'osoinputTime':Ext.getCmp("osoinputTime1").getValue()
                            },
                            success: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示",response.responseText,4000);
                                if(response.responseText=="EXCEL已导出"){
                                    window.location.href='schedulesort!downloadfile.action?helpdocs=邯钢.xls';
                                }
                            },
                            failure: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示","导出失败",4000);
                            }
                        });
                }else{
                    Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                    Ext.Ajax.request(
                        {
                            url: 'appointmentinfoxgwx!downloadExcel.action',
                            timeout: 100000000,
                            params: {
                                'ininputTime':Ext.getCmp("ininputTime1").getValue(),
                                'osoinputTime':Ext.getCmp("osoinputTime1").getValue()
                            },
                            success: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示",response.responseText,4000);
                                if(response.responseText=="EXCEL已导出"){
                                    window.location.href='schedulesort!downloadfile.action?helpdocs=邯钢.xls';
                                }
                            },
                            failure: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示","导出失败",4000);
                            }
                        });
                }
                hangangWindow.hide();
            }
        },{
            text:'取消',
            handler:function(){
                hangangWindow.hide();
            }
        },{
            text:'重置',
            handler:function(){
                hangangForm.form.reset();
            }
        }]
    });
    var hangangWindow = new Ext.Window({
        title:'导出',
        id:'hangangWindow',
        closeAction: 'hide',
        width:300,
        autoScroll:true,
        modal:true,
        items:hangangForm
    });

    var caiwuForm=Ext.create('Ext.form.FormPanel',{
        //url:'nucleicacids!request_entity.action',   //多条件跳转（当前页面）功能
        id : 'caiwuForm',
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
                columnWidth : 1,
                items : [{
                    xtype: 'fieldcontainer',
                    fieldLabel: '单号录入日期',
                    defaults: {
                        xtype     : 'datefield',
                    },
                    items: [
                        {
                            name: 'ininputTime3',
                            format :'Y-m-d',
                            editable:false,
                            id:'ininputTime3'
                        }, {
                            xtype: 'displayfield',
                            value: '至'
                        },
                        {
                            name : 'osoinputTime3',
                            format :'Y-m-d',
                            editable:false,
                            id:'osoinputTime3',
                        }
                    ]
                },

                ]
            }
            ]
        }
        ],

        buttons:[{
            text:'导出',
            handler:function(){
                    Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                    Ext.Ajax.request(
                        {
                            url: 'appointmentinfoxgwx!caiwuExcel.action',
                            timeout: 100000000,
                            params: {
                                'ininputTime':Ext.getCmp("ininputTime3").getValue(),
                                'osoinputTime':Ext.getCmp("osoinputTime3").getValue(),
                                'outtradeno' : Ext.getCmp("searchOuttradeno").getValue(),
                                'openid' : Ext.getCmp("searchOpenid").getValue(),
                                'isfapiao' : Ext.getCmp("searchIsfapiao").getValue(),
                                'caiyangdidian' : Ext.getCmp("searchCaiyangdidian").getValue(),
                                'cooperationstr' : Ext.getCmp("searchCooperationstr").getValue(),
                                'samplebindstarttime' : Ext.getCmp("samplebindstarttime").getValue(),
                                'samplebindendtime' : Ext.getCmp("samplebindendtime").getValue(),
                                'sampleNo' : Ext.getCmp("searchSampleNo").getValue(),
                                'duihuanma' : Ext.getCmp("searchDuihuanma").getValue(),
                                'shicaiyangdian' : Ext.getCmp("searchShicaiyangdian").getValue(),
                                'issuccessed' : Ext.getCmp("searchIssuccessed").getValue(),
                                'shuihao' : Ext.getCmp("searchShuihao").getValue(),
                                'qudao' : Ext.getCmp("searchQudao").getValue(),
                                'huikuanstate' : Ext.getCmp("searchHuikuanstate").getValue(),
                                'kaipiaostate' : Ext.getCmp("searchKaipiaostate").getValue(),
                                'phone' : Ext.getCmp("searchPhone").getValue(),
                                'tuikuan': Ext.getCmp("searchTuikuan").getValue(),
                                'kaipiaono': Ext.getCmp("searchFapiaono").getValue(),
                                'subjecttype': Ext.getCmp("searchSubjecttype").getValue(),
                            },
                            success: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示",response.responseText,4000);
                                if(response.responseText=="EXCEL已导出"){
                                    window.location.href='schedulesort!downloadfile.action?helpdocs=yuyuecaiwu.xls';
                                }
                            },
                            failure: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示","导出失败",4000);
                            }
                        });
                caiwuWindow.hide();
            }
        },{
            text:'取消',
            handler:function(){
                caiwuWindow.hide();
            }
        },{
            text:'重置',
            handler:function(){
                caiwuForm.form.reset();
            }
        }]
    });

    var caiwuWindow = new Ext.Window({
        title:'导出',
        id:'caiwuWindow',
        closeAction: 'hide',
        width:300,
        autoScroll:true,
        modal:true,
        items:caiwuForm
    });

    var huikuan_combox =Ext.create('Ext.form.ComboBox',{
        name:'searchHuikuanstate',
        id:'searchHuikuanstate',
        mode: 'local',
        fieldLabel:'是否已回款',
        triggerAction: 'all',
        store: Ext.create('Ext.data.ArrayStore',{
            idIndex :0,
            fields:[
                {name : 'saveresult'},
                {name : 'showresult'}
            ],
            data :[
                [ '', '' ],
                [ '是', '是' ],
                [ '否', '否' ]
            ]
        }),
        valueField: 'saveresult',
        displayField: 'showresult',
        editable:false
    });

    var fapiao_combox =Ext.create('Ext.form.ComboBox',{
        name:'searchKaipiaostate',
        id:'searchKaipiaostate',
        mode: 'local',
        fieldLabel:'是否已开发票',
        triggerAction: 'all',
        store: Ext.create('Ext.data.ArrayStore',{
            idIndex :0,
            fields:[
                {name : 'saveresult'},
                {name : 'showresult'}
            ],
            data :[
                [ '', '' ],
                [ '是', '是' ],
                [ '否', '否' ]
            ]
        }),
        valueField: 'saveresult',
        displayField: 'showresult',
        editable:false
    });

    var tuikuan_combox =Ext.create('Ext.form.ComboBox',{
        name:'searchTuikuan',
        id:'searchTuikuan',
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
    });

    var subjecttype_combox =Ext.create('Ext.form.ComboBox',{
        name:'searchSubjecttype',
        id:'searchSubjecttype',
        mode: 'local',
        fieldLabel:'受检者类型',
        triggerAction: 'all',
        store: Ext.create('Ext.data.ArrayStore',{
            idIndex :0,
            fields:[
                {name : 'saveresult'},
                {name : 'showresult'}
            ],
            data :[
                [ '', '' ],
                [ '复工', '复工' ],
                [ '外地返京', '外地返京' ],
                [ '住酒店', '住酒店' ],
                [ '住院', '住院' ],
                [ '开学', '开学' ],
                [ '滴滴司机', '滴滴司机' ],
                [ '其他人群', '其他人群' ],
            ]
        }),
        valueField: 'saveresult',
        displayField: 'showresult',
    });


	/****************高级搜索(start)*********************/
	var searchForm = Ext.create('Ext.form.FormPanel', {
		 	id : 'search_form',
			buttonAlign : 'center',
			border : false,
			width : 700,
			autoHeight : true,
			fieldDefaults : {
				labelAlign : 'right',
				labelWidth : 82,
			},
			autoScroll : true,
			items : [{
					layout : 'column',
					autoHeight: true,
					defaults : {
						anchor : '100%',
						bodyStyle : 'border-width:0 0 0 0;',
					},
					items : [{
							layout : 'form',
							columnWidth : .49,
							border:false,
							defaults:{
								xtype:'textfield',
								anchor:'95%'
							},
							items : [{
									id:'searchOuttradeno',
					                name:'outtradeno',
					                xtype:'textfield',
					                fieldLabel:'商户单号'
					            },{
                                    id:'searchYuyuenum',
                                    name:'yuyuenum',
                                    xtype:'textfield',
                                    fieldLabel:'预约号'
                                },{
                                    id:'searchSfz',
                                    name:'sfz',
                                    xtype:'textfield',
                                    fieldLabel:'身份证'
                                },{
					            	id:'searchOpenid',
					                name:'openid',
					                xtype:'textfield',
					                fieldLabel:'微信识别码'
					            },{
					            	id:'searchIsfapiao',
					                name:'isfapiao',
					                xtype:'textfield',
					                fieldLabel:'是否需要发票'
					            },{
					            	id:'searchCaiyangdidian',
					                name:'caiyangdidian',
					                xtype:'textfield',
					                fieldLabel:'采样地点'
					            },{
				                    xtype: 'fieldcontainer',
				                    fieldLabel: '样本绑定时间',
				                    layout : 'hbox',
				                    items: [
				                        {
				                        	id:'samplebindstarttime',
				                            name: 'samplebindstarttime',
				                            xtype : 'datefield',
				                            format :'Y-m-d',
				                            editable:false,
				                            //id:'ininputTime',
				                            flex : 15
				                        }, {
				                            xtype: 'displayfield',
				                            value: '至'
				                        },
				                        {
				                        	id : 'samplebindendtime',
				                            name : 'samplebindendtime',
				                            xtype : 'datefield',
				                            format :'Y-m-d',
				                            editable:false,
				                            //id:'osoinputTime',
				                            flex : 15
				                        }
				                    ]
				                },fapiao_combox,{
					            	id:'searchCooperationstr',
					                name:'cooperationstr',
					                xtype:'textfield',
					                fieldLabel:'cooperationstr'
					            },{
                                    id:'searchFapiaono',
                                    name:'kaipiaono',
                                    xtype:'textfield',
                                    fieldLabel:'发票号'
                                }
							]
						},{
							layout : 'form',
							columnWidth : .49,
							border:false,
							defaults:{
								xtype:'textfield',
								anchor:'95%'
							},
							items : [{
                                    id:'searchName',
                                    name:'name',
                                    xtype:'textfield',
                                    fieldLabel:'客户姓名'
                                },{
									id:'searchSampleNo',
					                name:'sampleNo',
					                xtype:'textfield',
					                fieldLabel:'样本编号'
					            },{
                                    id:'searchPhone',
                                    name:'phone',
                                    xtype:'textfield',
                                    fieldLabel:'手机号'
                                },{
					            	id:'searchDuihuanma',
					                name:'duihuanma',
					                xtype:'textfield',
					                fieldLabel:'兑换码'
					            },{
					            	id:'searchShicaiyangdian',
					                name:'shicaiyangdian',
					                xtype:'textfield',
					                fieldLabel:'实采样点'
					            },{
					            	id:'searchIssuccessed',
					                name:'issuccessed',
					                xtype:'textfield',
					                hidden:true,
					                fieldLabel:'是否付款到账'
					            },{
					            	id:'searchShuihao',
					                name:'shuihao',
					                xtype:'textfield',
					                fieldLabel:'税号'
					            },{
					            	id:'searchQudao',
					                name:'qudao',
					                xtype:'textfield',
					                fieldLabel:'渠道'
					            },huikuan_combox,tuikuan_combox,subjecttype_combox]
						}]
				}],
			buttons : [{
					text : '查询',
					handler : function () {
						main_store.load({
							params : {
								start : 0,
								limit : pageSize,
							}
						});
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
						searchForm.form.reset();
					}
				}
			]

		});

		var searchWindow = Ext.create('Ext.Window', {
			id : "searchWindow",
			closeAction : 'hide',
			title : "高级搜索",
			modal : true,
			items : searchForm

		});

	/****************高级搜索(end)*********************/


    /*********************修改（start）****************************/
    var updateForm = Ext.create('Ext.form.FormPanel',{
        id:'update_form',
        buttonAlign:'center',
        border:false,
        width:900,
        autoHeight:true,
        fieldDefaults:{
            labelAlign:'right',
            labelWidth: 100,
        },
        autoScroll:true,
        items:[{
            layout:'column',
            autoHeight:true,
            defaults:{
                anchor:'100%',
                bodyStyle:'border-width:0 0 0 0;',
            },
            items:[{
                layout:'form',
                columnWidth: .32,
                border:false,
                defaults:{
                    xtype:'textfield',
                    anchor:'95%'
                },
                items:[{
                    id:'updateId',
                    //hidden:true,
                    readOnly:true,
                    name:'id',
                    xtype:'textfield',
                    fieldLabel:'编号'
                },{
                    id:'updateOuttradeno',
                    name:'outtradeno',
                    xtype:'textfield',
                    fieldLabel:'商户编号'
                },{
                    id:'updateName1',//updateForm.getForm().loadRecord(record[0]);首先根据id赋值，所以不能使用updateName,此处是特例
                    name:'name',
                    xtype:'textfield',
                    fieldLabel:'姓名'
                },{
                    id:'updateCaiyangdidian',
                    name:'caiyangdidian',
                    xtype:'textfield',
                    fieldLabel:'预约采样点'
                },{
                    id:'updateYuyuedate',
                    name:'yuyuedate',
                    xtype:'textfield',
                    fieldLabel:'预约日期'
                },{
                    id:'updateYuyuetime',
                    name:'yuyuetime',
                    xtype:'textfield',
                    fieldLabel:'预约时间段'
                },{
                    id:'updateFapiaotaitou',
                    name:'fapiaotaitou',
                    xtype:'textfield',
                    fieldLabel:'发票抬头'
                },{
                    id:'updateFapiaotype',
                    name:'fapiaotype',
                    xtype:'textfield',
                    fieldLabel:'发票类型'
                }]

            },{
                layout:'form',
                columnWidth: .32,
                border:false,
                defaults:{
                    xtype:'textfield',
                    anchor:'95%'
                },
                items:[{
                    id:'updateYuyuenum',
                    name:'yuyuenum',
                    xtype:'textfield',
                    fieldLabel:'预约号'
                },{
                    id:'updateSfz',
                    name:'sfz',
                    xtype:'textfield',
                    fieldLabel:'身份证'
                },{
                    id:'updatePhone',
                    name:'phone',
                    xtype:'textfield',
                    fieldLabel:'联系电话'
                },{
                    id:'updateShicaiyangdian',
                    name:'shicaiyangdian',
                    xtype:'textfield',
                    fieldLabel:'实际采样点'
                },{
                    id:'updateSamplebindtime',
                    name:'samplebindtime',
                    xtype:'textfield',
                    fieldLabel:'样本绑定时间'
                },{
                    id:'updateSending',
                    name:'sending',
                    xtype:'textfield',
                    fieldLabel:'受检者单位'
                },{
                    id:'updateAccessname',
                    name:'accessname',
                    xtype:'textfield',
                    fieldLabel:'发票收件人姓名'
                },{
                    id:'updateAccessphone',
                    name:'accessphone',
                    xtype:'textfield',
                    fieldLabel:'发票收件人电话'
                }]

            },{
                layout:'form',
                columnWidth: .32,
                border:false,
                defaults:{
                    xtype:'textfield',
                    anchor:'95%'
                },
                items:[{
                    id:'updateSampleNo',
                    name:'sampleNo',
                    xtype:'textfield',
                    fieldLabel:'样本编号'
                },{
                    id:'updateIssuccessed',
                    name:'issuccessed',
                    xtype:'textfield',
                    fieldLabel:'是否付款到账'
                },{
                    id:'updateQudao',
                    name:'qudao',
                    xtype:'textfield',
                    fieldLabel:'销售渠道'
                },{
                    id:'updateDuihuanma',
                    name:'duihuanma',
                    xtype:'textfield',
                    fieldLabel:'兑换码'
                },{
                    id:'updateIsfapiao',
                    name:'isfapiao',
                    xtype:'textfield',
                    fieldLabel:'是否需要发票'
                },{
                    id:'updateShuihao',
                    name:'shuihao',
                    xtype:'textfield',
                    fieldLabel:'税号'
                },{
                    id:'updateAccessaddress',
                    name:'accessaddress',
                    xtype:'textfield',
                    fieldLabel:'发票收件人地址'
                },{
                    id:'updateAccessemail',
                    name:'accessemail',
                    xtype:'textfield',
                    fieldLabel:'电子发票邮箱'
                }]

            }]

        }],
        buttons:[{
            text:'提交',
            handler:function(){
                updateForm.getForm().submit({
                    url:'appointmentinfoxgwx!update.action',
                    waitMsg:'正在提交数据',
                    success:function(f,action){
                        if(action.result.success){
                            updateForm.getForm().reset();
                            updateWindow.hide();
                            Ext.example.msg('消息',action.result.msg);
                            main_store.load({
                                params:{
                                    start:returnStart,
                                    limit:pageSize
                                }
                            })

                        }else{
                            Ext.Msg.alert("消息",action.result.msg);
                        }
                    },
                    failure:handFailure
                });


            }
        },{
            text:'取消',
            handler:function(){
                updateForm.getForm().reset();
                updateWindow.hide();
            }
        }]


    });

    var updateWindow = Ext.create("Ext.Window",{
        id:'updateWindow',
        closeAction:'hide',
        title:'修改',
        modal:true,
        items:updateForm
    });


    /**********************修改（end）****************************/

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:23,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
       	 actionMethods : 'post',
         timeout: 100000000,
         url: 'appointmentinfoxgwx.action',
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



        var searchOuttradeno = Ext.getCmp("searchOuttradeno");
        var searchOpenid = Ext.getCmp("searchOpenid");
        var searchIsfapiao = Ext.getCmp("searchIsfapiao");
        var searchCaiyangdidian = Ext.getCmp("searchCaiyangdidian");
        var searchCooperationstr = Ext.getCmp("searchCooperationstr");
        var samplebindstarttime = Ext.getCmp("samplebindstarttime");
        var samplebindendtime = Ext.getCmp("samplebindendtime");
        var searchSampleNo = Ext.getCmp("searchSampleNo");
        var searchDuihuanma = Ext.getCmp("searchDuihuanma");
        var searchShicaiyangdian = Ext.getCmp("searchShicaiyangdian");
        var searchIssuccessed = Ext.getCmp("searchIssuccessed");
        var searchShuihao = Ext.getCmp("searchShuihao");
        var searchQudao = Ext.getCmp("searchQudao");
        var searchKaipiaostate = Ext.getCmp("searchKaipiaostate");
        var searchHuikuanstate = Ext.getCmp("searchHuikuanstate");
        var searchPhone = Ext.getCmp("searchPhone");
        var searouttradeno  = Ext.getCmp("searouttradeno");
        var searchTuikuan = Ext.getCmp("searchTuikuan");
        var searchFapiaono = Ext.getCmp("searchFapiaono");
        var searchSubjecttype = Ext.getCmp("searchSubjecttype");
        if(searchSfz){
            Ext.apply(store.proxy.extraParams, {
                searchSfz:searchSfz.getValue()
            });
        }
        if(searchTuikuan){
            Ext.apply(store.proxy.extraParams, {
                tuikuan:searchTuikuan.getValue()
            });
        }
        if(searchSubjecttype){
            Ext.apply(store.proxy.extraParams, {
                subjecttype:searchSubjecttype.getValue()
            });
        }
        if(searchFapiaono){
            Ext.apply(store.proxy.extraParams, {
                kaipiaono:searchFapiaono.getValue()
            });
        }
        if(searouttradeno){
            Ext.apply(store.proxy.extraParams, {
                outtradeno:searouttradeno.getValue()
            });
        }
        if(searchPhone){
            Ext.apply(store.proxy.extraParams, {
                phone:searchPhone.getValue()
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



        if(searchOuttradeno){
            Ext.apply(store.proxy.extraParams, {
                outtradeno:searchOuttradeno.getValue()
            });
        }
        if(searchOpenid){
            Ext.apply(store.proxy.extraParams, {
                openid:searchOpenid.getValue()
            });
        }
        if(searchIsfapiao){
            Ext.apply(store.proxy.extraParams, {
                isfapiao:searchIsfapiao.getValue()
            });
        }
        if(searchCaiyangdidian){
            Ext.apply(store.proxy.extraParams, {
                caiyangdidian:searchCaiyangdidian.getValue()
            });
        }
        if(searchCooperationstr){
            Ext.apply(store.proxy.extraParams, {
                cooperationstr:searchCooperationstr.getValue()
            });
        }
        if(samplebindstarttime){
            Ext.apply(store.proxy.extraParams, {
                samplebindstarttime:samplebindstarttime.getValue()
            });
        }
        if(samplebindendtime){
            Ext.apply(store.proxy.extraParams, {
                samplebindendtime:samplebindendtime.getValue()
            });
        }
        if(searchSampleNo){
            Ext.apply(store.proxy.extraParams, {
                sampleNo:searchSampleNo.getValue()
            });
        }
        if(searchDuihuanma){
            Ext.apply(store.proxy.extraParams, {
                duihuanma:searchDuihuanma.getValue()
            });
        }
        if(searchShicaiyangdian){
            Ext.apply(store.proxy.extraParams, {
                shicaiyangdian:searchShicaiyangdian.getValue()
            });
        }
        if(searchIssuccessed){
            Ext.apply(store.proxy.extraParams, {
                issuccessed:searchIssuccessed.getValue()
            });
        }
        if(searchShuihao){
            Ext.apply(store.proxy.extraParams, {
                shuihao:searchShuihao.getValue()
            });
        }
        if(searchQudao){
            Ext.apply(store.proxy.extraParams, {
                qudao:searchQudao.getValue()
            });
        }

        if(searchKaipiaostate){
            Ext.apply(store.proxy.extraParams, {
                kaipiaostate:searchKaipiaostate.getValue()
            });
        }

        if(searchHuikuanstate){
            Ext.apply(store.proxy.extraParams, {
                huikuanstate:searchHuikuanstate.getValue()
            });
        }

		//Ext.apply(store.proxy.extraParams, {filter_LIKES_department00deptName_OR_department00description:'IT'});

		});

		 main_store.loadPage(1);

    var caiyangdian = [];
	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid =Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : '新冠预约补录',
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
            header:'商户单号',
            width: 170,
            dataIndex:'outtradeno'
        }, {
            header : '姓名',
            dataIndex : 'name'
        },{
                header : '英文名字',
                dataIndex : 'englishName'
            },{
            header : '样本编号',
            dataIndex : 'sampleNo'
        },{
            header : '预约号',
            dataIndex : 'yuyuenum'
        },{
            header : '身份证号',
            width: 170,
            dataIndex : 'sfz'
        },{
            header : '护照号',
            dataIndex : 'passport'
        },{
            header:'联系电话',
            dataIndex:'phone'
        },{
            header:'受检者类型',
            dataIndex:'subjecttype'
        },{
            header:'预约采样点',
            width: 150,
            dataIndex:'caiyangdidian'
        },{
            header:'预约日期',
            dataIndex:'yuyuedate'
        },{
            header:'预约时间段',
            dataIndex:'yuyuetime'
        },{
            header:'实际采样点',
            width: 150,
            dataIndex:'shicaiyangdian'
        },{
            header:'受检者单位',
            width: 150,
            dataIndex:'sending'
        },{
            header:'样本绑定时间',
            width: 150,
            dataIndex:'samplebindtime'
        },{
            header:'是否付款到账',
            dataIndex:'issuccessed'
        },{
            header : '是否需要英文报告',
            dataIndex : 'englishreport'
        },{
            header:'销售渠道',
            dataIndex:'qudao'
        },{
            header:'兑换码',
            dataIndex:'duihuanma'
        },{
            header:'是否需要发票',
            dataIndex:'isfapiao',
        },{
            header:'税号',
            width: 150,
            dataIndex:'shuihao'
        },{
            header:'发票抬头',
            dataIndex:'fapiaotaitou'
        },{
            header:'发票类型',
            dataIndex:'fapiaotype'
        },{
            header:'发票收件人姓名',
            dataIndex:'accessname',
            width: 160,
        },{
            header:'发票收件人电话',
            dataIndex:'accessphone',
            width: 160,
        },{
            header:'发票收件人地址',
            dataIndex:'accessaddress',
            width: 160,
        },{
            header:'电子发票邮箱',
            dataIndex:'accessemail',
            width: 160,
        },{
            header:'注册地址',
            dataIndex:'zhucedizhi'
        },{
            header:'注册电话',
            dataIndex:'zhucedianhua'
        },{
            header:'开户行',
            dataIndex:'kaihuyinhang'
        },{
            header:'银行账号',
            dataIndex:'yinhangzhanghao'
        },{
            header:'退款状态',
            dataIndex:'tuikuan'
        },{
            header:'实际退款金额',
            dataIndex:'shijijine'
        },{
            header:'报告时间',
            dataIndex:'reportdate'
        },{
            header:'是否已回款',
            dataIndex:'huikuanstate'
        },{
            header:'手续费',
            dataIndex:'shouxufei'
        },{
            header:'进账金额',
            dataIndex:'jinzhangmoney'
        },{
            header:'回款方式',
            dataIndex:'collectionmethod'
        },{
            header:'开票金额',
            dataIndex:'kaipiaomoney'
        },{
            header:'发票号',
            dataIndex:'kaipiaono'
        },{
            header:'开票时间',
            dataIndex:'kaipiaodate'
        },{
            header:'录入人',
            width: 120,
            dataIndex:'inputName'
        },{
            header:'录入时间',
            width: 140,
            dataIndex:'inputTime'
        },{
            header:'更新人',
            width: 120,
            dataIndex:'updateName'
        },{
            header:'更新时间',
            width: 140,
            dataIndex:'updateTime'
        }],
		listeners:{

		  } ,
		tbar : [
		{
			text : '预约补录',
			id:'tbar0',
			hidden:true,
			icon : 'img/add.png',
			handler : function() {
                xjyb_window.show();
			}
		},{
                text : '预约补录模板',
                icon : 'img/0114.gif',
                id:'tbar1',
                hidden:true,
                handler:function(){
                    window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/yuyue.xls';
                }
            },{
                text : '申请退款',
                id:'tbar2',
                hidden:true,
                icon : 'img/add.png',
                handler : function() {
                    var record = grid.getSelectionModel().getSelection();
                    fk_jsonArray = [];
                    /*Ext.MessageBox.confirm("请确认","选中的信息是否申请退款？",function(button,text){
                        if(button=="yes"){*/
                    if(record.length<=0){
                        Ext.example.msg("提示","请选择一条要操作的数据");
                        return;
                    }
                    for (var i = 0; i < record.length; i++) {
                        if(record[i].data.tuikuan == '已退款' || record[i].data.tuikuan == '申请退款'){
                            Ext.example.msg("提示", '申请退款失败,姓名为“'+record[i].data.name+"”不能重复申请退款");
                            return;
                        }else{
                            fk_jsonArray.push(record[i].data);
                        }

                        if(record[i].data.sampleNo != null && record[i].data.sampleNo != ''){
                            Ext.example.msg("提示", "姓名为“"+record[i].data.name+"”的订单已生成报告,不能申请退款",3000);
                            return;
                        }

                    }
                    tuikuaninfo_window.show();
                }
            },{
                text : '导出上地医院客户信息',
                id:'tbar3',
                hidden:true,
                icon:'img/up.gif',
                handler:function(){
                    var xjyb_jsonArray = [];
                    iscaiwu = '是';
                    var record =grid.getSelectionModel().getSelection();

                    for(var i=0;i<record.length;i++){
                        xjyb_jsonArray.push(record[i].data);
                    }
                    if(xjyb_jsonArray.length>0){
                        Ext.Ajax.request(
                            {
                                url: 'appointmentinfoxgwx!shangdiExcel.action',
                                params: {'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                success: function (response) {
                                    if(response.responseText=="否"){
                                        Ext.example.msg("提示", "最多导出30000条数据！");
                                        return;
                                    }
                                    Ext.example.msg("提示",response.responseText,4000);
                                    if(response.responseText=="EXCEL已导出"){
                                        window.location.href='schedulesort!downloadfile.action?helpdocs=上地医院.xls';
                                    }
                                },
                                failure: handFailure,


                            });
                    }else{
                        shangdiWindow.show();
                    }
                }
            },{
                text : '导出邯钢宾馆客户信息',
                id:'tbar4',
                hidden:true,
                icon:'img/up.gif',
                handler:function(){
                    var xjyb_jsonArray = [];
                    iscaiwu = '是';
                    var record =grid.getSelectionModel().getSelection();

                    for(var i=0;i<record.length;i++){
                        xjyb_jsonArray.push(record[i].data);
                    }
                    if(xjyb_jsonArray.length>0){
                        Ext.Ajax.request(
                            {
                                url: 'appointmentinfoxgwx!hangangExcel.action',
                                params: {'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                success: function (response) {
                                    if(response.responseText=="否"){
                                        Ext.example.msg("提示", "最多导出30000条数据！");
                                        return;
                                    }
                                    Ext.example.msg("提示",response.responseText,4000);
                                    if(response.responseText=="EXCEL已导出"){
                                        window.location.href='schedulesort!downloadfile.action?helpdocs=邯钢.xls';
                                    }
                                },
                                failure: handFailure,


                            });
                    }else{
                        hangangWindow.show();
                    }
                }
            },{
                text : '导出财务信息',
                id:'tbar5',
                hidden:true,
                icon:'img/up.gif',
                handler:function(){
                    var xjyb_jsonArray = [];
                    iscaiwu = '否';
                    var record =grid.getSelectionModel().getSelection();

                    for(var i=0;i<record.length;i++){
                        xjyb_jsonArray.push(record[i].data);
                    }
                    if(xjyb_jsonArray.length>0){
                        Ext.Ajax.request(
                            {
                                url: 'appointmentinfoxgwx!caiwuExcel.action',
                                params: {'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                success: function (response) {
                                    if(response.responseText=="否"){
                                        Ext.example.msg("提示", "最多导出30000条数据！");
                                        return;
                                    }
                                    Ext.example.msg("提示",response.responseText,4000);
                                    if(response.responseText=="EXCEL已导出"){
                                        window.location.href='schedulesort!downloadfile.action?helpdocs=yuyuecaiwu.xls';
                                    }
                                },
                                failure: handFailure,


                            });
                    }else{
                        Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                        Ext.Ajax.request(
                            {
                                url: 'appointmentinfoxgwx!caiwuExcel.action',
                                timeout: 100000000,
                                params: {
                                    'searchYuyuenum' : Ext.getCmp("searchYuyuenum").getValue(),
                                    'searchSfz' : Ext.getCmp("searchSfz").getValue(),
                                    'searchName' : Ext.getCmp("searchName").getValue(),
                                    'ininputTime':Ext.getCmp("ininputTime3").getValue(),
                                    'osoinputTime':Ext.getCmp("osoinputTime3").getValue(),
                                    'outtradeno' : Ext.getCmp("searchOuttradeno").getValue(),
                                    'openid' : Ext.getCmp("searchOpenid").getValue(),
                                    'isfapiao' : Ext.getCmp("searchIsfapiao").getValue(),
                                    'caiyangdidian' : Ext.getCmp("searchCaiyangdidian").getValue(),
                                    'cooperationstr' : Ext.getCmp("searchCooperationstr").getValue(),
                                    'samplebindstarttime' : Ext.getCmp("samplebindstarttime").getValue(),
                                    'samplebindendtime' : Ext.getCmp("samplebindendtime").getValue(),
                                    'sampleNo' : Ext.getCmp("searchSampleNo").getValue(),
                                    'duihuanma' : Ext.getCmp("searchDuihuanma").getValue(),
                                    'shicaiyangdian' : Ext.getCmp("searchShicaiyangdian").getValue(),
                                    'issuccessed' : Ext.getCmp("searchIssuccessed").getValue(),
                                    'shuihao' : Ext.getCmp("searchShuihao").getValue(),
                                    'qudao' : Ext.getCmp("searchQudao").getValue(),
                                    'huikuanstate' : Ext.getCmp("searchHuikuanstate").getValue(),
                                    'kaipiaostate' : Ext.getCmp("searchKaipiaostate").getValue(),
                                    'phone' : Ext.getCmp("searchPhone").getValue(),
                                    'searouttradeno' : Ext.getCmp("searouttradeno").getValue(),
                                    'tuikuan': Ext.getCmp("searchTuikuan").getValue(),
                                    'kaipiaono': Ext.getCmp("searchFapiaono").getValue(),
                                    'subjecttype': Ext.getCmp("searchSubjecttype").getValue(),
                                },
                                success: function (response) {
                                    Ext.Msg.hide();
                                    if(response.responseText=="否"){
                                        Ext.example.msg("提示","最多导出30000条数据！",4000);
                                       return;
                                    }
                                    Ext.example.msg("提示",response.responseText,4000);
                                    if(response.responseText=="EXCEL已导出"){
                                        window.location.href='schedulesort!downloadfile.action?helpdocs=yuyuecaiwu.xls';
                                    }
                                },
                                failure: function (response) {
                                    Ext.Msg.hide();
                                    Ext.example.msg("提示","导出失败",4000);
                                }
                            });
                    }
                }
            },{
            	text : '高级搜索',
                id:'tbar6',
                hidden:true,
                icon:'img/search.png',
                handler:function(){
                    searchWindow.show();
                }
            },{
                text : '全部',
                id:'tbar7',
                hidden:true,
                icon:'img/search.png',
                handler:function(){
                    Ext.getCmp(this.id).hide();
                    Ext.getCmp('searchIssuccessed').setValue('全部');
                    main_store.loadPage(1);
                    Ext.getCmp('yifukuan').show();
                }
             },{
                text : '已付款',
                id:'yifukuan',
                hidden:true,
                icon:'img/search.png',
                handler:function(){
                    Ext.getCmp(this.id).hide();
                    Ext.getCmp('searchIssuccessed').setValue('是');
                    main_store.loadPage(1);
                    Ext.getCmp('tbar7').show();
                }
             },{
                text : '批量开发票',
                id:'tbar8',
                hidden:true,
                icon:'img/add.png',
                handler:function(){
                    kaipiao_window.show();
                }
            },{
                text : '批量回款',
                id:'tbar9',
                hidden:true,
                icon:'img/add.png',
                handler:function(){
                    huikuan_window.show();
                }
            },{
                text:'修改',
                id:'tbar10',
                hidden:true,
                icon:'img/record.gif',
                handler:function(){
                    var record =grid.getSelectionModel().getSelection();
                    console.log(record[0]);
                    if(record.length==1){
                        updateForm.getForm().loadRecord(record[0]);
                        updateWindow.show();
                    }else{
                        Ext.example.msg('提示','请选择一条要修改的数据');
                    }
                }

            },{
                text : '导出',
                id:'tbar11',
                hidden:true,
                icon:'img/up.gif',
                handler:function(){
                    var xjyb_jsonArray = [];
                    iscaiwu = '否';
                    var record =grid.getSelectionModel().getSelection();

                    for(var i=0;i<record.length;i++){
                        xjyb_jsonArray.push(record[i].data);
                    }
                    if(xjyb_jsonArray.length>0){
                        Ext.Ajax.request(
                            {
                                url: 'appointmentinfoxgwx!yuyueExcel.action',
                                params: {'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                success: function (response) {
                                    if(response.responseText=="否"){
                                        Ext.example.msg("提示", "最多导出30000条数据！");
                                        return;
                                    }
                                    Ext.example.msg("提示",response.responseText,4000);
                                    if(response.responseText=="EXCEL已导出"){
                                        window.location.href='schedulesort!downloadfile.action?helpdocs=yuyueinfo.xls';
                                    }
                                },
                                failure: handFailure,


                            });
                    }else{
                        Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                        Ext.Ajax.request(
                            {
                                url: 'appointmentinfoxgwx!yuyueExcel.action',
                                timeout: 100000000,
                                params: {
                                    'searchYuyuenum' : Ext.getCmp("searchYuyuenum").getValue(),
                                    'searchSfz' : Ext.getCmp("searchSfz").getValue(),
                                    'searchName' : Ext.getCmp("searchName").getValue(),
                                    'ininputTime':Ext.getCmp("ininputTime3").getValue(),
                                    'osoinputTime':Ext.getCmp("osoinputTime3").getValue(),
                                    'outtradeno' : Ext.getCmp("searchOuttradeno").getValue(),
                                    'openid' : Ext.getCmp("searchOpenid").getValue(),
                                    'isfapiao' : Ext.getCmp("searchIsfapiao").getValue(),
                                    'caiyangdidian' : Ext.getCmp("searchCaiyangdidian").getValue(),
                                    'cooperationstr' : Ext.getCmp("searchCooperationstr").getValue(),
                                    'samplebindstarttime' : Ext.getCmp("samplebindstarttime").getValue(),
                                    'samplebindendtime' : Ext.getCmp("samplebindendtime").getValue(),
                                    'sampleNo' : Ext.getCmp("searchSampleNo").getValue(),
                                    'duihuanma' : Ext.getCmp("searchDuihuanma").getValue(),
                                    'shicaiyangdian' : Ext.getCmp("searchShicaiyangdian").getValue(),
                                    'issuccessed' : Ext.getCmp("searchIssuccessed").getValue(),
                                    'shuihao' : Ext.getCmp("searchShuihao").getValue(),
                                    'qudao' : Ext.getCmp("searchQudao").getValue(),
                                    'huikuanstate' : Ext.getCmp("searchHuikuanstate").getValue(),
                                    'kaipiaostate' : Ext.getCmp("searchKaipiaostate").getValue(),
                                    'phone' : Ext.getCmp("searchPhone").getValue(),
                                    'searouttradeno' : Ext.getCmp("searouttradeno").getValue(),
                                    'tuikuan': Ext.getCmp("searchTuikuan").getValue(),
                                    'kaipiaono': Ext.getCmp("searchFapiaono").getValue(),
                                    'subjecttype': Ext.getCmp("searchSubjecttype").getValue(),
                                },
                                success: function (response) {
                                    Ext.Msg.hide();
                                    if(response.responseText=="否"){
                                        Ext.example.msg("提示","最多导出30000条数据！",4000);
                                        return;
                                    }
                                    Ext.example.msg("提示",response.responseText,4000);
                                    if(response.responseText=="EXCEL已导出"){
                                        window.location.href='schedulesort!downloadfile.action?helpdocs=yuyueinfo.xls';
                                    }
                                },
                                failure: function (response) {
                                    Ext.Msg.hide();
                                    Ext.example.msg("提示","导出失败",4000);
                                }
                            });
                    }
                }
            },{
                text:'开发票',
                id:'tbar12',
                hidden:true,
                icon:'img/add.png',
                handler:function(){
                    var record = grid.getSelectionModel().getSelection();
                    if(record.length<=0){
                        Ext.example.msg("提示","请选择一条要操作的数据");
                        return;
                    }
                    fk_jsonArray = [];
                    /*Ext.MessageBox.confirm("请确认","选中的信息是否申请退款？",function(button,text){
                        if(button=="yes"){*/
                    for (var i = 0; i < record.length; i++) {
                        fk_jsonArray.push(record[i].data);
                    }
                    onekaipiao_window.show();
                }

            },{
                id:'searouttradeno',
                xtype:'textfield',
                width:220,
                emptyText:'请输入要查找的商户单号',
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
                var nullno="";
                var i;
                var j;

                if(len.length>0){
                    for(var i=0;i<len.length;i++){
                        //excel表格同一行的多个cell是以空格 分割的，此处以空格为单位对字符串做 拆分操作。。
                        trStr = len[i].split("\t");
                        /*for(j=0;j<len.length-1;j++){
                            trStr2 = len[j].split("\t");
                            if(trStr[1].trim()==trStr2[1].trim()){
                                if(i!=j){
                                    Ext.example.msg("提示","兑换码重复:"+trStr[1].trim());
                                    return;
                                }
                            }
                        }*/

                        if(trStr[0].trim() == ''){
                            Ext.example.msg("提示","渠道不能为空:");
                            return;
                        }

                        if(trStr[1].trim() == ''){
                            Ext.example.msg("提示","商户单号不能为空:");
                            return;
                        }


                        localStore.insert(0,{
                            qudao:trStr[0].trim(),
                            outtradeno:trStr[1].trim(),//商户单号，可以重复
                            phone:trStr[2].trim(),
                        });
                    };
                }

                for(i=0;i<localStore.getCount();i++){
                    var record = localStore.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }

                xjyb_form.getForm().submit({
                    url: 'appointmentinfoxgwx!saveAll.action',
                    timeout: 100000000,
                    async: false,
                    params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    success: function(f, action) {
                        if (action.result.success) {
                            if(action.result.msg.indexOf("添加成功")!=-1){
                                Ext.example.msg('消息', action.result.msg);
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });


                                xjyb_window.hide();
                            }else{
                                var resultmsg=action.result.msg;
                            }
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

    kaipiao_form = Ext.create('Ext.form.Panel', {
        labelWidth : 50,
        labelAlign : 'right',
        buttonAlign : 'center',
        //width :600,
        autoHeight:true,
        items : [{
            width:500,
            height:300,
            id:'kaipiao_textarea',
            xtype : 'textarea',
            name : 'textarea',
            emptyText: '温馨提示：可直接复制EXCEL表中的对应列到此处，格式如下：\n样本编号-开票号-开票时间(2020-01-01)',
        }],
        buttons : [ {
            text : '提交',
            handler : function() {
                localPiao.removeAll();
                var str=Ext.getCmp("kaipiao_textarea").getValue().replace(/\n$/,"");
                var len = str.split("\n");//获取数据
                var xjyb_jsonArray=[];
                var trStr;

                if(len.length>0){
                    for(var i=0;i<len.length;i++){
                        trStr = len[i].split("\t");
                        localPiao.insert(0,{
                            sampleNo:trStr[0].trim(),
                            kaipiaono:trStr[1].trim(),
                            kaipiaodate:trStr[2].trim(),
                        });
                    };
                }

                for(i=0;i<localPiao.getCount();i++){
                    var record = localPiao.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }

                xjyb_form.getForm().submit({
                    url: 'fapiao!iskaipiao.action',
                    params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    timeout: 100000000,
                    success: function(f, action) {
                        if (action.result.msg.length>0) {
                            var m = "样本编号为"+action.result.msg+"已开发票是否更新开票信息?"
                            Ext.MessageBox.confirm("提示",m,function( button,text ){
                                    if( button == 'yes'){
                                        xjyb_form.getForm().submit({
                                            url: 'fapiao!kaipiao.action',
                                            timeout: 100000000,
                                            async: false,
                                            params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                            waitMsg : '正在提交数据',
                                            success: function(f, action) {
                                                if (action.result.success) {
                                                    if(action.result.msg =='开票成功'){
                                                        Ext.example.msg("提示",action.result.msg,2000);
                                                    }else{
                                                        Ext.Msg.alert('消息',  action.result.msg);
                                                    }
                                                    main_store.load({
                                                        params: {
                                                            start:returnStart,
                                                            limit:pageSize
                                                        }
                                                    });
                                                    kaipiao_window.hide();
                                                }else{
                                                    Ext.Msg.alert('消息',  action.result.msg);
                                                }
                                            },
                                            failure:handFailure
                                        });
                                    }else{
                                        kaipiao_window.hide();
                                    }
                                }
                            );

                        }else{
                            xjyb_form.getForm().submit({
                                url: 'fapiao!kaipiao.action',
                                timeout: 100000000,
                                async: false,
                                params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                waitMsg : '正在提交数据',
                                success: function(f, action) {
                                    if (action.result.success) {
                                        if(action.result.msg =='开票成功'){
                                            Ext.example.msg("提示",action.result.msg,2000);
                                        }else{
                                            Ext.Msg.alert('消息',  action.result.msg);
                                        }
                                        main_store.load({
                                            params: {
                                                start:returnStart,
                                                limit:pageSize
                                            }
                                        });
                                        kaipiao_window.hide();
                                    }else{
                                        Ext.Msg.alert('消息',  action.result.msg);
                                    }
                                },
                                failure:handFailure
                            });
                        }
                    },
                    failure:handFailure
                });
            }
        },
            {
                text : '取消',
                handler:function(){

                    kaipiao_window.hide();
                }
            } ]
    });

    huikuan_form = Ext.create('Ext.form.Panel', {
        labelWidth : 50,
        labelAlign : 'right',
        buttonAlign : 'center',
        //width :600,
        autoHeight:true,
        items : [{
            width:500,
            height:300,
            id:'huikuan_textarea',
            xtype : 'textarea',
            name : 'textarea',
            emptyText: '温馨提示：可直接复制EXCEL表中的对应列到此处，格式如下：\n样本编号-手续费-进账金额-回款方式-开票金额',
        }],
        buttons : [ {
            text : '提交',
            handler : function() {
                localHui.removeAll();
                var str=Ext.getCmp("huikuan_textarea").getValue().replace(/\n$/,"");
                var len = str.split("\n");//获取数据
                var xjyb_jsonArray=[];
                var trStr;

                if(len.length>0){
                    for(var i=0;i<len.length;i++){
                        trStr = len[i].split("\t");

                        localHui.insert(0,{
                            sampleNo:trStr[0].trim(),
                            shouxufei:trStr[1].trim(),
                            jinzhangmoney:trStr[2].trim(),
                            collectionmethod:trStr[3].trim(),
                            kaipiaomoney:trStr[4].trim(),
                        });
                    };
                }

                for(i=0;i<localHui.getCount();i++){
                    var record = localHui.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }

                xjyb_form.getForm().submit({
                    url: 'huikuan!ishuikuan.action',
                    params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    timeout: 100000000,
                    success: function(f, action) {
                        if (action.result.msg.length>0) {
                            var m = "样本编号为"+action.result.msg+"已回款是否更新回款信息?"
                            Ext.MessageBox.confirm("提示",m,function( button,text ){
                                    if( button == 'yes'){
                                        xjyb_form.getForm().submit({
                                            url: 'huikuan!huikuan.action',
                                            timeout: 100000000,
                                            async: false,
                                            params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                            waitMsg : '正在提交数据',
                                            success: function(f, action) {
                                                if (action.result.success) {
                                                    if(action.result.msg =='回款成功'){
                                                        Ext.example.msg("提示",action.result.msg,2000);
                                                    }else{
                                                        Ext.Msg.alert('消息',  action.result.msg);
                                                    }
                                                    main_store.load({
                                                        params: {
                                                            start:returnStart,
                                                            limit:pageSize
                                                        }
                                                    });
                                                    huikuan_window.hide();
                                                }else{
                                                    Ext.Msg.alert('消息',  action.result.msg);
                                                }
                                            },
                                            failure:handFailure
                                        });
                                    }else{
                                        huikuan_window.hide();
                                    }
                                }
                            );

                        }else{
                            xjyb_form.getForm().submit({
                                url: 'huikuan!huikuan.action',
                                timeout: 100000000,
                                async: false,
                                params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                waitMsg : '正在提交数据',
                                success: function(f, action) {
                                    if (action.result.success) {
                                        if(action.result.msg =='回款成功'){
                                            Ext.example.msg("提示",action.result.msg,2000);
                                        }else{
                                            Ext.Msg.alert('消息',  action.result.msg);
                                        }
                                        main_store.load({
                                            params: {
                                                start:returnStart,
                                                limit:pageSize
                                            }
                                        });
                                        huikuan_window.hide();
                                    }else{
                                        Ext.Msg.alert('消息',  action.result.msg);
                                    }
                                },
                                failure:handFailure
                            });
                        }
                    },
                    failure:handFailure
                });

            }
        },
            {
                text : '取消',
                handler:function(){

                    huikuan_window.hide();
                }
            } ]
    });

    onekaipiao_form = Ext.create('Ext.form.Panel', {
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
                        fieldLabel: '发票号',
                        id: 'fapiao_textarea',
                        allowBlank: false,
                        editable:false,
                        name: 'fapiao_textarea'
                    }
                    ]
                }, {
                    columnWidth: .9,
                    layout: 'form',
                    border: false,
                    editable:false,
                    defaults: {
                        xtype: 'datefield'
                    },
                    items: [{
                        fieldLabel: '发票时间',
                        id: 'fapiaodate_textarea',
                        format :'Y-m-d',
                        allowBlank: false,
                        editable:false,
                        name: 'fapiaodate_textarea'
                    }
                    ]
                },
            ]
        }],
        buttons : [ {
            text : '提交',
            handler : function() {
                localStore.removeAll();
                var fapiaodate = Ext.getCmp("fapiaodate_textarea").getValue();
                var fapiaono = Ext.getCmp("fapiao_textarea").getValue();
                if(fapiaono.length>0 && fapiaodate.toString().length>0) {
                    Ext.Ajax.request({
                        url : 'fapiao!onekaipiao.action',
                        actionMethods : 'post',
                        async : false,
                        reader : {
                            type : 'json'
                        },
                        params : {
                            'itemsxjyb' : Ext.encode(fk_jsonArray),
                            'kaipiaodate' : fapiaodate,
                            'kaipiaono' : fapiaono,
                        },
                        success : function (form, action) {

                            if (action.success) {
                                Ext.example.msg("提示", "开票成功");
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });
                                onekaipiao_window.hide();
                            }else{
                                Ext.example.msg("提示", "开票失败");
                            }

                        },
                        failure : function (form, action) {
                            Ext.example.msg("提示", "错误，请联系管理员！");
                        }
                    });
                    /* }
                 });*/

                }else{
                    Ext.MessageBox.alert("提示","发票号和发票时间不能为空");
                }
            }
        },
            {
                text : '取消',
                handler:function(){
                    onekaipiao_window.hide();
                }
            } ]
    });

    tuikuaninfo_form = Ext.create('Ext.form.Panel', {
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
                            fieldLabel: '申请退款金额(分)',
                            id: 'jine_textarea',
                            allowBlank: false,
                            editable:false,
                            name: 'jine_textarea'
                        }
                    ]
                }, {
                    columnWidth: .9,
                    layout: 'form',
                    border: false,
                    defaults: {
                        xtype: 'textarea'
                    },
                    items: [{
                        fieldLabel: '申请退款原因',
                        id: 'tuikuaninfo_textarea',
                        allowBlank: false,
                        editable:false,
                        name: 'tuikuaninfo_textarea'
                    }
                    ]
                },
            ]
        }],
        buttons : [ {
            text : '提交',
            handler : function() {
                localStore.removeAll();
                var yuanyin=Ext.getCmp("tuikuaninfo_textarea").getValue();
                var jine=Ext.getCmp("jine_textarea").getValue();
                if(jine.length>0) {

                    Ext.Ajax.request({
                        url : 'financial!shenqingtuikuan.action',
                        actionMethods : 'post',
                        async : false,
                        reader : {
                            type : 'json'
                        },
                        params : {
                            'itemsxjyb' : Ext.encode(fk_jsonArray),
                            'tuikuaninfo' : yuanyin,
                            'tuikuanjineinfo' : jine,
                        },
                        success : function (form, action) {
                            if (action.success) {
                                Ext.example.msg("提示", "申请退款成功,等待确认");
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });
                                tuikuaninfo_window.hide();
                            }else{
                                Ext.example.msg("提示", "申请退款失败");
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

    /*---------------------------批量添加---------------------------*/

    Ext.define('Hui',{
        extend : 'Ext.data.Model',
        fields   : [
            { name:'sampleNo',type:'string'},
            { name:'shouxufei',type:'string'},
            { name:'jinzhangmoney',type:'string'},
            { name:'collectionmethod',type:'string'},
            { name:'kaipiaomoney',type:'string'},
        ]
    });

    Ext.define('Piao',{
        extend : 'Ext.data.Model',
        fields   : [
            { name:'sampleNo',type:'string'},
            { name:'kaipiaono',type:'string'},
            { name:'kaipiaodate',type:'string'},
        ]
    });

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
            { name:'subjecttype',type:'string'},
            { name:'isgeli',type:'string'},
            { name:'comebjtime',type:'string'},
            { name:'comebjreason',type:'string'},
            { name:'yuyuedate',type:'string'},
            { name:'yuyuetime',type:'string'},
            { name:'caiyangdidian',type:'string'},
            { name:'isfapiao',type:'string'},
            { name:'shuihao',type:'string'},
            { name:'fapiaotaitou',type:'string'},
            { name:'zhucedizhi',type:'string'},
            { name:'zhucedianhua',type:'string'},
            { name:'kaihuyinhang',type:'string'},
            { name:'yinhangzhanghao',type:'string'},
            { name:'fapiaotype',type:'string'},
            { name:'accessname',type:'string'},
            { name:'accessphone',type:'string'},
            { name:'accessaddress',type:'string'},
            { name:'accessemail',type:'string'},
            { name:'isagree',type:'string'},
            { name:'outtradeno',type:'string'},
            { name:'totalfee',type:'string'},
            { name:'issuccessed',type:'string'},
            { name:'yuyuenum',type:'string'},
            { name:'xgreason',type:'string'},
            { name:'subjecttype',type:'string'},
            { name:'cooperationstr',type:'string'},
            { name:'sampleNo',type:'string'},
            { name:'qudao',type:'string'},
            { name:'shicaiyangdian',type:'string'},
            { name:'shijijine',type:'string'},
            { name:'inputTime',type:'string'},
            { name:'inputName',type:'string'},
            { name:'updateTime',type:'string'},
            { name:'updateName',type:'string'},
            { name:'tuikuan',type:'string'},
            { name:'kaipiaodate',type:'string'},
            { name:'kaipiaono',type:'string'},
            { name:'kaipiaostate',type:'string'},
            { name:'shouxufei',type:'string'},
            { name:'jinzhangmoney',type:'string'},
            { name:'collectionmethod',type:'string'},
            { name:'kaipiaomoney',type:'string'},
            { name:'huikuanstate',type:'string'},
            { name:'reportdate',type:'string'},
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

    var localPiao = Ext.create('Ext.data.Store',{
        model:Piao
        // data : localData1
    });

    var localHui = Ext.create('Ext.data.Store',{
        model:Hui
        // data : localData1
    });

    xjyb_window = Ext.create('Ext.window.Window',{
        title : '批量添加',
        closeAction : 'hide',
        modal : true,
        items : xjyb_form
    });

    kaipiao_window = Ext.create('Ext.window.Window',{
        title : '批量开发票',
        closeAction : 'hide',
        modal : true,
        items : kaipiao_form
    });
    huikuan_window = Ext.create('Ext.window.Window',{
        title : '批量回款',
        closeAction : 'hide',
        modal : true,
        items : huikuan_form
    });

    tuikuaninfo_window = Ext.create('Ext.window.Window',{
        title : '退款信息',
        closeAction : 'hide',
        modal : true,
        items : tuikuaninfo_form
    });

    onekaipiao_window = Ext.create('Ext.window.Window',{
        title : '发票信息',
        closeAction : 'hide',
        modal : true,
        items : onekaipiao_form
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
    var fk_jsonArray = [];
 
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
