<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新冠病毒英文报告自动化</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="新冠病毒英文报告自动化">
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
		},{
             name : 'sampleNo'
         }, {
             name : 'name'
         }, {
             name : 'englishName'
         },{
             name : 'sampleType'
         },{
             name:'idcard'
         },{
             name:'passport'
         },{
             name:'inspection'
         },{
             name:'samplingDate'
         },{
             name:'receptionDate'
         },{
             name:'checkProject'
         },{
             name:'detectionResult'
         },{
             name:'qudao'
         },{
             name:'inputName'
         },{
             name:'inputTime'
         },{
             name:'updateName'
         },{
             name:'updateTime'
         },{
             name:'pathName'
         }
     ]
 });

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:23,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
       	 actionMethods : 'post',
         url: 'norovirus!list2.action',
         reader: {
            // type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
 });




	main_store.on("beforeload",function(store,options){
		var searchSampleNo = Ext.getCmp("searchSampleNo");
        var searchName = Ext.getCmp("searchName");
        var searchPassport = Ext.getCmp("searchPassport");
		if(searchSampleNo){
			Ext.apply(store.proxy.extraParams, {
			    searchSampleNo:searchSampleNo.getValue()
			});
		}
        if(searchPassport){
            Ext.apply(store.proxy.extraParams, {
                searchPassport:searchPassport.getValue()
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


	/**---------------------------------显示数据的Grid开始---------------------------*/
	var grid =Ext.create('Ext.grid.Panel', {
		id : 'show_data_grid',
		title : '新冠病毒英文报告自动化',
		width : 600,
		region:'center',
		loadMask : true,
		stripeRows : true,
		frame : true,
        viewConfig:{
            enableTextSelection:true  //可复制内容
        } ,store : main_store,
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
            header : '样本编号',
            dataIndex : 'sampleNo'
        }, {
            header : '姓名',
            dataIndex : 'name'
        }, {
            header : '英文名字',
            width: 150,
            dataIndex : 'englishName'
        },{
            header : '样本类型',
            dataIndex : 'sampleType'
        },{
            header:'身份证号',
            width: 150,
            dataIndex:'idcard'
        },{
            header:'护照号',
            width: 120,
            dataIndex:'passport'
        },{
            header:'送检单位',
            dataIndex:'inspection'
        },{
            header:'检测项目',
            width: 170,
            dataIndex:'checkProject'
        },{
            header:'采样日期',
            width: 150,
            dataIndex:'samplingDate'
        },{
            header:'接收日期',
            width: 150,
            dataIndex:'receptionDate'
        },{
            header:'来源渠道',
            width: 150,
            dataIndex:'qudao'
        },{
            header:'检测结果',
            width: 150,
            dataIndex:'detectionResult'
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
            header:'更新时间',
            width: 150,
            dataIndex:'updateTime'
        },{
            header:'报告地址',
            width: 150,
            dataIndex:'pathName',
            hidden:true
        }],
		listeners:{

		  } ,
		tbar : [
		{
			text : '客服补录',
			id:'tbar0',
			hidden:true,
			icon : 'img/add.png',
			handler : function() {
                xjyb_window.show();
			}
		},{
                text : '生成报告',
                id:'tbar5',
                hidden:true,
                icon : 'img/add.png',
                handler : function() {
                    //screport = "norovirus!scyinReport.action";
                    sc_window.show();
                }
            }/*,{
                text : '生成未补录报告',
                id:'tbar8',
                hidden:true,
                icon:'img/add.png',
                handler:function(){
                    Ext.Ajax.request(
                        {
                            url: 'norovirus!buscReport.action',
                            timeout: 100000000,
                            success: function (response) {
                                var action =  JSON.parse(response.responseText);
                                if (action.success) {

                                    if(action.msg.indexOf('报告生成成功')!=-1){

                                        main_store.load({
                                            params: {
                                                start:returnStart,
                                                limit:pageSize
                                            }
                                        });

                                        sc_window.hide();
                                        Ext.MessageBox.confirm("请确认",action.msg+'是否下载报告',function(button,text){
                                            if(button=="yes"){
                                                Ext.getCmp("reportPath").setValue(action.o);
                                                cp.getForm().submit({
                                                    url:'norovirus!downloadreport.action',
                                                    timeout: 100000000
                                                });
                                                return;
                                            }
                                        });

                                    }else{
                                        sc_window.hide();
                                        Ext.MessageBox.alert("提示",action.msg);
                                    }
                                }else{
                                    Ext.example.msg('消息', action.msg);
                                }
                            },
                            failure: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示","生成失败",4000);
                            }
                        });



                }
            }*//*,{
                text : '预约生成报告',
                id:'tbar8',
                hidden:true,
                icon : 'img/add.png',
                handler : function() {
                    console.log(screport)
                    screport = "norovirus!scReport.action";
                    sc_window.show();
                }
            }*/,{
                text : '客服补录模板',
                icon : 'img/0114.gif',
                id:'tbar1',
                hidden:true,
                handler:function(){
                    window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/xinguankefuyin.xls';
                }
            },{
                text : '生成报告模板',
                icon : 'img/0114.gif',
                id:'tbar2',
                hidden:true,
                handler:function(){
                    window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/xinguan.xls';
                }
            },{
                text : '下载报告',
                icon : 'img/show.png',
                id:'tbar3',
                hidden:true,
                handler:function(){
                    var record = grid.getSelectionModel().getSelection();
                    var reportPath = '';
                    var sampleNo ='';
                    for(var i=0;i<record.length;i++){
                        if(record[i].data.detectionResult == ''){
                            sampleNo+=record[i].data.sampleNo+",";
                            continue;
                        }
                        reportPath+=record[i].data.pathName+",";
                    }
                    Ext.getCmp("reportPath").setValue(reportPath);

                    if(sampleNo!=''){
                        Ext.MessageBox.confirm("请确认","样本编号为"+sampleNo+'未生成报告,是否继续下载其它报告',function(button,text){
                            if(button=="yes"){
                                cp.getForm().submit({
                                    url:'norovirus!downloadreport.action',
                                    timeout: 100000000
                                });
                                return;
                            }
                        });
                    }else{
                        cp.getForm().submit({
                            url:'norovirus!downloadreport.action',
                            timeout: 100000000
                        });
                    }
                }
            },{
                text : '批量下载报告',
                id:'tbar4',
                hidden:true,
                icon : 'img/show.png',
                handler : function() {
                    pi_window.show();
                }
            },{
            	text: '生成个人报告',
            	id:'tbar6',
            	hidden:true,
            	icon:'img/add.png',
            	handler:function(){
            		gr_window.show();
            	}
            },{
                id:'searchSampleNo',
                xtype:'textfield',
                width:220,
                emptyText:'请输入要查找的样本编号',
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
                id:'searchPassport',
                xtype:'textfield',
                width:220,
                emptyText:'请输入要查找的护照号或身份证号',
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
                width:220,
                emptyText:'请输入要查找的客户姓名或英文姓名',
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
		
	gr_form = Ext.create('Ext.form.Panel',{
		labelWidth: 50,
		labelAlign:'right',
		buttonAlign:'center',
		autoHeight:true,
		items : [{
            width:500,
            height:300,
            id:'gr_textarea',
            xtype : 'textarea',
            name : 'textarea',
            emptyText : '可直接复制EXCEL表中的对应列到此处，格式如下：姓名-预约号-英文姓名-护照号'
        }],
		 buttons : [ {
            text : '提交',
            handler : function() {
                localStore.removeAll();
                var str=Ext.getCmp("gr_textarea").getValue().replace(/\n$/,"");
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
                        if(trStr.length!=4){
				            Ext.example.msg("提示","姓名为"+trStr[0].trim()+"的数据有未填项",3000);
							return;
				        }
                        localStore.insert(0,{
                            name:trStr[0].trim(),
                            reservation:trStr[1].trim(),
                            englishName:trStr[2].trim(),
                            passport:trStr[3].trim()
                        });

                    };
                }

                for(i=0;i<localStore.getCount();i++){
                    var record = localStore.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }

                gr_form.getForm().submit({
                    url: 'norovirus!scgryinReport.action',
                    timeout: 100000000,
                    async: false,
                    params:{'itmessc':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    success: function(f, action) {
                    	if (action.result.success) {
                            if(action.result.msg == '报告生成成功'){
                                Ext.example.msg('消息', action.result.msg);
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });

                                gr_window.hide();
                                Ext.MessageBox.confirm("请确认","是否要下载该批次生成的报告",function(button,text){
                                    if(button=="yes"){
                                        Ext.getCmp("reportPath").setValue(action.result.o);
                                        cp.getForm().submit({
                                            url:'norovirus!downloadreport.action',
                                            timeout: 100000000
                                        });
                                    }
                                });

                            }else{
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });
                                gr_window.hide();
                                Ext.MessageBox.confirm("请确认",action.result.msg+",是否继续下载其它的报告",function(button,text){
                                    if(button=="yes"){
                                        Ext.getCmp("reportPath").setValue(action.result.o);
                                        cp.getForm().submit({
                                            url:'norovirus!downloadreport.action',
                                            timeout: 100000000
                                        });
                                    }
                                });
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

                    sc_window.hide();
                }
            } ]
	
	});

	gr_window = Ext.create('Ext.window.Window',{
        title : '生成个人报告',
        closeAction : 'hide',
        modal : true,
        items : gr_form
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
                        for(j=0;j<len.length-1;j++){
                            trStr2 = len[j].split("\t");
                            if(trStr[0].trim()==trStr2[0].trim()){
                                if(i!=j){
                                    Ext.example.msg("提示","Excel样本编号重复"+trStr[0].trim());
                                    return;
                                }
                            }
                        }
                      /*if(trStr[5].trim().length!=10){
                            Ext.example.msg("提示",trStr[0]+"采样格式不正确,应为2000-01-01格式",3000);
                            return;
                        }
                        if(trStr[7].trim().length!=10){
                            Ext.example.msg("提示","接收日期格式不正确,应为2000-01-01格式",3000);
                            return;
                        }*/
                        localStore.insert(0,{
                            sampleNo:trStr[0].trim(),
                            name:trStr[1],
                            englishName:trStr[2],
                            idcard:trStr[3],
                            passport:trStr[4],
                            samplingDate:trStr[5],
                            sampleType:trStr[6],
                            receptionDate:trStr[7],
                            checkProject:trStr[8],
                            inspection:trStr[9],
                            qudao:trStr[10],
                            //detectionResult:trStr[10]
                        });

                    };
                }

                for(i=0;i<localStore.getCount();i++){
                    var record = localStore.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }

                xjyb_form.getForm().submit({
                    url: 'norovirus!kefusaveAll.action',
                    timeout: 100000000,
                    async: false,
                    params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    success: function(f, action) {
                        if (action.result.success) {
                            if(action.result.msg.indexOf("补录成功")!=-1){
                                Ext.example.msg('消息', action.result.msg);
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });

                                xjyb_window.hide();
                                /*Ext.MessageBox.confirm("请确认","是否要下载该批次添加的报告",function(button,text){
                                    if(button=="yes"){
                                        Ext.getCmp("reportPath").setValue(action.result.o);
                                        cp.getForm().submit({
                                            url:'norovirus!downloadreport.action',
                                            timeout: 100000000
                                        });
                                    }
                                });*/

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
                columnWidth : 1,
                items : [{
                    xtype: 'fieldcontainer',
                    fieldLabel: '报告日期',
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
                            url: 'norovirus!caiwuExcel.action',
                            timeout: 100000000,
                            params: {
                                'ininputTime':Ext.getCmp("ininputTime").getValue(),
                                'osoinputTime':Ext.getCmp("osoinputTime").getValue()
                            },
                            success: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示",response.responseText,4000);
                                if(response.responseText=="EXCEL已导出"){
                                    window.location.href='schedulesort!downloadfile.action?helpdocs=xinguancaiwu.xls';
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
                            url: 'norovirus!downloadExcel.action',
                            timeout: 100000000,
                            params: {
                                'ininputTime':Ext.getCmp("ininputTime").getValue(),
                                'osoinputTime':Ext.getCmp("osoinputTime").getValue()
                            },
                            success: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示",response.responseText,4000);
                                if(response.responseText=="EXCEL已导出"){
                                    window.location.href='schedulesort!downloadfile.action?helpdocs=xinguaninfo.xls';
                                }
                            },
                            failure: function (response) {
                                Ext.Msg.hide();
                                Ext.example.msg("提示","导出失败",4000);
                            }
                        });
                }
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

    sc_form = Ext.create('Ext.form.Panel', {
        labelWidth : 50,
        labelAlign : 'right',
        buttonAlign : 'center',
        //width :600,
        autoHeight:true,
        items : [{
            width:500,
            height:300,
            id:'sc_textarea',
            xtype : 'textarea',
            name : 'textarea'
        }],
        buttons : [ {
            text : '提交',
            handler : function() {
                localStore.removeAll();
                var str=Ext.getCmp("sc_textarea").getValue().replace(/\n$/,"");
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
                        for(j=0;j<len.length-1;j++){
                            trStr2 = len[j].split("\t");
                            if(trStr[0].trim()==trStr2[0].trim()){
                                if(i!=j){
                                    Ext.example.msg("提示","Excel样本编号重复"+trStr[0].trim());
                                    return;
                                }
                            }
                        }
                        localStore.insert(0,{
                            sampleNo:trStr[0].trim(),
                            detectionResult:trStr[1]
                        });

                    };
                }

                for(i=0;i<localStore.getCount();i++){
                    var record = localStore.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }

                sc_form.getForm().submit({
                    url: "norovirus!scyinReport.action",
                    timeout: 100000000,
                    async: false,
                    params:{'itmessc':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    success: function(f, action) {
                        if (action.result.success) {
                            if(action.result.msg == '报告生成成功'){
                                Ext.example.msg('消息', action.result.msg);
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });

                                sc_window.hide();
                                Ext.MessageBox.confirm("请确认","是否要下载该批次生成的报告",function(button,text){
                                    if(button=="yes"){
                                        Ext.getCmp("reportPath").setValue(action.result.o);
                                        cp.getForm().submit({
                                            url:'norovirus!downloadreport.action',
                                            timeout: 100000000
                                        });
                                    }
                                });

                            }else{
                                main_store.load({
                                    params: {
                                        start:returnStart,
                                        limit:pageSize
                                    }
                                });
                                sc_window.hide();
                                Ext.MessageBox.confirm("请确认",action.result.msg+",是否继续下载其它的报告",function(button,text){
                                    if(button=="yes"){
                                        Ext.getCmp("reportPath").setValue(action.result.o);
                                        cp.getForm().submit({
                                            url:'norovirus!downloadreport.action',
                                            timeout: 100000000
                                        });
                                    }
                                });
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

                    sc_window.hide();
                }
            } ]
    });

    pi_form = Ext.create('Ext.form.Panel', {
        labelWidth : 50,
        labelAlign : 'right',
        buttonAlign : 'center',
        //width :600,
        autoHeight:true,
        items : [{
            width:500,
            height:300,
            id:'pi_textarea',
            xtype : 'textarea',
            name : 'textarea'
        }],
        buttons : [ {
            text : '下载',
            handler : function() {
                localStore.removeAll();
                var str=Ext.getCmp("pi_textarea").getValue().replace(/\n$/,"");
                if(str == null || str==''){
                    Ext.Msg.alert('提示', '批量样本编号不能为空！');
                    return;
                }
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
                        localStore.insert(0,{
                            sampleNo:trStr[0].trim()
                        });

                    };
                }

                for(i=0;i<localStore.getCount();i++){
                    var record = localStore.getAt(i);
                    xjyb_jsonArray.push(record.data);
                }

                pi_form.getForm().submit({
                    url: 'norovirus!downloadsample.action',
                    timeout: 100000000,
                    async: false,
                    params:{'itmespi':Ext.encode(xjyb_jsonArray)},
                    waitMsg : '正在提交数据',
                    success: function(f, action) {
                        if (action.result.success) {
                            pi_window.hide();
                            Ext.getCmp("reportPath").setValue(action.result.o);

                            if(action.result.msg!=null && action.result.msg!=''){
                                Ext.MessageBox.confirm("请确认",action.result.msg+',是否继续下载其它报告',function(button,text){
                                    if(button=="yes"){
                                        cp.getForm().submit({
                                            url:'norovirus!downloadreport.action',
                                            timeout: 100000000
                                        });
                                        return;
                                    }
                                });
                            }else{
                                cp.getForm().submit({
                                    url:'norovirus!downloadreport.action',
                                    timeout: 100000000
                                });
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

                    pi_window.hide();
                }
            } ]
    });

    /*---------------------------批量添加---------------------------*/

    Ext.define('State',{
        extend : 'Ext.data.Model',
        fields   : [
           {
                name : 'sampleNo',
                type : 'string'
            }, {
                name : 'name',
                type : 'string'
            }, {
                name : 'englishName',
                type : 'string'
            },{
                name : 'sampleType',
                type : 'string'
            },{
                name:'idcard',
                type : 'string'
            },{
                name:'sendingPerson',
                type : 'string'
            },{
                name:'inspection',
                type : 'string'
            },{
                name:'samplingDate',
                type : 'string'
            },{
                name:'receptionDate',
                type : 'string'
            },{
                name:'checkProject',
                type : 'string'
            },{
                name:'detectionResult',
                type : 'string'
            },{
                name:'passport',
                type : 'string'
            },{
                name:'qudao',
                type : 'string'
            },{
                name:'reservation',
                type : 'string'
            }

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
        title : '客服补录',
        closeAction : 'hide',
        modal : true,
        items : xjyb_form
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
    var iscaiwu = '否';
    //生成报告路径 预约生成报告还是客服生成报告
    //var screport = "";

    sc_window = Ext.create('Ext.window.Window',{
        title : '生成报告',
        closeAction : 'hide',
        modal : true,
        items : sc_form
    });

    pi_window = Ext.create('Ext.window.Window',{
        title : '批量下载报告',
        closeAction : 'hide',
        modal : true,
        items : pi_form
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
		title : '新冠病毒英文报告自动化',
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
