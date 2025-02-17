	/*Ext.Ajax.on('requestcomplete',checkUserSessionStatus, this); 
		function checkUserSessionStatus(conn,response,options){
			var str = response.responseText;
			if(str == '9999'){
				alert("3");
				//alert("未登录或登陆过期 ,请重新登录");
				window.parent.location = '/uscilims/';
				}}*/

var ajax = function(config) {
	Ext.Ajax.request({
		url: config.url,
		params: config.params,
		method: 'post',
		callback: function(options, success, response) {
			config.callback(Ext.JSON.decode(response.responseText));
		}
	});
	return false;
};

function closewest() {
	  var w = Ext.getCmp('navtree');
	  if(document.getElementById("wcollapseid").value == "true"){
		  w.collapse();
	  }
	  if(document.getElementById("wcollapseid").value == "false"){
		  w.expand();
	  }
}

function openwest() {
	 var w = Ext.getCmp('navtree');
	 document.getElementById("wcollapseid").value = "false";
	 w.expand();
}

function setclosewest(){
	var w = Ext.getCmp('navtree');
	document.getElementById("wcollapseid").value = "true";
}

function setalwaysopen(){
	document.getElementById("alwaysopen").value = "false";
}
var helpdocs="个人门户"; //默认个人门户
var jspPage="";
function personalportal(){
	helpdocs="个人门户";
	window.location.href='user!remain.action';
}
function helpdoc(){
	
	if(helpdocs=="个人门户"){
		//window.location.href='schedulesort!moredownload.action';
	}else{
		
		//window.location.href='schedulesort!downloadone.action?helpdocs='+helpdocs;
	}
	
	
}
Ext.override(Ext.grid.GridPanel, {
    afterRender : Ext.Function.createSequence(Ext.grid.GridPanel.prototype.afterRender,
        function() {
            // 默认显示提示
            if (!this.cellTip) {
                return;
            }

            var view = this.getView();

            this.tip = new Ext.ToolTip({
                target: view.el,
                delegate : '.x-grid-cell-inner',
                trackMouse: true, 
                renderTo: document.body, 
                ancor : 'top',
                style : 'background-color: #FFFFCC;',
                listeners: {  
                    beforeshow: function updateTipBody(tip) {
                        //取cell的值
                        //fireFox  tip.triggerElement.textContent
                        //IE  tip.triggerElement.innerText 
                        var tipText = (tip.triggerElement.innerText || tip.triggerElement.textContent);
                        if (Ext.isEmpty(tipText) || Ext.isEmpty(tipText.trim()) ) {
                            return false;
                        }

                        tip.update(tipText);
                    }
                }
            });
        })
});
Ext.onReady(function() {
	Ext.QuickTips.init();
	
	var north = Ext.create("Ext.panel.Panel", {
		region: 'north',
		height: 40,
		contentEl: 'north',
		bodyStyle: 'line-height : 40px;font-family:黑体;background-color: #2A3F5D;color:white;',
		//html: '<div style="color:white; padding-left:20px;font-size:18px;font-weight:bolder">优迅实验室信息管理系统</div>'
	});
	//初始化、或刷新时显示第一个叶子节点的参数
	var treenodecount=0;
	var afterlayoutcount=0;
	var fstore;
	var flag=0;//解决火狐兼容
	function findchildnode(node){
        var childnodes = node.childNodes;
        for(var i=0;i<childnodes.length;i++){  //从节点中取出子节点依次遍历
            var rootnode = childnodes[0];
         //  alert(rootnode.data.text);
            //alert(rootnode.data.leaf);
            if(!rootnode.data.leaf){  //判断子节点下是否存在子节点
           	 rootnode.expand();
           	setTimeout(function(){findchildnode(rootnode);},100) 
           	// findchildnode(rootnode);    //如果存在子节点  递归
           	break;
            }else{
           	// alert("alert(rootnode.data.text);:"+rootnode.data.text);
           	 jspPage=rootnode.data.jspPage;
           	fstore.getSelectionModel().select(rootnode);
           	window.frames["content"].location.href = rootnode.data.url + '.action';
            }   
        }
    }
	var west = Ext.create("Ext.panel.Panel", {
		id: 'navtree',
		region: 'west',
		title: '菜单',
		layout: 'accordion',
		collapsible: true,
		split: true,
		width: 220,
		listeners:{
			afterlayout:function(){
				if(treenodecount!=0&&afterlayoutcount==(treenodecount*2)&&flag==0){
					//alert("tn");
					var roonodes =this.items.get(0).getRootNode();
					// alert("000");
					setTimeout(function(){findchildnode(roonodes);},100) 
					flag=1;// 解决在火狐下 的兼容问题
					afterlayoutcount=0;
				}else{
					afterlayoutcount++;
				}
				
				
			}
		}
	});
	
	var center = Ext.create("Ext.panel.Panel", {
		region: 'center',							//user!dashthings.action
		html: '<iframe scrolling="yes" name="content" src="" frameborder="0" width="100%" height="100%" ></iframe>',
		bodyStyle: "backgroundImage: url('img/square.gif');"
	});
	
	
	var model = Ext.define("TreeModel", { 
		extend: "Ext.data.Model",
		fields: [
		    {name: "id", type: "string"},
		    {name: "text", type: "string"},
		    {name: "url", type: "string"},
			{name: "iconCls", type: "string"},
			{name: "leaf", type: "boolean"},
			{name: "jspPage", type: "string"},
			{name: 'type'},
			{name: 'component'}
		]
	});
	
	var createStore = function(id) { 
		var me = this;
		return Ext.create("Ext.data.TreeStore", {
					defaultRootId : id, // 默认的根节点id
					model : model,
					proxy : {
						type : "ajax", // 获取方式
						url : "user!initMenu.action" // 获取树节点的地址
					},
					clearOnLoad : true,
					nodeParam : "id"// 设置传递给后台的参数名,值是树节点的id属性
				});
	};
	
	function addTree(data) {
		Ext.getBody().unmask();
		treenodecount=data.length;
		for (var i = 0; i < data.length; i++) {
			west.add(Ext.create("Ext.tree.Panel", {
						title : data[i].text,
						iconCls : data[i].iconCls,
						xtype: 'basic-trees',
						collapsible: true,
						autoScroll: false,
						split: true,
						useArrows: true,
						autoScroll : true,
						rootVisible : false,
						viewConfig : {
							loadingText : "正在加载..."
						},
						store : createStore(data[i].id),
						listeners : {
							afterlayout : function() {
							//alert("this.getStore()"+this.getStore());
							//alert(this.title);
							fstore=this;
								if (this.getView().el) {
									var el = this.getView().el;
									var table = el
											.down("table.x-grid-table");
									if (table) {
										table.setWidth(el.getWidth());
									}
								}
							},
							itemclick : function(view, node){
                                Ext.Ajax.request({
                                    url:'buildlibraries!getUserId.action',
                                    method:'post',
                                    success:function(data2){
                                    	var jie=data2.responseText.toString().split(",");
                                    	var id1=jie[0];
                                    	var id2=jie[1];
                                        var id3=jie[2];
                                    	if(id1==id2){
                                                Ext.Ajax.request({
                                                    url:'buildlibraries!updatexiadanstate.action',
                                                    method:'post',
                                                    success:function(data){}
                                                })
										}
										if(id1==id3){
                                            Ext.Ajax.request({
                                                url:'nucleicacids!updatexiadanstate.action',
                                                method:'post',
                                                success:function(data){}
                                            })
										}
									}
                                })
								if (node.data.leaf) { //判断是否是根节点
									helpdocs=node.data.text;//帮助文档
									//alert(helpdocs);
									 jspPage=node.data.jspPage;
									if (node.data.url != '#') {
										window.frames["content"].location.href = node.data.url + '.action';
										if(window.document.getElementById("alwaysopen").value == 'false') {
					            			window.closewest();
					            		}
									}
								}
							}
						}
					}));
			west.doLayout();
		}
	}
	
	Ext.create('Ext.container.Viewport', {
		layout: 'border',
		items: [
		    north,
		    west,
		    center
		],
		listeners : {
			afterrender: function(){
				Ext.getBody().mask('正在加载系统菜单....');
				ajax({
					url: "user!initMenu.action",// 获取面板的地址
					params: {
						action: "list"
					},
					callback: addTree
				});
			}
		}
	});
	
	//密码
	Ext.apply(Ext.form.VTypes, {
	    password : function(val, field) {
	    	if (field.initialPassField) {
	            var pwd = Ext.getCmp(field.initialPassField);
	            return (val == pwd.getValue());
	        }
	        return true;
	    },

	    passwordText : '密码不匹配'
	});
	
 /**-----性别combosex-----------*/
	
	var combosex=new Ext.form.ComboBox({  
		 	typeAhead: true,
		 	 fieldLabel: '性别',
		 	 triggerAction: 'all',
		 	 lazyRender:true,
		    name:'sex',
		    mode: 'local',
		    store : new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'savesex',
	            'showsex'
	        ],
	        data: [['男', '男'], ['女', '女']]
	    }) ,
	valueField :'savesex',
	displayField: 'showsex'
		}); 
	/**---------------学历--------------*/
	
	var comboedu = new Ext.form.ComboBox({
	    typeAhead: true,
	    fieldLabel: '学历',  
	    triggerAction: 'all',
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


	var form = new Ext.form.FormPanel({
		url:'user!personalSet.action',
		border:false,
		fieldDefaults: {
	        labelAlign: 'right',
	        labelWidth:110
	    },
		buttonAlign : 'center',
		autoHeight : true,
		width : 700,
		items: [{
            layout:'column',
            
            items:[{
                columnWidth:.3,
                layout: 'form',
                border:false,
                defaults:{
            		xtype:'textfield',
            		anchor:'95%'
                },
                items: [{
                	xtype:'hidden',
                	id:'username'
                },{
                	xtype:'hidden',
                	id:'saveorupdate',
                	name:'saveorupdate',
                	value:'修改'
                },{
                    fieldLabel: '账号',
                    disabled:true,
        			id:'account',
        			name:'account'
                },{
        			fieldLabel:'密码',
        			name:'password',
        			id:'pass',
        		    inputType: 'password',
        			minLength:8
        		}, {
                    fieldLabel: '真实姓名',
                    name:'name',
                    allowBlank:false,
                    id:'name',
                    listeners:{
                    	blur:function(){
        			
							if(Ext.getCmp('name').getValue().trim().length!=0){
								Ext.Ajax.request( {
									url : 'user!checkName.action',
									params:{name:Ext.getCmp('name').getValue().trim(),saveorupdate:"修改",id:Ext.getCmp("id").getValue()},
									success : function(response) {
										if(response.responseText==0){
											Ext.getCmp('name').setValue('');
											 Ext.example.msg('', "用户姓名已存在");	
											}
									},
									failure: handFailure
								});
								}
                		}
            		}
                },{
                	fieldLabel:'手机号码',
                	id:'mobile',
                	name:'mobile',
                	allowBlank:false,
                	minLength:11,
                	maxLength:11,
                	listeners:{
                    	blur:function(){
							if(Ext.getCmp('mobile').getValue().trim().length!=0){
								Ext.Ajax.request( {
									url : 'user!checkMobile.action',
									params:{mobile:Ext.getCmp('mobile').getValue().trim(),saveorupdate:"修改",id:Ext.getCmp("id").getValue()},
									success : function(response) {
										if(response.responseText==0){
											Ext.getCmp('mobile').setValue('');
											 Ext.example.msg('', "手机号已存在");	
											}
									},
									failure: handFailure
								});
								}
                		}
            		}
                },{
                	fieldLabel:'紧急联系人',
                	id:'emePerson',
                	name:'emePerson'
                	 
                },{
                	fieldLabel:'紧急联系人方式',
                	id:'emePhone',
                	name:'emePhone'
                }]
            },{
                columnWidth:.3,
                layout: 'form',
                border:false,
                 defaults:{
            		xtype:'textfield',
            		anchor:'95%'
                },
                items: [{
                    fieldLabel: '生日',
                    disabled:true,
        			id:'birthday',
        			name:'birthday'
                },{
        			fieldLabel:'确认密码',
        			inputType: 'password',
        			vtype: 'password',
        			id:'repass',
        			
        			
        		    initialPassField: 'pass' // id of the initial password field
        		},{
                	fieldLabel:'办公电话',
                	minLength:3,
                	id:'phone',
                	name:'phone'
                },{
                	fieldLabel:'QQ号',
                	minLength:6,
                	id:'qq',
                	name:'qq'
                },combosex

]
            },{
                columnWidth:.4,
                border:false,
                layout: 'form',
                 defaults:{
            		xtype:'textfield',
            		anchor:'95%'
                },
                items: [
							{
								fieldLabel:'身份证号',
								name:'idNumber',
								id:'userIdNumber',
								minLength:15,
								maxLength:18,
								allowBlank:false,
								 listeners:{
			                    	blur:function(){
										if(Ext.getCmp('userIdNumber').getValue().trim().length!=0){
											Ext.Ajax.request( {
												url : 'user!checkIDnumber.action',
												params:{idNumber:Ext.getCmp('userIdNumber').getValue().trim(),saveorupdate:"修改",id:Ext.getCmp("id").getValue()},
												success : function(response) {
													if(response.responseText==0){
														Ext.getCmp('userIdNumber').setValue('');
														 Ext.example.msg('', "身份证号已存在");	
														}
												},
												failure: handFailure
											});
											}
		                    		}
	                    		}
							},{
			                    fieldLabel: '电子邮件',
			                    id:'email',
			                    name:'email',
			                    allowBlank:false,
			                    vtype:'email',
			                    listeners:{
			                    	blur:function(){
										if(Ext.getCmp('email').getValue().trim().length!=0){
											Ext.Ajax.request( {
												url : 'user!checkEmail.action',
												params:{email:Ext.getCmp('email').getValue().trim(),saveorupdate:"修改",id:Ext.getCmp("id").getValue()},
												success : function(response) {
													if(response.responseText==0){
														Ext.getCmp('email').setValue('');
														 Ext.example.msg('', "邮箱已存在");	
														}
												},
												failure: handFailure
											});
											}
		                    		}
	                    		}
			                },{
			                	fieldLabel:'毕业院校',
			                	id:'school',
			                	name:'school',
			                },{
			                	fieldLabel:'住址',
			                	id:'address',
			                	name:'address'
			                },comboedu
                        ]
            }
    	]
        },{
            xtype:'textarea',
            name:'remark',
            id:'remark',
            fieldLabel:'备注',
            height:100,
            anchor:'98%'
      },{
			
			xtype:'hidden',
			name:'id',
			id:'id'
		}
	],
		buttons:[{
			text:'提交',
			handler:function(){
			if(Ext.getCmp("pass").getValue()!=""&&Ext.getCmp("repass").getValue()==""){
				 Ext.example.msg('', '请再次输入密码');
				}else{
					 Ext.Ajax.request({
							url : 'login!checkPassword.action',
							type: 'post',
							params :{"str":Ext.getCmp("pass").getValue()},
							success:function(response){
							  if(response.responseText=='ok'){
								  form.getForm().submit({
									    success: function(f, action) {
										 userWindow.hide();
										 Ext.Msg.alert('提醒','个人信息已修改');
									    },
									    failure: handFailure
									});
							  }else{
							     Ext.Msg.alert("提示",response.responseText);
							  }
								
							},
							failure:function(){
								Ext.Msg.alert("系统异常");	
							}

						});	
				}
			}
		},{
			text:'取消',
			handler:function(){
			userWindow.hide();
		}
		}]
	});
	
	
	
	
	var userWindow = new Ext.Window({
		title:'个人设置',
		closeAction: 'hide',
		
		autoScroll:true,
		modal:true,
		listeners:{
		afterlayout:function(){
			setTimeout(function(){//密码清空
				Ext.getCmp("pass").setValue('');
				Ext.getCmp("repass").setValue('');
			},500)
		
    	}
	},
		items:form
	});
	 this.hyperlink_onclicks = function(){
		 Ext.getCmp("pass").setValue('');
		 Ext.Ajax.request( {
				url : 'user!preupdate.action',
				success : function(response) {
			 		var userup = Ext.decode(response.responseText);
			 		  Ext.getCmp("id").setValue(userup.id);
			 		  Ext.getCmp("username").setValue(userup.username);
			 		 Ext.getCmp("account").setValue(userup.username);
			 		  Ext.getCmp("birthday").setValue(userup.birthday);
			 		  Ext.getCmp("phone").setValue(userup.phone);
			 		  Ext.getCmp("mobile").setValue(userup.mobile);
			 		  Ext.getCmp("email").setValue(userup.email);
			 		  Ext.getCmp("userIdNumber").setValue(userup.idNumber);
			 		  Ext.getCmp("name").setValue(userup.name);
			 		 Ext.getCmp("remark").setValue(userup.remark);
			 		 Ext.getCmp("address").setValue(userup.address);
			 		 if(userup.education!=null){
			 			 comboedu.setValue(userup.education);
			 		 }else{
			 			 comboedu.setValue('本科');
			 		 }
			 		 if(userup.sex!=null){ 
			 			combosex.setValue(userup.sex);
			 		 }else{
			 			 combosex.setValue('女');
			 		 }
			 		 Ext.getCmp("school").setValue(userup.school);
			 		 Ext.getCmp("qq").setValue(userup.qq);
			 		 Ext.getCmp("emePerson").setValue(userup.emePerson);
			 		 Ext.getCmp("emePhone").setValue(userup.emePhone);
			 		  userWindow.show();
				},
				failure:function(){
					Ext.Msg.alert('重新登陆');
				}
			});
		 
	 }
	 
	 
//////换肤start

	 var themes = [ 
	    ['默认','i'],
		['浅蓝', 'a'], 
		['青色', 'b'], 
		['绿色', 'c'], 
		['黄色', 'd'], 
		['橙色', 'e'], 
		['暗紫', 'g'], 
		['紫色','h'],
		['pink','m'],
		['红色','n']										           
		]; 
		var Mystore=new Ext.data.SimpleStore({ 
					fields:['theme','css'], 
					data:themes 
			 }); 
								          
		//   定义下拉列表框 
		var Mycombo=new Ext.form.ComboBox({ 
				fieldLabel:'更换皮肤', 
				id:'css', 
				triggerAction:'all', 
				store:Mystore, 
				displayField:'theme', 
				valueField:'css', 
				value:'默认', 
				mode:'local', 
				anchor:'95%', 
				handleHeight:10, 
				resizable:true, 
				selectOnfocus:true, 
				typeAhead:true 
			}); 
		//定义事件							          
		Mycombo.on({ 
			"select":function(){ 
				 var css =   Mycombo.getValue(); 
				 //设置cookies 
				 var date=new Date(); 
				 date.setTime(date.getTime()+30*24*3066*1000); 
				 document.cookie="css="+css+";expires="+date.toGMTString(); 
				// top.location.href='user!remain.action';
				//alert(parent.tiaozhuan);
				parent.tiaozhuan=1;
				top.location.href='/uscilims/welcome.jsp';
			} 
       });          										
         var MyPanel=new Ext.form.FormPanel({ 
         width:300, 
        // height:150, 
         labelWidth:70, 
         items:[Mycombo] 
     }); 
     
     //window
     var plw = new Ext.Window({
					title:'主题',
					closeAction: 'hide',	
					autoScroll:true,
					modal:true,
					items:MyPanel
				});

     this.bgtheme = function(){
    	 plw.show();
     }
//////换肤 end
	 


});
//权限详情S
function pageRight(){
/*  Ext.define('pageRight', {
  extend: 'Ext.data.Model',
  idProperty : 'xx',  //不加此属性时，默认 idProperty : 'id' ，修改后获取到的数据还是原来的 （注意实体id命名）
  fields: [
     {
			name : 'roleName'
		}, {
			name : 'roleRights'
		},{
			name:'roleUser'
		}
  ]
});*/

var pageRight_store = Ext.create('Ext.data.Store', {
  //model: 'pageRight',
  fields: [
     {
			name : 'roleName'
		}, {
			name : 'roleRights'
		},{
			name:'roleUser'
		}
  ],
  pageSize:200,//不加，点击下一页时默认25条
  proxy: {
      type: 'ajax',
      actionMethods: {
             read   : 'POST', // by default GET
         },
      url: 'role!pageRight.action',
      reader: {
         // type: 'json',
          root: 'result',
          totalProperty : 'totalCount'
      }
  },
  //autoLoad: true
});
pageRight_store.on("beforeload",function(store,options){
		
	//pageRight_grid.headerCt.getGridColumns()[1].autoSize();
	
			Ext.apply(store.proxy.extraParams, {jspPage:jspPage});  
	
		
		});
var pageRight_grid = Ext.create('Ext.grid.Panel', {
	//autoHeight:true, 
	//height: document.documentElement.clientHeight,
	//autoWidth:true,
		loadMask : true,
		stripeRows : true,
		frame : true,
		store : pageRight_store,
		width:1000,
		// layout:"fit",
		//bodyStyle:'width:100%', 
		viewConfig : {
			//forceFit : true
			 enableTextSelection:true  //可复制内容
		},
		 
		columns : [ {
		header : '角色名称',
		//width:150,
		//flex:1,//随着grid变化,设置列宽自适应（pageRight_grid.headerCt.getGridColumns()[0].autoSize()）后，此属性不起作用
		dataIndex : 'roleName'
	}, {
		header : '角色权限',
		width : 400,
		//height: 100,
		//autoHeight:true,
		dataIndex : 'roleRights',
		renderer : function (value, meta, record) {//内容超长自动换行/高度自适应
			meta.style = 'white-space:normal;word-break:break-all;';
			return value;
			}
	}, {
		header : '人员',
		//width : 400,
		//flex:1,
		dataIndex : 'roleUser'
	}]
	});

	var pageRight_win =Ext.create('Ext.Window', {
		closeAction : 'hide',
		title:"权限详情",
		modal : true,
		//autoHeight : true,
	    //autoWidth:true,
	    layout:"fit",//自适应内容
		items : pageRight_grid
	});
	
	
	 pageRight_store.load({
		 callback : function (records, options, success) {
		 //alert("123")
		 if (success){
			//列宽自适应 
			 pageRight_grid.headerCt.getGridColumns()[0].autoSize();
			// pageRight_grid.headerCt.getGridColumns()[1].autoSize();
			 pageRight_grid.headerCt.getGridColumns()[2].autoSize();
		
		 }
		 }
		 });
	 pageRight_win.show();
	 
}

