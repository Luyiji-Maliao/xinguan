<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ECharts</title>
     
    <!-- 引入 echarts.js -->
    <script type="text/javascript" src="js/echarts.min.js"></script>
    <!-- 引入jquery.js -->
    <script type="text/javascript" src="js/jquery.min.js"></script>
</head>
<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 1000px;height:400px;"></div>
     
    <script type="text/javascript">
         
        var myChart = echarts.init(document.getElementById('main'));
         // 显示标题，图例和空的坐标轴
         var option={
             title: {
                 text: '提取统计'
             },
             tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        },
        
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {show: true, type: ['line', 'bar','stack', 'tiled']},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
             legend: {
                 data:['合格','不合格']
             },
             xAxis: {
                 data: []
             },
             yAxis: {
             	xtype:'value'
             },
             series: [{
                 name: '合格',
                 type: 'bar',
                  stack: 'sum',
                  label: {
                normal: {
                    show: true,
                    position: 'insideRight',
                    formatter: function (params) {
                        if(params.value==0)
                        return '';
                     }
                }
            },
                 data: []
             },
             {
                 name: '不合格',
                 type: 'bar',
                  stack: 'sum',
                  label: {
                normal: {
                    show: true,
                    position: 'insideRight',
                    formatter: function (params) {
                        if(params.value==0)
                        return '';
                     }
                }
            },
               data: []
             }]
         };
          
         myChart.showLoading();    //数据加载完之前先显示一段简单的loading动画
          var vb1 = parent.s1;
          var vb2 = parent.s2;
         var a1=[];    //类别数组（实际用来盛放X轴坐标值）
         var a2=[];    //销量数组（实际用来盛放Y坐标值）
         var a3=[]; 
         $.ajax({
         type : "post",
         async : true,            //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
         url : "countsample!nucleicacidscount.action",    //请求发送到TestServlet处
         data : {osReceiveDateTime1:vb1,osReceiveDateTime2:vb2},
         dataType : "json",        //返回数据形式为json
         success : function(result) {
             //请求成功时执行该函数内容，result即为服务器返回的json对象
             if (result) {
                    for(var i=0;i<result.length;i++){       
                       a1.push(result[i].osReceiveDateTime);    //挨个取出类别并填入类别数组
                     }
                    for(var i=0;i<result.length;i++){       
                        a2.push(result[i].hegeCount);    //挨个取出销量并填入销量数组
                        
                         a3.push(result[i].buhegeCount);    //挨个取出销量并填入销量数组
                      }
                    myChart.hideLoading();    //隐藏加载动画
                    myChart.setOption({        //加载数据图表
                        xAxis: {
                            data: a1
                        },
                        series: [{
                            name: '合格',
                            data: a2
                        },{
                            name: '不合格',
                            data: a3
                        }]
                    });
                     
             }
          
        },
         error : function(errorMsg) {
             //请求失败时执行该函数
         alert("图表请求数据失败!");
         myChart.hideLoading();
         }
    })
          myChart.setOption(option);    //载入图表
    </script>
     
</body>
</html>