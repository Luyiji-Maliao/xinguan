function sendPhp(sampleNo){
	Ext.Ajax.request({
		url:"pushphp!sendData.action?sampleNo="+sampleNo,
		actionMethods : 'post',
		async : false,
		reader : {
			type : 'json'
		},
		waitMsg : '正在提交数据',
		timeout: 100000000,
		success : function (response) {
			Ext.Msg.hide();
			Ext.example.msg("提示", response.responseText);
			},
		failure : function (form, action) {
			Ext.Msg.hide();
			Ext.example.msg("提示", "错误，请联系管理员！");
		}
	});
}

function sendPhpYxy(sampleNo){
	Ext.Ajax.request({
		url:"pushphp!sendDataYxy.action?sampleNo="+sampleNo,
		actionMethods : 'post',
		async : false,
		reader : {
			type : 'json'
		},
		waitMsg : '正在提交数据',
		timeout: 100000000,
		success : function (response) {
			Ext.Msg.hide();
			Ext.example.msg("提示", response.responseText);
			},
		failure : function (form, action) {
			Ext.Msg.hide();
			Ext.example.msg("提示", "错误，请联系管理员！");
		}
	});
}

function sendPhpYy(sampleNo){
	Ext.Ajax.request({
		url:"pushphp!sendDataYy.action?sampleNo="+sampleNo,
		actionMethods : 'post',
		async : false,
		reader : {
			type : 'json'
		},
		waitMsg : '正在提交数据',
		timeout: 100000000,
		success : function (response) {
			Ext.Msg.hide();
			Ext.example.msg("提示", response.responseText);
		},
		failure : function (form, action) {
			Ext.Msg.hide();
			Ext.example.msg("提示", "错误，请联系管理员！");
		}
	});
}

function sendPhpNo(sampleNo){
	Ext.Ajax.request({
		url:"pushphp!sendDataNo.action?sampleNo="+sampleNo,
		success : function (response) {
			Ext.Msg.hide();
			Ext.example.msg("提示", response.responseText);
		},
		failure : function (form, action) {
			Ext.Msg.hide();
			Ext.example.msg("提示", "错误，请联系管理员！");
		}
	});
}

function sendPhpNopass(sampleNo){
	Ext.Ajax.request({
		url:"pushphp!sendDataDpnopass.action?sampleNo="+sampleNo,
		success : function (response) {
			Ext.Msg.hide();
			Ext.example.msg("提示", response.responseText);
		},
		failure : function (form, action) {
			Ext.Msg.hide();
			Ext.example.msg("提示", "错误，请联系管理员！");
		}
	});
}



