<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新冠团体录入</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="诺如病毒报告自动化">
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
         {name:'id'},
         {name:'tuanname'},
         {name:'yuyuedate'},
         {name:'yuyuetime'},
         {name:'caiyangdidian'},
         {name:'isfapiao'},
         {name:'fapiaotaitou'},
         {name:'shuihao'},
         {name:'zhucedizhi'},
         {name:'zhucedianhua'},
         {name:'kaihuyinhang'},
         {name:'yinhangzhanghao'},
         {name:'fapiaotype'},
         {name:'accessname'},
         {name:'accessphone'},
         {name:'accessaddress'},
         {name:'accessemail'},
         {name:'xgreason'},
         {name:'subjecttype'},
         {name:'cooperationstr'},
         {name:'qudao'},
         {name:'sampleType'},
         {name:'sending'},
         {name:'checkProject'},
         {name:'inspection'},
         {name:'inputTime'},
         {name:'inputName'},
         {name:'updateTime'},
         {name:'updateName'},
         {name:'imgpath'},
         {name:'isjiesuan'},
         {name:'code'},
     ]
 });

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:23,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
       	 actionMethods : 'post',
         url: 'tuanti.action',
         reader: {
            // type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
 });




	main_store.on("beforeload",function(store,options){
        if(Ext.getCmp("search_form")){
            var  tuanname=Ext.getCmp("tuanname").getRawValue();
            if(tuanname!=null&&tuanname!=""){
                Ext.apply(store.proxy.extraParams, {tuanname:tuanname});
            }else if(!tuanname){
                Ext.apply(store.proxy.extraParams, {tuanname:""});
            }

            var  caiyangdian=Ext.getCmp("caiyangdian").getRawValue();
            if(caiyangdian!=null&&caiyangdian!=" "){
                Ext.apply(store.proxy.extraParams, {caiyangdidian:caiyangdian});
            }else if(!caiyangdian){
                Ext.apply(store.proxy.extraParams, {caiyangdidian:" "});
            }
            var  s_sending=Ext.getCmp("s_sending").getRawValue();
            if(s_sending!=null&&s_sending!=" "){
                Ext.apply(store.proxy.extraParams, {sending:s_sending});
            }else if(!s_sending){
                Ext.apply(store.proxy.extraParams, {sending:" "});
            }
            var  s_xgreason=Ext.getCmp("s_xgreason").getRawValue();
            if(s_xgreason!=null&&s_xgreason!=" "){
                Ext.apply(store.proxy.extraParams, {xgreason:s_xgreason});
            }else if(!s_xgreason){
                Ext.apply(store.proxy.extraParams, {xgreason:" "});
            }
            var  s_fapiaotaitou=Ext.getCmp("s_fapiaotaitou").getRawValue();
            if(s_fapiaotaitou!=null&&s_fapiaotaitou!=" "){
                Ext.apply(store.proxy.extraParams, {fapiaotaitou:s_fapiaotaitou});
            }else if(!s_fapiaotaitou){
                Ext.apply(store.proxy.extraParams, {fapiaotaitou:" "});
            }
            var  s_accessname=Ext.getCmp("s_accessname").getRawValue();
            if(s_accessname!=null&&s_accessname!=" "){
                Ext.apply(store.proxy.extraParams, {accessname:s_accessname});
            }else if(!s_accessname){
                Ext.apply(store.proxy.extraParams, {accessname:" "});
            }
            var  s_code=Ext.getCmp("s_code").getRawValue();
            if(s_code!=null&&s_code!=" "){
                Ext.apply(store.proxy.extraParams, {code:s_code});
            }else if(!s_code){
                Ext.apply(store.proxy.extraParams, {code:" "});
            }
            var  s_shuihao=Ext.getCmp("s_shuihao").getRawValue();
            if(s_shuihao!=null&&s_shuihao!=" "){
                Ext.apply(store.proxy.extraParams, {shuihao:s_shuihao});
            }else if(!s_shuihao){
                Ext.apply(store.proxy.extraParams, {shuihao:" "});
            }

            if(Ext.getCmp("startDate")){
                var  sstarttime=Ext.getCmp("startDate").getRawValue();
                if(sstarttime!=null&&sstarttime!=""){
                    sstarttime=Ext.getCmp("startDate").getRawValue();
                }
                var  sendtime=Ext.getCmp("endDate").getRawValue();
                if(sendtime!=null&&sendtime!=""){
                    sendtime=Ext.getCmp("endDate").getRawValue();
                }
                var new_params={startDate:sstarttime,endDate:sendtime};
                Ext.apply(store.proxy.extraParams, new_params);
            }
        };
		});

		 main_store.loadPage(1);


	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid =Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : '新冠团体录入',
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
            header : '团体名称',
            width:200,
            dataIndex : 'tuanname'
        },{
            header : '受检人单位',
            dataIndex : 'sending'
        },/* {
            header : '来源渠道',
            dataIndex : 'qudao'
        }, */{
            header : '受检人类型',
            dataIndex : 'subjecttype'
        },{
            header : '检测原由',
            dataIndex : 'xgreason'
        },{
            header : '预约日期',
            dataIndex : 'yuyuedate'
        },{
            header : '预约时间',
            dataIndex : 'yuyuetime'
        },{
            header:'预约采样点',
            width: 150,
            dataIndex:'caiyangdidian'
        },{
            header:'是否需要发票',
            dataIndex:'isfapiao'
        },{
            header:'验证码',
            dataIndex:'code'
        },{
            header:'发票类型',
            dataIndex:'fapiaotype'
        },{
            header:'税号',
            width: 150,
            dataIndex:'shuihao'
        },{
            header:'发票抬头',
            width: 150,
            dataIndex:'fapiaotaitou'
        },{
            header:'发票收件人姓名',
            width: 150,
            dataIndex:'accessname'
        },{
            header:'发票收件人电话',
            width: 150,
            dataIndex:'accessphone'
        },{
            header:'发票收件人地址',
            width: 150,
            dataIndex:'accessaddress'
        },{
            header:'电子发票接收邮箱',
            width: 150,
            dataIndex:'accessemail'
        },{
            header:'注册地址',
            width: 150,
            dataIndex:'zhucedizhi'
        },{
            header:'注册电话',
            width: 150,
            dataIndex:'zhucedianhua'
        },{
            header:'开户银行',
            dataIndex:'kaihuyinhang'
        },{
            header:'银行账号',
            dataIndex:'yinhangzhanghao'
        },{
            header:'录入人',
            width: 120,
            dataIndex:'inputName'
        },{
            header:'录入时间',
            width: 150,
            dataIndex:'inputTime'
        },{
            header:'更新人',
            width: 120,
            dataIndex:'updateName'
        },{
            header:'录入时间',
            width: 150,
            dataIndex:'updateTime'
        },{
            header:'二维码路径',
            width: 150,
            dataIndex:'imgpath',
            hidden:true
        },{
            header:'是否停止扫码',
            width: 150,
            dataIndex:'isjiesuan'
        }],
		listeners:{

		  } ,
		tbar : [
		{
			text : '团体录入',
			id:'tbar0',
			hidden:true,
			icon : 'img/add.png',
			handler : function() {
                xjyb_window.show();
			}
		},{
                text : '团体详细信息录入',
                id:'tbar5',
                hidden:true,
                icon : 'img/add.png',
                handler : function() {
                    var record = grid.getSelectionModel().getSelection();
                    console.log(record);
                    if(record.length == 1){
                        app_window.show();
                    }else if(record.length > 1){
                        Ext.example.msg("提示","只能选择一条要操作的数据",2000);
                    }else{
                        Ext.example.msg("提示","请选择一条要操作的数据",2000);
                    }
                }
            },{
                text : '下载团体二维码',
                icon : 'img/show.png',
                id:'tbar1',
                hidden:true,
                handler:function(){
                    var record = grid.getSelectionModel().getSelection();
                    if(record.length<=0){
                        Ext.example.msg("提示","请选择一个团体下载二维码",3000);
                    }
                    var a =document.createElement('a');
                    a.href="<%=basePath%>"+record[0].data.imgpath;
                    a.download="file";
                    a.click();
                }
            },{
                text : '团体录入模板',
                icon : 'img/0114.gif',
                id:'tbar2',
                hidden:true,
                handler:function(){
                    window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/tuanti.xls';
                }
            },{
                text : '详细信息录入模板',
                icon : 'img/0114.gif',
                id:'tbar6',
                hidden:true,
                handler:function(){
                    window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/tuantixiangxi.xls';
                }
            },{
                text:'停止扫码',
                hidden:true,
                id:'tbar3',
                icon:'img/new.png',
                handler:function(){
                    var xjyb_jsonArray = [];
                    var record =grid.getSelectionModel().getSelection();

                    if(record.length<=0){
                        Ext.example.msg("提示", "请写一条团单进行停止扫码",3000);
                        return;
                    }
                    for(var i=0;i<record.length;i++){
                        if(record[i].data.isjiesuan == '是'){
                            Ext.example.msg("提示", "团单不能重复停止扫码",3000);
                            return;
                        }
                        xjyb_jsonArray.push(record[i].data);
                    }
                    if(xjyb_jsonArray.length>0){
                        Ext.Ajax.request(
                            {
                                url: 'tuanti!updateJiesuan.action',
                                params: {'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                success: function (response) {
                                    Ext.example.msg("提示","停止扫码成功",3000);
                                    main_store.loadPage(1);
                                },
                                failure: handFailure,
                            });
                    }
                }
            },
            {
                text:'团体结算',
                id:'tbar4',
                hidden:true,
                icon:'img/new.png',
                handler:function(){
                    xjyb_jsonArray = [];
                    var record =grid.getSelectionModel().getSelection();

                    for(var i=0;i<record.length;i++){
                        xjyb_jsonArray.push(record[i].data);
                    }
                    if(xjyb_jsonArray.length>0) {
                        jiesuanWindow.show();

                        /*Ext.Ajax.request(
                            {
                                url: 'tuanti!tuantijiesuan.action',
                                params: {'itemsxjyb': Ext.encode(xjyb_jsonArray)},
                                success: function (response) {
                                    if (response.responseText == "否") {
                                        Ext.example.msg("提示", "最多导出30000条数据！");
                                        return;
                                    }
                                    Ext.example.msg("提示", response.responseText, 4000);
                                    if (response.responseText == "EXCEL已导出") {

                                    }
                                },
                                failure: handFailure,


                            });*/
                    }else{
                        Ext.example.msg('提示','请选择一条要修改的数据');
                    }
                }

            }, {
                text:'高级搜索',
                id:'search',
                icon:'img/simplesearch.png',
                handler:function(){
                    searchWindow.show();
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


	/*var crud_department_combox =Ext.create('Ext.form.ComboBox',{
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
		});	*/


    var jiesuanForm=Ext.create('Ext.form.FormPanel',{
        //url:'nucleicacids!request_entity.action',   //多条件跳转（当前页面）功能
        id : 'jiesuan_form',
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
                    fieldLabel: '采样日期',
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
                Ext.example.msg("提示", "正在结算团单,请不要进行任何操作,耐心等待下载",5000);
                var date = new Date( Ext.getCmp("ininputTime").getValue());
                var date1 = new Date( Ext.getCmp("osoinputTime").getValue());
                var ininputTime=date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                var osoinputTime=date1.getFullYear() + '-' + (date1.getMonth() + 1) + '-' + date1.getDate();
               console.log(ininputTime)
                console.log(osoinputTime)
                cp.getForm().submit({
                    url: 'tuanti!tuantijiesuan.action',
                    timeout: 100000000,
                    params:{
                        'itemsxjyb':Ext.encode(xjyb_jsonArray),
                        'ininputTime':ininputTime,
                        'osoinputTime':osoinputTime
                    },
                    success: function(f, action) {

                    },
                    failure:handFailure
                });
                jiesuanWindow.hide();

            }
        },{
            text:'取消',
            handler:function(){
                jiesuanWindow.hide();
            }
        },{
            text:'重置',
            handler:function(){
                jiesuanForm.form.reset();
            }
        }]
    });

    var xjyb_jsonArray = [];
    var jiesuanWindow = new Ext.Window({
        title:'结算',
        id:'jiesuanWindow',
        closeAction: 'hide',
        width:300,
        autoScroll:true,
        modal:true,
        items:jiesuanForm
    });

    var searchForm = Ext.create('Ext.form.FormPanel', {
        id : 'search_form',
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
                columnWidth : 1,
                items : [{
                    xtype : 'textfield',
                    fieldLabel : '团体名称',
                    id : 'tuanname'
                },{
                    xtype : 'textfield',
                    fieldLabel : '受检人单位',
                    id : 's_sending'
                },{
                    xtype : 'textfield',
                    fieldLabel : '检测原由',
                    id : 's_xgreason'
                },{
                    xtype : 'textfield',
                    fieldLabel : '验证码',
                    id : 's_code'
                },{
                    xtype : 'textfield',
                    fieldLabel : '税号',
                    id : 's_shuihao'
                },{
                    xtype : 'textfield',
                    fieldLabel : '发票抬头',
                    id : 's_fapiaotaitou'
                },{
                    xtype : 'textfield',
                    fieldLabel : '发票收件人',
                    id : 's_accessname'
                },{
                    xtype : 'textfield',
                    fieldLabel : '预约采样点',
                    id : 'caiyangdian'
                },{
                    xtype : 'fieldcontainer',
                    fieldLabel : '预约日期',
                    layout : 'hbox',
                    items : [{
                        xtype : 'datefield',
                        flex : 15,
                        format : 'Y-m-d',
                        id : 'startDate'
                    }, {
                        xtype : 'displayfield',
                        value : '~',
                        flex : 1
                    }, {
                        xtype : 'datefield',
                        flex : 15,
                        format : 'Y-m-d',
                        id : 'endDate'
                    }]
                }
                ]
            }]
        }],
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
                searchForm.form.reset();
            }
        }
        ]
    });
    var searchWindow = new Ext.Window({
        title:'结算',
        id:'searchWindow',
        closeAction: 'hide',
        autoScroll:true,
        modal:true,
        items:searchForm
    });
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

                        var reDate = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$/;
                        var isDate = reDate.test(trStr[5].trim());
                        if(!isDate){
                            Ext.example.msg("提示","团体名称为"+trStr[0].trim()+"的预约日期格式不正确,格式为0000-00-00");
                            return;
                        }

                        var reTime = /^(20|21|22|23|[0-1]\d):[0-5]\d-(20|21|22|23|[0-1]\d):[0-5]\d$/;
                        var isTime = reTime.test(trStr[6].trim());
                        if(!isTime){
                            Ext.example.msg("提示","客户姓名为"+trStr[0].trim()+"的预约时间格式不正确,格式为00:00-00:00");
                            return;
                        }

                        localStore.insert(0,{
                            tuanname:trStr[0].trim(),
                            //qudao:trStr[1].trim(),
                            sending:trStr[1].trim(),
                            cooperationstr:trStr[2].trim(),
                            subjecttype:trStr[3].trim(),
                            xgreason:trStr[4].trim(),
                            yuyuedate:trStr[5].trim(),
                            yuyuetime:trStr[6].trim(),
                            caiyangdidian:trStr[7].trim(),
                            isfapiao:trStr[8].trim(),
                            fapiaotaitou:trStr[9].trim(),
                            shuihao:trStr[10].trim(),
                            zhucedizhi:trStr[11].trim(),
                            zhucedianhua:trStr[12].trim(),
                            kaihuyinhang:trStr[13].trim(),
                            yinhangzhanghao:trStr[14].trim(),
                            fapiaotype:trStr[15].trim(),
                            accessname:trStr[16].trim(),
                            accessphone:trStr[17].trim(),
                            accessaddress:trStr[18].trim(),
                            accessemail:trStr[19].trim()

                        });

                    };
                }

                for(i=0;i<localStore.getCount();i++){
                    var record = localStore.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }
                xjyb_form.getForm().submit({
                    url: 'tuanti!istuanname.action',
                    params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    success: function(f, action) {
                        if (action.result.msg.length>0) {
                            var m = "团体名称为"+action.result.msg+"已存在是否更新团体信息?"
                            Ext.MessageBox.confirm("提示",m,function( button,text ){
                                    if( button == 'yes'){
                                        xjyb_form.getForm().submit({
                                            url: 'tuanti!saveAll.action',
                                            timeout: 100000000,
                                            async: false,
                                            params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                            waitMsg : '正在提交数据',
                                            success: function(f, action) {
                                                if (action.result.success) {
                                                    if(action.result.msg.indexOf("添加成功")!=-1){
                                                        main_store.load({
                                                            params: {
                                                                start:returnStart,
                                                                limit:pageSize
                                                            }
                                                        });


                                                        xjyb_window.hide();

                                                        Ext.example.msg("提示",action.result.msg,3000);
                                                    }else{
                                                        var resultmsg=action.result.msg;
                                                    }
                                                }else{
                                                    Ext.example.msg('消息', action.result.msg);
                                                }
                                            },
                                            failure:handFailure
                                        });
                                    }else{

                                    }
                                }
                            );

                        }else{
                            xjyb_form.getForm().submit({
                                url: 'tuanti!saveAll.action',
                                timeout: 100000000,
                                async: false,
                                params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                waitMsg : '正在提交数据',
                                success: function(f, action) {
                                    if (action.result.success) {
                                        if(action.result.msg.indexOf("添加成功")!=-1){
                                            main_store.load({
                                                params: {
                                                    start:returnStart,
                                                    limit:pageSize
                                                }
                                            });


                                            xjyb_window.hide();

                                            Ext.example.msg("提示",action.result.msg,3000);
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

    app_form = Ext.create('Ext.form.Panel', {
        labelWidth : 50,
        labelAlign : 'right',
        buttonAlign : 'center',
        //width :600,
        autoHeight:true,
        items : [{
            width:500,
            height:300,
            id:'app_textarea',
            xtype : 'textarea',
            name : 'textarea'
        }],
        buttons : [ {
            text : '提交',
            handler : function() {
                localApp.removeAll();
                var str=Ext.getCmp("app_textarea").getValue().replace(/\n$/,"");
                var len = str.split("\n");//获取数据
                var records = grid.getSelectionModel().getSelection();
                var xjyb_jsonArray=[];
                var trStr;
                var nullno="";
                var i;
                var j;


                if(len.length>0){
                    for(var i=0;i<len.length;i++){
                        //excel表格同一行的多个cell是以空格 分割的，此处以空格为单位对字符串做 拆分操作。。
                        trStr = len[i].split("\t");

                        localApp.insert(0,{
                            name:trStr[0].trim(),
                            //qudao:trStr[1].trim(),
                            sex:trStr[1].trim(),
                            age:trStr[2].trim(),
                            sfz:trStr[3].trim(),
                            phone:trStr[4].trim(),
                            /*subjecttype:trStr[5].trim(),
                            xgreason:trStr[6].trim(),*/
                        });

                    };
                }

                for(i=0;i<localApp.getCount();i++){
                    var record = localApp.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }

                app_form.getForm().submit({
                    url: 'tuanti!tuantiinfo.action',
                    timeout: 100000000,
                    async: false,
                    params:{'tuanname':records[0].data.tuanname,'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    success: function(f, action) {
                        if (action.result.success) {
                            if(action.result.msg.indexOf("添加成功")!=-1){

                                app_window.hide();

                                Ext.example.msg("提示",action.result.msg,3000);
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
                    app_window.hide();
                }
            } ]
    });


    /*---------------------------批量添加---------------------------*/

    Ext.define('State',{
        extend : 'Ext.data.Model',
        fields   : [
            {name:'id',type : 'string'},
            {name:'tuanname',type : 'string'},
            {name:'yuyuedate',type : 'string'},
            {name:'yuyuetime',type : 'string'},
            {name:'caiyangdidian',type : 'string'},
            {name:'isfapiao',type : 'string'},
            {name:'shuihao',type : 'string'},
            {name:'fapiaotaitou',type : 'string'},
            {name:'zhucedizhi',type : 'string'},
            {name:'zhucedianhua',type : 'string'},
            {name:'kaihuyinhang',type : 'string'},
            {name:'yinhangzhanghao',type : 'string'},
            {name:'fapiaotype',type : 'string'},
            {name:'accessname',type : 'string'},
            {name:'accessphone',type : 'string'},
            {name:'accessaddress',type : 'string'},
            {name:'accessemail',type : 'string'},
            {name:'subjecttype',type : 'string'},
            {name:'xgreason',type : 'string'},
            {name:'cooperationstr',type : 'string'},
            {name:'qudao',type : 'string'},
            {name:'sampleType',type : 'string'},
            {name:'sending',type : 'string'},
            {name:'checkProject',type : 'string'},
            {name:'inspection',type : 'string'},
            {name:'inputTime',type : 'string'},
            {name:'inputName',type : 'string'},
            {name:'updateTime',type : 'string'},
            {name:'updateName',type : 'string'},
            {name:'imgpath',type : 'string'},
            {name:'isjiesuan',type : 'string'},
            {name:'code',type : 'string'}
        ]
    });

    Ext.define('App',{
        extend : 'Ext.data.Model',
        fields   : [
            {name:'name',type : 'string'},
            {name:'sex',type : 'string'},
            {name:'age',type : 'string'},
            {name:'sfz',type : 'string'},
            {name:'phone',type : 'string'},
            /*{name:'xgreason',type : 'string'},
            {name:'subjecttype',type : 'string'},*/
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
    });


    //2.定义store,这里区分是内存中的还是远程服务器的
    //2.1 内存中的
    var localStore = Ext.create('Ext.data.Store',{
        model:State
        // data : localData1
    });

    var localApp = Ext.create('Ext.data.Store',{
        model:App
        // data : localData1
    });

    xjyb_window = Ext.create('Ext.window.Window',{
        title : '批量添加',
        closeAction : 'hide',
        modal : true,
        items : xjyb_form
    });

    app_window = Ext.create('Ext.window.Window',{
        title : '团体详细信息录入',
        closeAction : 'hide',
        modal : true,
        items : app_form
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
 function checkTime(i){
     if (i<10){
         i="0" + i
     }
     return i;
 }
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
