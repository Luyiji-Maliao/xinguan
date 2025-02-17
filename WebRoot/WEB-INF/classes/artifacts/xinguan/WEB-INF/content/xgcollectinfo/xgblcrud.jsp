<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="S" uri="/struts-tags" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>核酸检测信息采集补录</title>
    <script type="text/javascript" src="./js/jquery-1.7.2.min.js"></script>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="核酸检测信息采集补录">
    <jsp:include page="/include/header4.jsp"></jsp:include>
    <script type="text/javascript" charset="UTF-8">
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
                        name : 'samplenum'
                    },{
                        name : 'phone'
                    },{
                        name : 'certNumber'
                    },{
                        name : 'partyName'
                    },{
                        name : 'inputtime'
                    },{
                        name : 'inputname'
                    },{
                        name : 'updatetime'
                    },{
                        name : 'updatename'
                    },{
                        name : 'inputAddress'
                    },{
                        name:'gender'
                    },{
                        name:'nation'
                    },{
                        name:'certAddress'
                    },{
                        name:'certOrg'
                    },{
                        name:'certType'
                    },{
                        name:'searchSampleNo'
                    },{
                        name:'searchCertNumber'
                    },{
                        name:'searchpartyName'
                    },{
                        name:'searchinputaddress'
                    },{
                        name:'searchphone'
                    },{
                        name:'startDate'
                    },{
                        name:'endDate'
                    },{
                        name:'all'
                    },{
                        name : 'fiveone'
                    },{
                        name : 'isruku'
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
                    url: 'xgcollectinfo2!list2.action',
                    reader: {
                        //type: 'list',
                        root: 'result',
                        totalProperty : 'totalCount'
                    }
                }
            });
            var arr = "";
            main_store.on("beforeload",function(store,options){
                var searchCertNumber = Ext.getCmp("searchCertNumber");
                var searchSampleNo = Ext.getCmp("searchSampleNo");
                var searchpartyName = Ext.getCmp("searchpartyName");
                var searchinputaddress = Ext.getCmp("searchinputaddress");
                var searchphone = Ext.getCmp("searchphone");
                var startDate = Ext.getCmp("startDate");
                var endDate = Ext.getCmp("endDate");
                var searchfiveone = Ext.getCmp("searchfiveone");
                var searchisruku = Ext.getCmp("searchisruku");

                if(searchfiveone){
                    Ext.apply(store.proxy.extraParams, {
                        searchfiveone:searchfiveone.getValue()
                    });
                }
                if(searchisruku){
                    Ext.apply(store.proxy.extraParams, {
                        searchisruku:searchisruku.getValue()
                    });
                }
                if(arr!=""){
                    Ext.apply(store.proxy.extraParams, {
                        arr:arr
                    });
                }
                if(searchCertNumber){
                    Ext.apply(store.proxy.extraParams, {
                        searchCertNumber:searchCertNumber.getValue()
                    });
                }
                if(searchpartyName){
                    Ext.apply(store.proxy.extraParams, {
                        searchpartyName:searchpartyName.getValue()
                    });
                }

                if(searchSampleNo){
                    Ext.apply(store.proxy.extraParams, {
                        searchSampleNo:searchSampleNo.getValue()
                    });
                }
                if(searchinputaddress){
                    Ext.apply(store.proxy.extraParams, {
                        searchinputaddress:searchinputaddress.getValue()
                    });
                }
                if(searchphone){
                    Ext.apply(store.proxy.extraParams, {
                        searchphone:searchphone.getValue()
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
                        name : 'samplenum',
                        type : 'string'
                    }, {
                        name : 'partyName',
                        type : 'string'
                    },{
                        name : 'phone',
                        type : 'string'
                    },{
                        name:'certNumber',
                        type : 'string'
                    },{
                        name:'certNumber',
                        type : 'string'
                    },{
                        name:'inputtime',
                        type : 'string'
                    },{
                        name:'inputname',
                        type : 'string'
                    },{
                        name:'updatetime',
                        type : 'string'
                    },{
                        name:'updatename',
                        type : 'string'
                    },{
                        name:'inputAddress',
                        type : 'string'
                    },{
                        name:'all',
                        type : 'string'
                    },{
                        name:'isruku',
                        type : 'string'
                    },{
                        name:'fiveone',
                        type : 'string'
                    }
                ]
            });



            var localStore = Ext.create('Ext.data.Store',{
                model:State,
                pageSize:20,//不加，点击下一页时默认25条
            });
            /**---------------------------------显示数据的Grid开始---------------------------*/
            var grid =Ext.create('Ext.grid.Panel', {
                id : 'show_data_grid',
                title : '核酸检测信息采集补录',
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
                    dataIndex : 'samplenum'
                },{
                    header : '姓名',
                    dataIndex : 'partyName'
                },{
                    header : '手机号',
                    dataIndex : 'phone'
                },{
                    header : '身份证号',
                    width: 150,
                    dataIndex : 'certNumber'
                },{
                    header : '录入时间',
                    width: 150,
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
                },{
                    header : '采样地点',
                    dataIndex : 'inputAddress'
                },{
                    header : '是否入库',
                    dataIndex : 'isruku'
                },{
                    header : '五合一管编号',
                    dataIndex : 'fiveone'
                }],
                listeners:{

                } ,
                tbar : [
                    {
                        text : '添加',
                        id:'tbar0',
                        hidden:true,
                        icon : 'img/add.png',
                        handler : function() {
                            Ext.getCmp('update_data_form').form.reset();
                            Ext.getCmp("saveorupdate").setValue("添加");
                            Ext.getCmp("samplenum").setReadOnly(false);
                            screport = "xgcollectinfo2!save.action";
                            update_data_window.show();
                        }
                    },{
                        text:'修改',
                        id:'tbar1',
                        hidden:true,
                        icon:'img/update.png',
                        handler:function() {
                            Ext.getCmp('update_data_form').form.reset();
                            update_data_window.setTitle("修改角色");
                            var record = grid.getSelectionModel().getSelection();
                            if (record.length) {
                                Ext.getCmp("saveorupdate").setValue("修改");
                                Ext.getCmp("samplenum").setReadOnly(true);
                                update_data_form.getForm().loadRecord(record[0]);
                                screport = "xgcollectinfo2!upd.action";
                                update_data_window.show();
                            }else{
                                Ext.example.msg("提示","请选择要修改的数据");
                            }
                        }
                    },{
                        text:'删除',
                        id:'tbar2',
                        hidden:true,
                        icon: 'img/delete2.gif',
                        handler:function() {
                            var record = grid.getSelectionModel().getSelection();
                            if (record.length) {
                                var ids = "";
                                for(var i=0;i<record.length;i++){ //循环每一行
                                    ids +=record[i].data.id+",";
                                }
                                Ext.Ajax.request({
                                    url: 'xgcollectinfo2!delete.action',
                                    params: {ids: ids},
                                    success: function (response) {
                                        Ext.Msg.alert('提示', "已删除");
                                        main_store.loadPage(1);

                                    },
                                    failure: function () {
                                        Ext.example.msg("提示", "很遗憾，发生了一点异常，请稍候再试！");
                                    }
                                });
                            }else{
                                Ext.example.msg("提示","请选择要删除的数据");
                            }
                        }
                    },{
                        text:'高级搜索',
                        id:'tbar3',
                        icon:'img/search.png',
                        hidden:true,
                        handler:function() {
                            searchWindow.show();
                        }
                    },{
                        text:'导出',
                        id:'tbar4',
                        hidden:true,
                        icon:'img/up.gif',
                        handler : function() {
                            var xjyb_jsonArray = [];
                            main_store.commitChanges();
                            grid.getView().refresh();
                            var record = grid.getSelectionModel().getSelection();
                            for (var i = 0; i < record.length; i++) {
                                xjyb_jsonArray.push(record[i].data);
                            }
                            var a = grid.getStore().totalCount;
                            if(a == ""){
                                Ext.example.msg("提示", "暂无数据导出！");
                            }else{
                                if(xjyb_jsonArray.length>0){
                                    Ext.Ajax.request({
                                        url :'xgcollectinfo2!Excel.action',
                                        actionMethods : 'post',
                                        async : false,
                                        reader : {
                                            type : 'json'
                                        },
                                        params : {
                                            'jsonArray' : Ext.encode(xjyb_jsonArray),
                                            'searchCertNumber' : Ext.getCmp("searchCertNumber").getValue(),
                                            'searchSampleNo' : Ext.getCmp("searchSampleNo").getValue(),
                                            'searchpartyName' : Ext.getCmp("searchpartyName").getValue(),
                                            'searchinputaddress' : Ext.getCmp("searchinputaddress").getValue(),
                                            'searchphone' : Ext.getCmp("searchphone").getValue(),
                                            'startDate' : Ext.getCmp("startDate").getValue(),
                                            'endDate' : Ext.getCmp("endDate").getValue(),
                                            'searchisruku' : Ext.getCmp("searchisruku").getValue(),
                                            'searchfiveone' : Ext.getCmp("searchfiveone").getValue(),
                                            'arr':arr
                                        },
                                        success : function (form, action) {
                                            window.location.href = "xgcollectinfo2!daochuExcel.action";
                                            Ext.example.msg("提示", "导出成功");
                                        },
                                        failure : function (form, action) {
                                            Ext.example.msg("提示", "错误，请联系管理员！");
                                        }
                                    });
                                }
                                else{
                                    Ext.Ajax.request({
                                        url :'xgcollectinfo2!Excel.action',
                                        actionMethods : 'post',
                                        async : false,
                                        reader : {
                                            type : 'json'
                                        },
                                        params : {
                                            'jsonArray' : Ext.encode(xjyb_jsonArray),
                                            'searchCertNumber' : Ext.getCmp("searchCertNumber").getValue(),
                                            'searchSampleNo' : Ext.getCmp("searchSampleNo").getValue(),
                                            'searchpartyName' : Ext.getCmp("searchpartyName").getValue(),
                                            'searchinputaddress' : Ext.getCmp("searchinputaddress").getValue(),
                                            'searchphone' : Ext.getCmp("searchphone").getValue(),
                                            'startDate' : Ext.getCmp("startDate").getValue(),
                                            'endDate' : Ext.getCmp("endDate").getValue(),
                                            'searchisruku' : Ext.getCmp("searchisruku").getValue(),
                                            'searchfiveone' : Ext.getCmp("searchfiveone").getValue(),
                                            'arr':arr
                                        },
                                        success : function (form, action) {
                                            window.location.href = "xgcollectinfo2!daochuExcel.action";
                                            Ext.example.msg("提示", "导出成功");
                                        },
                                        failure : function (form, action) {
                                            Ext.example.msg("提示", "错误，请联系管理员！");
                                        }
                                    });

                                }
                            }

                        }
                    },{
                        text:'全部',
                        hidden:true,
                        id:'tbar5',
                        icon:'img/new.png',
                        handler:function() {
                            if(this.getText()=="非全部"){
                                this.setText("全部");
                                arr = "非全部";
                            }else{
                                this.setText("非全部");
                                arr = "全部";
                            }
                            main_store.load({
                                params:{
                                    start:0,
                                    limit:pageSize,
                                }
                            });
                        }
                    },{
                        text : '批量录入',
                        id:'tbar6',
                        hidden:true,
                        icon : 'img/add.png',
                        handler : function() {
                            screport = "xgcollectinfo2!saves.action";
                            lr_window.show();
                        }
                    },{
                        text : '录入模板',
                        id:'tbar7',
                        hidden:true,
                        icon : 'img/0114.gif',
                        handler : function() {
                            window.location.href='xgcollectinfo2!downloadlrfile.action';
                        }
                    }],
                bbar : new Ext.PagingToolbar( {
                    store : main_store,
                    displayInfo : true,
                    pageSize : pageSize
                })
            });

            var qrgrid =Ext.create('Ext.grid.Panel',{
                id : 'qrgrid',
                title : '确认录入信息',
                width : 850,
                height:document.body.clientHeight*0.6,
                region:'center',
                loadMask : true,
                stripeRows : true,
                frame : true,
                viewConfig:{
                    enableTextSelection:true  //可复制内容
                } ,
                store : localStore,
                columns:[{
                    header : '样本编号',
                    dataIndex : 'samplenum'
                },{
                    header : '姓名',
                    dataIndex : 'partyName'
                },{
                    header : '手机号',
                    dataIndex : 'phone'
                },{
                    header : '身份证号',
                    width: 150,
                    dataIndex : 'certNumber'
                },{
                    header : '采样地点',
                    dataIndex : 'inputAddress'
                },{
                    header : '五合一管编号',
                    dataIndex : 'fiveone'
                },{
                    header : '录入时间',
                    dataIndex : 'inputtime'
                },{
                    header : '录入人',
                    dataIndex : 'inputname'
                }],
                // bbar : new Ext.PagingToolbar( {
                //     store : localStore,
                //     displayInfo : true,
                //     pageSize : 20,
                // })
            })

            var searchForm=	 new Ext.form.FormPanel({
                buttonAlign : 'center',
                id: 'search_form',
                url: 'xgcollectinfo2!list2.action',
                labelAlign: 'right',
                autoHeight: true,
                defaults : {
                    xtype : 'textfield',
                    width : 200
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

            var update_data_form = Ext.create('Ext.form.Panel', {
                id : 'update_data_form',
                buttonAlign : 'center',
                width : 720,
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
                    xtype:'hidden',
                    id:'saveorupdate',
                    name:'saveorupdate'
                },{
                    fieldLabel : '<font color="red">*</font>样本编号',
                    anchor:'95%',
                    name : 'samplenum',
                    id : 'samplenum',
                    allowBlank:false
                },{
                    fieldLabel : '<font color="red">*</font>姓名',
                    anchor:'95%',
                    name : 'partyName',
                    allowBlank:false
                },{
                    fieldLabel : '<font color="red">*</font>手机号',
                    anchor:'95%',
                    name : 'phone',
                    allowBlank:false
                },{
                    fieldLabel : '<font color="red">*</font>身份证号',
                    anchor:'95%',
                    name : 'certNumber',
                    allowBlank:false
                },{
                    fieldLabel : '<font color="red">*</font>采样地点',
                    anchor:'95%',
                    name : 'inputAddress',
                    allowBlank:false
                },{
                    fieldLabel : '<font color="red">*</font>五合一管编号',
                    anchor:'95%',
                    name : 'fiveone',
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

            lr_form = Ext.create('Ext.form.Panel', {
                labelWidth : 50,
                labelAlign : 'right',
                buttonAlign : 'center',
                //width :600,
                autoHeight:true,
                items : [{
                    width:700,
                    height:350,
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
                                    if(trStr[0].trim()==trStr2[0].trim()){
                                        if(i!=j){
                                            Ext.example.msg("提示","Excel样本编号重复："+trStr[0].trim());
                                            return;
                                        }
                                    }
                                }
                                for(j=0;j<len.length-1;j++){
                                    trStr2 = len[j].split("\t");
                                    if(trStr[3].trim()==trStr2[3].trim()){
                                        if(i!=j){
                                            Ext.example.msg("提示","Excel身份证号重复："+trStr[3].trim());
                                            return;
                                        }
                                    }
                                }
                                localStore.insert(0,{
                                    samplenum:trStr[0].trim(),
                                    partyName:trStr[1],
                                    phone:trStr[2],
                                    certNumber:trStr[3],
                                    inputAddress:trStr[4],
                                    fiveone:trStr[5],
                                    inputtime:trStr[6],
                                    inputname:trStr[7],
                                });
                            };
                            for(var i=0;i<len.length;i++){
                                trStr = len[i].split("\t");
                                console.log(trStr.length);
                                if(trStr.length == 8){
                                    for(var j=0;j<trStr.length;j++){
                                        if(trStr[j] == ""){
                                            var num = Number(i)+Number(1);
                                            Ext.example.msg("提示","第"+num+"行有一列为空！");
                                            return;
                                        }
                                    }
                                }else{
                                    var lie = Number(i)+Number(1);
                                    Ext.example.msg("提示","第"+lie+"行列数不正确！");
                                    return;
                                }

                            }
                        }
                        for(i=0;i<localStore.getCount();i++){
                            var record = localStore.getAt(i);
                            xjyb_jsonArray.push(record.data);
                        }
                        lr_window.hide();
                        // Ext.getCmp('qr_textarea').setValue(Ext.getCmp('lr_textarea').getValue());
                        qr_window.show();
                    }
                },{
                    text : '取消',
                    handler:function(){
                        lr_window.hide();
                    }
                }]
            });


            qr_form = Ext.create('Ext.form.Panel', {
                labelWidth : 50,
                labelAlign : 'right',
                buttonAlign : 'center',
                //width :600,
                autoHeight:true,
                items : [qrgrid],
                buttons : [ {
                    text : '提交',
                    handler : function() {
                        localStore.removeAll();
                        var str=Ext.getCmp("lr_textarea").getValue().replace(/\n$/,"");
                        var len = str.split("\n");//获取数据
                        var xjyb_jsonArray=[];
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
                                    if(trStr[0].trim()==trStr2[0].trim()){
                                        if(i!=j){
                                            Ext.example.msg("提示","Excel样本编号重复"+trStr[0].trim());
                                            return;
                                        }
                                    }
                                }
                                localStore.insert(0,{
                                    samplenum:trStr[0].trim(),
                                    partyName:trStr[1],
                                    phone:trStr[2],
                                    certNumber:trStr[3],
                                    inputAddress:trStr[4],
                                    fiveone:trStr[5],
                                    inputtime:trStr[6],
                                    inputname:trStr[7],
                                });
                            };
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
                                Ext.Msg.alert("消息",action.result.msg);
                                lr_form.getForm().reset();
                                qr_window.hide();
                                grid.getStore().reload();
                                // qrgrid.getStore().reload();

                            },
                            failure:handFailure
                        });
                    }
                },{
                    text : '取消',
                    handler:function(){
                        qr_window.hide();
                    }
                }]
            });

            qr_window = Ext.create('Ext.window.Window',{
                closeAction : 'hide',
                modal : true,
                items : qr_form
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
