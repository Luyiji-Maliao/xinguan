<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>部门管理</title>
    
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
   
	var deptid="";
    var parentID="";
    var dname="";
    var dec="";
   
var store = Ext.create('Ext.data.TreeStore', { 
proxy: { 
type: 'ajax', 
actionMethods: {
          read   : 'POST', // by default GET
            },
          
//the store will get the content from the .json file 
url: 'department!departmentShowJson.action'
},
 fields : [{
    name: 'id',
    type: 'int'
  }, {
    name: 'deptName',
    type: 'string'
  }, {
    name: 'description',
    type: 'string'
  }, {
    name: 'pid',
    type: 'string'
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
//the 'columns' property is now 'headers' 
dockedItems: [{
        xtype: 'toolbar',
        dock: 'top',
        items: [{
	    		text:'添加', 
	    		hidden:true,
	    		id:'tbar0',
	    		icon:'img/add.png',
				handler:function(){
					  add_data_form.getForm().reset();
	    	 		  Ext.getCmp("addparentid").setValue(deptid);
	    	 		  add_data_window.show();
	    	 		  deptid="";
	  			}	
	           },{
				text : '修改', 
				id:'tbar1',
				hidden:true,
				icon : 'img/update.png',
				handler : function() {
				
					if(deptid!=""){
						Ext.getCmp("deptid").setValue(deptid);
						Ext.getCmp("updateparentid").setValue(parentID);
						Ext.getCmp("dname").setValue(dname);
						Ext.getCmp("remark").setValue(dec);
						update_data_window.show();
						
						deptid="";
						parentID="";
						dname="";
						dec="";
					}else{
						Ext.example.msg("提示","请选择数据");
					}
				}
			}]
       }],
columns: [
{ 
xtype: 'treecolumn', //this is so we know which column will show the tree 
text: 'ID', 
flex: 2, //此配置项将被应用到布局管理的容器的子项中. 每个含有flex属性的子项将会被根据当前子项的flex值与所有其他含flex值子项的值的和 的相对比例进行伸缩('hbox'中横向, 'vbox'中纵向). 任何'flex=0'或'flex=undefined'的子项将不被伸缩(即组件原始尺寸不会被修改).
sortable: true, 
dataIndex: 'id' 
},
{ 
text: '部门', 
flex: 3, 
//xtype: 'treecolumn',
dataIndex: 'deptName', 
sortable: true 
},{ 
	text: '描述', 
	flex: 3, 
	dataIndex: 'description', 
	sortable: true 
	},
{ 
	text: 'PID', 
	flex: 3, 
	dataIndex: 'pid', 
	sortable: true 
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

  
    //用户页面权限
if(Ext.getCmp("show_data_grid")){
	roleHaveRight();
}
    /**--------------------------------添加数据Form-------------------------------------*/
	
	var add_data_form = Ext.create('Ext.form.Panel', {
			id : 'add_data_form',
			fieldDefaults: {
		        labelAlign: 'right',
		        anchor:'95%',
		        labelWidth:75
		    },
			buttonAlign : 'center',
			//frame : true,
			url : 'department!save.action',
			autoHeight : true,
			defaults : {
				xtype : 'textfield'
			},
			width : 285,
			items : [{
				fieldLabel : '上级部门ID',
				id:'addparentid',
				name:'parentid',
				value:''
			},{
				fieldLabel : '<font color="red">*</font>部门名称',
				name : 'deptName',
				allowBlank:false
			}, {
				fieldLabel : '部门描述',
				xtype : 'textarea',
				name : 'description'
			}],
			buttons : [ {
				text : '提交',
				handler : function() {
				
					add_data_form.getForm().submit({
		                    success: function(f, action) {
		                     if (action.result.success) {
		                       window.location='department!modulepage.action';
		                      }
		                    },
		                     failure: handFailure
				});
			}} ]
		});
    /**--------------------------------添加数据Window---------------------------------------------*/
 
	var add_data_window =Ext.create('Ext.window.Window',{
		id : "add_data_window",
		title : '添加部门',
		closeAction :'hide',
		modal : true,
		items : add_data_form
	});
/**--------------------------------结束添加数据Window-------------------------------------------*/
 
    
    
  /**--------------------------------修改数据Form-------------------------------------*/
	
	var update_data_form = Ext.create('Ext.form.Panel', {
			id : 'update_data_form',
			buttonAlign : 'center',
			border:false,
			fieldDefaults: {
		        labelAlign: 'right',
		        anchor:'95%',
		        labelWidth:75
		    },
			//frame : true,
			url : 'department!update.action',
			autoHeight : true,
			autoWidth : true,
			defaults : {
				xtype : 'textfield'
			},
			width : 285,
			items : [{
				fieldLabel : '当前ID',
				id:'deptid',
				hidden:true,
				name:'deptid'
			},{
				fieldLabel : '上级部门ID',
				id:'updateparentid',
				xtype: 'numberfield',
				name:'parentid',
				
				value:''
			},{
				fieldLabel : '部门名称',
				id:'dname',
				name : 'deptName',
				allowBlank:false
			}, {
				fieldLabel : '部门描述',
				xtype : 'textarea',
				id:'remark',
				name : 'description'
			}],
			buttons : [ {
				text : '修改',
				handler : function() {
				if(Ext.getCmp("deptid").getValue().trim()!=Ext.getCmp("updateparentid").getValue()){
					update_data_form.getForm().submit({
		                    success: function(f, action) {
		                     if (action.result.success) {
		                       window.location='department!modulepage.action';
		                      }
		                    },
		                     failure: handFailure
				});
				}else{
					Ext.example.msg("提示","父节点ID不能与当前ID相同");
				}
			}} ]
		});
    /**--------------------------------修改数据Window---------------------------------------------*/
 
	var update_data_window = Ext.create('Ext.window.Window',{
		id : "update_data_window",
		title : '修改部门',
		closeAction :'hide',
		modal : true, //遮罩层 
		items : update_data_form
	});
/**--------------------------------结束修改数据Window-------------------------------------------*/
   
     var view =Ext.create('Ext.Viewport',{
		layout:'border',
		items:tree
	});
});

</script>
  </head>
  <body>
   
  </body>
</html>
