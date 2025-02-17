	Ext.Ajax.on('requestcomplete',checkUserSessionStatus, this); 
		function checkUserSessionStatus(conn,response,options){
			var str = response.responseText;
			if(str == '9999'){
				alert("未登录或登陆过期 ,请重新登录");
				window.parent.location = '/uscilims/';
				}}

Ext.onReady(function() {
	Ext.QuickTips.init();
	
	
	
	
  var westpanel = new Ext.Panel({
		region:'west',
		split:true,
		id:'navtree',
		border:true,
		//iconCls:'home',
		collapsible:true,
		collapsed:false,
		maxSize:300,
		width:190,
		layout:'accordion',
		listeners:{
			beforeshow:function(){
	  			
				Ext.Ajax.request({
					scope: this,
					url:'user!initMenu.action',
					success:function(response){
						var menudata = eval(response.responseText);
						for(var i=0;i<menudata.length;i++){
							var tree = new Ext.tree.TreePanel({
								id : menudata[i].id,
								title : menudata[i].text,
								iconCls:'titleIcon',
								border : false,
								autoScroll : true,
								dataUrl: 'user!initMenu.action',
							    root: {
							        nodeType: 'async',
							        text: 'Ext JS',
							        draggable: false,
							        expanded:false,
							        id: menudata[i].id
							    },

								listeners : {
									click:  function(n){
									if(n.isLeaf()){					
										helpdocs=n.attributes.text;//帮助文档
											if(n.attributes.url!=undefined){
						            			window.frames["maincontentframe"].location.href=n.attributes.url+".action";
							            		if(window.document.getElementById("alwaysopen").value=='false'){
							            			window.closewest();
							            		}
						            		}
											
										}else{
											

										}
									}
								},
								rootVisible : false
							});
							westpanel.add(tree);
							westpanel.doLayout();
						}
					}
				});
			}
		}
  });

  westpanel.show();

   var viewport=new Ext.Viewport({
		layout:'border',
		items:[
		       new Ext.BoxComponent({
		    	   region: 'north',
		    	   contentEl: 'north'
		       }),
		       westpanel, 
		       new Ext.BoxComponent({
		    	   id:'maincontent',
		    	   region: 'center',                                     //user!toDashboard.action个人门户路径user!dashthings.action
		    	   html:'<iframe scrolling="yes"  name="maincontentframe" src="user!dashthings.action"  frameborder="0" width="100%" height="100%" ></iframe>'     	
		       })
		       ]
	});
	
});

function closewest(){
	  var w = Ext.getCmp('navtree');
	  if(document.getElementById("wcollapseid").value =="true"){
		  w.collapse();
	  }
	  if(document.getElementById("wcollapseid").value =="false"){
    w.expand();
	  }
}
function openwest(){
	
	 var w = Ext.getCmp('navtree');
     document.getElementById("wcollapseid").value = "false";
     w.expand();
}

function treelocation(){
	 var w = Ext.getCmp('navtree');
	 w.findById('5').expand();
	 var ic= w.findById('5').getRootNode().childNodes;
	 ic[0].expand();
		var  s=ic[0]
		       if(ic[0].id==9){
		    	   setTimeout(function(){
		  			 s.eachChild(function(child){
		  				 if(child.text=="待办任务"){
		  					 child.select();
		  				 }
		  			 })
		  		 },1000);
		       }
		
	 

	
}

function setclosewest(){
	var w = Ext.getCmp('navtree');
	document.getElementById("wcollapseid").value = "true";
	
}
function setalwaysopen(){
	
	document.getElementById("alwaysopen").value = "false";
	
}
function personalportal(){
	helpdocs="个人门户";
	window.location.href='user!remain.action';
}
function helpdoc(){
	
	if(helpdocs=="个人门户"){
		window.location.href='schedulesort!moredownload.action';
	}else{
		
		window.location.href='schedulesort!downloadone.action?helpdocs='+helpdocs;
	}
	
	
}
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

Ext.onReady(function() {
	Ext.QuickTips.init();
	

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
		labelWidth : 75,
		labelAlign:'right',
		trackResetOnLoad:true,
		buttonAlign : 'center',
		frame : true,
		autoHeight : true,
		width : 700,
		items: [{
            layout:'column',
            items:[{
                columnWidth:.3,
                layout: 'form',
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
                	value:'修改'
                },{
                    fieldLabel: '账号',
                    disabled:true,
        			id:'account'
                },{
        			fieldLabel:'密码',
        			name:'password',
        			id:'pass',
        		    inputType: 'password',
        			minLength:4
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
                 defaults:{
            		xtype:'textfield',
            		anchor:'95%'
                },
                items: [{
                    fieldLabel: '生日',
                    disabled:true,
        			id:'birthday'
                },{
        			fieldLabel:'确认密码',
        			inputType: 'password',
        			vtype: 'password',
        			id:'repass',
        			
        			
        		    initialPassField: 'pass' // id of the initial password field
        		},{
                	fieldLabel:'办公电话',
                	minLength:3,
                	id:'phone'	
                },{
                	fieldLabel:'QQ号',
                	minLength:6,
                	id:'qq'
                },combosex

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
			                	id:'school'
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
				form.getForm().submit({
				    success: function(f, action) {
					
					 userWindow.hide();
					 Ext.Msg.alert('提醒','个人信息已修改');
				    },
				    failure: handFailure
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
		animateTarget:'',
		autoScroll:true,
		modal:true,
		listeners:{
		afterlayout:function(){
			setTimeout(function(){//密码清空
				Ext.getCmp("pass").setValue('');
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
	
})


