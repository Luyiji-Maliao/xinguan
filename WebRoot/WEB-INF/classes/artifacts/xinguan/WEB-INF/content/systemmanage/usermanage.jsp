<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<title>用户管理</title>
		<jsp:include page="/include/header4.jsp"></jsp:include>
		<script type="text/javascript">


        //密码
        Ext.apply(Ext.form.VTypes, {
            password: function (val, field) {
                if (field.initialPassField) {
                    var pwd = Ext.getCmp(field.initialPassField);
                    return (val == pwd.getValue());
                }
                return true;
            },

            passwordText: '密码不匹配'
        });

        Ext.onReady(function () {
            Ext.QuickTips.init();
            var pageSize = 23;
            //gridColumnstate();

            /**------------------------存储Grid中的主数据--------------------------------*/
            Ext.define('mainPage', {
                extend: 'Ext.data.Model',
                fields: [
                    {
                        name: 'id'
                    }, {
                        name: 'username'
                    }, {
                        name: 'name'
                    }, {
                        name: 'sex'
                    }, {
                        name: 'email'
                    }, {
                        name: 'mobile'
                    }, {
                        name: 'phone'
                    }, {
                        name: 'qq'
                    }, {
                        name: 'roles'
                    }, {
                        name: 'loginStatus'
                    }, {
                        name: 'roleright'
                    }, {
                        name: 'department.deptName'
                    }, {
                        name: 'position.posName'
                    }, {
                        name: 'idNumber'
                    }, {
                        name: 'emePerson',
                    }, {
                        name: 'emePhone'
                    }, {
                        name: 'school'
                    }, {
                        name: 'userProfession'
                    }, {
                        name: 'address'
                    }, {
                        name: 'education'
                    }, {
                        name: 'entryDate'
                    }, {
                        name: 'regularDate'
                    }, {
                        name: 'contractDate'
                    }, {
                        name: 'leaveDate'
                    }, {
                        name: 'departmentId'
                    }, {
                        name: 'positionId'
                    }, {
                        name: 'jobNumber'
                    }, {
                        name: 'jobLocation'
                    }, {
                        name: 'jobType'
                    }, {
                        name: 'nation'
                    }, {
                        name: 'politicsStatus'
                    }, {
                        name: 'isMarried'
                    }, {
                        name: 'residence'
                    }, {
                        name: 'nativePlace'
                    }, {
                        name: 'firstEntryDate'
                    }, {
                        name: 'isSignedLc'
                    }, {
                        name: 'isSignedIcca'
                    }, {
                        name: 'residenceType'
                    }, {
                        name: 'isPaidInsur'
                    }
                    , {
                        name: 'remark'
                    }
                ]
            });

            var main_store = Ext.create('Ext.data.Store', {
                model: 'mainPage',
                pageSize: 23,//不加，点击下一页时默认25条
                proxy: {
                    type: 'ajax',
                    actionMethods: 'POST', // by default GET
                    url: 'user.action',
                    reader: {
                        type: 'json',
                        root: 'result',
                        totalProperty: 'totalCount'
                    }
                },
            });

            main_store.on("beforeload", function (store, options) {
                var searchField = Ext.getCmp("searchField");
                if (searchField) {
                    var new_params = {filter_LIKES_username_OR_name: searchField.getValue()};
                    Ext.apply(store.proxy.extraParams, new_params);
                }
                //Ext.apply(store.proxy.extraParams, {filter_EQI_loginStatus:1});

                //多条件
                if (Ext.getCmp("search_data_form")) {
                    if (Ext.getCmp("username")) {
                        Ext.apply(store.proxy.extraParams, {filter_LIKES_account: Ext.getCmp("username").getValue()});
                    }

                    if (Ext.getCmp("name")) {
                        Ext.apply(store.proxy.extraParams, {filter_LIKES_name: Ext.getCmp("name").getValue()});
                    }

                    if (Ext.getCmp("departmentId")) {
                        Ext.apply(store.proxy.extraParams, {filter_EQI_departmentId: Ext.getCmp("departmentId").getValue()});
                    }

                    if (Ext.getCmp("mobile")) {
                        Ext.apply(store.proxy.extraParams, {filter_LIKES_mobile: Ext.getCmp("mobile").getValue()});
                    }

                    if (Ext.getCmp("phone")) {
                        Ext.apply(store.proxy.extraParams, {filter_LIKES_officePhone: Ext.getCmp("phone").getValue()});
                    }

                    if (Ext.getCmp("qq")) {
                        Ext.apply(store.proxy.extraParams, {filter_LIKES_userqq: Ext.getCmp("qq").getValue()});
                    }

                    if (Ext.getCmp("sex")) {
                        if (Ext.getCmp("sex").getValue() != null) {
                            Ext.apply(store.proxy.extraParams, {filter_EQS_sex: Ext.getCmp("sex").getValue().inputValue});
                        } else {
                            Ext.apply(store.proxy.extraParams, {filter_EQS_sex: null});
                        }
                    }

                    if (Ext.getCmp("loginStatus")) {
                        if (Ext.getCmp("loginStatus").getValue() != null) {
                            Ext.apply(store.proxy.extraParams, {filter_EQI_loginStatus: Ext.getCmp("loginStatus").getValue().inputValue});
                        } else {
                            Ext.apply(store.proxy.extraParams, {filter_EQI_loginStatus: null});
                        }
                    }
                }

            });

            main_store.loadPage(1);


            /**---------------------------------显示数据的Grid开始---------------------------*/
            var grid = Ext.create('Ext.grid.Panel', {
                id: 'show_data_grid',
                title: '显示用户',
                width: 600,
                region: 'center',
                loadMask: true,
                stripeRows: true,
                frame: true,
                store: main_store,
                viewConfig: {
                    enableTextSelection: true  //可复制内容
                },
                columns: [{
                    id: '1',
                    hidden: false,
                    header: '编号',
                    width: 50,
                    sortable: true,
                    dataIndex: 'id'
                }, {
                    hidden: false,
                    header: '所有权限',
                    align: 'center',
                    xtype: 'actioncolumn',
                    width: 100,
                    items: [{
                        icon: 'img/users.png',
                        align: 'center',
                        tooltip: '权限',
                        handler: function (grid, rowIndex, colIndex) {
                            var rec = main_store.getAt(rowIndex);
                           
                        var myModel = Ext.define("rights", {  
					            extend : "Ext.data.Model",  
					            fields : [ {  
					                type : "string",  
					                name : "moduleNames"  
					            }, {  
					                type : "string",  
					                name : "rights"  
					            }]  
					     });  
						 var myStore = Ext.create("Ext.data.Store", {
						      model: "rights"
						 });   
						 
						 // 表格
							var ItemGrid = new Ext.grid.Panel({
								columns : [{
									text : "模块",
									width:350,
									dataIndex : "moduleNames"
								}, {
									text : "权限",
									width:790,
									dataIndex : "rights"
								}],
							
						        height: 480,
						        autoScroll: true,
						      
						        split: true,
						     
						        margin: 0,
						        store: myStore,
						        columnLines: true,
						        viewConfig: {
						            stripeRows: true,//在表格中显示斑马线
						            enableTextSelection: true //可以复制单元格文字 "GGXH", "XMSL", "XMDJ", "XMJE", "SL", "SE", "SPBM", "TaxItem"],
						        }
							});
							
							// 窗口
							var new_windows = Ext.create("Ext.window.Window", {
								title : "角色权限表",
								width : 1150,
								height : 500,
								closable: true,
						       // closeAction: 'hide',
						        modal: true,
						        frame: true,
						        layout: {
						            type: 'border',
						            padding: 0
						        },
						        items: [{
						            region: 'center',
						            items: ItemGrid
						        }]
							});

							new_windows.show();
							
						var j=rec.get("roles").length;
						var flag=0;
                        for(var i=0;i<j;i++){
                        flag++;
                        //alert(rec.get("roles")[i].id+",,,,,,,,"+rec.get("roles")[i].roleName);
                        var roleName=rec.get("roles")[i].roleName;
                        Ext.Ajax.request({
						url:'role!roleuser.action',
						async:false,
						params: { id: rec.get("roles")[i].id},
						success:function(response){
							var resultData = Ext.decode(response.responseText);
							myStore.add({moduleNames:"<font style='color:red;'>"+roleName+"</font>",rights: ''});  
							for(var i=0;i<resultData.moduleNames.split("*").length;i++){
								if(resultData.jspPages.split("*")[i]!=''){
									myStore.add({moduleNames: resultData.moduleNames.split("*")[i],rights: resultData.rights.split("*")[i]});  
								}
							}
							
						},
						failure:function(){
							Ext.example.msg("提示","很遗憾，发生了一点异常，请稍候再试！");
						}
					});
                    }   
                        
					
						
							
                    }
                    }]
                }, {
                    id: '2',
                    hidden: false,
                    header: '设置角色',
                    align: 'center',
                    xtype: 'actioncolumn',
                    width: 100,
                    items: [{
                        icon: 'img/users.png',
                        align: 'center',
                        tooltip: '设置角色',
                        handler: function (grid, rowIndex, colIndex) {
                            var rec = main_store.getAt(rowIndex);
                            Ext.Ajax.request({
                                scope: this,
                                url: 'role!listAll.action',
                                success: function (response) {
                                    var data = Ext.decode(response.responseText);
                                    var items = [];
                                    var items_s = [];
                                    for (var i = 0; i < data.length; i++) {
                                        var role = data[i];
                                        items.push({
                                            boxLabel: '' + role.roleName + '',
                                            name: 'roleIds',
                                            inputValue: role.id
                                        });
                                        items_s.push(role.roleName);
                                    }
                                    var s = rec.get("roles");
                                    if (s != null) {
                                        for (var j = 0; j < s.length; j++) {
                                            items.splice(items_s.indexOf(s[j].roleName), 1, {
                                                boxLabel: s[j].roleName,
                                                name: 'roleIds',
                                                inputValue: s[j].id,
                                                checked: true
                                            });
                                        }
                                    }
                                    var roleCheckgroup = new Ext.form.CheckboxGroup({
                                        columns: 3,
                                        items: items
                                    });
                                    var roleForm = new Ext.FormPanel({
                                        width: 800,
                                        labelWidth: 10,
                                        height: 300,
                                        autoScroll: true,
                                        buttonAlign: 'center',
                                        url: 'user!userRole.action',
                                        items: [{
                                            xtype: 'hidden',
                                            name: 'id',
                                            value: rec.data.id
                                        }, roleCheckgroup],
                                        buttons: [{
                                            text: '提交',
                                            handler: function () {
                                                roleForm.form.submit({
                                                    success: function (f, action) {
                                                        if (action.result.success) {
                                                            roleWindow.hide();
                                                            grid.getStore().reload();
                                                            roleForm.getForm().reset();
                                                            Ext.example.msg('消息', action.result.msg);
                                                        }
                                                    },
                                                    failure: handFailure
                                                });
                                            }
                                        }, {
                                            text: '重置',
                                            handler: function () {
                                                roleForm.form.reset();
                                            }
                                        }]
                                    });
                                    var roleWindow = new Ext.Window({
                                        title: '所有角色',
                                        modal: true,
                                        autoScroll: true,
                                        items: roleForm
                                    });
                                    roleWindow.show();
                                },
                                failure: handFailure

                            });
                        }
                    }]
                }, {
                    id: '3',
                    hidden: false,
                    header: '账号',
                    dataIndex: 'username'
                }, {
                    id: '4',
                    hidden: false,
                    header: '真实姓名',
                    dataIndex: 'name'
                }, {
                    id: '5',
                    hidden: false,
                    header: '部门',
                    dataIndex: 'department.deptName'
                }, {
                    id: '6',
                    hidden: false,
                    header: '职位',
                    dataIndex: 'position.posName'
                }, {
                    id: '7',
                    hidden: false,
                    header: '性别',
                    dataIndex: 'sex'
                }, {
                    id: '8',
                    hidden: false,
                    header: '电子邮箱',
                    dataIndex: 'email'
                }, {
                    id: '9',
                    hidden: false,
                    header: '手机号码',
                    dataIndex: 'mobile'
                }, {
                    id: '10',
                    hidden: false,
                    header: '办公电话',
                    dataIndex: 'phone'
                }, {
                    id: '11',
                    hidden: false,
                    header: 'QQ',
                    dataIndex: 'qq'
                }, {
                    id: '12',
                    hidden: false,
                    header: '拥有角色',
                    dataIndex: 'roles',
                    renderer: function (v) {
                        var a = [];
                        for (var i = 0; i < v.length; i++) {
                            a.push('<a href="role!modulepage.action?roleName=' + v[i].roleName + '">' + v[i].roleName + '</a>');
                        }
                        return a.join(",");
                    }
                }, {
                    id: '13',
                    hidden: false,
                    header: '用户状态',
                    dataIndex: 'loginStatus',
                    renderer: function (u) {
                        if (u == 0) {
                            return '失效';
                        } else {
                            return '生效';
                        }
                    }
                }, {
                    id: '14',
                    hidden: false,
                    header: '工号',
                    dataIndex: 'jobNumber'
                }, {
                    id: '15',
                    hidden: false,
                    header: '工作性质',
                    dataIndex: 'jobType'
                }, {
                    id: '16',
                    hidden: false,
                    header: '民族',
                    dataIndex: 'nation'
                }, {
                    id: '17',
                    hidden: false,
                    header: '政治面貌',
                    dataIndex: 'politicsStatus'
                }, {
                    id: '18',
                    hidden: false,
                    header: '婚姻状况',
                    dataIndex: 'isMarried'
                }, {
                    id: '19',
                    hidden: false,
                    header: '户口所在地',
                    dataIndex: 'residence'
                }, {
                    id: '20',
                    hidden: false,
                    header: '籍贯',
                    dataIndex: 'nativePlace'
                }, {
                    id: '21',
                    hidden: false,
                    header: '参加工作时间',
                    dataIndex: 'firstEntryDate'
                }, {
                    id: '22',
                    hidden: false,
                    header: '是否签订合同',
                    dataIndex: 'isSignedLc'
                }, {
                    id: '23',
                    hidden: false,
                    header: '是否签订知识产权及保密协议',
                    dataIndex: 'isSignedIcca'
                }, {
                    id: '24',
                    hidden: false,
                    header: '户口性质',
                    dataIndex: 'residenceType'
                }, {
                    id: '25',
                    hidden: false,
                    header: '是否在本市缴纳过保险',
                    dataIndex: 'isPaidInsur'
                }, {
                    id: '26',
                    hidden: false,
                    header: '常驻工作地',
                    dataIndex: 'jobLocation'
                }]
                ,
                //selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
                listeners: {

                    rowdblclick: function (grid, row) {
                        var record = grid.getSelectionModel().getSelected();  //单选模式（被选中的）
                        if (record) {
                            Ext.getCmp("subbut").hide();
                            update_win.show();
                            Ext.getCmp("saveorupdate").setValue("修改");
                            update_data_form.getForm().loadRecord(record);
                            position_combox.setValue(record.get('positionId'));
                            position_combox.setRawValue(record.get('position.posName'));
                        } else {
                            Ext.example.msg("提示", "请选择要查看的数据");
                        }
                    }

                },
                tbar: [
                    {
                        text: '查看',
                        id: 'tbar0',
                        hidden: true,
                        icon: 'img/show.png',
                        handler: function () {
                            update_win.setTitle("查看用户");
                            var record = grid.getSelectionModel().getSelection();
                            if (record.length) {
                                Ext.getCmp("subbut").hide();
                                update_win.show();
                                update_data_form.getForm().loadRecord(record[0]);
                                position_combox.setValue(record[0].get('positionId'));
                                position_combox.setRawValue(record[0].get('position.posName'));
                                Ext.getCmp(record[0].data.sex).setValue(true);
                                if (record[0].data.loginStatus == 0) {
                                    Ext.getCmp("loginStatuss").items.get(1).setValue(true);
                                } else {
                                    Ext.getCmp("loginStatuss").items.get(0).setValue(true);
                                }
                            } else {
                                Ext.example.msg("提示", "请选择要查看的数据");
                            }
                        }
                    }, {
                        text: '添加',
                        id: 'tbar1',
                        hidden: true,
                        icon: 'img/add.png',
                        handler: function () {
                            update_win.setTitle("添加用户");
                            update_data_form.getForm().reset();

                            Ext.getCmp("subbut").show();
                            Ext.getCmp('saveorupdate').setValue("添加");
                            Ext.getCmp("username").setReadOnly(false);
                            Ext.getCmp('remark').setValue('');
                            Ext.getCmp("jobType").setValue('全职');
                            Ext.getCmp("nation").setValue('汉');
                            Ext.getCmp("politicsStatus").setValue('团员');
                            Ext.getCmp("isSignedLc").setValue('是');
                            Ext.getCmp("isSignedIcca").setValue('是');
                            Ext.getCmp("isPaidInsur").setValue('是');
                            Ext.getCmp("jobLocation").setValue('北京');
                            Ext.getCmp('id').setValue();
                            Ext.getCmp('男').setValue(true);
                            Ext.getCmp('loginStatuss').items.get(0).setValue(true)
                            update_win.show();
                        }
                    },
                    {
                        text: '修改',
                        hidden: true,
                        id: 'tbar2',
                        icon: 'img/update.png',
                        handler: function () {
                            update_win.setTitle("修改用户");
                            var record = grid.getSelectionModel().getSelection();  //单选模式（被选中的）
                            if (record.length) {

                                Ext.getCmp("subbut").show();
                                Ext.getCmp('saveorupdate').setValue("修改");
                                Ext.getCmp("username").setReadOnly(true);
                                update_win.show();
                                var v = record[0].get("roles");
                                var vv = "";
                                for (var i = 0; i < v.length; i++) {
                                    vv += v[i].id + ",";
                                }
                                Ext.getCmp("uproleIds").setValue(vv);
                                update_data_form.getForm().loadRecord(record[0]);

                                position_combox.setValue(record[0].get('positionId'));
                                position_combox.setRawValue(record[0].get('position.posName'));
                                Ext.getCmp(record[0].data.sex).setValue(true);
                                if (record[0].data.loginStatus == 0) {
                                    Ext.getCmp("loginStatuss").items.get(1).setValue(true);
                                } else {
                                    Ext.getCmp("loginStatuss").items.get(0).setValue(true);
                                }
                            } else {
                                Ext.example.msg("提示", "请选择要修改的数据");
                            }
                        }
                    }, {
                        text: '删除',
                        hidden: true,
                        id: 'tbar3',
                        icon: 'img/delete2.gif',
                        handler: function () {
                            var record = grid.getSelectionModel().getSelection();  //单选模式（被选中的）
                            if (record.length) {
                                Ext.Ajax.request({
                                    url: 'user!delete.action',
                                    params: {id: record[0].get("id")},
                                    success: function (response) {
                                        Ext.Msg.alert('提示', "已删除");
                                        main_store.loadPage(1);

                                    },
                                    failure: function () {
                                        Ext.example.msg("提示", "很遗憾，发生了一点异常，请稍候再试！");
                                    }
                                });
                            } else {
                                Ext.example.msg("提示", "请选择一条数据");
                            }
                        }
                    }, {
                        text: '检索',
                        id: 'tbar4',
                        hidden: true,
                        icon: 'img/search.png',
                        handler: function () {
                            search_win.show();
                        }
                    }, {
                        id: 'searchField',
                        xtype: 'textfield',
                        width: 200,
                        emptyText: '请输入要查找用户名称或姓名',
                        enableKeyEvents: true,
                        listeners: {
                            keyup: function (tf, e) {
                                var key = e.getKey();
                                if (key == e.ENTER) {
                                    main_store.loadPage(1);
                                }
                            }
                        }
                    }, "-", {
                        text: '搜索',
                        icon: 'img/simplesearch.png',
                        handler: function () {
                            main_store.loadPage(1);
                        }
                    }],
                bbar: new Ext.PagingToolbar({
                    store: main_store,
                    displayInfo: true,
                    pageSize: pageSize
                })
            });

            /**---------------------------结束显示数据的Grid------------------------------------*/

//用户页面权限
            if (Ext.getCmp("show_data_grid")) {
                roleHaveRight();
            }

            /**
             department(部门) start
             */
            var update_department_combox = new Ext.form.ComboBox({
                typeAhead: false,
                lazyRender: true,
                name: 'departmentId',
                // multiSelect:true,  //多选
                fieldLabel: '<font color="red">*</font>所属部门',
                queryMode: 'local',  //这个需要加上。。
                store: new Ext.data.Store({
                    singleton: true,
                    proxy: {
                        type: 'ajax',
                        url: 'department.action',
                        actionMethods: 'post',
                        extraParams:{
			                limit:'100'
			            },
                        reader: {
                            type: 'json',
                            totalProperty: 'totalCount',
                            root: 'result'
                        }
                    },
                    fields: ['id', 'deptName'],
                    autoLoad: true
                }),
                valueField: 'id',
                displayField: 'deptName',
                editable: false,
                allowBlank: false,
                listeners: {
                    select: function (combo, record, index) {
                        position_combox.clearValue();
                        /*position_store.setBaseParam("start",0);
                        position_store.setBaseParam("limit",100);
                        position_store.setBaseParam("filter_EQI_deptId",this.value);*/
                        dtval = this.value
                        position_combox.store.load();
                    }
                }
            });


            /**
             department(部门) end
             */
            /**
             职位 start
             */
            var position_combox = new Ext.form.ComboBox({
                typeAhead: false,
                lazyRender: true,
                name: 'positionId',
                // multiSelect:true,  //多选
                fieldLabel: '<font color="red">*</font>职位',
                queryMode: 'local',  //这个需要加上。。
                store: new Ext.data.Store({
                    singleton: true,
                    proxy: {
                        type: 'ajax',
                        url: 'position.action',
                        actionMethods: 'post',
                        reader: {
                            type: 'json',
                            totalProperty: 'totalCount',
                            root: 'result'
                        }
                    },
                    fields: ['id', 'posName'],
                    autoLoad: true
                }),
                valueField: 'id',
                displayField: 'posName',
                editable: false,
                allowBlank: false,
                listeners: {
                    expand: function (combo, record, index) {
                        position_combox.clearValue();
                        position_combox.store.load();
                    }
                }
            });
            position_combox.store.on("beforeload", function (store, options) {

                var new_params = {filter_EQI_deptId: update_department_combox.getValue()};
                Ext.apply(store.proxy.extraParams, new_params);

            });

            /**
             职位  end
             */
            /*	var search_department_combox = new Ext.form.ComboBox({
                    typeAhead: true,
                       lazyRender:true,
                       hiddenName:'departmentId',
                       mode: 'local',
                       fieldLabel:'所属部门',
                    triggerAction: 'all',
                    store: department_store,
                    valueField: 'id',
                    displayField: 'deptName',
                     id:'departmentId',
                    editable:false,
                    allowBlank:false

                });

        /**---------------学历--------------*/

            var update_comboedu = Ext.create('Ext.form.ComboBox', {

                name: 'education',
                fieldLabel: '<font color="red">*</font>学历',
                queryMode: 'local',  //这个需要加上。。
                store: Ext.create('Ext.data.Store', {
                    fields: ['edu', 'education'],
                    data: [
                        {"edu": "初中及以下", "education": "初中及以下"},
                        {"edu": "高中", "education": "高中"},
                        {"edu": "中专", "education": "中专"},
                        {"edu": "专科", "education": "专科"},
                        {"edu": "本科", "education": "本科"},
                        {"edu": "硕士", "education": "硕士"},
                        {"edu": "博士", "education": "博士"}

                    ]
                }),
                valueField: 'edu',
                displayField: 'education',
                editable: false,
                allowBlank: false
            });


            /**---------------------------------CRUD  form--------------------------------------------------**/
            var update_data_form = Ext.create('Ext.form.Panel', {
                url: 'user!save.action',
                border: false,
                fieldDefaults: {
                    labelAlign: 'right',
                    labelWidth: 110
                },
                buttonAlign: 'center',
                //frame : true,
                readOnly: true,
                autoHeight: true,
                width: 800,
                items: [{
                    layout: 'column',
                    border: false,
                    items: [{
                        columnWidth: .3,
                        border: false,
                        layout: 'form',
                        defaults: {
                            xtype: 'textfield',
                            anchor: '95%'
                        },
                        items: [{
                            xtype: 'hidden',
                            name: 'id',
                            id: 'id'
                        },
                            {
                                xtype: 'hidden',
                                name: 'uproleIds',
                                id: 'uproleIds'
                            },
                            {
                                fieldLabel: '账号',
                                id: 'username',
                                allowBlank: false,
                                name: 'username'
                            }, {
                                fieldLabel: '工号',
                                name: 'jobNumber'
                            }, {
                                fieldLabel: 'QQ',
                                name: 'qq'
                            }, {
                                fieldLabel: '工作性质',
                                name: 'jobType',
                                id: 'jobType',
                                value: '全职'
                            }, {
                                fieldLabel: '民族',
                                name: 'nation',
                                id: 'nation',
                                value: '汉'
                            }, {
                                fieldLabel: '政治面貌',
                                name: 'politicsStatus',
                                id: 'politicsStatus',
                                value: '团员'
                            }, {
                                fieldLabel: '<font color="red">*</font>真实姓名',
                                allowBlank: false,
                                name: 'name',
                                id: 'realname',
                                listeners: {
                                    blur: function () {
                                        if (Ext.getCmp('realname').getValue().trim().length != 0) {
                                            Ext.Ajax.request({
                                                url: 'user!checkName.action',
                                                params: {
                                                    name: Ext.getCmp('realname').getValue().trim(),
                                                    saveorupdate: Ext.getCmp("saveorupdate").getValue(),
                                                    id: Ext.getCmp("id").getValue()
                                                },
                                                success: function (response) {
                                                    if (response.responseText == 0) {
                                                        Ext.getCmp('realname').setValue('');
                                                        Ext.example.msg('', "用户姓名已存在");
                                                    }
                                                },
                                                failure: handFailure
                                            });
                                        }
                                    }
                                }

                            }, {
                                fieldLabel: '手机号码',
                                name: 'mobile',
                                id: 'usermobile',
                                //allowBlank:false,
                                //minLength:11,
                                //maxLength:11,
                                listeners: {
                                    blur: function () {
                                        if (Ext.getCmp('usermobile').getValue().trim().length != 0) {
                                            Ext.Ajax.request({
                                                url: 'user!checkMobile.action',
                                                params: {
                                                    mobile: Ext.getCmp('usermobile').getValue().trim(),
                                                    saveorupdate: Ext.getCmp("saveorupdate").getValue(),
                                                    id: Ext.getCmp("id").getValue()
                                                },
                                                success: function (response) {
                                                    if (response.responseText == 0) {
                                                        Ext.getCmp('usermobile').setValue('');
                                                        Ext.example.msg('', "手机号已存在");
                                                    }
                                                },
                                                failure: handFailure
                                            });
                                        }
                                    }
                                }
                            }, {
                                fieldLabel: '紧急联系人',
                                name: 'emePerson'
                            }, {
                                fieldLabel: '紧急联系人方式',
                                name: 'emePhone'
                            }, {
                                xtype: 'datefield',
                                name: 'entryDate',
                                format: 'Y-m-d',
                                editable: false,
                                allowBlank: false,
                                fieldLabel: '<font color="red">*</font>入职日期'
                            }, {
                                xtype: 'datefield',
                                name: 'leaveDate',
                                format: 'Y-m-d',
                                editable: false,
                                fieldLabel: '离职日期'
                            },
                            {
                                xtype: 'datefield',
                                name: 'contractDate',
                                format: 'Y-m-d',
                                editable: false,
                                fieldLabel: '合同到期时间'
                            }]
                    }, {
                        columnWidth: .3,
                        border: false,
                        layout: 'form',
                        defaults: {
                            xtype: 'textfield',
                            anchor: '95%'
                        },
                        items: [{
                            fieldLabel: '密码',
                            name: 'password',
                            id: 'upass',
                            inputType: 'password',
                            minLength: 4
                        }, {
                            fieldLabel: '确认密码',
                            inputType: 'password',
                            vtype: 'password',
                            id: 'surepwd',
                            name: 'surepwd',
                            initialPassField: 'upass'
                        }, {
                            fieldLabel: '办公电话',
                            name: 'phone',
                            minLength: 4,
                            maxLength: 20
                        }, update_comboedu
                            , update_department_combox
                            , position_combox
                            , {
                                fieldLabel: '婚姻状况',
                                name: 'isMarried',
                                value: '未婚'
                            }, {
                                fieldLabel: '户口所在地',
                                name: 'residence',
                                value: ''
                            }, {
                                fieldLabel: '籍贯',
                                name: 'nativePlace',
                                value: ''
                            }, {
                                fieldLabel: '是否签订劳动合同',
                                name: 'isSignedLc',
                                id: 'isSignedLc',
                                value: '是'
                            }, {
                                xtype: 'datefield',
                                name: 'firstEntryDate',
                                format: 'Y-m-d',
                                editable: false,
                                fieldLabel: '参加工作时间'
                            },
                            {
                                xtype: 'datefield',
                                name: 'regularDate',
                                format: 'Y-m-d',
                                editable: false,
                                fieldLabel: '转正时间'
                            }
                        ]
                    }, {
                        columnWidth: .35,
                        border: false,
                        layout: 'form',
                        defaults: {
                            xtype: 'textfield',
                            anchor: '95%'
                        },
                        items: [
                            {
                                fieldLabel: '<font color="red">*</font>身份证号',
                                name: 'idNumber',
                                id: 'blurIdNumber',
                                minLength: 15,
                                maxLength: 18,
                                allowBlank: false,
                                listeners: {
                                    blur: function () {
                                        if (Ext.getCmp('blurIdNumber').getValue().trim().length != 0) {
                                            Ext.Ajax.request({
                                                url: 'user!checkIDnumber.action',
                                                params: {
                                                    idNumber: Ext.getCmp('blurIdNumber').getValue().trim(),
                                                    saveorupdate: Ext.getCmp('saveorupdate').getValue(),
                                                    id: Ext.getCmp("id").getValue()
                                                },
                                                success: function (response) {
                                                    if (response.responseText == 0) {
                                                        Ext.getCmp('blurIdNumber').setValue('');
                                                        Ext.example.msg('', "身份证号已存在");
                                                    }
                                                },
                                                failure: handFailure
                                            });
                                        }
                                    }
                                }
                            }, {
                                fieldLabel: '<font color="red">*</font>电子邮件',
                                name: 'email',
                                allowBlank: false,
                                id: 'useremail',
                                vtype: 'email',
                                listeners: {
                                    blur: function () {
                                        if (Ext.getCmp('useremail').getValue().trim().length != 0) {
                                            Ext.Ajax.request({
                                                url: 'user!checkEmail.action',
                                                params: {
                                                    email: Ext.getCmp('useremail').getValue().trim(),
                                                    saveorupdate: Ext.getCmp("saveorupdate").getValue(),
                                                    id: Ext.getCmp("id").getValue()
                                                },
                                                success: function (response) {
                                                    if (response.responseText == 0) {
                                                        Ext.getCmp('useremail').setValue('');
                                                        Ext.example.msg('', "邮箱已存在");
                                                    }
                                                },
                                                failure: handFailure
                                            });
                                        }
                                    }
                                }
                            }, {
                                fieldLabel: '毕业院校',
                                name: 'school'

                            }, {
                                fieldLabel: '专业',
                                name: 'userProfession'

                            }, {
                                fieldLabel: '住址',
                                name: 'address'

                            }, {
                                fieldLabel: '是否签订知识产权及保密协议',
                                name: 'isSignedIcca',
                                id: 'isSignedIcca',
                                value: '是'
                            }, {
                                fieldLabel: '户口性质',
                                name: 'residenceType'
                            }, {
                                fieldLabel: '是否在本市缴纳过保险',
                                name: 'isPaidInsur',
                                id: 'isPaidInsur',
                                value: '是'
                            }, {
                                fieldLabel: '常驻工作地',
                                name: 'jobLocation',
                                id: 'jobLocation',
                                value: '北京'
                            }, {
                                xtype: 'radiogroup',
                                fieldLabel: '性别',
                                name: 'sex',
                                id: 'sexs',
                                allowBlank: false,
                                anchor: '80%',
                                items: [

                                    {boxLabel: '男', name: 'sex', inputValue: '男', id: '男'},
                                    {boxLabel: '女', name: 'sex', inputValue: '女', id: '女'}
                                ]
                            }, {
                                xtype: 'radiogroup',
                                fieldLabel: '状态',
                                id: 'loginStatuss',
                                name: 'loginStatus',
                                anchor: '80%',
                                items: [

                                    {boxLabel: '生效', name: 'loginStatus', inputValue: '1'},
                                    {boxLabel: '失效', name: 'loginStatus', inputValue: '0'}
                                ]
                            }
                        ]
                    }
                    ]
                }, {
                    xtype: 'textarea',
                    name: 'remark',
                    id: 'remark',
                    fieldLabel: '备注',
                    height: 100,
                    anchor: '95%'
                }, {
                    xtype: 'hidden',
                    id: 'saveorupdate',
                    name: 'saveorupdate',
                    value: '添加'
                }
                ],
                buttons: [{
                    text: '提交',
                    id: 'subbut',
                    handler: function () {
                        if (Ext.getCmp("upass").getValue() != "" && Ext.getCmp("surepwd").getValue() == "") {
                            Ext.example.msg('', '请再次输入密码');
                        } else {
                            if (Ext.getCmp("id").getValue() != "" && Ext.getCmp("id").getValue() != null) {  //修改
                                var zt=Ext.getCmp("loginStatuss").getChecked()[0].boxLabel;
                                if(zt == "失效"){
                                    Ext.MessageBox.confirm("请确认","是否注销用户,注销后权限清空!!",function(button,text){
                                        if(button=="yes"){
                                            update_data_form.getForm().submit({
                                                success: function (f, action) {
                                                    update_win.hide();
                                                    main_store.loadPage(1);
                                                    Ext.example.msg('', action.result.msg);
                                                },
                                                failure: handFailure
                                            });
                                        }else{
                                            Ext.getCmp('loginStatuss').items.get(0).setValue(true);
                                        }
                                    });
                                }else{
                                    update_data_form.getForm().submit({
                                        success: function (f, action) {
                                            update_win.hide();
                                            main_store.loadPage(1);
                                            Ext.example.msg('', action.result.msg);
                                        },
                                        failure: handFailure
                                    });
                                }
                            } else { //添加
                                update_data_form.getForm().submit({
                                    success: function (f, action) {
                                        update_win.hide();
                                        main_store.loadPage(1);
                                        Ext.example.msg('', action.result.msg);
                                    },
                                    failure: handFailure
                                });
                            }
                        }
                    }
                }, {
                    text: '取消',
                    handler: function () {
                        update_win.hide();
                    }
                }]
            });

            var update_win = new Ext.Window({
                title: '用户修改',
                closeAction: 'hide',
                animateTarget: '',
                //width : 830,
                autoScroll: true,
                modal: true,
                items: update_data_form,
                listeners: {
                    hide: function (win, eOpts) {
                        update_data_form.getForm().reset();
                    }
                }
            });
            /**---------------------------------修改form--------------------------------------------------**/

            /*---------------------------------检索form-------------------------------------------*/
            /*	var search_data_form = new Ext.form.FormPanel( {
                    id : 'search_data_form',
                    labelWidth : 55,
                    labelAlign:'right',
                    trackResetOnLoad:true,
                    buttonAlign : 'center',
                    frame : true,
                    autoHeight : true,
                    width : 600,
                    items: [{
                        layout:'column',
                        items:[{
                            columnWidth:.5,
                            layout: 'form',
                            defaults:{
                                xtype:'textfield',
                                anchor:'95%'
                            },
                            items: [{
                                fieldLabel: '账号',
                                id:'usernames'
                            }, {
                                fieldLabel: '真实姓名',
                                id:'name'
                            },search_department_combox,{
                                fieldLabel:'手机号码',
                                id:'mobile'
                            }]
                        },{
                            columnWidth:.5,
                            layout: 'form',
                             defaults:{
                                xtype:'textfield',
                                anchor:'95%'
                            },
                            items: [{
                                fieldLabel:'办公电话',
                                id:'phone'
                            },{
                                fieldLabel:'QQ',
                                id:'qq'
                            },{
                                xtype: 'radiogroup',
                                fieldLabel: '性别',
                                name: 'sex',
                                id:'sex',
                                   anchor:'50%',
                                items: [
                                        {boxLabel: '男', name: 'sex', inputValue: '男'},
                                        {boxLabel: '女', name: 'sex', inputValue: '女'}
                                 ]
                            },{
                                xtype: 'radiogroup',
                                fieldLabel: '状态',
                                id:'loginStatus',
                                   anchor:'50%',
                                items: [
                                        {boxLabel: '失效', name: 'loginStatus', inputValue: '0'},
                                        {boxLabel: '生效', name: 'loginStatus', inputValue: '1'}
                                 ]
                            }]
                        }
                    ]
                    }
                ],
                buttons : [ {
                    text : '查询',
                    handler : function() {
                                    main_store.setBaseParam("start",0);
                                    main_store.setBaseParam("limit",pageSize);
                                    main_store.load();
                                    search_win.hide();
                    }
                }, {
                    text : '重置',
                    handler:function(){
                        search_data_form.form.reset();
                    }
                },{
                    text : '取消',
                    handler:function(){
                        search_win.hide();
                    }
                } ]
            });
        /*---------------------------------检索form end-------------------------------------------*/

            /*---------------------------------检索window start-------------------------------------------*/
            /*var search_win = new Ext.Window( {
                    id : "search_win",
                    title : '检索用户',
                    closeAction : 'hide',
                    modal : true,
                    items : search_data_form
                });
            /*---------------------------------检索window end-------------------------------------------*/

            var view = Ext.create('Ext.Viewport', {
                layout: 'border',
                items: grid
            });
        });

    </script>
	</head>
	<body>

	</body>
</html>
