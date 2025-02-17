<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>新冠病毒报告自动化</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="新冠病毒报告自动化">
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
                    },{
                        name : 'sampleType'
                    },{
                        name:'idcard'
                    },{
                        name:'sendingPerson'
                    },{
                        name:'inspection'
                    },{
                        name:'samplingDate'
                    },{
                        name:'receptionDate'
                    },{
                        name:'checkProject'
                    },{
                        name:'ct'
                    },{
                        name:'iss'
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
                    },{
                        name:'reservation'
                    },{
                        name:'fiveone'
                    },{
                        name:'englishName'
                    },{
                        name:'passport'
                    },{
                        name:'englishreport'
                    },{
                        name:'reportdate'
                    }
                ]
            });

            var main_store = Ext.create('Ext.data.Store', {
                model: 'mainPage',
                pageSize:23,//不加，点击下一页时默认25条
                proxy: {
                    type: 'ajax',
                    actionMethods : 'post',
                    url: 'norovirus!list1.action',
                    reader: {
                        // type: 'json',
                        root: 'result',
                        totalProperty : 'totalCount'
                    }
                },
            });




            main_store.on("beforeload",function(store,options){
                var searchSampleNo = Ext.getCmp("searchSampleNo");
               /* var searchName = Ext.getCmp("searchName");
                var searchReservation = Ext.getCmp("searchReservation");*/
                var name = Ext.getCmp("s_name");
                var sampleNo = Ext.getCmp("s_sampleNo");
                var englishName = Ext.getCmp("s_englishName");
                var idcard = Ext.getCmp("s_idcard");
                var passport = Ext.getCmp("s_passport");
                var reservation = Ext.getCmp("s_reservation");
                var inputName = Ext.getCmp("s_inputName");
                var inspection = Ext.getCmp("s_inspection");
                var checkProject = Ext.getCmp("s_checkProject");
                var detectionResult = Ext.getCmp("s_detectionResult");
                var samplingDate = Ext.getCmp("s_samplingDate");
                var receptionDate = Ext.getCmp("s_receptionDate");
                var reportdate = Ext.getCmp("s_reportdate");
                var inputTime = Ext.getCmp("s_inputTime");
                var sendingPerson = Ext.getCmp("s_sendingPerson");
                var englishreport = Ext.getCmp("s_englishreport");
                var qudao = Ext.getCmp("s_qudao");
                var ssamplingDate = Ext.getCmp("ss_samplingDate");
                var sreceptionDate = Ext.getCmp("ss_receptionDate");
                var sreportdate = Ext.getCmp("ss_reportdate");
                var sinputTime = Ext.getCmp("ss_inputTime");

                if(searchSampleNo){
                    Ext.apply(store.proxy.extraParams, {
                        searchSampleNo:searchSampleNo.getValue()
                    });
                }
                if(name){
                    Ext.apply(store.proxy.extraParams, {
                        name:name.getValue()
                    });
                }
                if(sampleNo){
                    Ext.apply(store.proxy.extraParams, {
                        sampleNo:sampleNo.getValue()
                    });
                }

                if(englishName){
                    Ext.apply(store.proxy.extraParams, {
                        englishName:englishName.getValue()
                    });
                }

                if(idcard){
                    Ext.apply(store.proxy.extraParams, {
                        idcard:idcard.getValue()
                    });
                }
                if(passport){
                    Ext.apply(store.proxy.extraParams, {
                        passport:passport.getValue()
                    });
                }
                if(reservation){
                    Ext.apply(store.proxy.extraParams, {
                        reservation:reservation.getValue()
                    });
                }
                if(inputName){
                    Ext.apply(store.proxy.extraParams, {
                        inputName:inputName.getValue()
                    });
                }
                if(inspection){
                    Ext.apply(store.proxy.extraParams, {
                        inspection:inspection.getValue()
                    });
                }
                if(checkProject){
                    Ext.apply(store.proxy.extraParams, {
                        checkProject:checkProject.getValue()
                    });
                }
                if(detectionResult){
                    Ext.apply(store.proxy.extraParams, {
                        detectionResult:detectionResult.getValue()
                    });
                }
                if(samplingDate){
                    Ext.apply(store.proxy.extraParams, {
                        samplingDate:samplingDate.getValue()
                    });
                }
                if(receptionDate){
                    Ext.apply(store.proxy.extraParams, {
                        receptionDate:receptionDate.getValue()
                    });
                }
                if(reportdate){
                    Ext.apply(store.proxy.extraParams, {
                        reportdate:reportdate.getValue()
                    });
                }
                if(inputTime){
                    Ext.apply(store.proxy.extraParams, {
                        inputTime:inputTime.getValue()
                    });
                }
                if(sendingPerson){
                    Ext.apply(store.proxy.extraParams, {
                        sendingPerson:sendingPerson.getValue()
                    });
                }
                if(englishreport){
                    Ext.apply(store.proxy.extraParams, {
                        englishreport:englishreport.getValue()
                    });
                }
                if(qudao){
                    Ext.apply(store.proxy.extraParams, {
                        qudao:qudao.getValue()
                    });
                }
                if(ssamplingDate){
                    Ext.apply(store.proxy.extraParams, {
                        ssamplingDate:ssamplingDate.getValue()
                    });
                }
                if(sreceptionDate){
                    Ext.apply(store.proxy.extraParams, {
                        sreceptionDate:sreceptionDate.getValue()
                    });
                }
                if(sreportdate){
                    Ext.apply(store.proxy.extraParams, {
                        sreportdate:sreportdate.getValue()
                    });
                }
                if(sinputTime){
                    Ext.apply(store.proxy.extraParams, {
                        sinputTime:sinputTime.getValue()
                    });
                }


                //Ext.apply(store.proxy.extraParams, {filter_LIKES_department00deptName_OR_department00description:'IT'});

            });

            main_store.loadPage(1);


            /**---------------------------------显示数据的Grid开始---------------------------*/
            var grid =Ext.create('Ext.grid.Panel', {
                id : 'show_data_grid',
                title : '新冠病毒报告自动化',
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
                    dataIndex : 'id',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header : '样本编号',
                    dataIndex : 'sampleNo',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                }, {
                    header : '姓名',
                    dataIndex : 'name',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header : '英文姓名',
                    dataIndex : 'englishName',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                }, {
                    header : '样本类型',
                    dataIndex : 'sampleType',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'身份证号',
                    width: 150,
                    dataIndex:'idcard',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header : '护照号',
                    dataIndex : 'passport',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'预约号',
                    width: 120,
                    dataIndex:'reservation',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'受检人单位',
                    dataIndex:'sendingPerson',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'采样单位',
                    dataIndex:'inspection',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'检测项目',
                    width: 170,
                    dataIndex:'checkProject',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'采样日期',
                    width: 150,
                    dataIndex:'samplingDate',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'接收日期',
                    width: 150,
                    dataIndex:'receptionDate',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header : '是否需要英文报告',
                    dataIndex : 'englishreport',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'来源渠道',
                    width: 150,
                    dataIndex:'qudao',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'检测结果',
                    width: 150,
                    dataIndex:'detectionResult',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'报告时间',
                    width: 150,
                    dataIndex:'reportdate',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'录入人',
                    width: 120,
                    dataIndex:'inputName',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'录入时间',
                    width: 150,
                    dataIndex:'inputTime',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'更新人',
                    width: 120,
                    dataIndex:'updateName',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'更新时间',
                    width: 150,
                    dataIndex:'updateTime',
                    renderer : function(data, cell, record, rowIndex,columnIndex, store) {
                        if(record.data["detectionResult"]=='阳性'){
                            return  "<font color='red'>"+data+"</font>";
                        }else{
                            return  data;
                        }
                    }
                },{
                    header:'报告地址',
                    width: 150,
                    dataIndex:'pathName',
                    hidden:true
                },{
                    header:'报告日期',
                    width: 150,
                    dataIndex:'reportdate',
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
                            screport = "norovirus!scReport.action";
                            sc_window.show();
                        }
                    },{
                        text : '补录生成报告',
                        id:'tbar8',
                        hidden:true,
                        icon : 'img/add.png',
                        handler : function() {
                            blsc_window.show();
                        }

                    },{
                        text : '合管生成报告',
                        id:'tbar11',
                        hidden:true,
                        icon : 'img/add.png',
                        handler : function() {
                            screport = "norovirus!fiveoneReport.action";
                            hg_window.show();
                        }
                    },{
                        text : '合管报告模板',
                        id:'tbar12',
                        hidden:true,
                        icon : 'img/0114.gif',
                        handler : function() {
                            window.location.href='norovirus!heguanExcel.action';
                        }
                    }
                    /*,{
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
                            window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/xinguankefu.xls';
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
                        text : '导出',
                        id:'tbar6',
                        hidden:true,
                        icon:'img/up.gif',
                        handler:function(){
                            var xjyb_jsonArray = [];
                            var record =grid.getSelectionModel().getSelection();
                            iscaiwu = '否';
                            for(var i=0;i<record.length;i++){
                                xjyb_jsonArray.push(record[i].data);
                            }
                            if(xjyb_jsonArray.length>0){
                                Ext.Ajax.request(
                                    {
                                        url: 'norovirus!downloadExcel.action',
                                        params: {'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                        success: function (response) {
                                            if(response.responseText=="否"){
                                                Ext.example.msg("提示", "最多导出30000条数据！");
                                                return;
                                            }
                                            Ext.example.msg("提示",response.responseText,4000);
                                            if(response.responseText=="EXCEL已导出"){
                                                window.location.href='schedulesort!downloadfile.action?helpdocs=xinguaninfo.xls';
                                            }
                                        },
                                        failure: handFailure,


                                    });
                            }else{
                                    Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                                    Ext.Ajax.request(
                                        {
                                            url: 'norovirus!downloadExcel.action',
                                            timeout: 100000000,
                                            params: {
                                                'inputTime':Ext.getCmp("s_inputTime").getValue(),
                                                'name ': Ext.getCmp("s_name").getValue(),
                                                'sampleNo': Ext.getCmp("s_sampleNo").getValue(),
                                                'englishName': Ext.getCmp("s_englishName").getValue(),
                                                'idcard': Ext.getCmp("s_idcard").getValue(),
                                                'passport': Ext.getCmp("s_passport").getValue(),
                                                'reservation': Ext.getCmp("s_reservation").getValue(),
                                                'inputName': Ext.getCmp("s_inputName").getValue(),
                                                'inspection': Ext.getCmp("s_inspection").getValue(),
                                                'checkProject': Ext.getCmp("s_checkProject").getValue(),
                                                'detectionResult': Ext.getCmp("s_detectionResult").getValue(),
                                                'samplingDate': Ext.getCmp("s_samplingDate").getValue(),
                                                'receptionDate': Ext.getCmp("s_receptionDate").getValue(),
                                                'reportdate': Ext.getCmp("s_reportdate").getValue(),
                                                'inputTime': Ext.getCmp("s_inputTime").getValue(),
                                                'sendingPerson': Ext.getCmp("s_sendingPerson").getValue(),
                                                'englishreport': Ext.getCmp("s_englishreport").getValue(),
                                                'qudao': Ext.getCmp("s_qudao").getValue(),
                                                'ssamplingDate': Ext.getCmp("ss_samplingDate").getValue(),
                                                'sreceptionDate': Ext.getCmp("ss_receptionDate").getValue(),
                                                'sreportdate': Ext.getCmp("ss_reportdate").getValue(),
                                                'sinputTime': Ext.getCmp("ss_inputTime").getValue(),
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
                        }
                    },{
                        text : '导出财务',
                        id:'tbar7',
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
                                        url: 'norovirus!caiwuExcel.action',
                                        params: {'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                        success: function (response) {
                                            if(response.responseText=="最多导出30000条数据！"){
                                                Ext.example.msg("提示", "最多导出30000条数据！");
                                                return;
                                            }
                                            Ext.example.msg("提示",response.responseText,4000);
                                            if(response.responseText=="EXCEL已导出"){
                                                window.location.href='schedulesort!downloadfile.action?helpdocs=xinguancaiwu.xls';
                                            }
                                        },
                                        failure: handFailure,


                                    });
                            }else{
                                Ext.Msg.wait('提示','正在导出Excel,请稍候...');
                                Ext.Ajax.request(
                                    {
                                        url: 'norovirus!caiwuExcel.action',
                                        timeout: 100000000,
                                        params: {
                                            'inputTime':Ext.getCmp("s_inputTime").getValue(),
                                            'name': Ext.getCmp("s_name").getValue(),
                                            'sampleNo': Ext.getCmp("s_sampleNo").getValue(),
                                            'englishName': Ext.getCmp("s_englishName").getValue(),
                                            'idcard': Ext.getCmp("s_idcard").getValue(),
                                            'passport': Ext.getCmp("s_passport").getValue(),
                                            'reservation': Ext.getCmp("s_reservation").getValue(),
                                            'inputName': Ext.getCmp("s_inputName").getValue(),
                                            'inspection': Ext.getCmp("s_inspection").getValue(),
                                            'checkProject': Ext.getCmp("s_checkProject").getValue(),
                                            'detectionResult': Ext.getCmp("s_detectionResult").getValue(),
                                            'samplingDate': Ext.getCmp("s_samplingDate").getValue(),
                                            'receptionDate': Ext.getCmp("s_receptionDate").getValue(),
                                            'reportdate': Ext.getCmp("s_reportdate").getValue(),
                                            'inputTime': Ext.getCmp("s_inputTime").getValue(),
                                            'sendingPerson': Ext.getCmp("s_sendingPerson").getValue(),
                                            'englishreport': Ext.getCmp("s_englishreport").getValue(),
                                            'qudao': Ext.getCmp("s_qudao").getValue(),
                                            'ssamplingDate': Ext.getCmp("ss_samplingDate").getValue(),
                                            'sreceptionDate': Ext.getCmp("ss_receptionDate").getValue(),
                                            'sreportdate': Ext.getCmp("ss_reportdate").getValue(),
                                            'sinputTime': Ext.getCmp("ss_inputTime").getValue(),
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
                            }
                        }
                    },{
                        text : '批量导出上传数据',
                        icon : 'img/up.gif',
                        id:'tbar9',
                        hidden:true,
                        handler:function(){
                            rdWindow.show();
                            //window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/nuoru.xls';
                        }
                    },{
                        text : '批量修改送检人单位',
                        icon : 'img/up.gif',
                        id:'tbar10',
                        hidden:true,
                        handler:function(){
                            xgsjWindow.show();
                            //window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/nuoru.xls';
                        }
                    },{
                        text : '修改',
                        icon : 'img/update.png',
                        id:'tbar13',
                        hidden:true,
                        handler:function(){
                            var record = grid.getSelectionModel().getSelection();
                            if (record.length) {
                                /* Ext.getCmp("tid1").setValue("specialmark!update.action");*/
                                Ext.getCmp("submit_btn").show();
                                window1.show();
                                form1.getForm().loadRecord(record[0]);
                            } else {
                                Ext.example.msg("提示","请选择要修改的数据");
                            }
                        }
                    },{
                        text : '重生成报告',
                        icon : 'img/up.gif',
                        id:'tbar14',
                        hidden:true,
                        handler:function(){

                            var record = grid.getSelectionModel().getSelection();
                            if (record.length) {
                                var xjyb_jsonArray=[];
                                if(record[0].data.detectionResult==null || record[0].data.detectionResult==''){
                                    Ext.Msg.alert("提示",'样本编号为'+record[0].data.sampleNo+'未上传结果');
                                    return;
                                }
                                if(record[0].data.detectionResult=='阳性'){
                                    Ext.Msg.alert("提示",'样本'+record[0].data.sampleNo+'为阳性,不能生成报告');
                                    return;
                                }
                                Ext.MessageBox.confirm("请确认","是否重新生成报告",function(button,text){
                                    if(button=="yes"){
                                        console.log(record[0])
                                        xjyb_jsonArray.push(record[0].data);
                                        sc_form.getForm().submit({
                                            url: "norovirus!scReport.action",
                                            timeout: 100000000,
                                            async: false,
                                            params:{'itmessc':Ext.encode(xjyb_jsonArray)},
                                            waitMsg : '正在提交数据',
                                            success: function(f, action) {
                                                if (action.result.success) {
                                                    if(action.result.msg == '报告重生成成功'){
                                                        Ext.example.msg('消息', action.result.msg);
                                                        main_store.load({
                                                            params: {
                                                                start:returnStart,
                                                                limit:pageSize
                                                            }
                                                        });
                                                        Ext.getCmp("reportPath").setValue(action.result.o);
                                                        cp.getForm().submit({
                                                            url:'norovirus!downloadreport.action',
                                                            timeout: 100000000
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
                                });
                            } else {
                                Ext.example.msg("提示","请选择要重新生成报告的数据");
                            }

                            //window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/nuoru.xls';
                        }
                    },{
                        text : '高级搜索',
                        icon : 'img/search.png',
                        id:'tbar15',
                        hidden:true,
                        handler:function(){
                            searchWindow.show();
                            //window.location.href='schedulesort!downloadfile.action?helpdocs=piliang/nuoru.xls';
                        }
                    },{
                        id:'searchSampleNo',
                        xtype:'textfield',
                        width:200,
                        emptyText:'请输入要查找的样本编号或客户姓名',
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
                    }/*,"-",{
                id:'searchReservation',
                xtype:'textfield',
                width:200,
                emptyText:'请输入要查找的预约号或身份证',
                enableKeyEvents:true,
                listeners:{
                    keyup:function(tf,e){
                        var key = e.getKey();
                        if(key == e.ENTER){
                            /!*main_store.setBaseParam("start",0);
                            main_store.setBaseParam("limit",pageSize);
                            main_store.setBaseParam("sort",'outSampleId'); Ext3后台排序参数
                            main_store.setBaseParam("dir",'DESC'); Ext3后台排序参数
                            main_store.load();  *!/
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
                            /!*main_store.setBaseParam("start",0);
                            main_store.setBaseParam("limit",pageSize);
                            main_store.setBaseParam("sort",'outSampleId'); Ext3后台排序参数
                            main_store.setBaseParam("dir",'DESC'); Ext3后台排序参数
                            main_store.load();  *!/
                            main_store.loadPage(1);
                        }
                    }
                }
            }*/,"-",{
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
            rdForm = Ext.create('Ext.form.FormPanel', {
                id : 'rd_form',
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
                            xtype: 'fieldcontainer',
                            fieldLabel: '导出时间',
                            layout : 'hbox',
                            items: [
                                {
                                    name: 'startdate',
                                    xtype : 'datefield',
                                    format :'Y-m-d',
                                    editable:false,
                                    id:'startdate',
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
                        rdForm.getForm().submit({
                            url:'norovirus!reportdataexcel.action',
                            waitMsg:'正在提交数据',
                            success:function(f,action){
                                console.log(action.result);
                                if(action.result.success){
                                    if(action.result.msg.toString().indexOf("Excel已导出")!=-1){
                                        Ext.example.msg("提示",action.result.msg,3000)
                                        window.location.href='schedulesort!downloadfile.action?helpdocs=核酸检测信息-北京优迅医学检验实验室-孟岳峰-13718137762.xls'
                                    }
                                }else{
                                    Ext.Msg.alert("消息",action.result.msg,4000);
                                }
                            },
                            failure:handFailure
                        })

                        main_store.loadPage(1);
                        rdWindow.hide();
                    }
                }, {
                    text : '取消',
                    handler : function () {
                        rdForm.form.reset();
                        rdWindow.hide();
                    }
                }
                ]
            });
            var rdWindow = Ext.create('Ext.Window', {
                id : "searchWindow",
                closeAction : 'hide',
                title : "高级搜索",
                modal : true,
                items : rdForm

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
                                    reservation:trStr[3],
                                    idcard:trStr[4],
                                    passport:trStr[5],
                                    sendingPerson:trStr[6],
                                    samplingDate:trStr[7],
                                    sampleType:trStr[8],
                                    receptionDate:trStr[9],
                                    checkProject:trStr[10],
                                    inspection:trStr[11],
                                    englishreport:trStr[12],
                                    qudao:trStr[13],
                                    //detectionResult:trStr[10]
                                });

                            };
                        }

                        for(i=0;i<localStore.getCount();i++){
                            var record = localStore.getAt(i);
                            xjyb_jsonArray.push(record.data);
                        }

                        xjyb_form.getForm().submit({
                            url: 'norovirus!isyuyue.action',
                            timeout: 100000000,
                            async: false,
                            params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                            waitMsg : '正在提交数据',
                            success: function(f, action) {
                                if (action.result.success) {
                                    if(action.result.msg.indexOf(",")!=-1){
                                        Ext.Msg.alert('提示', '样本为'+action.result.msg+'预约已绑定或已补录,请修改后重新上传');
                                        return;
                                    }

                                    // xjyb_form.getForm().submit({
                                    //     url: 'norovirus!isyuyue.action',
                                    //     timeout: 100000000,
                                    //     async: false,
                                    //     params:{'itemsxjyb':Ext.encode(xjyb_jsonArray)},
                                    //     waitMsg : '正在提交数据',
                                    //     success: function(f, action) {
                                    //         if (action.result.success) {
                                    //             if(action.result.msg.indexOf(",")!=-1){
                                    //                 Ext.Msg.alert('提示', '样本为'+action.result.msg+'预约已绑定或已补录,请修改后重新上传');
                                    //                 return;
                                    //             }


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

            //是否需要英文报告
            var englishreport_combox =Ext.create('Ext.form.ComboBox',{
                name:'englishreport',
                id:'s_englishreport',
                mode: 'local',
                fieldLabel:'是否英文报告',
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
            });

            //检测结果
            var detectionResult_combox =Ext.create('Ext.form.ComboBox',{
                name:'detectionResult',
                id:'s_detectionResult',
                mode: 'local',
                fieldLabel:'检测结果',
                triggerAction: 'all',
                store: Ext.create('Ext.data.ArrayStore',{
                    idIndex :0,
                    fields:[
                        {name : 'saveresult'},
                        {name : 'showresult'}
                    ],
                    data :[
                        [ '', '' ],
                        [ '阴性', '阴性' ],
                        [ '阳性', '阳性' ]
                    ]
                }),
                valueField: 'saveresult',
                displayField: 'showresult',
            });


            var form1 =Ext.create('Ext.form.FormPanel',{
                id:'update_form',
                buttonAlign:'center',
                border:false,
                width:900,
                autoHeight:true,
                fieldDefaults: {
                    labelAlign: 'right',
                    labelWidth:120
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
                        columnWidth: .45,
                        style: "margin-left: 2px;padding-left:5px",
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
                            id:'updatesampleNo',
                            name:'sampleNo',
                            xtype:'textfield',
                            fieldLabel:'样本编号',
                            listeners: {
                                blur: function () {
                                    if (this.getValue().trim().length != 0) {
                                        Ext.Ajax.request({
                                            url: 'norovirus!checkSampleNo.action',
                                            params: {
                                                sampleNo: this.getValue().trim(),
                                                Id: Ext.getCmp('updateId').getValue()
                                            },
                                            success: function (response) {
                                                if (response.responseText == "存在") {
                                                    Ext.getCmp('updatesampleNo').setValue();
                                                    Ext.example.msg('', "样本编号已存在", 3000);
                                                }
                                            },
                                            failure: handFailure
                                        });
                                    }
                                }
                            }
                        },{
                            id:'updateName1',//updateForm.getForm().loadRecord(record[0]);首先根据id赋值，所以不能使用updateName,此处是特例
                            name:'name',
                            xtype:'textfield',
                            fieldLabel:'姓名'
                        },{
                            id:'updateEnglishName',//updateForm.getForm().loadRecord(record[0]);首先根据id赋值，所以不能使用updateName,此处是特例
                            name:'englishName',
                            xtype:'textfield',
                            fieldLabel:'英文姓名'
                        },{
                            id:'updateSampleType',
                            name:'sampleType',
                            xtype:'textfield',
                            fieldLabel:'样本类型'
                        },{
                            id:'updateIdcard',
                            name:'idcard',
                            xtype:'textfield',
                            fieldLabel:'身份证'
                        },{
                            id:'updatePassport',
                            name:'passport',
                            xtype:'textfield',
                            fieldLabel:'护照号'
                        },{
                            id:'updateReservation',
                            name:'reservation',
                            xtype:'textfield',
                            fieldLabel:'预约号'
                        }]

                    },{
                        layout:'form',
                        columnWidth: .45,
                        style: "margin-left: 2px;padding-left:5px",
                        border:false,
                        defaults:{
                            xtype:'textfield',
                            anchor:'95%'
                        },
                        items:[{
                            id:'updateSendingPerson',
                            name:'sendingPerson',
                            xtype:'textfield',
                            fieldLabel:'受检人单位'
                        },{
                            id:'updateInspection',
                            name:'inspection',
                            xtype:'textfield',
                            fieldLabel:'采样单位'
                        },{
                            id:'updateCheckProject',
                            name:'checkProject',
                            xtype:'textfield',
                            fieldLabel:'检测项目'
                        },{
                            id:'updateSamplingDate',
                            name:'samplingDate',
                            xtype  : 'datefield',
                            format :'Y-m-d H:i:s',
                            fieldLabel:'采样日期'
                        },{
                            id:'updateReceptionDate',
                            name:'receptionDate',
                            xtype  : 'datefield',
                            format :'Y-m-d H:i:s',
                            fieldLabel:'接收日期'
                        },{
                            id:'updateEnglishreport',
                            name:'englishreport',
                            xtype:'textfield',
                            fieldLabel:'英文报告'
                        },{
                            id:'updateQudao',
                            name:'qudao',
                            xtype:'textfield',
                            fieldLabel:'渠道'
                        },{
                            id:'updateReportdate',
                            name:'reportdate',
                            xtype  : 'datefield',
                            format :'Y-m-d H:i:s',
                            fieldLabel:'报告日期'
                        }]

                    }]

                }],
                buttons:[{
                    text:'提交',
                    id:'submit_btn',
                    handler:function(){
                        form1.getForm().submit({
                            url:'norovirus!update.action',
                            waitMsg:'正在提交数据',
                            success:function(f,action){
                                if(action.result.success){
                                    form1.getForm().reset();
                                    window1.hide();
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
                        form1.getForm().reset();
                        window1.hide();
                    }
                }]

            });

            var window1 =Ext.create('Ext.Window',{
                id:'updateWindow',
                closeAction:'hide',
                title:'修改',
                modal:true,
                items : form1
            });

            var searchForm=Ext.create('Ext.form.FormPanel',{
                id:'search_form',
                buttonAlign:'center',
                border:false,
                width:900,
                autoHeight:true,
                fieldDefaults: {
                    labelAlign: 'right',
                    labelWidth:120
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
                        style: "margin-left: 2px;padding-left:5px",
                        border:false,
                        defaults:{
                            xtype:'textfield',
                            anchor:'95%'
                        },
                        items:[{
                            id:'s_sampleNo',
                            name:'sampleNo',
                            xtype:'textfield',
                            fieldLabel:'样本编号'
                        },{
                            id:'s_name',//updateForm.getForm().loadRecord(record[0]);首先根据id赋值，所以不能使用updateName,此处是特例
                            name:'name',
                            xtype:'textfield',
                            fieldLabel:'姓名'
                        },{
                            id:'s_englishName',//updateForm.getForm().loadRecord(record[0]);首先根据id赋值，所以不能使用updateName,此处是特例
                            name:'englishName',
                            xtype:'textfield',
                            fieldLabel:'英文姓名'
                        },{
                            id:'s_idcard',
                            name:'idcard',
                            xtype:'textfield',
                            fieldLabel:'身份证'
                        },{
                            id:'s_passport',
                            name:'passport',
                            xtype:'textfield',
                            fieldLabel:'护照号'
                        },{
                            id:'s_reservation',
                            name:'reservation',
                            xtype:'textfield',
                            fieldLabel:'预约号'
                        },{
                            id:'s_inputName',
                            name:'inputName',
                            xtype:'textfield',
                            fieldLabel:'录入人'
                        }]

                    },{
                        layout:'form',
                        columnWidth: .32,
                        style: "margin-left: 2px;padding-left:5px",
                        border:false,
                        defaults:{
                            xtype:'textfield',
                            anchor:'95%'
                        },
                        items:[{
                            id:'s_inspection',
                            name:'inspection',
                            xtype:'textfield',
                            fieldLabel:'采样单位'
                        },{
                            id:'s_checkProject',
                            name:'checkProject',
                            xtype:'textfield',
                            fieldLabel:'检测项目'
                        },detectionResult_combox,{
                            id:'s_samplingDate',
                            name:'samplingDate',
                            xtype  : 'datefield',
                            format :'Y-m-d',
                            fieldLabel:'采样日期起',
                            editable:false
                        },{
                            id:'s_receptionDate',
                            name:'receptionDate',
                            xtype  : 'datefield',
                            format :'Y-m-d',
                            fieldLabel:'接收日期起',
                            editable:false
                        },{
                            id:'s_reportdate',
                            name:'reportdate',
                            xtype  : 'datefield',
                            format :'Y-m-d',
                            fieldLabel:'报告日期起',
                            editable:false
                        },{
                            id:'s_inputTime',
                            name:'reportdate',
                            xtype  : 'datefield',
                            format :'Y-m-d',
                            fieldLabel:'录入日期起',
                            editable:false
                        }]

                    },{
                        layout:'form',
                        columnWidth: .32,
                        style: "margin-left: 2px;padding-left:5px",
                        border:false,
                        defaults:{
                            xtype:'textfield',
                            anchor:'95%'
                        },
                        items:[{
                            id:'s_sendingPerson',
                            name:'sendingPerson',
                            xtype:'textfield',
                            fieldLabel:'受检人单位'
                        },englishreport_combox,{
                            id:'s_qudao',
                            name:'qudao',
                            xtype:'textfield',
                            fieldLabel:'渠道'
                        },{
                            id:'ss_samplingDate',
                            name:'samplingDate',
                            xtype  : 'datefield',
                            format :'Y-m-d',
                            fieldLabel:'采样日期止',
                            editable:false
                        },{
                            id:'ss_receptionDate',
                            name:'receptionDate',
                            xtype  : 'datefield',
                            format :'Y-m-d',
                            fieldLabel:'接收日期止',
                            editable:false
                        },{
                            id:'ss_reportdate',
                            name:'reportdate',
                            xtype  : 'datefield',
                            format :'Y-m-d',
                            fieldLabel:'报告日期止',
                            editable:false
                        },{
                            id:'ss_inputTime',
                            name:'reportdate',
                            xtype  : 'datefield',
                            format :'Y-m-d',
                            fieldLabel:'录入日期止',
                            editable:false
                        }]

                    }]

                }],
                buttons:[{
                    text:'查询',
                    handler:function(){
                        main_store.loadPage(1);
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

            hg_form = Ext.create('Ext.form.Panel', {
                labelWidth : 50,
                labelAlign : 'right',
                buttonAlign : 'center',
                //width :600,
                autoHeight:true,
                items : [{
                    width:500,
                    height:300,
                    id:'hg_textarea',
                    xtype : 'textarea',
                    name : 'hg_textarea'
                }],
                buttons : [ {
                    text : '提交',
                    handler : function() {
                        localStore.removeAll();
                        var str=Ext.getCmp("hg_textarea").getValue().replace(/\n$/,"");
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
                                            Ext.example.msg("提示","Excel合管编号重复"+trStr[0].trim());
                                            return;
                                        }
                                    }
                                }
                                localStore.insert(0,{
                                    fiveone:trStr[0].trim(),
                                    detectionResult:trStr[1]
                                });

                            };
                        }

                        for(i=0;i<localStore.getCount();i++){
                            var record = localStore.getAt(i);
                            xjyb_jsonArray.push(record.data);
                        }

                        hg_form.getForm().submit({
                            url: screport,
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



                                        hg_window.hide();
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
                                        hg_window.hide();
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
                            hg_window.hide();
                        }
                    } ]
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
                            url: screport,
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

            var blsc_form = Ext.create('Ext.form.Panel', {
                labelWidth : 50,
                labelAlign : 'right',
                buttonAlign : 'center',
                //width :600,
                autoHeight:true,
                items : [{
                    width:500,
                    height:300,
                    id:'blsc_textarea',
                    xtype : 'textarea',
                    name : 'textarea',
                    emptyText: '温馨提示：此生成报告方式只用于生成报告不展示样本编号的情况',
                }],
                buttons : [ {
                    text : '提交',
                    handler : function() {
                        localStore.removeAll();
                        var str=Ext.getCmp("blsc_textarea").getValue().replace(/\n$/,"");
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
                                if(trStr.length!=12){
                                    Ext.example.msg("提示","姓名为"+trStr[1].trim()+"的数据有未填项",3000);
                                    return;
                                }
                                if(trStr[11]!="阴性"){
                                    Ext.example.msg("提示","姓名为"+trStr[1].trim()+"的数据是阳性样本,无法生成报告",3000);
                                    return;
                                }
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
                                    name:trStr[1],
                                    reservation:trStr[2],
                                    idcard:trStr[3],
                                    sendingPerson:trStr[4],
                                    samplingDate:trStr[5],
                                    sampleType:trStr[6],
                                    receptionDate:trStr[7],
                                    checkProject:trStr[8],
                                    inspection:trStr[9],
                                    qudao:trStr[10],
                                    detectionResult:trStr[11]
                                });

                            };
                        }

                        for(i=0;i<localStore.getCount();i++){
                            var record = localStore.getAt(i);
                            xjyb_jsonArray.push(record.data);
                        }

                        xjyb_form.getForm().submit({
                            url: 'norovirus!blscReport.action',
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
                                        Ext.MessageBox.confirm("请确认","是否要下载该批次添加的报告",function(button,text){
                                            if(button=="yes"){
                                                Ext.getCmp("reportPath").setValue(action.result.o);
                                                cp.getForm().submit({
                                                    url:'norovirus!downloadreport.action',
                                                    timeout: 100000000
                                                });
                                            }
                                        });

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

            blsc_window = Ext.create('Ext.window.Window',{
                title : '补录生成报告',
                closeAction : 'hide',
                modal : true,
                items : blsc_form
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
                    },{
                        name : 'sampleType',
                        type : 'string'
                    },{
                        name:'idcard'
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
                        name:'reservation',
                        type : 'string'
                    },{
                        name:'qudao',
                        type : 'string'
                    },{
                        name:'fiveone',
                        type:'string'
                    },{
                        name:'englishName',
                        type:'string'
                    },{
                        name:'passport',
                        type:'string'
                    },{
                        name:'englishreport',
                        type:'string'
                    },{
                        name:'reportdate',
                        type:'string'
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
                title:'高级搜索',
                id:'searchwindow',
                closeAction: 'hide',
                autoScroll:true,
                modal:true,
                items:searchForm

            });
            var iscaiwu = '否';
            //生成报告路径 预约生成报告还是客服生成报告
            var screport = "";

            sc_window = Ext.create('Ext.window.Window',{
                title : '生成报告',
                closeAction : 'hide',
                modal : true,
                items : sc_form
            });

            hg_window = Ext.create('Ext.window.Window',{
                title : '合管生成报告',
                closeAction : 'hide',
                modal : true,
                items : hg_form
            });

            pi_window = Ext.create('Ext.window.Window',{
                title : '批量下载报告',
                closeAction : 'hide',
                modal : true,
                items : pi_form
            });

            xgsjForm = Ext.create('Ext.form.Panel', {
                labelWidth : 50,
                labelAlign : 'right',
                buttonAlign : 'center',
                //width :600,
                autoHeight:true,
                items : [{
                    width:500,
                    height:300,
                    id:'xgsj_textarea',
                    xtype : 'textarea',
                    name : 'textarea',
                    emptyText : '可直接复制EXCEL表中的对应列到此处，格式如下：送检人单位-样本编号'
                }],
                buttons : [ {
                    text : '提交',
                    handler : function() {
                        localStore.removeAll();
                        var str=Ext.getCmp("xgsj_textarea").getValue().replace(/\n$/,"");
                        var len = str.split("\n");//获取数据
                        var xjyb_jsonArray=[];
                        var trStr;
                        var nullno="";
                        var i;
                        var j;
                        if(len.length>0){
                            console.log(length.length);
                            for(var i=0;i<len.length;i++){
                                //excel表格同一行的多个cell是以空格 分割的，此处以空格为单位对字符串做 拆分操作。。
                                trStr = len[i].split("\t");
                                localStore.insert(0,{
                                    sendingPerson:trStr[0].trim(),
                                    sampleNo:trStr[1].trim()
                                });

                            };
                        }

                        for(i=0;i<localStore.getCount();i++){
                            var record = localStore.getAt(i);
                            xjyb_jsonArray.push(record.data);
                        }

                        sc_form.getForm().submit({
                            url: 'norovirus!updateSendingPerson.action',
                            timeout: 100000000,
                            async: false,
                            params:{'itmessc':Ext.encode(xjyb_jsonArray)},
                            waitMsg : '正在提交数据',
                            success: function(f, action) {
                                Ext.example.msg('消息', action.result.msg);
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
            xgsjWindow = Ext.create('Ext.window.Window',{
                title : '批量修改送检人单位',
                closeAction : 'hide',
                modal : true,
                items : xgsjForm
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
                title : '新冠病毒报告自动化',
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
