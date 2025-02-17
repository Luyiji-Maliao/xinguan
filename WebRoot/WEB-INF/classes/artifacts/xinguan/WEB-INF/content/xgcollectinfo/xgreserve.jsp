<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>核酸用户信息存储</title>
    <script type="text/javascript" src="./js/jquery-1.7.2.min.js"></script>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="核酸用户信息存储">
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
                        name : 'name'
                    },{
                        name : 'certNumber'
                    },{
                        name : 'phone'
                    },{
                        name : 'inputname'
                    },{
                        name : 'inputtime'
                    },{
                        name : 'searchName'
                    },{
                        name : 'searchphone'
                    },{
                        name : 'searchCertNumber'
                    },{
                        name : 'startDate'
                    },{
                        name : 'endDate'
                    },{
                        name : 'updatename'
                    },{
                        name : 'updatetime'
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
                    url: 'xgreserve!list.action',
                    reader: {
                        //type: 'list',
                        root: 'result',
                        totalProperty : 'totalCount'
                    }
                }
            });

            main_store.on("beforeload",function(store,options){
                var searchName = Ext.getCmp("searchName");
                var searchphone = Ext.getCmp("searchphone");
                var searchCertNumber = Ext.getCmp("searchCertNumber");
                var startDate = Ext.getCmp("startDate");
                var endDate = Ext.getCmp("endDate");
                if(searchName){
                    Ext.apply(store.proxy.extraParams, {
                        searchName:searchName.getValue()
                    });
                }
                if(searchphone){
                    Ext.apply(store.proxy.extraParams, {
                        searchphone:searchphone.getValue()
                    });
                }
                if(searchCertNumber){
                    Ext.apply(store.proxy.extraParams, {
                        searchCertNumber:searchCertNumber.getValue()
                    });
                }
                if(startDate){
                    Ext.apply(store.proxy.extraParams, {
                        startDate:startDate.getValue()
                    });
                }
                if(endDate){
                    Ext.apply(store.proxy.extraParams, {
                        endDate:endDate.getValue()
                    });
                }
                Ext.apply(store.proxy.extraParams, {filter_LIKES_department00deptName_OR_department00description:'IT'});
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
                        name : 'name',
                        type : 'string'
                    }, {
                        name : 'certNumber',
                        type : 'string'
                    }, {
                        name : 'phone',
                        type : 'string'
                    }, {
                        name : 'inputname',
                        type : 'string'
                    }, {
                        name : 'inputtime',
                        type : 'string'
                    },{
                        name : 'updatetime',
                        type : 'string'
                    },{
                        name : 'updatename',
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
                title : '核酸用户信息存储',
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
                    header : '姓名',
                    width: 170,
                    dataIndex : 'name'
                },{
                    header : '身份证号',
                    width: 150,
                    dataIndex : 'certNumber'
                },{
                    header : '手机号',
                    dataIndex : 'phone'
                },{
                    header : '录入时间',
                    dataIndex : 'inputtime'
                },{
                    header : '录入人',
                    dataIndex : 'inputname'
                },{
                    header : '修改时间',
                    dataIndex : 'updatetime'
                },{
                    header : '修改人',
                    dataIndex : 'updatename'
                }],
                listeners:{

                } ,
                tbar : [
                    {
                        text : '批量录入',
                        id:'tbar0',
                        hidden:true,
                        icon : 'img/add.png',
                        handler : function() {
                            screport = "xgreserve!save.action";
                            lr_window.show();
                        }
                    },{
                        text : '录入模板',
                        id:'tbar1',
                        hidden:true,
                        icon : 'img/0114.gif',
                        handler : function() {
                            window.location.href='xgreserve!downloadlrfile.action';
                        }
                    },{
                        text:'高级搜索',
                        id:'tbar2',
                        icon:'img/search.png',
                        hidden:true,
                        handler:function() {
                            searchWindow.show();
                        }
                    },{
                        text:'修改',
                        id:'tbar3',
                        icon:'img/update.png',
                        hidden:true,
                        handler:function() {
                            Ext.getCmp('update_data_form').form.reset();
                            update_data_window.setTitle("修改");
                            var record = grid.getSelectionModel().getSelection();
                            if (record.length == 1) {
                                update_data_form.getForm().loadRecord(record[0]);
                                screport = "xgreserve!upd.action";
                                update_data_window.show();
                            }else{
                                Ext.example.msg("提示","请选择要修改的数据");
                            }

                        }
                    },{
                        text:'删除',
                        id:'tbar4',
                        icon:'img/delete2.gif',
                        hidden:true,
                        handler:function() {
                            var record = grid.getSelectionModel().getSelection();
                            if (record.length) {
                                Ext.MessageBox.confirm(
                                    "请确认"
                                    ,"确定删除吗？"
                                    ,function( button,text ){
                                        if( button == 'yes'){
                                            var ids = "";
                                            for(var i=0;i<record.length;i++){ //循环每一行
                                                ids +=record[i].data.id+",";
                                            }
                                            Ext.Ajax.request({
                                                url: 'xgreserve!delete.action',
                                                params: {ids: ids},
                                                success: function (response) {
                                                    Ext.example.msg('消息', '删除成功！');
                                                    main_store.loadPage(1);
                                                },
                                                failure: function () {
                                                    Ext.example.msg("提示", "很遗憾，发生了一点异常，请稍候再试！");
                                                }
                                            });
                                        }
                                    }
                                )
                            }else{
                                Ext.example.msg("提示","请选择要删除的数据");
                            }
                        }
                    }],
                bbar : new Ext.PagingToolbar( {
                    store : main_store,
                    displayInfo : true,
                    pageSize : pageSize
                })
            });


            var update_data_form = Ext.create('Ext.form.Panel', {
                id : 'update_data_form',
                buttonAlign : 'center',
                width : 500,
                autoHeight : true,
                fieldDefaults: {
                    labelAlign: 'right',
                    labelWidth:70

                },
                defaults : {
                    xtype : 'textfield',
                    width : 200
                },
                items : [{
                    xtype:'hidden',
                    name:'id'
                },{
                    fieldLabel : '<font color="red">*</font>姓名',
                    anchor:'95%',
                    name : 'name',
                    allowBlank:false
                },{
                    fieldLabel : '<font color="red">*</font>身份证号',
                    anchor:'95%',
                    name : 'certNumber',
                    allowBlank:false
                },{
                    fieldLabel : '<font color="red">*</font>手机号',
                    anchor:'95%',
                    name : 'phone',
                    allowBlank:false
                }],
                buttons : [ {
                    text : '提交',
                    id:'subbut',
                    handler : function() {
                        update_data_form.getForm().submit({
                            url: screport,
                            success: function(f, action) {
                                if (action.result.success) {
                                    update_data_window.hide();
                                    // Ext.getCmp("searchField").setValue("");
                                    grid.getStore().reload();
                                    update_data_form.getForm().reset();
                                    Ext.example.msg('消息', action.result.msg);
                                }
                            },
                            failure:handFailure
                        });
                    }}, {
                    text : '取消',
                    handler:function(){
                        update_data_window.hide();
                    }
                } ]
            });

            var update_data_window = new Ext.Window( {
                id : "update_data_window",
                title : '添加信息',
                closeAction : 'hide',
                modal : true,
                items : update_data_form
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
                    fieldLabel : '姓名',
                    anchor:'95%',
                    name : 'searchName',
                    id : 'searchName'
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

            lr_form = Ext.create('Ext.form.Panel', {
                labelWidth : 50,
                labelAlign : 'right',
                buttonAlign : 'center',
                //width :600,
                autoHeight:true,
                items : [{
                    width:500,
                    height:300,
                    id:'lr_textarea',
                    xtype : 'textarea',
                    name : 'lr_textarea'
                }],
                buttons : [ {
                    text : '确认',
                    handler : function() {
                        localStore.removeAll();
                        var str=Ext.getCmp("lr_textarea").getValue().replace(/\n$/,"");
                        var len = str.split("\n");//获取数据
                        var xjyb_jsonArray=[];
                        var trStr=[];
                        var i;
                        var j;
                        var islie = -1;
                        if(len.length>0){
                            for(var i=0;i<len.length;i++){
                                //excel表格同一行的多个cell是以空格 分割的，此处以空格为单位对字符串做 拆分操作。。
                                trStr = len[i].split("\t");
                                for(j=0;j<len.length-1;j++){
                                    trStr2 = len[j].split("\t");
                                    if(trStr[1].trim()==trStr2[1].trim()){
                                        if(i!=j){
                                            Ext.example.msg("提示","Excel身份证号重复"+trStr[1].trim());
                                            return;
                                        }
                                    }
                                }
                                localStore.insert(0,{
                                    name:trStr[0].trim(),
                                    certNumber:trStr[1],
                                    phone:trStr[2]
                                });
                            };
                            for(var i=0;i<len.length;i++){
                                trStr = len[i].split("\t");
                                for(var j=0;j<trStr.length;j++){
                                    if(trStr[j] == ""){
                                        var num = Number(i)+Number(1);
                                        Ext.example.msg("提示","第"+num+"行有一列为空！");
                                        return;
                                    }
                                }
                            }
                        }
                        for(i=0;i<localStore.getCount();i++){
                            var record = localStore.getAt(i);
                            xjyb_jsonArray.push(record.data);
                        }
                        lr_form.getForm().submit({
                            url: screport,
                            timeout: 100000000,
                            async: false,
                            params:{'itmessc':Ext.encode(xjyb_jsonArray)},
                            waitMsg : '正在提交数据',
                            success: function(f, action) {
                                if(action.result.msg == "录入成功！"){
                                    Ext.example.msg('消息', action.result.msg);
                                }else{
                                    Ext.Msg.alert("成功",action.result.msg);
                                }
                                lr_form.getForm().reset();
                                lr_window.hide();
                                grid.getStore().reload();
                                // qrgrid.getStore().reload();

                            },
                            failure:handFailure
                        });
                    }
                },{
                    text : '取消',
                    handler:function(){
                        lr_window.hide();
                        lr_form.getForm().reset();
                    }
                }]
            });

            lr_window = Ext.create('Ext.window.Window',{
                title : '录入',
                closeAction : 'hide',
                modal : true,
                items : lr_form
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
