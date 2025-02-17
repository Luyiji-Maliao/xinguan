jQuery.fn.extend({
	selectdate : function(data){
		
		this.click(function(){
			if(!isexists("#select_full_window")){
				var full_window = '<div id="select_full_window"></div>';
				jQuery('body').append(full_window);
			}
			var title = '设置时间';
			if(typeof(data['title']) !== 'undefined'){
				title = data['title'];
			}
			if(typeof(data['input_title']) !== 'undefined'){
				var input_title = data['input_title'];
				if(jQuery(this).attr("input_title") != ""){
					title = jQuery(this).attr(input_title);
				}
			}
			var date = new Date();
			var year = date.getFullYear();
			var month = date.getMonth() + 1;
			var str = '<div class="select_gc_main">'+
			               '<div class="select_content">'+
					           '<div class="select_content_title">'+
					               '<h2>'+title+'</h2>'+
					               '<i class="iconfont icon-close close"></i>'+
					           '</div>'+
					           '<div class="select_content_main fn-clear">'+
					              '<select class="select_year">'+
					                   make_select_option_year()+
					              '</select>'+
					              '<select class="select_month">'+
					                   make_select_option_month()+
					              '</select>'+
					              '<select class="select_day">'+
					                  make_select_option_day(year,month)+
					              '</select>'+
					              '<select class="select_hour">'+
					                    make_select_option_hour()+ 
					              '</select>'+
					              '<select class="select_minute">'+
					                    make_select_option_minute()+
					              '</select>'+
					           '</div>'+
					           '<div class="select_content_bottom fn-clear">'+
					               '<button class="transition select_button_case">取消</button>'+
					               '<button class="transition select_button_sure">确定</button>'+
					           '</div>'+
					       '</div>'+
					   '</div>';
			jQuery("#select_full_window").html(str).show();
			select_year_month_do();
			select_date_close(jQuery(this));
			select_date_sure(jQuery(this));
		});
		function select_date_sure(thisobj){
			//2018-06-18 19:23:51
			jQuery("#select_full_window .select_button_sure").click(function(){
				var str = jQuery(".select_year").val() + '-' + jQuery(".select_month").val() + '-' +jQuery(".select_day").val() + ' ' + jQuery(".select_hour").val() + ':' + jQuery(".select_minute").val() + ':' + '00';
				thisobj.val(str);
				thisobj.focus();
				jQuery("#select_full_window").hide();
			})
			
		}
		function select_date_close(thisobj){
			jQuery("#select_full_window .select_button_case,#select_full_window .icon-close").unbind().click(function(){
				jQuery("#select_full_window").hide();
				thisobj.focus();
			});
		}
		function select_year_month_do(){
			jQuery(".select_month").change(function(){
				var year = jQuery(".select_year").val();
				var month = jQuery(this).val();
				var str = make_select_option_day(year,month);
				jQuery(".select_day").html(str);
			});
			jQuery(".select_year").change(function(){
				var year = jQuery(this).val();
				var month = jQuery(".select_month").val();
				var str = make_select_option_day(year,month);
				jQuery(".select_day").html(str);
			});
		}
		function make_select_option_year(){
			var date = new Date();
			var year = date.getFullYear();
			var str = make_select_option(year-1,year+1,'年',year);
			return str;
		}
		function make_select_option_month(){
			var date = new Date();
			var month = date.getMonth() + 1;
			var str = make_select_option(1,12,'月',month);
			return str;
		}
		function make_select_option_day(year,month){
			var year = Number(year);
			var month = Number(month);
			var date = new Date();
			var day = date.getDate();
			var day31 = [1,3,5,7,8,10,12];
			var max = 30;
			if(month == 2){
				if(isleapyear(year)){
					max = 29;
				}else{
					max = 28;
				}
			}else{
				if(jQuery.inArray(month,day31) >= 0){
					max = 31;
				}
			}
			var str = make_select_option(1,max,'日',day);
			return str;
		}
		function make_select_option_hour(){
			return make_select_option(0,23,'时');
		}
		function make_select_option_minute(){
			return make_select_option(0,59,'分');
		}
		function make_select_option(min,max,name,selected){
			var str = '';
			var std = false;
			if(selected !== 'undefined'){
				std = selected;
			}
			if(std){
				for(i=min;i<=max;i++){
					var str_std = '';
					if(i == std){
						str_std = 'selected="selected" ';
					}
					var two_i = select_option_two_digits(i);
					str = str + '<option '+str_std+'value="'+two_i+'">'+two_i+name+'</option>';
				}
			}else{
				for(i=min;i<=max;i++){
					var two_i = select_option_two_digits(i);
					str = str + '<option value="'+two_i+'">'+two_i+name+'</option>';
				}
			}
			return str;
		}
		function isleapyear(year){
			if(year % 4 == 0 && year % 100 !=0){
				return true;
			}else if(year % 400 == 0){
				return true;
			}
			return false;
		}
		function isexists(a){
			return 0<jQuery(a).length?!0:!1
		}
		function select_option_two_digits(i){
			var i = Number(i);
			if(i < 10){
				return '0'+i;
			}
			return i;
		}
	},
});
