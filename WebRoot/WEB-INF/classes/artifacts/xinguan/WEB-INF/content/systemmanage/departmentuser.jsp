<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>组织部门</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<jsp:include page="/include/header4.jsp"></jsp:include>

	<script>
	
Ext.onReady(function() {
    Ext.QuickTips.init();
   
	var store = Ext.create('Ext.data.TreeStore', { 
proxy: { 
type: 'ajax', 
actionMethods: {
          read   : 'POST', // by default GET
            },
          
//the store will get the content from the .json file 
url: 'department!departmentShowJsons.action'
},
 fields : [{
		    name: 'id',
		    type: 'int'
	  	}, {
		    name: 'deptName',
		    type: 'string'
	 	 }, {
           
            name: 'posName'
        },{
            
            name: 'sex'
        },{
           
            name: 'idNumber'
        },{
            
            name: 'nativePlace'
        },{
           
            name: 'entryDate'
        },{
           
            name: 'regularDate'
        },{
            
            name: 'contractDate'
        },{
            
            name: 'school'
        },{
            
            name: 'userProfession'
        },{
           
            name: 'education'
        },{
            
            name: 'address'
        },{
            
            name: 'userMobile'
        },{
            
            name: 'userEmail'
        }]  
//folderSort: true 
}); 
//Ext.ux.tree.TreeGrid is no longer a Ux. You can simply use a tree.TreePanel 
var tree = Ext.create('Ext.tree.Panel', { 
title: '部门管理',  
height: 300, 
id:'show_data_grid',
region:'center',
//collapsible: true, 
useArrows: true, //在tree中使用Vista-style样式的箭头。 ...
rootVisible: false, //false，隐藏根节点。
store: store, 
multiSelect: false, //设置为true则启用'MULTI'多行选择模式
singleExpand: true,// true，如果每个分支只有1个节点可能展开。 ..
columns: [
{
            header: '部门/人员',
            xtype: 'treecolumn', //this is so we know which column will show the tree 
            width: 250,
            dataIndex: 'deptName'
            //flex: 3, //此配置项将被应用到布局管理的容器的子项中. 每个含有flex属性的子项将会被根据当前子项的flex值与所有其他含flex值子项的值的和 的相对比例进行伸缩('hbox'中横向, 'vbox'中纵向). 任何'flex=0'或'flex=undefined'的子项将不被伸缩(即组件原始尺寸不会被修改).
            //align: 'center'
        },{
            header: '职位',
            width: 120,
            dataIndex: 'posName'
        },{
            header: '性别',
            width: 50,
            dataIndex: 'sex'
        },{
            header: '身份证',
            width: 150,
            dataIndex: 'idNumber'
        },{
            header: '籍贯',
           // hidden:true,
            width: 50,
            dataIndex: 'nativePlace'
        },{
            header: '入职日期',
            width: 100,
            dataIndex: 'entryDate'
        },{
            header: '转正日期',
            width: 100,
            dataIndex: 'regularDate'
        },{
            header: '合同到期日期',
            width: 100,
            dataIndex: 'contractDate'
        },{
            header: '学院',
            width: 150,
            dataIndex: 'school'
        },{
            header: '专业',
            width: 150,
            dataIndex: 'userProfession'
        },{
            header: '学历',
            width: 80,
            dataIndex: 'education'
        },{
            header: '住址',
            width: 80,
            //hidden:true,
            dataIndex: 'address'
        },{
            header: '联系电话',
            width: 150,
            hidden:true,
            dataIndex: 'userMobile'
        },{
            header: '邮箱',
            width: 150,
            dataIndex: 'userEmail'
        }],
	listeners: {
		'itemclick':function(m,n) {
			deptid=n.data.id;
			dname=n.data.deptName;
			dec=n.data.description;
			parentID=n.data.pid;
        }
    }
});

  /*  var tree = new Ext.ux.tree.TreeGrid({
        region:'center',
        enableDD: true,
        columns:[{
            header: '部门/人员',
            width: 250,
            dataIndex: 'deptName',
            align: 'center'
        },{
            header: '职位',
            width: 120,
            dataIndex: 'posName'
        },{
            header: '性别',
            width: 50,
            dataIndex: 'sex'
        },{
            header: '身份证',
            width: 150,
            dataIndex: 'idNumber'
        },/*{
            header: '籍贯',
           // hidden:true,
            width: 50,
            dataIndex: 'nativePlace'
        },{
            header: '入职日期',
            width: 100,
            dataIndex: 'entryDate'
        },{
            header: '转正日期',
            width: 100,
            dataIndex: 'regularDate'
        },{
            header: '合同到期日期',
            width: 100,
            dataIndex: 'contractDate'
        },{
            header: '学院',
            width: 150,
            dataIndex: 'school'
        },{
            header: '专业',
            width: 150,
            dataIndex: 'userProfession'
        },{
            header: '学历',
            width: 80,
            dataIndex: 'education'
        },/*{
            header: '住址',
            width: 80,
            //hidden:true,
            dataIndex: 'address'
        },{
            header: '联系电话',
            width: 150,
            hidden:true,
            dataIndex: 'userMobile'
        },{
            header: '邮箱',
            width: 150,
            dataIndex: 'userEmail'
        }],
        dataUrl: 'department!departmentShowJsons.action',
        listeners: {
             dblclick: function(node,en){
	             if(node.id>100){
	             	 Ext.Ajax.request({
							url:'user!findUserById.action',
							params:{id:node.id-100},
							success:function(response){
							var userup = Ext.decode(response.responseText);
								 Ext.getCmp("qq").setValue(userup.qq);
						 		 Ext.getCmp("username").setValue(userup.username);
						 		 Ext.getCmp("jobNumber").setValue(userup.jobNumber);
						 		 Ext.getCmp("jobType").setValue(userup.jobType);
						 		 Ext.getCmp("nation").setValue(userup.nation);
						 		 Ext.getCmp("politicsStatus").setValue(userup.politicsStatus);
						 		 Ext.getCmp("name").setValue(userup.name);
						 	     Ext.getCmp("mobile").setValue(userup.mobile);
						 	     Ext.getCmp("emePerson").setValue(userup.emePerson);
						 		 Ext.getCmp("emePhone").setValue(userup.emePhone);
						 		 Ext.getCmp("entryDate").setValue(userup.entryDate);
						 		 Ext.getCmp("leaveDate").setValue(userup.leaveDate);

						 		 Ext.getCmp("phone").setValue(userup.phone);
						 		 update_comboedu.setValue(userup.education);
						 		 update_department_combox.setValue(userup.departmentId);
						 		 position_combox.setValue(userup.positionId);
						 		 Ext.getCmp("isMarried").setValue(userup.isMarried);
						 		 Ext.getCmp("residence").setValue(userup.residence);
						 		 Ext.getCmp("nativePlace").setValue(userup.nativePlace);
						 		 Ext.getCmp("firstEntryDate").setValue(userup.firstEntryDate);
						 		 Ext.getCmp("isSignedLc").setValue(userup.isSignedLc);

						 	     Ext.getCmp("idNumber").setValue(userup.idNumber);
						 	     Ext.getCmp("email").setValue(userup.email);
						 	     Ext.getCmp("school").setValue(userup.school);
						 	     Ext.getCmp("address").setValue(userup.address);
						 	     Ext.getCmp("isSignedIcca").setValue(userup.isSignedIcca);
						 	     Ext.getCmp("residenceType").setValue(userup.residenceType);
						 	     Ext.getCmp("isPaidInsur").setValue(userup.isPaidInsur);
						 	     Ext.getCmp("jobLocation").setValue(userup.jobLocation);
						 		 Ext.getCmp("sex").setValue(userup.sex);
						 		 Ext.getCmp("loginStatus").setValue(userup.loginStatus);
						 		 Ext.getCmp("remark").setValue(userup.remark);
								user_win.show();
							},
							failure: handFailure
						});
	             }
               }
          }
    });
    
     /**
	 department(部门) start 
	 */
	 
	/* var department_store = new Ext.data.JsonStore( {
		url : 'department.action',
		totalProperty: 'totalCount',  //设置数据总条数：值来源于响应的json对象的属性
		root:'result', //设置当前页要显示的数据
		fields : [{
			name:'id'
		},{
			name:'deptName'
		}]
	});

	//加载数据
		department_store.load({params: {
	        // specify params for the first page load if using paging
	        start: 0,          
	        limit: 300
	    	}}
		);

		var update_department_combox = new Ext.form.ComboBox({
		    typeAhead: true,
		   	lazyRender:true,
		   	hiddenName:'departmentId',
		   	mode: 'local',
		   	fieldLabel:'所属部门',
		    triggerAction: 'all',
		    store: department_store,
		    valueField: 'id',
		    displayField: 'deptName',
		    editable:false
		}); 
		 /**
		 department(部门) end 
		 */
		/**
		职位 start
		*/			
	/*	 var position_store = new Ext.data.JsonStore( {
				url : 'position.action',
				totalProperty: 'totalCount',  //设置数据总条数：值来源于响应的json对象的属性
				root:'result', //设置当前页要显示的数据
				fields : [{
					name:'id'
				},{
					name:'posName'
				}]
			});
		//加载数据
			position_store.load({params: {
		        // specify params for the first page load if using paging
		        start: 0,          
		        limit: 30
		    	}}
			);
		
			var position_combox = new Ext.form.ComboBox({
			    typeAhead: true,
			   	lazyRender:true,
			   	hiddenName:'positionId',
			   	mode: 'local',
			   	fieldLabel:'职位',
			    triggerAction: 'all',
			    store: position_store,
			    valueField: 'id',
			    displayField: 'posName'
			   
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
	/*var update_comboedu = new Ext.form.ComboBox({
	    typeAhead: true,
	    fieldLabel: '学历',  
	    triggerAction: 'all',
	    allowBlank:false,
	   editable:false,
	    lazyRender:true,
	    name:'education',
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'edu',
	            'education'
	        ],
	        data: [['初中及以下', '初中及以下'], ['高中', '高中'],['中专','中专'],['专科', '专科'],['本科', '本科'],['硕士','硕士'],['博士','博士']]
	    }),
	    valueField: 'edu',
	    displayField: 'education'
	});
    
    
    
    /**---------------------------------CRUD  form--------------------------------------------------**/
	/*var user_data_form = new Ext.form.FormPanel({
		labelWidth : 100,
		labelAlign:'right',
		trackResetOnLoad:true,
		buttonAlign : 'center',
		frame : true,
		 readOnly:true,
		autoHeight : true,
		width : 800,
		items: [{
            layout:'column',
            items:[{
                columnWidth:.3,
                layout: 'form',
                defaults:{
            		xtype:'textfield',
            		anchor:'95%'
                },
                items: [
                {
                    fieldLabel: '账号',
                    id:'username'
                },{
					fieldLabel:'工号',
					id:'jobNumber'
                },{
					fieldLabel:'工作性质',
					id:'jobType',
					value:'全职'
                }, {
					fieldLabel:'民族',
					id:'nation',
					value:'汉'
                }, {
					fieldLabel:'政治面貌',
					id:'politicsStatus',
					value:'团员'
                },{
                    fieldLabel: '真实姓名',
                    allowBlank:false,
                    id: 'name',
                },{
                	fieldLabel:'手机号码',
                	id:'mobile'
                	
                },{
                	fieldLabel:'紧急联系人',
                	id:'emePerson'
                },{
                	fieldLabel:'紧急联系人方式',
                	id:'emePhone'
                }, {
                    xtype     : 'datefield',
                    id      : 'entryDate',
                    format :'Y-m-d', 
                    editable:false,
            	    allowBlank:false,
                    fieldLabel: '入职日期'
                }, {
                    xtype     : 'datefield',
                    id      : 'leaveDate',
                    format :'Y-m-d', 
                    editable:false,
            	    
                    fieldLabel: '离职日期' 
                },
                {
                    xtype     : 'datefield',
                    id      : 'contractDate',
                    format :'Y-m-d', 
                    editable:false,
                    fieldLabel: '合同到期时间' 
                }]
            },{
                columnWidth:.3,
                layout: 'form',
                 defaults:{
            		xtype:'textfield',
            		anchor:'95%'
                },
                items: [{
                	fieldLabel:'办公电话',
                	id:'phone'
                },{
                    fieldLabel: 'QQ',
                    id:'qq'
                },update_comboedu
                ,update_department_combox
                ,position_combox
                , {
					fieldLabel:'婚姻状况',
					id:'isMarried',
					value:'未婚'
                }, {
					fieldLabel:'户口所在地',
					id:'residence',
					value:''
                }, {
					fieldLabel:'籍贯',
					id:'nativePlace',
					value:''
                }, {
					fieldLabel:'是否签订劳动合同',
					id:'isSignedLc',
					value:''
                }, {
                    xtype     : 'datefield',
                    id      : 'firstEntryDate',
                    format :'Y-m-d', 
                    editable:false,
                    fieldLabel: '参加工作时间' 
                },
                {
                    xtype     : 'datefield',
                    id      : 'regularDate',
                    format :'Y-m-d', 
                    editable:false,
                    fieldLabel: '转正时间' 
                }
			]
            },{
                columnWidth:.4,
                layout: 'form',
                 defaults:{
            		xtype:'textfield',
            		anchor:'95%'
                },
                items: [
							{
								fieldLabel:'身份证号',
								id:'idNumber',
								minLength:15,
								maxLength:18,
								allowBlank:false
							},{
			                    fieldLabel: '电子邮件',
			                    id: 'email'
			                   
			                },{
			                	fieldLabel:'毕业院校',
			                	id:'school'
			                	
			                },
			                {
			                	fieldLabel:'专业',
			                	id:'userProfession'
			                	
			                },{
			                	fieldLabel:'住址',
			                	id:'address'
			                	
			                },{
			                	fieldLabel:'是否签订知识产权及保密协议',
			                	id:'isSignedIcca',
			                	value:'是'				                	
			                },{
			                	fieldLabel:'户口性质',
			                	id:'residenceType'
			                },{
			                	fieldLabel:'是否在本市缴纳过保险',
			                	id:'isPaidInsur',
			                	value:'是'
			                },{
			                	fieldLabel:'常驻工作地',
			                	id:'jobLocation'
			                },{
				        fieldLabel: '性别', 	            
	                    id:'sex',
	                   	anchor:'80%'
				        
	                },{
				        xtype: 'radiogroup',
				        fieldLabel: '账号状态', 	 
	                    id:'loginStatus',
	                   	anchor:'80%',
				        items: [
				               
				                {boxLabel: '生效', name: 'loginStatus', inputValue: '1'},
				                 {boxLabel: '失效', name: 'loginStatus', inputValue: '0'}
				         ]
	                }
                 ]
            }
    	]
        },{
            xtype:'htmleditor',
            id:'remark',
            fieldLabel:'备注',
            height:100,
            anchor:'98%'
      }
	],
		buttons:[{
			text:'取消',
			handler:function(){
			user_win.hide();
		}
		}]
	});
	
		var user_win = new Ext.Window({
		title:'用户详情',
		closeAction: 'hide',
		animateTarget:'',
		autoScroll:true,
		modal:true,
		items:user_data_form,
		listeners: {
            hide: function (win, eOpts) {
               //update_data_form.getForm().getEl().dom.reset();
            }
        }
	});
/**---------------------------------修改form--------------------------------------------------**/
	

     var view = new Ext.Viewport({
		layout:'border',
		items:tree
	});
});

</script>
  </head>
  <body>
   
  </body>
</html>
