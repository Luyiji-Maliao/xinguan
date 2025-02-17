<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>echarts.html</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
     <script type="text/javascript" src="js/jquery.min.js"></script>
 		   <script src="echarts/echarts-2.2.7/build/dist/echarts.js"></script>
 
  <script type="text/javascript">
  $.ajaxSettings.async = false;  
        // 路径配置
        require.config({
            paths: {
                echarts: 'echarts/echarts-2.2.7/build/dist'
            }
        });
        
               // 使用
        require(
            [
                'echarts',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('main')); 
                  // 同步执行  
         var vb1 = "";
          var vb2 = "";    
         var a1=[];    //类别数组（实际用来盛放X轴坐标值）
         var a2=[];    //销量数组（实际用来盛放Y坐标值）
         var a3=[];      
        $.ajax({
         type : "post",
         async : false,            //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
         url : "countsample!barcount.action",    //请求发送到TestServlet处
         data : {osReceiveDateTime1:vb1,osReceiveDateTime2:vb2},
         dataType : "json",        //返回数据形式为json
         success : function(result) {
             //请求成功时执行该函数内容，result即为服务器返回的json对象
             if (result) {
                   
                    for(var i=0;i<result.length;i++){   
                   // alert("2");
                    a1.push(result[i].osReceiveDateTime);    //挨个取出类别并填入类别数组    
                        a2.push(result[i].hegeCount);    //挨个取出销量并填入销量数组
                        
                         a3.push(result[i].buhegeCount);    //挨个取出销量并填入销量数组
                      }
                  
                     
             }
          
        },
         error : function(errorMsg) {
             //请求失败时执行该函数
         alert("图表请求数据失败!");
         myChart.hideLoading();
         }
    });
   // alert("2");
     var      option = {
    title : {
        text: '温度计式图表',
        subtext: 'From ExcelHome',
        sublink: 'http://e.weibo.com/1341556070/AizJXrAEa'
    },
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        },
       /* formatter: function (params){
            return params[0].name + '<br/>'
                   + params[0].seriesName + ' : ' + params[0].value + '<br/>'
                   + params[1].seriesName + ' : ' + (params[1].value + params[0].value);
        }*/
    },
    legend: {
        selectedMode:false,
        data:['合格', '不合格']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            data : a1
        }
    ],
    yAxis : [
        {
            type : 'value',
            boundaryGap: [0, 0.1]
        }
    ],
    series : [
        {
            name:'合格',
            type:'bar',
            stack: 'sum',
            barCategoryGap: '50%',
            itemStyle: {
                normal: {
                    color: 'tomato',
                    barBorderColor: 'tomato',
                    barBorderWidth: 6,
                    barBorderRadius:0,
                    label : {
                        show: true, position: 'insideTop'
                    }
                }
            },
            data:a2
        },
        {
            name:'不合格',
            type:'bar',
            stack: 'sum',
            itemStyle: {
                normal: {
                    color: '#fff',
                    barBorderColor: 'tomato',
                    barBorderWidth: 6,
                    barBorderRadius:0,
                    label : {
                        show: true, 
                        position: 'top',
                        formatter: function (params) {
                            for (var i = 0, l = option.xAxis[0].data.length; i < l; i++) {
                            console.log(option.xAxis[0].data.length);
                                if (option.xAxis[0].data[i] == params.name) {
                                	//alert(option.series[0].data[i] + params.value);
                                    return parseInt(option.series[0].data[i]) + parseInt(params.value);
                                }
                            }
                        },
                        textStyle: {
                            color: 'tomato'
                        }
                    }
                }
            },
            data:a3
        }
    ]
};
                    
       
                // 为echarts对象加载数据 
                myChart.setOption(option); 
            }
        );
    </script>
  </head>
  
  <body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="height:400px"></div>
</body>
</html>
