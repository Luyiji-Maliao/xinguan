<%--
  Created by IntelliJ IDEA.
  User: jianzhiping
  Date: 2020/5/12
  Time: 9:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <title>新冠统计信息</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="新冠统计信息">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <meta name="viewport" content=" maximum-scale=1.0, user-scalable=0">

    <link rel="stylesheet" href="css/xinguansign.css">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.js"></script>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/echarts@4.5.0/dist/echarts.min.js"></script>
    <script src="https://gw.alipayobjects.com/os/antv/assets/f2/3.4.2/f2.min.js"></script>
    <style>
        .echartdiv{
            font-size:40px;
            width: 100%;
            height: 600px;
            margin-top:20px;
            margin-bottom:20px;
        }
        .div{
            margin: 40px 20px;
            padding: 30px;
            border-radius: 30px;
            background-color: #fff;
        }
        .echartdiv2{
            height: 750px;
        }
        h1{
            font-size:40px;
            font-weight: bold;
        }
        .inputdate{
            margin: 0px 10px;
            height:80px;
            color:#D1D6DC;
            width:300px;
            border: 0px;
            text-align:center;
            background-color: #fff;
            border-bottom:1px solid #D1D6DC;
        }
        button.btn{
            font-size:40px;    height: 100px;
            width: 120px;
            padding: 0px 20px;
            border-radius: 10px;
            margin: 5px;
            background-color: #0045C7;
        }
        .info{
            font-size:32px;
        }
        #table{
            margin-top:30px;
            border-radius: 30px;
        }
        #table tr{
            height: 90px;
            border: 0px;
            border-bottom: 2px solid #D1D6DC;
        }
        thead tr{
            background-color: #ECF6FF;
            color: #0142C4;
            font-size: 42px;
        }
        #table thead tr th{
            min-width: 130px;
            font-size: 32px;
        }
        #table tr td{
            text-align: center;
            line-height: 100px;
        }
        hr{
            border-width:5px;
        }
        li{
            list-style: none;
        }
    </style>
</head>
<body style="font-size:40px;">
<h1 >各采样点人数统计</h1>
<from>
    <h2 style="font-size:40px;display: inline-block">开始时间：</h2>
    <input type="datetime-local" name="startdate" id="startdate" min="2020-04-18T00:00:00" class="inputdate">
    <button class="btn btn-info" onclick="alltable()">全部</button>
    <br>
    <h2 style="font-size:40px;display: inline-block">结束时间：</h2>
    <input type="datetime-local" name="enddate" id="enddate" min="2020-04-18T00:00:00" class="inputdate">
    <button class="btn btn-info" onclick="query()">确定</button>
</from>
<%--<button class="btn btn-info" style="width:100%;">查看全部</button>--%>
<table class="table table-bordered table-striped" id="table">
    <thead>
    <th>采样点</th>
    <th>现场</th>
    <th>线上</th>
    <th>合计</th>
    <th>实际</th>
    </thead>
    <tbody>
    </tbody>
</table>

<h1 >各采样点已出报告数统计</h1>
<from>
    <h2 style="font-size:40px;display: inline-block">开始时间：</h2>
    <input type="datetime-local" name="startdate" id="startdate3" min="2020-04-18T00:00:00" class="inputdate">
    <button class="btn btn-info" onclick="alltable2()">全部</button>
    <br>
    <h2 style="font-size:40px;display: inline-block">结束时间：</h2>
    <input type="datetime-local" name="enddate" id="enddate3" min="2020-04-18T00:00:00" class="inputdate">
    <button class="btn btn-info" onclick="query2()">确定</button>
</from>
<%--<button class="btn btn-info" style="width:100%;">查看全部</button>--%>
<table class="table table-bordered table-striped" id="table2">
    <thead>
    <th>采样点</th>
    <th>已出</th>
    </thead>
    <tbody>
    </tbody>
</table>

<h1 >今日出报告数查询</h1>
<from>
    <h2 style="font-size:40px;display: inline-block">开始时间：</h2>
    <input type="datetime-local" name="startdate" id="startdate2" min="2020-04-18T00:00:00" class="inputdate">
    <button class="btn btn-info" onclick="alltable1()">全部</button>
    <br>
    <h2 style="font-size:40px;display: inline-block">结束时间：</h2>
    <input type="datetime-local" name="enddate" id="enddate2" min="2020-04-18T00:00:00" class="inputdate" >
    <button class="btn btn-info" onclick="query1()">确定</button>
