	
	//计算dat 后itervalByDay个工作日是几号（工作日指除周六日外的所有日期）
	function getworkday(dat,itervalByDay){
		var str=dat.split("-");
		var date=new Date(); 
		date.setUTCFullYear(str[0], str[1] - 1, str[2]); 
		date.setUTCHours(0, 0, 0, 0); 
		var millisceonds =date.getTime();
		for(var i=1;i<=itervalByDay;i++){
			millisceonds +=24*60*60*1000;
			date.setTime(millisceonds);
			if(date.getDay()==0||date.getDay()==6){
				i--;
			}
		}

		var year = date.getFullYear(); 
		var month = date.getMonth() + 1; 
		var day = date.getDate(); 
		var rq = year + "-" + formatTen(month) + "-" + formatTen(day);
		
		return rq;
	}
	
	//获取数据库的法定节假日表
	var holiday=function init() {
		var holiday=new Array();
		$.ajax({
			type: 'POST',
			url: 'tjpmorder!workday.action',
			data: '',
			success: function(msg){
				if(msg!=''){
					var str=msg.split(",");
					for(var i=0;i<str.length;i++){
						holiday[i]=(str[i].substring(0,4)+"-"+str[i].substring(4,6)+"-"+str[i].substring(6,8));
					}
				}
			}
		});
		return holiday;
	}
	
	var holidayMap={};
	function getMap(){
		var holiday=new Array();
		Ext.Ajax.request({
			url:"tjpmorder!workday.action",
			actionMethods : 'post',
			async : false,
			reader : {
				type : 'json'
			},
			waitMsg : '正在提交数据',
			timeout: 100000000,
			success : function (response) {
				if(Ext.decode(response.responseText).result!=''){
					var str=Ext.decode(response.responseText).result.split(",");
					for(var j=0;j<str.length;j++){
						holiday[j]=(str[j].substring(0,4)+"-"+str[j].substring(4,6)+"-"+str[j].substring(6,8));
					}
				}
			},
			failure : function (form, action) {
				Ext.Msg.hide();
				Ext.example.msg("提示", "错误，请联系管理员！");
			}
		}); 
	   for(var i=0;i<holiday.length;i++){
	      holidayMap[holiday[i]]='1';
	   }
	}
	function formatTen(f){
	  if (parseInt(f,10)<10){
	      return '0'+f;
	  }
	  return f;
	}
	function formateDate(date){   
		var year = date.getFullYear(); 
		var month = date.getMonth() + 1; 
		var day = date.getDate(); 
		return year + "-" + formatTen(month) + "-" + formatTen(day);
	}
	getMap();
	//计算dat 后itervalByDay个工作日是几号（工作日指除法定节假日外的日期）
	function getworkdayfd(dat,itervalByDay){
		var str=dat.split("-");
		var date=new Date(); 
		date.setUTCFullYear(str[0], str[1] - 1, str[2]); 
		date.setUTCHours(0, 0, 0, 0); 
		var millisceonds =date.getTime();
		for(var i=1;i<=itervalByDay;i++){
		   millisceonds +=24*60*60*1000;
		   date.setTime(millisceonds);
	
		   var d=formateDate(date);
		     if(holidayMap[d]){
		        i--;
		     }
		}

		var year = date.getFullYear(); 
		var month = date.getMonth() + 1; 
		var day = date.getDate(); 
		var rq = year + "-" + formatTen(month) + "-" + formatTen(day);
	
		return rq;
	}
	
	//计算dat 后itervalByDay个自然日是几号
	function getworkdayzr(dat,itervalByDay){
		var str=dat.split("-");
		var date=new Date(); 
		date.setUTCFullYear(str[0], str[1] - 1, str[2]); 
		date.setUTCHours(0, 0, 0, 0); 
		var millisceonds =date.getTime();
		for(var i=1;i<=itervalByDay;i++){
			millisceonds +=24*60*60*1000;
			date.setTime(millisceonds);
		}

		var year = date.getFullYear(); 
		var month = date.getMonth() + 1; 
		var day = date.getDate(); 
		var rq = year + "-" + formatTen(month) + "-" + formatTen(day);
		
		return rq;
	}
	