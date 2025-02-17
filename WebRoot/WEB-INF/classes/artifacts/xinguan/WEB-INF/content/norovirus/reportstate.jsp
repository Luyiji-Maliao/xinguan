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
         { name:'sampleNo'},
         { name:'detectionResult'},
         { name:'reportState'},
         { name:'inputTime'},
         { name:'inputName'},
         { name:'updateTime'},
         { name:'updateName'},
     ]
 });

 var main_store = Ext.create('Ext.data.Store', {
     model: 'mainPage',
     pageSize:23,//不加，点击下一页时默认25条
     proxy: {
         type: 'ajax',
       	 actionMethods : 'post',
         url: 'norovirusreportstate!list.action',
         reader: {
            // type: 'json',
             root: 'result',
             totalProperty : 'totalCount'
         }
     },
 });




	main_store.on("beforeload",function(store,options){
        var searchSampleNo = Ext.getCmp("searchSampleNo");

        if(searchSampleNo){
            Ext.apply(store.proxy.extraParams, {
                searchSampleNo:searchSampleNo.getValue()
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
            header:'样本编号',
            width: 170,
            dataIndex:'sampleNo'

        }, {
            header : '检测结果',
            dataIndex : 'detectionResult'
        },{
            header : '报告状态',
            dataIndex : 'reportState'
        },{
            header:'录入人',
            dataIndex:'inputName'
        },{
            header : '录入时间',
            width: 170,
            dataIndex : 'inputTime'
        },{
            header:'更新人',
            dataIndex:'updateName'
        },{
            header : '更新时间',
            width: 170,
            dataIndex : 'updateTime'
        }],
		listeners:{

		  } ,
		tbar : [
		{
                text : '生成报告',
                id:'tbar0',
                hidden:true,
                icon : 'img/add.png',
                handler : function() {
                    var fk_jsonArray = [];
                    var record = grid.getSelectionModel().getSelection();
                    Ext.MessageBox.confirm("请确认","是否生成选择信息的报告？",function(button,text){
                        if(button=="yes"){
                            for (var i = 0; i < record.length; i++) {
                                fk_jsonArray.push(record[i].data);
                            }

                            Ext.Ajax.request({
                                url : 'norovirusreportstate!buscReport.action',
                                actionMethods : 'post',
                                async : false,
                                reader : {
                                    type : 'json'
                                },
                                params : {
                                    'itmessc' : Ext.encode(fk_jsonArray),
                                },
                                success : function (form, action) {
                                    var msg = JSON.parse(form.responseText);
                                    Ext.example.msg("提示", msg.msg,3000);
                                    main_store.load({
                                        params: {
                                            start:returnStart,
                                            limit:pageSize
                                        }
                                    });
                                },
                                failure : function (form, action) {
                                    Ext.example.msg("提示", "错误，请联系管理员！");
                                }
                            });
                        }
                    });


                }
            },{
                id:'searchSampleNo',
                xtype:'textfield',
                width:200,
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
                            outtradeno:trStr[1].trim(), //商户单号，可以重复
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

    /*---------------------------批量添加---------------------------*/

    Ext.define('State',{
        extend : 'Ext.data.Model',
        fields   : [

            { name:'id',type:'string'},
            { name:'sampleNo',type:'string'},
            { name:'detectionResult',type:'string'},
            { name:'reportState',type:'string'},
            { name:'inputTime',type:'string'},
            { name:'inputName',type:'string'},
            { name:'updateTime',type:'string'},
            { name:'updateName',type:'string'},
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
        title : '批量添加',
        closeAction : 'hide',
        modal : true,
        items : xjyb_form
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