</from>
<h1 id="num"></h1>
</body>
<script>
    var date=new Date();
    $("#startdate").val(date.toJSON().substr(0,10)+"T00:00:00");
    $("#enddate").val(date.toJSON().substr(0,10)+"T23:59:59");


    $("#startdate").attr("max",date.toJSON().substr(0,10)+"T23:59:59");
    $("#enddate").attr("max",date.toJSON().substr(0,10)+"T23:59:59");

    var startdate=$("#startdate").val();
    var enddate=$("#enddate").val();
    function alltable(){
        startdate="";
        enddate="";
        gettable();
    }

    function query(){
        startdate=$("#startdate").val();
        enddate=$("#enddate").val();
        gettable();
    }
    gettable();
    function gettable(){
        if(startdate>enddate){
            alert("起始时间请不要大于结束时间");
            return;
        }
        $.post({
            url:'sign!getcaiyangren.action',
            data:{"startdate":startdate,"enddate":enddate},
            success:function (data) {
                $("#table tbody").html("");
                var didian=[];
                var xianchang=[];
                var xianshang=[];
                var heji=[];
                var shiji=[];
                var zongchang=0;
                var zongxian=0;
                var zonghe=0;
                var zongshi=0;
                //合计
                for(var i=0;i<data.shijilist.length;i++){
                    didian[i]=data.shijilist[i].caiyangdian;
                    if(data.shijilist[i].qudao=="团体"){
                        didian[i]+="（团体）";
                    }
                    shiji[i]=data.shijilist[i].renshu;
                }
                //现场   根据采样点循环匹配到对应的下标然后填入数据
                for(var i=0;i<data.xianchanglist.length;i++){
                    var biao2=didian.indexOf(data.xianchanglist[i]==null?-1:data.xianchanglist[i].caiyangdian);
                    if(biao2!=-1){
                        xianchang[biao2]=data.xianchanglist[i].renshu;
                    }
                }
                //线上预约  根据采样点循环匹配到对应的下标然后填入数据
                for(var i=0;i<data.xianshanglist.length;i++){
                    var biao=didian.indexOf(data.xianshanglist[i]==null?-1:data.xianshanglist[i].caiyangdian);
                    if(biao!=-1){
                        xianshang[biao]=data.xianshanglist[i].renshu;
                    }
                }
                //合计  根据采样点循环匹配到对应的下标然后填入数据
                for(var i=0;i<data.hejilist.length;i++){
                    var biao3=didian.indexOf(data.hejilist[i]==null?-1:data.hejilist[i].caiyangdian);
                    if(biao3!=-1){
                        heji[biao3]=data.hejilist[i].renshu;
                    }
                }
                //计算总计人数，并将为匹配到采样点的数据修改为0  并将数据添加到页面
                for (var i=0;i<didian.length;i++){
                    if(didian[i] && didian[i].toString().indexOf("(")>-1){
                        didian[i]=didian[i].substring(0,didian[i].indexOf("("));
                    }
                    if(didian[i] && didian[i].indexOf("优迅医学")>-1){
                        didian[i]="优迅医学检验实验室"
                    }
                    if(!xianchang[i]) {
                        xianchang[i] = 0;
                    }
                    if(!xianshang[i]){
                        xianshang[i]=0;
                    }
                    if(!heji[i]){
                        heji[i]=0;
                    }
                    zongchang+=xianchang[i];
                    zongxian+=xianshang[i];
                    zonghe+=heji[i];
                    zongshi+=shiji[i];
                    $("#table tbody").append("<tr><td style='text-align:left;'>"+didian[i]+"</td><td>"+xianchang[i]+"</td><td>"+xianshang[i]+"</td><td>"+heji[i]+"</td><td>"+shiji[i]+"</td></tr>");
                }

                $("#table tbody").append("<tr class='info' style='font-size:50px'><td style='text-align:left;'>总计：</td><td>"+zongchang+"</td><td>"+zongxian+"</td><td>"+zonghe+"</td><td>"+zongshi+"</td></tr>");
            }
        })
    }


    var day1 = new Date();
    day1.setTime(day1.getTime()-24*60*60*1000);
    var mh=(day1.getMonth()+1)+"";
    if(mh.length<2){
        mh="0"+mh;
    }
    var s1 = day1.getFullYear()+"-" + mh + "-" + (day1.getDate().toString().length<2?"0"+day1.getDate().toString():day1.getDate());
  /*  $("#startdate3").val(s1);
    $("#enddate3").val(s1);
    $("#startdate3").attr("max",date.toJSON().substr(0,10));
    $("#enddate3").attr("max",date.toJSON().substr(0,10));
*/
    /*document.getElementById("startdate3").value=s1+"T00:00:00";
    document.getElementById("enddate3").value=s1.substr(0,10)+"T23:59:59";*/
    $("#startdate3").val(s1+"T00:00:00")
    $("#enddate3").val(s1.substr(0,10)+"T23:59:59")
    $("#startdate3").attr("max",date.toJSON().substr(0,10)+"T23:59:59");
    $("#enddate3").attr("max",date.toJSON().substr(0,10)+"T23:59:59");

    var startdate3=$("#startdate3").val();
    var enddate3=$("#enddate3").val();
    function alltable2(){
        startdate3="";
        enddate3="";
        gettable2();
    }
    function query2(){
        startdate3=$("#startdate3").val();
        enddate3=$("#enddate3").val();
        gettable2();
    }
    gettable2();
    function gettable2(){
        if(startdate>enddate){
            alert("起始时间请不要大于结束时间");
            return;
        }
        $.post({
            url:'sign!yichubaogaocyd.action',
            data:{"startdate":startdate3,"enddate":enddate3},
            success:function (data) {
                $("#table2 tbody").html("");
                var didian=[];
                var zongshi=0;
                //合计
                for(var i=0;i<data.shijilist.length;i++){
                    didian[i]=data.shijilist[i].caiyangdian;
                    if(data.shijilist[i].qudao=="团体"){
                        didian[i]+="（团体）";
                    }
                    if(didian[i] && didian[i].toString().indexOf("(")>-1){
                        didian[i]=didian[i].substring(0,didian[i].indexOf("("));
                    }
                    if(didian[i] && didian[i].indexOf("优迅医学")>-1){
                        didian[i]="优迅医学检验实验室"
                    }
                    zongshi+=data.shijilist[i].renshu;
                    $("#table2 tbody").append("<tr><td style='text-align:left;'>"+didian[i]+"</td><td>"+data.shijilist[i].renshu+"</td></tr>");
                }

                $("#table2 tbody").append("<tr class='info' style='font-size:50px'><td style='text-align:left;'>总计：</td><td>"+zongshi+"</td></tr>");
            }
        })
    }

    function alltable1(){
        startdate2="";
        enddate2="";
        getnum();
    }
    function query1(){
        startdate2=$("#startdate2").val();
        enddate2=$("#enddate2").val();
        getnum();
    }
    var day1 = new Date();
    day1.setTime(day1.getTime()-24*60*60*1000);
    var mh=(day1.getMonth()+1)+"";
    if(mh.length<2){
        mh="0"+mh;
    }
    var s1 = day1.getFullYear()+"-" + mh + "-" + (day1.getDate().toString().length<2?"0"+day1.getDate().toString():day1.getDate());
    document.getElementById("startdate2").value=s1+"T17:00:00";
    document.getElementById("enddate2").value=date.toJSON().substr(0,10)+"T17:00:00";
    /*$("#startdate2").val(s1+"T00:00:00")
    $("#enddate2").val(date.toJSON().substr(0,10)+"T23:59:59")*/
    $("#startdate2").attr("max",date.toJSON().substr(0,10)+"T23:59:59");
    $("#enddate2").attr("max",date.toJSON().substr(0,10)+"T23:59:59");

    /*document.getElementById("startdate").value=s1+"T00:00:00";
    document.getElementById("enddate").value=date.toJSON().substr(0,10)+"T00:00:00";*/
    $("#startdate").attr("max",date.toJSON().substr(0,10)+"T23:59:59");
    $("#enddate").attr("max",date.toJSON().substr(0,10)+"T23:59:59");


    var startdate2=$("#startdate2").val();
    var enddate2=$("#enddate2").val();
    function getnum(){
        $.post({
            url:'sign!getyichubaogao.action',
            data:{"startdate":startdate2,"enddate":enddate2},
            success:function (data) {
                $("#num").text("总数："+data);

            }
        })
    }
    getnum();
</script>
</html>