//权限详情E
//更新日志s
function operateWin(){
	var main_store = Ext.create('Ext.data.Store', {
	    // model: 'mainPage',
		 fields: [
				      {
				    	  name : 'id'
				      },{
				    	  name : 'svnNo'
				      }, {
				    	  name : 'operation' 
				      }, {
				    	  name : 'submitter'
				      }, {
				    	  name : 'submitTime'
				      },{
				    	  name:'inputName'
				      },{
				    	  name:'inputTime'
				      },{
				    	  name:'updateName'
				      },{
				    	  name:'updateTime'
				      },{
				    	  name:'jspName'
				      }
				  ],
	     pageSize:20,//不加，点击下一页时默认21条
	    
	     proxy: {
	         type: 'ajax',
	         actionMethods: {
	                read   : 'POST', // by default GET`
	            },
	     url: 'operate.action',
	     reader: {
	            // type: 'json',
	             root: 'result',
	             totalProperty : 'totalCount'
	         }
	     },
	     //autoLoad: true
	 });
	 //-----------------------------查询条件判断-------------------------------
			/*main_store.on("beforeload",function(store,options){	
			//查询前单条件模糊查询,只查询当前页jsp
				var new_params={filter_LIKES_jspName:jspPage};			
				Ext.apply(store.proxy.extraParams, new_params);  
			});*/
	 //-------------grid开始------------------------------------------
	var operate_grid = Ext.create('Ext.grid.Panel', {
		//id:operate_grid,
		width : 900,
		region:'center',
		loadMask : true,
		stripeRows : true,
		cellTip : true,  //鼠标悬浮提示
		frame : true,
		store : main_store,
		viewConfig:{  
		   enableTextSelection:true  //可复制内容
		} ,
		columns : [{
			width : 150,
			hidden : false,
			header : 'SVN版本号',
			dataIndex : 'svnNo'
		}, {
			width : 400,
			hidden : false,
			header : '更新内容',
			dataIndex : 'operation'
		},{
			width : 150,
			hidden : false,
			header:'提交人',
			dataIndex:'submitter'
		},{
			width:150,
			hidden : false,
			header:'提交日期',
			dataIndex:'submitTime'
		}],
		
		//分页工具栏--------------------------
		bbar : new Ext.PagingToolbar( {
			store :main_store,
			displayInfo : true,
			pageSize : 20
		})
	});
	//-------------grid结束------------------------------------------
	
			//---------------加
		var operate_win =Ext.create('Ext.Window', {
			
			closeAction:'hide',
			title:"更新日志",
			x:200,
			y:50,
			modal : true,
			items :operate_grid,
			resizable:false 
		});
		
	
			
			 main_store.load();
			 //debugger;
			 operate_win.show()
}
//更新日志E
function isTrueUserPwd(s){ 
	//强：字母+数字+特殊字符 
	var patrn=/^(?![a-zA-z]+$)(?!\d+$)(?![!@.#$%^*]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@.#$%^*]+$)(?![\d!@.#$%^*]+$)[a-zA-Z\d!@.#$%^*]+$/;
	if(s.length > 7 && patrn.test(s)){
		return true;
	}else{
		return false;	
	}																															  
} 
//权限图S
function pageRightImg(){
 		if(jspPage!=""){
 			jspPage2=jspPage.substring(0,jspPage.indexOf("."));
 		}
 var rpi=new Ext.FormPanel({   
			  height:600,
			  width:700,
			  region:'center',
			  autoScroll:true,
			  split: true,      
			  html: "<img id='imagesp' name='imagesp' src='upload/rightpage/"+jspPage2+".png' border='0' width='650' style='position: relative; left: 0px; top: 0px;'>"
	 })
  
 
	var rpi_win = new Ext.Window( {
		title : '图片',
		closeAction : 'hide',
		modal : true,
		items : rpi
	});
	rpi_win.show();
	//失效了  $("#imagesp").attr("src","upload/rightpage/"+jspPage+".png");
 }
 //权限图E