<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>合管入库</title>
    <script type="text/javascript" src="./js/jquery-1.7.2.min.js"></script>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="合管入库">
    <jsp:include page="/include/header4.jsp"></jsp:include>
    <script type="text/javascript">
        Ext.onReady(function () {
            Ext.QuickTips.init();
            var pageSize = 20;
            var screport = "";

            Ext.define('mainPage', {
                extend: 'Ext.data.Model',
                idProperty : 'xx',  //不加此属性时，默认 idProperty : 'id' ，修改后获取到的数据还是原来的 （注意实体id命名）
                fields: [
                    {
                        name : 'id'
                    },{
                        name : 'fiveonenum'
                    },{
                        name : 'isruku'
                    },{
                        name : 'rukutime'
                    },{
                        name : 'rukuname'
                    },{
                        name : 'inputtime'
                    },{
                        name : 'inputname'
                    }
                ]
            });

            var main_store = Ext.create('Ext.data.Store', {
                model: 'mainPage',
                pageSize:20,//不加，点击下一页时默认25条
                remoteSort: true, //请求后台排序
                proxy: {
                    type: 'ajax',
                    actionMethods : 'post',
                    url: 'fiveoneinfo!list2.action',
                    reader: {
                        //type: 'list',
                        root: 'result',
                        totalProperty : 'totalCount'
                    }
                }
            });
            var arr = "";
            main_store.on("beforeload",function(store,options){

            });

            main_store.loadPage(1);


            //2.定义store,这里区分是内存中的还是远程服务器的
            //2.1 内存中的

            Ext.define('State',{
                extend : 'Ext.data.Model',
                fields   : [
                    {
                        name : 'id',
                        type : 'string'
                    }, {
                        name : 'fiveonenum',
                        type : 'string'
                    }, {
                        name : 'isruku',
                        type : 'string'
                    }, {
                        name : 'rukutime',
                        type : 'string'
                    }, {
                        name : 'rukuname',
                        type : 'string'
                    }, {
                        name : 'inputtime',
                        type : 'string'
                    }, {
                        name : 'inputname',
                        type : 'string'
                    }
                ]
            });

            var localStore = Ext.create('Ext.data.Store',{
                model:State,
                pageSize:20,//不加，点击下一页时默认25条
                // data : localData1
            });
            /**---------------------------------显示数据的Grid开始---------------------------*/
            var grid =Ext.create('Ext.grid.Panel', {
                id : 'show_data_grid',
                title : '合管入库',
                width : 600,
                region:'center',
                loadMask : true,
                stripeRows : true,
                frame : true,
                viewConfig:{
                    enableTextSelection:true  //可复制内容
                } ,store : main_store,
                columns:[{
                    header : '编号',
                    width : 50,
                    sortable : true,
                    dataIndex : 'id'
                },{
                    header : '五合一管编号',
                    dataIndex : 'fiveonenum'
                },{
                    header : '是否入库',
                    dataIndex : 'isruku'
                },{
                    header : '入库时间',
                    dataIndex : 'rukutime'
                },{
                    header : '入库人',
                    dataIndex : 'rukuname'
                },{
                    header : '录入时间',
                    dataIndex : 'inputtime'
                },{
                    header : '录入人',
                    dataIndex : 'inputname'
                }],
                listeners:{

                } ,
                tbar : [
                    {
                        text : '入库',
                        id:'tbar0',
                        hidden:true,
                        icon : 'img/add.png',
                        handler : function() {
                            Ext.getCmp('ruku_form').form.reset();
                            screport = "fiveoneinfo!ruku.action";
                            ruku_window.show();
                        }
                    },{
                        text:'高级搜索',
                        id:'tbar2',
                        icon:'img/search.png',
                        hidden:true,
                        handler:function() {
                            searchWindow.show();
                        }
                    }],
                bbar : new Ext.PagingToolbar( {
                    store : main_store,
                    displayInfo : true,
                    pageSize : pageSize
                })
            });

            var ruku_form = Ext.create('Ext.form.Panel', {
                id : 'ruku_form',
                buttonAlign : 'center',
                width : 500,
                autoHeight : true,
                fieldDefaults: {
                    labelAlign: 'right',
                    labelWidth:70
                },
                items : [{
                    xtype : 'textfield',
                    fieldLabel : '<font color="red">*</font>五合一管编号',
                    anchor:'95%',
                    name : 'fiveonenum',
                    id : 'fiveonenum',
                    allowBlank:false,
                    listeners:{
                        specialkey: function(field, e){
                            if(e.getKey() == Ext.EventObject.ENTER){
                                ruku_form.getForm().submit({
                                    url: screport,
                                    success: function(f, action) {
                                        if (action.result.success) {
                                            ruku_form.getForm().reset();
                                            // ruku_window.hide();
                                            grid.getStore().reload();
                                            Ext.example.msg('消息', action.result.msg);
                                            document.getElementById('fiveonenum-inputEl').focus();
                                        }
                                    },
                                    failure:handFailure
                                });
                            }
                        }
                    }
                }],
                listeners:{
                    show : function () {
                        console.log("!");
                        document.getElementById('fiveonenum-inputEl').focus();
                    }
                },
                buttons : [ {
                    text : '确认',
                    id:'subbut',
                    handler : function() {
                        ruku_form.getForm().submit({
                            url: screport,
                            success: function(f, action) {
                                if (action.result.success) {
                                    // ruku_window.hide();
                                    grid.getStore().reload();
                                    Ext.example.msg('消息', action.result.msg);
                                    document.getElementById('fiveonenum-inputEl').focus();
                                }
                            },
                            failure:handFailure
                        });
                    }
                }, {
                    text : '取消',
                    handler:function(){
                        ruku_window.hide();
                    }
                } ]
            });

            var ruku_window = new Ext.Window( {
                id : "ruku_window",
                title : '合管入库',
                closeAction : 'hide',
                modal : true,
                items : ruku_form
            });

            var searchForm=	 new Ext.form.FormPanel({
                buttonAlign : 'center',
                id: 'search_form',
                url: 'fiveoneinfo!list2.action',
                labelAlign: 'right',
                autoHeight: true,
                defaults : {
                    xtype : 'textfield',
                    width : 200
                },
                fieldDefaults : {
                    labelAlign : 'right',
                    labelWidth : 82,
                },
                items :[{
                    fieldLabel : '样本编号',
                    anchor:'95%',
                    name : 'searchSampleNo',
                    id : 'searchSampleNo'
                },{
                    fieldLabel : '姓名',
                    anchor:'95%',
                    name : 'searchpartyName',
                    id : 'searchpartyName'
                },{
                    fieldLabel : '手机号',
                    anchor:'95%',
                    name : 'searchphone',
                    id : 'searchphone'
                },{
                    fieldLabel : '身份证号',
                    anchor:'95%',
                    name : 'searchCertNumber',
                    id : 'searchCertNumber'
                },{
                    fieldLabel : '录入日期',
                    anchor:'95%',
                    name : 'startDate',
                    format:"Y-m-d",
                    id : 'startDate',
                    xtype:'datefield',
                },{
                    fieldLabel : '结束日期',
                    anchor:'95%',
                    name : 'endDate',
                    id : 'endDate',
                    format:"Y-m-d",
                    xtype:'datefield',
                },{
                    fieldLabel : '采样地点',
                    anchor:'95%',
                    name : 'searchinputaddress',
                    id : 'searchinputaddress'
                },{
                    fieldLabel : '是否入库',
                    anchor:'95%',
                    name : 'searchisruku',
                    id : 'searchisruku'
                },{
                    fieldLabel : '五合一管编号',
                    anchor:'95%',
                    name : 'searchfiveone',
                    id : 'searchfiveone'
                }],
                buttons: [{
                    text: '查询',
                    handler: function () {
                        main_store.load({
                            params : {
                                start : 0,
                                limit : pageSize,
                            }
                        });
                        searchWindow.hide();
                    }
                }, {
                    text: '取消',
                    handler: function () {
                        searchWindow.hide();
                    }
                }, {
                    text: '重置',
                    handler: function () {
                        searchForm.form.reset();
                    }
                }],
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
