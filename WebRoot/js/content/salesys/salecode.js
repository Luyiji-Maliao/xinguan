function findCode(agent,code){
	Ext.Ajax.request( {
		url : 'salecode!findAgentAreaName.action',
		params:{agentorAreaName:agent},
		success : function(response) {
			
			var info=JSON.parse(response.responseText);
			if(info.success){
				Ext.getCmp('agentorAreaCode').setValue(info.msg);
				Ext.getCmp('agentorAreaCode').setFieldStyle('background:#FFFFFF');
			}else{
				//随机一个代码
				if(code==0){
					Ext.getCmp('agentorAreaCode').setValue(info.msg);
					Ext.getCmp('agentorAreaCode').setFieldStyle('background:#FF3333');
					//Ext.getCmp('code').setFieldStyle('color:#FF3333');
				}
				
			}
				
		},
		failure: handFailure
	});
}